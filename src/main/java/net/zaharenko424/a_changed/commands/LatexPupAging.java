package net.zaharenko424.a_changed.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.ability.LatexPupAgingAbility;
import net.zaharenko424.a_changed.attachments.LatexPupAgingData;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.jetbrains.annotations.NotNull;

public class LatexPupAging {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("latex_pup_aging")
                        .requires(UnTransfur::check)
                        .then(
                                Commands.argument("target", EntityArgument.entity())
                                        .then(
                                                Commands.literal("set_age")
                                                        .then(
                                                                Commands.argument("age", IntegerArgumentType.integer(0, LatexPupAgingData.turnAfter))
                                                                        .executes(context ->
                                                                                setAge(context, EntityArgument.getEntity(context, "target"), IntegerArgumentType.getInteger(context, "age")))
                                                        )
                                        ).then(
                                                Commands.literal("freeze")
                                                        .then(
                                                                Commands.argument("freeze", BoolArgumentType.bool())
                                                                        .executes(context -> freeze(context, EntityArgument.getEntity(context, "target"), BoolArgumentType.getBool(context, "freeze")))
                                                        )
                                        ).then(
                                                Commands.literal("get_age")
                                                        .executes(context ->
                                                                getAge(context, EntityArgument.getEntity(context, "target")))
                                        )
                        )
        );
    }

    private static int setAge(CommandContext<CommandSourceStack> context, Entity target, int age){
        if(!(target instanceof LivingEntity entity)) {
            context.getSource().sendFailure(Component.literal("Invalid target"));
            return -1;
        }

        LatexPupAgingAbility ability = getAbility(entity);
        if(ability == null) {
            context.getSource().sendFailure(Component.literal("Invalid target"));
            return -1;
        }

        ability.getAbilityData(entity).setAge(age);
        return Command.SINGLE_SUCCESS;
    }

    private static int freeze(CommandContext<CommandSourceStack> context, Entity target, boolean freeze){
        if(!(target instanceof LivingEntity entity)) {
            context.getSource().sendFailure(Component.literal("Invalid target"));
            return -1;
        }

        LatexPupAgingAbility ability = getAbility(entity);
        if(ability == null) {
            context.getSource().sendFailure(Component.literal("Invalid target"));
            return -1;
        }

        ability.getAbilityData(entity).freezeAging(freeze);
        return Command.SINGLE_SUCCESS;
    }

    private static int getAge(CommandContext<CommandSourceStack> context, Entity target){
        if(!(target instanceof LivingEntity entity)) {
            context.getSource().sendFailure(Component.literal("Invalid target"));
            return -1;
        }

        LatexPupAgingAbility ability = getAbility(entity);
        if(ability == null) {
            context.getSource().sendFailure(Component.literal("Invalid target"));
            return -1;
        }

        context.getSource().sendSuccess(() -> Component.literal("Age: " + ability.getAbilityData(entity).getAge()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static LatexPupAgingAbility getAbility(LivingEntity entity){
        if(AbilityUtils.hasAbility(AbilityRegistry.DL_PUP_AGE, entity)) return AbilityRegistry.DL_PUP_AGE.get();
        return AbilityUtils.hasAbility(AbilityRegistry.WL_PUP_AGE, entity) ? AbilityRegistry.WL_PUP_AGE.get() : null;
    }
}
