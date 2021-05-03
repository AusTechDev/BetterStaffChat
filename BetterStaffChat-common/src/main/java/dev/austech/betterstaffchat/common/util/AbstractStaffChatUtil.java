/*
 * BetterStaffChat - AbstractStaffChatUtil.java
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

import dev.austech.betterstaffchat.common.discord.DiscordWebhook;

public abstract class AbstractStaffChatUtil {
    public abstract String getFormattedMessage(Object sender, String message);

    public abstract void broadcast(String string, String permission);

    public abstract void discord(Object sender, String string);

    public abstract void handleDiscord(Object event);

    protected abstract DiscordWebhook.EmbedObject generateEmbed(Object sender, String string);
}
