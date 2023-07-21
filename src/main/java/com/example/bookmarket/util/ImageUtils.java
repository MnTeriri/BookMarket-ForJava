package com.example.bookmarket.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageUtils {
    public static String encodeImageString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static InputStream decodeImageString(String imageString) {
        byte[] bytes = Base64.getDecoder().decode(imageString);
        return new ByteArrayInputStream(bytes);
    }
}
