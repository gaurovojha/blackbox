<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div role="tabpanel" class="tab-pane active" id="InitiateIDStab">
	<div>
		<ul class="tab-actions pull-right">
			<li>
				<p>Legend: OA (Office Action)</p>
			</li>
		</ul>

		<ul class="nav nav-tabs custom-tabs inner" role="tablist">
			<li role="presentation" class="active"><a
				href="#initiateIdsHigh" role="tab" data-toggle="tab" id="lnkUrgentIDS">Urgent</a></li>
			<li role="presentation"><a href="#initiateIdsMedium" role="tab"
				data-toggle="tab" id="lnkAllIDS">All Records</a></li>
		</ul>

		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="initiateIdsHigh">
				<div class="tab-info-text">
					<h4>List of Records (10)</h4>
				</div>

				<jsp:include page="initiateIDS-urgent-header.jsp"></jsp:include>

			</div>
			<div role="tabpanel" class="tab-pane" id="initiateIdsMedium">
				<div class="tab-info-text">
					<h4>List of Records (10)</h4>
				</div>
				<jsp:include page="initiateIDS-all-header.jsp"></jsp:include>
			</div>
		</div>
	</div>
</div>