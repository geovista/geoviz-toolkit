/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.*/
package geovista.common.color;

import java.awt.Color;
import java.util.HashMap;

/**
 * The <CODE>OblivionBivariatePalette</CODE> class.
 * 
 * @author Chris Weaver
 * 
 */
// Hacked together. Clean me up.
public final class OblivionBivariatePalette implements Palette2D {

	public static final HashMap MAP = new HashMap();

	private final int type;
	private final String name;
	private final int[][] colors;

	public OblivionBivariatePalette(String name, int type, int[][] colors) {
		this.name = name;
		this.type = type;

		this.colors = colors;

		MAP.put(name, this);
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	// Definitely wrong
	public Color[][] getColors(int hsize, int vsize) {
		Color[][] c = new Color[5][5];

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				c[i][j] = new Color(colors[i][j]);
			}
		}

		return c;
	}

	public Color getColor(int index1, int index2) {
		if ((index1 < 0) || (index1 >= 5)) {
			return null;
		}

		if ((index2 < 0) || (index2 >= 5)) {
			return null;
		}

		return new Color(colors[index1][index2]);
	}

	public int getMaxLength() {
		return 5;
	}

	// Starting with red, and ending with purple
	// vertex=141, divider=7500, lightness range=26~95,
	// startinghue=25, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette QUA_SEQ_BELL_CURVE1 = new OblivionBivariatePalette(
			"quaseqbellcurve1", QUALITATIVE_SEQUENTIAL, new int[][] {
					{ 0xffd5d1, 0xfff2a0, 0x94ffe0, 0x81ffff, 0xffe0ff },
					{ 0xff9494, 0xd2c050, 0x00d8a8, 0x00d1ff, 0xe1a7ff },
					{ 0xff5360, 0xa19300, 0x00ac77, 0x00a7ff, 0xb673ef },
					{ 0xda002f, 0x6e6900, 0x008048, 0x007fe7, 0x8a3cd2 },
					{ 0xb40008, 0x3d4200, 0x00541b, 0x0059d3, 0x5900ba } });

	// Starting with red, and ending with purple
	// vertex=164, divider=4913, lightness range=23~87,
	// startinghue=292, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette QUA_SEQ_BELL_CURVE2 = new OblivionBivariatePalette(
			"quaseqbellcurve2", QUALITATIVE_SEQUENTIAL, new int[][] {
					{ 0xc9d2ff, 0xffafd5, 0xffcf73, 0x87f1a0, 0x00f3ff },
					{ 0x94a5ff, 0xff78a9, 0xe4a136, 0x44c56d, 0x00c8f8 },
					{ 0x577bfb, 0xf2377e, 0xb87600, 0x009b3b, 0x009fd5 },
					{ 0x0053de, 0xcc0057, 0x8b4e00, 0x007200, 0x0076b4 },
					{ 0x0031c7, 0xa80033, 0x612600, 0x004a00, 0x004f98 } });

	// Starting with red, and ending with purple
	// vertex=141, divider=7500, lightness range=26~95,
	// startinghue=25, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette SEQ_QUA_BELL_CURVE1 = new OblivionBivariatePalette(
			"seqquabellcurve1", SEQUENTIAL_QUALITATIVE, new int[][] {
					{ 0xffd5d1, 0xff9494, 0xff5360, 0xda002f, 0xb40008 },
					{ 0xfff2a0, 0xd2c050, 0xa19300, 0x6e6900, 0x3d4200 },
					{ 0x94ffe0, 0x00d8a8, 0x00ac77, 0x008048, 0x00541b },
					{ 0x81ffff, 0x00d1ff, 0x00a7ff, 0x007fe7, 0x0059d3 },
					{ 0xffe0ff, 0xe1a7ff, 0xb673ef, 0x8a3cd2, 0x5900ba } });

	// Starting with red, and ending with purple
	// vertex=164, divider=4913, lightness range=23~87,
	// startinghue=292, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette SEQ_QUA_BELL_CURVE2 = new OblivionBivariatePalette(
			"seqquabellcurve2", SEQUENTIAL_QUALITATIVE, new int[][] {
					{ 0xc9d2ff, 0x94a5ff, 0x577bfb, 0x0053de, 0x0031c7 },
					{ 0xffafd5, 0xff78a9, 0xf2377e, 0xcc0057, 0xa80033 },
					{ 0xffcf73, 0xe4a136, 0xb87600, 0x8b4e00, 0x612600 },
					{ 0x87f1a0, 0x44c56d, 0x009b3b, 0x007200, 0x004a00 },
					{ 0x00f3ff, 0x00c8f8, 0x009fd5, 0x0076b4, 0x004f98 } });

	// Starting with red, and ending with blue
	// cone height=130, cone radius=120, lightness range=35~97,
	// startinghue=10, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette QUA_SEQ_CONE1 = new OblivionBivariatePalette(
			"quaseqcone1", QUALITATIVE_SEQUENTIAL, new int[][] {
					{ 0xffe2ee, 0xfff2bd, 0xc7ffdc, 0xa6ffff, 0xf9f0ff },
					{ 0xffa8bd, 0xefc375, 0x7fdda3, 0x00ddff, 0xcac0ff },
					{ 0xff7091, 0xc99931, 0x27b770, 0x00b7ec, 0x9c94ff },
					{ 0xe42265, 0x9f6f00, 0x008f3c, 0x0090d1, 0x6568e8 },
					{ 0xc40040, 0x754a00, 0x006905, 0x006cb9, 0x0342d2 } });

	// Starting with red, and ending with blue
	// cone height=130, cone radius=120, lightness range=35~97,
	// startinghue=10, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette SEQ_QUA_CONE1 = new OblivionBivariatePalette(
			"quaseqcone1", QUALITATIVE_SEQUENTIAL, new int[][] {
					{ 0xffe2ee, 0xffa8bd, 0xff7091, 0xe42265, 0xc40040 },
					{ 0xfff2bd, 0xefc375, 0xc99931, 0x9f6f00, 0x754a00 },
					{ 0xc7ffdc, 0x7fdda3, 0x27b770, 0x008f3c, 0x006905 },
					{ 0xa6ffff, 0x00ddff, 0x00b7ec, 0x0090d1, 0x006cb9 },
					{ 0xf9f0ff, 0xcac0ff, 0x9c94ff, 0x6568e8, 0x0342d2 } });

	// meshspan=135, vertex=100, divider=7500,
	// startinghue=100, deviation a-b=0, deviationy=2
	public static final OblivionBivariatePalette DIV_DIV_BELL_CURVE1 = new OblivionBivariatePalette(
			"divdivbellcurve1", DIVERGING_DIVERGING, new int[][] {
					{ 0x008adc, 0x00b3d9, 0x00c4ba, 0x00b27a, 0x00881d },
					{ 0x00a9ff, 0x66e1ff, 0xa7faf3, 0xa3e2a8, 0x73a93d },
					{ 0x73abff, 0xd8eaff, 0xffffff, 0xf5ecb7, 0xb8ae46 },
					{ 0xac87f1, 0xfbbffd, 0xffd8e3, 0xffc299, 0xcf8d2d },
					{ 0xb340bd, 0xed6dba, 0xff819c, 0xf5735d, 0xc24e00 } });

	// meshspan=157, vertex=95, divider=8435,
	// startinghue=130, deviation a-b=0, deviationy=10
	public static final OblivionBivariatePalette DIV_DIV_BELL_CURVE2 = new OblivionBivariatePalette(
			"divdivbellcurve2", DIVERGING_DIVERGING, new int[][] {
					{ 0x007fff, 0x00acff, 0x00c4f2, 0x00b6b4, 0x008d61 },
					{ 0x4297ff, 0x8cdcff, 0x99ffff, 0x64ebcd, 0x00b264 },
					{ 0xc693ff, 0xffe5ff, 0xffffff, 0xdafac1, 0x79bb4e },
					{ 0xee65cc, 0xffb5de, 0xffdeca, 0xf7d081, 0x9c9f00 },
					{ 0xe80081, 0xff597f, 0xff8364, 0xdd831a, 0x946a00 } });

	// top angle=90, shape index=80, lightness range=46~100,
	// startinghue=138, deviation a-b=0, deviationy=4
	public static final OblivionBivariatePalette DIV_SEQ_ELLIPSE_DOWN1 = new OblivionBivariatePalette(
			"divseqellipsedown1", DIVERGING_SEQUENTIAL, new int[][] {
					{ 0xffb6ff, 0xffefff, 0xffffff, 0xdbffce, 0x79fe6d },
					{ 0xfb9eff, 0xfcceff, 0xe6e6e6, 0xbdebb2, 0x6bdb60 },
					{ 0xd085ee, 0xd1acde, 0xbfbfbf, 0x9ec395, 0x5cb653 },
					{ 0xaa6ec2, 0xaa8db4, 0x9c9c9c, 0x829f7b, 0x4f9547 },
					{ 0x825693, 0x826d89, 0x777777, 0x657a5f, 0x407339 } });

	// top angle=90, shape index=80, lightness range=46~100,
	// startinghue=138, deviation a-b=0, deviationy=4
	public static final OblivionBivariatePalette SEQ_DIV_ELLIPSE_DOWN1 = new OblivionBivariatePalette(
			"seqdivellipsedown1", SEQUENTIAL_DIVERGING, new int[][] {
					{ 0xffb6ff, 0xfb9eff, 0xd085ee, 0xaa6ec2, 0x825693 },
					{ 0xffefff, 0xfcceff, 0xd1acde, 0xaa8db4, 0x826d89 },
					{ 0xffffff, 0xe6e6e6, 0xbfbfbf, 0x9c9c9c, 0x777777 },
					{ 0xdbffce, 0xbdebb2, 0x9ec395, 0x829f7b, 0x657a5f },
					{ 0x79fe6d, 0x6bdb60, 0x5cb653, 0x4f9547, 0x407339 } });

	// top angle=109, radius=75, lightness range=50~95,
	// startinghue=230, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette DIV_SEQ_TRAPEZOID1 = new OblivionBivariatePalette(
			"divseqtrapezoid1", DIVERGING_SEQUENTIAL, new int[][] {
					{ 0xffb24f, 0xffcf93, 0xf1f1f1, 0xbefaff, 0x00ffff },
					{ 0xff9234, 0xffaf77, 0xcfcfcf, 0x9fd8ec, 0x00e7ff },
					{ 0xff7619, 0xff935f, 0xb1b1b1, 0x83b9cc, 0x00c7ff },
					{ 0xff5a00, 0xe07747, 0x949494, 0x689bad, 0x00a8e1 },
					{ 0xdc3d00, 0xbc5c30, 0x777777, 0x4d7e8f, 0x008abf } });

	// top angle=109, radius=75, lightness range=50~95,
	// startinghue=230, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette SEQ_DIV_TRAPEZOID1 = new OblivionBivariatePalette(
			"seqdivtrapezoid1", SEQUENTIAL_DIVERGING, new int[][] {
					{ 0xffb24f, 0xff9234, 0xff7619, 0xff5a00, 0xdc3d00 },
					{ 0xffcf93, 0xffaf77, 0xff935f, 0xe07747, 0xbc5c30 },
					{ 0xf1f1f1, 0xcfcfcf, 0xb1b1b1, 0x949494, 0x777777 },
					{ 0xbefaff, 0x9fd8ec, 0x83b9cc, 0x689bad, 0x4d7e8f },
					{ 0x00ffff, 0x00e7ff, 0x00c7ff, 0x00a8e1, 0x008abf } });

	// top angle=150, saturation range=120, lightness range=35~100,
	// startinghue=0, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette DIV_SEQ_GRID1 = new OblivionBivariatePalette(
			"divseqgrid1", DIVERGING_SEQUENTIAL, new int[][] {
					{ 0x00eed0, 0xa1f9e7, 0xffffff, 0xffd4ea, 0xffa2d4 },
					{ 0x00bda2, 0x72c8b8, 0xcfcfcf, 0xeea5ba, 0xff72a6 },
					{ 0x009178, 0x459d8d, 0xa4a4a4, 0xc07a8f, 0xd2447c },
					{ 0x006651, 0x0e7365, 0x7a7a7a, 0x945267, 0xa30655 },
					{ 0x003e2d, 0x004b3f, 0x535353, 0x692c41, 0x740031 } });

	// top angle=150, saturation range=120, lightness range=35~100,
	// startinghue=0, deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette SEQ_DIV_GRID1 = new OblivionBivariatePalette(
			"seqdivgrid1", SEQUENTIAL_DIVERGING, new int[][] {
					{ 0x00eed0, 0x00bda2, 0x009178, 0x006651, 0x003e2d },
					{ 0xa1f9e7, 0x72c8b8, 0x459d8d, 0x0e7365, 0x004b3f },
					{ 0xffffff, 0xcfcfcf, 0xa4a4a4, 0x7a7a7a, 0x535353 },
					{ 0xffd4ea, 0xeea5ba, 0xc07a8f, 0x945267, 0x692c41 },
					{ 0xffa2d4, 0xff72a6, 0xd2447c, 0xa30655, 0x740031 } });

	// Gray axis, red on one wing, and green on the other wing
	// Top angle=137, lightness range=6~100, startinghue=356
	public static final OblivionBivariatePalette SEQ_SEQ_GRAY_DIAMOND1 = new OblivionBivariatePalette(
			"seqseqgraydiamond1", SEQUENTIAL_SEQUENTIAL, new int[][] {
					{ 0xe6e6e6, 0xf4bccf, 0xfc8eb8, 0xff5aa5, 0xff008f },
					{ 0x99d8c7, 0xb1b1b1, 0xbe899b, 0xc75e88, 0xc81473 },
					{ 0x23c7aa, 0x67a394, 0x7f7f7f, 0x8d5b6d, 0x912b59 },
					{ 0x00b48e, 0x009379, 0x367264, 0x535353, 0x5c2e40 },
					{ 0x009f73, 0x00805e, 0x00614b, 0x00463a, 0x282828 } });

	// Gray axis, yellow-orange on one wing, and blue on the other wing
	// Top angle=129, lightness range=2~98, startinghue=53,
	// deviation a-b=12, deviationy=14
	public static final OblivionBivariatePalette SEQ_SEQ_GRAY_DIAMOND2 = new OblivionBivariatePalette(
			"seqseqgraydiamond2", SEQUENTIAL_SEQUENTIAL, new int[][] {
					{ 0xfffff4, 0xdbeef6, 0x8ddbf8, 0x00c7f9, 0x00b3fa },
					{ 0xffdcbb, 0xe5cbbe, 0xa6b8c0, 0x56a6c2, 0x0093c3 },
					{ 0xffb884, 0xe6a788, 0xaf978b, 0x74858d, 0x12748e },
					{ 0xff934d, 0xe08453, 0xae7558, 0x7c665b, 0x45565d },
					{ 0xff6c02, 0xd5601b, 0xa65326, 0x79462c, 0x4d392f } });

	// Near-gray axis, purple on one wing, and green-cyan on the other wing
	// Top angle=141, tilt angle=5, lightness range=18~100, startinghue=125,
	// deviation a-b=0, deviationy=0
	public static final OblivionBivariatePalette SEQ_SEQ_NONGRAY_DIAMOND1 = new OblivionBivariatePalette(
			"seqseqnongraydiamond1", SEQUENTIAL_SEQUENTIAL, new int[][] {
					{ 0xf9e3df, 0xe9c6ec, 0xd6a8f8, 0xc18bff, 0xa76dff },
					{ 0xd5d3a6, 0xc7b7b3, 0xb89ac0, 0xa67ecb, 0x8f61d5 },
					{ 0xaec36d, 0xa5a77c, 0x988c89, 0x897195, 0x77569f },
					{ 0x85b22d, 0x7f9745, 0x777d55, 0x6d6362, 0x5d4a6c },
					{ 0x55a100, 0x568700, 0x536e1e, 0x4c5531, 0x423d3d } });
}

// ******************************************************************************
