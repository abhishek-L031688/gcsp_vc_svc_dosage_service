package com.lilly.vclaudia.service.dosage.handler;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lilly.vclaudia.service.dosage.exception.ExceptionList;
import com.lilly.vclaudia.service.dosage.exception.ServiceException;
import com.lilly.vclaudia.service.dosage.model.ValidationError;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

/**
 * Service request handler class which contains common methods used 
 * for all the dosage endpoints.
 * 
 * @author cramaswamy
 *
 */
public abstract class AbsractRequestHandler {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(AbsractRequestHandler.class);

	/**
	 * Authenticates the service request. Validates headers and JWT token.
	 * 
	 * @param jwtBearerToken
	 * @param xForwardedProto
	 * @throws Exception
	 */
	public void authenticateServiceRequest(String jwtBearerToken, String xForwardedProto) throws Exception {
		if (null != System.getenv("NEW_RELIC_APP_NAME") && !(System.getenv("NEW_RELIC_APP_NAME").contains("dev"))
				&& !(System.getenv("NEW_RELIC_APP_NAME").contains("tst"))) {
			// Check to see if the request came in as HTTPS SSL or missing
			// header
			// completely. If not, error out immediately.
			if (xForwardedProto == null || !xForwardedProto.equals("https")) {
				LOGGER.error("Header X-Forwarded-Proto is either null or not https");
				throw new ServiceException(ExceptionList.HTTPSERROR);
			}
			// Check to see if we got an Authorization HTTP Header if not, error
			// out immediately.
			if (jwtBearerToken == null) {
				LOGGER.error("Authorization header is null");
				throw new ServiceException(ExceptionList.TOKENERROR);
			}
			// Strip out the "Bearer" portion of the token string.
			final String jwtToken = jwtBearerToken.replace("Bearer ", "");

			validateJWTToken(jwtToken);

		}
	}

	/**
	 * Validates JWT token.
	 * 
	 * @param jwtToken
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	private void validateJWTToken(final String jwtToken) throws Exception {
		try {
			ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
			JWKSource keySource = new RemoteJWKSet(new URL(System.getenv("JKU_URL")));
			JWSAlgorithm expectedJWSAlg = JWSAlgorithm.ES512;
			JWSKeySelector keySelector = new JWSVerificationKeySelector(expectedJWSAlg, keySource);
			jwtProcessor.setJWSKeySelector(keySelector);
			SecurityContext ctx = null;
			try {
				jwtProcessor.process(jwtToken, ctx);
			} catch (Exception e) {
				LOGGER.error("Error occurred in processing JWT Token");
				throw new ServiceException(ExceptionList.TOKENERROR);
			}

			JWTClaimsSet claimsSet = jwtProcessor.process(jwtToken, ctx);
			// If claimsSet is valid, it won't be null. If it's invalid, it'll be null.
			// If claimsSet is NOT NULL, check for value of "csp_api" of key "aud".
			if (claimsSet.toJSONObject() == null || !claimsSet.toJSONObject().containsValue("csp_api")) {
				LOGGER.error("Claimset JsonObject is null or it contains value csp_api");
				throw new ServiceException(ExceptionList.TOKENERROR);
			}
		} catch (Exception e) {
			LOGGER.error("Validation of JWT token failed", e);
			throw e;
		}
	}
	
	/**
	 * Request validation exception handler.
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError handleException(MethodArgumentNotValidException exception) {
		final BindingResult errors = exception.getBindingResult();
		ValidationError error = new ValidationError("Validation failed. " + errors.getErrorCount() + " error(s)");
        for (ObjectError objectError : errors.getAllErrors()) {
            error.addValidationError(objectError.getDefaultMessage());
        }
        return error;
    }
	
}
