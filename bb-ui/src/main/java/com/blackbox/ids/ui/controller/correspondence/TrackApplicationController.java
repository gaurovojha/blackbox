package com.blackbox.ids.ui.controller.correspondence;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.DatatableAttribute;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.services.correspondence.ICorrespondenceService;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.controller.BaseController;

/**
 * The Class TrackApplicationController.
 * @author Nagarro
 */
@Controller
@RequestMapping(value = "/correspondence")
public class TrackApplicationController extends BaseController {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(ActionItemController.class);

	/** The Constant KEY_SEARCH_RESULT. */
	private static final String KEY_SEARCH_RESULT = "searchResult";

	/** The Constant CORRESPONDENCE_TRACK_APPLICATION. */
	private static final String CORRESPONDENCE_TRACK_APPLICATION = "correspondence-track-application";

	/** The Constant CORRESPONDENCE_MY_TRACK_APPLICATION. */
	private static final String CORRESPONDENCE_MY_TRACK_APPLICATION = "correspondence-my-track-application";

	/** The Constant CORRESPONDENCE_ALL_TRACK_APPLICATION. */
	private static final String CORRESPONDENCE_ALL_TRACK_APPLICATION = "correspondence-all-track-application";

	/** The corr service. */
	@Autowired
	private ICorrespondenceService corrService;

	/**
	 * The Enum TrackApplicationsView.
	 * @author Nagarro
	 */
	public enum TrackApplicationsView {

		/** The my request view. */
		MY_REQUEST_VIEW, /** The all request view. */
		ALL_REQUEST_VIEW;

		/** The Constant MODEL_KEY. */
		public static final String MODEL_KEY = "trackApplicationView";
	}

	/**
	 * Handles and retrieves the track application view.
	 * @param model
	 *            the model
	 * @return the track application view
	 */
	@RequestMapping(value = "/trackApplication", method = RequestMethod.GET)
	public String trackApplications(final Model model) {
		LOGGER.info("[Track Application] - Showing Track Application Tab.");
		setSelectedTab(model, Constants.CONSTANT_CORRESPONDENCE_TRACK_APPLICATION_TAB);
		model.addAttribute(Constants.CORRESPONDENCE_ACTION_ITEM_COUNT, corrService.fetchActionItemCount());
		model.addAttribute(TrackApplicationsView.MODEL_KEY, TrackApplicationsView.MY_REQUEST_VIEW.name());
		return CORRESPONDENCE_TRACK_APPLICATION;
	}

	/**
	 * Fetches the applications notifications created by logged in user.
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the my track application view
	 */
	@RequestMapping(value = "/trackApplication/myRequest", method = RequestMethod.POST)
	public String trackMyApplications(final HttpServletRequest request, final Model model) {
		LOGGER.info("[Track Application --> My Request tab] - Showing Track Application My Request Records.");
		model.addAttribute(TrackApplicationsView.MODEL_KEY, TrackApplicationsView.MY_REQUEST_VIEW.name());

		PaginationInfo pageInfo = getPaginationInfo(request);
		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(ownedBy,
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName));

		SearchResult<CorrespondenceRecordDTO> searchResult = corrService.fetchMyTrackApplications(filter, pageInfo);

		model.addAttribute(KEY_SEARCH_RESULT, searchResult);
		return CORRESPONDENCE_MY_TRACK_APPLICATION;
	}

	/**
	 * Fetches the applications notifications permitted to view to logged in user on the basis of user roles.
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/trackApplication/allRequest", method = RequestMethod.POST)
	public String trackAllApplications(final HttpServletRequest request, final Model model) {
		LOGGER.info("[Track Application --> All Request tab] - Showing Track Application All Records.");
		model.addAttribute(TrackApplicationsView.MODEL_KEY, TrackApplicationsView.ALL_REQUEST_VIEW.name());

		PaginationInfo pageInfo = getPaginationInfo(request);
		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(ownedBy,
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName));

		SearchResult<CorrespondenceRecordDTO> searchResult = corrService.fetchAllTrackApplications(filter, pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);
		return CORRESPONDENCE_ALL_TRACK_APPLICATION;
	}
}
