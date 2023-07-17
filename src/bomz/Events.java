package bomz;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import ObjectsB.BodyTemp;
import ObjectsB.BottlePlace;
import ObjectsB.Clotch;
import ObjectsB.House;
import ObjectsB.KeyBreak;
import ObjectsB.PlayerInfo;
import ObjectsB.Shaurma;
import ObjectsB.Shop;
import ObjectsB.disease;
import ObjectsB.pibus;
import ObjectsB.priut;
import invsUtil.InvEvents;
import invsUtil.Invs;
import utilsB.GepUtil;
import utilsB.ItemUtil;
import utilsB.Items;
import works.begging;
import works.loader;
import works.mcd;

public class Events implements Listener{
	public static HashMap<String, PlayerInfo> plist = new HashMap<>();
	
	@EventHandler
	public void j(PlayerJoinEvent e){
		doJoin(e.getPlayer());
	}
	public static void doJoin(Player p){
		File file = new File(main.instance.getDataFolder()+File.separator+"players"+File.separator+p.getName()+".yml");
		if(file.exists()){
			FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
			plist.put(p.getName(), new PlayerInfo(conf, p.getName()));
		}else{
			plist.put(p.getName(), new PlayerInfo(p.getName()));
			p.teleport(main.locs.get("spawn"));
		}
		
		//PlayerInfo pi = plist.get(p.getName());
		/*if(p.getGameMode().equals(GameMode.SURVIVAL)){
			if(main.locs.containsKey("spawn")&&pi.house==null)p.teleport(main.locs.get("spawn"));
			else if(pi.house!=null)pi.house.teleportHome(p);
		}*/
		Scoreboard newScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective o = newScoreboard.registerNewObjective("stats", ChatColor.GOLD+"��������� �����", "dummy");
		newScoreboard.registerNewObjective("buffer", "", "dummy");
		newScoreboard.registerNewObjective("t", "", "dummy");
		//o.setDisplayName(ChatColor.GOLD+"��������� �����");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(newScoreboard);
		main.updateScoreboard(p);
		p.getInventory().setItem(8, Items.menu);
	}
	@EventHandler
	public void l(PlayerQuitEvent e){
		doLeave(e.getPlayer());
	}
	public static void doLeave(Player p){
		PlayerInfo pi = plist.get(p.getName());
		if(pi.waits.containsKey("box"))pi.waits.remove("box");
		for(pibus bus:pi.buss){
			if(bus.name.contains("bottle")&&bus.own){
				for(BottlePlace place:main.bottlePlaces){
					if(place.owner.equals(p.getName())){
						place.changeOwner();
					}
				}
			}
		}
		File file = new File(main.instance.getDataFolder()+File.separator+"players"+File.separator+p.getName()+".yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		pi.save(conf);
		GepUtil.saveCfg(conf, file);
		plist.remove(p.getName());
	}
	static List<Material> ignores = new ArrayList<>();
	public static void setIgnores(){
		ignores.add(Material.AIR);
		ignores.add(Material.GRASS);
		ignores.add(Material.POPPY);
		ignores.add(Material.FIRE);
	}
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p =e.getPlayer();
		PlayerInfo pi = plist.get(p.getName());
		Material mat = p.getLocation().subtract(0,0.1,0).getBlock().getType();
		Material matIn = p.getLocation().getBlock().getType();
		if(matIn.equals(Material.WATER)){
			if(!pi.fastTimers.containsKey("watered")){
				pi.fastTimers.put("watered", 35);
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 2, 2);
				pi.dirt-=pi.dirt*0.2;
				pi.addDisease(new disease("���������", 160, 30));
			}
			return;
		}
		if(!ignores.contains(mat)){
			pi.lastWalkMat=mat;
		}
		if(p.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.DIRT)){
			double chance = e.getFrom().distance(e.getTo())/20.0;
			if(GepUtil.isFullyItem(p.getInventory().getBoots(), null, null, "����")){
				int dirt = GepUtil.intFromLore(p.getInventory().getBoots(), "����");
				chance-=dirt/100;
			}
			if(GepUtil.isFullyItem(p.getInventory().getLeggings(), null, null, "����")){
				int dirt = GepUtil.intFromLore(p.getInventory().getLeggings(), "����");
				chance-=dirt/100;
			}
			if(chance<0.001){
				chance=0.001;
			}
			if(new Random().nextDouble()<chance){
				pi.dirt++;
				p.sendMessage(ChatColor.RED+"��! �� �������� ���� � �����!");
			}
		}
		if(!p.getGameMode().equals(GameMode.CREATIVE)){
			Scoreboard s=Bukkit.getScoreboardManager().getMainScoreboard();
			Objective o=s.getObjective("jump");
			if(o.getScore(p.getName()).getScore()>0){
				if(!pi.fastTimers.containsKey("noJump")){
					int t=15-(int) (p.getWalkSpeed()*50);
					if(t<0)t=0;
					t+=10;
					pi.fastTimers.put("noJump", t);
				}
				else{
					e.setCancelled(true);
					int t=pi.fastTimers.get("noJump");
					p.sendTitle(ChatColor.GOLD+"���������� ������ "+ChatColor.YELLOW+GepUtil.CylDouble(t/10.0, "#0.0")+"�.",ChatColor.GRAY+"Ÿ ���� �� ���� ��������.", 3, 10, 10);
				}
				o.getScore(p.getName()).setScore(0);
			}
		}
	}
	@EventHandler
	public void arminteract(PlayerArmorStandManipulateEvent e){
		Player p=e.getPlayer();
		e.setCancelled(true);
		ArmorStand a=e.getRightClicked();
		Inventory inv = Bukkit.createInventory(null, 27, "test");
		int i=10;
		for(ItemStack item:a.getEquipment().getArmorContents()){
			inv.setItem(i, item);
			i+=2;
		}
		p.openInventory(inv);
	}
	@EventHandler
	public void interact(PlayerInteractEvent e){
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		PlayerInfo pi = plist.get(p.getName());
		ItemStack hitem = p.getInventory().getItemInMainHand();
		if (e.getHand()!=null&&!e.getHand().equals(EquipmentSlot.HAND)){return;}
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)||e.getAction().equals(Action.RIGHT_CLICK_AIR)){
			if(hitem.equals(Items.menu)){
				Invs.open(p, InvEvents.Menu);
				e.setCancelled(true);
				return;
			}
			ItemStack test = hitem.clone();
			test.setAmount(1);
			if(test.equals(Items.foodbox)){
				pi.waits.put("box", 0);
				pi.key=new KeyBreak(4);
				pi.key.openGUI(p);
				return;
			}
			if(GepUtil.isItem(hitem, ChatColor.LIGHT_PURPLE+"����", null, null)){
				int am=GepUtil.intFromLore(hitem, "�������");
				pi.dirt-=am;
				if(pi.dirt<0)pi.dirt=0;
				p.playSound(p.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, 1, 0);
				hitem.setAmount(hitem.getAmount()-1);
				e.setCancelled(true);
				return;
			}
			if(hitem.getType().equals(Material.RABBIT_FOOT)){
				String dname="";
				if(hitem.getItemMeta().getDisplayName().equals(ChatColor.BLUE+"��������")){
					String[] sts = {"������","�������","������"};
					dname=sts[new Random().nextInt(sts.length)];
				}else{
					dname=hitem.getItemMeta().getDisplayName().substring(26);
				}
				if(p.isOp())p.sendMessage("n="+dname);
				boolean healed=false;
				if(new Random().nextBoolean()){
					for(disease d:pi.diseases){
						if(d.name.equals(dname)){
							d.toRemove-=d.toRemove*(new Random().nextInt(3)+1)*0.01;
							d.preStage+=60;
							p.sendMessage(ChatColor.AQUA+"� �������� ������ ���!");
							healed=true;
						}
					}
				}
				if(!healed){
					p.sendMessage(ChatColor.RED+"���� � �� ����� ���, ��� ��� �����, ���� �������� �� ���������...");
				}
				hitem.setAmount(hitem.getAmount()-1);
				if(new Random().nextBoolean()){
					pi.addDisease(new disease("����������", 120, 30));
					p.sendMessage(ChatColor.DARK_GREEN+"������, � ��������� �� ���� ��������...");
				}
			}
			if(hitem.getType().equals(Material.PAPER)){
				e.setCancelled(true);
				if(GepUtil.isFullyItem(hitem, ChatColor.BLUE+"������� ������", null, null)){
					p.sendMessage(ChatColor.BLUE+"����� ������� ����������� ����� ���������� �� "+ChatColor.AQUA+(Temperature.center-5)+ChatColor.YELLOW+", � ��� ��� ����� ��������� �� "+ChatColor.GOLD+(Temperature.center+5));
				}
				if(GepUtil.isFullyItem(hitem, ChatColor.GOLD+"������� ���������", null, null)){
					double d=0;
					double dist=0;
					BottlePlace place=null;
					for(BottlePlace plac:main.bottlePlaces){
						if(plac.price>d){
							place=plac;
							d=plac.price;
							dist=plac.loc.distance(p.getLocation());
						}
					}
					p.sendMessage(ChatColor.GRAY+"������� ���� �� ������� ������ ���������� "+ChatColor.GREEN+main.bottlePrice+ChatColor.GRAY+". ������ �������� ����� ������� ������� � ������, ����������� �� ����������� "+ChatColor.GREEN+GepUtil.locInfo(place.loc)+", � �� ������ ���� "+place.price+"�./���. "+ChatColor.GRAY+"(��� ������ �� ���� "+GepUtil.CylDouble(dist, "#0.0")+" ������)");
					for(BottlePlace plac:main.bottlePlaces){
						if(p.getLocation().distance(plac.loc)<=dist){
							place=plac;
							dist=p.getLocation().distance(plac.loc);
							d=plac.price;
						}
					}
					p.sendMessage(ChatColor.YELLOW+"� ��������� �� ��� ������ "+ChatColor.GRAY+GepUtil.locInfo(place.loc)+ChatColor.YELLOW+" ������ ���� "+ChatColor.DARK_GREEN+place.price+"�./���. "+ChatColor.GRAY+"(��� ������ �� ���� "+GepUtil.CylDouble(dist, "#0.0")+" ������)");
				}
				hitem.setAmount(hitem.getAmount()-1);
				return;
			}
			if(hitem.getType().equals(Material.OAK_SIGN)&&p.getGameMode().equals(GameMode.SURVIVAL)){
				e.setCancelled(true);
				for(begging beg:main.beggs){
					if(p.getLocation().distance(beg.loc)<=1.5){
						if(pi.bools.contains("begging")){
							pi.toggleBool("begging", false);
							p.sendMessage(ChatColor.YELLOW+"�� �������� ��������������.");
						}
						else{
							pi.toggleBool("begging", true);
							p.sendMessage(ChatColor.YELLOW+"�� ����� ��������������.");
							p.sendMessage(ChatColor.AQUA+"���������� ����� ��� ������ - "+ChatColor.GREEN+beg.people+ChatColor.AQUA+".");
							GepUtil.HashMapReplacer(pi.waits, "beg"+GepUtil.locInfo(beg.loc), 0, false, false);
							p.sendMessage(ChatColor.RED+"���� ������������ ��� - "+pi.waits.get("beg"+GepUtil.locInfo(beg.loc)));
							if(pi.dirt>=10)p.sendMessage(ChatColor.RED+"�� �������! ����� �������� ��� ���������� �����! � ������ �������� ����� ����� ���������!");
						}
						return;
					}
				}
				p.sendMessage(ChatColor.RED+"��� �������������� ����������. "+ChatColor.YELLOW+"� ��� ������� - ��������� ������� � � '������ ����������'.");
				return;
			}
			if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){//���� �������� �� ����!!!!!!!!!!!!!!!!!!
				pi.lastClickLoc=b.getLocation();
				if(b.getType().equals(Material.TRAPPED_CHEST)){
					e.setCancelled(true);
					if(pi.timers.containsKey("chest"+GepUtil.locInfo(b.getLocation()))){
						p.sendMessage(ChatColor.RED+"�� ��� ��������� ���� ����. ������� ���� �� "+ChatColor.GOLD+GepUtil.CylDouble(pi.timers.get("chest"+GepUtil.locInfo(b.getLocation()))/60.0,"#0.0")+" ���."+ChatColor.RED+", ���� ��� �������� ������.");
						return;
					}
					pi.timers.put("chest"+GepUtil.locInfo(b.getLocation()), 300);
					pi.key=new KeyBreak(5);
					pi.key.openGUI(p);
					return;
				}
				if(b.getType().equals(Material.QUARTZ_STAIRS)&&b.getLocation().subtract(0,1,0).getBlock().getType().toString().contains("TERRACOTTA")){
					pi.bank.openGUI(p);
					return;
				}
				if(b.getType().equals(Material.CAULDRON)){
					if(pi.timers.containsKey("trash"+GepUtil.locInfo(b.getLocation()))){
						p.sendMessage(ChatColor.RED+"�� ��� ������� � ���� �������. ������� ���� �� "+ChatColor.GOLD+GepUtil.CylDouble(pi.timers.get("trash"+GepUtil.locInfo(b.getLocation()))/60.0,"#0.0")+" ���."+ChatColor.RED+", ���� � �� ��������� ���.");
						return;
					}
					Invs.open(p, InvEvents.trash);
				}
				if(b.getType().equals(Material.CHEST)){
					e.setCancelled(true);
					if(pi.bools.contains("inHouse")&&pi.house!=null){
						for(int i=0;i<54;i++){
							if(i<pi.house.storage&&pi.inv.getItem(i)!=null&&pi.inv.getItem(i).getType().equals(Material.IRON_BARS)){
								pi.inv.setItem(i, new ItemStack(Material.AIR));
							}
							if(pi.inv.getItem(i)==null&&i>=pi.house.storage){
								pi.inv.setItem(i, ItemUtil.create(Material.IRON_BARS, ChatColor.RED+"���������!"));
							}
						}
						p.openInventory(pi.inv);
					}
				}
				if(b.getType().toString().contains("_BED")){
					e.setCancelled(true);
					if(pi.bools.contains("inHouse")&&pi.house!=null){
						pi.house.BedGUI(p);
					}
				}
				if(pi.work.equals("mac")){
					works.mcd.click(b, p);
					e.setCancelled(true);
				}
				if(b.getType().equals(Material.OAK_WALL_SIGN)){
					if(pi.waits.containsKey("joinSign")){
						works.mcd.locs.put("joinSign", b.getLocation());
					}
					if(b.getLocation().equals(mcd.locs.get("joinSign"))){
						if(!main.hasArmored(p)){
							p.sendMessage(ChatColor.RED+"�� ����� ����, ��� � ��� ���� ������ ����! �������� �� ���� ���� ���-��.");
							return;
						}
						if(pi.prestige<0){
							p.sendMessage(ChatColor.RED+"���� ���, �� ��������� �����! ���������� ���-������ ��-���������.");
							return;
						}
						mcd.start(p);
					}
				}
				if(b.getType().equals(Material.IRON_DOOR)){
					for(Location l:main.meds){
						if(l.distance(b.getLocation())<=3){
							Invs.open(p, InvEvents.medGUI);
							return;
						}
					}
					for(House house:main.houses){
						if(b.getLocation().equals(house.door)){
							if(!house.whoInHouse.contains(p.getName())){
								house.openGUI(p);
							}
							else{
								Location loc = house.leaveLoc.clone();
								loc.setPitch(p.getLocation().getPitch());
								loc.setYaw(p.getLocation().getYaw());
								p.teleport(loc);
								house.whoInHouse.remove(p.getName());
								pi.toggleBool("inHouse", false);
							}
						}
					}
					for(priut pr:main.priuts){
						if(b.getLocation().equals(pr.loc)){
							pr.openGUI(p);
						}
					}
				}
				if(b.getType().equals(Material.BARRIER)){
					if(p.isOp()&&p.getGameMode().equals(GameMode.CREATIVE)){
						if(cmd.shopsWaits.containsKey(p.getName())){
							Shop w = cmd.shopsWaits.get(p.getName());
							main.shops.add(new Shop(b.getLocation(), w.shopType, w.name));
							cmd.shopsWaits.remove(p.getName());
						}
						else if(pi.waits.containsKey("bottleLoc")){
							main.bottlePlaces.add(new BottlePlace(b.getLocation()));
							pi.waits.remove("bottleLoc");
						}
						for(Shaurma sha:main.shas){
							if(sha.loc==null){
								sha.loc=b.getLocation();
								break;
							}
						}
					}
					for(BottlePlace place:main.bottlePlaces){
						if(b.getLocation().equals(place.loc))place.openShop(p);
					}
					for(Shaurma sha:main.shas){
						if(b.getLocation().equals(sha.loc))sha.openShop(p);
					}
					for(Shop shop:main.shops){
						if(shop.loc.equals(b.getLocation())){
							if(shop.name.equals("�������")){
								Invs.open(p, InvEvents.Seller);
								return;
							}
							shop.openShop(p);
							return;
						}
					}
				}
				for(loader l:main.loads){
					if(b.getLocation().equals(l.boxLoc)){
						if(pi.waits.containsKey("loader"+GepUtil.locInfo(l.putLoc))){
							p.sendMessage(ChatColor.RED+"�� ��� ����� �������.");
							return;
						}
						pi.waits.put("loader"+GepUtil.locInfo(l.putLoc), 0);
					}
				}
			}
		}
	}
	@EventHandler
	public void bp(BlockPlaceEvent e){
		e.setCancelled(true);
		Player p = e.getPlayer();
		PlayerInfo pi = plist.get(p.getName());
		if(p.isOp()&&p.getGameMode().equals(GameMode.CREATIVE)){
			Block b = e.getBlock();
			e.setCancelled(false);
			if(pi.waits.containsKey("loaderLoc")){
				for(loader l:main.loads){
					if(l.boxLoc==null){
						l.boxLoc=b.getLocation();
						p.sendMessage(ChatColor.GREEN+"��. �������� ����� ��������! :D");
					}
				}
				pi.waits.remove("loaderLoc");
			}
			if(b.getType().equals(Material.FLOWER_POT)){
				main.bottles.put(b.getLocation(), 1);
				p.sendMessage(ChatColor.YELLOW+"��������� ����� ������� � ���� ���.");
			}
			if(b.getType().equals(Material.IRON_DOOR)){
				if(pi.waits.containsKey("priutloc")){
					main.priuts.add(new priut(b.getLocation().add(0,1,0)));
					pi.waits.remove("priutloc");
				}
				for(House house:main.houses){
					if(house.door==null&&house.owner.equals(p.getName())){
						house.door=b.getLocation();
						house.door.setY(house.door.getY()+1);
						house.leaveLoc=p.getLocation();
						house.owner=null;
						p.sendMessage(ChatColor.GREEN+"����� ��� ������.");
					}
				}
			}
		}
	}
	@EventHandler
	public void bb(BlockBreakEvent e){
		Player p = e.getPlayer();
		PlayerInfo pi = plist.get(p.getName());
		Block b = e.getBlock();
		if(b.getType().equals(Material.FLOWER_POT)&&main.bottles.containsKey(b.getLocation())){
			b.setType(Material.AIR);
			if(p.getGameMode().equals(GameMode.CREATIVE)&&p.isOp()){
				main.bottles.remove(b.getLocation());
				p.sendTitle(ChatColor.DARK_GREEN+"�������", ChatColor.DARK_RED+"�������.", 5, 10, 5);
				return;
			}
			if(new Random().nextDouble()<=0.8+pi.perkLvl("bottle")/500.000){
				if(new Random().nextDouble()>0.15){
					p.getInventory().addItem(utilsB.Items.bottle);
					p.sendTitle(ChatColor.GREEN+"�������:", ChatColor.DARK_GREEN+"�������", 5, 10, 5);
				}
				else{
					p.getInventory().addItem(utilsB.Items.alcbottle);
					p.sendTitle(ChatColor.GREEN+"�������:", ChatColor.BLUE+"������� � ��������� ��������", 5, 15, 10);
				}
				pi.addPerk(p, ChatColor.GREEN+"'������ ����'", "hands", 1, 100);
				pi.addPerk(p, ChatColor.DARK_GREEN+"'���������� ����'", "bottle", 5, 100);
			}
			else{
				p.sendTitle(ChatColor.RED+"׸��!", ChatColor.YELLOW+"������� ���������...", 5, 15, 10);
				p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 2);
				pi.addPerk(p, ChatColor.GREEN+"'������ ����'", "hands", 5, 100);
				pi.addPerk(p, ChatColor.DARK_GREEN+"'���������� ����'", "bottle", 15, 100);
			}
			GepUtil.HashMapReplacer(main.bottles, b.getLocation(), 25, false, true);
		}
		if(!p.isOp()||!p.getGameMode().equals(GameMode.CREATIVE)){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void eat(PlayerItemConsumeEvent e){
		Player p=e.getPlayer();
		PlayerInfo pi = plist.get(p.getName());
		ItemStack item = e.getItem();
		int feed=GepUtil.intFromLore(item, "�������������");
		int tox=GepUtil.intFromLore(item, "�����������");
		int am=GepUtil.intFromLore(item, "�� ���");
		int psy=GepUtil.intFromLore(item, "����������");
		//int heat=GepUtil.intFromLore(item, "�������");
		if(am>0){
			int need=(int) (1000-pi.hunger)/feed+1;
			if(need>am)need=am;
			if(need>item.getAmount())need=item.getAmount();
			feed*=need;
			tox*=need;
			psy*=need;
			//heat*=need;
			item.setAmount(item.getAmount()-need+1);
			e.setItem(item);
		}
		pi.addPsy(psy);
		if(feed>1){
			pi.hunger+=feed;
		}else{
			p.sendMessage(ChatColor.RED+"������ ��� ����������� �������������. ��������, ��� ��� ���� �������� �� ����������. ����� �������, ��� ������������� ���� 25)");
			p.sendMessage("item="+item.getType());
			pi.hunger+=25;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				if(p.hasPotionEffect(PotionEffectType.HUNGER)){
					p.removePotionEffect(PotionEffectType.HUNGER);
				}
			}
		}.runTaskLater(main.instance, 0);
		if(tox>1){
			if(new Random().nextDouble()<=tox*0.01){
				p.sendMessage(ChatColor.DARK_GREEN+"�� ���������.");
				pi.addDisease(new disease("����������", 120, 30));
			}
		}
		double dcoef=0;
		if(GepUtil.loreContains(item, "������� � ������."))dcoef=1;
		if(GepUtil.loreContains(item, "������� � ��������."))dcoef=0.5;
		if(GepUtil.loreContains(item, "������� � ��������."))dcoef=0.25;
		dcoef*=0.75+0.125*pi.lvl;
		double chance = (tox/75.0)*(feed/125.0);
		GepUtil.HashMapReplacer(pi.timers, "rvota", 10*tox, false, false);
		chance*=dcoef;
		chance+=pi.dirt*0.0025;
		if(new Random().nextDouble()<=chance){
			HashMap<String,disease> diss=new HashMap<>();
			HashMap<String,Integer> dchances=new HashMap<>();
			diss.put("������", new disease("������",1000, 100));
			diss.put("�������", new disease("�������",2400, 120));
			diss.put("������", new disease("������",2400, 120));
			dchances.put("������", 125);
			dchances.put("�������", 45);
			dchances.put("������", 100);
			if(pi.addDisease((diss.get(GepUtil.chancesByCoef(dchances))))){
				p.sendMessage(ChatColor.DARK_GREEN+"��, ���-�� ��� ���-�� ��������� ����� �����... �������, � �������.");
			}else{
				p.sendMessage(ChatColor.DARK_GREEN+"�������, ����� �������� ����� ����� ����� �� �����...");
			}
			p.sendMessage(ChatColor.GRAY+"�� ������ ����������� �� ������� � ����� �������� �� ��������� �����.");
			if(p.isOp())p.sendMessage("ch="+chance*100+"% (��� ����� ������ ����))");
		}
		if(GepUtil.intFromLore(item, "�������")>1){
			int t = GepUtil.intFromLore(item, "�������");
			if(!pi.timers.containsKey("heatfood")||pi.timers.get("heatfood")<t){
				GepUtil.HashMapReplacer(pi.timers, "heatfood", t, true, true);
			}
		}
		if(GepUtil.isFullyItem(item, ChatColor.AQUA+"������� � ���������", null, null)){
			p.sendMessage(ChatColor.LIGHT_PURPLE+"���, �� ����� ��� ������ ����� �����!");
			GepUtil.HashMapReplacer(pi.timers, "alco", new Random().nextInt(241)+60, false, false);
		}
	}
	@EventHandler
	public void hurt(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			//Player p = (Player) e.getEntity();
			if(e.getCause().equals(DamageCause.ENTITY_ATTACK)||e.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK)){
				//EntityDamageByEntityEvent enev = (EntityDamageByEntityEvent) e;
				e.setCancelled(true);
			}
			if(e.getCause().equals(DamageCause.FALL)||e.getCause().equals(DamageCause.FIRE)){
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void hurt(EntityDamageByEntityEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void die(PlayerDeathEvent e){
		Player p = e.getEntity();
		PlayerInfo pi=plist.get(p.getName());
		if(pi.lvl<2){
			p.setHealth(10);
			pi.hunger=500;
			p.sendMessage(ChatColor.BLUE+"�� 2 ������ �� ������������� ��������� � ������� ��������.");
		}
		else if(pi.lvl<3){
			p.setHealth(5);
			pi.hunger=250;
			pi.dirt/=2;
			p.sendMessage(ChatColor.GOLD+"��������: � 3 ������ �� ������ ������������ ������!");
		}
		else if(pi.money<50){
			p.setHealth(3);
			pi.hunger=150;
			p.sendMessage(ChatColor.BLUE+"��� ��� ������ ������� �����. ��� �������� �� ������ � ������� ������ ���������� � ������� �� �������� ������.");
		}
		else if(pi.money<100){
			p.setHealth(6);
			pi.hunger=350;
			pi.money-=50;
			p.sendMessage(ChatColor.BLUE+"� ����� �������� ���� ������� 50 ������. ��� ������� � ���������� ��������� � �������.");
		}
		else if(pi.money<250){
			p.setHealth(10);
			pi.hunger=550;
			pi.money-=100;
			p.sendMessage(ChatColor.BLUE+"� ����� �������� ����� 100 ������. �� ��� ����� � ��������������, ��� ��� ��� ������ ��������.");
		}
		else if(pi.money<500){
			p.setHealth(20);
			pi.hunger=1050;
			pi.money-=250;
			p.sendMessage(ChatColor.BLUE+"� ����� �������� ����� 250 ������. ������ ��� �� ������, � ���. ����� � ������ ������, ������� ��� � ������������� ������ � ��� ��� ������ � �������� ��� �� ��������.");
		}
		/*if(!pi.timers.containsKey("new")){
			for(ItemStack item:p.getInventory().getStorageContents()){
				if(item!=null)item.setAmount(0);
			}
		}*/
		pi.temp.temp=BodyTemp.perfectBody;
		p.getInventory().setItem(8, Items.menu);
		GepUtil.HashMapReplacer(pi.timers, "noRvot", 60, false, true);
		if(pi.house==null)p.teleport(main.locs.get("spawn"));
		else pi.house.teleportHome(p);
	}
	@EventHandler
	public void hunger(FoodLevelChangeEvent e){
		if(e.getEntity() instanceof Player){
			e.setCancelled(true);
			Player p=(Player) e.getEntity();
			PlayerInfo pi=plist.get(p.getName());
			double f = pi.hunger;
			p.setFoodLevel((int) (f/50));
		}
	}
	@EventHandler
	public void chat(AsyncPlayerChatEvent e){
		Player p=e.getPlayer();
		PlayerInfo pi=plist.get(p.getName());
		String mes=e.getMessage();
		e.setCancelled(true);
		if(p.isOp()){
			if(mes.equals("food")){
				pi.hunger=500;
				return;
			}
			if(mes.equals("diseases")){
				for(disease d:pi.diseases){
					d.searched=true;
				}
				return;
			}
		}
		GepUtil.globMessage(ChatColor.GOLD+"["+ChatColor.DARK_GRAY+pi.lvl+ChatColor.GOLD+"] "+ChatColor.GRAY+p.getName()+": "+ChatColor.WHITE+mes);
	}
	public static ItemStack NewClotchCreate(String[] names, List<String> subadds, int subaddcount){
		String name = names[new Random().nextInt(names.length)];
		Clotch cl = new Clotch(name);
		for(int i=0;i<subaddcount;i++){
			if(subadds.size()<=0)break;
			String sub = subadds.get(new Random().nextInt(subadds.size()));
			cl.addClotchType(sub);
		}
		return cl.create();
	}
}
