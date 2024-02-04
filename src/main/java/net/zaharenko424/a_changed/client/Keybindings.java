package net.zaharenko424.a_changed.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Keybindings {

    private static final String CATEGORY = name(".keyCategory");

    public static final KeyMapping GRAB_KEY = new KeyMapping(name(".grab"), KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, InputConstants.KEY_G, CATEGORY);
    public static final KeyMapping GRAB_MODE_KEY = new KeyMapping(name(".grab_mode"), KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, InputConstants.KEY_M, CATEGORY);

    @Contract(pure = true)
    private static @NotNull String name(String str){
        return "key." + AChanged.MODID + str;
    }
}