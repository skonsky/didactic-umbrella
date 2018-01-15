/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.invoice.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.invoice.entity.Invoice;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceArrearage;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceStatus;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceSum;
import com.thinkgem.jeesite.modules.invoice.service.InvoiceService;
import com.thinkgem.jeesite.modules.sys.entity.Log;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.LogService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.io.FileOutputStream;
import freemarker.template.SimpleDate;

/**
 * 
 * @author fy
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/invoice/info")
public class InvoiceController extends BaseController {
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static final String[] RADICES = {"", "拾", "佰", "仟"};
    private static final String[] BIG_RADICES = {"", "万", "亿", "兆"};
    private DecimalFormat df = new DecimalFormat("0.00");
    @Autowired
    private InvoiceService invoiceService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @RequiresPermissions("invoice:info:search")
    @RequestMapping(value = { "invoiceinfo" })
    public String list(String aac001, String aac002, String aac007, String startDate, String endDate,
            String exception,String aac003,HttpServletRequest request, HttpServletResponse response, Model model) {
        InvoiceSum entity = new InvoiceSum();

        Calendar cal = Calendar.getInstance();
        entity.setAac001(aac001);
        entity.setAac002(aac002);
        entity.setFlag("2");
        entity.setUserid(UserUtils.getUser().getId());
        Page<InvoiceSum> page = invoiceService.getInvoiceSuminfo(new Page<InvoiceSum>(request, response), entity,
                startDate, endDate,exception,aac003);
        double money = 0;
        
        for (InvoiceSum in : page.getList()) {
            if (in.getAac006() != null)
                money += Double.parseDouble((in.getAac006()));
        }
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = sdf.format(cal.getTime());
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        String lastday = sdf.format(cal.getTime());
        model.addAttribute("page", page);
        model.addAttribute("money", money);
        model.addAttribute("sdate", startDate==null?firstday:startDate);
        model.addAttribute("edate", endDate==null?lastday:endDate);
        model.addAttribute("aac007", aac007==null?"2":aac007);
        model.addAttribute("aac003", aac003==null?"1":aac003);
        model.addAttribute("exception", exception);
        model.addAttribute("aac001", aac001);
        model.addAttribute("aac002", aac002);
        return "modules/invoice/invoiceSearch";
    }
    @RequiresPermissions("invoice:info:search")
    @RequestMapping(value = { "invoiceprintinfo" })
    public String list5(String aac001, String aac002, String printstartDate, String printendDate,
            String exception,HttpServletRequest request, HttpServletResponse response, Model model) {
        InvoiceSum entity = new InvoiceSum();

        Calendar cal = Calendar.getInstance();
        entity.setAac001(aac001);
        entity.setAac002(aac002);
        entity.setUserid(UserUtils.getUser().getId());
        Page<InvoiceSum> page = invoiceService.getInvoicePrintinfo(new Page<InvoiceSum>(request, response), entity,
                printstartDate, printendDate,exception);
        double money = 0;

        for (InvoiceSum in : page.getList()) {
            if (in.getAac006() != null)
                money += Double.parseDouble((in.getAac006()));
        }
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = sdf.format(cal.getTime());
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        String lastday = sdf.format(cal.getTime());
        model.addAttribute("page", page);
        model.addAttribute("money", money);
        model.addAttribute("sdate", printstartDate==null?firstday:printstartDate);
        model.addAttribute("edate", printendDate==null?lastday:printendDate);
        model.addAttribute("exception", exception);
        model.addAttribute("aac001", aac001);
        model.addAttribute("aac002", aac002);
        return "modules/invoice/invoiceArr";
    }
    @RequiresPermissions("invoice:info:print")
    @RequestMapping(value = { "invoicestatus" })
    public String list1(String aac001, String aac002, String aac009, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        InvoiceStatus entity = new InvoiceStatus();
        entity.setAac001(aac001);
        entity.setAac002(aac002);
        entity.setAac009(aac009);
        Page<InvoiceStatus> page = invoiceService.getInvoiceStatusinfo(new Page<InvoiceStatus>(request, response),
                entity);
        model.addAttribute("page", page);
        return "modules/invoice/invoiceInfo";
    }

    @RequiresPermissions("invoice:info:print")
    @RequestMapping(value = { "invoiceprint" })
    public String list3(String aac001, String aac002,String aac009, HttpServletRequest request,
            HttpServletResponse response,Model model) {
        // 数据准备
        List<Invoice> infolist = invoiceService.getInvoiceinfo(aac001, aac009);
        if(infolist.size()<=0) {
            //model.addAttribute("message", "数据不完整");
            infolist.add(new Invoice(aac001,aac002,"","","","","","",""));
            //return "modules/invoice/invoiceSearch";
        }
        // 开票日期
        String kprq = sdf.format(new Date());
        // 总金额
        InvoiceArrearage iar=invoiceService.getArr(aac001, aac009);
        String zje = iar.getAac006();
        //本月结余
        String byjy=iar.getAac200();
        
        if(iar.getFlag().equals("1")) {
            model.addAttribute("message", "该发票已打印");
            return "modules/invoice/invoiceSearch";
        } 
        // 起码
        String qm = infolist.get(0).getAac003();
        // 起码
        String zm = infolist.get(0).getAac004();
        //户号
        String hh=aac001+"("+infolist.get(0).getAac012()+")";
        // 总电量
        int zdl = 0;
        double azje=0;
        for (Invoice invoice : infolist) {
            if (invoice.getAac013() != null && !invoice.getAac013().equals(""))
                zdl += Integer.parseInt(invoice.getAac013());
            if(invoice.getAac006() != null && !invoice.getAac006().equals("")) {
                azje += Double.parseDouble(invoice.getAac006());
            }
        }
        String ajze1=df.format(azje);
        // 电费周期
        int year=Integer.valueOf(aac009.substring(0, 4));
        int month=Integer.valueOf(aac009.substring(4, 6));
        if(month==1) {
            month=12;
            year=year-1;
        }else {
            month=month-1;
        }
        String dfzq = year + "." + month + ".15 - " + aac009.substring(0, 4) + "."
                + aac009.substring(4, 6) + ".14";
        //创建excle
        HSSFWorkbook wb = new HSSFWorkbook(); 
        HSSFSheet sheet = wb.createSheet("Sheet0"); 
        sheet.getPrintSetup().setPaperSize(XSSFPrintSetup.LETTER_ROTATED_PAPERSIZE);
        sheet.setDisplayGridlines(true);  
        HSSFRow row = sheet.createRow((int) 0);
        row = sheet.createRow(1);
        row.createCell(1).setCellValue(kprq);
        row.createCell(4).setCellValue("电力销售");
        row = sheet.createRow(2);
        row = sheet.createRow(3);
        row.createCell(0).setCellValue("用户名："+aac002);
        row.createCell(2).setCellValue("电费周期："+dfzq);
        row.createCell(5).setCellValue("户号："+hh);
        row = sheet.createRow(4);
        row = sheet.createRow(5);
        row.createCell(0).setCellValue("用电类型");
        row.createCell(1).setCellValue("起码");
        row.createCell(2).setCellValue("止码");
        row.createCell(3).setCellValue("倍率");
        row.createCell(4).setCellValue("电量");
        row.createCell(5).setCellValue("单价");
        row.createCell(6).setCellValue("金额");
        row = sheet.createRow(6);     
        row.createCell(0).setCellValue("居民用电");
        row.createCell(1).setCellValue(qm);
        row.createCell(2).setCellValue(zm);
        row.createCell(3).setCellValue("1");
        row.createCell(4).setCellValue(zdl+"");
        row.createCell(5).setCellValue("");
        row.createCell(6).setCellValue(ajze1);
        int i;
        for(i=1;i<=5;i++) {
            if(i<=infolist.size()) {
                if(infolist.get(i-1).getAac013()==null || infolist.get(i-1).getAac013().equals("")) {
                    continue;
                }
                row = sheet.createRow(6+i);
                row.createCell(0).setCellValue("");
                row.createCell(1).setCellValue("");
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("1");
                row.createCell(4).setCellValue(infolist.get(i-1).getAac013());
                row.createCell(5).setCellValue(infolist.get(i-1).getAac005());
                row.createCell(6).setCellValue(infolist.get(i-1).getAac006());
            }else {
                row = sheet.createRow(6+i); 
            }
            
        }
        row = sheet.createRow(7+i);
        row.createCell(0).setCellValue("金额合计：（大写）"+getRMB(Long.parseLong(zje+"")));
        row.createCell(4).setCellValue("本次缴费："+zje+".00");
        row.createCell(6).setCellValue("违约金：0");
        row = sheet.createRow(8+i);
        row.createCell(0).setCellValue("上月结余："+df.format(azje+Double.parseDouble(byjy)-Double.parseDouble(zje)));
        row.createCell(3).setCellValue("本月结余："+byjy);
        row.createCell(5).setCellValue("收费员："+UserUtils.getUser().getName());
        OutputStream out=null;
        String path="";
        String filename="";
        try {
            filename=System.currentTimeMillis()+".xls";
            path=request.getSession().getServletContext().getRealPath("/exltemple")+"\\"+System.currentTimeMillis()+".xls";
            out=new FileOutputStream(path);
            System.out.println("path:"+path);
            System.out.println("uri:"+request.getRequestURI());
            System.out.println("url:"+request.getRequestURL());
            wb.write(out);
            model.addAttribute("filename", filename);            
            invoiceService.updateStatus(aac001, "1", aac009,UserUtils.getUser().getId(),sdf.format(new Date()));
            return "modules/invoice/printback";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block              
                e.printStackTrace();
                return null;
            }
        }
    }

    @RequiresPermissions("invoice:info:drop")
    @RequestMapping(value = { "invoiceupdatestatus" })
    public String list2(String aac001, String flag, String aac009) {
        invoiceService.updateStatus(aac001, flag,aac009,"","");
        return "modules/invoice/invoiceInfo";
    }
    public static String getRMB(long money) {
        StringBuilder result = new StringBuilder("");
        if (money == 0) {
            return "零元整";
        }
        long integral = money;//整数部分
        int integralLen = (integral + "").length();
        int decimal = (int) (money % 100);//小数部分
        if (integral > 0) {
            int zeroCount = 0;
            for (int i = 0; i < integralLen; i++) {
                int unitLen = integralLen - i - 1;
                int d = Integer.parseInt((integral + "").substring(i, i + 1));//当前数字的值
                int quotient = unitLen / 4;//大单位的下标{"", "万", "亿"}
                int modulus = unitLen % 4;//获取单位的下标（整数部分都是以4个数字一个大单位，比如：个、十、百、千、个万、十万、百万、千万、个亿、十亿、百亿、千亿）
                if (d == 0) {
                    zeroCount++;
                } else {
                    if (zeroCount > 0) {
                        result.append(CN_UPPER_NUMBER[0]);
                    }
                    zeroCount = 0;
                    result.append(CN_UPPER_NUMBER[d]).append(RADICES[modulus]);
                }
                if (modulus == 0 && zeroCount < 4) {
                    result.append(BIG_RADICES[quotient]);
                }
            }
            result.append("元");
        }
        /*if (decimal > 0) {
            int j = decimal / 10;
            if (j > 0) {
                result.append(CN_UPPER_NUMBER[j]).append("角");
            }
            j = decimal % 10;
            if (j > 0) {
                result.append(CN_UPPER_NUMBER[j]).append("分");
            }
        } else {
            result.append("整");
        }*/
        return result.toString();
    }
    public static void main(String[] args) {
        DecimalFormat df=new DecimalFormat("#.0000");
        double d=1252.2563;
        String st=df.format(d);
        System.out.println(st);
    }
}
