package net.zaharenko424.a_changed.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.a_changed.attachment.*;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import org.jetbrains.annotations.ApiStatus;

import static net.zaharenko424.a_changed.AChanged.MODID;

@ApiStatus.Internal
public class AttachmentRegistry {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    //DL Pup aging ability data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<LatexPupAgingData>> LATEX_PUP_AGING_DATA = ATTACHMENTS
            .register("latex_pup_age", () -> AttachmentType.builder(LatexPupAgingData::new)
                    .serialize(LatexPupAgingData.SERIALIZER).build());

    //DL Pup melt ability data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<DLPupMeltData>> DL_PUP_MELT_DATA = ATTACHMENTS
            .register("dl_pup_melt", () -> AttachmentType.builder(DLPupMeltData::new)
                    .serialize(DLPupMeltData.SERIALIZER).build());

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

    //Latex covered data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<LatexCoveredData>> LATEX_COVERED = ATTACHMENTS
            .register("latex_covered", ()-> AttachmentType.builder(LatexCoveredData::new)
                    .serialize(LatexCoveredData.Serializer.INSTANCE).build());
}