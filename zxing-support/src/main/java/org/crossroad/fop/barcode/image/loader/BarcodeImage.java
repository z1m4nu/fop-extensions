package org.crossroad.fop.barcode.image.loader;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.AbstractImage;
import org.crossroad.fop.barcode.IKeywords;

import com.google.zxing.common.BitMatrix;

public class BarcodeImage extends AbstractImage {
	private BitMatrix matrix = null;

	public BarcodeImage(ImageInfo info, BitMatrix matrix) {
		super(info);
		this.matrix = matrix;
	}


	public ImageFlavor getFlavor() {
		return IKeywords.BARCODE_IMAGE_FLAVOR;
	}

	public boolean isCacheable() {
		return true;
	}


	public BitMatrix getMatrix() {
		return matrix;
	}
	
}
