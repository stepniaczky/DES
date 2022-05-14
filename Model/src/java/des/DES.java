package des;

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

    /**
     * Generates 16 keys for all rounds of DES
     *
     * @param key 64-bits key in Array representation of int values
     * @return 16 x 48-bits keys in Array representation of int values
     */
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

    /**
     * Core function of this class that manage whole encryption / decryption
     *
     * @return Encrypted String in hexadecimal representation
     * or Decrypted String encoded in ASCII
     */
    public String encrypt() {
        // List of Arrays of int values that represents 64-bits blocks of plain text
        List<int[]> blocks64bits = new ArrayList<>();


        // (encryption) Checks if plain text can be divided into equal 64-bits blocks
        // If not, it adds char of "~" on the beginning of text
        if (!isEncrypted) {
            int remainder = plainText.length() % 8;
            if (remainder != 0) {
                for (int i = 0; i < (8 - remainder); i++)
                    plainText = "~" + plainText;
            }
        }

        byte[] bytePlainText;
        if (!isEncrypted) {
            // Converts standard UTF-8 to byte Array (encryption)
            bytePlainText = plainText.getBytes(StandardCharsets.UTF_8);
        } else {
            // Converts hex String to an array of byte values (decryption)
            bytePlainText = HexFormat.of().parseHex(plainText);
        }

        // Creates 64-bits blocks of plain text and adds them to the list
        for (int i = 0; i < bytePlainText.length; i += 8) {
            byte[] oneBlock = new byte[8];
            System.arraycopy(bytePlainText, i, oneBlock, 0, 8);
            blocks64bits.add(ByteArrToIntArr(oneBlock));
        }

        // Binary key value in Array representation of int values
        byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
        int[] binKey = ByteArrToIntArr(byteKey);

        // Encrypting / Decrypting process on each 64-bits block
        for (int i = 0; i < blocks64bits.size(); i++) {
            blocks64bits.set(i, encodeBlock(blocks64bits.get(i), binKey, isEncrypted));
        }

        // (decrypting) Flag on first block of plain text
        boolean isFirstBlock = true;

        // Creates encrypted / decrypted String of all blocks that will be returned
        StringBuilder encrypted = new StringBuilder();
        for (int[] block : blocks64bits) {
            byte[] bytes = new byte[8];
            for (int i = 0; i < 8; i++) {
                StringBuilder oneByte = new StringBuilder();
                for (int j = 0; j < 8; j++) {
                    oneByte.append(block[(8 * i) + j]);
                }
                // Decimal value of sign in ASCII table
                int decimal = Integer.parseInt(oneByte.toString(), 2);
                bytes[i] = (byte) decimal;
            }
            if (!isEncrypted)
                // (encrypting) String in hexadecimal representation
                encrypted.append(byteArrayToHex(bytes));
            else {
                // (decrypting) Removing additional padding in first block
                // Rest of bytes gets encoded in UTF-8
                if (isFirstBlock) {
                    String[] s = new String(bytes, StandardCharsets.UTF_8).split("");
                    StringBuilder result = new StringBuilder();
                    for (String n : s) {
                        if (!Objects.equals(n, "~"))
                            result.append(n);
                    }
                    encrypted.append(result);
                    isFirstBlock = false;
                }
                else {
                    encrypted.append(new String(bytes, StandardCharsets.UTF_8));
                }
            }
        }

        return encrypted.toString();
    }


    /**
     * Main part of DES algorithm
     *
     * @param block       Block of given plain text in 64-bits Array representation
     * @param key         Given key in binary representation (Array of int values)
     * @param isEncrypted A Boolean value that specifies if it will be encryption or decryption
     * @return An encrypted / decrypted 64-bits block of int values
     */
    public static int[] encodeBlock(int[] block, int[] key, boolean isEncrypted) {
        // Generating 48-bits keys for all 16 rounds
        int[][] key_48 = generateRoundKeys(key);

        // Initial permutation on block of plain text
        int[] permutedBlock = Permute(block, IP);

        // Dividing 64-bits block in 2 halves: LPT and RPT
        int[] leftHalf = new int[32];
        int[] rightHalf = new int[32];
        System.arraycopy(permutedBlock, 0, leftHalf, 0, 32);
        System.arraycopy(permutedBlock, 32, rightHalf, 0, 32);

        int[] temp = new int[32];
        int[] fOutput;

        // does all 16 rounds of DES
        for (int i = 0; i < 16; i++) {

            if (!isEncrypted) {
                fOutput = f(rightHalf, key_48[i]);
            } else {
                fOutput = f(rightHalf, key_48[15 - i]);
            }

            // 32-bits LPT is XORed with 32-bits P-Box output from P-Box Permutation
            leftHalf = Xor(leftHalf, fOutput);

            // swaps left and right halves
            System.arraycopy(leftHalf, 0, temp, 0, 32);
            System.arraycopy(rightHalf, 0, leftHalf, 0, 32);
            System.arraycopy(temp, 0, rightHalf, 0, 32);

        }

        // Rejoin 2 halves after DES to 64-bits block
        int[] result = new int[64];
        System.arraycopy(rightHalf, 0, result, 0, 32);
        System.arraycopy(leftHalf, 0, result, 32, 32);

        // Final Permutation
        return Permute(result, reversedIP);
    }

    /**
     * Feistel cipher
     *
     * @param RPT 32-bits RPT of the given round
     * @param key 48-bits key of the given round
     * @return 32-bits output of P-Box Permutation
     */
    private static int[] f(int[] RPT, int[] key) {

        // Expansion Permutation: 32-bits RTP => 48-bits RTP
        int[] expandedRPT = Permute(RPT, E);

        // 48-bits RPT XORed 48-bits key
        int[] xored = Xor(expandedRPT, key);

        // S-Box substitution: 48-bits RPT => 32-bits RPT (as same size as LPT size)
        int[] sboxed = SBoxSubstitution(xored);

        // P-Box Permutation
        return Permute(sboxed, P);
    }


    /**
     * Generic Permutation
     *
     * @param input            An Array of ints values that will be permuted
     * @param permutationTable Permutation Table of byte values
     * @return Permuted Array with size of Permutation Table
     */
    public static int[] Permute(int[] input, byte[] permutationTable) {
        int[] result = new int[permutationTable.length];
        for (int i = 0; i < permutationTable.length; i++)
            result[i] = input[permutationTable[i] - 1];
        return result;
    }

    /**
     * Both arrays should be the same length
     *
     * @param t1 first Array of int values
     * @param t2 second Array of int values
     * @return First Array XORed with Second Array
     */
    private static int[] Xor(int[] t1, int[] t2) {
        int[] result = new int[t1.length];
        for (int i = 0; i < t1.length; i++) {
            result[i] = t1[i] ^ t2[i];
        }
        return result;
    }

    /**
     * S-Box Substituion
     *
     * @param RPT 48-bits
     * @return RPT 32-bits
     */
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

            // Update result array
            System.arraycopy(bin, 0, result, (4 * i), 4);
        }

        return result;
    }

    /**
     * Circular shift
     *
     * @param input An 28-bits Array that will be shifted
     * @param n     Number of shifts to the left
     * @return Shifted Array of 28-bits
     */
    private static int[] rotateLeft(int[] input, int n) {
        int[] result = new int[input.length];
        System.arraycopy(input, 0, result, 0, input.length);
        for (int i = 0; i < n; i++) {
            int firstBit = result[0];
            // left shift by 1 shift bit
            System.arraycopy(result, 1, result, 0, input.length - 1);
            result[input.length - 1] = firstBit;
        }

        return result;
    }
}
