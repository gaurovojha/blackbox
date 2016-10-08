<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="jurisdiction" value="${application.jurisdictionName}" />
<form:select id="ddApplicationType" path="applicationType" class="ddApplicationType applicationInit form-control ${editApplication ? 'updateApplicationType' : ''}">
	<form:option value="" linkFamily="false">Please Select</form:option>
	<c:forEach items="${listApplicationTypes_US}" var="appType">
		<c:if test="${(not nonFirstOnly) or (not fn:containsIgnoreCase(appType, 'FIRST_FILING'))}">
			<form:option value="${appType}" linkFamily="${not fn:containsIgnoreCase(appType, 'FIRST_FILING')}" class="appTypeUS" style="display: ${jurisdiction eq 'US' ? '' : 'none'};">
				<spring:message code="application.type.${appType}"/>
			</form:option>
		</c:if>
	</c:forEach>
	<c:forEach items="${listApplicationTypes_WO}" var="appType">
		<c:if test="${(not nonFirstOnly) or (not fn:containsIgnoreCase(appType, 'FIRST_FILING'))}">
			<form:option value="${appType}" linkFamily="${not fn:containsIgnoreCase(appType, 'FIRST_FILING')}" class="appTypeWO" style="display: ${jurisdiction eq 'WO' ? '' : 'none'};">
				<spring:message code="application.type.${appType}"/>
			</form:option>
		</c:if>
	</c:forEach>
	<c:forEach items="${listApplicationTypes_Other}" var="appType">
		<c:if test="${(not nonFirstOnly) or (not fn:containsIgnoreCase(appType, 'FIRST_FILING'))}">
			<form:option value="${appType}" linkFamily="${not fn:containsIgnoreCase(appType, 'FIRST_FILING')}" class="appTypeOther" 
				style="display: ${((not empty jurisdiction) and (jurisdiction ne 'US') and (jurisdiction ne 'WO')) ? '' : 'none'};">
				<spring:message code="application.type.${appType}"/>
			</form:option>
		</c:if>
	</c:forEach>
</form:select>
<form:errors path="applicationType" class="error" />