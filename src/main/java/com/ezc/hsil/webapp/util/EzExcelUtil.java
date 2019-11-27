package com.ezc.hsil.webapp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class EzExcelUtil {

	public String getCellValue(String fileExt,Iterator cellIter)
	{
		String celValue = "";
		if(cellIter.hasNext())
		{
			if("xlsx".equals(fileExt))
			{
				XSSFCell myCellX = (XSSFCell) cellIter.next();
				if(myCellX.getCellTypeEnum() == CellType.NUMERIC) 
				{
					celValue=new BigDecimal(myCellX.getNumericCellValue())+"";
					//log.debug("Integer:::"+celValue);
				}
				else if(myCellX.getCellTypeEnum() == CellType.STRING) 
				{
					celValue=myCellX.getStringCellValue()+"";
					//log.debug("String:::"+celValue);
				}
				else
				{
					celValue=myCellX.getStringCellValue()+"";
				}
			}
			else
			{
				HSSFCell myCellH = (HSSFCell) cellIter.next();
				if(myCellH.getCellTypeEnum() == CellType.NUMERIC) 
				{
					celValue=myCellH.getNumericCellValue()+"";
					//log.debug("Integer:::"+celValue);
				}
				else if(myCellH.getCellTypeEnum() == CellType.STRING) 
				{
					celValue=myCellH.getStringCellValue()+"";
					//log.debug("String:::"+celValue);
				}else{
					celValue=myCellH.getStringCellValue()+"";
				}
			}
			if(celValue == null || "null".equals(celValue.trim()) || "".equals(celValue.trim()))
				celValue ="";
		}
		return celValue;
	}	

	public List<Object[]> readExcel(InputStream uploadedInputStream,String fileName)
	{
		XSSFWorkbook wbX 	= null;
		HSSFWorkbook wbH 	= null;
		HSSFSheet mySheetH	= null;
		XSSFSheet mySheetX 	= null;
		Iterator rowIter	= null;
		int ExtPos 	= fileName.lastIndexOf(".");
		String fileExt	= fileName.substring(ExtPos+1,fileName.length());
		List<Object[]> retObjArr = new ArrayList<Object[]>(); 
		try {
			if("xlsx".equals(fileExt))
			{
				wbX 		= new XSSFWorkbook(uploadedInputStream);
				mySheetX	= wbX.getSheetAt(0);
				rowIter 	= mySheetX.rowIterator();
			}
			else
			{
				wbH 		= new HSSFWorkbook(uploadedInputStream);
				mySheetH 	= wbH.getSheetAt(0);
				rowIter 	= mySheetH.rowIterator();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		int firstRow=0;
		int noOfCols = 0;
		try{
			HSSFRow myRowH 	= null;
			XSSFRow myRowX 	= null;
			while (rowIter.hasNext()) 
			{
				if("xlsx".equals(fileExt))
					myRowX 	= (XSSFRow) rowIter.next();
				else
					myRowH 	= (HSSFRow) rowIter.next();
				Iterator cellIter = null;
				if("xlsx".equals(fileExt))
					cellIter = myRowX.cellIterator();
				else
					cellIter = myRowH.cellIterator();
				if(firstRow==0)
				{
					
					boolean endLoop = true;
					while(endLoop)
					{
						String colName = getCellValue(fileExt,cellIter);
						if(!"".equals(colName))
						{
							//retObj.addColumn(colName.toUpperCase());
							noOfCols++;
						}else{
							endLoop=false;
						}
					}
					
					
					firstRow++;
				}
				else
				{
					Object[] rowObj = new Object[noOfCols];
					for(int i=0;i<noOfCols;i++)
					{
						String colValue = "";//getCellValue(fileExt,cellIter);
						if("xlsx".equals(fileExt))
						{
							XSSFCell myCellX = (XSSFCell) myRowX.getCell(i);
							if(myCellX != null)
							{
								if(myCellX.getCellTypeEnum() == CellType.NUMERIC) 
								{
									colValue=new BigDecimal(myCellX.getNumericCellValue())+"";
									log.debug("Integer:::"+colValue);
								}
								else if(myCellX.getCellTypeEnum() == CellType.STRING) 
								{
									colValue=myCellX.getStringCellValue()+"";
									log.debug("String:::"+colValue);
								}
								else
								{
									colValue=myCellX.getStringCellValue()+"";
								}
							}
							
						}
						else
						{
							HSSFCell myCellH = (HSSFCell) myRowH.getCell(i);
							if(myCellH != null)
							{
								if(myCellH.getCellTypeEnum() == CellType.NUMERIC) 
								{
									colValue=myCellH.getNumericCellValue()+"";
									log.debug("Integer:::"+colValue);
								}
								else if(myCellH.getCellTypeEnum() == CellType.STRING) 
								{
									colValue=myCellH.getStringCellValue()+"";
									log.debug("String:::"+colValue);
								}else{
									colValue=myCellH.getStringCellValue()+"";
								}
							}
						}
						//retObj.setFieldValue(i, colValue);
						rowObj[i]=colValue;
					}
					/*
					boolean endLoop = true;
					int colIndex = 0;
					while(endLoop)
					{
						String colValue = getCellValue(fileExt,cellIter);
						if(!"".equals(colValue))
						{
							retObj.setFieldValue(colIndex, colValue);
							colIndex++;
						}else{
							endLoop=false;
						}
					}
					*/
					
					retObjArr.add(rowObj);


				}	
			}
		}
		catch(Exception Ex)
		{
			log.debug(Ex+":::Exception:::");

		}
		finally
		{
			try
			{
				uploadedInputStream.close();
			}
			catch(Exception ex){}
		} 
		return retObjArr;
	}
	public StreamingResponseBody writeExcel(String [] headerCols,List<Object[]> objList,String sheetName)
	{
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(sheetName);
		int rowNum = 0;
		log.debug("Creating excel");
		Row headerRow = sheet.createRow(rowNum++);
		int headerColNum = 0;
		String [] docArray = headerCols;
		CellStyle headerStyle = workbook.createCellStyle();//Create style
	    Font font = workbook.createFont();//Create font
	    font.setBold(true);//Make font bold
	    headerStyle.setFont(font);//set it to bold
		for(int i=0;i<docArray.length;i++)
		{
			Cell headerCell= headerRow.createCell(headerColNum++);
			headerCell.setCellValue(docArray[i]);
			headerCell.setCellStyle(headerStyle);
		}
		if(objList != null)
		{
			for (Object[] obj : objList) {
				Row row = sheet.createRow(rowNum++);
				int colNum = 0;
				for (Object field : obj) {
					Cell cell = row.createCell(colNum++);
					if (field instanceof String) {
						cell.setCellValue((String) field);
					} else if (field instanceof Integer) {
						cell.setCellValue((Integer) field);
					}else if (field instanceof Double) {
						cell.setCellValue((Double) field);
					}else if (field instanceof Long) {
						cell.setCellValue((Long) field);
					}
				}
			}
		}
		
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final byte[] bytes = outputStream.toByteArray();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

		
		return outputStream1 -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            	outputStream1.write(data, 0, nRead);
            }
            inputStream.close();
        };
	}
	

}
