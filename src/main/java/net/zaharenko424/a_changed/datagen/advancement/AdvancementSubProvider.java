package net.zaharenko424.a_changed.datagen.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.a_changed.AChangedTags;
import net.zaharenko424.a_changed.criterion.ShotWithSyringeTrigger;
import net.zaharenko424.a_changed.criterion.SteppedOnSyringeTrigger;
import net.zaharenko424.a_changed.criterion.TransfurTrigger;
import net.zaharenko424.a_changed.criterion.TransfurTypePredicate;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static net.zaharenko424.a_changed.AChanged.resourceLoc;

public class AdvancementSubProvider implements AdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.@NotNull Provider lookup, @NotNull Consumer<AdvancementHolder> saver, @NotNull ExistingFileHelper existingFileHelper) {
        AdvancementHolder root = Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.LATEX_SYRINGE,
                        Component.translatable("advancements.a_changed.root.title"),
                        Component.translatable("advancements.a_changed.root.description"),
                        resourceLoc("textures/gui/advancements/background.png"),
                        AdvancementType.TASK,
                        false, false, false
                )
                .addCriterion("instant_trigger", PlayerTrigger.TriggerInstance.tick())
                .save(saver, resourceLoc("root"), existingFileHelper);


        AdvancementHolder obtainOrange = Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.ORANGE_ITEM,
                        Component.translatable("advancements.a_changed.orange.title"),
                        Component.translatable("advancements.a_changed.orange.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(root)
                .addCriterion("obtain_orange", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.ORANGE_ITEM))
                .save(saver, resourceLoc("obtain_orange"), existingFileHelper);
        AdvancementHolder obtainOrangeJuice = Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.ORANGE_JUICE_ITEM,
                        Component.translatable("advancements.a_changed.orange_juice.title"),
                        Component.translatable("advancements.a_changed.orange_juice.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .parent(obtainOrange)
                .addCriterion("obtain_orange_juice", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.ORANGE_JUICE_ITEM))
                .save(saver, resourceLoc("obtain_orange_juice"), existingFileHelper);
        Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.CANNED_ORANGES_ITEM,
                        Component.translatable("advancements.a_changed.canned_oranges.title"),
                        Component.translatable("advancements.a_changed.canned_oranges.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, true
                )
                .parent(obtainOrangeJuice)
                .addCriterion("obtain_canned_oranges", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.CANNED_ORANGES_ITEM))
                .rewards(AdvancementRewards.Builder.experience(100))
                .save(saver, resourceLoc("obtain_canned_oranges"), existingFileHelper);


        AdvancementHolder stepOnSyringe = Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.SYRINGE_ITEM,
                        Component.translatable("advancements.a_changed.step_on_syringe.title"),
                        Component.translatable("advancements.a_changed.step_on_syringe.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, true
                )
                .parent(root)
                .addCriterion("step_on_syringe", SteppedOnSyringeTrigger.TriggerInstance.playerSteppedOnSyringe())
                .save(saver, resourceLoc("step_on_syringe"), existingFileHelper);
        Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.SYRINGE_ITEM,
                        Component.translatable("advancements.a_changed.step_on_all_syringes.title"),
                        Component.translatable("advancements.a_changed.step_on_all_syringes.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .parent(stepOnSyringe)
                .addCriterion("step_on_adrenaline_syringe", SteppedOnSyringeTrigger.TriggerInstance.playerSteppedOnSyringe(ItemPredicate.Builder.item().of(ItemRegistry.ADRENALINE_SYRINGE).build()))
                .addCriterion("step_on_syringe", SteppedOnSyringeTrigger.TriggerInstance.playerSteppedOnSyringe(ItemPredicate.Builder.item().of(ItemRegistry.SYRINGE_ITEM).build()))
                .addCriterion("step_on_latex_syringe", SteppedOnSyringeTrigger.TriggerInstance.playerSteppedOnSyringe(ItemPredicate.Builder.item().of(ItemRegistry.LATEX_SYRINGE).build()))
                .addCriterion("step_on_st_latex_syringe", SteppedOnSyringeTrigger.TriggerInstance.playerSteppedOnSyringe(ItemPredicate.Builder.item().of(ItemRegistry.STABILIZED_LATEX_SYRINGE).build()))
                .addCriterion("step_on_dl_untransfur_syringe", SteppedOnSyringeTrigger.TriggerInstance.playerSteppedOnSyringe(ItemPredicate.Builder.item().of(ItemRegistry.DARK_LATEX_UNTRANSFUR_SYRINGE).build()))
                .addCriterion("step_on_wl_untransfur_syringe", SteppedOnSyringeTrigger.TriggerInstance.playerSteppedOnSyringe(ItemPredicate.Builder.item().of(ItemRegistry.WHITE_LATEX_UNTRANSFUR_SYRINGE).build()))
                .addCriterion("step_on_untransfur_syringe", SteppedOnSyringeTrigger.TriggerInstance.playerSteppedOnSyringe(ItemPredicate.Builder.item().of(ItemRegistry.UNIVERSAL_UNTRANSFUR_SYRINGE).build()))
                .addCriterion("step_on_solvent_syringe", SteppedOnSyringeTrigger.TriggerInstance.playerSteppedOnSyringe(ItemPredicate.Builder.item().of(ItemRegistry.LATEX_SOLVENT_SYRINGE).build()))
                .rewards(AdvancementRewards.Builder.experience(200))
                .save(saver, resourceLoc("step_on_all_syringes"), existingFileHelper);


        AdvancementHolder getTransfurred = Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.LATEX_SYRINGE,
                        Component.translatable("advancements.a_changed.get_transfurred.title"),
                        Component.translatable("advancements.a_changed.get_transfurred.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(root)
                .addCriterion("get_transfurred", TransfurTrigger.TriggerInstance.playerTransfurred())
                .save(saver, resourceLoc("get_transfurred"), existingFileHelper);
        Advancement.Builder.recipeAdvancement()
                .display(
                        Items.SALMON,
                        Component.translatable("advancements.a_changed.cat_transfur.title"),
                        Component.translatable("advancements.a_changed.cat_transfur.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(getTransfurred)
                .addCriterion("cat_transfur", TransfurTrigger.TriggerInstance.playerTransfurred(TransfurTypePredicate.of(TransfurTypePredicate.Type.CAT)))
                .save(saver, resourceLoc("cat_transfur"), existingFileHelper);
        Advancement.Builder.recipeAdvancement()
                .display(
                        Items.WATER_BUCKET,
                        Component.translatable("advancements.a_changed.swimming_transfur.title"),
                        Component.translatable("advancements.a_changed.swimming_transfur.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(getTransfurred)
                .addCriterion("swimming_transfur", TransfurTrigger.TriggerInstance.playerTransfurred(TransfurTypePredicate.of(TransfurTypePredicate.Type.SWIMMING)))
                .save(saver, resourceLoc("swimming_transfur"), existingFileHelper);
        Advancement.Builder.recipeAdvancement()
                .display(
                        Items.ELYTRA,
                        Component.translatable("advancements.a_changed.flying_transfur.title"),
                        Component.translatable("advancements.a_changed.flying_transfur.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(getTransfurred)
                .addCriterion("flying_transfur", TransfurTrigger.TriggerInstance.playerTransfurred(TransfurTypePredicate.of(TransfurTypePredicate.Type.FLYING)))
                .save(saver, resourceLoc("flying_transfur"), existingFileHelper);
        Advancement.Builder allTransfurs = Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.LATEX_SYRINGE,
                        Component.translatable("advancements.a_changed.all_transfurs.title"),
                        Component.translatable("advancements.a_changed.all_transfurs.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .parent(getTransfurred);
        TransfurRegistry.TRANSFUR_TYPES.getEntries().forEach(type -> {
            if(type == TransfurRegistry.SPECIAL_TF) return;
            allTransfurs.addCriterion(type.getId().toString(), TransfurTrigger.TriggerInstance.playerTransfurred(TransfurTypePredicate.of(type.get())));
        });
        allTransfurs
                .rewards(AdvancementRewards.Builder.experience(300))
                .save(saver, resourceLoc("all_transfurs"), existingFileHelper);


        AdvancementHolder rangedTransfur = Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.PNEUMATIC_SYRINGE_RIFLE,
                        Component.translatable("advancements.a_changed.ranged_transfur.title"),
                        Component.translatable("advancements.a_changed.ranged_transfur.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(root)
                .addCriterion("use_pneumatic_syringe_rifle", ShotCrossbowTrigger.TriggerInstance.shotCrossbow(ItemRegistry.PNEUMATIC_SYRINGE_RIFLE))
                .save(saver, resourceLoc("use_pneumatic_syringe_rifle"), existingFileHelper);
        Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.SYRINGE_COIL_GUN,
                        Component.translatable("advancements.a_changed.ranged_transfur1.title"),
                        Component.translatable("advancements.a_changed.ranged_transfur1.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .parent(rangedTransfur)
                .addCriterion("hit_entity", ShotWithSyringeTrigger.TriggerInstance
                        .playerShotEntityWithSyringe(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(AChangedTags.Entity.TRANSFURRABLE_TAG)
                                .distance(DistancePredicate.absolute(MinMaxBounds.Doubles.atLeast(64))).build())))
                .save(saver, resourceLoc("hit_entity_syringe_coilgun"), existingFileHelper);


        Advancement.Builder.recipeAdvancement()
                .display(
                        ItemRegistry.SYRINGE_ITEM,
                        Component.translatable("advancements.a_changed.armor_or_luck.title"),
                        Component.translatable("advancements.a_changed.armor_or_luck.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, true
                )
                .parent(root)
                .addCriterion("syringe_bounced", ShotWithSyringeTrigger.TriggerInstance.syringeBouncedOffPlayer())
                .save(saver, resourceLoc("armor_or_luck"), existingFileHelper);
    }
}