package ObjectsB;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import bomz.Events;
import bomz.GUI;
import utilsB.GepUtil;
import utilsB.ItemUtil;

public class business {
	public String name;
	public int subownprice;
	public int ownprice;
	public int sellown=0;
	public int sellowncoef;
	public int moneyPerDay;
	public String owner="";
	public business(String Name, int sub, int own, int sellcoef, int MPD){
		name=Name;
		subownprice=sub;
		ownprice=own;
		sellowncoef=sellcoef;
		moneyPerDay=MPD;
	}
	public Inventory bussGui(Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, name);
		if(!pi.hasBuss(name))inv.setItem(22, ItemUtil.create(Material.EMERALD, 1, ChatColor.DARK_GREEN+"Купить бизнес", new String[]{
				ChatColor.DARK_GREEN+"стать под-владельцем этого бизнеса."
				,ChatColor.LIGHT_PURPLE+"под-владельцы получают 10% прибыли."
				,ChatColor.GOLD+"Цена: "+GepUtil.boolCol(pi.money>=subownprice)+subownprice+" руб."
		}));
		else inv.setItem(22, ItemUtil.create(Material.EMERALD, 1, ChatColor.DARK_GREEN+"Купить бизнес", new String[]{
				ChatColor.DARK_GREEN+"стать одним из владельцев этого бизнеса."
				,ChatColor.LIGHT_PURPLE+"владельцы получают 25% прибыли."
				,ChatColor.LIGHT_PURPLE+"каждый влад. может бесплатно стать"
				,ChatColor.GREEN+"ГЛАВНЫМ ВЛАДЕЛЬЦЕМ"
				,ChatColor.AQUA+"(Прибыль x2, управление бизнесом.)"
				,ChatColor.RED+"Глав. владелец выдаётся на время."
				,ChatColor.GRAY+"(До 0:00 игр. времени или до выхода из игры)."
				,ChatColor.GOLD+"Цена: "+GepUtil.boolCol(pi.money>=ownprice)+ownprice+" руб."
		}));
		if(pi.hasBuss(name)&&pi.getBuss(name).own)inv.setItem(22, ItemUtil.create(Material.NETHER_STAR, 1, ChatColor.AQUA+"Макс. позиция в бизнесе!", new String[]{
				ChatColor.GREEN+"Вы уже стали владельцем этого бизнеса."
		}));
		if(pi.buss.contains("own"+name)&&sellown>0&&!owner.equals(p.getName()))inv.setItem(14, ItemUtil.create(Material.GOLDEN_HELMET, 1, ChatColor.GOLD+"Купить главное место", new String[]{
				ChatColor.GREEN+"Глав. владелец решил продать своё место за "+GepUtil.boolCol(pi.money>=sellown)+sellown+" руб."
				,ChatColor.RED+"Глав. владелец выдаётся на время."
				,ChatColor.GRAY+"(До 0:00 игр. времени или до выхода из игры)."
		}));
		if(sellown==0&&owner.equals(p.getName()))inv.setItem(14, ItemUtil.create(Material.GOLDEN_HELMET, 1, ChatColor.GOLD+"Продать главное место", new String[]{
				ChatColor.GREEN+"Вы можете выставить глав. место на продажу."
				,ChatColor.GOLD+"Другие владельцы смогут купить у вас это место."
				,ChatColor.YELLOW+"Вы станите обычным владельцем (который за "+ownprice+")."
		}));
		if(sellown>0&&owner.equals(p.getName()))inv.setItem(14, ItemUtil.create(Material.GOLDEN_HELMET, 1, ChatColor.GOLD+"Вы уже продаёте своё место.", new String[]{
				ChatColor.RED+"Вы продаёте своё место за "+sellown+" руб."
				,ChatColor.RED+"Изменить цену теперь нельзя."
		}));
		inv.setItem(26, ItemUtil.create(Material.PAPER, 1, ChatColor.BLUE+"Информация о бизнесе", new String[]{
				ChatColor.GREEN+"Стабильный доход: "+ChatColor.AQUA+moneyPerDay+" р./д."
				,ChatColor.GRAY+"(Не учитывая вашу долю и доход с клиентов)"
		}));
		return inv;
	}
	public void click(InventoryClickEvent e){
		Player p=(Player) e.getWhoClicked();
		PlayerInfo pi = Events.plist.get(p.getName());
		e.setCancelled(true);
		if(e.getCurrentItem().getType().equals(Material.EMERALD)){
			if(pi.hasBuss(name)&&!pi.getBuss(name).own){
				if(GUI.buy(ownprice, p)){
					pibus bus = pi.getBuss(name);
					bus.own=true;
					p.sendMessage(ChatColor.LIGHT_PURPLE+"Вы стали одним из владельцев данного бизнеса! Теперь вы получаете 25% прибыли этого места, и у вас есть шанс стать "+ChatColor.GREEN+"главным владельцем! "+ChatColor.YELLOW+"(возможность управлять бизнесом, получение x2 дохода.)");
					if(owner.equals(""))changeOwner();
				}
			}
			else{
				if(GUI.buy(subownprice, p)){
					pi.buss.add(new pibus(name, moneyPerDay));
					p.sendMessage(ChatColor.LIGHT_PURPLE+"Вы стали одним из под-владельцев данного бизнеса! Теперь вы получаете 10% прибыли этого места!");
				}
			}
			p.closeInventory();
		}
		return;
	}
	public void changeOwner(){
		ArrayList<String> canBeOwn = new ArrayList<>();
		for(Player p:Bukkit.getOnlinePlayers()){
			for(pibus bus:Events.plist.get(p.getName()).buss){
				if(bus.name.equals(name)&&bus.own){
					canBeOwn.add(p.getName());
					break;
				}
			}
		}
		if(canBeOwn.size()>0){
			sellown=0;
			if(Bukkit.getPlayer(owner)!=null)Bukkit.getPlayer(owner).sendMessage(ChatColor.RED+"Вы потеряли своё главное место в бизнесе '"+name+"'.");
			owner=canBeOwn.get(new Random().nextInt(canBeOwn.size()));
			Bukkit.getPlayer(owner).sendMessage(ChatColor.AQUA+"Вы заняли место главного в бизнесе '"+name+"'!");
			Bukkit.getPlayer(owner).sendMessage(ChatColor.LIGHT_PURPLE+"Теперь вы получаете в 2 раза больше дохода оттуда, можете управлять бизнесом, или же можете выставить на продажу своё место. Всё это делается в самом здании бизнеса.");
		}
	}
	public void addMoney(double m, double omgNeed, String omgMessage){
		for(Player pl:Bukkit.getOnlinePlayers()){
			PlayerInfo pip = Events.plist.get(pl.getName());
			if(pip.buss.contains(name)){
				int coef=10;
				if(pip.buss.contains("own"+name)){
					coef+=15;
					if(owner.equals(pl.getName())){
						coef+=25;
					}
				}
				pip.money+=m/100.00*coef;
				if(m>=omgNeed){
					omgMessage=omgMessage.replaceAll("/got/", GepUtil.CylDouble(m/100.00*coef, "#0.00"));
					pl.sendMessage(omgMessage);
				}
			}
		}
	}
	public void saveToConf(String st, FileConfiguration conf){
		conf.set(st+".sub", subownprice);
		conf.set(st+".own", ownprice);
		conf.set(st+".sell", sellowncoef);
		conf.set(st+".name", name);
		conf.set(st+".MPD", moneyPerDay);
	}
	public business(String st, FileConfiguration conf){
		subownprice=conf.getInt(st+".sub");
		ownprice=conf.getInt(st+".own");
		sellowncoef=conf.getInt(st+".sell");
		name=conf.getString(st+".name");
		moneyPerDay=conf.getInt(st+".MPD");
	}
}
