package com.vitalykalenik.topsecret;

import org.junit.Test;

/**
 * @author Vitaly Kalenik
 */
public class EncodeTest {

    @Test
    public void test(){
        System.out.println(EncodeUtils.encodeSha256("qwer", "kcfyyjhqoj"));
    }
}
