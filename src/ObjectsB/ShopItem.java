package ObjectsB;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import bomz.Events;
import bomz.GUI;
import utilsB.GepUtil;

public class ShopItem {
	public String shop="";
	public String priceType="money";
	public ItemStack item;
	public double price;
	public ShopItem(String Shop, ItemStack Item, double Price, String PriceType){
		price=Price;
		item=Item;
		shop=Shop;
		priceType=PriceType;
	}
	public ShopItem(String Shop, ItemStack Item, double Price){
		price=Price;
		item=Item;
		shop=Shop;
	}
	public ItemStack shopItem(Player p){
		ItemStack Item = new ItemStack(item);
		ItemMeta meta = Item.getItemMeta();
		List<String> ret = new ArrayList<>();
		if(meta.hasLore())for(String s:meta.getLore()){
			ret.add(s);
		}
		PlayerInfo pi = Events.plist.get(p.getName());
		if(priceType.equals("money"))ret.add(ChatColor.DARK_GREEN+"Цена: "+GepUtil.boolCol(pi.money>=price)+price+" руб.");
		if(priceType.equals("vodka"))ret.add(ChatColor.DARK_GREEN+"Цена: "+GepUtil.boolCol(GUI.CanBuyItem((int) price, Material.POTION, ChatColor.AQUA+"Бутылка с алкоголем", p))+(int)price+" бутылок с алкоголем.");
		meta.setLore(ret);
		Item.setItemMeta(meta);
		return Item;
	}
}
