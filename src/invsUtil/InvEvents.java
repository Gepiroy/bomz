package invsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ObjectsB.Help;
import ObjectsB.disease;
import bomz.Events;
import bomz.GUI;
import bomz.main;
import utilsB.GepUtil;
import utilsB.ItemUtil;
import utilsB.Items;

public class InvEvents {
	
	public static List<Inv> invs = new ArrayList<>();
	
	public static final Inv Menu = new Inv("&a�������") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(3, ItemUtil.create(Material.OAK_SIGN, 1, ChatColor.GREEN+"���� ����������", new String[]{ChatColor.AQUA+"��������� ����� �� ����������",ChatColor.AQUA+"����� ����������������.",GepUtil.boolString(ChatColor.GREEN+"���. "+ChatColor.YELLOW+"(����, ����� ����.)", ChatColor.RED+"����. "+ChatColor.YELLOW+"(����, ����� ���.)", pi.bools.contains("begEye"))}));
			inv.setItem(4, ItemUtil.create(Material.FLOWER_POT, 1, ChatColor.GREEN+"��������� ����� �������", new String[]{ChatColor.AQUA+"��������� ����� �� ����������",ChatColor.AQUA+"������ ����� �������.",GepUtil.boolString(ChatColor.GREEN+"���. "+ChatColor.YELLOW+"(����, ����� ����.)", ChatColor.RED+"����. "+ChatColor.YELLOW+"(����, ����� ���.)", pi.bools.contains("bottleEye"))}));
			inv.setItem(5, ItemUtil.create(Material.WATER_BUCKET, 1, ChatColor.GOLD+"��������� �����", new String[]{ChatColor.AQUA+"��������� ����� �� ����������",ChatColor.AQUA+"������.",GepUtil.boolString(ChatColor.GREEN+"���. "+ChatColor.YELLOW+"(����, ����� ����.)", ChatColor.RED+"����. "+ChatColor.YELLOW+"(����, ����� ���.)", pi.bools.contains("pEye"))}));
			
			inv.setItem(15, ItemUtil.create(Material.BLUE_BED, 1, ChatColor.BLUE+"���", new String[]{ChatColor.AQUA+"��� �� � ���� �����?"}));
			inv.setItem(18, ItemUtil.create(Material.RED_DYE, 1, 1, ChatColor.RED+"�������", new String[]{ChatColor.AQUA+"��� � �����, "+p.getName().substring(0, 3)+"-y?"},null,0));
			
			inv.setItem(26, ItemUtil.create(Material.EXPERIENCE_BOTTLE, 1, ChatColor.AQUA+"������", new String[]{ChatColor.GREEN+"���� ������"}));
		}
		@Override public void click(InventoryClickEvent e) {
			switch(e.getSlot()){
			case 3:{
				pi.toggleBool("begEye");
			}break;
			case 4:{
				pi.toggleBool("bottleEye");
			}break;
			case 5:{
				pi.toggleBool("pEye");
			}break;
			case 15:{
				Invs.open(p, SleepGUI);
				return;
			}
			case 18:{
				pi.diseaseGUI();
			}break;
			case 26:{
				Invs.open(p, help);
				return;
			}
			}
			Invs.open(p, Menu);
		}
	};
	public static final Inv SleepGUI = new Inv("&1���") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(10, ItemUtil.create(Material.GRASS, 1, 0, ChatColor.GREEN+"����� �� �����", new String[]{
					ChatColor.GRAY+"����� ����� �� �����. ����� �",
					ChatColor.GRAY+"�������, ��� ������, ��� �������.",
					ChatColor.GRAY+"�� ������� ����, ��� ������� ��������."
			}, null, 0));
			inv.setItem(12, ItemUtil.create(Material.OAK_STAIRS, 1, 0, ChatColor.GREEN+"����� �� ��������", new String[]{
					ChatColor.GRAY+"��������. ��������� ���� �� �����.",
					ChatColor.GRAY+"�� ������� ����, ����� ���� �",
					ChatColor.GRAY+"�� ��� �� ������� �������.",
					ChatColor.YELLOW+"����� ���� �� ��������."
			}, null, 0));
			inv.setItem(14, ItemUtil.create(Material.ORANGE_BED, 1, 0, ChatColor.GREEN+"����� � ���� ����", new String[]{
					ChatColor.GRAY+"�� ������� �� ������ ����.",
					ChatColor.GRAY+"��� ��� ����� �� �������.",
					ChatColor.YELLOW+"����� ���� � ����."
			}, null, 0));
			inv.setItem(16, ItemUtil.create(Material.BLUE_BED, 1, 1, ChatColor.GREEN+"����� � ������", new String[]{
					ChatColor.GRAY+"������� ��� ������-�������.",
					ChatColor.GRAY+"�� ������ �������� � ������, ��",
					ChatColor.YELLOW+"��� �������� � ����� ������."
			}, null, 0));
		}
		@Override public void click(InventoryClickEvent e) {
			switch(e.getSlot()){
			case 10:{
				pi.sleep="grass";
				p.sendMessage(ChatColor.AQUA+"�� ������� ��� �� �����. ��� �, ��� ��� �����.");
			}break;
			case 12:{
				if(pi.lastWalkMat.toString().contains("_STAIRS")){
					pi.sleep="stairs";
					p.sendMessage(ChatColor.AQUA+"�� ������� ��� �� ��������. ��� �, ��� ��� �����.");
				}else{
					p.sendMessage(ChatColor.GOLD+"����� ������� ���, �� ������ ���������� �� �������� (���� ���������� ��������)");
				}
			}break;
			default:{
				p.sendMessage(ChatColor.RED+"� ����������.");
			}break;
			}
			p.closeInventory();
		}
	};
	public static final Inv help = new Inv("&b������") {
		@Override public void displItems(Inventory inv) {
			int i=0;
			for(Help h:main.helps){
				p.sendMessage(h.name);
				inv.setItem(i, h.GUIItem());
				i++;
			}
		}
		@Override public void click(InventoryClickEvent e) {
			for(Help h:main.helps){
				if(h.name.equals(e.getCurrentItem().getItemMeta().getDisplayName())){
					if(e.getClick().isLeftClick()){
						p.sendMessage(ChatColor.AQUA+"[������ '"+h.name+ChatColor.AQUA+"' (�������)]: "+ChatColor.ITALIC+ChatColor.WHITE+h.shrt);
					}
					else p.sendMessage(ChatColor.AQUA+"[������ '"+h.name+ChatColor.AQUA+"' (��������)]: "+ChatColor.ITALIC+ChatColor.WHITE+h.lng);
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
					return;
				}
			}
		}
	};
	public static final Inv medGUI = new Inv("&a��������") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(1, ItemUtil.create(Material.COMPASS, 1, 0, ChatColor.AQUA+"�������������", new String[]{
					ChatColor.GREEN+"���������� ���� ��������� �������.",
					ChatColor.YELLOW+"����: "+GepUtil.boolCol(pi.money>=25)+25
			}, null, 0));
			int i=9;
			for(disease d:pi.diseases){
				if(d.searched){
					inv.setItem(i, d.healItem(pi));
					i+=2;
				}
			}
		}
		@Override public void click(InventoryClickEvent e) {
			if(e.getSlot()==1){
				List<String> avaliable = new ArrayList<>();
				for(disease d:pi.diseases){
					if(!d.searched)avaliable.add(d.name);
				}
				if(avaliable.size()<=0){
					p.sendMessage(ChatColor.GREEN+"� ��� ��� ����������� ��������!");
				}else{
					String toUnl=avaliable.get(new Random().nextInt(avaliable.size()));
					for(disease d:pi.diseases){
						if(d.name.equals(toUnl)){
							if(pi.money>=25){
								pi.money-=25;
								d.searched=true;
								p.sendMessage(ChatColor.GOLD+"� ��� ���������� ������� "+d.infoItem(pi).getItemMeta().getDisplayName());
								p.closeInventory();
							}else{
								p.sendMessage(ChatColor.RED+"�� ���� � ���� ����������� ���������, �������!");
								p.closeInventory();
							}
							return;
						}
					}
				}
			}
			else{
				disease d=null;
				int i=9;
				for(disease dd:pi.diseases){
					if(dd.searched){
						if(i==e.getSlot()){
							d=dd;
							break;
						}
						i+=2;
					}
				}
				if(d==null){
					p.sendMessage(ChatColor.RED+"������ ����������� �������.");
					return;
				}
				if(pi.money>=d.healPrice()){
					pi.money-=d.healPrice();
					d.toRemove-=d.toRemove*0.2;
					d.toNextHeal=300;
					p.sendMessage(ChatColor.AQUA+"������� ������� ��������. "+ChatColor.GRAY+"(��� "+GepUtil.timeStr(d.toRemove)+")");
					p.closeInventory();
				}else{
					p.sendMessage(ChatColor.RED+"��������� �� ������ ����� � ������ �����, ��������� � �������. ����������!");
					p.closeInventory();
				}
			}
		}
	};
	public static final Inv Seller = new Inv("&6�������") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(11, ItemUtil.create(Material.ROTTEN_FLESH, 1, 0, ChatColor.DARK_GREEN+"������� �����", new String[]{ChatColor.GOLD+"0.25 ���./��."}, null, null));
			inv.setItem(20, ItemUtil.create(Material.POTATO, 1, 0, ChatColor.YELLOW+"������� ��������", new String[]{ChatColor.GOLD+"0.1 ���./��."}, null, null));
			inv.setItem(13, ItemUtil.create(Material.LEATHER_CHESTPLATE, 1, 0, ChatColor.GOLD+"������� ������", new String[]{ChatColor.YELLOW+"(��, ��� ��������)"}, null, null));
			inv.setItem(15, ItemUtil.create(Material.GLASS_BOTTLE, 1, 0, ChatColor.BLUE+"������� ������� � ��������� ��������", new String[]{ChatColor.GOLD+"0.50 ���./��."}, null, null));
			inv.setItem(0, ItemUtil.create(Material.IRON_NUGGET, 1, 0, ChatColor.BLUE+"������� �������", new String[]{ChatColor.GOLD+"0.2 ���./��."}, null, null));
			double price = GUI.CountOfItem(Material.IRON_NUGGET, ChatColor.GRAY+"�������", p)*0.5;
			inv.setItem(9, ItemUtil.create(Material.IRON_NUGGET, 1, 0, ChatColor.RED+"���������� ������� � �������", new String[]{ChatColor.GOLD+"0.5 ���./��.",ChatColor.DARK_GREEN+"����� "+GepUtil.boolCol(pi.money>=price)+GepUtil.CylDouble(price, "#0.00")+" ���."}, null, null));
		}
		@Override public void click(InventoryClickEvent e) {
			switch (e.getSlot()) {
			case 11:{
				double sold=GepUtil.sellItems(p, Material.ROTTEN_FLESH, ChatColor.RED+"�����", 1000)*0.25;
				p.sendMessage(ChatColor.DARK_GREEN+"�� ������� ������ �� "+ChatColor.GREEN+GepUtil.CylDouble(sold, "#0.00")+" ���.");
				pi.money+=sold;
			}break;
			case 20:{
				double sold=GepUtil.sellItems(p, Material.POTATO, null, 1000)*0.1;
				p.sendMessage(ChatColor.DARK_GREEN+"�� ������� �������� �� "+ChatColor.GREEN+GepUtil.CylDouble(sold, "#0.00")+" ���.");
				pi.money+=sold;
			}break;
			case 13:{
				Invs.open(p, clotchGUI);
			}break;
			case 15:{
				double sold=GepUtil.sellItems(p, Material.GLASS_BOTTLE, null, 1000)*0.5;
				p.sendMessage(ChatColor.BLUE+"�� ������� ������� �� "+ChatColor.GREEN+GepUtil.CylDouble(sold, "#0.00")+" ���.");
				pi.money+=sold;
			}break;
			case 0:{
				int am = GUI.CountOfItem(Material.IRON_NUGGET, ChatColor.GRAY+"�������", p);
				double price = am*0.2;
				p.sendMessage(ChatColor.RED+"�� ������� "+ChatColor.GOLD+am+" ������� "+ChatColor.DARK_GREEN+"�� "+GepUtil.CylDouble(price, "#0.00")+" ���.");
				pi.money+=price;
				GepUtil.sellItems(p, Material.IRON_NUGGET, ChatColor.GRAY+"�������", am);
			}break;
			case 9:{
				int am = GUI.CountOfItem(Material.IRON_NUGGET, ChatColor.GRAY+"�������", p);
				double price = am*0.5;
				p.sendMessage(ChatColor.RED+"�� ���������� "+ChatColor.GOLD+am+" ������� "+ChatColor.DARK_GREEN+"�� "+GepUtil.CylDouble(price, "#0.00")+" ���.");
				pi.money-=price;
				GepUtil.sellItems(p, Material.IRON_NUGGET, ChatColor.GRAY+"�������", am);
				p.getInventory().addItem(Items.more(Items.pickKey, am));
			}break;
			default:{
				
			}break;
			}
		}
	};
	public static final Inv clean = new Inv("&b�-��-�������)") {//�-��-������)
		@Override public void displItems(Inventory inv) {
			inv.setItem(new Random().nextInt(27), ItemUtil.create(Material.WATER_BUCKET, 1, 0, ChatColor.BLUE+"������", new String[]{ChatColor.GOLD+"JUST... CLICK!"}, null, null));
			p.openInventory(inv);
			int count=pi.waits.get("clean");
			GepUtil.HashMapReplacer(pi.waits, "clean", count, false, true);
			GepUtil.HashMapReplacer(pi.waits, "cleanning", count, false, true);
		}
		@Override public void click(InventoryClickEvent e) {
			if(e.getCurrentItem().getType().equals(Material.WATER_BUCKET)){
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_SWIM, 1, 2);
				if(GepUtil.HashMapReplacer(pi.waits, "cleanning", -main.r.nextInt(3)-1, true, false)){
					p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1, 2);
					pi.dirt--;
					if(pi.dirt<=0){
						p.closeInventory();
						p.sendMessage(ChatColor.AQUA+"�� ��������� �����!");
						return;
					}
					Invs.open(p, clean);
				}
			}
		}
	};
	public static final Inv clotchGUI = new Inv("&6������� ������") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(26, ItemUtil.create(Material.WHITE_STAINED_GLASS_PANE, 1, 0, ChatColor.GRAY+"�������� ������.", new String[]{ChatColor.AQUA+"����� ��������, ����,",ChatColor.AQUA+"����� ������ ����."}, null, null));
		}
		@Override public void click(InventoryClickEvent e) {
			ItemStack item = e.getCurrentItem();
			if(item==null){
				return;
			}
			if(item.getType().toString().contains("_STAINED_GLASS_PANE")){
				e.setCancelled(true);
				if(!item.getItemMeta().getDisplayName().equals(ChatColor.GRAY+"�������� ������.")){
					p.closeInventory();
					pi.money+=GepUtil.intFromString(item.getItemMeta().getDisplayName())/100.00;
					p.sendMessage(ChatColor.GOLD+"�� ������� ������ �� "+ChatColor.GREEN+GepUtil.CylDouble(GepUtil.intFromString(item.getItemMeta().getDisplayName())/100.00, "#0.00")+" ���.");
				}
				else{
					e.getInventory().setItem(26, GUI.clotchGuiItemUpdate(p, e.getInventory()));
				}
			}
			else if(!e.getInventory().getItem(26).getItemMeta().getDisplayName().equals(ChatColor.GRAY+"�������� ������.")){
				e.setCancelled(true);
				e.getInventory().setItem(26, ItemUtil.create(Material.WHITE_STAINED_GLASS_PANE, 1, 0, ChatColor.GRAY+"�������� ������.", new String[]{ChatColor.AQUA+"����� ��������, ����,",ChatColor.AQUA+"����� ������ ����."}, null, null));
				p.updateInventory();
			}else e.setCancelled(false);
		}
	}.unlocked();
	public static final Inv trash = new Inv("&2�������") {
		@Override public void displItems(Inventory inv) {
			GepUtil.HashMapReplacer(pi.waits, "trash", 0, true, true);
			for(int i=0;i<27;i++){
				inv.setItem(i, ItemUtil.create(Material.WHITE_STAINED_GLASS_PANE, 1, 0, ChatColor.LIGHT_PURPLE+"???", new String[]{ChatColor.GREEN+"����, ����� ������!"},  null, null));
			}
			GepUtil.HashMapReplacer(pi.waits, "trashwarns", pi.perkLvl("trash"), false, true);
			pi.timers.put("trash"+GepUtil.locInfo(pi.lastClickLoc), 300);
		}
		@Override public void click(InventoryClickEvent e) {
			ItemStack item = e.getCurrentItem();
			if(item==null){
				e.setCancelled(false);
				return;
			}
			if(GepUtil.isFullyItem(item, ChatColor.LIGHT_PURPLE+"???", Material.WHITE_STAINED_GLASS_PANE, null)){
				e.setCancelled(true);
				p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 0.5f, 1);
				if(pi.waits.get("trash")!=null&&pi.waits.get("trash")>7&&pi.lvl<2){
					p.sendMessage(ChatColor.RED+"�� 2 ������ �� �� ������ ������ � ������� ������, ��� �� 7.");
					return;
				}
				HashMap<String,Integer> drops = new HashMap<>();
				drops.put("nothing", 500);
				drops.put("bottle", 100);
				drops.put("clotch", 7);
				drops.put("apple", 50);
				drops.put("bread", 50);
				drops.put("potato", 50);
				drops.put("prosrok", 100);
				drops.put("trash", 175);
				drops.put("key", 25);
				drops.put("pills", 15);
				GepUtil.HashMapReplacer(pi.waits, "trash", 1, false, false);
				String drop = GepUtil.chancesByCoef(drops);
				if(drop.equals("trash")){
					pi.addPerk(p, ChatColor.RED+"�������� ������", "trash", 1, 50);
					double trCh=0.5;
					List<String> lore = new ArrayList<>();
					lore.add(ChatColor.GOLD+"������� ���� �������: "+ChatColor.YELLOW+"50%");
					trCh+=pi.perkLvl("trash")*0.05;
					lore.add(ChatColor.GOLD+"������� ������: "+ChatColor.GREEN+"+"+pi.perkLvl("trash")*5+"%");
					trCh-=pi.waits.get("trash")*0.01;
					lore.add(ChatColor.GOLD+"�������: "+ChatColor.RED+"-"+pi.waits.get("trash")+"%");
					lore.add(ChatColor.LIGHT_PURPLE+"�������� ����: "+ChatColor.GOLD+GepUtil.CylDouble(trCh*100, "#0")+"%");
					ItemStack trash=null;
					if(new Random().nextDouble()<=trCh){
						trash=ItemUtil.create(Material.ORANGE_STAINED_GLASS_PANE, 1, 1, ChatColor.RED+"����!", new String[]{
								ChatColor.RED+"�� ���� �� �������� � �����-��",
								ChatColor.RED+"����! ���� ���� ����� '�������� ������'."
								}, null, null);
					}
					else{
						p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 2, 0);
						p.sendMessage(ChatColor.RED+"׸��! �� ��������� �� �����! �� ���������� � ��, ��!");
						trash=ItemUtil.create(Material.RED_STAINED_GLASS_PANE, 1, 14, ChatColor.RED+"ר��!", new String[]{
								ChatColor.RED+"�� �������� � �����-�� ����!"
								}, null, null);
						int d = new Random().nextInt(5)+1;
						if(GepUtil.isFullyItem(p.getInventory().getChestplate(), null, null, "����")){
							int dirt = GepUtil.intFromLore(p.getInventory().getChestplate(), "����");
							d-=GepUtil.randCoef(0, dirt/2);
						}
						if(d<1)d=1;
						pi.dirt+=d;
					}
					ItemMeta meta = trash.getItemMeta();
					List<String> lore2 = meta.getLore();
					for(String st:lore){
						lore2.add(st);
					}
					meta.setLore(lore2);
					trash.setItemMeta(meta);
					e.getClickedInventory().setItem(e.getSlot(),trash);
				}
				else if(drop.equals("prosrok")){
					e.getClickedInventory().setItem(e.getSlot(),Items.genFood(Material.ROTTEN_FLESH, new Random().nextInt(3)+1, ChatColor.RED+"�����", 25, 65, 0, -50, 1, new String[]{
							ChatColor.GRAY+"��� ����� ���������.",
							ChatColor.DARK_PURPLE+"������� � ������."
					}));
					//e.getClickedInventory().setItem(e.getSlot(),Items.item("rotten",new Random().nextInt(4)+2));
					//e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.ROTTEN_FLESH, new Random().nextInt(3)+1, ChatColor.DARK_GREEN+"��������� ", new String[]{ChatColor.DARK_GREEN+"�����������: "+ChatColor.RED+"20%"}));
				}
				else if(drop.equals("bread")){
					e.getClickedInventory().setItem(e.getSlot(),Items.genFood(Material.BREAD, 1, ChatColor.GOLD+"���� � ��������", 20, 75, 0, -25, 1, new String[]{
							ChatColor.GRAY+"���� '������' ��� �����.",
							ChatColor.DARK_PURPLE+"������� � ������."
					}));
					//e.getClickedInventory().setItem(e.getSlot(),Items.item("rotten",new Random().nextInt(4)+2));
					//e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.ROTTEN_FLESH, new Random().nextInt(3)+1, ChatColor.DARK_GREEN+"��������� ", new String[]{ChatColor.DARK_GREEN+"�����������: "+ChatColor.RED+"20%"}));
				}
				else if(drop.equals("apple")){
					e.getClickedInventory().setItem(e.getSlot(),Items.genFood(Material.APPLE, 1, ChatColor.GREEN+"������������ ������", 1, 45, 0, -10, 1, new String[]{
							ChatColor.GRAY+"������ �������� � ����� �����.",
							ChatColor.DARK_PURPLE+"������� � ������."
					}));
					//e.getClickedInventory().setItem(e.getSlot(),Items.item("rotten",new Random().nextInt(4)+2));
					//e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.ROTTEN_FLESH, new Random().nextInt(3)+1, ChatColor.DARK_GREEN+"��������� ", new String[]{ChatColor.DARK_GREEN+"�����������: "+ChatColor.RED+"20%"}));
				}
				else if(drop.equals("nothing")){
					e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.LILY_PAD, 1, ChatColor.GRAY+"��������� ����������� �����", new String[]{ChatColor.GRAY+"������. ���� �� ������."}));
				}
				else if(drop.equals("pills")){
					HashMap<String,Integer> pills = new HashMap<>();
					pills.put("rand", 100);
					pills.put("������", 10);
					pills.put("������", 10);
					pills.put("�������", 10);
					String pill = GepUtil.chancesByCoef(pills);
					if(pill.equals("rand")){
						e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.RABBIT_FOOT, 1, ChatColor.BLUE+"��������", new String[]{
								ChatColor.AQUA+"������, � ���� �������� �������� ���� ��������...",
								ChatColor.AQUA+"��� �������� ��� ��� ��������...",
								ChatColor.AQUA+"������� �� �� �����, ��� ���� ���."
								}));
					}
					else{//26!
						e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.RABBIT_FOOT, 1, ChatColor.BLUE+"�������� "+ChatColor.GREEN+"�� ������� "+ChatColor.DARK_GREEN+pill, new String[]{
								ChatColor.AQUA+"������, � ���� �������� �������� ���� ��������...",
								ChatColor.GREEN+"��� �������� ��� � ���������!",
								ChatColor.AQUA+"������� �� �����, ��� ���� ���!"
								}));
					}
				}
				else if(drop.equals("clotch")){
					List<String> subs = new ArrayList<>();
					subs.add("����");
					subs.add("������");
					subs.add("����");
					e.getClickedInventory().setItem(e.getSlot(),Events.NewClotchCreate(new String[]{"������","�����","�����-������","�����","������","�������","�������"}, subs, new Random().nextInt(3)+1));
					if(pi.learnTitle!=null&&pi.learnTitle.contains("������")){
						p.sendMessage(ChatColor.GREEN+"���! �� ����� �! ��� ������ ����� �����.");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
						pi.learnMessage=ChatColor.GREEN+"��� �, ������ � ������� �� ��������... �� ��������� ������� ��� �������, �� �� ������ ������ � ��� ���� ��� ����-����. ��� ������� ��������, ����� ��������� ��� � ���� �������? "+ChatColor.YELLOW+"����� �������� ��������, ����� ����� ������� 10 ������� � ��������� � ������ � ���������, � �������� ��� �� ������� �������.";
						p.getInventory().addItem(Items.more(Items.bottle, 16));
						p.getInventory().addItem(Items.more(Items.alcbottle, 32));
						pi.learnTitle=ChatColor.DARK_GREEN+"�������� ������� ��������!";
					}
				}
				else if(drop.equals("potato")){
					e.getClickedInventory().setItem(e.getSlot(),Items.genFood(Material.POTATO, new Random().nextInt(4)+2, ChatColor.GOLD+"�������� �����", 7, 25, 0, -3, 3, new String[]{
							ChatColor.GRAY+"������� - �� ������ ������...",
							ChatColor.DARK_PURPLE+"������� � ������."
					}));
				}
				else if(drop.equals("bottle")){
					if(new Random().nextDouble()>0.2)e.getClickedInventory().setItem(e.getSlot(),Items.more(Items.bottle, new Random().nextInt(3)+1));
					else e.getClickedInventory().setItem(e.getSlot(),utilsB.Items.alcbottle);
				}
				else if(drop.equals("key")){
					e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.IRON_NUGGET, 1, 0, ChatColor.GRAY+"�������", new String[]{""}, null, null));
				}
			}
			else if(GepUtil.isFullyItem(item, ChatColor.RED+"����!", Material.ORANGE_STAINED_GLASS_PANE, null)||GepUtil.isFullyItem(item, ChatColor.RED+"ר��!", Material.RED_STAINED_GLASS_PANE, null)){
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED+"�� �� ���� � ��� ����� �������!");
			}
			else if(GepUtil.isFullyItem(item, null, Material.LILY_PAD, null)){
				e.setCancelled(true);
			}else e.setCancelled(false);
		}
	}.unlocked();
	/*public static final Inv mngshdsjkhdfm = new Inv() {
		@Override public void displItems(Inventory inv) {
			
		}
		@Override public void click(InventoryClickEvent e) {
			
		}
	};
	public static final Inv mngshdsjkhdfm = new Inv() {
		@Override public void displItems(Inventory inv) {
			
		}
		@Override public void click(InventoryClickEvent e) {
			
		}
	};
	public static final Inv mngshdsjkhdfm = new Inv() {
		@Override public void displItems(Inventory inv) {
			
		}
		@Override public void click(InventoryClickEvent e) {
			
		}
	};
	public static final Inv mngshdsjkhdfm = new Inv() {
		@Override public void displItems(Inventory inv) {
			
		}
		@Override public void click(InventoryClickEvent e) {
			
		}
	};
	public static final Inv mngshdsjkhdfm = new Inv() {
		@Override public void displItems(Inventory inv) {
			
		}
		@Override public void click(InventoryClickEvent e) {
			
		}
	};
	public static final Inv mngshdsjkhdfm = new Inv() {
		@Override public void displItems(Inventory inv) {
			
		}
		@Override public void click(InventoryClickEvent e) {
			
		}
	};*/
}