<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="recordsTotal" value="${nplListDeleted.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${nplListDeleted.totalElements}"/>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<p id="splitter"/>

<c:forEach items="${nplListDeleted.content}" var="delNplData" varStatus="status">
	<tr>
		<td>
			<div class="checkbox-without-label">
				<input type="checkbox" checked><label>default</label>
			</div>
		</td>
		<td class="bdr-rt-none">${delNplData.citeNo}</td>
		<td class="bdr-rt-none">${delNplData.patentNo}</td>
		<td>${delNplData.filingDate}</td>
		<td><span class="action-danger">${delNplData.status}</span></td>
		<td><a href="javascript:void(0)" class="showMore">More...</a></td>
	</tr>	
</c:forEach>