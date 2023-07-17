package bomz;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ObjectsB.Heat;
import ObjectsB.House;
import ObjectsB.PlayerInfo;
import ObjectsB.Shaurma;
import ObjectsB.Shop;
import utilsB.GepUtil;
import works.begging;
import works.loader;
import works.mcd;

public class cmd implements CommandExecutor{
	public static HashMap<String,Shop> shopsWaits = new HashMap<>();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			GepUtil.debug(label+args, sender.getName(), "info");
			Player p = (Player) sender;
			PlayerInfo pi = Events.plist.get(p.getName());
			if(!p.isOp()){
				p.sendMessage(ChatColor.RED+"Только великий Гепи знает, что делает эта команда!");
				return true;
			}
			if(args.length>0){
				if(args[0].equalsIgnoreCase("mac")){
					if(args[1].equalsIgnoreCase("exitPlace")){
						mcd.locs.put("exitPlace", p.getLocation());
						p.sendMessage("setted location of mac leave place.");
					}
					if(args[1].equalsIgnoreCase("joinTable")){
						pi.waits.put("macJoinTable", 1);
						p.sendMessage("so, only our god, Geppy, knows what to do now :D");
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("shop")){
					if(args.length<3){
						p.sendMessage("/loc shop [name] [type]");
						return true;
					}
					shopsWaits.put(sender.getName(), new Shop(null, args[2], args[1]));
					return true;
				}
				if(args[0].equalsIgnoreCase("heat")){
					if(args.length<3){
						p.sendMessage("/loc heat [min] [max]");
						return true;
					}
					main.heats.add(new Heat(p.getLocation(), Double.parseDouble(args[1]), Double.parseDouble(args[2])));
					return true;
				}
				if(args[0].equalsIgnoreCase("house")){
					if(args.length!=2){
						p.sendMessage("/loc house [type]");
						return true;
					}
					File hfile = new File(main.instance.getDataFolder() + File.separator + "houses.yml");
					FileConfiguration hconf = YamlConfiguration.loadConfiguration(hfile);
					if(!hconf.contains("Types."+args[1])){
						new House(args[1]).SaveType(args[1], hconf);
					}
					GepUtil.saveCfg(hconf, hfile);
					House h = new House(args[1]);
					h.owner=p.getName();
					main.houses.add(h);
					return true;
				}
				if(args[0].equalsIgnoreCase("beg")){
					if(args.length!=2){
						p.sendMessage("/loc beg [max]");
						return true;
					}
					main.beggs.add(new begging(p.getLocation(), Integer.parseInt(args[1])));
				}
				if(args[0].equalsIgnoreCase("bottle")){
					pi.waits.put("bottleLoc", 1);
					p.sendMessage("so, only our god, Geppy, knows what to do now :D");
					return true;
				}
				if(args[0].equalsIgnoreCase("loader")){
					main.loads.add(new loader(p.getLocation()));
					pi.waits.put("loaderLoc", 1);
					p.sendMessage("so, only our god, Geppy, knows what to do now :D");
					return true;
				}
				if(args[0].equalsIgnoreCase("priut")){
					pi.waits.put("priutloc", 1);
					p.sendMessage("so, only our god, Geppy, knows what to do now :D");
					return true;
				}
				if(args[0].equalsIgnoreCase("med")){
					main.meds.add(p.getLocation());
					return true;
				}
				if(args[0].equalsIgnoreCase("sha")){
					if(args.length<6){
						p.sendMessage("/loc sha [price] [sub] [sell] [pois] [name]");
						return true;
					}
					main.shas.add(new Shaurma(null, Double.parseDouble(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), args[5]));
					return true;
				}
				if(args[0].equalsIgnoreCase("clotch")){
					if(args.length<3){
						p.sendMessage("/loc clotch [name] [type]");
						updateClotch();
						return true;
					}
					boolean searched=false;
					File file = new File(main.instance.getDataFolder() + File.separator + "clotch.yml");
					FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
					for(Entity en:p.getWorld().getNearbyEntities(p.getLocation(), 1, 1, 1)){
						if(en.getType().equals(EntityType.ARMOR_STAND)){
							int i=0;
							if(conf.contains("Clotchs."+args[2])){
								i=conf.getConfigurationSection("Clotchs."+args[2]).getKeys(false).size();
							}else{
								for(int in=0;in<4;in++){
									String st="Clotchs."+args[2]+".equip."+in;
									conf.set(st+".chance", "50");
									if(in==0)conf.set(st+".name", "Шапка");
									if(in==1)conf.set(st+".name", "Куртка");
									if(in==2)conf.set(st+".name", "Штаны");
									if(in==3)conf.set(st+".name", "Ботинки");
									List<String> toSet=new ArrayList<>();
									toSet.add("Рван");
									toSet.add("Помойн");
									conf.set(st+".subs", toSet);
								}
							}
							String st="Clotchs."+args[2]+"."+i;
							conf.set(st+".id", en.getUniqueId()+"");
							searched=true;
						}
					}
					GepUtil.saveCfg(conf, file);
					if(searched){
						p.sendMessage("ok");
					}
					else{
						p.sendMessage("nok");
					}
					return true;
				}
				if(args.length==1){
					main.locs.put(args[0], p.getLocation());
				}
			}
		}
		return true;
	}
	public static void updateClotch(){
		File file = new File(main.instance.getDataFolder() + File.separator + "clotch.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		for(String st:conf.getConfigurationSection("Clotchs").getKeys(false)){
			for(String s:conf.getConfigurationSection("Clotchs."+st).getKeys(false)){//Clotchs.test.0.id
				if(s.equals("equip"))continue;
				ArmorStand en=(ArmorStand) Bukkit.getEntity(UUID.fromString(conf.getString("Clotchs."+st+"."+s+".id")));
				if(en==null)continue;
				for(String cnum:conf.getConfigurationSection("Clotchs."+st+".equip").getKeys(false)){
					String cl = "Clotchs."+st+".equip."+cnum;
					if(cnum.equals("0"))en.getEquipment().setHelmet(Events.NewClotchCreate(new String[]{conf.getString(cl+".name")}, conf.getStringList(cl+".subs"), 1));
					if(cnum.equals("1"))en.getEquipment().setChestplate(Events.NewClotchCreate(new String[]{conf.getString(cl+".name")}, conf.getStringList(cl+".subs"), 1));
					if(cnum.equals("2"))en.getEquipment().setLeggings(Events.NewClotchCreate(new String[]{conf.getString(cl+".name")}, conf.getStringList(cl+".subs"), 1));
					if(cnum.equals("3"))en.getEquipment().setBoots(Events.NewClotchCreate(new String[]{conf.getString(cl+".name")}, conf.getStringList(cl+".subs"), 1));
					
				}
			}
		}
	}
}
