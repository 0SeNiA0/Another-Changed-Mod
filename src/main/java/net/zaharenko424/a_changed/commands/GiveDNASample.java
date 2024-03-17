package net.zaharenko424.a_changed.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.item.DNASample;
import net.zaharenko424.a_changed.DNAType;
import net.zaharenko424.a_changed.registry.DNATypeRegistry;
import org.jetbrains.annotations.NotNull;

public class GiveDNASample {

    private static final SuggestionProvider<CommandSourceStack> suggestions = SuggestionProviders.register(
            AChanged.resourceLoc("dna_types"),
            (context, builder) -> SharedSuggestionProvider.suggestResource(DNATypeRegistry.DNA_TYPE_REGISTRY.keySet().stream(), builder)
    );

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("dna")
                        .requires(CommandSourceStack::isPlayer)
                        .then(
                                Commands.argument("dnaType", ResourceLocationArgument.id())
                                        .suggests(suggestions)
                                        .executes(
                                                context -> execute(context.getSource().getPlayer(), ResourceLocationArgument.getId(context, "dnaType"), 1)
                                        )
                                        .then(
                                                Commands.argument("amount", IntegerArgumentType.integer(1, 16))
                                                        .executes(
                                                                context -> execute(context.getSource().getPlayer(), ResourceLocationArgument.getId(context, "dnaType"), IntegerArgumentType.getInteger(context, "amount"))
                                                        )
                                        )
                        )
        );
    }

    private static int execute(ServerPlayer player, ResourceLocation dnaType, int amount){
        DNAType type = DNATypeRegistry.DNA_TYPE_REGISTRY.get(dnaType);
        if(type == null) return 0;
        ItemStack stack = DNASample.encodeDNA(type);
        if(amount > 1) stack.setCount(amount);
        return player.addItem(stack) ? Command.SINGLE_SUCCESS : 0;
    }
}