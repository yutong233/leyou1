package com.leyou.cart.config;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {

    private String pubKeyPath; //公钥
    private String cookieName;

    private PublicKey publicKey;

    // 对象一旦实例化后，就应该读取公钥和私钥
    @PostConstruct // 构造函数执行完毕后就执行
    public void init(){
        try {
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.CONFIG_ERROR);
        }

    }
}
