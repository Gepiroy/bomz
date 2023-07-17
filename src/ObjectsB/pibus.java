package ObjectsB;

import org.bukkit.configuration.file.FileConfiguration;

public class pibus {
	public String name;
	public int moneyPerDay;
	public boolean own=false;
	public String owner="";
	public pibus(String Name, int MPD){
		name=Name;
		moneyPerDay=MPD;
	}
	public pibus(String st, FileConfiguration conf){
		name=conf.getString(st+".name");
		own=conf.getBoolean(st+".own");
		moneyPerDay=conf.getInt(st+".MPD");
	}
	public void saveToConf(String st, FileConfiguration conf){
		conf.set(st+".name", name);
		conf.set(st+".own", own);
		conf.set(st+".MPD", moneyPerDay);
	}
}
