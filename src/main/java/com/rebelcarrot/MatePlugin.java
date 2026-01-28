package com.rebelcarrot;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.rebelcarrot.config.BlockBreakConfig;
import com.rebelcarrot.interactions.CleanMateInteraction;
import com.rebelcarrot.interactions.DrinkMateInteraction;
import com.rebelcarrot.interactions.FillMateInteraction;
import com.rebelcarrot.systems.BlockBreakEventSystem;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class MatePlugin extends JavaPlugin {
    private final Config<BlockBreakConfig> config;

    public MatePlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
        this.config = this.withConfig("BlockBreakConfig", BlockBreakConfig.CODEC);
    }

    @Override
    protected void setup() {

        //  Save config
        this.config.save();

        this.getCodecRegistry(Interaction.CODEC)
                .register("DrinkMateInteraction", DrinkMateInteraction.class, DrinkMateInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC)
                .register("CleanMateInteraction", CleanMateInteraction.class, CleanMateInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC)
                .register("FillMateInteraction", FillMateInteraction.class, FillMateInteraction.CODEC);

        //  Initialize Event Systems
        this.getEntityStoreRegistry().registerSystem(new BlockBreakEventSystem(config));
    }
}