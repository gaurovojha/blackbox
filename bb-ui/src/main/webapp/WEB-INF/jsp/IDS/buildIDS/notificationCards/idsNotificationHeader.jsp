<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row">
	<div class="col-sm-3">
		<label class="control-label"><spring:message
				code="ids.initiateIDS.idsNotification.jurisdiction" /></label>
		<div class="form-control-static">${appForm.jurisdiction}</div>
	</div>
	<div class="col-sm-3">
		<label class="control-label"><spring:message
				code="ids.initiateIDS.idsNotification.application" /></label>
		<div class="form-control-static">${appForm.applicationNo}</div>
	</div>
	<div class="col-sm-3">
		<label class="control-label"><spring:message
				code="ids.initiateIDS.idsNotification.familyId" /></label>
		<div class="form-control-static">
			<a href="" class="familyId" data="${appForm.familyId}" data-toggle="modal"
				data-target="#viewFamily">${appForm.familyId}</a>
		</div>
	</div>
	<div class="col-sm-3">
		<label class="control-label"><spring:message
				code="ids.initiateIDS.idsNotification.attorneyDocketNo" /></label>
		<div class="form-control-static">${appForm.docketNo}</div>
	</div>

</div>
