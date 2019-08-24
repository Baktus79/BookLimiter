package no.vestlandetmc.BookLimiter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BookEvent implements Listener {

	final int pageLimit = BookLimiterPlugin.getInstance().getConfig().getInt("PageLimit");
	final int characterLimit = BookLimiterPlugin.getInstance().getConfig().getInt("CharacterLimit");
	final boolean character = BookLimiterPlugin.getInstance().getConfig().getBoolean("CharacterCount");

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void bookEvent(PlayerEditBookEvent e) {
		final Player player = e.getPlayer();

		final int charCount = e.getNewBookMeta().getPages().toString().replaceAll("[\\[\\]\\,\\ ]", "").length();
		final int pageCount = e.getNewBookMeta().getPageCount();
		final String warningChar = BookLimiterPlugin.getInstance().getConfig().getString("WarningCharacter").replace("%maxchar%", characterLimit + "").replace("%maxpages%", pageLimit + "").replace("%charcount%", charCount + "").replace("%pagecount%", pageCount + "").replace("%player%", player.getDisplayName());
		final String warningPage = BookLimiterPlugin.getInstance().getConfig().getString("WarningPages").replace("%maxchar%", characterLimit + "").replace("%maxpages%", pageLimit + "").replace("%charcount%", charCount + "").replace("%pagecount%", pageCount + "").replace("%player%", player.getDisplayName());
		final String notifyPage = BookLimiterPlugin.getInstance().getConfig().getString("NotifyMessagePage").replace("%maxchar%", characterLimit + "").replace("%maxpages%", pageLimit + "").replace("%charcount%", charCount + "").replace("%pagecount%", pageCount + "").replace("%player%", player.getDisplayName());
		final String notifyChar = BookLimiterPlugin.getInstance().getConfig().getString("NotifyMessageChar").replace("%maxchar%", characterLimit + "").replace("%maxpages%", pageLimit + "").replace("%charcount%", charCount + "").replace("%pagecount%", pageCount + "").replace("%player%", player.getDisplayName());

		if(character) {
			if(characterLimit < charCount) {
				e.setCancelled(true);
				player.getInventory().setItem(e.getSlot(), new ItemStack(Material.WRITABLE_BOOK));
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', warningChar)));
				notifyWarning(notifyChar);
			}
		}
		else {
			if(pageLimit < pageCount) {
				e.setCancelled(true);
				player.getInventory().setItem(e.getSlot(), new ItemStack(Material.WRITABLE_BOOK));
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', warningPage)));
				notifyWarning(notifyPage);
			}
		}
	}

	@EventHandler
	public void offHandCancel(InventoryClickEvent e) {
		final HumanEntity player = e.getWhoClicked();
		final String warningOffHand = BookLimiterPlugin.getInstance().getConfig().getString("WarningOffHand").replace("%maxchar%", characterLimit + "").replace("%maxpages%", pageLimit + "").replace("%player%", player.getName());

		if(e.getCursor().getType() == Material.WRITABLE_BOOK && e.getSlot() == 40) {
			e.setCancelled(true);
			player.getInventory().addItem(new ItemStack(Material.WRITABLE_BOOK));
			e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',warningOffHand));
		}
	}

	@EventHandler
	public void itemHandSwap(PlayerSwapHandItemsEvent e) {
		final Player player = e.getPlayer();
		final String warningOffHand = BookLimiterPlugin.getInstance().getConfig().getString("WarningOffHand").replace("%maxchar%", characterLimit + "").replace("%maxpages%", pageLimit + "").replace("%player%", player.getName());

		if(e.getOffHandItem().getType() == Material.WRITABLE_BOOK) {
			e.setCancelled(true);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', warningOffHand));
		}
	}

	private void notifyWarning(String notify) {
		for (final Player p : Bukkit.getOnlinePlayers()) {
			if(p.hasPermission("booklimiter.notify")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', notify));
			}
		}
	}
}
