package fr.bibouh.survivor.tasks;

import fr.bibouh.survivor.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GGameCycle extends BukkitRunnable {

    private Main main;
    private int timer = 10;

    public GGameCycle(Main main) {
        this.main = main;
    }

    @Override
    public void run() {

        if (timer == 10) {

            Bukkit.broadcastMessage("§aLes zombies commenceront à vous envahir dans 10 secondes...");

        }

        if (timer == 0) {

            cancel();
            Bukkit.broadcastMessage("§aLes zombies commencent à vous envahir !");
        }

        timer--;

    }
}
