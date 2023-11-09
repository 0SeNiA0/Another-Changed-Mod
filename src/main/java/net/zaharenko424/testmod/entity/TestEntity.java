package net.zaharenko424.testmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TestEntity extends AbstractLatexBeast {

    public TestEntity(EntityType<? extends Monster> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_,"test_latex");
    }

    public static AttributeSupplier.@NotNull Builder createAttributes(){
        return baseAttributes();
    }
}