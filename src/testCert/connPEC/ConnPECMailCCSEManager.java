package testCert.connPEC;

import java.util.Properties;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import testCert.authenticator.MailAuthenticator;

public class ConnPECMailCCSEManager extends ConnMailCCSEManager {

	public ConnPECMailCCSEManager(int tipoConn) {
		super();
	}

	public Store apriConnessione() {
		Store store = null;
		try {
			Properties props = new Properties();
			props.put("mail.store.protocol", "imaps");
			props.put("mail.imaps.host", "mbox.cert.legalmail.it");
			props.put("mail.debug", "false");
			props.put("mail.imaps.auth", "true");
			props.put("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.imaps.starttls.enable", "true");

			Session mailSession = Session.getDefaultInstance(props, new MailAuthenticator("M", "1"));

			store =  mailSession.getStore();
			
		} catch (NoSuchProviderException e) {
						e.printStackTrace();
		}finally {
			return store;
		}
	}
}
