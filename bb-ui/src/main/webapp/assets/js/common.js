$(document)
		.ajaxError(function() {
			$("#progressBar").css("display","none");
			/*
			 if ($('.modal:visible').length > 0) {
						$('.modal:visible').find('.modal-body').prepend(
								'<div id="ajax-error" class="action-error">OOPs! Something went wrong</div>');
						window.setTimeout(function() {
							$('#ajax-error').html('');
						}, 5000);
					} else {
						$('.main-content')
								.prepend(
										'<div id="action-error" class="action-error">OOPs! Something went wrong</div>');
						window.setTimeout(function() {
							$('#action-error').html('');
						}, 5000);
					}
			 */
		}
);

$(document)
.ajaxSuccess(function() {
	$("#progressBar").css("display","none");
}
);

var sess_pollInterval = 6000;
var sess_expirationMinutes = 30;
var sess_warningMinutes = 29.5;

var sess_intervalId;
var sess_lastActivity;

var uploadGoing = false;

function initSessionMonitor() {
	sess_lastActivity = new Date();
	sessSetInterval();
	$(document).bind('keypress.session', function(ed, e) {
		sessKeyPressed(ed, e);
	});
}

function sessSetInterval() {
	sess_intervalId = setInterval('sessInterval()', sess_pollInterval);
}

function sessClearInterval() {
	clearInterval(sess_intervalId);
}

function sessKeyPressed(ed, e) {
	sess_lastActivity = new Date();
}

function sessPingServer() {
	window.location.reload(true);
}

function sessLogOut() {
	window.location.href = logoutUrl;
}

function sessInterval() {
	var now = new Date();
	var diff = now - sess_lastActivity;
	var diffMins = (diff / 1000 / 60);

	if (diffMins >= sess_warningMinutes) {
		sessClearInterval();
		showTimeoutPopup();
	}
}

function continueSession() {
	var now = new Date();
	var diff = now - sess_lastActivity;
	var diffMins = (diff / 1000 / 60);
	if (diffMins > sess_expirationMinutes) {
		sessLogOut();
	} else {
		sessPingServer();
		// sessSetInterval();
		// sess_lastActivity = new Date();
	}
}


if (window.attachEvent) {
	window.attachEvent('onload', initSessionMonitor);
} else if (window.addEventListener) {
	window.addEventListener('load', initSessionMonitor, false);
}

$(document).ajaxStart(function() {
	sess_lastActivity = new Date();
	var $loader = $("#progressBar");
	if ($loader.attr('disableLoader') !== 'true') {
		$loader.css("display","block");
	}
	
	/*if ($('.modal:visible').length > 0 && $('#ajax-error:visible').length > 0) {
		$('#ajax-error').html('');
	} else if ($('#action-error:visible').length > 0) {
		$('#action-error').html('');
	}*/
});

function showLogoutPopup(){
	$("#logoutPopup").show();
	console.log('uploadGong: '+uploadGoing) ;
	if(uploadGoing){
		$('#uploadMsgWarning').text("bulk upload is currently in Progress") ;
	}
	$("#logoutPopup").wrap("<div class='overlay'>");
}
function hideLogoutPopup(){
	$("#logoutPopup").hide();
	$("#logoutPopup").unwrap("<div class='overlay'>");
}

function showTimeoutPopup(){
	$("#timeoutPopup").show();
	$("#timeoutPopup").wrap("<div class='overlay'>");
}
function hideTimeoutPopup(){
	$("#timeoutPopup").hide();
	$("#timeoutPopup").unwrap("<div class='overlay'>");
}

function change(){
$('a').onchange = function(event){	
	alert(event.target);
}
}
change();

$(function(){
	var url = $(location).attr('href');
	if(url.indexOf("/admin") !== -1){
		$("#adminMenu li").eq(0).addClass('active').siblings().removeClass('active');
	}else if(url.indexOf("/notification") !== -1){
		$("#adminMenu li").eq(1).addClass('active').siblings().removeClass('active');
	}
});


$(function(){
	var tab = $('#activeTab').val();
	if(tab == 'navDashboard') {
		removeAllNavActiveClass();
		$('#navDashboard').addClass('active');
	}else if(tab == 'navActionItems') {
		removeAllNavActiveClass();
		$('#navActionItems').addClass('active');
	}else if(tab == 'navDrafts') {
		removeAllNavActiveClass();
		$('#navDrafts').addClass('active');
	}
});

function removeAllNavActiveClass() {
	$('#navActionItems').removeClass('active');
	$('#navDrafts').removeClass('active');
	$('#navDashboard').removeClass('active');
}