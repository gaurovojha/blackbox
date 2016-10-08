<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table id="tblAllRecords" class="table custom-table referenceTable">
    <thead>
        <tr>
            <th class="noSort"></th>
            <th>Jurisdiction</th>
            <th>Application #</th>
            <th>Mailing Date</th>
            <th>Document Description</th>
            <th class="noSort">Reference Count</th>
            <th class="noSort">Uploaded by</th>
            <th class="noSort">Action</th>
        </tr>
    </thead>
    <tbody id="tbodyAllRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>