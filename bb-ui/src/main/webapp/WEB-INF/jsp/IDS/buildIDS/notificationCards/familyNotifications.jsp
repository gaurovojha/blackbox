<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<div class="panel panel-default">
	<div class="panel-heading" role="tab" id="Notifications">
		<h4 class="panel-title">
			<span></span> <a role="button" data-toggle="collapse"
				data-parent="#accordion" href="#collapseTwo" aria-expanded="true"
				aria-controls="collapseTwo"> Notifications related to other
				family members &nbsp;&nbsp;<i><img
					src="${pathImg}/exclamation.svg" class="icon16x"></i> <span
				class="action-faulty countFamily">${countNotification['family']}</span><span
				class="selected"></span><span class="icon icon-arrow-down"></span><span
				class="icon icon-arrow-up"></span>
			</a>
		</h4>
	</div>
<c:if test="${countNotification['family'] gt 0}">
	<div id="collapseTwo" class="panel-collapse collapse" role="tabpanel"
		aria-labelledby="foreignPatents">
		<div class="panel-body">
				<div id="myCarousel" class="carousel slide" data-interval="false">
					<div id="familyNotificationData">
						<jsp:text />
					</div>
					<a class="left carousel-control prevCarousel" role="button"
						data-slide="prev"> <span
						class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						<span class="sr-only">Previous</span>
					</a> <a class="right carousel-control nextCarousel" role="button"
						data-slide="next"> <span
						class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
						<span class="sr-only">Next</span>
					</a>
				</div>
		</div>
	</div>
</c:if>
</div>

