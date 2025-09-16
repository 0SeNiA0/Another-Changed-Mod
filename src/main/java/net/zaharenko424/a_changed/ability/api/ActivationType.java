package net.zaharenko424.a_changed.ability.api;

import net.minecraft.world.entity.LivingEntity;

public interface ActivationType {

    void onInput(AbilityHolder holder, InputController controller, Ability ability);

    static boolean isPassive(ActivationType type){
        return type == NEVER;
    }

    ActivationType NEVER = ((holder, controller, ability) -> {});

    ActivationType INSTANT = ((holder, controller, ability) -> {
        LivingEntity entity = holder.asEntity();
       if(controller.isDown() && ability.canUse(entity)) ability.activate(entity);
    });

    ActivationType HOLD = ((holder, controller, ability) -> {
        LivingEntity entity = holder.asEntity();
        if(controller.isDown()) {
           if(!ability.isActivated(entity) && ability.canUse(entity)) ability.activate(entity);
       } else if(ability.isActivated(entity)) ability.deactivate(entity);
    });

    ActivationType SWITCH = ((holder, controller, ability) -> {
        if(!controller.consumeInput()) return;

        LivingEntity entity = holder.asEntity();
        if(ability.isActivated(entity)){
            ability.deactivate(entity);
        } else if(ability.canUse(entity)) ability.activate(entity);
    });

    ActivationType SWITCH_CROUCH = ((holder, controller, ability) -> {
        if(!controller.consumeInput()) return;

        LivingEntity entity = holder.asEntity();
        if(ability.isActivated(entity)){
            if(entity.isCrouching()) ability.deactivate(entity);
        } else if(ability.canUse(entity)) ability.activate(entity);
    });
}
