package com.kreative.templature.template;

import java.util.List;

public interface ComponentFactory<C> {
	public abstract C createBooleanComponent(BooleanDataModel m);
	public abstract C createColorComponent(ColorDataModel m);
	public abstract C createDateTimeComponent(DateTimeDataModel m);
	public abstract C createEnumComponent(EnumDataModel m);
	public abstract C createFormComponent(List<String> labels, List<? extends C> components, int count);
	public abstract C createHexAreaComponent(HexDataModel m);
	public abstract C createListComponent(ListDataModel m);
	public abstract C createPoint2DComponent(Point2DDataModel m);
	public abstract C createPoint3DComponent(Point3DDataModel m);
	public abstract C createRectangleComponent(RectangleDataModel m);
	public abstract C createSwitchComponent(SwitchDataModel m);
	public abstract C createTextAreaComponent(StringDataModel m);
	public abstract C createTextFieldComponent(StringDataModel m);
	public abstract C createTextFieldComponent(StringDataModel m, int columns);
	public abstract C createHeadingComponent(String s, int level);
	public abstract C createParagraphComponent(String s);
	public abstract C createSeparatorComponent();
}
