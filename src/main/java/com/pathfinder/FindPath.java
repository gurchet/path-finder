package com.pathfinder;

import java.io.File; 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List; 

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xpath.operations.Variable;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.tavant.base.DriverFactory;
import com.tavant.base.WebPage;
import com.tavant.utils.TwfException;

public class FindPath extends WebPage{

	String baseUrl;
	String filePath;
	
	public FindPath(String FilePath,String baseUrl){
		this.baseUrl = baseUrl;
		this.filePath = filePath;
	}
/*
 * Method is to find all 
 */
	public void getAllElementOfPage() throws IOException, TwfException {

		System.out.println(" In FindPath constructor - baseUrl: " + this.baseUrl + "filePath : " + filePath );
		//String FilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\Repository.xls";
		int SheetNumber = 0;
		int CellNumberForXPath = 2;
		int CellNumberForElementName = 1;
		//WebDriver driver = DriverFactory.getDriver();
		WebDriver driver = new FirefoxDriver();
		driver.get(baseUrl);
		// Fetching File
		FileInputStream inputFile = new FileInputStream(new File(filePath));
		XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
		XSSFSheet sheet = workbook.getSheetAt(SheetNumber);

		System.out
				.println(" \n  getAllElementOfPage :  get Physical Number Of Rows :"
						+ sheet.getPhysicalNumberOfRows());
		System.out.println(" \n  getAllElementOfPage :  get Last Row Num :"
				+ sheet.getLastRowNum());

		if (sheet.equals(null))
			System.out.println("\n getAllElementOfPage : no Rows in Sheet ");
		// Search All elements in 'Popular course' section with the help of
		// ancestor of the anchor whose text is 'SELENIUM'
		List<WebElement> elements = driver.findElements(By.xpath(".//*"));
		if (elements.isEmpty())
			addExceptionToReport("Root Node not found: ", null,
					elements.toString());

		for (WebElement webElement : elements) {
			if(webElement.getTagName()=="link" || webElement.getTagName()=="script" )
				continue;
			System.out.println("in method : getAllElementOfPage");
			// System.out.println(getAbsoluteXPath(webElement,driver));
			String absPath = getAbsoluteXPath(webElement, driver);
			if (absPath.isEmpty())
				addExceptionToReport("No XPath found for element : ",
						webElement.getText(), absPath);
			else
				System.out.println("absPath : " + absPath);

			XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
			XSSFCell cell = row.createCell(CellNumberForXPath);
			XSSFCell cell2 = row.createCell(CellNumberForElementName);
			// Set a string to be the value
			cell.setCellValue(absPath);
			// ----------------------------------------------

			if (webElement.getAttribute("NAME") != null && webElement.getAttribute("NAME").length() < 10) {
				System.out.println("NAME Name : "
						+ webElement.getAttribute("NAME"));
				cell2.setCellValue(webElement.getAttribute("NAME"));
			} else if (webElement.getAttribute("alt") != null  && webElement.getAttribute("alt").length() < 10) {
				System.out.println("alternative Name : "
						+ webElement.getAttribute("alt"));
				cell2.setCellValue(webElement.getAttribute("alt"));
			} else if (webElement.getAttribute("title") != null  && webElement.getAttribute("title").length() < 10) {
				System.out.println("title Name : "
						+ webElement.getAttribute("title"));
				cell2.setCellValue(webElement.getAttribute("title"));
			} else if (webElement.getAttribute("id") != null  && webElement.getAttribute("id").length() < 10) {
				System.out
						.println("id Name : " + webElement.getAttribute("id"));
				cell2.setCellValue(webElement.getAttribute("id"));
			} else if (webElement.getAttribute("CLASSNAME") != null  && webElement.getAttribute("class").length() < 10) {
				System.out.println("CLASS NAME Name : "
						+ webElement.getAttribute("CLASSNAME"));
				cell2.setCellValue(webElement.getAttribute("CLASSNAME"));
			} else if (webElement.getAttribute("CSS") != null  && webElement.getAttribute("CSS").length() < 10) {
				System.out.println("CSS_SELECTOR Name : "
						+ webElement.getAttribute("CSS"));
				cell2.setCellValue(webElement.getAttribute("CSS"));
			} else {
				System.out.println("nothing there");
				cell2.setCellValue("Your Choice :)");
			}

		}
		FileOutputStream outFile;
		try {
			outFile = new FileOutputStream(new File(filePath));
			workbook.write(outFile);
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	// ///////////////////////////////////////////////////////////

	public static String getAbsoluteXPath(WebElement element, WebDriver driver) {
		System.out.println("in method : getAbsoluteXPath");
		return (String) ((JavascriptExecutor) driver)
				.executeScript(
						"function absoluteXPath(element) {"
								+ "var comp, comps = [];"
								+ "var parent = null;"
								+ "var xpath = '';"
								+ "var getPos = function(element) {"
								+ "var position = 1, curNode;"
								+ "if (element.nodeType == Node.ATTRIBUTE_NODE) {"
								+ "return null;"
								+ "}"
								+ "for (curNode = element.previousSibling; curNode; curNode = curNode.previousSibling){"
								+ "if (curNode.nodeName == element.nodeName) {"
								+ "++position;"
								+ "}"
								+ "}"
								+ "return position;"
								+ "};"
								+

								"if (element instanceof Document) {"
								+ "return '/';"
								+ "}"
								+

								"for (; element && !(element instanceof Document); element = element.nodeType == Node.ATTRIBUTE_NODE ? element.ownerElement : element.parentNode) {"
								+ "comp = comps[comps.length] = {};"
								+ "switch (element.nodeType) {"
								+ "case Node.TEXT_NODE:"
								+ "comp.name = 'text()';" + "break;"
								+ "case Node.ATTRIBUTE_NODE:"
								+ "comp.name = '@' + element.nodeName;"
								+ "break;"
								+ "case Node.PROCESSING_INSTRUCTION_NODE:"
								+ "comp.name = 'processing-instruction()';"
								+ "break;" + "case Node.COMMENT_NODE:"
								+ "comp.name = 'comment()';" + "break;"
								+ "case Node.ELEMENT_NODE:"
								+ "comp.name = element.nodeName;" + "break;"
								+ "}" + "comp.position = getPos(element);"
								+ "}" +

								"for (var i = comps.length - 1; i >= 0; i--) {"
								+ "comp = comps[i];"
								+ "xpath += '/' + comp.name.toLowerCase();"
								+ "if (comp.position !== null) {"
								+ "xpath += '[' + comp.position + ']';" + "}"
								+ "}" +

								"return xpath;" +

								"} return absoluteXPath(arguments[0]);",
						element);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void checkPage() {
		// TODO Auto-generated method stub

	}

}
