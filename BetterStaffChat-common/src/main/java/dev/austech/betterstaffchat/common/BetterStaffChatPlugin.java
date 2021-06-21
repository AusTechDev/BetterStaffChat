/*
 * BetterStaffChat - BetterStaffChatPlugin.java
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

import java.io.File;

public interface BetterStaffChatPlugin {
    /**
     * Used to get a plugin's <code>plugins/{name}</code> folder.
     *
     * @return the plugin's data folder
     */
    File getPluginDataFolder();

    /**
     * Logs a message with a prefix.
     *
     * @param string the message to log
     */
    void logPrefix(String string);

    /**
     * Logs a message with a prefix if <code>debug</code> is set to <code>true</code> in the console
     *
     * @param string the message to log
     */
    void logPrefixDebug(String string);

    /**
     * Logs a message.
     *
     * @param string the message to log
     */
    void log(String string);

}
