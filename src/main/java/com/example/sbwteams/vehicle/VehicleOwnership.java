package com.example.sbwteams.vehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class VehicleOwnership {

    private static final String TEAM_KEY = "SBWTeamOwner";

    public static void setOwnerTeam(Entity vehicle, String teamName) {
        CompoundTag tag = vehicle.getPersistentData();
        tag.putString(TEAM_KEY, teamName);
    }

    public static String getOwnerTeam(Entity vehicle) {
        CompoundTag tag = vehicle.getPersistentData();
        return tag.contains(TEAM_KEY) ? tag.getString(TEAM_KEY) : null;
    }

    public static void clearOwner(Entity vehicle) {
        vehicle.getPersistentData().remove(TEAM_KEY);
    }

    public static boolean canPlayerMount(Player player, Entity vehicle) {
        String ownerTeam = getOwnerTeam(vehicle);
        if (ownerTeam == null) return true;

        var team = com.example.sbwteams.team.TeamManager.getTeam(player);
        if (team == null) return false;

        return ownerTeam.equals(team.getName());
    }
}