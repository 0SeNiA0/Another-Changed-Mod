package net.zaharenko424.testmod.entity;

import net.zaharenko424.testmod.entity.Transfurrable;

public interface TransfurHolder extends Transfurrable {

    boolean mod$isTransfurred();

    void mod$unTransfur();
}