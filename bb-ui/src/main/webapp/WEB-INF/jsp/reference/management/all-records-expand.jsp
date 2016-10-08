<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
   String context = request.getContextPath();
   String css = context+"/assets/css";
   String js = context+"/assets/js";
   String images = context+"/assets/images";
   %>  

<c:forEach items="${correspondenceList}" var="correspondence">
	<tr class="odd hidden-row" style='display: table-row;' id="${correspondence.id}">
		<td colspan="3"></td>
		<td>${correspondence.mailingDate}</td>
		<td>
			<form:form method="post" action="management/download" id="download${correspondence.id}"  target="_blank">
     			<input type="hidden" name="correspondenceId" value="${correspondence.id}">
   			    <div class="">${correspondence.documentDescription} <a href="javascript:void(0)" onclick="document.getElementById('download${correspondence.id}').submit();" target="_blank">
				<img src="<%=images%>/svg/attachment.svg" class="icon16x pull-right attachmenticon"></a></div>
     		</form:form>
		</td>
		<td>${correspondence.referenceCount} </td>
		<td>${correspondence.uploadedBy}
			<br>${correspondence.createdDate}</td>
		<td>
		<sec:authorize access="canAccessUrl('/reference/management/review')">
   			<form:form method="post" action="management/review" id="reviewRefLinkForm${correspondence.id}" >
   				<input type="hidden" name="correspondence" value="${correspondence.id}">
   				<div class="action-btns-grid"><a href="javascript:void(0)" onclick="document.getElementById('reviewRefLinkForm${correspondence.id}').submit();">
  				 	<img src="<%=images%>/svg/review-doc-reference.svg" class="icon16x">Review</a>
			 	</div> 
   			</form:form>
          </sec:authorize>
		</td>	
	</tr>
</c:forEach> 