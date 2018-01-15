package com.thinkgem.jeesite.modules.invoice.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;


public class Invoice extends DataEntity<Invoice>{
    private static final long serialVersionUID = 112L;
    private String aac001;
    private String aac002;
    private String aac003;
    private String aac004;
    private String aac005;
    private String aac006;
    private String aac007;
    private String aac008;
    private String aac009;
    private String aac010;
    private String aac011;
    private String aac012;
    private String aac013;
    private String userid;
    private String impdate;
    public Invoice() {
        super();

    }

    
    public Invoice(String aac001, String aac002, String aac003, String aac004, String aac005, String aac006,
			String aac011, String aac012, String aac013) {
		super();
		this.aac001 = aac001;
		this.aac002 = aac002;
		this.aac003 = aac003;
		this.aac004 = aac004;
		this.aac005 = aac005;
		this.aac006 = aac006;
		this.aac011 = aac011;
		this.aac012 = aac012;
		this.aac013 = aac013;
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
    public String getAac005() {
        return aac005;
    }
    public void setAac005(String aac005) {
        this.aac005 = aac005;
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
    public String getAac011() {
        return aac011;
    }
    public void setAac011(String aac011) {
        this.aac011 = aac011;
    }
    public String getAac012() {
        return aac012;
    }
    public void setAac012(String aac012) {
        this.aac012 = aac012;
    }
    public String getAac013() {
        return aac013;
    }
    public void setAac013(String aac013) {
        this.aac013 = aac013;
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
    
}

