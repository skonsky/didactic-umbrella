/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.invoice.entity.Invoice;
import com.thinkgem.jeesite.modules.invoice.entity.InvoiceArrearage;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * 
 * @author ThinkGem
 * @version 2013-03-10
 */
public class ImportExcel {

    private static Logger log = LoggerFactory.getLogger(ImportExcel.class);

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 标题行号
     */
    private int headerNum;

    /**
     * 构造函数
     * 
     * @param path
     *            导入文件，读取第一个工作表
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(String fileName, int headerNum) throws InvalidFormatException, IOException {
        this(new File(fileName), headerNum);
    }

    /**
     * 构造函数
     * 
     * @param path
     *            导入文件对象，读取第一个工作表
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(File file, int headerNum) throws InvalidFormatException, IOException {
        this(file, headerNum, 0);
    }

    /**
     * 构造函数
     * 
     * @param path
     *            导入文件
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @param sheetIndex
     *            工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(String fileName, int headerNum, int sheetIndex) throws InvalidFormatException, IOException {
        this(new File(fileName), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     * 
     * @param path
     *            导入文件对象
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @param sheetIndex
     *            工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(File file, int headerNum, int sheetIndex) throws InvalidFormatException, IOException {
        this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     * 
     * @param file
     *            导入文件对象
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @param sheetIndex
     *            工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(MultipartFile multipartFile, int headerNum, int sheetIndex)
            throws InvalidFormatException, IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     * 
     * @param path
     *            导入文件对象
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @param sheetIndex
     *            工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex)
            throws InvalidFormatException, IOException {
        if (StringUtils.isBlank(fileName)) {
            throw new RuntimeException("导入文档为空!");
        } else if (fileName.toLowerCase().endsWith("xls")) {
            this.wb = new HSSFWorkbook(is);
        } else if (fileName.toLowerCase().endsWith("xlsx")) {
            this.wb = new XSSFWorkbook(is);
        } else {
            throw new RuntimeException("文档格式不正确!");
        }
        if (this.wb.getNumberOfSheets() < sheetIndex) {
            throw new RuntimeException("文档中没有工作表!");
        }
        this.sheet = this.wb.getSheetAt(sheetIndex);
        this.headerNum = headerNum;
        log.debug("Initialize success.");
    }

    /**
     * 获取行对象
     * 
     * @param rownum
     * @return
     */
    public Row getRow(int rownum) {
        return this.sheet.getRow(rownum);
    }

    /**
     * 获取数据行号
     * 
     * @return
     */
    public int getDataRowNum() {
        return headerNum + 1;
    }

    /**
     * 获取最后一个数据行号
     * 
     * @return
     */
    public int getLastDataRowNum() {
        return this.sheet.getLastRowNum() + headerNum;
    }

    /**
     * 获取最后一个列号
     * 
     * @return
     */
    public int getLastCellNum() {
        return this.getRow(headerNum).getLastCellNum();
    }

    /**
     * 获取单元格值
     * 
     * @param row
     *            获取的行
     * @param column
     *            获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column,String type) {
        Object val = "";
        DecimalFormat df = new DecimalFormat("#.####");
        DecimalFormat df1 = new DecimalFormat("0");
        try {
            Cell cell = row.getCell(column);
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    if (type.equals("1")) {
                        val = df1.format(cell.getNumericCellValue());
                    } else {
                        val = df.format(cell.getNumericCellValue());
                    }
                    //val=cell.getNumericCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                    val = cell.getCellFormula();
                } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                    val = cell.getErrorCellValue();
                }
            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }

    public List<InvoiceArrearage> getArrearageFromExcel() throws ParseException {
        if (sheet == null) {
            return null;
        }
        InvoiceArrearage ia;
        List<InvoiceArrearage> inlist = new ArrayList<InvoiceArrearage>();
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        SimpleDateFormat sdf1 =   new SimpleDateFormat( "yyyyMM" );
        Calendar cal=Calendar.getInstance();
        Date date;
        Row hssfRow;
        String aac001;
        String aac002;
        String aac006;
        String aac009;
        String userid = UserUtils.getUser().getId();
        String aac200;
        int start;
        start = sheet.getLastRowNum();
        for (int i = 1; i < start+1; i++) {
            hssfRow = sheet.getRow(i);
            ia = new InvoiceArrearage();
            aac001 = String.valueOf(getCellValue(hssfRow, 1,"1")); // 户号
            aac002 = String.valueOf(getCellValue(hssfRow, 2,"")); // 用户名称
            //aac006 = (String) (getCellValue(hssfRow, 3)+"");// 收费金额
            aac006 = String.valueOf(getCellValue(hssfRow, 3,""));
            aac009 = String.valueOf(getCellValue(hssfRow, 0,""));// 收费日期
            aac200 = String.valueOf(getCellValue(hssfRow, 4,""));
            System.out.println(aac009);
            date=sdf.parse(aac009);
            cal.setTime(date);
            cal.add(Calendar.MONTH,-1);
            aac009=sdf1.format(cal.getTime());
            ia.setAac001(aac001);
            ia.setAac002(aac002);
            ia.setAac006(aac006);
            ia.setAac009(aac009);
            ia.setUserid(userid);
            ia.setFlag("2");
            ia.setImpdate(sdf.format(new Date()));
            ia.setAac200(aac200);
            inlist.add(ia);
        }
        return inlist;
    }

    public List<Invoice> getInvoiceListFromExcel() {
        if (sheet == null) {
            return null;
        }
        List<Invoice> inlist = new ArrayList<Invoice>();
        Invoice iv;
        Invoice ivTemp = new Invoice();
        Row hssfRow;
        int pageStart;
        int pageEnd;
        int info;
        int page ;
        if((sheet.getLastRowNum() + 1) % 45==0) {
            page = (sheet.getLastRowNum() + 1) / 45;
        }else {
            page = (sheet.getLastRowNum() + 1) / 45 + 1;
        }
        if(page<=0) {
            page=1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String aac001;
        String aac002;
        String aac003;
        String aac004;
        String aac013;
        String aac005;
        String aac006;
        String aac009;
        String aac011;
        String aac012;
        String userid;
        String impdate;
        String Taac005;

        for (int i = 0; i < page; i++) {
            info = 2 + i * 45;
            pageStart = 4 + i * 45;
            pageEnd = 4 + i * 45 + 39;
            aac012 = (String.valueOf(getCellValue(sheet.getRow(info), 1,""))).substring(6); // 抄表段名称
            aac011 = (String.valueOf(getCellValue(sheet.getRow(info), 7,""))).substring(5); // 抄表人员
            aac009 = (String.valueOf(getCellValue(sheet.getRow(info), 12,""))).substring(5); // 电费年月
            userid = UserUtils.getUser().getId();
            impdate = sdf.format(new Date());
            for (int j = pageStart; j < pageEnd; j++) {
                hssfRow = sheet.getRow(j);
                aac001 = String.valueOf(getCellValue(hssfRow, 1,"1")); // 户号
                aac002 = String.valueOf(getCellValue(hssfRow, 2,"")); // 用户名称
                aac003 = String.valueOf(getCellValue(hssfRow, 3,"")); // 上次示数
                aac004 = String.valueOf(getCellValue(hssfRow, 4,"")); // 本次示数
                aac013 = String.valueOf(getCellValue(hssfRow, 5,"")); // 电量数
                aac005 = String.valueOf(getCellValue(hssfRow, 6,"")); // 档位
                Taac005 = String.valueOf(getCellValue(hssfRow, 6,""));
                aac006 = String.valueOf(getCellValue(hssfRow, 7,""));
                if (aac001.equals("终止")) {
                    break;
                }
                if (!aac001.equals("") && !aac002.equals("")) {
                    ivTemp.setAac001(aac001);
                    ivTemp.setAac002(aac002);
                    ivTemp.setAac003(aac003);
                    ivTemp.setAac004(aac004);
                    iv = new Invoice();
                    if (!aac013.equals("") && !Taac005.equals("")) {
                        iv.setAac001(aac001);
                        iv.setAac002(aac002);
                        iv.setAac003(aac003);
                        iv.setAac004(aac004);
                        iv.setAac013(aac013);
                        iv.setAac005(aac005);
                        iv.setAac006(aac006);
                        iv.setAac009(aac009);
                        iv.setAac011(aac011);
                        iv.setAac012(aac012);
                        iv.setUserid(userid);
                        iv.setImpdate(impdate);
                        inlist.add(iv);
                    } else {
                        continue;
                    }
                } else {
                    if (!aac013.equals("") && !Taac005.equals("")) {
                        iv = new Invoice();
                        iv.setAac001(ivTemp.getAac001());
                        iv.setAac002(ivTemp.getAac002());
                        iv.setAac003(ivTemp.getAac003());
                        iv.setAac004(ivTemp.getAac004());
                        iv.setAac013(aac013);
                        iv.setAac005(aac005);
                        iv.setAac006(aac006);
                        iv.setAac009(aac009);
                        iv.setAac011(aac011);
                        iv.setAac012(aac012);
                        iv.setUserid(userid);
                        iv.setImpdate(impdate);
                        inlist.add(iv);
                    }                   
                }

            }
            for (int k = pageStart; k < pageEnd; k++) {
                hssfRow = sheet.getRow(k);
                aac001 = String.valueOf(getCellValue(hssfRow, 8,"1")); // 户号
                aac002 = String.valueOf(getCellValue(hssfRow, 9,"")); // 用户名称
                aac003 = String.valueOf(getCellValue(hssfRow, 10,"")); // 上次示数
                aac004 = String.valueOf(getCellValue(hssfRow, 11,"")); // 本次示数
                aac013 = String.valueOf(getCellValue(hssfRow, 12,"")); // 电量数
                aac005 = String.valueOf(getCellValue(hssfRow, 13,"")); // 档位
                Taac005 = String.valueOf(getCellValue(hssfRow, 13,""));
                //aac006 = (String) (getCellValue(hssfRow, 14)); // 金额
                aac006 = String.valueOf(getCellValue(hssfRow, 14,""));
                if (aac001.equals("终止")) {
                    break;
                }
                if (!aac001.equals("") && !aac002.equals("")) {
                    ivTemp.setAac001(aac001);
                    ivTemp.setAac002(aac002);
                    ivTemp.setAac003(aac003);
                    ivTemp.setAac004(aac004);
                    iv = new Invoice();
                    if (!aac013.equals("") && !Taac005.equals("")) {
                        iv.setAac001(aac001);
                        iv.setAac002(aac002);
                        iv.setAac003(aac003);
                        iv.setAac004(aac004);
                        iv.setAac013(aac013);
                        iv.setAac005(aac005);
                        iv.setAac006(aac006);
                        iv.setAac009(aac009);
                        iv.setAac011(aac011);
                        iv.setAac012(aac012);
                        iv.setAac007("2");
                        iv.setUserid(userid);
                        iv.setImpdate(impdate);
                        inlist.add(iv);
                    } else {
                        continue;
                    }
                } else {
                    iv = new Invoice();
                    iv.setAac001(ivTemp.getAac001());
                    iv.setAac002(ivTemp.getAac002());
                    iv.setAac003(ivTemp.getAac003());
                    iv.setAac004(ivTemp.getAac004());
                    iv.setAac013(aac013);
                    iv.setAac005(aac005);
                    iv.setAac006(aac006);
                    iv.setAac009(aac009);
                    iv.setAac011(aac011);
                    iv.setAac012(aac012);
                    iv.setAac007("2");
                    iv.setUserid(userid);
                    iv.setImpdate(impdate);
                    inlist.add(iv);
                }

            }
        }
        return inlist;
    }

    /**
     * 获取导入数据列表
     * 
     * @param cls
     *            导入对象类型
     * @param groups
     *            导入分组
     */
    public <E> List<E> getDataList(Class<E> cls, int... groups) throws InstantiationException, IllegalAccessException {
        List<Object[]> annotationList = Lists.newArrayList();
        // Get annotation field
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == 2)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int g : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int efg : ef.groups()) {
                            if (g == efg) {
                                inGroup = true;
                                annotationList.add(new Object[] { ef, f });
                                break;
                            }
                        }
                    }
                } else {
                    annotationList.add(new Object[] { ef, f });
                }
            }
        }
        // Get annotation method
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            ExcelField ef = m.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == 2)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int g : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int efg : ef.groups()) {
                            if (g == efg) {
                                inGroup = true;
                                annotationList.add(new Object[] { ef, m });
                                break;
                            }
                        }
                    }
                } else {
                    annotationList.add(new Object[] { ef, m });
                }
            }
        }
        // Field sorting
        Collections.sort(annotationList, new Comparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return new Integer(((ExcelField) o1[0]).sort()).compareTo(new Integer(((ExcelField) o2[0]).sort()));
            };
        });
        // log.debug("Import column count:"+annotationList.size());
        // Get excel data
        List<E> dataList = Lists.newArrayList();
        for (int i = this.getDataRowNum(); i < this.getLastDataRowNum(); i++) {
            E e = (E) cls.newInstance();
            int column = 0;
            Row row = this.getRow(i);
            StringBuilder sb = new StringBuilder();
            for (Object[] os : annotationList) {
                Object val = this.getCellValue(row, column++,"");
                if (val != null) {
                    ExcelField ef = (ExcelField) os[0];
                    // If is dict type, get dict value
                    if (StringUtils.isNotBlank(ef.dictType())) {
                        val = DictUtils.getDictValue(val.toString(), ef.dictType(), "");
                        // log.debug("Dictionary type value: ["+i+","+colunm+"]
                        // " + val);
                    }
                    // Get param type and type cast
                    Class<?> valType = Class.class;
                    if (os[1] instanceof Field) {
                        valType = ((Field) os[1]).getType();
                    } else if (os[1] instanceof Method) {
                        Method method = ((Method) os[1]);
                        if ("get".equals(method.getName().substring(0, 3))) {
                            valType = method.getReturnType();
                        } else if ("set".equals(method.getName().substring(0, 3))) {
                            valType = ((Method) os[1]).getParameterTypes()[0];
                        }
                    }
                    // log.debug("Import value type: ["+i+","+column+"] " +
                    // valType);
                    try {
                        if (valType == String.class) {
                            String s = String.valueOf(val.toString());
                            if (StringUtils.endsWith(s, ".0")) {
                                val = StringUtils.substringBefore(s, ".0");
                            } else {
                                val = String.valueOf(val.toString());
                            }
                        } else if (valType == Integer.class) {
                            val = Double.valueOf(val.toString()).intValue();
                        } else if (valType == Long.class) {
                            val = Double.valueOf(val.toString()).longValue();
                        } else if (valType == Double.class) {
                            val = Double.valueOf(val.toString());
                        } else if (valType == Float.class) {
                            val = Float.valueOf(val.toString());
                        } else if (valType == Date.class) {
                            val = DateUtil.getJavaDate((Double) val);
                        } else {
                            if (ef.fieldType() != Class.class) {
                                val = ef.fieldType().getMethod("getValue", String.class).invoke(null, val.toString());
                            } else {
                                val = Class
                                        .forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
                                                "fieldtype." + valType.getSimpleName() + "Type"))
                                        .getMethod("getValue", String.class).invoke(null, val.toString());
                            }
                        }
                    } catch (Exception ex) {
                        log.info("Get cell value [" + i + "," + column + "] error: " + ex.toString());
                        val = null;
                    }
                    // set entity value
                    if (os[1] instanceof Field) {
                        Reflections.invokeSetter(e, ((Field) os[1]).getName(), val);
                    } else if (os[1] instanceof Method) {
                        String mthodName = ((Method) os[1]).getName();
                        if ("get".equals(mthodName.substring(0, 3))) {
                            mthodName = "set" + StringUtils.substringAfter(mthodName, "get");
                        }
                        Reflections.invokeMethod(e, mthodName, new Class[] { valType }, new Object[] { val });
                    }
                }
                sb.append(val + ", ");
            }
            dataList.add(e);
            log.debug("Read success: [" + i + "] " + sb.toString());
        }
        return dataList;
    }

    /**
     * 导入测试
     */
    public static void main(String[] args) throws Throwable {

        //ImportExcel ei = new ImportExcel("F:\\Download\\石油公司.xls", 0);
        ImportExcel ei = new ImportExcel("F:\\Download\\1501464909863174.xls", 0);
        List l = ei.getArrearageFromExcel();
        System.out.println(l.size());

        for (int i = 0; i < l.size(); i++) {
            System.out.println(l.get(i));
        }

    }

}
