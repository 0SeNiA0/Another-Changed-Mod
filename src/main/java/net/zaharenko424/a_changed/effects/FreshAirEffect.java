package net.zaharenko424.a_changed.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;
@ParametersAreNonnullByDefault
public class FreshAirEffect extends MobEffect {
    public FreshAirEffect() {
        super(MobEffectCategory.BENEFICIAL, 15597018);
    }

    @Override
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(new IClientMobEffectExtensions() {
            @Override
            public boolean isVisibleInInventory(MobEffectInstance instance) {
                return false;
            }
            @Override
            public boolean isVisibleInGui(MobEffectInstance instance) {
                return false;
            }
        });
    }
}