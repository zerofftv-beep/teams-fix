package com.example.sbwteams.team;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Team {
    private final String name;
    private final UUID leader;
    private final Set<UUID> members = new HashSet<>();
    private final Set<UUID> pendingInvites = new HashSet<>();

    public Team(String name, UUID leader) {
        this.name = name;
        this.leader = leader;
        this.members.add(leader);
    }

    public String getName() { return name; }
    public UUID getLeader() { return leader; }
    public Set<UUID> getMembers() { return members; }

    public boolean isLeader(UUID uuid) { return leader.equals(uuid); }

    public void addMember(UUID uuid) { members.add(uuid); }
    public void removeMember(UUID uuid) { members.remove(uuid); }

    public void addInvite(UUID uuid) { pendingInvites.add(uuid); }
    public boolean hasInvite(UUID uuid) { return pendingInvites.contains(uuid); }
    public void removeInvite(UUID uuid) { pendingInvites.remove(uuid); }
}