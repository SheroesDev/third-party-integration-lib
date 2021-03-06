package com.sheroes.thirdparty.utils;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.sheroes.thirdparty.model.ClaimResponse;
import com.sheroes.thirdparty.model.ThirdPartyClaim;
import com.sheroes.thirdparty.model.Token;
import com.sheroes.thirdparty.model.ValidationResponse;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

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
				// Doing the actual encryption
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

	public static ThirdPartyClaim decrypt(Token tokenParse) {

		ThirdPartyClaim claims = new ThirdPartyClaim();

		if (tokenParse.getToken() == null) {
			return null;
		}
		try {

			EncryptedJWT jwt = EncryptedJWT.parse(tokenParse.getToken());
			RSAPrivateKey privateRsaKey = (RSAPrivateKey) PrivateKeyReader.get();
			RSADecrypter decrypter = new RSADecrypter(privateRsaKey);
			jwt.decrypt(decrypter);
			Map<String, Object> claimMap = jwt.getJWTClaimsSet().getClaims();
			claims = getThirdPartyClaim(claimMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return claims;
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

	private static ThirdPartyClaim getThirdPartyClaim(Map<String, Object> claimMap) {
		ThirdPartyClaim claims = new ThirdPartyClaim();
		claims.setApikey(claimMap.get("apiKey") != null ? claimMap.get("apiKey").toString() : null);
		claims.setApiSecret(claimMap.get("apiSecret") != null ? claimMap.get("apiSecret").toString() : null);
		claims.setFirstName(claimMap.get("firstName") != null ? claimMap.get("firstName").toString() : null);
		claims.setLastName(claimMap.get("lastName") != null ? claimMap.get("lastName").toString() : null);
		claims.setMobile(claimMap.get("mobile") != null ? claimMap.get("mobile").toString() : null);
		claims.setEmail(claimMap.get("email") != null ? claimMap.get("email").toString() : null);
		claims.setLang(claimMap.get("lang") != null ? claimMap.get("lang").toString() : null);
		claims.setLocation(claimMap.get("loc") != null ? claimMap.get("loc").toString() : null);
		return claims;
	}
}
