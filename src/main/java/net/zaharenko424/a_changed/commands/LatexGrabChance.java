package net.zaharenko424.a_changed.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.attachments.GrabChanceData;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.a_changed.AChanged.LOGGER;

public class LatexGrabChance {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("latex_grab_chance")
                        .then(
                                Commands.literal("get")
                                        .executes(
                                                context->get(context.getSource())
                                        )
                        )
                        .requires(UnTransfur::check)
                        .then(
                                Commands.literal("set")
                                        .then(
                                                Commands.argument("chance", FloatArgumentType.floatArg(0, 1))
                                                        .executes(
                                                                context->set(context.getSource(), FloatArgumentType.getFloat(context,"chance"))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("DEFAULT")
                                                        .executes(
                                                                context->set(context.getSource(), GrabChanceData.DEF_CHANCE)
                                                        )
                                        )
                        )

        );
    }

    private static int get(@NotNull CommandSourceStack source){
        float chance = GrabChanceData.of(source.getLevel()).getGrabChance();
        if(source.isPlayer()) {
            source.sendSystemMessage(Component.translatable("command.transfur_tolerance.get").append(String.valueOf(chance)));
        } else LOGGER.info("Latex grab chance is {}", chance);
        return Command.SINGLE_SUCCESS;
    }

    private static int set(@NotNull CommandSourceStack source , float chance){
        GrabChanceData data = GrabChanceData.of(source.getLevel());

        if(data.getGrabChance() == chance) return Command.SINGLE_SUCCESS;
        data.setGrabChance(chance);

        if(source.isPlayer()) source.sendSystemMessage(Component.translatable("command.transfur_tolerance.set").append(String.valueOf(chance)));
        LOGGER.info("Latex grab chance is set to {}", chance);

        return Command.SINGLE_SUCCESS;
    }
}
