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
    /**
     * Gets a formatted message, that has placeholders replaced and colour codes <code>(&)</code> replaced.
     *
     * @param sender  the sender
     * @param message the original message
     * @return a formatted message
     */
    public abstract String getFormattedMessage(Object sender, String message);

    /**
     * Alternative to Bukkit's broadcast method with bungeecord support and console logging.
     *
     * @param string     the message to broadcast
     * @param permission the required permission node to view the message
     */
    public abstract void broadcast(String string, String permission);

    /**
     * Sends a message to Discord via a Discord bot or a {@link DiscordWebhook}.
     *
     * @param sender the sender
     * @param string the message to send
     */
    public abstract void discord(Object sender, String string);

    /**
     * Method to handle a received GuildMessageCreateEvent from Discord.
     *
     * @param event the event to handle
     */
    public abstract void handleDiscord(Object event);

    /**
     * Generates an embed with placeholders replaced
     *
     * @param sender the sender
     * @param string the message
     * @return an {@link dev.austech.betterstaffchat.common.discord.DiscordWebhook.EmbedObject}
     */
    protected abstract DiscordWebhook.EmbedObject generateEmbed(Object sender, String string);
}
