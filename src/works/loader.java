package works;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import utilsB.GepUtil;

public class loader {
	public Location boxLoc;
	public Location putLoc;
	public loader(Location loc){
		putLoc=loc.getBlock().getLocation().add(0.5, 0, 0.5);
	}
	public loader(FileConfiguration conf, String st){
		boxLoc=GepUtil.getLocFromConf(conf, st+".box");
		putLoc=GepUtil.getLocFromConf(conf, st+".put");
	}
	public void save(FileConfiguration conf, String st){
		GepUtil.saveLocToConf(conf, st+".box", boxLoc);
		GepUtil.saveLocToConf(conf, st+".put", putLoc);
	}
}
