package com.kreative.templature.template.factory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.kreative.templature.template.BinaryTemplateItemFactory;
import com.kreative.templature.template.TextTemplateItemFactory;

public class BOM {
	public static final List<BinaryTemplateItemFactory> BINARY = Collections.unmodifiableList(
		Arrays.<BinaryTemplateItemFactory>asList(
			new AlignmentItemFactory(),
			new Base128ItemFactory(),
			new BooleanItemFactory(),
			new CharacterItemFactory(),
			new ColorItemFactory(),
			new ConditionalBlockFactory(),
			new ConstantItemFactory(),
			new EnumItemFactory(),
			new FixedItemFactory(),
			new FloatItemFactory(),
			new HexItemFactory(),
			new IntegerItemFactory(),
			new ListBlockFactory(),
			new MetaItemFactory(),
			new PointItemFactory(),
			new StaticItemFactory(),
			new StringItemFactory(),
			new SwitchBlockFactory()
		)
	);
	
	public static final List<TextTemplateItemFactory> TEXT = Collections.unmodifiableList(
		Arrays.<TextTemplateItemFactory>asList(
			new AlignmentItemFactory(),
			new Base128ItemFactory(),
			new BooleanItemFactory(),
			new CharacterItemFactory(),
			new ColorItemFactory(),
			new ConditionalBlockFactory(),
			new ConstantItemFactory(),
			new EnumItemFactory(),
			new FixedItemFactory(),
			new FloatItemFactory(),
			new HexItemFactory(),
			new IntegerItemFactory(),
			new ListBlockFactory(),
			new MetaItemFactory(),
			new PointItemFactory(),
			new StaticItemFactory(),
			new StringItemFactory(),
			new SwitchBlockFactory()
		)
	);
}
