/**
 * 
 */
package org.crossroad.fop.barcode;

import com.google.zxing.BarcodeFormat;

/**
 * @author e.soden
 *
 */
public final class Utils {

	/**
	 * 
	 */
	private Utils() {
		// TODO Auto-generated constructor stub
	}

	public static BarcodeFormat getFormat(String text) {

		BarcodeFormat format = null;

		if (BarcodeFormat.AZTEC.name().equalsIgnoreCase(text)) {
			format = BarcodeFormat.AZTEC;
		} else if (BarcodeFormat.CODABAR.name().equalsIgnoreCase(text)) {
			format = BarcodeFormat.CODABAR;
		} else if (BarcodeFormat.CODE_128.name().equalsIgnoreCase(text) || "code128".equalsIgnoreCase(text)) {
			format = BarcodeFormat.CODE_128;
		} else if (BarcodeFormat.CODE_39.name().equalsIgnoreCase(text) || "code39".equalsIgnoreCase(text)) {
			format = BarcodeFormat.CODE_39;
		} else if (BarcodeFormat.CODE_93.name().equalsIgnoreCase(text) || "code93".equalsIgnoreCase(text)) {
			format = BarcodeFormat.CODE_93;
		} else if (BarcodeFormat.DATA_MATRIX.name().equalsIgnoreCase(text) || "matrix".equalsIgnoreCase(text)
				|| "datamatrix".equalsIgnoreCase(text)) {
			format = BarcodeFormat.DATA_MATRIX;
		} else if (BarcodeFormat.EAN_13.name().equalsIgnoreCase(text) || "ean13".equalsIgnoreCase(text)
				|| "ean-13".equalsIgnoreCase(text)) {
			format = BarcodeFormat.EAN_13;
		} else if (BarcodeFormat.EAN_8.name().equalsIgnoreCase(text) || "ean8".equalsIgnoreCase(text)
				|| "ean-8".equalsIgnoreCase(text)) {
			format = BarcodeFormat.EAN_8;
		} else if (BarcodeFormat.ITF.name().equalsIgnoreCase(text) || text.toUpperCase().startsWith("ITF")) {
			format = BarcodeFormat.ITF;
		} else if (BarcodeFormat.PDF_417.name().equalsIgnoreCase(text) || "pdf417".equalsIgnoreCase(text)
				|| "pdf-417".equalsIgnoreCase(text)) {
			format = BarcodeFormat.PDF_417;
		} else if (BarcodeFormat.QR_CODE.name().equalsIgnoreCase(text) || "qrcode".equalsIgnoreCase(text)
				|| "qr-code".equalsIgnoreCase(text)) {
			format = BarcodeFormat.QR_CODE;
		} else if (BarcodeFormat.UPC_A.name().equalsIgnoreCase(text) || "upca".equalsIgnoreCase(text)
				|| "upc-a".equalsIgnoreCase(text)) {
			format = BarcodeFormat.UPC_A;
		} else if (BarcodeFormat.UPC_E.name().equalsIgnoreCase(text) || "upce".equalsIgnoreCase(text)
				|| "upc-e".equalsIgnoreCase(text)) {
			format = BarcodeFormat.UPC_E;
		} else if (BarcodeFormat.UPC_EAN_EXTENSION.name().equalsIgnoreCase(text)
				|| "upceanextension".equalsIgnoreCase(text) || "upc-ean-extension".equalsIgnoreCase(text)) {
			format = BarcodeFormat.UPC_EAN_EXTENSION;
		} else {
			throw new IllegalArgumentException("Barcode \"" + text + "\" is unknown");
		}

		return format;
	}
	
	

}
