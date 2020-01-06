package com.leyou.gateway.filters;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class AuthFilter extends ZuulFilter {

    @Autowired
    private JwtProperties properties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        //过滤器类型，前置
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //过滤顺序
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    //是否过滤
    @Override
    public boolean shouldFilter() {
        //获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = ctx.getRequest();
        //获取请求的URL路径
        String path = request.getRequestURI();
        //判断知否放行，放行则返回false，不放行，要走过滤则true
        boolean allow = !isAllowPath(path);
        return allow;
    }

    private boolean isAllowPath(String path) {
        //遍历baimingd
        for (String allowPath : filterProperties.getAllowPaths()) {
            //判断是否允许
            if (path.startsWith(allowPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = ctx.getRequest();
        //获取token
        String token = CookieUtils.getCookieValue(request, properties.getCookieName());
        try {
            //解析token
            UserInfo user = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
            //TODO 校验权限
        } catch (Exception e) {
            //解析token失败，未登录，拦截
            ctx.setSendZuulResponse(false);
            //返回状态码
            ctx.setResponseStatusCode(403);

        }
        //校验权限
        return null;
    }
}
