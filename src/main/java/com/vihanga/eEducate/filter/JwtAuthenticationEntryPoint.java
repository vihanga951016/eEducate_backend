package com.vihanga.eEducate.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vihanga.eEducate.constant.SecurityConstant;
import com.vihanga.eEducate.domain.HttpResponse;

@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint{
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)throws IOException {
		
		HttpResponse httpResponse = new HttpResponse(HttpStatus.FORBIDDEN.value(),
				HttpStatus.FORBIDDEN,
				HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(),
				SecurityConstant.FORBIDDEN_MESSAGE);
		/*
		 * 'HttpStatus.FORBIDDEN.value()' is the value of the status(200/400/500).
		 * 'HttpStatus.FORBIDDEN' is the http status.
		 * 'HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase()' get the reason phrase and convert it to uppercase.
		 * 'SecurityConstant.FORBIDDEN_MESSAGE' our forbidden message.
		 */

		response.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE); //Set the response value to json format.
		response.setStatus(HttpStatus.FORBIDDEN.value()); // Set value 403 to state of response.


		//Now we have to get the 'response' and stream into 'httpResponse'. To do that...
		OutputStream outputStream = response.getOutputStream(); //Stream the 'response'.
		ObjectMapper mapper = new ObjectMapper(); //Create the 'mapper' object.
		mapper.writeValue(outputStream, httpResponse); //write the http response.
		outputStream.flush();
	}
}
