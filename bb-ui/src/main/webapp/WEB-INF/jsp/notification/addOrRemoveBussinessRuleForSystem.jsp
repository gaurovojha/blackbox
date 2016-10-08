<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
 <link rel="stylesheet" type="text/css" href="${context}/assets/css/notification.css">
</head>
<body>
	<div class="main-content container">
		<div class="row">
			<div class="col-sm-12">
				<div class="page-header">
					<span class="pull-right form-fields-tip">All <span
						class="asterisk">*</span> marked fields are compulsory
					</span>
					<h2 class="page-heading">Add New Business Rule</h2>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-9">
				<form:form action="#" class="form-horizontal"
					commandName="editBussinessRules">

					<input type="hidden" id="notificationId"
						name="${editBussinessRules.notificationId}"></input>

					<div class="form-group">
						<div class="col-sm-4">
							<label class="control-label">Notification Name</label>
							<div class="form-control-static">${editBussinessRules.notificationName}</div>
						</div>
						<div class="col-sm-4">
							<label class="control-label">Notification Subject</label>
							<div class="form-control-static">${editBussinessRules.notificationSubject}</div>
						</div>
						<div class="col-sm-4">
							<label class="control-label">Notification Type</label>
							<div class="form-control-static">${editBussinessRules.notificationType}</div>
						</div>
					</div>
				
					<div id="hiddenNotificationFields">
						<div class="form-group">
							<div class="col-sm-12">
								<label class="control-label">Notification Message</label>
								<div class="form-control-static">${editBussinessRules.notificationMessage}</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-6">
								<label class="control-label">Display while preparing IDS</label>
								<div class="form-control-static">${editBussinessRules.displayOnIDSReview}</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Send E- mail for this
									Notification</label>
								<div class="form-control-static">${editBussinessRules.emailNotification}</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-6">
								<label class="control-label">Number of Reminders</label>
								<div class="form-control-static">${editBussinessRules.noOfreminders}</div>
							</div>
							<div class="col-sm-6">
								<label class="control-label">Escalation</label>
								<div class="form-control-static">${editBussinessRules.escalation}</div>
							</div>
						</div>
					</div>
					<div>
						<a href="javascript:void(0)" id="toggleNotificationFields">Show
							All Details</a>
					</div>
					<div class="divider"></div>
					
						<div class="form-group">
							<div class="col-sm-4">
								<label class="control-label">Level of Notification</label>
								 <form:select id="notificationLevelNo" path="notificationLevelNo"  class="form-control" disabled="true">
                                 <option value="1" selected="selected" >Level 1</option>
                                 </form:select>
							</div>
							<div class="col-sm-6">
							
							 <label class="control-label">Default Recipent</label>
							 
							<!-- <div class="form-control-static">Recipent Role <a href="javascript:void(0)"><span class="icon icon-edit"></span></a></div> -->
							 
							 <div class="form-control-static"><a href="#" onclick="populatedefalutRolesRoles()">Add Role </a></div>
							 <div class="role-details" style="width:200px">
							<c:forEach items="${editBussinessRules.defaultRoles}" var="defaultRole" begin="0">
							   <div class="form-group">
								  <div class="col-sm-12">
								<!-- <div class="rule-text defaultRole">${defaultRole.name}</div> -->  
								
									 <div class="input-group role-input-block" style="display:block">
										<input type="text" id="${defaultRole.roleId}"  value="${defaultRole.name}" style="width:150px;height:25px;" class="form-control defaultRolesDelete"></input>		        			
										<span class="input-group-btn"><i class="icon icon-delete defaultRoleDelete"></i></span>
									 </div>
								  </div>
							   </div>
							</c:forEach>
							</div> 
							 
							</div>
						</div>

					<div id="message" class="message"></div>

					<div id="addOrRemoveBussinessRuleByLevel2">
						<div class="form-group">
							<div class="col-sm-6">
								<label class="control-label">Notification Business Rules<span
									class="required">*</span></label>
							</div>
						</div>
						<div class="sender-recipent-block">
							<div class="col-sm-9">
								<span>Sender</span>
							     </div>
								 <div class="col-sm-3">
									<span class="recipent">Recipient ${editBussinessRules.notificationLevelNo}</span>
								</div>
						</div>
						<!--business rules grid-->
						<div class="business-rules-grid clearfix">
							<!--row-->
							<c:forEach items="${editBussinessRules.businessRuleDtos}"
								var="notificationBussinessRule">

								<div id="${notificationBussinessRule.notificationBusinessRuleId}"  class="clearfix odd-row ruleRow">

									<c:forEach
										items="${notificationBussinessRule.businessLevelDto}"
										var="notificationBussinessRuleLevel">

										<c:if
											test="${notificationBussinessRuleLevel.currentLevelNo==1}">

													<div class="clearfix odd-row ">
													<div class="col-sm-9 ruleCol trasactionAttriButesForSystem">
                                                      <c:forEach
													items="${notificationBussinessRuleLevel.levelRoleDtos}"
													var="notificationRole">
                                                      <c:if test="${notificationRole.type eq 'Sender'}">
                                                      
                                                      <input id="systemRoleId" type="hidden"  value="${notificationRole.roleId} "/>
                                                      
																<div class="rule-text">System</div>
																	<div class="sender-details clearfix">
																		<div class="block">
																			<div class="field">Jurisdiction</div>
																		<c:forEach items="${notificationRole.jurisdictions}" var="jurisdiction">
																			<div class="value" id="${jurisdiction.id}">${jurisdiction.name}</div>
																		</c:forEach>
																		</div>
																		<div class="block">
																			<div class="field">Assignee</div>
																			<c:forEach items="${notificationRole.assignees}" var="assignee">
																			<div class="value" id="${assignee.id}">${assignee.name}</div>
																		</c:forEach>
																	
																		</div>
																		<div class="block">
																			<div class="field">Customer No.</div>
																			<c:forEach items="${notificationRole.customerNos}" var="customerNo">
																			<div class="value" id="${customerNo.id}">${customerNo.name}</div>
																		</c:forEach>
																	
																		</div>
																		<div class="block">
																			<div class="field">Technology Group</div>
																			<c:forEach items="${notificationRole.techGroups}" var="techGroup">
																			<div class="value" id="${techGroup.id}">${techGroup.name}</div>
																		</c:forEach>
																	
																		</div>
																		<div class="block">
																			<div class="field">Organization</div>
																			<c:forEach items="${notificationRole.organisations}" var="organisation">
																			<div class="value" id="${organisation.id}">${organisation.name}</div>
																		</c:forEach>
																	
																		</div>
																	</div>
																	</c:if>
																	</c:forEach>
														
																	 </div>
																<div class="col-sm-3 ruleCol rolesForSystem">
																	 <c:forEach items="${notificationBussinessRuleLevel.levelRoleDtos}" var="notificationRole">
																	  <c:if test="${notificationRole.type eq 'Receiver'}">   
																	<div class="rule-text">${notificationRole.roleName}</div>
																	
																	<div class="form-group role-input-block addedNewElement" style="display:none">
                                                                    <div class="input-group">
                                                                    <input type="text" id="${notificationRole.roleId}#${notificationRole.roleName}" name="${notificationRole.roleName}" value="${notificationRole.roleName}" class="form-control">
                                                                    <span class="input-group-btn"><i class="icon icon-delete inputRole"></i>
                                                                    </span></div>
                                                                    </div>
																	
																	</c:if>
																	</c:forEach>
																		<div class="form-group role-input-block addNewReciepientRoleAnchor" style="display:none"><a href="javascript:void(0)" onclick="addRolesModalPanel(this);showRolesOnlyByAttributes(this);">Add New Recipient Role</a></div>
																</div>
																	
																
																<div class="action-btns-grid">
																		<a href="javascript:void(0)" class="editRuleForSytem"><span class="icon icon-edit"></span>
																			Edit</a> <a href="javascript:void(0)" class="deleteRuleIcon"><span class="icon icon-delete"></span>
																			Delete</a>
																</div>
													
													</div>
												<div
													class="form-group role-input-block"
													style="display: none">
													<a href="javascript:void(0)" onclick="addRolesModalPanel(this)">Add New Sender Role</a>
												</div>
											
										</c:if>

									</c:forEach>
                                 <div class="buttonContainer">
									<button type="button" class="btn submitRuleEditOrAdd"
										style="display: inline-block; border: 2px #2e3192 solid; background: #fff; color: #2e3192"
										onclick="closeTheRuleDivContainer(this)">Cancel</button>
									<button type="button" class="btn submitRuleEditOrAdd"
										style="display: inline-block; border: 2px #2e3192 solid; background: #2e3192; color: #fff"
										onclick="saveRolesForSystem(this)">Save</button>
								</div>
								</div>
								
						</c:forEach>
						<div id="addNewBussinessRuleId">
							<a href="javascript:void(0)" onclick="addNewRuleRowForSystem()">Add New Business Rule</a>
						</div>
					</div>
			</div>

			<div class="form-group form-footer">
				<div class="col-sm-12 text-left cancelAndsaveContainer">
					<button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="closePage();" >Back</button>
				</div>
			</div>

			<!-- add Role Modal Panel -->
			<div class="modal custom fade modal-wide" id="addRolesModal"
				tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="myModalLabel">Add Roles</h4>
						</div>
						<div class="modal-body">
							<div class="row">
								<div class="col-sm-12">

									<div class="col-sm-3">
										<select id="roles" multiple="multiple" class="form-control"
											onChange="onselectOptions(this);">
											<c:forEach begin="0" items="${editBussinessRules.roleDTOs}"
												var="roleDTO" varStatus="position">
												<option id="${roleDTO.id}" value="${roleDTO.name}">${roleDTO.name}</option>
											</c:forEach>
										</select>
									</div>

									<div class="col-sm-9">
										<table class="table custom-table">
											<thead>
												<tr>
													<th>Role Name</th>
													<th>Access Profile</th>
													<th>User</th>
													<th>Created by</th>
													<th>OTP Activate</th>
													<th>Status</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach begin="0" items="${editBussinessRules.roleDTOs}"
													var="roleDTO" varStatus="position">
													<tr id="tableRow${roleDTO.id}" class="odd"
														style="display: none">
														<td id="${roleDTO.name}">${roleDTO.name}</td>
														<td>${roleDTO.accessProfile.name}</td>
														<td>${roleDTO.userCount}</td>
														<td>${roleDTO.createdBy}</td>
														<td>${roleDTO.otpActivate}</td>
														<td>${roleDTO.status}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-cancel" data-dismiss="modal">Cancel</button>
							<button type="button" class="btn btn-submit"
								onclick="subMitRolesAnadCloseModalPanel()">Add New
								Roles</button>
						</div>
					</div>
				</div>
			</div>
			
			<!-- default reciepient roles -->
				 <div class="modal custom fade modal-wide" id="addRolesForDefaultReciepientRoles" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				<div class="modal-dialog" role="document">
				<div class="modal-content">
				<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Add Roles</h4>
				</div>
				<div class="modal-body">
							<div class="row">
								<div class="col-sm-12">

									<div class="col-sm-3">
										<select id="defaultRoles" multiple="multiple" class="form-control"
											onChange="">
											<c:forEach begin="0" items="${editBussinessRules.roleDTOs}"
												var="roleDTO" varStatus="position">
												<option id="${roleDTO.id}" value="${roleDTO.name}">${roleDTO.name}</option>
											</c:forEach>
										</select>
									</div>

									<div class="col-sm-9">
										<table class="table custom-table">
											<thead>
												<tr>
													<th>Role Name</th>
													<th>Access Profile</th>
													<th>User</th>
													<th>Created by</th>
													<th>OTP Activate</th>
													<th>Status</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach begin="0" items="${editBussinessRules.roleDTOs}"
													var="roleDTO" varStatus="position">
													<tr id="defaultRolesRow${roleDTO.id}" class="odd"
														style="display: none">
														<td id="${roleDTO.name}">${roleDTO.name}</td>
														<td>${roleDTO.accessProfile.name}</td>
														<td>${roleDTO.userCount}</td>
														<td>${roleDTO.createdBy}</td>
														<td>${roleDTO.otpActivate}</td>
														<td>${roleDTO.status}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-cancel" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-submit" onclick="addingRolesDefaultRoles();savedefaultsRoles();">Add New Default Roles</button>
				</div>
				</div>
				</div>
				</div>
			
			
	<div class="modal custom fade modal-wide" id="systemModalAttributes" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Add New Transaction Attribute</h4>
	      </div>
	     
	     
	     
	     <div class="modal-body">
		       <div class="panel-body">
			      	<div class="user-management-tabs">
				        <ul class="nav nav-tabs custom-tabs small" role="tablist">
							<li role="presentation" class="active"><a href="#acc4Tab1" role="tab" data-toggle="tab">Jurisdiction</a></li>
							<li role="presentation"><a href="#acc4Tab2" role="tab" data-toggle="tab">Assignee</a></li>
							<li role="presentation"><a href="#acc4Tab3" role="tab" data-toggle="tab">Customer No.</a></li>
							<li role="presentation"><a href="#acc4Tab4" role="tab" data-toggle="tab">Technology Group</a></li>
							<li role="presentation"><a href="#acc4Tab5" role="tab" data-toggle="tab">Organisation</a></li>
						</ul>
                    <div id="modalMessage" class="modalMessage" ></div>
						<div class="tab-content">
							<div role="tabpanel"  class="tab-pane active Jurisdiction" id="acc4Tab1">
								<div class="row">
									<div class="multiselect-control clearfix">
										<div class="col-sm-5 col-lg-3">
											<div class="title">Select Jurisdictions:</div>
											<select name="from[]" class="multiselect form-control" size="8" multiple="multiple" data-right="#jurisdiction" data-right-all="#right_All_1" data-right-selected="#right_Selected_1" data-left-all="#left_All_1" data-left-selected="#left_Selected_1">
												<c:forEach items="${jurisdictions}" var="jurisdiction">
			                                  <option id="${jurisdiction.id}#${jurisdiction.name}">${jurisdiction.name}</option>
			                                      </c:forEach>
												<c:forEach items=""></c:forEach>
											</select>
										</div>
										
										<div class="col-sm-2 col-lg-1 text-center">
											<div class="move-select-items">
												<button type="button" id="right_Selected_1" class=""><span class="icon icon-move-right"></span></button>
												<button type="button" id="left_Selected_1" class=""><span class="icon icon-move-left"></span></button>
											</div>
										</div>
										
										<div class="col-sm-5 col-lg-3 selectedAttributes">
											<div class="title">Selected:</div>
											<select name="to[]" id="jurisdiction" class="form-control" size="8" multiple="multiple"></select>
										</div>
									</div>
								</div>
							</div>
							
							<div role="tabpanel" class="tab-pane Assignee" id="acc4Tab2">
								<div class="row">
									<div class="multiselect-control clearfix">
										<div class="col-sm-5 col-lg-3">
											<div class="title">Select Assignee:</div>
											<select name="from[]" class="multiselect form-control" size="8" multiple="multiple" data-right="#assignee" data-right-all="#right_All_2" data-right-selected="#right_Selected_2" data-left-all="#left_All_2" data-left-selected="#left_Selected_2">
												<c:forEach items="${assignees}" var="assignee">
			                                      <option id="${assignee.id}#${assignee.name}">${assignee.name}</option>
			                                     </c:forEach>
											</select>
										</div>
										
										<div class="col-sm-2 col-lg-1 text-center">
											<div class="move-select-items">
												<button type="button" id="right_Selected_2" class=""><span class="icon icon-move-right"></span></button>
												<button type="button" id="left_Selected_2" class=""><span class="icon icon-move-left"></span></button>
											</div>
										</div>
										
										<div class="col-sm-5 col-lg-3 selectedAttributes">
											<div class="title">Selected:</div>
											<select name="to[]" id="assignee" class="form-control" size="8" multiple="multiple"></select>
										</div>
									</div>
								</div>
							</div>
							<div role="tabpanel" class="tab-pane Customer" id="acc4Tab3">
								<div class="row">
									<div class="multiselect-control clearfix">
										<div class="col-sm-5 col-lg-3 selectedAttributes">
											<div class="title">Select Customer No.:</div>
											<select name="from[]" class="multiselect form-control" size="8" multiple="multiple" data-right="#customerNo" data-right-all="#right_All_3" data-right-selected="#right_Selected_3" data-left-all="#left_All_3" data-left-selected="#left_Selected_3">
											<c:forEach items="${customers}" var="customer">
			                                <option id="${customer.id}#${customer.number}">${customer.number}</option>
			                                </c:forEach>
											</select>
										</div>
										
										<div class="col-sm-2 col-lg-1 text-center">
											<div class="move-select-items">
												<button type="button" id="right_Selected_3" class=""><span class="icon icon-move-right"></span></button>
												<button type="button" id="left_Selected_3" class=""><span class="icon icon-move-left"></span></button>
											</div>
										</div>
										
										<div class="col-sm-5 col-lg-3 selectedAttributes">
											<div class="title">Selected:</div>
											<select name="to[]" id="customerNo" class="form-control" size="8" multiple="multiple"></select>
										</div>
									</div>
								</div>
							</div>
							<div role="tabpanel" class="tab-pane technologyGroup" id="acc4Tab4">
								<div class="row">
									<div class="multiselect-control clearfix">
										<div class="col-sm-5 col-lg-3">
											<div class="title">Select Technology Group:</div>
											<select name="from[]" class="multiselect form-control" size="8" multiple="multiple" data-right="#technologyGroup" data-right-all="#right_All_4" data-right-selected="#right_Selected_4" data-left-all="#left_All_4" data-left-selected="#left_Selected_4">
													
			                               <c:forEach items="${technologygroups}" var="technologygroup">
			                              <option id="${technologygroup.id}#${technologygroup.name}">${technologygroup.name}</option>
			                              </c:forEach>
											</select>
										</div>
										
										<div class="col-sm-2 col-lg-1 text-center">
											<div class="move-select-items">
												<button type="button" id="right_Selected_4" class=""><span class="icon icon-move-right"></span></button>
												<button type="button" id="left_Selected_4" class=""><span class="icon icon-move-left"></span></button>
											</div>
										</div>
										
										<div class="col-sm-5 col-lg-3 selectedAttributes">
											<div class="title">Selected:</div>
											<select name="to[]" id="technologyGroup" class="form-control" size="8" multiple="multiple"></select>
										</div>
									</div>
								</div>
							</div>
							<div role="tabpanel" class="tab-pane organization" id="acc4Tab5">
								<div class="row">
									<div class="multiselect-control clearfix">
										<div class="col-sm-5 col-lg-3">
											<div class="title">Select Organisation:</div>
											<select name="from[]" class="multiselect form-control" size="8" multiple="multiple" data-right="#organization" data-right-all="#right_All_5" data-right-selected="#right_Selected_5" data-left-all="#left_All_5" data-left-selected="#left_Selected_5">
												<c:forEach items="${organizations}" var="organization">
			                                    <option id="${organization.id}#${organization.name}">${organization.name}</option>
			                                    </c:forEach>
											</select>
										</div>
										
										<div class="col-sm-2 col-lg-1 text-center">
											<div class="move-select-items">
												<button type="button" id="right_Selected_5" class=""><span class="icon icon-move-right"></span></button>
												<button type="button" id="left_Selected_5" class=""><span class="icon icon-move-left"></span></button>
											</div>
										</div>
										
										<div class="col-sm-5 col-lg-3 selectedAttributes">
											<div class="title">Selected:</div>
											<select name="to[]" id="organization" class="form-control" size="8" multiple="multiple"></select>
										</div>
									</div>
									 
								</div>
							</div>
						</div>
					</div>
			      </div>
	      </div> 
	      
	      <div class="modal-footer">
	        <button type="button" class="btn btn-cancel" data-dismiss="modal" >Cancel</button>
	        <button type="button" class="btn btn-submit addAttributeFromModal">Add Attribute</button>
	      </div>
	    </div>
	  </div>
	</div>
			</form:form>

			<!--popup msg-->
			<div class="popup-msg">
				<a class="close" href="#" onclick="hideMessage()">&times;</a>
				<div class="content">
					<p class="msg"></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-cancel" data-dismiss="modal"
						onclick="deleteSystemRule();hideMessage();">YES</button>
					<button type="button" class="btn btn-submit"
						onclick="hideMessage();">NO</button>
				</div>
			</div>
			
			<div id="cancelRulePopup" class="popup-msg">
		<div class="text-right"><span class="close" onclick="hidecancelRuleMessage()">&times;</span></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="hidecancelRuleMessage();window.location.reload(true);">YES</button>
                <button type="button" class="btn btn-submit" onclick="hidecancelRuleMessage();">NO</button>
        </div>
	</div> 

  <!--popup msg-->
	 <div id="deleteRulePopup" class="popup-msg">
		<div class="text-right"><span class="close"  onclick="hideMessageForSystemDeleteRule()">&times;</span></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="deleteRule();hideMessageForSystemDeleteRule();">YES</button>
                <button type="button" class="btn btn-submit" onclick="hideMessageForSystemDeleteRule();">NO</button>
        </div>
	</div> 

		</div>
		<div class="col-sm-5"></div>
	</div>
	</div>
	<script type="text/javascript"
		src="${context}/assets/js/notification/notification.js"></script>

</body>
</html>


