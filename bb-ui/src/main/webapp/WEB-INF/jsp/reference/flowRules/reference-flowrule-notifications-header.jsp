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
		<span><i><img src="<%=images%>/svg/info.svg" class="icon16x"></i></span> <p>${notificationMessage}</p> 
	</div>
</div> 

<input type="hidden" id="dashbordSubMenu" value="${dashbordSubMenu}" />

<table class="table custom-table referenceEntryTable" id="referenceEntryTable">
	<thead>
		<tr class="small-height-row">
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th colspan="3" class="text-center">Subject Matter Link to:</th>
			<th></th>
			<th></th>
		</tr>

		<tr>
			<th>Family ID</th>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Reference Document</th>
			<th class="bdr-rt-none">Family ID</th>
			<th class="bdr-rt-none">Jurisdiction</th>
			<th>Application #</th>
			<th>Notified</th>
			<th>Actions</th>
		</tr>
	</thead>
	<tbody id="referenceEntryTableBody1">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>