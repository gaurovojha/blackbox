<script>
	// Creating namespace for Reference
	//$.blackbox = {};
	$.blackbox.reference = {};
	
	$.blackbox.reference.DataTable = function(tblSelector, ajaxSource, filterData, htmlResponse, callbacks) {
		return $(tblSelector).DataTable({
			"aoColumnDefs": [{
				"bSortable": false,
				"aTargets": ["noSort"]
			}],
			"bProcessing" : true,
			"bServerSide" : true,
			"bFilter": false,
			"bLengthChange" : true,
			"responsive" : true,
			"sort" : "position",
			"bStateSave" : false,
			//"iDisplayLength": 20,
			"iDisplayStart" : 0,
			//"aLengthMenu": [[10, 20, 30, 40, -1], [10, 20, 30, 40, "All"]],
			"ajax" : {
				"url" : $('#contextPath').val() + ajaxSource,
				"type" : "POST",
				"data" : function(oSettings) {
					var pageInfo = {
						"iStart" : oSettings.start,
						"iLength" : oSettings.length,
						"iSortBy" : oSettings.order[0].column,
						'iSortOrder' : oSettings.order[0].dir,
						'iSearch' : oSettings.search.value,
					}
					var data = $.extend(pageInfo, filterData);
					return data;
				},
				// Reference supplied attributes.
				"htmlResonse" : htmlResponse,
				"toJson" : function(html) {
					var json = this.convertToJson(html);
					return json;
				},
				
				"convertToJson" : function ( html ) {
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
							"recordsTotal": $('#recordsTotal').val(),
						    "recordsFiltered": $('#recordsFiltered').val(),
						    "data" : output,
					}
					return json;
				},
				
			},
			
			"fnDrawCallback" : function() {
				if (typeof callbacks !== "undefined") {
					callbacks();
				}
			}
		})
	}

</script>