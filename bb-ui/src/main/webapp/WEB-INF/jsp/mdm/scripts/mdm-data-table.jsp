<script>
	// Creating namespace for MDM
	$.blackbox.mdm = {};

	$.blackbox.mdm.DataTable = function(tblSelector, ajaxSource, filterData,
			htmlResponse, callbacks, colDef) {
		return $(tblSelector).DataTable({
			/* "dom": '<"bottom"l>', */
			"bProcessing" : true,
			"bServerSide" : true,
			"bFilter" : false,
			"bLengthChange" : true,
			"responsive" : true,
			"sort" : "position",
			"bStateSave" : false,
			"iDisplayLength": 20,
			"iDisplayStart" : 0,
			"aLengthMenu": [[10,20,30, 40], [10,20,30, 40]],
			"columnDefs" : colDef.col,
			"order" : colDef.order,
			//'sColumns' : ["Jurisdiction","Application","AttorneyDocket","FilingDate","Assignee","ApplicationType","Createdby"],

			"ajax" : {
				"url" : $('#contextPath').val() + ajaxSource,
				"type" : "POST",
				"data" : function(oSettings) {
					var pageInfo = {
						"iStart" : oSettings.start,
						"iLength" : oSettings.length,
						"iSortBy" : oSettings.columns[oSettings.order[0].column].name,
						'iSortOrder' : oSettings.order[0].dir,
						'iSearch' : oSettings.search.value,
					}
					var data = $.extend(pageInfo, filterData);
					return data;
				},
				// MDM supplied attributes.
				"htmlResonse" : htmlResponse,
				"toJson" : function(html) {
					var json = this.convertToJson(html);
					return json;
				},

				"convertToJson" : function(html) {
					var result = html.split('<p id="splitter"/>');
					$('#pageInfo').html(result[0].trim());
					var records = result[1].trim();
					var output = [];

					records = records.replace(/<tr>/g, "").split("</tr>");
					$.each(records, function(ind, val) {
						var temp = val.replace(/<td>/g, "").split("</td>");
						output.push(temp);
					});

					output.splice(-1, 1);
					var json = {
						"recordsTotal" : $('#recordsTotal').val(),
						"recordsFiltered" : $('#recordsFiltered').val(),
						"data" : output,
					}
					return json;
				},

			},
			//"aoColumnDefs" : colDef.disabledCols,

			"fnDrawCallback" : function() {
				if (typeof callbacks !== "undefined") {
					callbacks();
				}
			}
		})
	}

	/* function getSortBy(oSettings, coldef) {
		if (oSettings.columns[oSettings.order[0].column].name == "") {
			return coldef.defaultVal;
		} else {
			return oSettings.columns[oSettings.order[0].column].name;
		}

	} */
</script>