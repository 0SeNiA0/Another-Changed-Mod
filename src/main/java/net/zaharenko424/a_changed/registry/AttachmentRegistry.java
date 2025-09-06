package net.zaharenko424.a_changed.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.a_changed.ability.AbilityHolderAttachment;
import net.zaharenko424.a_changed.attachment.*;
import org.jetbrains.annotations.ApiStatus;

import static net.zaharenko424.a_changed.AChanged.MODID;

@ApiStatus.Internal
public class AttachmentRegistry {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    //Transfur
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<TransfurHandler>> TRANSFUR_HANDLER = ATTACHMENTS
            .register("transfur_handler", ()-> AttachmentType.builder(TransfurHandler::new)
                    .serialize(TransfurHandler.SERIALIZER).sync(TransfurHandler.SYNC)
                    .copyOnDeath().copyHandler(TransfurHandler.COPY_HANDLER).build());

    //Player ability holder
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AbilityHolderAttachment>> ABILITY_HOLDER = ATTACHMENTS
            .register("ability_holder", () -> AttachmentType.builder(AbilityHolderAttachment::new)
                    .serialize(AbilityHolderAttachment.SERIALIZER).sync(AbilityHolderAttachment.SYNC)
                    .copyHandler(AbilityHolderAttachment.COPY_HANDLER).copyOnDeath().build());


    //DL Pup aging ability data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<LatexPupAgingData>> LATEX_PUP_AGING_DATA = ATTACHMENTS
            .register("latex_pup_age", () -> AttachmentType.builder(LatexPupAgingData::new)
                    .serialize(LatexPupAgingData.SERIALIZER).sync(LatexPupAgingData.SYNC).build());

    //DL Pup melt ability data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<DLPupMeltData>> DL_PUP_MELT_DATA = ATTACHMENTS
            .register("dl_pup_melt", () -> AttachmentType.builder(DLPupMeltData::new)
                    .serialize(DLPupMeltData.SERIALIZER).sync(DLPupMeltData.SYNC).build());

    //Hypnosis ability data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<HypnosisData>> HYPNOSIS_DATA = ATTACHMENTS
            .register("hypnosis_data", ()-> AttachmentType.builder(HypnosisData::new)
                    .sync(HypnosisData.SYNC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<HypnotisedData>> HYPNOTISED_DATA = ATTACHMENTS
            .register("hypnotised_data", () -> AttachmentType.builder(HypnotisedData::new).build());

    //Grab ability data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<GrabData>> GRAB_DATA = ATTACHMENTS
            .register("grab_data", ()-> AttachmentType.builder(GrabData::new)
                    .serialize(GrabData.SERIALIZER).copyOnDeath().sync(GrabData.SYNC).build());



    //Latex covered data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<LatexCoveredData>> LATEX_COVERED = ATTACHMENTS
            .register("latex_covered", ()-> AttachmentType.builder(LatexCoveredData::new)
                    .serialize(LatexCoveredData.Serializer.INSTANCE).build());
}