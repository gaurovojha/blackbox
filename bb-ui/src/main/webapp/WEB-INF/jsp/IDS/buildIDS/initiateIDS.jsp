<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"scope="request" />
<link rel="stylesheet" href="${contextPath}/assets/css/ids-style.css">

 <div class="main-content container">
	<div class="wizard">
		<!-- Comment -->
		<jsp:include page="buildSteps.jsp" />
		
		<!-- Application Information -->
		<div id="idsAppInfo"><jsp:include page="applicationInfo.jsp" /></div>
		
		<!--<div>Steps for form submission</div>-->
		<!--<div>Step 1 : US Patents </div>-->
		<div id="usPatent">
			<jsp:include page="usPatent.jsp"></jsp:include>
		</div>
		<div id="usPublication"></div>
		<div id="foreign"></div>
		<div id="npl"></div>
		<div id="confirmationPage"></div>
		<div id="previewPage"></div>
		<div class="form-footer">
			<button class="btn btn-cancel" id="back" type="button">Back</button>
			<button class="btn btn-submit" id="continue" type="button">Continue</button>
			<button class="btn btn-submit" id="submit" type="button" data-target="#refCountPopUP" >Preview</button>
			<div class="pull-right">
				<button class="btn btn-cancel red" type="button" data-toggle="modal" data-target="#popup_idsCertificationStatement">Certification Statement</button>
				<button id="btnSaveIDS" class="btn btn-cancel" type="button">Save for later</button>
				<button class="btn btn-cancel red btnDiscardIDS" type="button" data-dismiss="modal">Discard IDS</button>
			</div>
		</div>
	</div>
</div>
<form method="post" action="/bb-ui/reference/dashboard/createSelfCitedReference" target="_blank" id="selfCitationForm">
	<input type="hidden" name="applicationNumber" value="${appForm.applicationNo}">
	<input type="hidden" name="jurisdictionCode" value="US"> 
</form>
<jsp:include page="dropReferencePopUp.jsp"></jsp:include>
<jsp:include page="popup-ids.jsp" />
<jsp:include page="../scripts/build-ids.jsp" />
