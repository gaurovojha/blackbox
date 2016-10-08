<script>
	function exportTableToCSV(tab, filename) {

		var tab_text = "<table border='2px'>";
		var textRange;
		var j = 0;
		tab = document.getElementById(tab); // id of table

		for (j = 0; j < tab.rows.length; j++) {

			//only formatting of upper row
			if (j == 0) {
				tab_text = tab_text + "<tr bgcolor='#87AFC6'>";
			} else {
				tab_text = tab_text + "<tr>";
			}

			for (k = 0; k < tab.rows[j].cells.length - 1; k++) {

				tab_text = tab_text + "<td>" + tab.rows[j].cells[k].innerHTML
						+ "</td>";

			}
			tab_text = tab_text + "</tr>";

		}

		tab_text = tab_text + "</table>";
		tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
		tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
		tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

		// Data URI
		//csvData = ('data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text));

		//Save file for IE 10+
		if(window.navigator.msSaveOrOpenBlob) {
			 csvData = decodeURIComponent(tab_text);

			 if(window.navigator.msSaveBlob){
				 var blob = new Blob([csvData],{ type: "application/csv;charset=utf-8;"});
				 navigator.msSaveBlob(blob, filename);
			 }
		 }
		//Other browsers
		else{
			 csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text);
			 $(this).attr({
				 "href": csvData,
				 "target": "_blank",
				 "download": filename
			 });
		}
	}
</script>
