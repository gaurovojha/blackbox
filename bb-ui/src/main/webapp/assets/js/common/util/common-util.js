/** A contact object contains all the contacts specific to the client-side. */
(function( $ ) {

	$.blackbox.util.CommonUtil = function() {
	};

	$.blackbox.util.CommonUtil = {
			/**
			 * Provide internationalisation to JavaScript from '.properties' files. Language reported by browser is used by default.
			 * 
			 * @param propertyFiles
			 * 			'.properties' files containing locale specific key-value pairs.
			 * 			<p>Partial name (or names) of files representing resource bundles (eg, ‘Messages’ or ['Msg1','Msg2']).</p>
			 */
			loadResourceBundle : function(propertyFiles) {
				propertyFiles = decodeURIComponent(propertyFiles);
				var lang = navigator.language || navigator.userLanguage;
				var _resourceBundle = $('#contextPath').val() + '/i18n/loadProp/';

				jQuery.i18n.properties({
					name: propertyFiles, 
					path: _resourceBundle, 
					mode:'both', 
					language: lang,
				});
			},
	}
	
	$.blackbox.util.exportTableToCSV = function(tab, filename) {

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

            for (k = 0; k < tab.rows[j].cells.length-1; k++) {

                tab_text = tab_text + "<td>"
                    + tab.rows[j].cells[k].innerHTML
                    + "</td>";

            }
            tab_text = tab_text + "</tr>";

        }

        tab_text = tab_text + "</table>";
        tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
        tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
        tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi,""); // reomves input params
        
      	//Save file for IE 10+
        if (window.navigator.msSaveBlob) {
        	 csvData = decodeURIComponent(tab_text);
        	 var blob = new Blob([csvData],{ type: "application/csv;charset=utf-8;"});
        	 navigator.msSaveBlob(blob, filename);
         }
        //Other browsers
        else{
        	 csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text); // Data URI
        	 $(this).attr({
        		 "href": csvData,
        		 "target": "_blank",
        		 "download": filename
        	 });
         }

    };

}) ( jQuery );