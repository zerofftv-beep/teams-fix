package com.example.sbwteams.team;

import com.example.sbwteams.vehicle.VehicleOwnership;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class TeamManager {

    private static final Map<String, Team> teams = new HashMap<>();
    private static final Map<UUID, String> playerToTeam = new HashMap<>();

    public static int MAX_TEAM_SIZE = 4;
    public static int NAMETAG_DISTANCE = 128;

    public static void createTeam(String name, ServerPlayer leader) {
        if (teams.containsKey(name) || playerToTeam.containsKey(leader.getUUID())) return;

        Team team = new Team(name, leader.getUUID());
        teams.put(name, team);
        playerToTeam.put(leader.getUUID(), name);
    }

    public static boolean invitePlayer(String teamName, ServerPlayer inviter, ServerPlayer target) {
        Team team = teams.get(teamName);
        if (team == null || !team.isLeader(inviter.getUUID()) || playerToTeam.containsKey(target.getUUID())) return false;
        if (team.getMembers().size() >= MAX_TEAM_SIZE) return false;

        team.addInvite(target.getUUID());
        return true;
    }

    public static boolean joinTeam(String teamName, ServerPlayer player) {
        Team team = teams.get(teamName);
        if (team == null || !team.hasInvite(player.getUUID())) return false;

        team.addMember(player.getUUID());
        playerToTeam.put(player.getUUID(), teamName);
        team.removeInvite(player.getUUID());
        return true;
    }

    public static void leaveTeam(ServerPlayer player) {
        String teamName = playerToTeam.remove(player.getUUID());
        if (teamName != null) {
            Team team = teams.get(teamName);
            if (team != null) {
                team.removeMember(player.getUUID());
                if (team.getMembers().isEmpty()) teams.remove(teamName);
            }
        }
    }

    public static Team getTeam(Player player) {
        String name = playerToTeam.get(player.getUUID());
        return name != null ? teams.get(name) : null;
    }

    public static void setMaxSize(int size) {
        MAX_TEAM_SIZE = size;
    }

    public static void setNametagDistance(int distance) {
        NAMETAG_DISTANCE = distance;
    }

    public static void claimVehicle(Entity vehicle, ServerPlayer player) {
        Team team = getTeam(player);
        if (team != null) {
            VehicleOwnership.setOwnerTeam(vehicle, team.getName());
        }
    }

    public static boolean canMountVehicle(Player player, Entity vehicle) {
        return VehicleOwnership.canPlayerMount(player, vehicle);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {}

    public static void loadTeams(MinecraftServer server) {}
    public static void saveTeams(MinecraftServer server) {}
}