package com.keepthinker.wavemessaging.core.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CryptoUtils {

    /**
     * ${src}-->sha256-->md5-->${hashValue}
     *
     * @param src
     * @return
     */
    public static String hash(String src) {
        MessageDigest sha256 = DigestUtils.getSha256Digest();
        byte[] r1 = sha256.digest(src.getBytes(StandardCharsets.UTF_8));
        MessageDigest md5 = DigestUtils.getMd5Digest();
        byte[] r2 = md5.digest(r1);
        return Hex.encodeHexString(r2);
    }
}
