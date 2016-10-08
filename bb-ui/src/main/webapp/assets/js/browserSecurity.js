
function browserSec(){
	
	//to check back/forward button
	if (window.history && window.history.pushState) {

		$(window).on('popstate', function() {
			sessLogOut();
		});

		window.history.pushState('forward', null, "");
	}
	// to check F5 button
	$(document).on("keydown", this, function(event) {
		if (event.keyCode == 116) {
			console.log('F5 pressed!');
			window.location.href = logoutUrl;
			sessLogOut();
			return false;
			//event.preventDefault();
		}

		//To check  Ctrl + R to stop refresh page
		if (event.keyCode == 82 && event.ctrlKey) {
			console.log('ctrl R');
			sessLogOut();
			return false;
			//event.preventDefault();
		}
	});
	

}
