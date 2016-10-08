<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String context = request.getContextPath();
	String js = context + "/assets/js";
	String images = context + "/assets/images";
%>
<div class="row">
	<div class="col-sm-8 left-col">
		<div class="form-horizontal">
			<div class="row margin-btm10" id="citedHeader">
				
			</div>
			<div class="form-group">
				<div class="col-sm-12">
					<div class="panel-group" id="accordion" role="tablist"
						aria-multiselectable="true">
						<div class="panel panel-default">
							<div class="panel-heading" role="tab">
								<h4 class="panel-title">
									<span></span> <a role="button" class="collapse"
										data-toggle="collapse" data-parent="#accordion"
										href="#collapseOne" aria-expanded="false"
										aria-controls="collapseOne" class="collapsed">Patents (<span class="patentsCount">${statusCount.citedpatentCount}</span>)<span
										class="selected"></span><span class="icon icon-arrow-down"></span><span
										class="icon icon-arrow-up"></span>
									</a>
								</h4>
							</div>
							<div id="collapseOne" class="panel-collapse collapse in"
								role="tabpanel" aria-labelledby="defineRole"
								aria-expanded="false">
								<div class="panel-body">
									<table class="table custom-table clickable"	id="citedPatentTable">
										<thead>
											<tr class="noSort">
												<th>
													<div class="checkbox-without-label">
														<input type="checkbox"><label>default</label>
													</div>
												</th>
												<th class="noSort">Jurisdiction</th>
												<th class="noSort">Patent No.</th>
												<th class="noSort">Patentee/Applicant</th>
												<th>IDS Filing<br /> Date
												</th>
												<th>USPTO <br /> Status
												</th>
												<th class="noSort">View</th>
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
										data-parent="#accordion" href="#collapseFour"
										aria-expanded="false" aria-controls="collapseFour"> NPL (
										<span class="nplCount">${statusCount.citedNplCount}</span>)<span class="selected"></span><span
										class="icon icon-arrow-down"></span><span
										class="icon icon-arrow-up"></span>
									</a>
								</h4>
							</div>
							<div id="collapseFour" class="panel-collapse collapse"
								role="tabpanel" aria-expanded="false">
								<div class="panel-body">
									<table class="table custom-table clickable" id="citedNplTable">
										<thead>
											<tr>
												<th>
													<div class="checkbox-without-label">
														<input type="checkbox"><label>default</label>
													</div>
												</th>
												<th>Cite No.</th>
												<th>NPL</th>
												<th>IDS Filing<br /> Date
												</th>
												<th>USPTO <br /> Status
												</th>
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
	<div class="col-sm-4 right-col" id="citeSourceDocumentId">
		<!-- Here source Document will be available -->
	</div>
</div>

<div id="pageInfo"><jsp:text/></div>
