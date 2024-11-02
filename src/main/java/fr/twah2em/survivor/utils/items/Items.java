package fr.twah2em.survivor.utils.items;

import fr.twah2em.survivor.inventories.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataType;

public abstract class Items {
    public static final ItemBuilder WAND = new ItemBuilder(Material.WOODEN_AXE)
            .withName("§aCréateur de cuboids")
            .withLore("§7§oComme avec World Edit, le premier clique gauche",
                    "§7§opermet de définir le point n°1 du cuboid et le",
                    "§7§osecond clique gauche le point n°2 !",
                    "§7§o",
                    "§7§oAppuyez sur votre touche de drop pour annuler",
                    "§7§ola prise en compte du dernier point défini.",
                    "§7§oSi aucun point défini, alors le menu de la",
                    "§7§ocréation de cuboid va vous être rouvert et",
                    "§7§ovous allez perdre la hache.",
                    "§7§oClique droit pour valider la sélection.")
            .withPersistentData("survivor", "cuboid_wand", true, PersistentDataType.BOOLEAN);

    public static final ItemBuilder RETURN_CUBOIDS_GUI = new ItemBuilder(Material.OAK_SIGN)
            .withName("§aRetourner dans le menu précédent")
            .withLore("§7§oClique droit pour quitter la prévisualisation des cuboids et",
                    "§7§o retourner dans le menu des cuboids de la pièce.")
            .withPersistentData("survivor", "cuboid_sign", true, PersistentDataType.BOOLEAN);
}
