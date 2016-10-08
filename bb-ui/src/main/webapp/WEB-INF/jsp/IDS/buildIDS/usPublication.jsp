<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="step-element">
	<!--<div>buttons</div>-->
	<c:if test="${module eq 'buildIDS'}">
	<div class="tab-info-text">
		<h4 class="pull-left">
			<a class="Url-referesh" href="javascript:void(0)"><span
				class="glyphicon glyphicon-refresh"></span> </a> US Publication (${searchResult.recordsTotal})
		</h4>
		<a href="javascript:void(0)" class="selfCitationBtn" ><button class="btn btn-submit pull-right" >Add  Self-citation</button></a>
	</div>
</c:if>
	<input type="hidden" value="${searchResult.recordsTotal}" class="totalRecords">
	<table class="table custom-table">
		<thead>
			<tr>
				<th>Examiner Initials</th>
				<th>Cite No.</th>
				<th>Publication #</th>
				<th>Kind Code</th>
				<th>Publication Date</th>
				<th>Name of Patentee or...</th>
				<th>Pages,Columns,Lines...</th>
		<c:if test="${module eq 'buildIDS'}">
				<th class="highlighted-col">Review Status</th>
				<th class="highlighted-col">Action</th>
				</c:if>
		<c:if test="${module eq 'attorney'}">
		        <th class="highlighted-col">Review Source <br/> Document</th>    
                <th class="highlighted-col"><div class="checkbox-without-label"><input type="checkbox"><label>default</label></div></th>

		</c:if>
			</tr>
		</thead>
		<tbody>
		<jsp:include page="uspatentTableRow.jsp"></jsp:include>
		</tbody>
	</table>
	<div class="text-right form-group"><c:if test="${module eq 'buildIDS'}"><span>Showing <span class="recordsFiltered">${searchResult.recordsFiltered}</span> of <span class="recordsTotal">${searchResult.recordsTotal}</span> records. </span></c:if><a class="show-more" href="javascript:void(0)">Show more...</a></a></div>
	<%-- <div class="text-right form-group"><span>Showing ${searchResult.recordsFiltered} of ${searchResult.recordsTotal} Records. </span><a class="show-more" href="javascript:void(0)">Show more...</a></a></div> --%>
</div>