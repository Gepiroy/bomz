package ObjectsB;

import org.bukkit.Location;

public class CleanPlace {
	public Location loc;
	public int clicks;
	public int regen;
	public CleanPlace(Location Loc, int Cli, int Regen){
		clicks=Cli;
		loc=Loc;
		regen=Regen;
	}
}
