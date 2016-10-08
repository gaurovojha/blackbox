<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--<div>buttons</div>-->

<div class="step-element">
<c:if test="${module eq 'buildIDS'}">
	<div class="tab-info-text">
		<h4 class="pull-left">
			<a class="Url-referesh" href="javascript:void(0)"><span
				class="glyphicon glyphicon-refresh"></span> </a> NPL (${searchResult.recordsTotal})
		</h4>
		<a href="javascript:void(0)" class="selfCitationBtn" ><button class="btn btn-submit pull-right" >Add  Self-citation</button></a>
		<button class="btn btn-submit pull-right mRight10" data-target="#oaNpl" data-toggle="modal">Add OA as NPL</button>
	</div>
	</c:if>
	<input type="hidden" value="${searchResult.recordsTotal}" class="totalRecords">
	<table class="table custom-table">
		<thead>
			<tr>
				<th>Examiner <br>Initials
				</th>
				<th>Cite No.</th>
				<th>Include name of the author (in CAPITAL LETTERS), title of
					the article (when appropriate),<br> title of the item (book,
					magazine, journal, serial, symposium, catalog, etc), date,
					pages(s),<br> volume-issue number(s), publisher, city and/or
					country where published
				</th>
				<th class="text-center">T</th>
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
			<jsp:include page="nplTableRow.jsp"></jsp:include>
		</tbody>
	</table>
	
	<div class="text-right form-group">
		<c:if test="${module eq 'buildIDS'}">
			<span>Showing <span class="recordsFiltered">${searchResult.recordsFiltered}</span>
				of <span class="recordsTotal">${searchResult.recordsTotal}</span> records.
			</span>
		</c:if>
		<a class="show-more" href="javascript:void(0)">Show more...</a>
	</div>
</div>
