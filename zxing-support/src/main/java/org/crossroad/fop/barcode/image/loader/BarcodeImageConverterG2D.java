package org.crossroad.fop.barcode.image.loader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageConverter;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.crossroad.fop.barcode.IKeywords;
import org.crossroad.fop.barcode.Message;

import com.google.zxing.common.BitMatrix;

public class BarcodeImageConverterG2D extends AbstractImageConverter {
	private static final int BORDER = 2;

	public Image convert(Image source, @SuppressWarnings("rawtypes") Map hints) {
		BarcodeImage qrCodeImage = (BarcodeImage) source;

		Graphics2DImagePainterBarcode painter = new Graphics2DImagePainterBarcode(qrCodeImage);
		ImageGraphics2D g2dImage = new ImageGraphics2D(source.getInfo(), painter);
		return g2dImage;
	}

	public ImageFlavor getTargetFlavor() {
		return ImageFlavor.BUFFERED_IMAGE;
	}

	public ImageFlavor getSourceFlavor() {
		return IKeywords.BARCODE_IMAGE_FLAVOR;
	}

	private static class Graphics2DImagePainterBarcode implements Graphics2DImagePainter {
		private boolean DEBUG = false;
		private BarcodeImage barcodeImage;

		public Graphics2DImagePainterBarcode(BarcodeImage barcodeImage) {
			this.barcodeImage = barcodeImage;
		}

		public Dimension getImageSize() {
			return barcodeImage.getSize().getDimensionMpt();
		}

		public void paint(Graphics2D g2d, Rectangle2D area) {
			double w = area.getWidth();
			double h = area.getHeight();

			BitMatrix matrix = barcodeImage.getMatrix();

			int rect[] = matrix.getEnclosingRectangle();
			Message message = (Message)barcodeImage.getInfo().getCustomObjects().get(IKeywords.DATA_MESSAGE);

			int matrixWidth = matrix.getWidth();
			int matrixHeight = matrix.getHeight();
			int imageHeight = barcodeImage.getSize().getHeightPx() + BORDER;
			int left = rect[0];
			
			if (DEBUG)
			System.out
					.println("[ZXING] - Area WxH " + w + "x" + h + " Bitmatrix WxH " + matrixWidth + "x" + matrixHeight
							+ " Real image " + matrixWidth + "x" + imageHeight + " Rowsize #" + matrix.getRowSize());

			g2d.translate(area.getX(), area.getY());

			double bsx = w / matrixWidth;
			double bsy = h / matrixHeight;
			g2d.scale(bsx, bsy);

			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, matrixWidth, matrixHeight);


			g2d.setColor(Color.BLACK);

			for (int x = 0; x < matrixWidth; x++) {
				for (int y = 0; y < matrixHeight; y++) {
					if (matrix.get(x, y)) {
						g2d.fillRect(x, y, 1, 1);
					}
				}
			}
			if (message.isShow()) {
				Font oldFont = g2d.getFont();
				Font font = new Font("Helvetica", Font.PLAIN, 12);

				g2d.setFont(font);

				int len = g2d.getFontMetrics(font).stringWidth(message.getText());
				int start = ((matrixWidth - len) / 2);

				g2d.drawString(message.getText(), start, imageHeight - 3);

				g2d.setFont(oldFont);
			}

		}

	}

	
	
}
