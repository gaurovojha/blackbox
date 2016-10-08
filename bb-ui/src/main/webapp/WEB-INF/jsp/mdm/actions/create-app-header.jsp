<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="pathImg" value="${contextPath}/assets/images" scope="request" />

<table class="table custom-table" id="createAppRecords">
	<thead>
		<tr>
			<th>Jurisdiction</th>
			<th>Application No.</th>
			<th>Sent by</th>
			<th>Notified Date</th>
			<th>Actions</th>
		</tr>
	</thead>
	<tbody id="tbodyCreateAppRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>