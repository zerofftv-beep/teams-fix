package com.example.sbwteams.command;

import com.example.sbwteams.team.TeamManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TeamCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("team")
            .then(Commands.literal("create")
                .then(Commands.argument("name", StringArgumentType.string())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        String name = StringArgumentType.getString(ctx, "name");
                        TeamManager.createTeam(name, player);
                        player.sendSystemMessage(Component.literal("Team created: " + name));
                        return 1;
                    })))

            .then(Commands.literal("invite")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(ctx -> {
                        ServerPlayer inviter = ctx.getSource().getPlayerOrException();
                        ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                        var team = TeamManager.getTeam(inviter);
                        if (team != null && TeamManager.invitePlayer(team.getName(), inviter, target)) {
                            target.sendSystemMessage(Component.literal("You were invited to team: " + team.getName()));
                        }
                        return 1;
                    })))

            .then(Commands.literal("join")
                .then(Commands.argument("name", StringArgumentType.string())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        String name = StringArgumentType.getString(ctx, "name");
                        if (TeamManager.joinTeam(name, player)) {
                            player.sendSystemMessage(Component.literal("Joined team: " + name));
                        }
                        return 1;
                    })))

            .then(Commands.literal("leave")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    TeamManager.leaveTeam(player);
                    player.sendSystemMessage(Component.literal("You left the team"));
                    return 1;
                }))
        );

        dispatcher.register(Commands.literal("teamadmin")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("maxsize")
                .then(Commands.argument("size", IntegerArgumentType.integer(1, 20))
                    .executes(ctx -> {
                        int size = IntegerArgumentType.getInteger(ctx, "size");
                        TeamManager.setMaxSize(size);
                        ctx.getSource().sendSuccess(() -> Component.literal("Max team size set to " + size), true);
                        return 1;
                    })))
            .then(Commands.literal("nametagdistance")
                .then(Commands.argument("distance", IntegerArgumentType.integer(10, 512))
                    .executes(ctx -> {
                        int dist = IntegerArgumentType.getInteger(ctx, "distance");
                        TeamManager.setNametagDistance(dist);
                        ctx.getSource().sendSuccess(() -> Component.literal("Nametag distance set to " + dist), true);
                        return 1;
                    })))
        );
    }
}