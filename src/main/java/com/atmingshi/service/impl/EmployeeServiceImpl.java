package com.atmingshi.service.impl;

import com.atmingshi.mapper.EmployeeMapper;
import com.atmingshi.pojo.Employee;
import com.atmingshi.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @create 2023-07-11 18:45
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
