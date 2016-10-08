<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context + "/assets/js";
	String images = context+"/assets/images";
%>

	<sec:authorize access="canAccessUrl('/reference/management/allRecords')" var="allRecords"/>
	<sec:authorize access="canAccessUrl('/reference/management/pendingRecords')" var="pendingRecords"/>

<div class="main-content container">

	<c:if test="${allRecords || pendingRecords}">
	
		<div class="tab-container">
		    <ul class="tab-actions pull-right">
		        <li>
		            <div class="daterange-picker tab">
	         				<input id="dateRangeFilter" type="text" class="form-control date"  name="datefilter" value="Showing till date">
						<span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
	         			</div>
		        </li>
		        <li><a id="exportData" href="javascript:void(0);"><i data-alt="calendar"><img src="<%=images%>/svg/export.svg" class="icon20x"></i> Export</a></li>
		    </ul>
		    
		    <c:choose >
				<c:when test="${referenceSubMenu eq 'allRecords'}">
					<c:set var="activeAllRecords" value="active"/>
					<c:set var="activePendingRecords" value=""/>
				</c:when>
				<c:otherwise>
					<c:set var="activeAllRecords" value=""/>
					<c:set var="activePendingRecords" value="active"/>
				</c:otherwise>
			</c:choose>
			
		    <ul class="nav nav-tabs custom-tabs" role="tablist">
		    	<c:if test="${allRecords}">
					<li id="allRecordsTab" role="presentation" class="${activeAllRecords}">
						<a class="navigate" id="showAllRecords" url="${pageContext.request.contextPath}/reference/management/allRecords" targetId='referenceSubMenu' href="#allRecords" role="tab" data-toggle="tab">All Records </a>
					</li>
				</c:if>	
				<c:if test="${pendingRecords}">
					<li id="pendingReferenceTab" role="presentation" class="${activePendingRecords}">
						<a class="navigate" id="showPendingReferences" url="${pageContext.request.contextPath}/reference/management/pendingRecords" targetId='referenceSubMenu' href="#pendingReferenceEntry" role="tab" data-toggle="tab">Reference Entry in Progress</a>
					</li>
				</c:if>	
			</ul>
			
			<c:if test="${referenceSubMenu ne ''}">
				<div class="tab-content">
				    <div class="col-md-12">
				        <div class="col-md-7">
				        
				            <span><strong>List of all Documents </strong></span>
				        </div>
				        <div class="col-md-5 text-right">
				            <span><strong>Jurisdiction: </strong> 
				                <select id="select-jurisdiction">
				                    <option selected value="ALL">All</option>
				                    <option value="US">US</option>
				                    <option value="OTHER">Non-US</option>
				                </select>
				            </span>
				            <span class="my-notificatoins">
				                <label><input type="checkbox" id="myDocumentsOnly">Show My Documents Only</label>
				        	</span>
				        </div>
				    </div>
				    
					<div role="tabpanel" class="tab-pane ${activeAllRecords}" id="allRecords">
						<jsp:include page="all-records-header.jsp"></jsp:include>
					</div>

					<div role="tabpanel" class="tab-pane ${activePendingRecords}" id="pendingReferenceEntry">
						<jsp:include page="pending-reference-list-header.jsp"></jsp:include>
					</div>
				</div>
			</c:if>	
		</div>
	</c:if>	
</div>

<script type="text/javascript" src="<%=js%>/reference/reference-management.js"></script>
<%-- <script type="text/javascript" src="<%=js%>/reference/reference.js"></script> --%>
<jsp:include page="../scripts/reference-data-table.jsp"></jsp:include>