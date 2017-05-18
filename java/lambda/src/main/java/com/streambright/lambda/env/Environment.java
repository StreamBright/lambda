package com.streambright.lambda.env;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class Environment {
    private static Optional<String> getEnvKey(String key_name) {
        try {
            return Optional.of(System.getenv(key_name));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static String getRegion() {
        Optional<String> regionOptional = getEnvKey("region");
        return (regionOptional.isPresent() ? regionOptional.get() : "");
    }

    public static String getRdsConfig(String region) {
        Optional<String> rdsConfigOptional = getEnvKey(region);
        return (rdsConfigOptional.isPresent() ? new String(Base64.getDecoder().decode(rdsConfigOptional.get()), StandardCharsets.UTF_8) : "");
    }
}
