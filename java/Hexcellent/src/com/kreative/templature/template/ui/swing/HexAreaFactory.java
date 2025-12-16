package com.kreative.templature.template.ui.swing;

import java.awt.Component;
import com.kreative.templature.template.HexDataModel;

public abstract class HexAreaFactory {
	public abstract Component createHexAreaComponent(HexDataModel m);
	
	private static final String CLASSNAME = "com.kreative.templature.template.ui.ext.jhex.JHexEditorFactory";
	public static HexAreaFactory newInstance() {
		try { return HexAreaFactory.class.cast(Class.forName(CLASSNAME).newInstance()); }
		catch (Throwable t) { return new JHexAreaFactory(); }
	}
}
