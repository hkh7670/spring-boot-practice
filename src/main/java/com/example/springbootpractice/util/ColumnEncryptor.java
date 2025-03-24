package com.example.springbootpractice.util;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

@Converter
public class ColumnEncryptor implements AttributeConverter<String, String> {

    private static final String key = "oingisprettyintheworld1234567890"; // 암호화에 사용할 키 (16, 24, 32 bytes)

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    @PostConstruct
    public void init() throws Exception {
        encryptCipher = Cipher.getInstance("AES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, generateMySQLAESKey(key, "UTF-8"));
        decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, generateMySQLAESKey(key, "UTF-8"));
    }

    /**
     * 필드의 값을 데이터베이스 칼럼 값으로 변환하는 코드를 여기 작성합니다.
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return new String(Hex.encodeHex(
                encryptCipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8)))).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 칼럼 값을 필드의 값으로 변환하는 코드를 여기 작성합니다.
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return new String(decryptCipher.doFinal(Hex.decodeHex(dbData.toCharArray())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for (byte b : key.getBytes(encoding)) {
                finalKey[i++ % 16] ^= b;
            }
            return new SecretKeySpec(finalKey, "AES");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}