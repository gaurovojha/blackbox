<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<input type="hidden" id="recordsTotal"
	value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered"
	value="${searchResult.recordsFiltered}" />
<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
<c:set var="pathJs" value="${contextPath}/assets/js" scope="request" />
 

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="app" varStatus="status">
	<tr>
		<td><span class="icon icon-plus expandFamily"><jsp:text /></span></td>
			<c:choose>
		<c:when test = "${fn:contains(pageFamilies, app.familyId)}">
			<td><div class="duplicateFamily familyId">${app.familyId}</div></td>
		</c:when>
		<c:otherwise>
			<td><div class = "familyId">${app.familyId}</div></td>
			<c:set var="pageFamilies" value="${pageFamilies},${app.familyId}"/>
		</c:otherwise>
	</c:choose>
		<td>${app.jurisdiction}</td>
		<td ><a href="javascript:void(0)" data-toggle="modal" data-target="viewFamily" class="appId" data="${app.applicationID}"></a>${app.applicationNo }</td>
	   	<c:choose>
		<c:when test = "${app.lastIDSFillingDate eq null}">
			<td><div style="text-align:center;">-</div></td>
		</c:when>
		<c:otherwise> 
			<td>"${app.lastIDSFillingDate}"</td>
		</c:otherwise>
	</c:choose>
	   
	   <td> <a href="javascript:void(0)" data-toggle="modal" data-target="viewFamily" class="notificationId" data="${app.notificationProcessId}"></a>${app.prosecutionStatus}</td>
	  <td>${app.referenceCount }</td>
	  <td>${app.idsPreparedby}</td>                                                      
	  <td>${app.attorneyApprovalComments}</td>
	  <td>
      <div class="action-btns-grid">
       <sec:authorize access="canAccessUrl('/ids/attorneyApproval/approveIDS')">
          <form method="post" action="../attorneyApproval/approveIDS" id="addRefLinkFrom${app.dbId}" >
          <input type="hidden" name="idsId" value="${app.dbId}"/>
              <a href="javascript:void(0)" onclick="document.getElementById('addRefLinkFrom${app.dbId}').submit();"><i><img src="${pathImg}/view-doc.svg" class="icon16x"></i> Approve IDS</a>
      	 
        </form>
           <a href="javascript:void(0)" class="action-danger" id="donotFileLink${app.dbId}" onclick="popupMsgForReview(${app.dbId});" ><i><img src="${pathImg}/delete.svg" class="icon16x"></i> Do Not File</a>
       </sec:authorize>
     </div>
     </td> 
</tr>
</c:forEach>

<script type="text/javascript">

function donotFileIDSAction(){
	hideReviewPopUp();
	var idsId= document.getElementById('idsId');
	var action;
	$.ajax({type: "POST",
        url: $('#contextPath').val() +"/ids/attorneyApproval/IDSActions",
        data: { idsId: idsId,
        		action:"donotFileIDS",
        		
         },
        success:function(result){
        	
        	// window.location.href= $('#contextPath').val() +'/ids/attorneyApproval/approveIDS ';
         }});	
	}
	
</script>
 
