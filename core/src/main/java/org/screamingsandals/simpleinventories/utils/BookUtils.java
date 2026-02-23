/*
 * Copyright (C) 2026 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.simpleinventories.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class BookUtils {

    public static void openBook(Player player) {
        try {
            if (pc == null || ppocp == null || pds == null)
                throw new NullPointerException("Craftbukkit classes not found!");
            int number = Integer.parseInt(version.split("_")[0].substring(1)) * 100
                    + Integer.parseInt(version.split("_")[1]);
            if (number < 113) {
                pc.getMethod("sendPacket", p).invoke(getConnection(player),
                        ppocp.getConstructor(String.class, pds).newInstance("MC|BOpen",
                                pds.getConstructor(ByteBuf.class).newInstance(
                                        Unpooled.buffer(256).setByte(0, (byte) 0).writerIndex(1))));
            } else if (number == 113) {
                Class<?> mk = getNMSClass("MinecraftKey", "net.minecraft.resources");
                pc.getMethod("sendPacket", p).invoke(getConnection(player),
                        ppocp.getConstructor(mk, pds).newInstance(
                                mk.getMethod("a", String.class).invoke(mk, "minecraft:book_open"),
                                pds.getConstructor(ByteBuf.class).newInstance(
                                        Unpooled.buffer(256).setByte(0, (byte) 0).writerIndex(1))));
            } else if (number > 113) {
                pc.getMethod("sendPacket", p).invoke(getConnection(player), ppoob.getConstructor(eh)
                        .newInstance(eh.getDeclaredMethod("valueOf", String.class).invoke(eh, "MAIN_HAND")));
            }

        } catch (Throwable ignored) {
        }
    }

    private static Class<?> getNMSClass(String nmsClassString, String newPackage) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + nmsClassString);
        } catch (Throwable ignore) {
            try {
                return Class.forName(newPackage + "." + nmsClassString);
            } catch (Throwable ignore2) {
            }
        }
        return null;
    }

    private static Object getConnection(Player player) throws SecurityException, NoSuchMethodException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
        try {
            return nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
        } catch (Throwable ignored) {
            return nmsPlayer.getClass().getField("connection").get(nmsPlayer);
        }
    }

    private static final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
            .split(",")[3];
    private static final Class<?> ppocp = getNMSClass("PacketPlayOutCustomPayload", "net.minecraft.network.protocol.game");
    private static final Class<?> pc = getNMSClass("PlayerConnection", "net.minecraft.server.network");
    private static final Class<?> pds = getNMSClass("PacketDataSerializer", "net.minecraft.network");
    private static final Class<?> ppoob = getNMSClass("PacketPlayOutOpenBook", "net.minecraft.network.protocol.game");
    private static final Class<?> eh = getNMSClass("EnumHand", "net.minecraft.world");
    private static final Class<?> p = getNMSClass("Packet", "net.minecraft.network.protocol");
}
