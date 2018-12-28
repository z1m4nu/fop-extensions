/**
 * 
 */
package org.crossroad.fop.barcode;

import org.apache.xmlgraphics.image.loader.ImageFlavor;

/**
 * @author e.soden
 *
 */
public interface IKeywords {
	public static final String NAME_HINTS = "hints";
	public static final String NAME_HINT = "hint";
	public static final String NAME_HINT_DIMENSION = "dimension";
	public static final String NAME_MESSAGE = "message";

	public static final String NAME_SIZE = "size";
			
	public static final String ATTR_MIN_COLS = "min-cols";
	public static final String ATTR_MAX_COLS = "max-cols";
	public static final String ATTR_MIN_ROWS = "min-rows";
	public static final String ATTR_MAX_ROWS = "max-rows";
	
	public static final String ATTR_NAME = "name";
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_MESSAGE = "message";
	public static final String ATTR_VALUE = "value";
	public static final String ATTR_WIDTH = "width";
	public static final String ATTR_HEIGHT = "height";
	public static final String ATTR_ORIENTATION ="orientation";
	public static final String ATTR_LABELED ="labeled";
	public static final String ATTR_VPOS ="vpos";
	public static final String ATTR_HPOS ="hpos";
	
	
	
	public static final String DEFAULT_ERROR_CORRECTION_TYPE = "L";
    public static final String DEFAULT_CHARACTER_SET = "ISO-8859-1";
    public static final String DEFAULT_WIDTH = "50mm";
    
    public static final ImageFlavor BARCODE_IMAGE_FLAVOR = new ImageFlavor("crossroad");
    public static final String MIME_TYPE = "application/crossroad+xml";
    public static final String NAMESPACE = "http://crossroad.org/fop/barcode/";
	public static final int DEFAULT_ORIENTATION = 0;
	
	

	public static final String DATA_CONFIG = "config";
	public static final String DATA_MESSAGE = "message";
	public static final String DATA_BARCODE = "barcode";
	
}
