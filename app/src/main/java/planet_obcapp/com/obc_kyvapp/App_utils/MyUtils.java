package planet_obcapp.com.obc_kyvapp.App_utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class MyUtils {
    private final static String HEX = "0123456789ABCDEF";


    public static String encrypt(String cleartext, Context context)
            throws Exception {
        byte[] rawKey = getRawKey(context);
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    public static String decrypt(String encrypted, Context context)
            throws Exception {

        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(enc, context);
        return new String(result);
    }

    private static byte[] getRawKey(Context context) throws Exception {

        byte[] keyValue = generatekey(context);
        SecretKey key = new SecretKeySpec(keyValue, "AES");
        byte[] raw = key.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKey skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] encrypted, Context context)
            throws Exception {

        byte keyValue[] = getkey(context);

        SecretKey skeySpec = new SecretKeySpec(keyValue, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    static String getAlphaNumericString() {
        int n = 16;
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static String e(String text) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(Utility.seed);
        String encrypted = encryptor.encrypt(text);
        return encrypted;
    }

    public static String d(String encodedtext) {

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(Utility.seed);

        String decrypted = encryptor.decrypt(encodedtext);
        return decrypted;
    }

    static byte[] generatekey(Context context) {
        byte[] value = null;
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String val = preferences.getString(Utility.KEY_TESTING, "");
            if (val.equals("")) {
                String s = getAlphaNumericString();
                String val1 = s.substring(0, 4);
                String val2 = s.substring(4, 8);
                String val3 = s.substring(8, 12);
                String val4 = s.substring(12, 16);
                val1 = e(val1);//encry
                val2 = e(val2);//encry
                val3 = e(val3);//encry
                val4 = e(val4);//encry

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Utility.KEY_TESTING, val1);
                editor.putString(Utility.KEY_PLANET, val2);
                editor.putString(Utility.KEY_REPORT, val3);
                editor.putString(Utility.KEY_LAUGH, val4);
                editor.commit();
                value = getkey(context);
            } else {
                value = getkey(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;

    }

    static byte[] getkey(Context context) {
        byte[] value = null;
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String val1 = preferences.getString(Utility.KEY_TESTING, "");
            String val2 = preferences.getString(Utility.KEY_PLANET, "");
            String val3 = preferences.getString(Utility.KEY_REPORT, "");
            String val4 = preferences.getString(Utility.KEY_LAUGH, "");
            val1 = d(val1);//dec
            val2 = d(val2);//dec
            val3 = d(val3);//dec
            val4 = d(val4);//dec
            String val = val1 + val2 + val3 + val4;
            value = val.getBytes("UTF-8");
        } catch (Exception e) {
            e.getMessage();
        }
        return value;
    }
}
