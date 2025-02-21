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
import net.zaharenko424.a_changed.ability.DLPupAgingAbility;
import net.zaharenko424.a_changed.attachments.DLPupAgingData;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.jetbrains.annotations.NotNull;

public class DLPupAging {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("dl_pup_aging")
                        .requires(UnTransfur::check)
                        .then(
                                Commands.argument("target", EntityArgument.entity())
                                        .then(
                                                Commands.literal("set_age")
                                                        .then(
                                                                Commands.argument("age", IntegerArgumentType.integer(0, DLPupAgingData.turnAfter))
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
        DLPupAgingAbility ability = AbilityRegistry.DL_PUP_AGE.get();
        if(!(target instanceof LivingEntity entity) || !AbilityUtils.hasAbility(ability, entity)) {
            context.getSource().sendFailure(Component.literal("Invalid target"));
            return -1;
        }
        DLPupAgingData data = ability.getAbilityData(entity);
        data.setAge(age);
        return Command.SINGLE_SUCCESS;
    }

    private static int freeze(CommandContext<CommandSourceStack> context, Entity target, boolean freeze){
        DLPupAgingAbility ability = AbilityRegistry.DL_PUP_AGE.get();
        if(!(target instanceof LivingEntity entity) || !AbilityUtils.hasAbility(ability, entity)) {
            context.getSource().sendFailure(Component.literal("Invalid target"));
            return -1;
        }
        ability.getAbilityData(entity).freezeAging(freeze);
        return Command.SINGLE_SUCCESS;
    }

    private static int getAge(CommandContext<CommandSourceStack> context, Entity target){
        DLPupAgingAbility ability = AbilityRegistry.DL_PUP_AGE.get();
        if(!(target instanceof LivingEntity entity) || !AbilityUtils.hasAbility(ability, entity)) {
            context.getSource().sendFailure(Component.literal("Invalid target"));
            return -1;
        }
        context.getSource().sendSuccess(() -> Component.literal("Age: " + ability.getAbilityData(entity).getAge()), false);
        return Command.SINGLE_SUCCESS;
    }
}
