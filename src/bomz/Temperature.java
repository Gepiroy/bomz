package bomz;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import ObjectsB.PlayerInfo;

public class Temperature {
	static Random r = main.r;
	public static float temperature=18;
	static float center=18;
	public static int gameDate=0;//0-15=весна;16-30=лето;31-45....
	public static float chance = 0.2f;
	
	public static void changeDay(){
		gameDate++;
		center+=r.nextFloat()*2-1;
	}
	
	public static void sec(){
		int dayTime = main.dayTime;
		
		//Пик - в 15, дно - в 3 ночи.
		
		int h = hoursAfter(dayTime, 3);
		int coef=1;
		if(h>12){
			h=hoursAfter(dayTime, 15);
			coef=-1;
		}
		float c = center+5*coef;
		float delta = c-temperature;//13--6.8=19.8
		//float dech = (5-delta)*0.2f;
		if(r.nextFloat()<=chance*Math.abs(delta*0.1)){
			if(delta>0)coef=1;
			else coef=-1;
			temperature+=0.1*coef;
		}
		
		
		for(Player p:Bukkit.getOnlinePlayers()){
			PlayerInfo pi = Events.plist.get(p.getName());
			pi.temp.affectByWorld(temperature);
		}
	}
	
	//6, 8 -> 22.
	public static int hoursAfter(int h, int after){
		h-=after;
		if(h<0)h+=24;
		return h;
	}
	
	public static void save(FileConfiguration globalc){
		globalc.set("T", temperature);
		globalc.set("TC", center);
	}
	
	public static void load(FileConfiguration conf){
		if(conf.contains("T"))temperature=(float) conf.getDouble("T");
		if(conf.contains("TC"))center=(float) conf.getDouble("TC");
	}
}
