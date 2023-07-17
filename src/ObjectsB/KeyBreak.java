package ObjectsB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import bomz.Events;
import bomz.GUI;
import utilsB.GepUtil;
import utilsB.ItemUtil;

public class KeyBreak {
	List<Boolean> keys = new ArrayList<>();
	int opened=0;
	public KeyBreak(int dif){
		for(int i=0;i<dif;i++){
			keys.add(new Random().nextBoolean());
		}
	}
	public void openGUI(Player p){
		Inventory inv = Bukkit.createInventory(null, ((keys.size()-1)/9+1)*9, ChatColor.RED+"����� ����� ��������");
		int i=0;
		for(boolean b:keys){
			if(opened>i)inv.setItem(i, ItemUtil.create(Material.DIAMOND, GepUtil.boolString(ChatColor.AQUA+"<--", ChatColor.AQUA+"-->", b)));
			else inv.setItem(i, ItemUtil.create(Material.IRON_NUGGET, 1, ChatColor.GRAY+"???", new String[]{ChatColor.YELLOW+"���, ����� ��������� �����",ChatColor.YELLOW+"���, ����� ��������� ������"}));
			i++;
		}
		p.openInventory(inv);
	}
	public boolean click(InventoryClickEvent e){
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = Events.plist.get(p.getName());
		if(!e.getCurrentItem().getType().equals(Material.IRON_NUGGET))return false;
		if(e.getSlot()!=opened)return false;
		if(!GUI.CanBuyItem(1, Material.IRON_NUGGET, ChatColor.RED+"�������", p)){
			p.sendMessage(ChatColor.RED+"��������� �������.");
			p.closeInventory();
			return false;
		}
		boolean left = e.getClick().isLeftClick();
		if(keys.get(e.getSlot())==left){
			opened++;
			if(opened>=keys.size()){
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
				p.sendMessage(ChatColor.GREEN+"����� �������!");
				p.closeInventory();
				return true;
			}
		}
		else{
			if(new Random().nextDouble()>pi.perkLvl("key")/50.00)p.sendMessage(ChatColor.RED+"׸��! ������� ���������! "+ChatColor.GOLD+"(�� ���� ��������)");
			else p.sendMessage(ChatColor.RED+"��� ������� ��������� �������! "+ChatColor.GOLD+"(����� '��������')");
			pi.addPerk(p, ChatColor.RED+"��������", "key", 1, 10);
			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 1);
			GepUtil.sellItems(p, Material.IRON_NUGGET, ChatColor.RED+"�������", 1);
			opened=0;
		}
		openGUI(p);
		return false;
	}
}
