<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<table class="table custom-table referenceTable" id="tblPendingRecords">
    <thead>
        <tr>
            <th class="noSort"></th>
            <th>Jurisdiction</th>
            <th>Application#</th>
            <th>Mailing Date</th>
            <th>Document Description</th>
            <th class="noSort">Ocr Status</th>
            <th class="noSort">Uploaded By</th>
            <th class="noSort">Action</th>
        </tr>
    </thead>
    <tbody id="tbodyPendingRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>