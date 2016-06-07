package com.moises.ormlite;

import java.io.Serializable;

public class Contact implements Serializable{

	public String fullName, phone, email, address;

	public String getFullName() {
		return fullName;
	}

	public Contact setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public Contact setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Contact setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public Contact setAddress(String address) {
		this.address = address;
		return this;
	}
	
	
}
