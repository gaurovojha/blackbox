<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String context = request.getContextPath();
	String js = context + "/assets/js";
%>
<%-- <script type="text/javascript"
	src="<%=js%>/correspondence/correspondence.js"></script> --%>


<div class="modal-dialog" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="myModalLabel">Add New Record</h4>
		</div>
		<div class="modal-body">
			<form:form class="form-horizontal" id="newCorrespondence"
				modelAttribute="correspondence" method="POST"
				enctype="multipart/form-data" action="./addCorrespondence">
				<form:errors path="*" />
				<div class="form-group">
				<form:hidden path=""/>
					<div class="col-sm-6">
						<label class="control-label">Application #</label>
						<form:input type="text" name="applicationNumber"
							class="form-control" readonly="true" path="applicationNumber" />

						<form:errors path="applicationNumber" class="error" />
					</div>
					<div class="col-sm-6">
						<label class="control-label">Jurisdiction</label>
						<form:input type="text" name="jurisdiction" id="jurisdiction2"
							class="form-control" readonly="true" path="jurisdiction" />
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6">
						<label class="control-label">Mailing Date</label>
						<form:input type="text" name="mailingDate" id="mailingDateId"
							class="form-control" readonly="true" path="mailingDate" />
						<form:errors path="mailingDate" class="error" />
					</div>

				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<label class="control-label">Document Description</label>
						<form:input type="text" name="documentDescription"
							id="documentDescriptionId" class="form-control" readonly="true"
							path="documentDescription" />
						<form:errors path="documentDescription" class="error" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<label class="control-label">Attachment</label>
						<div class="input-group">
							<input id="uploadFile" placeholder="Choose File"
								disabled="disabled" class="form-control" />
							<div class="fileUpload btn btn-blue input-group-btn">
								<span>Browse</span> <input type="file" name="file"
									id="uploadBtn" class="upload" accept="application/pdf" />

							</div>

						</div>
						<form:errors path="file" class="error" />
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-cancel" data-dismiss="modal">Cancel</button>
					<button type="button" id="createNewCorrespondence"
						class="btn btn-submit">Create</button>
				</div>
			</form:form>

		</div>
	</div>
</div>