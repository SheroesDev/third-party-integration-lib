package com.sheroes.thirdparty.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class KeyReader {

	public static byte[] toByteArray(InputStream in) throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		return os.toByteArray();
	}
}
