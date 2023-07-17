package ObjectsB;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import bomz.GUI;

public class Shop {
	public Location loc;
	public String shopType;
	public String name;
	public Shop(Location Loc, String Type, String Name){
		loc=Loc;
		shopType=Type;
		name=Name;
	}
	public void openShop(Player p){
		Inventory inv = Bukkit.createInventory(null, 9, name);
		int i=0;
		for(ShopItem sitem:GUI.sitems){
			if(sitem.shop.equals(shopType)){
				inv.setItem(i, sitem.shopItem(p));
				i++;
			}
		}
		p.openInventory(inv);
	}
}
