package com.atmingshi.service;

import com.atmingshi.pojo.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yang
 * @create 2023-07-17 16:05
 */
public interface AddressBookService extends IService<AddressBook> {


    public void addAddress(AddressBook addressBook);

}
