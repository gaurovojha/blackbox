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

<!DOCTYPE html>

<input type="hidden" id="recordsTotal" value="${pendingList.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${pendingList.totalElements}"/>

<p id="splitter"/>
 
<c:forEach items="${pendingList.content}" var="correspondence">
  <tr class="odd">
      <td class="text-center"><span class="icon icon-plus expand corrId" data="${correspondence.id}"><jsp:text /></span></td>
      <td>${correspondence.jurisdictionCode}</td>
      <td>${correspondence.applicationNumber}</td>
      <td>${correspondence.mailingDate}</td>
      <td>
         <form:form method="post" action="management/download" id="download"  target="_blank">
       				<input type="hidden" name="correspondenceId" value="${correspondence.id}">
     				<div class="">${correspondence.documentDescription} <a href="javascript:void(0)" onclick="document.getElementById('download').submit();" target="_blank">
					<img src="<%=images%>/svg/attachment.svg" class="icon16x pull-right attachmenticon"></a></div>
         </form:form>
          
      </td>
      <td class="error">${correspondence.ocrStatus} </td>
		<td>${correspondence.uploadedBy} 
         <br/>${correspondence.createdDate} </td> 
	  <td>
          <sec:authorize access="canAccessUrl('/reference/management/addRefPending')">
       			<form method="post" action="management/addRefPending" id="addRefLinkFrom${correspondence.id}" >
       				<input type="hidden" name="correspondence" value="${correspondence.id}">
      				 	<div class="action-btns-grid"><a href="javascript:void(0)" onclick="document.getElementById('addRefLinkFrom${correspondence.id}').submit();">
      				 		<img src="<%=images%>/svg/add-reference.svg" class="icon16x">Add References</a></div> 
       			</form>
          </sec:authorize>
	  </td>
  </tr>
 </c:forEach>