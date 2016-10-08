<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String css = context+"/assets/css";
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<html>
<body>
	<div class="main-content container">
		<div class="row">
			<div class="col-sm-12">
				<div class="page-header">
					<span class="pull-right form-fields-tip">All <span class="asterisk">*</span> marked fields are compulsory</span>
					<h2 class="page-heading">Create New User</h2>
				</div>
			</div>
		</div>
		<div class="row">
        	<div class="col-sm-7 mdm-right-pad">
        		<form:form class="form-horizontal" method="post" modelAttribute="userForm" action="create">
        			<div class="form-group">
        				<div class="col-sm-4">
	        				<label class="control-label">Last Name <span class="required">*</span></label>
	        				<form:input type="text" class="form-control" value="" path="lastName"/>
	        				<form:errors path="lastName" class="error" />
	        			</div>
	        			<div class="col-sm-4">
	        				<label class="control-label">Middle Name </label>
	        				<form:input type="text" class="form-control" value="" path="middleName"/>
	        				<form:errors path="middleName" class="error" />
	        			</div>
	        			<div class="col-sm-4">
	        				<label class="control-label">First Name <span class="required">*</span></label>
	        				<form:input type="text" class="form-control" value="" path="firstName"/>
	        				<form:errors path="firstName" class="error" />
	        			</div>
	        		</div>
	        		<div class="form-group">
        				<div class="col-sm-8">
	        				<label class="control-label">Email id: <span class="required">*</span></label>
	        				<form:input type="text" class="form-control" value="" path="emailId"/>
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
	        					<form:option value="${role.id}">${role.name}</form:option>
	        				</c:forEach>
	        				</form:select>
	        				<form:errors path="roleIds" class="error" />
	        			</div>
	        			
	        		</div>
	        		<div class="divider"></div>
	        		<div class="form-group">
	        			<div class="col-sm-6">
	        				<label class="control-label">Employee Id</label>
	        				<form:input type="text" class="form-control" value="" path="employeeId"/>
	        			</div>
	        			<div class="col-sm-6">
	        				<label class="control-label">Designation</label>
	        				<form:input type="text" class="form-control" value="" path="designation"/>
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
	        		<div class="divider"></div>
	        		<div id="cancelPopup" class="popup-msg" >
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
	        				<form:button id="createUser" type="submit" value="create" class="btn btn-submit">Create User</form:button>
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
		$('.glyphicon-calendar').click(function(){
	    	var futureDate = new Date();
		    	futureDate.setDate(futureDate.getDate() + 1);
		    	$("#datetimepicker1").datepicker({ startDate: futureDate  });
		    	$('#datetimepicker1').on('change', function(){
		            $('.datepicker').hide();
	        });
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
				$('#createUser').addClass("disabled");
			}
			$('#endingOn').append('<span class="required">*</span>');
			
		}
		else
		{
			$('#createUser').removeClass("disabled");
			$('#endingOn').children("span").remove();
		}
	});
	
	$('#endingDate' ).on('change' , function(){
		if($(this).val()=="" && $('#selectedUserType').val()=="2")
		{
			$('#createUser').addClass("disabled");
		}
		else
		{
			$('#createUser').removeClass("disabled");
			
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
	
	window.location.href='../admin/';
}

</script>
<script type="text/javascript" src="<%=js%>/common/namespace.js"></script>
<script type="text/javascript" src="<%=js%>/common/constants.js"></script>
<script type="text/javascript" src="<%=js%>/common/util/validation-util.js"></script>
<script type="text/javascript" src="<%=js%>/common/validator/user-validator.js"></script>
<script type="text/javascript" src="<%=js%>/admin/user.js"></script>

</body>
</html>