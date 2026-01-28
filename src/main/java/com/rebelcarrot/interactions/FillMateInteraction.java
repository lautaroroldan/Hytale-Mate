package com.rebelcarrot.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

/**
 * FillMateInteraction - Ceba el mate con agua (temporal hasta que exista el termo)
 *
 * Preserva la durabilidad al cambiar de estado, a diferencia de RefillContainer.
 */
public class FillMateInteraction extends SimpleInteraction {

    public static BuilderCodec<FillMateInteraction> CODEC = BuilderCodec.builder(
            FillMateInteraction.class, FillMateInteraction::new, SimpleInteraction.CODEC
    ).build();

    private static final String STATE_FILLED_WATER = "Filled_Water";

    @Override
    protected void tick0(boolean firstRun, float time, @NonNullDecl InteractionType type,
                         @NonNullDecl InteractionContext context, @NonNullDecl CooldownHandler cooldownHandler) {
        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        if (commandBuffer == null) return;

        Ref<EntityStore> playerRef = context.getEntity();
        Player player = commandBuffer.getComponent(playerRef, Player.getComponentType());
        if (player == null) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        ItemStack itemStack = context.getHeldItem();
        if (itemStack == null || itemStack.isEmpty()) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        // Cebar el mate - withState preserva la durabilidad
        ItemStack mateCebado = itemStack.withState(STATE_FILLED_WATER);

        // Actualizar el item en la mano
        ItemContainer container = context.getHeldItemContainer();
        byte slot = context.getHeldItemSlot();
        if (container != null) {
            container.setItemStackForSlot(slot, mateCebado);
        }

        context.getState().state = InteractionState.Finished;
    }

}