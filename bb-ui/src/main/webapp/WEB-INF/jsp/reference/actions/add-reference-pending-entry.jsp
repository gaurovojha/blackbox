	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String images = context+"/assets/images";
	String js = context+"/assets/js";
%>
<input type ="hidden" id="correspondenceId" value="${correspondenceDTO.id}" /> 
<input type="hidden" id="jurisdictionList"	value="${listJurisdictions}" />
<input type="hidden" name="redirectUrl" value="${redirectUrl}" />
<div id="main" class="main-content container">
	<ul class="breadcrumb">
		<li><a href="../management">Reference Management</a></li>
		<li>Add References</li>
	</ul>
	<form id="nullReference" method="post" action="../management/addNullReference">
		<input  type="hidden" name="correspondence" value="${correspondenceDTO.id}">	
	</form>
	<table class="table custom-table">
		<thead>
			<tr>
				<th>Jurisdiction</th>
				<th>Application #</th>
				<th>Mailing Date</th>
				<th>Document Description</th>
				<th>Uploaded by</th>
				<th>Last Updated</th>
			</tr>
		</thead>
		<tbody><tr> 
					<td>${correspondenceDTO.jurisdictionCode} </td>
					<td>${correspondenceDTO.applicationNumber}</td>
					<td>${correspondenceDTO.mailingDate}</td>
					<td>${correspondenceDTO.documentDescription}</td>
					<td><div>${correspondenceDTO.uploadedBy}</div><div>${correspondenceDTO.createdDate}</div></td>
					<td><div>${correspondenceDTO.lastUpdateBy}</div><div>${correspondenceDTO.updatedDate}</div></td>
				</tr>
		</tbody>
	</table>
	<div class="form-horizontal">
		<div class="row">
			<div class="col-sm-6">
				<div class="pdf-preview-container single">
					<h2>OCR'ed PDF</h2>
					<div>
					<iframe style="height:800px; width:100%" src="<%=context%>/reference/management/download?correspondenceId=${correspondenceDTO.id}"></iframe>
				</div>
				</div>
			</div>
			<c:choose>
			           <c:when test="${hide eq 'true'}" >
							        <div id="add-ref-btn" class="col-sm-6">
											<div class="reference-btns-group">
												<a id="action" class="btn btn-submit">Add References</a>
												<c:if test="${!isReferenceExist}">
												    <a href="javascript:void(0)" onclick="$('#nullReference').submit();" id="noRef" class="btn btn-cancel">No Reference Found</a>
												</c:if>
											</div>
												
										</div>
	                      	</c:when>
	                      	
	                      	<c:otherwise>
	                      			<div class="col-sm-6">
	                      	       <jsp:include page="add-reference-pending-entry-list.jsp"></jsp:include>
	                      	       </div>
	                      	</c:otherwise>
		   </c:choose>	
		</div>
		</div>	
		</div>
		
<script>
console.log($('.successResult').attr('data-success'));
if(($('.successResult').attr('data-success'))==='success'){
	$('.overlay').show();
}
else{
	$('.overlay').hide();
}
$('.closeModal').on('click',function(){
	$('.overlay').hide();
	}
);

$("#action").on('click',function(){
	console.log('in');
	var correspondenceId = $('#correspondenceId').val();
	var hide=false;
	refAddAction('/reference/management/addReferenceManualAdd',"add-ref-btn", correspondenceId, hide);
});

function refAddAction(url, id, correspondenceId, hide) {
	$.ajax({
		type: "GET",
		url: $('#contextPath').val() + url,
		data: {'correspondence':correspondenceId, 'hide':hide},
		contentType: "application/json",
		success: function(response) {
			$('#'+id).html(response);
		}	
	});
}
</script>