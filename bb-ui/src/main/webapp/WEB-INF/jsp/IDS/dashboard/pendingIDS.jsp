<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />


<div role="tabpanel" class="tab-pane" id="pendingIDStab">
	<div>
		<ul class="tab-actions pull-right">
			<li>
				<p>Legend: OA (Office Action)</p>
			</li>
		</ul>
		<ul class="nav nav-tabs custom-tabs inner" role="tablist">
			<li role="presentation"><a href="#NewReferences" role="tab"
				data-toggle="tab" id="lnkPendingIDS">IDS (<span id="idsPendingApprovalCount">${pendingApprovalCount}</span>)</a></li>
			<li role="presentation" class="active"><a href="#FromAttorney"
				role="tab" data-toggle="tab" id="lnkPendingResponse">Respond to the Attorney (<span id="idsPendingResponseCount">${pendingResponseCount}</span>)</a></li>
		</ul>
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="FromAttorney">
				<div class="tab-info-text">
					<span><i><img src="images/svg/info.svg" class="icon16x"></i></span>
					<p>Please review the request &amp; create an application
						record. You have received this request as the sender does not have
						rights to create an application record or the system is unable to
						find this application on Private PAIR. Please reject the
						notification if you do not wish to create the application record.</p>
				</div>
				<div class="tab-info-text">
					<!-- <h4>List of Records ()</h4> -->
				</div>
				<!--  JSP INclude from Attroney -->
				<jsp:include page="pending-response-header.jsp"></jsp:include>
			</div>
			<div role="tabpanel" class="tab-pane" id="NewReferences">
				<div class="tab-info-text">
					<span><i><img src="images/svg/info.svg" class="icon16x"></i></span>
					<p>Please review the request &amp; create an application
						record. You have received this request as the sender does not have
						rights to create an application record or the system is unable to
						find this application on Private PAIR. Please reject the
						notification if you do not wish to create the application record.</p>
				</div>
				<div class="tab-info-text">
					<!-- <h4>List of Records ()</h4> -->
				</div>
				<!--  JSP INclude new Refrences -->
				<jsp:include page="pending-ids-header.jsp"></jsp:include>
			</div>
		</div>
	</div>
</div>