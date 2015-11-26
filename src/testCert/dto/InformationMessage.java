package testCert.dto;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

public class InformationMessage {

	private String subjectFE = "";
	private Address[] a;
	private Address[] da;
	private String idMessage;

	public InformationMessage(Message m) {

		try {
			this.subjectFE = m.getSubject();

			this.a = m.getFrom();
			this.da = m.getAllRecipients();
			if (m.getSize() != -1) {
				String[] temp = m.getSubject().split(" ");
				this.idMessage = temp[4];
			}
			System.out.println("Size: " + m.getSize());
			Flags flag = m.getFlags();
			System.out.println("attach " + m.ATTACHMENT);
			System.out.println("inline " + m.INLINE);
			System.out.println("expunge " + m.isExpunged());
			System.out.println("num mess: " + m.getMessageNumber());
			System.out.println("linea count: " + m.getLineCount());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getSubjectFE() {
		return subjectFE;
	}

	public void setSubjectFE(String subjectFE) {

		this.subjectFE = subjectFE;
	}

	public Address[] getA() {
		return a;
	}

	public void setA(Address[] a) {
		this.a = a;
	}

	public Address[] getDa() {
		return da;
	}

	public void setDa(Address[] da) {
		this.da = da;
	}

	public String getIdMessage() {

		return idMessage;
	}

	public void setIdMessage(String idMessage) {
		this.idMessage = idMessage;
	}

}
