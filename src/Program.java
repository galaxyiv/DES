
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Program {

	private static byte[] makeKey(String key) throws UnsupportedEncodingException {
		byte[] keyByte = new byte[8];
		byte[] keyResult = key.getBytes("UTF-8");
		for (int i = 0; i < keyResult.length && i < keyByte.length; i++) {
			keyByte[i] = keyResult[i];
		}
		return keyByte;
	}

	private static byte[] coderByDES(byte[] plainText, String key, int mode)
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
			BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		SecureRandom sr = new SecureRandom();
		byte[] resultKey = makeKey(key);
		DESKeySpec desSpec = new DESKeySpec(resultKey);
		SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(desSpec);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(mode, secretKey, sr);
		return cipher.doFinal(plainText);
	}

	private static byte[] hexStr2ByteArr(String strIn) throws NumberFormatException {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	private static String byteArr2HexStr(byte[] arrB) {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	private static String encoderByDES(String plainText, String key) {
		try {
			byte[] result = coderByDES(plainText.getBytes("UTF-8"), key, Cipher.ENCRYPT_MODE);
			return byteArr2HexStr(result);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	private static String decoderByDES(String secretText, String key) {
		try {
			byte[] result = coderByDES(hexStr2ByteArr(secretText), key, Cipher.DECRYPT_MODE);
			return new String(result, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException {
		final String key = "1qaz";
		String decode = encoderByDES("大家好,我叫野原新之助;今年5岁了.我喜欢漂亮的大姐姐.", key) ;
		System.out.println(decode);
		
		System.out.println(decoderByDES(decode, key));
		

	}
}
