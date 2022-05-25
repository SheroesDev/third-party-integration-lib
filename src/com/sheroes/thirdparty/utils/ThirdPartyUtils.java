package com.sheroes.thirdparty.utils;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.sheroes.thirdparty.model.ClaimResponse;
import com.sheroes.thirdparty.model.ThirdPartyClaim;
import com.sheroes.thirdparty.model.ValidationResponse;
import java.security.interfaces.RSAPublicKey;

public class ThirdPartyUtils {

	public static ClaimResponse encrypt(ThirdPartyClaim claims) {

		if (claims == null) {
			return null;
		}
		ClaimResponse claimResponse = new ClaimResponse();
		String encryptedValue = null;
		try {

			RSAPublicKey publicRsaKey = (RSAPublicKey) PublicKeyReader.get();
			ValidationResponse validationResponse = validateClaim(claims);
			if (validationResponse.isValid()) {
				JWTClaimsSet.Builder claimsSet = builderClaim(claims);
				JWEHeader header = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256CBC_HS512);
				EncryptedJWT jwt = new EncryptedJWT(header, claimsSet.build());
				RSAEncrypter encrypter = new RSAEncrypter(publicRsaKey);
				jwt.encrypt(encrypter);
				encryptedValue = jwt.serialize();
				claimResponse.setSuccess(true);
				claimResponse.setToken(encryptedValue);
			} else {

				claimResponse.setMessage(validationResponse.getMessage());
				claimResponse.setSuccess(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			claimResponse.setMessage("Error Occured" + e.getMessage());
			claimResponse.setSuccess(false);

		}
		return claimResponse;
	}

	private static ValidationResponse validateClaim(ThirdPartyClaim claim) {

		String message = "";
		boolean isValid = true;

		if (claim.getApikey() == null || claim.getApikey() == "") {
			message = "Please make sure that you have included the provided api key";
			isValid = false;

		} else if (claim.getApiSecret() == null || claim.getApiSecret() == "") {
			message = "Api secret is required";
			isValid = false;
		} else if (claim.getMobile() == null || claim.getMobile() == "") {
			message = "Mobile number is required";
			isValid = false;
		} else if (claim.getFirstName() == null || claim.getFirstName() == "") {
			message = "First Name is required";
			isValid = false;
		} else if (claim.getLocation() == null || claim.getLocation() == "") {
			message = "Location is required";
			isValid = false;
		}

		return new ValidationResponse(isValid, message);
	}

	private static JWTClaimsSet.Builder builderClaim(ThirdPartyClaim claims) {
		JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();
		claimsSet.claim("apiKey", claims.getApikey());
		claimsSet.claim("apiSecret", claims.getApiSecret());
		claimsSet.claim("firstName", claims.getFirstName());
		claimsSet.claim("lastName", claims.getLastName());
		claimsSet.claim("email", claims.getEmail());
		claimsSet.claim("mobile", claims.getMobile());
		claimsSet.claim("lang", claims.getLang());
		claimsSet.claim("loc", claims.getLocation());
		return claimsSet;
	}

}
