package net.zaharenko424.a_changed.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.capability.item.ItemEnergyCapability;
import net.zaharenko424.a_changed.capability.item.PneumaticSyringeRifleItemHandlerCapability;
import org.jetbrains.annotations.ApiStatus;

import static net.zaharenko424.a_changed.AChanged.MODID;

@ApiStatus.Internal
public class AttachmentRegistry {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    //Transfur
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<TransfurCapability.TransfurHandler>> TRANSFUR_HANDLER = ATTACHMENTS
            .register("transfur_handler", ()-> AttachmentType.builder(TransfurCapability.TransfurHandler::new)
                    .serialize(TransfurCapability.Serializer.INSTANCE).build());

    //Grab
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<GrabCapability.GrabHandler>> GRAB_HANDLER = ATTACHMENTS
            .register("grab_handler", ()-> AttachmentType.builder(GrabCapability.GrabHandler::new)
                    .serialize(GrabCapability.Serializer.INSTANCE).build());

    //Item energy
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ItemEnergyCapability.ItemEnergyStorage>> ITEM_ENERGY_HANDLER = ATTACHMENTS
            .register("item_energy_handler", ()-> AttachmentType.builder(ItemEnergyCapability.ItemEnergyStorage::new)
                    .serialize(ItemEnergyCapability.Serializer.INSTANCE).build());

    //Pneumatic syringe rifle inv
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PneumaticSyringeRifleItemHandlerCapability.PneumaticSyringeRifleItemHandler>> SYRINGE_RIFLE_ITEM_HANDLER = ATTACHMENTS
            .register("syringe_rifle_item_handler", ()-> AttachmentType.builder(PneumaticSyringeRifleItemHandlerCapability.PneumaticSyringeRifleItemHandler::new)
                    .serialize(PneumaticSyringeRifleItemHandlerCapability.Serializer.INSTANCE).build());
}