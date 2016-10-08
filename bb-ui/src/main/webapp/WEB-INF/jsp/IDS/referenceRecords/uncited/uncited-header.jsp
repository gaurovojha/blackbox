<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<div role="tabpanel" class="tab-pane" id="unCited">
	<div class="row">
		<div class="col-sm-8 left-col">
			<div class="form-horizontal">
				<div class="row margin-btm10 text-right">
					<div class="col-sm-4">
						<label class="control-label pull-left">*IDS in Progress</label>
					</div>
					<div class="col-sm-8 reference-cited-btns">
						<button class="btn disabled" id="uncitedDonot">Do Not
							File</button>
						<button class="btn btn-submit" id="uncitedIds">Initiate
							IDS</button>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<div class="panel-group user-management" id="accordionTest"
							role="tablist" aria-multiselectable="true">
							<div class="panel panel-default">
								<div class="panel-heading" role="tab">
									<h4 class="panel-title">
										<span></span> <a role="button" class="collapse"
											data-toggle="collapse" data-parent="#accordionTest"
											href="#collapseTwo" aria-expanded="false"
											aria-controls="collapseTwo" class="collapsed">Patents (${statusCount.uncitedpatentCount})<span
											class="selected"></span><span class="icon icon-arrow-down"></span><span
											class="icon icon-arrow-up"></span>
										</a>
									</h4>
								</div>
								<div id="collapseTwo" class="panel-collapse collapse in"
									role="tabpanel" aria-labelledby="defineRole"
									aria-expanded="false">
									<div class="panel-body">
										<table class="table custom-table mTop10 clickable" id="uncitedPatentTable">
											<thead>
												<tr>
													<th>
														<div class="checkbox-without-label">
															<input type="checkbox"><label>default</label>
														</div>
													</th>
													<th>Jurisdiction</th>
													<th>Patent No.</th>
													<th>Kind Code</th>
													<th>Patentee/Applicant</th>
													<th>View</th>
												</tr>
											</thead>
											<tbody>
												
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<div class="panel panel-default">
								<div class="panel-heading" role="tab">
									<h4 class="panel-title">
										<span></span> <a role="button" class="collapse collapsed"
											data-toggle="collapse" data-parent="#accordionTest"
											href="#uncitedNplTab"> NPL (${statusCount.uncitedNplCount})<span
											class="selected"></span><span class="icon icon-arrow-down"></span><span
											class="icon icon-arrow-up"></span>
										</a>
									</h4>
								</div>
								<div id="uncitedNplTab" class="panel-collapse collapse" aria-expanded="false">
									<div class="panel-body">
										<table class="table custom-table clickable" id="uncitedNplTable">
											<thead>
												<tr>
													<th>
														<div class="checkbox-without-label">
															<input type="checkbox"><label>default</label>
														</div>
													</th>
													<th>Jurisdiction</th>
													<th>Patent No.</th>
													<th>Kind Code</th>
													<th>Patentee/Applicant</th>
													<th>View</th>
												</tr>
											</thead>
											<tbody>												
											</tbody>
										</table>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- <div class="col-sm-4 right-col">
			<div class="clearfix document-reader" id="uncitedSourceDoc">
				<span class="triangle-corner"></span>
				<p class="title">Source Document</p>
				<span class="doc-action-icons"> <a href="javascript:void(0)"
					title="PDF"><i><img src="images/svg/pdf.svg"
							class="icon16x"></i></a> <a href="edit-reference.html"
					title="Edit References"><i><img src="images/svg/edit.svg"
							class="icon16x"></i></a>
				</span>
				<div class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<div class="col-sm-12">
								<label class="control-label">Document Description:</label>
								<div class="form-control-static">Non-final Rejection</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Mailing Date</label>
								<div class="form-control-static">Feb 25, 2016</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Family ID</label>
								<div class="form-control-static">F292456</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Jurisdiction</label>
								<div class="form-control-static">US</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Application #</label>
								<div class="form-control-static">14/832,004</div>
							</div>
						</div>

					</div>
				</div>

			</div>


			<div class="clearfix document-reader-duplicate">

				<div class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<div>
								<p class="heading">Duplicate Sources</p>
							</div>
							<span class="doc-action-icons"> <a
								href="javascript:void(0)" title="PDF"><i><img
										src="images/svg/pdf.svg" class="icon16x"></i></a> <a
								href="javascript:void(0)"><i><img
										src="images/svg/edit.svg" class="icon16x"></i></a>
							</span>
							<div class="col-sm-12">
								<label class="control-label">Document Description:</label>
								<div class="form-control-static">Non-final Rejection</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Mailing Date</label>
								<div class="form-control-static">Mar 20, 2016</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Family ID</label>
								<div class="form-control-static">F2340769</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Jurisdiction</label>
								<div class="form-control-static">US</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Application #</label>
								<div class="form-control-static">13/367,505</div>
							</div>
							<div class="col-sm-6">
								<a href="#" data-target="#duplicateSource" data-toggle="modal">1
									More...</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div> -->
	</div>
</div>

<div id="pageInfo"><jsp:text/></div>