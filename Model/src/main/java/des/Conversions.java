package des;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Conversions {

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    public static String byteArrayToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8).toLowerCase();
    }

    public static byte[] hexStringToByteArray(String s) {
        if (s.length() % 2 != 0) {
            System.out.println("error: hexstringtobytearray");
        }
        s = s.toUpperCase();
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String ByteToBin(byte oneByte, int nr) {
        StringBuilder s = new StringBuilder(String.format("%s", Integer.toBinaryString(oneByte & 0xFF)));
        int length = nr - s.length();
        for (int i = 0; i < length; i++) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    public static int[] BinStringToIntArr(String b) {
        int[] result = new int[b.length()];
        for(int i = 0; i < b.length(); i++) {
            result[i] = Integer.parseInt(b.split("")[i]);
        }

        return result;
    }

    public static int[] ByteArrToIntArr(byte[] input) {
        int[] result = new int[input.length * 8];
        for(int i = 0; i < input.length; i++) {
            String[] b = ByteToBin(input[i], 8).split("");
            for(int j = 0; j < 8; j++){
                result[(8 * i) + j] = Integer.parseInt(b[j]);
            }
        }
        return result;
    }

    public static String setBitAt (String oneByte, int index, char value) {
        StringBuilder result = new StringBuilder(oneByte);
        result.setCharAt(7 - index, value);
        return result.toString();
    }



}

