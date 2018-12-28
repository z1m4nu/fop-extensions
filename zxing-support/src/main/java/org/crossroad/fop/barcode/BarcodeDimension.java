package org.crossroad.fop.barcode;

import java.awt.geom.Point2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.util.UnitConv;

public class BarcodeDimension {

   
    private static final Pattern WIDTH_PATTERN = Pattern.compile("^(\\d+(?:.\\d+)?)(mm|pt|in|cm)?$");

    private static double getSizeInPt(String length) {
        Matcher matcher = WIDTH_PATTERN.matcher(length);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("error calculating width");
        }
        double size = Double.valueOf(matcher.group(1));
        double sizeInPt = 0;
        if (matcher.groupCount() > 1) {
            String measure = matcher.group(2);
            if ("mm".equals(measure)) {
                sizeInPt = UnitConv.convert(size + UnitConv.MM);
            } else if ("pt".equals(measure)) {
                sizeInPt = size;
            } else if ("in".equals(measure)) {
                sizeInPt = UnitConv.convert(size + UnitConv.INCH);
            } else if ("cm".equals(measure)) {
                sizeInPt = UnitConv.convert(size + UnitConv.CM);
            }
        } else {
            sizeInPt = size;
        }
        return sizeInPt;
    }

    private final double width;
    private final double height;
    public BarcodeDimension(Configuration configuration) {
        this.width = getSizeInPt(configuration.getAttribute(IKeywords.ATTR_WIDTH, IKeywords.DEFAULT_WIDTH));
        this.height = getSizeInPt(configuration.getAttribute(IKeywords.ATTR_HEIGHT, IKeywords.DEFAULT_WIDTH));
    }

    public ImageSize toImageSize(ImageContext context) {
        ImageSize size = new ImageSize();
        size.setSizeInMillipoints((int)width, (int)height);
        
        size.setResolution(context.getSourceResolution());
        size.calcPixelsFromSize();
        return size;
    }
    
    public int getWidthPixel()
    {
    	int w = (int)UnitConv.mpt2px(width, 72);
    	return w;
    }

    public int getHeightPixel()
    {
    	return (int)UnitConv.mpt2px(height, 72);
    }

    public double getHeight() {
		return height;
	}
    
    public double getWidth() {
		return width;
	}

    public Point2D toPoint2D() {
        return new Point2D.Double(this.width, this.height);
    }
}
