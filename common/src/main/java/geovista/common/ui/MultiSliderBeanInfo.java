/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Masahiro Takatsuka*/
package geovista.common.ui;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/*====================================================================
 Implementation of class MultiSliderBeanInfo              
 ====================================================================*/
/**
 * MultiSliderBeanInfo provides information on the MtltiSlider bean.
 * 
 * 
 * @author Masahiro Takatsuka (jh9gpz@yahoo.com)
 * @see SimpleBeanInfo
 */

public class MultiSliderBeanInfo extends SimpleBeanInfo {
	private static final Class beanClass = MultiSlider.class;

	private static String iconColor16x16Filename = "resources/MultiSlider/IconClor16.gif";
	private static String iconColor32x32Filename = "resources/MultiSlider/IconColor32.gif";
	private static String iconMono16x16Filename;
	private static String iconMono32x32Filename;

	@Override
	public Image getIcon(int iconKind) {
		switch (iconKind) {
		case BeanInfo.ICON_COLOR_16x16:
			return iconColor16x16Filename != null
					? loadImage(iconColor16x16Filename) : null;
		case BeanInfo.ICON_COLOR_32x32:
			return iconColor32x32Filename != null
					? loadImage(iconColor32x32Filename) : null;
		case BeanInfo.ICON_MONO_16x16:
			return iconMono16x16Filename != null
					? loadImage(iconMono16x16Filename) : null;
		case BeanInfo.ICON_MONO_32x32:
			return iconMono32x32Filename != null
					? loadImage(iconMono32x32Filename) : null;
		}
		return null;
	}

	public MultiSliderBeanInfo() {
	}

	@Override
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor bd = new BeanDescriptor(beanClass);
		bd.setPreferred(true);
		bd
				.setShortDescription("A component that supports selecting a integer value from a range.");
		bd.setValue("hidden-state", Boolean.TRUE);
		bd.setValue("helpSetName",
				"edu/psu/geovista/ui/slider/resources/MultiSlider/jhelpset.hs");
		return bd;
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			return (new PropertyDescriptor[] {
					new PropertyDescriptor("labelTable", beanClass),
					new PropertyDescriptor("minorTickSpacing", beanClass),
					new PropertyDescriptor("visualUpdate", beanClass),
					new PropertyDescriptor("majorTickSpacing", beanClass),
					new PropertyDescriptor("orientation", beanClass),
					new PropertyDescriptor("model", beanClass),
					new PropertyDescriptor("paintLabels", beanClass),
					new PropertyDescriptor("paintTrack", beanClass),
					new PropertyDescriptor("extent", beanClass),
					new PropertyDescriptor("inverted", beanClass),
					new PropertyDescriptor("minimum", beanClass),
					new PropertyDescriptor("maximum", beanClass),
					new PropertyDescriptor("value", beanClass),
					new PropertyDescriptor("paintTicks", beanClass),
					new PropertyDescriptor("snapToTicks", beanClass),
					new PropertyDescriptor("numberOfThumbs", beanClass),
					new PropertyDescriptor("bounded", beanClass) });
		} catch (Exception e) {
			return new PropertyDescriptor[] {};
		}
	}
}
