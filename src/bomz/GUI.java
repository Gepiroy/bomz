package bomz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ObjectsB.Clotch;
import ObjectsB.House;
import ObjectsB.PlayerInfo;
import ObjectsB.Shaurma;
import ObjectsB.Shop;
import ObjectsB.ShopItem;
import invsUtil.Inv;
import invsUtil.InvEvents;
import invsUtil.Invs;
import utilsB.GepUtil;
import utilsB.ItemUtil;
import utilsB.Items;

public class GUI implements Listener{
	public static ArrayList<ShopItem> sitems = new ArrayList<>();
	public static void setItems(){
		sitems.add(new ShopItem("mac", Items.genFood(Material.POTATO, 10, ChatColor.GOLD+"Картошка фри", 1, 35, 2, 2, 3, new String[]{
				ChatColor.GRAY+"Фри же значит бесплатно!",
				ChatColor.DARK_PURPLE+"Куплено в фастфуде."
		}),30.99));
		sitems.add(new ShopItem("mac", Items.genFood(Material.COOKED_CHICKEN, 1, ChatColor.GOLD+"Бургер", 5, 250, 5, 0, 30, new String[]{
				ChatColor.GRAY+"Вполне хорошая еда.",
				ChatColor.DARK_PURPLE+"Куплено в фастфуде."
		}),34.99));
		sitems.add(new ShopItem("mac", Items.genFood(Material.COOKED_BEEF, 1, ChatColor.GOLD+"БигФаск", 14, 475, 7, 0, 50, new String[]{
				ChatColor.GRAY+"Вы готовы обожраться?!",
				ChatColor.DARK_PURPLE+"Куплено в фастфуде."
		}),49.99));
		sitems.add(new ShopItem("5", Items.genFood(Material.BREAD, 1, ChatColor.GOLD+"Буханка", 0, 225, 0, 0, 0, new String[]{
				ChatColor.GRAY+"У некоторых это завтрак, обед и ужин.",
				ChatColor.DARK_PURPLE+"Куплено в магазине."
		}),24.99));
		sitems.add(new ShopItem("5", Items.genFood(Material.COOKIE, 5, ChatColor.GOLD+"Печенька", 1, 35, 0, 20, 5, new String[]{
				ChatColor.GRAY+"Уряяя, печеньки)",
				ChatColor.DARK_PURPLE+"Куплено в магазине."
		}),29.99));
		sitems.add(new ShopItem("5", Items.genFood(Material.ROTTEN_FLESH, 1, ChatColor.DARK_GREEN+"Просрочка", 10, 65, 0, -1, 1, new String[]{
				ChatColor.GRAY+"По закону она бесплатная...",
				ChatColor.DARK_PURPLE+"Куплено в магазине."
		}),3.99));
		sitems.add(new ShopItem("5", ItemUtil.create(Material.POTION, 1, ChatColor.LIGHT_PURPLE+"Духи", new String[]{ChatColor.LIGHT_PURPLE+"Мгновенно очищает вас на "+ChatColor.GREEN+"20 очков грязи."}), 20.99));
		sitems.add(new ShopItem("vasya", Items.genFood(Material.BREAD, 5, ChatColor.GOLD+"Обрезанный хлеб", 9, 50, 0, -1, 0, new String[]{
				ChatColor.GRAY+"Уже не такой противный!",
				ChatColor.DARK_PURPLE+"Найдено в помоях."
		}), 1, "vodka"));
		sitems.add(new ShopItem("vasya", utilsB.Items.more(utilsB.Items.bottle,16), 1, "vodka"));
		sitems.add(new ShopItem("vasya", utilsB.Items.more(utilsB.Items.bottle,64), 3, "vodka"));
		sitems.add(new ShopItem("vasya", ItemUtil.create(Material.OAK_SIGN, 1, ChatColor.GOLD+"Картонка", new String[]{ChatColor.YELLOW+"Позволяет попрошайничать."}), 2, "vodka"));
		sitems.add(new ShopItem("vasya", ItemUtil.createTool(Material.IRON_SWORD, ChatColor.GOLD+"Старый нож", new String[]{
				ChatColor.GRAY+"Дряхленький ножик для резки.",
				ChatColor.GRAY+"Срезать плесень, пожалуй, сможет.",
				ChatColor.GREEN+"Прочность: "+ChatColor.YELLOW+200
		}, null, null), 125.0));
		sitems.add(new ShopItem("paper", ItemUtil.create(Material.PAPER, 1, ChatColor.BLUE+"Прогноз погоды", new String[]{ChatColor.YELLOW+"Показывает, какая сегодня погода."}), 2.99));
		sitems.add(new ShopItem("paper", ItemUtil.create(Material.PAPER, 1, ChatColor.GOLD+"Новости экономики", new String[]{ChatColor.YELLOW+"Позволяет узнать мировые цены."}), 5.99));
		sitems.add(new ShopItem("РынОдежда", ItemUtil.createArmorColored(Material.LEATHER_HELMET, "Шапка", new String[]{"Потрёпанная","Старая"}, 0, 0, 0), 75.0));
		sitems.add(new ShopItem("РынОдежда", ItemUtil.createArmorColored(Material.LEATHER_CHESTPLATE, "Куртка", new String[]{"Потрёпанная","Старая"}, 150, 150, 150), 120.0));
		sitems.add(new ShopItem("РынОдежда", ItemUtil.createArmorColored(Material.LEATHER_LEGGINGS, "Джинсы", new String[]{"Потрёпанные","Старые"}, 100, 100, 200), 100.0));
		sitems.add(new ShopItem("РынОдежда", ItemUtil.create(Material.LEATHER_BOOTS, 1, "Ботинки", new String[]{"Потрёпанные","Старые"}), 60.0));
		sitems.add(new ShopItem("РынОдежда", ItemUtil.create(Material.LEATHER_HELMET, 1, "Шапка-ушанка", new String[]{"Потрёпанная","Старая"}), 105.0));
		sitems.add(new ShopItem("РынОдежда", ItemUtil.create(Material.LEATHER_CHESTPLATE, 1, "Пуховик", new String[]{"Потрёпанный","Старый"}), 180.0));
	}
	public static ItemStack clotchGuiItemUpdate(Player p, Inventory inv){
		double totalPrice=0;
		for(int i=0;i<27;i++){
			ItemStack item = inv.getItem(i);
			if(item==null)continue;
			if(!GepUtil.loreContains(item, "Теплота")){
				continue;
			}
			double price=0;
			int tepl=GepUtil.intFromLore(item, "Теплота");
			int comfort=GepUtil.intFromLore(item, "Комфорт");
			int prestige=GepUtil.intFromLore(item, "Престиж");
			int dirt = GepUtil.intFromLore(item, "гряз");
			price+=0.25*tepl;
			price+=0.3*dirt;
			if(comfort>0)price+=0.5*comfort;
			else price-=0.25*-comfort;
			if(prestige>0)price+=1.0*prestige;
			else price-=0.25*-prestige;
			if(price<0.1)price=0.1;
			price*=14;
			totalPrice+=price;
		}
		String pricest=GepUtil.CylDouble(totalPrice, "#0.00");
		if(totalPrice>0)return ItemUtil.create(Material.ORANGE_STAINED_GLASS_PANE, 1, 0, ChatColor.DARK_GREEN+"Продать за "+ChatColor.GREEN+pricest+" руб.", null, null, null);
		else return ItemUtil.create(Material.WHITE_STAINED_GLASS_PANE, 1, 0, ChatColor.GRAY+"Положите одежду.", new String[]{ChatColor.AQUA+"Когда положите, клик,",ChatColor.AQUA+"чтобы узнать цену."}, null, null);
	}
	@EventHandler
	public void drop(PlayerDropItemEvent e){
		ItemStack item=e.getItemDrop().getItemStack();
		if(item.equals(Items.menu)){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void click(InventoryClickEvent e){
		if(e.getClickedInventory() != null) {
			Player p = (Player) e.getWhoClicked();
			PlayerInfo pi = Events.plist.get(p.getName());
			ItemStack item = e.getCurrentItem();
			
			for(Inv inv:InvEvents.invs){
				if(inv.title(e.getView().getTitle())){
					Invs.event(e);
					return;
				}
			}
			
			if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD+"Домашний склад")){
				if(e.getClickedInventory()==e.getView().getTopInventory()&&e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD+"Домашний склад")){
					if(e.getCurrentItem().getType().equals(Material.IRON_BARS)){
						e.setCancelled(true);
					}
				}
			}
			if(item!=null&&item.getItemMeta()!=null){//ЕСЛИ КЛИКНУЛИ НА ПРЕДМЕТ!
				if(item.equals(Items.menu)&&!p.getGameMode().equals(GameMode.CREATIVE)){
					e.setCancelled(true);
					return;
				}
				if(e.getView().getTitle().equals(ChatColor.RED+"Болезни")){
					e.setCancelled(true);
					return;
				}
				if(e.getClickedInventory()==e.getView().getTopInventory()&&e.getView().getTitle().equalsIgnoreCase(ChatColor.RED+"Взлом замка отмычкой")){
					if(pi.key.click(e)){
						if(pi.waits.containsKey("box")){
							if(pi.waits.get("box")==0){
								GepUtil.sellItems(p, Material.OAK_WOOD, ChatColor.GOLD+"Коробка с едой", 1);
								Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Коробка с едой");
								for(int i=0;i<27;i++){
									String drop = GepUtil.chancesByCoef(new String[]{"bread","cookie","apple","alc",""}, new int[]{10,10,10,3,300});
									if(drop.equals("bread")){
										inv.setItem(i,ItemUtil.create(Material.BREAD, 1, ChatColor.GOLD+"Буханка", null));
									}
									if(drop.equals("cookie")){
										inv.setItem(i,ItemUtil.create(Material.COOKIE, 1, ChatColor.GOLD+"Печенька", null));
									}
									if(drop.equals("apple")){
										inv.setItem(i,ItemUtil.create(Material.APPLE, 1, ChatColor.RED+"Яблоко", null));
									}
									else if(drop.equals("alc")){
										inv.setItem(i,Items.alcohol);
									}
								}
								p.openInventory(inv);
							}
						}
						if(pi.lastClickLoc.getBlock().getType().equals(Material.TRAPPED_CHEST)){
							double d = new Random().nextInt(21)*0.25;
							if(d>0){
								p.sendMessage(ChatColor.GREEN+"Вы нашли "+GepUtil.CylDouble(d, "#0.00")+" руб!");
							}
							Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Ящик с замком");
							for(int i=0;i<27;i++){
								String drop = GepUtil.chances(new String[]{"bread","clotch","bottle","alc"}, new double[]{2,0.75,3,1});
								if(drop.equals("bread")){
									inv.setItem(i,ItemUtil.create(Material.BREAD, 1, ChatColor.GOLD+"Буханка", null));
								}
								else if(drop.equals("clotch")){
									List<String> subs = new ArrayList<>();
									subs.add("Стар");
									subs.add("Рван");
									subs.add("Потрёпанн");
									subs.add("Утеплённ");
									subs.add("Водоотталкивающ");
									inv.setItem(i,Events.NewClotchCreate(new String[]{"Куртка","Шапка","Шапка-ушанка","Джинсы","Ботинки","Пуховик"}, subs, new Random().nextInt(3)+1));
								}
								else if(drop.equals("bottle")){
									inv.setItem(i,Items.more(Items.bottle, new Random().nextInt(10)+1));
								}
								else if(drop.equals("alc")){
									inv.setItem(i,Items.alcohol);
								}
							}
							p.openInventory(inv);
						}
					}
				}
				if(e.getClickedInventory()==e.getView().getTopInventory()){
					if(e.getView().getTitle().contains("Банк")){
						e.setCancelled(true);
						pi.bank.click(e);
					}
					if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD+"Дом")){
						e.setCancelled(true);
						House h = pi.house;
						if(h==null){
							e.setCancelled(true);
							p.sendMessage(ChatColor.RED+"Ошибка... Дома нет? Попробуйте ещё раз чтоли...");
							p.closeInventory();
							return;
						}
						if(item.getType().equals(Material.EMERALD)){
							if(item.getItemMeta().getDisplayName().equals(ChatColor.GREEN+"Арендовать на 24 дня")){
								if(pi.house!=null){
									p.sendMessage(ChatColor.RED+"У вас уже есть дом. Продайте его, чтоли, раз уж вам так хочется купить этот.");
								}
								else if(buy(h.price, p)){
									h.owner=p.getName();
									h.rentTime=24;
									pi.house=h;
									p.sendMessage(ChatColor.GREEN+"Вы купили новый дом! Он будет вашим ещё 24 дня. После этого срока вы должны будете купить себе новый (или этот же).");
									h.teleportHome(p);
								}
							}
							if(item.getItemMeta().getDisplayName().equals(ChatColor.RED+"Продать хату")){
								pi.money+=h.price/10.0;
								h.owner=null;
								h.rentTime=0;
								pi.house=null;
								p.sendMessage(ChatColor.RED+"Вы продали свой дом. Теперь вы БОМЖ.");
							}
						}
						if(item.getType().equals(Material.IRON_DOOR)){
							p.sendMessage(ChatColor.GREEN+"Добро пожаловать домой!");
							h.teleportHome(p);
						}
						p.closeInventory();
					}
				}
				if(e.getView().getTitle().equals(ChatColor.GOLD+"Что делать дома?")){
					pi.house.click(e);
				}
				for(Shaurma sha:main.shas){
					if(e.getClickedInventory()==e.getView().getTopInventory()&&e.getView().getTitle().equals(sha.bus.name)){
						sha.click(e);
					}
				}
				for(Shop shop:main.shops){
					if(e.getView().getTitle().equalsIgnoreCase(shop.name)){
						e.setCancelled(true);
						int i=0;
						for(ShopItem sitem:sitems){
							if(sitem.shop.equals(shop.shopType)){
								if(e.getSlot()==i){
									if(sitem.priceType.equals("money")&&buy(sitem.price, p)||sitem.priceType.equals("vodka")&&CanBuyItem((int) sitem.price, Material.POTION, ChatColor.AQUA+"Бутылка с алкоголем", p)){
										if(shop.name.contains("Одежда")||shop.name.contains("одежда")){
											Clotch cl=new Clotch(sitem.item.getItemMeta().getDisplayName());
											for(String st:sitem.item.getItemMeta().getLore()){
												st=st.substring(0, st.length()-2);
												cl.addClotchType(st);
											}
											p.getInventory().addItem(cl.create());
										}
										else p.getInventory().addItem(sitem.item.clone());
										if(sitem.priceType.equals("money"))p.sendMessage(ChatColor.GREEN+"Вы купили предмет за "+ChatColor.DARK_GREEN+sitem.price+" руб.");
										else if(sitem.priceType.equals("vodka")){
											BuyItem((int) sitem.price, Material.POTION, ChatColor.AQUA+"Бутылка с алкоголем", p);
											p.sendMessage(ChatColor.GREEN+"Вы купили предмет за "+ChatColor.BLUE+(int)sitem.price+" бутылок с алкоголем.");
										}
										shop.openShop(p);
									}
								}
								i++;
							}
						}
					}
				}
				if(GepUtil.isFullyItem(e.getCursor(), utilsB.Items.alcbottle)&&e.getCursor().getAmount()>=10&&GepUtil.isFullyItem(e.getCurrentItem(), utilsB.Items.bottle)){
					e.setCancelled(true);
					p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 2, 0);
					if(new Random().nextDouble()<=0.5+pi.perkLvl("hands")/100.00){
						p.getInventory().addItem(Items.alcohol);
						p.getInventory().addItem(utilsB.Items.more(utilsB.Items.bottle, 9));
						e.getCursor().setAmount(e.getCursor().getAmount()-10);
						if(pi.learnTitle.contains("Изготовь")){
							pi.learnMessage=ChatColor.GREEN+"Что ж, теперь у тебя полно бутылок. Что с ними делать? Правильно! Продавать! "+ChatColor.YELLOW+"В меню есть GPS до ближайшего пункта сдачи бутылок.";
							pi.learnTitle=ChatColor.BLUE+"Продай бутылки!";
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
						}
					}
					else{
						p.getInventory().addItem(utilsB.Items.bottle);
						e.getCursor().setAmount(e.getCursor().getAmount()-1);
						pi.addPerk(p, ChatColor.GREEN+"'Прямые руки'", "hands", 5, 100);
						p.sendMessage(ChatColor.RED+"Эх, ты, криворукий бомж... Ты только что пролил мимо остатки алкоголя из 1 бутылки!");
					}
				}
				if(e.getCursor().getType().equals(Material.IRON_SWORD)){
					if(GepUtil.isItem(item, "Хлеб с плесенью", Material.BREAD, null)){
						e.setCancelled(true);
						p.playSound(p.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
						item.setAmount(item.getAmount()-1);
						p.getInventory().addItem(Items.genFood(Material.BREAD, 1, ChatColor.GOLD+"Обрезанный хлеб", 9, 50, 0, -1, 0, new String[]{
								ChatColor.GRAY+"Уже не такой противный!",
								ChatColor.DARK_PURPLE+"Найдено в помоях."
						}));
						GepUtil.replaceLore(e.getCursor(), "Прочность", ChatColor.GREEN+"Прочность: "+ChatColor.GOLD+(GepUtil.intFromLore(e.getCursor(), "Прочность")-1));
					}
				}
			}
		}
	}
	public static boolean buy(double price, Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		if(pi.money<price){
			p.sendMessage(ChatColor.RED+"Не хватает денег.");
			return false;
		}
		else{
			pi.money-=price;
			return true;
		}
	}
	public static boolean CanBuyItem(int count, Material mat, String name, Player p){
		int have=0;
		for(ItemStack item:p.getInventory()){
			if(GepUtil.isItem(item, name, mat, null)){
				have+=item.getAmount();
			}
		}
		return have>=count;
	}
	public static int CountOfItem(Material mat, String name, Player p){
		int have=0;
		for(ItemStack item:p.getInventory()){
			if(GepUtil.isItem(item, name, mat, null)){
				have+=item.getAmount();
			}
		}
		return have;
	}
	static void BuyItem(int count, Material mat, String name, Player p){
		for(ItemStack item:p.getInventory()){
			if(GepUtil.isFullyItem(item, name, mat, null)){
				if(item.getAmount()<=count){
					count-=item.getAmount();
					item.setAmount(0);
				}
				else {
					item.setAmount(item.getAmount()-count);
					return;
				}
			}
		}
	}
}
