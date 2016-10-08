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
	String images = context + "/assets/images";
%>

	<div>
		<sec:authorize access="canAccessUrl('/admin/userDetails')" var="accessUserCountView"/>
		<sec:authorize access="canAccessUrl('/role/view')" var="accessRoleView"/>
		<sec:authorize access="canAccessUrl('/role/create')" var="accessRoleCreate"/>
		<sec:authorize access="canAccessUrl('/role/edit')" var="accessRoleEdit"/>
		<sec:authorize access="canAccessUrl('/role/delete')" var="accessRoleDelete"/>
		<sec:authorize access="canAccessUrl('/accessprofile/view')" var="accessAccessProfileView"/>
	</div>
	
	<c:set var="disableCreateRole" value="" scope="page"/>
	<c:if test="${!accessRoleCreate}">
		<%-- <c:set var="disableCreateRole" value="disabled" scope="page"/> --%>
		<c:set var="disableCreateRole" value="hidden-control" scope="page"/>
	</c:if>

	<div class="tab-heading clearfix">
		<h3 class="pull-left"><spring:message code="admin.page.role.list.table.name" /></h3>
		<button type="button" class="btn btn-submit pull-right ${disableCreateRole}" data-toggle="modal" data-target="#roleModal">
			<spring:message code="admin.page.heading.user.management.role.create.new" />
		</button>
	</div>

	<table id="rolesTable" class="table custom-table">

		<thead>
			<tr>
				<th><spring:message
						code="admin.page.role.list.table.header.name" /></th>
				<th><spring:message
						code="admin.page.role.list.table.header.access.profile" /></th>
				<th><spring:message
						code="admin.page.role.list.table.header.user" /></th>
				<th><spring:message
						code="admin.page.role.list.table.header.created.by" /></th>
				<th><spring:message
						code="admin.page.role.list.table.header.otp.activate" /></th>
				<th><spring:message
						code="admin.page.role.list.table.header.status" /></th>
				<th><spring:message
						code="admin.page.role.list.table.header.actions" /></th>
				<!-- Would be a hidden colomn used for sorting by datatable -->
				<th><spring:message code="admin.page.role.list.table.header.created.date" /></th>
				<!-- Would be a hidden colomn used for sorting by datatable -->
				<th><spring:message code="admin.page.role.list.table.header.modified.date" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${roleList}" var="role">
				<c:choose>
					<c:when test="${role.status == 'Active'}">
						<tr class="odd">
							<td>
								<c:choose>
									<c:when test="${accessRoleView}">
										<a href="../role/view?id=${role.id}"><span id="roleInfo" class="icon icon-info" title="${role.description}" ></span>&nbsp;&nbsp;${role.name}</a>
									</c:when>
									<c:otherwise>
										<span id="roleInfo" class="icon icon-info" title="${role.description}" ></span>&nbsp;&nbsp;${role.name}
									</c:otherwise>
								</c:choose>
							</td>
							
							<td>
								<c:choose>
									<c:when test="${accessAccessProfileView}">
										<a href="../accessprofile/view?id=${role.accessProfile.id}">${role.accessProfile.name}</a>
									</c:when>
									<c:otherwise>
										${role.accessProfile.name}
									</c:otherwise>
								</c:choose>
							</td>
							
							<td id="${role.id}">
								<c:choose>
									<c:when test="${accessUserCountView}">
										<a href="#" data-toggle="modal" data-target="#modalRoles" onclick="showUserPopupForRole(${role.id})">${role.userCount}</a>
									</c:when>
									<c:otherwise>
										${role.userCount}
									</c:otherwise>
								</c:choose>
							</td>
							
							<td>
								${role.createdBy}<br/>${role.createdDate}
							</td>

							<td>
								<c:choose>
									<c:when test="${role.otpActivate}">
										<spring:message code="text.yes" />
									</c:when>
									<c:otherwise>
										<spring:message code="text.no" />
									</c:otherwise>	
								</c:choose>
							</td>

							<td>${role.status}</td>
							
							<td>
								<div class="action-btns-grid">
								<c:choose>
									<c:when test="${!role.seeded}">
										<c:choose>
											<c:when test="${accessRoleEdit}">
												<a href="../role/edit?id=${role.id}"><i><img src="<%=images %>/svg/edit.svg" class="icon16x" alt="Edit"></i> 
													<spring:message code="admin.page.role.edit" />
												</a>
											</c:when>
											<c:otherwise>
												<!-- <span class="icon icon-edit disabled"></span> -->
												<i><img src="<%=images %>/svg/edit.svg"  class="icon16x disabled" alt="Edit"></i>
												<span class="disabled"><spring:message code="admin.page.role.edit" /></span>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${role.userCount == 0 && accessRoleDelete}">
												<a href="javascript:void()" data-href="../role/delete?id=${role.id}" onclick="popupMsgForRole(this)">
												<%-- <span class="icon icon-drop"></span>
												<a href="javascript:void()" data-href="../role/delete?id=${role.id}" data-toggle="modal" data-target="#modalConfirmDeleteRole"><span class="icon icon-drop"></span> 
												<spring:message code="admin.page.role.drop" /> --%>
													<span class="action-danger"><img class="icon16x" alt="Delete" src="<%=images %>/svg/delete.svg"> Delete</span>
												</a>
											</c:when>
											<c:otherwise>
											<span class="disabled-link action-danger">
												<img class="icon16x" alt="Delete" src="<%=images %>/svg/delete.svg"> 
											Delete</span>										
										
												<%-- <span class="icon icon-drop disabled"></span>
												<span class="disabled"><spring:message code="admin.page.role.drop" /></span> --%>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<span class="disabled-link">
											<i><img src="<%=images %>/svg/edit.svg"  class="icon16x disabled" alt="Edit"></i>&nbsp;<spring:message code="text.edit" />
										</span>&nbsp;&nbsp;&nbsp;
										<span class="disabled-link action-danger">
											<img class="icon16x" alt="Delete" src="<%=images %>/svg/delete.svg">&nbsp;<spring:message code="text.delete" />
										</span>		
									</c:otherwise>
								</c:choose>
								</div>
							</td>
							
							<td>${role.createdDate}</td>
							<td>${role.modifiedDate}</td>
						</tr>
				</c:when>
					<c:otherwise>
						<tr class="odd disabled">
							<td>${role.name}</td>
							<td>${role.accessProfile.name}</td>
							<td>${role.userCount}</td>
							<td>${role.createdBy}<br/>${role.createdDate}</td>
							<c:choose>
								<c:when test="${role.otpActivate}">
									<td>Yes</td>
								</c:when>
								<c:otherwise>
									<td>No</td>
								</c:otherwise>	
							</c:choose>
							<td><a href="javascript:void();" onclick="showInactiveRoleInfo('${role.modifiedBy}','${role.modifiedDate}');">${role.status}</a><br/>${role.endDate}</td>							
							<td>
								<span class="disabled-link action-danger">
									<spring:message code="text.deleted" />
								</span>
							</td>
							<td>${role.createdDate}</td>
							<td>${role.modifiedDate}</td>
						</tr>	
					</c:otherwise>	
				</c:choose>
			</c:forEach>
		</tbody>

	</table>
	
	<div class="modal inmodal" id="modalRoles" tabindex="-1" role="dialog" aria-hidden="true">
    	<jsp:text/>
    </div>

	<div class="modal fade" id="modalConfirmDeleteRole" tabindex="-1" role="dialog" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                Delete
	            </div>
	            <div class="modal-body">
	                Do you want to delete this Role ?
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	                <a class="btn btn-danger btn-ok">Delete</a>
	            </div>
	        </div>
	    </div>
	</div>
	
	<div id="popupMsgRole" class="popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hideRolePopUp();">&times;></a></div>
		<div class="content">
			<p class="msg"><spring:message code="admin.page.role.delete.confirmation.msg" /></p>
		</div>
		<div class="modal-footer">
                <a type="button" class="btn btn-submit" data-dismiss="modal" href=""><spring:message code="text.yes" /></a>
                <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hideRolePopUp();"><spring:message code="text.no" /></button>
        </div>
	</div>
	
	<div id="inactiveRoleInfo" class="popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hideInactiveRoleInfo();">&times;</a></div>
		<div class="content">
			<p class="msg">
				<table class="table custom-table">
					<thead>
						<tr>
							<th>User Name</th>
							<th>Inactive Date</th>
						</tr>
					</thead>
					<tbody>
						<tr class="odd">
							<td id="inactiveRoleUser"></td>
							<td id="inactiveDate"></td>
						</tr>
					</tbody>
				</table>
			</p>
		</div>
		<div class="modal-footer">
               <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hideInactiveRoleInfo();"><spring:message code="button.close" /></button>
        </div>
	</div>
				
	<script type="text/javascript" src="<%=js%>/admin/admin.js"></script>