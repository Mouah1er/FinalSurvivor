package fr.bibouh.survivor.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import utils.ItemBuilder;

import java.util.HashMap;

public class packAPunchBlock implements Listener {

    public HashMap<Player, ItemMeta> map = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block block = event.getClickedBlock();
            if (block.getType() == Material.ANVIL) {

                event.setCancelled(true);

                ItemStack armeDuJoueur = new ItemBuilder(player.getInventory().getItemInHand().getType()).addEnchant(Enchantment.SILK_TOUCH, 1).hideInformation().build();
                ItemMeta armeDuJoueurM = armeDuJoueur.getItemMeta();

                map.put(player, armeDuJoueurM);
                player.getInventory().removeItem(player.getItemInHand());
                player.getInventory().addItem(armeDuJoueur);
                player.sendMessage("§2Tu as réussi à pack à puncher ton arme !");

            }

        }

    }

}
