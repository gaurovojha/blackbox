<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="pathImg" value="${contextPath}/assets/images/svg" />

<div>
	<sec:authorize
		access="canAccessUrl('/correspondence/changeReviewDocumentStatus')"
		var="accessDeleteDoc" />
</div>

	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Review Document</h4>
	      </div>
	      	<div class="modal-body">
	      	<form:form class="form-horizontal" method="get"
				modelAttribute="correspondenceReviewDocument"
				action="${contextPath}/correspondence/changeReviewDocumentStatus">
		        <div class="row">
		        	<div class="col-sm-5">
		        		<div class="form-horizontal">
		        			<div class="action-danger"><i><img src="${pathImg}/exclamation.svg" class="icon16x"></i> Atachment Error (01234), Ths is an error description.</div>
		        			<div class="divider"></div>
		        			<div class="form-group">
			        			<div class="col-sm-6">
			        				<label class="control-label">Application No</label>
			        				<div class="form-contorl-static">${reviewDocument.applicationNumber}</div>
			        			</div>
			        			<input type="hidden" name="recordId" value="${reviewDocument.dbId}" />
			        			<div class="col-sm-6">
			        				<label class="control-label">Jurisdiction</label>
			        				<div class="form-contorl-static">${reviewDocument.jurisdictionCode}</div>
			        			</div>
			        		</div>
			        		<div class="form-group">
			        			<div class="col-sm-6">
			        				<label class="control-label">Mailing Date</label>
			        				<div class="form-contorl-static">${reviewDocument.mailingDate}</div>
			        			</div>
			        		</div>
			        		<div class="form-group">
			        			<div class="col-sm-12">
			        				<label class="control-label">Document Description</label>
			        				<div class="form-contorl-static">${reviewDocument.documentDescription}</div>
			        			</div>
			        		</div>
		        		</div>
		        	</div>
		        	<div class="col-sm-7">
		        		<div class="pdf-preview"><embed id="embedPdf" src="${contextPath}/correspondence/reviewDoc/viewPdf/${reviewDocument.dbId}" width="500" height="575" type='application/pdf'></embed></div>	
		        	</div>
		        </div>
		        <div class="modal-footer">
		        	<a type="button" class="btn btn-submit" href="${contextPath}/correspondence/actionItems">No Change Required</a>
	     			<!-- <button type="button" class="btn btn-cancel">No Change Required</button> -->
	      			<button type="submit" class="btn btn-submit" disabled="${accessDeleteDoc}">Delete Document</button>
	     		 </div>
		      </form:form>
      		</div>
	    </div>
	  	</div>