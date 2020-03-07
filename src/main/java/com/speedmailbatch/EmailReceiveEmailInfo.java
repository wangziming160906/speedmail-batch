package com.speedmailbatch;

public class EmailReceiveEmailInfo {
	
	private String subject;
	
	private String sendUser;
	
	private String receiverUser;
	
	private String sendDate;
	
	private Boolean isRead;
	
	private StringBuffer content;

	private String userAddr;

	private String userPws;

	public String getUserAddr() {
		return userAddr;
	}

	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	public String getUserPws() {
		return userPws;
	}

	public void setUserPws(String userPws) {
		this.userPws = userPws;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

	public String getReceiverUser() {
		return receiverUser;
	}

	public void setReceiverUser(String receiverUser) {
		this.receiverUser = receiverUser;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public Boolean getRead() {
		return isRead;
	}

	public void setRead(Boolean read) {
		isRead = read;
	}

	public StringBuffer getContent() {
		return content;
	}

	public void setContent(StringBuffer content) {
		this.content = content;
	}

}
