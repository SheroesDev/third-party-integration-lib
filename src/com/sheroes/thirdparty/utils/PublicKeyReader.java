package com.sheroes.thirdparty.utils;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public final class PublicKeyReader extends KeyReader {

	public static PublicKey get() throws Exception {
		InputStream inputStream = PublicKeyReader.class.getResourceAsStream("/public");
		byte[] targetArray = toByteArray(inputStream);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(targetArray);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}
}