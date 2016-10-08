<script>
	$(document).ready(

			function() {
				$(".daterange-picker .date").daterangepicker({
					opens : 'left',
					locale : {
						format : 'MMM DD, YYYY',
						cancelLabel : 'Clear'
					}
				});
				$('.daterange-picker i').click(function() {
					$(this).parents(".daterange-picker").find('input').click();
				});

				$('.daterange-picker .date').on(
						'cancel.daterangepicker',
						function(ev, picker) {
							$(this).parents(".daterange-picker").find('input')
									.val('');
						});

				$('#newDocument').on("click", function() {
					$.ajax({
						url : "./searchApplicationForm",
						context : document.body
					}).done(function(data) {
						loadMessages();
						$('#dynamicData').html(data).show();
						//bind searchApplication call
						searchApplication();
						//bind autocomplete jurisdiction
						autocompleteJurisdiction();

					});
				}

				);
				$('#myModal2').on('shown.bs.modal', function() {

				})

			}

	);

	$(function() {
		$(".hidden-row").hide();
		$(".has-hidden-row span")
				.on(
						"click",
						function() {

							if ($(this).hasClass("icon-plus")) {
								$(this).parents(".has-hidden-row").addClass(
										"active");
								$(this).removeClass("icon-plus").addClass(
										"icon-minus");
								$(this).parents(".has-hidden-row").siblings(
										".hidden-row").toggle();
							} else if ($(this).hasClass("icon-minus")) {
								$(this).parents(".has-hidden-row").removeClass(
										"active");
								$(this).removeClass("icon-minus").addClass(
										"icon-plus");
								$(this).parents(".has-hidden-row").siblings(
										".hidden-row").toggle();
							}

						});



		////////////////////////////////////////

		/////////////////////////////////////////////////

		$(document).on('click', '.filter-dropdown', function(e) {
			e.stopPropagation();
		});
		$(".filter-dropdown .btn-cancel").on("click", function() {
			$("body").click();
		});
		//////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////
		$("#viewDoc").on(
				"click",
				function() {
					var $dataHeight = $("#viewDocContainer table").height()
							+ $("#viewDoc").height();

					if ($("#viewDocContainer").height() > 40) {
						$("#viewDocContainer").animate({
							"height" : 38
						}, 1000);
						$(this).text("+ View Related Documents");
					}

					else {
						$("#viewDocContainer").animate({
							"height" : $dataHeight
						}, 1000);
						$(this).text("- Hide Related Documents");
					}
				});

		//filter link

		$("#filterSearchLink").hide();

		$(document).on(
				"click",
				".nav.nav-tabs.custom-tabs li:last-child",
				function() {
					if ($(".search-dropdown .nav-tabs li:last-child").hasClass(
							"active")) {
						$("#filterSearchLink").show();
					}

				});
		$(document).on("click",
				".nav.nav-tabs.custom-tabs li:not(:last-child)", function() {
					$("#filterSearchLink").hide();

				});

		///////////////////////////////////////////////////////////////////////////
		$("#createDoc, #addNewDocDetail").hide();
		$("#showAddNewDoc").on("click", function() {
			$("#createDoc, #addNewDocDetail").show();
			$(this).hide();
		});
	});
	///////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////

	function removeModal() {
		$(this).on("click", function() {
			$("#myModal").modal('hide');
			$(".modal-backdrop").remove();
		});
	}

	//////////////////////////////////////////////////////////////////////
	//this method is called when search application -> dynamic data is clicked

	//////////////////////////////////////////////////////////////////////
	//this method is called when search application is clicked
	function searchApplication() {

		$("#SearchApplication")
				.on(
						"click",
						function() {
							var frm = $('#searchCorrespondenceForm');
							var status = $.blackbox.validator.SearchCorrespondenceFormValidator
									.validate();
							if (status) {
								frm
										.ajaxSubmit({
											url : frm.attr('action'),
											type : frm.attr('method'),
											success : function(data) {
												$('#dynamicData').html(data);

												$('#dynamicData').show();

												//bind autocomplete jurisdiction
												autocompleteJurisdiction();
												//bind autocomplete assignee 
												if (document
														.getElementById("assigneeData")) {
													autocompleteAssignee();
												}

												//bind search application call
												searchApplication();
												//bind create new correspondence call
												createNewCorrespondence();
												//bind createApplicationRequest call
												createApplicationRequest();
												//bind send application request call
												sendApplicationRequest();
												//bind urgent request call 
												createApplicationUrgent();
												//add datepicker
												addDatePicker();
												//upload button

												if (document
														.getElementById("uploadBtn") != null) {
													uploadbutton();
												}
												//cancel confirmation
												cancelNewCorrespondenceCreate();
											},
											error : function(request, status,
													error) {
												$('#dynamicData').html(
														"<h1>Not Found</h1>")
														.show();
											}
										});
							} else {
								return false;
							}

						});
	}
	///////////////////////////////////////////////////////////////////////
	//this method is called when Create on Create new document pop up is clicked
	function createNewCorrespondence() {
		$("#createNewCorrespondence")
				.on(
						"click",
						function() {
							var frm = $('#newCorrespondence');
							var status = $.blackbox.validator.NewCorrespondenceFormValidator
									.validate();
							if (status) {
								frm
										.ajaxSubmit({
											url : frm.attr('action'),
											type : frm.attr('method'),
											success : function(data) {
												$('#dynamicData').html(data);
												if ("true" == $('#response')
														.val()) {
													$('#myModal2')
															.modal('hide');
													location.reload();

												} else {
													$('#dynamicData')
															.html(data).show();
													//	$("#default").hide();
													//bind autocomplete jurisdiction
													autocompleteJurisdiction();
													//bind autocomplete assignee 
													if (document
															.getElementById("assigneeData")) {
														autocompleteAssignee();
													}

													//bind create new correspondence call
													createNewCorrespondence();
													//bind createApplicationRequest call
													createApplicationRequest();
													//bind send application request call
													sendApplicationRequest();
													//bind urgent request call 
													createApplicationUrgent();
													//add datepicker
													addDatePicker();
													//upload button
													uploadbutton();
												}

											},
											error : function(request, status,
													error) {
												// $('#successDiv').hide();
												// $('#divNotification').hide();
											}
										});
							} else {
								return false;
							}

						});
	}

	//////////////////////////////////////////////////////////////////////////
	//this method enables autocomplete for jurisdiction
	function autocompleteJurisdiction() {
		console.log($("#jurisdictionData").val().split(','));
		$("#jurisdiction").autocomplete({
			source : $("#jurisdictionData").val().split(','),
			minLength : 1,
		});
		$('#modalIns').modal('show');
		$("#jurisdiction").autocomplete("option", "appendTo", ".eventInsForm");
	}

	//////////////////////////////////////////////////////////////////////////
	//this method enables autocomplete for assignee
	function autocompleteAssignee() {
		console.log($("#assigneeData").val().split(','));
		$("#assignee").autocomplete({
			source : $("#assigneeData").val().split(','),
			minLength : 1,
		});
		$('#modalIns').modal('show');
		$("#assignee").autocomplete("option", "appendTo", ".eventInsForm");
	}

	///////////////////////////////////////////////////////////////////////////
	//this method opens popup for new create application request
	function createApplicationRequest() {
		$("#createApplicationRequest").on("click", function() {
			var frm = $('#createNewApplicationNoAccessForm');
			frm.ajaxSubmit({
				url : frm.attr('action'),
				type : frm.attr('method'),
				success : function(data) {
					$('#dynamicData').html(data);
					if ($('#response').val() == "true") {
						$('#myModal2').modal('hide');
					} else {
						$('#dynamicData').show();
					}
					//$("#default").hide();
					//bind autocomplete jurisdiction
					autocompleteJurisdiction();
					//bind autocomplete assignee 
					if (document.getElementById("assigneeData")) {
						autocompleteAssignee();
					}

					//bind create new correspondence call
					createNewCorrespondence();
					//bind createApplicationRequest call
					createApplicationRequest();
					//bind send application request call
					sendApplicationRequest();
					//bind urgent request call 
					createApplicationUrgent();
				    //cancel confirmation
					cancelNewCorrespondenceCreate();
					//add datepicker
					addDatePicker();
					//upload button
					uploadbutton();

				},
				error : function(request, status, error) {
					// $('#successDiv').hide();
					// $('#divNotification').hide();
				}
			});

		});

	}

	///////////////////////////////////////////////////////////////////////////
	//this method is used to create urgent correspondence
	function createApplicationUrgent() {
		$("#createNewCorresponcenceUrgent")
				.on(
						"click",
						function() {
							var frm = $('#newCorrespondenceUrgent');
							var status = $.blackbox.validator.NewCorrespondenceFormValidator
									.validate();
							if (status) {
								frm
										.ajaxSubmit({
											url : frm.attr('action'),
											type : frm.attr('method'),
											success : function(data) {
												$('#dynamicData').html(data);
												if ($('#response').val() == "true") {
													$('#myModal2')
															.modal('hide');
													location.reload();
												} else {
													$('#dynamicData').show();
												}

												//$("#default").hide();
												//bind autocomplete jurisdiction
												autocompleteJurisdiction();
												//bind autocomplete assignee 
												if (document
														.getElementById("assigneeData")) {
													autocompleteAssignee();
												}

												//bind create new correspondence call
												createNewCorrespondence();
												//bind createApplicationRequest call
												createApplicationRequest();
												//bind send application request call
												sendApplicationRequest();
												//bind urgent request call 
												createApplicationUrgent();
												//add datepicker
												addDatePicker();
												//upload button
												uploadbutton();

											},
											error : function(request, status,
													error) {
												// $('#successDiv').hide();
												// $('#divNotification').hide();
											}
										});
							} else {
								return false;
							}

						});

	}

	/////////////////////////////////////////////////////////////////////////////////////
	//////////// This method is used to populate upload label
	function uploadbutton() {
		document.getElementById("uploadBtn").onchange = function() {
			document.getElementById("uploadFile").value = $(this).val()
					.replace(/.*(\/|\\)/, '');
		};
		$("#removeFileUpload").on("click", function(e) {
			e.preventDefault();
			document.getElementById("uploadFile").value = "";
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////
	//this method is used to send create application request

	function sendApplicationRequest() {
		$("#SendCreateApplicationRequest")
				.on(
						"click",
						function() {
							var frm = $('#SendCreateApplicationRequestForm');
							var status = $.blackbox.validator.NewCorrespondenceFormValidator
									.validate();
							if (status) {
								frm
										.ajaxSubmit({
											url : frm.attr('action'),
											type : frm.attr('method'),
											success : function(data) {
												$('#dynamicData').html(data);
												if ($('#response').val() == "true") {
													$('#myModal2')
															.modal('hide');
													
												} else {
													$('#dynamicData').show();
												}
												//$("#default").hide();
												//bind autocomplete jurisdiction
												autocompleteJurisdiction();
												//bind autocomplete assignee 
												if (document
														.getElementById("assigneeData")) {
													autocompleteAssignee();
												}

												//bind create new correspondence call
												createNewCorrespondence();
												//bind createApplicationRequest call
												createApplicationRequest();
												//bind send application request call
												sendApplicationRequest();
												//bind urgent request call 
												createApplicationUrgent();
												////cancel confirmation
												cancelNewCorrespondenceCreate();
												//add datepicker
												addDatePicker();
												//upload button
												uploadbutton();

											},
											error : function(request, status,
													error) {

											}
										});
							} else {
								return false;
							}

						});

	}

	//////////////////////////////////////////////////////////////////////////////////
	//this method is used to create datepicker
	function addDatePicker() {
		$('#datetimepicker1').datepicker();
		$('#datetimepicker2').datepicker();
		$('#newCorrespondencedatetimepicker').datepicker({
			format : "M dd, yyyy"
		});
	}
	///////////////////////////////////////////////////////////////////////////////////
	//this method is used to load message properties
	function loadMessages() {
		try {
			$.blackbox.util.CommonUtil
					.loadResourceBundle(encodeURIComponent('i18n/correspondence'));
		} catch (error) {
			console.log(error);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////
	//this method is used to open popup for cancel in case of newApplicationCreate popup
	function cancelNewCorrespondenceCreate() {
		$("#newCorrespondenceFormCancel")
				.on(
						'click',
						function() {
							showConfirmationBoxNewCorrespondence();
							$('#confirmationBoxNewCorrespondence')
									.find('.msg')
									.text(
											'Your changes will not be saved. Do you want to proceed ');
						});

		$(".newCorrespondenceYes").click(function() {
			$(".overlay").hide();
			$('#confirmationBoxNewCorrespondence').hide();
		});
		$(".newCorrespondenceNo").click(function() {
			$(".overlay").hide();
			$('#confirmationBoxNewCorrespondence').hide();
			$('#myModal2').modal('show');
		});
	}
</script>
