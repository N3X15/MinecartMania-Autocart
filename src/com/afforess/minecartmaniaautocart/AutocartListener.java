package com.afforess.minecartmaniaautocart;

import java.util.Calendar;

import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.event.MinecartClickedEvent;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils;
import com.afforess.minecartmaniacore.world.Item;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class AutocartListener implements Listener {
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleMove(final VehicleMoveEvent event) {
        if (event.getVehicle() instanceof Minecart) {
            final MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart) event.getVehicle());
            
            //Cooldown to prevent minecarts from running away
            if (Autocart.doCooldown(minecart))
                return;
            
            if ((minecart.getLocation().getBlock().getTypeId() == Item.RAILS.getId()) && (!minecart.isAtIntersection() || !minecart.hasPlayerPassenger())) {
                if (!MinecartManiaAutocart.isAutocartOnlyForPlayers() || minecart.hasPlayerPassenger()) {
                    final int l = MinecartManiaWorld.getBlockData(minecart.minecart.getWorld(), minecart.getX(), minecart.getY(), minecart.getZ());
                    
                    final int ai[][] = Autocart.trackMetadata[l];
                    double d8 = ai[1][0] - ai[0][0];
                    double d10 = ai[1][2] - ai[0][2];
                    final double d12 = (minecart.getMotionX() * d8) + (minecart.getMotionZ() * d10);
                    if (d12 < 0.0D) {
                        d8 = -d8;
                        d10 = -d10;
                    }
                    
                    d8 /= 4;
                    d10 /= 4;
                    
                    final double d13 = Math.sqrt((minecart.getMotionX() * minecart.getMotionX()) + (minecart.getMotionZ() * minecart.getMotionZ()));
                    
                    minecart.setMotionX(d13 > 0.0D ? d8 : 0.0D);
                    minecart.setMotionZ(d13 > 0.0D ? d10 : 0.0D);
                    
                    Autocart.doThrottle(minecart);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleEnter(final VehicleEnterEvent event) {
        if (event.isCancelled())
            return;
        if ((event.getVehicle() instanceof Minecart) && (event.getVehicle().getPassenger() == null)) {
            final MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart) event.getVehicle());
            minecart.setDataValue("Cooldown", null);
            if (event.getEntered() instanceof Player) {
                ((Player) event.getEntered()).sendMessage(LocaleParser.getTextKey("AutoCartEnteredMessage"));
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleExit(final VehicleExitEvent event) {
        if (event.isCancelled())
            return;
        if (event.getVehicle() instanceof Minecart) {
            final MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart) event.getVehicle());
            if (!minecart.isMoving()) {
                minecart.setDataValue("Cooldown", new Long(Calendar.getInstance().getTimeInMillis() + 3000));
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMinecartClickedEvent(final MinecartClickedEvent event) {
        if (event.isActionTaken())
            return;
        final MinecartManiaMinecart minecart = event.getMinecart();
        if (!minecart.isMoving()) {
            final DirectionUtils.CompassDirection facingDir = DirectionUtils.getDirectionFromMinecartRotation((minecart.minecart.getPassenger().getLocation().getYaw() - 90.0F) % 360.0F);
            minecart.minecart.setVelocity(Autocart.getMotionFromDirection(facingDir));
        } else {
            minecart.stopCart();
        }
    }
}
