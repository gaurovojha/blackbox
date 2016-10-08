<script type="text/javascript" src="${contextPath}/assets/js/common/util/validation-util.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/common/validator/application-validator.js"></script>

<script type="text/javascript">
	$('#back').hide();
	/* $('.step-element').hide();
	$('#step1').show();
	 */$('#submit').hide();
	function indexofacticveTab() {
		for (var i = 0; i < $('.initiateTag').length; i++) {
			if ($($('.initiateTag')[i]).hasClass('active')) {
				continue;
			} else {
				break;
			}
		}
		return i;
	}
	var urlArray = [ 'usPatent', 'usPublication', 'foreign', 'npl','confirmationPage' ];
	$('#continue').on('click',function() {
				var index = indexofacticveTab();
				console.log(index);
				console.log($('#contextPath').val());

				$('.initiateTag').hasClass()
				if ($('#' + urlArray[index + 1]).html() === '') {
					$.ajax({
						url : $('#contextPath').val() + '/ids/'
								+ urlArray[index + 1] + '/'+$('#idsAppId').val(),
						type : 'POST',
						data:{
							sourceURL:"buildIDS"
						},
						success : function(data) {
							$('#back').show();
							var ullist = $('ul.patentNav li');
							var displayElement = $('.step-element');
							$(ullist[index]).addClass('active');
							//console.log(ullist[licount].class);
							//console.log('hi'+licount);
							//console.log(data);
							$('.step-element').hide();
							$('#' + urlArray[index + 1]).show();
							$('#' + urlArray[index + 1]).html(data);
							$(ullist[index + 1]).removeClass('disabled');
							changeStep(index + 1);
							refereshClickevent();
							moreBtnClk();
							showMoreBtn();
							selCitationBtnClick();
							dropEventBind();
							includeIdsEvent();
						}
					});
				} else {
					$('#back').show();
					var ullist = $('ul.patentNav li');
					var displayElement = $('.step-element');
					$(ullist[index]).addClass('active');
					//console.log(ullist[licount].class);
					//console.log('hi'+licount);
					//console.log(data);
					$('.step-element').hide();
					$('#' + urlArray[index + 1]).show();
					$('#' + urlArray[index + 1] + '> .step-element').show();
					$(ullist[index + 1]).removeClass('disabled');
					changeStep(index + 1);
					showMoreBtn();
					selCitationBtnClick();
				}
			});

	function changeStep(licount) {
		if (licount === 0) {
			$('#back, #submit').hide();
			$('#continue').show();
		} else if (licount > 0 && licount < 4) {
			$('#submit').hide();
			$('#continue, #back').show();
		} else if (licount === 4) {
			$('#continue').hide();
			$('#submit, #back').show();
		} else {
			$('#back,#submit, #continue').show();
			//$('').hide();
		}
	}

	$('ul.patentNav li').on('click', function() {
		if ($(event.target).parent('li').hasClass('active')) {
			$('.step-element').parent('div').hide();
			$(event.target).parent('li').removeClass('active');
			$(event.target).parent('li').nextAll().removeClass('active');
			$(event.target).parent('li').nextAll().addClass('disabled');
			var index = indexofacticveTab();
			$('#' + urlArray[index]).show();
			$('#' + urlArray[index] + '> .step-element').show();
			changeStep(index);
			showMoreBtn();
			selCitationBtnClick();
		}

		/* console.log($('ul.patentNav li').length);
		if ($(event.target).parent('li').nextAll().length+ licount > ($('ul.patentNav li').length - 1)) {
			licount = $('ul.patentNav li').length
					- $(event.target).parent('li').nextAll().length
					- 1;
			($(event.target).parent('li').nextAll().length);
			($(event.target).parent('li').nextAll()
					.removeClass('active'));
			($(event.target).parent('li').nextAll()
					.addClass('disabled'));
			($(event.target).parent('li')
					.removeClass('disabled'));
			($(event.target).parent('li').removeClass('active'));
			(licount);
			var displayElement = $('.step-element');
			$(displayElement).hide();
			$(displayElement[licount]).show();
			changeStep();
		} */
	});

	$('#back').on('click', function() {

		$('#back').show();
		var index = indexofacticveTab();
		var ullist = $('ul.patentNav li');
		var displayElement = $('.step-element');
		$(ullist[index]).addClass('disabled');
		$('#' + urlArray[index]).hide();
		$('#' + urlArray[index - 1]).show();
		$(displayElement[index]).hide();
		$(displayElement[index - 1]).show();
		$('#previewPage').hide();
		$(ullist[index - 1]).removeClass('active');
		changeStep(index - 1);
		showMoreBtn();
		selCitationBtnClick();

	});
	function refereshClickevent() {
		$('.Url-referesh').unbind("click")
		$('.Url-referesh').on('click',function() {
					console.log('Url-referesh click');
					refereshAjaxQuery();
	});
	}
	refereshClickevent();
	function refereshAjaxQuery(){
		var index = indexofacticveTab();
		$.ajax({
			url : $('#contextPath').val() + '/ids/'
					+ urlArray[index] + '/'+$('#idsAppId').val(),
			type : 'POST',
			data:{
				sourceURL:"buildIDS"
			},
			success : function(data) {
				$('#back').show();
				var ullist = $('ul.patentNav li');
				var displayElement = $('.step-element');
				//$(ullist[index]).addClass('active');
				$('.step-element').hide();
				$('#' + urlArray[index]).show();
				$('#' + urlArray[index]).html(data);
				//$(ullist[index]).removeClass('disabled');
				changeStep(index);
				refereshClickevent();
				moreBtnClk();
				showMoreBtn();
				selCitationBtnClick();
				dropEventBind();
			}
		});
	}

	function moreBtnClk() {
		$('.show-more').unbind('click');
		$('.show-more').on('click',function() {
							var index = indexofacticveTab();
							console.log(urlArray[index]);
							console.log($('#' + urlArray[index] + ' > tr').length);
							console.log($('#' + urlArray[index]+' .step-element tbody tr').length);
							$.ajax({
								url : $('#contextPath').val() + '/ids/'+urlArray[index]+'/'+$('#idsAppId').val(),
										type : 'POST',
										data : {
											offset : $('#' + urlArray[index]+ ' .step-element tbody tr').length,
											sourceURL: "buildIDS"
										},
										success : function(data) {
											$('#back').show();
											var ullist = $('ul.patentNav li');
											var displayElement = $('.step-element');
											//$(ullist[index]).addClass('active');
											//$('.step-element').hide();
											//$('#'+urlArray[index]).show();
											//$('#'+urlArray[index]).html(data);
											$('#'+ urlArray[index]+ ' .step-element tbody').append(data);
											//$(ullist[index]).removeClass('disabled');
											$('#'+ urlArray[index]+ ' .recordsFiltered').html($('#'+ urlArray[index]+ ' .step-element tbody tr').length);
											changeStep(index);
											refereshClickevent();
											showMoreBtn();
											selCitationBtnClick();
											dropEventBind();
										}
									});
						});
	}
	moreBtnClk();
	function showMoreBtn() {
		var index = indexofacticveTab();
		if (Number($('#' + urlArray[index] + ' .recordsTotal').html()) === $('#'+urlArray[index] + ' .step-element tbody tr').length) {
			$('#' + urlArray[index] + ' .text-right.form-group a').hide();
		}
		 else {
					$('#' + urlArray[index] + ' .text-right.form-group a').show();
		}
	}
	showMoreBtn();

	function selCitationBtnClick() {
		$('.selfCitationBtn').unbind('click');
		$('.selfCitationBtn').on('click', function() {
			$('#selfCitationForm').submit();
		});
	}
	selCitationBtnClick();
	
	function includeIdsEvent() {
		$('#idsInclude').unbind('click');
		$('#idsInclude').on('change', function() {
			if ($('#idsInclude').prop('checked')) {
				$('.checkboxElem').prop('checked', true);
				$('.checkboxElem').prop('disabled', true);
			} else {
				$('.checkboxElem').prop('checked', false);
				$('.checkboxElem').prop('disabled', false);
			}
		})
	}
	var checkboxSelected=[];
	
	$('#submit').on('click', function() {
		var checkboxItem=$('.checkboxElem');
		//console.log(checkboxItem);
		//console.log(checkboxItem.length);
		checkboxSelected=[];
		for(var i=0;i<checkboxItem.length;i++){
			if($(checkboxItem[i]).prop('checked')){
				checkboxSelected.push( $(checkboxItem[i]).attr('data-refID'));
			}
		}
		//console.log(checkboxSelected);
		 $.ajax({
			'url' : $('#contextPath').val() + '/ids/selectedSourceRef/'+$('#idsAppId').val(),
			'type':'POST',
			'data':{
				selectedSourceRef:checkboxSelected,
			},
			'success' : function(data) {
				console.log(data);
				//if(Number(data)>0){
					//previewPageReqest();
				//}
				bindRefsCount();
			}
		}); 
		
	});
	function bindRefsCount(){
	$.ajax({
		'url' : $('#contextPath').val() + '/ids/buildIDS/checkRefCount/'+$('#idsAppId').val(),
		"data":{
			idsID:$('#idsDbID').val()
		},
		success : function(data) {
			bindShowRefMessages(data.US_PUBLICATION,data.FP,data.NPL);
		}
	});
}

function bindShowRefMessages(usRefs,fpRefs,nplRefs){
	if(usRefs || fpRefs || nplRefs){
	$('#refCountPopUP').modal('show');
	if(usRefs){
		$('#popupMsgs').append("<p id='usRefs'></p>");
		$("#usRefs").html(jQuery.i18n.prop('ids.buildids.preview.popup.usreferences'));
	}
	
	if(fpRefs){
		$('#popupMsgs').append("<p id='fpRefs'></p>");
		$("#fpRefs").html(jQuery.i18n.prop('ids.buildids.preview.popup.fpreferences'));
	}
	
	if(nplRefs){
		$('#popupMsgs').append("<p id='nplRefs'></p>");
		$("#nplRefs").html(jQuery.i18n.prop('ids.buildids.preview.popup.nplreferences'));
	}
	
	$('#refPopupContinue').unbind('click').on('click', function() {
		bindViewPreviewPage();
	});
  }
	else{
		bindViewPreviewPage();
	}
}

function bindViewPreviewPage(){
		$.ajax({
			'url' : $('#contextPath').val() + '/ids/previewPage/'+$('#idsAppId').val(),
		'data':{
			idsID:$('#idsDbID').val()
		},
			'success' : function(data) {
				$('.step-element').hide();
				$('#previewPage').html(data);
				console.log($('.panel-title a').attr('aria-expanded'));
				preview();
				var index = indexofExpand();
				previewAjaxCall(index);
				$('#previewPage').show();
			}
		});
}
	/*function previewPageReqest(){
		$.ajax({
			'url' : $('#contextPath').val() + '/ids/previewPage/'+$('#idsAppId').val(),
			'data':{
				idsID:$('#idsDbID').val()
			},
			'success' : function(data) {
				$('.step-element').hide();
				$('#previewPage').html(data);
				console.log($('.panel-title a').attr('aria-expanded'));
				preview();
				var index = indexofExpand();
				previewAjaxCall(index);
				$('#previewPage').show();
			}
		}); 
	}*/
	function preview() {
		$('.panel-heading').on(	'click',function() {
							//aria-controls="collapseOne"
							//console.log($(event.target).children().length);
							var targetattr = $(event.target).attr("aria-controls");
							console.log(targetattr);
							var $targetattr = $(event.target);
							//console.log(indexofExpandTab($targetattr));
							console.log($targetattr.children().length);

							if ($targetattr.children().length > 0) {
								//if ($targetattr.hasClass('panel-title')) {
								var $aElement = $(event.target);

							} else {
								console.log('in else')
								console.log($targetattr.parent('a'));
								var $aElement = $($targetattr.parent('a'));

							}
							for (var i = 0; i < $('.panel-title a').length; i++) {
								if ($($('.panel-title  a')[i]).attr('aria-controls') === $($aElement).attr('aria-controls')) {
									break;
								} else {
									continue;
								}
							}
							console.log('final i ' + i);
							if (i === $('.panel-title a').length) {
								alert('all condition fail' + i);
							} 
							else if ($($aElement).hasClass('collapsed')) {
								var targetId = $($aElement).attr('aria-controls');
								console.log($('#' + targetId+ ' .step-element').length);
								if ($('#' + targetId + ' .step-element').length === 0) {
									previewAjaxCall(i);
								}
							}
							 else {

							}
						});
		/* $('.panel-group.user-management').on('show.bs.collapse',function(){
			console.log('hi event');
			window.abc=event.target;
			console.log(event.target);
		}) */
	}
	function previewAjaxCall(index) {
		$.ajax({
			url : $('#contextPath').val() + '/ids/' + urlArray[index] + '/'+ $('#idsAppId').val(),
			type : 'POST',
			data : {
				sourceURL : "previewIDS"
			},
			success : function(data) {
				//console.log(data);
				$('#back').show();
				var ullist = $('.panel-title a');
				var targetId = $(ullist[index]).attr('aria-controls');
				$('#' + targetId + ' .previewIDS').html(data);
				previewShowMoreClick();
				showMorePreviewBtn(targetId);
				//$('.step-element').hide();

			}
		});
	}

	function previewShowMoreClick() {
		$('.user-management .show-more').unbind('click');
		$('.user-management .show-more').on('click',function() {
					var index = indexofExpand();
					var ullist = $('.panel-title a');

					var elemnetId = $(ullist[index]).attr('aria-controls');

					$.ajax({
						url : $('#contextPath').val() + '/ids/'	+ urlArray[index] + '/' + $('#idsAppId').val(),
						type : 'POST',
						data : {
							offset : $('#' + elemnetId+ ' .step-element tbody tr').length,
							sourceURL : "previewIDS"
						},
						success : function(data) {
							$('#back').show();

							$('#' + elemnetId + ' .step-element tbody').append(data);
							showMorePreviewBtn(elemnetId);

						}
					});
				});
	}
	function indexofExpand() {
		var pannela = $('.panel-title a');

		for (var i = 0; i < $('.panel-title a').length; i++) {
			console.log($($('.panel-title a')[i]).attr('aria-expanded'));
			if ($($('.panel-title a')[i]).attr('aria-expanded') === "true") {
				console.log('hi');
				break;
			}
		}
		return i;
	}
	function showMorePreviewBtn(parentElement) {
		console.log('show more hide');
		console.log($('#' + parentElement + ' .show-more'));
		console.log($('#' + parentElement + ' .totalRecords').val());
		console.log(Number($('#' + parentElement + ' .totalRecords').val()));
		console.log($('#' + parentElement + ' .step-element tbody tr').length);
		if (Number($('#' + parentElement + ' .totalRecords').val()) == $('#'+ parentElement + ' .step-element tbody tr').length) {
			$('#' + parentElement + ' .show-more').hide();
		}
		 else {
			$('#' + parentElement + ' .show-more').show();
		}
	}
	var delREfID;

	function dropEventBind() {
		$('.drop-all').unbind('click');
		$('.drop-all').on('click', function() {
			//$('#dropRefAllIdsPopUp').show();
			//$('#dropRefAllIdsPopUp').addClass('in');
			delREfID = $(event.target).attr('data-RefID');
		});
		$('.drop-this').unbind('click');
		$('.drop-this').on('click', function() {
			//$('#dropRefFromThisIdsPopUp').show();
			delREfID = $(event.target).attr('data-RefID');
		});
	}

	$('#dropRefAllIdsPopUp .yes').on('click', function() {
		$.ajax({
			url : $('#contextPath').val() + '/ids/dontFileReference',
			type : 'POST',
			data : {
				refID : delREfID,
			},
			success : function(data) {
				if (Number(data) > 0) {
					refereshAjaxQuery();
				}
			},
			error : function(err) {
				console.log(err);
			}
		});
	});
	$('#dropRefFromThisIdsPopUp .yes').on('click', function() {
		$.ajax({
			url : $('#contextPath').val() + '/ids/dropRefFromIDS',
			type : 'POST',
			data : {
				refID : delREfID,
			},
			success : function(data) {
				if (Number(data) > 0) {
					refereshAjaxQuery();
				}
			},
			error : function(err) {
				console.log(err);
			}
		});
	});
	
	/*- ---------------------------------------------------------
		--------------------------------------------- Discard IDS
	--------------------------------------------------------- */
	function bindDiscardIDS() {
		$('.btnDiscardIDS').on('click', function() {
			openPopup('#popupDiscardIDS');
		});

		$('#btnYesDiscardIDS').on('click',function() {
					$.post($('#contextPath').val() + '/ids/discard/'+ $('#idsAppId').val());
					window.location.href = $('#contextPath').val()+ '/ids/dashboard';
				});

		$('#btnCancelDiscardIDS').on('click', function() {
			closePopup();
		});
	}

	function openPopup(selector) {
		$(selector).show();
		$(selector).wrap("<div class='overlay'>");
	}

	function closePopup() {
		$(".idsPopup").hide();
		$(".idsPopup").unwrap("<div class='overlay'>");
	}

	/*- ---------------------------------------------------------
		---------------------------- IDS Certification Statement
		--------------------------------------------------------- */
	$('#btnCertificationStatement').on('click', function() {
		openPopup('#popup_idsCertificationStatement');
	});

	$('#popup_idsCertificationStatement').on('show.bs.modal', function() {
		var target = $('#contextPath').val() +  "/ids/certificate/" + $('#idsAppId').val() + "/" + $('#idsDbID').val();

				$.get(target, function(data) {
					$('#popup_idsCertificationStatement').html(data);
					bindCertificationControls();
				});
			});

	function bindCertificationControls() {
		$('#ddAttorney').on('change', function() {
			var $selected = $(this).find('option:selected');

			if ($selected.val() === '') {
				$('#lblRegistrationNo').html('');
				$('#lblSignature').html('');
			} else {
				$('#lblRegistrationNo').html($selected.attr('regNo'));
				$('#lblSignature').html('/' + $selected.text() + '/');
			}
		});

		$('.mutuxCheck').click(function() {
			var checkedState = !$(this).is('checked');
			$('.mutuxCheck').prop('checked', false);
			$(this).prop('checked', checkedState);
		});

		$('.certificationCheck').on('change', function() {
			var checked = [];
			$('.certificationCheck:checked').each(function() {
				checked.push(parseInt($(this).attr('checkno')));
			});

			var $lblFilingFee = $('#lblFilingFee');
			if (contains(checked, 4)) {
				$lblFilingFee.html($('#filingFee').val());
			} else if (contains(checked, 5)) {
				$lblFilingFee.html(0);
			}

		});

		function contains(array, element) {
			return array.indexOf(element) !== -1;
		}

		bindCertificationButtons();
	}

	function bindCertificationButtons() {
		bindDiscardIDS();
		bindRequestApproval();
		bindAddPdfAction();
	}

	function bindAddPdfAction() {
		$("#chkCertificationAttached").on("click", function() {
			if ($(this).is(":checked")) {
				$("#addPdfBtn").show();
			} else {
				$("#addPdfBtn").hide();
			}
		});

		$("#addPdfBtn").on("click", function() {
			$("#browserPdf").click();
		});

		$("#browserPdf").on('change', function() {
			$("#addPdfBtn").text(parseFileName($(this).val()));
		});

		function parseFileName(filePath) {
			return filePath.substring(filePath.lastIndexOf('\\') + 1);
		}
	}

	function bindRequestApproval() {
		$('#btnRequestApproval').on('click', function() {

			if (validCertificationStatement()) {
				submitCertificate();
			}
		});
	}

	function validCertificationStatement() {
		var status = true;

		// Atleast 1 checkbox should be selected
		var numChecked = $('.certificationCheck:checked').length;
		if (numChecked === 0) {
			status = false;
			$('#divErrorCertificationCheck').html(jQuery.i18n.prop('ids.certificate.noTnC.accepted'));
		} else {
			$('#divErrorCertificationCheck').html('');
		}

		// Certification statement must be attached (conditionally)
		var attachedmentRequired = $('.addPdf').is(':checked');
		var attachmentProvided = $('#browserPdf').val() !== '';
		if (attachedmentRequired && !attachmentProvided) {
			status = false;
			$('#errorAddPdf').html(jQuery.i18n.prop('ids.certificate.certificate.noAttachment'));
		} else {
			$('#errorAddPdf').html('');
		}

		// Attorney user must be specified
		if ($('#ddAttorney').val() === '') {
			status = false;
			$('#errorAttorney').html(jQuery.i18n.prop('ids.certificate.blank.attorney'));
		} else {
			$('#errorAttorney').html('');
		}

		return status;
	}

	function submitCertificate() {
		var $form = $('#formIdsCertificate');

		$form.ajaxSubmit({
			url : $form.attr('action'),
			type : $form.attr('method'),
			success : function(result) {
				if (result === 'success') {
					window.location = $('#contextPath').val() + '/ids/dashboard';
				} else {
					console.log(result);
				}
			},
			error : function(request, status, error) {}
		});
	}

	/*- ---------------------------------------------------------
		------------------------------------- Edit Application
		--------------------------------------------------------- */
	var appValidator = null;
	(function() {
		appValidator = $.blackbox.validator.ApplicationValidator;
		bindEditAppControls();
		bindDiscardIDS();
		dropEventBind();
		try {
			$.blackbox.util.CommonUtil.loadResourceBundle(encodeURIComponent('i18n/ids'));
		} catch (error) {
			console.log(error);
		}
	})();

	function bindEditAppControls() {
		$('#btnEditApp').on('click',function() {
					var $this = $(this);
					$this.parents(".info-patent").find(".form-control-static:not(:first)").hide();
					$(".edit-view").show();
					$this.hide();
					$this.siblings(".view-ref-btn").show();
				})

		$('#btnCancelEditApp').on('click',function() {
					var $form = $("#formEditApp");
					$.ajax({
						type : $form.attr('method'),
						url : $('#contextPath').val() + '/ids/resetApp/'+ $('#idsAppId').val(),
						success : function(response) {
							$('#idsAppInfo').html(response);
							bindEditAppControls();
						},
					});
				});

		$("#btnSaveEditApp").on('click', function() {
			var $this = $(this);
			if (appValidator.prototype.validateIDSApp()) {
				var $form = $("#formEditApp");
				$.ajax({
					type : $form.attr('method'),
					url : $form.attr('action'),
					data : $form.serialize(),
					success : function(response) {
						$('#idsAppInfo').html(response);
						bindEditAppControls();
					},
				});

				/*-
				$this.parents(".info-patent").find(".form-control-static").show();
				$(".edit-view, .view-ref-btn").hide();
				$this.hide();
				$this.siblings("#btnEditApp").show();
				 */
			}
		});

		$('#dpFilingDate').datepicker({
			format : "M dd, yyyy"
		});

	}
	
	/* function cancelIDS() {
		$('#cancelIDS').on('click', function(){
			showConfirmationBox("Do you want to cancel FILE IDS ?")
		});
	}*/
	
	function submitFileIDS(){
		$('#submitFileIDS').on('click', function(){
			$.ajax({
				type : "GET",
				url : $('#contextPath').val() + '/ids/fileIDS/submit/'+$('#idsAppId').val() + '/' + $('#idsDbID').val(),
				success : function(data) {
					$('#popup_idsCertificationStatement').html(data);
					$('#popup_idsCertificationStatement').modal('show');
					bindCertificationControls();
					//fileIDSMySelf();
				}
			});
		});
	}
	
	
	/* function fileIDSMySelf() {
		$('#fileMySelfConfirmation').on('click', function(){
			showConfirmationBox("IDS Confirmation Message");
		});
	} */
	
	function showConfirmationBox(text) {
		$('.fileIDSMySelf').find('.msg ').text(
				'Are you sure you want to select - ' + text  +'?');
		$('.fileIDSMySelf').show();
		$('.fileIDSMySelf').wrap("<div class='overlay'>");
	}
	
	function hideConfirmationBox() {
		$('.fileIDSMySelf').hide();
		$('.fileIDSMySelf').unwrap("<div class='overlay'>");
	}
	
</script>