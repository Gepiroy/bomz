package ObjectsB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import utilsB.ItemUtil;

public class Clotch {
	int heat=0;
	int prestige=0;
	int comfort=0;
	int dirt=0;
	Material mat = null;
	Color color = null;
	String prist=".";
	String name="";
	List<String> lore = new ArrayList<>();
	public Clotch(String Name){
		name=Name;
		if(name.contains("������")){
			color=Color.fromRGB(150, 150, 150);
			heat+=new Random().nextInt(6)+15;
			comfort+=new Random().nextInt(6)-5;
			dirt+=new Random().nextInt(11)+5;
			mat=Material.LEATHER_CHESTPLATE;
			prist="��";
		};
		if(name.contains("�������")){
			heat+=new Random().nextInt(11)+25;
			comfort-=new Random().nextInt(11)+10;
			mat=Material.LEATHER_CHESTPLATE;
			prist="��";
		};
		if(name.contains("�����-������")){
			prestige-=new Random().nextInt(5)+4;
			heat+=new Random().nextInt(6)+20;
			comfort+=new Random().nextInt(3)+5;
			mat=Material.LEATHER_HELMET;
			prist="��";
		}
		else if(name.contains("�����")){
			color=Color.fromRGB(0, 0, 0);
			heat+=new Random().nextInt(6)+15;
			comfort+=new Random().nextInt(3)+5;
			mat=Material.LEATHER_HELMET;
			prist="��";
		}
		if(name.contains("�����")){
			color=Color.fromRGB(255, 255, 255);
			heat+=new Random().nextInt(4);
			prestige+=new Random().nextInt(5)+4;
			mat=Material.LEATHER_HELMET;
			prist="��";
		}
		if(name.contains("���������")){
			color=Color.fromRGB(255, 255, 255);
			heat+=new Random().nextInt(4);
			comfort+=new Random().nextInt(10)+10;
			prestige+=new Random().nextInt(5)+8;
			dirt+=new Random().nextInt(11);
			mat=Material.LEATHER_BOOTS;
			prist="��";
		}
		if(name.contains("�������")){
			heat+=new Random().nextInt(6)+5;
			comfort+=new Random().nextInt(10)+7;
			dirt+=new Random().nextInt(11)+5;
			mat=Material.LEATHER_BOOTS;
			prist="��";
		}
		if(name.contains("�����")){
			heat+=new Random().nextInt(6)+4;
			comfort+=new Random().nextInt(6)+5;
			mat=Material.LEATHER_LEGGINGS;
			dirt+=new Random().nextInt(11)+5;
			prist="��";
		}
		if(name.contains("������")){
			heat+=new Random().nextInt(5)+7;
			prestige+=new Random().nextInt(5)+6;
			color=Color.fromRGB(100, 100, 200);
			dirt+=new Random().nextInt(11)+5;
			mat=Material.LEATHER_LEGGINGS;
			prist="��";
		}
	}
	public void addClotchType(String st){
		if(st.contains("����")){
			prestige-=new Random().nextInt(3);
			comfort+=new Random().nextInt(3)+5;
		}
		if(st.contains("���")){
			prestige+=new Random().nextInt(3)+3;
			comfort-=new Random().nextInt(2);
		}
		if(st.contains("��������")){
			prestige+=new Random().nextInt(3)+3;
			comfort+=new Random().nextInt(5)+7;
			heat-=new Random().nextInt(6)+5;
			dirt+=new Random().nextInt(5);
		}
		if(st.contains("����")){
			heat-=new Random().nextInt(5)+5;
			prestige-=new Random().nextInt(6)+10;
			comfort-=new Random().nextInt(5);
			dirt-=new Random().nextInt(11)+5;
		}
		if(st.contains("������")){
			prestige-=new Random().nextInt(6)+25;
			comfort-=new Random().nextInt(4)+5;
			dirt-=new Random().nextInt(11)+5;
		}
		if(st.contains("��������")){
			prestige-=new Random().nextInt(3);
			comfort+=new Random().nextInt(4)+2;
		}
		if(st.contains("�������")){
			heat+=new Random().nextInt(10);
			comfort-=new Random().nextInt(10);
		}
		if(st.contains("���������������")){
			comfort-=new Random().nextInt(10);
			dirt+=new Random().nextInt(11)+5;
		}
		st+=prist;
		lore.add(st);
	}
	public ItemStack create(){
		if(heat<0)heat=0;
		if(dirt<0)dirt=0;
		lore.add(ChatColor.GOLD+"�������: "+ChatColor.YELLOW+heat);
		lore.add(ChatColor.LIGHT_PURPLE+"�������: "+ChatColor.DARK_PURPLE+prestige);
		lore.add(ChatColor.AQUA+"�������: "+ChatColor.GREEN+comfort);
		lore.add(ChatColor.DARK_GREEN+"����-�����: "+ChatColor.BLUE+dirt);
		ItemStack item = ItemUtil.create(mat, name);
		ItemMeta meta = item.getItemMeta();
		if(color!=null)((LeatherArmorMeta) meta).setColor(color);
		meta.setUnbreakable(true);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
