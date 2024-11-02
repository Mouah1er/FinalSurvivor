package fr.twah2em.survivor.commands.weapons;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.commands.internal.SurvivorCommand;
import fr.twah2em.survivor.commands.internal.SurvivorCommandCallback;
import fr.twah2em.survivor.commands.internal.SurvivorCommandMetadata;
import fr.twah2em.survivor.inventories.weapon.GiveWeaponInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveWeaponCommand extends SurvivorCommand {
    public GiveWeaponCommand(Main main) {
        super(new SurvivorCommandMetadata.SurvivorCommandMetadataBuilder("giveweapon")
                .withDescription("Permet de give une arme")
                .withUsage("giveweapon")
                .withPermission("survivor.giveweapon")
                .build());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ((Player) sender).openInventory(new GiveWeaponInventory().survivorInventory().getInventory());
    }

    @Override
    public SurvivorCommandCallback shouldBeExecuted(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) return SurvivorCommandCallback.notAPlayerCallback(commandSender);
        if (!commandSender.hasPermission("survivor.giveweapon")) return SurvivorCommandCallback.permissionCallback(commandSender);

        return SurvivorCommandCallback.emptyTrueCallback();
    }
}
