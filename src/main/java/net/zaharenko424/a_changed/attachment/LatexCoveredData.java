package net.zaharenko424.a_changed.attachment;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChangedTags;
import net.zaharenko424.a_changed.block.LatexBlock;
import net.zaharenko424.a_changed.block.LatexImmuneBlock;
import net.zaharenko424.a_changed.network.ClientPacketHandler;
import net.zaharenko424.a_changed.network.packets.ClientboundLTCDataPacket;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.transfurSystem.CoveredWith;
import net.zaharenko424.cmrs.util.StreamCodecUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class LatexCoveredData {

    private static final byte CLEAR_SYNC = -1;
    private static final byte FULL_SYNC = 0;
    private static final byte DIFF_SYNC = 1;

    public static @NotNull LatexCoveredData of(@NotNull LevelChunk chunk){
        return chunk.getData(AttachmentRegistry.LATEX_COVERED);
    }

    public static boolean isLatex(BlockState state){
        return state.getBlock() instanceof LatexBlock;
    }

    /**
     * @return true if state cannot be covered with latex.
     */
    public static boolean isLatexImmune(@NotNull BlockState state){
        Block block = state.getBlock();
        return state.isEmpty() || block instanceof LiquidBlock || block instanceof LatexImmuneBlock
                || state.is(AChangedTags.Block.LATEX_RESISTANT) || state.getRenderShape() != RenderShape.MODEL;
    }

    private final LevelChunk holder;
    private Int2ObjectMap<Short2ObjectMap<CoveredWith>> sections; //Can be improved by using palette when there are > 512 blocks in a section -- Int2ObjectMap<Either<Short2ObjectMap<CoveredWith>, PalettedContainer<CoveredWith>>> sections;
    private HashSet<SectionPos> sectionsToUpdate;

    public LatexCoveredData(@NotNull IAttachmentHolder holder){
        if(!(holder instanceof LevelChunk chunk)) throw new IllegalArgumentException();
        this.holder = chunk;
    }

    protected Short2ObjectMap<CoveredWith> getSection(BlockPos pos){
        return sections == null ? null : sections.get(pos.getY() >> 4);
    }

    protected Short2ObjectMap<CoveredWith> getOrCreateSection(int sectionY){
        if(sections == null) sections = new Int2ObjectOpenHashMap<>();
        return sections.computeIfAbsent(sectionY, y -> new Short2ObjectOpenHashMap<>());
    }

    protected Short2ObjectMap<CoveredWith> getOrCreateSection(BlockPos pos){
        return getOrCreateSection(pos.getY() >> 4);
    }

    protected boolean removeCover(BlockPos pos){
        Short2ObjectMap<CoveredWith> section = getSection(pos);
        if(section == null) return false;

        CoveredWith cover = section.remove(toChunkRelative(pos));
        if(section.isEmpty()) sections.remove(pos.getY() >> 4);
        return cover != null;
    }

    public boolean isEmpty(){
        return sections == null || sections.isEmpty();
    }

    /**
     * Latex blocks return CoveredWith.NOTHING
     * @return CoveredWith.NOTHING if no data is present.
     */
    public CoveredWith getCoveredWith(@NotNull BlockPos pos){
        Short2ObjectMap<CoveredWith> section = getSection(pos);
        if(section == null) return CoveredWith.NOTHING;
        return section.getOrDefault(toChunkRelative(pos), CoveredWith.NOTHING);
    }

    public void coverWith(@NotNull BlockPos pos, @NotNull CoveredWith coverWith){
        if(holder.getLevel().isClientSide || !verifyPos(pos)) return;
        BlockState state = holder.getBlockState(pos);
        if((isLatex(state) || isLatexImmune(state)) && coverWith != CoveredWith.NOTHING) return;

        if(coverWith == CoveredWith.NOTHING){
            if(removeCover(pos)) {
                holder.setUnsaved(true);
                PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) holder.getLevel(), holder.getPos(), getPacket(pos, CoveredWith.NOTHING));
            }

            return;
        }

        getOrCreateSection(pos).put(toChunkRelative(pos), coverWith);
        holder.setUnsaved(true);

        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) holder.getLevel(), holder.getPos(), getPacket(pos, coverWith));
    }

    public static short toChunkRelative(BlockPos pos){
        return (short) (((pos.getX() & 0xF) << 12) | ((pos.getY() & 0xF) << 8) | ((pos.getZ() & 0xF) << 4));
    }

    public static BlockPos toGlobal(short chunkRelative, int section, ChunkPos chunkPos){
        return chunkPos.getBlockAt(chunkRelative >> 12 & 0xF, (section << 4) | ((chunkRelative >> 8) & 0xF), (chunkRelative >> 4) & 0xF);
    }

    private boolean verifyPos(@NotNull BlockPos pos){
        int y = pos.getY();
        if(y > 2047 || y < -2048) return false;

        ChunkPos chPos = holder.getPos();
        return pos.getX() >> 4 == chPos.x
                && pos.getZ() >> 4 == chPos.z;
    }

    public void readPacket(byte flags, FriendlyByteBuf buf) {
        if(!holder.getLevel().isClientSide) return;

        if(flags == CLEAR_SYNC) {
            if(sections != null) sections.clear();
            return;
        }

        if(flags == DIFF_SYNC){
            BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            CoveredWith cover = buf.readEnum(CoveredWith.class);
            if(cover == CoveredWith.NOTHING) {
                removeCover(pos);
            } else getOrCreateSection(pos).put(toChunkRelative(pos), cover);

            addSectionToUpdate(SectionPos.of(pos));
            updateSections();
            return;
        }

        if(flags == FULL_SYNC && sections != null) sections.clear();

        read(buf);
    }

    public CustomPacketPayload getUpdatePacket(){
        if(holder.getLevel().isClientSide) return null;
        return getPacket(null, null);
    }

    private CustomPacketPayload getPacket(@Nullable BlockPos pos, CoveredWith cover){
        if(isEmpty()) return new ClientboundLTCDataPacket(holder.getPos(), CLEAR_SYNC, new byte[0]);

        if(pos != null) return new ClientboundLTCDataPacket(holder.getPos(), DIFF_SYNC, StreamCodecUtils.writeCustomData(buf ->
                buf.writeInt(pos.getX()).writeInt(pos.getY()).writeInt(pos.getZ()).writeEnum(cover)));

        return new ClientboundLTCDataPacket(holder.getPos(), FULL_SYNC, StreamCodecUtils.writeCustomData(this::write));
    }

    private void read(FriendlyByteBuf buf){
        int sectionCount = buf.readVarInt();
        short sectionIndex;
        Short2ObjectMap<CoveredWith> section;
        CoveredWith cover;
        int types, positions, posPairs;
        short first;
        byte second;
        for(int i = 0; i < sectionCount; i++){
            sectionIndex = buf.readShort();
            section = getOrCreateSection(sectionIndex);
            addSectionToUpdate(SectionPos.of(holder.getPos(), sectionIndex));

            types = buf.readVarInt();

            for(int type = 0; type < types; type++) {
                cover = buf.readEnum(CoveredWith.class);
                positions = buf.readVarInt();
                posPairs = positions / 2;

                for (int ii = 0; ii < posPairs; ii++) {//Split pos pairs
                    first = buf.readShort();
                    second = buf.readByte();
                    addOrRemove(section, (short) (first & 0xFFF0), cover);
                    addOrRemove(section, (short) ((first << 12) | ((second & 0xFF) << 4)), cover);
                }

                if(positions % 2 != 0) addOrRemove(section, buf.readShort(), cover);
            }
        }

        sections.int2ObjectEntrySet().removeIf(entry -> entry.getValue().isEmpty());

        updateSections();
    }

    private void addSectionToUpdate(SectionPos pos){
        if(!holder.getLevel().isClientSide) return;
        if(sectionsToUpdate == null) sectionsToUpdate = new HashSet<>();
        sectionsToUpdate.add(pos);
    }

    private void updateSections(){
        if(!holder.getLevel().isClientSide) return;
        ClientPacketHandler.INSTANCE.updateChunkSections(sectionsToUpdate);
        sectionsToUpdate.clear();
    }

    private void addOrRemove(Short2ObjectMap<CoveredWith> section, short pos, CoveredWith cover){
        if(cover == CoveredWith.NOTHING) {
            section.remove(pos);
        } else section.put(pos, cover);
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeVarInt(sections.size());

        Object2ObjectMap<CoveredWith, ShortList> inverse = new Object2ObjectArrayMap<>();
        ShortList positions;
        boolean lastFinished;
        short prev = 0;
        for(Int2ObjectMap.Entry<Short2ObjectMap<CoveredWith>> section : sections.int2ObjectEntrySet()){
            buf.writeShort(section.getIntKey());

            inverse.clear();
            for(Short2ObjectMap.Entry<CoveredWith> entry : section.getValue().short2ObjectEntrySet()) {
                inverse.computeIfAbsent(entry.getValue(), cover -> new ShortArrayList()).add(entry.getShortKey());
            }

            buf.writeVarInt(inverse.size());
            for(Object2ObjectMap.Entry<CoveredWith, ShortList> type : inverse.object2ObjectEntrySet()){
                buf.writeEnum(type.getKey());
                positions = type.getValue();
                buf.writeVarInt(positions.size());

                lastFinished = true;
                for(short pos : positions) {//Merge 1.5 byte positions into 3 byte pairs
                    if(lastFinished){
                        prev = pos;
                        lastFinished = false;
                        continue;
                    }

                    buf.writeByte(prev >> 8);
                    buf.writeByte((prev & 0xF0) | ((pos >> 12) & 0xF));
                    buf.writeByte((pos >> 4) & 0xFF);
                    lastFinished = true;
                }

                if(!lastFinished) buf.writeShort(prev);
            }
        }
    }
//-7 -12 -90     1. (short)((byte)-7 << 8 & 0xFF00) | ((byte)-12 & 0xF0) -1552     2. (short)((((byte)-12 & 0xF) << 12) | (((byte)-90 & 0xFF) << 4)) 19040

    public static class Serializer implements IAttachmentSerializer<Tag, LatexCoveredData> {

        public static final int DATA_VERSION = 0;
        public static final Serializer INSTANCE = new Serializer();

        private Serializer(){}

        @Override
        public @NotNull LatexCoveredData read(@NotNull IAttachmentHolder holder, @NotNull Tag tag, HolderLookup.@NotNull Provider lookup) {
            LatexCoveredData data = new LatexCoveredData(holder);
            if(tag instanceof ByteArrayTag rawData && !rawData.isEmpty()) {
                data.read_(rawData.getAsByteArray());
                return data;
            }

            if(tag instanceof CompoundTag compound && compound.getInt("version") == DATA_VERSION){//TODO better old data handling
                byte[] rawData = compound.getByteArray("data");
                if(rawData.length != 0) data.read(new FriendlyByteBuf(Unpooled.wrappedBuffer(rawData)));
            }
            return data;
        }

        @Override
        public @Nullable Tag write(@NotNull LatexCoveredData attachment, HolderLookup.@NotNull Provider lookup) {
            if(attachment.isEmpty()) return null;

            CompoundTag tag = new CompoundTag();
            tag.putInt("version", DATA_VERSION);
            tag.putByteArray("data", StreamCodecUtils.writeCustomData(attachment::write));

            return tag;
        }
    }

    //==================================================== Legacy read ===================================================//

    private void read_(byte[] rawData){
        int size = rawData.length / 3;

        CoveredWith[] values = CoveredWith.values();
        ChunkPos pos = holder.getPos();
        int chX = pos.x;
        int chZ = pos.z;

        int j;
        byte xz, by, yState;
        int y;
        CoveredWith coveredWith;
        BlockPos blockPos;
        for(int i = 0; i < size; i++){
            j = i * 3;
            xz = rawData[j];
            by = rawData[j + 1];
            yState = rawData[j + 2];

            y = ((yState & 0x7) << 8) | (by & 0xFF);
            if((yState & 0x8) == 8){
                y = 0xFFFFF800 | y;
            }
            blockPos = new BlockPos((chX << 4) | (xz & 0xF), y, (chZ << 4) | ((xz >> 4) & 0xF));

            coveredWith = values[yState >> 4];

            addOrRemove(getOrCreateSection(blockPos), toChunkRelative(blockPos), coveredWith);

            addSectionToUpdate(SectionPos.of(blockPos));
        }

        sections.int2ObjectEntrySet().removeIf(entry -> entry.getValue().isEmpty());

        updateSections();
    }
}