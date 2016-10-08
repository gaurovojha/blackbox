<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String context = request.getContextPath();
	String js = context + "/assets/js";
%>
<%-- <script type="text/javascript" src="<%=js%>/correspondence/correspondence.js"></script> --%>
<div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Add New Document</h4>
	      </div>
	      <div class="modal-body">
		        <form class="form-horizontal">
		        	<div class="form-group">
		        		<div class="col-sm-6">
		        			<label class="control-label">Application #</label>
		        			<input id="applicationNumber" type="text" class="form-control" value="">
		        		</div>
		        		<div class="col-sm-6">
		        			<label  class="control-label">Jurisdiction</label>
		        			<input id="jurisdiction" type="text" class="form-control" value="">	
		        		</div>
		        	</div>
		        	
	    		</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-cancel" data-dismiss="modal">Cancel</button>
	        <button id="SearchApplication" type="button" class="btn btn-submit" id="showAddNewDoc">Search</button>
	      </div>
	    </div>
	  </div>