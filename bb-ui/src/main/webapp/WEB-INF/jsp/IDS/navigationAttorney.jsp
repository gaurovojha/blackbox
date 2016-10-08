<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

 <sec:authorize access="canAccessUrl('/ids/attorneyApproval/dashboard')">
<!-- IDS: Attronery approval Navigation Bar -->
<nav class="main-nav">
	<div class="container" id="tabs">
            <ul>
                <li id="navDashboard" class="navButton"><a href="${contextPath}/ids/attorneyApproval/dashboard">MY DASHBOARD</a></li>
				<li id="navInitiateIDS" class="navButton"><a href="${contextPath}/ids/attorneyApproval/approveIDS">APPROVE IDS</a></li>
            </ul>
    </div>
</nav>
</sec:authorize>