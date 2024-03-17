package net.zaharenko424.a_changed.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.CustomModelManager;
import org.jetbrains.annotations.NotNull;

public class RemoveModel {

    private static final SuggestionProvider<CommandSourceStack> suggestions = SuggestionProviders.register(
            AChanged.resourceLoc("model_queue"),
            (context, builder) -> SharedSuggestionProvider.suggestResource(CustomModelManager.getInstance().getQueuedModels(Minecraft.getInstance().player), builder)
    );

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("remove_model")
                        .then(
                                Commands.argument("modelId", ResourceLocationArgument.id())
                                        .suggests(suggestions)
                                        .executes(
                                                context -> execute(ResourceLocationArgument.getId(context, "modelId"), Minecraft.getInstance().player)
                                        )
                        )
        );
    }

    private static int execute(ResourceLocation modelId, AbstractClientPlayer player){
        CustomModelManager.getInstance().removePlayerModel(player, modelId);
        return Command.SINGLE_SUCCESS;
    }
}