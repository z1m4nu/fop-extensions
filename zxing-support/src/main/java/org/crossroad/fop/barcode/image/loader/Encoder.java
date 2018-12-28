package org.crossroad.fop.barcode.image.loader;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.crossroad.fop.barcode.BarcodeDimension;
import org.crossroad.fop.barcode.IKeywords;
import org.crossroad.fop.barcode.Message;
import org.crossroad.fop.barcode.Message.HPOS;
import org.crossroad.fop.barcode.Message.VPOS;
import org.crossroad.fop.barcode.Utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.Code93Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.oned.UPCEANWriter;
import com.google.zxing.oned.UPCEWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.pdf417.encoder.Compaction;
import com.google.zxing.pdf417.encoder.Dimensions;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class Encoder {
	private Message message = null;
	private Writer writer = null;
	private Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
	private BarcodeFormat format = null;
	private Configuration barcodeConfig = null;

	private Encoder() {

	}

	public BitMatrix encode() throws Exception {
		BarcodeDimension dimension = new BarcodeDimension(barcodeConfig.getChild(IKeywords.NAME_SIZE));
		return encode(dimension.getWidthPixel(), dimension.getHeightPixel());
	}

	public BitMatrix encode(int widthPx, int heightPx) throws Exception {
		return writer.encode(message.getText(), format, widthPx, heightPx, hints);
	}

	public Configuration getRootConfig() {
		return barcodeConfig;
	}

	public Message getMessage() {
		return message;
	}

	private static ErrorCorrectionLevel getErrorCorrectionLevel(String level) {
		level = level.toLowerCase();
		if ("h".equalsIgnoreCase(level)) {
			return ErrorCorrectionLevel.H;
		} else if ("l".equalsIgnoreCase(level)) {
			return ErrorCorrectionLevel.L;
		} else if ("m".equalsIgnoreCase(level)) {
			return ErrorCorrectionLevel.M;
		} else if ("q".equalsIgnoreCase(level)) {
			return ErrorCorrectionLevel.Q;
		} else {
			throw new IllegalArgumentException("\"" + level + "\" not supported. Allowed correction levels: H/L/M/Q");
		}
	}

	public static Encoder create(Configuration rootCFG) throws Exception {
		Encoder encoder = new Encoder();

		encoder.barcodeConfig = rootCFG;
		encoder.message = new Message();
		
		Configuration msgCFG = encoder.barcodeConfig.getChild(IKeywords.NAME_MESSAGE);
		encoder.message.setText(encoder.barcodeConfig.getAttribute(IKeywords.ATTR_MESSAGE));
		encoder.message.setShow(msgCFG.getAttributeAsBoolean(IKeywords.ATTR_LABELED,false));
		encoder.message.setHpos(HPOS.valueOf(msgCFG.getAttribute(IKeywords.ATTR_HPOS,"CENTERED").toUpperCase()));
		encoder.message.setVpos(VPOS.valueOf(msgCFG.getAttribute(IKeywords.ATTR_VPOS,"CENTERED").toUpperCase()));
				
		encoder.format = Utils.getFormat(encoder.barcodeConfig.getAttribute(IKeywords.ATTR_TYPE));
		

		Configuration hintCfg = encoder.barcodeConfig.getChild(IKeywords.NAME_HINTS);

		for (Configuration cfg : hintCfg.getChildren(IKeywords.NAME_HINT)) {
			EncodeHintType type = EncodeHintType.valueOf(cfg.getAttribute(IKeywords.ATTR_NAME).toUpperCase());

			switch (type) {
			case AZTEC_LAYERS:
				break;
			case CHARACTER_SET:
				encoder.hints.put(type,
						cfg.getAttribute(IKeywords.ATTR_VALUE, IKeywords.DEFAULT_CHARACTER_SET).toUpperCase());
				break;
			case DATA_MATRIX_SHAPE:
				encoder.hints.put(type, SymbolShapeHint.valueOf(cfg.getAttribute(IKeywords.ATTR_VALUE).toUpperCase()));
				break;
			case ERROR_CORRECTION:
				encoder.hints.put(type, getErrorCorrectionLevel(
						cfg.getAttribute(IKeywords.ATTR_VALUE, IKeywords.DEFAULT_ERROR_CORRECTION_TYPE)));
				break;
			case MARGIN:
				encoder.hints.put(type, cfg.getAttributeAsInteger(IKeywords.ATTR_VALUE, 0));
				break;
			case MAX_SIZE:
				break;
			case MIN_SIZE:
				break;
			case PDF417_COMPACT:
			case GS1_FORMAT:
				encoder.hints.put(type, cfg.getAttributeAsBoolean(IKeywords.ATTR_VALUE, false));
				break;
			case PDF417_COMPACTION:
				encoder.hints.put(type, Compaction.valueOf(cfg.getAttribute(IKeywords.ATTR_VALUE).toUpperCase()));
				break;
			case PDF417_DIMENSIONS:
				Configuration dimcfg = cfg.getChild(IKeywords.NAME_HINT_DIMENSION);
				Dimensions dim = new Dimensions(dimcfg.getAttributeAsInteger(IKeywords.ATTR_MIN_COLS),
						dimcfg.getAttributeAsInteger(IKeywords.ATTR_MAX_COLS),
						dimcfg.getAttributeAsInteger(IKeywords.ATTR_MIN_ROWS),
						dimcfg.getAttributeAsInteger(IKeywords.ATTR_MAX_ROWS));
				encoder.hints.put(type, dim);
				break;
			case QR_VERSION:
				encoder.hints.put(type, cfg.getAttributeAsInteger(IKeywords.ATTR_VALUE));
				break;
			default:
				throw new ConfigurationException(type.name() + " is not implemented");
			}

		}

		if (!encoder.hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
			encoder.hints.put(EncodeHintType.ERROR_CORRECTION,
					getErrorCorrectionLevel(IKeywords.DEFAULT_ERROR_CORRECTION_TYPE));
		}

		if (!encoder.hints.containsKey(EncodeHintType.CHARACTER_SET)) {
			encoder.hints.put(EncodeHintType.CHARACTER_SET, IKeywords.DEFAULT_CHARACTER_SET);
		}

				
		switch (encoder.format) {
		case AZTEC:
			encoder.writer = new AztecWriter();
			break;
		case CODABAR:
			encoder.writer = new CodaBarWriter();
			break;
		case CODE_128:
			encoder.writer = new Code128Writer();
			break;
		case CODE_39:
			encoder.writer = new Code39Writer();
			break;
		case CODE_93:
			encoder.writer = new Code93Writer();
			break;
		case DATA_MATRIX:
			encoder.writer = new DataMatrixWriter();
			break;
		case EAN_13:
			encoder.writer = new EAN13Writer();
			break;
		case EAN_8:
			encoder.writer = new EAN8Writer();
			break;
		case ITF:
			encoder.writer = new ITFWriter();
			break;
		case MAXICODE:
		case RSS_14:
		case RSS_EXPANDED:
			throw new Exception(encoder.format.name() + " not implemented");
		case PDF_417:
			encoder.writer = new PDF417Writer();
			break;
		case QR_CODE:
			encoder.writer = new QRCodeWriter();
			break;
		case UPC_A:
			encoder.writer = new UPCAWriter();
			break;
		case UPC_E:
			encoder.writer = new UPCEWriter();
			break;
		case UPC_EAN_EXTENSION:
			encoder.writer = new UPCEANWriter() {

				@Override
				public boolean[] encode(String contents) {
					// TODO Auto-generated method stub
					return null;
				}
			};
			break;
		default:
			throw new Exception("Unknown type [" + encoder.format.toString() + "]");
		}

		return encoder;
	}
}
