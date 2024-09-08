package net.zaharenko424.a_changed.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.CommandBlock;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import org.jetbrains.annotations.NotNull;

public class UnTransfur {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("untransfur")
                        .requires(UnTransfur::check)
                        .executes(command->command.getSource().isPlayer()?execute(command.getSource().getPlayer()):0)
                        .then(
                                Commands.argument("target", EntityArgument.player())
                                        .executes(command-> execute(EntityArgument.getPlayer(command,"target")))));

    }

    static boolean check(@NotNull CommandSourceStack stack){
        return stack.getLevel().getBlockState(BlockPos.containing(stack.getPosition())).getBlock() instanceof CommandBlock
                || stack.hasPermission(Commands.LEVEL_ADMINS);
    }

    private static int execute(@NotNull ServerPlayer player){
        TransfurHandler.nonNullOf(player).unTransfur(TransfurContext.UNTRANSFUR);
        return Command.SINGLE_SUCCESS;
    }
}