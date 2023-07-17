package ObjectsB;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import invsUtil.Inv;
import invsUtil.InvEvents;
import invsUtil.Invs;
import utilsB.GepUtil;
import utilsB.ItemUtil;

public class priut {
	public Location loc;
	public business bus;
	public priut(Location Loc){
		loc=Loc;
		bus = new business("����� "+GepUtil.locInfo(loc), 100000, 500000, 25000, 20000);
	}
	
	public static final Inv inv = new Inv("&6�����") {
		
		@Override
		public void displItems(Inventory inv) {
			inv.setItem(10, ItemUtil.create(Material.WATER_BUCKET, 1, 0, ChatColor.DARK_GREEN+"�������� � ����� ��������", new String[]{
					ChatColor.DARK_GREEN+"����: "+GepUtil.boolCol(pi.money>=10)+"10 ���."
			}, null, null));
			inv.setItem(12, ItemUtil.create(Material.WATER_BUCKET, 1, 0, ChatColor.BLUE+"�������� � ������", new String[]{
					ChatColor.DARK_GREEN+"����: "+GepUtil.boolCol(pi.money>=25)+"25 ���."
			}, null, null));
			inv.setItem(14, ItemUtil.create(Material.DIAMOND, 1, 0, ChatColor.AQUA+"��������� ��������", new String[]{
					ChatColor.DARK_GREEN+"����: "+GepUtil.boolCol(pi.money>=50)+"50 ���."
			}, null, null));
		}
		
		@Override
		public void click(InventoryClickEvent e) {
			ItemStack item = e.getCurrentItem();
			if(item.getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN+"�������� � ����� ��������")){
				if(pi.money<10){
					p.sendMessage(ChatColor.RED+"�� ������ ��� �� ������ �� ������, �������!");
					return;
				}
				pi.money-=10;
				GepUtil.HashMapReplacer(pi.waits, "clean", 5, false, true);
				Invs.open(p, InvEvents.clean);
			}
			if(item.getItemMeta().getDisplayName().equals(ChatColor.BLUE+"�������� � ������")){
				if(pi.money<25){
					p.sendMessage(ChatColor.RED+"�� ������ ��� �� ������ �� ������, �������!");
					return;
				}
				pi.money-=25;
				GepUtil.HashMapReplacer(pi.waits, "clean", 2, false, true);
				Invs.open(p, InvEvents.clean);
			}
			if(item.getItemMeta().getDisplayName().equals(ChatColor.AQUA+"��������� ��������")){
				if(pi.money<50){
					p.sendMessage(ChatColor.RED+"�� ������ ��� �� ������ �� ������, �������!");
					return;
				}
				p.closeInventory();
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 2, 1);
				p.sendMessage(ChatColor.AQUA+"�� ��������� �����!");
				pi.money-=50;
				pi.dirt=0;
			}
		}
	};
	
	public void openGUI(Player p){
		Invs.open(p, inv);
	}
}
