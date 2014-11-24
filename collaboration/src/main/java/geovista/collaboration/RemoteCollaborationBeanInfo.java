/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Authors: Frank Hardisty and Linna Li */

package geovista.collaboration;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class RemoteCollaborationBeanInfo extends SimpleBeanInfo {
	Class beanClass = RemoteCollaboration.class;
	static String iconColor16x16Filename = "resources/CollaborationIcon16.GIF";
	static String iconColor32x32Filename = "resources/CollaborationIcon32.GIF";
	String iconMono16x16Filename;
	String iconMono32x32Filename;

	public RemoteCollaborationBeanInfo() {
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		PropertyDescriptor[] pds = new PropertyDescriptor[] {};
		return pds;
	}

	@Override
	public Image getIcon(int iconKind) {
		switch (iconKind) {
		case BeanInfo.ICON_COLOR_16x16:
			return ((iconColor16x16Filename != null)
					? loadImage(iconColor16x16Filename) : null);

		case BeanInfo.ICON_COLOR_32x32:
			return ((iconColor32x32Filename != null)
					? loadImage(iconColor32x32Filename) : null);

		case BeanInfo.ICON_MONO_16x16:
			return ((iconMono16x16Filename != null)
					? loadImage(iconMono16x16Filename) : null);

		case BeanInfo.ICON_MONO_32x32:
			return ((iconMono32x32Filename != null)
					? loadImage(iconMono32x32Filename) : null);
		}

		return null;
	}
}
