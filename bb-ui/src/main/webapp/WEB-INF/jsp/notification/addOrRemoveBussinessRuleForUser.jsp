<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
               <span class="pull-right form-fields-tip">All <span class="asterisk">*</span> marked fields are compulsory</span>
               <h2 class="page-heading">Add New Business Rule</h2>
            </div>
         </div>
      </div>
      <div class="row">
      <div class="col-sm-9">
         <form:form action="#" class="form-horizontal" commandName="editBussinessRules">
         
         <input type="hidden"  id="notificationId" name="${editBussinessRules.notificationId}"></input>
         
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
                     <label class="control-label">Send E- mail for this Notification</label>
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
               <a href="javascript:void(0)" id="toggleNotificationFields">Show All Details</a>
            </div>
            <div class="divider"></div>
            <div class="form-group">
               <div class="col-sm-3">
                  <label class="control-label">Level of Notification</label>
                 <c:choose>
                 <c:when test="${editBussinessRules.notificationType == 'Action Item - Approval'}">
                  <form:select id="notificationLevelNo" path="notificationLevelNo"  class="form-control">
                     <c:forEach var="i" begin="1" end="5">
                        <option value="${i}" <c:if test="${editBussinessRules.notificationLevelNo ==i}"> <c:out value= "selected=selected"/></c:if>>${i}</option>
                     </c:forEach>
                  </form:select>
                  </c:when>
                  <c:otherwise>
                  <form:select id="notificationLevelNo" path="notificationLevelNo"  class="form-control" disabled="true">
                     <option value="1" selected="selected" >Level 1</option>
                  </form:select>
                  </c:otherwise>
                  </c:choose>
               </div>
            </div>
            
            <div id= "message" class="message"></div>
            
               <div id ="addOrRemoveBussinessRuleByLevel2" >
                  <div class="form-group">
                     <div class="col-sm-6">
                        <label class="control-label">Notification Business Rules<span class="required">*</span></label>
                     </div>
                  </div>
                  <div class="sender-recipent-block">
                     <div class="col-sm-4 senderReceiver">
                        <span>Sender</span>
                     </div>
                      <c:forEach var="i" begin="1" end="${editBussinessRules.notificationLevelNo}">
                        <div class="col-sm-4 senderReceiver">
                        <span class="recipent">Recipient ${i}</span>
                        
                     </div>
                     </c:forEach>
                     
                  </div>
                  <!--business rules grid-->
                  <div class="business-rules-grid clearfix">
                     <!--row-->
                     <c:forEach items="${editBussinessRules.businessRuleDtos}" var="notificationBussinessRule">
                     
                     <div id="${notificationBussinessRule.notificationBusinessRuleId}" length="${fn:length(notificationBussinessRule.businessLevelDto)}" class="clearfix odd-row ruleRow">
                     
                     	<c:forEach items="${notificationBussinessRule.businessLevelDto}"  var ="notificationBussinessRuleLevel">
                     	
	                     	<c:if test="${notificationBussinessRuleLevel.currentLevelNo==1}">
	                     	
	                     	<div class="col-sm-4 ruleCol">
	                     	
	                     		<c:forEach items="${notificationBussinessRuleLevel.levelRoleDtos}" var="notificationRole">
			                    <c:if test="${notificationRole.type eq 'Sender'}">
			                    
			                      <div class="form-group role-input-block" style="display:none">
			        					<div class="input-group">
				        					<input type="text" id="${notificationRole.roleId}#${notificationRole.roleName}" name="${notificationRole.roleName}" value="${notificationRole.roleName}" class="form-control">
				        					<span class="input-group-btn"><i class="icon icon-delete inputRole"></i></span>
				        				</div>
			        				</div>		                    
			        				
			                           <div class="rule-text">${notificationRole.roleName}</div>
			                     </c:if>   
					                      
			                     </c:forEach>
			                     <div class="form-group role-input-block addNewReciepientRoleAnchor" style="display:none">
                                 <a href="javascript:void(0)" onclick="addRolesModalPanel(this)">Add New Sender Role</a>
                                 </div>
			                  </div>
	                        </c:if>
	                       
                        </c:forEach>
                        
                        <c:forEach items="${notificationBussinessRule.businessLevelDto}"  var ="notificationBussinessRuleLevel">
                        
                      
                         <div class="col-sm-4 ruleCol">
	                     	<c:forEach items="${notificationBussinessRuleLevel.levelRoleDtos}" var="notificationRole">
	                     	 <c:if test="${notificationRole.type eq 'Receiver'}">   
	                        <div class="form-group role-input-block" style="display:none">
			        	    <div class="input-group">
                            <input type="text" id="${notificationRole.roleId}#${notificationRole.roleName}" name="${notificationRole.roleName}" value="${notificationRole.roleName}" class="form-control">				        			
                           
				             <span class="input-group-btn"><i class="icon icon-delete inputRole"></i></span>
				        				
				        				</div>
			        				</div>
	                        
	                           <div class="rule-text">${notificationRole.roleName}</div>                    
	                        </c:if>
	                        </c:forEach>
	                         <div class="form-group role-input-block addNewReciepientRoleAnchor" style="display:none">
                                 <a href="javascript:void(0)" onclick="addRolesModalPanel(this)">Add New Recipient Role</a>
                                 </div>
                        </div>
                        
                        </c:forEach>
                        
                        <div class="col-sm-offset-12">
                           <div class="action-btns-grid">
                              <a href="javascript:void(0)" class="ediRuleIcon"><span class="icon icon-edit"></span> Edit</a>
                              <a href="javascript:void(0)" class="deleteRuleIcon"><span class="icon icon-delete"></span> Delete</a>
                           </div>
                        </div>
                        <div class="buttonContainer">
                          <button type="button" class="btn submitRuleEditOrAdd" style="display: inline-block;border: 2px #2e3192 solid;background: #fff; color:#2e3192" onclick="closeTheRuleDivContainer(this)">Cancel</button>
                         <button type="button" class="btn submitRuleEditOrAdd" style="display: inline-block;border: 2px #2e3192 solid;background: #2e3192; color:#fff" onclick="saveRoles(this)">Save</button>
                        </div>
                     </div>
                     </c:forEach>
                     <div id="addNewBussinessRuleId">
                        <a href="javascript:void(0)" onclick="addNewRuleRow()">Add New Business Rule</a>
                     </div>
                  </div>
                  </div>
                  
                  <div class="form-group form-footer">
                     <div class="col-sm-12 text-left cancelAndsaveContainer">
                        <button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="showMesageForCancel();">Cancel</button>
                        <button type="button" class="btn btn-submit" onclick="saveNotification()">Save</button>
                     </div>
                  </div>
                  
    <!-- add Role Modal Panel -->
<div class="modal custom fade modal-wide" id="addRolesModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
                  <select id="roles" multiple="multiple" class="form-control" onChange="onselectOptions(this);" >
                 <c:forEach begin="0" items="${editBussinessRules.roleDTOs}"  var="roleDTO" varStatus="position">
                        <option id="${roleDTO.id}"  value="${roleDTO.name}">${roleDTO.name}</option>
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
                         <c:forEach begin="0" items="${editBussinessRules.roleDTOs}"  var="roleDTO" varStatus="position">
                            <tr id="tableRow${roleDTO.id}" class="odd" style="display:none">
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
                <button type="button" class="btn btn-submit" onclick="subMitRolesAnadCloseModalPanel()">Add New Roles</button>
            </div>
        </div>
    </div>
</div>
         </form:form>
         
       <!--popup msg-->
	 <div id="deleteRulePopup" class="popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hideMessage()">&times;</a></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="deleteRule();hideMessage();">YES</button>
                <button type="button" class="btn btn-submit" onclick="hideMessage();">NO</button>
        </div>
	</div> 
		  
	   <div id="cancelPopup" class="popup-msg">
		<div class="text-right"> <a class="close" href="#" onclick="hidecancelMessage()">&times;</a></div>
		<div class="content">
			<p class="msg">Your changes will not be saved.Do you want to proceed.</p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="closePage();hidecancelMessage();">YES</button>
                <button type="button" class="btn btn-submit" onclick="hidecancelMessage();">NO</button>
        </div>
	</div> 
	
	
	<div id="cancelRulePopup" class="popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hidecancelRuleMessage()">&times;</a></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="closePopUpForRule();hidecancelRuleMessage();">YES</button>
                <button type="button" class="btn btn-submit" onclick="hidecancelRuleMessage();">NO</button>
        </div>
	</div> 
	
         </div>
         <div class="col-sm-5">
         </div>
         </div>
      </div>
       <script type="text/javascript" src="${context}/assets/js/notification/notification.js"></script>
   </body>
</html>