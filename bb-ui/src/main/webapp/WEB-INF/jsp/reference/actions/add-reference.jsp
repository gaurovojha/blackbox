<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%
   String context = request.getContextPath();
   String css = context+"/assets/css";
   String js = context+"/assets/js";
   String images = context+"/assets/images";
   %>  

<div class="col-sm-12">
   <div class="switch-control reference text-right">
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
            
            <form:form id="usReference" modelAttribute= "referenceDTO" class="form-horizontal" method="post" action="../management/addReference" enctype="multipart/form-data">
	            <form:hidden id="referenceType" path="referencetype"/>
	            <form:hidden id="manualAdd" path="manualAdd" value="true"/>
	            <form:hidden path="correspondenceId.id"/>
	            <form:hidden id="notificationProcessId" path="notificationProcessId"/>
	            <form:hidden path="applicationNumber"/>
	            <form:hidden path="applicationJurisdictionCode"/>
	            <form:hidden id="jurisdiction" path="jurisdiction.name"/>
	           	<input type="hidden" name="redirectUrl" value="${redirectUrl}" />
	           	<input type="hidden" name="hide" value="false" />
	           	
	            <!-- Hidden fields from applicationBase set for validation  -->
	          
	            <form:hidden id="fillingDate" path="applicationFilingDate"/>  
	            <form:hidden id="grantDate" path="applicationIssuedOn"/>   
	            <form:hidden id="typeofNumber" path="typeOfNumber"/>   
	            <form:hidden id="jurisdictionType" path="applicationJurisdictionType"/>   
	            <input type="hidden" name="closeNotification" value="${closeNotification}"/>
	            <input type="hidden" name="redirectToDashboard" value="${redirectToDashboard}"/>
	            <input type="hidden" name="id" value="${id}"/>
	               
	               
	               <div id="usData" >
	                  <jsp:include page="create-reference-pus.jsp"></jsp:include>
	               </div>
	               <div id="foreignData" >
	                  <jsp:include page="create-reference-fp.jsp"></jsp:include>
	               </div> 
	               <div id="nplData" >
	               	  <jsp:include page="create-reference-npl.jsp"></jsp:include>
	               </div>
	               <form:button type = "button" class="btn btn-submit" id="BtnSubmit" value="../management/addReference">Add </form:button>
	               <div class="form-footer">
	                  <div class="col-sm-12">
	                     <div class="form-group  text-left">
	
	                        <form:button type="button" class="btn btn-cancel" onclick="popupMsgForAddReference(this);">Close</form:button>
	                     </div>
	                  </div>						
	               </div>       
            </form:form>
         </div>
      </div>
   </div>
</div>

<div id="popupMsgForAddReference" class="popup-msg">
	<div class="text-right"><a class="close" href="#">&times;</a></div>
	<div class="content">
		<p class="msg">Are you sure you want to proceed? </p>
	</div>
	<div class="modal-footer">
		<c:choose>
			<c:when test="${dashbordSubMenu} eq 'referenceEntry'">
				<a type="button" class="btn btn-submit" data-dismiss="modal" href="../dashboard">Yes</a>
			</c:when>
			<c:otherwise>
				<a type="button" class="btn btn-submit" data-dismiss="modal" href="../management">Yes</a>	
			</c:otherwise>
        </c:choose>
        <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hidepopupMsgForAddReference();">No</button>
    </div>
</div>

<div id="popupMsgReview" class="popup-msg">
	<div class="text-right"><a class="close" href="javascript:void(0)" onclick="hideReviewPopUp();">&times;</a></div>
	<div class="content">
		<p class="msg">This reference will be deleted.</p>
		<p class="msg">Do you want to proceed ?</p>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-submit" onclick="reviewDeleteAction();"><spring:message code="text.yes" /></button>
        <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hideReviewPopUp();"><spring:message code="text.no" /></button>
    </div>
</div>

<script type="text/javascript" src="<%=js%>/reference/reference-management.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/common/util/validation-util.js"></script>
<script type="text/javascript" src = "<%=js%>/reference/validator/reference-validation.js"></script>