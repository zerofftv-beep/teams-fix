package com.example.sbwteams.client;

import com.example.sbwteams.team.TeamManager;
import com.example.sbwteams.vehicle.VehicleOwnership;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VehicleNameTagRenderer {

    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        Entity entity = event.getEntity();
        String ownerTeam = VehicleOwnership.getOwnerTeam(entity);

        if (ownerTeam == null) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        var team = TeamManager.getTeam(player);

        if (team != null && team.getName().equals(ownerTeam)) {
            Font font = Minecraft.getInstance().font;
            Component text = Component.literal("§a[Team: " + ownerTeam + "]");

            event.setContent(text);
            event.setResult(RenderNameTagEvent.Result.ALLOW);
        }
    }
}