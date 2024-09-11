package net.zaharenko424.a_changed.atest;

import net.minecraft.world.entity.Entity;

import java.util.List;

public abstract class ProceduralAnimation<E extends Entity> {

    public boolean checkRequirements(UniversalModel<E> model){
        for(String name : requirements()){
            if(model.getPart(name) == null) return false;
        }
        return true;
    }

    public abstract List<String> requirements();

    public abstract void animate(E pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch);
}