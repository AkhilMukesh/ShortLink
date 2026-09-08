package com.shortlink.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class Base62ShortCodeGenerator implements ShortCodeGenerator {

    private static final String CHARACTERS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int CODE_LENGTH = 7;

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {

        StringBuilder code =
                new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {

            int index =
                    random.nextInt(CHARACTERS.length());

            code.append(
                    CHARACTERS.charAt(index)
            );
        }

        return code.toString();
    }
}