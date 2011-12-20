package com.afforess.minecartmaniaautocart;

import com.afforess.minecartmaniacore.event.MinecartClickedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils;

public class AutocartActionListener extends MinecartManiaListener {
    
    @Override
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
