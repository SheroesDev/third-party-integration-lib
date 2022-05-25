package com.sheroes.thirdparty.utils;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.sheroes.thirdparty.model.ThirdPartyClaim;
import com.sheroes.thirdparty.model.Token;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

public class ThirdPartyUtils {

	public static Token encrypt(ThirdPartyClaim claims)  {

		String encryptedValue = null;
		try {

			RSAPublicKey publicRsaKey = (RSAPublicKey) PublicKeyReader.get();
			JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();
			claimsSet.claim("apiKey",claims.getApikey());
			claimsSet.claim("apiSecret",claims.getApiSecret());
			claimsSet.claim("firstName",claims.getFirstName());
			claimsSet.claim("lastName",claims.getLastName());
			claimsSet.claim("email",claims.getEmail());
			claimsSet.claim("mobile",claims.getMobile());
			claimsSet.claim("lang",claims.getLang());
			claimsSet.claim("loc",claims.getLocation());

			JWEHeader header = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256CBC_HS512);

			EncryptedJWT jwt = new EncryptedJWT(header, claimsSet.build());
			RSAEncrypter encrypter = new RSAEncrypter(publicRsaKey);
			// Doing the actual encryption
			jwt.encrypt(encrypter);

			encryptedValue = jwt.serialize();

		}catch (Exception e){
			e.printStackTrace();

		}
		finally {

		}

		return new Token(encryptedValue);

	}
	public static ThirdPartyClaim decrypt(Token tokenParse) {

		ThirdPartyClaim claims = new ThirdPartyClaim();

		try{

			EncryptedJWT jwt = EncryptedJWT.parse(tokenParse.getToken());

			RSAPrivateKey privateRsaKey = (RSAPrivateKey) PrivateKeyReader.get();
			RSADecrypter decrypter = new RSADecrypter(privateRsaKey);
			jwt.decrypt(decrypter);

			Map<String, Object> claimMap = jwt.getJWTClaimsSet().getClaims();

			claims.setApikey(String.valueOf(claimMap.get("apiKey")));
			claims.setApiSecret(String.valueOf(claimMap.get("apiSecret")));
			claims.setFirstName(String.valueOf(claimMap.get("firstName")));
			claims.setLastName(String.valueOf(claimMap.get("lastName")));
			claims.setMobile(String.valueOf(claimMap.get("mobile")));
			claims.setEmail(String.valueOf(claimMap.get("email")));
			claims.setLang(String.valueOf(claimMap.get("lang")));
			claims.setLocation(String.valueOf(claimMap.get("loc")));

//			claims.setFirstName(claimMap.get("firstName").toString());
//			ObjectMapper mapper = new ObjectMapper();
//			claims = mapper.convertValue(claimMap,ThirdPartyClaim.class);

		}catch (Exception e){
			e.printStackTrace();
		}finally {

		}


		return claims;
	}

	public static byte[] toByteArray(InputStream in) throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int len;

		// read bytes from the input stream and store them in the buffer
		while ((len = in.read(buffer)) != -1)
		{
			// write bytes from the buffer into the output stream
			os.write(buffer, 0, len);
		}

		return os.toByteArray();
	}
}
