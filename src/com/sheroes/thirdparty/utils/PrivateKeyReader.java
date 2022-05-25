package com.sheroes.thirdparty.utils;

//import org.apache.commons.io.IOUtils;
//import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public final  class PrivateKeyReader {

	public static PrivateKey get()
			throws Exception {

		//InputStream inputStream = new ClassPathResource("keys/private").getInputStream();

		//InputStream inputStream = PublicKeyReader.class.getResourceAsStream("/public");
		InputStream inputStream = PrivateKeyReader.class.getResourceAsStream("/private");
		//byte[] keyBytes = Files.readAllBytes(file.toPath());
		//byte[] keyBytes = IOUtils.toByteArray(inputStream);
//		byte[] targetArray = new byte[inputStream.available()];
//		inputStream.read(targetArray);

        byte[] targetArray = KeyReader.toByteArray(inputStream);
		PKCS8EncodedKeySpec spec =
				new PKCS8EncodedKeySpec(targetArray);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}
}
