package com.kreative.templature.template;

import java.util.Date;

public interface DateTimeDataModel {
	public abstract Date getDateValue();
	public abstract Date getDateMinValue();
	public abstract Date getDateMaxValue();
	public abstract void setDateValue(Date value);
}
