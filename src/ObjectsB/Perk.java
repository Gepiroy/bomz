package ObjectsB;

import org.bukkit.configuration.file.FileConfiguration;

public class Perk {
	public int max=2;
	public int toNext=2;
	public int lvl=1;
	public Perk(int Lvl, int ToNext, int Max){
		toNext=ToNext;
		max=Max;
		lvl=Lvl;
	}
	public void load (FileConfiguration conf, String st){
		toNext=conf.getInt(st+".toNext");
		lvl=conf.getInt(st+".lvl");
	}
	public int addExp(int exp, int ToNext){
		toNext-=exp;
		if(toNext<=0){
			int back=-toNext;
			toNext=ToNext;
			max=ToNext;
			lvl++;
			return back;
		}
		return -1;
	}
}
