<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<input type="hidden" id="recordsTotal" value="${nplListUnCited.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${nplListUnCited.totalElements}"/>

<p id="splitter"/>

<c:forEach items="${nplListUnCited.content}" var="nplData" varStatus="status">
	<tr>
		<td>
			<div class="checkbox-without-label">
				<input type="checkbox" checked><label>default</label>
			</div>
		</td>
		<td class="bdr-rt-none">${nplData.citeNo}</td>
		<td class="bdr-rt-none">${nplData.patentNo}</td>
		<td>${nplData.filingDate}</td>
		<td><span class="action-danger">${nplData.status}</span></td>
		<td><a href="javascript:void(0)" class="showMore">More...</a></td>
	</tr>	
</c:forEach>