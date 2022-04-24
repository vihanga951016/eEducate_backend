package com.vihanga.eEducate.domain;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

//Normally when user send a request, and it is a good request, it's fine.
//But when it has an error, that also send as a response to user.
//Otherwise it can be a vulnerability for this application.
//That's why I create this class.
public class HttpResponse {

	@JsonFormat(shape = Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Asia/Colombo")
	private Date timeStamp;
	private int httpStatusCode;// 200, 201, 400, 500
	private HttpStatus httpStatus;//This HttpStatus is an enum class(Singleton)
	//which is store all of the information about the http response(200,400,500).
	private String reason;
	private String message;

//	{
//		code: 200,
//		httpStatus: "OK",
//		reason: "ok",
//		message: "Your request was successful"
//	}
	
	public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
		this.timeStamp = new Date();//Whenever build this HttpRespons, create new 'timeStamp'. 
		this.httpStatusCode = httpStatusCode;
		this.httpStatus = httpStatus;
		this.reason = reason;
		this.message = message;
	}


	public HttpResponse() {
	
	}//This default constructor is may useful or not, because
	//if we create an empty object from this class, properties
	//of this class is probably gonna be null or 0 values.
	//Therefore we don't need this actually. But I'll leave it.

	public Date getTimeStamp() {
		return timeStamp;
	}


	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}


	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}


	public HttpStatus getHttpStatus() {
		return httpStatus;
	}


	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	
	
}
