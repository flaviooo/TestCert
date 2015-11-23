package testCert.connPEC;

import javax.mail.MessagingException;
import javax.mail.Store;

public abstract class ConnMailCCSEManager {
	
	public static final int MAIL_PEC = 1;
	public static final int MAIL = 2;
	protected Store store;
	
	public abstract Store apriConnessione();
	public boolean chiudi(Store store){
		boolean esito = false;
		
		try {
			store.close();
			esito = false;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return esito;
		
	}
		
	public static ConnMailCCSEManager getConnManager(int tipoConn){
		switch(tipoConn){
		case MAIL_PEC : return new ConnPECMailCCSEManager(tipoConn);
		default: return null;
		
		}
				
	}
}
