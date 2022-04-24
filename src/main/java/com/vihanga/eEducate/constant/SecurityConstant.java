package com.vihanga.eEducate.constant;

//define some constants for provider of the token(Utility/Provider class)
public class SecurityConstant {

	public static final long EXPIRATION_TIME = 432000000;//expire token after 5 days. 
	public static final String TOKEN_PREFIX = "Bearer ";// just for verification of my token.
	public static final String JWT_TOKEN_HEADER = "Jwt-Token";// actual token header/token.
	public static final String TOKEN_CANNOT_BE_VERIFIED = "Token Cannot be Verified";
	public static final String VIHANGA_PRODUCTION = "VIHANGA_PRODUCTION";
	public static final String GET_VP_ADMINISTRATION = "eEducate Portal";//check this token is for who? 
//	audience of this application.
	public static final String AUTHORITIES = "Authorities";// hold all authorities in application.
	public static final String FORBIDDEN_MESSAGE = "You need to login to access this page";
	public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to this page";
	public static final String OPTIONS_HTTP_METHOD = "OPTIONS"; //options(GET,POST) that are coming through the request.
	public static final String[] PUBLIC_URLS = { "/eeducate/login" , "/eeducate/register" , "/eeducate/resetpassword/**"};
//	"/eeducate/image/**"}; //public Url are always allow to this application.
//	public static final String[] PUBLIC_URLS = { "**"};
	
}
