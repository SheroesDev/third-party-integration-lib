package com.sheroes.thirdparty.utils;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public final  class PrivateKeyReader extends KeyReader{

	public static PrivateKey get()
			throws Exception {

		InputStream inputStream = PrivateKeyReader.class.getResourceAsStream("/private");
        byte[] targetArray = toByteArray(inputStream);
		PKCS8EncodedKeySpec spec =
				new PKCS8EncodedKeySpec(targetArray);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}
}
