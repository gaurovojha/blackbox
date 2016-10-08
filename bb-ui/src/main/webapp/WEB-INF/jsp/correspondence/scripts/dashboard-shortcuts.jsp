<script>
 $(function () {
 //Alt key shortcut combination 
 var k=0;
 	function KeyUPEvent(e){
 		if (e.ctrlKey || e.altKey) {
 		k=0;
 		$(".shortcut-pic").hide();
 		}
 	}
    function KeyCode(e) {
    	if(k===0){
	        if (e.which == 9) {
	            $(".a").focus();
	        }
	       
	        if (e.ctrlKey && e.altKey) {
	        	$(".shortcut-pic").show(); //show/hide shortcuts
	        	k=1;
	            if (e.preventDefault) { //prevent default action that belongs to the event.
	                e.preventDefault();
	            } else {
	                e.returnValue = false;
	            }
	            
	        }
    	}
	
	        var keycode = e.which; //get Keycode
	        if (e.ctrlKey && e.altKey && keycode == 78) {
	            $('#newDocument').trigger('click');
	        } else if (e.ctrlKey && e.altKey && keycode == 66) {
	             $('#bulkUpload').trigger('click');
	        } else if (e.ctrlKey && e.altKey && keycode == 80) {
	             $('#pairAudit').trigger('click');
	        } else if (e.ctrlKey && e.altKey && keycode == 65) {
	             $('#actionItems')[0].click()
	        } else if (e.ctrlKey && e.altKey && keycode == 84) {
	             $('#trackApplication')[0].click()
	        }
	        else if (e.ctrlKey && e.altKey && keycode == 68) {
	            $('#dashboard')[0].click()
	       }
			else if (e.ctrlKey && e.altKey && keycode == 88) {
	             $('#updateRequest')[0].click()
	        }
			else if (e.ctrlKey && e.altKey && keycode == 89) {
	             $('#uploadRequest')[0].click()
	        }
    	}
    
    document.onkeydown = KeyCode;
    document.onkeyup=KeyUPEvent;

});
 </script>
