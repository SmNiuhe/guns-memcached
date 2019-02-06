package cn.stylefeng.guns.modular.HouseManager.controller;

import cn.stylefeng.guns.core.common.page.PageInfoBT;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.HouseManager.model.TblHouse;
import cn.stylefeng.guns.modular.HouseManager.service.ITblHouseService;
import cn.stylefeng.guns.modular.system.model.LoginLog;
import cn.stylefeng.guns.modular.system.warpper.LogWarpper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.page.PageFactory;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.page.PageQuery;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 房屋管理控制器
 *
 * @author fengshuonan
 * @Date 2019-02-04 14:53:42
 */
@Controller
@RequestMapping("/tblHouse")
public class TblHouseController extends BaseController {

    private String PREFIX = "/HouseManager/tblHouse/";

    @Autowired
    private ITblHouseService tblHouseService;

    /**
     * 跳转到房屋管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "tblHouse.html";
    }

    /**
     * 跳转到添加房屋管理
     */
    @RequestMapping("/tblHouse_add")
    public String tblHouseAdd() {
        return PREFIX + "tblHouse_add.html";
    }

    /**
     * 跳转到修改房屋管理
     */
    @RequestMapping("/tblHouse_update/{tblHouseId}")
    public String tblHouseUpdate(@PathVariable Integer tblHouseId, Model model) {
        TblHouse tblHouse = tblHouseService.selectById(tblHouseId);
        model.addAttribute("item", tblHouse);
        LogObjectHolder.me().set(tblHouse);
        return PREFIX + "tblHouse_edit.html";
    }

    /**
     * 获取房屋管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {

        if (ToolUtil.isEmpty(condition)) {
            Page<TblHouse> housePage = PageFactory.defaultPage();
            Page<Map<String, Object>> mapPage = tblHouseService.selectMapsPage(housePage, null);
            return new PageInfoBT<>(mapPage);
        } else {
            EntityWrapper<TblHouse> entityWrapper = new EntityWrapper<>();
            Wrapper<TblHouse> houseWrapper = entityWrapper.like("house_user", condition);

            Page<TblHouse> housePage = PageFactory.defaultPage();
            Page<Map<String, Object>> mapPage = tblHouseService.selectMapsPage(housePage, houseWrapper);
            return new PageInfoBT<>(mapPage);
        }
    }

    /**
     * 新增房屋管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(TblHouse tblHouse) {
        tblHouseService.insert(tblHouse);
        return SUCCESS_TIP;
    }

    /**
     * 删除房屋管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer tblHouseId) {
        tblHouseService.deleteById(tblHouseId);
        return SUCCESS_TIP;
    }

    /**
     * 修改房屋管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(TblHouse tblHouse) {
        tblHouseService.updateById(tblHouse);
        return SUCCESS_TIP;
    }

    /**
     * 房屋管理详情
     */
    @RequestMapping(value = "/detail/{tblHouseId}")
    @ResponseBody
    public Object detail(@PathVariable("tblHouseId") Integer tblHouseId) {
        return tblHouseService.selectById(tblHouseId);
    }
}
