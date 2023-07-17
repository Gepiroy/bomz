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
	public String stage="������";
	public int toNextHeal=0;
	public HashMap<String,Integer> basics = new HashMap<>();
	public disease(){}
	public disease(String Name, int rem, int pre){
		name=Name;
		toRemove=rem;
		preStage=pre;
		List<String> startSearched = new ArrayList<>();
		startSearched.add("��������");
		startSearched.add("����������");
		startSearched.add("���������");
		if(startSearched.contains(name))searched=true;
	}
	public void stage(PlayerInfo pi){
		basics.clear();
		if(preStage>0){
			preStage--;
		}
		if(name.equals("������")){
			if(preStage>0){
				stage=ChatColor.AQUA+"������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 25, false, true);
			}
			if(toRemove<=240){
				stage=ChatColor.GREEN+"�������������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 20, false, true);
			}
			else if(toRemove<=1200){
				stage=ChatColor.GOLD+"��������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 50, false, true);
				GepUtil.HashMapReplacer(basics, "speed", -10, false, true);
			}
			else{
				stage=ChatColor.RED+"������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 125, false, true);
				GepUtil.HashMapReplacer(basics, "speed", -25, false, true);
			}
		}
		if(name.equals("�������")){
			if(preStage>0){
				stage=ChatColor.AQUA+"������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 25, false, true);
			}
			if(toRemove<=480){
				stage=ChatColor.GREEN+"�������������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 20, false, true);
			}
			else if(toRemove<=2400){
				stage=ChatColor.GOLD+"��������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 50, false, true);
			}
			else{
				stage=ChatColor.RED+"������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 125, false, true);
			}
		}
		if(name.equals("������")){
			if(preStage>0){
				stage=ChatColor.AQUA+"������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 35, false, true);
			}
			if(toRemove<=480){
				stage=ChatColor.GREEN+"�������������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 65, false, true);
			}
			else if(toRemove<=2400){
				stage=ChatColor.GOLD+"��������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 100, false, true);
			}
			else{
				stage=ChatColor.RED+"������";
				GepUtil.HashMapReplacer(basics, "foodSpeed", 250, false, true);
			}
		}
		if(name.equals("����������")){
			if(preStage>0){
				stage=ChatColor.AQUA+"������";
				GepUtil.HashMapReplacer(basics, "speed", -5, false, true);
			}
			if(toRemove<=100){
				stage=ChatColor.GREEN+"�������������";
				GepUtil.HashMapReplacer(basics, "speed", -5, false, true);
			}
			else if(toRemove<=200){
				stage=ChatColor.GOLD+"��������";
				GepUtil.HashMapReplacer(basics, "speed", -10, false, true);
				basics.put("rvota", 35);
			}
			else{
				stage=ChatColor.RED+"������";
				GepUtil.HashMapReplacer(basics, "speed", -25, false, true);
				basics.put("rvota", 100);
			}
		}
		if(name.equals("���������")){
			if(preStage>0){
				stage=ChatColor.AQUA+"������ ���";
				GepUtil.HashMapReplacer(basics, "speed", 1, false, true);
				basics.put("temp", -1);
			}
			if(toRemove<=60){
				stage=ChatColor.GREEN+"����� �����";
				GepUtil.HashMapReplacer(basics, "speed", -5, false, true);
				basics.put("temp", -2);
			}
			else if(toRemove<=120){
				stage=ChatColor.GOLD+"��������";
				GepUtil.HashMapReplacer(basics, "speed", -10, false, true);
				basics.put("temp", -10);
			}
			else{
				stage=ChatColor.RED+"��������";
				GepUtil.HashMapReplacer(basics, "speed", -25, false, true);
				basics.put("temp", -20);
			}
		}
		if(basics.size()>0){
			if(pi.lvl<3)for(String st:basics.keySet()){
				GepUtil.HashMapReplacer(basics, st, (int) (basics.get(st)*(0.5+pi.lvl*0.25)), false, true);
			}
		}else stage="������! n="+toRemove;
	}
	public ItemStack healItem(PlayerInfo pi){
		ItemStack item = infoItem(pi);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"������ ������� '"+ChatColor.DARK_GREEN+name+ChatColor.GREEN+"'");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.BLUE+"���� �������: "+GepUtil.boolCol(pi.money>=healPrice())+healPrice());
		lore.add(GepUtil.boolString(ChatColor.GREEN+"����� ������!", ChatColor.GOLD+"��������� ������� ����� "+ChatColor.YELLOW+GepUtil.timeStr(toNextHeal), toNextHeal<=0));
		lore.add(ChatColor.DARK_GREEN+"������� ������� 20% �������. "+ChatColor.GRAY+"("+GepUtil.timeStr((int) (toRemove*0.20))+")");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public int healPrice(){
		if(name.equals("������")){
			return 25;
		}
		if(name.equals("������")){
			return 80;
		}
		if(name.equals("�������")){
			return 35;
		}
		if(name.equals("��������")){
			return 30;
		}
		if(name.equals("������")){
			return 20;
		}
		if(name.equals("����������")){
			return 10;
		}
		return -1;
	}
	public ItemStack infoItem(PlayerInfo pi){
		if(!searched){
			return ItemUtil.create(Material.GLASS, 1, 0, ChatColor.GRAY+"����������� �������", new String[]{
					ChatColor.AQUA+"������ ������� ����� � ��������."
			}, null, 0);
		}
		ItemStack ret=ItemUtil.create(Material.BARRIER, ChatColor.RED+"������. ID="+name);
		if(name.equals("������")){
			ret=ItemUtil.create(Material.ACACIA_BOAT, 1, 3, ChatColor.DARK_GREEN+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"����� �������� ������ ����� ���."
			}, null, 0);
		}
		if(name.equals("�������")){
			ret=ItemUtil.create(Material.ACACIA_FENCE_GATE, 1, 11, ChatColor.YELLOW+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"����� �������� ������ ����� ���."
			}, null, 0);
		}
		if(name.equals("������")){
			ret=ItemUtil.create(Material.ACACIA_PRESSURE_PLATE, 1, 1, ChatColor.RED+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"����� �������� ����� ���",
					ChatColor.DARK_GREEN+"� ����� ���."
			}, null, 0);
		}
		if(name.equals("��������")){
			ret=ItemUtil.create(Material.ARMOR_STAND, 1, 6, ChatColor.RED+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"�� ���� � ������ ������ ��������..."
			}, null, 0);
		}
		if(name.equals("������")){
			ret=ItemUtil.create(Material.BLACK_CONCRETE_POWDER, 1, 3, ChatColor.DARK_GREEN+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"����� �������� ������ ����� ���."
			}, null, 0);
		}
		if(name.equals("����������")){
			ret=ItemUtil.create(Material.ANCIENT_DEBRIS, 1, 3, ChatColor.DARK_GREEN+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"������ ��������, ����� ����������."
			}, null, 0);
		}
		if(name.equals("���������")){
			ret=ItemUtil.create(Material.WATER_BUCKET, 1, 0, ChatColor.BLUE+name+ChatColor.GRAY+" ("+stage+ChatColor.GRAY+")", new String[]{
					ChatColor.DARK_GREEN+"����... �����... �������!"
			}, null, 0);
		}
		ItemMeta meta = ret.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore==null)lore=new ArrayList<>();
		if(basics.containsKey("foodSpeed"))lore.add(ChatColor.GOLD+"�������� ������ "+ChatColor.RED+"+"+basics.get("foodSpeed")+"%");
		if(basics.containsKey("speed"))lore.add(ChatColor.AQUA+"�������� "+ChatColor.RED+""+basics.get("speed")*0.01+"x");
		if(basics.containsKey("rvota"))lore.add(ChatColor.DARK_GREEN+"����� "+ChatColor.RED+"+"+basics.get("rvota"));
		if(basics.containsKey("temp"))lore.add(ChatColor.YELLOW+"����������� "+GepUtil.boolString(ChatColor.GOLD+"+", ChatColor.AQUA+"", basics.get("temp")>0)+basics.get("temp"));
		if(pi.lvl<3)lore.add(ChatColor.AQUA+"��������� ������: "+ChatColor.GREEN+"x"+GepUtil.CylDouble(0.5+pi.lvl, "#0.00"));
		lore.add(ChatColor.GRAY+"������ ����� "+ChatColor.YELLOW+GepUtil.timeStr(toRemove));
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
