package com.rebelcarrot.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class DrinkMateInteraction extends SimpleInteraction {

    private static final String STATE_BASE = "Mate";
    private static final String STATE_FILLED_WATER = "Filled_Water";
    private static final String STATE_WASHED_OUT = "Washed_Out";

    public static BuilderCodec<DrinkMateInteraction> CODEC = BuilderCodec.builder(
            DrinkMateInteraction.class, DrinkMateInteraction::new, SimpleInteraction.CODEC
    ).build();

    @Override
    protected void tick0(boolean firstRun, float time, @NonNullDecl InteractionType type,
                         @NonNullDecl InteractionContext context, @NonNullDecl CooldownHandler cooldownHandler) {

        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        if (commandBuffer == null) return;
        Ref<EntityStore> owningEntity = context.getOwningEntity();

        Player player = commandBuffer.getComponent(owningEntity, Player.getComponentType());
        if(player == null) return;

        ItemStack heldItem = context.getHeldItem();
        if(heldItem == null) return;

        String itemId = heldItem.getItemId();

        // Solo se puede beber un mate lleno de agua
        if(!itemId.contains(STATE_FILLED_WATER)) return;

        double currentDurability = heldItem.getDurability();
        double newDurability = currentDurability - 1.0;

        ItemStack updatedItem;

        if (newDurability > 0) {
            updatedItem = new ItemStack(STATE_BASE, heldItem.getQuantity(), newDurability, heldItem.getMaxDurability(), null);

        } else {
            updatedItem = heldItem
                    .withState(STATE_WASHED_OUT);
        }

        ItemContainer itemContainer = context.getHeldItemContainer();
        byte slot = context.getHeldItemSlot();
        if (itemContainer != null) {
            itemContainer.setItemStackForSlot(slot, updatedItem);
        }

        context.getState().state = InteractionState.Finished;
    }
}
