<script>
	// Creating namespace for correspondence
	$.blackbox.correspondence = {};
	$.blackbox.correspondence.DataTable = function(tblSelector, ajaxSource,
			filterData, htmlResponse, callbacks, colDef) {
		return $(tblSelector).DataTable({
			"bProcessing" : true,
			"bServerSide" : true,
			"bFilter" : false,
			"bLengthChange" : true,
			"responsive" : true,
			"sort" : "position",
			"bStateSave" : false,
			"iDisplayLength": 20,
			"iDisplayStart" : 0,
			"aLengthMenu": [[20, 10, 30, 40], [20, 10, 30, 40]],
			"columnDefs" : colDef.col,
			"order" : colDef.order,
			"ajax" : {
				"url" : $('#contextPath').val() + ajaxSource,
				"type" : "POST",
				"data" : function(oSettings) {
					var pageInfo = {
						"iStart" : oSettings.start,
						"iLength" : oSettings.length,
						"iSortBy" : getSortBy(oSettings, colDef),
						'iSortOrder' : oSettings.order[0].dir,
						'iSearch' : oSettings.search.value,
					}
					var data = $.extend(pageInfo, filterData);
					return data;
				},
				// correspondence supplied attributes.
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
			"aoColumnDefs" : colDef.disabledCols,

			"fnDrawCallback" : function() {
				if (typeof callbacks !== "undefined") {
					callbacks();
				}
			}
		})
	}

	function getSortBy(oSettings, coldef) {
		if (oSettings.columns[oSettings.order[0].column].name == "") {
			return coldef.defaultVal;
		} else {
			return oSettings.columns[oSettings.order[0].column].name;
		}
	}
</script>