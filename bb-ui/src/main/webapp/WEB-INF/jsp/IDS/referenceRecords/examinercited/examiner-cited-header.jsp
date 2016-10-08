<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<div class="row">
	<div class="col-sm-8 left-col">
		<div class="form-horizontal">
			<div class="form-group">
				<div class="col-sm-12">
					<div class="panel-group user-management" id="accordionExam"
						role="tablist" aria-multiselectable="true">
						<div class="panel panel-default">
							<div class="panel-heading" role="tab">
								<h4 class="panel-title">
									<span></span> <a role="button" class="collapse"
										data-toggle="collapse" data-parent="#accordionExam"
										href="#collapseThree" aria-expanded="false"
										aria-controls="collapseOne" class="collapsed">Patents (2)<span
										class="selected"></span><span class="icon icon-arrow-down"></span><span
										class="icon icon-arrow-up"></span>
									</a>
								</h4>
							</div>
							<div id="collapseThree" class="panel-collapse in" role="tabpanel"
								aria-labelledby="defineRole" aria-expanded="false">
								<div class="panel-body">
									<table class="table custom-table mTop10 clickable" id="examinerCitedPatentTable">
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
										data-parent="#accordionExam" href="#collapseFour"
										aria-expanded="false" aria-controls="collapseFour"> NPL
										(1) <span class="selected"></span><span
										class="icon icon-arrow-down"></span><span
										class="icon icon-arrow-up"></span>
									</a>
								</h4>
							</div>
							<div id="collapseFour" class="panel-collapse collapse"
								role="tabpanel" aria-expanded="false">
								<div class="panel-body">
									<table class="table custom-table mTop10 clickable" id="examinerCitedNplTable">
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
				<div>
					<div class="col-sm-12">
						<label class="control-label">Document Description:</label>
						<div class="form-control-static">Non-final Rejection</div>
					</div>
					<div class="col-sm-6">
						<label class="control-label">Mailing Date</label>
						<div class="form-control-static">Nov 20, 2015</div>
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
						<div class="form-control-static">14/800,234</div>
					</div>
					<div class="col-sm-12 devider"></div>
				</div>

			</div>


		</div>
		<div class="clearfix document-reader-duplicate">

			<div class="form-horizontal">
				<div class="form-group">

					<div class="col-sm-12">
						<div>
							<p class="heading">Reference Flow</p>
						</div>
						<div class="col-sm-12 mTop10">
							<a href="#">Cited in IDS (Dec 10, 2015) in US 14/832,004
								(F292456) <i><img src="images/svg/external-link.svg"
									class="icon16x"></i>
							</a>
						</div>
						<div class="col-sm-12 mTop10">
							<a href="#">Uncited in US 13/367,505 (F2340769) <i><img
									src="images/svg/external-link.svg" class="icon16x"></i></a>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>
<div id="pageInfo"><jsp:text/></div>