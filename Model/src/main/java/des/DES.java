package des;

import com.google.common.io.BaseEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static des.Conversions.*;
import static des.Tables.*;

public class DES {

    private String plainText;
    private final String key;
    private final boolean isEncrypted;

    public DES(String plainText, String key, boolean isEncrypted) {
        this.plainText = plainText;
        this.key = key;
        this.isEncrypted = isEncrypted;
    }

    private byte[] GetRightHalf(byte[] source) {
        int length = source.length / 2;
        byte[] rightHalf = new byte[length];

        for (int i = 0; i < length; i++) {
            rightHalf[i] = source[i + length];
        }

        return rightHalf;
    }

    private byte[] GetLeftHalf(byte[] source) {
        int length = source.length / 2;
        byte[] leftHalf = new byte[length];

        for (int i = 0; i < length; i++) {
            leftHalf[i] = source[i];
        }

        return leftHalf;
    }

    public static int[][] generateRoundKeys(int[] key) {
        // Permutation Choice 1: 64-bits => 56-bits
        int[] permutedChoice1 = Permute(key, PC1);

        // Dividing 56-bits output of PC1 into 2 halves
        int[] leftHalf = new int[28];
        int[] rightHalf = new int[28];
        System.arraycopy(permutedChoice1, 0, leftHalf, 0, 28);
        System.arraycopy(permutedChoice1, 28, rightHalf, 0, 28);

        int[][] roundKeys = new int[16][48];

        // generates all 16 48-bits keys
        for (int i = 0; i < 16; i++) {
            // Circular left shifts performed on each halves
            leftHalf = rotateLeft(leftHalf, iterationShift[i]);
            rightHalf = rotateLeft(rightHalf, iterationShift[i]);

            // Rejoining 2 halves
            int[] temp = new int[56];
            System.arraycopy(leftHalf, 0, temp, 0, 28);
            System.arraycopy(rightHalf, 0, temp, 28, 28);

            // Compression Permutation: 56-bits => 48-bits
            roundKeys[i] = Permute(temp, PC2);
        }

        return roundKeys;
    }

    public String encrypt() throws DecoderException {
        List<int[]> blocks64bits = new ArrayList<>();


        // dodanie uzupelnienia bloku do 64 bitow jesli potrzeba
        if (!isEncrypted) {
            int remainder = plainText.length() % 8;
            if (remainder != 0) {
                for (int i = 0; i < (8 - remainder); i++)
                    plainText = "~" + plainText;
            }
        }

        // zamiana stringa na bajty
        byte[] bytePlainText;
        if(!isEncrypted) {
            bytePlainText = plainText.getBytes(StandardCharsets.UTF_8);
        } else {
            bytePlainText = hexStringToByteArray(plainText);
        }


        // tworzenie blokow skladajacych sie z 64 bitow i dodawanie ich do listy
        for (int i = 0; i < bytePlainText.length; i += 8) {
            byte[] oneBlock = new byte[8];
            System.arraycopy(bytePlainText, i, oneBlock, 0, 8);
            blocks64bits.add(ByteArrToIntArr(oneBlock));
        }

        // binary representation of key
        byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
        int[] binKey = ByteArrToIntArr(byteKey);

        // zaszyfrowanie kolejnych blokow
        for (int i = 0; i < blocks64bits.size(); i++) {
            blocks64bits.set(i, encodeBlock(blocks64bits.get(i), binKey, isEncrypted));
        }

        // do odszyfrowania
        boolean isFirstBlock = true;

        StringBuilder encrypted = new StringBuilder();
        for (int[] block : blocks64bits) {
            byte[] bytes = new byte[8];
            for (int i = 0; i < 8; i++) {
                StringBuilder oneByte = new StringBuilder();
                for (int j = 0; j < 8; j++) {
                    oneByte.append(block[(8 * i) + j]);
                }
                int decimal = Integer.parseInt(oneByte.toString(), 2);
                bytes[i] = (byte) decimal;
            }
            if(!isEncrypted)
                encrypted.append(byteArrayToHex(bytes));
            else {
                if(isFirstBlock) {
                    String[] s = new String(bytes).split("");
                    StringBuilder result = new StringBuilder();
                    for(String n : s) {
                        if(!Objects.equals(n, "~"))
                            result.append(n);
                    }
                    encrypted.append(result);
                    isFirstBlock = false;
                } else {
                    encrypted.append(new String(bytes));
              }
            }
        }

        return encrypted.toString();
    }


    public static int[] encodeBlock(int[] block_64, int[] key, boolean isEncrypted) {
        // Generating keys for all 16 rounds
        int[][] key_48 = generateRoundKeys(key);

        // Initial permutation
        int[] permutedBlock = Permute(block_64, IP);

        // Dividing 64-bits block in 2 halves: LPT and RPT
        int[] leftHalf = new int[32];
        int[] rightHalf = new int[32];
        System.arraycopy(permutedBlock, 0, leftHalf, 0, 32);
        System.arraycopy(permutedBlock, 32, rightHalf, 0, 32);

        int[] temp = new int[32];
        int[] fOutput = new int[32];
        // does all 16 rounds of DES
        for (int i = 0; i < 16; i++) {

            if(!isEncrypted) {
                fOutput = f(rightHalf, key_48[i]);
            } else {
                fOutput = f(rightHalf, key_48[15 - i]);
            }

            // 32-bits LPT is XORed with 32-bits P-Box output from Feistel
            leftHalf = Xor(leftHalf, fOutput);

            // swaps left and right halves
            System.arraycopy(leftHalf, 0, temp, 0, 32);
            System.arraycopy(rightHalf, 0, leftHalf, 0, 32);
            System.arraycopy(temp, 0, rightHalf, 0, 32);

        }

        int[] result = new int[64];
        System.arraycopy(rightHalf, 0, result, 0, 32);
        System.arraycopy(leftHalf, 0, result, 32, 32);
        return Permute(result, reversedIP);
    }


    // Feistel function => return 32-bits output of P-Box Permutation
    private static int[] f(int[] RPT, int[] key_48) {
        // Expansion Permutation: 32-bits RTP => 48-bits RTP
        int[] expandedRPT = Permute(RPT, E);

        // 48-bits RPT XORed 48-bits key
        int[] xored = Xor(expandedRPT, key_48);

        // S-Box substitution: 48-bits RPT => 32-bits RPT (as same size as LPT size)
        int[] sboxed = SBoxSubstitution(xored);

        // P-Box Permutation
        return Permute(sboxed, P);
    }


    public static int[] Permute(int[] input, byte[] permutationTable) {
        int[] result = new int[permutationTable.length];
        for (int i = 0; i < permutationTable.length; i++)
            result[i] = input[permutationTable[i] - 1];
        return result;
    }


    private static int[] Xor(int[] t1, int[] t2) {
        int[] result = new int[t1.length];
        for (int i = 0; i < t1.length; i++) {
            result[i] = t1[i] ^ t2[i];
        }
        return result;
    }

    private static int[] SBoxSubstitution(int[] RPT) {
        int[] result = new int[32];

        // dividing into 8 blocks, each with 6-bits size
        for (int i = 0; i < 8; i++) {

            // outer elements values equals to binary number of row of S-Box table
            int[] row = {RPT[6 * i],
                    RPT[(6 * i) + 5]};

            // inner elements values equals to binary number of column of S-Box table
            int[] column = {RPT[(6 * i) + 1],
                    RPT[(6 * i) + 2],
                    RPT[(6 * i) + 3],
                    RPT[(6 * i) + 4]};

            // Binary number of row and column of S-Box table
            String binRow = "" + row[0] + row[1];
            String binColumn = "" + column[0] + column[1] + column[2] + column[3];

            // Decimal number of row and column of S-Box table
            int decRow = Integer.parseInt(binRow, 2);
            int decColumn = Integer.parseInt(binColumn, 2);

            // Converting element from S-Box table to binary arr with eventually padding
            byte nr = SBox[i][(decRow * 16) + decColumn];
            int[] bin = BinStringToIntArr(ByteToBin(nr, 4));

            for (int j = 0; j < 4; j++) {
                result[(4 * i) + j] = bin[j];
            }
        }

        return result;
    }

    private static int[] rotateLeft(int[] input, int n) {
        int[] result = new int[input.length];
        System.arraycopy(input, 0, result, 0, input.length);
        for (int i = 0; i < n; i++) {
            int firstBit = result[0];
            for (int j = 0; j < input.length - 1; j++) {
                result[j] = result[j + 1];
            }
            result[input.length - 1] = firstBit;
        }

        return result;
    }
}
