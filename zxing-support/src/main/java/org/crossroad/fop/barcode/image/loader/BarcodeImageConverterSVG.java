package org.crossroad.fop.barcode.image.loader;

import java.util.Map;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.XMLNamespaceEnabledImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageConverter;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.crossroad.fop.barcode.IKeywords;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

public class BarcodeImageConverterSVG extends AbstractImageConverter {

	private static final String SVG_ATTR_WIDTH = "width";
	private static final String SVG_ATTR_HEIGHT = "height";
	private static final String SVG_ATTR_FILL = "fill";
	private static final String SVG_ATTR_STROKE = "stroke";
	private static final String SVG_ATTR_STROKE_WIDTH = "stroke-width";
	private static final String SVG_ATTR_X = "x";
	private static final String SVG_ATTR_Y = "y";
	private static final String SVG_ELEMENT_SVG = "svg";
	private static final String SVG_ELEMENT_RECT = "rect";

	private static final String COLOR_BLACK = "black";
	private static final String STROKE_WIDTH = "0.01";
	private static final String SIZE = "1";

	public Image convert(Image source, @SuppressWarnings("rawtypes") Map hints) {
		BarcodeImage qrCodeImage = (BarcodeImage) source;
		Document document = null;

		BitMatrix matrix = qrCodeImage.getMatrix();
		document = bitMatrixToSVGDocument(matrix);

		return new ImageXMLDOM(source.getInfo(), document, SVGDOMImplementation.SVG_NAMESPACE_URI);
	}

	public ImageFlavor getTargetFlavor() {
		return XMLNamespaceEnabledImageFlavor.SVG_DOM;
	}

	public ImageFlavor getSourceFlavor() {
		return IKeywords.BARCODE_IMAGE_FLAVOR;
	}

	private Document bitMatrixToSVGDocument(BitMatrix matrix) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document svg = impl.createDocument(svgNS, SVG_ELEMENT_SVG, null);
		Element svgRoot = svg.getDocumentElement();
		int matrixWidth = matrix.getWidth();
		int matrixHeight = matrix.getHeight();
		svgRoot.setAttributeNS(null, SVG_ATTR_WIDTH, Integer.toString(matrixWidth));
		svgRoot.setAttributeNS(null, SVG_ATTR_HEIGHT, Integer.toString(matrixHeight));
		for (int y = 0; y < matrixHeight; y++) {
			BitArray bitArray = matrix.getRow(y, null);
			for (int x = 0; x < bitArray.getSize(); x++) {
				if (bitArray.get(x)) {
					Element rectangle = svg.createElementNS(svgNS, SVG_ELEMENT_RECT);
					rectangle.setAttributeNS(null, SVG_ATTR_X, Integer.toString(x));
					rectangle.setAttributeNS(null, SVG_ATTR_Y, Integer.toString(y));
					rectangle.setAttributeNS(null, SVG_ATTR_WIDTH, SIZE);
					rectangle.setAttributeNS(null, SVG_ATTR_HEIGHT, SIZE);
					rectangle.setAttributeNS(null, SVG_ATTR_FILL, COLOR_BLACK);
					rectangle.setAttributeNS(null, SVG_ATTR_STROKE, COLOR_BLACK);
					rectangle.setAttributeNS(null, SVG_ATTR_STROKE_WIDTH, STROKE_WIDTH);
					svgRoot.appendChild(rectangle);
				}
			}
		}
		return svg;
	}
}
