package ObjectsB;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import bomz.main;
import utilsB.ActionBar;
import utilsB.ItemUtil;
import utilsB.TextUtil;

public class PlayerInfo {
	public final Player p;
	public int lvl=1;
	public int exp=0;
	public double money = 0.0;
	public int tepl=0;
	public int prestige=0;
	public int comfort=0;
	public int dirt=0;
	public double hunger=1000;
	int psy=1000;
	public HashMap<String,Integer> waits = new HashMap<>();
	public ArrayList<pibus> buss = new ArrayList<>();
	public Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GOLD+"Домашний склад");
	public String work="";
	public House house;
	public BankInfo bank = new BankInfo();
	public HashMap<String,Perk> perks = new HashMap<>();
	public HashMap<String,Integer> timers = new HashMap<>();
	public HashMap<String,Integer> fastTimers = new HashMap<>();
	public ArrayList<String> bools = new ArrayList<>();
	public Location lastClickLoc;
	public Material lastWalkMat=Material.STONE;
	public String lastInv;
	public KeyBreak key = null;
	public gang g = null;
	public String learnMessage;
	public String learnTitle;
	public String sleep="";
	public String pname;
	public BodyTemp temp;
	public int homeNum=0;
	public ArrayList<disease> diseases = new ArrayList<>();
	
	public PlayerInfo(String name){
		pname=name;
		p=Bukkit.getPlayer(name);
		temp = new BodyTemp(p);
	}
	public PlayerInfo(FileConfiguration conf, String name){
		pname=name;
		p=Bukkit.getPlayer(name);
		temp = new BodyTemp(p);
		for(House h:main.houses){
			if(h.owner!=null&&h.owner.equals(name))house=h;
		}
		lvl=conf.getInt("Lvl");
		exp=conf.getInt("Exp");
		money=conf.getDouble("Money");
		dirt=conf.getInt("Dirt");
		hunger=conf.getDouble("Hunger");
		if(conf.contains("Perks")){
			if(conf.contains("Perks"))for(String st:conf.getConfigurationSection("Perks").getKeys(false)){
				perks.put(st, new Perk(conf.getInt("Perks."+st+".lvl"),conf.getInt("Perks."+st+".toNext"),conf.getInt("Perks."+st+".max")));
			}
		}
		bank.depos=conf.getInt("Bank.depos");
		bank.canTake=conf.getDouble("Bank.canTake");
		bank.deposDay=conf.getInt("Bank.deposDay");
		bank.creditDay=conf.getInt("Bank.creditDay");
		bank.creditPay=conf.getDouble("Bank.creditPay");
		bank.daysToCredit=conf.getInt("Bank.daysToCredit");
		if(conf.contains("Items"))for(String st:conf.getConfigurationSection("Items").getKeys(false)){
			inv.setItem(Integer.parseInt(st), conf.getItemStack("Items."+st));
		}
		if(conf.contains("Buss"))for(String st:conf.getConfigurationSection("Buss").getKeys(false)){
			buss.add(new pibus("Buss."+st, conf));
		}
		if(conf.contains("Timers"))for(String st:conf.getConfigurationSection("Timers").getKeys(false)){
			timers.put(st, conf.getInt("Timers."+st));
		}
		if(conf.contains("Waits"))for(String st:conf.getConfigurationSection("Waits").getKeys(false)){
			waits.put(st, conf.getInt("Waits."+st));
		}
		if(conf.contains("diseases"))for(String st:conf.getConfigurationSection("diseases").getKeys(false)){
			diseases.add(new disease(conf, "diseases."+st, st));
		}
	}
	public void save(FileConfiguration conf){
		conf.set("Lvl",lvl);
		conf.set("Exp",exp);
		conf.set("Money",money);
		conf.set("Dirt",dirt);
		conf.set("Hunger",hunger);
		for(String st:perks.keySet()){
			Perk perk=perks.get(st);
			conf.set("Perks."+st+".lvl",perk.lvl);
			conf.set("Perks."+st+".toNext",perk.toNext);
		}
		conf.set("lastLeave", new Date().getTime());
		conf.set("Items",null);
		conf.set("Bank.depos",bank.depos);
		conf.set("Bank.canTake",bank.canTake);
		conf.set("Bank.deposDay",bank.deposDay);
		conf.set("Bank.creditDay",bank.creditDay);
		conf.set("Bank.creditPay",bank.creditPay);
		conf.set("Bank.daysToCredit",bank.daysToCredit);
		for(int i=0;i<54;i++){
			if(inv.getItem(i)!=null&&!inv.getItem(i).getType().equals(Material.IRON_BARS)){
				conf.set("Items."+i,inv.getItem(i));
			}
		}
		for(pibus bus:buss){
			bus.saveToConf("Buss."+bus.name, conf);
		}
		conf.set("Timers",null);
		for(String st:timers.keySet()){
			conf.set("Timers."+st,timers.get(st));
		}
		conf.set("Waits",null);
		for(String st:waits.keySet()){
			conf.set("Waits."+st,waits.get(st));
		}
		conf.set("diseases", null);
		for(disease d:diseases){
			d.save(conf, "diseases."+d.name);
		}
	}
	public void addPerk(Player p, String displayName, String name, int exp, int toUp){
		toUp*=RealPerkLvl(name);
		if(!perks.containsKey(name))perks.put(name, new Perk(1,toUp, toUp));
		Perk perk = perks.get(name);
		if(perks.get(name).addExp(exp, toUp)>=0){
			ActionBar actionBar = new ActionBar(ChatColor.LIGHT_PURPLE + "Прокачено! ["+displayName+ChatColor.LIGHT_PURPLE+"]");
	        actionBar.sendToPlayer(p);
			p.sendTitle(ChatColor.AQUA+"Перк прокачен!", displayName+ChatColor.YELLOW+"("+perkLvl(name)+")", 10, 50, 25);
			p.sendMessage(ChatColor.GREEN+"Вы прокачали перк "+displayName+ChatColor.GREEN+" до уровня "+ChatColor.YELLOW+perkLvl(name));
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 0);
		}
		else{
			String st=ChatColor.LIGHT_PURPLE+"["+displayName+ChatColor.LIGHT_PURPLE+"]";
			int gr=(int) ((perk.max-perk.toNext)/(perk.max/100.00)/5);
			for(int i=0;i<20;i++){
				if(gr>0)st+=ChatColor.AQUA+"=";
				else st+=ChatColor.BLUE+"-";
				gr--;
			}
			ActionBar actionBar = new ActionBar(st);
	        actionBar.sendToPlayer(p);
		}
	}
	public int perkLvl(String name){
		int ret = 1;
		if(!perks.containsKey(name))return 1;
		ret=perks.get(name).lvl;
		if(timers.containsKey("alco")){
			if(name.equals("hot"))ret+=10;
			if(name.equals("cold"))ret+=10;
			if(name.equals("hands"))ret-=20;
		}
		return ret;
	}
	public int RealPerkLvl(String name){
		if(!perks.containsKey(name))return 1;
		return perks.get(name).lvl;
	}
	public boolean toggleBool(String st){
		return toggleBool(st, !bools.contains(st));
	}
	public boolean toggleBool(String st, boolean set){
		if(bools.contains(st)&&!set){
			bools.remove(st);
		}
		else if(!bools.contains(st)&&set){
			bools.add(st);
		}
		return bools.contains(st);
	}
	public boolean hasBuss(String st){
		for(pibus bus:buss){
			if(bus.name.equals(st))return true;
		}
		return false;
	}
	public pibus getBuss(String st){
		for(pibus bus:buss){
			if(bus.name.equals(st))return bus;
		}
		return null;
	}
	public int toLvl(){
		return (int) (50*Math.pow(2, lvl));
	}
	public void addExp(int am){
		Player p=Bukkit.getPlayer(pname);
		exp+=am;
		p.sendMessage(ChatColor.AQUA+"+"+am+" exp");
		if(exp>=toLvl()){
			exp-=toLvl();
			lvl++;
			p.sendMessage(ChatColor.GREEN+"Уровень поднят!");
			p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 10, 1);
		}
	}
	public boolean addDisease(disease d){
		for(disease dis:diseases){
			if(dis.name.equals(d.name)){
				dis.toRemove+=d.toRemove/2;
				dis.preStage/=2;
				return false;
			}
		}
		diseases.add(d);
		return true;
	}
	public void diseaseGUI(){
		Player p=Bukkit.getPlayer(pname);
		Inventory inv=Bukkit.createInventory(null, 27, ChatColor.RED+"Болезни");
		if(diseases.size()<=0){
			inv.setItem(13, ItemUtil.create(Material.DIAMOND, 1, 0, ChatColor.GREEN+"Болезней нет!", new String[]{
					ChatColor.GREEN+"Здоровый, здоровенный бомж!"
			}, null, 0));
		}else{
			int i=0;
			for(disease d:diseases){
				inv.setItem(i, d.infoItem(this));
				i+=2;
				if(i>26)break;
			}
		}
		p.openInventory(inv);
	}
	public Location hloc(){
		return new Location(Bukkit.getWorld("world"),homeNum*1000,80,homeNum/100*1000);
	}
	public double addPsy(int i){
		if(i==0)return psy;
		int pre=psy;
		double coef=1;
		if(i<0){
			coef=0.25*lvl;
		}
		psy+=i*coef;
		Player p=Bukkit.getPlayer(pname);
		if(pre>500&&psy<=500){
			p.sendMessage(ChatColor.RED+"Что-то мне грустно... Надо развлечься.");
		}
		if(pre<=500&&psy>500){
			p.sendMessage(ChatColor.AQUA+"Ради этого стоит жить!");
		}
		return psy;
	}
	public double getPsy(){
		return psy;
	}
	
	public List<String> notifs = new ArrayList<>();
	
	public void notif(String code, String pref, String mes){
		if(notifs.contains(code))return;
		if(mes!=null)TextUtil.mes(p, pref, mes);
		notifs.add(code);
	}
	
	public void unnotif(String code, String pref, String mes){
		if(!notifs.contains(code))return;
		if(mes!=null)TextUtil.mes(p, pref, mes);
		notifs.remove(code);
	}
}
