<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="table custom-table" id="uploadReqRecords">
	<thead>
		<tr>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Mailing Date</th>
			<th>Document Description</th>
			<th>Notified</th>
			<th>Action</th>
		</tr>
	</thead>
	<tbody id="tbodyUploadReqRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>
<div class="modal custom fade" id="UploadDocumentModalId" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel"></div>
<jsp:include page="../common/confirmation-box.jsp"></jsp:include>
