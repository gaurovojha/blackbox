<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>

<div class="panel-group user-management" id="accordionActions" role="tablist" aria-multiselectable="true">
	<c:forEach var="module" items="${moduleList}" varStatus="count">
		<div class="panel panel-default">
		    <div class="panel-heading clearfix" role="tab" id="adminSection">
		     	 <h4 class="panel-title">
		     	 <span></span>
		         <a role="button" class="collapsed" data-toggle="collapse" data-parent="#accordionActions" href="#adminSectionAcc${count.index}" aria-expanded="true" aria-controls="adminSectionAcc">
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
												<form:checkbox path="accessProfile.accessControlIds" value="${accessControl.accessControlId}" label="${accessControl.name}" disabled="true"></form:checkbox>
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
												<form:checkbox path="accessProfile.accessControlIds" value="${accessControl.accessControlId}" label="${accessControl.name}" disabled="true"></form:checkbox>
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
    </c:forEach>
</div> 