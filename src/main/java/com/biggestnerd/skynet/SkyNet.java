package com.biggestnerd.skynet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

@Mod(modid="skynet", name="SkyNet", version="v1.1.0")
public class SkyNet {
	
	private Minecraft mc = Minecraft.getMinecraft();
	private boolean enabled = true;
	private KeyBinding toggle;
	private List<String> previousPlayerList = new ArrayList();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)  {
    	FMLCommonHandler.instance().bus().register(this);
    	MinecraftForge.EVENT_BUS.register(this);
    	toggle = new KeyBinding("Toggle SkyNet", Keyboard.KEY_N, "SkyNet");
        ClientRegistry.registerKeyBinding(toggle);     
    }
    
    public String filterChatColors(String s) {
    	return EnumChatFormatting.getTextWithoutFormattingCodes(s);
    }
    
    public void onPlayerLeave(String player) {
    	mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + "[SkyNet] "+ EnumChatFormatting.GRAY + player + " left the game"));
    }
    
    public void onPlayerJoin(String player) {
    	mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + "[SkyNet] "+ EnumChatFormatting.GRAY + player + " joined the game"));
    }

	@SubscribeEvent
    public void onTick(ClientTickEvent event) {
    	if(event.phase == TickEvent.Phase.START) {
    		if(enabled) {
    			if(mc.theWorld != null) { 		
    			ArrayList<String> playerList = new ArrayList();
    			Collection players = mc.getNetHandler().func_175106_d();
    			for(Object o : players) {
    				if((o instanceof NetworkPlayerInfo)) {
    					NetworkPlayerInfo info = (NetworkPlayerInfo)o;
    					playerList.add(filterChatColors(info.getGameProfile().getName()));
    				}
    			}
    			ArrayList<String> temp = (ArrayList)playerList.clone();
    			playerList.removeAll(previousPlayerList);
    			previousPlayerList.removeAll(temp);
    			for(String player : previousPlayerList) {
    				onPlayerLeave(player);
    			}
    			for(String player : playerList) {
    				onPlayerJoin(player);
    			}
    			previousPlayerList = temp;
    			}
    		}
    	}
    }
	
	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(toggle.isKeyDown()){
        	enabled = !enabled;
        	mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + "[SkyNet] " + EnumChatFormatting.GRAY + "SkyNet " + (enabled ? "enabled" : "disabled")));
        }              
    }
}