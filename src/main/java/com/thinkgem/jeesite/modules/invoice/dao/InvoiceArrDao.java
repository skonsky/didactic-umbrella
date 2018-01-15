/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.invoice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.invoice.entity.Invoice;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceArrearage;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceSum;

/**
 * invoice DAO接口
 * 
 * @author fy
 * @version
 */
@MyBatisDao
public interface InvoiceArrDao extends CrudDao<InvoiceArrearage> {

    @Override
    int insert(InvoiceArrearage entity);
    InvoiceArrearage getArr(@Param("aac001")String aac001,@Param("aac009")String aac009);
}
