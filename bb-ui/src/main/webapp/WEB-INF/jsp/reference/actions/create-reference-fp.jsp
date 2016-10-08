<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
String context = request.getContextPath();
  String images = context+"/assets/images";
%>

   <div class="form-group">
      <div class="col-sm-2">
         <label class="control-label">Jurisdiction<span class="required">*</span></label> 
         <input type="text" class="form-control form-element jurisdiction" id="fgnJurisdictioin" />
         <form:errors path="jurisdiction.name" class="error" />
      </div>
     
     <div class="col-sm-5">
         <label class="control-label">Patent / Publication No.<span class="required">*</span></label> 
         <form:input type="text" class="form-control form-element" id="fgnPatentNo" path= "publicationNumber" />
   <%--       <form:hidden path="correspondenceId" value="${referenceDTO.correspondenceId}"/> --%>
         <form:errors path="publicationNumber" class="error"></form:errors>
      </div>
      
      <div class="col-sm-5">
         <label class="control-label">Kind Code <span class="required">*</span></label> 
           <select class="form-control form-element" id="fpKindCode" name="kindCode">
                     <option value="">Select a Kind Code</option>
                     <option value="A1">A1</option>
                     <option value="A2">A2</option>
            </select>
          </div>
       </div>
  <div id="InManualFP">
   <div class="form-group">  
      <div class="col-sm-7">
         <label class="control-label">Patentee / Applicant Name <span class="required">*</span></label> 
         <form:input  class="form-control form-element" id="fgnPatenteeName" path="applicantName" />
         <form:errors path="applicantName" class="error" />
      </div>
        <div class="col-sm-5">
         <label class="control-label">Issue / Publication Date <span class="required">*</span></label> 
         <div class="input-group date datepicker">
            <form:input class="form-control form-element" id="fgnPublicationDate" path="publicationDateStr"/>
            <span class="input-group-addon"> <i class="glyphicon glyphicon-calendar"></i> </span> 
            <form:errors path="publicationDateStr" class="error" />
         </div>
      </div>
   </div>
        <div class="form-group">
     <div class="col-sm-6">
         <label class="control-label">Add Comments</label> 
         <div class="input-group">
            <form:textarea id="FPComments" class="form-element"  path="referenceComments" />
            <form:errors path= "referenceComments" class="error"></form:errors>
         </div>
      </div>
      </div>
   <div class="divider"></div>
<div class="form-group">
   <div class="col-sm-6 text-left">
      <div class="form-control-static">
         <a href="javascript:document.getElementById('PdfFile').click()"><i><img src="<%=images %>/svg/add.svg" class="icon16x"></i>
         <form:input path="file" id="PdfFile" class="form-element" type="file" style="display:none" accept="application/pdf" /><label for="PdfFile">Add an Attachment (PDF Only)</label></a><div id="filename"></div> 
      </div>
   </div>
   <div class="col-sm-6 text-right">
      <input type="checkbox" name="englishTranslation" class="form-element" id="engTranslation" />
      <label class="control-label" for="engTranslation">English Translation</label>
   </div>
</div>
</div>