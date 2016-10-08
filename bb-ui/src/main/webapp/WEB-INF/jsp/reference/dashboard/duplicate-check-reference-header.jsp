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

<table id="duplicateCheckReferenceTable" class="table custom-table duplicateCheckReferenceTable">
    <thead>
        <tr>
            <th class="set-width">NPL Description</th>
            <th class="text-center set-width noSort">Source Application
                <div class="col-md-12">
                    <div class="col-md-4">Jurisdiction</div>
                    <div class="col-md-4">Application #</div>
                    <div class="col-md-4">Source Doc.</div>
                </div>
            </th>
            <th>Notified</th>
            <th class="text-center noSort">Actions</th>
        </tr>
    </thead>
    <tbody id="duplicateCheckRefTableBody">
    	<jsp:text/>
    </tbody>
</table>

<div id="pageInfo"><jsp:text/></div>