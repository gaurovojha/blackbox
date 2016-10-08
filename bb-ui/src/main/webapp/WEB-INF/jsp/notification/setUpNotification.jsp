<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<html>
  <body>
      <div class="main-content container">
         <div class="row">
            <div class="col-sm-12">
               <div class="page-header">
                  <span class="pull-right form-fields-tip">All <span class="asterisk">*</span> marked fields are compulsory</span>
                  <h2 class="page-heading">Setup Notification</h2>
               </div>
            </div>
         </div>
         <div class="row">
            <div class="col-sm-7">
               <form:form cssClass="form-horizontal" action="../notification/submitNotificationForm" method="Post" commandName="setUpNotificationObject" >
                  <div class="form-group">
                     <div class="col-sm-6">
                        <label class="control-label">Notification Code</label>
                        <p>${setUpNotificationObject.notificationcode}</p>
                        
                        <form:hidden path="notificationId" value="${setUpNotificationObject.notificationId}" />
                        <form:hidden path="notificationcode" value="${setUpNotificationObject.notificationcode}" />
                        <form:hidden path="notificationLevelNo" value="${setUpNotificationObject.notificationLevelNo}" />
                     </div>
                     <div class="col-sm-6">
                        <label class="control-label">Notification Name  <span class="required">*</span></label>
                        <form:input path="notificationName" value="${setUpNotificationObject.notificationName}" label="Enter your first name:" class="form-control" />
                     <form:errors path="notificationName" class="error" />
                     </div>
                  </div>
                  <div class="form-group">
                     <div class="col-sm-6">
                        <label class="control-label">Notification Subject <span class="required">*</span></label>
                        <p>${setUpNotificationObject.notificationSubject}</p>
                        <form:hidden path="notificationSubject" value="${setUpNotificationObject.notificationSubject}" />
                     </div>
                  </div>
                  <div class="form-group">
                     <div class="col-sm-12">
                        <label class="control-label">Notification Message  <span class="required">*</span></label>
                        <form:textarea class="form-control" rows="4"  cols ="4" path="notificationMessage"  value="${setUpNotificationObject.notificationMessage}" />
                        <form:errors path="notificationMessage" class="error" />
                     </div>
                  </div>
                  <div class="form-group">
                     <div class="col-sm-6">
                        <label class="control-label">Notification Type<span class="required"></span></label>
                        <p>${setUpNotificationObject.notificationType}</p>
                        <form:hidden path="notificationType" value="${setUpNotificationObject.notificationType}" />
                     </div>
                  </div>
                  <div class="form-group">
                     <div class="col-sm-6">
                        <label class="control-label">Display while preparing IDS <span class="required">*</span></label>
                        <div class="switch-control">
                           <span class="switch-text">Yes</span>
                           <label class="switch">
                              <input type="checkbox" id="displayOnIDSReview" name="displayOnIDSReview"  class="switch-input" 
                              <c:if test="${setUpNotificationObject.displayOnIDSReview =='Yes'}">
                                 <c:out value= "checked=checked"/>
                              </c:if>
                              >
                              <span class="switch-label" data-on="Parent" data-off="School"></span>
                              <span class="switch-handle"></span>
                           </label>
                           <span class="switch-text">No</span>
                        </div>
                     </div>
                     <div class="col-sm-6">
                        <label class="control-label">Send E- mail for this Notification <span class="required">*</span></label>
                        <div class="switch-control">
                           <span class="switch-text">Yes</span>
                           <label class="switch">
                              <input type="checkbox" id="emailNotification" name="emailNotification"   class="switch-input"  
                              <c:if test="${setUpNotificationObject.emailNotification =='Yes'}">
                                 <c:out value= "checked=checked"/>
                              </c:if>
                              ></input>
                              <span class="switch-label" data-on="Parent" data-off="School"></span>
                              <span class="switch-handle"></span>
                           </label>
                           <span class="switch-text">No</span>
                        </div>
                     </div>
                  </div>
                  <div class="form-group">
                     <div class="col-sm-6">
                       <div class="form-group">
                       		<div class="col-sm-12">
                       			<label class="control-label">Number of Reminders: <span class="required">*</span></label>
		                        <div class="switch-control">
		                           <span class="switch-text">Yes</span>
		                           <label class="switch">
		                              <input type="checkbox" id="reminder" name="reminder"  class="switch-input"  
		                              <c:if test="${setUpNotificationObject.reminder =='Yes'}">
		                                 <c:out value= "checked=checked"/>
		                              </c:if>
		                              ></input>
		                              <span class="switch-label" data-on="Parent" data-off="School"></span>
		                              <span class="switch-handle"></span>
		                           </label>
		                           <span class="switch-text">No</span>
		                        </div>
                       		</div>
                       </div>
                        
                        <div id="reminderContainer"  
                        <c:if test="${setUpNotificationObject.reminder =='No'}">
                           <c:out value= "style=display:none"/>
                        </c:if>
                        >
                        <div class="form-group">
                        	<div class="col-sm-12">
                        		<div class="form-inline">
                        			<form:select id="noOfReminders" path="noOfreminders"  class="form-control">
			                           <c:forEach var="i" begin="0" end="9">
			                              <option value="${i}" 
			                              <c:if test="${setUpNotificationObject.noOfreminders ==i}">
			                                 <c:out value= "selected=selected"/>
			                              </c:if>
			                              >${i}</option>
			                           </c:forEach>
			                        </form:select>
                        		</div>
                        	</div>
                        </div>
                        <div class="form-group">
                        	<div class="col-sm-12">
	                           <label class="control-label">
	                              Reminder Every : 
	                              <form:input path="frequencyOfSendingRemindes" value="${setUpNotificationObject.frequencyOfSendingRemindes}"  class="editSmallTextBox" />
	                              days <a href="javascript:void(0)"><span class="icon icon-edit"></span></a>
	                           </label>
	                           <form:errors path="frequencyOfSendingRemindes" class="error" />
	                        </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-sm-6">
                        <div class="form-group">
                        	<div class="col-sm-12">
                        		<label class="control-label"> Escalation Reciepient: <span class="required">*</span></label>
		                  		<div class="switch-control">
		        					<span class="switch-text">Yes</span>
		        					<label class="switch">
		                                 <input type="checkbox" id="escalation" name="escalation"  class="switch-input"   <c:if test="${setUpNotificationObject.escalation =='Yes'}"> <c:out value= "checked=checked"/></c:if> >
		                                 <span class="switch-label" data-on="Parent" data-off="School"></span>
		                                 <span class="switch-handle"></span>
		                               </label>
	                               <span class="switch-text">No</span>
		        				</div>
                        	</div>
                        </div>
        				<div class="escalationContainer" <c:if test="${setUpNotificationObject.escalation =='No'}"> <c:out value= "style=display:none"/></c:if>>
		                     <div class="clearfix">
		                        <label class="control-label pull-left">Escalation Role<span class="required">*</span></label>
		                        <label class="control-label pull-right">
		                           Days past due - 
		                           <form:input path="noOfPastDueDays" value="${setUpNotificationObject.noOfPastDueDays}" class="editSmallTextBox" />

		                           days <a href="javascript:void(0)"><span class="icon icon-edit"></span></a>
		                        </label>
		                        <form:errors path="noOfPastDueDays" class="error" />
		                     </div>
		                     <div class="form-control-static"><a href="#" onclick="populateRoles()">Add an Escalation Role </a></div>
		                     <div class=" role-details" >
		                        <c:forEach items="${setUpNotificationObject.escalationRoles}" var="escalationRole" begin="0">
		                           <div class="form-group">
		                              <div class="col-sm-12">
		                                 <div class="input-group role-input-block">
		                                    <input type="text" id="${escalationRole.roleId}#${escalationRole.name}"  value="${escalationRole.name}" style="width:150px;height:25px;" class="form-control inputEscalation"></input>		        			
		                                    <span class="input-group-btn"><i class="icon icon-delete escalationDelete"></i></span>
		                                 </div>
		                              </div>
		                           </div>
		                        </c:forEach>
		                        <input type="hidden" id="escalationSelectedRoles" name="escalationSelectedRoles"  path="escalationSelectedRoles" />
		                        <form:errors path="escalationSelectedRoles" class="error" />
		                     </div>
		                  </div>
                  </div>
                  
            </div>
            
            <div class="form-group form-footer">
            <div class="col-sm-12 text-left ">
            <button type="reset"  class="btn btn-cancel" data-dismiss="modal" onclick="showMessage();">Cancel</button>
            <button type="submit" class="btn btn-submit" onClick="setFormValues()">Save</button>
            </div>
            </div>
            <!-- modal panel -->
            <div class="modal custom fade modal-wide" id="addRolesForSetUp" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
            <h4 class="modal-title" id="myModalLabel">Add Escalation Roles</h4>
            </div>
            <div class="modal-body">
            <div class="row">
            <div class="col-sm-12">
            <div class="col-sm-3">
            <select id="escalationRoles" multiple="multiple" class="form-control">
            <c:forEach begin="0" items="${setUpNotificationObject.roleDTOs}"  var="roleDTO" varStatus="position">
            <option id="${roleDTO.id}"  value="${roleDTO.id}#${roleDTO.name}"> ${roleDTO.name}</option>
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
            <c:forEach begin="0" items="${setUpNotificationObject.roleDTOs}"  var="roleDTO" varStatus="position">
            <tr id="escalationableRow${roleDTO.id}" class="odd" style="display:none">
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
            <button type="button" class="btn btn-submit" onclick="addingRolesToEscalationRoles()">Add New Escalation Roles</button>
            </div>
            </div>
            </div>
            </div>
            
            </form:form>
            
            <!--simple popup msg-->
	<div class="popup-msg alert" id="simpleMsg">
		<div class="text-right"><a class="close" href="#">&times;</a></div>
		<div role="alert" class="content">
			Are you sure want to continue ? 	
		</div>
	</div>
	<!--Error popup msg-->
	<div class="popup-msg alert alert-danger" id="errorMsg">
		<div class="text-right"><a class="close" href="#">&times;</a></div>
		<div role="alert" class="content">
		  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		  <span class="sr-only">Error:</span>
		  Enter a valid email address
		</div>
	</div>
	<!--information popup msg-->
	<div class="popup-msg alert alert-information" id="infoMsg">
		<div class="text-right"><a class="close" href="#">&times;</a></div>
		<div role="alert" class="content">
		  <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
		  <span class="sr-only">Information:</span>
		  This can be done using the ALSA method.
		</div>
	</div>
	
	<div id="popupMsg" class="popup-msg">
		<div class="text-right"> <span class="close" href="javascript:void(0)" onclick="hideMessage()">&times;</span></div>
		<div class="content">
			<p class="msg">Your changes will not be saved.Do you want to proceed.</p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="closePage();hideMessage();">YES</button>
                <button type="button" class="btn btn-submit" onclick="hideMessage();">NO</button>
        </div>
	</div> 
         </div>
         <div class="col-sm-5">
         </div>
      </div>
      </div>
 
      <script type="text/javascript" src="${context}/assets/js/notification/notificationSetup.js"></script>
      
   </body>
</html>