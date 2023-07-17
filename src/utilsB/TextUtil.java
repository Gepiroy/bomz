package utilsB;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import bomz.main;

public class TextUtil {
	public static final String PP=ChatColor.LIGHT_PURPLE+"ОВ";
	public static String twoCols(String st, ChatColor c1, ChatColor c2, int iter, String addict){
		String RAID="";
		for(int i=0;i<st.length();i++){
			char c=st.charAt(i);
			RAID+=GepUtil.boolCol(c1, c2, (iter+i)%2==0)+addict+c;
		}
		return RAID;
	}
	public static String hours(int m){
		if(m%10==0||m%10>=5||(m%100>10&&m%100<20))return m+" часов";
		if(m%10==1)return m+" час";
		if(m%10>=2&&m%10<=4)return m+" часа";
		return m+" ч.";
	}
	public static String minutes(int m){
		if(m%10==0||m%10>=5||(m%100>10&&m%100<20))return m+" минут";
		if(m%10==1)return m+" минута";
		if(m%10>=2&&m%10<=4)return m+" минуты";
		return m+" мин.";
	}
	public static String secundes(int m){
		if(m%10==0||m%10>=5||(m%100>10&&m%100<20))return m+" секунд";
		if(m%10==1)return m+" секунда";
		if(m%10>=2&&m%10<=4)return m+" секунды";
		return m+" сек.";
	}
	public static String minutesto(int m){
		if(m%10==0||m%10>=5||(m%100>10&&m%100<20))return m+" минут";
		if(m%10==1)return m+" минуту";
		if(m%10>=2&&m%10<=4)return m+" минуты";
		return m+" мин.";
	}
	public static String secundesto(int m){
		if(m%10==0||m%10>=5||(m%100>10&&m%100<20))return m+" секунд";
		if(m%10==1)return m+" секунду";
		if(m%10>=2&&m%10<=4)return m+" секунды";
		return m+" сек.";
	}
	public static String string(ChatColor defCol, String text){
		return string(defCol+"", text);
	}
	public static String string(String text){
		return string(ChatColor.GRAY+"", text);
	}
	public static void mes(Player p, String pref, String mes){
		if(pref==null)p.sendMessage(string(mes));
		else p.sendMessage(string("["+pref+"|] &f"+mes));
	}
	public static void globMessage(String pref, String mes){
		GepUtil.globMessage(string("["+pref+"|] &f"+mes));
	}
	public static void globMessage(String prefix, String mes, Sound sound, float vol, float speed, String title, String subtitle, int spawn, int hold, int remove){
		String chat=string(ChatColor.GRAY, "["+prefix+"|] &f"+mes);
		String tit=null;
		if(title!=null)tit=string(title);
		String sub=null;
		if(subtitle!=null)sub=string(subtitle);
		for(Player p:Bukkit.getOnlinePlayers()){
			if(mes!=null)p.sendMessage(chat);
			if(sound!=null){
				p.playSound(p.getLocation(), sound, vol, speed);
			}
			if(title!=null||subtitle!=null) {
				p.sendTitle(tit, sub, spawn, hold, remove);
			}
		}
	}
	public static void globTitle(String up, String down, int spawn, int hold, int out){
		for(Player p:Bukkit.getOnlinePlayers()){
			p.sendTitle(string(up), string(down), spawn, hold, out);
		}
	}
	public static String string(String defCol, String text){
		String ret=defCol;
		for(int i=0;i<text.length();i++){
			char c=text.charAt(i);
			if((c+"").equals("|"))ret+=defCol;
			else if((c+"").equals("$"))ret+=ChatColor.DARK_GREEN+"$"+defCol;
			else if((c+"").equals("~")){
				if((text.charAt(i+1)+"").equals("A"))ret+=ChatColor.BLUE+"~A~"+defCol;
				if((text.charAt(i+1)+"").equals("P"))ret+=ChatColor.LIGHT_PURPLE+"ОВ"+defCol;
				if((text.charAt(i+1)+"").equals("T"))ret+=ChatColor.AQUA+"["+ChatColor.GREEN+"Обучение"+ChatColor.AQUA+"]";
				i++;
			}else if((c+"").equals("&"))ret+="§";
			else ret+=c;
		}
		return ret;
	}
	static List<Double> doublesFromString(String st){
		List<Double> ret=new ArrayList<>();
		Pattern pat=Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
		Matcher matcher=pat.matcher(st);
		while (matcher.find()) {
			ret.add(Double.parseDouble(matcher.group()));
		}
		return ret;
	}
	public static double lastDoubleFromString(String st){
		List<Double> pret = doublesFromString(st);
		if(pret.size()==0)return 0;
		return pret.get(pret.size()-1);
	}
	
	public static void debug(String st){
		Bukkit.getConsoleSender().sendMessage(string("deb from &6"+main.instance.getName()+"&f: "+st));
	}
}
