<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>


<div class="form-horizontal row">
		<div class="tab-info-text col-sm-7">
			 <p>List All Records</p> 
	</div>
</div> 

<input type="hidden" id="refFlowSubMenu" value="${refFlowSubMenu}" />

<table class="table custom-table referenceEntryTable" id="referenceEntryTable">
	<thead>
		<tr>
			<th>Family ID</th>
			<th class="noSort">Count of Family Members</th>
			<th>Count of Other Related Families<br/> (Subject Matter Link)</th>
			<th>Actions</th>
		</tr>
	</thead>
	<tbody id="referenceEntryTableBody1">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>