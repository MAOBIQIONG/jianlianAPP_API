package org.yx.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	public static void main(String[] args) {
		String filename = "E:\\work\\wb\\门店\\管理系统\\管理系统\\基础功能管理\\连锁店管理\\数据.xls";
		List<Map<Integer, String>> list = ExcelUtil.readExcel(filename, 1, 0, 0);
		
		for(Map<Integer, String> map : list){
			for(int i=0; i<map.size(); i++){
				System.out.print(map.get(i));
				System.out.print(", ");
			}
			System.out.println("");
		}
	}
	
	public static List<Map<Integer, String>> readExcel(String file, int startrow, int startcol, int sheetnum) {
		return readExcel(new File(file), startrow, startcol, sheetnum);
	}
	
	/**
	 * @param filepath //文件路径
	 * @param filename //文件名
	 * @param startrow //开始行号
	 * @param startcol //开始列号
	 * @param sheetnum //sheet
	 * @return list
	 */
	public static List<Map<Integer, String>> readExcel(String filepath, String filename, int startrow, int startcol, int sheetnum) {
		File file = new File(filepath, filename);
		return readExcel(file, startrow, startcol, sheetnum);
	}
	
	public static List<Map<Integer, String>> readExcel(String filepath, String filename, int startrow, int startcol,String sheetname,int minColumns){
		List<Map<Integer, String>> dataList = new ArrayList<Map<Integer, String>>();
		Map<Integer, String> rowData;
		try {
			List<String[]> list = XLSXCovertCSVReader.readerExcel(filepath + filename, sheetname, minColumns);
			for(int i = 0; i < list.size(); i++){
				if(i < startrow) continue;
				rowData = new HashMap<Integer, String>();
				String[] data = list.get(i);
				for(int j = 0; j < data.length; j++){
					if(j < startcol) continue;
					rowData.put(j, data[j]);
				}
				dataList.add(rowData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	public static List<Map<Integer, String>> readExcel(File file, int startrow, int startcol, int sheetnum){
		List<Map<Integer, String>> sheetList = new ArrayList<Map<Integer, String>>();
		Map<Integer, String> rowData = null;
		
		FileInputStream fi = null;
		Workbook wb = null;
		boolean isE2007 = false;    //判断是否是excel2007格式  
        if(file.getName().endsWith("xlsx"))  
            isE2007 = true; 
		try {
			fi = new FileInputStream(file);
//			wb = new HSSFWorkbook(fi);
			//根据文件格式(2003或者2007)来初始化  
            if(isE2007)  
                wb = new XSSFWorkbook(fi);  
            else  
                wb = new HSSFWorkbook(fi);
			Sheet sheet = wb.getSheetAt(sheetnum); 					//sheet 从0开始
			int rowNum = sheet.getLastRowNum() + 1; 					//取得最后一行的行号
			
			Cell cell = null;
			Row row = null;
			int cellNum = 0;
			String cellValue = "";

			for (int i = startrow; i < rowNum; i++) {					//行循环开始
				rowData = new HashMap<Integer, String>();
				row = sheet.getRow(i); 							//行
				cellNum = row.getLastCellNum(); 					//每行的最后一个单元格位置

				for (int j = startcol; j < cellNum; j++) {				//列循环开始
					cell = row.getCell(j);
					cellValue = getCell(cell);
					rowData.put(j, cellValue);
				}
				
				sheetList.add(rowData);
			}

		} catch (Exception e) {
			//System.out.println(e);
		}finally{
//			try {
//				if(null!=wb) wb.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			try {
				if(null!=fi) fi.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sheetList;
	}

	/**
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCell(Cell cell){
		String cellValue = null;
		if (null != cell) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = Boolean.toString(cell.getBooleanCellValue());
				break;
			// 数值
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
					SimpleDateFormat sdf = null;
					if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
						sdf = new SimpleDateFormat("HH:mm");
					} else if (cell.getCellStyle().getDataFormat() == 178) {
						sdf = new SimpleDateFormat("HH:mm");
					} else{// 日期
						sdf = new SimpleDateFormat("yyyy-MM-dd");
					}
					Date date = cell.getDateCellValue();
					cellValue = sdf.format(date);
				} else if (cell.getCellStyle().getDataFormat() == 58) {
					// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd");
					double value = cell.getNumericCellValue();
					Date date = org.apache.poi.ss.usermodel.DateUtil
							.getJavaDate(value);
					cellValue = sdf.format(date);
				} else {
					double value = cell.getNumericCellValue();
					CellStyle style = cell.getCellStyle();
					DecimalFormat format = new DecimalFormat();
					String temp = style.getDataFormatString();
					// 单元格设置成常规
					if (temp.equals("General")) {
						format.applyPattern("#");
					}
					cellValue = format.format(value);
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue().trim();
				break;
			case Cell.CELL_TYPE_ERROR:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_FORMULA:
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue();
				if (cellValue != null) {
					cellValue = cellValue.replaceAll("#N/A", "").trim();
				}
				break;
			default:
				cellValue = "";
				break;
			}
		} 
		return cellValue;
	}
}
