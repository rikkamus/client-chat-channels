package com.rikkamus.clientchatchannels;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.scores.PlayerTeam;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class LocalPlayerUtil {

    public static SortedSet<String> getNamesOfPlayersWithinRadius(double radius) {
        final LocalPlayer localPlayer = Minecraft.getInstance().player;

        return localPlayer.clientLevel.players()
                                      .stream()
                                      .filter(player -> !localPlayer.equals(player) && player.position().distanceTo(localPlayer.position()) <= radius)
                                      .map(player -> player.getGameProfile().getName())
                                      .collect(Collectors.toCollection(TreeSet::new));
    }

    public static Optional<String> getNameOfNearestPlayer() {
        final LocalPlayer localPlayer = Minecraft.getInstance().player;

        return localPlayer.clientLevel.players()
                                      .stream()
                                      .filter(player -> !localPlayer.equals(player))
                                      .min(Comparator.comparingDouble(p -> p.position().distanceTo(localPlayer.position())))
                                      .map(player -> player.getGameProfile().getName());
    }

    public static SortedSet<String> getNamesOfPlayersInTeam() {
        final LocalPlayer localPlayer = Minecraft.getInstance().player;
        final PlayerTeam team = localPlayer.getTeam();

        return localPlayer.clientLevel.players()
                                      .stream()
                                      .filter(player -> !localPlayer.equals(player) && team == player.getTeam())
                                      .map(player -> player.getGameProfile().getName())
                                      .collect(Collectors.toCollection(TreeSet::new));
    }

    public static boolean isPlayerInTeam() {
        return Minecraft.getInstance().player.getTeam() != null;
    }

}
