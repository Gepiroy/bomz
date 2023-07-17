package ObjectsB;

import java.util.Random;

import org.bukkit.entity.Player;

import bomz.Events;
import bomz.main;

public class BodyTemp {
	static Random r = main.r;
	public static final float perfectBody = 36.6f;
	public static final float perfect = 21;
	final Player p;
	
	public BodyTemp(Player p){
		this.p=p;
	}
	
	public float temp = perfectBody;
	
	public void affectByWorld(float t){//every sec
		if(p==null)return;
		PlayerInfo pi=Events.plist.get(p.getName());
		//20-25 = �������� ��� ��������� ������.
		//18-24 ��� �������...
		
		//����� 21 - ��������� ������, +-3 - ����������, � ������ - ��������� �� ���������.
		
		float differ = temp-t;//] t=15 => differ=21.6
		
		t+=differ*pi.tepl*0.01;
		
		float affect = t-perfect;//-61
		
		if(affect-3>0){//���������
			affect-=3;
			if(r.nextFloat()<=affect*0.2){
				pi.notif("hot", "&6�����", "�� ������ ������ �� �� ����...");
				pi.dirt++;
			}
		}else if(affect+3<0){//�����������
			affect+=3;
			if(r.nextFloat()<=-affect*0.2){
				pi.notif("cold", "&2�������", "�� �� ������ �� ������ ������ ����.");
				pi.hunger-=10;
			}
		}else{
			pi.unnotif("hot", "&a���������", "��� ������ �� �����.");
			pi.unnotif("cold", "&a���������", "��� ������ �� �������.");
		}
	}
}
