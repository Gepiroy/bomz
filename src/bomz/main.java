package bomz;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import ObjectsB.BodyTemp;
import ObjectsB.BottlePlace;
import ObjectsB.Heat;
import ObjectsB.Help;
import ObjectsB.House;
import ObjectsB.PlayerInfo;
import ObjectsB.Shaurma;
import ObjectsB.Shop;
import ObjectsB.business;
import ObjectsB.disease;
import ObjectsB.pibus;
import ObjectsB.priut;
import utilsB.ActionBar;
import utilsB.GepUtil;
import utilsB.ItemUtil;
import ObjectsB.Perk;
import works.begging;
import works.mcd;

public class main extends JavaPlugin{
	
	public static Random r = new Random();
	
	public static main instance;
	//public static double t=0.0;
	public static int day=0;
	public static double BANK=0;
	public static double bottlePrice=0.6;
	//public static int mint=0;
	//public static int maxt=0;
	public static int dayTime = 0; //Текущее время в часах обычных (правильных)
	public static int maxHome=0;
	public static HashMap<Location,Integer> bottles = new HashMap<>();
	public static ArrayList<Heat> heats = new ArrayList<>();
	public static ArrayList<Shop> shops = new ArrayList<>();
	public static ArrayList<Shaurma> shas = new ArrayList<>();
	public static ArrayList<priut> priuts = new ArrayList<>();
	public static ArrayList<BottlePlace> bottlePlaces = new ArrayList<>();
	public static ArrayList<begging> beggs = new ArrayList<>();
	public static ArrayList<works.loader> loads = new ArrayList<>();
	public static ArrayList<House> houses = new ArrayList<>();
	public static ArrayList<Help> helps = new ArrayList<>();
	public static ArrayList<Location> meds = new ArrayList<>();
	public static HashMap<String, Location> locs = new HashMap<>();
	
	
	
	
	
	
	static FileConfiguration bs;
	int secRate=0;
	int fiveRate=0;
	boolean newDayed=false;
	public void onEnable(){
		Events.setIgnores();
		instance=this;
		GUI.setItems();
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		Bukkit.getPluginManager().registerEvents(new GUI(), this);
		Bukkit.getPluginCommand("loc").setExecutor(new cmd());
		saveDefaultConfig();
		bs = getConfig();
		if(bs.contains("bottles"))for(String st:bs.getConfigurationSection("bottles").getKeys(false)){
			bottles.put(new Location(Bukkit.getWorld(bs.getString("bottles."+st+".world")),bs.getDouble("bottles."+st+".x"),bs.getDouble("bottles."+st+".y"),bs.getDouble("bottles."+st+".z")), 1);
		}
		if(bs.contains("bottlePlaces"))for(String st:bs.getConfigurationSection("bottlePlaces").getKeys(false)){
			BottlePlace place = new BottlePlace(GepUtil.getLocFromConf(bs, "bottlePlaces."+st));
			place.price=bs.getDouble("bottlePlaces."+st+".price");
			bottlePlaces.add(place);
		}
		if(bs.contains("Shas"))for(String st:bs.getConfigurationSection("Shas").getKeys(false)){
			Shaurma s = new Shaurma(GepUtil.getLocFromConf(bs, "Shas."+st+".loc"), bs.getDouble("Shas."+st+".price"), bs.getInt("Shas."+st+".sub"), bs.getInt("Shas."+st+".sell"), bs.getInt("Shas."+st+".poison"), bs.getString("Shas."+st+".name"));
			s.bus=new business("Shas."+st, bs);
			shas.add(s);
		}
		File mac = new File(instance.getDataFolder() + File.separator + "mac.yml");
		FileConfiguration macc = YamlConfiguration.loadConfiguration(mac);
		for(String st:macc.getKeys(false)){
			works.mcd.locs.put(st, new Location(Bukkit.getWorld(macc.getString(st+".world")),macc.getDouble(st+".x"),macc.getDouble(st+".y"),macc.getDouble(st+".z")));
		}
		File hfile = new File(instance.getDataFolder() + File.separator + "houses.yml");
		FileConfiguration hconf = YamlConfiguration.loadConfiguration(hfile);
		if(hconf.contains("Houses"))for(String st:hconf.getConfigurationSection("Houses").getKeys(false)){
			houses.add(new House(hconf, st));
		}
		File beg = new File(instance.getDataFolder() + File.separator + "beg.yml");
		FileConfiguration begc = YamlConfiguration.loadConfiguration(beg);
		int i=0;
		while(begc.contains(i+"")){
			beggs.add(new begging(begc, i+""));
			i++;
		}
		File loa = new File(instance.getDataFolder() + File.separator + "loaders.yml");
		FileConfiguration loac = YamlConfiguration.loadConfiguration(loa);
		i=0;
		while(loac.contains(i+"")){
			loads.add(new works.loader(loac, i+""));
			i++;
		}
		if(houses.size()>0)GepUtil.globMessage(""+houses.get(0), null, 0, 0, "", "", 0, 0, 0);
		GepUtil.globMessage(""+houses.size(), null, 0, 0, "", "", 0, 0, 0);
		
		Scoreboard s=Bukkit.getScoreboardManager().getMainScoreboard();
		if(s.getObjective("jump")==null)s.registerNewObjective("jump", "jump", "ПРЫГУНКИ АХАХ");
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			int tick2=0;
			public void run(){
				secRate++;
				fiveRate++;
				tick2++;
				if(tick2>=2){
					tick2-=2;
					for(Player p:Bukkit.getOnlinePlayers()){
						PlayerInfo pi=Events.plist.get(p.getName());
						for(String st:new ArrayList<>(pi.fastTimers.keySet())){
							if(GepUtil.HashMapReplacer(pi.fastTimers, st, -1, true, false)){
								if(st.equals("noJump")){
								}
							}
						}
					}
				}
				if(secRate>=20){//TODO 20 ticks
					secRate-=20;
					long time=(Bukkit.getWorld("world").getTime()+6000)/1000;
					if(time>=24)time-=24;
					if(time!=dayTime){
						changeTime();
					}
					//double coef = 1000.000;
					//if(dayTime>=18||dayTime<6)coef=500.00;
					Temperature.sec();
					for(Player p:Bukkit.getOnlinePlayers()){
						if(p.getGameMode().equals(GameMode.CREATIVE))continue;
						PlayerInfo pi = Events.plist.get(p.getName());
						double toRemove=1;
						for(disease d:new ArrayList<>(pi.diseases)){
							d.toRemove--;
							if(d.toRemove<=0){
								pi.diseases.remove(d);
								p.sendMessage(ChatColor.GREEN+"Болезнь '"+ChatColor.DARK_GREEN+d.name+ChatColor.GREEN+"' прошла!");
								continue;
							}
							d.stage(pi);
							if(d.basics.containsKey("foodSpeed")){
								toRemove+=d.basics.get("foodSpeed")/100.0;
							}
							if(d.basics.containsKey("rvota")){
								GepUtil.HashMapReplacer(pi.timers, "rvota", d.basics.get("rvota"), false, false);
							}
						}
						GepUtil.HashMapReplacer(pi.timers, "rvota", (int) Math.max(0, pi.dirt-50*0.15), false, false);
						if(pi.timers.containsKey("rvota")&&!pi.timers.containsKey("noRvot")){
							if(r.nextInt(1000)+1000<=pi.timers.get("rvota")){
								p.getWorld().spawnParticle(Particle.SLIME, p.getEyeLocation(), 100, 0.1, 0.3, 0.1, 0);
								Location l=p.getLocation();
								l.setPitch(70);
								p.teleport(l);
								p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
								pi.hunger-=r.nextInt(150)+75;
								pi.timers.remove("rvota");
								pi.dirt+=r.nextInt(4)+1;
								p.sendMessage(ChatColor.DARK_GREEN+"Вас вырвало! Фу!");
								pi.timers.put("noRvot", 10);
								p.damage(2.5);
							}
						}
						
						double health = p.getHealth();
						if(health<pi.hunger/50&&pi.hunger<900){
							health+=0.075;
							toRemove+=0.075;
							if(health>pi.hunger/50)health=pi.hunger/50;
							p.setHealth(health);
						}
						
						toRemove*=0.375;
						pi.hunger-=toRemove;
						if(pi.hunger<0)pi.hunger=0;
						p.setFoodLevel((int) (pi.hunger/50));
						
						
						
						float speed=25F;
						float comfCoef=(float) (pi.comfort*0.002)*100;
						float tempCoef=(float) -(Math.abs(pi.temp.temp-BodyTemp.perfectBody)*0.0035)*100;
						speed=speed+comfCoef+tempCoef;
						String message=ChatColor.GRAY+"(25 (дефолт) +"+GepUtil.boolNumCol(comfCoef)+GepUtil.CylDouble(comfCoef, "#0.0")+" (комфорт) "+GepUtil.boolNumCol(tempCoef)+GepUtil.CylDouble(tempCoef, "#0.0")+" (температура)";
						if(pi.lastWalkMat.equals(Material.GRASS)){
							speed*=0.75;
							message+=ChatColor.RED+" x0.75 (ходьба по траве)";
						}else if(pi.lastWalkMat.equals(Material.STONE)){
							speed*=1.1;
							message+=ChatColor.GREEN+" x1.1 (ходьба по камню)";
						}else if(pi.lastWalkMat.equals(Material.DIRT)){
							speed*=0.6;
							message+=ChatColor.RED+" x0.6 (ходьба по грязи)";
						}else if(pi.lastWalkMat.equals(Material.STONE_SLAB)){
							speed*=0.9;
							message+=ChatColor.RED+" x0.9 (ходьба по бардюру)";
						}
						double disCoef=1;
						for(disease d:pi.diseases){
							if(d.basics.containsKey("speed")){
								disCoef+=d.basics.get("speed")*0.01;
							}
						}
						speed*=disCoef;
						if(disCoef!=1){
							message+=GepUtil.boolCol(disCoef>1)+" x"+GepUtil.CylDouble(disCoef, "#0.0")+" (болезни)";
						}
						if(speed<0)speed=0;
						if(Math.abs(p.getWalkSpeed()*100-speed)>=1.0){
							p.setWalkSpeed(speed/100.0F);
							message+=")";
							ActionBar bar=new ActionBar("Cкорость "+GepUtil.CylDouble(speed, "#0.0")+". "+message);
							bar.sendToPlayer(p);
						}
						if(pi.lvl<2){
							pi.dirt=0;
						}
						if(new Random().nextDouble()<=(pi.dirt-50)*0.00065&&!pi.timers.containsKey("noDirt")){
							pi.timers.put("noDirt", 10);
							p.sendMessage(ChatColor.DARK_GREEN+"Вам противно от своей вони.");
							p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, new Random().nextInt(pi.dirt-30), new Random().nextInt(pi.dirt/25+1)));
						}
						if(pi.bools.contains("bottleEye")){
							List<Location> locs = new ArrayList<>();
							for(BottlePlace place:bottlePlaces){
								locs.add(place.loc);
							}
							Location loc = GepUtil.nearest(p.getLocation(), locs).clone();
							loc.setX(loc.getX()+0.5);
							loc.setY(loc.getY()+0.5);
							loc.setZ(loc.getZ()+0.5);
							drawLine(p, loc, 0.75, Particle.VILLAGER_HAPPY);
						}
						if(pi.bools.contains("begEye")){
							List<Location> locs = new ArrayList<>();
							for(begging b:beggs){
								locs.add(b.loc);
							}
							Location loc = GepUtil.nearest(p.getLocation(), locs).clone();
							drawLine(p, loc, 0.75, Particle.VILLAGER_HAPPY);
							circle(p, loc.clone().add(0, 0.1, 0), 1.5, Particle.VILLAGER_HAPPY);
						}
						if(pi.bools.contains("pEye")){
							List<Location> locs = new ArrayList<>();
							for(priut place:priuts){
								locs.add(place.loc);
							}
							Location loc = GepUtil.nearest(p.getLocation(), locs).clone();
							loc.setX(loc.getX()+0.5);
							loc.setY(loc.getY()+0.5);
							loc.setZ(loc.getZ()+0.5);
							drawLine(p, loc, 0.75, Particle.WATER_BUBBLE);
						}
						for(String st:new ArrayList<String>(pi.timers.keySet())){
							if(pi.learnMessage!=null&&!pi.timers.containsKey("learnmessage"))pi.timers.put("learnmessage", 1);
							if(GepUtil.HashMapReplacer(pi.timers, st, -1, true, false)){
								if(st.equals("alco")){
									p.sendMessage(ChatColor.DARK_PURPLE+"Действие алкоголя закончилось.");
								}
								if(st.equals("learnmessage")&&pi.learnTitle!=null){
									p.sendMessage(ChatColor.BLUE+"[Обучение] "+pi.learnMessage);
									p.sendTitle(pi.learnTitle, ChatColor.AQUA+"Подробнее в чате.", 10, 500, 10);
									if(pi.learnMessage!=null)pi.timers.put("learnmessage", 20);
								}
							}
						}
						if(pi.bools.contains("begging")){
							boolean inloc=false;
							begging bg=null;
							for(begging beg:main.beggs){
								if(p.getLocation().distance(beg.loc)<=1.5){
									bg=beg;
									inloc=true;
									break;
								}
							}
							if(!inloc){
								pi.toggleBool("begging",false);
								p.sendMessage(ChatColor.YELLOW+"Вы закончили попрошайничать. "+ChatColor.GRAY+"(Вы отошли от места попрошайничества)");
								//return;
							}
							double rareOfDrop = 0.025;
							if(pi.prestige<0)rareOfDrop+=-pi.prestige/10000.000;
							rareOfDrop*=bg.people/50.00;
							
							double rememberAm = 0.010;
							if(pi.prestige>=0)rememberAm+=pi.prestige/1000.000;
							if(pi.dirt>0)rememberAm+=pi.dirt/200.00;
							rememberAm*=bg.people/50.00;
							
							int hurtChance=pi.dirt;
							hurtChance+=pi.waits.get("beg"+GepUtil.locInfo(bg.loc))/10;
							//Если пристижа много, то тебя запоминают сильно и часто, дают реже, но много. Одежду и хлеб не дадут.
							//Если пристиж в минус, то [старая система попрошайки].
							//От вони повышается запоминаемость и шанс того, что тоби прогонят нахер.
							if(new Random().nextDouble()<=rememberAm){
								GepUtil.HashMapReplacer(pi.waits, "beg"+GepUtil.locInfo(bg.loc), 1, false, false);
								p.sendMessage(ChatColor.LIGHT_PURPLE+"+1 узнаваемость. "+ChatColor.DARK_PURPLE+"("+ChatColor.RED+pi.waits.get("beg"+GepUtil.locInfo(bg.loc))+ChatColor.DARK_PURPLE+" всего)");
							}
							if(new Random().nextDouble()<=rareOfDrop){
								String drop="";
								if(pi.prestige<0){
									drop=GepUtil.chancesByCoef(new String[]{"money","bread","beer","clotch","hurt"}, new int[]{50,20,10,10,hurtChance});
								}
								else{
									drop=GepUtil.chancesByCoef(new String[]{"bigmoney","hurt"}, new int[]{pi.prestige+10,hurtChance});
								}
								if(drop.equals("bigmoney")){
									double d = (new Random().nextInt(pi.prestige)+10);
									pi.money+=d;
									p.sendMessage(ChatColor.GRAY+"[Попрошайничество] "+ChatColor.DARK_GREEN+"С виду вроде нормальный человек. Что ж тебя вынудило попрошайничеством заниматься? Ладно, помогу... Возьми эти "+ChatColor.GREEN+d+" руб.");
								}
								if(drop.equals("money")){
									double d = (new Random().nextInt(20)+1)*0.5;
									pi.money+=d;
									p.sendMessage(ChatColor.GRAY+"[Попрошайничество] "+ChatColor.DARK_GREEN+"Вот, возьми эти "+ChatColor.GREEN+d+" руб.");
								}
								if(drop.equals("bread")){
									p.getInventory().addItem(ItemUtil.create(Material.BREAD, ChatColor.GOLD+"Буханка"));
									p.sendMessage(ChatColor.GRAY+"[Попрошайничество] "+ChatColor.GOLD+"Вот, возьми этот хлеб. Голодный, наверное...");
								}
								if(drop.equals("beer")){
									p.getInventory().addItem(ItemUtil.createPotion(Material.POTION, 1, ChatColor.AQUA+"Бутылка с алкоголем", new String[]{ChatColor.DARK_GREEN+"Предмет торговли.",ChatColor.DARK_GREEN+"Токсичность: "+ChatColor.RED+"55%",ChatColor.GOLD+"При использовании увеличивает",ChatColor.YELLOW+"Переносимость холода и жары ",ChatColor.GOLD+"на некоторое время."}, 100, 125, 0, null));
									p.sendMessage(ChatColor.GRAY+"[Попрошайничество] "+ChatColor.DARK_GREEN+"Я знаю, чего ты добиваешься. Бери, пей, да проваливай отсюда!");
								}
								if(drop.equals("clotch")){
									List<String> sts = new ArrayList<>();
									sts.add("Стар");
									sts.add("Утеплённ");
									sts.add("Потрёпанн");
									sts.add("Водоотталкивающ");
									p.getInventory().addItem(Events.NewClotchCreate(new String[]{"Куртка","Шапка","Шапка-ушанка","Джинсы","Ботинки","Пуховик"}, sts, new Random().nextInt(3)+1));
									p.sendMessage(ChatColor.GRAY+"[Попрошайничество] "+ChatColor.GOLD+"Тебе, наверное, холодно... Держи, одень, согреешься)");
								}
								if(drop.equals("hurt")){
									p.damage(new Random().nextInt(5)+1);
									p.sendMessage(ChatColor.GRAY+"[Попрошайничество] "+ChatColor.RED+"А ну пошёл отсюда! Нечего тут стоять! На тебе! Козёл! "+ChatColor.GRAY+"(Вас узнали, либо от вас воняет.)");
								}
							}
						}
						updateScoreboard(p);
					}
					for(Location loc:bottles.keySet()){
						if(bottles.get(loc)>0){
							GepUtil.HashMapReplacer(bottles, loc, -1, false, false);
							if(bottles.get(loc)<=0){
								loc.getBlock().setType(Material.FLOWER_POT);
							}
						}
					}
				}
				if(fiveRate>=5){//TODO 5 ticks
					fiveRate-=5;
					if(dayTime>=18||dayTime<6)Bukkit.getWorld("world").setTime(Bukkit.getWorld("world").getTime()+5);
					else Bukkit.getWorld("world").setTime(Bukkit.getWorld("world").getTime()+2);
					works.mcd.base();
					for(Player p:Bukkit.getOnlinePlayers()){
						PlayerInfo pi = Events.plist.get(p.getName());
						for(works.loader l:loads){
							if(pi.waits.containsKey("loader"+GepUtil.locInfo(l.putLoc))){
								drawLine(p, l.putLoc, 0.75, Particle.FIREWORKS_SPARK);
								circle(p, l.putLoc.clone().add(0, 0.1, 0), 1.5, Particle.FIREWORKS_SPARK);
								if(p.getLocation().distance(l.putLoc)<=1.5){
									double money=(int)(l.boxLoc.distance(l.putLoc))*0.25;
									p.sendMessage(ChatColor.DARK_GREEN+"Вы получили "+ChatColor.GREEN+GepUtil.CylDouble(money, "#0.00")+" руб. "+ChatColor.DARK_GREEN+"за работу.");
									pi.money+=money;
									pi.waits.remove("loader"+GepUtil.locInfo(l.putLoc));
								}
								else if(p.getLocation().distance(l.putLoc)>l.boxLoc.distance(l.putLoc)+7.5){
									pi.waits.remove("loader"+GepUtil.locInfo(l.putLoc));
									p.sendMessage(ChatColor.RED+"ЭЙ! Куда ты убежал! Вернись! Козёл, коробку украл! ШТРАФ ВЫПИШУ!!!");
									if(pi.money>=20){
										pi.money-=20;
										p.sendMessage(ChatColor.RED+"-20 руб.");
									}
									else{
										pi.money=0;
										p.sendMessage(ChatColor.RED+"-"+GepUtil.CylDouble(pi.money, "#0.00")+" руб.");
									}
									p.getInventory().addItem(utilsB.Items.foodbox);
								}
								break;
							}
						}
						if((p.getOpenInventory()==null||p.getOpenInventory().getTitle().equals("container.crafting"))&&pi.lastInv!=null){
							pi.lastInv=null;
							if(pi.waits.containsKey("box"))pi.waits.remove("box");
						}
						else if(pi.lastInv!=null&&!p.getOpenInventory().getTitle().equals(pi.lastInv)){
							pi.lastInv=p.getOpenInventory().getTitle();
						}
						else if(p.getOpenInventory()!=null&&pi.lastInv==null){
							pi.lastInv=p.getOpenInventory().getTitle();
						}
					}
				}
			}
		}, 0, 1);
		File file = new File(instance.getDataFolder() + File.separator + "global.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		Temperature.load(conf);
		if(conf.contains("BottlePrice"))bottlePrice=conf.getDouble("BottlePrice");
		//if(conf.contains("minT"))mint=conf.getInt("minT");
		//if(conf.contains("maxT"))maxt=conf.getInt("maxT");
		if(conf.contains("day"))day=conf.getInt("day");
		if(conf.contains("BANK"))BANK=conf.getDouble("BANK");
		if(conf.contains("Help"))for(String st:conf.getConfigurationSection("Help").getKeys(false)){
			helps.add(new Help(conf, st));
		}
		if(conf.contains("Shops"))for(String st:conf.getConfigurationSection("Shops").getKeys(false)){
			shops.add(new Shop(GepUtil.getLocFromConf(conf, "Shops."+st), conf.getString("Shops."+st+".type"), st));
		}
		if(conf.contains("Priuts"))for(String st:conf.getConfigurationSection("Priuts").getKeys(false)){
			priuts.add(new priut(GepUtil.getLocFromConf(conf, "Priuts."+st)));
		}
		if(conf.contains("Locs"))for(String st:conf.getConfigurationSection("Locs").getKeys(false)){
			locs.put(st, GepUtil.getLocFromConf(conf, "Locs."+st));
		}
		if(conf.contains("Heats"))for(String st:conf.getConfigurationSection("Heats").getKeys(false)){
			heats.add(new Heat(GepUtil.getLocFromConf(conf, "Heats."+st), conf.getDouble("Heats."+st+".min"), conf.getDouble("Heats."+st+".max")));
		}
		if(conf.contains("Meds"))for(String st:conf.getConfigurationSection("Meds").getKeys(false)){
			meds.add(GepUtil.getLocFromConf(conf, "Meds."+st));
		}
		for(Player p:Bukkit.getOnlinePlayers()){
			Events.doJoin(p);
		}
	}
	void changeTime(){
		for(begging b:beggs){
			b.change();
		}
		dayTime = (int)((Bukkit.getWorld("world").getTime()+6000)/1000);
		if(dayTime>=24){
			dayTime-=24;
			int c = playersWhoSleep();
			if(c<35){
				GepUtil.globMessage(ChatColor.BLUE+"Спят всего "+c+"% людей. Пропуска ночи не будет.", null, 0, 0, null, "", 0, 0, 0);
			}
			else{
				GepUtil.globMessage(ChatColor.AQUA+"Сейчас спит "+c+"% людей.", null, 0, 0, null, "", 0, 0, 0);
				skipDay();
			}
		}
		for(Player p:Bukkit.getOnlinePlayers()){
			PlayerInfo pi = Events.plist.get(p.getName());
			if(new Random().nextBoolean())pi.dirt++;
		}
		if(dayTime==18){
			GepUtil.globMessage(ChatColor.AQUA+"Ночь. Вам нужно найти место, где вы будете спать. В меню появилась вкладка о сне, там вы сможете выбрать место для сна.", Sound.ENTITY_WITHER_SPAWN, 2, 0, ChatColor.BLUE+"Время спать...", ChatColor.AQUA+"Где же ты будешь ночевать?", 50, 20, 50);
		}
		if(dayTime==6){
			newDay();
		}
	}
	int playersWhoSleep(){
		int c=0;
		for(PlayerInfo pi:Events.plist.values()){
			if(!pi.sleep.equals(""))c++;
		}
		return (int) (100.00/Events.plist.size()*c);
	}
	void sleep(Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		if(pi.sleep.equals("grass")){
			p.sendMessage(ChatColor.AQUA+"="+ChatColor.BLUE+"Этой ночью вы спали на земле."+ChatColor.AQUA+"=");
			double r = new Random().nextDouble();
			if(r<0.2){
				int stealcoef = (new Random().nextInt(41)+10);
				p.sendMessage(ChatColor.GRAY+"-"+ChatColor.RED+"Вас ограбили! "+ChatColor.DARK_GREEN+"-"+GepUtil.CylDouble(pi.money/100.00*stealcoef,"#0.00")+" руб.");
				pi.money-=pi.money/100.00*stealcoef;
			}
			else if(r>0.8){
				p.sendMessage(ChatColor.GRAY+"-"+ChatColor.GREEN+"Кто-то положил вам немного денег! "+ChatColor.DARK_GREEN+"+"+(new Random().nextInt(10)+1)+" руб.");
			}
			/*if(new Random().nextDouble()<=((mint-20)*2.5)+pi.tepl){
				double CPS=((mint-20)*2.5)+pi.tepl;
				p.sendMessage(ChatColor.GRAY+"-"+ChatColor.BLUE+"Ночь была холодная, вы обморозились! "+ChatColor.GRAY+"Получен урон ("+GepUtil.CylDouble(CPS, "#0")+")");
				p.damage(CPS);
			}*/
		}
		p.sendMessage(ChatColor.AQUA+"===[Вот такая была ночка]===");
	}
	public void skipDay(){
		for(Player p:Bukkit.getOnlinePlayers()){
			PlayerInfo pi = Events.plist.get(p.getName());
			if(pi.sleep.equals("")){
				pi.sleep="grass";
			}
			sleep(p);
		}
		Bukkit.getWorld("world").setTime(0);
	}
	public void newDay(){
		bottlePrice=(new Random().nextInt(15)+6)/10.0;
		for(BottlePlace place:bottlePlaces){
			if(place.owner.equals(""))place.price=(5+new Random().nextInt((int) (bottlePrice*10-5)))/10.0;
			place.changeOwner();
		}
		GepUtil.globMessage(ChatColor.GOLD+"Новый день... Цены на бутылки, погода и многое другое изменилось.", Sound.ENTITY_CHICKEN_HURT, 2, 1, ChatColor.GOLD+"Новый день", "", 50, 20, 50);
		for(House h:houses){
			if(h.rentTime>0){
				h.rentTime--;
				if(h.rentTime==0){
					h.owner=null;
				}
			}
		}
		day++;
		File global = new File(instance.getDataFolder() + File.separator + "global.yml");
		FileConfiguration globalc = YamlConfiguration.loadConfiguration(global);
		Temperature.save(globalc);
		globalc.set("BottlePrice",bottlePrice);
		//mint=-new Random().nextInt(31);
		//maxt=mint+new Random().nextInt(21)+10;
		Temperature.changeDay();
		globalc.set("day",day);
		globalc.set("BANK",BANK);
		for(Shop s:shops){
			GepUtil.saveLocToConf(globalc, "Shops."+s.name, s.loc);
			globalc.set("Shops."+s.name+".type",s.shopType);
		}
		for(String st:locs.keySet()){
			GepUtil.saveLocToConf(globalc, "Locs."+st, locs.get(st));
		}
		int i=0;
		for(Heat h:heats){
			GepUtil.saveLocToConf(globalc, "Heats."+i, h.loc);
			globalc.set("Heats."+i+".min",h.min);
			globalc.set("Heats."+i+".max",h.max);
			i++;
		}
		GepUtil.saveCfg(globalc, global);
		for(Player p:Bukkit.getOnlinePlayers()){
			PlayerInfo pi=Events.plist.get(p.getName());
			for(pibus bus:pi.buss){
				int got = bus.moneyPerDay;
				if(!bus.own)got/=10;
				else got/=(int) (100.00*25);
				p.sendMessage(ChatColor.DARK_GREEN+"[Бизнес] "+ChatColor.GREEN+"+"+got+" руб.");
			}
		}
	}
	public void onDisable(){
		for(Player p:Bukkit.getOnlinePlayers()){
			Events.doLeave(p);
		}
		File global = new File(instance.getDataFolder() + File.separator + "global.yml");
		FileConfiguration globalc = YamlConfiguration.loadConfiguration(global);
		Temperature.save(globalc);
		globalc.set("BottlePrice",bottlePrice);
		if(helps.size()==0){
			new Help(Material.STONE,ChatColor.GREEN+"Test","this is short","this is long").save(globalc);
		}
		for(Shop s:shops){
			GepUtil.saveLocToConf(globalc, "Shops."+s.name, s.loc);
			globalc.set("Shops."+s.name+".type",s.shopType);
		}
		int i=0;
		for(Location l:meds){
			GepUtil.saveLocToConf(globalc, "Meds."+i, l);
			i++;
		}
		i=0;
		for(priut p:priuts){
			GepUtil.saveLocToConf(globalc, "Priuts."+i,p.loc);
			i++;
		}
		for(String st:locs.keySet()){
			GepUtil.saveLocToConf(globalc, "Locs."+st, locs.get(st));
		}
		i=0;
		for(Heat h:heats){
			GepUtil.saveLocToConf(globalc, "Heats."+i, h.loc);
			globalc.set("Heats."+i+".min",h.min);
			globalc.set("Heats."+i+".max",h.max);
			i++;
		}
		GepUtil.saveCfg(globalc, global);
		File mac = new File(instance.getDataFolder() + File.separator + "mac.yml");
		FileConfiguration macc = YamlConfiguration.loadConfiguration(mac);
		for(String st:mcd.locs.keySet()){
			GepUtil.saveLocToConf(macc, st, mcd.locs.get(st));
		}
		GepUtil.saveCfg(macc, mac);
		
		File beg = new File(instance.getDataFolder() + File.separator + "beg.yml");
		FileConfiguration begc = YamlConfiguration.loadConfiguration(beg);
		i=0;
		for(begging b:beggs){
			b.save(begc, i+"");
			i++;
		}
		GepUtil.saveCfg(begc, beg);
		
		File loa = new File(instance.getDataFolder() + File.separator + "loaders.yml");
		FileConfiguration loac = YamlConfiguration.loadConfiguration(loa);
		i=0;
		for(works.loader l:loads){
			l.save(loac, i+"");
			i++;
		}
		GepUtil.saveCfg(loac, loa);
		
		File hfile = new File(instance.getDataFolder() + File.separator + "houses.yml");
		FileConfiguration hconf = YamlConfiguration.loadConfiguration(hfile);
		i=0;
		for(House h:houses){
			h.SaveHouse("Houses."+i, hconf);
			i++;
		}
		GepUtil.saveCfg(hconf, hfile);
		i=0;
		for(Location loc:bottles.keySet()){
			GepUtil.saveLocToConf(bs, "bottles."+i, loc);
			i++;
		}
		i=0;
		for(BottlePlace place:bottlePlaces){
			GepUtil.saveLocToConf(bs, "bottlePlaces."+i, place.loc);
			bs.set("bottlePlaces."+i+".price", place.price);
			i++;
		}
		i=0;
		for(Shaurma sha:shas){
			GepUtil.saveLocToConf(bs, "Shas."+i+".loc", sha.loc);
			bs.set("Shas."+i+".price", sha.price);
			bs.set("Shas."+i+".poison", sha.poison);
			sha.bus.saveToConf("Shas."+i, bs);
			i++;
		}
		saveConfig();
	}
	@SuppressWarnings("deprecation")
	public static void updateScoreboard(Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		ArrayList<String> strings = new ArrayList<>();
		pi.prestige=0;
		pi.comfort=0;
		pi.tepl=0;
		for(ItemStack item:p.getInventory().getArmorContents()){
			if(GepUtil.isFullyItem(item, null, null,null)){
				pi.tepl+=GepUtil.intFromLore(item, "Теплота");
				pi.comfort+=GepUtil.intFromLore(item, "Комфорт");
				pi.prestige+=GepUtil.intFromLore(item, "Престиж");
			}
		}
		if(pi.timers.containsKey("heatfood"))pi.tepl+=pi.timers.get("heatfood");
		strings.add(ChatColor.AQUA+"Уровень: "+ChatColor.LIGHT_PURPLE+pi.lvl+ChatColor.AQUA+" ("+pi.exp+"/"+pi.toLvl()+")");
		strings.add(ChatColor.GOLD+"Грязь: "+ChatColor.RED+pi.dirt);
		strings.add(ChatColor.BLUE+"Настроение: "+GepUtil.boolCol(pi.getPsy()>500)+pi.getPsy());
		strings.add(ChatColor.LIGHT_PURPLE+"Престиж: "+ChatColor.DARK_PURPLE+pi.prestige);
		double realtempr=Temperature.temperature;
		List<Heat> hlist = new ArrayList<>();
		for(Heat h:heats){
			Location ploc = p.getLocation();
			Location hloc = h.loc.clone();
			ploc.setY(hloc.getY()-1);
			hloc.setY(hloc.getY()-1);
			if(ploc.getBlock().getType().equals(hloc.getBlock().getType())&&ploc.getBlock().getData()==hloc.getBlock().getData()){
				hlist.add(h);
			}
		}
		double dist = 50;
		Heat ret = null;
		for(Heat h:hlist){
			if(h.loc.distance(p.getLocation())<dist){
				dist=h.loc.distance(p.getLocation());
				ret=h;
			}
		}
		if(ret!=null)realtempr=(Temperature.temperature+30.0)/(60.0/(ret.max-ret.min))+ret.min;
		if(pi.bools.contains("inHouse")){
			for(House h:houses){
				if(h.whoInHouse.contains(p.getName())){
					realtempr=(Temperature.temperature+30.0)/(60.0/(h.maxT-h.minT))+h.minT;
				}
			}
		}
		if(locs.containsKey("spawn")&&locs.get("spawn").distance(p.getLocation())<7.5)realtempr=20.0;
		double tshow=realtempr;
		strings.add(ChatColor.GOLD+"Температура: "+ChatColor.YELLOW+"+"+GepUtil.CylDouble(tshow, "#0.0")+ChatColor.GRAY+"°C|"+GepUtil.CylDouble(realtempr, "#0.0"));
		/*double isolatCoef=1-Math.min(pi.tepl, 45)*0.02;//изоляция (в шубе ты медленнее сваришься в котле))
		double toT=0;
		toT+=(tshow-20)*isolatCoef;//Влияние окр. среды, идеальная = 20.
		for(disease d:pi.diseases){
			if(d.basics.containsKey("temp")){
				toT+=d.basics.get("temp")*0.25*isolatCoef;
			}
		}
		toT+=pi.tepl*0.05;//Сохранение тепла одеждой
		toT-=pi.temp*0.075;//Температура стремится к 0
		toT/=5;
		pi.temp+=toT;
		String znak=ChatColor.GREEN+"=";
		if(toT>=0.1)znak=ChatColor.YELLOW+"+";
		if(toT>=0.25)znak=ChatColor.GOLD+"+!";
		if(toT>=0.5)znak=ChatColor.RED+"!+!";
		if(toT<=-0.1)znak=ChatColor.AQUA+"-";
		if(toT<=-0.25)znak=ChatColor.BLUE+"-!";
		if(toT<=-0.5)znak=ChatColor.DARK_BLUE+"!-!";
		if(pi.lvl<2){
			if(pi.temp<-10){
				pi.temp=-10;
			}
			if(pi.temp>10){
				pi.temp=10;
			}
		}
		strings.add(ChatColor.GOLD+"Тело: "+ChatColor.YELLOW+""+GepUtil.CylDouble(pi.temp, "#0.0")+ChatColor.GRAY+" ("+znak+ChatColor.GRAY+")");
		*/
		if(pi.timers.containsKey("new")){
			strings.add(ChatColor.LIGHT_PURPLE+"Бонус новичка: "+ChatColor.DARK_PURPLE+pi.timers.get("new"));
		}/*
		if(realtempr>20)realtempr=20;
		int toAdd=(int) pi.temp;
		if(toAdd>7)toAdd-=6;
		if(toAdd<-7)toAdd+=6;
		else toAdd=0;
		GepUtil.HashMapReplacer(pi.waits, "cold", toAdd, false, false);
		String st = "";
		if(pi.waits.get("cold")>10)GepUtil.HashMapReplacer(pi.waits, "cold", (-pi.perkLvl("hot")+5), false, false);
		if(pi.waits.get("cold")<10)GepUtil.HashMapReplacer(pi.waits, "cold", (pi.perkLvl("cold")+5), false, false);
		if(p.getGameMode().equals(GameMode.SURVIVAL)){
			if(pi.waits.get("cold")<=-250){
				p.sendMessage(ChatColor.BLUE+"[Холод] "+ChatColor.AQUA+"Вам холодно! Срочно найдите, что одеть, либо бегайте, едите тёплую пищу или забегите в тёплое место!");
				p.damage(new Random().nextInt(3)+1);
				GepUtil.HashMapReplacer(pi.waits, "cold", 0, false, true);
			}
			if(pi.waits.get("cold")>=500){
				p.sendMessage(ChatColor.GOLD+"[Жара] "+ChatColor.YELLOW+"Вам жарко! Снимите одежду, либо выпейте что-нибудь холодное!");
				p.damage(new Random().nextInt(2)+1);
				GepUtil.HashMapReplacer(pi.waits, "cold", 0, false, true);
			}
		}
		double CPS=pi.temp;
		if(CPS>65)st=ChatColor.DARK_RED+"КАК В ПЕЧИ!!";
		if(CPS<=50)st=ChatColor.RED+"очень жарко!";
		if(CPS<=35)st=ChatColor.GOLD+"жарко";
		if(CPS<=17)st=ChatColor.YELLOW+"жарковато...";
		if(CPS<=7)st=ChatColor.GREEN+"нормально!";
		if(CPS<=-7)st=ChatColor.AQUA+"прохладно...";
		if(CPS<=-17)st=ChatColor.BLUE+"холодно";
		if(CPS<=-35)st=ChatColor.DARK_BLUE+"ОЧ ХОЛОДНО!!";
		strings.add(ChatColor.GRAY+"Вам "+st+ChatColor.GRAY+" ("+znak+ChatColor.GRAY+")");*/
		long displtime = ((Bukkit.getWorld("world").getTime()+6000)/10)%100;
		displtime=(long) (displtime*0.60);
		String displt = new DecimalFormat("#00").format(displtime);
		strings.add(ChatColor.BLUE+"Время: "+ChatColor.AQUA+dayTime+":"+displt);
		String money = new DecimalFormat("#0.00").format(pi.money);
		strings.add(ChatColor.GREEN+"Деньги: "+ChatColor.DARK_GREEN+money+" руб.");
		GepUtil.oupscor(p, strings, ChatColor.AQUA+"---==={Gep Craft}===---");
	}
	public static FileConfiguration confP(String p){
		File file = new File(instance.getDataFolder() + File.separator + "info_"+p+".yml");
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
		if(!file.exists())GepUtil.saveCfg(fileConfig, file);
		return fileConfig;
	}
	public static void saveInfoToConfP(String p){
		File file = new File(instance.getDataFolder() + File.separator + "info_"+p+".yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		PlayerInfo pi=Events.plist.get(p);
		conf.set("Money",pi.money);
		for(String st:pi.perks.keySet()){
			Perk perk=pi.perks.get(st);
			conf.set("Perks."+st+".lvl",perk.lvl);
			conf.set("Perks."+st+".toNext",perk.toNext);
			conf.set("Perks."+st+".max",perk.max);
		}
		conf.set("leavedDay", day);
		conf.set("Items",null);
		conf.set("Dirt",pi.dirt);
		conf.set("Bank.depos",pi.bank.depos);
		conf.set("Bank.canTake",pi.bank.canTake);
		conf.set("Bank.deposDay",pi.bank.deposDay);
		conf.set("Bank.creditDay",pi.bank.creditDay);
		conf.set("Bank.creditPay",pi.bank.creditPay);
		conf.set("Bank.daysToCredit",pi.bank.daysToCredit);
		for(int i=0;i<54;i++){
			if(pi.inv.getItem(i)!=null&&!pi.inv.getItem(i).getType().equals(Material.IRON_BARS)){
				conf.set("Items."+i,pi.inv.getItem(i));
			}
		}
		for(pibus bus:pi.buss){
			bus.saveToConf("Buss."+bus.name, conf);
		}
		conf.set("Timers",null);
		for(String st:pi.timers.keySet()){
			conf.set("Timers."+st,pi.timers.get(st));
		}
		conf.set("Waits",null);
		for(String st:pi.waits.keySet()){
			conf.set("Waits."+st,pi.waits.get(st));
		}
		GepUtil.saveCfg(conf, file);
	}
	public static boolean loadInfoFroConf(String p){
		File file = new File(instance.getDataFolder() + File.separator + "info_"+p+".yml");
		if(!file.exists())return false;
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		PlayerInfo pi=Events.plist.get(p);
		pi.money=conf.getDouble("Money");
		pi.dirt=conf.getInt("Dirt");
		pi.bank.depos=conf.getInt("Bank.depos");
		pi.bank.canTake=conf.getDouble("Bank.canTake");
		pi.bank.deposDay=conf.getInt("Bank.deposDay");
		pi.bank.creditDay=conf.getInt("Bank.creditDay");
		pi.bank.creditPay=conf.getDouble("Bank.creditPay");
		pi.bank.daysToCredit=conf.getInt("Bank.daysToCredit");
		if(conf.contains("Perks"))for(String st:conf.getConfigurationSection("Perks").getKeys(false)){
			pi.perks.put(st, new Perk(conf.getInt("Perks."+st+".lvl"),conf.getInt("Perks."+st+".toNext"),conf.getInt("Perks."+st+".max")));
		}
		if(conf.contains("Items"))for(String st:conf.getConfigurationSection("Items").getKeys(false)){
			pi.inv.setItem(Integer.parseInt(st), conf.getItemStack("Items."+st));
		}
		if(conf.contains("Buss"))for(String st:conf.getConfigurationSection("Buss").getKeys(false)){
			pi.buss.add(new pibus("Buss."+st, conf));
		}
		if(conf.contains("Timers"))for(String st:conf.getConfigurationSection("Timers").getKeys(false)){
			pi.timers.put(st, conf.getInt("Timers."+st));
		}
		if(conf.contains("Waits"))for(String st:conf.getConfigurationSection("Waits").getKeys(false)){
			pi.waits.put(st, conf.getInt("Waits."+st));
		}
		return true;
	}
	public static boolean hasArmored(Player p){
		for(ItemStack item:p.getInventory().getArmorContents()){
			if(!GepUtil.isFullyItem(item, null, null, null)){
				return false;
			}
		}
		return true;
	}
	void drawLine(Player p, Location point2, double space, Particle part) {
		Location point1 = p.getLocation();
		point1.setY(point1.getY()+1);
	    World world = point1.getWorld();
	    Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
	    double distance = point1.distance(point2);
	    Vector p1 = point1.toVector();
	    Vector p2 = point2.toVector();
	    Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
	    double length = 0;
	    for (; length < distance; p1.add(vector)) {
	        p.spawnParticle(part, p1.getX(), p1.getY(), p1.getZ(), 1);
	        length += space;
	    }
	}
	public void circle(Player p, Location loc, double r, Particle part){
		Location l = loc.clone();
		l.subtract(r, 0, r/2);
		for(double t = 0; t < Math.PI*2; t+=1){
			double x = r * Math.sin(t);
			double z = r * Math.cos(t);
			l.add(x,0,z);
			p.spawnParticle(part, l,1,0.1,0.1,0.1,0);
		}
	}
}