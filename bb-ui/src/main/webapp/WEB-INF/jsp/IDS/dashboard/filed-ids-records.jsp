<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:forEach items="${listItems}" var="item">
	<tr>
		<td>${item.idsId}</td>
		 <td>${item.familyId}</td> 
		<td>${item.jurisdiction}</td>
		<td>${item.applicationNo}</td>
		<c:choose>
			<c:when test="${item.filingInstructedOn eq null}">
				<td><div style="text-align: center;">-</div></td>
			</c:when>
			<c:otherwise> 
				<td>${item.filingInstructedBy}<br/><bbx:date dateFormat="MMM dd, yyyy" date="${item.filingInstructedOn}" /></td>
			</c:otherwise>
		</c:choose> 
		<td> $ ${item.filingFee}</td>
		<td>${item.filingChannel}</td>
		<td>${item.status}</td>
		 <td>
			<div class="action-btns-grid">
				<a href="javascript:void(0)" data-target="#notificationLink"
					data-toggle="modal"><i><img
						src="${contextPath}/assets/images/svg/download.svg"
						class="icon16x"></i> IDS</a> <a href="javascript:void(0)"><i><img
						src="${contextPath}/assets/images/svg/download.svg"
						class="icon16x"></i> Filing Package</a>
			</div>
		</td> 
	</tr>
</c:forEach>