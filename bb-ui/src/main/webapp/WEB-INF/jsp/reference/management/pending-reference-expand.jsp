<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
   String context = request.getContextPath();
   String css = context+"/assets/css";
   String js = context+"/assets/js";
   String images = context+"/assets/images";
   %>

<c:forEach items="${correspondenceList}" var="correspondence">
	<tr class="odd" id="${correspondence.id}">
		<td colspan="3"></td>
		<td>${correspondence.mailingDate}</td>
		<td>
			<form:form method="post" action="management/download" id="download"  target="_blank">
        				<input type="hidden" name="correspondenceId" value="${correspondence.id}">
       				<div class="">${correspondence.documentDescription} <a href="javascript:void(0)" onclick="document.getElementById('download').submit();" target="_blank">
				<img src="<%=images%>/svg/attachment.svg" class="icon16x pull-right attachmenticon"></a></div>
        			</form:form>
		</td>
		<td>${correspondence.ocrStatus} </td>
		<td>${correspondence.uploadedBy} 
         <br/>${correspondence.createdDate} </td> 
	    <td>
          <sec:authorize access="canAccessUrl('/reference/management/addRefPending')">
        			<form:form method="post" action="management/addRefPending" id="addRefLinkFrom${correspondence.id}" >
        				<input type="hidden" name="correspondence" value="${correspondence.id}">
       				 <div class="action-btns-grid"><a href="javascript:void(0)" onclick="document.getElementById('addRefLinkFrom${correspondence.id}').submit();">
       				 <img src="<%=images%>/svg/add-reference.svg" class="icon16x">Add References</a></div> 
        			</form:form>
          </sec:authorize>
	    </td>
	</tr>
</c:forEach>