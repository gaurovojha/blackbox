<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
   String context = request.getContextPath();
   String css = context+"/assets/css";
   String js = context+"/assets/js";
   String images = context+"/assets/images";
   %> 
   
<div class="popup-msg alert" id="cancelUpdateRefPopup">
	<div class="text-right"><a class="close" href="#">&times;</a></div>
	<div role="alert" class="content">
		<p>Are you sure want to continue ?</p>
		<a href="../dashboard?requestComeFrom=updateReference"><button class="btn btn-submit">Yes</button></a>	
		<button class="btn btn-cancel">No</button>
	</div>
</div> 

<div class="col-sm-12">
	<div class="switch-control reference text-right hide">
      <label class="switch">
      <input type="checkbox" class="switch-input">
      <span class="switch-label" data-on="Automated: 24 hrs Turnaround" data-off="I Will do it myself"></span>
      <span class="switch-handle"></span>
      </label>
   </div>
   <div class="form-horizontal">
      <h4 class="reference-head">Add Reference Entry</h4>
      <div id="entry-block-container" class="margin-btm-10"></div>
      <div class="row">
         <div class="col-sm-12">
            <div class="form-group">
               <div class="col-sm-6">
                  <label class="control-label">Add New</label>
                  <select class="form-control" id="referenceEntries">
                     <option>Select a Reference Type</option>
                     <option value="us">US</option>
                     <option value="foreign">Foreign</option>
                     <option value="npl">NPL</option>
                  </select>
               </div>
               <div class="col-sm-6">
                  <div id="nplCheckbox" style="display: none;">
                     <input type="checkbox" id="usApp"><label class="control-label" for="usApp">US Unpublished Application</label>
                  </div>
               </div>
            </div>
            <form:form id="usReference" modelAttribute= "referenceDTO" class="form-horizontal" method="post"  action="../dashboard/submitAddDetailsUpdateReference">
	            <form:hidden id="referenceType" path="referencetype"/>
	            <form:hidden id="notificationProcessId" path="notificationProcessId"/>
	            <form:hidden path="applicationNumber"/>
	            <form:hidden path="applicationJurisdictionCode"/>
		        <form:hidden id="jurisdiction" path="jurisdiction.name"/>
		        <form:hidden id="refStagingId" path="refStagingId"/>
		        
		        <form:hidden id="fillingDate" path="applicationFilingDate"/>  
	            <form:hidden id="grantDate" path="applicationIssuedOn"/>   
	            <form:hidden id="typeofNumber" path="typeOfNumber"/>   
	            <form:hidden id="jurisdictionType" path="applicationJurisdictionType"/>
               <div id="usData" >
                  <jsp:include page="create-reference-pus.jsp"></jsp:include>
               </div>
               <div id="foreignData" >
                  <jsp:include page="create-reference-fp.jsp"></jsp:include>
               </div> 
               <div id="nplData" >
	               	  <jsp:include page="create-reference-npl.jsp"></jsp:include>
	            </div>
               <div class="form-footer">
                  <div class="col-sm-12">
                     <div class="form-group  text-left">

                        <a href="javascript:void(0)" onclick="openUpdateRefPopup();"><form:button type="button" class="btn btn-cancel">Cancel</form:button></a>
                        <form:button type = "button" class="btn btn-submit" id="BtnSubmit" value="..dashboard/submitAddDetailsUpdateReference">Submit </form:button>
                     </div>
                  </div>						
               </div>
            </form:form>
         </div>
      </div>
   </div>
</div>

<script type="text/javascript" src="${contextPath}/assets/js/common/util/validation-util.js"></script>
<script type="text/javascript" src = "<%=js%>/reference/validator/reference-validation.js"></script>

<script type="text/javascript">
	function openUpdateRefPopup() {
		$("#cancelUpdateRefPopup").removeClass("hide");
		$("#cancelUpdateRefPopup").show();
		$("#cancelUpdateRefPopup").wrap("<div class='overlay'>");
	}
	
	$(document).on("click", ".popup-msg a.close, .popup-msg .btn-cancel", function() {
		$(this).parents(".popup-msg").addClass("hide");
		$(this).parents(".popup-msg").unwrap("<div class='overlay'>");
	});
</script>

