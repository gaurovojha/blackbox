<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<input type="hidden" id="screen" value="${screen}-${countApplications + 1}"/>

<div class="form-group appInits">
	<div class="col-sm-4">
		<label class="control-label">Jurisdiction:</label>
		<form:input id="jurisdiction" path="jurisdictionName" class="jurisdiction applicationInit form-control" maxlength="2" />
		<form:errors path="jurisdictionName" class="error" />
	</div>
	<div class="col-sm-4">
		<label class="control-label">Application No.</label>
		<form:input id="applicationNo" path="applicationNo" class="applicationInit form-control" />
		<form:errors path="applicationNo" class="error" />
	</div>
	<div class="col-sm-4">
		<label class="control-label">Application Type:</label>
		<jsp:include page="dropdown-applicationTypes.jsp" />
	</div>
</div>

<script>
$(function(){
	$("#jurisdiction").focusout();
});
</script>