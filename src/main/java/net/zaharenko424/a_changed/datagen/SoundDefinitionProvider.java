package net.zaharenko424.a_changed.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.registry.SoundRegistry.*;

@ParametersAreNonnullByDefault
public class SoundDefinitionProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    public SoundDefinitionProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, AChanged.MODID, helper);
    }

    @Override
    public void registerSounds() {
        add(BUTTON_PRESSED, definition()
                .subtitle(subtitle("button_pressed"))
                .with(sound(AChanged.resourceLoc("decision1"))));
        addSimpleSound(COMPRESSOR);
        addSimpleSound(DOOR_CLOSE);
        addSimpleSound(DOOR_LOCKED);
        addSimpleSound(DOOR_OPEN);
        addSimpleSound(GAS_LEAK);
        addSimpleSound(KEYPAD_UNLOCKED);
        addSimpleSound(KEYPAD_WRONG_PASSWORD);
        addSimpleSound(LASER);
        addSimpleSound(PNEUMATIC_RIFLE);
        addSimpleSound(PUSH);
        addSimpleSound(SAVE);
        add(SMART_SEWAGE_CONSUME, definition()
                .subtitle(subtitle("smart_sewage_absorb"))
                .with(sound(AChanged.resourceLoc("water1"))));
        addSimpleSound(SPACE_DOOR_CLOSE);
        addSimpleSound(SPACE_DOOR_OPEN);
        addSimpleSound(TRANSFUR);
        addSimpleSound(TRANSFUR_1);
    }

    private void addSimpleSound(DeferredHolder<SoundEvent,SoundEvent> sound){
        ResourceLocation id=sound.getId();
        add(sound, definition()
                .subtitle(subtitle(id.getPath()))
                .with(sound(id)));
    }

    @Contract(pure = true)
    private @NotNull String subtitle(String str){
        return "sounds."+ AChanged.MODID+"."+str;
    }
}