<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="recordsTotal" value="${patentListDoNotFile.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${patentListDoNotFile.totalElements}"/>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<p id="splitter"/>

<c:forEach items="${patentListDoNotFile.content}" var="patentData" varStatus="status">
	<tr>
		<td>
			<input type="hidden" value="${patentData.refEnteredBy}"/>
			<input type="hidden" value="${patentData.kindCode}"/>
			<input type="hidden" value="${patentData.comments}"/>
			<div class="checkbox-without-label">
				<input type="checkbox"><label>default</label>
			</div>
		</td> 
		<td class="bdr-rt-none">${patentData.jurisdiction}</td>
		<td class="bdr-rt-none">${patentData.patentNo}</td>
		<td>${patentData.patentee}</td>
		<td>${patentData.filingDate}</td>
		<td><span class="action-danger">${patentData.status}</span></td>
		<td><a href="javascript:void(0)" class="showMoreDetails">More...</a></td>
	</tr>	
</c:forEach>