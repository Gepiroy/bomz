package ObjectsB;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import bomz.Events;
import bomz.GUI;
import bomz.main;
import invsUtil.Inv;
import invsUtil.Invs;
import utilsB.GepUtil;
import utilsB.ItemUtil;

public class BottlePlace {
	public Location loc;
	public double price=0.5;
	public int ownerPrice=0;
	public boolean priceChanged=false;
	public String owner="";
	public business bus;
	public BottlePlace(Location Loc){
		loc=Loc;
		bus=new business("bottle"+GepUtil.locInfo(Loc), 10000, 25000, 1000, 2000);
	}
	
	public static final Inv shop = new Inv(ChatColor.DARK_GREEN+"�������") {
		
		@Override
		public void displItems(Inventory inv) {
			Location loc = pi.lastClickLoc;
			BottlePlace place = null;
			for(BottlePlace pl:main.bottlePlaces){
				if(loc.equals(pl.loc))place=pl;
			}
			inv.setItem(13, ItemUtil.create(Material.FLOWER_POT, 1, ChatColor.DARK_GREEN+"����� �������", new String[]{
					ChatColor.DARK_GREEN+"1 ������� ����� ����� "+ChatColor.GREEN+new DecimalFormat("#0.0").format(place.price)+" ���."
					,ChatColor.YELLOW+"�� �������� �� ��� "+ChatColor.GOLD+new DecimalFormat("#0.0").format(GUI.CountOfItem(Material.FLOWER_POT, ChatColor.DARK_GREEN+"�������", p)*place.price)
			}));
			if(!pi.buss.contains("bottle"+GepUtil.locInfo(loc)))inv.setItem(22, ItemUtil.create(Material.EMERALD, 1, ChatColor.DARK_GREEN+"������ ������", new String[]{
					ChatColor.DARK_GREEN+"����� ���-���������� ����� �������."
					,ChatColor.LIGHT_PURPLE+"���-��������� �������� 10% �������."
					,ChatColor.GOLD+"����: "+GepUtil.boolCol(pi.money>=10000)+"10 000 ���."
			}));
			if(pi.buss.contains("bottle"+GepUtil.locInfo(loc)))inv.setItem(22, ItemUtil.create(Material.EMERALD, 1, ChatColor.DARK_GREEN+"������ ������", new String[]{
					ChatColor.DARK_GREEN+"����� ����� �� ���������� ����� �������."
					,ChatColor.LIGHT_PURPLE+"��������� �������� 25% �������."
					,ChatColor.LIGHT_PURPLE+"������ ����. ����� ��������� �����"
					,ChatColor.GREEN+"������� ����������"
					,ChatColor.AQUA+"(����. ������ ����, ������� x2.)"
					,ChatColor.RED+"����. �������� ������� �� �����."
					,ChatColor.GRAY+"(�� 0:00 ���. ������� ��� �� ������ �� ����)."
					,ChatColor.GOLD+"����: "+GepUtil.boolCol(pi.money>=25000)+"25 000 ���."
			}));
			if(pi.buss.contains("ownbottle"+GepUtil.locInfo(loc)))inv.setItem(22, ItemUtil.create(Material.NETHER_STAR, 1, ChatColor.AQUA+"����. ������� � �������.", new String[]{
					ChatColor.GREEN+"�� ��� ����� ���������� ����� �������."
			}));
			if(pi.buss.contains("ownbottle"+GepUtil.locInfo(loc))&&place.ownerPrice>0&&!place.owner.equals(p.getName()))inv.setItem(14, ItemUtil.create(Material.GOLDEN_HELMET, 1, ChatColor.GOLD+"������ ������� �����", new String[]{
					ChatColor.GREEN+"����. �������� ����� ������� ��� ����� �� "+GepUtil.boolCol(pi.money>=place.ownerPrice)+" ���."
					,ChatColor.RED+"����. �������� ������� �� �����."
					,ChatColor.GRAY+"(�� 0:00 ���. ������� ��� �� ������ �� ����)."
			}));
			if(place.ownerPrice==0&&place.owner.equals(p.getName()))inv.setItem(14, ItemUtil.create(Material.GOLDEN_HELMET, 1, ChatColor.GOLD+"������� ������� �����", new String[]{
					ChatColor.GREEN+"�� ������ ��������� ����. ����� �� �������."
					,ChatColor.GOLD+"������ ��������� ������ ������ � ��� ��� �����."
					,ChatColor.YELLOW+"�� ������� ������� ���������� (������� �� 25�)."
			}));
			if(place.ownerPrice>0&&place.owner.equals(p.getName()))inv.setItem(14, ItemUtil.create(Material.GOLDEN_HELMET, 1, ChatColor.GOLD+"�� ��� ������� ��� �����.", new String[]{
					ChatColor.RED+"�� ������� ��� ����� �� "+place.ownerPrice+" ���."
					,ChatColor.RED+"�������� ���� ������ ������."
			}));
			if(place.owner.equals(p.getName()))inv.setItem(4, ItemUtil.create(Material.GLASS, 1, ChatColor.GREEN+"���������� ���� �� ������", new String[]{
					ChatColor.YELLOW+"������ ���� ������ ������� ������"
					,"�� "+place.price+" ���./��. "+ChatColor.GREEN+"(����� �����: "+GepUtil.CylDouble((main.bottlePrice-place.price*1.000), "#0.000")+"���./��.)"
					,GepUtil.boolString(ChatColor.RED+"�������� ���� ������ ������.", ChatColor.GREEN+"����, ����� �������� ����!", place.priceChanged)
			}));
		}
		
		@Override
		public void click(InventoryClickEvent e) {
			BottlePlace place = null;
			for(BottlePlace plac:main.bottlePlaces){
				if(p.getLocation().distance(plac.loc)<=10){
					place=plac;
					break;
				}
			}
			if(place==null){p.sendMessage(ChatColor.RED+"���?! ����� ������� �� �������� ��� ������ �� ������?");return;}
			if(e.getCurrentItem().getType().equals(Material.FLOWER_POT)){
				int cOb=GUI.CountOfItem(Material.FLOWER_POT, ChatColor.DARK_GREEN+"�������", p);
				double sold = GepUtil.sellItems(p, Material.FLOWER_POT, null, 1000)*place.price;
				p.sendMessage(ChatColor.GREEN+"�� ������� "+ChatColor.YELLOW+cOb+ChatColor.GREEN+" ������� �� "+ChatColor.DARK_GREEN+GepUtil.CylDouble(sold, "#0.00")+" ���.");
				pi.money+=sold;
				pi.addExp((int) (sold/2.5));
				if(pi.learnTitle!=null&&pi.learnTitle.contains("�������")){
					pi.learnMessage=null;
					pi.learnTitle=null;
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 0);
					p.sendTitle("��������", "��������", 10, 50, 10);
					p.sendMessage(ChatColor.GREEN+"�� ������ ��������! ������ �� ��� �� ����.");
				}
				for(Player pl:Bukkit.getOnlinePlayers()){
					PlayerInfo pip = Events.plist.get(pl.getName());
					if(pip.buss.contains("bottle"+GepUtil.locInfo(place.loc))){
						int coef=10;
						if(pip.buss.contains("ownbottle"+GepUtil.locInfo(place.loc))){
							coef+=15;
							if(place.owner.equals(pl.getName())){
								coef+=25;
							}
						}
						pip.money+=cOb*(main.bottlePrice-place.price)/coef;
						if(cOb>=100){
							pl.sendMessage(ChatColor.GOLD+p.getName()+ChatColor.GREEN+" ������ "+ChatColor.GOLD+cOb+" ������� "+ChatColor.GREEN+"� ����� ������ �����! "+ChatColor.DARK_GREEN+"�� �������� "+(cOb*(main.bottlePrice-place.price)/coef)+" ���.");
						}
					}
				}
				p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
				p.closeInventory();
			}
			if(e.getCurrentItem().getType().equals(Material.EMERALD)){
				if(pi.hasBuss("bottle"+GepUtil.locInfo(place.loc))){
					if(GUI.buy(25000, p)){
						//business toAdd = place.bus;
						//pi.buss.add("ownbottle"+GepUtil.locInfo(place.loc));
						p.sendMessage(ChatColor.LIGHT_PURPLE+"�� ����� ����� �� ���������� ������� �������! ������ �� ��������� 25% ������� ����� �����, � � ��� ���� ���� ����� "+ChatColor.GREEN+"������� ����������! "+ChatColor.YELLOW+"(����������� ������ ����, ��������� x2 ������.)");
						if(place.owner.equals(""))place.changeOwner();
					}
				}
				else{
					if(GUI.buy(10000, p)){
						//pi.buss.add("bottle"+GepUtil.locInfo(place.loc));
						p.sendMessage(ChatColor.LIGHT_PURPLE+"�� ����� ����� �� ���-���������� ������� �������! ������ �� ��������� 10% ������� ����� �����!");
					}
				}
				p.closeInventory();
			}
			if(e.getCurrentItem().getType().equals(Material.GLASS)){
				if(place.owner.equals(p.getName())){
					if(!place.priceChanged){
						place.changePriceGUI(p);
					}
					else{
						p.sendMessage(ChatColor.RED+"�� ��� ������ ���� � ���� ������.");
					}
				}
			}
			if(place.owner.equals(p.getName())&&main.bottlePrice>GepUtil.intFromString(e.getCurrentItem().getItemMeta().getDisplayName())/10.0){
				place.price=GepUtil.intFromString(e.getCurrentItem().getItemMeta().getDisplayName())/10.0;
				place.priceChanged=true;
				p.closeInventory();
				for(Player pl:Bukkit.getOnlinePlayers()){
					PlayerInfo pli=Events.plist.get(pl.getName());
					if(pli.buss.contains("bottle"+GepUtil.locInfo(place.loc))){
						pl.sendMessage(ChatColor.GOLD+p.getName()+ChatColor.YELLOW+" ������� ���� ������� � ����� ������ �� "+ChatColor.GOLD+GepUtil.intFromString(e.getCurrentItem().getItemMeta().getDisplayName())/10.0+ChatColor.YELLOW+".");
					}
				}
			}
			return;
		}
	};
	
	public void openShop(Player p){
		Invs.open(p, shop);
	}
	public void changePriceGUI(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN+"������� ���� �� �������");
		double pr=0.5;
		for(int i=2;i<25;i++){
			Material mat = Material.GLASS;
			if(pr>=main.bottlePrice)mat=Material.IRON_BARS;
			if(i==7||i==16)i+=4;
			inv.setItem(i, ItemUtil.create(mat, 1, ChatColor.DARK_GREEN+""+GepUtil.CylDouble(pr, "#0.0")+" ���./�������", new String[]{
				ChatColor.GOLD+"����� ����� ��� ����� "+main.bottlePrice+"�./���. ����� "+GepUtil.boolCol(main.bottlePrice-pr>0)+GepUtil.CylDouble((main.bottlePrice-pr*1.000),"#0.000")+"�./���."
				,GepUtil.boolString(ChatColor.RED+"��� �� �������. ������ ����� � �����!!!", ChatColor.GREEN+"�� ��������� ��� �����������.", pr>=main.bottlePrice)
		}));
			pr+=0.1;
		}
		p.openInventory(inv);
	}
	public void changeOwner(){
		ArrayList<String> canBeOwn = new ArrayList<>();
		for(Player p:Bukkit.getOnlinePlayers()){
			for(pibus b:Events.plist.get(p.getName()).buss){
				if(b.name.equals("bottle"+GepUtil.locInfo(loc))&&b.own){
					canBeOwn.add(p.getName());
					break;
				}
			}
		}
		if(canBeOwn.size()>0){
			ownerPrice=0;
			if(Bukkit.getPlayer(owner)!=null)Bukkit.getPlayer(owner).sendMessage(ChatColor.RED+"�� �������� ��� ������� ����� � ������ ����� ������� "+ChatColor.GRAY+GepUtil.locInfo(loc)+ChatColor.RED+".");
			owner=canBeOwn.get(new Random().nextInt(canBeOwn.size()));
			Bukkit.getPlayer(owner).sendMessage(ChatColor.AQUA+"�� ������ ����� �������� � ������ ����� ������� "+ChatColor.GRAY+GepUtil.locInfo(loc)+ChatColor.AQUA+"!");
			Bukkit.getPlayer(owner).sendMessage(ChatColor.LIGHT_PURPLE+"������ �� ��������� � 2 ���� ������ ������ ������, ������ �������� ���� �� �������, ��� �� ������ ��������� �� ������� ��� �����. �� ��� �������� � ����� ������.");
		}
	}
}
