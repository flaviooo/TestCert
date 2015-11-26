package testCert;

import java.io.InputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bouncycastle.mail.smime.SMIMESignedParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import testCert.service.PECWorker;


public class Main {

	public static void main(String[] args) {

		
		PECWorker wp = new PECWorker();
		wp.ExtractorMailPec();

	}
	
	private static Document extractXMLCert(final SMIMESignedParser s) throws Exception {
		final MimeBodyPart mimePart = s.getContent();
		final DataHandler data = mimePart.getDataHandler();
		final MimeMultipart multiPart = (MimeMultipart) data.getContent();
		if (multiPart.getCount() < 1) {
			throw new MessagingException("Missing attachments");
		}
		final BodyPart bodyCert = multiPart.getBodyPart(1);
		final DataHandler dataCert = bodyCert.getDataHandler();
		final DataSource dataSourceCert = dataCert.getDataSource();
		final InputStream idataCert = dataSourceCert.getInputStream();
		final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder parser = builderFactory.newDocumentBuilder();
		final InputSource source = new InputSource(idataCert);
		final Document domCert = parser.parse(source);
		return domCert;
	}

}
