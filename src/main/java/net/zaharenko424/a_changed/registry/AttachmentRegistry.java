package net.zaharenko424.a_changed.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.attachments.HypnosisData;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.capability.item.ItemEnergyCapability;
import net.zaharenko424.a_changed.capability.item.SyringeRifleItemHandlerCapability;
import org.jetbrains.annotations.ApiStatus;

import static net.zaharenko424.a_changed.AChanged.MODID;

@ApiStatus.Internal
public class AttachmentRegistry {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    //Hypnosis ability data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<HypnosisData>> HYPNOSIS_DATA = ATTACHMENTS
            .register("hypnosis_data", ()-> AttachmentType.builder(HypnosisData::new).build());

    //Grab ability data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<GrabData>> GRAB_DATA = ATTACHMENTS
            .register("grab_data", ()-> AttachmentType.builder(GrabData::new)
                    .serialize(GrabData.SERIALIZER).build());

    //Transfur
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<TransfurHandler>> TRANSFUR_HANDLER = ATTACHMENTS
            .register("transfur_handler", ()-> AttachmentType.builder(TransfurHandler::new)
                    .serialize(TransfurHandler.SERIALIZER).copyOnDeath().copyHandler(TransfurHandler.COPY_HANDLER).build());

    //Item energy
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ItemEnergyCapability.ItemEnergyStorage>> ITEM_ENERGY_HANDLER = ATTACHMENTS
            .register("item_energy_handler", ()-> AttachmentType.builder(ItemEnergyCapability.ItemEnergyStorage::new)
                    .serialize(ItemEnergyCapability.Serializer.INSTANCE).build());

    //Latex covered data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<LatexCoveredData>> LATEX_COVERED = ATTACHMENTS
            .register("latex_covered", ()-> AttachmentType.builder(LatexCoveredData::new)
                    .serialize(LatexCoveredData.Serializer.INSTANCE).build());

    //Pneumatic syringe rifle inv
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SyringeRifleItemHandlerCapability.PneumaticSyringeRifleItemHandler>> PNEUMATIC_SYRINGE_RIFLE_ITEM_HANDLER = ATTACHMENTS
            .register("pneumatic_syringe_rifle_item_handler", ()-> AttachmentType.builder(SyringeRifleItemHandlerCapability.PneumaticSyringeRifleItemHandler::new)
                    .serialize(SyringeRifleItemHandlerCapability.Serializer.INSTANCE).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SyringeRifleItemHandlerCapability.SyringeCoilGunItemHandler>> SYRINGE_COIL_GUN_HANDLER = ATTACHMENTS
            .register("syringe_coil_gun_item_handler", ()-> AttachmentType.builder(SyringeRifleItemHandlerCapability.SyringeCoilGunItemHandler::new)
                    .serialize(SyringeRifleItemHandlerCapability.Serializer1.INSTANCE).build());
}