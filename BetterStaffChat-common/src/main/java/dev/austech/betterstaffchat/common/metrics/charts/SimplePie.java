/*
 * BetterStaffChat - SimplePie.java
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

package dev.austech.betterstaffchat.common.metrics.charts;

import dev.austech.betterstaffchat.common.metrics.json.JsonObjectBuilder;

import java.util.concurrent.Callable;

public class SimplePie extends CustomChart {

    private final Callable<String> callable;

    /**
     * Class constructor.
     *
     * @param chartId The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */
    public SimplePie(String chartId, Callable<String> callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        String value = callable.call();
        if (value == null || value.isEmpty()) {
            // Null = skip the chart
            return null;
        }
        return new JsonObjectBuilder()
                .appendField("value", value)
                .build();
    }
}