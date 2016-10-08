<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>
	<form:form commandName="roleForm" method="post" action="update">

		<form:hidden path="id" />
		<form:hidden path="version"/>
		
		<div class="main-content container">
			<div class="row">
				<div class="col-sm-12">
					<div class="page-header">
						<span class="pull-right form-fields-tip">All <span class="asterisk">*</span> <spring:message code="role.page.fields.mandatory.message" /></span>
						<h2 class="page-heading">
							<spring:message code="role.page.edit" />
						</h2>
					</div>
				</div>
			</div>
			<!--Edit existing role-->
			<div class="panel-group user-management" id="accordion"
				role="tablist" aria-multiselectable="true">
				<div class="panel panel-default">
					<div class="panel-heading" role="tab" id="editRole">
						<h4 class="panel-title">
							<span></span> 
							<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne"> 
								<spring:message code="role.page.edit" /><span class="asterisk">&nbsp;&nbsp;*</span><span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
							</a>
						</h4>
					</div>
					<div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="editRole">
						<div class="panel-body">
							<div class="form-horizontal">
					       		<div class="form-group">
					       			<div class="col-sm-3">
					       				<span class="highlight-text">
											<spring:message code="role.page.edit.existing" />
											<br/>${roleForm.name}
					       				</span>
					       			</div>
					       		</div>
					       		<div class="form-group">
					       			<div class="col-sm-5">
					       				<form:label class="control-label" path="name"><spring:message code="role.page.name" /></form:label>
					       				<form:input type="text" class="form-control" path="name"></form:input>
					       				<form:errors class="error" path="name"></form:errors>
					       			</div>
					       			<div class="col-sm-7">
					       				<small class="pull-right pad-top10"><spring:message code="label.max.words" /></small>
				        				<form:label class="control-label" path="description"><spring:message code="role.page.description" /></form:label>
				        				<form:input type="text" class="form-control" path="description"></form:input>
				        				<form:errors class="error" path="description"></form:errors>
					       			</div>
					       		</div>
					       </div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading" role="tab" id="roleDetails">
						<h4 class="panel-title">
							<a class="collapsed" role="button" data-toggle="collapse"
								data-parent="#accordion" href="#collapseTwo"
								aria-expanded="false" aria-controls="collapseTwo"> <spring:message
									code="role.page.detail" /> <span class="asterisk">*</span><span
								class="selected"></span><span class="icon icon-arrow-down"></span><span
								class="icon icon-arrow-up"></span>
							</a>
						</h4>
					</div>
					<div id="collapseTwo" class="panel-collapse collapse"
						role="tabpanel" aria-labelledby="roleDetails">
						<div class="panel-body">
							<div class="form-horizontal">
					        	<div class="form-group">
					        		<div class="col-sm-12">
					        			<div class="col-sm-6 pad-left0">
					        				<div class="col-sm-6 col-lg-4 pad-left0">
							        			<label class="control-label"><spring:message code="role.page.view.accessprofile.selected" /></label>
							        		</div>
											<div class="col-sm-6">
												<form:select path="accessProfileId" id="selectProfile" class="form-control" onchange="loadAccessProfile();">
													<c:forEach items="${activeAccessProfileList}" var="accessProfile">
														<form:option id="${accessProfile.name}" value="${accessProfile.id}">${accessProfile.name}</form:option>
													</c:forEach>
												</form:select>
											</div>
					        			</div>
					        			<div class="col-sm-6 form-fields-tip text-right pad-right0">
					        				<spring:message code="role.page.deselect.role.msg" />
					        			</div>
					        		</div>
								</div>
						<div id="accessProfileView">
						    <jsp:include page="access-profile-view.jsp"></jsp:include>
					  	</div>
			        </div>

						</div>
					</div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading" role="tab" id="dataAccess">
						<h4 class="panel-title">
						<c:choose>
					      	<c:when test="${empty roleForm.dataAccessErrors}">
					      		<c:set var="collapse" value="" scope="page"/>
					      		<c:set var="expand" value="false" scope="page"/>
								<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
						          Data Access <span class="asterisk">*</span> <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
						        </a>
							</c:when>
					        <c:otherwise>
						        <c:set var="collapse" value="in" scope="page"/>
						        <c:set var="expand" value="true" scope="page"/>
					        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFour" aria-expanded="true" aria-controls="collapseFour">
					          Data Access <span class="asterisk">*</span> <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
					        </a>
					        </c:otherwise>
			        	</c:choose>
						</h4>
					</div>
					<div id="collapseFour" class="panel-collapse collapse ${collapse}" role="tabpanel" aria-labelledby="dataAccess" aria-expanded="${collapse}">
						<div class="panel-body">
							<form:errors path="dataAccessErrors" class="error"/>
							<div class="user-management-tabs">
								<ul class="nav nav-tabs custom-tabs inner" role="tablist">
									<li role="presentation" class="active"><a href="#acc4Tab1" role="tab" data-toggle="tab"><spring:message code="role.page.jurisdiction" /></a></li>
									<li role="presentation"><a href="#acc4Tab2" role="tab" data-toggle="tab"><spring:message code="role.page.assignee" /></a></li>
									<li role="presentation"><a href="#acc4Tab3" role="tab" data-toggle="tab"><spring:message code="role.page.customer" /></a></li>
									<li role="presentation"><a href="#acc4Tab4" role="tab" data-toggle="tab"><spring:message code="role.page.technologygroup" /></a></li>
									<li role="presentation"><a href="#acc4Tab5" role="tab" data-toggle="tab"><spring:message code="role.page.organization" /></a></li>
								</ul>

								<div class="tab-content">
									<div role="tabpanel" class="tab-pane active" id="acc4Tab1">
										<div class="row">
											<div class="multiselect-control clearfix">
												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="role.page.jurisdiction.select" />:</div>
													<select name="from[]" class="multiselect form-control"
														size="8" multiple="multiple"
														data-right="#multiselect_to_1"
														data-right-all="#right_All_1"
														data-right-selected="#right_Selected_1"
														data-left-all="#left_All_1"
														data-left-selected="#left_Selected_1">
														<c:forEach items="${jurisdictionMasterList}" var="jurisdiction">
															<option value="${jurisdiction.id}" data-position="${jurisdiction.id}">${jurisdiction.name}</option>
														</c:forEach>
													</select>
												</div>

												<div class="col-sm-2 col-lg-1 text-center">
													<div class="move-select-items">
														<button type="button" id="right_Selected_1" class=""><span class="icon icon-move-right"></span></button>
														<button type="button" id="left_Selected_1" class=""><span class="icon icon-move-left"></span></button>
													</div>
												</div>

												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="text.selected" />:</div>
													<form:select name="to[]" id="multiselect_to_1" path="jurisdictions" class="form-control" size="8" multiple="multiple">
														<c:forEach items="${roleForm.jurisdictions}" var="jurisdictionId">
															<c:forEach items="${jurisdictionMasterList}" var="jurisdiction">
																<c:if test="${jurisdictionId ==  jurisdiction.id}">
																	<form:option value="${jurisdiction.id}">${jurisdiction.name}</form:option>
																</c:if>	
															</c:forEach>	
														</c:forEach>												
													</form:select>
													<form:errors path="jurisdictions" class="error"/>
												</div>
											</div>
										</div>
									</div>
									<div role="tabpanel" class="tab-pane" id="acc4Tab2">
										<div class="row">
											<div class="multiselect-control clearfix">
												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="role.page.assignee.select" />:</div>
													<select name="from[]" class="multiselect form-control"
														size="8" multiple="multiple"
														data-right="#multiselect_to_2"
														data-right-all="#right_All_2"
														data-right-selected="#right_Selected_2"
														data-left-all="#left_All_2"
														data-left-selected="#left_Selected_2">
														<c:forEach items="${assigneeMasterList}" var="assignee">
															<option value="${assignee.id}" data-position="${assignee.id}">${assignee.name}</option>
														</c:forEach>
													</select>
												</div>

												<div class="col-sm-2 col-lg-1 text-center">
													<div class="move-select-items">
														<button type="button" id="right_Selected_2" class="">
															<span class="icon icon-move-right"></span>
														</button>
														<button type="button" id="left_Selected_2" class="">
															<span class="icon icon-move-left"></span>
														</button>
													</div>
												</div>

												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="text.selected" />:</div>
													<form:select name="to[]" id="multiselect_to_2" path="assignees" class="form-control" size="8" multiple="multiple">
														<c:forEach items="${roleForm.assignees}" var="assigneeId">
															<c:forEach items="${assigneeMasterList}" var="assignee">
																<c:if test="${assigneeId ==  assignee.id}">
																	<form:option value="${assignee.id}">${assignee.name}</form:option>
																</c:if>	
															</c:forEach>	
														</c:forEach>
													</form:select>
													<form:errors path="assignees" class="error"/>
												</div>
											</div>
										</div>
									</div>
									<div role="tabpanel" class="tab-pane" id="acc4Tab3">
										<div class="row">
											<div class="multiselect-control clearfix">
												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="role.page.customer.select" />:</div>
													<select name="from[]" class="multiselect form-control"
														size="8" multiple="multiple"
														data-right="#multiselect_to_3"
														data-right-all="#right_All_3"
														data-right-selected="#right_Selected_3"
														data-left-all="#left_All_3"
														data-left-selected="#left_Selected_3">
														<c:forEach items="${customerMasterList}" var="customer">
															<option value="${customer.id}" data-position="${customer.id}">${customer.number}</option>
														</c:forEach>
													</select>
												</div>

												<div class="col-sm-2 col-lg-1 text-center">
													<div class="move-select-items">
														<button type="button" id="right_Selected_3" class="">
															<span class="icon icon-move-right"></span>
														</button>
														<button type="button" id="left_Selected_3" class="">
															<span class="icon icon-move-left"></span>
														</button>
													</div>
												</div>

												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="text.selected" />:</div>
													<form:select name="to[]" id="multiselect_to_3" path="customers" class="form-control" size="8" multiple="multiple">
														<c:forEach items="${roleForm.customers}" var="customerId">
															<c:forEach items="${customerMasterList}" var="customer">
																<c:if test="${customerId ==  customer.id}">
																	<form:option value="${customer.id}">${customer.number}</form:option>
																</c:if>	
															</c:forEach>	
														</c:forEach>
													</form:select>
													<form:errors path="customers" class="error"/>
												</div>
											</div>
										</div>
									</div>
									<div role="tabpanel" class="tab-pane" id="acc4Tab4">
										<div class="row">
											<div class="multiselect-control clearfix">
												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="role.page.technologygroup.select" />:</div>
													<select name="from[]" class="multiselect form-control"
														size="8" multiple="multiple"
														data-right="#multiselect_to_4"
														data-right-all="#right_All_4"
														data-right-selected="#right_Selected_4"
														data-left-all="#left_All_4"
														data-left-selected="#left_Selected_4">
														<c:forEach items="${technologyGroupMasterList}" var="technologyGroup">
															<option value="${technologyGroup.id}" data-position="${technologyGroup.id}">${technologyGroup.name}</option>
														</c:forEach>
													</select>
												</div>

												<div class="col-sm-2 col-lg-1 text-center">
													<div class="move-select-items">
														<button type="button" id="right_Selected_4" class="">
															<span class="icon icon-move-right"></span>
														</button>
														<button type="button" id="left_Selected_4" class="">
															<span class="icon icon-move-left"></span>
														</button>
													</div>
												</div>

												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="text.selected" />:</div>
													<form:select name="to[]" id="multiselect_to_4" path="technologyGroups" class="form-control" size="8" multiple="multiple">
														<c:forEach items="${roleForm.technologyGroups}" var="technologyGroupId">
															<c:forEach items="${technologyGroupMasterList}" var="technologyGroup">
																<c:if test="${technologyGroupId ==  technologyGroup.id}">
																	<form:option value="${technologyGroup.id}">${technologyGroup.name}</form:option>
																</c:if>	
															</c:forEach>	
														</c:forEach>
													</form:select>
													<form:errors path="technologyGroups" class="error"/>
												</div>
											</div>
										</div>
									</div>
									<div role="tabpanel" class="tab-pane" id="acc4Tab5">
										<div class="row">
											<div class="multiselect-control clearfix">
												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="role.page.organization.select" />:</div>
													<select name="from[]" class="multiselect form-control"
														size="8" multiple="multiple"
														data-right="#multiselect_to_5"
														data-right-all="#right_All_5"
														data-right-selected="#right_Selected_5"
														data-left-all="#left_All_5"
														data-left-selected="#left_Selected_5">
														<c:forEach items="${organizationMasterList}" var="organization">
															<option value="${organization.id}" data-position="${organization.id}">${organization.name}</option>
														</c:forEach>
													</select>
												</div>

												<div class="col-sm-2 col-lg-1 text-center">
													<div class="move-select-items">
														<button type="button" id="right_Selected_5" class="">
															<span class="icon icon-move-right"></span>
														</button>
														<button type="button" id="left_Selected_5" class="">
															<span class="icon icon-move-left"></span>
														</button>
													</div>
												</div>

												<div class="col-sm-5 col-lg-3">
													<div class="title"><spring:message code="text.selected" />:</div>
													<form:select name="to[]" id="multiselect_to_5" path="organizations" class="form-control" size="8" multiple="multiple">
													<c:forEach items="${roleForm.organizations}" var="organizationId">
															<c:forEach items="${organizationMasterList}" var="organization">
																<c:if test="${organizationId ==  organization.id}">
																	<form:option value="${organization.id}">${organization.name}</form:option>
																</c:if>	
															</c:forEach>	
														</c:forEach>
													</form:select>
													<form:errors path="organizations" class="error"/>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!--create new role ends-->
			<!--form footer-->
			<div class="form-horizontal form-footer">
				<div class="col-sm-12">
					<div class="form-group">
						<form:checkbox path="otpActivate" id="emailOTP"></form:checkbox>
					<form:label path="otpActivate" for="emailOTP" class="control-label"><spring:message code="role.page.otp.activate.email" /></form:label>
					</div>
					<div class="form-group text-left">
						<form:button type="button" class="btn btn-cancel" onclick="popupMsgForCancelSaveRole();"><spring:message code="button.cancel" /></form:button>
						<form:button id="createRole" value="update" class="btn btn-submit"><spring:message code="button.submit" /></form:button>
						<%-- <form:button type="button" class="btn btn-cancel" data-dismiss="modal" data-toggle="modal" data-target="#modalCancelSaveRole" data-href="window.location.href = '../admin/';">Cancel</form:button> --%>				
					</div>
				</div>
			</div>
		</div>
	</form:form>

	<div class="modal fade" id="modalCancelSaveRole" tabindex="-1" role="dialog" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                Cancel
	            </div>
	            <div class="modal-body">
                <p class="msg"><spring:message code="text.cancel.confirmation" /></p>
	            </div>
	            <div class="modal-footer">
	                <button class="btn btn-danger btn-ok" ><spring:message code="text.yes" /></button>
	                <a class="btn btn-default" data-dismiss="modal"><spring:message code="text.no" /></a>
	            </div>
	        </div>
	    </div>
	</div>

	<div id="popupMsgCancelSaveRole" class="popup-msg">
		<div class="text-right"><a class="close" href="#">&times;</a></div>
		<div class="content">
			<p class="msg"><spring:message code="text.cancel.confirmation" /></p>
		</div>
		<div class="modal-footer">
             <a type="button" class="btn btn-submit" data-dismiss="modal" href="../admin/"><spring:message code="text.yes" /></a>
             <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hideRoleCancelSavePopUp();"><spring:message code="text.no" /></button>
       </div>
	</div>

	<script type="text/javascript" src="<%=js%>/admin/role.js"></script>