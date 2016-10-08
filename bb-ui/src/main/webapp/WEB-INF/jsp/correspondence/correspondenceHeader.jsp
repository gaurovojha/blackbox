<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="selectedTab"
	value="${selectedTab}" />
<nav class="main-nav">
		<div class="container" id="tabs">
			<ul>
				<li><a id="dashboard" href="../correspondence/dashboard" >My Dashboard</a><p class="shortcut-pic"><i>D</i></p></li>
				<li><a id="actionItems" href="../correspondence/actionItems">Action Items<sup  id="actionItemCount">${actionItemCount}</sup></a><p class="shortcut-pic"><i>A</i></p></li>
				<li><a id="trackApplication" href="../correspondence/trackApplication">Track Application</a><p class="shortcut-pic"><i>T</i></p></li>
			</ul>
		</div>
	</nav>


<jsp:include page="../correspondence/scripts/dashboard-shortcuts.jsp" />