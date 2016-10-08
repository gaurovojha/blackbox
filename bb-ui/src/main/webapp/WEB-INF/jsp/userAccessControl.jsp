<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 
 <input type="hidden" class="isEnabled" value="${user.enabled}"/>
 <input type="hidden" class="isLocked" value="${user.locked}"/>
 <input type="hidden" class="status" value="${user.status}"/>
 <div>
 		<sec:authorize access="canAccessUrl('/user/enable/{userId}')" var="accessUserEnable"/>
		<sec:authorize access="canAccessUrl('/user/unlock/{userId}')" var="accessUserUnlock"/>
 </div>
<c:choose>
	<c:when test="${not user.status}">
		${user.enabled ? "Enabled" : "Disabled"}
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${user.enabled and (not user.locked)}">Enabled</c:when>
			<c:when test="${(not user.enabled) and (not user.locked)}">
				<c:choose>
						<c:when test="${accessUserEnable}">
							<a href="javascript:void(0);" class="enableAccess btn1">Enable Access</a>
							<span>Disabled</span>
						</c:when>
						<c:otherwise>
							<a href="javascript:void(0);" class="enableAccess btn1 hidden-control" disabled='true'>Enable Access</a>
							<span>Disabled</span>
						</c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${user.enabled and user.locked}">
				<c:choose>
					<c:when test="${accessUserUnlock}">
						<a href="javascript:void()" class="unlockAccess btn1">Unlock Access</a> <span class="icon icon-lock"></span>
					</c:when>
					<c:otherwise>
						<a href="javascript:void()" class="unlockAccess btn1 hidden-control">Unlock Access</a> <span class="icon icon-lock"></span>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${(not user.enabled) and (user.locked)}">Disabled</c:when>
		</c:choose>
	</c:otherwise>
</c:choose>