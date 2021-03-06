package com.simon.wa.domain.reports;

import com.simon.wa.domain.reports.columns.ColumnMetadata;

public class ReportRow {

	
	private final ReportKey keys;
	private final Object[] values;
	private int keyIndex;
	private int valueIndex;
	
	public ReportRow(ColumnMetadata meta) {
		int keys = 0, values = 0;
		for (ReportColumn def : meta.getColumns()) {
			if (def.isKey()) keys++;
			else values++;
		}
		this.keys = new ReportKey(new Object[keys]);
		this.values = new Object[values];
	}
	
	public void addValue(Object outputValue, boolean key) {
		if (key) {
			this.keys.addValue(keyIndex++,outputValue);
		}
		else {
			this.values[valueIndex++] = outputValue;
		}
	}
	
	public Object[] output() {
		Object[] out = new Object[this.keys.getValues().length + this.values.length];
		int index = 0;
		for (Object k : this.keys.getValues()) {
			out[index++] = k;
		}
		for (Object v : this.values) {
			out[index++] = v;
		}
		return out;
	}

	public ReportKey getKeys() {
		return keys;
	}

	public Object[] getValues() {
		return values;
	}

	public int getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}

	public int getValueIndex() {
		return valueIndex;
	}

	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
	}



	
	
}
