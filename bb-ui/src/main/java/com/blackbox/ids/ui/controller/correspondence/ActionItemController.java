package com.blackbox.ids.ui.controller.correspondence;

import java.io.File;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.DatatableAttribute;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.services.correspondence.ICorrespondenceService;
import com.blackbox.ids.ui.common.BlackboxFileHelper;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.controller.BaseController;
import com.blackbox.ids.ui.util.NumberUtil;

/**
 * This controller handles the Action Item tab related view(s) in correspondence depending on the URI Template.
 * @author Nagarro
 */
@Controller
@RequestMapping(value = "/correspondence")
public class ActionItemController extends BaseController {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ActionItemController.class);

	/** The Constant KEY_SEARCH_RESULT. */
	private static final String KEY_SEARCH_RESULT = "searchResult";

	/** The Constant CORRESPONDENCE_ACTION_ITEMS. */
	private static final String CORRESPONDENCE_ACTION_ITEMS = "correspondence-action-items";

	/** The Constant CORRESPONDENCE_UPDATE_REQUEST. */
	private static final String CORRESPONDENCE_UPDATE_REQUEST = "correspondence-update-request";

	/** The Constant CORRESPONDENCE_UPLOAD_REQUEST. */
	private static final String CORRESPONDENCE_UPLOAD_REQUEST = "correspondence-upload-request";

	/** The Constant CORRESPONDENCE_REVIEW_DOCUMENT. */
	private static final String CORRESPONDENCE_REVIEW_DOCUMENT = "correspondence-review-document";

	/** The Constant CORRESPONDENCE_UPLOAD_DOCUMENT. */
	private static final String CORRESPONDENCE_UPLOAD_DOCUMENT = "correspondence-upload-document";

	/** The correspondence base service. */
	@Autowired
	private ICorrespondenceService correspondenceBaseService;

	/** The file helper. */
	@Autowired
	private BlackboxFileHelper fileHelper;

	/**
	 * The Enum ActionItemsView.
	 * @author Nagarro
	 */
	public enum ActionItemsView {

		/** The update request view. */
		UPDATE_REQUEST_VIEW, /** The upload request view. */
		UPLOAD_REQUEST_VIEW;

		/** The Constant MODEL_KEY. */
		public static final String MODEL_KEY = "actionItemView";
	}

	/**
	 * Handles and retrieves the action item view.
	 * @param model
	 *            the model
	 * @return the action item JSP page
	 */
	@RequestMapping(value = "/actionItems", method = RequestMethod.GET)
	public String viewActionItems(final Model model) {
		logger.info("[Action Item] - Showing Action Item Tab.");
		setSelectedTab(model, Constants.CONSTANT_CORRESPONDENCE_ACTION_ITEM_TAB);
		final long updateRequestCount = correspondenceBaseService.fetchUpdateRequestCount();
		final long uploadRequestCount = correspondenceBaseService.fetchUploadRequestCount();
		model.addAttribute(Constants.CORRESPONDENCE_UPDATE_REQUEST_COUNT, updateRequestCount);
		model.addAttribute(Constants.CORRESPONDENCE_UPLOAD_REQUEST_COUNT, uploadRequestCount);
		model.addAttribute(Constants.CORRESPONDENCE_ACTION_ITEM_COUNT, updateRequestCount + uploadRequestCount);
		model.addAttribute(ActionItemsView.MODEL_KEY, ActionItemsView.UPDATE_REQUEST_VIEW.name());
		return CORRESPONDENCE_ACTION_ITEMS;
	}

	/**
	 * Fetches the update request records under action item tab.
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the update request view
	 */
	@RequestMapping(value = "/actionItems/updateRequest", method = RequestMethod.POST)
	public String fetchUpdateRequestRecords(final HttpServletRequest request, final Model model) {
		logger.info("[Action Item --> Update Request tab] - Showing Action Item Update Request Records.");
		model.addAttribute(ActionItemsView.MODEL_KEY, ActionItemsView.UPDATE_REQUEST_VIEW.name());

		final PaginationInfo pageInfo = getPaginationInfo(request);
		final String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		final Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		final CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(ownedBy,
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName));

		final SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceBaseService
				.fetchUpdateRequestRecords(filter, pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);
		return CORRESPONDENCE_UPDATE_REQUEST;
	}

	/**
	 * Fetches the upload request records under action item tab.
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the upload request view
	 */
	@RequestMapping(value = "/actionItems/uploadRequest", method = RequestMethod.POST)
	public String fetchUploadRequestRecords(final HttpServletRequest request, final Model model) {
		logger.info("[Action Item --> Upload Request tab] - Showing Action Item Upload Request Records.");
		model.addAttribute(ActionItemsView.MODEL_KEY, ActionItemsView.UPLOAD_REQUEST_VIEW.name());

		final PaginationInfo pageInfo = getPaginationInfo(request);
		final String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		final Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		final CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(ownedBy,
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName));

		final SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceBaseService
				.fetchUploadRequestRecords(filter, pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);
		return CORRESPONDENCE_UPLOAD_REQUEST;
	}

	/**
	 * Handles and retrieves the review document page.
	 * @param correspondenceId
	 *            the correspondence id
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/reviewDocument", method = RequestMethod.GET, produces = "text/html")
	public String reviewDocument(@RequestParam("correspondenceId") final Long correspondenceId, final Model model) {
		logger.debug(
				format("[Action Item --> Update Request tab --> Review Document] - Showing Action Item Update Request Review Document {0}.",
						correspondenceId));
		final CorrespondenceRecordDTO correspondenceRecordDTO = correspondenceBaseService
				.getCorrespondenceRecordDTO(correspondenceId);
		model.addAttribute("reviewDocument", correspondenceRecordDTO);
		return CORRESPONDENCE_REVIEW_DOCUMENT;
	}

	/**
	 * Changes the status of correspondence document under review.
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @throws ApplicationException
	 *             the application exception
	 * @return the model and view of action item page
	 */
	@RequestMapping(value = "/changeReviewDocumentStatus", method = RequestMethod.GET)
	public ModelAndView changeRecordStatus(final HttpServletRequest request) throws ApplicationException {
		final String correspondenceId = request.getParameter("recordId");
		Long corrId = null;
		logger.debug(
				format("[Action Item --> Update Request tab --> Delete Document] - Showing Action Item Update Request Delete Document {0}.",
						correspondenceId));

		if (NumberUtil.isValidNumericalValue(correspondenceId, Long.class)) {
			corrId = Long.valueOf(correspondenceId);
		} else {
			throw new ApplicationException("Correspondence Id for delete document can't be null or empty.");
		}
		correspondenceBaseService.deleteCorrespondenceRecord(corrId, null);
		return new ModelAndView("redirect:/correspondence/actionItems");
	}

	/**
	 * Rejects notification of correspondence document.
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the model and view
	 */
	@RequestMapping(value = "/rejectDocument", method = RequestMethod.GET)
	public ModelAndView rejectDocumentNotification(final HttpServletRequest request) {
		final String notificationProcessId = request.getParameter("recordId");
		Long processId = null;
		logger.debug(format("[Action Item --> Upload Request tab --> Reject Document] - Reject Notification {0}.",
				notificationProcessId));
		if (NumberUtil.isValidNumericalValue(notificationProcessId, Long.class)) {
			processId = Long.valueOf(notificationProcessId);
		} else {
			throw new ApplicationException(
					"Notification Process Id for reject document can't be null,empty or non numeric value.");
		}
		correspondenceBaseService.rejectDownloadOfficeNotification(processId);
		return new ModelAndView("redirect:/correspondence/actionItems");
	}

	/**
	 * Upload document.
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/uploadDocument", method = RequestMethod.GET, produces = "text/html")
	public String uploadDocument(final HttpServletRequest request) {
		final String notificationIProcessd = request.getParameter("notificationId");
		Long processId = null;
		if (NumberUtil.isValidNumericalValue(notificationIProcessd, Long.class)) {
			processId = Long.valueOf(notificationIProcessd);
		} else {
			throw new ApplicationException(MessageFormat.format(
					"Notification Process Id {0} can't be null,empty or non numeric value.", notificationIProcessd));
		}
		/*
		 * CorrespondenceRecordDTO correspondenceRecordDTO = correspondenceBaseService
		 * .getDownloadOfficeCorrespondence(processId); model.addAttribute("correspondence", correspondenceRecordDTO);
		 */
		return CORRESPONDENCE_UPLOAD_DOCUMENT;
	}

	/**
	 * Adds the correspondence.
	 * @param correspondenceRecordDTO
	 *            the correspondence record dto
	 * @param result
	 *            the result
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the model and view
	 */
	@RequestMapping(value = "/addCorrespondence", method = RequestMethod.POST)
	public ModelAndView addCorrespondence(
			@ModelAttribute("correspondence") final CorrespondenceRecordDTO correspondenceRecordDTO,
			final BindingResult result, final MultipartHttpServletRequest request, final Model model) {
		return new ModelAndView("redirect:/correspondence/dashboard");
	}

	/**
	 * Shows the document of correspondence.
	 * @param correspondenceId
	 *            the correspondence id
	 * @param response
	 *            the response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "/reviewDoc/viewPdf/{correspondenceId}", method = RequestMethod.GET)
	public void viewPdfFile(@PathVariable("correspondenceId") final Long correspondenceId,
			final HttpServletResponse response) throws Exception {
		final String pdfFilePath = BlackboxUtils.concat(FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("base"),
				File.separator, String.valueOf(correspondenceId), File.separator, String.valueOf(correspondenceId), ".pdf");
		final File file = new File(pdfFilePath);
		fileHelper.viewFile(response, file);
	}
}