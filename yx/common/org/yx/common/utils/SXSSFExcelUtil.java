package org.yx.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.yx.common.entity.PageData;

/**
 * 大数据导入导出
 * @author Administrator
 *
 */
public class SXSSFExcelUtil {

	/**
	 * 数据导出
	 * @param model
	 * @param response
	 */
	public static void export(Map<String, Object> dataMap, HttpServletResponse response){
		String[] titles = (String[]) dataMap.get("titles");
		String[] titles1 = (String[]) dataMap.get("titles1");
		String[] titles2 = (String[]) dataMap.get("titles2");
		String[] columns = (String[]) dataMap.get("columns");
		String[] columns1 = (String[]) dataMap.get("columns1");
		String filename = null!=dataMap.get("filename")?(String)dataMap.get("filename"):"";
		List<PageData> citylist = (List<PageData>) dataMap.get("citylist");
		List<PageData> projectlist = (List<PageData>) dataMap.get("projectlist");
		List<PageData> prosessionlist = (List<PageData>) dataMap.get("prosessionlist");
		
		//设置工作簿内存数据保有量
	    Workbook wb = new SXSSFWorkbook(5000);
	    Sheet sheet= wb.createSheet();
	    CellStyle cellStyle = wb.createCellStyle();
	    cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 居中
	    int len = titles.length;
	    int len1 = titles.length*4;
		Cell cell = null;
	    int jk=2;
	    for (int i = 0; i < len; i++) { // 设置标题城市
	    	
			String title = titles[i];
			cell = getCell(sheet, 0, jk);
			cell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, jk, jk+3));//添加合并
			jk = jk+4;
		}
	    for (int i = 0; i < 2; i++) { // 设置标题项目编号、项目名称
	    	String title1 = titles1[i];
	    	cell = getCell(sheet, 1, i);
	    	cell.setCellValue(title1);
	    }
	    int ii = 0;
	    for (int i = 1; i < len1+1; i++) { // 设置标题场次
	    	String title2 = titles2[ii];
	    	cell = getCell(sheet, 1, i+1);
	    	cell.setCellValue(title2);
	    	ii = i%4;
	    }
	    
	    for (int i = 0; i < projectlist.size(); i++) {
			PageData vpd = projectlist.get(i);
			String vpname = vpd.getString("NAME");
			String vpproject_no = vpd.getString("PROJECT_NO");
			for(int ki = 0;ki<prosessionlist.size();ki++){
				PageData pse = prosessionlist.get(ki);
				String psename = prosessionlist.get(ki).getString("NAME");
				String pseproject_no = prosessionlist.get(ki).getString("PROJECT_NO");
				String psecity = prosessionlist.get(ki).getString("CITY");
				if(vpname.equals(psename) && vpproject_no.equals(pseproject_no)){
					System.out.println("项目==="+psename);
					if(psecity != null && !psecity.equals("")){
						for(int kk = 0;kk<citylist.size();kk++){
							if(psecity.equals(citylist.get(kk).getString("CITY")) ){
								System.out.println("城市==="+psecity);
								int index = (kk+1)*4-2;
								for (int j = 0; j < 4; j++) {
									String varstr = pse.get(columns1[j]) != null ? toString(pse.get(columns1[j])) : "";
									cell = getCell(sheet, i + 2, j+index);
									cell.setCellValue(varstr);
								}
							}
						}
					}
					
				}
			}
			
			for (int j = 0; j < 2; j++) {
				String varstr = vpd.get(columns[j]) != null ? toString(vpd.get(columns[j])) : "";
				cell = getCell(sheet, i + 2, j);
				cell.setCellValue(varstr);
			}

		}
	    
	    filename = filename+Tools.date2Str(new Date(), "yyyyMMddHHmmss");
	    
		try {
			filename = URLEncoder.encode(filename,"UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename + ".xlsx");
			
			OutputStream  os = response.getOutputStream();
			//FileOutputStream os=new FileOutputStream("F:/123.xls");
			
			wb.write(os);
			//结束
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 数据导出（城市场次）
	 * @param model
	 * @param response
	 */
	public static void export1(Map<String, Object> dataMap, HttpServletResponse response){
		String[] titles = (String[]) dataMap.get("titles");
		String[] columns = (String[]) dataMap.get("columns");
		String filename = null!=dataMap.get("filename")?(String)dataMap.get("filename"):"";
		List<PageData> dataList = (List<PageData>) dataMap.get("dataList");
		
		//设置工作簿内存数据保有量
	    Workbook wb = new SXSSFWorkbook(5000);
	    Sheet sheet= wb.createSheet();
	    
	    int len = titles.length;
		Cell cell;
	    
	    for (int i = 0; i < len; i++) { // 设置标题
			String title = titles[i];
			cell = getCell(sheet, 0, i);
			cell.setCellValue(title);
		}
	    
	    for (int i = 0; i < dataList.size(); i++) {
			PageData vpd = dataList.get(i);
			for (int j = 0; j < len; j++) {
				String varstr = vpd.get(columns[j]) != null ? toString(vpd.get(columns[j])) : "";
				cell = getCell(sheet, i + 1, j);
				cell.setCellValue(varstr);
			}

		}
		
		filename = filename+Tools.date2Str(new Date(), "yyyyMMddHHmmss");
		
		try {
			filename = URLEncoder.encode(filename,"UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename + ".xlsx");
			
			OutputStream  os = response.getOutputStream();
			//FileOutputStream os=new FileOutputStream("F:/123.xls");
			
			wb.write(os);
			//结束
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static Cell getCell(Sheet sheet, int rowNum, int cellNum){
		Row row = sheet.getRow(rowNum);
		row = null!=row?row:sheet.createRow(rowNum);
		return row.createCell(cellNum);
	}
	
	private static String toString(Object value){
		String v = "";
		if(String.class == value.getClass()){
			v = value.toString();
		}
		else if(Double.class == value.getClass()){
			v = String.valueOf(value);
		}
		else if(Integer.class == value.getClass()){
			v = String.valueOf(value);
		}
		else if(Long.class == value.getClass()){
			v = String.valueOf(value);
		}
		else{
			v = value.toString();
		}
		
		return v;
	}
}
