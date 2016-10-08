<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<input type="hidden" id="idsFillingInfoId" value="${idsFilingInfoId}" />


<div class="main-content container">
	<form:form commandName="idsUpdateRefForm" method="post"
		id="formApplication" action="${contextPath}/ids/userAction/updateRefStatus/submit"
		class="form-horizontal">
		<form:hidden path="idsId" id="idsId" value="${idsId}" />
		<form:hidden path="notificationProcessId" id="notificationProcessId" value="${notificationProcessId}" />
		<div class="row">
			<div class="col-sm-12">
				<div class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<div class="panel-group user-management" id="accordion"
								role="tablist" aria-multiselectable="true">
								<div class="panel panel-default">
									<div class="panel-heading" role="tab">
										<h4 class="panel-title">
											<span></span> <a role="button" data-toggle="collapse"
												data-parent="#accordion" href="#collapseOne"
												aria-expanded="false" aria-controls="collapseOne" id="lnkUs">US
												Patents (${refCount['PUS']})<span class="selected"></span><span
												class="icon icon-arrow-down"></span><span
												class="icon icon-arrow-up"></span>
											</a>
										</h4>
									</div>
									
									<!-- US Patents -->
									<div id="collapseOne" class="panel-collapse collapse in"
										role="tabpanel" aria-labelledby="defineRole"
										aria-expanded="false">
										<div class="panel-body">
											<table class="table custom-table">
												<thead>
													<tr>
														<th>Publication #</th>
														<th>Kind Code</th>
														<th>Patentee / Applicant</th>
														<th>Current Status</th>
														<th>Update Reference Status</th>
													</tr>
												</thead>
												<tbody id="refUsBody">
													<c:forEach items="${items}" var="app" varStatus="status">

														<form:hidden path="usPatentReference[${status.index}].refFlowId" id="refFlowId" value="${app.refFlowId}" />
														<tr>
															<td>${app.publicationNo}</td>
															<td>${app.kindCode}</td>
															<td>${app.applicant }</td>
															<td>${app.currentStatus}/${app.currentSubStatus}</td>

															<td>
															<c:choose>
															<c:when test="${app.currentStatus eq 'CITED' and app.currentSubStatus eq 'PENDING_USPTO_FILING'}">
																<form:checkbox
																	path="usPatentReference[${status.index}].notCited"
																	id="nocitedRadio" /> <label
																for="nocitedRadio" class="control-label">Not
																	Cited </label> 
																	<form:checkbox
																	path="usPatentReference[${status.index}].cited" id="citedIDSRadio"
																	class="citedIDSRadio" /><label for="citedIDSRadio"
																class="control-label">Cited in IDS </label>
																<div class="inline-block form-inline idsFillingDate">
																	<div class="input-group date datepicker"
																		id="datetimepicker">
																		<form:input path="usPublicationReference[${status.index}].filingDate" type="text" class="form-control"
																			placeholder="IDS Filing Date" />
																		<span class="input-group-addon"> <i
																			class="glyphicon glyphicon-calendar"></i>
																		</span>
																	</div>
																</div>
															</c:when>
															<c:when test="${app.currentStatus eq 'CITED' and app.currentSubStatus eq 'PENDING_1449' or app.currentSubStatus eq 'REJECTED'}">
																<form:checkbox path="usPatentReference[${status.index}].mappedInIDS" 
																	id="filedIDS" value="checked"/> <label for="filedIDS"
																class="control-label">Mapped to a filed IDS </label>
															</c:when>
															</c:choose>
															
															 </td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</div>
								</div>
								<div class="panel panel-default">
									<div class="panel-heading" role="tab">
										<h4 class="panel-title">
											<span></span> <a role="button" data-toggle="collapse"
												class="collapsed" data-parent="#accordion"
												href="#usPublications" aria-expanded="false"
												aria-controls="usPublications" id="lnkUsPub">US
												Publications (${refCount['US_PUBLICATION']})<span
												class="selected"></span><span class="icon icon-arrow-down"></span><span
												class="icon icon-arrow-up"></span>
											</a>
										</h4>
									</div>
									<div id="usPublications" class="panel-collapse collapse"
										role="tabpanel" aria-labelledby="defineRole"
										aria-expanded="false">
										<div class="panel-body">
											<table class="table custom-table">
												<thead>
													<tr>
														<th>Publication #</th>
														<th>Kind Code</th>
														<th>Patentee / Applicant</th>
														<th>Current Status</th>
														<th>Update Reference Status</th>
													</tr>
												</thead>
												<!-- US PUBLICATION references will go here -->
												<tbody id="refUsPubBody">
												</tbody>
											</table>
										</div>
									</div>
								</div>
								<div class="panel panel-default">
									<div class="panel-heading" role="tab">
										<h4 class="panel-title">
											<span></span> <a role="button" data-toggle="collapse"
												class="collapsed" data-parent="#accordion"
												href="#foreignTab" aria-expanded="false"
												aria-controls="foreignTab" id="lnkFp">Foreign
												(${refCount['FP']})<span class="selected"></span><span
												class="icon icon-arrow-down"></span><span
												class="icon icon-arrow-up"></span>
											</a>
										</h4>
									</div>
									<div id="foreignTab" class="panel-collapse collapse"
										role="tabpanel" aria-labelledby="defineRole"
										aria-expanded="false">
										<div class="panel-body">
											<table class="table custom-table">
												<thead>
													<tr>
														<th>Jurisdiction</th>
														<th>Publication #</th>
														<th>Kind Code</th>
														<th>Patentee / Applicant</th>
														<th>Current Status</th>
														<th>Update Reference Status</th>
													</tr>
												</thead>
												<!-- FP patents will go here -->
												<tbody id="refFpBody">
												</tbody>
											</table>
										</div>
									</div>
								</div>

								<div class="panel panel-default">
									<div class="panel-heading" role="tab">
										<h4 class="panel-title">
											<a class="collapsed" role="button" data-toggle="collapse"
												data-parent="#accordion" href="#collapseThree"
												aria-expanded="false" aria-controls="collapseThree"
												id="lnkNpl"> NPL (${refCount['NPL']})<span
												class="selected"></span><span class="icon icon-arrow-down"></span><span
												class="icon icon-arrow-up"></span>
											</a>
										</h4>
									</div>
									<div id="collapseThree" class="panel-collapse collapse"
										role="tabpanel" aria-labelledby="accessProfile"
										aria-expanded="false">
										<div class="panel-body">
											<table class="table custom-table">
												<thead>
													<tr>
														<th>NPL</th>
														<th>Current Status</th>
														<th>Update Reference Status</th>
													</tr>
												</thead>
												<tbody id="refNplBody">
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="form-horizontal">
						<div class="divider"></div>
						<div class="col-sm-12">
							<div class="form-group  text-left">
								<form:button type="button" class="btn btn btn-cancel">Cancel</form:button>
								<form:button type="submit" class="btn btn-submit">Update reference Status</form:button>
							</div>
						</div>
					</div>


				</div>
			</div>
		</div>
	</form:form>
</div>

<jsp:include page="../scripts/admin.jsp" />
<jsp:include page="../scripts/reference-update.jsp" />