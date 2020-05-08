package fr.bibouh.survivor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import utils.ItemBuilder;

public class packAPunch implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        Player player = (Player) sender;
        ItemStack armeDuJoueur = new ItemBuilder(player.getInventory().getItemInHand().getType()).addEnchant(Enchantment.SILK_TOUCH, 1).hideInformation().build();

        player.getInventory().removeItem(player.getItemInHand());
        player.getInventory().addItem(armeDuJoueur);
        player.sendMessage("§2Tu as réussi à pack à punché ton arme !");

        return false;
    }
}
