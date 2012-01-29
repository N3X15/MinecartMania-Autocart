package com.afforess.minecartmaniaautocart;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.afforess.minecartmaniacore.event.MinecartClickedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils;

public class AutocartActionListener extends MinecartManiaListener {
    
    @Override
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
