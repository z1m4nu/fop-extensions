package org.crossroad.fop.barcode.image.loader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.AbstractImagePreloader;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.util.UnitConv;
import org.apache.xmlgraphics.util.io.SubInputStream;
import org.crossroad.fop.barcode.BarcodeDimension;
import org.crossroad.fop.barcode.BarcodeElementMapping;
import org.crossroad.fop.barcode.IKeywords;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.zxing.common.BitMatrix;

public class BarcodeImagePreloader extends AbstractImagePreloader {

	private boolean isSupportedSource(Source src) {
		if (src instanceof DOMSource) {
			DOMSource domSrc = (DOMSource) src;
			return (domSrc.getNode() instanceof Document);
		} else {
			return ImageUtil.hasInputStream(src);
		}
	}

	public ImageInfo preloadImage(String uri, Source src, ImageContext context) throws IOException {
		ImageInfo info = null;
		if (!isSupportedSource(src)) {
			return null;
		}
		info = getImage(uri, src, context);
		if (info != null) {
			ImageUtil.closeQuietly(src); // Image is fully read
		}
		return info;
	}

	private ImageInfo getImage(String uri, Source src, ImageContext context) throws IOException {
		InputStream in = null;
		try {
			Document doc;
			if (src instanceof DOMSource) {
				DOMSource domSrc = (DOMSource) src;
				doc = (Document) domSrc.getNode();
			} else {
				in = ImageUtil.needInputStream(src);
				int length = in.available();
				in.mark(length + 1);
				try {
					doc = getDocument(new SubInputStream(in, Long.MAX_VALUE, false));
				} catch (IOException ioe) {
					resetInputStream(in);
					return null;
				}
			}

			Element element = doc.getDocumentElement();

			if (!BarcodeElementMapping.NAMESPACE.equals(element.getNamespaceURI())) {
				resetInputStream(in);
				return null;
			}

			ImageInfo info;
			try {
				info = createImageInfo(uri, ConfigurationUtil.toConfiguration(element), context);
			} catch (Exception e) {
				resetInputStream(in);
				throw new IOException("Error processing Barcode XML: " + e.getLocalizedMessage());
			}

			return info;
		} catch (SAXException se) {
			resetInputStream(in);
			return null;
		} catch (ParserConfigurationException pce) {
			// Parser not available, propagate exception
			throw new RuntimeException(pce);
		}
	}

	private void resetInputStream(InputStream in) {
		try {
			if (in != null) {
				in.reset();
			}
		} catch (IOException ioe) {
			// Ignored. We're more interested in the original exception.
		}
	}

	private ImageInfo createImageInfo(String uri, Configuration rootConfig, ImageContext context) throws Exception {

		Encoder encoder = Encoder.create(rootConfig);
		BarcodeDimension dimension = new BarcodeDimension(encoder.getRootConfig().getChild(IKeywords.NAME_SIZE));
		BitMatrix matrix = encoder.encode(dimension.getWidthPixel(), dimension.getHeightPixel());
		ImageInfo info = new ImageInfo(uri, IKeywords.MIME_TYPE);

		int fontMPT = UnitConv.convert("12pt");
		int realHeight = (encoder.getMessage().isShow())
				? matrix.getHeight() + (int) UnitConv.mpt2px(fontMPT, (int) context.getSourceResolution())
				: matrix.getHeight();
		
		ImageSize size = new ImageSize();
		size.setSizeInMillipoints(UnitConv.convert(matrix.getWidth() + "px") / 3,
				UnitConv.convert(realHeight + "px") / 3);
		size.setResolution(context.getSourceResolution());
		size.calcPixelsFromSize();
		info.setSize(size);

		info.getCustomObjects().put(ImageInfo.ORIGINAL_IMAGE, new BarcodeImage(info, matrix));
		info.getCustomObjects().put(IKeywords.DATA_CONFIG, encoder.getRootConfig());
		info.getCustomObjects().put(IKeywords.DATA_MESSAGE, encoder.getMessage());

		return info;
	}

	private Document getDocument(InputStream in) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setErrorHandler(new ErrorHandler() {

			public void error(SAXParseException exception) throws SAXException {
				throw exception;
			}

			public void fatalError(SAXParseException exception) throws SAXException {
				throw exception;
			}

			public void warning(SAXParseException exception) throws SAXException {
				throw exception;
			}

		});
		Document doc = db.parse(in);
		return doc;
	}

}
