<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<sec:authorize access="canAccessUrl('/mdm/dashboard')" var="canAccessDashboard"/>
<sec:authorize access="canAccessUrl('/mdm/actionItems')" var="canAccessActionItems"/>
<sec:authorize access="canAccessUrl('/mdm/drafts')" var="canAccessDrafts"/>

<!-- MDM: Main Navigation Bar -->
<nav class="main-nav">
	<div class="container">
		<ul>
			<c:if test="${canAccessDashboard}">
				<li id="navDashboard" class='active navButton'><a href="${contextPath}/mdm/dashboard">My Dashboard</a></li>
			</c:if>
			<c:if test="${canAccessActionItems}">
				<li id="navActionItems" class="${not canAccessDashboard and canAccessActionItems ? 'active navButton' : 'navButton'}"><a href="${contextPath}/mdm/actionItems">Action Items <sup id="actionItemCount">${mdmCount['countActionItems']}</sup></a></li>
			</c:if>
			<c:if test="${canAccessDrafts}">
				<li id="navDrafts" class="${not canAccessDashboard and not canAccessActionItems and canAccessDrafts? 'active navButton' : 'navButton'}"><a href="${contextPath}/mdm/drafts">Draft <sup id="draftItemsCount">${mdmCount['countDraftItems']}</sup></a></li>
			</c:if>
			
			<li class="last"><a href="javascript:void(0)">Master Data Management Store</a></li>
		</ul>
	</div>
</nav>
<script>
$(document).ready(function(){
	$('ul a[href="'+location.pathname.split('/')[3]+'"]').parent().addClass('active');
});
</script>
