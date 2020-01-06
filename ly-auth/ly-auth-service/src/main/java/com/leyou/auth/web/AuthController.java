package com.leyou.auth.web;


import com.leyou.auth.config.JwtProperties;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.common.utils.CookieUtils;
import com.leyou.auth.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties properties;

    /**
     * 登录授权
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response,
            HttpServletRequest request
    ){
        //登录
        String token = authService.login(username,password);

        //写入cookie
        CookieUtils.newBuilder(response).httpOnly().request(request).build(properties.getCookieName(), token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("LY_TOKEN") String token,
            HttpServletResponse response, HttpServletRequest request) {
        try {
            //解析token
            UserInfo info = JwtUtils.getInfoFromToken(token, properties.getPublicKey());

            //刷新token,重新生成token
            String newToken = JwtUtils.generateToken(info, properties.getPrivateKey(), properties.getExpire());
            //写入cookie
            CookieUtils.newBuilder(response).httpOnly().request(request).build(properties.getCookieName(), newToken);

            return ResponseEntity.ok(info);
        } catch (Exception e) {
            //token已过期或者被篡改
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
    }

}

