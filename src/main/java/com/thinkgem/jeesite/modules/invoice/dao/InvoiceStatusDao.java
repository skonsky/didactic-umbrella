/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.invoice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceStatus;

/**
 * invoice DAO接口
 * 
 * @author fy
 * @version
 */
@MyBatisDao
public interface InvoiceStatusDao extends CrudDao<InvoiceStatus> {

    List<InvoiceStatus> getInvoiceStatusinfo(@Param("invoiceStatus") InvoiceStatus invoiceStatus);

    void updateStatus(@Param("aac001") String aac001, @Param("flag") String flag, @Param("aac009") String aac009,
            @Param("printuserid") String printuserid, @Param("printdate") String printdate);

}
