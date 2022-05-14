package des;

public class Conversions {

    // Converts an Array of bytes to hex String
    // Checks if every hex value has length of 2
    // If not this element gets an extra 0xFF
    public static String byteArrayToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // Converts 1 byte to String form with complement to a given number <n>
    // e.g. nr = 8: (byte) 10 => (String) "00001010"
    public static String ByteToBin(byte oneByte, int nr) {
        StringBuilder s = new StringBuilder(String.format("%s", Integer.toBinaryString(oneByte & 0xFF)));
        int length = nr - s.length();
        for (int i = 0; i < length; i++) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    // Converts 1 String of byte value to int array of binary values, e.g "10101010" => int[8]
    public static int[] BinStringToIntArr(String b) {
        int[] result = new int[b.length()];
        for (int i = 0; i < b.length(); i++) {
            result[i] = Integer.parseInt(b.split("")[i]);
        }

        return result;
    }

    // Converts byte array to int array of binary values, e.g. byte[8] => int[64]
    public static int[] ByteArrToIntArr(byte[] input) {
        int[] result = new int[input.length * 8];
        for (int i = 0; i < input.length; i++) {
            String[] b = ByteToBin(input[i], 8).split("");
            for (int j = 0; j < 8; j++) {
                result[(8 * i) + j] = Integer.parseInt(b[j]);
            }
        }
        return result;
    }
}

