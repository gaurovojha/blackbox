<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<div class="main-content container">
	<ul class="breadcrumb">
		<li><a href="../dashboard?requestComeFrom=updateReference">My Dashboard</a></li>
		<li>Add References</li>
	</ul>
	<table class="table custom-table">
		<thead>
			<tr class="small-height-row">
	            <th></th>
	            <th></th>
	            <th colspan="3" class="text-center">Source</th>
             	<th></th>
	        </tr>
			<tr>
				<th>Ref. Jurisdiction</th>
				<th>Ref. Publication No</th>
				<th class="bdr-rt-none">Jurisdiction</th>
				<th class="bdr-rt-none">Application No.</th>
				<th>Source Doc.</th>
				<th>Notification Date</th>
			</tr>
		</thead>
		<tbody>
			<tr class="odd">
				<td>${dto.refJurisdictionCode}</td>
				<td>${dto.publicationNo}</td>
				<td class="bdr-rt-none">${dto.sourceJurisdictionCode}</td>
				<td class="bdr-rt-none">${dto.applicationNumber}</td>
				<td>
					<c:if test="${not empty dto.correspondenceId}">
						<a href="downloadFile" target="_blank">
		        			<span class="icon icon-attach" data-toggle="tooltip" title="${reference.documentDescription}"></span>
		        		</a>
					</c:if>
				</td>
				<td>${dto.notifiedDate}</td>
			</tr>
		</tbody>
	</table>
	
	<div class="form-horizontal">
		<div class="row">
			<c:if test="${not empty dto.correspondenceId}">
				<div class="col-sm-6">
					<div class="pdf-preview-container">
						<h2>OCR - Scanned</h2>
						<div class="hide-pdf">- Hide OCR PDF</div>
						<div class="show-pdf">+ Show Original PD</div>
						<div>
							<iframe style="height:800px; width:100%" src="<%=context%>/reference/dashboard/downloadFile"></iframe>
						</div>
					</div>
				</div>
			</c:if>
			<div class="col-sm-6">
				<%-- <jsp:include page="update-reference-detail.jsp"></jsp:include --%>
				<jsp:include page="update-add-reference.jsp"></jsp:include>
			</div>
        </div>
	    
	</div>
</div>

