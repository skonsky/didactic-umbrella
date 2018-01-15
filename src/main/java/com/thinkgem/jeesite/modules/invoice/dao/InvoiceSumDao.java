/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.invoice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.invoice.entity.Invoice;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceSum;

/**
 * invoice DAO接口
 * 
 * @author fy
 * @version
 */
@MyBatisDao
public interface InvoiceSumDao extends CrudDao<InvoiceSum> {
    
    
    List<InvoiceSum> getInvoiceSuminfo(@Param("invoiceSum") InvoiceSum invoiceSum, @Param("startDate") String startDate,
            @Param("endDate") String endDate, @Param("flag") String flag, @Param("aac003") String aac003,@Param("pageNo") int pageNo,@Param("pageSize") int pageSize);
    List<InvoiceSum> getInvoicePrintinfo(@Param("invoiceSum") InvoiceSum invoiceSum, @Param("startDate") String startDate,
            @Param("endDate") String endDate,@Param("flag") String flag);
}
