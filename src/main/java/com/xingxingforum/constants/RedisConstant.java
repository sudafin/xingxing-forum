package com.xingxingforum.constants;

import java.time.Duration;

public interface RedisConstant {
    Duration JWT_REMEMBER_ME_TTL = Duration.ofDays(7);
    Duration JWT_REFRESH_TTL = Duration.ofMinutes(30);
    String JWT_REDIS_KEY_PREFIX = "jwt:uid:";
    String PAYLOAD_JTI_KEY = "jti";
}
