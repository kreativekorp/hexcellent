package com.kreative.templature.test;

import java.math.BigInteger;
import com.kreative.templature.template.PackedBlock;
import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.PackedBooleanItem;
import com.kreative.templature.template.item.PackedIntegerItem;

public class PackedBlockTest extends AbstractUITest {
	public String getTitle() {
		return "Packed Block Test";
	}
	
	public TemplateItem createItem() {
		PackedBlock b = new PackedBlock(0, 0, "", "", "", false);
		b.add(new PackedIntegerItem(0, "", "A nibble", 4, false, 16));
		b.add(new PackedIntegerItem(0, "", "Another nibble", 4, false, 16));
		b.add(new PackedBooleanItem(0, "", "Bit 7", 1, BigInteger.ZERO, BigInteger.ONE, true));
		b.add(new PackedBooleanItem(0, "", "Bit 6", 1, BigInteger.ZERO, BigInteger.ONE, true));
		b.add(new PackedBooleanItem(0, "", "Bit 5", 1, BigInteger.ZERO, BigInteger.ONE, true));
		b.add(new PackedBooleanItem(0, "", "Bit 4", 1, BigInteger.ZERO, BigInteger.ONE, true));
		b.add(new PackedBooleanItem(0, "", "Bit 3", 1, BigInteger.ZERO, BigInteger.ONE, true));
		b.add(new PackedBooleanItem(0, "", "Bit 2", 1, BigInteger.ZERO, BigInteger.ONE, true));
		b.add(new PackedBooleanItem(0, "", "Bit 1", 1, BigInteger.ZERO, BigInteger.ONE, true));
		b.add(new PackedBooleanItem(0, "", "Bit 0", 1, BigInteger.ZERO, BigInteger.ONE, true));
		return b;
	}
	
	public static void main(String[] args) {
		new PackedBlockTest().main();
	}
}
