<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>

	<form:form action="update" commandName="accessProfileForm" method="post">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<div class="main-content container">
			<div class="row">
				<div class="col-sm-12">
					<div class="page-header">
						<span class="pull-right form-fields-tip">All <span class="asterisk">*</span><spring:message code="role.page.fields.mandatory.message" /></span>
						<h2 class="page-heading"><spring:message code="accessprofile.page.edit.existing" /></h2>
					</div>
				</div>
			</div>
			<!--Edit role-->
			<div class="panel-group user-management" id="accordion" role="tablist" aria-multiselectable="true">
				<div class="panel panel-default">
				    <div class="panel-heading" role="tab" id="defineRole">
				      <h4 class="panel-title">
				      	<span></span>
				        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
				        <spring:message code="admin.page.accessprofile.type" /><span class="asterisk">*</span><span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
				        </a>
				      </h4>
				    </div>
				    <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="defineRole">
				      <div class="panel-body">
				       <div class="form-horizontal">
				       		
				       		<div class="form-group">
				       			<div class="col-sm-6">
				       				<form:label class="control-label" path="name"><spring:message code="accessprofile.page.name" /></form:label>
				       				<form:input type="text" class="form-control" path="name"></form:input>
				       				<form:errors path="name" class="error"></form:errors>
				       			</div>
				       			<div class="col-sm-6">
				       				<small class="pull-right pad-top10"><spring:message code="label.max.words" /></small>
			        				<form:label class="control-label" path="description"><spring:message code="accessprofile.page.description" /></form:label>
			        				<form:input type="text" class="form-control" path="description"></form:input>
			        				<form:errors path="description" class="error"></form:errors>
				       			</div>
				       		</div>
				       </div>
				      </div>
				    </div>
			    </div>
	
				<div class="panel panel-default">  
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
																			<form:checkbox path="accessControlIds" value="${accessControl.accessControlId}" label="${accessControl.name}"></form:checkbox>
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
																			<form:checkbox path="accessControlIds" value="${accessControl.accessControlId}" label="${accessControl.name}"></form:checkbox>
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
			  	</div>
			</div><!--create new role ends-->
			<!--form footer-->
			<div class="form-horizontal form-footer">
				<div class="col-sm-12">
					<div class="form-group">
						<form:button type="button" class="btn btn-cancel" onclick="popupMsgForCancelSaveProfile(this);"><spring:message code="button.cancel" /></form:button>
						<form:button id="createAccessProfile" value="update" class="btn btn-submit"><spring:message code="button.submit" /></form:button>
						<%-- <form:button type="button" class="btn btn-cancel" value="cancel" data-toggle="modal" data-target="#modalCancelSaveProfile" data-href="window.location.href = '../admin/'">Cancel</form:button> --%>
					</div>
				</div>
			</div>
		</div>
	</form:form>
	
	<!-- Not used as of now. Just a hidden model. -->
	<div class="modal fade" id="modalCancelSaveProfile" tabindex="-1" role="dialog" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <spring:message code="text.cancel" />
	            </div>
	            <div class="modal-body">
	                <spring:message code="text.cancel.confirmation" />
	            </div>
	            <div class="modal-footer">
	                <button class="btn btn-danger btn-ok" ><spring:message code="text.yes" /></button>
	                <a class="btn btn-default" data-dismiss="modal"><spring:message code="text.no" /></a>
	            </div>
	        </div>
	    </div>
	</div>
	
	<div id="popupMsgCancelSaveProfile" class="popup-msg">
		<div class="text-right"><a class="close" href="#">&times;</a></div>
		<div class="content">
			<p class="msg"><spring:message code="text.cancel.confirmation" /></p>
		</div>
		<div class="modal-footer">
	               <a type="button" class="btn btn-submit" data-dismiss="modal" href="../admin/"><spring:message code="text.yes" /></a>
	               <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hideProfileCancelSavePopUp();"><spring:message code="text.no" /></button>
	       </div>
	</div>
	
	
	<script type="text/javascript" src="<%=js%>/admin/access-profile.js"></script>