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
	
	public static final Inv Menu = new Inv("&aМенюшка") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(3, ItemUtil.create(Material.OAK_SIGN, 1, ChatColor.GREEN+"Глаз попрошайки", new String[]{ChatColor.AQUA+"Проложить линию до ближайшего",ChatColor.AQUA+"места попрошайничества.",GepUtil.boolString(ChatColor.GREEN+"Вкл. "+ChatColor.YELLOW+"(Клик, чтобы выкл.)", ChatColor.RED+"Выкл. "+ChatColor.YELLOW+"(Клик, чтобы вкл.)", pi.bools.contains("begEye"))}));
			inv.setItem(4, ItemUtil.create(Material.FLOWER_POT, 1, ChatColor.GREEN+"Ближайший пункт бутылок", new String[]{ChatColor.AQUA+"Проложить линию до ближайшего",ChatColor.AQUA+"пункта сдачи бутылок.",GepUtil.boolString(ChatColor.GREEN+"Вкл. "+ChatColor.YELLOW+"(Клик, чтобы выкл.)", ChatColor.RED+"Выкл. "+ChatColor.YELLOW+"(Клик, чтобы вкл.)", pi.bools.contains("bottleEye"))}));
			inv.setItem(5, ItemUtil.create(Material.WATER_BUCKET, 1, ChatColor.GOLD+"Ближайший приют", new String[]{ChatColor.AQUA+"Проложить линию до ближайшего",ChatColor.AQUA+"приюта.",GepUtil.boolString(ChatColor.GREEN+"Вкл. "+ChatColor.YELLOW+"(Клик, чтобы выкл.)", ChatColor.RED+"Выкл. "+ChatColor.YELLOW+"(Клик, чтобы вкл.)", pi.bools.contains("pEye"))}));
			
			inv.setItem(15, ItemUtil.create(Material.BLUE_BED, 1, ChatColor.BLUE+"Сон", new String[]{ChatColor.AQUA+"Где же я буду спать?"}));
			inv.setItem(18, ItemUtil.create(Material.RED_DYE, 1, 1, ChatColor.RED+"Болезни", new String[]{ChatColor.AQUA+"Что с тобой, "+p.getName().substring(0, 3)+"-y?"},null,0));
			
			inv.setItem(26, ItemUtil.create(Material.EXPERIENCE_BOTTLE, 1, ChatColor.AQUA+"Помощь", new String[]{ChatColor.GREEN+"Меню помощи"}));
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
	public static final Inv SleepGUI = new Inv("&1Сон") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(10, ItemUtil.create(Material.GRASS, 1, 0, ChatColor.GREEN+"Спать на земле", new String[]{
					ChatColor.GRAY+"Спать прямо на земле. Рядом с",
					ChatColor.GRAY+"червями, без одеяла, без чистоты.",
					ChatColor.GRAY+"По крайней мере, вас сложнее заметить."
			}, null, 0));
			inv.setItem(12, ItemUtil.create(Material.OAK_STAIRS, 1, 0, ChatColor.GREEN+"Спать на скамейке", new String[]{
					ChatColor.GRAY+"Скамейка. Выставить себя на показ.",
					ChatColor.GRAY+"По крайней мере, здесь чище и",
					ChatColor.GRAY+"по ней не ползают червяки.",
					ChatColor.YELLOW+"Нужно быть на скамейке."
			}, null, 0));
			inv.setItem(14, ItemUtil.create(Material.ORANGE_BED, 1, 0, ChatColor.GREEN+"Спать в своём доме", new String[]{
					ChatColor.GRAY+"Всё зависит от вашего дома.",
					ChatColor.GRAY+"Там вас точно не ограбят.",
					ChatColor.YELLOW+"Нужно быть в доме."
			}, null, 0));
			inv.setItem(16, ItemUtil.create(Material.BLUE_BED, 1, 1, ChatColor.GREEN+"Спать в приюте", new String[]{
					ChatColor.GRAY+"Вариант для бомжей-мажоров.",
					ChatColor.GRAY+"Вы можете ночевать в приюте, но",
					ChatColor.YELLOW+"это делается в самом приюте."
			}, null, 0));
		}
		@Override public void click(InventoryClickEvent e) {
			switch(e.getSlot()){
			case 10:{
				pi.sleep="grass";
				p.sendMessage(ChatColor.AQUA+"Вы выбрали сон на земле. Что ж, это ваш выбор.");
			}break;
			case 12:{
				if(pi.lastWalkMat.toString().contains("_STAIRS")){
					pi.sleep="stairs";
					p.sendMessage(ChatColor.AQUA+"Вы выбрали сон на скамейке. Что ж, это ваш выбор.");
				}else{
					p.sendMessage(ChatColor.GOLD+"Чтобы выбрать это, вы должны находиться на скамейке (блок деревянных ступенек)");
				}
			}break;
			default:{
				p.sendMessage(ChatColor.RED+"В разработке.");
			}break;
			}
			p.closeInventory();
		}
	};
	public static final Inv help = new Inv("&bПомощь") {
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
						p.sendMessage(ChatColor.AQUA+"[Помощь '"+h.name+ChatColor.AQUA+"' (Вкратце)]: "+ChatColor.ITALIC+ChatColor.WHITE+h.shrt);
					}
					else p.sendMessage(ChatColor.AQUA+"[Помощь '"+h.name+ChatColor.AQUA+"' (Подробно)]: "+ChatColor.ITALIC+ChatColor.WHITE+h.lng);
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
					return;
				}
			}
		}
	};
	public static final Inv medGUI = new Inv("&aБольница") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(1, ItemUtil.create(Material.COMPASS, 1, 0, ChatColor.AQUA+"Обследоваться", new String[]{
					ChatColor.GREEN+"раскрывает одну случайную болезнь.",
					ChatColor.YELLOW+"Цена: "+GepUtil.boolCol(pi.money>=25)+25
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
					p.sendMessage(ChatColor.GREEN+"У вас нет неизвестных болезней!");
				}else{
					String toUnl=avaliable.get(new Random().nextInt(avaliable.size()));
					for(disease d:pi.diseases){
						if(d.name.equals(toUnl)){
							if(pi.money>=25){
								pi.money-=25;
								d.searched=true;
								p.sendMessage(ChatColor.GOLD+"У вас обнаружена болезнь "+d.infoItem(pi).getItemMeta().getDisplayName());
								p.closeInventory();
							}else{
								p.sendMessage(ChatColor.RED+"Не буду я тебя обследывать бесплатно, бомжара!");
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
					p.sendMessage(ChatColor.RED+"Ошибка определения болезни.");
					return;
				}
				if(pi.money>=d.healPrice()){
					pi.money-=d.healPrice();
					d.toRemove-=d.toRemove*0.2;
					d.toNextHeal=300;
					p.sendMessage(ChatColor.AQUA+"Лечение болезни ускорено. "+ChatColor.GRAY+"(Ещё "+GepUtil.timeStr(d.toRemove)+")");
					p.closeInventory();
				}else{
					p.sendMessage(ChatColor.RED+"Бесплатно ты можешь пойти и съесть уголёк, найденный в помойке. ПРОВАЛИВАЙ!");
					p.closeInventory();
				}
			}
		}
	};
	public static final Inv Seller = new Inv("&6Скупщик") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(11, ItemUtil.create(Material.ROTTEN_FLESH, 1, 0, ChatColor.DARK_GREEN+"Продать помои", new String[]{ChatColor.GOLD+"0.25 руб./шт."}, null, null));
			inv.setItem(20, ItemUtil.create(Material.POTATO, 1, 0, ChatColor.YELLOW+"Продать картошку", new String[]{ChatColor.GOLD+"0.1 руб./шт."}, null, null));
			inv.setItem(13, ItemUtil.create(Material.LEATHER_CHESTPLATE, 1, 0, ChatColor.GOLD+"Продать одежду", new String[]{ChatColor.YELLOW+"(Ту, что выберите)"}, null, null));
			inv.setItem(15, ItemUtil.create(Material.GLASS_BOTTLE, 1, 0, ChatColor.BLUE+"Продать бутылки с остатками алкоголя", new String[]{ChatColor.GOLD+"0.50 руб./шт."}, null, null));
			inv.setItem(0, ItemUtil.create(Material.IRON_NUGGET, 1, 0, ChatColor.BLUE+"Продать скрепки", new String[]{ChatColor.GOLD+"0.2 руб./шт."}, null, null));
			double price = GUI.CountOfItem(Material.IRON_NUGGET, ChatColor.GRAY+"Скрепка", p)*0.5;
			inv.setItem(9, ItemUtil.create(Material.IRON_NUGGET, 1, 0, ChatColor.RED+"Переделать скрепки в отмычки", new String[]{ChatColor.GOLD+"0.5 руб./шт.",ChatColor.DARK_GREEN+"Всего "+GepUtil.boolCol(pi.money>=price)+GepUtil.CylDouble(price, "#0.00")+" руб."}, null, null));
		}
		@Override public void click(InventoryClickEvent e) {
			switch (e.getSlot()) {
			case 11:{
				double sold=GepUtil.sellItems(p, Material.ROTTEN_FLESH, ChatColor.RED+"Помои", 1000)*0.25;
				p.sendMessage(ChatColor.DARK_GREEN+"Вы продали помоев на "+ChatColor.GREEN+GepUtil.CylDouble(sold, "#0.00")+" руб.");
				pi.money+=sold;
			}break;
			case 20:{
				double sold=GepUtil.sellItems(p, Material.POTATO, null, 1000)*0.1;
				p.sendMessage(ChatColor.DARK_GREEN+"Вы продали картошки на "+ChatColor.GREEN+GepUtil.CylDouble(sold, "#0.00")+" руб.");
				pi.money+=sold;
			}break;
			case 13:{
				Invs.open(p, clotchGUI);
			}break;
			case 15:{
				double sold=GepUtil.sellItems(p, Material.GLASS_BOTTLE, null, 1000)*0.5;
				p.sendMessage(ChatColor.BLUE+"Вы продали бутылок на "+ChatColor.GREEN+GepUtil.CylDouble(sold, "#0.00")+" руб.");
				pi.money+=sold;
			}break;
			case 0:{
				int am = GUI.CountOfItem(Material.IRON_NUGGET, ChatColor.GRAY+"Скрепка", p);
				double price = am*0.2;
				p.sendMessage(ChatColor.RED+"Вы продали "+ChatColor.GOLD+am+" скрепок "+ChatColor.DARK_GREEN+"за "+GepUtil.CylDouble(price, "#0.00")+" руб.");
				pi.money+=price;
				GepUtil.sellItems(p, Material.IRON_NUGGET, ChatColor.GRAY+"Скрепка", am);
			}break;
			case 9:{
				int am = GUI.CountOfItem(Material.IRON_NUGGET, ChatColor.GRAY+"Скрепка", p);
				double price = am*0.5;
				p.sendMessage(ChatColor.RED+"Вы переделали "+ChatColor.GOLD+am+" скрепок "+ChatColor.DARK_GREEN+"за "+GepUtil.CylDouble(price, "#0.00")+" руб.");
				pi.money-=price;
				GepUtil.sellItems(p, Material.IRON_NUGGET, ChatColor.GRAY+"Скрепка", am);
				p.getInventory().addItem(Items.more(Items.pickKey, am));
			}break;
			default:{
				
			}break;
			}
		}
	};
	public static final Inv clean = new Inv("&bУ-мы-вааайся)") {//У-мы-ваайся)
		@Override public void displItems(Inventory inv) {
			inv.setItem(new Random().nextInt(27), ItemUtil.create(Material.WATER_BUCKET, 1, 0, ChatColor.BLUE+"Мыться", new String[]{ChatColor.GOLD+"JUST... CLICK!"}, null, null));
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
						p.sendMessage(ChatColor.AQUA+"Вы полностью чисты!");
						return;
					}
					Invs.open(p, clean);
				}
			}
		}
	};
	public static final Inv clotchGUI = new Inv("&6Продажа одежды") {
		@Override public void displItems(Inventory inv) {
			inv.setItem(26, ItemUtil.create(Material.WHITE_STAINED_GLASS_PANE, 1, 0, ChatColor.GRAY+"Положите одежду.", new String[]{ChatColor.AQUA+"Когда положите, клик,",ChatColor.AQUA+"чтобы узнать цену."}, null, null));
		}
		@Override public void click(InventoryClickEvent e) {
			ItemStack item = e.getCurrentItem();
			if(item==null){
				return;
			}
			if(item.getType().toString().contains("_STAINED_GLASS_PANE")){
				e.setCancelled(true);
				if(!item.getItemMeta().getDisplayName().equals(ChatColor.GRAY+"Положите одежду.")){
					p.closeInventory();
					pi.money+=GepUtil.intFromString(item.getItemMeta().getDisplayName())/100.00;
					p.sendMessage(ChatColor.GOLD+"Вы продали одежду за "+ChatColor.GREEN+GepUtil.CylDouble(GepUtil.intFromString(item.getItemMeta().getDisplayName())/100.00, "#0.00")+" руб.");
				}
				else{
					e.getInventory().setItem(26, GUI.clotchGuiItemUpdate(p, e.getInventory()));
				}
			}
			else if(!e.getInventory().getItem(26).getItemMeta().getDisplayName().equals(ChatColor.GRAY+"Положите одежду.")){
				e.setCancelled(true);
				e.getInventory().setItem(26, ItemUtil.create(Material.WHITE_STAINED_GLASS_PANE, 1, 0, ChatColor.GRAY+"Положите одежду.", new String[]{ChatColor.AQUA+"Когда положите, клик,",ChatColor.AQUA+"чтобы узнать цену."}, null, null));
				p.updateInventory();
			}else e.setCancelled(false);
		}
	}.unlocked();
	public static final Inv trash = new Inv("&2Мусорка") {
		@Override public void displItems(Inventory inv) {
			GepUtil.HashMapReplacer(pi.waits, "trash", 0, true, true);
			for(int i=0;i<27;i++){
				inv.setItem(i, ItemUtil.create(Material.WHITE_STAINED_GLASS_PANE, 1, 0, ChatColor.LIGHT_PURPLE+"???", new String[]{ChatColor.GREEN+"Клик, чтобы узнать!"},  null, null));
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
					p.sendMessage(ChatColor.RED+"До 2 уровня вы не можете рыться в мусорке глубже, чем на 7.");
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
					pi.addPerk(p, ChatColor.RED+"Мусорное зрение", "trash", 1, 50);
					double trCh=0.5;
					List<String> lore = new ArrayList<>();
					lore.add(ChatColor.GOLD+"Базовый шанс уворота: "+ChatColor.YELLOW+"50%");
					trCh+=pi.perkLvl("trash")*0.05;
					lore.add(ChatColor.GOLD+"Влияние навыка: "+ChatColor.GREEN+"+"+pi.perkLvl("trash")*5+"%");
					trCh-=pi.waits.get("trash")*0.01;
					lore.add(ChatColor.GOLD+"Глубина: "+ChatColor.RED+"-"+pi.waits.get("trash")+"%");
					lore.add(ChatColor.LIGHT_PURPLE+"Итоговый шанс: "+ChatColor.GOLD+GepUtil.CylDouble(trCh*100, "#0")+"%");
					ItemStack trash=null;
					if(new Random().nextDouble()<=trCh){
						trash=ItemUtil.create(Material.ORANGE_STAINED_GLASS_PANE, 1, 1, ChatColor.RED+"СТОП!", new String[]{
								ChatColor.RED+"Ты чуть не вляпался в какую-то",
								ChatColor.RED+"жижу! Тебя спас навык 'Мусорное зрение'."
								}, null, null);
					}
					else{
						p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 2, 0);
						p.sendMessage(ChatColor.RED+"Чёрт! Ты наткнулся на грязь! Ты испачкался в нём, фу!");
						trash=ItemUtil.create(Material.RED_STAINED_GLASS_PANE, 1, 14, ChatColor.RED+"ЧЁРТ!", new String[]{
								ChatColor.RED+"Ты вляпался в какую-то жижу!"
								}, null, null);
						int d = new Random().nextInt(5)+1;
						if(GepUtil.isFullyItem(p.getInventory().getChestplate(), null, null, "гряз")){
							int dirt = GepUtil.intFromLore(p.getInventory().getChestplate(), "гряз");
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
					e.getClickedInventory().setItem(e.getSlot(),Items.genFood(Material.ROTTEN_FLESH, new Random().nextInt(3)+1, ChatColor.RED+"Помои", 25, 65, 0, -50, 1, new String[]{
							ChatColor.GRAY+"Для самых отчаянных.",
							ChatColor.DARK_PURPLE+"Найдено в помоях."
					}));
					//e.getClickedInventory().setItem(e.getSlot(),Items.item("rotten",new Random().nextInt(4)+2));
					//e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.ROTTEN_FLESH, new Random().nextInt(3)+1, ChatColor.DARK_GREEN+"Просрочка ", new String[]{ChatColor.DARK_GREEN+"Токсичность: "+ChatColor.RED+"20%"}));
				}
				else if(drop.equals("bread")){
					e.getClickedInventory().setItem(e.getSlot(),Items.genFood(Material.BREAD, 1, ChatColor.GOLD+"Хлеб с плесенью", 20, 75, 0, -25, 1, new String[]{
							ChatColor.GRAY+"Хлеб 'ракфор' для элиты.",
							ChatColor.DARK_PURPLE+"Найдено в помоях."
					}));
					//e.getClickedInventory().setItem(e.getSlot(),Items.item("rotten",new Random().nextInt(4)+2));
					//e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.ROTTEN_FLESH, new Random().nextInt(3)+1, ChatColor.DARK_GREEN+"Просрочка ", new String[]{ChatColor.DARK_GREEN+"Токсичность: "+ChatColor.RED+"20%"}));
				}
				else if(drop.equals("apple")){
					e.getClickedInventory().setItem(e.getSlot(),Items.genFood(Material.APPLE, 1, ChatColor.GREEN+"Надгрызенное яблоко", 1, 45, 0, -10, 1, new String[]{
							ChatColor.GRAY+"Слегка откусано и стоит много.",
							ChatColor.DARK_PURPLE+"Найдено в помоях."
					}));
					//e.getClickedInventory().setItem(e.getSlot(),Items.item("rotten",new Random().nextInt(4)+2));
					//e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.ROTTEN_FLESH, new Random().nextInt(3)+1, ChatColor.DARK_GREEN+"Просрочка ", new String[]{ChatColor.DARK_GREEN+"Токсичность: "+ChatColor.RED+"20%"}));
				}
				else if(drop.equals("nothing")){
					e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.LILY_PAD, 1, ChatColor.GRAY+"Абсолютно бесполезный мусор", new String[]{ChatColor.GRAY+"Совсем. Прям ну вообще."}));
				}
				else if(drop.equals("pills")){
					HashMap<String,Integer> pills = new HashMap<>();
					pills.put("rand", 100);
					pills.put("Диарея", 10);
					pills.put("Глисты", 10);
					pills.put("Гастрит", 10);
					String pill = GepUtil.chancesByCoef(pills);
					if(pill.equals("rand")){
						e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.RABBIT_FOOT, 1, ChatColor.BLUE+"Таблетка", new String[]{
								ChatColor.AQUA+"Похоже, у этой таблетки кончился срок годности...",
								ChatColor.AQUA+"Она валялась тут без упаковки...",
								ChatColor.AQUA+"Поэтому мы не знаем, для чего она."
								}));
					}
					else{//26!
						e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.RABBIT_FOOT, 1, ChatColor.BLUE+"Таблетка "+ChatColor.GREEN+"от болезни "+ChatColor.DARK_GREEN+pill, new String[]{
								ChatColor.AQUA+"Похоже, у этой таблетки кончился срок годности...",
								ChatColor.GREEN+"Она валялась тут с упаковкой!",
								ChatColor.AQUA+"Поэтому мы знаем, для чего она!"
								}));
					}
				}
				else if(drop.equals("clotch")){
					List<String> subs = new ArrayList<>();
					subs.add("Стар");
					subs.add("Помойн");
					subs.add("Рван");
					e.getClickedInventory().setItem(e.getSlot(),Events.NewClotchCreate(new String[]{"Куртка","Шапка","Шапка-ушанка","Кепка","Джинсы","Ботинки","Пуховик"}, subs, new Random().nextInt(3)+1));
					if(pi.learnTitle!=null&&pi.learnTitle.contains("одежду")){
						p.sendMessage(ChatColor.GREEN+"Ура! Ты нашёл её! Для начала сойдёт любая.");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
						pi.learnMessage=ChatColor.GREEN+"Что ж, лазить в мусорке ты научился... Ты наверняка находил там бутылки, но на всякий случай я дам тебе ещё чуть-чуть. Там остался алкоголь, может перельёшь его в одну бутылку? "+ChatColor.YELLOW+"Чтобы перелить алкоголь, нужно взять минимум 10 бутылок с остатками в курсор в инвентаре, и кликнуть ими по обычной бутылке.";
						p.getInventory().addItem(Items.more(Items.bottle, 16));
						p.getInventory().addItem(Items.more(Items.alcbottle, 32));
						pi.learnTitle=ChatColor.DARK_GREEN+"Изготовь бутылку алкоголя!";
					}
				}
				else if(drop.equals("potato")){
					e.getClickedInventory().setItem(e.getSlot(),Items.genFood(Material.POTATO, new Random().nextInt(4)+2, ChatColor.GOLD+"Картошка гниль", 7, 25, 0, -3, 3, new String[]{
							ChatColor.GRAY+"Главное - не съесть глазок...",
							ChatColor.DARK_PURPLE+"Найдено в помоях."
					}));
				}
				else if(drop.equals("bottle")){
					if(new Random().nextDouble()>0.2)e.getClickedInventory().setItem(e.getSlot(),Items.more(Items.bottle, new Random().nextInt(3)+1));
					else e.getClickedInventory().setItem(e.getSlot(),utilsB.Items.alcbottle);
				}
				else if(drop.equals("key")){
					e.getClickedInventory().setItem(e.getSlot(),ItemUtil.create(Material.IRON_NUGGET, 1, 0, ChatColor.GRAY+"Скрепка", new String[]{""}, null, null));
				}
			}
			else if(GepUtil.isFullyItem(item, ChatColor.RED+"СТОП!", Material.ORANGE_STAINED_GLASS_PANE, null)||GepUtil.isFullyItem(item, ChatColor.RED+"ЧЁРТ!", Material.RED_STAINED_GLASS_PANE, null)){
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED+"Да не буду я это говно трогать!");
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