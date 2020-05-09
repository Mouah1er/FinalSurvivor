package fr.bibouh.survivor.tasks;

import fr.bibouh.survivor.GState;
import fr.bibouh.survivor.Main;
import fr.bibouh.survivor.listeners.GPLayerListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import com.shampaggon.crackshot.CSUtility;
import java.util.Arrays;

public class GAutaStartTask extends BukkitRunnable {

    private Main main;
    private int timer = 20;
    private GPLayerListener gpLayerListener;
    private CSUtils csUtils = new CSUtils();


    public GAutaStartTask(Main main) {
        this.main = main;
    }

    public GAutaStartTask(GPLayerListener gpLayerListener) {
        this.gpLayerListener = gpLayerListener;
    }


    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.setLevel(timer);

        }

        if (timer == 20 || timer == 10 || timer == 5 || timer == 4)
            Bukkit.broadcastMessage("§6Le jeu va commencer dans §a" + timer + "§6s.");

        if (timer == 3) Bukkit.broadcastMessage("§6Le jeu va commencer dans §e" + timer + "§6s.");

        if (timer == 2) Bukkit.broadcastMessage("§6Le jeu va commencer dans §c" + timer + "§6s.");

        if (timer == 1) Bukkit.broadcastMessage("§6Le jeu va commencer dans §4" + timer + "§6s.");

        if (timer == 0) {


            Bukkit.broadcastMessage("§6On commence ! Bonne chance !");
            main.setState(GState.PLAYING);

            cancel();

            for (Player player : Bukkit.getOnlinePlayers()) {

                World world = Bukkit.getWorld("world");
                Location spawn = new Location(world, -287, 65, 80, 0.3f, 0.8f);
                player.getInventory().clear();
                player.teleport(spawn);
                player.setGameMode(GameMode.ADVENTURE);
                ItemStack itemStack = GPLayerListener.map.get(player);
                ItemStack trousseDeSoin = new ItemStack(Material.PAPER, 5);
                ItemStack grenade = new ItemStack(Material.FIREWORK_CHARGE, 6);
                ItemStack cocopops = new ItemStack(Material.COCOA, 3);

                ItemMeta trousseDeSoinM = trousseDeSoin.getItemMeta();
                trousseDeSoinM.setDisplayName("§dTrousse de soin");
                trousseDeSoinM.setLore(Arrays.asList("§7Permet de se soigner soit même."));
                trousseDeSoin.setItemMeta(trousseDeSoinM);

                if (itemStack == GPLayerListener.medecin) {

                    player.getInventory().setItem(3, trousseDeSoin);

                } else if (itemStack == GPLayerListener.grenadier) {

                    if(csUtils.giveWeapon(player, "grenade", "6")){
                        // ok ecrire un message
                    }else {
                        // Y a eu un probleme, ecrire un message
                    }

                }
                //else if() {

                //}


            }

            GGameCycle cycle = new GGameCycle(main);
            cycle.runTaskTimer(main, 0, 20);

            cancel();

        }

        timer--;

    }
}
