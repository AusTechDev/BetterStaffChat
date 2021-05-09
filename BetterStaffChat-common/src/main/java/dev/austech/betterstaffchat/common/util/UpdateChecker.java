/*
 * BetterStaffChat - UpdateChecker.java
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

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

@UtilityClass
public class UpdateChecker {
    @Getter public int currentConfigVersion = 3;

    public boolean needsUpdate(Object plugin, String currentVersion) {
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=91991").openStream()) {
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                return !currentVersion.equals(scanner.next());
            }
        } catch (IOException exception) {
            LogUtil.logPrefix(plugin, "&Failed to check for updates...");
        }
        return false;
    }
}