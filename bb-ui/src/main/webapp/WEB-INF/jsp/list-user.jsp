<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String js = context + "/assets/js";
	String img = context + "/assets/images";
%>
<div>
		<sec:authorize access="canAccessUrl('/user/edit')" var="accessUserEdit"/>
		<sec:authorize access="canAccessUrl('/user/generateotp')" var="accessUserGenerateOtp"/>
		<sec:authorize access="canAccessUrl('/user/disableaccess')" var="accessUserDisable"/>
		<sec:authorize access="canAccessUrl('/user/drop')" var="accessUserDrop"/>
		<sec:authorize access="canAccessUrl('/user/create')" var="accessUserCreate"/>
		<sec:authorize access="canAccessUrl('/user/view/{userId}')" var="accessUserView"/>
		<sec:authorize access="canAccessUrl('/role/view')" var="accessRoleView"/>
</div>
	<div class="tab-heading clearfix">
						<h3 class="pull-left">List of User</h3>
						<c:choose>
								<c:when test="${accessUserCreate}">
									<button type="button" class="btn btn-submit pull-right"  data-toggle="modal" id="create_user">Create New User</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="btn btn-submit pull-right hidden-control"  data-toggle="modal" id="create_user">Create New User</button>
								</c:otherwise>
						</c:choose>
						<div class="pull-right user-action-btns">
							<!-- Show edit button when user has access -->
							<c:choose>
								<c:when test="${accessUserEdit}">
									<a href="javascript:void()" class="btn1 disabled" id="edit_user">Edit User</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void()" class="btn1 disabled hidden-control" id="edit_user">Edit User</a>
								</c:otherwise>
							</c:choose>
							
							<!-- Show generate otp button when user has access -->
							<c:choose>
								<c:when test="${accessUserGenerateOtp}">
									<a href="javascript:void()" class="btn1 disabled" id="generate_otp">Generate OTP</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void()" class="btn1 disabled hidden-control" id="generate_otp">Generate OTP</a>
								</c:otherwise>
							</c:choose>
							
							<!-- Show disable access button when user has access -->
							<c:choose>
								<c:when test="${accessUserDisable}">
									<a href="javascript:void()" class="btn1 disabled disableUserIcon" id="disable_access">Disable Access</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void()" class="btn1 disabled disableUserIcon hidden-control" id="disable_access">Disable Access</a>
								</c:otherwise>
							</c:choose>
							
							<!-- Show drop access button when user has access -->
							<c:choose>
								<c:when test="${accessUserDrop}">
									<a href="javascript:void()" class="btn1 disabled dropUserIcon" id="drop_access">Drop User</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void()" class="btn1 disabled dropUserIcon hidden-control" id="drop_access">Drop User</a>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="modal custom fade" id="myOTPModal" tabindex="-1"
                                                role="dialog" aria-labelledby="OTPModalLabel">
                                         <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                       <div class="modal-header">
                                                              <button type="button" class="close close-modal" data-dismiss="modal"
                                                                     aria-label="Close">
                                                                     <span aria-hidden="true">&times;</span>
                                                              </button>
                                                              <h4 class="modal-title" id="OTPModalLabel">
                                                                     <spring:message code="users.page.user.generateotp.popup" />
                                                              </h4>
                                                              <h3><div id="otp_msg"  class="control-label"></div></h3>
                                                       </div>
                                                </div>
                                         </div>
                         </div>
                         <div class="modal custom fade" id="dropUserModal" tabindex="-1"
                                                role="dialog" aria-labelledby="dropUserModal">
                                         <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                       <div class="modal-header">
                                                              <button type="button" class="close close-modal" data-dismiss="modal"
                                                                     aria-label="Close">
                                                                     <span aria-hidden="true">&times;</span>
                                                              </button>
                                                              <h4 class="modal-title" id="dropAccessModalLabel">
                                                                     <spring:message code="users.page.user.dropaccess.popup" />
                                                              </h4>
                                                              <div id="drop_access_msg"  class="control-label"></div>
                                                       </div>
                                                </div>
                                         </div>
                         </div>
                         <div class="modal custom fade" id="disableUserModal" tabindex="-1"
                                                role="dialog" aria-labelledby="disableUserModal">
                                         <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                       <div class="modal-header">
                                                              <button type="button" class="close close-modal" data-dismiss="modal"
                                                                     aria-label="Close">
                                                                     <span aria-hidden="true">&times;</span>
                                                              </button>
                                                              <h4 class="modal-title" id="disableAccessModalLabel">
                                                                     <spring:message code="users.page.user.disableaccess.popup" />
                                                              </h4>
                                                              <div id="disable_access_msg"  class="control-label"></div>
                                                       </div>
                                                </div>
                                         </div>
                         </div>
     
                         <%-- <div class="modal" id="loadingModal" tabindex="-1"
                                                role="dialog" aria-labelledby="LoadingModalLabel">
                                         <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                       <div class="modal-header">
                                                              	<div class="loader">
																	<div>
																		<img src = "<%=img%>/loader.gif">
																		<span>Loading...</span>
																	</div>
																</div>
                                                       </div>
                                                </div>
                                         </div>
                         </div> --%>
						
					</div>
		<table class="table custom-table" id="usersTable" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th><div class="checkbox-without-label" ><input id="selectAllUsers" type="checkbox"><label>default</label></div></th>
								<th><spring:message
										code="admin.page.user.list.table.header.name" /></th>
								<th><spring:message
										code="admin.page.user.list.table.header.email.id" /></th>
								<th><spring:message
										code="admin.page.user.list.table.header.role" /></th>
								<th><spring:message
										code="admin.page.user.list.table.header.pending.notification" /></th>
								<th><spring:message
										code="admin.page.user.list.table.header.user.type" /></th>
								<th><spring:message
										code="admin.page.user.list.table.header.status" /></th>
								<th width="15%"><spring:message
										code="admin.page.user.list.table.header.user.access" /></th>
								<th width="15%"><spring:message
										code="admin.page.user.list.table.header.user.createdOn" /></th>
								<th width="15%"><spring:message
										code="admin.page.user.list.table.header.user.modifiedOn" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${userList}" var="user">
							<c:choose>
								<c:when test="${user.status}">
									<tr id="userRow" data="${user.id}">
								</c:when>
								<c:otherwise>
									<tr class="disabled" id="userRow" data="${user.id}">
								</c:otherwise>	
							</c:choose>
							<c:choose>
								<c:when test="${user.status}">
									<td><div class='checkbox-without-label'><input class='selectUser' type='checkbox' data-otp="${user.otpEnabled}" value="${user.id}"><label>default</label></div></td>
								</c:when>
								<c:otherwise>
									<td><div class='checkbox-without-label'><input class='selectUser' type='checkbox' disabled='true' data-otp="${user.otpEnabled}" value="${user.id}"><label>default</label></div></td>
								</c:otherwise>	
							</c:choose>
							
							<c:choose>
								<c:when test="${(user.status) and (accessUserView)}">
									<td><a href="../user/view/${user.id}">${user.userName}</a></td>
								</c:when>
								<c:otherwise>
									<td>${user.userName}</td>
								</c:otherwise>	
							</c:choose>
									
										
							<td class='userEmail'>${user.emailId}</td>
							
							<c:choose>
								<c:when test="${(user.status) and (accessRoleView)}">
									<td>
										<c:forEach items="${user.roles}" var="role" begin="0" end="1">
										<div class="action-btns-grid"></div>
											<a href="../role/view?id=${role.id}"> ${role.name}</a>
										</c:forEach>
										<!-- More Roles -->
										<c:forEach items="${user.roles}" var="role" begin="2" varStatus="status">
											<c:if test="${status.index eq 2}">
												<a href='javascript:void(0);' class="linkMoreRoles">...More</a>
											</c:if>
											<div class="divMoreRoles action-btns-grid" style="display: none;">
												<a href="../role/view?id=${role.id}"> ${role.name}</a>
											</div>
										</c:forEach>
									</td>
								</c:when>
								<c:otherwise>
									<td>
										<c:forEach items="${user.roles}" var="role" begin="0" end="1">
											<div class="action-btns-grid"> ${role.name}</div>
										</c:forEach>
										<!-- More Roles -->
										<c:forEach items="${user.roles}" var="role" begin="2" varStatus="status">
										<c:if test="${status.index eq 2}">
											<a href='javascript:void(0);' class="linkMoreRoles">...More</a>
											</c:if>
											<div class="divMoreRoles action-btns-grid" style="display: none;">
												${role.name}
											</div>
										</c:forEach>
									</td>
								</c:otherwise>	
							</c:choose>
									
									<td>TBD</td>
									<td>${user.userType.name}</td>
									<c:choose>
										<c:when test="${user.userType.name eq 'TEMP'}">
											<td class = tdUserStatus><div>${user.status=='true' ? "Active" : "Inactive"}</div> <div>${user.endingOn}</div></td>
										</c:when>
										<c:otherwise>
											<td class = tdUserStatus>${user.status=='true' ? "Active" : "Inactive"}</td>
										</c:otherwise>	
									</c:choose>
									<td class="tdUserAccess">
										<c:set var="user" value="${user}" scope="request"/>
										<jsp:include page="userAccessControl.jsp"/>
									</td>
									<td>${user.createdOn}</td>
									<td>${user.modifiedOn}</td>
								</tr>
							</c:forEach>
						</tbody>
				
					</table>
	<div id="dropUserPopup" class="popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hideDropMessage()">&times;</a></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-submit" data-dismiss="modal" onclick="dropUser();hideDropMessage();">YES</button>
                <button type="button" class="btn btn-cancel" onclick="hideDropMessage();">NO</button>
        </div>
	</div> 
	<div id="disableUserPopup" class="popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hideDisableMessage()">&times;</a></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-submit" data-dismiss="modal" onclick="disableUser();hideDisableMessage();">YES</button>
                <button type="button" class="btn btn-cancel" onclick="hideDisableMessage();">NO</button>
        </div>
	</div>
	<div id="generateOtpPopup" class="popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hideOtpMessage()">&times;</a></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-submit" data-dismiss="modal" onclick="hideOtpMessage();">OK</button>
        </div>
	</div> 
	<script>
	$(document).ready(function(){
		createUser();
	});
	</script>
