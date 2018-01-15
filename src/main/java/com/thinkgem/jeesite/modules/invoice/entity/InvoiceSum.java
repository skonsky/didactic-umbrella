package com.thinkgem.jeesite.modules.invoice.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class InvoiceSum extends DataEntity<InvoiceSum>{
    private static final long serialVersionUID = 111L;
    private String aac001;
    private String aac002;
    private String aac003;
    private String aac004;
    private String aac013;
    private String aac009;
    
    private String sum;
    private String aac006;
    private String aac007;
    private String userid;
    private String name;
    private String flag;
    private String impdate;

    public InvoiceSum() {
        super();
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
    public String getSum() {
        return sum;
    }
    public void setSum(String sum) {
        this.sum = sum;
    }
    public String getAac007() {
        return aac007;
    }
    public void setAac007(String aac007) {
        this.aac007 = aac007;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getImpdate() {
        return impdate;
    }
    public void setImpdate(String impdate) {
        this.impdate = impdate;
    }
    public String getAac013() {
        return aac013;
    }
    public void setAac013(String aac013) {
        this.aac013 = aac013;
    }
    public String getAac009() {
        return aac009;
    }
    public void setAac009(String aac009) {
        this.aac009 = aac009;
    }
    public String getAac006() {
        return aac006;
    }
    public void setAac006(String aac006) {
        this.aac006 = aac006;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
