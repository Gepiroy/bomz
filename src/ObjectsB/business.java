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
		if(!pi.hasBuss(name))inv.setItem(22, ItemUtil.create(Material.EMERALD, 1, ChatColor.DARK_GREEN+"������ ������", new String[]{
				ChatColor.DARK_GREEN+"����� ���-���������� ����� �������."
				,ChatColor.LIGHT_PURPLE+"���-��������� �������� 10% �������."
				,ChatColor.GOLD+"����: "+GepUtil.boolCol(pi.money>=subownprice)+subownprice+" ���."
		}));
		else inv.setItem(22, ItemUtil.create(Material.EMERALD, 1, ChatColor.DARK_GREEN+"������ ������", new String[]{
				ChatColor.DARK_GREEN+"����� ����� �� ���������� ����� �������."
				,ChatColor.LIGHT_PURPLE+"��������� �������� 25% �������."
				,ChatColor.LIGHT_PURPLE+"������ ����. ����� ��������� �����"
				,ChatColor.GREEN+"������� ����������"
				,ChatColor.AQUA+"(������� x2, ���������� ��������.)"
				,ChatColor.RED+"����. �������� ������� �� �����."
				,ChatColor.GRAY+"(�� 0:00 ���. ������� ��� �� ������ �� ����)."
				,ChatColor.GOLD+"����: "+GepUtil.boolCol(pi.money>=ownprice)+ownprice+" ���."
		}));
		if(pi.hasBuss(name)&&pi.getBuss(name).own)inv.setItem(22, ItemUtil.create(Material.NETHER_STAR, 1, ChatColor.AQUA+"����. ������� � �������!", new String[]{
				ChatColor.GREEN+"�� ��� ����� ���������� ����� �������."
		}));
		if(pi.buss.contains("own"+name)&&sellown>0&&!owner.equals(p.getName()))inv.setItem(14, ItemUtil.create(Material.GOLDEN_HELMET, 1, ChatColor.GOLD+"������ ������� �����", new String[]{
				ChatColor.GREEN+"����. �������� ����� ������� ��� ����� �� "+GepUtil.boolCol(pi.money>=sellown)+sellown+" ���."
				,ChatColor.RED+"����. �������� ������� �� �����."
				,ChatColor.GRAY+"(�� 0:00 ���. ������� ��� �� ������ �� ����)."
		}));
		if(sellown==0&&owner.equals(p.getName()))inv.setItem(14, ItemUtil.create(Material.GOLDEN_HELMET, 1, ChatColor.GOLD+"������� ������� �����", new String[]{
				ChatColor.GREEN+"�� ������ ��������� ����. ����� �� �������."
				,ChatColor.GOLD+"������ ��������� ������ ������ � ��� ��� �����."
				,ChatColor.YELLOW+"�� ������� ������� ���������� (������� �� "+ownprice+")."
		}));
		if(sellown>0&&owner.equals(p.getName()))inv.setItem(14, ItemUtil.create(Material.GOLDEN_HELMET, 1, ChatColor.GOLD+"�� ��� ������� ��� �����.", new String[]{
				ChatColor.RED+"�� ������� ��� ����� �� "+sellown+" ���."
				,ChatColor.RED+"�������� ���� ������ ������."
		}));
		inv.setItem(26, ItemUtil.create(Material.PAPER, 1, ChatColor.BLUE+"���������� � �������", new String[]{
				ChatColor.GREEN+"���������� �����: "+ChatColor.AQUA+moneyPerDay+" �./�."
				,ChatColor.GRAY+"(�� �������� ���� ���� � ����� � ��������)"
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
					p.sendMessage(ChatColor.LIGHT_PURPLE+"�� ����� ����� �� ���������� ������� �������! ������ �� ��������� 25% ������� ����� �����, � � ��� ���� ���� ����� "+ChatColor.GREEN+"������� ����������! "+ChatColor.YELLOW+"(����������� ��������� ��������, ��������� x2 ������.)");
					if(owner.equals(""))changeOwner();
				}
			}
			else{
				if(GUI.buy(subownprice, p)){
					pi.buss.add(new pibus(name, moneyPerDay));
					p.sendMessage(ChatColor.LIGHT_PURPLE+"�� ����� ����� �� ���-���������� ������� �������! ������ �� ��������� 10% ������� ����� �����!");
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
			if(Bukkit.getPlayer(owner)!=null)Bukkit.getPlayer(owner).sendMessage(ChatColor.RED+"�� �������� ��� ������� ����� � ������� '"+name+"'.");
			owner=canBeOwn.get(new Random().nextInt(canBeOwn.size()));
			Bukkit.getPlayer(owner).sendMessage(ChatColor.AQUA+"�� ������ ����� �������� � ������� '"+name+"'!");
			Bukkit.getPlayer(owner).sendMessage(ChatColor.LIGHT_PURPLE+"������ �� ��������� � 2 ���� ������ ������ ������, ������ ��������� ��������, ��� �� ������ ��������� �� ������� ��� �����. �� ��� �������� � ����� ������ �������.");
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
