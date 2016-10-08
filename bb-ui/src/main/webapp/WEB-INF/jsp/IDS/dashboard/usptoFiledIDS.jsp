<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
	
<div role="tabpanel" class="tab-pane" id="notification1449">

	<ul class="nav nav-tabs custom-tabs inner" role="tablist">
	<li role="presentation" class="active"><a id = "lnk1449" href="#1449Tab" role="tab"
			data-toggle="tab">1449</a></li>
		<li role="presentation" ><a  id = "lnkIDSFilingPackage" href="#idsTab"
			role="tab" data-toggle="tab">IDS Filing Packages</a></li>
		
	</ul>
	<div class="tab-content">
		
		<div role="tabpanel" class="tab-pane active" id="1449Tab">
			<div class="tab-info-text">
				<span><i><img src="images/svg/info.svg" class="icon16x"></i></span>
				<p>This is a system generated message. You have received this
					notification as the system is unable to find this document on
					Private PAIR. Please review the document details &amp; upload it
					manually. In case this document does not exist, please reject the
					notification.</p>
			</div>
			<div class="tab-info-text">
				<h4>List of Records (1)</h4>
			</div>
			<!-- JSP Include : 1449notification -->
			<jsp:include page="1449-notification-header.jsp"></jsp:include>
		</div>
		<div role="tabpanel" class="tab-pane " id="idsTab">
			<div class="tab-info-text">
				<span><i><img src="images/svg/info.svg" class="icon16x"></i></span>
				<p>This is a system generated message. You have received this
					notification as the system is unable to find this document on
					Private PAIR. Please review the document details &amp; upload it
					manually. In case this document does not exist, please reject the
					notification.</p>
			</div>
			<div class="tab-info-text">
				<h4>List of Records (1)</h4>
			</div>
			<!-- JSP Include : IDS -->
			<jsp:include page="filed-ids-header.jsp"></jsp:include>
			
		</div>
	</div>
</div>