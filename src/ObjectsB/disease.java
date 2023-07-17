package ObjectsB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import utilsB.GepUtil;
import utilsB.ItemUtil;

public class disease {
	public String name;
	public int toRemove=10;
	public int preStage=100;
	public boolean searched=false;
	public String stage="Ошибка";
	public int toNextHeal=0;
	public HashMap<String,Integer> basics = new HashMap<>();
	public disease(){}
	public disease(String Name, int rem, int pre){
		name=Name;
		toRemove=rem;
		preStage=pre;
		List<String> startSearched = new ArrayList<>();
		startSearched.add("Простуда");
		startSearched.add("Отравление");
		startSearched.add("Промокший");
		if(startSearched.contains(name))searched=true;
	}
	public void stage(PlayerInfo pi){
		basics.clear();
		if(preStage>0){
			preStage--;
		}
		if(name.equals("Диарея")){
			if(preStage>0){
				stage=ChatColor.AQUA+"начало";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 25, false, true);
			}
			if(toRemove<=240){
				stage=ChatColor.GREEN+"выздоравление";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 20, false, true);
			}
			else if(toRemove<=1200){
				stage=ChatColor.GOLD+"активная";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 50, false, true);
				GepUtil.HashMapReplacer(basics, "speed", -10, false, true);
			}
			else{
				stage=ChatColor.RED+"тяжёлая";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 125, false, true);
				GepUtil.HashMapReplacer(basics, "speed", -25, false, true);
			}
		}
		if(name.equals("Гастрит")){
			if(preStage>0){
				stage=ChatColor.AQUA+"начало";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 25, false, true);
			}
			if(toRemove<=480){
				stage=ChatColor.GREEN+"выздоравление";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 20, false, true);
			}
			else if(toRemove<=2400){
				stage=ChatColor.GOLD+"активный";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 50, false, true);
			}
			else{
				stage=ChatColor.RED+"тяжёлый";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 125, false, true);
			}
		}
		if(name.equals("Глисты")){
			if(preStage>0){
				stage=ChatColor.AQUA+"начало";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 35, false, true);
			}
			if(toRemove<=480){
				stage=ChatColor.GREEN+"выздоравление";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 65, false, true);
			}
			else if(toRemove<=2400){
				stage=ChatColor.GOLD+"активные";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 100, false, true);
			}
			else{
				stage=ChatColor.RED+"тяжёлые";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 250, false, true);
			}
		}
		if(name.equals("Отравление")){
			if(preStage>0){
				stage=ChatColor.AQUA+"начало";
				GepUtil.HashMapReplacer(basics, "speed", -5, false, true);
			}
			if(toRemove<=100){
				stage=ChatColor.GREEN+"выздоравление";
				GepUtil.HashMapReplacer(basics, "speed", -5, false, true);
			}
			else if(toRemove<=200){
				stage=ChatColor.GOLD+"активное";
				GepUtil.HashMapReplacer(basics, "speed", -10, false, true);
				basics.put("rvota", 35);
			}
			else{
				stage=ChatColor.RED+"тяжёлое";
				GepUtil.HashMapReplacer(basics, "speed", -25, false, true);
				basics.put("rvota", 100);
			}
		}
		if(name.equals("Промокший")){
			if(preStage>0){
				stage=ChatColor.AQUA+"только что";
				GepUtil.HashMapReplacer(basics, "speed", 1, false, true);
				basics.put("temp", -1);
			}
			if(toRemove<=60){
				stage=ChatColor.GREEN+"почти высох";
				GepUtil.HashMapReplacer(basics, "speed", -5, false, true);
				basics.put("temp", -2);
			}
			else if(toRemove<=120){
				stage=ChatColor.GOLD+"высыхает";
				GepUtil.HashMapReplacer(basics, "speed", -10, false, true);
				basics.put("temp", -10);
			}
			else{
				stage=ChatColor.RED+"насквозь";
				GepUtil.HashMapReplacer(basics, "speed", -25, false, true);
				basics.put("temp", -20);
			}
		}
		if(basics.size()>0){
			if(pi.lvl<3)for(String st:basics.keySet()){
				GepUtil.HashMapReplacer(basics, st, (int) (basics.get(st)*(0.5+pi.lvl*0.25)), false, true);
			}
		}else stage="Ошибка! n="+toRemove;
	}
	public ItemStack healItem(PlayerInfo pi){
		ItemStack item = infoItem(pi);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"Лечить болезнь '"+ChatColor.DARK_GREEN+name+ChatColor.GREEN+"'");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.BLUE+"Цена лечения: "+GepUtil.boolCol(pi.money>=healPrice())+healPrice());
		lore.add(GepUtil.boolString(ChatColor.GREEN+"Можно лечить!", ChatColor.GOLD+"Повторное лечение через "+ChatColor.YELLOW+GepUtil.timeStr(toNextHeal), toNextHeal<=0));
		lore.add(ChatColor.DARK_GREEN+"Лечение сбивает 20% времени. "+ChatColor.GRAY+"("+GepUtil.timeStr((int) (toRemove*0.20))+")");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public int healPrice(){
		if(name.equals("Диарея")){
			return 25;
		}
		if(name.equals("Глисты")){
			return 80;
		}
		if(name.equals("Гастрит")){
			return 35;
		}
		if(name.equals("Простуда")){
			return 30;
		}
		if(name.equals("Исжога")){
			return 20;
		}
		if(name.equals("Отравление")){
			return 10;
		}
		return -1;
	}
	public ItemStack infoItem(PlayerInfo pi){
		if(!searched){
			return ItemUtil.create(Material.GLASS, 1, 0, ChatColor.GRAY+"Неизвестная болезнь", new String[]{
					ChatColor.AQUA+"Узнать болезни можно в больнице."
			}, null, 0);
		}
		ItemStack ret=ItemUtil.create(Material.BARRIER, ChatColor.RED+"Ошибка. ID="+name);
		if(name.equals("Диарея")){
			ret=ItemUtil.create(Material.ACACIA_BOAT, 1, 3, ChatColor.DARK_GREEN+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"Можно получить только через еду."
			}, null, 0);
		}
		if(name.equals("Гастрит")){
			ret=ItemUtil.create(Material.ACACIA_FENCE_GATE, 1, 11, ChatColor.YELLOW+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"Можно получить только через еду."
			}, null, 0);
		}
		if(name.equals("Глисты")){
			ret=ItemUtil.create(Material.ACACIA_PRESSURE_PLATE, 1, 1, ChatColor.RED+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"Можно получить через еду",
					ChatColor.DARK_GREEN+"и через сон."
			}, null, 0);
		}
		if(name.equals("Простуда")){
			ret=ItemUtil.create(Material.ARMOR_STAND, 1, 6, ChatColor.RED+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"От чего её только нельзя получить..."
			}, null, 0);
		}
		if(name.equals("Изжога")){
			ret=ItemUtil.create(Material.BLACK_CONCRETE_POWDER, 1, 3, ChatColor.DARK_GREEN+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"Можно получить только через еду."
			}, null, 0);
		}
		if(name.equals("Отравление")){
			ret=ItemUtil.create(Material.ANCIENT_DEBRIS, 1, 3, ChatColor.DARK_GREEN+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"Быстро проходит, часто происходит."
			}, null, 0);
		}
		if(name.equals("Промокший")){
			ret=ItemUtil.create(Material.WATER_BUCKET, 1, 0, ChatColor.BLUE+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"Бррр... Мокро... Холодно!"
			}, null, 0);
		}
		ItemMeta meta = ret.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore==null)lore=new ArrayList<>();
		if(basics.containsKey("foodSpeed"))lore.add(ChatColor.GOLD+"Скорость голода "+ChatColor.RED+"+"+basics.get("foodSpeed")+"%");
		if(basics.containsKey("speed"))lore.add(ChatColor.AQUA+"Скорость "+ChatColor.RED+""+basics.get("speed")*0.01+"x");
		if(basics.containsKey("rvota"))lore.add(ChatColor.DARK_GREEN+"Рвота "+ChatColor.RED+"+"+basics.get("rvota"));
		if(basics.containsKey("temp"))lore.add(ChatColor.YELLOW+"Температура "+GepUtil.boolString(ChatColor.GOLD+"+", ChatColor.AQUA+"", basics.get("temp")>0)+basics.get("temp"));
		if(pi.lvl<3)lore.add(ChatColor.AQUA+"Множитель уровня: "+ChatColor.GREEN+"x"+GepUtil.CylDouble(0.5+pi.lvl, "#0.00"));
		lore.add(ChatColor.GRAY+"Пройдёт через "+ChatColor.YELLOW+GepUtil.timeStr(toRemove));
		meta.setLore(lore);
		ret.setItemMeta(meta);
		return ret;
	}
	public void save(FileConfiguration conf, String st){
		conf.set(st+".toRemove", toRemove);
		conf.set(st+".preStage", preStage);
		conf.set(st+".searched", searched);
	}
	public disease(FileConfiguration conf, String st, String name){
		this.name=name;
		toRemove=conf.getInt(st+".toRemove");
		preStage=conf.getInt(st+".preStage");
		searched=conf.getBoolean(st+".searched");
	}
}
