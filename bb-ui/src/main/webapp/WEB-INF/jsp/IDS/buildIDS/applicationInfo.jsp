<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="infopanel table-bordered">
	<div class="row">
		<div class="col-xs-12 col-sm-6 col-md-4">
			<div class="text-center ">
				<h3>INFORMATION DISCLOSURE</h3>
				<p>STATEMENT BY APPLICANT <br>( Not for submission under 37 CFR 1.99)</p>
			</div>
		</div>

		<div class="col-xs-12 col-sm-6 col-md-8">
			<div class="row info-patent">
				<form:form commandName="appForm" method="post" id="formEditApp" action="${contextPath}/ids/saveApp" class="form-horizontal">
					<form:hidden id="idsAppId" path="dbId"/>
					<form:hidden id="idsDbID" path="idsID"/>
					
					<c:set var="editView" value="${appForm.editView}" />
					
					<div class="col-sm-3">
						<label class="control-label"><spring:message code="ids.initiate.application" /></label>
						<div class="form-control-static">${appForm.applicationNo}</div>
						<form:hidden path="applicationNo" />
					</div>
					<div class="col-sm-3">
						<label class="control-label"><spring:message code="ids.initiate.filingdate" /></label>
						<div class="form-control-static" style="display: ${editView ? 'none' : 'block'};">${appForm.filingDate}</div>
						<!-- Editable Input -->
						<div class="edit-view" style="display: ${editView ? 'block' : 'none'};">
							<div class="input-group date datepicker">
								<form:input id="dpFilingDate" path="filingDate" class="form-control"/>
								<span class="input-group-addon">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<form:errors path="filingDate" class="error"/>
							</div>
						</div>
					</div>
					<div class="col-sm-3">
						<label class="control-label"><spring:message code="ids.initiate.FirstNameInventor" /></label>
						<div class="form-control-static" style="display: ${editView ? 'none' : 'block'};">${appForm.inventor}</div>
						<!-- Editable Input -->
						<div class="edit-view" style="display: ${editView ? 'block' : 'none'};">
							<form:input path="inventor" class="form-control"/>
							<form:errors path="inventor" class="error"/>
						</div>
					</div>
					<div class="col-sm-3">
						<label class="control-label"><spring:message code="ids.initiate.AttorneyDocket" /></label>
						<div class="form-control-static" style="display: ${editView ? 'none' : 'block'};">${appForm.docketNo}</div>
						<!-- Editable Input -->
						<div class="edit-view" style="display: ${editView ? 'block' : 'none'};">
							<form:input id="attorneyDocketNo" path="docketNo" class="form-control"/>
							<form:errors path="docketNo" class="error"/>
						</div>
					</div>
					<div class="clearfix"></div>
					<div class="col-sm-3">
						<label class="control-label"><spring:message code="ids.initiate.ArtUnit" /></label>
						<div class="form-control-static" style="display: ${editView ? 'none' : 'block'};">${appForm.artUnit}</div>
						<!-- Editable Input -->
						<div class="edit-view" style="display: ${editView ? 'block' : 'none'};">
							<form:input path="artUnit" class="form-control"/>
							<form:errors path="artUnit" class="error"/>
						</div>
					</div>
					<div class="col-sm-3">
						<label class="control-label"><spring:message code="ids.initiate.ExaminerName" /></label>
						<div class="form-control-static" style="display: ${editView ? 'none' : 'block'};">${appForm.examiner}</div>
						<!-- Editable Input -->
						<div class="edit-view" style="display: ${editView ? 'block' : 'none'};">
							<form:input path="examiner" class="form-control"/>
							<form:errors path="examiner" class="error"/>
						</div>
					</div>
					
					<!-- Control Buttons -->
					<a class="view-ref-btn action-danger" href="javascript:void(0)" id="btnCancelEditApp" style="display: ${editView ? 'block' : 'none'};"><i><img src="${imgPath}/svg/delete.svg" class="icon16x"></i> Cancel</a>
					<a class="view-ref-btn" href="javascript:void(0)" id="btnSaveEditApp" style="display: ${editView ? 'block' : 'none'};"><i><img src="${imgPath}/svg/save.svg" class="icon16x"></i> Save</a>
					<a class="view-ref-btn" href="javascript:void(0)" id="btnEditApp" style="display: ${editView ? 'none' : 'block'};"><i><img src="${imgPath}/svg/edit.svg" class="icon16x"></i> Edit</a>
				
				</form:form>
			</div>
		</div>
	</div>
</div>