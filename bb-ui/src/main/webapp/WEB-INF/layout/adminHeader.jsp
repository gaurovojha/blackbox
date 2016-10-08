<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="main-nav">
	<div>
		<sec:authorize access="canAccessUrl('/notification/dashBoard')" var="accessNotification"/>
		<sec:authorize access="canAccessUrl('/admin/')" var="accessUserMgmt"/>
		<%-- <sec:authorize access="canAccessUrl('/product/dashBoard')" var="accessProduct"/> --%>
	</div>
	<div class="container">
		<ul id="adminMenu">
			<li>
				<c:choose>
					<c:when test="${accessUserMgmt}">
						<a href="${pageContext.request.contextPath}/admin/">
							<spring:message code="admin.page.heading.user.management" />
						</a>
					</c:when>
					<c:otherwise>
						<a href="#" class="hidden-control" >
							<spring:message code="admin.page.heading.user.management" />
						</a>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<c:choose>
					<c:when test="${accessNotification}">
						<a href="${pageContext.request.contextPath}/notification/dashBoard">
							<spring:message code="admin.page.heading.workflow.management" />
						</a>
					</c:when>
					<c:otherwise>
					<a href="#" class="hidden-control">
						<spring:message code="admin.page.heading.workflow.management" />
					</a>	
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<c:choose>
					<c:when test="${accessProduct}">
						<a href="../mdm/dashboard">
							<spring:message code="admin.page.heading.product.management" />
						</a>
					</c:when>
					<c:otherwise>
						<a href="../mdm/dashboard" class="hidden-control">
							<spring:message code="admin.page.heading.product.management" />
						</a>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<c:choose>
					<c:when test="true">
						<a href="../reference/dashboard">
							<spring:message code="admin.page.heading.product.reference" />
						</a>
					</c:when>
					<c:otherwise>
						<a href="../reference/dashboard" class="hidden-control">
							<spring:message code="admin.page.heading.product.reference" />
						</a>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<c:choose>
					<c:when test="true">
						<a href="../correspondence/dashboard">
							<spring:message code="admin.page.heading.product.correspondence" />
						</a>
					</c:when>
					<c:otherwise>
						<a href="../correspondence/dashboard" class="hidden-control">
							<spring:message code="admin.page.heading.product.correspondence" />
						</a>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<c:choose>
					<c:when test="true">
						<a href="../ids/dashboard">
							<spring:message code="admin.page.heading.product.IDS" />
						</a>
					</c:when>
					<c:otherwise>
						<a href="../ids/dashboard" class="hidden-control">
							<spring:message code="admin.page.heading.product.IDS" />
						</a>
					</c:otherwise>
				</c:choose>
			</li>
			
		</ul>
	</div>
</nav>
