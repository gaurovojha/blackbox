<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
	String context = request.getContextPath();
%>

<div class="modal-dialog" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="myModalLabel">View Document</h4>
		</div>
		<div class="modal-body">
			<div class="row">
				<div class="col-sm-5">
					<div class="form-horizontal">
						<div class="form-group">
							<div class="col-sm-6">
								<label class="control-label">Application No</label>
								<div class="form-contorl-static">${correspondenceDocument.applicationNumber}</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Jurisdiction</label>
								<div class="form-contorl-static">${correspondenceDocument.jurisdictionCode}</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-6">
								<label class="control-label">Mailing Date</label>
								<div class="form-contorl-static">${correspondenceDocument.mailingDate}</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12">
								<label class="control-label">Document Description</label>
								<div class="form-contorl-static">${correspondenceDocument.documentDescription}</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-sm-7">
					<div class="form-contorl-static">
					<c:choose>
					<c:when test="${correspondenceDocument.viewDocumentLink}">		
						<div style="height:643px" class="pdf-preview">
							<iframe style="height: 99%; width: 100%"
								src="<%=context%>/correspondence/viewDocument/viewPdf/${correspondenceDocument.dbId}">
							</iframe>	
						</div>						
					</c:when>
					<c:otherwise>
						<div class="col-sm-6 has-error">
							<div class="error">
								<label class="action-faulty"><spring:message code="message.correspondence.dashboard.correspondence.exportControl" /></label>
							</div>	
						</div>
					</c:otherwise>
					</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>