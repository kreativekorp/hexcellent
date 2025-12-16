package com.kreative.templature.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTemplateFactory {
	private static final int $PCKB = TemplateConstants.$PCKB;
	private static final int $BKCP = TemplateConstants.$BKCP;
	private static final int $PCKE = TemplateConstants.$PCKE;
	private static final int $EKCP = TemplateConstants.$EKCP;
	
	private static final String ERROR_TYPE_DUPLICATE = TemplateConstants.ERROR_TYPE_DUPLICATE;
	private static final String ERROR_TYPE_INVALID = TemplateConstants.ERROR_TYPE_INVALID;
	private static final String ERROR_PACKED_NESTED = TemplateConstants.ERROR_PACKED_NESTED;
	private static final String ERROR_PACKED_MIXED = TemplateConstants.ERROR_PACKED_MIXED;
	private static final String ERROR_PACKED_N_A = TemplateConstants.ERROR_PACKED_N_A;
	private static final String ERROR_PACKED_ALIGN = TemplateConstants.ERROR_PACKED_ALIGN;
	private static final String ERROR_SYNTAX_TYPE = TemplateConstants.ERROR_SYNTAX_TYPE;
	private static final String ERROR_SYNTAX_TYPE_EOF = TemplateConstants.ERROR_SYNTAX_TYPE_EOF;
	
	private static final Pattern NAME = Pattern.compile("^(\\p{L}[\\p{L}\\p{M}\\p{N}]*)$");
	private static final Pattern NAME_WITH_NUMBER = Pattern.compile("^(\\P{N}*)(\\p{N}+)(\\P{N}*)$");
	
	// All keys are expanded and normalized identifiers, not patterns.
	private final HashMap<String,TypeProperties> typeProperties;
	private final HashMap<String,TextTemplateItemFactory> templateTypes;
	private final HashMap<String,TextTemplateItemFactory> packedTypes;
	
	public TextTemplateFactory() {
		typeProperties = new HashMap<String,TypeProperties>();
		typeProperties.put("packed", new TypeProperties("packed&", null));
		typeProperties.put("packedbe", new TypeProperties("packed&", false));
		typeProperties.put("packedle", new TypeProperties("packed&", true));
		templateTypes = new HashMap<String,TextTemplateItemFactory>();
		packedTypes = new HashMap<String,TextTemplateItemFactory>();
	}
	
	public void registerAll(Iterable<? extends TextTemplateItemFactory> i) {
		for (TextTemplateItemFactory f : i) registerItemFactory(f);
	}
	
	public void registerDefaults() {
		try {
			for (Object o : (Iterable<?>)(
				Class.forName("com.kreative.templature.template.factory.BOM")
				.getField("TEXT")
				.get(null)
			)) registerItemFactory((TextTemplateItemFactory)o);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void registerExtensions() {
		try {
			for (Object o : (Iterable<?>)(
				Class.forName("java.util.ServiceLoader")
				.getMethod("load", Class.class)
				.invoke(null, TextTemplateItemFactory.class)
			)) registerItemFactory((TextTemplateItemFactory)o);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void registerItemFactory(TextTemplateItemFactory factory) {
		HashMap<String,TypeProperties> ti = new HashMap<String,TypeProperties>();
		HashMap<String,TypeProperties> pi = new HashMap<String,TypeProperties>();
		String[] tts = factory.getTemplateTypeStrings();
		String[] pts = factory.getPackedTypeStrings();
		if (tts != null) for (String ts : tts) putTypeProperties1(typeProperties, ti, ts);
		if (pts != null) for (String ts : pts) putTypeProperties1(typeProperties, pi, ts);
		typeProperties.putAll(ti);
		typeProperties.putAll(pi);
		for (String ts : ti.keySet()) templateTypes.put(ts, factory);
		for (String ts : pi.keySet()) packedTypes.put(ts, factory);
	}
	
	public Template createTemplate(TextTemplateTokenStream tokens) {
		return createTemplate(new TextTemplateInputState(), tokens);
	}
	
	public Template createTemplate(TextTemplateInputState state, TextTemplateTokenStream tokens) {
		Template template = new Template();
		while (tokens.hasNextType()) {
			TextTemplateTokenStream.Token token = tokens.next();
			template.add(createTemplateItem(state, tokens, token));
		}
		return template;
	}
	
	public TemplateItem createTemplateItem(TextTemplateInputState state, TextTemplateTokenStream tokens, TextTemplateTokenStream.Token token) {
		if (token != null && token.type == TextTemplateTokenStream.TokenType.KEYWORD) {
			String typeString = token.value.toString().toLowerCase();
			// packed block
			if (typeString.equals("packed")) return createPackedBlock(state, tokens, token, state.getLittleEndian());
			if (typeString.equals("packedbe")) return createPackedBlock(state, tokens, token, false);
			if (typeString.equals("packedle")) return createPackedBlock(state, tokens, token, true);
			// fixed string
			TextTemplateItemFactory factory = templateTypes.get(typeString);
			if (factory != null) {
				TypeProperties tp = typeProperties.get(typeString);
				boolean le = (tp.le != null) ? tp.le.booleanValue() : state.getLittleEndian();
				TemplateItem item = factory.createTemplateItem(this, state, tokens, token, tp.mts, 0, le);
				if (item != null) return item;
			}
			factory = packedTypes.get(typeString);
			if (factory != null) {
				TypeProperties tp = typeProperties.get(typeString);
				boolean le = (tp.le != null) ? tp.le.booleanValue() : state.getLittleEndian();
				PackedItem item = factory.createPackedItem(this, state, tokens, token, tp.mts, 0, le);
				if (item != null) return completePackedBlock(state, tokens, item, le);
			}
			// identifier contains a number?
			Matcher m = NAME_WITH_NUMBER.matcher(typeString);
			if (m.matches()) {
				try {
					int mv = Integer.parseInt(m.group(2));
					if (mv >= 0 && mv < 100) {
						// bit width
						typeString = m.group(1) + "#" + m.group(3);
						factory = templateTypes.get(typeString);
						if (factory != null && (mv & 7) == 0) {
							TypeProperties tp = typeProperties.get(typeString);
							boolean le = (tp.le != null) ? tp.le.booleanValue() : state.getLittleEndian();
							TemplateItem item = factory.createTemplateItem(this, state, tokens, token, tp.mts, mv, le);
							if (item != null) return item;
						}
						factory = packedTypes.get(typeString);
						if (factory != null) {
							TypeProperties tp = typeProperties.get(typeString);
							boolean le = (tp.le != null) ? tp.le.booleanValue() : state.getLittleEndian();
							PackedItem item = factory.createPackedItem(this, state, tokens, token, tp.mts, mv, le);
							if (item != null) return completePackedBlock(state, tokens, item, le);
						}
					}
					if (mv >= 0 && mv < 0x1000) {
						// byte length
						typeString = m.group(1) + "$" + m.group(3);
						factory = templateTypes.get(typeString);
						if (factory != null) {
							TypeProperties tp = typeProperties.get(typeString);
							boolean le = (tp.le != null) ? tp.le.booleanValue() : state.getLittleEndian();
							TemplateItem item = factory.createTemplateItem(this, state, tokens, token, tp.mts, mv, le);
							if (item != null) return item;
						}
						factory = packedTypes.get(typeString);
						if (factory != null) {
							TypeProperties tp = typeProperties.get(typeString);
							boolean le = (tp.le != null) ? tp.le.booleanValue() : state.getLittleEndian();
							PackedItem item = factory.createPackedItem(this, state, tokens, token, tp.mts, mv, le);
							if (item != null) return completePackedBlock(state, tokens, item, le);
						}
					}
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		}
		// unknown
		if (token == null) throw new TemplateException(ERROR_SYNTAX_TYPE_EOF);
		throw new TemplateException(ERROR_SYNTAX_TYPE, token.image);
	}
	
	private PackedBlock createPackedBlock(TextTemplateInputState state, TextTemplateTokenStream tokens, TextTemplateTokenStream.Token token, boolean le) {
		String blockTypeString = token.value.toString();
		String blockName = tokens.nextNameOrNull();
		tokens.nextBlockBeginOrThrow();
		ArrayList<PackedItem> items = new ArrayList<PackedItem>();
		BigIntegerBitOutputStream meterstick = new BigIntegerBitOutputStream();
		while (!tokens.nextBlockEnd()) {
			token = tokens.next();
			PackedItem item = createPackedItem(state, tokens, token, blockTypeString, le);
			items.add(item); item.measure(meterstick);
		}
		String endName = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		if ((meterstick.getWidth() & 7) != 0) throw new TemplateException(ERROR_PACKED_ALIGN);
		PackedBlock block = new PackedBlock((le ? $BKCP : $PCKB), (le ? $EKCP : $PCKE), blockTypeString, blockName, endName, le);
		block.addAll(items);
		return block;
	}
	
	private PackedBlock completePackedBlock(TextTemplateInputState state, TextTemplateTokenStream tokens, PackedItem item, boolean le) {
		ArrayList<PackedItem> items = new ArrayList<PackedItem>();
		BigIntegerBitOutputStream meterstick = new BigIntegerBitOutputStream();
		items.add(item); item.measure(meterstick);
		while ((meterstick.getWidth() & 7) != 0) {
			TextTemplateTokenStream.Token token = tokens.next();
			item = createPackedItem(state, tokens, token, null, le);
			if (item == null) throw new TemplateException(ERROR_PACKED_ALIGN);
			items.add(item); item.measure(meterstick);
		}
		PackedBlock block = new PackedBlock(0, 0, null, null, null, le);
		block.addAll(items);
		return block;
	}
	
	private PackedItem createPackedItem(TextTemplateInputState state, TextTemplateTokenStream tokens, TextTemplateTokenStream.Token token, String bts, boolean le) {
		if (token != null && token.type == TextTemplateTokenStream.TokenType.KEYWORD) {
			String ts = token.value.toString().toLowerCase();
			// packed block
			if (ts.equals("packed") || ts.equals("packedbe") || ts.equals("packedle")) {
				if (bts == null) return null;
				throw new TemplateException(ERROR_PACKED_NESTED, token.value, bts);
			}
			// fixed string
			TextTemplateItemFactory factory = packedTypes.get(ts);
			if (factory != null) {
				TypeProperties tp = typeProperties.get(ts);
				if (tp.le == null || tp.le.booleanValue() == le) {
					PackedItem item = factory.createPackedItem(this, state, tokens, token, tp.mts, 0, le);
					if (item != null) return item;
				} else {
					if (bts == null) return null;
					throw new TemplateException(ERROR_PACKED_MIXED, token.value, bts, (le ? "little" : "big"));
				}
			}
			if (templateTypes.get(ts) != null) {
				if (bts == null) return null;
				throw new TemplateException(ERROR_PACKED_N_A, token.value, bts);
			}
			// identifier contains a number?
			Matcher m = NAME_WITH_NUMBER.matcher(ts);
			if (m.matches()) {
				try {
					int mv = Integer.parseInt(m.group(2));
					if (mv >= 0 && mv < 100) {
						// bit width
						ts = m.group(1) + "#" + m.group(3);
						factory = packedTypes.get(ts);
						if (factory != null) {
							TypeProperties tp = typeProperties.get(ts);
							if (tp.le == null || tp.le.booleanValue() == le) {
								PackedItem item = factory.createPackedItem(this, state, tokens, token, tp.mts, mv, le);
								if (item != null) return item;
							} else {
								if (bts == null) return null;
								throw new TemplateException(ERROR_PACKED_MIXED, token.value, bts, (le ? "little" : "big"));
							}
						}
						if (templateTypes.get(ts) != null) {
							if (bts == null) return null;
							throw new TemplateException(ERROR_PACKED_N_A, token.value, bts);
						}
					}
					if (mv >= 0 && mv < 0x1000) {
						// byte length
						ts = m.group(1) + "$" + m.group(3);
						factory = packedTypes.get(ts);
						if (factory != null) {
							TypeProperties tp = typeProperties.get(ts);
							if (tp.le == null || tp.le.booleanValue() == le) {
								PackedItem item = factory.createPackedItem(this, state, tokens, token, tp.mts, mv, le);
								if (item != null) return item;
							} else {
								if (bts == null) return null;
								throw new TemplateException(ERROR_PACKED_MIXED, token.value, bts, (le ? "little" : "big"));
							}
						}
						if (templateTypes.get(ts) != null) {
							if (bts == null) return null;
							throw new TemplateException(ERROR_PACKED_N_A, token.value, bts);
						}
					}
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		}
		// unknown
		if (bts == null) return null;
		if (token == null) throw new TemplateException(ERROR_SYNTAX_TYPE_EOF);
		throw new TemplateException(ERROR_SYNTAX_TYPE, token.image);
	}
	
	private static void putTypeProperties1(
		HashMap<String,TypeProperties> checkSet,
		HashMap<String,TypeProperties> destSet,
		String ts
	) {
		if (ts.contains("&")) {
			// endianness specified in type string
			if (ts.indexOf("&") != ts.lastIndexOf("&")) throw new TemplateException(ERROR_TYPE_INVALID, ts);
			if (ts.contains("!") || ts.contains("^")) throw new TemplateException(ERROR_TYPE_INVALID, ts);
			putTypeProperties2(checkSet, destSet, ts.replace("&", ""), ts, null);
			putTypeProperties2(checkSet, destSet, ts.replace("&", "le"), ts, true);
			putTypeProperties2(checkSet, destSet, ts.replace("&", "be"), ts, false);
		} else if (ts.contains("!")) {
			// little-endian only
			if (ts.indexOf("!") != ts.lastIndexOf("!")) throw new TemplateException(ERROR_TYPE_INVALID, ts);
			if (ts.contains("&") || ts.contains("^")) throw new TemplateException(ERROR_TYPE_INVALID, ts);
			putTypeProperties2(checkSet, destSet, ts.replace("!", ""), ts, true);
		} else if (ts.contains("^")) {
			// big-endian only
			if (ts.indexOf("^") != ts.lastIndexOf("^")) throw new TemplateException(ERROR_TYPE_INVALID, ts);
			if (ts.contains("&") || ts.contains("!")) throw new TemplateException(ERROR_TYPE_INVALID, ts);
			putTypeProperties2(checkSet, destSet, ts.replace("^", ""), ts, false);
		} else {
			// endianness specified in state
			putTypeProperties2(checkSet, destSet, ts, ts, null);
		}
	}
	
	private static void putTypeProperties2(
		HashMap<String,TypeProperties> checkSet,
		HashMap<String,TypeProperties> destSet,
		String ts, String mts, Boolean le
	) {
		// check for multiple # or $ markers
		if (ts.contains("#") && ts.contains("$")) throw new TemplateException(ERROR_TYPE_INVALID, mts);
		if (ts.indexOf("#") != ts.lastIndexOf("#")) throw new TemplateException(ERROR_TYPE_INVALID, mts);
		if (ts.indexOf("$") != ts.lastIndexOf("$")) throw new TemplateException(ERROR_TYPE_INVALID, mts);
		// check for valid identifier when # or $ is replaced with a number
		String nameString = ts.replace('#', '0').replace('$', '0');
		if (!NAME.matcher(nameString).matches()) throw new TemplateException(ERROR_TYPE_INVALID, mts);
		// check for duplicate identifier
		String keyString = ts.toLowerCase();
		String keyString1 = keyString.replace('#', '$');
		String keyString2 = keyString.replace('$', '#');
		if (checkSet.containsKey(keyString1)) throw new TemplateException(ERROR_TYPE_DUPLICATE, mts);
		if (checkSet.containsKey(keyString2)) throw new TemplateException(ERROR_TYPE_DUPLICATE, mts);
		if (destSet.containsKey(keyString1)) throw new TemplateException(ERROR_TYPE_DUPLICATE, mts);
		if (destSet.containsKey(keyString2)) throw new TemplateException(ERROR_TYPE_DUPLICATE, mts);
		// put type properties
		destSet.put(keyString, new TypeProperties(mts, le));
	}
	
	private static class TypeProperties {
		private final String mts;
		private final Boolean le;
		private TypeProperties(String mts, Boolean le) {
			this.mts = mts;
			this.le = le;
		}
	}
}
