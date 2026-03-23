package com.escuela.techcup.core.util;

import java.util.UUID;

public final class IdGeneratorUtil {

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
    
}
