package testCert.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

public class ExstractorP7M {

	public static void main(String[] args) {
		try {
			String pathFilesSigned = "C:\\Users\\fL\\Desktop\\SDI\\SDIMAIL4";
			//System.out.println("esito: " + verify(pathFilesSigned));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public  boolean verifyEstrai(String pathFilesSigned) {

		boolean esito = false;
		File[] filesP7M = new File(pathFilesSigned).listFiles((new FileExtensionFilter("p7m")));
		int nFiles = filesP7M.length;
		System.out.println(String.format("Trovati %s file", nFiles));
		if (nFiles != 0) {
			for (int i = 0; i < filesP7M.length; i++) {

				String fileNameP7MExport = pathFilesSigned + filesP7M[i].getName().substring(0, filesP7M[i].getName().length() - 4).trim();

				byte[] buffer = new byte[(int) filesP7M[i].length()];
				DataInputStream in;
				try {
					in = new DataInputStream(new FileInputStream(filesP7M[i]));

					in.readFully(buffer);
					in.close();

					Security.addProvider(new BouncyCastleProvider());
					CMSSignedData signature = new CMSSignedData(buffer);
					SignerInformation signer = (SignerInformation)signature.getSignerInfos().getSigners().iterator().next();
					CertStore cs = signature.getCertificatesAndCRLs("Collection", "BC");
					
					Iterator iter = cs.getCertificates(signer.getSID()).iterator();
					X509Certificate certificate = (X509Certificate) iter.next();
					CMSProcessable sc = signature.getSignedContent();
					byte[] data = (byte[]) sc.getContent();

					// Verifie la signature
					//System.out.println(signer.verify(certificate, "BC"));
					System.out.println(	signer.verify(certificate.getPublicKey(), "BC"));
					
					System.out.println(data);
					System.out.println(fileNameP7MExport);
					FileOutputStream envfos = new FileOutputStream(fileNameP7MExport);
					envfos.write(data);
					envfos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return esito;
	}
}
