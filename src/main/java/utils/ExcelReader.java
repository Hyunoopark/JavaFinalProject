package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {
	
	//private int cellLength = 0;
	
	public ArrayList<String> getData(String path) {
		ArrayList<String> values = new ArrayList<String>();
		
		System.out.println(path);
		
		try (InputStream inp = new FileInputStream(path)) {
		    //InputStream inp = new FileInputStream("workbook.xlsx");
		    
		        Workbook wb = WorkbookFactory.create(inp);
		        Sheet sheet = wb.getSheetAt(0);
		        Row row = sheet.getRow(2);
		        Cell cell = row.getCell(1);
		        if (cell == null)
		            cell = row.createCell(3);
		        
		        values.add(cell.getStringCellValue());
		        
		    } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return values;
	}
	
	public ArrayList<String> getData(InputStream is) {
		ArrayList<String> values = new ArrayList<String>();
		
		try (InputStream inp = is) {
		    //InputStream inp = new FileInputStream("workbook.xlsx");
		    
		        Workbook wb = WorkbookFactory.create(inp);
		        Sheet sheet = wb.getSheetAt(0);
		        Iterator<Row> iterator = sheet.iterator();
		        String s = "";
		        
		        while(iterator.hasNext()) {
		        	String value = "";
		        	String lastValue = "";
		        	
		        	Row currentRow = iterator.next();
		        	
		        	Iterator<Cell> cellIterator = currentRow.iterator();
		        	
		        	while(cellIterator.hasNext()) {
		        		
		        		Cell currentCell = cellIterator.next();
		        		
		        		switch (currentCell.getCellType()){
                        case FORMULA:
                            value = currentCell.getCellFormula();
                            break;
                        case NUMERIC:
                            value = currentCell.getNumericCellValue()+"";
                            break;
                        case STRING:
                            value = currentCell.getStringCellValue()+"";
                            break;
                        case BLANK:
                            value = "";
                            break;
                        case ERROR:
                            value = currentCell.getErrorCellValue()+"";
                            break;
                        default:
                            value = new String();
                            break;
                        }
		        		
		        		/*if(!currentCell.getCellType().equals()) {
		        			
		        		}*/
		        		
		        		lastValue += value + "///";
		        		
		        		//cellLength++;
		        		
		        		//System.out.print(value + " ");
		        	}
		        	
		        	values.add(lastValue);
		        	//System.out.println();
		        }
		        
		    } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return values;
	}
}
