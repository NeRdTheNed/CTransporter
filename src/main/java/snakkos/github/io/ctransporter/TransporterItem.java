package snakkos.github.io.ctransporter;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TransporterItem {
	
	private CTransporter ctransporter;
	private ItemStack item;
	private String name;	
	private String owner;
	private boolean isEmpty = true;
	
	public TransporterItem(CTransporter ctransporter, String name) {
		this.ctransporter = ctransporter;
		this.item = new ItemStack(ctransporter.OPTION_ITEM_TYPE, 1);
		this.name = name;		
		
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add("§7" + name);
		
		itemMeta.setDisplayName(ctransporter.OPTION_DISPLAY_NAME);		
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
	}
	
	public TransporterItem(CTransporter ctransporter, Player player, ItemStack item) {
		this.ctransporter = ctransporter;
		this.item = item;
		List<String> lore = item.getItemMeta().getLore();
		this.name = lore.get(lore.size() - 1).substring(2);
		this.owner = player.getUniqueId().toString();
		if(item.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
			this.isEmpty = false;
		}
	}
	
	public TransporterItem(CTransporter ctransporter, Player player, String name) {
		this.ctransporter = ctransporter;
		this.item = new ItemStack(ctransporter.OPTION_ITEM_TYPE, 1);
		this.name = name;
		this.owner = player.getUniqueId().toString();
		this.isEmpty = false;
		
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add("§7" + name);
		
		itemMeta.setDisplayName(ctransporter.OPTION_DISPLAY_NAME);
		itemMeta.setLore(lore);
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);				
		item.setItemMeta(itemMeta);
	}
	
	/*
	 * Return the ItemStack.
	 */
	public ItemStack getItemStack() {
		return item;
	}
	
	/*
	 * Return the id of this transporter.
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * Return the owner's UUID of this transporter.
	 */
	public String getOwner() {
		return owner;
	}
	
	/*
	 * Return if this transporter is empty or not.
	 */
	public boolean isEmpty() {
		return isEmpty;
	}
	
	/*
	 * Give this transporter to a specified player.
	 */
	public void giveToPlayer(Player player) {	
		this.owner = player.getUniqueId().toString();
		player.getInventory().addItem(item);
	}	
	
	
	/*
	 * Change the state of this transporter to not empty.
	 */
	public void setNotEmpty() {
		this.isEmpty = false;
		ItemMeta itemMeta = this.item.getItemMeta();
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		this.item.setItemMeta(itemMeta);
	}
	
	/*
	 * Write the specified inventory to file.
	 */
	public void saveToFile(Inventory inventory) {
		
		for(int i = 0; i < inventory.getSize(); i++) {
			ItemStack currentItem = inventory.getItem(i);
			if(currentItem == null) {
				continue;
			}
			this.ctransporter.getSaveFile().set(owner + "." + name + "." + i, currentItem);
		}
	}
	
	/*
	 * Fill a chest
	 */
	public void fillChest(Chest chest) {
		
		Inventory inv = chest.getInventory();
		
		for(int i = 0; i < inv.getSize(); i++) {
			if(ctransporter.getSaveFile().contains(owner + "." + name + "." + i)) {
				inv.setItem(i, ctransporter.getSaveFile().getItemStack(owner + "." + name + "." + i));
			}
		}
	}
	
	/*
	 * Delete this transporter
	 */
	public void delete() {
		ctransporter.getSaveFile().set(owner + "." + name, null);
	}

}
