package ObjectsB;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import bomz.main;

public class PeoplePlace {
	public Location loc;
	public int max=10;
	public int people=0;
	public int range;
	public PeoplePlace(Location Loc, int Max, int Range){
		loc=Loc.getBlock().getLocation().add(0.5, 0, 0.5);
		max=Max;
		range=Range;
	}
	public void change(){
		people=(int) (max/100.00*((12-(Math.abs(main.dayTime-12)))*7.5)+10);
	}
	public void circle(Player p){
		Location l = loc.clone();
		double r=(l.getY()-20)/10.0;
		l.subtract(r*2, 0, r/2);
		for(double t = 0; t < Math.PI * 2; t+=0.5){
			double x = r * Math.sin(t);
			double z = r * Math.cos(t);
			l.add(x,0,z);
			p.spawnParticle(Particle.FLAME, l,1,0.1,0.1,0.1,0);
		}
	}
}
