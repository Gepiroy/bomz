package ObjectsB;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import bomz.Events;
import invsUtil.InvEvents;
import invsUtil.Invs;
import utilsB.GepUtil;
import utilsB.ItemUtil;

public class House {
	public String type;
	public Location door;
	public Location leaveLoc;
	public List<String> whoInHouse = new ArrayList<>();
	public String owner;
	public int rentTime=0;
	public int maxT=0;
	public int minT=0;
	public int price=0;
	public int storage=0;
	public int doorProtect=0;
	public int safeProtect=0;
	public int cleanClicks=0;
	
	public House(String Type){
		type=Type;
	}
	public House(Player p, int Price, int MinT, int MaxT, int Storage){
		owner=p.getName();
		maxT=MaxT;
		minT=MinT;
		price=Price;
		storage=Storage;
	}
	public House(FileConfiguration conf, String st){
		loadType(conf.getString("Houses."+st+".type"), conf);
		type=conf.getString("Houses."+st+".type");
		door=GepUtil.getLocFromConf(conf, "Houses."+st+".door");
		leaveLoc=GepUtil.getLocFromConf(conf, "Houses."+st+".leave");
		owner=conf.getString("Houses."+st+".own");
		rentTime=conf.getInt("Houses."+st+".time");
	}
	void loadType(String st,FileConfiguration conf){
		st="Types."+st;
		maxT=conf.getInt(st+".maxT");
		minT=conf.getInt(st+".minT");
		price=conf.getInt(st+".price");
		storage=conf.getInt(st+".storage");
		doorProtect=conf.getInt(st+".doorProtect");
		safeProtect=conf.getInt(st+".safeProtect");
		cleanClicks=conf.getInt(st+".CC");
	}
	public void SaveType(String st,FileConfiguration conf){
		st="Types."+st;
		conf.set(st+".maxT",maxT);
		conf.set(st+".minT",minT);
		conf.set(st+".price",price);
		conf.set(st+".storage",storage);
		conf.set(st+".doorProtect",doorProtect);
		conf.set(st+".safeProtect",safeProtect);
		conf.set(st+".CC",cleanClicks);
	}
	public void SaveHouse(String st,FileConfiguration conf){
		conf.set(st+".type", type);
		conf.set(st+".own", owner);
		conf.set(st+".time", rentTime);
		GepUtil.saveLocToConf(conf, st+".door", door);
		GepUtil.saveLocToConf(conf, st+".leave", leaveLoc);
	}
	public void openGUI(Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Дом");
		inv.setItem(4, ItemUtil.create(Material.EXPERIENCE_BOTTLE, 1, ChatColor.AQUA+"Информация", new String[]{
				ChatColor.GOLD+"Температура: "+ChatColor.BLUE+minT+ChatColor.WHITE+" - "+ChatColor.GOLD+maxT
				,ChatColor.WHITE+"Размер склада: "+ChatColor.AQUA+storage
				,ChatColor.BLUE+"Защита двери: "+ChatColor.AQUA+doorProtect
				,ChatColor.BLUE+"Защита склада: "+ChatColor.AQUA+safeProtect
				,ChatColor.AQUA+"Мытьё: "+ChatColor.AQUA+cleanClicks
				
				}));
		if(owner==null){
			inv.setItem(13, ItemUtil.create(Material.EMERALD, 1, ChatColor.GREEN+"Арендовать на 7 дней", new String[]{
					ChatColor.DARK_GREEN+"Цена аренды: "+GepUtil.boolCol(pi.money>=price)+price+" руб."
					,ChatColor.GREEN+"Цена продлений: "+GepUtil.boolCol(pi.money>=price/100.00*90)+GepUtil.CylDouble(price/100.00*90,"#0.00")+" руб./7 дней."
			}));
			inv.setItem(14, ItemUtil.create(Material.EMERALD, 1, ChatColor.GREEN+"Арендовать на 30 дней", new String[]{
					ChatColor.DARK_GREEN+"Цена аренды: "+GepUtil.boolCol(pi.money>=price*4)+price*4+" руб."
					,ChatColor.GREEN+"Цена продлений: "+GepUtil.boolCol(pi.money>=price*4/100.00*75)+GepUtil.CylDouble(price*4/100.00*75,"#0.00")+" руб./30 дней."
			}));
		}
		else{
			if(owner.equals(p.getName())){
				inv.setItem(13, ItemUtil.create(Material.IRON_DOOR, ChatColor.GREEN+"Войти в дом"));
				inv.setItem(26, ItemUtil.create(Material.EMERALD, 1, ChatColor.RED+"Продать хату", new String[]{ChatColor.DARK_GREEN+"Вы получите за это "+ChatColor.GREEN+price/10.0+" руб."}));
			}
			else inv.setItem(13, ItemUtil.create(Material.BARRIER, ChatColor.RED+"Дом занят игроком "+ChatColor.GOLD+owner));
			if(Bukkit.getPlayer(owner)==null) inv.setItem(22, ItemUtil.create(Material.IRON_PICKAXE, ChatColor.RED+"Ограбить"));
		}
		p.openInventory(inv);
	}
	public void BedGUI(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Что делать дома?");
		inv.setItem(13, ItemUtil.create(Material.WATER_BUCKET, 1, ChatColor.AQUA+"Мыться", null));
		inv.setItem(15, ItemUtil.create(Material.FURNACE, 1, ChatColor.GOLD+"Готовить", null));
		p.openInventory(inv);
	}
	public void teleportHome(Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		Location loc = door.clone();
		loc.setX(loc.getX()+0.5);
		loc.setY(loc.getY()-1);
		loc.setZ(loc.getZ()+0.5);
		loc.setPitch(p.getLocation().getPitch());
		loc.setYaw(p.getLocation().getYaw());
		p.teleport(loc);
		whoInHouse.add(p.getName());
		pi.toggleBool("inHouse", true);
	}
	public void click(InventoryClickEvent e){
		e.setCancelled(true);
		Player p=(Player) e.getWhoClicked();
		PlayerInfo pi = Events.plist.get(p.getName());
		if(e.getClickedInventory()==e.getView().getTopInventory()&&e.getView().getTitle().equals(ChatColor.GOLD+"Что делать дома?")){
			if(e.getCurrentItem().getType().equals(Material.WATER_BUCKET)){
				GepUtil.HashMapReplacer(pi.waits, "clean", cleanClicks, false, true);
				Invs.open(p, InvEvents.clean);
			}
		}
	}
}
