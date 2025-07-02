package net.zaharenko424.cmrs.client.property;

import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.client.ClientHooks;
import net.zaharenko424.cmrs.api.CustomModel;
import net.zaharenko424.cmrs.api.ModelLayer;
import net.zaharenko424.cmrs.client.model.RenderStack;
import net.zaharenko424.cmrs.client.model.Texture;
import net.zaharenko424.cmrs.client.renderer.ExtraRenderTypes;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;
import net.zaharenko424.cmrs.util.TransparencyType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public final class Armor implements ModelLayer {

    public static final StreamCodec<FriendlyByteBuf, Armor> CODEC = StreamCodec.of((buffer, dynArmor) -> {
        buffer.writeMap(dynArmor.idToType, ByteBufCodecs.VAR_INT, FriendlyByteBuf::writeEnum);
        buffer.writeMap(dynArmor.idToGlow, ByteBufCodecs.VAR_INT, FriendlyByteBuf::writeEnum);
    }, buffer -> new Armor(
            buffer.readMap(Int2ObjectArrayMap::new, ByteBufCodecs.VAR_INT, buf -> buf.readEnum(ArmorItem.Type.class)),
            buffer.readMap(Int2ObjectArrayMap::new, ByteBufCodecs.VAR_INT, buf -> buf.readEnum(ArmorItem.Type.class)))
    );

    private final Int2ObjectArrayMap<ArmorItem.Type> idToType;//TODO combine maps
    private final Int2ObjectArrayMap<ArmorItem.Type> idToGlow;
    private final IntSet renderIds;//don't save

    public Armor(@NotNull Int2ObjectArrayMap<ArmorItem.Type> types, @NotNull Int2ObjectArrayMap<ArmorItem.Type> glow){
        this.idToType = types;
        this.idToGlow = glow;
        renderIds = new IntArraySet();
        renderIds.addAll(idToType.keySet());
        idToGlow.keySet().forEach(i -> {if(!renderIds.add(i)) throw new IllegalArgumentException("Repeated renderId in armor!");});
    }

    @Override
    public IntSet renderIds() {
        return renderIds;
    }

    @Override
    public void verifyTextures(List<Texture> textures) {}

    @Override
    public boolean shouldRenderInFirstPerson() {
        return false;
    }

    @Override
    public void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, MultiBufferSource source) {
        if(entity.isSpectator()) return;

        ArmorItem.Type type;
        EquipmentSlot slot;
        ItemStack itemStack;
        RenderStack.ParameterList list;
        for(Int2ObjectMap.Entry<ArmorItem.Type> entry : idToType.int2ObjectEntrySet()){
            type = entry.getValue();
            slot = type.getSlot();
            itemStack = entity.getItemBySlot(slot);
            if(!(itemStack.getItem() instanceof ArmorItem armor) || entity.getEquipmentSlotForItem(itemStack) != slot) continue;
            list = stack.getOrCreate(entry.getIntKey());
            if(armor instanceof AnimalArmorItem animalArmor){
                setupAnimalArmor(list, source, itemStack, animalArmor);
            } else setupArmorPiece(entity, list, source, itemStack, armor, slot);
        }

        for(Int2ObjectMap.Entry<ArmorItem.Type> entry : idToGlow.int2ObjectEntrySet()){//Do the pass twice because glow buffer has to be created after everything else
            slot = entry.getValue().getSlot();                                         //Can be moved to setupArmor with getAndRefreshPooledBuffer but isn't used currently anyway
            itemStack = entity.getItemBySlot(slot);
            if(!(itemStack.getItem() instanceof ArmorItem armor) || entity.getEquipmentSlotForItem(itemStack) != slot) continue;
            if(armor instanceof AnimalArmorItem animalArmor){
                stack.getOrCreate(entry.getIntKey())
                    .add(source.getBuffer(ExtraRenderTypes.GLOW_SOLID.apply(animalArmor.getTexture()), TransparencyType.OPAQUE_DECAL))
                    .texture(64, animalArmor.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN ? 64 : 32);
            } else stack.getOrCreate(entry.getIntKey())//wrap here too
                    .add(source.getBuffer(ExtraRenderTypes.GLOW_SOLID.apply(ClientHooks.getArmorTexture(entity, itemStack, armor.getMaterial().value().layers().getFirst(), slot == EquipmentSlot.LEGS, slot)), TransparencyType.OPAQUE_DECAL))
                    .texture(64, 32);
        }
    }

    void setupArmorPiece(LivingEntity entity, RenderStack.ParameterList list, MultiBufferSource source, ItemStack itemStack, ArmorItem item, EquipmentSlot slot){
        boolean innerModel = slot == EquipmentSlot.LEGS;
        ArmorMaterial material = item.getMaterial().value();

        int color = itemStack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(itemStack, -6265536)) : -1;
        ResourceLocation texture;
        for (int layerIdx = 0; layerIdx < material.layers().size(); layerIdx++) {
            ArmorMaterial.Layer armormaterial$layer = material.layers().get(layerIdx);
            int j = armormaterial$layer.dyeable() ? color : -1;
            if(j == 0) continue;
            texture = ClientHooks.getArmorTexture(entity, itemStack, armormaterial$layer, innerModel, slot);

            list.add(source.getBuffer(RenderType.armorCutoutNoCull(texture), TransparencyType.OPAQUE_DECAL))
                    .texture(64, 32)// <-- wrap consumer here
                    .color(j).overlay(OverlayTexture.NO_OVERLAY);
        }

        ArmorTrim trim = itemStack.get(DataComponents.TRIM);
        if(trim != null){
            Holder<ArmorMaterial> holder = item.getMaterial();
            TextureAtlasSprite textureatlassprite = spriteGetter().apply(innerModel ? trim.innerTexture(holder) : trim.outerTexture(holder));
            VertexConsumer vertexconsumer = textureatlassprite.wrap(source.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal()), TransparencyType.OPAQUE_DECAL));
            list.add(vertexconsumer)
                    .texture(64, 32)//wrap consumer?
                    .overlay(OverlayTexture.NO_OVERLAY);
        }

        if(itemStack.hasFoil()) {
            list.add(source.getBuffer(RenderType.armorEntityGlint(), TransparencyType.DECAL))
                    .texture(64, 32)
                    .overlay(OverlayTexture.NO_OVERLAY);
        }
    }

    static Function<ResourceLocation, TextureAtlasSprite> func;
    static Function<ResourceLocation, TextureAtlasSprite> spriteGetter(){
        if(func == null) func = Minecraft.getInstance().getTextureAtlas(Sheets.ARMOR_TRIMS_SHEET);
        return func;
    }

    void setupAnimalArmor(RenderStack.ParameterList list, MultiBufferSource source, ItemStack itemStack, AnimalArmorItem item){
        if(item.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN){//Horse
            int color = itemStack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(itemStack, -6265536)) : -1;
            list.add(source.getBuffer(RenderType.entityCutoutNoCull(item.getTexture()), TransparencyType.OPAQUE_DECAL))
                    .texture(64, 64)
                    .overlay(OverlayTexture.NO_OVERLAY)
                    .color(color);
            return;
        }
        //Wolf
        list.add(source.getBuffer(RenderType.entityCutoutNoCull(item.getTexture()), TransparencyType.OPAQUE_DECAL))
                .texture(64, 32)//wrap consumer
                .overlay(OverlayTexture.NO_OVERLAY);

        if (!itemStack.is(ItemTags.DYEABLE)) return;//TODO add cracks?
        int color = DyedItemColor.getOrDefault(itemStack, 0);
        if (FastColor.ARGB32.alpha(color) == 0) return;
        ResourceLocation overlay = item.getOverlayTexture();
        if (overlay == null) return;
        list.add(source.getBuffer(RenderType.armorCutoutNoCull(overlay), TransparencyType.OPAQUE_DECAL))
                .texture(64, 32)//wrap
                .color(color).overlay(OverlayTexture.NO_OVERLAY);
    }
}