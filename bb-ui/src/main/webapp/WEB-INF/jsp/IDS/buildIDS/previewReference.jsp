<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pathimg" value="${contextPath}/assets/images/svg"
	scope="request" />

	<div class="panel-group user-management" id="accordion" role="tablist" aria-multiselectable="true">
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="UsPatents">
				<h4 class="panel-title">
					<span></span> <a role="button"  data-toggle="collapse"
						class="collapsed" data-parent="#accordion" href="#collapseOne"
						aria-expanded="false" aria-controls="collapseOne"> US Patents
						(${refCounts['PUS']}) 
							<small>
						<c:if test="${refCountsNew['PUS'] >0}">
						<span class="action-success new-notify"><i><img src="${pathimg }/bulb.svg" class="icon16x"></i> ${refCountsNew['PUS']} New</span>
						</c:if>
						</small>
						<span class="selected"></span><span
						class="icon icon-arrow-down"></span><span
						class="icon icon-arrow-up"></span>
					</a>
				</h4>
			</div>
			<div id="collapseOne" class="panel-collapse collapse" role="tabpanel"
				aria-labelledby="UsPatents" aria-expanded="true">
				<div class="panel-body">
					<div class="previewIDS"></div>
				</div>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="UsPublication">
				<h4 class="panel-title">
					<span></span> <a role="button" class="collapsed"
						data-toggle="collapse" data-parent="#accordion"
						href="#collapseTwo" aria-expanded="false"
						aria-controls="collapseTwo"> US Publication (${refCounts['US_PUBLICATION']}) 
						<small>
						<c:if test="${refCountsNew['US_PUBLICATION'] >0}">
						<span class="action-success new-notify"><i><img src="${pathimg }/bulb.svg" class="icon16x"></i> ${refCountsNew['US_PUBLICATION']} New</span>
						</c:if>
						</small>
						<span class="selected"></span><span class="icon icon-arrow-down"></span><span
						class="icon icon-arrow-up"></span>
					</a>
				</h4>
			</div>
			<div id="collapseTwo" class="panel-collapse collapse" role="tabpanel"
				aria-labelledby="US Publication" aria-expanded="false"
				style="height: 0px;">
				<div class="panel-body">
					<div class="previewIDS"></div>
				</div>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="foreignPatents">
				<h4 class="panel-title">
					<span></span>
					<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree" class="collapsed"> 
						Foreign	Patents (${refCounts['FP']}) <small><span class="asterisk">*</span> of these, 6 have attachment
						<c:if test="${refCountsNew['FP'] >0}">
						<span class="action-success new-notify"><i><img src="${pathimg }/bulb.svg" class="icon16x"></i> ${refCountsNew['FP']} New</span>
						</c:if>
						 </small>
						
						
						
						
						 <span class="selected"></span>
					 	<span class="icon icon-arrow-down"></span>
					 	<span class="icon icon-arrow-up"></span>
					</a>
				</h4>
			  </div>
			<div id="collapseThree" class="panel-collapse collapse"	role="tabpanel" aria-labelledby="foreignPatents" aria-expanded="false" style="height: 0px;">
				<div class="panel-body">
					<div class="previewIDS"></div>
				</div>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="npl">
				<h4 class="panel-title">
					<a class="collapsed" role="button" data-toggle="collapse"
						data-parent="#accordion" href="#collapseFour"
						aria-expanded="false" aria-controls="collapseFour"> NPL (${refCounts['NPL']}) <small>
						
						  
							
						   <span class="asterisk">*</span> of these, 5 have attachment
						   	<c:if test="${refCountsNew['NPL'] >0}">
						   <span class="action-success new-notify"><i><img src="${pathimg }/bulb.svg" class="icon16x"></i> ${refCountsNew['NPL']} New</span>
						   </c:if>
						   </small>
							<span class="selected"></span><span class="icon icon-arrow-down"></span><span
						   class="icon icon-arrow-up"></span>
					</a>
				</h4>
			</div>
			<div id="collapseFour" class="panel-collapse collapse"
				role="tabpanel" aria-labelledby="npl" aria-expanded="false"
				style="height: 0px;">
				<div class="panel-body">
					<div class="previewIDS"></div>
				</div>
			</div>
		</div>

	</div>
	
<%-- <c:set value="${refCounts}" var="typeCount"></c:set>
<c:forEach var="hash" items="${refCounts}">
        <c:set value="${if((hash.key eq 'FP'))then hash.value}" var="a"></c:set>
  </c:forEach> --%>
  <%-- 
<div>${testMap} - ${testMap['FP']}, ${refCounts}=${refCounts.FP} - ${refCounts['PUS']}-${refCounts.PUS}-${refCounts['PUS']}</div>
cout=<c:out value="${refCounts['PUS']}"/>
<div>hello <c:out value='${typeCount["FP"]}'></c:out> </div>
loop
<c:out value="${a}"></c:out> --%>
<%-- 
<c:forEach var="hash" items="${refCounts}">
        <option><c:out value="${hash.key}" var="${hash.key}"/></option>
  </c:forEach> --%>
<%-- <c:forEach var="country" items="${resultRecords}">
	Country: ${country.key}  - Capital: ${country.value}
	<c:if test="${country.key  eq 'FP'}">
	Saurabh: ${country.key}  - Capital: ${country.value}
	</c:if>
</c:forEach> --%>