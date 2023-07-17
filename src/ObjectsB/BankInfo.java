package ObjectsB;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import bomz.Events;
import bomz.main;
import utilsB.GepUtil;
import utilsB.ItemUtil;

public class BankInfo {
	public int depos=0;
	public int deposDay=0;
	public int save=0;
	public double canTake=0;
	public double creditPay=0;
	public int daysToCredit=0;
	public int creditDay=0;
	public void openGUI(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN+"����");
		if(deposDay<main.day){
			int coef = main.day-deposDay;
			if(coef>10){
				p.sendMessage(ChatColor.RED+"�� �� ���������� � ����� "+coef+" ����. ��� ��������� ������ 10 ���� ������ �� ������.");
				coef=10;
			}
			canTake+=depos/100.00*coef*3;
			deposDay=main.day;
		}
		if(creditDay<main.day&&creditPay==0){
			creditDay=main.day;
		}
		inv.setItem(11, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.YELLOW+"������� ������", new String[]{
				ChatColor.AQUA+"� ��� ������ � ������� "+depos+" ���."
				,ChatColor.GREEN+"���� ����������� ������ � �������: "+GepUtil.CylDouble(canTake, "#0.00")+" ���."
				,ChatColor.LIGHT_PURPLE+"�� ��������� ������ �� ������"
				,ChatColor.LIGHT_PURPLE+"�������� 3% �� ��� ������ ����!"
				,ChatColor.GOLD+"���� �� ������ ������� ���������"
				,ChatColor.GOLD+"������ � �������, �� ��������� 10%."
				,ChatColor.AQUA+"����, ����� ������� ���� �������."
		}, null, null));
		inv.setItem(13, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.BLUE+"��������� ������", new String[]{
				ChatColor.AQUA+"� ��� ������ �� ����� "+save+" ���."
				,ChatColor.GREEN+"���������� ����� ����� 5%."
				,ChatColor.AQUA+"����, ����� ������� ���� �����."
		}, null, null));
		inv.setItem(15, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.GOLD+"������", new String[]{
				ChatColor.GOLD+"�� ������ ����� "+creditPay+" ���."
				,ChatColor.GREEN+"���� ����� ������� - "+(main.day-creditDay-daysToCredit)
				,ChatColor.AQUA+"����, ����� ������� ���� ��������."
		}, null, null));
		p.openInventory(inv);
	}
	public void VKLAD(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN+"���� (�����)");
		String[] lore = {
				ChatColor.AQUA+"� ��� ������ � ������� "+ChatColor.GREEN+depos+" ���."
		};
		inv.setItem(1, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GREEN+"������� 100 ���.", lore, null, null));
		inv.setItem(3, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GREEN+"������� 500 ���.", lore, null, null));
		inv.setItem(5, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GREEN+"������� 1000 ���.", lore, null, null));
		inv.setItem(7, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GREEN+"������� 5000 ���.", lore, null, null));
		
		inv.setItem(13, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.DARK_GREEN+"������� ������", new String[]{
				ChatColor.GREEN+"���� ����������� ������ � �������: "+GepUtil.CylDouble(canTake, "#0.00")+" ���."
		}, null, null));
		inv.setItem(26, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.RED+"������� ��������� ������", new String[]{
				ChatColor.AQUA+"� ��� ������ � ������� "+depos+" ���."
				,ChatColor.GOLD+"���� �� ������ ������� ���������"
				,ChatColor.GOLD+"������ � �������, �� ��������� 10%."
		}, null, null));
		p.openInventory(inv);
	}
	public void CREDIT(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN+"���� (������)");
		
		inv.setItem(11, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GOLD+"����� ������", new String[]{
				ChatColor.AQUA+"����, ����� ������� ���� ������ �������."
		}, null, null));
		inv.setItem(15, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.DARK_GREEN+"�������� ������", new String[]{
				ChatColor.GOLD+"�� ������ ����� "+creditPay+" ���."
				,ChatColor.GREEN+"���� ����� ������� - "+(main.day-creditDay-daysToCredit)
				,ChatColor.AQUA+"����, ����� ������� ���� �������."
		}, null, null));
		p.openInventory(inv);
	}
	public void click(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = Events.plist.get(p.getName());
		ItemStack item = e.getCurrentItem();
		if(item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW+"������� ������")){
			VKLAD(p);
		}
		if(item.getItemMeta().getDisplayName().equals(ChatColor.GOLD+"������")){
			CREDIT(p);
		}
		if(item.getItemMeta().getDisplayName().contains(ChatColor.GREEN+"�������")){
			int am = GepUtil.intFromString(item.getItemMeta().getDisplayName());
			if(pi.money<am){
				p.sendMessage(ChatColor.DARK_GREEN+"[����] "+ChatColor.RED+"� ��� ������������ �����.");
				return;
			}
			pi.money-=am;
			depos+=am;
			main.BANK+=am;
			VKLAD(p);
		}
		if(item.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN+"������� ������")){
			if(canTake==0){
				p.sendMessage(ChatColor.DARK_GREEN+"[����] "+ChatColor.RED+"��� ������ ��������.");
				return;
			}
			p.sendMessage(ChatColor.DARK_GREEN+"[����] "+ChatColor.GREEN+"��, �������, ��� ���� "+GepUtil.CylDouble(canTake, "#0.00")+" ���.");
			pi.money+=canTake;
			main.BANK-=canTake;
			canTake=0;
			VKLAD(p);
		}
		if(item.getItemMeta().getDisplayName().contains(ChatColor.RED+"������� ��������� ������")){
			if(depos==0){
				p.sendMessage(ChatColor.DARK_GREEN+"[����] "+ChatColor.RED+"��� ������ ��������.");
				return;
			}
			p.sendMessage(ChatColor.DARK_GREEN+"[����] "+ChatColor.GREEN+"��, �������, ��� ���� "+GepUtil.CylDouble(depos/100.00*90, "#0.00")+" ���.");
			p.sendMessage(ChatColor.DARK_GREEN+"[����] "+ChatColor.GOLD+"�� ����� �������� � 10% ("+GepUtil.CylDouble(depos/10.00, "#0.00")+" ���.) �� ��������������� ���� ���������� :)");
			pi.money+=depos/100.00*90;
			main.BANK-=depos/100.00*90;
			depos=0;
			VKLAD(p);
		}
	}
}
