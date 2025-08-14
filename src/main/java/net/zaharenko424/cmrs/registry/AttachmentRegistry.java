package net.zaharenko424.cmrs.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.ModelProperties;

public class AttachmentRegistry {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CMRS.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ModelProperties>> MODEL_PROPERTIES = ATTACHMENTS.register("model_properties", () -> AttachmentType
            .builder(ModelProperties::new)
            .serialize(ModelProperties.SERIALIZER)
            .copyHandler(ModelProperties.COPY_HANDLER).copyOnDeath()
            .sync(ModelProperties.SYNC).build());
}
