/*
 * BetterStaffChat - ReflectionUtil.java
 * Copyright (C) 2021 AusTech Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.austech.betterstaffchat.spigot.reflection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil {
    protected static SimpleCommandMap getCommandMap() {
        try {
            return (SimpleCommandMap) Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().substring(23) + ".CraftServer").getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void registerCmd(Command command) {
        CommandMap commandMap = getCommandMap();
        if (commandMap != null)
            commandMap.register(command.getLabel(), command);
        if (!command.isRegistered()) throw new IllegalStateException("CommandMap is null");
    }
}