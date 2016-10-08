<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />

<div class="col-sm-6">
	<div class="family-view-block form-horizontal">
		<div class="family-head">
			<div class="row">
				<div class="col-sm-12 text-right">
					<a class="removeFamilyView" href="javascript:void(0)"><i><img src="${pathImg}/delete.svg" class="icon20x"></i></a>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">Jurisdiction <span
						class="required">*</span></label>
					<div class="form-control-static">${selectedFamilyDetails[0].jurisdiction}</div>
				</div>
				<div class="col-sm-3">
					<label class="control-label">Application No. <span
						class="required">*</span></label>
					<div class="form-control-static">${selectedFamilyDetails[0].applicationNo}</div>
				</div>
				<div class="col-sm-3">
					<label class="control-label">Family ID <span
						class="required">*</span></label>
					<div class="form-control-static">
						<a href="javascript:void()">${selectedFamilyDetails[0].familyId}</a>
						<input type="hidden" class="familyId" value="${selectedFamilyDetails[0].familyId}"/>
					</div>
				</div>
				<div class="col-sm-3">
					<label class="control-label">Assignee</label>
					<div class="form-control-static">${selectedFamilyDetails[0].assignee}</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<input type="checkbox" class="allFamilyCheckbox" id="allFamily1" checked="checked"><label
						class="control-label" for="allFamily1">Apply Linkage to	all family</label>
					<input type="hidden" class="dbId" value="${selectedFamilyDetails[0].dbId}"/>
				</div>
			</div>
		</div>
	</div>
</div>

