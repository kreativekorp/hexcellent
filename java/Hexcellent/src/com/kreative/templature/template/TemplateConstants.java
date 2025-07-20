package com.kreative.templature.template;

public interface TemplateConstants {
	public static final String ERROR_TYPE_DUPLICATE = "The field type '^0' is already defined.";
	public static final String ERROR_TYPE_INVALID = "The field type '^0' is incorrectly defined.";
	public static final String ERROR_TYPE_UNKNOWN = "It contains an unknown field type ('^0').";
	
	public static final String ERROR_ENUM_NO_VALUE = "It contains an enum field ('^0') that is not followed by an enum value field ('^1').";
	public static final String ERROR_ENUM_NO_END = "It contains an enum begin ('^0') that has no corresponding enum end ('^1').";
	public static final String ERROR_ENUM_VALUE = "It contains an enum value field ('^0') that is not preceeded by an enum field.";
	public static final String ERROR_ENUM_END = "It contains an enum end ('^0') that has no corresponding enum begin.";
	
	public static final String ERROR_TEXT_NOT_END = "It contains a '^0' field that is not the last field in the template.";
	public static final String ERROR_HEX_NOT_END = "It contains a hex dump ('^0') field that is not the last field in the template.";
	
	public static final String ERROR_LIST_COUNTER = "It contains a list counter ('^0') field that is not followed by a counted list ('^1') field.";
	public static final String ERROR_LIST_NO_COUNT = "It contains a counted list ('^0') field that is not preceeded by a list counter ('^1' or '^2') field.";
	public static final String ERROR_LIST_NO_END = "It contains a list begin ('^0') that has no corresponding list end ('^1').";
	public static final String ERROR_LIST_NESTED = "It contains an unterminated list ('^0') inside of another unterminated list ('^1').";
	public static final String ERROR_LIST_END = "It contains a list end ('^0') that has no corresponding list begin ('^1', '^2', or '^3').";
	
	public static final String ERROR_PACKED_NO_END = "It contains a packed begin ('^0') that has no corresponding packed end ('^1').";
	public static final String ERROR_PACKED_NESTED = "It contains a packed block ('^0') inside of another packed block ('^1').";
	public static final String ERROR_PACKED_MIXED = "It contains a packed field ('^0') inside of a packed block ('^1') of opposite endianness. All fields in a '^1' block must be ^2-endian.";
	public static final String ERROR_PACKED_N_A = "It contains a non-packable field ('^0') inside of a packed block ('^1').";
	public static final String ERROR_PACKED_ALIGN = "It contains an incorrect number of packed fields. Packed fields must add to a multiple of eight bits.";
	public static final String ERROR_PACKED_END = "It contains a packed end ('^0') that has no corresponding packed begin ('^1').";
	
	public static final String ERROR_COND_NO_END = "It contains a conditional begin ('^0') that has no corresponding conditional end ('^1').";
	public static final String ERROR_COND_CASE = "It contains a conditional case ('^0') that has no corresponding conditional begin ('^1').";
	public static final String ERROR_COND_DEF = "It contains a conditional default ('^0') that has no corresponding conditional begin ('^1').";
	public static final String ERROR_COND_DEAD_CASE = "It contains a conditional case ('^0') following a conditional default ('^1').";
	public static final String ERROR_COND_DEAD_DEF = "It contains a conditional default ('^0') following another conditional default ('^1').";
	public static final String ERROR_COND_END = "It contains a conditional end ('^0') that has no corresponding conditional begin ('^1').";
	
	public static final String ERROR_SWITCH_NO_END = "It contains a switch begin ('^0') that has no corresponding switch end ('^1').";
	public static final String ERROR_SWITCH_NO_CASE = "It contains a switch begin ('^0') that is not followed by a switch case ('^1') or switch default ('^2').";
	public static final String ERROR_SWITCH_CASE = "It contains a switch case ('^0') that has no corresponding switch begin ('^1').";
	public static final String ERROR_SWITCH_DEF = "It contains a switch default ('^0') that has no corresponding switch begin ('^1').";
	public static final String ERROR_SWITCH_DEAD_CASE = "It contains a switch case ('^0') following a switch default ('^1').";
	public static final String ERROR_SWITCH_DEAD_DEF = "It contains a switch default ('^0') following another switch default ('^1').";
	public static final String ERROR_SWITCH_END = "It contains a switch end ('^0') that has no corresponding switch begin ('^1').";
	
	public static final String ERROR_SYNTAX_TOKEN = "It contains a syntax error: Expected ^0 but found ^1.";
	public static final String ERROR_SYNTAX_TOKEN_EOF = "It contains a syntax error: Expected ^0 but found end of input.";
	public static final String ERROR_SYNTAX_TYPE = "It contains a syntax error: Expected a field type but found ^0.";
	public static final String ERROR_SYNTAX_TYPE_EOF = "It contains a syntax error: Expected a field type but found end of input.";
	public static final String ERROR_SYNTAX_NAME = "It contains a syntax error: Expected an identifier but found ^0.";
	public static final String ERROR_SYNTAX_NAME_EOF = "It contains a syntax error: Expected an identifier but found end of input.";
	public static final String ERROR_SYNTAX_STR = "It contains a syntax error: Expected a string but found ^0.";
	public static final String ERROR_SYNTAX_STR_EOF = "It contains a syntax error: Expected a string but found end of input.";
	public static final String ERROR_SYNTAX_EXPR = "It contains a syntax error: Expected an expression but found ^0.";
	public static final String ERROR_SYNTAX_EXPR_EOF = "It contains a syntax error: Expected an expression but found end of input.";
	
	public static final String ERROR_SYNTAX_CONB = "It contains a syntax error in a conditional begin ('^0') field: ^1";
	public static final String ERROR_SYNTAX_CONC = "It contains a syntax error in a conditional case ('^0') field: ^1";
	public static final String ERROR_SYNTAX_SELB = "It contains a syntax error in a switch begin ('^0') field: ^1";
	public static final String ERROR_SYNTAX_SELC = "It contains a syntax error in a switch case ('^0') field: ^1";
	
	public static final int $DNIB = 0x444E4942;	public static final int $BIND = 0x42494E44;	// decimal nibble
	public static final int $DBYT = 0x44425954;	public static final int $TYBD = 0x54594244;	// decimal byte
	public static final int $DWRD = 0x44575244;	public static final int $DRWD = 0x44525744;	// decimal word
	public static final int $DLNG = 0x444C4E47;	public static final int $GNLD = 0x474E4C44;	// decimal long
	public static final int $DLLG = 0x444C4C47;	public static final int $GLLD = 0x474C4C44;	// decimal long long
	public static final int $DI__ = 0x44490000;	public static final int $__ID = 0x00004944;	// decimal integer
	public static final int $DS__ = 0x44530000;	public static final int $__SD = 0x00005344;	// decimal signed
	public static final int $DU__ = 0x44550000;	public static final int $__UD = 0x00005544;	// decimal unsigned
	
	public static final int $HNIB = 0x484E4942;	public static final int $BINH = 0x42494E48;	// hex nibble
	public static final int $HBYT = 0x48425954;	public static final int $TYBH = 0x54594248;	// hex byte
	public static final int $HWRD = 0x48575244;	public static final int $DRWH = 0x44525748;	// hex word
	public static final int $HLNG = 0x484C4E47;	public static final int $GNLH = 0x474E4C48;	// hex long
	public static final int $HLLG = 0x484C4C47;	public static final int $GLLH = 0x474C4C48;	// hex long long
	public static final int $HI__ = 0x48490000;	public static final int $__IH = 0x00004948;	// hex integer
	public static final int $HS__ = 0x48530000;	public static final int $__SH = 0x00005348;	// hex signed
	public static final int $HU__ = 0x48550000;	public static final int $__UH = 0x00005548;	// hex unsigned
	
	public static final int $ONIB = 0x4F4E4942;	public static final int $BINO = 0x42494E4F;	// octal nibble
	public static final int $OBYT = 0x4F425954;	public static final int $TYBO = 0x5459424F;	// octal byte
	public static final int $OWRD = 0x4F575244;	public static final int $DRWO = 0x4452574F;	// octal word
	public static final int $OLNG = 0x4F4C4E47;	public static final int $GNLO = 0x474E4C4F;	// octal long
	public static final int $OLLG = 0x4F4C4C47;	public static final int $GLLO = 0x474C4C4F;	// octal long long
	public static final int $OI__ = 0x4F490000;	public static final int $__IO = 0x0000494F;	// octal integer
	public static final int $OS__ = 0x4F530000;	public static final int $__SO = 0x0000534F;	// octal signed
	public static final int $OU__ = 0x4F550000;	public static final int $__UO = 0x0000554F;	// octal unsigned
	
	public static final int $BNIB = 0x424E4942;	public static final int $BINB = 0x42494E42;	// binary nibble
	public static final int $BBYT = 0x42425954;	public static final int $TYBB = 0x54594242;	// binary byte
	public static final int $BWRD = 0x42575244;	public static final int $DRWB = 0x44525742;	// binary word
	public static final int $BLNG = 0x424C4E47;	public static final int $GNLB = 0x474E4C42;	// binary long
	public static final int $BLLG = 0x424C4C47;	public static final int $GLLB = 0x474C4C42;	// binary long long
	public static final int $BI__ = 0x42490000;	public static final int $__IB = 0x00004942;	// binary integer
	public static final int $BS__ = 0x42530000;	public static final int $__SB = 0x00005342;	// binary signed
	public static final int $BU__ = 0x42550000;	public static final int $__UB = 0x00005542;	// binary unsigned
	
	public static final int $ANIB = 0x414E4942;	public static final int $BINA = 0x42494E41;	// alignment nibble
	public static final int $ABYT = 0x41425954;	public static final int $TYBA = 0x54594241;	// alignment byte
	public static final int $AWRD = 0x41575244;	public static final int $DRWA = 0x44525741;	// alignment word
	public static final int $ALNG = 0x414C4E47;	public static final int $GNLA = 0x474E4C41;	// alignment long
	public static final int $ALLG = 0x414C4C47;	public static final int $GLLA = 0x474C4C41;	// alignment long long
	public static final int $AL__ = 0x414C0000;	public static final int $__LA = 0x00004C41;	// alignment (by bit width)
	
	public static final int $FBIT = 0x46424954;	public static final int $TIBF = 0x54494246;	// filler bit
	public static final int $FNIB = 0x464E4942;	public static final int $BINF = 0x42494E46;	// filler nibble
	public static final int $FBYT = 0x46425954;	public static final int $TYBF = 0x54594246;	// filler byte
	public static final int $FWRD = 0x46575244;	public static final int $DRWF = 0x44525746;	// filler word
	public static final int $FLNG = 0x464C4E47;	public static final int $GNLF = 0x474E4C46;	// filler long
	public static final int $FLLG = 0x464C4C47;	public static final int $GLLF = 0x474C4C46;	// filler long long
	public static final int $FL__ = 0x464C0000;	public static final int $__LF = 0x00004C46;	// filler (by bit width)
	
	public static final int $BBIT = 0x42424954;	public static final int $TIBB = 0x54494242;	// boolean bit
	public static final int $BOOL = 0x424F4F4C;	public static final int $LOOB = 0x4C4F4F42;	// boolean word; true=$0100
	public static final int $BL__ = 0x424C0000;	public static final int $__LB = 0x00004C42;	// boolean (by bit width); true=+1
	public static final int $BN__ = 0x424E0000;	public static final int $__NB = 0x00004E42;	// boolean (by bit width); true=-1
	
	public static final int $FX__ = 0x46580000;	public static final int $__XF = 0x00005846;	// fixed point (by bit width)
	
	public static final int $MFLT = 0x4D464C54;	public static final int $TLFM = 0x544C464D;	// micro float (1.2.1)
	public static final int $NFLT = 0x4E464C54;	public static final int $TLFN = 0x544C464E;	// NVidia float (1.5.2)
	public static final int $HFLT = 0x48464C54;	public static final int $TLFH = 0x544C4648;	// half float (1.5.10)
	public static final int $BFLT = 0x42464C54;	public static final int $TLFB = 0x544C4642;	// bfloat (1.8.7)
	public static final int $TFLT = 0x54464C54;	public static final int $TLFT = 0x544C4654;	// TensorFloat (1.8.10)
	public static final int $AFLT = 0x41464C54;	public static final int $TLFA = 0x544C4641;	// ATI float (1.7.16)
	public static final int $PFLT = 0x50464C54;	public static final int $TLFP = 0x544C4650;	// PXR float (1.8.15)
	public static final int $SFLT = 0x53464C54;	public static final int $TLFS = 0x544C4653;	// single float (1.8.23)
	public static final int $DFLT = 0x44464C54;	public static final int $TLFD = 0x544C4644;	// double float (1.11.52)
	public static final int $QFLT = 0x51464C54;	public static final int $TLFQ = 0x544C4651;	// quadruple float (1.15.112)
	public static final int $OFLT = 0x4F464C54;	public static final int $TLFO = 0x544C464F;	// octuple float (1.19.236)
	public static final int $FP__ = 0x46500000;	public static final int $__PF = 0x00005046;	// floating point (by bit width)
	public static final int $QUAR = 0x51554152;	public static final int $RAUQ = 0x52415551;	// [deprecated] quarter float (1.4.3)
	public static final int $HALF = 0x48414C46;	public static final int $FLAH = 0x464C4148;	// [deprecated] half float (1.5.10)
	public static final int $3QTR = 0x33515452;	public static final int $RTQ3 = 0x52545133;	// [deprecated] ATI float (1.7.16)
	public static final int $SING = 0x53494E47;	public static final int $GNIS = 0x474E4953;	// [deprecated] single float (1.8.23)
	public static final int $DOUB = 0x444F5542;	public static final int $BUOD = 0x42554F44;	// [deprecated] double float (1.11.52)
	public static final int $QUAD = 0x51554144;	public static final int $DAUQ = 0x44415551;	// [deprecated] quadruple float (1.15.112)
	
	public static final int $DIVL = 0x4449564C;	public static final int $LVID = 0x4C564944;	// decimal integer VLQ/Base128
	public static final int $DSVL = 0x4453564C;	public static final int $LVSD = 0x4C565344;	// decimal signed VLQ/Base128
	public static final int $DUVL = 0x4455564C;	public static final int $LVUD = 0x4C565544;	// decimal unsigned VLQ/Base128
	public static final int $HIVL = 0x4849564C;	public static final int $LVIH = 0x4C564948;	// hexadecimal integer VLQ/Base128
	public static final int $HSVL = 0x4853564C;	public static final int $LVSH = 0x4C565348;	// hexadecimal signed VLQ/Base128
	public static final int $HUVL = 0x4855564C;	public static final int $LVUH = 0x4C565548;	// hexadecimal unsigned VLQ/Base128
	public static final int $OIVL = 0x4F49564C;	public static final int $LVIO = 0x4C56494F;	// octal integer VLQ/Base128
	public static final int $OSVL = 0x4F53564C;	public static final int $LVSO = 0x4C56534F;	// octal signed VLQ/Base128
	public static final int $OUVL = 0x4F55564C;	public static final int $LVUO = 0x4C56554F;	// octal unsigned VLQ/Base128
	public static final int $BIVL = 0x4249564C;	public static final int $LVIB = 0x4C564942;	// binary integer VLQ/Base128
	public static final int $BSVL = 0x4253564C;	public static final int $LVSB = 0x4C565342;	// binary signed VLQ/Base128
	public static final int $BUVL = 0x4255564C;	public static final int $LVUB = 0x4C565542;	// binary unsigned VLQ/Base128
	public static final int $VLPD = 0x564C5044;	public static final int $DPLV = 0x44504C56;	// variable length packed decimal
	public static final int $VLFP = 0x564C4650;	public static final int $PFLV = 0x50464C56;	// variable length floating point
	
	public static final int $EBIT = 0x45424954;	public static final int $TIBE = 0x54494245;	// enum bit
	public static final int $ENIB = 0x454E4942;	public static final int $BINE = 0x42494E45;	// enum nibble
	public static final int $EBYT = 0x45425954;	public static final int $TYBE = 0x54594245;	// enum byte
	public static final int $EWRD = 0x45575244;	public static final int $DRWE = 0x44525745;	// enum word
	public static final int $ELNG = 0x454C4E47;	public static final int $GNLE = 0x474E4C45;	// enum long
	public static final int $ELLG = 0x454C4C47;	public static final int $GLLE = 0x474C4C45;	// enum long long
	public static final int $EN__ = 0x454E0000;	public static final int $__NE = 0x00004E45;	// enum (by bit width)
	public static final int $ENMV = 0x454E4D56;	public static final int $VMNE = 0x564D4E45;	// enum value
	public static final int $ENME = 0x454E4D45;	public static final int $EMNE = 0x454D4E45;	// enum end
	public static final int $EOPT = 0x454F5054;	public static final int $TPOE = 0x54504F45;	// [deprecated] enum value
	public static final int $EEND = 0x45454E44;	public static final int $DNEE = 0x444E4545;	// [deprecated] enum end
	
	public static final int $CHAR = 0x43484152;	public static final int $RAHC = 0x52414843;	// character
	public static final int $WCHR = 0x57434852;	public static final int $RHCW = 0x52484357;	// wide character (2 bytes)
	public static final int $TNAM = 0x544E414D;	public static final int $MANT = 0x4D414E54;	// type name (FCC) (4 bytes)
	public static final int $SYMB = 0x53594D42;	public static final int $BMYS = 0x424D5953;	// DFF symbol (ECC) (8 bytes)
	public static final int $UCHR = 0x55434852;	public static final int $RHCU = 0x52484355;	// UTF-16 character
	public static final int $LCHR = 0x4C434852;	public static final int $RHCL = 0x5248434C;	// UTF-32 character
	public static final int $CH__ = 0x43480000;	public static final int $__HC = 0x00004843;	// character constant (by bit width)
	
	public static final int $PSTR = 0x50535452;	public static final int $RTSP = 0x52545350;	// Pascal string
	public static final int $ESTR = 0x45535452;	public static final int $RTSE = 0x52545345;	// Pascal string (even length)
	public static final int $OSTR = 0x4F535452;	public static final int $RTSO = 0x5254534F;	// Pascal string (odd length)
	public static final int $P___ = 0x50000000;	public static final int $___P = 0x00000050;	// Pascal string (fixed length)
	public static final int $WSTR = 0x57535452;	public static final int $RTSW = 0x52545357;	// wide Pascal string (16-bit count)
	public static final int $EWST = 0x45575354;	public static final int $TSWE = 0x54535745;	// wide Pascal string (even length)
	public static final int $OWST = 0x4F575354;	public static final int $TSWO = 0x5453574F;	// wide Pascal string (odd length)
	public static final int $W___ = 0x57000000;	public static final int $___W = 0x00000057;	// wide Pascal string (fixed length)
	public static final int $MSTR = 0x4D535452;	public static final int $RTSM = 0x5254534D;	// medium Pascal string (24-bit count)
	public static final int $EMST = 0x454D5354;	public static final int $TSME = 0x54534D45;	// medium Pascal string (even length)
	public static final int $OMST = 0x4F4D5354;	public static final int $TSMO = 0x54534D4F;	// medium Pascal string (odd length)
	public static final int $M___ = 0x4D000000;	public static final int $___M = 0x0000004D;	// medium Pascal string (fixed length)
	public static final int $LSTR = 0x4C535452;	public static final int $RTSL = 0x5254534C;	// long Pascal string (32-bit count)
	public static final int $ELST = 0x454C5354;	public static final int $TSLE = 0x54534C45;	// long Pascal string (even length)
	public static final int $OLST = 0x4F4C5354;	public static final int $TSLO = 0x54534C4F;	// long Pascal string (odd length)
	public static final int $L___ = 0x4C000000;	public static final int $___L = 0x0000004C;	// long Pascal string (fixed length)
	public static final int $6STR = 0x36535452;	public static final int $RTS6 = 0x52545336;	// [deprecated] Pascal string (48-bit count)
	public static final int $8STR = 0x38535452;	public static final int $RTS8 = 0x52545338;	// [deprecated] Pascal string (64-bit count)
	public static final int $CSTR = 0x43535452;	public static final int $RTSC = 0x52545343;	// C string
	public static final int $ECST = 0x45435354;	public static final int $TSCE = 0x54534345;	// C string (even length)
	public static final int $OCST = 0x4F435354;	public static final int $TSCO = 0x5453434F;	// C string (odd length)
	public static final int $C___ = 0x43000000;	public static final int $___C = 0x00000043;	// C string (fixed length)
	public static final int $N___ = 0x4E000000;	public static final int $___N = 0x0000004E;	// Null-padded fixed-length string
	public static final int $S___ = 0x53000000;	public static final int $___S = 0x00000053;	// Space-padded fixed-length string
	public static final int $TEXT = 0x54455854;	public static final int $TXET = 0x54584554;	// the rest is text
	
	public static final int $H___ = 0x48000000;	public static final int $___H = 0x00000048;	// hex dump (fixed length)
	public static final int $HEXD = 0x48455844;	public static final int $DXEH = 0x44584548;	// the rest is hex
	
	public static final int $PT2D = 0x50543244;	public static final int $D2TP = 0x44325450;	// point (X,Y)
	public static final int $PT3D = 0x50543344;	public static final int $D3TP = 0x44335450;	// point (X,Y,Z)
	public static final int $RECT = 0x52454354;	public static final int $TCER = 0x54434552;	// rectangle (L,T,R,B)
	public static final int $2DPT = 0x32445054;	public static final int $TPD2 = 0x54504432;	// [deprecated] point (X,Y)
	public static final int $3DPT = 0x33445054;	public static final int $TPD3 = 0x54504433;	// [deprecated] point (X,Y,Z)
	
	public static final int $RGB4 = 0x52474234;	public static final int $4BGR = 0x34424752;	// RGB 4-4-4
	public static final int $BGR4 = 0x42475234;	public static final int $4RGB = 0x34524742;	// BGR 4-4-4
	public static final int $RGB5 = 0x52474235;	public static final int $5BGR = 0x35424752;	// RGB 5-5-5
	public static final int $BGR5 = 0x42475235;	public static final int $5RGB = 0x35524742;	// BGR 5-5-5
	public static final int $RGB6 = 0x52474236;	public static final int $6BGR = 0x36424752;	// RGB 5-6-5
	public static final int $BGR6 = 0x42475236;	public static final int $6RGB = 0x36524742;	// BGR 5-6-5
	public static final int $RGBC = 0x52474243;	public static final int $CBGR = 0x43424752;	// RGB 8-8-8
	public static final int $BGRC = 0x42475243;	public static final int $CRGB = 0x43524742;	// BGR 8-8-8
	public static final int $XRGB = 0x58524742;	public static final int $BGRX = 0x42475258;	// XRGB 8-8-8-8
	public static final int $XBGR = 0x58424752;	public static final int $RGBX = 0x52474258;	// XBGR 8-8-8-8
	public static final int $ARGB = 0x41524742;	public static final int $BGRA = 0x42475241;	// ARGB 8-8-8-8
	public static final int $ABGR = 0x41424752;	public static final int $RGBA = 0x52474241;	// ABGR 8-8-8-8
	public static final int $RGBc = 0x52474263;	public static final int $cBGR = 0x63424752;	// RGB 16-16-16
	public static final int $BGRc = 0x42475263;	public static final int $cRGB = 0x63524742;	// BGR 16-16-16
	public static final int $R444 = 0x52343434;	public static final int $444R = 0x34343452;	// [deprecated] RGB 4-4-4
	public static final int $R555 = 0x52353535;	public static final int $555R = 0x35353552;	// [deprecated] RGB 5-5-5
	public static final int $R565 = 0x52353635;	public static final int $565R = 0x35363552;	// [deprecated] RGB 5-6-5
	public static final int $R888 = 0x52383838;	public static final int $888R = 0x38383852;	// [deprecated] RGB 8-8-8
	public static final int $RXXX = 0x52585858;	public static final int $XXXR = 0x58585852;	// [deprecated] RGB 16-16-16
	public static final int $44R4 = 0x34345234;	public static final int $4R44 = 0x34523434;	// [deprecated] BGR 4-4-4
	public static final int $55R5 = 0x35355235;	public static final int $5R55 = 0x35523535;	// [deprecated] BGR 5-5-5
	public static final int $56R5 = 0x35365235;	public static final int $5R65 = 0x35523635;	// [deprecated] BGR 5-6-5
	public static final int $88R8 = 0x38385238;	public static final int $8R88 = 0x38523838;	// [deprecated] BGR 8-8-8
	public static final int $XXRX = 0x58585258;	public static final int $XRXX = 0x58525858;	// [deprecated] BGR 16-16-16
	
	public static final int $ZCNT = 0x5A434E54;	public static final int $TNCZ = 0x544E435A;	// list count, zero-based (16-bit)
	public static final int $ZC__ = 0x5A430000;	public static final int $__CZ = 0x0000435A;	// list count, zero-based (by bit width)
	public static final int $OCNT = 0x4F434E54;	public static final int $TNCO = 0x544E434F;	// list count, one-based (16-bit)
	public static final int $OC__ = 0x4F430000;	public static final int $__CO = 0x0000434F;	// list count, one-based (by bit width)
	
	public static final int $LSTC = 0x4C535443;	public static final int $CTSL = 0x4354534C;	// list, with count
	public static final int $LSTZ = 0x4C53545A;	public static final int $ZTSL = 0x5A54534C;	// list, zero-terminated
	public static final int $LSTB = 0x4C535442;	public static final int $BTSL = 0x4254534C;	// list, EOF-terminated
	public static final int $LSTE = 0x4C535445;	public static final int $ETSL = 0x4554534C;	// list end
	
	public static final int $PCKB = 0x50434B42;	public static final int $BKCP = 0x424B4350;	// packed struct begin
	public static final int $PCKE = 0x50434B45;	public static final int $EKCP = 0x454B4350;	// packed struct end
	
	public static final int $CONB = 0x434F4E42;	public static final int $BNOC = 0x424E4F43;	// condition begin (if)
	public static final int $CONC = 0x434F4E43;	public static final int $CNOC = 0x434E4F43;	// condition continue (else if)
	public static final int $COND = 0x434F4E44;	public static final int $DNOC = 0x444E4F43;	// condition default (else)
	public static final int $CONE = 0x434F4E45;	public static final int $ENOC = 0x454E4F43;	// condition end (end if)
	
	public static final int $CASB = 0x43415342;	public static final int $BSAC = 0x42534143;	// case begin (switch)
	public static final int $CASC = 0x43415343;	public static final int $CSAC = 0x43534143;	// case continue (case)
	public static final int $CASD = 0x43415344;	public static final int $DSAC = 0x44534143;	// case default (default)
	public static final int $CASE = 0x43415345;	public static final int $ESAC = 0x45534143;	// case end (end switch)
	
	public static final int $SELB = 0x53454C42;	public static final int $BLES = 0x424C4553;	// select begin (switch)
	public static final int $SELC = 0x53454C43;	public static final int $CLES = 0x434C4553;	// select continue (case)
	public static final int $SELD = 0x53454C44;	public static final int $DLES = 0x444C4553;	// select default (default)
	public static final int $SELE = 0x53454C45;	public static final int $ELES = 0x454C4553;	// select end (end switch)
	
	public static final int $HDNG = 0x48444E47;	public static final int $GNDH = 0x474E4448;	// [deprecated] heading
	public static final int $HDG1 = 0x48444731;	public static final int $1GDH = 0x31474448;	// heading 1
	public static final int $HDG2 = 0x48444732;	public static final int $2GDH = 0x32474448;	// heading 2
	public static final int $HDG3 = 0x48444733;	public static final int $3GDH = 0x33474448;	// heading 3
	public static final int $HDG4 = 0x48444734;	public static final int $4GDH = 0x34474448;	// heading 4
	public static final int $HDG5 = 0x48444735;	public static final int $5GDH = 0x35474448;	// heading 5
	public static final int $HDG6 = 0x48444736;	public static final int $6GDH = 0x36474448;	// heading 6
	public static final int $PARA = 0x50415241;	public static final int $ARAP = 0x41524150;	// paragraph
	public static final int $LABL = 0x4C41424C;	public static final int $LBAL = 0x4C42414C;	// [deprecated] label
	public static final int $SEPA = 0x53455041;	public static final int $APES = 0x41504553;	// separator
	public static final int $COMM = 0x434F4D4D;	public static final int $MMOC = 0x4D4D4F43;	// comment
	public static final int $CMNT = 0x434D4E54;	public static final int $TNMC = 0x544E4D43;	// [deprecated] comment
	public static final int $META = 0x4D455441;	public static final int $ATEM = 0x4154454D;	// metadata
	public static final int $CSET = 0x43534554;	public static final int $TESC = 0x54455343;	// charset
	public static final int $TENC = 0x54454E43;	public static final int $CNET = 0x434E4554;	// [deprecated] charset
	public static final int $LINE = 0x4C494E45;	public static final int $ENIL = 0x454E494C;	// line ending
	
	public static final int $IF__ = 0x49462020;	public static final int $__FI = 0x20204649;	// [deprecated] condition begin (if)
	public static final int $IFTH = 0x49465448;	public static final int $HTFI = 0x48544649;	// [deprecated] condition begin (if then)
	public static final int $ELIF = 0x454C4946;	public static final int $FILE = 0x46494C45;	// [deprecated] condition continue (else if)
	public static final int $ELSE = 0x454C5345;	public static final int $ESLE = 0x45534C45;	// [deprecated] condition default (else)
	public static final int $ENIF = 0x454E4946;	public static final int $FINE = 0x46494E45;	// [deprecated] condition end (end if)
	public static final int $CSOF = 0x43534F46;	public static final int $FOSC = 0x464F5343;	// [deprecated] case begin (switch)
	public static final int $CSLB = 0x43534C42;	public static final int $BLSC = 0x424C5343;	// [deprecated] case continue (case)
	public static final int $CSDF = 0x43534446;	public static final int $FDSC = 0x46445343;	// [deprecated] case default (default)
	public static final int $ENCS = 0x454E4353;	public static final int $SCNE = 0x53434E45;	// [deprecated]	case end (end switch)
}
