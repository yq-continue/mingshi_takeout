package com.atmingshi.controller;

import com.atmingshi.common.R;
import com.atmingshi.pojo.User;
import com.atmingshi.service.UserService;
import com.atmingshi.utils.SMSUtils;
import com.atmingshi.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

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

    /**
     * 验证码发送
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> getMessage(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if (phone != null){
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 使用 aliyun 发送短信
            log.info("验证码为{}",code);
//            SMSUtils.sendMessage("签名","模板名","电话号码",code);

            // 将验证码存入 session 域
            session.setAttribute(phone,code);
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
        // 获取用户登录信息
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        // 比对验证码是否正确
        String codeInSession = session.getAttribute(phone).toString();
        if (codeInSession != null && codeInSession.equals(code)){
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
