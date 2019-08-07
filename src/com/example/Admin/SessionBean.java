package com.example.Admin;

public class SessionBean 
{
	private String userId="";
	private String userName="";
	private String usertype="";
	private String AccountImage="D:\\AccountImages";
	
	public void setUserId(String id) {
		this.userId=id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserName(String name) {
		this.userName=name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserType(String type) {
		this.usertype=type;
	}
	public String gerUserType() {
		return usertype;
	}
	public String getAccountImagePath() {
		return AccountImage;
	}
}
