package com.rebelcarrot.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.ItemUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class CleanMateInteraction extends SimpleInteraction {

    private static final String STATE_WASHED_OUT = "Washed_Out";

    public static BuilderCodec<CleanMateInteraction> CODEC = BuilderCodec.builder(
            CleanMateInteraction.class, CleanMateInteraction::new, SimpleInteraction.CODEC
    ).build();

    @Override
    protected void tick0(boolean firstRun, float time, @NonNullDecl InteractionType type,
                         @NonNullDecl InteractionContext context, @NonNullDecl CooldownHandler cooldownHandler) {

        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        if (commandBuffer == null) return;
        Ref<EntityStore> owningEntity = context.getOwningEntity();

        Player player = commandBuffer.getComponent(owningEntity, Player.getComponentType());
        if (player == null) return;

        ItemStack heldItem = context.getHeldItem();
        if (heldItem == null) return;

        String itemId = heldItem.getItemId();
        if (!itemId.contains(STATE_WASHED_OUT)) return;

        ItemStack itemStack = new ItemStack("Mate");
        ItemContainer itemContainer = context.getHeldItemContainer();
        byte slot = context.getHeldItemSlot();
        if (itemContainer != null) {
            itemContainer.setItemStackForSlot(slot, itemStack);
        }

        ItemStack yerba = new ItemStack("Ingredient_Fibre", 1);

        Ref<EntityStore> ref = player.getReference();
        if (ref != null && ref.isValid()) {
            ItemUtils.throwItem(ref, yerba,1, commandBuffer);
        }
        context.getState().state = InteractionState.Finished;

    }
}
