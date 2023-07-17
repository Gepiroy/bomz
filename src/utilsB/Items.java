package utilsB;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {
	public static final ItemStack bottle = ItemUtil.create(Material.FLOWER_POT, 1, ChatColor.DARK_GREEN+"�������", new String[]{ChatColor.GRAY+"�� ������."});
	public static final ItemStack alcbottle = ItemUtil.create(Material.GLASS_BOTTLE, 1, ChatColor.BLUE+"������� � ��������� ��������", new String[]{ChatColor.YELLOW+"10 ���� ������ ����� ��������",ChatColor.YELLOW+"� 1 �������..."});
	public static final ItemStack menu = ItemUtil.create(Material.ENDER_EYE, 1, ChatColor.GREEN+"�������", new String[]{ChatColor.YELLOW+"Just... ���!"});
	public static final ItemStack alcohol = ItemUtil.createPotion(Material.POTION, 1, ChatColor.AQUA+"������� � ���������", new String[]{ChatColor.DARK_GREEN+"������� ��������.",ChatColor.DARK_GREEN+"�����������: "+ChatColor.RED+"55%",ChatColor.GOLD+"��� ������������� �����������",ChatColor.YELLOW+"������������� ������ � ����, �",ChatColor.YELLOW+"��������� ���� '������ ����'",ChatColor.GOLD+"�� ��������� �����."}, 100, 125, 0, null);
	public static final ItemStack pickKey = ItemUtil.create(Material.IRON_NUGGET, 1, ChatColor.RED+"�������", new String[]{ChatColor.LIGHT_PURPLE+"��������� ���������� ��������� �����."});
	public static final ItemStack foodbox = ItemUtil.create(Material.OAK_WOOD, 1, ChatColor.GOLD+"������� � ����", new String[]{ChatColor.RED+"��� ������� ����� ��������."});
	
	public static ItemStack more(ItemStack item, int am){
		ItemStack retItem = new ItemStack(item);
		retItem.setAmount(am);
		return retItem;
	}
	public static ItemStack item(String id, int am){
		ItemStack ret = ItemUtil.create(Material.BARRIER, "������id="+id);
		if(id.equals("preDie")){
			ret=ItemUtil.create(Material.PAPER, 1, 0, ChatColor.GOLD+"������������ �������", new String[]{
					ChatColor.YELLOW+"� � ������� ����� �������,",
					ChatColor.YELLOW+"����� ������� ��� ������ ���",
					ChatColor.YELLOW+"������. ��� �� ��� ����� ��",
					ChatColor.YELLOW+"������������ ����."
			}, null, 0);
		}
		/*
		 * 0-50 ���. - ������� �������, ��������� � ������ ������ ���������.
		 * 100 ���. - ������ ����������� ��� � ��������.
		 * 250 ���. - �������, ��������, ��� ��� � ������� �������� � ����� ��������� ���������.
		 * 500 ���. - ������ �������������� + ������� �������� �� �������� ������.
		 * 1000 ���. - ��������, ������� ��, ����� ������-������ ����, ��� ��� � ��������� ���� �����������! ������ �����,
		 *  �������� ���� � �������� ����� �� ��� ������, ����� ����� ��� �� �������!
		 */
		ret.setAmount(am);
		return ret;
	}
	public static ItemStack genBasicFood(Material mat, int am, String name, int toxic, int feed, int maxForOne, String[] addlore){
		return genFood(mat, am, name, toxic, feed, 0, 0, maxForOne, addlore);
	}
	public static ItemStack genFood(Material mat, int am, String name, int toxic, int feed, int heat, int psy, int maxForOne, String[] addlore){
		ItemStack ret = ItemUtil.create(mat, am, 0, name, null, null, 0);
		List<String> lore = new ArrayList<>();
		if(toxic>0)lore.add(ChatColor.DARK_GREEN+"�����������: "+ChatColor.RED+toxic+"%");
		lore.add(ChatColor.GOLD+"�������������: "+ChatColor.GREEN+feed);
		if(heat!=0)lore.add(ChatColor.YELLOW+"�������: "+GepUtil.boolCol(ChatColor.GOLD, ChatColor.AQUA, heat>0)+heat);
		if(psy!=0)lore.add(ChatColor.LIGHT_PURPLE+"����������: "+GepUtil.boolString(ChatColor.GREEN+"+"+psy, ChatColor.GOLD+""+psy, psy>0));
		if(maxForOne>1)lore.add(ChatColor.GREEN+"����� ������ "+ChatColor.YELLOW+maxForOne+" ��. "+ChatColor.GREEN+"�� ���!");
		for(String st:addlore){
			lore.add(st);
		}
		ItemMeta meta = ret.getItemMeta();
		meta.setLore(lore);
		ret.setItemMeta(meta);
		return ret;
	}
}
