<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<input type="hidden" id="patentCount" value="${headerCount.patentsCount}">
<input type="hidden" id="nplCount" value="${headerCount.nplCount}">

	<div class="col-sm-5 form-inline">
		<label class="control-label">Showing</label> <select
			class="form-control" id="iFilterDate">
			<option>All IDS</option>
			<c:forEach begin="1" items="${filingDate}" var="entry">
    			<option value="${entry.key}">${entry.key} (${entry.value})</option>
			</c:forEach>
		</select>
	</div>
	<div class="col-sm-7 text-right">
		<div class="dropdown grid-dropdown form-control-static">
			<a id="drop4" href="#" class="dropdown-toggle" data-toggle="dropdown"
				role="button" aria-haspopup="true" aria-expanded="false"><img
				src="<%=images%>/svg/change-status.svg" class="icon20x">
				Change Status <span class="caret"></span> </a>
			<ul id="menu1" class="dropdown-menu" aria-labelledby="drop4">
				<c:forEach begin="1" items="${changeStatus}" var="substatus" varStatus="status">
					<li><a href="#">${substatus}</a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
