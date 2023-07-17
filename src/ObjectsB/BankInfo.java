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
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN+"Банк");
		if(deposDay<main.day){
			int coef = main.day-deposDay;
			if(coef>10){
				p.sendMessage(ChatColor.RED+"Вы не появлялись в банке "+coef+" дней. Вам засчитано только 10 дней дохода от вклада.");
				coef=10;
			}
			canTake+=depos/100.00*coef*3;
			deposDay=main.day;
		}
		if(creditDay<main.day&&creditPay==0){
			creditDay=main.day;
		}
		inv.setItem(11, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.YELLOW+"Вложить деньги", new String[]{
				ChatColor.AQUA+"У вас сейчас в обороте "+depos+" руб."
				,ChatColor.GREEN+"Ваша несобранная прибль с оборота: "+GepUtil.CylDouble(canTake, "#0.00")+" руб."
				,ChatColor.LIGHT_PURPLE+"За вложенные деньги вы будете"
				,ChatColor.LIGHT_PURPLE+"получать 3% от них каждый день!"
				,ChatColor.GOLD+"Если вы хотите забрать ВЛОЖЕННЫЕ"
				,ChatColor.GOLD+"деньги с оборота, вы потеряете 10%."
				,ChatColor.AQUA+"Клик, чтобы открыть меню вкладов."
		}, null, null));
		inv.setItem(13, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.BLUE+"Сохранить деньги", new String[]{
				ChatColor.AQUA+"У вас сейчас на счету "+save+" руб."
				,ChatColor.GREEN+"Сохранение денег стоит 5%."
				,ChatColor.AQUA+"Клик, чтобы открыть меню счёта."
		}, null, null));
		inv.setItem(15, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.GOLD+"Кредит", new String[]{
				ChatColor.GOLD+"Вы должны банку "+creditPay+" руб."
				,ChatColor.GREEN+"Срок сдачи кредита - "+(main.day-creditDay-daysToCredit)
				,ChatColor.AQUA+"Клик, чтобы открыть меню кредитов."
		}, null, null));
		p.openInventory(inv);
	}
	public void VKLAD(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN+"Банк (вклад)");
		String[] lore = {
				ChatColor.AQUA+"У вас сейчас в обороте "+ChatColor.GREEN+depos+" руб."
		};
		inv.setItem(1, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GREEN+"Вложить 100 руб.", lore, null, null));
		inv.setItem(3, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GREEN+"Вложить 500 руб.", lore, null, null));
		inv.setItem(5, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GREEN+"Вложить 1000 руб.", lore, null, null));
		inv.setItem(7, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GREEN+"Вложить 5000 руб.", lore, null, null));
		
		inv.setItem(13, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.DARK_GREEN+"Забрать прибль", new String[]{
				ChatColor.GREEN+"Ваша несобранная прибль с оборота: "+GepUtil.CylDouble(canTake, "#0.00")+" руб."
		}, null, null));
		inv.setItem(26, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.RED+"Забрать вложенные деньги", new String[]{
				ChatColor.AQUA+"У вас сейчас в обороте "+depos+" руб."
				,ChatColor.GOLD+"Если вы хотите забрать ВЛОЖЕННЫЕ"
				,ChatColor.GOLD+"деньги с оборота, вы потеряете 10%."
		}, null, null));
		p.openInventory(inv);
	}
	public void CREDIT(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN+"Банк (кредит)");
		
		inv.setItem(11, ItemUtil.create(Material.GOLD_INGOT, 1, 0, ChatColor.GOLD+"Взять кредит", new String[]{
				ChatColor.AQUA+"Клик, чтобы открыть меню взятия кредита."
		}, null, null));
		inv.setItem(15, ItemUtil.create(Material.EMERALD, 1, 0, ChatColor.DARK_GREEN+"Оплатить кредит", new String[]{
				ChatColor.GOLD+"Вы должны банку "+creditPay+" руб."
				,ChatColor.GREEN+"Срок сдачи кредита - "+(main.day-creditDay-daysToCredit)
				,ChatColor.AQUA+"Клик, чтобы открыть меню кредита."
		}, null, null));
		p.openInventory(inv);
	}
	public void click(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = Events.plist.get(p.getName());
		ItemStack item = e.getCurrentItem();
		if(item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW+"Вложить деньги")){
			VKLAD(p);
		}
		if(item.getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Кредит")){
			CREDIT(p);
		}
		if(item.getItemMeta().getDisplayName().contains(ChatColor.GREEN+"Вложить")){
			int am = GepUtil.intFromString(item.getItemMeta().getDisplayName());
			if(pi.money<am){
				p.sendMessage(ChatColor.DARK_GREEN+"[БАНК] "+ChatColor.RED+"У вас недостаточно денег.");
				return;
			}
			pi.money-=am;
			depos+=am;
			main.BANK+=am;
			VKLAD(p);
		}
		if(item.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN+"Забрать прибль")){
			if(canTake==0){
				p.sendMessage(ChatColor.DARK_GREEN+"[БАНК] "+ChatColor.RED+"Вам нечего забирать.");
				return;
			}
			p.sendMessage(ChatColor.DARK_GREEN+"[БАНК] "+ChatColor.GREEN+"Да, конечно, вот ваши "+GepUtil.CylDouble(canTake, "#0.00")+" руб.");
			pi.money+=canTake;
			main.BANK-=canTake;
			canTake=0;
			VKLAD(p);
		}
		if(item.getItemMeta().getDisplayName().contains(ChatColor.RED+"Забрать вложенные деньги")){
			if(depos==0){
				p.sendMessage(ChatColor.DARK_GREEN+"[БАНК] "+ChatColor.RED+"Вам нечего забирать.");
				return;
			}
			p.sendMessage(ChatColor.DARK_GREEN+"[БАНК] "+ChatColor.GREEN+"Да, конечно, вот ваши "+GepUtil.CylDouble(depos/100.00*90, "#0.00")+" руб.");
			p.sendMessage(ChatColor.DARK_GREEN+"[БАНК] "+ChatColor.GOLD+"Мы взяли комиссию в 10% ("+GepUtil.CylDouble(depos/10.00, "#0.00")+" руб.) за предоставленные вами неудобства :)");
			pi.money+=depos/100.00*90;
			main.BANK-=depos/100.00*90;
			depos=0;
			VKLAD(p);
		}
	}
}
