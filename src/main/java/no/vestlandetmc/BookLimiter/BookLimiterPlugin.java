package no.vestlandetmc.BookLimiter;

import org.bukkit.plugin.java.JavaPlugin;

public class BookLimiterPlugin extends JavaPlugin {

	public static BookLimiterPlugin instance;

	public static BookLimiterPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		this.getServer().getPluginManager().registerEvents(new BookEvent(), this);

		saveDefaultConfig();
	}

	@Override
	public void onDisable() {

	}
}
