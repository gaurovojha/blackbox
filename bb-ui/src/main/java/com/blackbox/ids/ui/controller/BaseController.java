/**
 *
 */
package com.blackbox.ids.ui.controller;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dto.datatable.DatatableAttribute;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.dto.reference.ReferencePredicate;
import com.blackbox.ids.ui.common.Constants;
import com.mysema.query.types.Predicate;

/**
 * The {@code BaseController} serves as a container for APIs common to all module specific controllers.
 *
 * @author ajay2258
 */
@Controller
public class BaseController {

	private final static Integer DEFAULT_OFFSET = 0;
	private final static Integer DEFAULT_PAGINATION_LIMIT = 20;

	/** @return Currently logged in user's database Id. */
	protected Long loggedInUser() {
		return  BlackboxSecurityContextHolder.getUserId();
	}

	/**
	 * Parses the jQuery datatable pagination parameters from given request instance.
	 * <p/>
	 * Please note that parameters names used herein, are very specific to jQuery datatable only. The method shouldn't
	 * be invoked for other tables.
	 *
	 * @param request Contains request information for HTTP servlets.
	 * @return {@link PaginationInfo}.
	 */
	protected PaginationInfo getPaginationInfo(final HttpServletRequest request) {

		String dispStart = request.getParameter(DatatableAttribute.LIMIT.attrName);
		String dispLength = request.getParameter(DatatableAttribute.OFFSET.attrName);
		String sortDir = request.getParameter(DatatableAttribute.SORT_ORDER.attrName);

		final Long limit = StringUtil.isBlank(dispStart) ? null : Long.parseLong(dispStart);
		final Long offset = StringUtil.isBlank(dispLength) ? null : Long.parseLong(dispLength);
		final String sortBy = request.getParameter(DatatableAttribute.SORT_BY.attrName);
		final boolean isAsc = Constants.SPECIFIER_ASCENDING.equalsIgnoreCase(sortDir);

		return new PaginationInfo(limit, offset, sortBy, isAsc);
	}

	/**
	 * Similar to above method but Creates a pageable object specific to SpringDataJPA
	 *
	 * @param request Contains request information for HTTP servlets.
	 * @return {@link Pageable}
	 */
	protected Pageable getPageInfo(final HttpServletRequest request) {

		String dispStart = request.getParameter(DatatableAttribute.LIMIT.attrName);
		String dispLength = request.getParameter(DatatableAttribute.OFFSET.attrName);
		String sortDir = request.getParameter(DatatableAttribute.SORT_ORDER.attrName);

		Integer limit = StringUtil.isBlank(dispStart) ? DEFAULT_PAGINATION_LIMIT : Integer.parseInt(dispStart);
		Integer offset = StringUtil.isBlank(dispLength) ? DEFAULT_OFFSET : Integer.parseInt(dispLength);
		final String sortBy = request.getParameter(DatatableAttribute.SORT_BY.attrName);
		final boolean isAsc = Constants.SPECIFIER_ASCENDING.equalsIgnoreCase(sortDir);

		if(offset != DEFAULT_OFFSET) {
			offset = offset / limit;
		}

		Sort sort = null;
		
		switch (sortBy==null ? "0" : sortBy) {

		case "0" :
				sort = isAsc ? new Sort(new Order(Direction.ASC, "mailingDate")) : new Sort(new Order(Direction.DESC, "mailingDate"));
				break;
		case "1":
				sort = isAsc ? new Sort(new Order(Direction.ASC, "application.jurisdiction.code")) : new Sort(new Order(Direction.DESC, "application.jurisdiction.code"));
				break;
		case "2":
				sort = isAsc ? new Sort(new Order(Direction.ASC, "application.applicationNumber")) : new Sort(new Order(Direction.DESC, "application.applicationNumber"));
				break;
		case "3":
				sort = isAsc ? new Sort(new Order(Direction.ASC, "mailingDate")) : new Sort(new Order(Direction.DESC, "mailingDate"));
				break;
		case "4":
				sort = isAsc ? new Sort(new Order(Direction.ASC, "documentCode.description")) : new Sort(new Order(Direction.DESC, "documentCode.description"));
				break;
		default:
				sort = isAsc ? new Sort(new Order(Direction.ASC, "mailingDate")) : new Sort(new Order(Direction.DESC, "mailingDate"));
		}
		
		PageRequest page = new PageRequest(offset, limit, sort);

		return page;
	}

	/**
	 * Return all the filter conditions which can be applied on a page
	 *
	 * @param request Contains request information for HTTP servlets.
	 * @return @link(Predicate)
	 */
	protected Predicate getPredicate(final HttpServletRequest request) {

		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;
		String dateRange = request.getParameter(DatatableAttribute.DATE_RANGE.attrName);
		String juris = request.getParameter(DatatableAttribute.JURISDICTION.attrName);

		Predicate predicate = ReferencePredicate.getReferencePredicate(ownedBy, juris, dateRange);

		return predicate;
	}

	/**
	 * Usage Example:
	 * <code> format("[DRAFT]: Delete request received for draft {0}.", draftId) </code>
	 */
	protected String format(final String message, final Object...params) {
		return MessageFormat.format(message, params);
	}

	public void setSelectedTab(Model model, String selectedTab) {
		model.addAttribute("selectedTab", selectedTab);
	}
}
