<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
<link rel="stylesheet" href="${contextPath}/assets/css/ids-style.css">
<form:form commandName="fileIdsForm" method="post"
	id="formFileIds"
	action="${contextPath}/ids/userAction/updateRefStatus/submit"
	class="form-horizontal">
	<div class="main-content container">
		<div class="wizard">
			<!-- Application Information -->
			<div id="idsAppInfo"><jsp:include
					page="../buildIDS/applicationInfo.jsp" /></div>

			<!-- <div id="previewPage"></div> -->
			<div id="idsAppInfo"><jsp:include
					page="../buildIDS/previewReference.jsp" /></div>
			<div class="form-horizontal form-footer mtop20">
				<div class="pull-right">
					<button id="cancelIDS" class="btn btn-cancel" type="button"
						data-dismiss="modal" onclick="location.href='ids-dashboard.html'">Cancel</button>
					<button class="btn btn-submit" type="button" id="submitFileIDS">Submit</button>
				</div>
			</div>
		</div>
	</div>
</form:form>

<jsp:include page="../scripts/build-ids.jsp" />
<jsp:include page="../buildIDS/popup-ids.jsp" />
<script>
	$(function() {
		bindViewPreviewPage();
		submitFileIDS();
	})
</script>