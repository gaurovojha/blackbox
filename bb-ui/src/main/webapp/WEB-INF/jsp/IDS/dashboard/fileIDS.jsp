<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
	
<div role="tabpanel" class="tab-pane" id="fileIDStab">
	<div class="tab-info-text">
		<span><i><img src="images/svg/info.svg" class="icon16x"></i></span>
		<p>Please review the request &amp; create an application record.
			You have received this request as the sender does not have rights to
			create an application record or the system is unable to find this
			application on Private PAIR. Please reject the notification if you do
			not wish to create the application record.</p>
	</div>
	<div class="tab-info-text">
		<h4 class="pull-left">List of Records (3)</h4>
		<span class="pull-right legend-txt">Legend: OA (Office Action)</span>
	</div>
	<!-- JSP Include Ready for filing -->
	<jsp:include page="filing-ready-header.jsp"></jsp:include>
</div>