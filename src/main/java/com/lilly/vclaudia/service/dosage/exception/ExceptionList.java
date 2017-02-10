package com.lilly.vclaudia.service.dosage.exception;

/**
 * Common error codes and error messages.
 * 
 * @author cramaswamy
 *
 */
public class ExceptionList {
	
	public static final String ILCCODE="The application is not supported in your country.";
	public static final String ILLOCCODE="Your preferred language is not currently supported by this application. Please choose from the following supported languages.";
	public static final String GENERICCODE="Application Error Occured";
	public static final String VERSIONNOTFOUND="Version not found.";
	public static final String CONNISSUES="DB could not be connected";
	public static final String TOKENERROR="Invalid or Null Token";
	public static final String HTTPSERROR = "Not Authorized";
	
	public static final String CUST_UNKNOWN_ERROR="10301";
	public static final String NOT_SUPPORTED_IN_CNTRY="10001";
	public static final String NOT_SUPPORTED_LANG="10002";
	public static final String CUST_VERSIONNOTFOUND="10206";
	public static final String INVALID_UNKNOWN_TOKEN="10207";
	public static final String HTTPS_ERROR="10102";
}
