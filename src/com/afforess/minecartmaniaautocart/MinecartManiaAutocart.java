package com.afforess.minecartmaniaautocart;

import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.config.MinecartManiaConfigurationParser;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class MinecartManiaAutocart extends JavaPlugin {
    
    public static MinecartManiaLogger log = MinecartManiaLogger.getInstance();
    public static Server server;
    public static PluginDescriptionFile description;
    private static final AutocartListener listener = new AutocartListener();
    private static final AutocartActionListener actionListener = new AutocartActionListener();
    
    public void onEnable() {
        server = getServer();
        description = getDescription();
        MinecartManiaConfigurationParser.read(description.getName().replaceAll(" ", "") + "Configuration.xml", MinecartManiaCore.getDataDirectoryRelativePath(), new AutocartSettingParser());
        getServer().getPluginManager().registerEvents(listener, this);
        getServer().getPluginManager().registerEvents(actionListener, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, actionListener, Priority.Normal, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_MOVE, listener, Priority.Normal, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, listener, Priority.Normal, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_EXIT, listener, Priority.Normal, this);
        log.info(description.getName() + " version " + description.getVersion() + " is enabled!");
    }
    
    public void onDisable() {
        
    }
    
    public static boolean isAutocartOnlyForPlayers() {
        final Object o = MinecartManiaWorld.getConfigurationValue("AutocartForPlayersOnly");
        if ((o != null) && (o instanceof Boolean)) {
            final Boolean value = (Boolean) o;
            return value.booleanValue();
        }
        return false;
    }
    
    public static int getDefaultThrottle() {
        return (Integer) MinecartManiaWorld.getConfigurationValue("DefaultThrottle");
    }
}
