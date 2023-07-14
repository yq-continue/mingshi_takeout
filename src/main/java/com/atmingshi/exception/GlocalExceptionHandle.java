package com.atmingshi.exception;

import com.atmingshi.common.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 * @author yang
 * @create 2023-07-12 17:15
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlocalExceptionHandle {
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public R<String> usernameDuplicateException(SQLIntegrityConstraintViolationException exc){
        //1.获取到异常信息
        String msg = exc.getMessage();
        if (msg.contains("Duplicate entry")){
            //2.对异常信息进行处理，返回给前端
            String[] msgs = msg.split(" ");
            msg = msgs[2].substring(1,msgs[2].length() - 1) + " 已存在，请更换用户名再次尝试";
            return R.error(msg);
        }

        return R.error("未知错误，请稍后再试");
    }

    @ExceptionHandler({CustomException.class})
    public R<String> customException(CustomException exc){
        //1.获取到异常信息
        String msg = exc.getMessage();
        return R.error(msg);
    }

}
