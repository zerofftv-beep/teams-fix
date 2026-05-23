package com.example.sbwteams.client;

import com.example.sbwteams.team.Team;
import com.example.sbwteams.team.TeamManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TeamHUD {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        Team team = TeamManager.getTeam(mc.player);
        if (team == null) return;

        GuiGraphics gui = event.getGuiGraphics();
        int y = 10;

        gui.drawString(mc.font, Component.literal("§a[Team: " + team.getName() + "]"), 10, y, 0xFFFFFF);
        y += 12;

        for (var memberId : team.getMembers()) {
            var player = mc.level.getPlayerByUUID(memberId);
            if (player != null) {
                String hp = String.format("%.1f", player.getHealth());
                gui.drawString(mc.font, 
                    Component.literal("§f" + player.getName().getString() + " §c♥ " + hp), 
                    10, y, 0xFFFFFF);
                y += 11;
            }
        }
    }
}