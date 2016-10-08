<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> 
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String js = context + "/assets/js";
%>

<div>
	<sec:authorize access="canAccessUrl('/admin/listuser')" var="accessUser"/>
	<sec:authorize access="canAccessUrl('/admin/listaccessprofile')" var="accessAccessProfile"/>
	<sec:authorize access="canAccessUrl('/admin/listrole')" var="accessRole"/>
</div>

<c:set var="activeUserTab" value="false" scope="page"/>
<c:set var="activeAccessProfileTab" value="false" scope="page"/>
<c:set var="activeRoleTab" value="false" scope="page"/>

<c:if test="${accessUser && userActive}" var="activeUserTab"/>

<c:if test="${!activeUserTab}">
	<c:if test="${!activeUserTab && accessProfileActive}">
		<c:if test="${accessAccessProfile}" var="activeAccessProfileTab"/>
	</c:if>
	
	<c:if test="${!activeAccessProfileTab && roleActive}">
		<c:if test="${accessRole}" var="activeRoleTab"/>
	</c:if>
</c:if>

	<div class="main-content container">
		<div class="tab-container">
			<ul class="tab-actions pull-right">
				<li><div class="input-group inner-search">
						<span class="input-group-btn"><button class="search">
							<span class="icon icon-search-inner"></span></button>
						</span>
						<input type="text" placeholder="Search" id="searchText">
					</div>
				</li>
				<li><a href="" class="export"><span class="icon icon-export"></span>Export</a></li>
			</ul>
			<!-- Nav tabs -->
			<ul class="nav nav-tabs custom-tabs" role="tablist">
				
					<c:choose>
						<c:when test="${activeRoleTab}">
							<li id="roleList" role="presentation" class="active">
						</c:when>
						<c:otherwise>
							<li id="roleList" role="presentation">
						</c:otherwise>
					</c:choose>
				
					<c:choose>
						<c:when test="${accessRole}">
							<a href="#roleTab" role="tab" data-toggle="tab">
								<spring:message code="admin.page.heading.user.management.role" />
							</a>
						</c:when>
						<c:otherwise>
							<div class="hidden-control">
								<spring:message code="admin.page.heading.user.management.role" />
							</div>
						</c:otherwise>
					</c:choose>
				</li>
				
				<c:choose>
					<c:when test="${activeAccessProfileTab}">
						<c:set var="active" value="active" scope="page"/>
					</c:when>
					<c:otherwise>
						<c:set var="active" value="" scope="page"/>
					</c:otherwise>
				</c:choose>
				<li id="roleList" role="presentation" class="${active}">
					<c:choose>
						<c:when test="${accessAccessProfile}">
							<a href="#accessProfileTab" role="tab" data-toggle="tab">
								<spring:message code="admin.page.heading.user.management.access.profile" />
							</a>
						</c:when>
						<c:otherwise>
							<div class="hidden-control">
								<spring:message code="admin.page.heading.user.management.access.profile" />
							</div>
						</c:otherwise>
					</c:choose>
				</li> 
				
				<c:choose>
						<c:when test="${activeUserTab}">
							<li id="userList" role="presentation" class="active">
						</c:when>
						<c:otherwise>
							<li id="roleList" role="presentation">
						</c:otherwise>
					</c:choose>
				
					<c:choose>
						<c:when test="${accessUser}">
							<a href="#userTab" role="tab" id="usersTab" data-toggle="tab">
								<spring:message code="admin.page.heading.user.management.user" />
							</a>
						</c:when>
						<c:otherwise>
							<div class="hidden-control">
								<spring:message code="admin.page.heading.user.management.user" />
							</div>
						</c:otherwise>
					</c:choose>
				</li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">							
			
					<c:choose>
						<c:when test="${activeRoleTab}">
							<div role="tabpanel" class="tab-pane active" id="roleTab">
						</c:when>
						<c:otherwise>
							<div role="tabpanel" class="tab-pane" id="roleTab">
						</c:otherwise>
					</c:choose>
					<c:if test="${accessRole}">
						<jsp:include page="list-role.jsp"></jsp:include>
					</c:if>	
				</div>
					
					<c:choose>
						<c:when test="${activeAccessProfileTab}">
							<div role="tabpanel" class="tab-pane active" id="accessProfileTab">
						</c:when>
						<c:otherwise>
							<div role="tabpanel" class="tab-pane" id="accessProfileTab">
						</c:otherwise>
					</c:choose>	
				
					<c:if test="${accessAccessProfile}">
						<jsp:include page="list-access-profile.jsp"></jsp:include>
					</c:if>
				</div>
				
				
				<c:choose>
						<c:when test="${activeUserTab}">
							<div role="tabpanel" class="tab-pane active" id="userTab">
						</c:when>
						<c:otherwise>
							<div role="tabpanel" class="tab-pane" id="userTab">
						</c:otherwise>
					</c:choose>	
				<c:if test="${accessUser}">
					<jsp:include page="list-user.jsp"></jsp:include>
				</c:if>
				</div>
			</div>
		</div>
	</div>

	<!-- create new role modal -->
	<div class="modal custom fade modal-wide" id="roleModal" tabindex="-1"
		role="dialog" aria-labelledby="roleModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="roleModalLabel">
						<spring:message code="admin.page.role.select.role" />
					</h4>
				</div>
				<div class="modal-body">
					<div class="form-horizontal">
						<div class="form-group">
        					<div class="col-sm-4 col-lg-3">
        						<input type="radio" id="duplicateRole" name="radioRole" value="duplicate role"><label class="control-label" for="duplicateRole">
        						<spring:message code="admin.page.role.create.duplicate" /></label>
        					</div>
			        		<div class="col-sm-4 col-lg-3">
			        			<input type="radio" id="newRole" name="radioRole" value="new role"><label class="control-label" for="newRole">
			        			<spring:message code="admin.page.role.create.new" /></label>
			        		</div>
						</div>
        			
					<div class="form-group">
						<div id="duplicateRoleContainer" class="col-sm-12">
							<table id="rolePopupTable" class="table custom-table">
								<thead>
									<tr>
										<th><spring:message
												code="admin.page.role.list.table.header.name" /></th>
										<th><spring:message
												code="admin.page.role.list.table.header.access.profile" /></th>
										<th><spring:message
												code="admin.page.role.list.table.header.created.by" /></th>										
										<th><spring:message
												code="admin.page.role.list.table.header.otp.activate" /></th>
										<th><spring:message
												code="admin.page.role.list.table.header.status" /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${roleList}" var="role">
										<c:if test="${role.status == 'Active' }">
											<tr class="odd">
												<td><input type="radio" id="${role.name}" name="actionRole" value="${role.id}"><label class="control-label" for="${role.name}">${role.name}</label></td>
												<td>By ${role.accessProfile.name}</td>
												<td>${role.createdBy}<br />${role.createdDate}</td>
												<td><c:choose>
														<c:when test="${role.otpActivate}">
															Yes
														</c:when>
														<c:otherwise>
															No
														</c:otherwise>
													</c:choose>
												</td>
												<td>${role.status}</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>

							</table>
						</div>
					</div>
				</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-cancel" data-dismiss="modal">
						<spring:message code="button.cancel" />
					</button>
					<button type="button" class="btn btn-submit" id="btnRole">
						<spring:message code="button.submit" />
					</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal custom fade modal-wide" id="accessProfileModal" tabindex="-1"
		role="dialog" aria-labelledby="accessProfileModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="accessProfileModalLabel">
						<spring:message code="admin.page.accessprofile.select.accessprofile" />
					</h4>
				</div>
				<div class="modal-body">
					<div class="form-horizontal">
						<div class="form-group">
        					<div class="col-sm-4 col-lg-3">
        						<input type="radio" id="duplicateAccessProfile" name="radioAccessProfile" value="duplicate access profile"><label class="control-label" for="duplicateAccessProfile">
        						<spring:message code="admin.page.accessprofile.create.duplicate" /></label>
        					</div>
			        		<div class="col-sm-4 col-lg-3">
			        			<input type="radio" id="newAccessProfile" name="radioAccessProfile" value="new access profile"><label class="control-label" for="newAccessProfile">
			        			<spring:message code="admin.page.accessprofile.create.new" /></label>
			        		</div>
						</div>
					
						<div class="form-group">
							<div id="duplicateAccessProfileContainer" class="col-sm-12">
								<table id="accessProfilePopupTable" class="table custom-table">
									<thead>
										<tr>
											<th><spring:message
													code="admin.page.access.profile.list.table.header.name" /></th>
											<th><spring:message
													code="admin.page.access.profile.list.table.header.role" /></th>
											<th><spring:message
													code="admin.page.access.profile.list.table.header.create.by" /></th>	
											<th><spring:message
													code="admin.page.access.profile.list.table.header.status" /></th>								
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${accessProfileList}" var="accessProfile">
											<c:if test="${accessProfile.active}">
												<tr class="odd">
													<td><input type="radio" id="${accessProfile.name}" name="actionAccessProfile" value="${accessProfile.id}" /><label class="control-label" for="${accessProfile.name}">
		        									${accessProfile.name}</label></td>											
													<td>
														<c:forEach items="${accessProfile.roles}" var="role">
															${role.name}<br/>
														</c:forEach>
													</td>
													<td>${accessProfile.createdBy}<br/>${accessProfile.createdDate}</td>
													<td>
														Active
													</td>
												</tr>
											</c:if>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-cancel" data-dismiss="modal">
						<spring:message code="button.cancel" />
					</button>
					<button type="button" class="btn btn-submit" id="btnAccessProfile">
						<spring:message code="button.submit" />
					</button>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="<%=js%>/admin/admin.js"></script>
	<script type="text/javascript" src="<%=js%>/admin/role.js"></script>
	<script type="text/javascript" src="<%=js%>/admin/user.js"></script>
	<script type="text/javascript" src="<%=js%>/admin/access-profile.js"></script>
	
	<script type="text/javascript">

		$(function () {
	
			$("#duplicateRoleContainer").hide();
			$("#duplicateAccessProfileContainer").hide();
			
			$("#duplicateRole").on("click", function() {
				$("#duplicateRoleContainer").show();
			});
			
			$("#newRole").on("click", function() {
				$("#duplicateRoleContainer").hide();
			});
			
			$("#duplicateAccessProfile").on("click", function() {
				$("#duplicateAccessProfileContainer").show();
			});
			
			$("#newAccessProfile").on("click", function() {
				$("#duplicateAccessProfileContainer").hide();
			});
			
			prepareDataTable();
	
		});
	</script>
