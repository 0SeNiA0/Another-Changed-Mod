package net.zaharenko424.a_changed;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    public static final ModConfigSpec.BooleanValue HIDDEN_RELOAD;
    public static final ModConfigSpec.BooleanValue LIGHTLY_COVERED_BLOCKS;
    public static final ModConfigSpec.BooleanValue HIDE_GRAB_ABILITY_WHEN_NON_TF;

    static final ModConfigSpec CLIENT_SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        HIDDEN_RELOAD = builder
                .comment("Tells the mod whether to reload the resources after generating lightly latex covered textures on startup.",
                        "If turned off, the newly generated textures won't be automatically loaded (textures from converted textures folder will be loaded)")
                .define("hidden_reload", true);

        LIGHTLY_COVERED_BLOCKS = builder
                .comment("Tells the mod whether to display lightly latex covered blocks using generated texture or use fully covered one.")
                .define("lightly_covered_blocks", true);

        HIDE_GRAB_ABILITY_WHEN_NON_TF = builder
                .comment("Tells the mod whether to hide the grab ability icon when the player is not transfurred.")
                .define("hide_grab_ability_when_non_tf", false);

        CLIENT_SPEC = builder.build();
    }
}
