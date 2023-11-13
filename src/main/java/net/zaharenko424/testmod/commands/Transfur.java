package net.zaharenko424.testmod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.entity.Transfurrable;
import org.jetbrains.annotations.NotNull;

public class Transfur {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("transfur")
                        .requires(source->source.hasPermission(Commands.LEVEL_ADMINS))
                        .then(
                                Commands.argument("transfurType", ResourceLocationArgument.id())
                                        .executes(
                                                context->context.getSource().isPlayer()? execute(ResourceLocationArgument.getId(context,"transfurType"),context.getSource().getPlayer()) :0
                                        )
                                        .then(
                                                Commands.argument("target", EntityArgument.player())
                                                        .executes(context->execute(ResourceLocationArgument.getId(context,"transfurType"),EntityArgument.getPlayer(context,"target"))
                                                        )
                                        )
                        )
        );
    }

    private static int execute(@NotNull ResourceLocation transfurType, @NotNull ServerPlayer player){
        TransfurManager.transfur((Transfurrable) player,transfurType);
        return Command.SINGLE_SUCCESS;
    }
}