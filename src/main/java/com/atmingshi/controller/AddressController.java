package com.atmingshi.controller;

import com.atmingshi.common.R;
import com.atmingshi.pojo.AddressBook;
import com.atmingshi.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @create 2023-07-17 16:02
 */
@RequestMapping("/addressBook")
@Slf4j
@RestController
public class AddressController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 地址页面展示
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> addressList(HttpSession session){
        // 获取用户 id
        Long userId = (Long) session.getAttribute("user");
        // 查询用户收获地址
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,userId);
        List<AddressBook> addressList = addressBookService.list(wrapper);
        //返回数据展示
        return R.success(addressList);
    }

    /**
     * 新增收货地址
     * @return
     */
    @PostMapping
    public R<String> addAddress(@RequestBody AddressBook addressBook){
        addressBookService.addAddress(addressBook);
        return R.success("添加成功");
    }

    /**
     * 修改收货地址表单回显
     * @param addressId
     * @return
     */
    @GetMapping("/{addressId}")
    public R<AddressBook> echoUpdateAddress(@PathVariable("addressId")Long addressId){
        AddressBook address = addressBookService.getById(addressId);
        return R.success(address);
    }

    /**
     * 修改收货地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> updateAddress(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> deleteAddress(Long ids){
        addressBookService.removeById(ids);
        return R.success("删除成功");
    }

    /**
     * 修改默认地址
     * @param idMap
     * @return
     */
    @PutMapping("/default")
    public R<String> updateDefaultAddress(@RequestBody Map idMap){
        // 获取 addressid
        long id = Long.parseLong((String) idMap.get("id"));
        // 通过 addressid 获取 userid
        AddressBook address = addressBookService.getById(id);
        Long userId = address.getUserId();
        // 将此 userid 下的所有 is_default 改成 0
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(AddressBook::getIsDefault,0);
        wrapper.eq(AddressBook::getUserId,userId);
        addressBookService.update(wrapper);
        // 将此 addressid 的 id_default 改成 1
        LambdaUpdateWrapper<AddressBook> wrapperOne = new LambdaUpdateWrapper<>();
        wrapperOne.set(AddressBook::getIsDefault,1);
        wrapperOne.eq(AddressBook::getId,id);
        addressBookService.update(wrapperOne);
        return R.success("修改成功");
    }

    @GetMapping("default")
    public R<AddressBook> getDefaultAddress(HttpSession session){
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,userId);
        wrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(wrapper);
        return R.success(addressBook);
    }


}
