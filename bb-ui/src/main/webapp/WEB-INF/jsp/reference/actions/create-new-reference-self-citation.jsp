<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
   String context = request.getContextPath();
   String css = context+"/assets/css";
   String js = context+"/assets/js";
   String images = context+"/assets/images";
%>

<input type="hidden" id="jurisdictionList"	value="${listJurisdictions}" />

<div class="main-content container">
    <div class="page-header">
        <h2 class="page-heading">New Reference (Self- citation)</h2>
    </div>
    <table class="table custom-table">
        <thead>
            <tr>
                <th>Jurisdiction</th>
                <th>Application #</th>
            </tr>
        </thead>
        <tbody>
            <tr class="even">
                <td>${jurisdictionCode}</td>
                <td>${applicationNumber}</td>
            </tr>
        </tbody>
    </table>
    <div class="form-horizontal">
      <h4 class="reference-head">Add Reference Entry</h4>
      <div id="entry-block-container" class="margin-btm-10"></div>
      <div class="switch-control reference text-right">
	      <label class="switch">
	      <input type="checkbox" class="switch-input">
	      <span class="switch-label" data-on="Automated: 24 hrs Turnaround" data-off="I Will do it myself"></span>
	      <span class="switch-handle"></span>
	      </label>
	   </div>
      <div class="row">
         <div class="col-sm-6">
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
            
            <form:form id="usReference" commandName= "referenceDTO" class="form-horizontal" method="post"  action="../dashboard/submitSelfCitedReference" enctype="multipart/form-data">
	            <form:hidden id="referenceType" path="referencetype"/>
	            <form:hidden id="manualAdd" path="manualAdd" value="true"/>
	            <form:hidden id="notificationProcessId" path="notificationProcessId"/>
	            <form:hidden path="applicationNumber"/>
	            <form:hidden id="jurisdictionType" path="applicationJurisdictionCode"/>
	            <form:hidden id="usJurisdiction" path="jurisdiction.name"/>
	            
	            <!-- Application related detail used for conversion -->
	            <form:hidden id="fillingDate" path="applicationFilingDate"/>
	            <form:hidden id="grantDate" path="applicationIssuedOn"/>
	            <form:hidden id="typeofNumber" path="typeOfNumber"/>
	            
	               <div id="usData" >
		              <jsp:include page="create-reference-selfcited-pus.jsp"></jsp:include>
		           </div>
		           <div id="foreignData" >
		              <jsp:include page="create-reference-selfcited-fp.jsp"></jsp:include>
		           </div>
		           <div id="nplData" >
	               	  <jsp:include page="create-reference-selfcited-npl.jsp"></jsp:include>
	               </div>
	               <div class="form-footer">
				     <div class="col-sm-12">
				        <div class="form-group  text-left">
				        	<form:button type="button" class="btn btn-cancel" onclick="popupMsgForAddReference(this);">Cancel</form:button>
				           <form:button type = "button" class="btn btn-submit" id="BtnSubmit" >Submit </form:button>
				        </div>
				     </div>
				  </div>
            </form:form>
         </div>
      </div>
   </div>
</div>

<div id="popupMsgForAddReference" class="popup-msg">
	<div class="text-right"><a class="close" href="javascript:void(0)" onclick="hidepopupMsgForAddReference();">&times;</a></div>
	<div class="content">
		<p class="msg">All data will be lost. Do you want to proceed.</p>
	</div>
	<div class="modal-footer">
		<a type="button" class="btn btn-submit" data-dismiss="modal" href="../dashboard">Yes</a>
        <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hidepopupMsgForAddReference();">No</button>
    </div>
</div>

<script type="text/javascript" src="${contextPath}/assets/js/common/util/validation-util.js"></script>
<script type="text/javascript" src = "<%=js%>/reference/validator/reference-validation.js"></script>
<script type="text/javascript" src="<%=js%>/reference/reference-dashboard.js"></script>
