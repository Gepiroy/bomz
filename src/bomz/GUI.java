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
		sitems.add(new ShopItem("mac", Items.genFood(Material.POTATO, 10, ChatColor.GOLD+"�������� ���", 1, 35, 2, 2, 3, new String[]{
				ChatColor.GRAY+"��� �� ������ ���������!",
				ChatColor.DARK_PURPLE+"������� � ��������."
		}),30.99));
		sitems.add(new ShopItem("mac", Items.genFood(Material.COOKED_CHICKEN, 1, ChatColor.GOLD+"������", 5, 250, 5, 0, 30, new String[]{
				ChatColor.GRAY+"������ ������� ���.",
				ChatColor.DARK_PURPLE+"������� � ��������."
		}),34.99));
		sitems.add(new ShopItem("mac", Items.genFood(Material.COOKED_BEEF, 1, ChatColor.GOLD+"�������", 14, 475, 7, 0, 50, new String[]{
				ChatColor.GRAY+"�� ������ ����������?!",
				ChatColor.DARK_PURPLE+"������� � ��������."
		}),49.99));
		sitems.add(new ShopItem("5", Items.genFood(Material.BREAD, 1, ChatColor.GOLD+"�������", 0, 225, 0, 0, 0, new String[]{
				ChatColor.GRAY+"� ��������� ��� �������, ���� � ����.",
				ChatColor.DARK_PURPLE+"������� � ��������."
		}),24.99));
		sitems.add(new ShopItem("5", Items.genFood(Material.COOKIE, 5, ChatColor.GOLD+"��������", 1, 35, 0, 20, 5, new String[]{
				ChatColor.GRAY+"�����, ��������)",
				ChatColor.DARK_PURPLE+"������� � ��������."
		}),29.99));
		sitems.add(new ShopItem("5", Items.genFood(Material.ROTTEN_FLESH, 1, ChatColor.DARK_GREEN+"���������", 10, 65, 0, -1, 1, new String[]{
				ChatColor.GRAY+"�� ������ ��� ����������...",
				ChatColor.DARK_PURPLE+"������� � ��������."
		}),3.99));
		sitems.add(new ShopItem("5", ItemUtil.create(Material.POTION, 1, ChatColor.LIGHT_PURPLE+"����", new String[]{ChatColor.LIGHT_PURPLE+"��������� ������� ��� �� "+ChatColor.GREEN+"20 ����� �����."}), 20.99));
		sitems.add(new ShopItem("vasya", Items.genFood(Material.BREAD, 5, ChatColor.GOLD+"���������� ����", 9, 50, 0, -1, 0, new String[]{
				ChatColor.GRAY+"��� �� ����� ���������!",
				ChatColor.DARK_PURPLE+"������� � ������."
		}), 1, "vodka"));
		sitems.add(new ShopItem("vasya", utilsB.Items.more(utilsB.Items.bottle,16), 1, "vodka"));
		sitems.add(new ShopItem("vasya", utilsB.Items.more(utilsB.Items.bottle,64), 3, "vodka"));
		sitems.add(new ShopItem("vasya", ItemUtil.create(Material.OAK_SIGN, 1, ChatColor.GOLD+"��������", new String[]{ChatColor.YELLOW+"��������� ��������������."}), 2, "vodka"));
		sitems.add(new ShopItem("vasya", ItemUtil.createTool(Material.IRON_SWORD, ChatColor.GOLD+"������ ���", new String[]{
				ChatColor.GRAY+"����������� ����� ��� �����.",
				ChatColor.GRAY+"������� �������, �������, ������.",
				ChatColor.GREEN+"���������: "+ChatColor.YELLOW+200
		}, null, null), 125.0));
		sitems.add(new ShopItem("paper", ItemUtil.create(Material.PAPER, 1, ChatColor.BLUE+"������� ������", new String[]{ChatColor.YELLOW+"����������, ����� ������� ������."}), 2.99));
		sitems.add(new ShopItem("paper", ItemUtil.create(Material.PAPER, 1, ChatColor.GOLD+"������� ���������", new String[]{ChatColor.YELLOW+"��������� ������ ������� ����."}), 5.99));
		sitems.add(new ShopItem("���������", ItemUtil.createArmorColored(Material.LEATHER_HELMET, "�����", new String[]{"����������","������"}, 0, 0, 0), 75.0));
		sitems.add(new ShopItem("���������", ItemUtil.createArmorColored(Material.LEATHER_CHESTPLATE, "������", new String[]{"����������","������"}, 150, 150, 150), 120.0));
		sitems.add(new ShopItem("���������", ItemUtil.createArmorColored(Material.LEATHER_LEGGINGS, "������", new String[]{"����������","������"}, 100, 100, 200), 100.0));
		sitems.add(new ShopItem("���������", ItemUtil.create(Material.LEATHER_BOOTS, 1, "�������", new String[]{"����������","������"}), 60.0));
		sitems.add(new ShopItem("���������", ItemUtil.create(Material.LEATHER_HELMET, 1, "�����-������", new String[]{"����������","������"}), 105.0));
		sitems.add(new ShopItem("���������", ItemUtil.create(Material.LEATHER_CHESTPLATE, 1, "�������", new String[]{"����������","������"}), 180.0));
	}
	public static ItemStack clotchGuiItemUpdate(Player p, Inventory inv){
		double totalPrice=0;
		for(int i=0;i<27;i++){
			ItemStack item = inv.getItem(i);
			if(item==null)continue;
			if(!GepUtil.loreContains(item, "�������")){
				continue;
			}
			double price=0;
			int tepl=GepUtil.intFromLore(item, "�������");
			int comfort=GepUtil.intFromLore(item, "�������");
			int prestige=GepUtil.intFromLore(item, "�������");
			int dirt = GepUtil.intFromLore(item, "����");
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
		if(totalPrice>0)return ItemUtil.create(Material.ORANGE_STAINED_GLASS_PANE, 1, 0, ChatColor.DARK_GREEN+"������� �� "+ChatColor.GREEN+pricest+" ���.", null, null, null);
		else return ItemUtil.create(Material.WHITE_STAINED_GLASS_PANE, 1, 0, ChatColor.GRAY+"�������� ������.", new String[]{ChatColor.AQUA+"����� ��������, ����,",ChatColor.AQUA+"����� ������ ����."}, null, null);
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
			
			if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD+"�������� �����")){
				if(e.getClickedInventory()==e.getView().getTopInventory()&&e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD+"�������� �����")){
					if(e.getCurrentItem().getType().equals(Material.IRON_BARS)){
						e.setCancelled(true);
					}
				}
			}
			if(item!=null&&item.getItemMeta()!=null){//���� �������� �� �������!
				if(item.equals(Items.menu)&&!p.getGameMode().equals(GameMode.CREATIVE)){
					e.setCancelled(true);
					return;
				}
				if(e.getView().getTitle().equals(ChatColor.RED+"�������")){
					e.setCancelled(true);
					return;
				}
				if(e.getClickedInventory()==e.getView().getTopInventory()&&e.getView().getTitle().equalsIgnoreCase(ChatColor.RED+"����� ����� ��������")){
					if(pi.key.click(e)){
						if(pi.waits.containsKey("box")){
							if(pi.waits.get("box")==0){
								GepUtil.sellItems(p, Material.OAK_WOOD, ChatColor.GOLD+"������� � ����", 1);
								Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"������� � ����");
								for(int i=0;i<27;i++){
									String drop = GepUtil.chancesByCoef(new String[]{"bread","cookie","apple","alc",""}, new int[]{10,10,10,3,300});
									if(drop.equals("bread")){
										inv.setItem(i,ItemUtil.create(Material.BREAD, 1, ChatColor.GOLD+"�������", null));
									}
									if(drop.equals("cookie")){
										inv.setItem(i,ItemUtil.create(Material.COOKIE, 1, ChatColor.GOLD+"��������", null));
									}
									if(drop.equals("apple")){
										inv.setItem(i,ItemUtil.create(Material.APPLE, 1, ChatColor.RED+"������", null));
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
								p.sendMessage(ChatColor.GREEN+"�� ����� "+GepUtil.CylDouble(d, "#0.00")+" ���!");
							}
							Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"���� � ������");
							for(int i=0;i<27;i++){
								String drop = GepUtil.chances(new String[]{"bread","clotch","bottle","alc"}, new double[]{2,0.75,3,1});
								if(drop.equals("bread")){
									inv.setItem(i,ItemUtil.create(Material.BREAD, 1, ChatColor.GOLD+"�������", null));
								}
								else if(drop.equals("clotch")){
									List<String> subs = new ArrayList<>();
									subs.add("����");
									subs.add("����");
									subs.add("��������");
									subs.add("�������");
									subs.add("���������������");
									inv.setItem(i,Events.NewClotchCreate(new String[]{"������","�����","�����-������","������","�������","�������"}, subs, new Random().nextInt(3)+1));
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
					if(e.getView().getTitle().contains("����")){
						e.setCancelled(true);
						pi.bank.click(e);
					}
					if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD+"���")){
						e.setCancelled(true);
						House h = pi.house;
						if(h==null){
							e.setCancelled(true);
							p.sendMessage(ChatColor.RED+"������... ���� ���? ���������� ��� ��� �����...");
							p.closeInventory();
							return;
						}
						if(item.getType().equals(Material.EMERALD)){
							if(item.getItemMeta().getDisplayName().equals(ChatColor.GREEN+"���������� �� 24 ���")){
								if(pi.house!=null){
									p.sendMessage(ChatColor.RED+"� ��� ��� ���� ���. �������� ���, �����, ��� �� ��� ��� ������� ������ ����.");
								}
								else if(buy(h.price, p)){
									h.owner=p.getName();
									h.rentTime=24;
									pi.house=h;
									p.sendMessage(ChatColor.GREEN+"�� ������ ����� ���! �� ����� ����� ��� 24 ���. ����� ����� ����� �� ������ ������ ������ ���� ����� (��� ���� ��).");
									h.teleportHome(p);
								}
							}
							if(item.getItemMeta().getDisplayName().equals(ChatColor.RED+"������� ����")){
								pi.money+=h.price/10.0;
								h.owner=null;
								h.rentTime=0;
								pi.house=null;
								p.sendMessage(ChatColor.RED+"�� ������� ���� ���. ������ �� ����.");
							}
						}
						if(item.getType().equals(Material.IRON_DOOR)){
							p.sendMessage(ChatColor.GREEN+"����� ���������� �����!");
							h.teleportHome(p);
						}
						p.closeInventory();
					}
				}
				if(e.getView().getTitle().equals(ChatColor.GOLD+"��� ������ ����?")){
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
									if(sitem.priceType.equals("money")&&buy(sitem.price, p)||sitem.priceType.equals("vodka")&&CanBuyItem((int) sitem.price, Material.POTION, ChatColor.AQUA+"������� � ���������", p)){
										if(shop.name.contains("������")||shop.name.contains("������")){
											Clotch cl=new Clotch(sitem.item.getItemMeta().getDisplayName());
											for(String st:sitem.item.getItemMeta().getLore()){
												st=st.substring(0, st.length()-2);
												cl.addClotchType(st);
											}
											p.getInventory().addItem(cl.create());
										}
										else p.getInventory().addItem(sitem.item.clone());
										if(sitem.priceType.equals("money"))p.sendMessage(ChatColor.GREEN+"�� ������ ������� �� "+ChatColor.DARK_GREEN+sitem.price+" ���.");
										else if(sitem.priceType.equals("vodka")){
											BuyItem((int) sitem.price, Material.POTION, ChatColor.AQUA+"������� � ���������", p);
											p.sendMessage(ChatColor.GREEN+"�� ������ ������� �� "+ChatColor.BLUE+(int)sitem.price+" ������� � ���������.");
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
						if(pi.learnTitle.contains("��������")){
							pi.learnMessage=ChatColor.GREEN+"��� �, ������ � ���� ����� �������. ��� � ���� ������? ���������! ���������! "+ChatColor.YELLOW+"� ���� ���� GPS �� ���������� ������ ����� �������.";
							pi.learnTitle=ChatColor.BLUE+"������ �������!";
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
						}
					}
					else{
						p.getInventory().addItem(utilsB.Items.bottle);
						e.getCursor().setAmount(e.getCursor().getAmount()-1);
						pi.addPerk(p, ChatColor.GREEN+"'������ ����'", "hands", 5, 100);
						p.sendMessage(ChatColor.RED+"��, ��, ���������� ����... �� ������ ��� ������ ���� ������� �������� �� 1 �������!");
					}
				}
				if(e.getCursor().getType().equals(Material.IRON_SWORD)){
					if(GepUtil.isItem(item, "���� � ��������", Material.BREAD, null)){
						e.setCancelled(true);
						p.playSound(p.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
						item.setAmount(item.getAmount()-1);
						p.getInventory().addItem(Items.genFood(Material.BREAD, 1, ChatColor.GOLD+"���������� ����", 9, 50, 0, -1, 0, new String[]{
								ChatColor.GRAY+"��� �� ����� ���������!",
								ChatColor.DARK_PURPLE+"������� � ������."
						}));
						GepUtil.replaceLore(e.getCursor(), "���������", ChatColor.GREEN+"���������: "+ChatColor.GOLD+(GepUtil.intFromLore(e.getCursor(), "���������")-1));
					}
				}
			}
		}
	}
	public static boolean buy(double price, Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		if(pi.money<price){
			p.sendMessage(ChatColor.RED+"�� ������� �����.");
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
