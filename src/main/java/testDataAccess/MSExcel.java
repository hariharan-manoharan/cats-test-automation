package main.java.testDataAccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import main.java.executionSetup.TestParameters;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class MSExcel implements DataTable {

	public Workbook workBook;
	private String currentTestCase;
	private TestParameters testParameters;

	public MSExcel(String dataTablePath, TestParameters testParameters) {
		this.workBook = createWorkbookObj(dataTablePath);
		this.testParameters = testParameters;
	}

	public MSExcel(String dataTablePath) {
		this.workBook = createWorkbookObj(dataTablePath);

	}


	private Workbook createWorkbookObj(String dataTablePath) {
		Workbook workBook = null;

		if (dataTablePath.endsWith("xlsx")) {
			// this.workBook = new XSSFWorkbook(new FileInputStream(new
			// File(dataTablePath)));
		} else if (dataTablePath.endsWith("xls")) {
			try {
				workBook = new HSSFWorkbook(new FileInputStream(new File(dataTablePath)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("The Specified file is not Excel file");
		}
		return workBook;
	}

	private Sheet getSheet(String workSheetName) {

		Sheet workSheet = workBook.getSheet(workSheetName);
		return workSheet;

	}

	private int getRowNum(String workSheetName, String currentTestcase) {

		Sheet workSheet = workBook.getSheet(workSheetName);

		for (Row row : workSheet) {
			for (Cell cell : row) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim().equals(currentTestcase)) {
						return row.getRowNum();
					}
				}

			}
		}

		return 0;

	}

	private int getColNum(String workSheetName, String columnName) {

		Sheet workSheet = workBook.getSheet(workSheetName);

		for (Row row : workSheet) {
			for (Cell cell : row) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getSheet().getRow(0).getCell(cell.getColumnIndex()).getRichStringCellValue().toString()
							.equals(columnName)) {
						return cell.getColumnIndex();
					}
				}

			}
		}

		return 0;

	}

	public String getData(String workSheetName, int rowIndex, String columnName) {
		String data = null;
		Sheet sheet = getSheet(workSheetName);
		Row currentRow = sheet.getRow(rowIndex);
		int colNum = getColNum(workSheetName, columnName);

		Cell cell = currentRow.getCell(colNum);
		if(cell!=null){
			cell.setCellType(Cell.CELL_TYPE_STRING);
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			data = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			data = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			data = String.valueOf(cell.getNumericCellValue());
			break;
		}
		}
		return data;
	}

	@Override
	public String getData(String workSheetName, String columnName) {
		String data = null;
		Sheet sheet = getSheet(workSheetName);
		Row currentRow = sheet.getRow(getRowNum(workSheetName, currentTestCase));
		int colNum = getColNum(workSheetName, columnName);

		Cell cell = currentRow.getCell(colNum);
		
		if(cell!=null){
		cell.setCellType(Cell.CELL_TYPE_STRING);
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			data = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			data = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			data = String.valueOf(cell.getNumericCellValue());
			break;
		}
		}

		return data;
	}

	@Override
	public synchronized LinkedHashMap<String, String> getRowData(String workSheetName) {

		LinkedHashMap<String, String> rowData = new LinkedHashMap<String, String>();

		Sheet sheet = getSheet(workSheetName);
		Row colName = sheet.getRow(0);
		Row currentRow = sheet.getRow(getRowNum(workSheetName, currentTestCase));
		int lastColNum = currentRow.getLastCellNum();
		
		if(currentRow.getRowNum()!=0) {

		for (int col = 0; col < lastColNum; col++) {
			Cell key = colName.getCell(col, Row.RETURN_BLANK_AS_NULL);
			Cell value = currentRow.getCell(col, Row.RETURN_BLANK_AS_NULL);
			
			
			
			if (key == null) {
				// The spreadsheet is empty in this cell
			} else if (value != null){
				value.setCellType(Cell.CELL_TYPE_STRING);
				switch (value.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					rowData.put(key.getStringCellValue(), value.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					rowData.put(String.valueOf(key.getBooleanCellValue()), String.valueOf(value.getBooleanCellValue()));
					break;
				case Cell.CELL_TYPE_NUMERIC:
					rowData.put(String.valueOf(key.getNumericCellValue()), String.valueOf(value.getNumericCellValue()));
					break;
				case Cell.CELL_TYPE_FORMULA:
					switch(value.getCachedFormulaResultType()) {
		            case Cell.CELL_TYPE_NUMERIC:		                
		                rowData.put(String.valueOf(key.getNumericCellValue()), String.valueOf(value.getNumericCellValue()));
		                break;
		            case Cell.CELL_TYPE_STRING:		                
		                rowData.put(String.valueOf(key.getRichStringCellValue()), String.valueOf(value.getRichStringCellValue()));
		                break;
		        }
					break;
				}

			}

		}
		}
	
		return rowData;
	}

	@Override
	public synchronized LinkedHashMap<String, String> getRowData(String workSheetName, String currentTestCase) {

		LinkedHashMap<String, String> rowData = new LinkedHashMap<String, String>();
		
		Sheet sheet = getSheet(workSheetName);
		Row colName = sheet.getRow(0);
		Row currentRow = sheet.getRow(getRowNum(workSheetName, currentTestCase));
		int lastColNum = currentRow.getLastCellNum();
		
		if(currentRow.getRowNum()!=0) {

		for (int col = 0; col < lastColNum; col++) {
			Cell key = colName.getCell(col, Row.RETURN_BLANK_AS_NULL);
			Cell value = currentRow.getCell(col, Row.RETURN_BLANK_AS_NULL);
			
			if (key == null) {
				// The spreadsheet is empty in this cell
			} else if (value != null){
				value.setCellType(Cell.CELL_TYPE_STRING);
				switch (value.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					rowData.put(key.getStringCellValue(), value.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					rowData.put(String.valueOf(key.getBooleanCellValue()), String.valueOf(value.getBooleanCellValue()));
					break;
				case Cell.CELL_TYPE_NUMERIC:
					rowData.put(String.valueOf(key.getNumericCellValue()), String.valueOf(value.getNumericCellValue()));
					break;
				case Cell.CELL_TYPE_FORMULA:
					switch(value.getCachedFormulaResultType()) {
		            case Cell.CELL_TYPE_NUMERIC:		                
		                rowData.put(String.valueOf(key.getNumericCellValue()), String.valueOf(value.getNumericCellValue()));
		                break;
		            case Cell.CELL_TYPE_STRING:		                
		                rowData.put(String.valueOf(key.getRichStringCellValue()), String.valueOf(value.getRichStringCellValue()));
		                break;
		        }
					break;
				}

			}

		}
		}
		
		return rowData;
		
	}
	@Override
	public String writeData(String arg1, String arg2) {

		return null;
	}

	@Override
	public ArrayList<TestParameters> getRunManagerInfo() {
		ArrayList<TestParameters> runInfoArray = new ArrayList<TestParameters>();
		Sheet workSheet = workBook.getSheet("RunInfo");
		int lastRunNum = workSheet.getLastRowNum();

		for (int i = 1; i <= lastRunNum; i++) {
			TestParameters testParameter = new TestParameters();
			
			if(getData("RunInfo", i, "Execute").equalsIgnoreCase("Yes")){

			testParameter.setCurrentTestCase(getData("RunInfo", i, "TC_ID"));
			testParameter.setTestRailTestcaseID(getData("RunInfo", i, "TestRail_TC_ID"));
			testParameter.setDescription(getData("RunInfo", i, "Description"));
			testParameter.setSetCategory(getData("RunInfo", i, "SetCategory"));
			testParameter.setExecuteCurrentTestCase(getData("RunInfo", i, "Execute"));
			testParameter.setConnectDB(getData("RunInfo", i, "Connect_DB"));
			
			
			runInfoArray.add(testParameter);

			}
		}

		return runInfoArray;
	}

	@Override

	public void setCurrentRow(String currentTestcase) {
		this.currentTestCase = currentTestcase;
	}

}
