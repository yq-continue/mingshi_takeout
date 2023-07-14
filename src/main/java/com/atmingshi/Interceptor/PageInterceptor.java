//package com.atmingshi.Interceptor;
//
//import com.alibaba.fastjson.JSON;
//import com.atmingshi.common.R;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @author yang
// * @create 2023-07-11 21:45
// */
//@Slf4j
//public class PageInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("拦截请求{}",request.getRequestURI());
//        //1.判断登录状态，若已经登录，则放行
//        Long userId = (Long)request.getSession().getAttribute("userId");
//        if (userId != null){
//            log.info("用户已登录，userId为{}",userId);
//            return true;
//        }
//        //若还未登录，则通过输出流传输数据给前端页面，跳转登录页面
//        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
////        request.getRequestDispatcher("http://www.baidu.com").forward(request,response);
//        log.info("用户未登录，即将跳转登录页面！！");
//        return false;
//    }
//}
