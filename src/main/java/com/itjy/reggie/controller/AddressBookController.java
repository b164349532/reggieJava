package com.itjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.itjy.reggie.Service.AddressBookService;
import com.itjy.reggie.common.BaseContext;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.AddressBook;
import com.itjy.reggie.entity.ShoppingCart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.Context;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> getList() {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.in(AddressBook::getUserId,currentId);
        List<AddressBook> list = addressBookService.list(lqw);
        return R.success(list);
    }

    /**
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody  AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success("保存成功");
    }

    /**
     *
     * @param id
     * @return
     */
    @PutMapping("/default")
    public R<String> updateDefault (@RequestBody Long id) {
        LambdaUpdateWrapper<AddressBook> lqw = new LambdaUpdateWrapper<>();
        lqw.in(AddressBook::getUserId,BaseContext.getCurrentId());
        lqw.set(AddressBook::getIsDefault,0);
        List<AddressBook> list = addressBookService.list(lqw);
        addressBookService.update(lqw);

        AddressBook addressBook = addressBookService.getById(id);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault () {
       LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
       lqw.eq(AddressBook::getUserId,BaseContext.getCurrentId());
       lqw.eq(AddressBook::getIsDefault,1);
        AddressBook addressOne = addressBookService.getOne(lqw);
        return R.success(addressOne);
    }

//    lqw.in(AddressBook::getIsDefault, 1);
//    AddressBook addressDefault = addressBookService.getOne(lqw);


}
