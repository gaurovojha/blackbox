<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
	
<c:forEach items="${searchResult.items}" var="app">
	<%-- <tr class="${app.status eq 'DROPPED' ? 'hidden-row disabled': 'hidden-row' }" style="${app.status eq 'DROPPED' ? 'pointer-events: none': 'display: table-row;' }"> --%>
		<tr class="hidden-row" style="display: table-row">
		<td colspan="2"></td>
	<td>${app.jurisdiction}</td>
	<td>${app.applicationNo}</td>
	<td><a href="javascript:void(0)" >${app.lastIDSFillingDate}</a></td>
	<td>${app.prosecutionStatusAttorneyApproval}</td>
	<td>${app.referenceCount }</td>
	<td>${app.idsPreparedby}</td>
	<td>${app.attorneyApprovalComments}</td>
	<td>
      <div class="action-btns-grid">
         <sec:authorize access="canAccessUrl('/ids/attorneyApproval/approveIDS')">
          <form method="post" action="../attorneyApproval/approveIDS" id="addRefLinkFrom${app.dbId}" >
          <input type="hidden" name="idsId" value="${app.dbId}"/>
              <a href="javascript:void(0)" onclick="document.getElementById('addRefLinkFrom${app.dbId}').submit();"><i><img src="${pathImg}/view-doc.svg" class="icon16x"></i> Approve IDS</a>
          	  <a href="javascript:void(0)" class="action-danger" id="fileAction"><i><img src="${pathImg}/delete.svg" class="icon16x" id="${app.dbId}" onclick="popupMsgForReview(this);" ></i> Do Not File</a>
          </form>
       </sec:authorize>
     </div>
     </td> 

	</tr>
</c:forEach>


