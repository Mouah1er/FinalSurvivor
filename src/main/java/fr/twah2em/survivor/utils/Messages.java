package fr.twah2em.survivor.utils;

import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public abstract class Messages {
    public static final Component PREFIX = Component
            .text("[", GOLD)
            .append(text("Survivor", RED))
            .append(text("]", GOLD))
            .append(text(" ", WHITE));

    public static final Component RESTART_MESSAGE = PREFIX.append(text("Le serveur redémarre !", RED));

    public static Component ROUND_START_MESSAGE(String round) {
        return PREFIX.append(text("Le round " + round + " commence !", GREEN, BOLD));
    }

    public static Component ROUND_END_MESSAGE(String round) {
        return PREFIX.append(text("Le round " + round + " est terminé ! 30 secondes de pause !", GREEN, BOLD));
    }

    public static Component TEN_SEC_REMAINING = PREFIX.append(text("Il reste 10 seconde avant le début de la prochaine manche!", GREEN, BOLD));

    public static final Component ZOMBIE_KILLED_MESSAGE = PREFIX
            .append(text("+90", GOLD, ITALIC))
            .append(text("$", DARK_GREEN, ITALIC));

    public static final Component ZOMBIE_HIT_MESSAGE = PREFIX
            .append(text("+10", GOLD, ITALIC))
            .append(text("$", DARK_GREEN, ITALIC));

    public static final Component AREA_NOT_AUTHORIZED_MESSAGE = PREFIX.append(text("Vous n'êtes pas autorisé à aller dans cette zone !", RED));

    public static final Component ERROR_OCCURRED = PREFIX.append(text("Une erreur est survenue, la partie doit être arrêtée !", RED));

    public static final Component EMPTY_GAME = PREFIX.append(text("Tous les joueurs ont quitté, la partie s'arrête donc !", RED));

    public static final Component PERMISSION_ERROR = PREFIX.append(text("Vous n'avez pas la permission pour faire cela !", RED));

    public static final Component NOT_A_PLAYER_ERROR = PREFIX.append(text("Vous devez être un joueur pour utiliser cette commande !", RED));

    public static final Component ARGS_ERROR = PREFIX.append(text("Vous n'avez pas mis les bons arguments !", RED));

    public static final Component CANCELLED_ROOM_CREATION = PREFIX.append(text("Vous avez bien annulé la création de la pièce !", GREEN));

    public static Component CANNOT_CREATE_ROOM(String playerName) {
        return PREFIX
                .append(text("Vous ne pouvez pas créer une pièce tant qu'un joueur est en train d'en créer une ! Si vous voulez tout de même le faire, vous pouvez" +
                        " kick le joueur ", RED))
                .append(text(playerName, YELLOW))
                .append(text(" qui est en train d'en créer une.", RED));
    }

    public static final Component NO_CUBOIDS_IN_ROOM = text("La pièce n'a aucun cuboid !", RED);

    public static final Component CUBOID_1_SUCCESSFULLY_REMOVED = PREFIX.append(text("Vous avez bien supprimé la sélection du premier point du cuboid !",
            GREEN));

    public static final Component CUBOID_2_SUCCESSFULLY_REMOVED = PREFIX.append(text("Vous avez bien supprimé la sélection du second point du cuboid !",
            GREEN));

    public static final Component CUBOID_WAND_SUCCESSFULLY_REMOVED = PREFIX.append(text("Vous avez annulé la création du cuboid et avez par conséquent" +
            " perdu la hâche !", RED));

    public static Component CUBOID_1_SUCCESSFULLY_CREATED(String location) {
        return PREFIX
                .append(text("Vous avez bien défini la location ", GREEN))
                .append(text(location))
                .append(text(" comme le point", GREEN))
                .append(text(" n°1 ", GOLD))
                .append(text("du cuboid", GREEN));
    }

    public static Component CUBOID_2_SUCCESSFULLY_CREATED(String location) {
        return PREFIX
                .append(text("Vous avez bien défini la location ", GREEN))
                .append(text(location))
                .append(text(" comme le point", GREEN))
                .append(text(" n°2 ", GOLD))
                .append(text("du cuboid", GREEN));
    }

    public static final Component CANNOT_ADD_MORE_POINT = PREFIX.append(text("Vous avez déjà défini les deux points du cuboid, regardez la description de la hâche " +
            "pour en savoir plus.", RED));

    public static final Component ERROR_WITH_CUBOID = PREFIX.append(text("Il y a eu une erreur au moment de créer le cuboid, s'il vous plait supprimez les deux" +
            " locations avec votre touche drop et recommencez !", RED));

    public static final Component CANNOT_CREATE_CUBOID = PREFIX.append(text("Vous ne pouvez pas valider le cuboid puisque vous n'avez pas sélectionné 2 " +
            "locations !", RED));

    public static final Component CUBOID_SUCCESSFULLY_CREATED = PREFIX.append(text("Le cuboid a bien été créé !", GREEN));

    public static final Component CUBOID_SUCCESSFULLY_DELETED = PREFIX.append(text("Le cuboid a bien été supprimé !", GREEN));

    public static final Component CANT_DROP = PREFIX.append(text("Vous ne pouvez pas jeter cet objet!", RED));
}
