/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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

package cc.woverflow.hysentials.websocket;

import cc.woverflow.hysentials.util.DuoVariable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Request {
    List<DuoVariable<String, Object>> data = new ArrayList<>();
    public Request(Object... data) {
        for (int i = 0; i < data.length; i+=2) {
            if (data[i] instanceof String && data[i+1] != null) {
                this.data.add(new DuoVariable<>((String) data[i], data[i+1]));
            } else {
                throw new IllegalArgumentException("Invalid data provided");
            }
        }
    }
    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        for (DuoVariable<String, Object> duoVariable : data) {
            json.put(duoVariable.getFirst(), duoVariable.getSecond());
        }
        return json.toString();
    }
}
