<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="divDrafts" class="main-content container">
	<div class="page-header">
			<h2 class="page-heading">List of Drafts</h2>
		</div>

	<table id="tblDrafts" class="table custom-table">
		<thead>
			<tr>
				<th>Jurisdiction</th>
				<th>Application No.</th>
				<th>Date</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tbody id="tbodyDrafts">
			<jsp:include page="draft-records.jsp" />
		</tbody>
	</table>
	<jsp:include page="../common/confirmation-box.jsp"/>
	<jsp:include page="../scripts/draft.jsp"/>
</div>