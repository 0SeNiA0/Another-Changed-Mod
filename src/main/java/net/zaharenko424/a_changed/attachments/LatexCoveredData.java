package net.zaharenko424.a_changed.attachments;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.network.ClientPacketHandler;
import net.zaharenko424.a_changed.network.packets.ClientboundLTCDataPacket;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LatexCoveredData {

    public static final byte CLEAR_SYNC = -1;
    public static final byte FULL_SYNC = 0;
    public static final byte DIFF_SYNC = 1;

    public static @NotNull LatexCoveredData of(@NotNull LevelChunk chunk){
        return chunk.getData(AttachmentRegistry.LATEX_COVERED);
    }

    private final LevelChunk holder;
    private HashMap<BlockPos, CoveredWith> latexCoveredBlocks;
    private HashSet<SectionPos> sectionsToUpdate;

    public LatexCoveredData(@NotNull IAttachmentHolder holder){
        if(!(holder instanceof LevelChunk chunk)) throw new IllegalArgumentException();
        this.holder = chunk;
    }

    public CoveredWith getCoveredWith(@NotNull BlockPos pos){
        if(latexCoveredBlocks == null || !latexCoveredBlocks.containsKey(pos)) return CoveredWith.NOTHING;
        return latexCoveredBlocks.get(pos);
    }

    public void coverWith(@NotNull BlockPos pos, @NotNull CoveredWith coverWith){
        if(holder.getLevel().isClientSide || !verifyPos(pos)) return;
        if(holder.getBlockState(pos).is(AChanged.LATEX_RESISTANT) && coverWith != CoveredWith.NOTHING) return;

        if(coverWith == CoveredWith.NOTHING){
            if(latexCoveredBlocks != null && latexCoveredBlocks.remove(pos) != null)
                PacketDistributor.TRACKING_CHUNK.with(holder).send(getPacket(pos));

            return;
        }

        if(latexCoveredBlocks == null) latexCoveredBlocks = new HashMap<>();
        latexCoveredBlocks.put(pos, coverWith);

        PacketDistributor.TRACKING_CHUNK.with(holder).send(getPacket(pos));
    }

    private boolean verifyPos(@NotNull BlockPos pos){
        int y = pos.getY();
        if(y > 2047 || y < -2048) return false;

        ChunkPos chPos = holder.getPos();
        return pos.getX() >> 4 == chPos.x
                && pos.getZ() >> 4 == chPos.z;
    }

    public void readPacket(byte flags, byte[] rawData) {
        if(!holder.getLevel().isClientSide) return;

        if(flags == CLEAR_SYNC) {
            if(latexCoveredBlocks != null) latexCoveredBlocks.clear();
            return;
        }

        if(flags == FULL_SYNC && latexCoveredBlocks != null) latexCoveredBlocks.clear();

        read_(rawData);
    }

    public ClientboundLTCDataPacket getPacket(@Nullable BlockPos pos){
        if(holder.getLevel().isClientSide) return null;

        if(latexCoveredBlocks == null || latexCoveredBlocks.isEmpty()){
            if(pos == null) return new ClientboundLTCDataPacket(holder.getPos(), CLEAR_SYNC, new byte[0]);
            byte[] data = new byte[3];
            writeBlock(data, 0, pos.getX(), pos.getY(), pos.getZ(), CoveredWith.NOTHING);
            return new ClientboundLTCDataPacket(holder.getPos(), DIFF_SYNC, data);
        }

        if(pos != null){
            byte[] data = new byte[3];
            writeBlock(data, 0, pos.getX(), pos.getY(), pos.getZ(), latexCoveredBlocks.getOrDefault(pos, CoveredWith.NOTHING));
            return new ClientboundLTCDataPacket(holder.getPos(), DIFF_SYNC, data);
        }

        return new ClientboundLTCDataPacket(holder.getPos(), FULL_SYNC, write());
    }

    private void read_(byte[] rawData){
        int size = rawData.length / 3;
        if(latexCoveredBlocks == null) latexCoveredBlocks = new HashMap<>(size);

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

            if(coveredWith == CoveredWith.NOTHING){
                latexCoveredBlocks.remove(blockPos);
            } else latexCoveredBlocks.put(blockPos, coveredWith);

            if(holder.getLevel().isClientSide) {
                if(sectionsToUpdate == null) sectionsToUpdate = new HashSet<>();
                sectionsToUpdate.add(SectionPos.of(blockPos));
            }
        }

        if(holder.getLevel().isClientSide) {
            ClientPacketHandler.INSTANCE.updateChunkSections(sectionsToUpdate);
            sectionsToUpdate.clear();
        }
    }

    private byte[] write() {
        final int size = latexCoveredBlocks.size();
        byte[] rawData = new byte[size * 3];

        int i = -1;
        BlockPos pos;
        for(Map.Entry<BlockPos, CoveredWith> entry : latexCoveredBlocks.entrySet()){
            pos = entry.getKey();
            i++;
            writeBlock(rawData, i * 3, pos.getX(), pos.getY(), pos.getZ(), entry.getValue());
        }

        return rawData;
    }

    private void writeBlock(byte[] rawData, int j, int bx, int by, int bz, CoveredWith coveredWith){
        rawData[j] = (byte) ((bx & 0xF) | ((bz & 0xF) << 4));
        rawData[j + 1] = (byte) (by & 0xFF);
        rawData[j + 2] = (byte) (((by >> 8) & 0xF) | (coveredWith.ordinal() << 4));
        if(by < 0){
            rawData[j + 2] = (byte) (((by >> 28) & 0x8) | rawData[j + 2]);
        }
    }

    public static class Serializer implements IAttachmentSerializer<ByteArrayTag, LatexCoveredData> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer(){}

        @Override
        public @NotNull LatexCoveredData read(@NotNull IAttachmentHolder holder, @NotNull ByteArrayTag tag) {
            LatexCoveredData data = new LatexCoveredData(holder);
            if(!tag.isEmpty()) data.read_(tag.getAsByteArray());
            return data;
        }

        @Override
        public @Nullable ByteArrayTag write(@NotNull LatexCoveredData attachment) {
            if(attachment.latexCoveredBlocks == null || attachment.latexCoveredBlocks.isEmpty()) return null;
            return new ByteArrayTag(attachment.write());
        }
    }
}