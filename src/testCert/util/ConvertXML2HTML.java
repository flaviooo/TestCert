package testCert.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * HTML
 * This class demonstrates the conversion of an XML file to an XSL-FO file
 * using JAXP (XSLT).
 */
public class ConvertXML2HTML {
    /**
     * Converts an XML file to an XSL-FO file using JAXP (XSLT).
     * @param xml the XML file
     * @param xslt the stylesheet file
     * @param fo the target XSL-FO file
     * @throws IOException In case of an I/O problem
     * @throws TransformerException In case of a XSL transformation problem
     */
	   public void convertXML2FO(String fileNameHTMLMExport)
               throws IOException, TransformerException {
			File xsltfile = new File("C:\\Users\\f.tuosto\\Desktop\\SDI\\SDIMAILV4\\xsl", "fatturapa_v1.1.xsl");
       //Setup output
       OutputStream out = new java.io.FileOutputStream(new File(fileNameHTMLMExport+".html"));
       try {
           //Setup XSLT
           TransformerFactory factory = TransformerFactory.newInstance();
           Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

           //Setup input for XSLT transformation
           Source src = new StreamSource(new File(fileNameHTMLMExport));

           //Resulting SAX events (the generated FO) must be piped through to FOP
           Result res = new StreamResult(out);

           //Start XSLT transformation and FOP processing
           transformer.transform(src, res);
           
       } finally {
           out.close();
       }
   }
}
