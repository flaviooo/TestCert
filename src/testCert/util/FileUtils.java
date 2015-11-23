package testCert.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

	public static synchronized void scriviFile(File f, InputStream is)
	{
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			int abyte=is.read();
			while (abyte != -1)
			{
				fos.write(abyte);
				abyte=is.read();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e2) {
				
			}
		}
	}
	
}
