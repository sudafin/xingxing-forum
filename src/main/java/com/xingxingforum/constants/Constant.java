package com.xingxingforum.constants;

import java.time.Duration;

public interface Constant {
    Duration JWT_REFRESH_TTL = Duration.ofDays(7);

    interface Code{
        int SUCCESS = 200;
        int FAIL = 500;
        int ERROR = 404;
        int PARAM_ERROR = 400;
        int NO_AUTH = 401;
        int NO_PERMISSION = 403;
        int NO_LOGIN = 405;
    }
}
