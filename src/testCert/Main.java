package testCert;

import java.io.File;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bouncycastle.mail.smime.SMIMESignedParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import testCert.connPEC.ConnMailCCSEManager;
import testCert.util.ExstractorP7M;
import testCert.util.FileUtils;

public class Main {

	final static private String pathBase = "C:\\Users\\f.tuosto\\Desktop\\SDI";

	public static void main(String[] args) {

		ConnMailCCSEManager cm = ConnMailCCSEManager.getConnManager(ConnMailCCSEManager.MAIL_PEC);
		ExtractorMailPec(cm);

	}

	private static void ExtractorMailPec(ConnMailCCSEManager cm) {
		Store store = cm.apriConnessione();
		try {
			store.connect();
			System.out.println("Connesso: " + store.isConnected());
			// Folder folder = store.getFolder("INBOX.FatturePA_SDI");
			Folder folder = store.getFolder("INBOX.TEST_FATTURE");
			folder.open(Folder.READ_WRITE);

			elaboraMessaggi(folder.getMessages());

			System.out.println("ESISTE " + folder.exists() + " num MSG: " + folder.getMessages().length);
			folder.close(false);

		} catch ( Exception e) {
			e.printStackTrace();
		} finally {

			cm.chiudi(store);
			System.out.println("Connesso?: " + store.isConnected());
		}
	}

	private static void elaboraMessaggi(Message[] msg) throws Exception{
		
		for (int i = 0; i < msg.length; i++) {
			InformationMessage ifm = new InformationMessage((Message) msg[i]);
			
			if (ifm.getIdMessage() != null) {
				String idMessage = ifm.getIdMessage();
				
				//creo la cartella che conterrà i singoli file presenti in un msg
				 File f =new File (pathBase+File.separator+idMessage);
				 if(!f.exists()) f.mkdirs();
				elaboraSingoloMessaggio(msg[i],idMessage);
			}
			System.out.println("Elaborazione Fattura n: " + (i + 1));
		}
		
	}

	private static void elaboraSingoloMessaggio(Part msg, String idMessage) {
		
		try {

			if (msg.isMimeType("text/plain")) {

				System.out.println("This is plain text: " + msg.getFileName() + " " + msg.getDisposition());
				System.out.println("---------------------------");
				File fileSave = new File(pathBase+File.separator+idMessage,idMessage+"_testo.txt");		
				FileUtils.scriviFile(fileSave, msg.getInputStream());
				
			}
			// check if the content has attachment
			else if (msg.isMimeType("multipart/*")) {
//				System.out.println("This is a Multipart: " + msg.getFileName() + " " + msg.getDisposition());
//				System.out.println("---------------------------");
				Multipart mp = (Multipart) msg.getContent();
				int count = mp.getCount();
				for (int i = 0; i < count; i++)
					elaboraSingoloMessaggio(mp.getBodyPart(i),idMessage);
			}
			// check if the content is a nested message
			else if (msg.isMimeType("message/rfc822")) {
				System.out.println(
						"This is a message/rfc822 Nested Message: " + msg.getFileName() + " " + msg.getDisposition());
				System.out.println("---------------------------");
				elaboraSingoloMessaggio((Part) msg.getContent(),idMessage);
			}
			// check if the content is an inline image
			/*else if (msg.isMimeType("image/jpeg")) {
				System.out.println("--------> image/jpeg: " + msg.getFileName() + " " + msg.getDisposition());
				Object o = msg.getContent();

				InputStream x = (InputStream) o;
				// Construct the required byte array
				System.out.println("x.length = " + x.available());
			} else if (msg.getContentType().contains("image/")) {
				System.out.println("content type: " + msg.getFileName() + " " + msg.getDisposition());
				System.out.println("content type: " + msg.getContentType());
				//File f = new File("image" + new Date().getTime() + ".jpg");
				//DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
				com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) msg.getContent();
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = test.read(buffer)) != -1) {
					//output.write(buffer, 0, bytesRead);
				}
			}*/ else {
				Object o = msg.getContent();
				if (o instanceof String) {
					System.out.println("_+This is a string: " + msg.getFileName() + " " + msg.getDisposition());
					System.out.println("---------------------------" + msg.getContentType());
					if(msg.getFileName()==null && msg.getContentType().contains("html".toLowerCase())){
						File fileSave = new File(pathBase+File.separator+idMessage,idMessage+"_"+".html");		
						FileUtils.scriviFile(fileSave, msg.getInputStream());	
					}
					
				} else if (o instanceof InputStream) {
					System.out.println(
							"_This_ is just an input stream: " + msg.getFileName() + " " + msg.getDisposition());
					System.out.println("---------------------------"+msg.getContentType());
					
					

					if(msg.getFileName().toLowerCase().endsWith(".p7m")){
						
				
						File fileP7m = new File(pathBase+File.separator+idMessage,idMessage+"_"+msg.getFileName());		
						FileUtils.scriviFile(fileP7m, msg.getInputStream());
						//String bite = java.util.Base64.getMimeEncoder().encodeToString(IOUtils.toByteArray(iss));
						
						ExstractorP7M extrac = new ExstractorP7M();
						System.out.println(extrac.verify(pathBase+File.separator+idMessage+File.separator));

					}else{
						File fileSave = new File(pathBase+File.separator+idMessage,idMessage+"_"+msg.getFileName());		
						FileUtils.scriviFile(fileSave, msg.getInputStream());	
					}
				
				} else {
					System.out.println("This is an unknown type");
					System.out.println("---------------------------");
					System.out.println(o.toString());
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
