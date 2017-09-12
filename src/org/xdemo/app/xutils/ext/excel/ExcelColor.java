package org.xdemo.app.xutils.ext.excel;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月21日 下午4:21:20
 */
public enum ExcelColor {

	AQUA((short) 0x31), AUTOMATIC((short) 0x40), BLACK((short) 0x8), BLUE((short) 0xc), BLUE_GREY((short) 0x36), BRIGHT_GREEN((short) 0xb), BROWN((short) 0x3c), CORAL((short) 0x1d), CORNFLOWER_BLUE((short) 0x18), DARK_BLUE((short) 0x12), DARK_GREEN((short) 0x3a), DARK_RED((short) 0x10), DARK_TEAL(
			(short) 0x38), DARK_YELLOW((short) 0x13), GOLD((short) 0x33), GREEN((short) 0x11), GREY_25_PERCENT((short) 0x16), GREY_40_PERCENT((short) 0x37), GREY_50_PERCENT((short) 0x17), GREY_80_PERCENT((short) 0x3f), INDIGO((short) 0x3e), LAVENDER((short) 0x2e), LEMON_CHIFFON((short) 0x1a), LIGHT_BLUE(
			(short) 0x30), LIGHT_CORNFLOWER_BLUE((short) 0x1f), LIGHT_GREEN((short) 0x2a), LIGHT_ORANGE((short) 0x34), LIGHT_YELLOW((short) 0x2b), MAROON((short) 0x19), OLIVE_GREEN((short) 0x3b), ORANGE((short) 0x35), PINK((short) 0xe), RED((short) 0xa), ROSE((short) 0x2d), ROYAL_BLUE((short) 0x39), SEA_GREEN(
			(short) 0x31), SKY_BLUE((short) 0x28), TAN((short) 0x2f), WHITE((short) 0x9), YELLOW((short) 0xd), TEAL((short) 0x15);

	private short index;

	private ExcelColor(short index) {
		this.index = index;
	}

	public short getIndex() {
		return index;
	}
}
