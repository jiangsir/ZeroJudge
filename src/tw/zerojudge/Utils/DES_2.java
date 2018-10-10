package tw.zerojudge.Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class DES_2 {
	private String Algorithm = "DES";
	private KeyGenerator keygen;
	private SecretKey deskey;
	private Cipher c;
	private byte[] cipherByte;

	public DES_2() {
		init();
	}

	public void init() {
		try {
			keygen = KeyGenerator.getInstance(Algorithm);
			deskey = keygen.generateKey();
			c = Cipher.getInstance(Algorithm);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (NoSuchPaddingException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 對 String 進行加密
	 * 
	 */
	public byte[] createEncryptor(String str) {
		try {
			c.init(Cipher.ENCRYPT_MODE, deskey);
			cipherByte = c.doFinal(str.getBytes());
		} catch (java.security.InvalidKeyException ex) {
			ex.printStackTrace();
		} catch (javax.crypto.BadPaddingException ex) {
			ex.printStackTrace();
		} catch (javax.crypto.IllegalBlockSizeException ex) {
			ex.printStackTrace();
		}
		return cipherByte;
	}

	/**
	 * Byte 進行解密
	 * 
	 */
	public String createDecryptor(byte[] buff) {
		try {
			c.init(Cipher.DECRYPT_MODE, deskey);
			cipherByte = c.doFinal(buff);
		} catch (java.security.InvalidKeyException ex) {
			ex.printStackTrace();
		} catch (javax.crypto.BadPaddingException ex) {
			ex.printStackTrace();
		} catch (javax.crypto.IllegalBlockSizeException ex) {
			ex.printStackTrace();
		}
		return (new String(cipherByte));
	}
}
