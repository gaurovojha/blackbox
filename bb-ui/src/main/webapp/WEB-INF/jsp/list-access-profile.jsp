<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
	<sec:authorize access="canAccessUrl('/accessprofile/view')" var="accessAccessProfileView"/>
	<sec:authorize access="canAccessUrl('/accessprofile/create')" var="accessAccessProfileCreate"/>
	<sec:authorize access="canAccessUrl('/accessprofile/edit')" var="accessAccessProfileEdit"/>
	<sec:authorize access="canAccessUrl('/accessprofile/delete')" var="accessAccessProfileDelete"/>
</div>

<c:set var="disableCreateAccessProfile" value="" scope="page"/>
<c:if test="${!accessAccessProfileCreate}">
	<c:set var="disableCreateAccessProfile" value="hidden-control" scope="page"/>
	<%-- <c:set var="disableCreateAccessProfile" value="disabled" scope="page"/> --%>
</c:if>

<div class="tab-heading clearfix">
	<h3 class="pull-left"><spring:message code="admin.page.access.profile.list.table.name" /></h3>
	<button type="button" class="btn btn-submit pull-right ${disableCreateAccessProfile}" data-toggle="modal" id="createNewAccess" data-target="#accessProfileModal" >
		<spring:message code="admin.page.accessprofile.create.new" />
	</button>
</div>
<table id="accessProfileTable" class="table custom-table">
	<thead>
		<tr>
			<th>
				<spring:message code="admin.page.access.profile.list.table.header.name" />
			</th>
			<th>
				<spring:message code="admin.page.access.profile.list.table.header.role" />
			</th>
			<th>
				<spring:message code="admin.page.access.profile.list.table.header.user" />
			</th>
			<th>
				<spring:message code="admin.page.access.profile.list.table.header.create.by" />
			</th>
			<th>
				<spring:message code="admin.page.access.profile.list.table.header.status" />
			</th>
			<th>
				<spring:message code="admin.page.access.profile.list.table.header.actions" />
			</th>
			<!-- Would be a hidden column used for sorting by datatable -->
			<th>
				<spring:message code="admin.page.access.profile.list.table.header.created.date" />
			</th>
			<!-- Would be a hidden column used for sorting by datatable -->
			<th>
				<spring:message code="admin.page.access.profile.list.table.header.modified.date" />
			</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${accessProfileList}" var="accessProfile">
			<c:choose>
				<c:when test="${accessProfile.active}">
					<tr class="odd">
						<td>
							<c:choose>
								<c:when test="${accessAccessProfileView}">
									<a href="../accessprofile/view?id=${accessProfile.id}">
										<span class="icon icon-info" title="${accessProfile.description}"></span>&nbsp;&nbsp;${accessProfile.name}
									</a>
								</c:when>
								<c:otherwise>
									<span class="icon icon-info" title="${accessProfile.description}"></span>&nbsp;&nbsp;${accessProfile.name}
								</c:otherwise>
							</c:choose>
						</td>	
						<td>
							<c:choose>
								<c:when test="${accessRoleView}">
									<c:forEach items="${accessProfile.roles}" var="role" begin="0" end="1">
												<a href="../role/view?id=${role.id}">${role.name}<br/></a>
									</c:forEach>
									<!-- More Roles -->
									<c:forEach items="${accessProfile.roles}" var="role" begin="2" varStatus="status">
										<c:if test="${status.index eq 2}">
											<a href='javascript:void(0);' class="linkMoreRoles">...Show More</a>
										</c:if>
										<span class="spanMoreRoles" style="display: none;">
											<a href="../role/view?id=${role.id}">${role.name}</a>
											<br/>
										</span>	
									</c:forEach>
									
								</c:when>	
									<c:otherwise>
										<c:forEach items="${accessProfile.roles}" var="role" begin="0" end="1">
											${role.name}<br/>
										</c:forEach>
										<c:forEach items="${accessProfile.roles}" var="role" begin="2" varStatus="status">
											<c:if test="${status.index eq 2}">
												<span class="linkMoreRoles">...Show More</span>
											</c:if>
											<span class="spanMoreRoles" style="display: none;">
												${role.name}<br/>
											</span>
										</c:forEach>
									</c:otherwise>
							</c:choose>
						</td>
						
						<td>
							<c:choose>
								<c:when test="${accessUserCountView}">
									<a href="#" data-toggle="modal" data-target="#modalAccessProfiles" onclick="showUserPopupForAccessProfile(${accessProfile.id});">${accessProfile.userCount}</a>
								</c:when>
								<c:otherwise>
									${accessProfile.userCount}
								</c:otherwise>
							</c:choose>
						</td>
						
						<td>${accessProfile.createdBy}<br/>${accessProfile.createdDate}</td>
						
						<td>
							<spring:message code="text.active" />
						</td>
						
						<td>
							<div class="action-btns-grid">
								<c:choose>
								<c:when test="${!accessProfile.seeded}">
									<c:choose>
										<c:when test="${accessAccessProfileEdit}">
												<a href="../accessprofile/edit?id=${accessProfile.id}"><i><img src="<%=images %>/svg/edit.svg" class="icon16x" alt="Edit"></i>&nbsp;<spring:message code="text.edit" /></a>
										</c:when>
										<c:otherwise>
											<!-- <span class="icon icon-edit disabled"></span> -->
											<i><img src="<%=images %>/svg/edit.svg"  class="icon16x disabled" alt="Edit"></i>
											<span class="disabled">&nbsp;<spring:message code="text.edit" /></span>
										</c:otherwise>
									</c:choose>
									
									<c:choose>
										<c:when test="${empty accessProfile.roles && accessAccessProfileDelete}">
											<a href="javascript:void()" data-href="../accessprofile/delete?id=${accessProfile.id}" onclick="popupMsgForProfile(this)">
											<%-- <a href="javascript:void()" data-href="../accessprofile/delete?id=${accessProfile.id}" data-toggle="modal" data-target="#modalConfirmDeleteProfile"> --%>
											<%-- <span class="icon icon-drop"></span><spring:message code="text.drop" /> --%>
											<span class="action-danger"><img class="icon16x" alt="Delete" src="<%=images %>/svg/delete.svg">&nbsp;<spring:message code="text.delete" /></span>
											</a>
										</c:when>
										<c:otherwise>
											<%-- <span class="icon icon-drop disabled"></span><span class="disabled"><spring:message code="text.drop" /></span> --%>
											<span class="disabled-link action-danger">
												<img class="icon16x " alt="Delete" src="<%=images %>/svg/delete.svg">&nbsp;<spring:message code="text.delete" />
											</span>
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
						<td>
							${accessProfile.createdDate}
						</td>
						<td>
							${accessProfile.updatedDate}
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr class="odd disabled">
						<td>
							${accessProfile.name}
						</td>
						<td>
							<c:forEach items="${accessProfile.roles}" var="role">
									${role.name}<br/>
							</c:forEach>	
						</td>
						<td>
							${accessProfile.userCount}
						</td>
						<td>
							${accessProfile.createdBy}<br />${accessProfile.createdDate}
						</td>
						<td>
							<spring:message code="text.inactive" /><br/>
							${accessProfile.endDate}
						</td>
						<td>
							<span class="disabled-link action-danger"><spring:message code="text.deleted" /></span>
						</td>
						<td>
							${accessProfile.createdDate}
						</td>
						<td>
							${accessProfile.updatedDate}
						</td>
					</tr>		
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</tbody>
</table>

<div class="modal inmodal" id="modalAccessProfiles" tabindex="-1" role="dialog" aria-hidden="true">
	<jsp:text />
</div>

<div class="modal fade" id="modalConfirmDeleteProfile" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <spring:message code="text.delete" />
            </div>
            <div class="modal-body">
                <spring:message code="admin.page.accessprofile.delete.confirmation.msg" />
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="text.no" /></button>
                <a class="btn btn-danger btn-ok"><spring:message code="text.yes" /></a>
            </div>
        </div>
    </div>
</div>

<div id="popupMsgProfile" class="popup-msg">
	<div class="text-right"><a class="close" href="#" onclick="hideProfilePopUp();">&times;</a></div>
	<div class="content">
		<p class="msg"><spring:message code="admin.page.accessprofile.delete.confirmation.msg" /></p>
	</div>
	<div class="modal-footer">
		<a type="button" class="btn btn-submit" data-dismiss="modal" href=""><spring:message code="text.yes" /></a>
        <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hideProfilePopUp();"><spring:message code="text.no" /></button>
	</div>
</div>

	<div class="modal fade" role="dialog" id="modalPopupRole" tabindex="-1" aria-hidden="true">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="roleModalLabel"><spring:message code="admin.page.role.popup.title" /></h4>
			</div>
			<div class="modal-body">
				<div class="clearfix">
					<div id="roleListContainer">
						<table class="table custom-table">
							<thead>
								<tr>
									<th><spring:message code="admin.page.role.popup.header.name" /></th>
									<th><spring:message	code="admin.page.role.popup.header.status" /></th>
									<th><spring:message	code="admin.page.role.popup.header.enddate" /></th>
									<th><spring:message	code="admin.page.role.popup.header.otp" /></th>
									<th><spring:message	code="admin.page.role.popup.header.created.by" /></th>
								</tr>
							</thead>
								<tr>
									<th>TBD</th>
									<th>TBD</th>
									<th>TBD</th>
									<th>TBD</th>
									<th>TBD</th>
								</tr>
							<tbody>
								<c:forEach items="${accessProfile.roles}" var="role">
									<tr class="odd">
										<td>${role.name}</td>
										<td>${role.status}</td>
										<td>${role.endDate}</td>
										<td>${role.otpActivate}</td>
										<td>${role.createdBy}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="modal-footer">					
				<button type="button" class="btn btn-submit" id="roleDetails" data-dismiss="modal">
					<spring:message code="button.close" />
				</button>
			</div>
		</div>
	</div>
	
<script type="text/javascript" src="<%=js%>/admin/admin.js"></script>