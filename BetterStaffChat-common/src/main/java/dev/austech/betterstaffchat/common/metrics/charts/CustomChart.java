/*
 * BetterStaffChat - CustomChart.java
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

import java.util.function.BiConsumer;

public abstract class CustomChart {

    private final String chartId;

    protected CustomChart(String chartId) {
        if (chartId == null) {
            throw new IllegalArgumentException("chartId must not be null");
        }
        this.chartId = chartId;
    }

    public JsonObjectBuilder.JsonObject getRequestJsonObject(BiConsumer<String, Throwable> errorLogger, boolean logErrors) {
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.appendField("chartId", chartId);
        try {
            JsonObjectBuilder.JsonObject data = getChartData();
            if (data == null) {
                // If the data is null we don't send the chart.
                return null;
            }
            builder.appendField("data", data);
        } catch (Throwable t) {
            if (logErrors) {

                errorLogger.accept("Failed to get data for custom chart with id " + chartId, t);
            }
            return null;
        }
        return builder.build();
    }

    protected abstract JsonObjectBuilder.JsonObject getChartData() throws Exception;

}
