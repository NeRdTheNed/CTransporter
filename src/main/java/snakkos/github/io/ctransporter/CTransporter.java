package snakkos.github.io.ctransporter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CTransporter extends JavaPlugin {

	public final String COMMAND_PREFIX = "§aCTransporter§7>§f ";
	public final String COMMAND_ERROR_PREFIX = "§aCTransporter§7>§c ";
	public final String ERROR_NO_PERMISSIONS = COMMAND_ERROR_PREFIX + "You don't have the permissions!";
	
	public String OPTION_DISPLAY_NAME;	
	public Material OPTION_ITEM_TYPE;
		
	private FileConfiguration saveConfig = null;
	private File saveConfigFile = null;
	
	@Override
	public void onEnable() {
		
		this.saveDefaultConfig();
		
		this.OPTION_DISPLAY_NAME = this.getConfig().getString("configuration.display-name").replace("&", "§");
		this.OPTION_ITEM_TYPE = Material.getMaterial(this.getConfig().getString("configuration.item-type").toUpperCase());
				
		this.getCommand("ctransporter").setExecutor(new PluginCommands(this));
		
		this.getServer().getPluginManager().registerEvents(new PluginEvents(this), this);
		
		this.reloadSaveFile();		
	}
	
	@Override
	public void onDisable() {
		
		this.writeSaveFile();
		
	}
	
	public FileConfiguration getSaveFile() {
		if(saveConfig == null) {
			this.reloadSaveFile();
		}
		return saveConfig;
	}
	
	private void writeSaveFile() {
		if(saveConfig == null || saveConfigFile == null) {
			return;
		}
		try {
			this.getSaveFile().save(saveConfigFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Error saving data to file!", ex);
		}
	}
	
	private void reloadSaveFile() {
		if(saveConfigFile == null) {
			saveConfigFile = new File(getDataFolder(), "saved.yml");			
		}		
		saveConfig = YamlConfiguration.loadConfiguration(saveConfigFile);
	}
	
	
}
