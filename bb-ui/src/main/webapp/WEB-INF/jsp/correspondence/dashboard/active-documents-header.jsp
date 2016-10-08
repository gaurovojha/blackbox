
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<table class="table custom-table correspondenceTable"
	id="tblActiveDocuments">
	<thead>
		<tr>
			<th></th>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Mailing Date</th>
			<th>Document Description</th>
			<th>Uploaded by</th>
			<th>Actions</th>
		</tr>
	</thead>
	<tbody id="tbodyActiveDocuments">
		<jsp:text />
	</tbody>
</table>

<div id="pageInfo"><jsp:text /></div>
<div class="modal custom fade modal-wide" id="myModal4" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel"></div>
