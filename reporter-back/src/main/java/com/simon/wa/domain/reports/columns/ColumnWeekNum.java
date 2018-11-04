package com.simon.wa.domain.reports.columns;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.reports.ReportColumn;

public class ColumnWeekNum extends ColumnDefinition implements ReportColumn {

	public ColumnWeekNum() {
		super();
	}

	public ColumnWeekNum(String colName, boolean key, ColOutput outputType, String inputFieldName) {
		super(colName, key, outputType);
		this.setColumnType("weeknum");
		this.addInput("field", inputFieldName);
	}

	@Override
	public Object generateValue(ApiObject input) {

		String inputValue = null;
		try {
			inputValue = input.getValue((String) this.getInputs().get("field"), String.class);
		} catch (NullPointerException e) {
			System.out.println("Couldn't find field " + this.getInputs().get("inputFieldName"));
			return "";
		}

		if (inputValue != null) {
			LocalDate ld = LocalDate.parse(inputValue);
			WeekFields weekFields = WeekFields.of(Locale.getDefault());
			int weekNumber = ld.get(weekFields.weekOfWeekBasedYear());
			return weekNumber;
		} else
			return "";

	}

}