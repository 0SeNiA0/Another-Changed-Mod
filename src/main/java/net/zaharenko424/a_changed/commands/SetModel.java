package net.zaharenko424.a_changed.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.CustomModelManager;
import net.zaharenko424.a_changed.client.cmrs.model.URLLoadedModel;
import org.jetbrains.annotations.NotNull;

public class SetModel {

    private static final SuggestionProvider<CommandSourceStack> suggestionsReg = SuggestionProviders.register(
            AChanged.resourceLoc("custom_models"),
            (context, builder) -> SharedSuggestionProvider.suggestResource(CustomModelManager.getInstance().getRegisteredModels(), builder)
    );

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("set_model")
                .then(
                        Commands.literal("registered")
                        .then(
                                Commands.argument("modelId", ResourceLocationArgument.id())
                                        .suggests(suggestionsReg)
                                        .executes(
                                                context -> execute(ResourceLocationArgument.getId(context, "modelId"), Minecraft.getInstance().player, 0)
                                        )
                                        .then(
                                                Commands.argument("priority", IntegerArgumentType.integer())
                                                        .executes(
                                                                context -> execute(ResourceLocationArgument.getId(context, "modelId"), Minecraft.getInstance().player, IntegerArgumentType.getInteger(context, "priority"))
                                                        )
                                        )
                        )
                )
                .then(
                        Commands.literal("url_loaded")
                        .then(
                                Commands.argument("url", StringArgumentType.string())
                                        .executes(
                                                context -> execute(StringArgumentType.getString(context, "url"), Minecraft.getInstance().player, 0)
                                        )
                                        .then(
                                                Commands.argument("priority", IntegerArgumentType.integer())
                                                        .executes(
                                                                context -> execute(StringArgumentType.getString(context, "url"), Minecraft.getInstance().player, IntegerArgumentType.getInteger(context, "priority"))
                                                        )
                                        )
                        )
                )

        );
    }

    private static int execute(@NotNull ResourceLocation modelId, @NotNull AbstractClientPlayer player, int priority){
        CustomModelManager.getInstance().setPlayerModel(player, modelId, null, priority);
        return Command.SINGLE_SUCCESS;
    }

    private static int execute(@NotNull String url, @NotNull AbstractClientPlayer player, int priority){
        player.displayClientMessage(Component.literal("Attempting to load model for ").append(player.getName()), true);
        CustomModelManager.getInstance().loadModel(url, URLLoadedModel::new).whenComplete((id, err) -> {
            if(id == null){
                player.displayClientMessage(Component.literal("Error occurred while loading model!").withStyle(ChatFormatting.DARK_RED), true);
                return;
            }
            player.displayClientMessage(Component.literal("Model loaded successfully!").withStyle(ChatFormatting.AQUA), true);
            CustomModelManager.getInstance().setPlayerModel(player, id, null, priority);
        });
        return Command.SINGLE_SUCCESS;
    }
}