package com.djl.dmall.admin.ums.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djl.dmall.to.CommonResult;
import com.djl.dmall.ums.entity.MemberLevel;
import com.djl.dmall.ums.service.MemberLevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
public class UmsMemberLevelController {

    @Reference
    private MemberLevelService memberLevelService;


    /**
     * 查询所有会员等级
     *
     * @return
     */
    @GetMapping("/memberLevel/list")
    public Object memberLevelList() {
        List<MemberLevel> list = memberLevelService.list();
        System.out.println(list);
        return new CommonResult().success(list);
    }

}
