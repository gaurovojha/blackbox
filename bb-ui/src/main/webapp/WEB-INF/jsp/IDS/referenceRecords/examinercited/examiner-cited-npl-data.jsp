<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="recordsTotal" value="${nplListExaminerCited.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${nplListExaminerCited.totalElements}"/>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<p id="splitter"/>

<c:forEach items="${nplListExaminerCited.content}" var="exmNplData" varStatus="status">
	<tr>
		<td>
			<div class="checkbox-without-label">
				<input type="checkbox" checked><label>default</label>
			</div>
		</td>
		<td class="bdr-rt-none">${exmNplData.citeNo}</td>
		<td class="bdr-rt-none">${exmNplData.patentNo}</td>
		<td>${exmNplData.filingDate}</td>
		<td><span class="action-danger">${exmNplData.status}</span></td>
		<td><a href="javascript:void(0)" class="showMore">More...</a></td>
	</tr>	
</c:forEach>