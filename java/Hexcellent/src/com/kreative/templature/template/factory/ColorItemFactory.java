package com.kreative.templature.template.factory;

import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.ColorItem;

public class ColorItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$RGB4|TI, $4BGR|TI|LE,
			$BGR4|TI, $4RGB|TI|LE,
			$RGB5|TI, $5BGR|TI|LE,
			$BGR5|TI, $5RGB|TI|LE,
			$RGB6|TI, $6BGR|TI|LE,
			$BGR6|TI, $6RGB|TI|LE,
			$RGBC|TI, $CBGR|TI|LE,
			$BGRC|TI, $CRGB|TI|LE,
			$XRGB|TI, $BGRX|TI|LE,
			$XBGR|TI, $RGBX|TI|LE,
			$ARGB|TI, $BGRA|TI|LE,
			$ABGR|TI, $RGBA|TI|LE,
			$RGBc|TI, $cBGR|TI|LE,
			$BGRc|TI, $cRGB|TI|LE,
			$R444|TI, $444R|TI|LE,
			$R555|TI, $555R|TI|LE,
			$R565|TI, $565R|TI|LE,
			$R888|TI, $888R|TI|LE,
			$RXXX|TI, $XXXR|TI|LE,
			$44R4|TI, $4R44|TI|LE,
			$55R5|TI, $5R55|TI|LE,
			$56R5|TI, $5R65|TI|LE,
			$88R8|TI, $8R88|TI|LE,
			$XXRX|TI, $XRXX|TI|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; boolean le; int width; int rs, rw, gs, gw, bs, bw, as, aw;
		switch (mtc) {
			case $RGB4: typeString = "rgb444be";    le = false; width = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 8;  gs = 4;  bs = 0;  break;
			case $4BGR: typeString = "rgb444le";    le = true;  width = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 8;  gs = 4;  bs = 0;  break;
			case $BGR4: typeString = "bgr444be";    le = false; width = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 0;  gs = 4;  bs = 8;  break;
			case $4RGB: typeString = "bgr444le";    le = true;  width = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 0;  gs = 4;  bs = 8;  break;
			case $RGB5: typeString = "rgb555be";    le = false; width = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 10; gs = 5;  bs = 0;  break;
			case $5BGR: typeString = "rgb555le";    le = true;  width = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 10; gs = 5;  bs = 0;  break;
			case $BGR5: typeString = "bgr555be";    le = false; width = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 10; break;
			case $5RGB: typeString = "bgr555le";    le = true;  width = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 10; break;
			case $RGB6: typeString = "rgb565be";    le = false; width = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 11; gs = 5;  bs = 0;  break;
			case $6BGR: typeString = "rgb565le";    le = true;  width = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 11; gs = 5;  bs = 0;  break;
			case $BGR6: typeString = "bgr565be";    le = false; width = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 11; break;
			case $6RGB: typeString = "bgr565le";    le = true;  width = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 11; break;
			case $RGBC: typeString = "rgb888be";    le = false; width = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 16; gs = 8;  bs = 0;  break;
			case $CBGR: typeString = "rgb888le";    le = true;  width = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 16; gs = 8;  bs = 0;  break;
			case $BGRC: typeString = "bgr888be";    le = false; width = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 0;  gs = 8;  bs = 16; break;
			case $CRGB: typeString = "bgr888le";    le = true;  width = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 0;  gs = 8;  bs = 16; break;
			case $XRGB: typeString = "xrgb8888be";  le = false; width = 32; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 16; gs = 8;  bs = 0;  break;
			case $BGRX: typeString = "xrgb8888le";  le = true;  width = 32; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 16; gs = 8;  bs = 0;  break;
			case $XBGR: typeString = "xbgr8888be";  le = false; width = 32; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 0;  gs = 8;  bs = 16; break;
			case $RGBX: typeString = "xbgr8888le";  le = true;  width = 32; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 0;  gs = 8;  bs = 16; break;
			case $ARGB: typeString = "argb8888be";  le = false; width = 32; aw = 8; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 16; gs = 8;  bs = 0;  break;
			case $BGRA: typeString = "argb8888le";  le = true;  width = 32; aw = 8; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 16; gs = 8;  bs = 0;  break;
			case $ABGR: typeString = "abgr8888be";  le = false; width = 32; aw = 8; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 0;  gs = 8;  bs = 16; break;
			case $RGBA: typeString = "abgr8888le";  le = true;  width = 32; aw = 8; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 0;  gs = 8;  bs = 16; break;
			case $RGBc: typeString = "rgb161616be"; le = false; width = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 32; gs = 16; bs = 0;  break;
			case $cBGR: typeString = "rgb161616le"; le = true;  width = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 32; gs = 16; bs = 0;  break;
			case $BGRc: typeString = "bgr161616be"; le = false; width = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 0;  gs = 16; bs = 32; break;
			case $cRGB: typeString = "bgr161616le"; le = true;  width = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 0;  gs = 16; bs = 32; break;
			case $R444: typeString = "rgb444be";    le = false; width = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 8;  gs = 4;  bs = 0;  break;
			case $444R: typeString = "rgb444le";    le = true;  width = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 8;  gs = 4;  bs = 0;  break;
			case $R555: typeString = "rgb555be";    le = false; width = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 10; gs = 5;  bs = 0;  break;
			case $555R: typeString = "rgb555le";    le = true;  width = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 10; gs = 5;  bs = 0;  break;
			case $R565: typeString = "rgb565be";    le = false; width = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 11; gs = 5;  bs = 0;  break;
			case $565R: typeString = "rgb565le";    le = true;  width = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 11; gs = 5;  bs = 0;  break;
			case $R888: typeString = "rgb888be";    le = false; width = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 16; gs = 8;  bs = 0;  break;
			case $888R: typeString = "rgb888le";    le = true;  width = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 16; gs = 8;  bs = 0;  break;
			case $RXXX: typeString = "rgb161616be"; le = false; width = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 32; gs = 16; bs = 0;  break;
			case $XXXR: typeString = "rgb161616le"; le = true;  width = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 32; gs = 16; bs = 0;  break;
			case $44R4: typeString = "bgr444be";    le = false; width = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 0;  gs = 4;  bs = 8;  break;
			case $4R44: typeString = "bgr444le";    le = true;  width = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 0;  gs = 4;  bs = 8;  break;
			case $55R5: typeString = "bgr555be";    le = false; width = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 10; break;
			case $5R55: typeString = "bgr555le";    le = true;  width = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 10; break;
			case $56R5: typeString = "bgr565be";    le = false; width = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 11; break;
			case $5R65: typeString = "bgr565le";    le = true;  width = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 11; break;
			case $88R8: typeString = "bgr888be";    le = false; width = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 0;  gs = 8;  bs = 16; break;
			case $8R88: typeString = "bgr888le";    le = true;  width = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 0;  gs = 8;  bs = 16; break;
			case $XXRX: typeString = "bgr161616be"; le = false; width = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 0;  gs = 16; bs = 32; break;
			case $XRXX: typeString = "bgr161616le"; le = true;  width = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 0;  gs = 16; bs = 32; break;
			default: return null;
		}
		return new ColorItem(it.type, typeString, it.label, le, width, rs, rw, gs, gw, bs, bw, as, aw);
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		return null;
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"rgb444&", "bgr444&", "rgb555&", "bgr555&",
			"rgb565&", "bgr565&", "rgb888&", "bgr888&",
			"xrgb8888&", "xbgr8888&", "rgbx8888&", "bgrx8888&",
			"argb8888&", "abgr8888&", "rgba8888&", "bgra8888&",
			"rgb161616&", "bgr161616&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return null;
	}
	
	private static final List<String> MTS = Arrays.asList(
		"rgb444&", "bgr444&", "rgb555&", "bgr555&",
		"rgb565&", "bgr565&", "rgb888&", "bgr888&",
		"xrgb8888&", "xbgr8888&", "rgbx8888&", "bgrx8888&",
		"argb8888&", "abgr8888&", "rgba8888&", "bgra8888&",
		"rgb161616&", "bgr161616&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc; int rs, rw, gs, gw, bs, bw, as, aw;
		switch (MTS.indexOf(mts)) {
			case  0: tc = (le ? $4BGR : $RGB4); mv = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 8;  gs = 4;  bs = 0;  break;
			case  1: tc = (le ? $4RGB : $BGR4); mv = 12; aw = 0; rw = 4;  gw = 4;  bw = 4;  as = 0;  rs = 0;  gs = 4;  bs = 8;  break;
			case  2: tc = (le ? $5BGR : $RGB5); mv = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 10; gs = 5;  bs = 0;  break;
			case  3: tc = (le ? $5RGB : $BGR5); mv = 15; aw = 0; rw = 5;  gw = 5;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 10; break;
			case  4: tc = (le ? $6BGR : $RGB6); mv = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 11; gs = 5;  bs = 0;  break;
			case  5: tc = (le ? $6RGB : $BGR6); mv = 16; aw = 0; rw = 5;  gw = 6;  bw = 5;  as = 0;  rs = 0;  gs = 5;  bs = 11; break;
			case  6: tc = (le ? $CBGR : $RGBC); mv = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 16; gs = 8;  bs = 0;  break;
			case  7: tc = (le ? $CRGB : $BGRC); mv = 24; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 0;  gs = 8;  bs = 16; break;
			case  8: tc = (le ? $BGRX : $XRGB); mv = 32; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 16; gs = 8;  bs = 0;  break;
			case  9: tc = (le ? $RGBX : $XBGR); mv = 32; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 0;  gs = 8;  bs = 16; break;
			case 10: tc = (le ? $XBGR : $RGBX); mv = 32; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 24; gs = 16; bs = 8;  break;
			case 11: tc = (le ? $XRGB : $BGRX); mv = 32; aw = 0; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 8;  gs = 16; bs = 24; break;
			case 12: tc = (le ? $BGRA : $ARGB); mv = 32; aw = 8; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 16; gs = 8;  bs = 0;  break;
			case 13: tc = (le ? $RGBA : $ABGR); mv = 32; aw = 8; rw = 8;  gw = 8;  bw = 8;  as = 24; rs = 0;  gs = 8;  bs = 16; break;
			case 14: tc = (le ? $ABGR : $RGBA); mv = 32; aw = 8; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 24; gs = 16; bs = 8;  break;
			case 15: tc = (le ? $ARGB : $BGRA); mv = 32; aw = 8; rw = 8;  gw = 8;  bw = 8;  as = 0;  rs = 8;  gs = 16; bs = 24; break;
			case 16: tc = (le ? $cBGR : $RGBc); mv = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 32; gs = 16; bs = 0;  break;
			case 17: tc = (le ? $cRGB : $BGRc); mv = 48; aw = 0; rw = 16; gw = 16; bw = 16; as = 0;  rs = 0;  gs = 16; bs = 32; break;
			default: return null;
		}
		return new ColorItem(tc, ts, name, le, mv, rs, rw, gs, gw, bs, bw, as, aw);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		return null;
	}
}
