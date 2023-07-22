package com.atmingshi.controller;

import com.atmingshi.common.R;
import com.atmingshi.exception.PermissionsException;
import com.atmingshi.pojo.User;
import com.atmingshi.service.UserService;
import com.atmingshi.utils.SMSUtils;
import com.atmingshi.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yang
 * @create 2023-07-17 14:38
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 验证码发送
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> getMessage(@RequestBody User user){
        //获取手机号
        String phone = user.getPhone();
        if (phone != null){
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 使用 aliyun 发送短信
            log.info("验证码为{}",code);
//            SMSUtils.sendMessage("民师外卖","",phone,code);

            // 将验证码存入 session 域
//            session.setAttribute(phone,code);

            // 将验证码缓存到 redis 数据库中，设置过期时间为 5 分钟
            redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);
            return R.success("获取验证码成功");
        }
        return R.error("获取验证码失败");
    }

    /**
     * 用户登录逻辑
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        // 判断后台用户是否登录
        if (session.getAttribute("userId") != null){
            throw new PermissionsException("请先注销后台管理用户再次尝试登录");
        }
        // 获取用户登录信息
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        // 比对验证码是否正确
//        String codeInSession = session.getAttribute(phone).toString();

        // 在 redis 中获取验证码
        String codeInRedis = (String)redisTemplate.opsForValue().get(phone);

        if (codeInRedis != null && codeInRedis.equals(code)){
            // 查询数据库中是否存在该用户
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone,phone);
            User user = userService.getOne(wrapper);
            // 若不存此用户则创建
            if (user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            // 登录成功，将 redis 中的验证码删除
            redisTemplate.delete(phone);

            return R.success(user);
        }
        return R.error("登录失败，验证码错误");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("user");
        log.info("用户退出登录");
        return R.success("退出成功");
    }
}
