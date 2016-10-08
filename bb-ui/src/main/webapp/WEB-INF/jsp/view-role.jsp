<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>

	<div>
		<sec:authorize access="canAccessUrl('/role/edit')" var="accessRoleEdit"/>
		<sec:authorize access="canAccessUrl('/role/delete')" var="accessRoleDelete"/>
	</div>

	<c:set var="disableEditRole" value="" scope="page"/>
	<c:if test="${!accessRoleEdit}">
		<c:set var="disableEditRole" value="hidden-control" scope="page"/>
		<%-- <c:set var="disableCreateAccessProfile" value="disabled" scope="page"/> --%>
	</c:if>
	
	<c:set var="disableDeleteRole" value="" scope="page"/>
	<c:if test="${!accessRoleDelete}">
		<c:set var="disableDeleteRole" value="hidden-control" scope="page"/>
		<%-- <c:set var="disableCreateAccessProfile" value="disabled" scope="page"/> --%>
	</c:if>

	<form:form commandName="roleForm" method="post" action="update">

		<form:hidden path="id" />
		<form:hidden path="version"/>

		<div class="main-content container">
			<div class="row">
				<div class="col-sm-12">
					<div class="page-header">
						<h2 class="page-heading">
							<spring:message code="role.page.view" />
						</h2>
					</div>
				</div>
			</div>
			<!--view role-->
			<div class="panel-group user-management" id="accordion"
				role="tablist" aria-multiselectable="true">
				<div class="panel panel-default">
					<div class="panel-heading" role="tab" id="editRole">
						<h4 class="panel-title">
							<span></span> 
							<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne"> 
								<spring:message code="role.page.view" /><span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
							</a>
						</h4>
					</div>
					<div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="editRole">
						<div class="panel-body">
							<div class="form-horizontal">
					       		<div class="form-group">
					       			<div class="col-sm-3">
					       				<span class="highlight-text">
											<spring:message code="role.page.name" />
											<br/>${roleForm.name}
					       				</span>
					       			</div>
					       		</div>
					       		<div class="form-group">
					       			<div class="col-sm-5">
					       				<form:label class="control-label" path="name"><spring:message code="role.page.name" /></form:label>
					       				<form:input type="text" class="form-control" path="name" disabled="true"></form:input>
					       			</div>
					       			<div class="col-sm-7">
					       				<small class="pull-right pad-top10"><spring:message code="roleform.description.size" /></small>
				        				<form:label class="control-label" path="description"><spring:message code="role.page.description" /></form:label>
				        				<form:input type="text" class="form-control" path="description" disabled="true"></form:input>
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
									code="role.page.detail" /><span
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
												<form:select path="accessProfileId" id="selectProfile" class="form-control" disabled="true">
													<form:options items="${accessProfileList}" itemLabel="name" itemValue="id"></form:options>
												</form:select>
											</div>
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
							<a class="collapsed" role="button" data-toggle="collapse"
								data-parent="#accordion" href="#collapseFour"
								aria-expanded="false" aria-controls="collapseFour"><spring:message code="role.page.view.data.access" /><span class="selected"></span><span
								class="icon icon-arrow-down"></span><span
								class="icon icon-arrow-up"></span>
							</a>
						</h4>
					</div>
					<div id="collapseFour" class="panel-collapse collapse"
						role="tabpanel" aria-labelledby="dataAccess">
						<div class="panel-body">
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
														data-left-selected="#left_Selected_1"
														disabled="disabled">
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
													<form:select name="to[]" id="multiselect_to_1" path="jurisdictions" class="form-control" size="8" multiple="multiple" disabled="true">
														<form:options items="${roleForm.jurisdictionList}" itemValue="id" itemLabel="name"></form:options>S					
													</form:select>
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
														data-left-selected="#left_Selected_2"
														disabled="disabled">
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
													<form:select name="to[]" id="multiselect_to_2" path="assignees" class="form-control" size="8" multiple="multiple" disabled="true">
														<form:options items="${roleForm.assigneeList}" itemValue="id" itemLabel="name"></form:options>
													</form:select>
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
														data-left-selected="#left_Selected_3"
														disabled="disabled">
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
													<form:select name="to[]" id="multiselect_to_3" path="customers" class="form-control" size="8" multiple="multiple" disabled="true">
														<form:options items="${roleForm.customerList}" itemValue="id" itemLabel="number"></form:options>
													</form:select>
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
														data-left-selected="#left_Selected_4"
														disabled="disabled">
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
													<form:select name="to[]" id="multiselect_to_4" path="technologyGroups" class="form-control" size="8" multiple="multiple" disabled="true">
														<form:options items="${roleForm.technologyGroupList}" itemValue="id" itemLabel="name"></form:options>
													</form:select>
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
														data-left-selected="#left_Selected_5"
														disabled="disabled">
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
													<form:select name="to[]" id="multiselect_to_5" path="organizations" class="form-control" size="8" multiple="multiple" disabled="true">
														<form:options items="${roleForm.organizationList}" itemValue="id" itemLabel="name"></form:options>
													</form:select>
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
						<form:checkbox path="otpActivate" id="emailOTP" disabled="true"></form:checkbox>
						<form:label path="otpActivate" for="emailOTP" class="control-label"><spring:message code="role.page.otp.activate.email" /></form:label>
					</div>
					<div class="form-group">
					<c:choose>
						<c:when test="${accessRoleDelete}">
							<c:choose>
								<c:when test="${roleForm.userCount == 0 && roleForm.status == 'Active' && !roleForm.seeded}">
									<input type="button" class="btn btn-submit" value="Drop" onclick="window.location.href = 'delete?id=${roleForm.id}';">									
								</c:when>
								<c:otherwise>
									<input type="button" class="btn btn-submit disabled" value="Drop">
								</c:otherwise>
							</c:choose>	
						</c:when>
						<c:otherwise>
							<input type="button" class="btn btn-submit ${disableDeleteRole}" value="Drop">
						</c:otherwise>
					</c:choose>	
					
					<c:choose>
						<c:when test="${accessRoleEdit}">
							<c:choose>
								<c:when test="${roleForm.status == 'Active' && !roleForm.seeded}">
									<input type="button" class="btn btn-submit" value="Edit" onclick="window.location.href = 'edit?id=${roleForm.id}';">									
								</c:when>
								<c:otherwise>								
									<input type="button" class="btn btn-submit disabled" value="Edit">
								</c:otherwise>
							</c:choose>	
						</c:when>
						<c:otherwise>
							<input type="button" class="btn btn-submit ${disableEditRole}" value="Edit">
						</c:otherwise>
					</c:choose>
						
						<input type="button" class="btn btn-submit" value="Go Back To Admin" onclick="window.location.href = '../admin/';">				
					</div>
				</div>
			</div>
		</div>
	</form:form>
	
	<script type="text/javascript" src="<%=js%>/admin/role.js"></script>