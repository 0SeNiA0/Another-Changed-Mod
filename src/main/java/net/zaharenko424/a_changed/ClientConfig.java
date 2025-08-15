package net.zaharenko424.a_changed;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    public static final ModConfigSpec.BooleanValue HIDDEN_RELOAD;
    public static final ModConfigSpec.BooleanValue LIGHTLY_COVERED_BLOCKS;

    static final ModConfigSpec CLIENT_SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        HIDDEN_RELOAD = builder
                .comment("Tells the mod whether to reload the resources after generating lightly latex covered textures on startup.",
                        "If turned off, the newly generated textures wont be automatically loaded (textures from converted textures folder will be loaded)")
                .define("hidden_reload", true);

        LIGHTLY_COVERED_BLOCKS = builder
                .comment("Tells the mod whether to display lightly latex covered blocks using generated texture or use fully covered one.")
                .define("lightly_covered_blocks", true);

        CLIENT_SPEC = builder.build();
    }
}
