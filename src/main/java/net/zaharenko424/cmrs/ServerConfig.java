package net.zaharenko424.cmrs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {

    public static final ModConfigSpec.IntValue MAX_PROPERTIES;

    static final ModConfigSpec SERVER_SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        MAX_PROPERTIES = builder
                .comment("Tells the mod how many model properties each player is allowed to have.",
                        "Any properties above the specified amount will be ignored and not synced to server/other players.",
                        "There are no guarantees as to which properties will be ignored.")
                .defineInRange("max_model_properties", 10, 2, 128);

        SERVER_SPEC = builder.build();
    }
}
