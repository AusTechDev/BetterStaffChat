/*
 * BetterStaffChat - LogUtil.java
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

package dev.austech.betterstaffchat.common.util;

import dev.austech.betterstaffchat.common.BetterStaffChatPlugin;
import lombok.experimental.UtilityClass;

@UtilityClass @Deprecated
public class LogUtil {
    public void log(BetterStaffChatPlugin plugin, String string) {
        plugin.log(string);
    }

    public static void logPrefix(BetterStaffChatPlugin plugin, String string) {
        plugin.logPrefix(string);
    }

    public void logPrefixDebug(BetterStaffChatPlugin plugin, String string) {
        plugin.logPrefixDebug(string);
    }
}
