package snakkos.github.io.ctransporter;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PluginCommands implements CommandExecutor {

	private CTransporter ctransporter;
	
	public PluginCommands(CTransporter ctransporter) {
		this.ctransporter = ctransporter;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(label.equalsIgnoreCase("ctransporter")) {
			
			if(sender instanceof Player) {
				Player player = (Player) sender;
				
				//#########################
				
				if(args.length == 0) {
					if(player.hasPermission("ctransporter.cmd.help")) {
						this.displayHelp(player);
					}
					else {
						player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
					}
				}
				
				//#########################
				
				else if(args.length == 1) {	
					
					//-------------------------------
					
					if(args[0].equalsIgnoreCase("help")) {
						if(player.hasPermission("ctransporter.cmd.help")) {
							this.displayHelp(player);
						}
						else {
							player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
						}
					}
					
					//-------------------------------
					
					else if(args[0].equalsIgnoreCase("list")) {
						if(player.hasPermission("ctransporter.cmd.list")) {
							this.displayTransportersList(player);
						}
						else {
							player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
						}
					}	
					
					//-------------------------------
					
					else if(args[0].equalsIgnoreCase("new")) {
						if(player.hasPermission("ctransporter.cmd.new")) {
							player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "Error: You must specify a name! Use /new [name]");
							return false;			
						}
						else {
							player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
						}
					}	
					
					//-------------------------------
					
					else if(args[0].equalsIgnoreCase("get")) {
						if(player.hasPermission("ctransporter.cmd.get")) {
							player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "Error: You must specify the name! Use /get [name]");
						}
						else {
							player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
						}
					}	
					
					//-------------------------------
					
					else {
						if(player.hasPermission("ctransporter.cmd.help")) {
							player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "Error: Invalid command! Use /ctransporter help");
						}
						else {
							player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
						}
					}
				}				
				
				//#########################
				
				else if(args.length == 2) {
					
					if(args[0].equalsIgnoreCase("new")) {
						
						if(player.hasPermission("ctransporter.cmd.new")) {
							
							if(ctransporter.getSaveFile().contains(player.getUniqueId().toString() + "." + args[1])) {
								
								player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "A transporter with that name already exists!");
								
							} else {
								
								TransporterItem transporter = new TransporterItem(ctransporter, args[1]);
								
								if(player.getInventory().contains(transporter.getItemStack())) {
									player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "You already have a transporter with the same name in your inventory!");		
									return false;
								}
								
								transporter.giveToPlayer(player);
								player.sendMessage(ctransporter.COMMAND_PREFIX + "A new chest transporter named §6" + args[1] + "§f has been added to your inventory!");	
							}
						}
						else {
							
							player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
						}
					}
					
					//-------------------------------
					
					else if(args[0].equalsIgnoreCase("get")) {
						
						if(player.hasPermission("ctransporter.cmd.get")) {
							
							if(ctransporter.getSaveFile().contains(player.getUniqueId().toString() + "." + args[1])) {
								
								if(player.getInventory().firstEmpty() != -1) {
									
									TransporterItem transporter = new TransporterItem(ctransporter, player, args[1]);
									
									if(player.getInventory().contains(transporter.getItemStack())) {
										player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "You already have this transporter in your inventory!");		
										return false;
									}
									transporter.giveToPlayer(player);
								}
								else {
									player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "Error: Your inventory is full!");
								}
							}
							else {
								player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "Error: The specified transporter doesnt' exists!");
							}
						}
						else {
							player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
						}
					}
					
					//-------------------------------
					
					else {
						if(player.hasPermission("ctransporter.cmd.help")) {
							player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "Error: Invalid command! Use /ctransporter help");
						}
						else {
							player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
						}
					}
				} 
				
				//#########################
				
				else {
					if(player.hasPermission("ctransporter.cmd.help")) {
						player.sendMessage(ctransporter.COMMAND_ERROR_PREFIX + "Error: Invalid command! Use /ctransporter help");
					}
					else {
						player.sendMessage(ctransporter.ERROR_NO_PERMISSIONS);
					}
				}
				
				//#########################
			}
		}		
		return false;
	}

	/*
	 * Display the help page.
	 */
	private void displayHelp(Player player) {
		player.sendMessage("");
		player.sendMessage("§6----- [ §aCTransporter §6] -----");
		player.sendMessage("");
		player.sendMessage("§6/get [name]: §fGive an existing chest transporter.");
		player.sendMessage("§6/help: §fShow this page.");
		player.sendMessage("§6/list: §fShow the list of all your chest transporters.");
		player.sendMessage("§6/new [name]: §fGive an empty chest transporter.");
		player.sendMessage("");
	}
	
	/*
	 * Display the list of player's transporters.
	 */
	private void displayTransportersList(Player player) {
		player.sendMessage("§6----- Your saved transporters -----");
		player.sendMessage("");	
		
		if(ctransporter.getSaveFile().contains(player.getUniqueId().toString())) {
			Set<String> namesList = ctransporter.getSaveFile().getConfigurationSection(player.getUniqueId().toString()).getKeys(false); 			
			
			for(String name : namesList) {
				player.sendMessage("§6- " + name);
			}
		}	
		player.sendMessage("");
	}
	
}
