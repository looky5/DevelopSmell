package com.web.blog.model.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
public class Mail {

	private String address;
	private String title;
	private String message;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public Mail(String address, String title) {
		super();
		this.address = address;
		this.title = title;
	
	}
	public Mail(String address, String title, String message) {
		super();
		this.address = address;
		this.title = title;
		this.message = message;
	}
}
