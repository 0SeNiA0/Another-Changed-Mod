package net.zaharenko424.cmrs.client.material;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
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
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.CustomModel;
import net.zaharenko424.cmrs.api.Material;
import net.zaharenko424.cmrs.client.model.RenderStack;
import net.zaharenko424.cmrs.client.model.Texture;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;
import net.zaharenko424.cmrs.registry.MaterialRegistry;
import net.zaharenko424.cmrs.util.TransparencyType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public final class VanillaTexArmor implements Material {

    public static final StreamCodec<FriendlyByteBuf, VanillaTexArmor> CODEC = StreamCodec.of(
            (buffer, armor) -> buffer.writeEnum(armor.armorType),
            buffer -> new VanillaTexArmor(buffer.readEnum(ArmorItem.Type.class)));

    private final ArmorItem.Type armorType;

    public VanillaTexArmor(@NotNull ArmorItem.Type armorType){
        this.armorType = armorType;
    }

    @Override
    public DeferredHolder<MaterialType<?>, MaterialType<VanillaTexArmor>> type() {
        return MaterialRegistry.VANILLA_TEX_ARMOR;
    }

    @Override
    public void verifyTextures(List<Texture> textures) {}

    @Override
    public boolean shouldRenderInFirstPerson() {
        return false;
    }

    @Override
    public void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack.ParameterList parameters, MultiBufferSource source) {
        if(entity.isSpectator()) return;

        EquipmentSlot slot = armorType.getSlot();
        ItemStack stack = entity.getItemBySlot(slot);
        if(!(stack.getItem() instanceof ArmorItem armor) || entity.getEquipmentSlotForItem(stack) != slot) return;

        if(armor instanceof AnimalArmorItem animalArmor){
            setupAnimalArmor(parameters, source, stack, animalArmor);
        } else setupArmorPiece(entity, parameters, source, stack, armor, slot);
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