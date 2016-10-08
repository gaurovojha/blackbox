<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="tab-info-text">
		<h2 class="patent-heading">Source References</h2>
</div>
<div class="ref-section step-element">
	<ul class="list-group">
		<li class="list-group-item ">
			<div class="form-horizontal">
				<div class="form-group">
					<div class="col-sm-12">
						<input id="idsInclude" data-refID="-1" type="checkbox"><label
							class="control-label"  for="idsInclude">Include All in
							this IDS</label>
					</div>
				</div>
			</div>
		</li>
		<c:forEach items="${searchResult.items}" var="item" varStatus="status">
		<li class="list-group-item">
			<div class="checkbox-without-label pull-left">
				<input type="checkbox" class="checkboxElem" data-refID="${item.refFlowId}"><label>&nbsp;</label>
			</div>
			<div class="ref-info">${item.npl}</div>
		</li>
		</c:forEach>
	</ul>
</div>