package system.rdf.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class Encript {


    public static String encript(String textToEncript) throws IllegalStateException {
	MessageDigest md = null;
	try {
	    md = MessageDigest.getInstance("SHA"); // Instance of generator SHA-1
	}
	catch(NoSuchAlgorithmException e) {
	    throw new IllegalStateException(e.getMessage());
	}

	try {
	    md.update(textToEncript.getBytes("UTF-8")); // Generación de resumen de mensaje
	}
	catch(UnsupportedEncodingException e) {
	    throw new IllegalStateException(e.getMessage());
	}

	byte raw[] = md.digest(); // Obtención del resumen de mensaje
	String hash = (new BASE64Encoder()).encode(raw); // Converting to BASE64
	return hash;

    }
}
