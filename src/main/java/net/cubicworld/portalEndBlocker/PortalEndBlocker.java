package net.cubicworld.portalEndBlocker;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.PortalType;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PortalEndBlocker extends JavaPlugin implements Listener {

    private boolean cancel = true;

    @EventHandler
    public void portalEnter(EntityPortalEnterEvent event) {
        if (event.getPortalType() == PortalType.ENDER) {
            event.setCancelled(cancel);
        }
    }

    @Override
    public void onEnable() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(Commands.literal("toggleend").executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                if (sender.hasPermission("portal-end-blocker.activate")) {
                    cancel = !cancel;
                    sender.sendMessage("SUCCESS, end portals are now %s".formatted(cancel ? "DEACTIVATED" : "ACTIVATED"));
                }
                return Command.SINGLE_SUCCESS;
            }).build());
        });
        getServer().getPluginManager().registerEvents(this, this);
    }
}
