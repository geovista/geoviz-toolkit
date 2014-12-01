/*------------------------------------------------------------------------------
 * GeoVISTA Center, Penn State Geography Deptartment*
 * Copyright (c), 2002-5, GeoVISTA Center and Frank Hardisty
 *
 * Original Authors: Bonan Li, Frank Hardisty
 * $Author: hardisty $
 *
 * $Date: 2005/11/04 19:19:26 $
 *
 * $Id: Glyph.java,v 1.2 2005/11/04 19:19:26 hardisty Exp $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 A symbol is a shape with additional painting information. It allows a calling
 class to call symbol.draw(), and expect that the background color etc. will
 be set appropriately.
 ------------------------------------------------------------------------------*/
package geovista.symbolization.glyph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public interface Glyph {

	// protected Point location;//location of the center of this shape in user
	// space
	// protected Shape glyph;

	// protected boolean visible;
	// protected Color color;//symbol fill color
	// protected Color borderColor;
	// protected int borderWidth;
	// protected float rotation;

	// public Glyph() {
	//
	// this.visible = true;
	// this.color = Color.GRAY ;
	// this.borderColor = Color.BLACK ;
	// this.borderWidth = 1;
	// this.rotation = 0;
	// }
	public void setFillColor(Color fillColor);

	public Color getFillColor();

	public void setLocation(Point location);

	// public void setLocation(int x, int y);

	// public void setAlpha(int a) {
	// int alpha = this.color.getAlpha();
	// if (alpha == a){
	// return;
	// }
	//
	// int r = this.color.getRed();
	// int b = this.color.getBlue();
	// int g = this.color.getGreen();
	// this.color = new Color(r,g,b,a);
	//
	// }
	// public int getAlpha() {
	// return this.color.getAlpha();
	// }
	//
	// public void setColor(int r, int g, int b) {
	// this.color = new Color(r, g, b);
	// }
	// public void setColor(int r, int g, int b, int alpha) {
	// this.color = new Color(r,g,b,alpha);
	// }
	//
	// public Color getColor() {//returns a copy
	// int r = this.color.getRed();
	// int g = this.color.getGreen() ;
	// int b = this.color.getBlue() ;
	// int a = this.getAlpha() ;
	// Color c = new Color(r,g,b,a);
	// return c;
	// }
	//
	// public void setborderColor(int r, int g, int b) {
	// this.borderColor = new Color(r,g,b);
	// }
	//
	// public void setColor(Color c) {
	// this.color = c;
	// }
	//
	// public void setBorderColor(Color c) {
	// this.borderColor = c;
	// }
	//
	// public Color getBorderColor() {
	// int r = this.borderColor .getRed() ;
	// int g = this.borderColor .getGreen() ;
	// int b = this.borderColor .getBlue() ;
	// Color c = new Color(r,g,b);
	// return c;
	// }
	//
	// public void setBorderWidth(int w) {
	// this.borderWidth = w;
	// }
	// public int getBorderWidth() {
	// return this.borderWidth ;
	//
	// }

	public void draw(Graphics2D g2);

	public void setTargetArea(Rectangle rect);

	// {
	// g2.setColor(this.color);
	// g2.fill(this.glyph);
	// g2.setColor(this.borderColor);
	// g2.draw(this.glyph);
	// }

}
