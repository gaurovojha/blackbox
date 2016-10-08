
$(function () {

		$("#nplData input[type='text']").focusout(function () {
            var populatedData = '';
            $("#nplData input[type='text']").each(function () {
                if ($(this).val() !== 'undefined') {
                    populatedData += $(this).val() + " ";
                    $("#autoPupulatedData").text(populatedData);
                }
            });
        });

        $("#referenceEntries").on('change', function () {
            if (this.value == 'NPL') {
                $("#nplSelect").show();
            } else {
                $("#nplSelect").hide();
            }
        });

        $("#nplSelect").on('click', function () {
            if ($("#unpublishedApp").is(':checked') === true) {
                $(".USCheck").hide();
            } else {
                $(".USCheck").show();
            }
        });

        $("#searchNplBtn").on('click', function () {
            $("#RetrievalDate").show();
            $("#nplTitle").val('nplTitle1');
            if ($("#unpublishedApp").is(':checked') === false) {
                $("#nplPublicationDate").val('Dec 21, 2015');
                $("#nplAuthor").val('nplAuthor');
                $("#nplPublicationDetails").val('nplPublicationDetails');
                $("#nplRelevantPages").val('5');;
                $("#nplIssueNo").val('10');
                $("#nplURL").val('http://www.google.com');
                $("#nplRetrievalDate").val('Dec 20, 2015');
            };
            $("#nplData input[type='text']").trigger('focusout');
        });

        $("#nplURL").focusout(function () {
            //need to be verify for various urls
            var urlRegExp = new RegExp('^(https?:\\/\\/)?' + // protocol
                '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
                '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
                '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
                '(\\?[,;&a-z\\d%_.~+=-]*)?' + // query string
                '(\\#[-a-z\\d_]*)?$'); // hash

            if (urlRegExp.test($("#nplURL").val())) {
                $("#RetrievalDateError").hide();
                $("#RetrievalDate").show();
            } else {
                $("#RetrievalDate").hide();
                $("#RetrievalDateError").show();
            }
        });

        $(".input-group-addon").on('click', function () {
            $(this).siblings('input').focus();
        });
    //NPL JS functionality Ended here
    
    $("#usData, #nplData, #foreignData").hide();
    $("#referenceEntries").on("change", function () {
        if ($(this).val() == "US") {
            $("#usData").show();
            $("#nplData, #foreignData").hide();
        } else if ($(this).val() == "NPL") {
            $("#nplData").show();
            $("#usData, #foreignData").hide();
        } else if ($(this).val() == "FP") {
            $("#foreignData").show();
            $("#usData, #nplData").hide();
        }
    });
    
});