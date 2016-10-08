<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

<!DOCTYPE html>
<%
       String context = request.getContextPath();
       String js = context + "/assets/js";
       String img = context + "/assets/images";
%>
<div>
		<sec:authorize access="canAccessUrl('/user/edit')" var="accessUserEdit"/>
		<sec:authorize access="canAccessUrl('/user/drop')" var="accessUserDrop"/>
</div>

       <div class="main-content container">
              <div class="row">
                     <div class="col-sm-12">
                           <div class="page-header">
                                  <span class="pull-right form-fields-tip">All <span class="asterisk">*</span> marked fields are compulsory</span>
                                  <h2 class="page-heading">View User</h2>
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
                                                              <h4 class="modal-title" id="OTPModalLabel">
                                                                     <spring:message code="users.page.user.dropaccess.popup" />
                                                              </h4>
                                                              <div id="drop_access_msg"  class="control-label"></div>
                                                       </div>
                                                </div>
                                         </div>
          </div>

<div id="dropUserPopup" class="popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hideDropMessage()">&times;</a></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="dropUser();hideDropMessage();">YES</button>
                <button type="button" class="btn btn-submit" onclick="hideDropMessage();">NO</button>
        </div>
	</div> 
              <div class="row">
              <div class="col-sm-7 mdm-right-pad">
              <form:form class="form-horizontal" method="post" commandName="userForm" action="">
                           <div class="form-group">
                                  <form:hidden path="id"/>
                                  <div class="col-sm-4">
                                         <label class="control-label">Last Name: <span class="required">*</span></label>
                                         <form:input type="text" class="form-control" path="lastName" disabled='true'/>
                                         <form:errors path="lastName" class="error" />
                                  </div>
                                  <div class="col-sm-4">
                                         <label class="control-label">Middle Name </label>
                                         <form:input type="text" class="form-control" path="middleName" disabled='true'/>
                                  </div>
                                  <div class="col-sm-4">
                                         <label class="control-label">First Name <span class="required">*</span></label>
                                         <form:input type="text" class="form-control" path="firstName" disabled='true'/>
                                         <form:errors path="firstName" class="error" />
                                  </div>
                            </div>
                            <div class="form-group">
                                  <div class="col-sm-8">
                                         <label class="control-label">Email id: <span class="required">*</span></label>
                                         <form:input type="text" class="form-control" path="emailId" disabled='true'/>
                                         <form:errors path="emailId" class="error" />
                                  </div>
                                  <div class="col-sm-4">
                                         <label class="control-label">Nationality <span class="required">*</span></label>
                                         <form:select class="form-control" path="nationality.id" disabled='true'>
                                                <form:option value="${userForm.nationality.id}">${userForm.nationality.name}</form:option>
                                         </form:select>
                                  </div>
                            </div>
                            <div class="form-group" >
                                  <div class="col-sm-8">
                                         <label class="control-label">Select Role: <span class="required">*</span></label>
                                                <form:select class="form-control" multiple="true" path="roleIds" disabled='true'>
                                                       <c:forEach var="role" items="${userForm.roles}">
                                                              <c:set var="contains" value="false" />
                                                                     <c:forEach var="userRole" items="${userForm.userRoles}">
                                                                       <c:if test="${userRole eq role.id}">
                                                                         <c:set var="contains" value="true" />
                                                                       </c:if>
                                                                     </c:forEach>
                                                              
                                                                     <c:choose>
                                                                     <c:when test="${contains}">
                                                                            <form:option value="${role.id}" selected='selected'>${role.name}</form:option>
                                                                     </c:when>
                                                                     <c:otherwise>
                                                                            <form:option value="${role.id}">${role.name}</form:option>
                                                                     </c:otherwise>
                                                              </c:choose>
                                                       </c:forEach>
                                         </form:select>
                                         <form:errors path="roleIds" class="error" />
                                  </div>
                            </div>
                            
                            <div class="divider"></div>
                            <div class="form-group">
                                  <div class="col-sm-6">
                                         <label class="control-label">Employee Id</label>
                                         <form:input type="text" class="form-control" path="employeeId" disabled='true'/>
                                  </div>
                                  <div class="col-sm-6">
                                         <label class="control-label">Designation</label>
                                         <form:input type="text" class="form-control" path="designation" disabled='true'/>
                                  </div>
                            </div>
                            <div class="divider"></div>
                            <div class="form-group">
                                  <div class="col-sm-6">
                                         <label class="control-label">User Type <span class="required">*</span></label>
                                         <form:select class="form-control" multiple="false" path="userType.id" disabled='true'>
                                                <form:option value="${userForm.userType.id}">${userForm.userType.name}</form:option>
                                         </form:select>
                                  </div>
                                  <div class="col-sm-4">
                                         <label class="control-label">Ending On</label>
                                         <div class='input-group date' id='datetimepicker1'>
                                         <form:input type='text' class="form-control" path="endingOn" disabled='true'/>
                                         <span class="input-group-addon">
                                             <span class="glyphicon glyphicon-calendar" data-alt="calendar"></span>
                                         </span>
                                     </div>
                                  </div>
                            </div>
                            <div class="divider"></div>
                            
                            <div class="form-group form-footer">
                                  <div class="col-sm-12 text-left">
                                         <form:button type="reset" class="btn btn-cancel" data-dismiss="modal" onclick="window.location.href = '../../admin/';">Cancel</form:button>
                                         <c:choose>
                                                       <c:when test="${(userForm.status) and accessUserEdit}">
                                                       		<input type="button" class="btn btn-submit" value="Edit User" onclick="window.location.href='../../user/edit/${userForm.id}';"/>
                                                       </c:when>
                                                       <c:when test="${not accessUserEdit}">
                                                       		<input type="button" class="btn btn-submit hidden-control" value="Edit User" onclick="window.location.href='../../user/edit/${userForm.id}';"/>
                                                       </c:when>
                                                       <c:otherwise>
                                                       		<input type="button" class="btn btn-submit disabled" value="Edit User" onclick="window.location.href='../../user/edit/${userForm.id}';"/>
                                                       </c:otherwise>       
                                                </c:choose>
                                                <c:choose>
                                                       <c:when test="${(userForm.status) and accessUserDrop}">
                                                       		<input type="button" class="btn btn-submit dropUserIcon" value="Drop User" id="drop_access" data-id="${userForm.id}" data-email="${userForm.emailId}"/>
                                                       </c:when>
                                                       <c:when test="${not accessUserDrop}">
                                                       		<input type="button" class="btn btn-submit hidden-control" value="Drop User" id="drop_access" data-id="${userForm.id}" data-email="${userForm.emailId}"/>
                                                       </c:when>
                                                       <c:otherwise>
                                                              <input type="button" class="btn btn-submit disabled" value="Drop User" id="drop_access" data-id="${userForm.id}" data-email="${userForm.emailId}"/>
                                                       </c:otherwise>       
                                                </c:choose>
                                                
                                  </div>
                            </div>
                            </form:form>
                     </div>
              <div class="col-sm-5">
                     
              </div>
        </div>
       </div>

       <script type="text/javascript" src="<%=js%>/admin/user.js"></script>
       <script>
	$(document).ready(function(){
		dropUserIcon();
	});
	</script>
<body>
