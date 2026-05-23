package com.example.sbwteams;

import com.example.sbwteams.client.TeamHUD;
import com.example.sbwteams.client.VehicleNameTagRenderer;
import com.example.sbwteams.command.TeamCommands;
import com.example.sbwteams.team.TeamEvents;
import com.example.sbwteams.team.TeamManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;

@Mod(SBWTeams.MODID)
public class SBWTeams {
    public static final String MODID = "sbwteams";

    public SBWTeams() {
        MinecraftForge.EVENT_BUS.register(TeamManager.class);
        MinecraftForge.EVENT_BUS.register(TeamEvents.class);

        // Клиентские классы регистрируем только на клиенте
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(TeamHUD.class);
            MinecraftForge.EVENT_BUS.register(VehicleNameTagRenderer.class);
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        TeamManager.loadTeams(event.getServer());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        TeamCommands.register(event.getDispatcher());
    }
}