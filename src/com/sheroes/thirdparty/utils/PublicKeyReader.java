package com.sheroes.thirdparty.utils;

//import org.apache.commons.io.IOUtils;
//import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public final class PublicKeyReader {

	public static PublicKey get()
			throws Exception {

	//	File file = new ClassPathResource("keys/public").getFile();
	//	InputStream inputStream = new ClassPathResource("keys/public").getInputStream();
		//	byte[] keyBytes = Files.readAllBytes(file.toPath());
//		byte[] keyBytes = IOUtils.toByteArray(inputStream);

		InputStream inputStream = PublicKeyReader.class.getResourceAsStream("/public");
//		byte[] targetArray = new byte[inputStream.available()];
//		inputStream.read(targetArray);

		byte[] targetArray = KeyReader.toByteArray(inputStream);
		X509EncodedKeySpec spec =
				new X509EncodedKeySpec(targetArray);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}
}