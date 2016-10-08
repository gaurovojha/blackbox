<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>

<input type="hidden" id="recordsTotal" value="${familyGroup.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${familyGroup.totalElements}"/>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<p id="splitter"/>
	
<c:forEach items="${familyGroup.content}" var="allRecords" varStatus="status">
	<tr class="odd">
		<td>${allRecords.familyId}</td>
		<td>${allRecords.familyMemberCount}</td>
		<td>${allRecords.subjectMatterCount}</td>
		<td class="">
			<div class="action-btns-grid">
				 <form:form method="post" action="flowRule/reviewFamily" id="reviewRefFlowLinkForm${allRecords.familyId}"><!-- family ID we have to give--> 
					<input type="hidden" name="familyid" value="${allRecords.familyId}"/>
	      				<div class="action-btns-grid"><a href="javascript:void(0)" onclick="document.getElementById('reviewRefFlowLinkForm${allRecords.familyId}').submit();">
	      					<img src="<%=images%>/svg/review-doc-reference.svg" class="icon16x">Review</a>
	      				</div> 
				</form:form>
			</div>
		</td>
	</tr>
</c:forEach>