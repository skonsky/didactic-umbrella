package com.thinkgem.jeesite.modules.invoice.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class InvoiceStatus extends DataEntity<InvoiceStatus>{
    String aac001;
    String aac002;
    String aac003;
    String aac004;
    String aac006;
    String aac007;
    String aac008;
    String aac009;
    String aac010;
    String printuserid;
    String printdate;
    String name;
    public InvoiceStatus() {
        super();
        // TODO Auto-generated constructor stub
    }
    public InvoiceStatus(String id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    public String getAac001() {
        return aac001;
    }
    public void setAac001(String aac001) {
        this.aac001 = aac001;
    }
    public String getAac002() {
        return aac002;
    }
    public void setAac002(String aac002) {
        this.aac002 = aac002;
    }
    public String getAac003() {
        return aac003;
    }
    public void setAac003(String aac003) {
        this.aac003 = aac003;
    }
    public String getAac004() {
        return aac004;
    }
    public void setAac004(String aac004) {
        this.aac004 = aac004;
    }
    public String getAac006() {
        return aac006;
    }
    public void setAac006(String aac006) {
        this.aac006 = aac006;
    }
    public String getAac007() {
        return aac007;
    }
    public void setAac007(String aac007) {
        this.aac007 = aac007;
    }
    public String getAac008() {
        return aac008;
    }
    public void setAac008(String aac008) {
        this.aac008 = aac008;
    }
    public String getAac009() {
        return aac009;
    }
    public void setAac009(String aac009) {
        this.aac009 = aac009;
    }
    public String getAac010() {
        return aac010;
    }
    public void setAac010(String aac010) {
        this.aac010 = aac010;
    }
    public String getPrintuserid() {
        return printuserid;
    }
    public void setPrintuserid(String printuserid) {
        this.printuserid = printuserid;
    }
    public String getPrintdate() {
        return printdate;
    }
    public void setPrintdate(String printdate) {
        this.printdate = printdate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    

}
