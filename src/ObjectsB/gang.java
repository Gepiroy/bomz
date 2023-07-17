package ObjectsB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import utilsB.ItemUtil;

public class gang {
	public int hobos=0;
	public List<String> players = new ArrayList<>();
	public HashMap<String,Integer> drop = new HashMap<>();
	public int A=0;
	
	public void openGUI(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED+"Банда");
		inv.setItem(13, ItemUtil.create(Material.IRON_PICKAXE, 1, ChatColor.RED+"Действия банды", null));
		inv.setItem(0, ItemUtil.create(Material.SKELETON_SKULL, 1, ChatColor.RED+"Члены банды", null));
		p.openInventory(inv);
	}
	public void click(InventoryClickEvent e){
		
	}
}
