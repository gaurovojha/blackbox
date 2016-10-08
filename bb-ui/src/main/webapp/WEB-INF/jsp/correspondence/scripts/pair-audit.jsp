<script>

	$('#pairAudit').on('click', function() {
		$("#auditFooter,#auditDashBoard").hide();
		$("#uploadFileAudit,#uploadBtnAudit").val("");
	});

	$('#uploadBtnAudit').on('click', function() {
		$("#uploadBtnAudit").val("");
	});

	$('#uploadBtnAudit').on('change',function() {
						var fileName = $("#uploadBtnAudit").val().replace(/.*(\/|\\)/, '');
						$("#uploadFileAudit").val(fileName);
						var file = new FormData();
						file.append("fileUpload", document.getElementById("uploadBtnAudit").files[0]);
						$("#auditFooter,#auditDashBoard").hide();
						$(".data").html("");

						var validFile = validateFileExtension(fileName);
						if (validFile == true) {
							$.ajax({
										url : '../correspondence/pairAudit/validate',
										type : 'POST',
										data : file,
										enctype : 'multipart/form-data',
										processData : false,
										contentType : false,
										cache : false,
										success : function(response) {
											$("#auditFileUploadError").hide();
											$("#auditDashBoard, #auditDateRangeTable, #auditFooter").show();
											$("#initialDate").html(response.InitialDate);
											$("#finalDate").html(response.FinalDate);
										},
										error : function(error) {
											showErrorMessages('Incorrect file, please download file again from PAIR and upload');
										}
									});
						} else {
							showErrorMessages('Invalid file type.Only XML File can be uploaded');
						}
					});

	function validateFileExtension(fileName) {
		var validFile = false;
		if (fileName.length > 0) {
			var validExtension = ".xml";
			if (fileName.substr(fileName.length - validExtension.length,
					validExtension.length).toLowerCase() == validExtension
					.toLowerCase()) {
				validFile = true;
			}
		}
		return validFile;
	}

	function showErrorMessages(message) {
		$("#auditDashBoard").show();
		$("#auditDateRangeTable").hide();
		$("#auditFileUploadErrorMsg").html(message);
	}

	$('#auditSubmit').on(
			'click',
			function() {
				var file = new FormData();
				file.append("fileUpload", document
						.getElementById("uploadBtnAudit").files[0]);
				$.ajax({
					url : '../correspondence/pairAudit/upload',
					type : 'POST',
					data : file,
					enctype : 'multipart/form-data',
					processData : false,
					contentType : false,
					cache : false,
					complete : function() {
						$('#myModal3').modal('hide');
					}
				});
			});

	$(".auditCancel")
			.on(
					'click',
					function() {
						showConfirmationBox();
						$('#confirmationBox').find('.msg').text('Your changes will not be saved. Do you want to proceed ');
					});
	
	$(".auditYes").click(function() {
		$(".overlay").hide();
		$('#confirmationBox').hide();
	});
	$(".auditNo").click(function() {
		$(".overlay").hide();
		$('#confirmationBox').hide();
		$('#myModal3').modal('show');
	});
</script>