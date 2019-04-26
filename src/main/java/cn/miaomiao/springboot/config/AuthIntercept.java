package cn.miaomiao.springboot.config;

import cn.miaomiao.springboot.constant.RedisConstant;
import cn.miaomiao.springboot.constant.ResponseCode;
import cn.miaomiao.springboot.entity.UserLogin;
import cn.miaomiao.springboot.model.BaseResponse;
import cn.miaomiao.springboot.utils.RedisUtil;
import cn.miaomiao.springboot.utils.VerifyEmptyUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录鉴权拦截器
 *
 * @author miaomiao
 * @date 2019/4/25 14:57
 */
@Component
public class AuthIntercept extends HandlerInterceptorAdapter {
    private final Logger log = LoggerFactory.getLogger("api_info");
    @Resource
    private AuthConfig authConfig;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logApi(request);
        String uri = request.getRequestURI();
        if (authConfig.getUrlList().contains(uri)) {
            return true;
        }
        // 获取token
        String token = request.getHeader("authToken");
        if (VerifyEmptyUtil.isNotEmpty(token) && redisUtil.hasKey(token)) {
            UserLogin user = redisUtil.getObject(token, UserLogin.class);
            if (user != null) {
                redisUtil.expire(token, RedisConstant.TOKEN_TIMEOUT);
                return true;
            }
        }
        // token 验证失败
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        BaseResponse ret = BaseResponse.error(ResponseCode.USER_NOT_LOGIN);
        response.getWriter().write(JSONObject.toJSONString(ret));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    /**
     * 记录日志
     */
    private void logApi(HttpServletRequest request) {
        String sb = "[url]" +
                request.getRequestURI() +
                "[ip]" +
                request.getRemoteAddr() +
                "[method]" +
                request.getMethod();
        log.info(sb);
    }
}
