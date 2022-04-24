package com.vihanga.eEducate.exceptions.domain;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.vihanga.eEducate.domain.HttpResponse;

/*
	Rest Controller Adviser is use to handle requests that are coming through the Rest Controller. If user can access to the controller
	it may went bad. For prevent that, we use RestControllerAdvice.
*/
@RestControllerAdvice
public class ExceptionHandlling implements ErrorController{
	
	//like console.log
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact the administration.";
	private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on enpoint";
	private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request.";
	private static final String INCORRECT_CREDENTIALS = "Username / Password is incorrect. Please try again.";
	private static final String ACCOUNT_DISABLED = "Your account has been disabled.";
	private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file.";
	private static final String NOT_ENOUGHT_PERMISSION = "You do not have enough permission.";
	private static final String ERROR_PATH = "/error";

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> accountDisableException(){
		return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialsException(){
		return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> accessDeniedException(){
		return createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGHT_PERMISSION);
	}
	
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<HttpResponse> lockedException(){
		return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED);
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException tokenExpiredException){
		return createHttpResponse(HttpStatus.UNAUTHORIZED, tokenExpiredException.getMessage());
	}
	
	@ExceptionHandler(EmailExistException.class)
	public ResponseEntity<HttpResponse> emailExistException(EmailExistException emailExistException){
		return createHttpResponse(HttpStatus.BAD_REQUEST, emailExistException.getMessage());
	}
	
	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException emailNotFoundException){
		return createHttpResponse(HttpStatus.BAD_REQUEST, emailNotFoundException.getMessage());
	}
	
	@ExceptionHandler(UsernameExistException.class)
	public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException usernameExistException){
		return createHttpResponse(HttpStatus.BAD_REQUEST, usernameExistException.getMessage());
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<HttpResponse> usernameNotFoundException(UsernameNotFoundException usernameNotFoundException){
		return createHttpResponse(HttpStatus.BAD_REQUEST, usernameNotFoundException.getMessage());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)//If user tries to send a request, but it is wrong request.
	public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException){
		HttpMethod supportedMethod = Objects.requireNonNull(httpRequestMethodNotSupportedException.getSupportedHttpMethods()).iterator().next();
		return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception){
		LOGGER.error(exception.getMessage());
		return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
	}
	
	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<HttpResponse> notFoundException(NoResultException exception){
		LOGGER.error(exception.getMessage());
		return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<HttpResponse> ioException(IOException exception){
		LOGGER.error(exception.getMessage());
		return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
	}
	
	//This can handle every single exceptions that we try to catch.
	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
		return new ResponseEntity<HttpResponse>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase()), httpStatus);	
	}
	
	
	@RequestMapping(ERROR_PATH)
	public ResponseEntity<HttpResponse> notFound404(){
		return createHttpResponse(HttpStatus.NOT_FOUND, "The page was not found.");
	}
	
	public String getErrorPath() {
		return ERROR_PATH;
	}
	
}
