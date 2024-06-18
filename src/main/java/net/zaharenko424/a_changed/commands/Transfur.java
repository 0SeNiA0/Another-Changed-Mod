package net.zaharenko424.a_changed.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

public class Transfur {

    private static final SuggestionProvider<CommandSourceStack> suggestions = SuggestionProviders.register(
            AChanged.resourceLoc("transfur_types"),
            (context, builder) -> SharedSuggestionProvider.suggestResource(TransfurRegistry.TRANSFUR_REGISTRY.keySet().stream(), builder));

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("transfur")
                        .requires(UnTransfur::check)
                        .then(
                                Commands.argument("transfurType", ResourceLocationArgument.id())
                                        .suggests(suggestions)
                                        .executes(
                                                context -> context.getSource().isPlayer()? execute(ResourceLocationArgument.getId(context,"transfurType"), context.getSource().getPlayer()) :0
                                        )
                                        .then(
                                                Commands.argument("target", EntityArgument.player())
                                                        .executes(
                                                                context -> execute(ResourceLocationArgument.getId(context,"transfurType"), EntityArgument.getPlayer(context,"target"))
                                                        )
                                        )
                        )
        );
    }

    private static int execute(@NotNull ResourceLocation transfurType, @NotNull ServerPlayer player){
        TransfurType transfur = TransfurManager.getTransfurType(transfurType);
        if(transfur == null) return 0;
        TransfurEvent.TRANSFUR_TF.accept(player, transfur);
        return Command.SINGLE_SUCCESS;
    }
}