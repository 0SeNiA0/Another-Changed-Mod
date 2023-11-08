package net.zaharenko424.testmod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.testmod.TransfurManager;
import org.jetbrains.annotations.NotNull;

public class Transfur {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("transfur")
                        .requires(source->source.hasPermission(Commands.LEVEL_ADMINS))
                        .then(
                                Commands.argument("transfurType", ComponentArgument.textComponent())
                                        .executes(
                                                context->context.getSource().isPlayer()? execute(ComponentArgument.getComponent(context,"transfurType"),context.getSource().getPlayer()) :0
                                        )
                                        .then(
                                                Commands.argument("target", EntityArgument.player())
                                                        .executes(context->execute(ComponentArgument.getComponent(context,"transfurType"),EntityArgument.getPlayer(context,"target"))
                                                        )
                                        )
                        )
        );
    }

    private static int execute(@NotNull Component transfurType, @NotNull ServerPlayer player){
        TransfurManager.transfur(player,transfurType.getString());
        return Command.SINGLE_SUCCESS;
    }
}