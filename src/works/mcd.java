package works;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import ObjectsB.PlayerInfo;
import bomz.Events;
import bomz.main;
import utilsB.GepUtil;

public class mcd {
	static HashMap<String,String> macers=new HashMap<>();
	public static HashMap<String,Location> locs=new HashMap<>();
	public static void base(){
		for(Player p:Bukkit.getOnlinePlayers()){
			PlayerInfo pi = Events.plist.get(p.getName());
			if(pi.waits.containsKey("mac")){
				int mac = pi.waits.get("mac");
				if(macers.get(p.getName()).equals("potato")){
					Location loc = locs.get("potato_"+mac);
					if(loc!=null){
						p.spawnParticle(Particle.VILLAGER_HAPPY, loc.getX()+0.5, loc.getY()+1, loc.getZ()+0.5, 1, 0, 0, 0, 0);
						
					}
					if(p.getLocation().distance(loc)<=2){
						if(mac<=2||mac>3)progress(p);
					}
				}
			}
			GepUtil.HashMapReplacer(pi.waits, "macTime", 1, false, false);
		}
	}
	public static void click(Block b, Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		if(b.getType().equals(Material.OAK_WALL_SIGN)){
			if(!locs.containsKey("exit")){
				locs.put("exit", b.getLocation());
				p.sendMessage("added exit table location.");
			}
			else if(b.getLocation().equals(locs.get("exit"))){
				end(p);
				return;
			}
		}
		int mac = pi.waits.get("mac");
		if(macers.get(p.getName()).equals("potato")){
			if(locs.get("potato_"+mac)==null){
				locs.put("potato_"+mac, b.getLocation());
				p.sendMessage("locadded at num "+mac);
			}
			if(b.getLocation().equals(locs.get("potato_"+mac))){
				if(mac==3){
					progress(p);
				}
			}
		}
	}
	static void progress(Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		int mac = pi.waits.get("mac");
		if(pi.work.equals("mac")&&macers.get(p.getName()).equals("potato")){
			if(mac==1){
				p.sendTitle(ChatColor.YELLOW+"Порежь картошку!", ChatColor.GREEN+"Следуй зелёным точкам", 5, 25, 15);
			}
			if(mac==2){
				p.sendTitle(ChatColor.YELLOW+"Пожарь картошку!", ChatColor.GREEN+"Следуй зелёным точкам", 5, 25, 15);
			}
			if(mac==3){
				p.sendTitle(ChatColor.YELLOW+"Отдай картошку!", ChatColor.GREEN+"Следуй зелёным точкам", 5, 25, 15);
			}
			if(mac==4){
				mac=0;
				p.sendMessage(ChatColor.GREEN+"Вы получили 5 рублей за работу!");
				pi.money+=5;
				if(pi.waits.get("macTime")<30){
					p.sendMessage(ChatColor.DARK_GREEN+"И ещё 2 рубля за скорость!");
					pi.money+=2;
				}
				pi.waits.remove("macTime");
				start(p);
			}
			pi.waits.replace("mac", mac+1);
		}
	}
	public static void start(Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		if(!pi.work.equals("mac")){
			pi.work="mac";
			macers.put(p.getName(), "potato");
			pi.waits.put("mac", 1);
			p.teleport(main.locs.get("macJoinPlace"));
		}
		pi.waits.remove("macTime");
		p.sendTitle(ChatColor.YELLOW+"Возьми картошку!", ChatColor.GREEN+"Следуй зелёным точкам", 5, 25, 15);
		macers.replace(p.getName(), "potato");
	}
	public static void end(Player p){
		PlayerInfo pi = Events.plist.get(p.getName());
		pi.work="";
		macers.remove(p.getName());
		pi.waits.remove("mac");
		pi.waits.remove("macTime");
		p.teleport(locs.get("exitPlace"));
	}
}
