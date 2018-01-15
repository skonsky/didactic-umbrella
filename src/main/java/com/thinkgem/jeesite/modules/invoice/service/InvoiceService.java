/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.invoice.service;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.invoice.dao.InvoiceArrDao;
import com.thinkgem.jeesite.modules.invoice.dao.InvoiceDao;
import com.thinkgem.jeesite.modules.invoice.dao.InvoiceStatusDao;
import com.thinkgem.jeesite.modules.invoice.dao.InvoiceSumDao;
import com.thinkgem.jeesite.modules.invoice.entity.Invoice;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceArrearage;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceStatus;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceSum;
import com.thinkgem.jeesite.modules.sys.dao.LogDao;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Log;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = false)
public class InvoiceService extends CrudService<InvoiceDao, Invoice> {
    @Autowired
    private InvoiceSumDao invoiceSumDao;
    @Autowired
    private InvoiceStatusDao invoiceStatusDao;
    @Autowired
    private InvoiceArrDao invoiceArrDao;
    @Autowired
    private InvoiceDao invoiceDao;
    @Override
    public void save(Invoice entity) {
        // TODO Auto-generated method stub
        super.save(entity);
    }
    public void savearra(InvoiceArrearage invoiceArrearage) {
        invoiceArrDao.insert(invoiceArrearage);
    }
    public Page<InvoiceSum> getInvoiceSuminfo(Page<InvoiceSum> page,InvoiceSum invoice,String startDate,String endDate,String flag,String aac003){
        // 设置分页参数
        invoice.setPage(page);
        System.out.println(page.getPageSize()+"----"+page.getPageNo());
        // 执行分页查询
        page.setList(invoiceSumDao.getInvoiceSuminfo(invoice, startDate, endDate,flag,aac003,(page.getPageNo()-1)*page.getPageSize(),page.getPageSize()));
        return page;
    }
    public Page<InvoiceSum> getInvoicePrintinfo(Page<InvoiceSum> page,InvoiceSum invoice,String startDate,String endDate,String flag){
        // 设置分页参数
        invoice.setPage(page);
        // 执行分页查询
        page.setList(invoiceSumDao.getInvoicePrintinfo(invoice, startDate, endDate,flag));
        return page;
    }
    public Page<InvoiceStatus> getInvoiceStatusinfo(Page<InvoiceStatus> page,InvoiceStatus invoice){
        // 设置分页参数
        invoice.setPage(page);
        // 执行分页查询
        page.setList(invoiceStatusDao.getInvoiceStatusinfo(invoice));
        return page;
    }
    public void updateStatus(String aac001,String flag,String aac009,String printuserid,String printdate) {
        invoiceStatusDao.updateStatus(aac001, flag,aac009,printuserid,printdate);
    }
    public List<Invoice> getInvoiceinfo(String aac001,String aac009){
        return invoiceDao.getInvoiceinfo(aac001, aac009);
    }
    public InvoiceArrearage getArr(String aac001,String aac009) {
        return invoiceArrDao.getArr(aac001, aac009);
    }
}
