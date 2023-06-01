package fr.twah2em.survivor.utils;

import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

public abstract class Messages {
    public static final Component PREFIX = Component
            .text("[", GOLD)
            .append(Component.text("§cSurvivor", RED))
            .append(Component.text("]", GOLD))
            .append(Component.text(" ", WHITE));

    public static final Component RESTART_MESSAGE = text("Le serveur redémarre !", RED);

    public static final Component ROUND_START_MESSAGE = text("Le round %round% commence !", GREEN, BOLD);

    public static final Component ROUND_END_MESSAGE = text("Le round %round% est terminé ! 30 secondes de pause !", GREEN, BOLD);

    public static final Component ZOMBIE_KILLED_MESSAGE = text("+90$", DARK_GREEN, ITALIC);
}
