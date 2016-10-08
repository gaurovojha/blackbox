<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input type="hidden" value="${searchResult.recordsTotal}" class="totalRecords">
<div class="step-element">
	<!--<div>buttons</div>-->
	<c:if test="${module eq 'buildIDS'}">
		<div class="tab-info-text">
			<h4 class="pull-left">
				<a class="Url-referesh" href="javascript:void(0)"><span
					class="glyphicon glyphicon-refresh"></span> </a> US Patents (${searchResult.recordsTotal})
			</h4>
			<a href="javascript:void(0)" class="selfCitationBtn"><button
					class="btn btn-submit pull-right">Add Self-citation</button></a>
		</div>
	</c:if>
	<table class="table custom-table">
		<thead>
			<tr>
				<th>Examiner Initials</th>
				<th>Cite No.</th>
				<th>Patent #</th>
				<th>Kind Code</th>
				<th>Issue Date</th>
				<th>Name of Patentee or...</th>
				<th>Pages,Columns,Lines...</th>
				<c:if test="${module eq 'buildIDS'}">
					<th class="highlighted-col">Reviewed By</th>
					<th class="highlighted-col">Action</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<jsp:include page="uspatentTableRow.jsp"></jsp:include>
		</tbody>
		<tbody></tbody>
	</table>
	<div class="text-right form-group">
		<c:if test="${module eq 'buildIDS'}">
			<span>Showing <span class="recordsFiltered">${searchResult.recordsFiltered}</span>
				of <span class="recordsTotal">${searchResult.recordsTotal}</span>
				records.
			</span>
		</c:if>
		<a class="show-more" href="javascript:void(0)">Show more...</a></a>
	</div>
	<!-- <div class="text-right form-group"><span>Showing 10 of 20 Records. </span><a class="show-more" href="javascript:void(0)">Show more...</a></a></div> -->

</div>
