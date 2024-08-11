package net.zaharenko424.a_changed.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Keybindings {

    private static final String CATEGORY = name(".keyCategory");

    public static final KeyMapping ABILITY_SELECTION = new KeyMapping(name(".ability_selection"), KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, InputConstants.KEY_M, CATEGORY);
    public static final KeyMapping GRAB_KEY = new KeyMapping(name(".grab"), KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, InputConstants.KEY_G, CATEGORY);

    public static final KeyMapping QUICK_SELECT_ABILITY_1 = new KeyMapping(name(".quick_select_ability_1"), KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, InputConstants.KEY_ESCAPE, CATEGORY);
    public static final KeyMapping QUICK_SELECT_ABILITY_2 = new KeyMapping(name(".quick_select_ability_2"), KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, InputConstants.KEY_ESCAPE, CATEGORY);
    public static final KeyMapping QUICK_SELECT_ABILITY_3 = new KeyMapping(name(".quick_select_ability_3"), KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, InputConstants.KEY_ESCAPE, CATEGORY);

    @Contract(pure = true)
    private static @NotNull String name(String str){
        return "key." + AChanged.MODID + str;
    }
}