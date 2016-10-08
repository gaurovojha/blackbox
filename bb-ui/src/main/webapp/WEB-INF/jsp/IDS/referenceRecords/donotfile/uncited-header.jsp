<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<div class="row">
	<div class="col-sm-8 left-col">
		<div class="form-horizontal">
			<div class="row margin-btm10 text-right">
				<div class="col-sm-12 reference-cited-btns">
					<button class="btn btn-submit disabled">Include in IDS</button>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-12">
					<div class="panel-group user-management" id="accordion3"
						role="tablist" aria-multiselectable="true">
						<div class="panel panel-default">
							<div class="panel-heading" role="tab">
								<h4 class="panel-title">
									<span></span> <a role="button" class="collapse"
										data-toggle="collapse" data-parent="#accordion3"
										href="#collapseSeven" aria-expanded="false"
										aria-controls="collapseTwo" class="collapsed">Patents (2)<span
										class="selected"></span><span class="icon icon-arrow-down"></span><span
										class="icon icon-arrow-up"></span>
									</a>
								</h4>
							</div>
							<div id="collapseSeven" class="panel-collapse collapse in"
								role="tabpanel" aria-labelledby="defineRole"
								aria-expanded="false">
								<div class="panel-body">
									<table class="table custom-table mTop10 clickable" id="doNotFilePatentTable">
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
									<a class="collapsed" role="button" data-toggle="collapse"
										data-parent="#accordion3" href="#collapseEight"
										aria-expanded="false" aria-controls="collapseEight"> NPL
										(1) <span class="selected"></span><span
										class="icon icon-arrow-down"></span><span
										class="icon icon-arrow-up"></span>
									</a>
								</h4>
							</div>
							<div id="collapseEight" class="panel-collapse collapse"
								role="tabpanel" aria-expanded="false">
								<div class="panel-body">
									<table class="table custom-table mTop10 clickable" id="doNotFileNplTable">
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
	<div class="col-sm-4 right-col">
		<div class="clearfix document-reader">
			<span class="triangle-corner"></span>
			<p class="title">Source Document</p>
			<span class="doc-action-icons"> <a href="javascript:void(0)"
				title="PDF"><i><img src="images/svg/pdf.svg" class="icon16x"></i></a>
				<a href="edit-reference.html" title="Edit References"><i><img
						src="images/svg/edit.svg" class="icon16x"></i></a>
			</span>
			<div class="form-horizontal">
				<div class="form-group">
					<div class="col-sm-12">
						<div class="col-sm-12">
							<label class="control-label">Document Description:</label>
							<div class="form-control-static">Search Report</div>
						</div>
						<div class="col-sm-6">
							<label class="control-label">Mailing Date</label>
							<div class="form-control-static">Jul 22, 2015</div>
						</div>
						<div class="col-sm-6">
							<label class="control-label">Family ID</label>
							<div class="form-control-static">F292456</div>
						</div>
						<div class="col-sm-6">
							<label class="control-label">Jurisdiction</label>
							<div class="form-control-static">WO</div>
						</div>
						<div class="col-sm-6">
							<label class="control-label">Application #</label>
							<div class="form-control-static">PCT/JP15/04534</div>
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
							href="javascript:void(0)">PDF</a> <a href="javascript:void(0)"><i><img
									src="images/svg/edit.svg" class="icon16x"></i></a>
						</span>
						<div class="col-sm-12">
							<label class="control-label">Document Description:</label>
							<div class="form-control-static">Non-final Rejection</div>
						</div>
						<div class="col-sm-6">
							<label class="control-label">Mailing Date</label>
							<div class="form-control-static">Dec 2, 2015</div>
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
						<div class="col-sm-6">
							<a href="#">20 More...</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="pageInfo"><jsp:text/></div>