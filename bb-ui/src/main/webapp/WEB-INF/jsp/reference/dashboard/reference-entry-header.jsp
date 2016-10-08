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
	<div class="list-doc-control  col-sm-5">
		<span class="title">Jurisdiction 
			<div class="form-inline inline-block">
				<select class="form-control" id="jurisdictionFilter">
                    <option selected value="all">All</option>
                    <option value="US">US</option>
                    <option value="Non-US">Non-US</option>
                </select>	
			</div>
		</span> 
		<input type="checkbox" id="showMyRecordsFilter"><label class="control-label" for="uploadedDocs">Show My Notifications Only</label>
	</div>
</div> 

<input type="hidden" id="dashbordSubMenu" value="${dashbordSubMenu}" />

<table class="table custom-table referenceEntryTable" id="referenceEntryTable">
	<thead>
		<tr>
			<th>Jurisdiction</th>
			<th class="noSort">Application #</th>
			<th>Mailing Date</th>
			<th class="noSort">Document Description</th>
			<th class="noSort">Uploaded By</th>
			<th>OCR Status</th>
			<th class="noSort">Notified</th>
			<th class="noSort" >Actions</th>
		</tr>
	</thead>
	<tbody id="referenceEntryTableBody">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>