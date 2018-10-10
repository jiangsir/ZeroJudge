package tw.zerojudge.Utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;

public class DES {

	/** 加密、解密key. */
	private final String PASSWORD_CRYPT_KEY;

	/** 加密算法 */
	private final String ALGORITHM = "DES/CBC/PKCS5Padding";

	public DES() {
		this.PASSWORD_CRYPT_KEY = ApplicationScope.getAppConfig().getCryptKey();
	}

	public DES(String PASSWORD_CRYPT_KEY) {
		if (PASSWORD_CRYPT_KEY == null) {
			this.PASSWORD_CRYPT_KEY = ApplicationScope.getAppConfig().getCryptKey();
		} else {
			this.PASSWORD_CRYPT_KEY = PASSWORD_CRYPT_KEY;
		}
	}

	/**
	 * 
	 * 对数据进行DES加密.
	 * 
	 * @param data
	 *            待进行DES加密的数据
	 * 
	 * @return 返回经过DES加密后的数据
	 * 
	 * @throws Exception
	 */
	public String decrypt(String data) throws DataException {
		try {
			return new String(decrypt(hex2byte(data.getBytes()), PASSWORD_CRYPT_KEY.getBytes()), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * 
	 * 对用DES加密过的数据进行解密.
	 * 
	 * @param data
	 *            DES加密数据
	 * 
	 * @return 返回解密后的数据
	 * 
	 * @throws Exception
	 */

	public String encrypt(String data) throws Exception {
		return byte2hex(encrypt(data.getBytes("UTF-8"), PASSWORD_CRYPT_KEY.getBytes()));
	}

	/**
	 * 
	 * 用指定的key对数据进行DES加密.
	 * 
	 * @param data
	 *            待加密的数据
	 * 
	 * @param key
	 *            DES加密的key
	 * 
	 * @return 返回DES加密后的数据
	 * 
	 * @throws Exception
	 */

	private byte[] encrypt(byte[] data, byte[] key) throws Exception {


		DESKeySpec dks = new DESKeySpec(key);



		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

		SecretKey securekey = keyFactory.generateSecret(dks);


		IvParameterSpec iv = new IvParameterSpec(key);


		Cipher cipher = Cipher.getInstance(ALGORITHM);


		cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);



		return cipher.doFinal(data);

	}

	/** */
	/**
	 * 
	 * 用指定的key对数据进行DES解密.
	 * 
	 * @param data
	 *            待解密的数据
	 * 
	 * @param key
	 *            DES解密的key
	 * 
	 * @return 返回DES解密后的数据
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * 
	 */

	private byte[] decrypt(byte[] data, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);
		IvParameterSpec iv = new IvParameterSpec(key);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
		return cipher.doFinal(data);

	}

	public byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("長度不是偶數");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public String byte2hex(byte[] b) {
		String hs = "";

		String stmp = "";

		for (int n = 0; n < b.length; n++) {

			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1)

				hs = hs + "0" + stmp;

			else

				hs = hs + stmp;

		}

		return hs.toUpperCase();

	}

}
