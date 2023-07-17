package works;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import bomz.main;
import utilsB.GepUtil;

public class begging {
	public Location loc;
	public int max;
	//range default is 2
	public int people=0;
	public begging(Location Loc, int Max){
		loc=Loc.getBlock().getLocation().add(0.5, 0, 0.5);
		max=Max;
		change();
	}
	public begging(FileConfiguration conf, String st){
		loc=GepUtil.getLocFromConf(conf, st+".loc");
		max=conf.getInt(st+".max");
		change();
	}
	public void change(){
		people=(int) (max/100.00*((12-(Math.abs(main.dayTime-12)))*7.5)+max/10);
	}
	public void save(FileConfiguration conf, String st){
		GepUtil.saveLocToConf(conf, st+".loc", loc);
		conf.set(st+".max", max);
	}
}
