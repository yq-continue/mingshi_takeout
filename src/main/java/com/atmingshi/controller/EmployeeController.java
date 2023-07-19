package com.atmingshi.controller;


import com.atmingshi.common.R;
import com.atmingshi.exception.PermissionsException;
import com.atmingshi.pojo.Employee;
import com.atmingshi.service.impl.EmployeeServiceImpl;
import com.atmingshi.utils.TransferId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

/**
 * @author yang
 * @create 2023-07-11 18:49
 */
@RestController  //将数据响应到页面
@RequestMapping("/employee")    //employee/login
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    /**
     * 后台登录功能
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R login(HttpServletRequest request, @RequestBody Employee employee){
        //0.判断前台用户是否登录
        if (request.getSession().getAttribute("user") != null){
            throw new PermissionsException("请先注销前台用户账号再次尝试");
        }
        //1.将获取的密码使用 MD5 加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.查询用户名是否存在
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username",employee.getUsername());
        Employee user = employeeService.getOne(wrapper);
        if (user == null){
            return R.error("用户名不存在");
        }
        //3.查询密码是否正确
        if (!user.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //4.查看员工状态是否禁用
        if (user.getStatus() == 0){
            return R.error("用户已经被禁用");
        }
        //5.允许登录,将用户 id 信息存入 session
        request.getSession().setAttribute("userId",user.getId());
        return R.success(user);
    }

    /**
     * 后台推出登录功能
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> lougout(HttpServletRequest request){
        //1.清除 session 域中的用户 id
        request.getSession().removeAttribute("userId");
        //2.返回数据
        return R.success("用户登出");
    }

    /**
     * 添加新员工
     * @param employee
     * @param request
     * @return
     */
    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee,HttpServletRequest request){
        //1.设置初始密码 【123456】 ，使用 md5 加密
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);

        /*以下内容使用公共字段填充实现
        2.设置创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        3.设置新用户的创建人和修改人
        Long userID = (Long) request.getSession().getAttribute("userId");
        employee.setCreateUser(userID);
        employee.setUpdateUser(userID);*/
        //4.将用户数据存入数据库
        employeeService.save(employee);
        return R.success("用户添加成功");
    }

    /**
     * 员工分页查询
     * @param request
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> employeePage(HttpServletRequest request,int page,int pageSize,String name){
        //1.创建分页对象
        Page<Employee> pageInFo = new Page(page,pageSize);
        //2.创建 wrapper 封装查询条件
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name),Employee::getUsername,name);
        wrapper.orderByAsc(Employee::getCreateTime);
        wrapper.notIn(Employee::getId,request.getSession().getAttribute("userId"));
        //3.查询并返回数据
        employeeService.page(pageInFo,wrapper);
        return R.success(pageInFo);
    }

    /**
     * 员工信息修改
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        /*以下内容使用公共字段填充实现
        1.修改信息
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("userId"));*/
        //2.将修改的信息存入数据库
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> userEcho(@PathVariable Long id){
        if (id != null){
            Employee employee = employeeService.getById(id);
            return R.success(employee);
        }
        return R.error("查询失败，不存在此 id");
    }



}
