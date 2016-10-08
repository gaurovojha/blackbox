<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- MDM: my dashboard -->

<div class="main-content container">
	<div class="clearfix">
		<ul class="content-links pull-left">
			<li><a href=""><span class="icon icon-doc"></span>New
					Application</a></li>
		</ul>
		<div class="pull-right">
			<div class="switch-control mdm">
				<label class="switch"> <input type="checkbox"
					class="switch-input" checked="checked"> <span
					class="switch-label" data-on="Application View"
					data-off="Family View"></span> <span class="switch-handle"></span>
				</label>
			</div>
		</div>
	</div>
	<div class="tab-container">

		<ul class="tab-actions pull-right">
			<li>
				<div class='input-group date' id='datetimepicker3'>
					<input type='text' class="form-control" value="Showing: Till Date" />
					<span class="input-group-addon"> <span
						class="icon icon-calendar" data-alt="calendar"></span>
					</span>
				</div>
				<div class="input-group date-range-picker hide">
					<input type="text" id="config-demo" class="form-control date-input"
						value="Sep 01, 2015 - Sep 01, 2015"> <span
						class="input-group-btn"><span class="icon icon-calendar"
						data-alt="calendar"></span></span>
				</div>
			</li>
			<li><a href=""><span class="icon icon-export"></span>Export</a></li>
		</ul>
		<!-- Nav tabs -->
		<ul class="nav nav-tabs custom-tabs" role="tablist">
			<li role="presentation" class="active"><a href="#myRecordsTab"
				role="tab" data-toggle="tab">My Records</a></li>
			<li role="presentation"><a href="#allRecordsTab" role="tab"
				data-toggle="tab">All Records</a></li>
			<li role="presentation"><a href="#inactiveRecordsTab" role="tab"
				data-toggle="tab">Inactive Records</a></li>
		</ul>
		<!-- Tab panes -->
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="myRecordsTab">
				<jsp:include page="dashboard-active-appview-header.jsp" />
				<jsp:include page="dashboard-active-familyview-header.jsp" />
				
			</div>
			<div role="tabpanel" class="tab-pane" id="allRecordsTab">
				<jsp:include page="dashboard-active-appview-header.jsp" />
				<jsp:include page="dashboard-active-familyview-header.jsp" />
			</div>
			<div role="tabpanel" class="tab-pane" id="inactiveRecordsTab">
				<jsp:include page="dashboard-inactive-header.jsp" />
			</div>
		</div>

	</div>
</div>


