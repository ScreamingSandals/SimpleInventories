/*
 * Copyright (C) 2026 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.simpleinventories.inventory;

public enum Column {
	LEFT{
		public int convert(int row_size) {
			return 0;
		}
	},
	MIDDLE{
		public int convert(int row_size) {
			return row_size / 2;
		}
	},
	RIGHT{
		public int convert(int row_size) {
			return row_size - 1;
		}
	},
	
	FIRST{
		public int convert(int row_size) {
			return 0;
		}
	},
	CENTER{
		public int convert(int row_size) {
			return row_size / 2;
		}
	},
	LAST{
		public int convert(int row_size) {
			return row_size - 1;
		}
	};
	
	public abstract int convert(int row_size);
}
