<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<input type="hidden" id="recordsTotal" value="${nplListCited.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${nplListCited.totalElements}"/>

<p id="splitter"/>

<c:forEach items="${nplListCited.content}" var="nplData" varStatus="status">
	<tr id="${nplData.refFlowId}" onclick="getSourceDocument(${nplData.refFlowId});">
		<td data-id="${nplData.refFlowId}">
			<input type="hidden" value="${nplData.refEnteredBy}"/>
			<input type="hidden" value="${nplData.kindCode}"/>
			<input type="hidden" value="${nplData.comments}"/>
			<div class="checkbox-without-label">
				<input type="checkbox" checked><label>default</label>
			</div>
		</td>
		<td class="bdr-rt-none">${nplData.citeNo}</td>
		<td class="bdr-rt-none">${nplData.patentNo}</td>
		<td>${nplData.filingDate}</td>
		<td><span class="action-danger">${nplData.status}</span></td>
		<td><a href="javascript:void(0)" class="showMoreDetails">More... 
		<input type="hidden" value="${nplData.refFlowId}" class="refFlowId">
		</a></td>
	</tr>	
</c:forEach>