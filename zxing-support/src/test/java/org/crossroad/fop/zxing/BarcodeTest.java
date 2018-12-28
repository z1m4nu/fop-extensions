package org.crossroad.fop.zxing;
/**
 * 
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.PageSequenceResults;

/**
 * @author e.soden
 *
 */
public class BarcodeTest {

	/**
	 * 
	 */
	public BarcodeTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

		  OutputStream out = null;

	        try {
	            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
	            // configure foUserAgent as desired

	            // Setup output stream.  Note: Using BufferedOutputStream
	            // for performance reasons (helpful with FileOutputStreams).
	            out = new FileOutputStream("C:\\tmp\\myfile.pdf");
	            out = new BufferedOutputStream(out);

	            // Construct fop with desired output format
	            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

	            // Setup JAXP using identity transformer
	            TransformerFactory factory = TransformerFactory.newInstance();
	            Transformer transformer = factory.newTransformer();

	            // Setup input stream
	            Source src = new StreamSource("sample.xml");

	            // Resulting SAX events (the generated FO) must be piped through to FOP
	            Result res = new SAXResult(fop.getDefaultHandler());

	            // Start XSLT transformation and FOP processing
	            transformer.transform(src, res);

	            // Result processing
	            FormattingResults foResults = fop.getResults();
	            java.util.List pageSequences = foResults.getPageSequences();
	            for (Object pageSequence : pageSequences) {
	                PageSequenceResults pageSequenceResults = (PageSequenceResults) pageSequence;
	                System.out.println("PageSequence "
	                        + (String.valueOf(pageSequenceResults.getID()).length() > 0
	                        ? pageSequenceResults.getID() : "<no id>")
	                        + " generated " + pageSequenceResults.getPageCount() + " pages.");
	            }
	            System.out.println("Generated " + foResults.getPageCount() + " pages in total.");

	        } catch (Exception e) {
	            e.printStackTrace(System.err);
	            System.exit(-1);
	        } finally {
	            out.close();
	        }
        
        
	}

}
