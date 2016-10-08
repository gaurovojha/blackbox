<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="form-group">
	<div class="col-sm-12">
		<h4 class="heading4"> Family Linkage <span class="asterisk">*</span></h4>
	</div>
</div>

<!-- Family linkage type selector -->
<div class="form-group">
	<div class="col-sm-6">
		<form:select path="linkage" id="ddFamilySearch" class="form-control">
			<c:forEach items="${familyLinkageTypes}" var="linker">
				<form:option value="${linker}"><spring:message code="family.linkage.${linker}" /></form:option>
			</c:forEach>
		</form:select>
	</div>
</div>

<!-- Search by 'Application #' -->
<div class="form-group grpSearchAttr" id="searchAttr_APPLICATION_NUMBER">
	<div class="col-sm-3">
		<label class="control-label">Jurisdiction</label>
		<form:input id="familySearch_jurisdiction" path="jurisdiction" class="form-control" maxlength="2" />
	</div>
	<div class="col-sm-3">
		<label class="control-label">Application #</label>
		<form:input id="familySearch_applicationNo" path="applicationNo" class="form-control" />
	</div>
</div>

<!-- Search by 'Attroney Docket #' -->
<div class="form-group grpSearchAttr" id="searchAttr_ATTORNEY_DOCKET_NUMBER" style="display: none;">
	<div class="col-sm-6">
		<label class="control-label">Attorney Docket#</label>
		<form:input id="familySearch_docketNo" path="docketNo" class="form-control" />
	</div>
</div>

<!-- Search by 'Family Id' -->
<div class="form-group grpSearchAttr" id="searchAttr_FAMILY_ID" style="display: none;">
	<div class="col-sm-6">
		<label class="control-label">Family Id</label>
		<form:input id="familySearch_familyId" path="familyId" class="form-control" />
	</div>
</div>

<div class="divider"><jsp:text/></div>

<div class="form-group">
	<div class="col-sm-12">
		<button type="button" class="btn btn-submit" id="btnSearchFamilies">Search</button>
	</div>
</div>
