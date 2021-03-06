package com.djl.dmall.admin.sms.comtroller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.djl.dmall.sms.entity.FlashPromotionSession;
import com.djl.dmall.sms.service.FlashPromotionSessionService;
import com.djl.dmall.to.CommonResult;
import com.djl.dmall.vo.PageInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 限时购场次管理Controller
 */
@Slf4j
@CrossOrigin
@Controller
@Api(tags = "SmsFlashPromotionSessionController", description = "限时购场次管理")
@RequestMapping("/flashSession")
public class SmsFlashPromotionSessionController {
    @Reference
    private FlashPromotionSessionService flashPromotionSessionService;

    @ApiOperation("添加场次")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody FlashPromotionSession promotionSession) {
        flashPromotionSessionService.save(promotionSession);
        return new CommonResult().success(null);
    }

    @ApiOperation("修改场次")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody FlashPromotionSession promotionSession) {
        promotionSession.setId(id);
        flashPromotionSessionService.updateById(promotionSession);
        return new CommonResult().success(null);
    }

    @ApiOperation("修改启用状态")
    @RequestMapping(value = "/update/status/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id, Integer status) {
        flashPromotionSessionService.updateStatus(id, status);
        return new CommonResult().success(null);
    }

    @ApiOperation("删除场次")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        flashPromotionSessionService.removeById(id);
        return new CommonResult().success(null);
    }

    @ApiOperation("获取场次详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getItem(@PathVariable Long id) {
        FlashPromotionSession promotionSession = flashPromotionSessionService.getById(id);
        return new CommonResult().success(promotionSession);
    }

    @ApiOperation("获取全部场次")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult list() {
        List<FlashPromotionSession> promotionSessionList = flashPromotionSessionService.list();
        return new CommonResult().success(promotionSessionList);
    }

    @ApiOperation("获取全部可选场次及其数量")
    @RequestMapping(value = "/selectList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult selectList(Long flashPromotionId) {
        PageInfoVo  promotionSessionList = flashPromotionSessionService.selectListForPage(flashPromotionId);
        log.debug("读取id是：{}，返回的数据是：{}", flashPromotionId, promotionSessionList);
        return new CommonResult().success(promotionSessionList);
    }
}