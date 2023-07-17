package ObjectsB;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CleanWork {
	public Location loc1;
	public Location loc2;
	public Location joinSign;
	public CleanWork(Location Loc1, Location Loc2, Location jSign){
		loc1=Loc1;
		loc2=Loc2;
		joinSign=jSign;
	}
	public CleanWork(Player p){
	}
	Location randLocBetweenTwo(){
		int xmin=0;
		int ymin=0;
		int zmin=0;
		if(loc1.getBlockX()>loc2.getBlockX())xmin=loc2.getBlockX();else xmin=loc1.getBlockX();
		if(loc1.getBlockY()>loc2.getBlockY())ymin=loc2.getBlockY();else ymin=loc1.getBlockY();
		if(loc1.getBlockZ()>loc2.getBlockZ())zmin=loc2.getBlockZ();else zmin=loc1.getBlockZ();
		int x = Math.abs(loc1.getBlockX()-loc2.getBlockX());
		int y = Math.abs(loc1.getBlockY()-loc2.getBlockY());
		int z = Math.abs(loc1.getBlockZ()-loc2.getBlockZ());
		return new Location(loc1.getWorld(),xmin+new Random().nextInt(x+1),ymin+new Random().nextInt(y+1),zmin+new Random().nextInt(z+1));
	}
}
