package net.zaharenko424.a_changed.ability.api;

public interface AbilityData {

    default boolean isActivated(){
        return false;
    }

    void sync();
}