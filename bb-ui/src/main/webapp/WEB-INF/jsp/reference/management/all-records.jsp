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
<input type="hidden" id="recordsTotal" value="${referenceGroup.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${referenceGroup.totalElements}"/>

<p id="splitter"/>

<c:forEach items="${referenceGroup.content}" var="correspondence">
	 <tr class="odd">
	     <td class="text-center"><span class="icon icon-plus expand corrId" data="${correspondence.id}"><jsp:text /></span></td>
	     <td class="juris" data="${correspondence.jurisdictionCode}">${correspondence.jurisdictionCode}</td>
	     <td class="appNo" data="${correspondence.applicationNumber}">${correspondence.applicationNumber}</td>
	     <td>${correspondence.mailingDate}</td>
	     <td>
         	<a href="dashboard/downloadFile" target="_blank">
           		<form:form method="post" action="management/download" id="download${correspondence.id}"  target="_blank">
         				  <input type="hidden" name="correspondenceId" value="${correspondence.id}">
        				     <div class="">${correspondence.documentDescription} <a href="javascript:void(0)" onclick="document.getElementById('download${correspondence.id}').submit();" target="_blank">
					<img src="<%=images%>/svg/attachment.svg" class="icon16x pull-right attachmenticon"></a></div>
   			  	</form:form>
           	</a>
	     </td>
	     <td>${correspondence.referenceCount} </td>
		<td>${correspondence.uploadedBy}
			<br>${correspondence.createdDate}</td>
		<td>
			<sec:authorize access="canAccessUrl('/reference/management/review')">
       			<form method="post" action="management/review" id="reviewRefLinkForm${correspondence.id}" >
       				<input type="hidden" name="correspondence" value="${correspondence.id}">
   				    <div class="action-btns-grid"><a href="javascript:void(0)" onclick="document.getElementById('reviewRefLinkForm${correspondence.id}').submit();">
   	  				 	<img src="<%=images%>/svg/review-doc-reference.svg" class="icon16x">Review</a>
   	  				</div> 
       			</form>
        	</sec:authorize>
		</td>	
	 </tr>
</c:forEach> 