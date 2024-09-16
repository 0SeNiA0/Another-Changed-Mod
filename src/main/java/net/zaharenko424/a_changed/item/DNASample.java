package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.zaharenko424.a_changed.DNAType;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DNASample extends Item {

    public DNASample() {
        super(new Properties().stacksTo(16).rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack sample, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(sample, context, tooltipComponents, tooltipFlag);
        if(sample.has(ComponentRegistry.DNA_TYPE)){
            tooltipComponents.add(Component.translatable("dna." + sample.get(ComponentRegistry.DNA_TYPE).toLanguageKey()).withStyle(ChatFormatting.AQUA));
        } else tooltipComponents.add(Component.literal("Invalid dna!"));
    }

    public static @NotNull ItemStack encodeDNA(@NotNull DNAType dnaType){
        ItemStack sample = new ItemStack(ItemRegistry.DNA_SAMPLE.get());
        sample.set(ComponentRegistry.DNA_TYPE, dnaType.location());
        return sample;
    }

    public static @Nullable ResourceLocation decodeDNA(@NotNull ItemStack sample){
        if(!(sample.getItem() instanceof DNASample)) throw new IllegalArgumentException("sample must be an instance of DNASample!");
        if(!sample.has(ComponentRegistry.DNA_TYPE)) return null;
        return sample.get(ComponentRegistry.DNA_TYPE);
    }
}