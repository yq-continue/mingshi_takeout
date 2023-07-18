package com.atmingshi.service.impl;

import com.atmingshi.mapper.AddressBookMapper;
import com.atmingshi.pojo.AddressBook;
import com.atmingshi.pojo.User;
import com.atmingshi.service.AddressBookService;
import com.atmingshi.service.UserService;
import com.atmingshi.utils.TransferId;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yang
 * @create 2023-07-17 16:07
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {


    @Override
    @Transactional
    public void addAddress(AddressBook addressBook) {
        // 获取 userid
        Long userId = TransferId.gteId();
        // 设置用户 id
        addressBook.setUserId(userId);
        // 将地址信息存入表
        this.save(addressBook);
    }
}
