<script>



/////////////////////////////////////////////////////////////////////////////////////
// Bulk upload


(function() {
	$('#myModal').on('hidden.bs.modal', function () {
		$("#uploadBulkiFile").val("");
		$("#bulkFileUploadErrorMsg").html("");
		
	})
})();


(function() {
	try {
		$.blackbox.util.CommonUtil.loadResourceBundle(encodeURIComponent('i18n/correspondence'));
	} catch(error) { console.log(error); }
})();

//bulk upload and progress bar

	$("#bulkUploadBtn").on("click",function(){
		uploadGoing = true;
		console.log("jquery");
		var fileName = $("#uploadZipFile").val().replace(/.*(\/|\\)/, '');
		$("#uploadBulkiFile").val(fileName);
		if($('#uploadZipFile').val()!="" && validateBulkUploadFileExtension(fileName)==true){
			$("#uploadBulkiFile").val(fileName) ;
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
		    		fileData.append('file0', file);
		    	});
			    	var request = new XMLHttpRequest();
			    	  	request.upload.addEventListener('progress', function(e){
			    	  		$('#myModal').modal('hide');
			    	  	//	console.log(e.total);
			    		//console.log(e.loaded);
			    		//console.log((e.loaded/e.total) * 100 + '%') ;*/
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
			    			uploadGoing = false ;	
			    	}, false);
			    	$("#uploadZipFile").val("");
			    	request.open('POST', 'uploadFiles');
			    	request.send(fileData);
			    	
			    	
				}
		}
		else{
			
		
			$('#myModal').modal('show');
			$("#bulkFileUploadErrorMsg").html(jQuery.i18n.prop("message.correspondence.dashboard.bulk.invalidFile"));	
		}
		
	});
	

	$("#uploadZipFile").on('change', function(){
		$("#uploadBulkiFile").val("");
		var filename = $("#uploadZipFile").val().replace(/.*(\/|\\)/, '');
		$("#uploadBulkiFile").val(filename);
	})
	
	function validateBulkUploadFileExtension(fileName) {
		var validFile = false;
		if (fileName.length > 0) {
			var validExtension = ".zip";
			if (fileName.substr(fileName.length - validExtension.length,
					validExtension.length).toLowerCase() == validExtension
					.toLowerCase()) {
				validFile = true;
			}
		}
		return validFile;
	}
	
</script>