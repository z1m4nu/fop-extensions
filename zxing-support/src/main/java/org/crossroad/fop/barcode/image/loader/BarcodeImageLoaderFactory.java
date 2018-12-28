package org.crossroad.fop.barcode.image.loader;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoaderFactory;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;
import org.crossroad.fop.barcode.IKeywords;

public class BarcodeImageLoaderFactory extends AbstractImageLoaderFactory {

    private static final ImageFlavor[] SUPPORTED_FLAVORS = {
    		IKeywords.BARCODE_IMAGE_FLAVOR
    };
    
    private static final String[] SUPPORTED_MIME_TYPES = {
        IKeywords.MIME_TYPE
    };

    public ImageFlavor[] getSupportedFlavors(String mime) {
        return SUPPORTED_FLAVORS;
    }

    public String[] getSupportedMIMETypes() {
        return SUPPORTED_MIME_TYPES;
    }

    public boolean isAvailable() {
        return true;
    }

    public ImageLoader newImageLoader(ImageFlavor targetFlavor) {
        return new BarcodeImageLoader(targetFlavor);
    }
}
