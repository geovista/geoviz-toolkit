/*------------------------------------------------------------------------------
*
* Original Authors: Frank Hardisty, Bonan Li
* $Author: hardisty $
*
* $Date: 2005/07/17 19:53:06 $
*
* $Id: GlyphListener.java,v 1.1 2005/07/17 19:53:06 hardisty Exp $
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
 ------------------------------------------------------------------------------*/
package geovista.symbolization.glyph;

import java.util.EventListener;


public interface GlyphListener extends EventListener {
  public void glyphChanged(GlyphEvent e);
}
