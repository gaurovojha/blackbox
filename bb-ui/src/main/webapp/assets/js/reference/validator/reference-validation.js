$(function () {
    $('.datepicker').datepicker({
        format: "M dd, yyyy"
    });
});
$(function () {
    $("#usData, #nplData, #foreignData, #nplCheckbox, #BtnSubmit ,#appSerialNo, .entry-block, #addBtn").hide();
})
//1

$(function () {
    $("#usData, #nplData, #foreignData, #nplCheckbox, #appSerialNo, #BtnSubmit ,.entry-block, #addBtn").hide();
    $('#usApp').on('change',function(){
    	/*if(event.target.checked){
    		$('#usUnpublishApplication').hide();
    		$('#appSerialNo').show();
    	}
    	else{
    		$('#usUnpublishApplication').show();
    		$('#appSerialNo').hide();
    	}*/
    	nplFeildselection();
    })
    $("#referenceEntries").on("change", function () {
        $('span.error').html('');
        $('.form-element').val('');
        $('.switch-input').attr('disabled', true);
        $('#nplCheckbox').hide();
        $('#BtnSubmit').show();
        $('input[type=text]').attr('disabled',true);
        $('select.form-element').attr('disabled',true);
        if ($(this).val() == "us") {
            $('#usData').show();
            $('#foreignData').hide();
            $('#nplData').hide();
            $("#refEntriesContainer").empty();
/*
            $('#fpKindCode').attr('disabled', true);
            $('#fgnJurisdictioin').attr('disabled', true);
            $('#fgnPatentNo').attr('disabled', true);

            $('#fgnPublicationDate').attr('disabled', true);
            $('#fgnPatenteeName').attr('disabled', true);
            $('#FPComments').attr('disabled', true);
            $('#PdfFile').attr('disabled', true);
*/            $('#usJurisdiction').attr('disabled', false);


            if (document.querySelector('.switch-input').checked) {
         /*       $('#usApplicantName').attr('disabled', true);
                $('#publicationDate').attr('disabled', true);
                $('#usComments').attr('disabled', true);
         */       $('#usKindCode').attr('disabled', false);
                $('#usPatentNo').attr('disabled', false);


                $('#foreignData').hide();
                $('#usData').show();
                $('#nplData').hide();
                $("#refEntriesContainer").empty();
                //$("#refEntriesContainer").append(foreignData);
                $("#addBtn").show();
                $('#InMannualUS').hide();
                $("#nplCheckbox").hide();
                $("#manualAdd").attr("value", "false");
            }
            else {
                $('#usApplicantName').attr('disabled', false);
                $('#publicationDate').attr('disabled', false);
                $('#usComments').attr('disabled', false);
                $('#usKindCode').attr('disabled', false);
                $('#usPatentNo').attr('disabled', false);
                $('#foreignData').hide();
                $('#usData').show();
                $('#nplData').hide();
                $("#refEntriesContainer").empty();
                //$("#refEntriesContainer").append(foreignData);
                $("#addBtn").show();
                $('#InMannualUS').show();
                $("#nplCheckbox").hide();
                $("#manualAdd").attr("value", "true");
            }

            //$("#refEntriesContainer").append(usHtml);
            $("#addBtn").show();
        }
        else if ($(this).val() == "foreign") {

/*            $('#usApplicantName').attr('disabled', true);
            $('#publicationDate').attr('disabled', true);
            $('#usComments').attr('disabled', true);
            $('#usKindCode').attr('disabled', true);
            $('#usPatentNo').attr('disabled', true);
*/
            $('#usJurisdiction').attr('disabled', true);

            if (document.querySelector('.switch-input').checked) {

                $('#fpKindCode').attr('disabled', false);
                $('#fgnJurisdictioin').attr('disabled', false);
                $('#fgnPatentNo').attr('disabled', false);

         /*       $('#fgnPublicationDate').attr('disabled', true);
                $('#fgnPatenteeName').attr('disabled', true);
                $('#FPComments').attr('disabled', true);
                $('#PdfFile').attr('disabled', true);
*/
				 $("#manualAdd").attr("value", "false");


                $('#usData').hide();
                $('#foreignData').show();

                $('#nplData').hide();
                $("#refEntriesContainer").empty();
                //$("#refEntriesContainer").append(foreignData);
                $("#addBtn").show();
                $('#InManualFP').hide();
                $("#nplCheckbox").hide();
            }
            else {
                $('#fpKindCode').attr('disabled', false);
                $('#fgnJurisdictioin').attr('disabled', false);
                $('#fgnPatentNo').attr('disabled', false);

                $('#fgnPublicationDate').attr('disabled', false);
                $('#fgnPatenteeName').attr('disabled', false);
                $('#FPComments').attr('disabled', false);
                $('#PdfFile').attr('disabled', false);

                $("#manualAdd").attr("value", "true");
                
                $('#usData').hide();
                $('#foreignData').show();
                $('#InManualFP').show();
                $('#nplData').hide();
                $("#refEntriesContainer").empty();
                //$("#refEntriesContainer").append(foreignData);
                $("#addBtn").show();

                $("#nplCheckbox").hide();
            }
        }
        else if ($(this).val() == "npl") {
            $('#nplCheckbox, #nplData, #addBtn').show();
            $('#foreignData, #usData').hide();
            $("#refEntriesContainer").empty();
            //$("#nplCheckbox, #addBtn").show();
            nplFeildselection();
        }
        else {
            $('.switch-input').attr('disabled', false);
            $("#refEntriesContainer").empty();
            $("#addBtn, #BtnSubmit ").hide();
            $("#usData, #nplData, #foreignData, #nplCheckbox, #addBtn").hide();
        }

    });
});
function nplFeildselection(){
	$('input[type=text]').val('');
	$('input[type=text]').attr('disabled',true);
	$('select.form-element').attr('disabled',true);
	$('#usJurisdiction').val($('#jurisdictionType').val());
	if(document.querySelector('#usApp').checked){
		$('#usUnpublishApplication').hide();
		$('input[type=text]').attr('disabled',false);
		$('#usUnpublishApplication input[type=text]').attr('disabled',true);
		$('#appSerialNo').show();
	}
	else{
		$('#usUnpublishApplication').show();
		$('#appSerialNo').hide();
		$('#nplData input[type=text]').attr('disabled',false);
		$('#appSerialNo').attr('disabled',true);
	}
}

validationUtil = $.blackbox.util.ValidationUtil;
appValidator = $.blackbox.validator.ApplicationValidator;
try {
    $.blackbox.util.CommonUtil.loadResourceBundle(encodeURIComponent('i18n/mdm'));
} catch (error) {
    console.log(error);
}
//bindInitPageControls();

//2
function validFpForm() {
    var valid = false;
    /*var feildsID=['fgnJurisdictioin', 'fgnPatentNo', 'fgnPatenteeName', 'fgnPublicationDate', 'fpKindCode'];
    var feildsKey=['jurisdiction','jurisdiction','jurisdiction','jurisdiction','jurisdiction'];
    valid=validationUtil.checkMandatoryFields(feildsID,feildsKey,'mdm.app.error.empty.');
    *//*valid = validationUtil.checkRequiredFields(['fgnJurisdictioin', 'fgnPatentNo', 'fgnPatenteeName', 'fgnPublicationDate', 'fpKindCode'],
        ['mdm.app.error.empty.', 'Please enter a valid Patent Number#', 'Please enter a valid Patentee Name', 'Please enter a valid Publication Date', 'Please select a valid Kind Code']);*/
    valid = validationUtil.checkRequiredFields(['fgnJurisdictioin', 'fgnPatentNo', 'fgnPatenteeName', 'fgnPublicationDate', 'fpKindCode'],
        ['Please enter a valid Jurisdictioin', 'Please enter a valid Patent Number#', 'Please enter a valid Patentee Name', 'Please enter a valid Publication Date', 'Please select a valid Kind Code']);
    return valid;
}
function validFpFormAuto() {
    var valid = false;
    valid = validationUtil.checkRequiredFields(['fgnJurisdictioin', 'fgnPatentNo', 'fpKindCode'],
        ['Please enter a valid Jurisdictioin', 'Please enter a valid Patent Number#', 'Please select a valid Kind Code']);
    return valid;
}
//3


//2
function validUSForm() {

    var valid = false;
    valid = validationUtil.checkRequiredFields(['usPatentNo', 'publicationDate', 'usApplicantName', 'usKindCode'],
        ['Please enter a valid Patent Number', 'Please enter a valid Publication Date#', 'Please enter a valid Applicant Name', 'Please select a valid Kind Code']);
    return valid;
}
function validUSFormAuto() {

    var valid = false;
    valid = validationUtil.checkRequiredFields(['usPatentNo', 'usKindCode'],
        ['Please enter a valid Patent Number', 'Please select a valid Kind Code']);
    return valid;
}

function validNPLFormAuto() {

    var valid = false;
    valid = validationUtil.checkRequiredFields(['nplTitle', 'nplPublicationDate'],
        ['Please enter a valid Title', 'Please select a valid Publication Date']);
    return valid;
}
function validNPLUSUnpublish() {

    var valid = false;
    valid = validationUtil.checkRequiredFields(['nplTitle', 'nplApplicationSerialNumber'],
        ['Please enter a valid Title', 'Please select a valid Application Serial Number']);
    return valid;
}

//3
var auto = false;

$('#BtnSubmit').on('click', function () {
    var Currentdate = new Date();
    var mandatoryFeildValidation;
    console.log($('#referenceEntries').val() );
    if ($('#referenceEntries').val() === 'us') {
    	if(auto){
    		mandatoryFeildValidation=validUSFormAuto();
    	}
    	else{
    		mandatoryFeildValidation=validUSForm();
    	}
        if (mandatoryFeildValidation) {
            //fxSearchFamilies();

            var SelectDate = new Date($('#publicationDate').val());
	           if(!auto){
	        	   if (SelectDate <= Currentdate) {
	                if (regexValidation('usPatentNo')) {
	                    //alert('success');
	                    $('#referenceType').val('PUS');
	                    //$('#manualAdd').val((!auto));
	                    //console.log(regexValidation());
	                    $('#jurisdiction').val('US');
	                    // $("#refType").val("ReferenceType.US");
	                    $('#usReference').submit();
	                }
	                else {
	                    //$('#usPatentNo').parent('div').append('<span class="error">' + 'Patent number is not valid' + '</span>');
	                	errorAppend('#usPatentNo','Patent number is not valid');
	                	//$('#usPatentNo').parent('div').append('<span class="error">' + 'Patent number is not valid' + '</span>');
	                    /*<br/>Allowed String is '+allowedString*/
	                }
	            }
	            else {
	            	errorAppend('#publicationDate','Publication Date Shoul be less than current Date');
	                  // $('#publicationDate').parent('div').append('<span class="error">' + 'Publication Date Shoul be less than current Date' + '</span>');
	               }
            }
	        else{
	        	if (regexValidation('usPatentNo')) {
                    //alert('success');
                    $('#referenceType').val('PUS');
                    //$('#manualAdd').val((auto));
                    //console.log(regexValidation());
                    $('#jurisdiction').val('US');
                    // $("#refType").val("ReferenceType.US");
                    $('#usReference').submit();
                }
                else {
                	errorAppend('#usPatentNo','Patent number is not valid');
                    //$('#usPatentNo').parent('div').append('<span class="error">' + 'Patent number is not valid' + '</span>');
                    //$('#usPatentNo').parent('div').append('<span class="error">' + 'Patent number is not valid' + '</span>');
                    /*<br/>Allowed String is '+allowedString*/
                } 
	           }
        }
        //us submit button logic
    }
    else if ($('#referenceEntries').val() === 'foreign') {
        //forign submit button logic
    	if(auto){
    		mandatoryFeildValidation=validFpFormAuto();
    	}
    	else{
    		mandatoryFeildValidation=validFpForm();
    	}
        //

        if (mandatoryFeildValidation) {
            if ($('#fgnJurisdictioin').val() !== 'US' && $('#fgnJurisdictioin').val() !== 'us') {
                //fxSearchFamilies();
                var SelectDate = new Date($('#fgnPublicationDate').val());
	            if(!auto){
	            	if (SelectDate <= Currentdate) {
	            
	                    if (regexValidation('fgnPatentNo')) {
	                        $('#referenceType').val('FP');
	                        $('#jurisdiction').val($('#fgnJurisdictioin').val());
	                        //$('#manualAdd').val(!(auto));
	                        //alert('success');
	                        $('#usReference').submit();
	                    }
	                    else {
	                    	errorAppend('#fgnPatentNo','Patent number is not valid');
	                        //$('#fgnPatentNo').parent('div').append('<span class="error">' + 'Patent number is not valid' + '</span>');
	                        /*$('#fgnPatentNo').parent('div').append('<span class="error">' + 'Patent number is not valid<br/>Allowed String is '+allowedString + '</span>');*/
	                    }
	                }
	                else {
	                	errorAppend('#fgnPublicationDate','Publication Date should be less than current Date');
	                    //$('#fgnPublicationDate').parent('div').append('<span class="error">' + 'Publication Date Shoul be less than current Date' + '</span>');
	                }
	            }
	            else{
	            	   if (regexValidation('fgnPatentNo')) {
	                        $('#referenceType').val('FP');
	                        $('#jurisdiction').val($('#fgnJurisdictioin').val());
	                        //alert('success');
	                        //$('#manualAdd').val((auto));
	                        $('#usReference').submit();
	                    }
	                    else {
	                    	errorAppend('#fgnPatentNo','Patent number is not valid');
	                        //$('#fgnPatentNo').parent('div').append('<span class="error">' + 'Patent number is not valid' + '</span>');
	                        /*$('#fgnPatentNo').parent('div').append('<span class="error">' + 'Patent number is not valid<br/>Allowed String is '+allowedString + '</span>');*/
	                    }
	            }
            }
            else {

            	errorAppend('#fgnJurisdictioin','Juridiction is not valid');
                //$('#fgnJurisdictioin').parent('div').append('<span class="error">' + 'Juridiction is not valid' + '</span>');
            }
        }
    }
    else if($('#referenceEntries').val()==='npl') {
    	console.log('hji'+$('#referenceEntries').val());
    	if(validNPLFormAuto() && (!document.querySelector('#usApp').checked)){
    		 console.log('success');
    		  $('#referenceType').val('NPL');
            $('#usReference').submit();
    	}
    	else if(validNPLUSUnpublish() && (document.querySelector('#usApp').checked)){
    		console.log('success');
    		  $('#referenceType').val('NPL');
            $('#usReference').submit();
    	}
    	else{
    		console.log('error');
    	}
    	
    }
    else{
    	console.log('hji');
    }
});
function errorAppend(selectorError,message){ 
	var errorField = $(selectorError).parent('div').find('.error');
	if (errorField.length === 0) {
		$(selectorError).parent('div').append('<span class="error">' + message + '</span>');
	} else {
		$(errorField).html(message);
	}
}
//regex validation
var regex = [];
var allowedString = '';
function regexValidation(id) {
    regex.length = 0;
    var string = $('#' + id).val();
    var message = "Publication number is not in proper format";
    if ($('#jurisdictionType').val() == 'US') {
        if ($('#typeofNumber').val() == 'Publication') {
            //if ($('#fillingDate').val() == 'All') {
            //  if ($('#PublicationDate').val() == 'All') {
            var regex1 = /^([0-9]{2}[0-9]{2}[0-9]{7})$/;
            var regex2 = /^([0-9]{2}[0-9]{2}-[0-9]{7})$/;
            var regex3 = /^([0-9]{2}[0-9]{2}[/][0-9]{7})$/;
            regex.push(regex1, regex2, regex3);
            allowedString = ' ccyynnnnnnn,<br/> ccyy-nnnnnnn, <br/> ccyy/nnnnnnn';
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{7}$/;
            var regex2 = /^([0-9]{1}[,][0-9]{3}[,][0-9]{3})$/;


            regex.push(regex1, regex2);
            allowedString = ' n,nnn,nnn<br/> nnnnnnn';
        }
    }
    else if ($('#jurisdictionType').val() == 'WO') {
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{4}[/][0-9]{7}$/;
            var regex2 = /^([W][O][/]([0-9]{4})[/][0-9]{7})$/;
            var regex3 = /^([0-9]{2}[/][0-9]{5})$/;
            var regex4 = /^([W][O][/][0-9]{2}[/][0-9]{5})$/;

            regex.push(regex1, regex2, regex3, regex4);
            allowedString = ' ccyy/nnnnnnn<br/>WO/ccyy/nnnnnnnn <br/> yy/nnnnnnn<br/>WO/yy/nnnnnnn';
        }
    }
    else if ($('#jurisdictionType').val() == 'EP') {
        var regex1 = /^[0-9]{7}$/;
        regex.push(regex1);
        allowedString = ' nnnnnnn';
    }
    else if ($('#jurisdictionType').val() == 'AU') {
        var dateOne = new Date($('#fillingDate').val()); //Year, Month, Date
        var dateTwo = new Date(2002, 08, 08); //Year, Month, Date
        if ($('#typeofNumber').val() == 'Publication') {
            if (dateOne < dateTwo) {
                var regex1 = /^[0-9]{7}$/;
                allowedString = ' nnnnnyy';
            } else {
                var regex1 = /^[0-9]{10}$/;
                allowedString = ' ccyynnnnnn';
            }
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            if (dateOne < dateTwo) {
                var regex1 = /^[0-9]{6}$/;
                allowedString = ' nnnnnn';
            } else {
                var regex1 = /^[0-9]{10}$/;
                allowedString = ' ccyynnnnnn';
            }
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);

    }
    else if ($('#jurisdictionType').val() == 'GB') {
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{7}$/;

        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{7}$/;
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);
        allowedString = ' nnnnnnn';
    }
    else if ($('#jurisdictionType').val() == 'CA') {
        var dateOne = new Date($('#fillingDate').val()); //Year, Month, Date
        var dateTwo = new Date(1989, 10, 01); //Year, Month, Date
        if ($('#typeofNumber').val() == 'Publication') {
            if (dateOne < dateTwo) {
                var regex1 = /^[1][0-9]{6}$/;
                allowedString = ' 1nnnnnn';
            } else {
                var regex1 = /^[2][0-9]{6}$/;
                allowedString = ' 2nnnnnnn';
            }
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            if (dateOne < dateTwo) {
                var regex1 = /^[1][0-9]{6}$/;
                allowedString = ' 1nnnnnnn';
            } else {
                var regex1 = /^[2][0-9]{6}$/;
                allowedString = ' 2nnnnnnn';
            }
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);

    }
    else if ($('#jurisdictionType').val() == 'FR') {
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{7}$/;
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{7}$/;
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);
        allowedString = ' nnnnnnn';
    }
    else if ($('#jurisdictionType').val() == 'JP') {
        var dateOne = new Date($('#fillingDate').val()); //Year, Month, Date
        var dateTwo = new Date(2000, 01, 01); //Year, Month, Date
        if ($('#typeofNumber').val() == 'Publication') {
            if (dateOne < dateTwo) {
                var regex1 = /^([0-9]{4})([-])([0-9]{6})$/;
                var regex2 = /^([0-9]{2})([-])([0-9]{6})$/;
                regex.push(regex1, regex2);
                allowedString = ' ccyy-nnnnnn <br/>ev-nnnnnn where ev between 0-64';
            } else {
                var regex1 = /^([0-9]{4})([-])([0-9]{6})$/;
                regex.push(regex1);
                allowedString = ' ccyy-nnnnnn';
            }
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{7}$/;
            regex.push(regex1);
            allowedString = ' nnnnnnn';

        }
        else {
            $('#outputFeild').val('');
        }
    }
    else if ($('#jurisdictionType').val() == 'CN') {
        var dateOne = new Date($('#fillingDate').val()); //Year, Month, Date
        var dateTwo = new Date(2003, 10, 01); //Year, Month, Date
        if ($('#typeofNumber').val() == 'Publication') {
            if (dateOne < dateTwo) {
                var regex1 = /^[0-9]{7}$/;
                allowedString = ' nnnnnnn';
            } else {
                var regex1 = /^[0-9]{9}$/;
                allowedString = ' nnnnnnnnn';
            }
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            if (dateOne < dateTwo) {
                var regex1 = /^[0-9]{7}$/;
                allowedString = ' nnnnnnn';
            } else {
                var regex1 = /^[0-9]{9}$/;
                allowedString = ' nnnnnnnnn';
            }
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);
    }
    else if ($('#jurisdictionType').val() == 'DE') {
        var dateOne = new Date($('#fillingDate').val()); //Year, Month, Date
        var dateTwo = new Date(1995, 01, 01); //Year, Month, Date
        var dateThree = new Date(2003, 12, 31); //Year, Month, Date
        if (dateOne < dateTwo) {
            var regex1 = /^([P])([0-9]{2})([0-9]{5})([.][k])$/;
            var regex2 = /^([P])([0-9]{2})([0-9]{5})$/;
            allowedString = ' Pmmnnnnn.k<br/>Pmmnnnnn';
        }
        else if (dateOne > dateTwo && dateOne < dateThree) {
            var regex1 = /^([1]|[2]|[5]|[6])([0-9]{2})([0-9]{5})([.][k])$/;
            var regex2 = /^([1]|[2]|[5]|[6])([0-9]{2})([0-9]{5})$/;
            allowedString = ' zyynnnnn where z=1/2/5/6';
        }
        else if (dateOne > dateThree) {
            var regex1 = /^([1][0]|[1][1]|[2][0]|[2][1]|[5][0]|[6][0])([0-9]{10})([.][k])$/;
            var regex2 = /^([1][0]|[1][1]|[2][0]|[2][1]|[5][0]|[6][0])([0-9]{10})$/;
            allowedString = ' zzccyynnnnnn where zz=10/11/20/21/50/60';
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1, regex2);
    }
    else if ($('#jurisdictionType').val() == 'KR') {
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^(([1][0]|[2][0]|[3][0])([0-9]{7}))$/;
            allowedString = ' zzccyynnnnnnn where z=10/20/30';
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^(([1][0]|[2][0]|[3][0])[-]([0-9]{4})[-]([0-9]{7}))$/;
            allowedString = ' zz-xxyy-nnnnnnn where zz=10/11/20/21/50/60';
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);
    }
    else if ($('#jurisdictionType').val() == 'TW') {
        var dateOne = new Date($('#fillingDate').val()); //Year, Month, Date
        var dateTwo = new Date(2004, 07, 01); //Year, Month, Date
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{6}$/;
            var regex2 = /^[0-9]{9}$/;
            regex.push(regex1, regex2);
            allowedString = ' nnnnnn<br/> ccyynnnnn';
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            if (dateOne < dateTwo) {
                var regex1 = /^[0-9]{6}$/;
                regex.push(regex1);
            } else {
                var regex1 = /^[l][0-9]{6}$/;
                var regex2 = /^[M][0-9]{6}$/;
                var regex3 = /^[D][0-9]{6}$/;
                regex.push(regex1, regex2, regex3);
                allowedString = ' lnnnnnn<br/> Mnnnnnn<br/> Dnnnnnn';
            }
        }
        else {
            $('#outputFeild').val('');
        }
    }
    else if ($('#jurisdictionType').val() == 'RU') {
        var dateOne = new Date($('#fillingDate').val()); //Year, Month, Date
        var dateTwo = new Date(2000, 01, 01); //Year, Month, Date
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[2][0-9]{7}$/;
            allowedString = ' 2nnnnnnn';
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            if (dateOne < dateTwo) {
                var regex1 = /^[0-9]{8}$/;
                allowedString = ' yynnnnnn';
            } else {
                var regex1 = /^[0-9]{10}$/;
                allowedString = ' ccyynnnnnn';
            }
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);

    }
    else if ($('#jurisdictionType').val() == 'CH') {
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{6}$/;
            allowedString = ' nnnnnn';
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{6}$/;
            allowedString = ' nnnnnn';
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);
    }
    else if ($('#jurisdictionType').val() == 'BR') {
        var dateOne = new Date($('#fillingDate').val()); //Year, Month, Date
        var dateTwo = new Date(2012, 01, 01); //Year, Month, Date

        if (dateOne < dateTwo) {
            var regex1 = /^[P][l][0-9]{8}[.][k]$/;
            var regex2 = /^[M][U][0-9]{7}[.][k]$/;
            var regex3 = /^[P][l][0-9]{8}$/;
            var regex4 = /^[M][U][0-9]{7}$/;
            regex.push(regex1, regex2, regex3, regex4);
            allowedString = ' Plyynnnnnn.k<br/>Plyynnnnnn<br/>MUnnnnnnn.k<br/>MUnnnnnnn';
        } else {
            var regex1 = /^(([1][0]|[1][1]|[1][2]|[1][3]|[2][0]|[2][1]|[2][2])([0-9]{10})([.][k]))$/;
            var regex2 = /^(([1][0]|[1][1]|[1][2]|[1][3]|[2][0]|[2][1]|[2][2])([0-9]{10}))$/;
            regex.push(regex1, regex2);
            allowedString = ' zzccyynnnnnn.k<br/>zzccyynnnnnn where zz=10/11/12/13/20/21/22';
        }

    }

    else if ($('#jurisdictionType').val() == 'ES') {
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{7}$/;
            allowedString = ' nnnnnnn';
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{7}$/;
            allowedString = ' nnnnnnn';
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);

    }
    else if ($('#jurisdictionType').val() == 'NZ') {
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{6}$/;
            allowedString = ' nnnnnn';
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{6}$/;
            allowedString = ' nnnnnn';
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);
    }
    else if ($('#jurisdictionType').val() == 'SE') {
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{6}$/;
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{6}$/;
        }
        else {
            $('#outputFeild').val('');
        }
        regex.push(regex1);
        allowedString = ' nnnnnn';

    }
    else if ($('#jurisdictionType').val() == 'DK') {
        if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[0-9]{6}$/;
            regex.push(regex1);
            allowedString = ' nnnnnn';
        }
        else if ($('#typeofNumber').val() == 'Publication') {
            var regex1 = /^[P][a][0-9]{9}$/;
            var regex2 = /^[B][a][0-9]{9}$/;
            regex.push(regex1, regex2);
            allowedString = ' Paccyynnnnn<br/>Baccyynnnnn';

        }
        else {
            $('#outputFeild').val('');
        }
    }
    else {
        //console.log('no condition');
        return true;
        //$('#outputFeild').val(string);
    }
    if (checkregex(string, regex)) {
        return true;
    }
    else {
        return false;
    }

}
function checkregex(string, regex) {
    for (var i = 0; i < regex.length; i++) {
        if (regex[i].test(string)) {
            return true;
        }
    }
    return false;
}

//end regex validation
//pdf

$('#PdfFile').on('change', function (ev) {
   // console.log(ev.target.files[0].name);
    $('#filename').html(ev.target.files[0].name);
})

$('#PdfFileNPL').on('change', function (ev) {
	   // console.log(ev.target.files[0].name);
	    $('#filenameNPL<option value="npl">NPL</option><option value="npl">NPL</option><option value="npl">NPL</option>').html(ev.target.files[0].name);
	})
//switch manual auto

$('.switch-input').on('change', function () {
    auto = document.querySelector('.switch-input').checked;
})

function popupMsgForAddReference(current) {
    $("#popupMsgForAddReference").removeClass("hide");
    $("#popupMsgForAddReference").show();
    $("#popupMsgForAddReference").wrap("<div class='overlay'>");
    var $href = $(current).attr("data-href");
    $("#popupMsgForAddReference .btn-submit").attr("href", $href);
}

function hidepopupMsgForAddReference() {
    $("#popupMsgForAddReference").addClass("hide");
    $("#popupMsgForAddReference").unwrap("<div class='overlay'>");
}
$('.date > input[type=text]').on('change', function () {
    $('.datepicker.datepicker-dropdown').hide();
})
