package ObjectsB;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import utilsB.ItemUtil;

public class Help {
	public Material display;
	public String name;
	public String shrt;
	public String lng;
	public Help(Material dis, String Name, String s, String l){
		display=dis;
		name=Name;
		shrt=s;
		lng=l;
	}
	public Help(FileConfiguration conf, String st){
		name=st;
		display=Material.getMaterial(conf.getString("Help."+st+".mat"));
		shrt=conf.getString("Help."+st+".short");
		lng=conf.getString("Help."+st+".long");
	}
	public void save(FileConfiguration conf){
		conf.set("Help."+name+".mat",display+"");
		conf.set("Help."+name+".short",shrt);
		conf.set("Help."+name+".long",lng);
	}
	public ItemStack GUIItem(){
		return ItemUtil.create(display, 1, name, new String[]{
				ChatColor.GREEN+"ЛКМ, чтобы узнать вкратце"
				,ChatColor.AQUA+"ПКМ, чтобы узнать подробно"
		});
	}
}
