package des;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Subkeys {

    private byte[] PC1 = new byte[] { 57, 49, 41, 33, 25, 17,  9,
                                        1, 58, 50, 42, 34, 26, 18,
                                        10,  2, 59, 51, 43, 35, 27,
                                        19, 11,  3, 60, 52, 44, 36,
                                        63, 55, 47, 39, 31, 23, 15,
                                        7, 62, 54, 46, 38, 30, 22,
                                        14,  6, 61, 53, 45, 37, 29,
                                        21, 13,  5, 28, 20, 12,  4 };

    private byte[] PC2 = new byte[] { 14, 17, 11, 24,  1,  5,
                                        3, 28, 15,  6, 21, 10,
                                        23, 19, 12,  4, 26,  8,
                                        16,  7, 27, 20, 13,  2,
                                        41, 52, 31, 37, 47, 55,
                                        30, 40, 51, 45, 33, 48,
                                        44, 49, 39, 56, 34, 53,
                                        46, 42, 50, 36, 29, 32 };

    private byte[] ShiftTable = new byte[]
            { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

    private byte[] GetPermuted(byte[] original, byte[] permutation)
    {
        byte[] permuted = new byte[permutation.length];

        for (int i = 0; i < permutation.length; i++)
        {
            permuted[i] = original[permutation[i] - 1];
        }

        return permuted;
    }

    private byte[] GetRightHalf(byte[] source)
    {
        int length = source.length / 2;
        byte[] rightHalf = new byte[length];

        for (int i = 0; i < length; i++)
        {
            rightHalf[i] = source[i + length];
        }

        return rightHalf;
    }

    private byte[] GetLeftHalf(byte[] source)
    {
        int length = source.length / 2;
        byte[] leftHalf = new byte[length];

        for (int i = 0; i < length; i++)
        {
            leftHalf[i] = source[i];
        }

        return leftHalf;
    }

    private byte[] Concat(byte[] left, byte[] right)
    {
        byte[] concat = new byte[left.length + right.length];
        int j = 0;
        for (int i = 0; i < left.length; i++, j++)
        {
            concat[j] = left[i];
        }
        for (int i = 0; i < right.length; i++, j++)
        {
            concat[j] = right[i];
        }

        return concat;
    }

    private byte[] ShiftLeft(byte[] toShift, byte v)
    {
        byte[] shifted = new byte[toShift.length];
        shifted = toShift;
//        Array.clone(toShift, shifted, toShift.length);

        for (int i = 0; i < v; i++)
        {
            shifted = ShiftLeft(shifted);
        }

        return shifted;
    }

    private byte[] ShiftLeft(byte[] toShift)
    {
        byte[] shifted = new byte[toShift.length];
        byte first = toShift[0];

        for (int i = 0; i < toShift.length - 1; i++)
        {
            shifted[i] = toShift[i + 1];
        }

        shifted[toShift.length - 1] = first;

        return shifted;
    }

    private byte[][] GenerateSubkeys (byte[] originalKey) {
        byte[] permutedKey = GetPermuted(originalKey, PC1); //pc1???
        byte[] leftKey = GetLeftHalf(permutedKey);
        byte[] rightKey = GetRightHalf(permutedKey);

        byte[][] subkeys = new byte[16][];
        byte[] temp = new byte[permutedKey.length];
        for (int i = 0; i < 16; i++)
        {
            leftKey = ShiftLeft(leftKey, ShiftTable[i]);
            rightKey = ShiftLeft(rightKey, ShiftTable[i]);
            temp = Concat(leftKey, rightKey);
            subkeys[i] = GetPermuted(temp, PC2);
        }

        return subkeys;
    }
}
