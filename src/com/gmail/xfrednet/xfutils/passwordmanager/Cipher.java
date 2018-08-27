package com.gmail.xfrednet.xfutils.passwordmanager;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Cipher {

	private static final String PBKDF2_ALGORITHM  = "PBKDF2WithHmacSHA1";
	private static final int    PBKDF2_ITERATIONS = 1000;
	private static final int    PBKDF2_OUT_BYTES = 64;

	public static byte[] GenerateSalt(int saltSize) {
		byte[] salt = new byte[saltSize];
		SecureRandom random = new SecureRandom();

		random.nextBytes(salt);
		return salt;
	}

	public static byte[] HashPassword(char[] password, byte[] salt) {
		return pbkdf2(password, salt, PBKDF2_ITERATIONS, PBKDF2_OUT_BYTES);
	}
	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int outputBytes) {

		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, outputBytes * 8);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return null;
	}
}
