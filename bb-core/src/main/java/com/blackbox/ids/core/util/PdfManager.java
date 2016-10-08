package com.blackbox.ids.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.blackbox.ids.core.ApplicationException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.SimpleBookmark;

/**
 * Helper class to create and split pdf, read bookmarks and related information
 * 
 * @author rajatjain01
 *
 */
public final class PdfManager {

	private final static Logger log = Logger.getLogger(PdfManager.class);
	
	/**
	 * Returns list of all the bookmarks available in the pdf
	 * 
	 * @param file
	 * @return
	 */
	public static List<HashMap<String, Object>> readBookmarks(final String file) {

		PdfReader inputPDF = null;
		try {
			inputPDF = new PdfReader(file);
		} catch (IOException e) {
			log.debug("Unable to read bookmarks");
			throw new ApplicationException(1001, "Unable to read bookmarks", e);
		}
		inputPDF.consolidateNamedDestinations();
		List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(inputPDF);

		inputPDF.close();
		return bookmarks;

	}

	/**
	 * Return pages associated to given bookmark
	 * 
	 * @param bookmark
	 * @param nextBookmark
	 * @param lastPage
	 * @return
	 */
	public static Integer[] getBookmarkPageNumbers(final HashMap<String, Object> bookmark,
			final HashMap<String, Object> nextBookmark, final int lastPage) {

		String pageStrStart = bookmark.get("Page").toString();
		String[] pageStrArrStart = pageStrStart.split(" ");
		int startPageNumber = 0;

		if (pageStrArrStart.length > 0) {
			startPageNumber = Integer.valueOf(pageStrArrStart[0]);
		}

		int nextBookmarkStartPageNumber = 0;
		if (nextBookmark != null) {
			String pageStrEnd = nextBookmark.get("Page").toString();
			String[] pageStrArrEnd = pageStrEnd.split(" ");

			if (pageStrArrEnd.length > 0) {
				nextBookmarkStartPageNumber = Integer.valueOf(pageStrArrEnd[0]);
			}
		} else {
			nextBookmarkStartPageNumber = lastPage;
		}

		// number of pages associated to given bookmark is equal to difference between start page number of the next and
		// current bookmark
		Integer[] pageNumbers = new Integer[nextBookmarkStartPageNumber - startPageNumber];
		for (int i = 0; startPageNumber < nextBookmarkStartPageNumber; startPageNumber++, i++) {
			pageNumbers[i] = startPageNumber;
		}

		return pageNumbers;
	}

	/**
	 * Returns bookmark title
	 * 
	 * @param bookmark
	 * @return title
	 */
	public static String getBookmarkTitle(final HashMap<String, Object> bookmark) {
		return bookmark.get("Title").toString();
	}

	/**
	 * Split a given pdf for the given page numbers
	 * 
	 * @param pages
	 * @return
	 */
	public static void splitAndCreatePdf(final String inputFile, final String outputFile, final Integer[] pages) {

		Document document = new Document();
		PdfWriter writer = null;
		try {
			PdfReader inputPDF = new PdfReader(inputFile);

			// Create a writer for the output stream
			writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));

			document.open();
			PdfContentByte pcb = writer.getDirectContent(); // Holds the PDF data
			PdfImportedPage page;

			for (int i = 0; i < pages.length; i++) {
				document.newPage();
				page = writer.getImportedPage(inputPDF, pages[i]);
				pcb.addTemplate(page, 0, 0);
			}
			document.close();
			writer.close();
		} catch (IOException | DocumentException e) {
			throw new ApplicationException(1001, "Unable to split pdf", e);
		}

		finally {
			if (document.isOpen()) {
				document.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * Merges pdf files, Given n number of pdf file paths, and output file path
	 * 
	 * @param outputFile
	 * @param inputFile
	 *            list
	 */
	public static void mergePdf(final String outputFile, final List<String> inputFiles) throws ApplicationException {

		try {
			List<InputStream> pdfs = new ArrayList<InputStream>();
			for (String file : inputFiles) {
				pdfs.add(new FileInputStream(file));
			}
			OutputStream output = new FileOutputStream(outputFile);
			concatPDFs(pdfs, output, true);
		} catch (IOException e) {
			throw new ApplicationException(1001, "Unable to merge pdf", e);
		}
	}

	/**
	 * Add pdf files together and create a single file
	 * 
	 * @param streamOfPDFFiles
	 * @param outputStream
	 * @param paginate
	 */
	private static void concatPDFs(final List<InputStream> streamOfPDFFiles, final OutputStream outputStream,
			boolean paginate) {

		Document document = new Document();
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
			}

			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF

			// data
			PdfImportedPage page;
			int pageOffset = 0;
			int pageOfCurrentReaderPDF = 0;
			List<HashMap<String, Object>> master = new ArrayList<>();
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(pdfReader);
				if (bookmarks != null) {
					if (pageOffset != 0) {
						SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
					}
					master.addAll(bookmarks);
				}

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);
				}
				pageOfCurrentReaderPDF = 0;
			}

			// write bookmarks
			if (!master.isEmpty()) {
				writer.setOutlines(master);
			}

			// close all the opened resources
			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (IOException | DocumentException e) {
			throw new ApplicationException(1001, "Unable to read input pdf", e);
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				throw new ApplicationException(1001, "Unable to concat pdf", ioe);
			}
		}
	}

	/**
	 * Return total number of pages in a pdf file
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static int getNumberOfPages(final String file) {
		PdfReader pdfReader = null;
		try {
			pdfReader = new PdfReader(file);
			pdfReader.close();
		} catch (IOException e) {
			throw new ApplicationException(1001, "Unable to get number of pages", e);
		} finally {
			if (pdfReader != null) {
				pdfReader.close();
			}
		}

		return pdfReader.getNumberOfPages();
	}

}