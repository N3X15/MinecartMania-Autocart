package com.afforess.minecartmaniaautocart;

import java.util.Calendar;

import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils;

public class Autocart {
    
    public static final int trackMetadata[][][] = { { { 0, 0, -1 }, { 0, 0, 1 } }, { { -1, 0, 0 }, { 1, 0, 0 } }, { { -1, -1, 0 }, { 1, 0, 0 } }, { { -1, 0, 0 }, { 1, -1, 0 } }, { { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1, 0, 0 } }, { { 0, 0, 1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { 1, 0, 0 } } };
    
    public static Vector getMotionFromDirection(final DirectionUtils.CompassDirection direction) {
        final Vector vector = new Vector(0D, 0D, 0D);
        if (direction.equals(DirectionUtils.CompassDirection.NORTH)) {
            vector.setX(-0.5D);
        } else if (direction.equals(DirectionUtils.CompassDirection.EAST)) {
            vector.setZ(-0.5D);
        } else if (direction.equals(DirectionUtils.CompassDirection.SOUTH)) {
            vector.setX(0.5D);
        } else if (direction.equals(DirectionUtils.CompassDirection.WEST)) {
            vector.setZ(0.5D);
        }
        return vector;
    }
    
    public static boolean doCooldown(final MinecartManiaMinecart minecart) {
        final Object data = minecart.getDataValue("Cooldown");
        if (data != null) {
            final long cooldown = ((Long) data).longValue();
            if (Calendar.getInstance().getTimeInMillis() > cooldown) {
                minecart.setDataValue("Cooldown", null);
            } else {
                minecart.stopCart();
                return true;
            }
        }
        return false;
    }
    
    public static void doThrottle(final MinecartManiaMinecart minecart) {
        Object value = minecart.getDataValue("throttle");
        if (value == null) {
            value = new Double(MinecartManiaAutocart.getDefaultThrottle());
        }
        final double throttle = ((Double) (value)).doubleValue();
        minecart.setMotionX((minecart.getMotionX() * throttle) / 100);
        minecart.setMotionY((minecart.getMotionY() * throttle) / 100);
        minecart.setMotionZ((minecart.getMotionZ() * throttle) / 100);
    }
}
