<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="mdm" uri="/WEB-INF/tld/bbxTagLib"%>

<input type="hidden" id="screen" value="${applicationForm.screen}-${countApplications + 1}"/>

<c:if test="${empty application}">
	<c:set var="application" value="${applicationForm.apps[0]}" scope="request" />
</c:if>
<c:set var="subSource" value="${application.subSource}" scope="request" />

<form:hidden path="subSource" />
<!-- Data for Auto-Completes -->
<input type="hidden" id="assigneeList" value="${listAssignee}" />
<input type="hidden" id="entityList" value="${listEntity}" />
<input type="hidden" id="customerList" value="${listCustomer}" />
<input type="hidden" id="firstFiling" value="${application.firstFiling}" />

<c:set var="jurisdiction" value="${application.jurisdictionName}" scope="request" />
<c:set var="applicationType" value="${application.applicationType}" scope="request" />

<form:hidden id="scenario" path="scenario" scope="request" />

<!-- Global Errors -->
<div class="error"><form:errors path="applicationNo" /></div>

<%-- <c:if test="${editApplication and (not application.firstFiling)}"> --%>
	<c:set var="family" value="${applicationForm}" scope="request" />
	<jsp:include page="linked-family-details.jsp" />
<%-- </c:if> --%>
					
<div class="form-group">
	<c:if test="${mdm:validCombination('CUSTOMER_NO', jurisdiction, applicationType)}">
		<div class="col-sm-6">
			<label class="control-label">Customer No. <span class="required">*</span></label>
			<c:choose>
				<c:when test="${not editApplication or mdm:isEditable('CUSTOMER_NO', subSource)}">
					<form:input id="customerNo" path="customerNo" class="customerNo form-control" />
					<form:errors path="customerNo" class="error"/>
				</c:when>
				<c:otherwise>
					<form:hidden id="customerNo" path="customerNo" />
					<div class="form-control-static">${application.customerNo}</div>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
	<div class="col-sm-6">
		<label class="control-label">Attorney Docket No.<span class="required">*</span></label>
		<c:choose>
			<c:when test="${not editApplication or mdm:isEditable('DOCKET_NO', subSource)}">
				<form:input id="attorneyDocketNo" path="attorneyDocketNo" class="form-control" />
				<form:errors path="attorneyDocketNo" class="error"/>
			</c:when>
			<c:otherwise>
				<form:hidden id="attorneyDocketNo" path="attorneyDocketNo" />
				<div class="form-control-static">${application.attorneyDocketNo}</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<div class="form-group">
	<c:if test="${mdm:validCombination('ASSIGNEE', jurisdiction, applicationType)}">
		<div class="col-sm-6">
			<label class="control-label">Assignee <span class="required">*</span></label>
			<c:choose>
				<c:when test="${not editApplication or mdm:isEditable('ASSIGNEE', subSource)}">
					<form:input id="assignee" path="assignee" class="assignee form-control" />
					<form:errors path="assignee" class="error"/>
				</c:when>
				<c:otherwise>
					<form:hidden id="assignee" path="assignee" />
					<div class="form-control-static">${application.assignee}</div>
					<form:errors path="assignee" class="error"/>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
	<c:if test="${mdm:validCombination('ENTITY', jurisdiction, applicationType)}">
		<div class="col-sm-6">
			<label class="control-label">Entity <span class="required">*</span></label>
			<c:choose>
				<c:when test="${not editApplication or mdm:isEditable('ENTITY', subSource)}">
					<form:input id="entity" path="entity" class="entity form-control" />
					<form:errors path="entity" class="error"/>
				</c:when>
				<c:otherwise>
					<form:hidden id="entity" path="entity" />
					<div class="form-control-static">${application.entity}</div>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
</div>

<div class="divider"></div>

<div class="form-group">
	<div class="col-sm-6">
		<label class="control-label">Filing Date <span class="required">*</span></label>
		<c:choose>
			<c:when test="${not editApplication or mdm:isEditable('FILING_DATE', subSource)}">
				<div class="input-group date datepicker">
					<form:input id="dpFilingDate" path="filingDate" class="form-control"/>
					<span class="input-group-addon">
						<i class="glyphicon glyphicon-calendar"></i>
					</span>
					<form:errors path="filingDate" class="error"/>
				</div>
			</c:when>
			<c:otherwise>
				<form:hidden id="dpFilingDate" path="filingDate" />
				<div class="form-control-static">${application.filingDate}</div>
			</c:otherwise>
		</c:choose>
	</div>
	<c:if test="${mdm:validCombination('CONFIRMATION_NO', jurisdiction, applicationType)}">
		<div class="col-sm-6">
			<label class="control-label">Confirmation No <span class="required">*</span></label>
			<c:choose>
				<c:when test="${not editApplication or mdm:isEditable('CONFIRMATION_NO', subSource)}">
					<form:input id="confirmationNo" path="confirmationNo" class="form-control" />
					<form:errors path="confirmationNo" class="error"/>
				</c:when>
				<c:otherwise>
					<form:hidden id="confirmationNo" path="confirmationNo" />
					<div class="form-control-static">${application.confirmationNo}</div>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
</div>

<div class="form-group">
	<div class="col-sm-6">
		<label class="control-label">Title <span class="required">*</span></label>
		<c:choose>
			<c:when test="${not editApplication or mdm:isEditable('TITLE', subSource)}">
				<form:input id="title" path="title" class="form-control" />
				<form:errors path="title" class="error"/>
			</c:when>
			<c:otherwise>
				<form:hidden id="title" path="title" />
				<div class="form-control-static">${application.title}</div>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="col-sm-6">
		<div class="control-label">&nbsp;</div>
		<form:checkbox id="exportControl" path="exportControl"/>
		<label class="control-label" for="exportControl">US Export Controlled</label>
	</div>
</div>

<div class="divider"></div>

<div class="form-group">
	<div class="col-sm-6">
		<label class="control-label">Publication Number</label>
		<form:input id="publicationNumber" path="publicationNumber" class="form-control" />
		<form:errors path="publicationNumber" class="error"/>
	</div>
	<div class="col-sm-6">
		<label class="control-label">Publication Date</label>
		<div class="input-group date datepicker">
			<form:input id="publicationDate" path="publicationDate" class="form-control"/>
			<span class="input-group-addon">
				<i class="glyphicon glyphicon-calendar"></i>
			</span>
			<form:errors path="publicationDate" class="error"/>
		</div>
	</div>
</div>
<div class="form-group">
	<c:if test="${mdm:validCombination('PATENT_NO', jurisdiction, applicationType)}">
		<div class="col-sm-6">
			<label class="control-label">Patent Number</label>
			<form:input id="patentNumber" path="patentNumber" class="form-control" />
			<form:errors path="patentNumber" class="error"/>
		</div>
	</c:if>
	<c:if test="${mdm:validCombination('ISSUE_DATE', jurisdiction, applicationType)}">
		<div class="col-sm-6">
			<label class="control-label">Issue Date</label>
			<div class="input-group date datepicker">
				<form:input id="issueDate" path="issueDate" class="form-control"/>
				<span class="input-group-addon">
					<i class="glyphicon glyphicon-calendar"></i>
				</span>
				<form:errors path="issueDate" class="error"/>
			</div>
		</div>
	</c:if>
</div>

<c:if test="${screen eq 'DETAILS' and countApplications eq 1}">
	<jsp:include page="application-controls.jsp" />
</c:if>