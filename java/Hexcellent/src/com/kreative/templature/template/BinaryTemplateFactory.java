package com.kreative.templature.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BinaryTemplateFactory {
	private static final int TI = (1 << 0); // Template Item
	private static final int PI = (1 << 1); // Packed Item
	private static final int DM = (1 << 2); // Decimal Mask (form of ABmn or nmBA where mn is the number of bits)
	private static final int HM = (1 << 3); // Hexadecimal Mask (form of Axyz or zyxA where xyz is the number of bytes)
	private static final int LE = (1 << 4); // Little Endian
	
	private static final int $PCKB = TemplateConstants.$PCKB;
	private static final int $BKCP = TemplateConstants.$BKCP;
	private static final int $PCKE = TemplateConstants.$PCKE;
	private static final int $EKCP = TemplateConstants.$EKCP;
	
	private static final String ERROR_TYPE_DUPLICATE = TemplateConstants.ERROR_TYPE_DUPLICATE;
	private static final String ERROR_TYPE_INVALID = TemplateConstants.ERROR_TYPE_INVALID;
	private static final String ERROR_TYPE_UNKNOWN = TemplateConstants.ERROR_TYPE_UNKNOWN;
	private static final String ERROR_PACKED_NO_END = TemplateConstants.ERROR_PACKED_NO_END;
	private static final String ERROR_PACKED_NESTED = TemplateConstants.ERROR_PACKED_NESTED;
	private static final String ERROR_PACKED_MIXED = TemplateConstants.ERROR_PACKED_MIXED;
	private static final String ERROR_PACKED_N_A = TemplateConstants.ERROR_PACKED_N_A;
	private static final String ERROR_PACKED_ALIGN = TemplateConstants.ERROR_PACKED_ALIGN;
	private static final String ERROR_PACKED_END = TemplateConstants.ERROR_PACKED_END;
	
	private final HashMap<Integer,Integer> typeProperties;
	private final HashMap<Integer,BinaryTemplateItemFactory> unmaskedTypes;
	private final HashMap<Integer,BinaryTemplateItemFactory> unmaskedPackedTypes;
	private final HashMap<Integer,BinaryTemplateItemFactory> decMaskedTypes;
	private final HashMap<Integer,BinaryTemplateItemFactory> decMaskedPackedTypes;
	private final HashMap<Integer,BinaryTemplateItemFactory> hexMaskedTypes;
	private final HashMap<Integer,BinaryTemplateItemFactory> hexMaskedPackedTypes;
	
	public BinaryTemplateFactory() {
		typeProperties = new HashMap<Integer,Integer>();
		typeProperties.put($PCKB, TI); typeProperties.put($BKCP, TI|LE);
		typeProperties.put($PCKE, TI); typeProperties.put($EKCP, TI|LE);
		unmaskedTypes = new HashMap<Integer,BinaryTemplateItemFactory>();
		unmaskedPackedTypes = new HashMap<Integer,BinaryTemplateItemFactory>();
		decMaskedTypes = new HashMap<Integer,BinaryTemplateItemFactory>();
		decMaskedPackedTypes = new HashMap<Integer,BinaryTemplateItemFactory>();
		hexMaskedTypes = new HashMap<Integer,BinaryTemplateItemFactory>();
		hexMaskedPackedTypes = new HashMap<Integer,BinaryTemplateItemFactory>();
	}
	
	public void registerAll(Iterable<? extends BinaryTemplateItemFactory> i) {
		for (BinaryTemplateItemFactory f : i) registerItemFactory(f);
	}
	
	public void registerDefaults() {
		try {
			for (Object o : (Iterable<?>)(
				Class.forName("com.kreative.templature.template.factory.BOM")
				.getField("BINARY")
				.get(null)
			)) registerItemFactory((BinaryTemplateItemFactory)o);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void registerExtensions() {
		try {
			for (Object o : (Iterable<?>)(
				Class.forName("java.util.ServiceLoader")
				.getMethod("load", Class.class)
				.invoke(null, BinaryTemplateItemFactory.class)
			)) registerItemFactory((BinaryTemplateItemFactory)o);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void registerItemFactory(BinaryTemplateItemFactory factory) {
		for (long typeConstantAndProperties : factory.getTypeConstants()) {
			int typeConstant = (int)typeConstantAndProperties;
			if (typeProperties.containsKey(typeConstant)) {
				throw new TemplateException(ERROR_TYPE_DUPLICATE, typeConstant);
			}
			int properties = (int)(typeConstantAndProperties >> 32);
			if ((properties & HM) != 0) {
				boolean le = ((properties & LE) != 0);
				if (
					(properties & (TI|PI)) == 0 || (properties & DM) != 0 ||
					typeConstantMask(typeConstant) != (le ? 0x000000FF : 0xFF000000)
				) {
					throw new TemplateException(ERROR_TYPE_INVALID, typeConstant);
				}
				for (int w : (le ? HEX8 : HEX16)) {
					for (int v : (le ? HEX16 : HEX8)) {
						for (int u : (le ? HEX24 : HEX0)) {
							int mtc = typeConstant | w | v | u;
							if (typeProperties.containsKey(mtc)) {
								// Special exception for collision between $C___ and $___C.
								// This is the only collision allowed because reasons.
								if ((mtc & 0xFF0000FF) == 0x43000043) continue;
								throw new TemplateException(ERROR_TYPE_DUPLICATE, mtc);
							}
							typeProperties.put(mtc, properties);
						}
					}
				}
				typeProperties.put(typeConstant, properties);
				if ((properties & PI) != 0) hexMaskedPackedTypes.put(typeConstant, factory);
				if ((properties & TI) != 0) hexMaskedTypes.put(typeConstant, factory);
			} else if ((properties & DM) != 0) {
				boolean le = ((properties & LE) != 0);
				if (
					(properties & (TI|PI)) == 0 || (properties & HM) != 0 ||
					typeConstantMask(typeConstant) != (le ? 0x0000FFFF : 0xFFFF0000)
				) {
					throw new TemplateException(ERROR_TYPE_INVALID, typeConstant);
				}
				for (int v : (le ? DEC16 : DEC8)) {
					for (int u : (le ? DEC24 : DEC0)) {
						int mtc = typeConstant | v | u;
						if (typeProperties.containsKey(mtc)) {
							throw new TemplateException(ERROR_TYPE_DUPLICATE, mtc);
						}
						typeProperties.put(mtc, properties);
					}
				}
				typeProperties.put(typeConstant, properties);
				if ((properties & PI) != 0) decMaskedPackedTypes.put(typeConstant, factory);
				if ((properties & TI) != 0) decMaskedTypes.put(typeConstant, factory);
			} else {
				if ((properties & (TI|PI)) == 0 || typeConstantMask(typeConstant) != 0xFFFFFFFF) {
					throw new TemplateException(ERROR_TYPE_INVALID, typeConstant);
				}
				typeProperties.put(typeConstant, properties);
				if ((properties & PI) != 0) unmaskedPackedTypes.put(typeConstant, factory);
				if ((properties & TI) != 0) unmaskedTypes.put(typeConstant, factory);
			}
		}
	}
	
	public Template createTemplate(BinaryTemplateInputStream in) throws IOException {
		Template template = new Template();
		for (;;) {
			BinaryTemplateInputStream.Item it = in.read();
			if (it == null) return template;
			template.add(createTemplateItem(in, it));
		}
	}
	
	public TemplateItem createTemplateItem(BinaryTemplateInputStream in, BinaryTemplateInputStream.Item it) throws IOException {
		// Packed blocks
		if (it.type == $PCKB) return createPackedBlock(in, it, false);
		if (it.type == $BKCP) return createPackedBlock(in, it, true);
		if (it.type == $PCKE) throw new TemplateException(ERROR_PACKED_END, it.type, $PCKB);
		if (it.type == $EKCP) throw new TemplateException(ERROR_PACKED_END, it.type, $BKCP);
		// Four fixed characters
		BinaryTemplateItemFactory factory = unmaskedTypes.get(it.type);
		if (factory != null) {
			TemplateItem item = factory.createTemplateItem(this, in, it, it.type, 0);
			if (item != null) return item;
		}
		factory = unmaskedPackedTypes.get(it.type);
		if (factory != null) {
			PackedItem item = factory.createPackedItem(this, in, it, it.type, 0);
			if (item != null) {
				boolean ile = (typeProperties.get(it.type) & LE) != 0;
				return completePackedBlock(in, item, ile);
			}
		}
		// Two fixed characters, two decimal digits, big endian
		int maskedTypeConstant, maskedValue;
		if ((maskedValue = unmaskDecValue(it.type, false)) >= 0) {
			factory = decMaskedTypes.get((maskedTypeConstant = it.type & 0xFFFF0000)); 
			if (factory != null && (maskedValue & 7) == 0) {
				TemplateItem item = factory.createTemplateItem(this, in, it, maskedTypeConstant, maskedValue);
				if (item != null) return item;
			}
			factory = decMaskedPackedTypes.get(maskedTypeConstant);
			if (factory != null) {
				PackedItem item = factory.createPackedItem(this, in, it, maskedTypeConstant, maskedValue);
				if (item != null) return completePackedBlock(in, item, false);
			}
		}
		// Two fixed characters, two decimal digits, little endian
		if ((maskedValue = unmaskDecValue(it.type, true)) >= 0) {
			factory = decMaskedTypes.get((maskedTypeConstant = it.type & 0x0000FFFF));
			if (factory != null && (maskedValue & 7) == 0) {
				TemplateItem item = factory.createTemplateItem(this, in, it, maskedTypeConstant, maskedValue);
				if (item != null) return item;
			}
			factory = decMaskedPackedTypes.get(maskedTypeConstant);
			if (factory != null) {
				PackedItem item = factory.createPackedItem(this, in, it, maskedTypeConstant, maskedValue);
				if (item != null) return completePackedBlock(in, item, true);
			}
		}
		// One fixed character, three hexadecimal digits, big endian
		if ((maskedValue = unmaskHexValue(it.type, false)) >= 0) {
			factory = hexMaskedTypes.get((maskedTypeConstant = it.type & 0xFF000000));
			if (factory != null) {
				TemplateItem item = factory.createTemplateItem(this, in, it, maskedTypeConstant, maskedValue);
				if (item != null) return item;
			}
			factory = hexMaskedPackedTypes.get(maskedTypeConstant);
			if (factory != null) {
				PackedItem item = factory.createPackedItem(this, in, it, maskedTypeConstant, maskedValue);
				if (item != null) return completePackedBlock(in, item, false);
			}
		}
		// One fixed character, three hexadecimal digits, little endian
		if ((maskedValue = unmaskHexValue(it.type, true)) >= 0) {
			factory = hexMaskedTypes.get((maskedTypeConstant = it.type & 0x000000FF));
			if (factory != null) {
				TemplateItem item = factory.createTemplateItem(this, in, it, maskedTypeConstant, maskedValue);
				if (item != null) return item;
			}
			factory = hexMaskedPackedTypes.get(maskedTypeConstant);
			if (factory != null) {
				PackedItem item = factory.createPackedItem(this, in, it, maskedTypeConstant, maskedValue);
				if (item != null) return completePackedBlock(in, item, true);
			}
		}
		// Unknown
		throw new TemplateException(ERROR_TYPE_UNKNOWN, it.type);
	}
	
	private PackedBlock createPackedBlock(BinaryTemplateInputStream in, BinaryTemplateInputStream.Item it, boolean le) throws IOException {
		int typeConstant = it.type;
		int endConstant = le ? $EKCP : $PCKE;
		String typeString = le ? "packedle" : "packedbe";
		String beginName = it.label;
		ArrayList<PackedItem> items = new ArrayList<PackedItem>();
		BigIntegerBitOutputStream meterstick = new BigIntegerBitOutputStream();
		for (;;) {
			it = in.read();
			if (it == null) throw new TemplateException(ERROR_PACKED_NO_END, typeConstant, endConstant);
			if (it.type == endConstant) break;
			if (it.type == $PCKB || it.type == $BKCP) throw new TemplateException(ERROR_PACKED_NESTED, it.type, typeConstant);
			if (it.type == $PCKE || it.type == $EKCP) throw new TemplateException(ERROR_PACKED_MIXED, it.type, typeConstant, (le ? "little" : "big"));
			PackedItem item = createPackedItem(in, it, le, false);
			items.add(item); item.measure(meterstick);
		}
		if ((meterstick.getWidth() & 7) != 0) throw new TemplateException(ERROR_PACKED_ALIGN);
		PackedBlock block = new PackedBlock(typeConstant, it.type, typeString, beginName, it.label, le);
		block.addAll(items);
		return block;
	}
	
	private PackedBlock completePackedBlock(BinaryTemplateInputStream in, PackedItem item, boolean le) throws IOException {
		ArrayList<PackedItem> items = new ArrayList<PackedItem>();
		BigIntegerBitOutputStream meterstick = new BigIntegerBitOutputStream();
		items.add(item); item.measure(meterstick);
		while ((meterstick.getWidth() & 7) != 0) {
			BinaryTemplateInputStream.Item it = in.read();
			if (it == null) throw new TemplateException(ERROR_PACKED_ALIGN);
			if (it.type == $PCKB) throw new TemplateException(ERROR_PACKED_ALIGN);
			if (it.type == $BKCP) throw new TemplateException(ERROR_PACKED_ALIGN);
			if (it.type == $PCKE) throw new TemplateException(ERROR_PACKED_END, it.type, $PCKB);
			if (it.type == $EKCP) throw new TemplateException(ERROR_PACKED_END, it.type, $BKCP);
			item = createPackedItem(in, it, le, true);
			if (item == null) throw new TemplateException(ERROR_PACKED_ALIGN);
			items.add(item); item.measure(meterstick);
		}
		PackedBlock block = new PackedBlock(0, 0, null, null, null, le);
		block.addAll(items);
		return block;
	}
	
	private PackedItem createPackedItem(BinaryTemplateInputStream in, BinaryTemplateInputStream.Item it, boolean le, boolean ignoreErrors) throws IOException {
		// Four fixed characters
		BinaryTemplateItemFactory factory = unmaskedPackedTypes.get(it.type);
		if (factory != null) {
			boolean ile = (typeProperties.get(it.type) & LE) != 0;
			if (ile == le) {
				PackedItem item = factory.createPackedItem(this, in, it, it.type, 0);
				if (item != null) return item;
			} else {
				if (ignoreErrors) return null;
				throw new TemplateException(ERROR_PACKED_MIXED, it.type, (le ? $BKCP : $PCKB), (le ? "little" : "big"));
			}
		}
		if (unmaskedTypes.get(it.type) != null) {
			if (ignoreErrors) return null;
			throw new TemplateException(ERROR_PACKED_N_A, it.type, (le ? $BKCP : $PCKB));
		}
		// Two fixed characters, two decimal digits, big endian
		int maskedTypeConstant, maskedValue;
		if ((maskedValue = unmaskDecValue(it.type, false)) >= 0) {
			factory = decMaskedPackedTypes.get((maskedTypeConstant = it.type & 0xFFFF0000));
			if (factory != null) {
				if (!le) {
					PackedItem item = factory.createPackedItem(this, in, it, maskedTypeConstant, maskedValue);
					if (item != null) return item;
				} else {
					if (ignoreErrors) return null;
					throw new TemplateException(ERROR_PACKED_MIXED, it.type, (le ? $BKCP : $PCKB), (le ? "little" : "big"));
				}
			}
			if (decMaskedTypes.get(maskedTypeConstant) != null) {
				if (ignoreErrors) return null;
				throw new TemplateException(ERROR_PACKED_N_A, it.type, (le ? $BKCP : $PCKB));
			}
		}
		// Two fixed characters, two decimal digits, little endian
		if ((maskedValue = unmaskDecValue(it.type, true)) >= 0) {
			factory = decMaskedPackedTypes.get((maskedTypeConstant = it.type & 0x0000FFFF));
			if (factory != null) {
				if (le) {
					PackedItem item = factory.createPackedItem(this, in, it, maskedTypeConstant, maskedValue);
					if (item != null) return item;
				} else {
					if (ignoreErrors) return null;
					throw new TemplateException(ERROR_PACKED_MIXED, it.type, (le ? $BKCP : $PCKB), (le ? "little" : "big"));
				}
			}
			if (decMaskedTypes.get(maskedTypeConstant) != null) {
				if (ignoreErrors) return null;
				throw new TemplateException(ERROR_PACKED_N_A, it.type, (le ? $BKCP : $PCKB));
			}
		}
		// One fixed character, three hexadecimal digits, big endian
		if ((maskedValue = unmaskHexValue(it.type, false)) >= 0) {
			factory = hexMaskedPackedTypes.get((maskedTypeConstant = it.type & 0xFF000000));
			if (factory != null) {
				if (!le) {
					PackedItem item = factory.createPackedItem(this, in, it, maskedTypeConstant, maskedValue);
					if (item != null) return item;
				} else {
					if (ignoreErrors) return null;
					throw new TemplateException(ERROR_PACKED_MIXED, it.type, (le ? $BKCP : $PCKB), (le ? "little" : "big"));
				}
			}
			if (hexMaskedTypes.get(maskedTypeConstant) != null) {
				if (ignoreErrors) return null;
				throw new TemplateException(ERROR_PACKED_N_A, it.type, (le ? $BKCP : $PCKB));
			}
		}
		// One fixed character, three hexadecimal digits, little endian
		if ((maskedValue = unmaskHexValue(it.type, true)) >= 0) {
			factory = hexMaskedPackedTypes.get((maskedTypeConstant = it.type & 0x000000FF));
			if (factory != null) {
				if (le) {
					PackedItem item = factory.createPackedItem(this, in, it, maskedTypeConstant, maskedValue);
					if (item != null) return item;
				} else {
					if (ignoreErrors) return null;
					throw new TemplateException(ERROR_PACKED_MIXED, it.type, (le ? $BKCP : $PCKB), (le ? "little" : "big"));
				}
			}
			if (hexMaskedTypes.get(maskedTypeConstant) != null) {
				if (ignoreErrors) return null;
				throw new TemplateException(ERROR_PACKED_N_A, it.type, (le ? $BKCP : $PCKB));
			}
		}
		// Unknown
		if (ignoreErrors) return null;
		throw new TemplateException(ERROR_TYPE_UNKNOWN, it.type);
	}
	
	private static int typeConstantMask(int tc) {
		int mask = 0;
		if ((tc & 0xFF000000) != 0) mask |= 0xFF000000;
		if ((tc & 0x00FF0000) != 0) mask |= 0x00FF0000;
		if ((tc & 0x0000FF00) != 0) mask |= 0x0000FF00;
		if ((tc & 0x000000FF) != 0) mask |= 0x000000FF;
		return mask;
	}
	
	private static int unmaskDecValue(int tc, boolean le) {
		int v = udigit((tc >> (le ? 16 : 8)) & 0xFF); if (v < 0 || v > 9) return -1;
		int u = udigit((tc >> (le ? 24 : 0)) & 0xFF); if (u < 0 || u > 9) return -1;
		return (v * 10) + u;
	}
	
	private static int unmaskHexValue(int tc, boolean le) {
		int w = udigit((tc >> (le ? 8 : 16)) & 0xFF); if (w < 0 || w > 15) return -1;
		int v = udigit((tc >> (le ? 16 : 8)) & 0xFF); if (v < 0 || v > 15) return -1;
		int u = udigit((tc >> (le ? 24 : 0)) & 0xFF); if (u < 0 || u > 15) return -1;
		return (w << 8) | (v << 4) | u;
	}
	
	private static int udigit(int cp) {
		if (cp >= '0' && cp <= '9') return (cp - '0');
		if (cp >= 'A' && cp <= 'Z') return (cp - 'A' + 10);
		return -1;
	}
	
	private static final int[] HEX24 = {'0'<<24,'1'<<24,'2'<<24,'3'<<24,'4'<<24,'5'<<24,'6'<<24,'7'<<24,'8'<<24,'9'<<24,'A'<<24,'B'<<24,'C'<<24,'D'<<24,'E'<<24,'F'<<24};
	private static final int[] HEX16 = {'0'<<16,'1'<<16,'2'<<16,'3'<<16,'4'<<16,'5'<<16,'6'<<16,'7'<<16,'8'<<16,'9'<<16,'A'<<16,'B'<<16,'C'<<16,'D'<<16,'E'<<16,'F'<<16};
	private static final int[] HEX8 = {'0'<<8,'1'<<8,'2'<<8,'3'<<8,'4'<<8,'5'<<8,'6'<<8,'7'<<8,'8'<<8,'9'<<8,'A'<<8,'B'<<8,'C'<<8,'D'<<8,'E'<<8,'F'<<8};
	private static final int[] HEX0 = {'0'<<0,'1'<<0,'2'<<0,'3'<<0,'4'<<0,'5'<<0,'6'<<0,'7'<<0,'8'<<0,'9'<<0,'A'<<0,'B'<<0,'C'<<0,'D'<<0,'E'<<0,'F'<<0};
	private static final int[] DEC24 = {'0'<<24,'1'<<24,'2'<<24,'3'<<24,'4'<<24,'5'<<24,'6'<<24,'7'<<24,'8'<<24,'9'<<24};
	private static final int[] DEC16 = {'0'<<16,'1'<<16,'2'<<16,'3'<<16,'4'<<16,'5'<<16,'6'<<16,'7'<<16,'8'<<16,'9'<<16};
	private static final int[] DEC8 = {'0'<<8,'1'<<8,'2'<<8,'3'<<8,'4'<<8,'5'<<8,'6'<<8,'7'<<8,'8'<<8,'9'<<8};
	private static final int[] DEC0 = {'0'<<0,'1'<<0,'2'<<0,'3'<<0,'4'<<0,'5'<<0,'6'<<0,'7'<<0,'8'<<0,'9'<<0};
}
