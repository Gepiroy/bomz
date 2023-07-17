package ObjectsB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import bomz.Events;
import bomz.GUI;
import utilsB.GepUtil;
import utilsB.ItemUtil;

public class Shaurma {
	public Location loc;
	public double price=0.5;
	public int poison=0;
	public business bus;
	public List<ShopItem> sitems = new ArrayList<>();
	public Shaurma(Location Loc, double Price, int sub, int sell, int Poison, String Name){
		loc=Loc;
		price=Price;
		poison=Poison;
		bus=new business(Name, sub, sub*3, sell, sub/2);
	}
	
	
	
	public void openShop(Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		Inventory inv = bus.bussGui(p);
		inv.setItem(13, ItemUtil.create(Material.COOKED_PORKCHOP, 1, ChatColor.GOLD+"Шаурма", new String[]{
				ChatColor.DARK_GREEN+"Токсичность: "+ChatColor.RED+poison+"%"
				,ChatColor.DARK_GREEN+"Цена: "+GepUtil.boolCol(pi.money>=price)+price+" руб."
		}));
		if(bus.owner.equals(p.getName()))inv.setItem(0, ItemUtil.create(Material.FURNACE, 1, ChatColor.GOLD+"Добавить шаурму", new String[]{
				ChatColor.DARK_GREEN+"Вы ПОКА ЧТО НЕ можете добавлять в магазин шаурму."
		}));
		p.openInventory(inv);
	}
	public void changeOwner(){
		ArrayList<String> canBeOwn = new ArrayList<>();
		for(Player p:Bukkit.getOnlinePlayers()){
			for(pibus b:Events.plist.get(p.getName()).buss){
				if(b.name.equals(bus.name)&&b.own&&!p.getName().equals(bus.owner)){
					canBeOwn.add(p.getName());
					break;
				}
			}
		}
		if(canBeOwn.size()>0){
			bus.ownprice=0;
			if(Bukkit.getPlayer(bus.owner)!=null)Bukkit.getPlayer(bus.owner).sendMessage(ChatColor.RED+"Вы потеряли своё главное место в пункте сдачи бутылок "+ChatColor.GRAY+GepUtil.locInfo(loc)+ChatColor.RED+".");
			bus.owner=canBeOwn.get(new Random().nextInt(canBeOwn.size()));
			Bukkit.getPlayer(bus.owner).sendMessage(ChatColor.AQUA+"Вы заняли место главного в пункте сдачи бутылок "+ChatColor.GRAY+GepUtil.locInfo(loc)+ChatColor.AQUA+"!");
			Bukkit.getPlayer(bus.owner).sendMessage(ChatColor.LIGHT_PURPLE+"Теперь вы получаете в 2 раза больше дохода оттуда, можете изменять цены на бутылки, или же можете выставить на продажу своё место. Всё это делается в самом пункте.");
		}
	}
	public void click(InventoryClickEvent e){
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		if(e.getCurrentItem().getType().equals(Material.COOKED_PORKCHOP)){
			if(GUI.buy(price, p)){
				p.getInventory().addItem(ItemUtil.create(Material.COOKED_PORKCHOP, 1, ChatColor.GOLD+"Шаурма", new String[]{
				ChatColor.DARK_GREEN+"Токсичность: "+ChatColor.RED+poison+"%",
				ChatColor.GOLD+"Теплота: "+ChatColor.YELLOW+"10"
				}));
				bus.addMoney(price, price, "Шавуха вам принесла сеи дары в /got/ рубасов!");
			}
		}
		else bus.click(e);
	}
}
