<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="recordsTotal" value="${nplListCitedInParent.totalElements}" />
<input type="hidden" id="recordsFiltered" value="${nplListCitedInParent.totalElements}"/>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<p id="splitter"/>

<input type="hidden" id="nplCount" value="${headerCount.nplCount}">
<c:forEach items="${nplListCitedInParent.content}" var="nplData" varStatus="status">
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