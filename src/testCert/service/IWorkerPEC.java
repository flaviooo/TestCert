package testCert.service;

import javax.mail.Message;

public interface IWorkerPEC {
	
	final static public String pathBase = "C:\\Users\\f.tuosto\\Desktop\\SDI";
	public void elaboraMessaggi(Message[] msg);

}
