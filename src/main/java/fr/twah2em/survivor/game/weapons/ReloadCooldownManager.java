package fr.twah2em.survivor.game.weapons;

import fr.twah2em.survivor.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReloadCooldownManager {
    private final Main main;
    private final Map<UUID, Integer> cooldowns;

    public ReloadCooldownManager(Main main) {
        this.main = main;
        this.cooldowns = new HashMap<>();
    }

    public int playerCooldown(UUID player) {
        return cooldowns.getOrDefault(player, 0);
    }

    public void playerCooldown(UUID player, int time) {
        if (time == -1) {
            cooldowns.remove(player);
            return;
        }

        cooldowns.put(player, time);
    }

    public void cooldownRunnable(Player player, int time, Weapon weapon, ItemStack itemStack) {
        playerCooldown(player.getUniqueId(), time);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> {
            weapon.ammoInClip(weapon.clipSize(), itemStack);
            weapon.totalRemainingAmmo(weapon.totalRemainingAmmo(itemStack) - weapon.clipSize(), itemStack);

            final ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(
                    Component.text("§3" + weapon.name() + " §6- §f" + weapon.ammoInClip(itemStack) + "§e/§7" + weapon.totalRemainingAmmo(itemStack)));
            itemStack.setItemMeta(itemMeta);

            player.sendActionBar(Component.empty());
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.7f, 2.0f);

            playerCooldown(player.getUniqueId(), -1);
        }, time);
    }
}
