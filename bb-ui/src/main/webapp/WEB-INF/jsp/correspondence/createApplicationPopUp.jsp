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

<input type="hidden" id="response" value=${success} />
<input type="hidden" id="jurisdictionData" value="${jurisdiction}" />
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
			<form:form class="form-horizontal" id="newApplicationRedirect"  modelAttribute="correspondenceForm" method="POST" enctype="multipart/form-data" action="./createApplication">
				
				<div class="form-group">
					<div class="col-sm-6">
						<label class="control-label">Application #</label>
						 <form:input
							type="text" name="applicationNumber" id="applicationNumber" class="form-control"  readonly="true" path="applicationNumber"  />
							
							<form:errors path="applicationNumber"  class="error" />
					</div>
					<div class="col-sm-6">
						<label class="control-label">Jurisdiction</label> <form:input
							type="text"  name="jurisdiction" id="jurisdiction" class="form-control"  readonly="true" path="jurisdiction"  />
							<form:errors path="jurisdiction"  class="error" /> 
					</div>
				</div>

				 <div class="modal-footer">
	       <button type="button" id="newCorrespondenceFormCancel" class="btn btn-cancel" data-dismiss="modal">Cancel</button>
	        <button type="submit" id="CreateNewApplication" class="btn btn-submit" >Create</button>
	      </div>	
			</form:form>

		</div>
	</div>
</div>
