<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html>


	<div class="main-content container">
		<div class="row">
			<div class="col-sm-12">
				<div class="page-header">
					<span class="pull-right form-fields-tip">All <span class="asterisk">*</span> marked fields are compulsory</span>
					<h2 class="page-heading">Edit User</h2>
				</div>
			</div>
		</div>
		<div class="row">
        	<div class="col-sm-7 mdm-right-pad">
        		<form:form class="form-horizontal" method="post" commandName="userForm" action="${pageContext.request.contextPath}/user/update">
        			<div class="form-group">
        				<form:hidden path="id"/>
        				<div class="col-sm-4">
	        				<label class="control-label">Last Name: <span class="required">*</span></label>
	        				<form:input type="text" class="form-control" path="lastName"/>
	        				<form:errors path="lastName" class="error" />
	        			</div>
	        			<div class="col-sm-4">
	        				<label class="control-label">Middle Name </label>
	        				<form:input type="text" class="form-control" path="middleName"/>
	        				<form:errors path="middleName" Class="error" />
	        			</div>
	        			<div class="col-sm-4">
	        				<label class="control-label">First Name <span class="required">*</span></label>
	        				<form:input type="text" class="form-control" path="firstName"/>
	        				<form:errors path="firstName" class="error" />
	        			</div>
	        		</div>
	        		<div class="form-group">
        				<div class="col-sm-8">
	        				<label class="control-label">Email id: <span class="required">*</span></label>
	        				<form:input type="text" class="form-control" path="emailId"/>
	        				<form:errors path="emailId" class="error" />
	        			</div>
	        			<div class="col-sm-4">
	        				<label class="control-label">Nationality <span class="required">*</span></label>
	        				<form:select class="form-control" path="nationality.id">
	        				<c:forEach var="nationality" items="${nationalities}">
	        					<form:option value="${nationality.id}">${nationality.name}</form:option>
	        				</c:forEach>
	        				</form:select>
	        			</div>
	        		</div>
	        		<div class="form-group">
        				<div class="col-sm-8">
	        				<label class="control-label">Select Role: <span class="required">*</span></label>
	        					<form:select class="form-control" multiple="true" path="roleIds">
	        						<c:forEach var="role" items="${roles}">
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
	        				<form:input type="text" class="form-control" path="employeeId"/>
	        			</div>
	        			<div class="col-sm-6">
	        				<label class="control-label">Designation</label>
	        				<form:input type="text" class="form-control" path="designation"/>
	        			</div>
	        		</div>
	        		<div class="divider"></div>
	        		<div class="form-group">
	        			<div class="col-sm-6">
	        				<label class="control-label">User Type <span class="required">*</span></label>
	        				<form:select id="selectedUserType" class="form-control" multiple="false" path="userType.id">
	        					<c:forEach var="userType" items="${userTypes}">
	        					<form:option value="${userType.id}">${userType.name}</form:option>
	        				</c:forEach>
	        				</form:select>
	        			</div>
	        			<div class="col-sm-6">
	        				<label id="endingOn" class="control-label">Ending On</label>
	        				<div class='input-group date' id='datetimepicker1'>
			                    <form:input type='text' class="form-control" id="endingDate" path="endingOn"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar" data-alt="calendar"></span>
			                    </span>
			                </div>
			                <form:errors path="endingOn" class="error" />
	        			</div>
	        		</div>
	        		<%-- <div class="form-group">
	        			<div class="col-sm-4">
	        				<label class="control-label">Agent Name <span class="required">*</span></label>
	        				<form:input type="text" class="form-control" path="agentName"/>
	        			</div>
	        			<div class="col-sm-4">
	        				<label class="control-label">Client Name <span class="required">*</span></label>
	        				<form:input type="text" class="form-control" path="clientName"/>
	        			</div>
	        			<div class="col-sm-4">
	        				<label class="control-label">Ending On</label>
	        				<div class='input-group date' id='datetimepicker1'>
			                    <form:input type='text' class="form-control" path="endingOn"/>
			                    <span class="input-group-addon">
			                        <span class="icon icon-calendar" data-alt="calendar"></span>
			                    </span>
			                </div>
	        			</div>
	        		</div> --%>
	        		<div class="divider"></div>
	        		
	        		<div id="cancelPopup" class="popup-msg">
						<div class="text-right"> <a class="close" href="#" onclick="hideCancelMessage()">×</a></div>
						<div class="content">
							<p class="msg">Your changes will not be saved.Do you want to proceed.</p>
						</div>
						<div class="modal-footer">
                			<button type="button" class="btn btn-submit" data-dismiss="modal" onclick="closePage();hideCancelMessage();">YES</button>
                			<button type="button" class="btn btn-cancel" onclick="hideCancelMessage();">NO</button>
        				</div>
					</div>
	        		<div class="form-group form-footer">
	        			<div class="col-sm-12 text-left">
	        				<form:button type="reset" class="btn btn-cancel" data-dismiss="modal" onclick="showCancelMessage();">Cancel</form:button>
	        				<form:button type="submit" value="update" class="btn btn-submit" id="updateUser">Update User</form:button>
	        			</div>
	        		</div>
        		</form:form>
        	</div>
        	<div class="col-sm-5">
        		
        	</div>
        </div>
	</div>



	<script type="text/javascript">
	$(function(){
		$(".hidden-row").hide();
		$(".has-hidden-row span").on("click", function(){

			if($(this).hasClass("icon-plus")){
				$(this).parents(".has-hidden-row").addClass("active");
				$(this).removeClass("icon-plus").addClass("icon-minus");
				$(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
			}
			else if($(this).hasClass("icon-minus")){
				$(this).parents(".has-hidden-row").removeClass("active");
				$(this).removeClass("icon-minus").addClass("icon-plus");
				$(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
			}
		});
	});
	</script>

<script type="text/javascript">
	$(function(){
    	var futureDate = new Date();
    	futureDate.setDate(futureDate.getDate() + 1);
    	$("#datetimepicker1").datepicker({startDate: futureDate});
    	$('#datetimepicker1').on('change', function(){
            $('.datepicker').hide();
        });
	/////////////////////
		$(".header .search-input .form-control").on("focus", function(){
			$(this).parents(".search-input").addClass("active");
		});
		$(".header .search-input .form-control").on("focusout", function(){
			$(this).parents(".search-input").removeClass("active");
		});

	/////////////////////////
		$("#gotoSearch").on("click", function(){
			window.location.assign("search.html");
		});		
	/////////////////////////

		var searchHeight = $('.search-dropdown').height();
	//	$('.search-dropdown').css("marginTop", -searchHeight);

		$(".header .search-input .form-control").on("focus", function(){
			//$(this).parents(".search-input").addClass("active");
			//$('.search-dropdown').animate({"marginTop": 0, "toggle": "height"}, 1500);
			$('.search-dropdown').slideToggle("show");
		});

		$("#hideSearch").on("click", function(){
			$(this).parents(".search-input").removeClass("active");
			$(".search-dropdown").slideUp("hide");
		});

		//tooltip
	  $(function () {
	     $('[data-toggle="tooltip"]').tooltip();
	     $('.has-error .form-control').tooltip({trigger: 'manual'}).tooltip('show');
	     $('.has-error .form-control').on('focus',function(){$(this).tooltip('destroy');});
	  })

	});
	</script>

	<script type="text/javascript">
		jQuery(document).ready(function($) {
			$('.multiselect').multiselect();
				$('#selectedUserType').change(function(){
				if($(this).val()=="2" )
				{
					if($('#endingDate').val()=="")
					{
						$('#updateUser').addClass("disabled");
					}
					$('#endingOn').append('<span class="required">*</span>');
					
				}
				else
				{
					$('#updateUser').removeClass("disabled");
					$('#endingOn').children("span").remove();
				}
			});
			
			$('#endingDate' ).on('change' , function(){
				if($(this).val()=="" && $('#selectedUserType').val()=="2")
				{
					$('#updateUser').addClass("disabled");
			
				}
				else
				{
					$('#updateUser').removeClass("disabled");
					
				}
			});
			
		});
		function showCancelMessage(){
			$("#cancelPopup").show();
			$("#cancelPopup").wrap("<div class='overlay'>");
		}
		function hideCancelMessage(){
			$("#cancelPopup").hide();
			$("#cancelPopup").unwrap("<div class='overlay'>");
		}
		function closePage(){
			
			window.location.href='${pageContext.request.contextPath}/admin/';
		}
	</script>

</body>
</html>