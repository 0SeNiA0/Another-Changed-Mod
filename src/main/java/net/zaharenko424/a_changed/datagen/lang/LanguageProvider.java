package net.zaharenko424.a_changed.datagen.lang;

import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.GameRules;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.zaharenko424.a_changed.DNAType;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class LanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider {

    protected final String modid;

    public LanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.modid = modid;
    }

    protected void addAdvancement(String key, String title, String description){
        key = "advancements." + modid + "." + key;
        add(key + ".title", title);
        add(key + ".description", description);
    }

    protected void addAttribute(DeferredHolder<Attribute, Attribute> attribute, String value){
        add(attribute.getId().toLanguageKey("attribute"), value);
    }

    protected void addBlockFromId(DeferredBlock<?> block){
        addBlock(block, Arrays.stream(block.getId().getPath().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" ")));
    }

    protected void addCommand(String key, String value){
        add("command." + modid + "." + key, value);
    }

    protected void addContainer(String key, String value){
        add("container." + modid + "." + key, value);
    }

    protected void addDeathMessage(ResourceKey<DamageType> resourceKey, String generic, @Nullable String item, @Nullable String player){
        String key = "death.attack." + resourceKey.location().getPath();
        add(key, generic);
        if(item != null) add(key + ".item", item);
        if(player != null) add(key + ".player", player);
    }

    protected void addDNA(DeferredHolder<DNAType, DNAType> type, String value){
        assert Objects.equals(modid, type.getId().getNamespace());
        add(type.getId().toLanguageKey("dna"), value);
    }

    protected void addGamerule(GameRules.Key<?> key, String value){
        add("gamerule." + key.getId(), value);
    }

    protected void addKey(KeyMapping key, String value){
        add(key.getName(), value);
    }

    protected void addDItem(DeferredItem<?> item, String value){
        add(item.get(), value);
    }

    protected void addItemFromId(DeferredItem<?> item){
        addDItem(item, Arrays.stream(item.getId().getPath().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" ")));
    }

    protected void addMessage(String key, String value){
        add("message." + modid + "." + key, value);
    }

    protected void addMisc(String key, String value){
        add("misc." + modid + "." + key, value);
    }

    protected void addScreen(String key, String value){
        add("screen." + modid + "." + key, value);
    }

    protected void addSound(DeferredHolder<SoundEvent, SoundEvent> sound, String value){
        add(sound.getId().toLanguageKey("sounds"), value);
    }

    protected void addTooltip(String key, String value){
        add("tooltip." + modid + "." + key, value);
    }

    protected void addTransfur(DeferredHolder<TransfurType<?>, ? extends TransfurType<?>> type){
        add(type.getId().toLanguageKey("transfur"), type.getId().getPath().replace("_", " "));
    }
}
