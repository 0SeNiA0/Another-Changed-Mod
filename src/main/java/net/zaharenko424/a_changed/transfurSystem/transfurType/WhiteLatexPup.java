package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.minecraft.world.entity.*;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.client.model.WhiteLatexPupModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WhiteLatexPup extends AbstractLatexPup<net.zaharenko424.a_changed.entity.WhiteLatexPup> {

    public WhiteLatexPup(@NotNull Properties<net.zaharenko424.a_changed.entity.WhiteLatexPup> properties) {
        super(properties
                .maxHealthModifier(-4)
                .addAbility(AbilityRegistry.WL_PUP_AGE).addAbility(AbilityRegistry.WOLF_PASSIVE));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, WhiteLatexPupModel::new);
    }

    protected final EntityDimensions dimensions = new EntityDimensions(.6f, .85f, .68f,
            EntityAttachments.builder().attach(EntityAttachment.PASSENGER, new Vec3(0.0, 0.81875, -0.0625)).build(.6f, .85f),
            false);

    @Override
    public @Nullable EntityDimensions getPoseDimensions(@NotNull LivingEntity entity, @NotNull Pose pose) {
        return dimensions.scale(entity.getAgeScale());
    }
}
