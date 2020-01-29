package com.djl.dmall.admin.sms.comtroller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djl.dmall.sms.service.CouponHistoryService;
import com.djl.dmall.to.CommonResult;
import com.djl.dmall.vo.PageInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 优惠券领取记录管理Controller
 */
@CrossOrigin
@Controller
@Api(tags = "SmsCouponHistoryController", description = "优惠券领取记录管理")
@RequestMapping("/couponHistory")
public class SmsCouponHistoryController {
    @Reference
    private CouponHistoryService historyService;

    @ApiOperation("根据优惠券id，使用状态，订单编号分页获取领取记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult list(@RequestParam(value = "couponId", required = false) Long couponId,
                                                           @RequestParam(value = "useStatus", required = false) Integer useStatus,
                                                           @RequestParam(value = "orderSn", required = false) String orderSn,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfoVo historyList = historyService.listCouponHistoryForPage(couponId, useStatus, orderSn, pageSize, pageNum);
        return new CommonResult().success(historyList);
    }
}
