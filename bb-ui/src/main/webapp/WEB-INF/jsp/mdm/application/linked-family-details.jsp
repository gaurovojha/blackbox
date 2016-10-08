<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div id="divLinkedFamily" class="edit-box" style="display: ${showFamilyLinkageDetails ? 'block' : 'none'};">
	
	<c:choose>
		<c:when test="${editApplication}">
			<!-- Must have privileges to edit family. -->
			<sec:authorize access="canAccessUrl('/mdm/editFamily/{familyId}')">
				<a href="javascript:void(0)" class="pull-right" id="familySearchForm" 
					data-toggle="modal" data-target="#popupEditFamily">
					<i><img src="${imgPath}/svg/edit.svg" class="icon16x"></i> Edit</a>
			</sec:authorize>
		</c:when>
		<c:otherwise>
			<a href="javascript:void(0)" class="pull-right" id="showFamilyLinkage">
				<i><img src="${imgPath}/svg/edit.svg" class="icon16x"></i> Edit</a>
		</c:otherwise>
	</c:choose>
		
	<h4 class="heading4">
		Linked to Family <span class="asterisk">*</span>
	</h4>
	<div class="form-group">
		<div class="col-sm-3">
			<label class="control-label">Family ID</label>
			<div class="form-control-static">
				<a href="" class="familyId tdFamilyId" id="_familyId" data="" 
					data-toggle="modal" data-target="#viewFamily">${family.parentFamily}</a>
			</div>
		</div>
		<div class="col-sm-3">
			<label class="control-label">Jurisdiction</label>
			<div class="form-control-static" id="_jurisdiction">${family.jurisdiction}</div>
		</div>
		<div class="col-sm-3">
			<label class="control-label">Application No.</label>
			<div class="form-control-static" id="_applicationNo">${family.applicationNo}</div>
		</div>
		<div class="col-sm-3">
			<label class="control-label">Attorney Docket #</label>
			<div class="form-control-static" id="_docketNo">${family.docketNo}</div>
		</div>
	</div>
</div>