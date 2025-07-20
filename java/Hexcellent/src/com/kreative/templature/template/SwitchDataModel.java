package com.kreative.templature.template;

public interface SwitchDataModel {
	public abstract int getCaseCount();
	public abstract int getCaseIndex();
	public abstract Template.Instance getCase(int index);
	public abstract void addSwitchListener(SwitchListener listener);
	public abstract void removeSwitchListener(SwitchListener listener);
}
