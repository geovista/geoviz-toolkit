/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.*/

package geovista.common.color;

/**
 * The <CODE>Palette</CODE> interface.
 * 
 * @author Chris Weaver
 */
public interface OblivionPalette {

	int NONE = 0;
	int POOR = 1;
	int IFFY = 2;
	int GOOD = 3;

	int COLORBLIND = 0;
	int PHOTOCOPY = 1;
	int PROJECTOR = 2;
	int LCD = 3;
	int CRT = 4;
	int PRINTER = 5;

	String[] PURPOSE = new String[] { "ColorBlind", "PhotoCopy",
			"LCD Projector", "LCD", "CRT", "Color Printing", };

	String getName();
}
