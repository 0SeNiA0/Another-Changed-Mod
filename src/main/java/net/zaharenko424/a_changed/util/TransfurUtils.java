package net.zaharenko424.a_changed.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.*;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

import static net.zaharenko424.a_changed.registry.EntityRegistry.WHITE_LATEX_WOLF_MALE;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.TRANSFUR_TOLERANCE;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.getTransfurEntity;

public class TransfurUtils {


    public static final Consumer<LivingEntity> RECALCULATE_PROGRESS = target -> {
        if(target.level().isClientSide) throw new IllegalStateException("Cannot run serverside only methods on client!");
        ITransfurHandler handler = TransfurCapability.of(target);
        if(handler == null) return;

        if(handler.isTransfurred() || handler.isBeingTransfurred()) return;
        if(handler.getTransfurProgress() >= TRANSFUR_TOLERANCE && handler.getTransfurType() != null)
            handler.transfur(handler.getTransfurType(), TransfurContext.TRANSFUR_DEF);
    };

    public static void addModifiers(LivingEntity holder, TransfurType transfurType){
        AttributeMap map = holder.getAttributes();
        AttributeInstance[] instance = new AttributeInstance[1];
        transfurType.modifiers.asMap().forEach((attribute, modifiers) -> {
            if(!map.hasAttribute(attribute)){
                AChanged.LOGGER.error("Attempted to add transfur modifier to not existing attribute {} {}", attribute, transfurType);
                return;
            }

            if(attribute == Attributes.MAX_HEALTH){
                float maxHealthO = holder.getMaxHealth();
                instance[0] = map.getInstance(attribute);
                for(AttributeModifier modifier : modifiers){
                    instance[0].addTransientModifier(modifier);
                }
                float diff = holder.getMaxHealth() - maxHealthO;
                if(diff > 0) holder.setHealth(holder.getHealth() + diff);
                return;
            }

            instance[0] = map.getInstance(attribute);
            for(AttributeModifier modifier : modifiers){
                instance[0].addTransientModifier(modifier);
            }
        });
    }

    public static void removeModifiers(LivingEntity holder, Multimap<Attribute, AttributeModifier> modifierMap){
        AttributeMap map = holder.getAttributes();
        AttributeInstance[] instance = new AttributeInstance[1];
        modifierMap.asMap().forEach((attribute, modifiers) -> {
            if(!map.hasAttribute(attribute)) return;

            if(attribute == Attributes.MAX_HEALTH){
                float maxHealthO = holder.getMaxHealth();
                instance[0] = map.getInstance(attribute);
                for(AttributeModifier modifier : modifiers){
                    instance[0].removeModifier(modifier.getId());
                }
                if(holder.getMaxHealth() - maxHealthO < 0) holder.setHealth(holder.getMaxHealth());
                return;
            }
            instance[0] = map.getInstance(attribute);
            for(AttributeModifier modifier : modifiers){
                instance[0].removeModifier(modifier.getId());
            }
        });
    }

    public static AbstractLatexBeast spawnLatex(@NotNull TransfurType transfurType, @NotNull ServerLevel level, @NotNull BlockPos pos){
        return Objects.requireNonNullElseGet(getTransfurEntity(transfurType.id), WHITE_LATEX_WOLF_MALE).spawn(level, pos, MobSpawnType.CONVERSION);
    }
}