package com.moises.broadcastreceiver;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Contact implements Serializable{

	private String fullName, address;
	public Contact(String fullName, String address){
		this.fullName = fullName;
		this.address = address;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
