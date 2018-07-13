package org.xdemo.app.xutils.ext.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xdemo.app.xutils.ext.GsonTools;
import org.xdemo.app.xutils.j2se.ListUtils;
import org.xdemo.app.xutils.j2se.ReflectUtils;
import org.xdemo.app.xutils.j2se.StringUtils;

/**
 * Excel工具类,注解的方式
 * 
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月21日 下午3:30:55
 * @see Excel
 */
public class ExcelUtils {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Workbook write(List<T> list) throws Exception {

		if (ListUtils.isEmpty(list)) {
			// return null;
		}

		// 创建工作簿
		Workbook wb = new SXSSFWorkbook();
		// 创建一个工作表sheet
		Sheet sheet = wb.createSheet();
		// 申明行
		Row row = sheet.createRow(0);
		// 申明单元格
		Cell cell = null;

		Class clazz = list.get(0).getClass();

		List<Field> fields = ReflectUtils.getFields(clazz, true);

		Excel excel = null;

		int columnIndex = 0;

		CellStyle cs = null;

		Font font = null;

		Map<String, Class<? extends ExcelFormatter>> formatter = new TreeMap<String, Class<? extends ExcelFormatter>>();
		System.out.println(fields);
		// 写入标题
		for (Field field : fields) {
			// field.setAccessible(true);
			excel = field.getAnnotation(Excel.class);
			if (excel == null || excel.skip() == true) {
				continue;
			}

			formatter.put(field.getName(), excel.formatter());
			cell = row.createCell(columnIndex);
			cs = wb.createCellStyle();
			cs.setBorderBottom(BorderStyle.THIN);
			cs.setBorderLeft(BorderStyle.THIN);// 左边框
			cs.setBorderTop(BorderStyle.THIN);// 上边框
			cs.setBorderRight(BorderStyle.THIN);// 右边框
			cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cs.setFillForegroundColor(excel.bgColor().getIndex());
			font = wb.createFont();
			font.setColor(excel.fontColor().getIndex());// HSSFColor.VIOLET.index
			font.setBold(true);
			cs.setFont(font);
			cell.setCellStyle(cs);
			cell.setCellValue(excel.value());
			columnIndex++;
		}
		
		System.out.println(formatter);

		int rowIndex = 1;
		Object value = null;
		String method="";
		for (T t : list) {
			row = sheet.createRow(rowIndex);
			columnIndex = 0;
			for (Field field : fields) {
				cell = row.createCell(columnIndex);
				cs = wb.createCellStyle();
				cs.setWrapText(true);
				cs.setVerticalAlignment(VerticalAlignment.CENTER);
				cs.setBorderBottom(BorderStyle.THIN); // 下边框
				cs.setBorderLeft(BorderStyle.THIN);// 左边框
				cs.setBorderTop(BorderStyle.THIN);// 上边框
				cs.setBorderRight(BorderStyle.THIN);// 右边框
				cell.setCellStyle(cs);
				method="get" + StringUtils.firstCharToUpperCase(field.getName());
				value = clazz.getMethod(method, null).invoke(t, null);
				cell.setCellValue(formatter.get(field.getName()).newInstance().format(value));
				columnIndex++;
			}
			rowIndex++;
		}

		columnIndex = 0;
		// 重新设置列宽，否则不生效
		for (Field field : fields) {
			// field.setAccessible(true);
			excel = field.getAnnotation(Excel.class);
			if (excel == null || excel.skip() == true) {
				continue;
			}

			if (excel.width() == -1) {
				sheet.autoSizeColumn(columnIndex, true);
			} else {
				sheet.setColumnWidth(columnIndex, excel.width() * 256);
			}

			columnIndex++;
		}

		return wb;
	}

	public static <T> void writeToFile(List<T> list, String file) throws Exception {
		Workbook wb = write(list);
		FileOutputStream fos = new FileOutputStream(file);
		wb.write(fos);
		fos.close();
	}

	public static <T> void download(HttpServletResponse response, String name, List<T> list) {
		try {
			Workbook wb = write(list);
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + new String(name.getBytes("gbk"), "iso8859-1") + ".xlsx");
			response.setContentType("application/msexcel");// 定义输出类型
			wb.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取Excel文件
	 * @param file excel文件路径
	 * @param startRow 数据起始行
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String[]> read(String file,int startRow) throws FileNotFoundException, IOException {
		Workbook wb = new XSSFWorkbook(new FileInputStream(new File(file)));

		Sheet sheet = wb.getSheetAt(0);

		Row row = null;
		Cell cell = null;

		List<String[]> list = new ArrayList<String[]>();

		String[] rowData = new String[sheet.getRow(0).getPhysicalNumberOfCells()];
		for (Iterator<Row> it = sheet.rowIterator(); it.hasNext();) {
			row = it.next();
			for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
				cell = row.getCell(i);
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_BOOLEAN:
					rowData[i] = cell.getBooleanCellValue()+"";
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					rowData[i] = cell.getNumericCellValue()+"";
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						rowData[i] = DateUtil.getJavaDate(cell.getNumericCellValue())+"";
					}
					break;
				case XSSFCell.CELL_TYPE_STRING:
					rowData[i] = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_ERROR:
					rowData[i] = cell.getErrorCellValue()+"";
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					rowData[i] = null;
					break;
				case XSSFCell.CELL_TYPE_FORMULA:
					rowData[i] = cell.getCellFormula();
					break;
				default:
					rowData[i] = null;
					break;
				}
				list.add(rowData);
			}
		}

		return list;
	}
}
