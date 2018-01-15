/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.excelexp.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.invoice.entity.Invoice;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceArrearage;
import com.thinkgem.jeesite.modules.invoice.service.InvoiceService;
import com.thinkgem.jeesite.modules.sys.entity.Log;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.LogService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 
 * @author fy
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/invoice/imp")
public class InvoiceImpController extends BaseController {
    @Autowired
    private InvoiceService invoiceService;
    @RequiresPermissions("invoice:info:imp")
    @RequestMapping(value = {"importPage", ""})
    public String importPage() {
        return "modules/invoice/invoiceImp";
    }
    /**
     * 导入用户数据
     * @param file
     * @param redirectAttributes
     * @return
     */
    /*@RequiresPermissions("invoice:info:imp")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        if(Global.isDemoMode()){
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 0,0);
            List<Invoice> list = ei.getInvoiceListFromExcel();
            for (Invoice invoice : list){
                try{
                    invoiceService.save(invoice);
                    successNum++;
                }catch (Exception ex) {
                    failureMsg.append("<br/>登录名 "+invoice.getAac001()+" 导入失败："+ex.getMessage());
                    failureNum++;
                }
            }
            if (failureNum>0){
                failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/invoice/imp/importPage?repage";
    }*/
    @RequiresPermissions("invoice:info:imp")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(String imptype,MultipartFile file, RedirectAttributes redirectAttributes) {
        if(Global.isDemoMode()){
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 0,0);
            List<InvoiceArrearage> list=new ArrayList<InvoiceArrearage>();;
            List<Invoice> list1=new ArrayList<Invoice>();
            if(imptype.equals("1")) {
                list1=ei.getInvoiceListFromExcel();
            }else if(imptype.equals("2")) {
                list=ei.getArrearageFromExcel();
            }else {
                addMessage(redirectAttributes, "未选择导入类别");
            }
            if(imptype.equals("1")) {
                for (Invoice invoice : list1){
                    try{
                        invoiceService.save(invoice);
                        successNum++;
                    }catch(DuplicateKeyException ex) {
                        failureMsg.append("<br/>户号 "+invoice.getAac001()+" 导入失败：发票已存在");
                        failureNum++;
                    }catch (Exception ex) {
                        failureMsg.append("<br/>户号 "+invoice.getAac001()+" 导入失败："+ex.getMessage());
                        failureNum++;
                    }
                }
            }else if(imptype.equals("2")) {
                for (InvoiceArrearage invoice : list){
                    try{
                        invoiceService.savearra(invoice);
                        successNum++;
                    }catch(DuplicateKeyException ex) {
                        failureMsg.append("<br/>户号 "+invoice.getAac001()+" 导入失败：发票已存在");
                        failureNum++;
                    }catch (Exception ex) {
                        failureMsg.append("<br/>户号 "+invoice.getAac001()+" 导入失败："+ex.getMessage());
                        failureNum++;
                    }
                }
            }
            
            if (failureNum>0){
                failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户",failureMsg.toString());
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/invoice/imp/importPage?repage";
    }
}
