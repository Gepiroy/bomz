package ShopSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import ObjectsB.ShopItem;

public class ArmorStandInfo {
	public List<ShopItem> items = new ArrayList<>();
	public UUID id;
	public ArmorStandInfo(FileConfiguration conf, String st, UUID Id){
		id=Id;
		for(String s:conf.getConfigurationSection("Clotchs."+st).getKeys(false)){//Clotchs.test.0.id
			if(s.equals("equip"))continue;
			ArmorStand en=(ArmorStand) Bukkit.getEntity(id);
			if(en==null)continue;
			for(String cnum:conf.getConfigurationSection("Clotchs."+st+".equip").getKeys(false)){
				String cl = "Clotchs."+st+".equip."+cnum;
				ItemStack item=en.getEquipment().getHelmet();
				if(cnum.equals("1"))item=en.getEquipment().getChestplate();
				if(cnum.equals("2"))item=en.getEquipment().getLeggings();
				if(cnum.equals("3"))item=en.getEquipment().getBoots();
				items.add(new ShopItem("test", item, conf.getInt(cl+".price")));
			}
		}
	}
}
