<script type="text/javascript">
	function htmlEntities(str) {
		var SCRIPT_REGEX = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;
		while (SCRIPT_REGEX.test(str)) {
			str = str.replace(SCRIPT_REGEX, "");
		}
		return str;
	}

	$(document).ready(
			function() {
				//$("#tabs").tabs({active: document.tabTest.currentTab.value});
				var inputElems = $(":input[type='text']");
				//var inputElems = document.getElementsByTagName("input");
				for (i = 0; i < inputElems.length; i++) {
					inputElems[i].onchange = function() {
						var text = this.value;
						this.value = htmlEntities(text);
					}
				}

				var tabSelected = $('#selectedTab').val();
				if(tabSelected != undefined && tabSelected != '') {
				$(
						$('.main-nav li a').filter(
								function(index) {
									return $(this).text().toUpperCase()
											.indexOf(tabSelected.toUpperCase()) >= 0;
								})[0]).parent().addClass('active');
			} });
</script>