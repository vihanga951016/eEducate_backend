package com.vihanga.eEducate.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vihanga.eEducate.constant.SecurityConstant;
import com.vihanga.eEducate.domain.HttpResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		HttpResponse httpResponse = new HttpResponse(HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase(), SecurityConstant.ACCESS_DENIED_MESSAGE);
		/*
		 * 'HttpStatus.UNAUTHORIZED.value()' is the value of the status(200/400/500).
		 * 'HttpStatus.UNAUTHORIZED' is the http status.
		 * 'HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase()' get the reason phrase and convert it to uppercase.
		 * 'SecurityConstant.UNAUTHORIZED_MESSAGE' our unauthorized message.
		 */
		
		response.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE); //Set the respose value to json format.
		response.setStatus(HttpStatus.UNAUTHORIZED.value()); // Set value 403 to state of response.
		
		//Now we have to get the 'response' and stream into 'httpResponse'. To do that...
		OutputStream outputStream = response.getOutputStream(); //Stream the 'response'.
		ObjectMapper mapper = new ObjectMapper(); //Create the 'mapper' object.
		mapper.writeValue(outputStream, httpResponse); //write the http response.
		outputStream.flush();
	}

	
}
