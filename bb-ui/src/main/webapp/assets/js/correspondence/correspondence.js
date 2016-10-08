//table expand collapse js
$(function () {
 /*   $(".hidden-row").hide();
    $(".has-hidden-row span").on("click", function () {

        if ($(this).hasClass("icon-plus")) {
            $(this).parents(".has-hidden-row").addClass("active");
            $(this).removeClass("icon-plus").addClass("icon-minus");
            $(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
        } else if ($(this).hasClass("icon-minus")) {
            $(this).parents(".has-hidden-row").removeClass("active");
            $(this).removeClass("icon-minus").addClass("icon-plus");
            $(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
        }
    });


    $("#gotoSearch").on("click", function () {
        window.location.assign("search.html");
    });

    var searchHeight = $('.search-dropdown').height();

    $(".header .search-control .icon-search").on("click", function () {
        $('.search-dropdown').slideToggle("show");
    });

    $("#hideSearch").on("click", function () {
        $(this).parents(".search-input").removeClass("active");
        $(".search-dropdown").slideUp("hide");
    });

    //bootstrap date picker
    $('#datetimepicker1').datepicker();
    $('#datetimepicker2').datepicker();
    $('#datetimepicker3').datepicker();

    //file input browse
    document.getElementById("uploadBtn").onchange = function () {
        document.getElementById("uploadFile").value = this.value;
    };
    $("#removeFileUpload").on("click", function (e) {
        e.preventDefault();
        document.getElementById("uploadFile").value = "";
    });

    //filter js
    $(document).on('click', '.filter-dropdown', function (e) {
        e.stopPropagation();
    });
    $(".filter-dropdown .btn-cancel").on("click", function () {
        $("body").click();
    });

    //date range picker js
    $('.date-range-picker .icon-calendar').click(function () {
        $(document).ready(function () {
            $("#config-demo").daterangepicker({
                opens: 'left'
            }).focus();
        });
    });*/

    function removeModal() {
        $(this).on("click", function () {
            $("#myModal").modal('hide');
            $(".modal-backdrop").remove();
        });
    }


    $("#viewDoc").on("click", function () {
        var $dataHeight = $("#viewDocContainer table").height() + $("#viewDoc").height();

        if ($("#viewDocContainer").height() > 40) {
            $("#viewDocContainer").animate({
                "height": 38
            }, 1000);
            $(this).text("+ View Related Documents");
        } else {
            $("#viewDocContainer").animate({
                "height": $dataHeight
            }, 1000);
            $(this).text("- Hide Related Documents");
        }
    });

    //Alt key shortcut combination 
    function KeyCode(e) {
        if (e.which == 9) {
            $(".a").focus();
        }

        if (e.shiftKey) {
            if (e.preventDefault) { //prevent default action that belongs to the event.
                e.preventDefault();
            } else {
                e.returnValue = false;
            }
            $(".shortcut-pic").toggle(); //show/hide shortcuts
        }

        var keycode = e.which; //get Keycode
        if (e.shiftKey && keycode == 78) {
            $('.modal').modal('hide');
            $("#myModal2").modal();
        } else if (e.shiftKey && keycode == 66) {
            $('.modal').modal('hide');
            $("#myModal").modal();
        } else if (e.shiftKey && keycode == 80) {
            $('.modal').modal('hide');
            $("#myModal3").modal();
        } else if (e.shiftKey && keycode == 65) {
            $(location).attr('href', 'action-item.html');
        } else if (e.shiftKey && keycode == 82) {
            $(location).attr('href', 'sent-requests.html');
        }
    }
    document.onkeydown = KeyCode;
    
	
	$("#uploadZipFile").on('change', function(){
	//	$("#uploadBulkiFile").val("");
		var filename = $("#uploadZipFile").val().replace(/.*(\/|\\)/, '');
		$("#uploadBulkiFile").val(filename);
	})
	

	//bulk upload and progress bar
	$("#bulkUploadBtn").on("click",function(){
		console.log("jquery changed");
		console.log('here');
		if($('#uploadZipFile').val()!=""){
			//$("#uploadBulkiFile").val("") ;
			document.getElementById('FileUploadProgress').style.display='block';
			document.getElementById('progressoffileupload').style.width='0';
			var bar = $('.bar');
			var percent = $('.percent');
			var status = $('#status');
			var fileData = new FormData();
			var ext = $('#uploadZipFile').val().split('.').pop().toLowerCase();
		//	console.log("len: "+ $('#uploadZipFile').size);
			if(!($.inArray(ext, ['zip']) == -1)) {
		    	jQuery.each(jQuery('#uploadZipFile')[0].files, function(i, file) {
		    		console.log(file.name);
		    		image=file.name;
		    		fileData.append('file'+i, file);
		    	});
			    	var request = new XMLHttpRequest();
			    	request.upload.addEventListener('progress', function(e){
			    	//	console.log("total-"+e.total);
			    		/*//console.log(e.loaded);
			    		console.log((e.loaded/e.total) * 100 + '%') ;*/
			    		//console.log(e.total);
			    		bytes = e.total;
			    		 var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
			    		   if (bytes == 0) return '0 Byte';
			    		  var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
			    		  var totSize =  Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
			    		$("#totalSize").text(totSize);
			    		document.getElementById('progressoffileupload').innerText=Math.ceil((e.loaded/e.total) * 100) + '%'
			    		document.getElementById('progressoffileupload').style.width=(e.loaded/e.total) * 100 + '%';
			    		$("#percent").html(Math.round((e.loaded/e.total) * 100) + '%');
			    		//console.log(document.getElementById('progressoffileupload').style.width);
			    	}, false);
			    	request.upload.addEventListener('load', function(e){
			    			setTimeout(function(){document.getElementById('FileUploadProgress').style.display='none';},1000);
			    	}, false);
			   // 	$("#uploadZipFile").val("");
			    	console.log('yes');
			    	console.log(fileData);
			    	request.open('POST', '../correspondence/uploadFiles');
			    	request.send(fileData);
				}
		}
		else{
			console.log("please select a zip file");
		}
	});
});


function showLogoutPopup(){
	$("#logoutPopup").show();
	console.log('in co');
	if($('#FileUploadProgress').css('display') != 'none'){
		$('#uploadMsgWarning').text("Upload is going") ;
	}
	$("#logoutPopup").wrap("<div class='overlay'>");
}
