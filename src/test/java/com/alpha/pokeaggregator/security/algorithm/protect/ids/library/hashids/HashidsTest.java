package com.alpha.pokeaggregator.security.algorithm.protect.ids.library.hashids;


import java.util.Arrays;

import com.alpha.pokeaggregator.security.protect.ids.algorithm.library.Hashids;
import com.alpha.pokeaggregator.security.util.SecurityIdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
@ExtendWith(MockitoExtension.class)
public class HashidsTest {

    @Test
    public void test_large_nummber(){
        long num_to_hash = 9007199254740992L;
        Hashids a = new Hashids("this is my salt");
        String res = a.encode(num_to_hash);
        long[] b = a.decode(res);
        Assertions.assertEquals(num_to_hash, b[0]);
    }

    @Test
    public void test_large_number_notSupported() {
        long numToHash = 9007199254740993L;
        Hashids a = new Hashids("this is my salt");
        assertThrows(IllegalArgumentException.class, () -> {
            a.encode(numToHash);
        });
    }

    @Test
    public void testWrongDecoding(){
        Hashids a = new Hashids("this is my pepper");
        long[] b = a.decode("NkK9");
        Assertions.assertEquals(b.length, 0);
    }

    @Test
    public void testEncodeHex(){
        Hashids a = new Hashids("this is my salt");
        String valueHex=SecurityIdUtils.stringToHex("5");
        String encodeValueHex = a.encodeHex(valueHex);

        String decodeValueHex = a.decodeHex(encodeValueHex);
        String decodeValue = SecurityIdUtils.hexToString(decodeValueHex);

        Assertions.assertEquals("35", valueHex);
        Assertions.assertEquals("QD2", encodeValueHex);
        Assertions.assertEquals("35", decodeValueHex);
        Assertions.assertEquals("5", decodeValue);
    }

    @Test
    public void testOneNumber(){
        String expected = "NkK9", res;
        long numToHash = 12345L;
        long[] res2;
        Hashids a = new Hashids("this is my salt");
        res = a.encode(numToHash);
        Assertions.assertEquals(res, expected);
        res2 = a.decode(expected);
        Assertions.assertEquals(res2.length, 1);
        Assertions.assertEquals(res2[0], numToHash);
    }

    @Test
    public void testServeralNumbers(){
        String expected = "aBMswoO2UB3Sj", res;
        long[] numToHash = {683L, 94108L, 123L, 5L}, res2;
        Hashids a = new Hashids("this is my salt");
        res = a.encode(numToHash);
        Assertions.assertEquals(res, expected);
        res2 = a.decode(expected);
        Assertions.assertEquals(res2.length, numToHash.length);
        Assertions.assertTrue(Arrays.equals(res2, numToHash));
    }

    @Test
    public void testSpecifyingCustomHashLength(){
        String expected = "gB0NV05e", res;
        long numToHash = 1L;
        long[] res2;
        Hashids a = new Hashids("this is my salt", 8);
        res = a.encode(numToHash);
        Assertions.assertEquals(res, expected);
        res2 = a.decode(expected);
        Assertions.assertEquals(res2.length, 1);
        Assertions.assertEquals(res2[0], numToHash);
    }

    @Test
    public void testRandomness(){
        String expected = "1Wc8cwcE", res;
        long[] numToHash = {5L, 5L, 5L, 5L}, res2;
        Hashids a = new Hashids("this is my salt");
        res = a.encode(numToHash);
        Assertions.assertEquals(res, expected);
        res2 = a.decode(expected);
        Assertions.assertEquals(res2.length, numToHash.length);
        Assertions.assertTrue(Arrays.equals(res2, numToHash));
    }

    @Test
    public void testRandomnessForIncrementingNumbers(){
        String expected = "kRHnurhptKcjIDTWC3sx", res;
        long[] numToHash = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L}, res2;
        Hashids a = new Hashids("this is my salt");
        res = a.encode(numToHash);
        Assertions.assertEquals(res, expected);
        res2 = a.decode(expected);
        Assertions.assertEquals(res2.length, numToHash.length);
        Assertions.assertTrue(Arrays.equals(res2, numToHash));
    }

    @Test
    public void testRandomnessForIncrementing(){
        Hashids a;
        a = new Hashids("this is my salt");
        Assertions.assertEquals(a.encode(1L), "NV");
        Assertions.assertEquals(a.encode(2L), "6m");
        Assertions.assertEquals(a.encode(3L), "yD");
        Assertions.assertEquals(a.encode(4L), "2l");
        Assertions.assertEquals(a.encode(5L), "rD");
    }



    @Test
    public void testForValuesGreaterIntMaxval(){
        Hashids a = new Hashids("this is my salt");
        Assertions.assertEquals(a.encode(9876543210123L), "Y8r7W1kNN");
    }

    @Test
    public void testIssue10(){
        String expected = "3kK3nNOe", res;
        long numToHash = 75527867232l;
        long[] res2;
        Hashids a = new Hashids("this is my salt");
        res = a.encode(numToHash);
        Assertions.assertEquals(res, expected);
        res2 = a.decode(expected);
        Assertions.assertEquals(res2.length, 1);
        Assertions.assertEquals(res2[0], numToHash);
    }
}
