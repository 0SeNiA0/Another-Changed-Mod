package net.zaharenko424.testmod;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class TransfurManager {

    public static boolean isTransfurred(@NotNull Player player){
        return player.getPersistentData().contains("transfur");
    }
}