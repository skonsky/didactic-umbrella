package com.thinkgem.jeesite.modules.invoice.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class InvoiceArrearage extends DataEntity<InvoiceArrearage>{
    String aac001;
    String aac002;
    String aac006;
    String aac009;
    String flag;
    String userid;
    String printdate;
    String impdate;
    String aac200;
    String aac201;
    public InvoiceArrearage() {
        super();
        // TODO Auto-generated constructor stub
    }
    public InvoiceArrearage(String id) {
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
    public String getAac006() {
        return aac006;
    }
    public void setAac006(String aac006) {
        this.aac006 = aac006;
    }
    public String getAac009() {
        return aac009;
    }
    public void setAac009(String aac009) {
        this.aac009 = aac009;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getPrintdate() {
        return printdate;
    }
    public void setPrintdate(String printdate) {
        this.printdate = printdate;
    }
    public String getImpdate() {
        return impdate;
    }
    public void setImpdate(String impdate) {
        this.impdate = impdate;
    }
    public String getAac200() {
        return aac200;
    }
    public void setAac200(String aac200) {
        this.aac200 = aac200;
    }
    public String getAac201() {
        return aac201;
    }
    public void setAac201(String aac201) {
        this.aac201 = aac201;
    }
    

}
