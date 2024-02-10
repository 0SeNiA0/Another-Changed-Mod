package net.zaharenko424.a_changed.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.DNAType;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DNASample extends Item {

    public DNASample() {
        super(new Properties().stacksTo(16).rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        CompoundTag tag = pStack.getTag();
        if(NBTUtils.hasModTag(tag)) pTooltipComponents.add(Component.translatable("dna."
                + new ResourceLocation(NBTUtils.modTag(tag).getString("dna")).toLanguageKey()));
    }

    public static @NotNull ItemStack encodeDNA(@NotNull DNAType dnaType){
        ItemStack sample = new ItemStack(ItemRegistry.DNA_SAMPLE.get());
        CompoundTag tag = sample.hasTag() ? sample.getTag() : new CompoundTag();
        NBTUtils.modTag(tag).putString("dna", dnaType.toString());
        sample.setTag(tag);
        return sample;
    }
}