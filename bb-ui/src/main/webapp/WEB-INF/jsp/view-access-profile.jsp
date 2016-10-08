<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>

	<div>
		<sec:authorize access="canAccessUrl('/accessprofile/edit')" var="accessAccessProfileEdit"/>
		<sec:authorize access="canAccessUrl('/accessprofile/delete')" var="accessAccessProfileDelete"/>
	</div>
	
	<c:set var="disableDeleteAccessProfile" value="" scope="page"/>
	<c:if test="${!accessAccessProfileDelete}">
		<c:set var="disableDeleteAccessProfile" value="hidden-control" scope="page"/>
		<%-- <c:set var="disableDeleteAccessProfile" value="disabled" scope="page"/> --%>
	</c:if>

	<c:set var="disableEditAccessProfile" value="" scope="page"/>
	<c:if test="${!accessAccessProfileEdit}">
		<c:set var="disableEditAccessProfile" value="hidden-control" scope="page"/>
		<%-- <c:set var="disableEditAccessProfile" value="disabled" scope="page"/> --%>
	</c:if>
		
	<div class="main-content container">
		<div class="row">
			<div class="col-sm-12">
				<div class="page-header">
					<h2 class="page-heading"><spring:message code="accessprofile.page.view" /></h2>
				</div>
			</div>
		</div>
		<!--create new access profile -->
		<div class="panel-group user-management" id="accordion" role="tablist" aria-multiselectable="true">
			<div class="panel panel-default">
			    <div class="panel-heading" role="tab" id="defineRole">
			      <h4 class="panel-title">
			      	<span></span>
			        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
			        <spring:message code="admin.page.accessprofile.type" /><span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
			        </a>
			      </h4>
			    </div>
			    <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="defineRole">			      
			       <div class="panel-body">
			       <div class="form-horizontal">
			       		<div class="form-group">
			       			<div class="col-sm-6 checkbox-control >input + label">
			       				<label class="control-label"><spring:message code="accessprofile.page.name" /></label>
			       				<input type="text" class="form-control" value="${accessProfile.name}" disabled="disabled">
			       			</div>
			       			<div class="col-sm-6">
		        				<label class="control-label"><spring:message code="accessprofile.page.description" /></label>
		        				<input type="text" class="form-control" value="${accessProfile.description}" disabled="disabled">
			       			</div>
			       		</div>
			       </div>
			      </div>
			    </div>
		    </div>
		</div>
		<!--create new access profile ends-->

		<form:form action="submit" commandName="accessProfile" method="post">
			<c:forEach var="module" items="${moduleList}" varStatus="count">
				<div class="panel-group user-management" id="accordionActions${count.index}" role="tablist" aria-multiselectable="true">
					<div class="panel panel-default">
					    <div class="panel-heading clearfix" role="tab" id="adminSection">
					     	 <h4 class="panel-title">
					     	 <span></span>
					         <a role="button" class="collapsed" data-toggle="collapse" data-parent="#accordionActions${count.index}" href="#adminSectionAcc${count.index}" aria-expanded="true" aria-controls="adminSectionAcc">
					          	${module.name}
					          	<span class="selected"></span>
					          	<span class="icon icon-arrow-down"></span>
					          	<span class="icon icon-arrow-up"></span>
					         </a>
					         </h4>
					    </div>
					    <div id="adminSectionAcc${count.index}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="adminSection">
					      <div class="panel-body">
						        <div class="form-horizontal">
						        	<div class="content mCustomScrollbar height200 blue">
					        			<div class="user-checklist">
						        			<div class="form-group">
						        				<c:forEach var="accessControl" items="${module.accessControlDtos}" varStatus="acCount">
													<c:if test="${accessControl.actionType}">
														<div class="col-sm-3 checkbox-control">
															<form:checkbox class="control-label" path="accessControlIds" value="${accessControl.accessControlId}" label="${accessControl.name}" disabled="true"></form:checkbox>
														</div>
													</c:if>
						        				</c:forEach>
											</div>
										</div>
					        			<div class="user-checklist">
											<div class="form-group">
												<c:forEach var="accessControl" items="${module.accessControlDtos}" varStatus="acCount">
													<c:if test="${!accessControl.actionType}">
														<div class="col-sm-12 checkbox-control">
															<form:checkbox path="accessControlIds" value="${accessControl.accessControlId}" label="${accessControl.name}" disabled="true"></form:checkbox>
														</div>
													</c:if>
						        				</c:forEach>
											</div>
										</div>
									</div>
								</div>
						    </div>
					    </div>
				    </div>
				</div> 
			</c:forEach>
		</form:form>
	    
		<!--form footer-->
		<div class="form-horizontal form-footer">
			<div class="col-sm-12">
				<div class="form-group text-left">
					<c:choose>
						<c:when test="${accessAccessProfileDelete}">
							<c:choose>
								<c:when test="${empty accessProfile.roles && !accessProfile.seeded && accessProfile.active}">
									<input type="button" class="btn btn-submit" value="Drop" onclick="window.location.href = 'delete?id=${accessProfile.id}';">							
								</c:when>
								<c:otherwise>
									<input type="button" class="btn btn-submit disabled" value="Drop">							
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<input type="button" class="btn btn-submit ${disableDeleteAccessProfile}" value="Drop">	
						</c:otherwise>	
					</c:choose>
					
					
						<c:choose>
							<c:when test="${!accessProfile.seeded && accessProfile.active && accessAccessProfileEdit}">
								<input type="button" class="btn btn-submit" value="Edit" onclick="window.location.href = 'edit?id=${accessProfile.id}';">
							</c:when>
							<c:otherwise>
								<input type="button" class="btn btn-submit disabled" value="Edit">
							</c:otherwise>						
						</c:choose>
					<input type="button" class="btn btn-submit" value="Go Back To Admin" onclick="window.location.href = '../admin/';">
				</div>
			</div>
		</div>
	</div>
