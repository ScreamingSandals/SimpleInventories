package org.screamingsandals.simpleguiformat.inventory;

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
