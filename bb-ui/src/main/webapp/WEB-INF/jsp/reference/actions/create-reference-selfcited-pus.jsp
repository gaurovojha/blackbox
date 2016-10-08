<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<div class="form-group">
      <div class="col-sm-6">
         <form:label class="control-label" path="publicationNumber">Patent / Publication No. <span class="required">*</span></form:label>
         <form:input class="form-control form-element" id="usPatentNo" path="publicationNumber" />
         <form:errors path="publicationNumber" class="error" />
      </div>
       <div class="col-sm-6">
         <label class="control-label">Kind Code <span class="required">*</span></label> 
     		<select class="form-control form-element" id="usKindCode" name="kindCode">
               <option value="">Select a Kind Code</option>
               <option value="A1">A1</option>
               <option value="A2">A2</option>
            </select>
       </div>
   </div>

<div id="InMannualUS">
    <div class="form-group" id="usDataMyself">
      <div class="col-sm-6">
      	<c:choose>
      		<c:when test="${not empty referenceDTO.correspondenceId}">
      	<c:set var="someVariable" value="${referenceDTO.correspondenceId.id}"/>
      		</c:when>
      		<c:otherwise>
      			<c:set var="someVariable" value="${referenceDTO.correspondenceId}"/>
      		</c:otherwise>
      	</c:choose>
      	
         <form:hidden path="correspondenceNumber" value="${someVariable}"/>
         <form:label class="control-label form-element" path="applicantName">Patentee / Applicant Name  <span class="required">*</span></form:label>
         <form:input class="form-control" id="usApplicantName" path="applicantName" />
         <form:errors path="applicantName" class="error" />
      </div>
      <div class="col-sm-6">
         <label class="control-label">Issue / Publication Date <span class="required">*</span></label> 
         <div class="input-group date datepicker">
            <form:input class="form-control form-element" id="publicationDate" path ="publicationDateStr"/>
            <span class="input-group-addon"> <i class="glyphicon glyphicon-calendar"></i> </span> 
            <form:errors path= "publicationDateStr" class="error"></form:errors>
         </div>
      </div>
   </div>
   
   <div class="form-group">
		<div class="col-sm-6">
         <label class="control-label">Add Comments</label> 
         <div class="input-group">
            <form:textarea id="usComments"  class="form-element" path="referenceComments" />
            <form:errors path= "referenceComments" class="error"></form:errors>
         </div>
      </div>
  </div>
</div>  
 