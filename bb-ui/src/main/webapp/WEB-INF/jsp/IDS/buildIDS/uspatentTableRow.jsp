<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="startIndex" scope="page" value="${offset}" />
<c:set var="viewpermission" scope="session" value="${2000*2}"/>
<c:forEach items="${searchResult.items}" var="item" varStatus="status">
	<tr class="${(status.count mod 2 == 0) ? 'even' : 'odd'}">
		<td></td>
		<td><c:out value="${startIndex+1}" />
			<c:set var="startIndex" scope="page" value="${startIndex+1}" /></td>
		<td>${item.patentNo}</td>
		<td>${item.kindCode}</td>
		<td>${item.publishedOn}</td>
		<td>${item.patentee}</td>
		<td>${item.comments}</td>
		<c:if test="${module eq 'buildIDS'}">
		<td>${item.reviewedBy}</td>
		<td>
			<div class="action-btns-grid">
				<a href="javascript:void(0)"><i><img src="${contextPath}/assets/images/svg/review-doc.svg" class="icon16x"></i> Review</a>
				<div class="dropdown grid-dropdown">
					<a id="drop4" href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false"><img src="${contextPath}/assets/images/svg/drop.svg"
						class="icon20x"> <span class="action-danger">Drop</span> <span
						class="caret"></span> </a>
					<ul id="menu1" class="dropdown-menu" aria-labelledby="drop4">
						<li><a class="drop-all" data-RefID="${item.refFlowId}" href="#" data-toggle="modal" data-target="#dropRefAllIdsPopUp">Do Not File</a></li>
						<li><a class="drop-all" data-RefID="${item.refFlowId}" href="#" data-toggle="modal" data-target="#dropRefFromThisIdsPopUp">Drop from this IDS</a></li>
					</ul>
				</div>
			</div>
		</td>
		</c:if>
	<c:if test="${module eq 'attorney'}">
	<td class="text-center"><a href="ids-review.html"><i><img src="images/svg/review-doc.svg" class="icon16x" alt="Review Source Document"></i></a></td>
    <td><div class="checkbox-without-label"><input type="checkbox" data-id="${item.refFlowId}" class="activeInactiveBtns"><label>default</label></div></td>
	</c:if>
	</tr>
</c:forEach>