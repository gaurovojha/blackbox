package com.blackbox.ids.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.pdfbox.util.PDFMergerUtility;

/**
 * This class is responsible for handling pdf functionality.
 * 
 * @author garvitgupta
 * 
 */
public class PDFUtil {

	/** The log. */
	private static final Logger logger = Logger.getLogger(PDFUtil.class);

	/** The Constant DATE_FORMAT_1. */
	public static final String DATE_FORMAT_XML = "yyyy-MM-dd";

	/** The Constant DATE_FORMAT_1. */
	public static final String DATE_FORMAT_PDF = "MM-dd-yyyy";

	/** The Constant AUTOMATIC. */
	public static final String AUTOMATIC = "Automatic";

	/**
	 * These enum is used to describe type of book mark. B1 is used to describe book mark that start with mail number.
	 * B2 is used to describe book mark that start with Date.
	 * 
	 * @author garvitgupta
	 * 
	 */
	public static enum BookMarkType {

		/** The B1. */
		B1,
		/** The B2. */
		B2;
	}

	/**
	 * This method is used to extract book mark from the pdf file.
	 *
	 * @param pdffile
	 *            the pdffile
	 * @return pdfBookmarks
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static LinkedList<String> getBookmarksNames(String pdffile) throws IOException {

		LinkedList<String> pdfBookmarks = new LinkedList<>();
		PDDocument document = null;
		FileInputStream file = null;
		try {
			file = new FileInputStream(pdffile);
			PDFParser parser = new PDFParser(file);
			parser.parse();
			document = parser.getPDDocument();
			if (document.isEncrypted()) {
				try {
					document.decrypt("");
				} catch (CryptographyException e) {
					e.printStackTrace();
				}
			}
			PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
			if (outline != null) {

				getBookmark(outline, "", document, pdfBookmarks);
			} 
		} finally {
			if (file != null) {
				file.close();
			}
			if (document != null) {
				document.close();
			}
		}
		logger.info("returning list of bookmark names");
		return pdfBookmarks;
	}

	/**
	 * This method is used to extract the book mark page number from where it starts.
	 *
	 * @param pdffile
	 *            the pdffile
	 * @return pdfPageNumber
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws CryptographyException
	 *             the cryptography exception
	 */
	public static LinkedList<Integer> getBookMarkPageNumber(String pdffile) throws IOException, CryptographyException {

		LinkedList<Integer> pdfPageNumber = new LinkedList<>();
		PDDocument document = null;
		FileInputStream file = null;

		try {
			file = new FileInputStream(pdffile);
			PDFParser parser = new PDFParser(file);
			parser.parse();
			document = parser.getPDDocument();
			if (document.isEncrypted()) {
				document.decrypt("");
			}
			PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
			if (outline != null) {

				getPageNumber(outline, "", document, pdfPageNumber);
			} else {
				logger.info("This document does not contain any  bookmarks");
			}
		} finally {
			if (file != null) {
				file.close();
			}
			if (document != null) {
				document.close();
			}
		}
		
		return pdfPageNumber;
	}

	/**
	 * This method is used by getBookmarksFromPdf() to create list of book mark.
	 *
	 * @param bookmark
	 *            the bookmark
	 * @param indentation
	 *            the indentation
	 * @param document
	 *            the document
	 * @param pdfBookmarks
	 *            the pdf bookmarks
	 * @return the bookmark
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void getBookmark(PDOutlineNode bookmark, String indentation, PDDocument document,
			LinkedList<String> pdfBookmarks) throws IOException {
		PDOutlineItem current = bookmark.getFirstChild();

		while (current != null) {

			pdfBookmarks.add(indentation + current.getTitle());
			getBookmark(current, indentation + "    ", document, pdfBookmarks);
			current = current.getNextSibling();
		}
	}

	/**
	 * This method is used by getPageNumber() to create list of pageNumber for each book mark.
	 *
	 * @param bookmark
	 *            the bookmark
	 * @param indentation
	 *            the indentation
	 * @param document
	 *            the document
	 * @param pdfPageNumber
	 *            the pdf page number
	 * @return the page number
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void getPageNumber(PDOutlineNode bookmark, String indentation, PDDocument document,
			LinkedList<Integer> pdfPageNumber) throws IOException {

		PDOutlineItem current = bookmark.getFirstChild();

		while (current != null) {

			COSObject targetPageRef = null;
			PDDestination dest = current.getDestination();
			if (dest != null)
				targetPageRef = (COSObject) ((COSArray) dest.getCOSObject()).get(0);

			String objStr = String.valueOf(targetPageRef.getObjectNumber().intValue());
			String genStr = String.valueOf(targetPageRef.getGenerationNumber().intValue());
			Integer pageNumber = (Integer) document.getPageMap().get(objStr + "," + genStr);
			pdfPageNumber.add(pageNumber);

			getPageNumber(current, indentation + "    ", document, pdfPageNumber);
			current = current.getNextSibling();
		}
		logger.debug("page numbers filled in list");
	}

	/**
	 * This method identifies whether the pdf contains more than one book mark. if book mark is more than one it return
	 * true else it return false.
	 *
	 * @param pdffile
	 *            the pdffile
	 * @return boolean
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws CryptographyException
	 *             the cryptography exception
	 */
	public static boolean isMultipleBookmark(String pdffile) throws IOException, CryptographyException {

		return (getBookmarksNames(pdffile).size() > 1) ? true : false;
	}

	/**
	 * This method validates whether the file contains all book mark of parameter bookmarkType. if it contain different
	 * type of book mark than it return false else it return true.
	 *
	 * @param file
	 *            the file
	 * @param bookmarkType
	 *            the bookmark type
	 * @return boolean
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws CryptographyException
	 *             the cryptography exception
	 */
	public static boolean isValidPdfForBookMark(String file, BookMarkType bookmarkType) throws IOException {

		List<String> bookMark = getBookmarksNames(file);

		for (String str : bookMark) {

			if (getBookMarkType(str) != bookmarkType)
				return false;

		}
		return true;
	}

	/**
	 * This method return type of bookmark.B1 is used to describe book mark that start with mail number. B2 is used to
	 * describe book mark that start with Date.
	 *
	 * @param str
	 *            the str
	 * @return bookmarktype
	 */
	public static BookMarkType getBookMarkType(String str) {

		String temp[] = str.split("\\s+");
		String patternB1="[0-9]+\\s+[A-Za-z0-9\\/]+\\s+[0-9]{2}[-]{1}[0-9]{2}[-]{1}[0-9]{4}\\s+[0-9a-zA-Z-\\/\\:\\(\\)\\s]+";
		String patternB2 ="[0-9]{4}[-]{1}[0-9]{2}[-]{1}[0-9]{2}\\s+[0-9a-zA-Z-\\/\\:\\(\\)\\s]+"; 
		if(str.matches(patternB1))
		{
			return BookMarkType.B1;
		}
		else if(str.matches(patternB2))
		{
			return BookMarkType.B2;
		}
		return null;
		
	}

	/**
	 * This method return mailing number of the book mark.
	 *
	 * @param str
	 *            the str
	 * @return string
	 */
	public static String getBookmarkCustomerNumber(String str) {

		try {
			return str.split("\\s+")[0];
		} catch (ArrayIndexOutOfBoundsException aiexp) {
			return null;
		} catch (Exception exp) {
			return null;
		}
	}

	/**
	 * This method return application number of the book mark.
	 *
	 * @param str
	 *            the str
	 * @return string
	 */
	public static String getBookmarkApplicationNumber(String str) {

		try {
			return str.split("\\s+")[1];
		} catch (ArrayIndexOutOfBoundsException aiexp) {
			return null;
		} catch (Exception exp) {
			return null;
		}
	}

	/**
	 * This method return date of the book mark.
	 *
	 * @param str
	 *            the str
	 * @return string
	 */
	public static String getBookmarkDate(String str) {

		try {
			return str.split("\\s+")[2];
		} catch (ArrayIndexOutOfBoundsException aiexp) {
			return null;
		} catch (Exception exp) {
			return null;
		}
	}

	/**
	 * This method return date of the book mark of type B2.
	 *
	 * @param str
	 *            the str
	 * @return string
	 */
	public static String getBookmarkB2Date(String str) {

		try {
			return str.split("\\s+")[0];
		} catch (ArrayIndexOutOfBoundsException aiexp) {
			return null;
		} catch (Exception exp) {
			return null;
		}
	}
	
	/**
	 * This method return description of the book mark.
	 *
	 * @param str
	 *            the str
	 * @return the bookmark description
	 */
	public static String getBookmarkDescription(String str) {

		try {
			String bookMarkArray[] = str.split("\\s+");
			String description = "";

			for (int i = 3; i < bookMarkArray.length; i++)
				description += bookMarkArray[i] + " ";

			return description;
		} catch (ArrayIndexOutOfBoundsException aiexp) {
			return null;
		} catch (Exception exp) {
			return null;
		}
	}
	
	/**
	 * This method return description of the book mark for bookmark type B2.
	 *
	 * @param str
	 *            the str
	 * @return the bookmark description
	 */
	public static String getBookmarkB2Description(String str) {

		try {
			String bookMarkArray[] = str.split("\\s+");
			String description = "";

			for (int i = 1; i < bookMarkArray.length; i++)
				description += bookMarkArray[i] + " ";

			return description;
		} catch (ArrayIndexOutOfBoundsException aiexp) {
			return null;
		} catch (Exception exp) {
			return null;
		}
	}

	/**
	 * Gets the PDF page count.
	 *
	 * @param pdffilePath
	 *            the pdffile path
	 * @return the PDF page count
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static int getPDFPageCount(String pdffilePath) throws IOException {

		PDDocument doc = PDDocument.load(new File(pdffilePath));

		int count = doc.getNumberOfPages();
		return count;
	}

	/**
	 * Gets the PDF size.
	 *
	 * @param pdffilePath
	 *            the pdffile path
	 * @return the PDF page count
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Long getPDFsize(String pdfFilePath) throws IOException {

		File file = new File(pdfFilePath);
		return file.length();
	}

	/**
	 * It merges all the file from flist into a destinationFileName
	 * 
	 * @param fList
	 * @param destinationFileName
	 * @return
	 * @throws IOException
	 * @throws COSVisitorException
	 */
	public static String mergerPDF(List<String> fList, String destinationFileName)
			throws IOException, COSVisitorException {
		PDFMergerUtility ut = new PDFMergerUtility();

		for (String filePath : fList) {
			ut.addSource(filePath);
		}
		ut.setDestinationFileName(destinationFileName);
		ut.mergeDocuments();
		return destinationFileName;
	}

	/**
	 * It changes the existing file name to new file name.
	 * 
	 * @param parentFilePath
	 * @param newFileName
	 * @throws IOException
	 */
	public static void changeFileName(String parentFilePath, String newFileName) throws IOException {

		File oldFile = new File(parentFilePath);
		File newFile = new File(oldFile.getParent(), newFileName);
		Files.move(oldFile.toPath(), newFile.toPath());

	}

}
