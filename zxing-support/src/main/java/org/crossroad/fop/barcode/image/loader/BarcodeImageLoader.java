package org.crossroad.fop.barcode.image.loader;

import java.io.IOException;
import java.util.Map;

import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoader;

public class BarcodeImageLoader extends AbstractImageLoader {

    private final ImageFlavor targetFlavor;

    public BarcodeImageLoader(ImageFlavor targetFlavor) {
        this.targetFlavor = targetFlavor;
    }

    public ImageFlavor getTargetFlavor() {
        return this.targetFlavor;
    }

    public Image loadImage(ImageInfo info, @SuppressWarnings("rawtypes") Map hints, ImageSessionContext session) throws ImageException, IOException {
        return (BarcodeImage) info.getOriginalImage();
    }
}
