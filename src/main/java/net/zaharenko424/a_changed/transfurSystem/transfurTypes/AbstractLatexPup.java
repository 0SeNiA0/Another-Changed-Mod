package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLatexPup extends TransfurType {

    public AbstractLatexPup(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    public void onTransfur(@NotNull LivingEntity entity) {//Drop unsupported armor
        super.onTransfur(entity);
        if(!(entity instanceof Player player)) return;

        ItemStack stack = player.getOffhandItem();
        if(!stack.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, stack);
            player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }

        NonNullList<ItemStack> armor = player.getInventory().armor;
        for(int i = 0; i < armor.size(); i++){
            stack = armor.get(i);
            if(!stack.is(Items.WOLF_ARMOR)){
                ItemHandlerHelper.giveItemToPlayer(player, stack);
                armor.set(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void onUnTransfur(@NotNull LivingEntity entity) {//Drop unsupported armor
        super.onUnTransfur(entity);
        if(!(entity instanceof Player player)) return;

        ItemStack stack = player.getInventory().armor.getFirst();
        if(stack.isEmpty()) return;
        ItemHandlerHelper.giveItemToPlayer(player, stack);
        player.getInventory().armor.set(0, ItemStack.EMPTY);
    }
}
