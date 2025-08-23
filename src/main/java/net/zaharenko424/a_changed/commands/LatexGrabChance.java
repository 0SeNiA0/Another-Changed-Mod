package net.zaharenko424.a_changed.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.attachment.GrabChanceData;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.a_changed.AChanged.LOGGER;

public class LatexGrabChance {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("latex_grab_chance")
                        .executes(context -> get(context.getSource()))
                        .requires(UnTransfur::check)
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
        );
    }

    private static int get(@NotNull CommandSourceStack source){
        Component comp = Component.translatable("command.a_changed.latex_grab_chance.get", GrabChanceData.of(source.getLevel()).getGrabChance());

        if(source.isPlayer()) {
            source.sendSystemMessage(comp);
        } else LOGGER.info(comp.getString());

        return Command.SINGLE_SUCCESS;
    }

    private static int set(@NotNull CommandSourceStack source , float chance){
        GrabChanceData data = GrabChanceData.of(source.getLevel());

        if(data.getGrabChance() == chance) return Command.SINGLE_SUCCESS;

        data.setGrabChance(chance);
        Component comp = Component.translatable("command.a_changed.latex_grab_chance.set", String.valueOf(chance));

        if(source.isPlayer()) source.sendSystemMessage(comp);
        LOGGER.info(comp.getString());

        return Command.SINGLE_SUCCESS;
    }
}
