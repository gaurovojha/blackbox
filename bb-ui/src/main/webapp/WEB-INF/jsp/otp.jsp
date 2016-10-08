<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%
	String context = request.getContextPath();
	String css = context+"/assets/css";
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<!DOCTYPE html>
<html>
<head>
<style>
.message {
    color: green;
    font-style: italic;
    font-weight: bold;
}
.error {
    color: #ff0000;
    font-style: italic;
    font-weight: bold;
}
</style>
	<title>><spring:message code="login.blackbox" /></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap-datepicker.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/daterangepicker.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/main.css">
</head>
<body>

	<!--login-->
	<div class="login-container">
		<h2><spring:message code="blackbox.signin" /></h2>
		<div class="login-logo">
			<img src="<%=images%>/logo_ids.gif" alt="IDS logo">
			<div><spring:message code="title.blackbox" /></div>
		</div>
		<div class="form-horizontal">
			<form:form class="form-horizontal" method="post"
				modelAttribute="otpForm" action="otp">
			<div class="countdown">
				<span>00</span>:<span id="time">15:00</span>
				<form:hidden path="otpTimer" id="otpHidden"/>
				<!-- <input type="hidden" id="otpHidden" name="otpHidden"/> -->
			</div>
			<div class="form-group">
				<div class="col-sm-12">
					<label class="control-label"><spring:message code="username" /></label>
					<div class="form-control-static">${otpForm.userId}</div>
				</div>
			</div>
			<%-- <div id="otp-error" class="error">${error}</div> --%>
			<div id="otp-message" class="message">${message}</div>
			<div class="form-group">
				<div class="col-sm-12">
					<label class="control-label"><spring:message code="otp.page.onetimepassword" /></label>
					<form:input type="password" class="form-control" path="otp" ></form:input>
					<form:errors cssClass="error" path="otp"></form:errors>
					<!-- <input type="password" class="form-control" name="otp"/> -->
					<div class="otp-request"><spring:message code="otp.page.notreceiveotp" /><a href="otp/requestOTP"  id="resendOTP"><spring:message code="otp.page.resend" /></a></div>
				</div>
			</div>
			
			<div class="form-group footer-login">
				<div class="col-sm-12">
				<form:button class="btn btn-submit col-xs-12"><spring:message code="button.submit" /></form:button>
					<%-- <button class="btn btn-submit col-xs-12"><spring:message code="button.submit" /></button> --%>
				</div>
			</div>
			</form:form>
			<div id="popupMsg" class="popup-msg">
				<div class="text-right"> <a class="close" href="#" onclick="hideMessage()">&times;</a></div>
				<div class="content">
					<p class="msg">Do you  want to resend otp?</p>
				</div>
				<div class="modal-footer">
                <button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="resendOTP();hideMessage();">OK</button>
                <button type="button" class="btn btn-submit" onclick="hideMessage();">Cancel</button>
        		</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="<%=js%>/jquery.min.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=js%>/moment.js"></script>
	<script type="text/javascript" src="<%=js%>/daterangepicker.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
	 var fifteenMinutes = $('#otpHidden').attr('value');
			function startTimer(duration, display) {
			    var timer = duration, minutes, seconds;
			    setInterval(function () {
			        minutes = parseInt(timer / 60, 10)
			        seconds = parseInt(timer % 60, 10);

			        minutes = minutes < 10 ? "0" + minutes : minutes;
			        seconds = seconds < 10 ? "0" + seconds : seconds;

			        display.text(minutes + ":" + seconds);
			        jQuery('#otpHidden').attr('value',minutes*60+seconds);
			        if (--timer < 0) {
			            timer = duration;
			        }
			    }, 1000);
			};
			
			checkResendOTP = function() {
				$("#popupMsg").show();
			   /*  var confirmation = confirm('Do you  want to resend otp?');
			    if(confirmation == true)
			    {
					document.getElementById("resendOTP").click();
			    }
			    else
			    {
			          return true;
			    } */
			};
				
			//window.setInterval(checkResendOTP, 120000);

		jQuery(function ($) {
		   
		        var display = $('#time');
		    if(fifteenMinutes == null || fifteenMinutes == 'undefined' || fifteenMinutes == '') {
		    	fifteenMinutes = 60 * 15;
		    	jQuery('#otpHidden').attr('value',fifteenMinutes);
		    }
		    else{
		    var minutes, seconds;
		    minutes = parseInt(fifteenMinutes / 60, 10);
	        seconds = parseInt(fifteenMinutes % 60, 10);

	        minutes = minutes < 10 ? "0" + minutes : minutes;
	        seconds = seconds < 10 ? "0" + seconds : seconds;

	        display.text(minutes + ":" + seconds);
	        jQuery('#otpHidden').attr('value',minutes*60+seconds);
		    }
		    startTimer(fifteenMinutes, display);
		    if(fifteenMinutes > 60 * 13) {
		    	window.setTimeout(checkResendOTP, (fifteenMinutes - 60 * 13) * 1000);
		    }
		   });
		
		function showMessage(){
			$("#popupMsg").show();
			$("#popupMsg").wrap("<div class='overlay'>");
		}
		function hideMessage(){
			$("#popupMsg").hide();
			$("#popupMsg").unwrap("<div class='overlay'>");
		}
		
		function resendOTP(){
			document.getElementById("resendOTP").click();
		}
	</script>

</body>
</html>