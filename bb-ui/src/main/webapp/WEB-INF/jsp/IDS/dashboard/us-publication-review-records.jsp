<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:forEach items="${referenceList}" var="item">
	<tr>
	<input type = "hidden" class="flowId" value = "${item.referenceFlowId}"/>
		<td>
			<div class="checkbox-without-label">
				<input class ="selectOne" type="checkbox"><label>default</label>
			</div>
		</td>
		<td>${item.citeId }</td>
		<td>${item.publicationNo }</td>
		<td>${item.kindCode }</td>
		<td><bbx:date dateFormat="MMM dd, yyyy"
				date="${item.publicationDate}" /></td>
		<td>${item.applicantName }</td>
		<td class="usptoStatus">${item.refFlowStatus}/${item.refFlowSubStatus}</td>
	</tr>
</c:forEach>
