package fr.bibouh.survivor.listeners;

import fr.bibouh.survivor.GState;
import fr.bibouh.survivor.Main;
import fr.bibouh.survivor.tasks.GAutaStartTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;


public class GPLayerListener implements Listener {

    private Main main;
    public static HashMap<Player, ItemStack> map = new HashMap<>();

    public GPLayerListener(Main main) {
        this.main = main;

    }

    public static ItemStack medecin = new ItemStack(Material.PAPER);
    public static ItemStack grenadier = new ItemStack(Material.FIREWORK_CHARGE);
    public static ItemStack support = new ItemStack(Material.STICK);
    public static ItemStack piegeur = new ItemStack(Material.TRAP_DOOR);
    public static ItemStack ranger = new ItemStack(Material.BONE);

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        ItemStack kit = new ItemStack(Material.LEATHER);

        Location spawn = new Location(Bukkit.getWorld("world"),-291, 93, 14, -0.0f, 1.2f);
        player.teleport(spawn);
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);

        if(main.isState(GState.PLAYING)) {

            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("§4Le jeu a déjà démarré");
            event.setJoinMessage(null);
            return;

        }

        /*if(main.isState(GState.FINISH)) {

            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("§4Le jeu a déjà démarré");
            event.setJoinMessage(null);
            return;

        }*/

        if(!main.getPlayers().contains(player))main.getPlayers().add(player);
        player.setGameMode(GameMode.ADVENTURE);
        event.setJoinMessage("§f[§6The Legend Of Craft§f] §6"+player.getName()+" §6a rejoint la partie ! §6[§a" +main.getPlayers().size()+"§a/4§6]");

        if(main.isState(GState.WAITING) && main.getPlayers().size() == 2) {

            GAutaStartTask start = new GAutaStartTask(main);
            start.runTaskTimer(main, 0, 20);
            main.setState(GState.STARTING);

        }

        ItemMeta kitM = kit.getItemMeta();
        kitM.setDisplayName("§2Choisir un kit");
        kitM.setLore(Arrays.asList("§7Clique §6droit §7pour choisir un §6kit §7acheté au §6lobby §7du serveur"));

        kit.setItemMeta(kitM);

        if(player.getInventory().contains(kit.getType())) return; else player.getInventory().setItem(0, kit);


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        event.setQuitMessage("§f[§6The Legend Of Craft§f] §6"+player.getName()+" §6a quitté la partie ! §6[§c   "+main.getPlayers().size()+"§c/4§6]");

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack itemStack = event.getItem();
        Inventory inventory = Bukkit.createInventory(null, 9, "§7Les kits");

        if(itemStack != null && itemStack.getType() == Material.LEATHER) {

            ItemMeta grenadierM = grenadier.getItemMeta();
            grenadierM.setDisplayName("§2Grenadier");
            grenadierM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS);
            grenadierM.setLore(Arrays.asList("§1Vous obtenez à chaque début de vague:", "§3-§6 6 grenades", "§3-§6 1 grenade incendiaire", "§3- §63 grenades à fragmentations"));
            grenadier.setItemMeta(grenadierM);
            inventory.setItem(1, grenadier);

            ItemMeta medecinM = medecin.getItemMeta();
            medecinM.setDisplayName("§dMédecin");
            medecinM.setLore(Arrays.asList("§3- §6Vous réanimez instantanément vos coéquipiers", "§3- §6Vous vous soignez plus rapidement avec vos kits de soins"));
            medecin.setItemMeta(medecinM);
            inventory.setItem(0, medecin);

            ItemMeta supportM = support.getItemMeta();
            supportM.setDisplayName("§9Support");
            supportM.setLore(Arrays.asList(" §1Vous obtenez à chaque début de vague:", "§3- §6CHOPPER CHICAGO 1921, 50 balles", "§3- §6Une boite de munition pour" , "§6réapprovisionner vos coéquipiers et vous en munitions"));
            support.setItemMeta(supportM);
            inventory.setItem(2, support);

            ItemMeta piegeurM = piegeur.getItemMeta();
            piegeurM.setDisplayName("§aPiégeur de zombies");
            piegeurM.setLore(Arrays.asList("§1Vous obtenez à chaque munitions max:", "§3- §64 mines terrestres", "§3- §62 C4 de 2 charges chacun §4§l(Vous infligent des", "§4§ldégâts si vous êtes trop près !!)"));
            piegeur.setItemMeta(piegeurM);
            inventory.setItem(3, piegeur);

            ItemMeta rangerM = ranger.getItemMeta();
            rangerM.setDisplayName("§bRanger");
            rangerM.setLore(Arrays.asList("§1Vous obtenez à chaque début de vague:", "§3- §6Kar 98, 25 balles", "§3- §6Une compétence vous permettant", "§6de vous camoufler aux yeux des", "§6zombies pendant 20 secondes."));
            ranger.setItemMeta(rangerM);
            inventory.setItem(4, ranger);

            player.openInventory(inventory);

        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if(current == null) return;

        if(inventory.getName().equalsIgnoreCase("§7Les kits")) {

            if(current.getType() == Material.PAPER) {

                event.setCancelled(true);

                player.closeInventory();
                player.sendMessage("§6Vous avez bien choisi le kit §dMédecin§6.");
                map.put(player, medecin);

            }

            if(current.getType() == Material.FIREWORK_CHARGE) {

                event.setCancelled(true);

                player.closeInventory();
                player.sendMessage("§6Vous avez bien choisi le kit §2Grenadier§6.");
                map.put(player, grenadier);

            }

            if(current.getType() == Material.STICK) {

                event.setCancelled(true);

                player.closeInventory();
                player.sendMessage("§6Vous avez bien choisi le kit §9Support§6.");
                map.put(player, support);

            }

            if(current.getType() == Material.TRAP_DOOR) {

                event.setCancelled(true);

                player.closeInventory();
                player.sendMessage("§6Vous avez bien choisi le kit §aPiégeur de zombies§6.");
                map.put(player, piegeur);

            }

            if(current.getType() == Material.BONE) {

                event.setCancelled(true);

                player.closeInventory();
                player.sendMessage("§6Vous avez bien choisi le kit §bRanger§6.");
                map.put(player, ranger);

            }

        }
    }

}
