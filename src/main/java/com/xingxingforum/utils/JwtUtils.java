package com.xingxingforum.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.xingxingforum.constants.ErrorInfo;
import com.xingxingforum.constants.RedisConstant;
import com.xingxingforum.entity.dto.users.UserDTO;
import com.xingxingforum.expcetions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Date;

import static com.xingxingforum.constants.Constant.JWT_REFRESH_TTL;

@Component
public class JwtUtils {

    @Value("${jwt.token.expirationTime:}")
    private long expirationTime;
    private final StringRedisTemplate stringRedisTemplate;
    private final JWTSigner jwtSigner;
    //生成密钥工具


    public JwtUtils(StringRedisTemplate stringRedisTemplate) {
        KeyPair keyPair = RSAUtils.generateKeyPair();
        this.jwtSigner = JWTSignerUtil.hs256(RSAUtils.getPrivateKey(keyPair).getBytes());
        this.stringRedisTemplate = stringRedisTemplate;
    }
    /**
     * 创建token
     * @param userDTO token需要携带的用户对象信息,如用户id姓名等
     * @return token
     */
    public String createToken(UserDTO userDTO){
                //将密钥算法加密生成签名,防止token被修改
        return JWT.create()
                .setPayload("payload", userDTO)
                .setExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .setSigner(jwtSigner)
                .sign();
    }

    //解析token
    public UserDTO parseToken(String token){
        Object payload = JWT.of(token).getPayload("payload");
        return BeanUtil.toBean(payload, UserDTO.class);
    }

    //验证token是否过期
    public boolean validateToken(String token){
        try {
            // 创建 JWTValidator 验证token时间实例
            JWTValidator validator = JWTValidator.of(token);
            // 验证签名算法
            validator.validateAlgorithm(jwtSigner);
            // 验证 token 是否过期
            validator.validateDate();

            // 如果验证通过，返回 true
            return true;
        } catch (Exception e) {
            // 如果验证失败，捕获异常并返回 false
            return false;
        }
    }

    //将验证token的真假和时间的过期结合一起
    public boolean checkToken(String token){
        return validateToken(token) && JWTUtil.verify(token, jwtSigner);
    }

    /**
     * 创建刷新token，并将token的JTI记录到Redis中
     *
     * @param userDTO 用户信息
     * @return 刷新token
     */
    public String createRefreshToken(UserDTO userDTO) {
        // 1.生成 JTI
        String jti = UUID.randomUUID().toString(true);
        // 2.生成jwt
        // 2.1 有效期7天
        Duration ttl = JWT_REFRESH_TTL;
        // 2.2.生成token
        String token = JWT.create()
                .setJWTId(jti)
                .setPayload("payload", userDTO)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
        // 3.缓存jti，有效期与token一致，过期或删除JTI后，对应的refresh-token失效
        stringRedisTemplate.opsForValue()
                .set(RedisConstant.JWT_REDIS_KEY_PREFIX + userDTO.getId(), jti, ttl);
        return token;
    }

    /**
     * 解析刷新token
     *
     * @param refreshToken 刷新token
     * @return 解析刷新token得到的用户信息
     */
    public UserDTO parseRefreshToken(String refreshToken) {
        // 1.校验token是否为空
        AssertUtils.isNotNull(refreshToken, ErrorInfo.Msg.INVALID_TOKEN);
        // 2.校验并解析jwt
        JWT jwt;
        try {
            jwt = JWT.of(refreshToken).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new BadRequestException(400, ErrorInfo.Msg.INVALID_TOKEN, e);
        }
        // 2.校验jwt是否有效
        if (!jwt.verify()) {
            // 验证失败
            throw new BadRequestException(400, ErrorInfo.Msg.INVALID_TOKEN);
        }
        // 3.校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            throw new BadRequestException(400, ErrorInfo.Msg.EXPIRED_TOKEN);
        }
        // 4.数据格式校验
        Object payload = jwt.getPayload("payload");
        Object jtiPayload = jwt.getPayload(RedisConstant.PAYLOAD_JTI_KEY);
        if (jtiPayload == null || payload == null) {
            // 数据为空
            throw new BadRequestException(400, ErrorInfo.Msg.INVALID_TOKEN);
        }

        // 5.数据解析
        UserDTO userDTO;
        try {
            userDTO = ((JSONObject) payload).toBean(UserDTO.class);
        } catch (RuntimeException e) {
            // 数据格式有误
            throw new BadRequestException(400, ErrorInfo.Msg.INVALID_TOKEN);
        }

        // 6.JTI校验
        String jti = stringRedisTemplate.opsForValue().get(RedisConstant.JWT_REDIS_KEY_PREFIX + userDTO.getId());
        if (!StringUtils.equals(jti, jtiPayload.toString())) {
            // jti不一致
            throw new BadRequestException(400, ErrorInfo.Msg.INVALID_TOKEN);
        }
        return userDTO;
    }
    public void cleanJtiCache() {
        stringRedisTemplate.delete(RedisConstant.JWT_REDIS_KEY_PREFIX + UserContextUtils.getUser());
    }
}
