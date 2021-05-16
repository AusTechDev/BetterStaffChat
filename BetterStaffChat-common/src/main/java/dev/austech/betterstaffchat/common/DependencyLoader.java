/*
 * BetterStaffChat - DependencyLoader.java
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

package dev.austech.betterstaffchat.common;

import dev.austech.betterstaffchat.common.dependency.LibraryLoader;

import java.lang.reflect.InvocationTargetException;

public class DependencyLoader {
    public static void init(Object plugin) {
        try {
            if ((boolean) plugin.getClass().getDeclaredMethod("isDiscordEnabled").invoke(plugin)) {
                LibraryLoader.load("org.ow2.asm", "asm", "7.1", plugin);
                LibraryLoader.load("org.ow2.asm", "asm-commons", "7.1", plugin);
                LibraryLoader.load("me.lucko", "jar-relocator", "1.4", plugin);
                LibraryLoader.load("org.jetbrains", "annotations", "16.0.1", plugin);
                LibraryLoader.load("com.squareup.okhttp3", "okhttp", "3.13.0", plugin);
                LibraryLoader.load("com.squareup.okio", "okio", "1.17.2", plugin);
                LibraryLoader.load("org.apache.commons", "commons-collections4", "4.1", plugin);
                LibraryLoader.load("com.fasterxml.jackson.core", "jackson-databind", "2.10.1", plugin);
                LibraryLoader.load("com.fasterxml.jackson.core", "jackson-core", "2.10.1", plugin);
                LibraryLoader.load("com.fasterxml.jackson.core", "jackson-annotations", "2.10.1", plugin);
                LibraryLoader.load("com.fasterxml.jackson.core", "jackson-annotations", "2.10.1", plugin);
                LibraryLoader.load("com.fasterxml.jackson.core", "jackson-annotations", "2.10.1", plugin);
                LibraryLoader.load("org.slf4j", "slf4j-api", "1.7.25", plugin);
                LibraryLoader.load("net.sf.trove4j", "trove4j", "3.0.3", plugin);
                LibraryLoader.load("com.neovisionaries", "nv-websocket-client", "2.14", plugin);
                LibraryLoader.loadRelocate("net.dv8tion", "JDA", "4.2.1_262", "https://m2.dv8tion.net/releases/", plugin);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
