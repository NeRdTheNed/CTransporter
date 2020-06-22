package snakkos.github.io.ctransporter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PluginEvents implements Listener {
	
	private CTransporter ctransporter;
	
	/*
	 * Constructor
	 */
	public PluginEvents(CTransporter ctransporter) {
		this.ctransporter = ctransporter;
	}
	
	@EventHandler
	public void chestPickupEvent(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		// If the player doesn't have the permission, return.
		if(!player.hasPermission("ctransporter.chest.pickup")) {
			return;
		}
						
		// If the player right clicked on a block.
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			ItemStack item = event.getItem();
			Block block = event.getClickedBlock();
			Chest chest;
			
			// Check if the used item is a chest transporter
			if(item != null && item.getType() == ctransporter.OPTION_ITEM_TYPE && item.getItemMeta().getDisplayName().equalsIgnoreCase(ctransporter.OPTION_DISPLAY_NAME)) {
							
				TransporterItem transporter = new TransporterItem(ctransporter, player, item);
				
				// Check if the chest transporter is empty
				if(transporter.isEmpty()) {
					
					// Check if the clicked block is a chest.
					if(block.getType() == Material.CHEST) {						
						chest = (Chest) block.getState();	
						
						Inventory blockInventory = chest.getInventory();
						boolean isChestEmpty = true;
						
						// Check if the chest is empty
						for(int i = 0; i < blockInventory.getSize(); i++) {
							if (blockInventory.getItem(i) != null) {
								isChestEmpty = false;
								break;
							}
						}								
						
						if(isChestEmpty) {
							player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "You can't move empty chests!");
							return;
						}
						
						transporter.saveToFile(chest.getBlockInventory());				
						chest.getInventory().clear();
						block.setType(Material.AIR);
						transporter.setNotEmpty();
					}
					// If is not empty
				} else {													
					
					Block newBlock = null;
					
					switch(event.getBlockFace()) {					
					case DOWN:
						newBlock = player.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());					
						break;
					case UP:
						newBlock = player.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ());
						break;
					case NORTH:
						newBlock = player.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() - 1);
						break;
					case SOUTH:
						newBlock = player.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() + 1);
						break;
					case EAST:
						newBlock = player.getWorld().getBlockAt(block.getX() + 1, block.getY(), block.getZ());
						break;
					case WEST:
						newBlock = player.getWorld().getBlockAt(block.getX() - 1, block.getY(), block.getZ());
						break;
					default:
						break;
					}
					
					newBlock.setType(Material.CHEST);
					
					BlockData data = newBlock.getBlockData();
					((Directional)data).setFacing(getChestFacing(player));
					newBlock.setBlockData(data);				
										
					transporter.fillChest((Chest)newBlock.getState());					
					transporter.delete();
					player.getInventory().remove(item);					
				}
			}			
		}
		
	}
	
	/*
	 * Calculate the chest direction based on player yaw
	 */
	private BlockFace getChestFacing(Player player) {
		
		float playerYaw = Math.abs(player.getLocation().getYaw());		
		
		if((playerYaw >= 315 && playerYaw <= 360) || (playerYaw >= 0 && playerYaw < 45)) {
			return BlockFace.NORTH;
		}
		else if(playerYaw >= 45 && playerYaw < 135) {
			return BlockFace.EAST;
		}
		else if(playerYaw >= 135 && playerYaw < 225) {
			return BlockFace.SOUTH;
		}
		else if(playerYaw >= 225 && playerYaw < 315) {
			return BlockFace.WEST;
		}
	
		return null;
	}

}
