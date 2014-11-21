/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Isaac Brewer */

package geovista.common.ui;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import javax.swing.event.ChangeListener;

/**
 * Generally describe RangeSliderLegend in here.
 * 
 * 
 * @author IsaacBrewer (isaacbrewer@hotmail.com)
 */

/*
 * ====================================================================
 * Implementation of class VisualClassifierBeanInfo
 * ====================================================================
 */

public class RangeSliderLegendBeanInfo extends SimpleBeanInfo {
	private final static Class beanClass = RangeSliderLegend.class;
	private static String iconColor16x16Filename = "resources/RangeSliderLegendIconColor16.gif";
	private static String iconColor32x32Filename = "resources/RangeSliderLegendIconColor32.gif";
	private static String iconMono16x16Filename;
	private static String iconMono32x32Filename;

	/**
	 * Describes Editable Bean Properties
	 * 
	 * @return
	 */
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor createSlider = new PropertyDescriptor(
					"createSlider", beanClass);
			PropertyDescriptor maximum = new PropertyDescriptor("maximum",
					beanClass);
			PropertyDescriptor minimum = new PropertyDescriptor("minimum",
					beanClass);
			PropertyDescriptor minorTickSpacing = new PropertyDescriptor(
					"minorTickSpacing", beanClass);
			PropertyDescriptor majorTickSpacing = new PropertyDescriptor(
					"majorTickSpacing", beanClass);
			PropertyDescriptor units = new PropertyDescriptor("units",
					beanClass);
			PropertyDescriptor tabTitle = new PropertyDescriptor("tabTitle",
					beanClass);
			PropertyDescriptor title = new PropertyDescriptor("title",
					beanClass);
			PropertyDescriptor background = new PropertyDescriptor(
					"background", beanClass);
			PropertyDescriptor labelPanelColor = new PropertyDescriptor(
					"labelPanelColor", beanClass);
			PropertyDescriptor sliderColor = new PropertyDescriptor(
					"sliderColor", beanClass);
			PropertyDescriptor fontColor = new PropertyDescriptor("fontColor",
					beanClass);

			createSlider.setBound(true);
			maximum.setBound(true);
			minimum.setBound(true);
			minorTickSpacing.setBound(true);
			majorTickSpacing.setBound(true);
			units.setBound(true);
			tabTitle.setBound(true);
			title.setBound(true);
			background.setBound(true);
			labelPanelColor.setBound(true);
			sliderColor.setBound(true);
			fontColor.setBound(true);

			PropertyDescriptor[] rv = { createSlider, minorTickSpacing,
					majorTickSpacing, maximum, minimum, units, tabTitle, title,
					background, labelPanelColor, sliderColor, fontColor };
			return rv;
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns EventSetDescriptors associated with a RangeSliderLegend bean.
	 * 
	 * <PRE>
	 * </PRE>
	 * 
	 * @return EventSetDescriptor[]
	 */
	@Override
	public EventSetDescriptor[] getEventSetDescriptors() {
		try {
			String[] lmnames = { "stateChanged" };
			EventSetDescriptor stateChanged = new EventSetDescriptor(beanClass, // sourceClass
					"SelectedAction", // eventSetName
					ChangeListener.class, lmnames, // listenerMethodNames
					"addChangeListener", "removeChangeListener");
			EventSetDescriptor[] rv = { stateChanged };
			return rv;
		} catch (IntrospectionException ie) {
			ie.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a icons of the specified size.
	 * 
	 * <PRE>
	 * </PRE>
	 * 
	 * @param iconKind
	 *            an ID (ICON_COLOR_16x16 or ICON_COLOR_32x32) indicating the
	 *            size of the icon.
	 * @return Image
	 */
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
}
