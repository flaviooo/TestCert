package testCert.ExceptionCert;

public class CertException extends Exception {
	
	private static final long serialVersionUID = -8584689365043278709L;

	public CertException() {
		super();
		
	}

	public CertException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public CertException(String message) {
		super(message);
		
	}

	public CertException(Throwable cause) {
		super(cause);
		
	}
}
