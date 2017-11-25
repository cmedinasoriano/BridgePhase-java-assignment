package com.bridgephase.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Helper {
	public static InputStream inputStreamFromString(String value) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(value.getBytes("UTF-8"));
	}
}
