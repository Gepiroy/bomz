package utilsB;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {
	public static final ItemStack bottle = ItemUtil.create(Material.FLOWER_POT, 1, ChatColor.DARK_GREEN+"Бутылка", new String[]{ChatColor.GRAY+"Не садись."});
	public static final ItemStack alcbottle = ItemUtil.create(Material.GLASS_BOTTLE, 1, ChatColor.BLUE+"Бутылка с остатками алкоголя", new String[]{ChatColor.YELLOW+"10 этих штучек можно перелить",ChatColor.YELLOW+"в 1 обычную..."});
	public static final ItemStack menu = ItemUtil.create(Material.ENDER_EYE, 1, ChatColor.GREEN+"Менюшка", new String[]{ChatColor.YELLOW+"Just... ПКМ!"});
	public static final ItemStack alcohol = ItemUtil.createPotion(Material.POTION, 1, ChatColor.AQUA+"Бутылка с алкоголем", new String[]{ChatColor.DARK_GREEN+"Предмет торговли.",ChatColor.DARK_GREEN+"Токсичность: "+ChatColor.RED+"55%",ChatColor.GOLD+"При использовании увеличивает",ChatColor.YELLOW+"Переносимость холода и жары, и",ChatColor.YELLOW+"уменьшает перк 'Прямые руки'",ChatColor.GOLD+"на некоторое время."}, 100, 125, 0, null);
	public static final ItemStack pickKey = ItemUtil.create(Material.IRON_NUGGET, 1, ChatColor.RED+"Отмычка", new String[]{ChatColor.LIGHT_PURPLE+"Позволяет взламывать некоторые замки."});
	public static final ItemStack foodbox = ItemUtil.create(Material.OAK_WOOD, 1, ChatColor.GOLD+"Коробка с едой", new String[]{ChatColor.RED+"Эту коробку можно взломать."});
	
	public static ItemStack more(ItemStack item, int am){
		ItemStack retItem = new ItemStack(item);
		retItem.setAmount(am);
		return retItem;
	}
	public static ItemStack item(String id, int am){
		ItemStack ret = ItemUtil.create(Material.BARRIER, "Ошибкаid="+id);
		if(id.equals("preDie")){
			ret=ItemUtil.create(Material.PAPER, 1, 0, ChatColor.GOLD+"Предсмертная записка", new String[]{
					ChatColor.YELLOW+"С её помощью можно указать,",
					ChatColor.YELLOW+"какое лечение вам окажут при",
					ChatColor.YELLOW+"смерти. Без неё вас лечат по",
					ChatColor.YELLOW+"максимальной цене."
			}, null, 0);
		}
		/*
		 * 0-50 руб. - Базовое лечение, выпускают в крайне плохом состоянии.
		 * 100 руб. - Помимо возрождения ещё и накормят.
		 * 250 руб. - Вылечат, накормят, так ещё и болезни заглушат и дадут выспаться нормально.
		 * 500 руб. - Полное восстановление + лечение болезней на неплохом уровне.
		 * 1000 руб. - Выпустят, вылечат всё, кроме какого-нибудь рака, так ещё и забаффают тебя ускорялками! Помимо этого,
		 *  запомнят тебя и откачают сразу же при смерти, чтобы никто вас не ограбил!
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
		if(toxic>0)lore.add(ChatColor.DARK_GREEN+"Токсичность: "+ChatColor.RED+toxic+"%");
		lore.add(ChatColor.GOLD+"Питательность: "+ChatColor.GREEN+feed);
		if(heat!=0)lore.add(ChatColor.YELLOW+"Теплота: "+GepUtil.boolCol(ChatColor.GOLD, ChatColor.AQUA, heat>0)+heat);
		if(psy!=0)lore.add(ChatColor.LIGHT_PURPLE+"Настроение: "+GepUtil.boolString(ChatColor.GREEN+"+"+psy, ChatColor.GOLD+""+psy, psy>0));
		if(maxForOne>1)lore.add(ChatColor.GREEN+"Можно съесть "+ChatColor.YELLOW+maxForOne+" шт. "+ChatColor.GREEN+"за раз!");
		for(String st:addlore){
			lore.add(st);
		}
		ItemMeta meta = ret.getItemMeta();
		meta.setLore(lore);
		ret.setItemMeta(meta);
		return ret;
	}
}
