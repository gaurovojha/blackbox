
<script>
	//Initiate IDS Notification

	// View Family Details
	
	$(document).on('click','.familyId' ,function() {
		var familyId = $(this).attr('data');
		var target = $('#contextPath').val()+"/mdm/viewFamily/" + familyId;
		$.get(target, function(data) {
			$('#viewFamilyBody').html(data).show();
		});
	});
	
	$(document).tooltip({
		selector : '[data-toggle="tooltip"]'
	});
	
	// Function to Show More Details for Notification Message and Email Subject		
	function fxMoreNotificationDetails() {
		$(".show-more").unbind('click');
		//$moreNotification = $(".more-notification");
		$(".show-more").on(
				"click",
				function(event) {
					$moreNotificationBlock = $(this).parents(
							".notification-box").find(".more-notification");
					$moreNotificationBlock.height($(event.currentTarget)
							.parents(".notification-box").outerHeight(false));
					event.preventDefault();
					$moreNotificationBlock.fadeIn();
				});
		$(".show-less").unbind('click');
		$(".show-less").on(
				"click",
				function(event) {
					$moreNotificationBlock = $(this).parents(
							".notification-box").find(".more-notification");
					event.preventDefault();
					$moreNotificationBlock.fadeOut();
				});

	}

	//Functions for Ajax Call for Notification Fetch On Page Load
	$(document).ready(function() {
		try {
			$.blackbox.util.CommonUtil.loadResourceBundle(('i18n/ids'));
		} catch (error) {
			console.log(error);
		}
		
		var appId = $("#appId").val();
		searchNotificationsApp(appId);
	});

	//Fetch the App Notifications
	function searchNotificationsApp(appId) {
		var currentPage = $('#currentPage').val();
		var data = {
			page : currentPage
		}
		appDataInputs(data,appId);
	}

	//Fetch the Family Notification
	function searchNotificationsFamily(appId) {
		var currentPage = $('#currentPageFamily').val();
		var data = {
			page : currentPage
		}
		familyDataInputs(data,appId);
	}

	// Application Notification Div and Target Details
	function appDataInputs(data,appId) {
		var appDataDiv = "#appNotificationData";
		var target = $('#contextPath').val()
				+ '/ids/initiate/appNotifications/'+appId;
		searchPendingNotifications(data, appDataDiv, target);
	}

	// Family Notification Div and Target Details
	function familyDataInputs(data,appId) {
		var familyDataDiv = "#familyNotificationData";
		var target = $('#contextPath').val()
				+ '/ids/initiate/familyNotifications/' +appId;
		searchPendingNotifications(data, familyDataDiv, target);
	}
	
	//Generic Method for ajax call for Notifications
	function searchPendingNotifications(data, dataDiv, target) {
		$.ajax({
			type : "GET",
			data : data,
			url : target,
			success : function(result) {
				showNotificationCards(result, dataDiv);
				var seletedAccordion =  $(".in").attr("id");
				if(seletedAccordion === "collapseOne"){
				var numNotification = parseInt($('#totalAppRecords').val());
				$('.countApp').html(numNotification);
				if (numNotification == 0) {
					$('#collapseOne').remove();
				}
				}else if(seletedAccordion === "collapseTwo"){
						var numNotification = parseInt($('#recordsTotalFamily').val());
						$('.countFamily').html(numNotification);
						if (numNotification == 0) {
							$('#collapseTwo').remove();
						}
					}
				changeButtonText();
			}
		});
	}
	
	//Will show the notifications cards in specific data div
	function showNotificationCards(result, dataDiv) {
		$(dataDiv).html(result).show();
		var collapseOne = $("#collapseOne").hasClass("in");
		if (collapseOne == true) {
			showAppPrevLink();
			showAppNextLink();
		}
		var collapseTwo = $("#collapseTwo").hasClass("in");
		if (collapseTwo == true) {
			showFamilyPrevLink();
			showFamilyNextLink();
		}
		fxMoreNotificationDetails();
		refreshNotification();
	}

	//Will show the Application Cards Previous Page Link
	function showAppPrevLink() {
		var currentAppPage = $('#currentPage').val();
		if (currentAppPage > 1) {
			$("#collapseOne").find(".prevCarousel").show();
		} else {
			$("#collapseOne").find(".prevCarousel").hide();
		}
	}
	
	//Will show the Family Cards Previous Page Link
	function showFamilyPrevLink() {
		var currentFamilyPage = $('#currentPageFamily').val();
		if (currentFamilyPage > 1) {
			$("#collapseTwo").find(".prevCarousel").show();
		} else
			$("#collapseTwo").find(".prevCarousel").hide();
	}

	//Will show the Application Cards Next Page Link
	function showAppNextLink() {
		var currentAppPage = Number($('#currentPage').val());
		var noOfPagesApp = Number($('#noOfPages').val());
		if (currentAppPage >= noOfPagesApp) {
			$("#collapseOne").find(".nextCarousel").hide();
		} else {
			$("#collapseOne").find(".nextCarousel").show();
		}
	}

	//Will show the Family Cards Next Page Link
	function showFamilyNextLink() {
		var currentFamilyPage = Number($('#currentPageFamily').val());
		var noOfPagesFamily = Number($('#noOfPagesFamily').val());
		if (currentFamilyPage < noOfPagesFamily) {
			$("#collapseTwo").find(".nextCarousel").show();
		} else
			$("#collapseTwo").find(".nextCarousel").hide();
	}
	
	//Will reload the data when previous page link is clicked
	$('.prevCarousel').on('click', function() {
		var collapseOne = $("#collapseOne").hasClass("in");
		var collapseTwo = $("#collapseTwo").hasClass("in");
		var appId = $('#appId').val();
		if (collapseOne == true) {
			var currentPage = Number($('#currentPage').val());
			var data = {
				page : currentPage - 1
			}
			appDataInputs(data,appId);
		} else if (collapseTwo == true) {
			var currentPage = Number($('#currentPageFamily').val());
			var data = {
				page : currentPage - 1
			}
			familyDataInputs(data,appId);
		}
	});

	//Will reload the data when next page link is clicked
	$('.nextCarousel').on('click', function() {
		var collapseOne = $("#collapseOne").hasClass("in");
		var collapseTwo = $("#collapseTwo").hasClass("in");
		var appId = $('#appId').val();
		if (collapseOne == true) {
			var currentPage = Number($('#currentPage').val());
			var data = {
				page : currentPage + 1
			}
			appDataInputs(data,appId);
		} else if (collapseTwo == true) {
			var currentPage = Number($('#currentPageFamily').val());
			var data = {
				page : currentPage + 1
			}
			familyDataInputs(data,appId);
		}
	});

	//Ajax call on remove notification Link
	$(document)
			.on(
					'click',
					'.removeNotification',
					function() {
						var seletedA =  $(".in").attr("id");
						var appId = $("#appId").val();
						var data = {
							notificationId : $(this).attr('notificationId'),
							selectedAccordion : $(".in").attr("id"),
						}
						var target = getRemoveTarget(seletedA,appId);
						$.ajax({
							type : 'GET',
							url : target,
							data : data,
							success : function(result) {
								reloadRemoveNotificationData(result);
							}
						});
					});

	//Method to get the required target for remove notification
	function getRemoveTarget(selectedA,appId){
		if(selectedA === "collapseOne"){
			var target = $('#contextPath').val()
			+ "/ids/initiate/removeAppNotification/"+appId;
			return target;			
		}
		else if(selectedA === "collapseTwo"){
			var target = $('#contextPath').val()
			+ "/ids/initiate/removeFamilyNotification/"+appId;
			return target;
		}
	}

	//Will Reload the data after remove notification ajax call success
	function reloadRemoveNotificationData(result) {
		var selectedA =  $(".in").attr("id");
		if(selectedA === "collapseOne"){
			var appDataDiv = "#appNotificationData";
			showNotificationCards(result, appDataDiv);
			var numNotification = parseInt($('#totalAppRecords').val());
			$('.countApp').html(numNotification);
			if (numNotification == 0) {
				$('#collapseOne').remove();
			}
		}
		else if(selectedA === "collapseTwo"){
			var familyDataDiv = "#familyNotificationData";
			showNotificationCards(result, familyDataDiv);
			var numNotification = parseInt($('#recordsTotalFamily').val());
			$('.countFamily').html(numNotification);
			if (numNotification == 0) {
				$('#collapseTwo').remove();
			}
		}
		reloadData();
		changeButtonText();
	}
	
	//Event triggered on Accordion Clicked
	$('.panel').on('shown.bs.collapse', function(e) {
		var target = e.target.id;
		var appId = $('#appId').val();
		if (target === "collapseTwo") {
			searchNotificationsFamily(appId);
		}
		else if (target === "collapseOne") {
			searchNotificationsApp(appId);
		}
	})
	
	//Reloading Data
	function reloadData(){
		var seletedAccordion =  $(".in").attr("id");
		var appId = $('#appId').val();
		if(seletedAccordion === "collapseOne"){
		searchNotificationsApp(appId);
		}
		else if(seletedAccordion === "collapseTwo"){
			searchNotificationsFamily(appId);
		}
	}
	
	// Event for Refresh Link
	function refreshNotification(){	
			$(".refreshNotification").unbind('click');
			//$moreNotification = $(".more-notification");
			$(".refreshNotification").on(
					"click",
					function(event) { 
						reloadData();
					});
	}

	//Method for Changing the button text
	function changeButtonText(){
		var countApp = $('.countApp').html();
		var countFamily = $('.countFamily').html();
		if(countApp == 0 && countFamily == 0 ){
			 $("#notificationButton").html("Continue");
		}
		else{
			 $("#notificationButton").html("Skip and Proceed");
		}
	
	}

	$(document).on('click','#notificationButton',function(){
		var appId = $("#appId").val();;
		var target = $("#contextPath").val() + '/ids/newIDS/' +appId;
		$.ajax({
			url : target,
			data : appId,
			success : function(idsId) {		
				bindIDSConfirmPopUp(idsId);	
			},

		});
	});
	//IDS Confirm 
	function bindIDSConfirmPopUp(idsId){
		var idsId = idsId;
		var target = $("#contextPath").val() + '/ids/checkValidPopUp';
		var data = {
				 referenceAge :  $('#referenceAge').val(),
				 prosecutionStatus : $('#prosecutionStatus').val(),
				 appId : $('#appId').val(),
		}
		$.ajax({
			type : "POST",
			url : target,
			data : data,
			success : function(result) {
				bindOpenIDSConfirmPopUp(result.popUpName,idsId,result.refFlowsIds);
			},

		});
 }
	
	function bindOpenIDSConfirmPopUp(confirmPopUpName,idsId,refFlowList) {
		switch(confirmPopUpName){
		case 'issued':
			$('#notificationLink').modal('show');
			$('#popupMsgs').find('.msg').html(jQuery.i18n.prop('ids.notification.prosecution.status.ids.issued'));
			break;
		case 'notIssued':
			$('#notificationLink').modal('show');
			$('#popupMsgs').find('.msg').html(jQuery.i18n.prop('ids.notification.prosecution.status.ids.not.issued'));
			break;
		case 'refFIDSNIssued':
			$('#notificationLink').modal('show');
			$('#popupMsgs').find('.msg').html(jQuery.i18n.prop('ids.notification.prosecution.status.ids.not.issued'));
			break;
		case 'refFIDSIssued':
			$('#notificationLink').modal('show');
			$('#popupMsgs').find('.msg').html(jQuery.i18n.prop('ids.notification.prosecution.status.ids.not.issued'));
			break;	
		case 'refFound':
			openNewRefPopup(idsId,refFlowList);
			break;
		default:
			bindCallBuildIDS();
			break;
		}
		
		if(confirmPopUpName != "refFound"){
		bindIDSPopupButtons(confirmPopUpName,idsId,refFlowList);
		} 
	}
	
function openNewRefPopup(idsId,refFlowList){
	$('#notificationLink').attr('id', 'notificationRef');
	$('#notificationButton').attr('data-target','#notificationRef');
	
	$('#notificationRef').modal('show');
	$('#popupMsgs').find('.msg').html(jQuery.i18n.prop('ids.notification.new.references.found'));
	
	$('.idsConfirmRefNO').unbind("click").on('click', function() {
		bindCallBuildIDS();
	});
	
	$('.idsConfirmRefYes').unbind("click").on('click', function() {
		bindRefIncludeCurrentIDS(idsId,refFlowList);
	});
}
	
function bindIDSPopupButtons(confirmPopUpName,idsId,refFlowList){
	 $('.idsConfirmNO').unbind("click").on('click', function() {
		bindBackToDashBoard();
	}); 
	
	$('.idsConfirmYes').unbind("click").on('click', function() {
		if(confirmPopUpName == "refFIDSNIssued" || confirmPopUpName == "refFIDSIssued" ){
			//$('#notificationLink').modal('hide');
			openNewRefPopup(idsId,refFlowList);
		}
		else{
			bindCallBuildIDS();
		} 
	});
}

	function bindCallBuildIDS(){
 	var appId = $("#appId").val(); 
	window.location = $('#contextPath').val() + '/ids/buildIDS/'+appId;
	}

	function bindRefIncludeCurrentIDS(idsId,refFlowList){
		var idsId = idsId;
		var target = $("#contextPath").val() + '/ids/includeRef';
		var data = {
				 idsID :  idsId,
				 refFlowsIds:refFlowList,
		}
		$.ajax({
			type : "POST",
			url : target,
			data : JSON.stringify(data),
			contentType: 'application/json',
			success : function(data) {
				bindCallBuildIDS();
			},

		});
	}
	
	function bindBackToDashBoard(){
		window.location = $('#contextPath').val() + '/ids/dashboard';
	}
	
	//Notification Cards Actions

	//Update Family Linkage Notification Reject Action
	$(document).on(
			'click',
			'.rejectFamilyLinkage',
			function() {
				var requestUrl = $("#contextPath").val()
						+ "/mdm/actionItems/createRequestApp/reject";
				$.ajax({
					type : "POST",
					url : requestUrl,
					data : {
						'notificationId' : $($(this).closest('div')).find(
								'.notificationId').val(),
						'entityId' : $($(this).closest('div'))
								.find('.entityId').val(),
						'entityName' : $($(this).closest('div')).find(
								'.entityName').val(),
					},
					success : function(result) {
						reloadData();
					}
				});

			});

	//Status Change Notification Approv Action
	$(document)
			.on(
					'click',
					'.approveStatusNotification',
					function() {
						var notificationId = $(this)
								.attr('data-notificationId');
						var recordId = $(this).attr('data-eid');
						var target = $(this).attr('data-newStatus') == 'DROPPED' ? "actions/drop/approve"
								: "actions/deactivate/approve";
						$.ajax({
							type : "POST",
							url : $("#contextPath").val() + "/mdm/" + target,
							data : {
								'recordId' : recordId,
								'notificationId' : notificationId,
							},
							success : function(result) {
								reloadData();
							},
						});
					});

	//Status Change Notification Reject Action
	$(document).on('click','.rejectStatusNotification',function() {
						var notificationId = $(this)
								.attr('data-notificationId');
						var recordId = $(this).attr('data-eid');
						var target = $(this).attr('data-newStatus') == 'DROPPED' ? "actions/drop/reject"
								: "actions/deactivate/reject";
						$.ajax({
							type : "POST",
							url : $("#contextPath").val() + "/mdm/" + target,
							data : {
								'recordId' : recordId,
								'notificationId' : notificationId,
							},
							success : function(result) {
								reloadData();
							},
						});
					});
	
//Reference Entry Notification Delete Action
	
$(document).on('click', '.deleteRefEntry', function(){
		var	notificationId = $(this).siblings('input').val();
			clickedDeleteRefYes(notificationId);
		});	
	
function clickedDeleteRefYes(notificationId){
			$(document).on('click', '.deleteRefEntryYes', function(){
			$.ajax({
				url: $('#contextPath').val()+"/reference/dashboard/deleteDocument",
				method:"POST",
				cache:false,
				data:{id: notificationId},
				success : function(result) {
					reloadData();
				},
			});
		});
}

</script>