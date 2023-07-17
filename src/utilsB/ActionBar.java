package utilsB;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;

public class ActionBar {
	    static PacketPlayOutChat packet;

	    public ActionBar(String text) {
	        PacketPlayOutChat Packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"),ChatMessageType.GAME_INFO,UUID.randomUUID());
	        packet = Packet;
	    }
	   
	    public void sendToPlayer(Player p) {
	        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	    }
	   
	    public void sendToAll() {
	        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
	            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	        }
	    }
}
