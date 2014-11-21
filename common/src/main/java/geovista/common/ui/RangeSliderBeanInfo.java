package geovista.common.ui;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * 
 */

public class RangeSliderBeanInfo extends SimpleBeanInfo {
    Class beanClass = RangeSlider.class;
    static String iconColor16x16Filename = "resources/RangeSlider/rangeslider16.gif";
    static String iconColor32x32Filename = "resources/RangeSlider/rangeslider32.gif";
    static String iconMono16x16Filename;
    static String iconMono32x32Filename;

    public RangeSliderBeanInfo() {
    }
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _fontColor = new PropertyDescriptor("fontColor", beanClass, "getFontColor", "setFontColor");
            PropertyDescriptor _labelPanelColor = new PropertyDescriptor("labelPanelColor", beanClass, "getLabelPanelColor", "setLabelPanelColor");
            PropertyDescriptor _valueAdjusting = new PropertyDescriptor("valueAdjusting", beanClass, "getValueAdjusting", "setValueAdjusting");
			PropertyDescriptor _majorTickSpacing = new PropertyDescriptor("majorTickSpacing", beanClass, "getMajorTickSpacing", "setMajorTickSpacing");
            PropertyDescriptor _maximum = new PropertyDescriptor("maximum", beanClass, "getMaximum", "setMaximum");
            PropertyDescriptor _maxValue = new PropertyDescriptor("maxValue", beanClass, null, "setMaxValue");
            PropertyDescriptor _minimum = new PropertyDescriptor("minimum", beanClass, "getMinimum", "setMinimum");
            PropertyDescriptor _minorTickSpacing = new PropertyDescriptor("minorTickSpacing", beanClass, "getMinorTickSpacing", "setMinorTickSpacing");
            PropertyDescriptor _minValue = new PropertyDescriptor("minValue", beanClass, null, "setMinValue");
            PropertyDescriptor _name = new PropertyDescriptor("name", beanClass, "getName", null);
            PropertyDescriptor _numberOfThumbs = new PropertyDescriptor("numberOfThumbs", beanClass, "getNumberOfThumbs", "setNumberOfThumbs");
            PropertyDescriptor _sliderColor = new PropertyDescriptor("sliderColor", beanClass, "getSliderColor", "setSliderColor");
            PropertyDescriptor _title = new PropertyDescriptor("title", beanClass, "getTitle", "setTitle");
            PropertyDescriptor _value = new PropertyDescriptor("value", beanClass, "getValue", null);
            PropertyDescriptor[] pds = new PropertyDescriptor[] {
	            _fontColor,
	            _labelPanelColor,
				_valueAdjusting,
	            _majorTickSpacing,
	            _maximum,
	            _maxValue,
	            _minimum,
	            _minorTickSpacing,
	            _minValue,
	            _name,
	            _numberOfThumbs,
	            _sliderColor,
	            _title,
	            _value};
            return pds;

















}
        catch(IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
        case BeanInfo.ICON_COLOR_16x16:
              return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
        case BeanInfo.ICON_COLOR_32x32:
              return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
        case BeanInfo.ICON_MONO_16x16:
              return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
        case BeanInfo.ICON_MONO_32x32:
              return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
                                }
        return null;
    }
}