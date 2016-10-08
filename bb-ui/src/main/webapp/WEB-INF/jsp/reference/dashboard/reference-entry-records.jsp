<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>

<input type="hidden" id="recordsTotal" value="${referenceEntryRecords.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${referenceEntryRecords.totalElements}"/>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<script type="text/javascript">
	$('.overlay').hide();
	
	function dismiss(){
		$('.overlay').hide();
	}
	
	var referenceId;
	
	function delbtn(id) {
		referenceId=id;
		$("#deleteDocPopup").removeClass("hide");
		$("#deleteDocPopup").show();
		$("#deleteDocPopup").wrap("<div class='overlay'>");
		//$('.overlay').show();
		
	}
	
	function deleteRecord(){
		//$('.overlay').hide();
		$("#deleteDocPopup").addClass("hide");
		$("#deleteDocPopup").unwrap("<div class='overlay'>");
		$('input[type=hidden][name=id]').val(referenceId);
		$('#deleteReference').submit();
	}
	
	$(document).on("click", ".popup-msg a.close, .popup-msg .btn-cancel", function() {
		$(this).parents(".popup-msg").addClass("hide");
		$(this).parents(".popup-msg").unwrap("<div class='overlay'>");
	});
</script>

<form:form method="post" action="${pageContext.request.contextPath}/reference/dashboard/deleteDocument" id="deleteReference">
	<input type="hidden" name="id">
</form:form>

<div class="popup-msg alert" id="deleteDocPopup">
	<div class="text-right"><a class="close" href="#">&times;</a></div>
	<div role="alert" class="content">
		<p>Are you sure want to continue ?</p>
		<button class="btn btn-submit" onclick="deleteRecord();">Yes</button>	
		<button class="btn btn-cancel">No</button>
	</div>
</div>

<%-- <div class="overlay">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<div class="text-right">
					<a class="close" onclick="dismiss()" href="#">×</a>
				</div>
			</div>
			<div class="modal-body">
				<div class="content">
					<p class="msg">Are you sure you want to delete this document?</p>
				</div>
				<div class="modal-footer">
					<form:form method="post" action="${pageContext.request.contextPath}/reference/dashboard/deleteDocument" id="deleteReference">
						<input type="hidden" name="id">
					</form:form>
					<button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="deleteRecord();">Yes</button>
					<button type="button" class="btn btn-submit" onclick="dismiss()">No</button>
				</div>
			</div>
		</div>
	</div>
</div> --%>

<p id="splitter"/>
	
<c:forEach items="${referenceEntryRecords.content}" var="refEntry" varStatus="status">
	<tr class="odd">
		<td>${refEntry.jurisdictionCode}</td>
		<td>${refEntry.applicationNumber}</td>
		<td>${refEntry.mailingDate}</td>
		<td>
            <div class="">${refEntry.documentDescription} 
            	<a href="dashboard/downloadFile" target="_blank">
            		<i><img src="<%=images%>/svg/attachment.svg" class="icon16x pull-right"></i>
            	</a>
            </div>
        </td>
		<td>${refEntry.uploadedBy}<br /> ${refEntry.uploadedOn}</td>
		<td>
			<c:choose>
				<c:when test="${refEntry.ocrStatus == 'DONE'}">
					<div class="action-success">Done</div>
				</c:when>
				<c:otherwise>
					<div class="action-danger">Error</div>
				</c:otherwise>
			</c:choose>
		</td>
		<td>${refEntry.notifiedOn}</td>
		<c:url var="arfd" value="dashboard/addReferenceForDone">
			<c:param name="id" value="${refEntry.notificationProcessId}"/>			
		</c:url>
		
	
		<td class="">
			<div class="action-btns-grid">
			<c:choose>
				<c:when test="${refEntry.locked}">
					<span  class="icon icon-lock"> </span> Locked By: ${refEntry.lockedByUser}
				</c:when>
				<c:otherwise>
		 		        <c:choose>
			 		        <c:when test="${refEntry.ocrStatus eq 'FAILED'}">
				 		        <form:form method="post" action="dashboard/addReferenceForFailed" id="addReference${refEntry.ocrDataId}">
							         <input type="hidden" name="id" value="${refEntry.notificationProcessId}">
							         <input type="hidden" name="ocrid" value="${refEntry.ocrDataId}">					
									 <a href="javascript:void(0)" onclick="document.getElementById('addReference${refEntry.ocrDataId}').submit();" >
									 	<i><img src="<%=images%>/svg/add-reference.svg" class="icon16x"></i> Add References
									 </a> 
						       </form:form>
					       </c:when>
					       <c:otherwise>
					       		<form:form method="post" action="dashboard/addReferenceForDone" id="addReference${refEntry.ocrDataId}">
							         <input type="hidden" name="id" value="${refEntry.notificationProcessId}">
							         <input type="hidden" name="ocrid" value="${refEntry.ocrDataId}">
									 <a href="javascript:void(0)" onclick="document.getElementById('addReference${refEntry.ocrDataId}').submit();" >
									 	<i><img src="<%=images%>/svg/add-reference.svg" class="icon16x"></i> Add References
									 </a> 
						       </form:form>
					       </c:otherwise>
				       </c:choose>
				       <form:form method="post" action="dashboard/deleteDocumentConfirmation" id="deleteDocumentConfirmation${refEntry.ocrDataId}">
					        <input type="hidden" name="id" value="${refEntry.notificationProcessId}">					
							<a href="javascript:void(0)" class="action-danger" onclick="delbtn(${refEntry.notificationProcessId})">
								<i><img src="<%=images%>/svg/delete.svg" class="icon16x"></i> Delete Document
							</a> 
				        </form:form>
				</c:otherwise>
			</c:choose>
			</div>
		</td>
	</tr>
</c:forEach>
