package com.blackbox.ids.services.ids.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dto.IDS.dashboard.InitiateIDSRecordDTO;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.services.ids.IDSDashboardService;
import com.blackbox.ids.services.ids.IDSExcelService;

@Service("idsExcelService")
public class IDSExcelServiceImpl implements IDSExcelService {

	/** The logger. */
	private static Logger logger = Logger.getLogger(IDSExcelService.class);
	
	private static final List<String>	HEADERS			= Arrays.asList("Family ID", "Jurisdiction", "Application",
			"Last IDS Filing Date", "Uncited References", "Prosecution Status", "Time Since last OA Response filed");

	private static final String			REPORT			= "UrgentRecordReport";
	private static final String			URGENT_IDS		= "UrgentIDS_";
	private static final String			XLS_FILE_EXT	= ".xlsx";
	private static final String			TITLE			= "title";
	private static final String			HEADER			= "header";
	private static final String			DATA			= "data";
	private static final String			URGENT_RECORDS	= "UrgentRecords";
	private static final String			SHEET_TITLE		= "Urgent IDS Records";

	@Value("${root.folder.dir}")
	private String						rootDir;

	@Autowired
	private IDSDashboardService			idsDashboardService;

	@Autowired
	private EmailService				emailService;
	
	@Autowired
	private UserRepository 				userRepository;


	/**
	 * This method will generate the Urgent IDS records report and 
	 * sent the report to the IP Admin.
	 */
	@Override
	public void generateUrgentIDSReport() throws ApplicationException{
		logger.info("Generating the Urgent IDS Records Reports");
		List<InitiateIDSRecordDTO> urgentIDSList = getUrgentIDSRecords();
		if (CollectionUtils.isNotEmpty(urgentIDSList)) {
			String reportBasePath = getBasePath();
			File directory;
			String reportName = "";
			try {
				directory = new File(reportBasePath);
				cleanDirectory(directory);
				reportName = generateReport(reportBasePath, urgentIDSList);
				if (!reportName.isEmpty()) {
					sendUrgentIDSMail(reportName);
				}
			} catch (IOException e) {
				String error = MessageFormat.format("Some exception occured while generating the excel report for report base path {0} ", reportBasePath);
				logger.info(error);
				throw new ApplicationException(error);
			}

		}
	}

	
	/**
	 * It will clean the existing IDS Report Directory.
	 * @param directory the directory where IDS report to be saved
	 */
	private void cleanDirectory(final File directory) throws IOException {
		if (directory.exists())
			FileUtils.deleteDirectory(directory);
	}

	
	/**
	 * This method will mail the urgent IDS Records Report to the IP Admin.
	 * @param reportName the name of the report file to be send
	 */
	private void sendUrgentIDSMail(final String reportName) {
		String userEmailId = userRepository.getEmailId(User.SYSTEM_ID);		
		String userName = userRepository.getUserFullName(userEmailId);
		List<String> recipient = new ArrayList<String>();
		recipient.add(userEmailId);
		List<MessageData> messageDatas = createMessageData(reportName, userName);
		createAndSaveMessage(TemplateType.URGENT_IDS_REPORT, recipient, messageDatas);
	}


	/**
	 * This method will create the message data for Mail.
	 * @param reportName the name of the report file to be send
	 * @param userName user name of the recipient
 	 *
	 */
	private List<MessageData> createMessageData(final String reportName,final String userName) {
		List<MessageData> messageDatas = new ArrayList<>();
		MessageData reportMessageData = null;
		reportMessageData = new MessageData(com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_URGENT_IDS_REPORT_NAME,
				reportName);
		messageDatas.add(reportMessageData);
		
		MessageData receiverName = new MessageData(com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_RECEIVER_NAME, userName);
		messageDatas.add(receiverName);
		return messageDatas;
	}

	/**
	 * This method will create the message and sent the mail to IPAdmin.
	 * @param urgentIdsReport the Template to be selected for Urgent IDS Mail
	 * @param recipients  the list of recipients of the mail
	 * @param messageDatas the list of Message Data(s) for Report
	 */
	private void createAndSaveMessage(final TemplateType urgentIdsReport, final List<String> recipients,
			final List<MessageData> messageDatas) {
		Message message = null;
		if (urgentIdsReport != null && !CollectionUtils.isEmpty(recipients)) {
			message = new Message();
			message.setTemplateType(urgentIdsReport);
			StringBuffer strBuffer = new StringBuffer();
			for (String recipient : recipients) {
				strBuffer.append(recipient).append(Constant.SEMI_COLON);
			}
			message.setReceiverList(strBuffer.toString());
			message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);
			emailService.send(message, messageDatas);
		}
	}

	/**
	 * This method will generate the Excel file Report of Urgent IDS Records.
	 * @param reportBasePath the base path of the directory where report to be saved
	 * @param urgentIDSRecordsList  the list of Urgent IDS Recods
	 * @throws IOException 
	 */
	private String generateReport(final String reportBasePath,final List<InitiateIDSRecordDTO> urgentIDSRecordsList) throws IOException {
		
		String reportQualifiedName = BlackboxUtils.concat(reportBasePath, File.separator, REPORT, File.separator,
				URGENT_IDS, BlackboxDateUtil.getCurrentDate(TimestampFormat.YYYYMMDDHHMM), XLS_FILE_EXT);
		Workbook workbook = createExcelFile(urgentIDSRecordsList);
		File file = new File(reportQualifiedName);
			file.getParentFile().mkdirs();
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		return file.getName();
	}

	/**
	 * This method will fetch the Urgent IDS Records from Database and save them in 
	 * list of InitiateIDSRecordDTO
	 */
	private List<InitiateIDSRecordDTO> getUrgentIDSRecords() {
		List<InitiateIDSRecordDTO> idsRecordDTOs = idsDashboardService.getUrgentIDSRecords();
		return idsRecordDTOs;
	}

	/**
	 * This method will fetch the base path of the directory 
	 * where created excel report is saved
	 */
	private String getBasePath() {
		return FolderRelativePathEnum.IDS.getAbsolutePath();
	}

	/**
	 * This method will create the Excel Report of Urgent IDS Records
	 * @param idsRecordDTOs the list of Urgent IDS Records for which report to be created
	 * 
	 */
	private Workbook createExcelFile(final List<InitiateIDSRecordDTO> idsRecordDTOs) {
		
		Workbook wb = new XSSFWorkbook();
		Map<String, CellStyle> styles = createStyles(wb);		
		Sheet sheet = wb.createSheet(URGENT_RECORDS);

		printSetOFExcelFile(sheet);
		// create title row
		createTitleRow(styles, sheet);
		// create header row
		createHeader(styles, sheet);
		// create data rows
		int rownum = 2;
		rownum = createDataRows(idsRecordDTOs, styles, sheet, rownum);
		// Auto size the column widths
		for (int columnPosition = 0; columnPosition < HEADERS.size(); columnPosition++) {
			sheet.autoSizeColumn((short) (columnPosition));
		}
		return wb;
	}

	/**
	 * This method will create the Header of the Excel File
	 * @param styles the map containing different styles for different parts of Excel file
	 * @param sheet the excel sheet for which Header is created
	 * 
	 */
	private void createHeader(final Map<String, CellStyle> styles,final Sheet sheet) {
		Row headerRow = sheet.createRow(1);
		headerRow.setHeightInPoints(40);
		Cell headerCell;
		for (int cellNum = 0; cellNum < HEADERS.size(); cellNum++) {
			headerCell = headerRow.createCell(cellNum);
			headerCell.setCellValue(HEADERS.get(cellNum));
			headerCell.setCellStyle(styles.get(HEADER));
		}
	}
	
	/**
	 * This method will create the Title Row of the Excel File
	 * @param styles the map containing different styles for different parts of Excel file
	 * @param sheet the excel sheet for which Header is created
	 * 
	 */
	private void createTitleRow(final Map<String, CellStyle> styles,final Sheet sheet) {
		Row titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(45);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(SHEET_TITLE);
		titleCell.setCellStyle(styles.get(TITLE));
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$G$1"));
	}
	
	/**
	 * This method will create the Data Row of the Excel File
	 * @param idsRecordDTOs the actual data list 
	 * @param styles the map containing different styles for different parts of Excel file
	 * @param sheet the excel sheet for which Header is created
	 * @param rownum current row number of the excel sheet
	 * 
	 */
	private int createDataRows(final List<InitiateIDSRecordDTO> idsRecordDTOs,final Map<String, CellStyle> styles,final Sheet sheet,
			 int rownum) {
		for (InitiateIDSRecordDTO recordDTO : idsRecordDTOs) {
			Row dataRow = sheet.createRow(rownum++);
			setCellValues(recordDTO, dataRow, styles.get(DATA));
		}
		return rownum;
	}

	/**
	 * This method will create the Cell data for Data Row of the Excel File
	 * @param recordDTO the record data to be inserted in file 
	 * @param row the current row in which data is getting inserted
	 * @param style the style applied on the sheet
	 * 
	 */
	private void setCellValues(final InitiateIDSRecordDTO recordDTO,final Row row,final CellStyle style) {

		int cellnum = 0;
		createFormattedCell(row, cellnum++, recordDTO.getFamilyId(), style);
		createFormattedCell(row, cellnum++, recordDTO.getJurisdiction(), style);
		createFormattedCell(row, cellnum++, recordDTO.getApplicationNo(), style);
		if (recordDTO.getFilingDate() == null) {
			createFormattedCell(row, cellnum++, "-", style);
		} else {
			createFormattedCell(row, cellnum++,
					BlackboxDateUtil.dateToStr(recordDTO.getFilingDate(), TimestampFormat.MMMDDYYYY), style);
		}
		if (recordDTO.getUncitedReferencesAge() < 0) {
			createFormattedCell(row, cellnum++, String.valueOf(recordDTO.getUncitedReferences()), style);
		} else {
			createAgeFormattedCell(row, cellnum++, String.valueOf(recordDTO.getUncitedReferences()));
		}
		createFormattedCell(row, cellnum++, recordDTO.getProsecutionStatus(), style);
		if (recordDTO.getLastOAResponse() == null) {
			createFormattedCell(row, cellnum++, "-", style);
		} else {
			createFormattedCell(row, cellnum++,
					BlackboxUtils.concat(String.valueOf(recordDTO.getLastOAResponse()), Constant.SPACE_STRING, "days"),
					style);
		}

	}

	/**
	 * This method will create the formatted cell data for the row of Excel file
	 * @param row the current row in which data is getting inserted
	 * @param cellNum the cell number where data to be set
	 * @param data the data to be inserted in the cell
	 * @param style the style(formatting) to applied to the cell 
	 * 
	 */
	private void createFormattedCell(final Row row, final int cellnum, final String data, final CellStyle style) {
		Cell cell = row.createCell(cellnum);
		cell.setCellStyle(style);
		cell.setCellValue(data);
	}
	
	/**
	 * This method will create the specific cell data for the aging of the references
	 * @param row the current row in which data is getting inserted
	 * @param cellNum the cell number where data to be set
	 * @param data the data to be inserted in the cell
	 * 
	 */
	private void createAgeFormattedCell(final Row row,final int cellNum,final String data) {
		Cell cell = row.createCell(cellNum);
		CellStyle style;
		Font dataFont = row.getSheet().getWorkbook().createFont();
		dataFont.setFontHeightInPoints((short) 11);
		dataFont.setColor(IndexedColors.RED.getIndex());
		style = row.getSheet().getWorkbook().createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		style.setFont(dataFont);
		borderStyle(style);
		cell.setCellStyle(style);
		cell.setCellValue(data);
	}

	/**
	 * This method will create the different styles for different parts of the Excel File.
	 * @param wb the workbook for which styles to be created
	 * 
	 */
	private Map<String, CellStyle> createStyles(final Workbook wb) {

		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		titleCellStyle(wb, styles);
		headerCellStyle(wb, styles);
		dataCellStyle(wb, styles);
		return styles;
	}

	/**
	 * This method will create the style for title of Excel File.
	 * @param wb the workbook for which styles to be created
	 * @param styles the map containing the key as title and value as its style
	 * 
	 */
	private void titleCellStyle(final Workbook wb,final  Map<String, CellStyle> styles) {
		CellStyle style;
		Font titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 18);
		titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(titleFont);
		borderStyle(style);
		styles.put(TITLE, style);
	}
	
	/**
	 * This method will create the style for header of Excel File.
	 * @param wb the workbook for which styles to be created
	 * @param styles the map containing the key as header and value as its style
	 * 
	 */
	private void headerCellStyle(final Workbook wb,final Map<String, CellStyle> styles) {
		CellStyle style;
		Font header = wb.createFont();
		header.setFontHeightInPoints((short) 11);
		header.setColor(IndexedColors.WHITE.getIndex());
		header.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(header);
		borderStyle(style);
		style.setWrapText(true);
		styles.put(HEADER, style);
	}

	/**
	 * This method will create the style for data rows of Excel File.
	 * @param wb the workbook for which styles to be created
	 * @param styles the map containing the key as data and value as its style
	 * 
	 */
	private Map<String, CellStyle> dataCellStyle(final Workbook wb,final Map<String, CellStyle> styles) {
		CellStyle style;
		Font dataFont = wb.createFont();
		dataFont.setFontHeightInPoints((short) 11);
		dataFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		style.setFont(dataFont);
		borderStyle(style);
		styles.put(DATA, style);
		return styles;
	}

	/**
	 * This method will create the style for borders of the cell data.
	 * @param style the border style
	 * 
	 */
	private void borderStyle(final CellStyle style) {
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	}

	/**
	 * This method set the print setup of the Excel File.
	 * @param sheet the sheet for which print setup to be set
	 * 
	 */
	private void printSetOFExcelFile(final Sheet sheet) {
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
	}
	
	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}
}
