//Plug-in to fetch page data 
jQuery.fn.dataTableExt.oApi.fnPagingInfo = function(oSettings) {
    return {
        "iStart": oSettings._iDisplayStart,
        "iEnd": oSettings.fnDisplayEnd(),
        "iLength": oSettings._iDisplayLength,
        "iTotal": oSettings.fnRecordsTotal(),
        "iFilteredTotal": oSettings.fnRecordsDisplay(),
        "iPage": oSettings._iDisplayLength === -1 ? 0 : Math
            .ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
        "iTotalPages": oSettings._iDisplayLength === -1 ? 0 : Math
            .ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength)
    };
};

$(document)
    .ready(
        function() {

            $("#notificationTable")
                .dataTable({

                    "bProcessing": true,
                    "bFilter": true,
                    "bServerSide": false,
                    "sort": "position",
                   // "dom": 'rti<"top"flp><"clear">',
                    "bStateSave": false,
                    "order": [
                        [7, "asc"]
                    ],
                    // Default: Page display length
                    "aLengthMenu": [
                        [6, 10, 20, -1],
                        [6, 10, 20, "All"]
                    ],
                    "iDisplayLength": 6,
                    "iDisplayStart": 0,

                    "initComplete": function() {

                        // var table =
                        // $('#notificationTable').dataTable();
                        // table.find('tbody
                        // tr:first').css('background-color','#BDC2D4');

                    },

                    "fnDrawCallback": function() {
    
                    },

                    "columnDefs": [{
                        "render": function(data,
                            type, row) {
                        	var html = "";
                        	if($("#setupNotificationAccess").val() === 'true'){
                        		 return "<a href='javascript:void(0)' onclick='callSetUp(this)'><p>" + data + "</p></a>";
                        		 
                        	}else{
                        		
                        		return data;
                        	}
                           
                        },
                        "targets": 0

                    }, {
                        "render": function(data,
                            type, row) {
                        	
                        	if($("#editNotificationRulesAccess").val() === 'true'){
                        		
                        		 return "<a href='javascript:void(0)' onclick='callBussinessRule(this)'><p>" + data + "</p></a>";
                        	}else{
                        		
                        		return data;
                        	}
                           
                        },
                        "targets": 6

                    }, {
                       
                        "render": function(data,
                            type, row) {
                        	
                        	var cellHtml ="<div id=" + data + " class='action-btns-grid'>";
							
							if($("#setupNotificationAccess").val() === 'true'){
								cellHtml+= "<a href='javascript:void(0)' onclick='editSetUp(\"" + data + "\")'><span class='icon icon-edit'></span>" + ' Edit Setup' + "</a>";
								
							}
							if($("#editNotificationRulesAccess").val() === 'true'){
								cellHtml+="<a href='javascript:void(0)' onclick='editBussinessRule(\"" + data + "\")'><span class='icon icon-edit2'></span>" + ' Edit Business Rule' + "</a>";
							}
							
							html=+"</div>";
							
                        	return cellHtml;
                        },
                        "targets": 7
                    }],
                    "sAjaxSource": "../notification/workflowManagementAjaxCall",
                    "aoColumns": [{
                            "mData": "notificationName"
                        }, {
                            "mData": "notificationType"
                        }, {
                            "mData": "displayOnIDSReview"
                        }, {
                            "mData": "emailNotification"
                        }, {
                            "mData": "reminder"
                        }, {
                            "mData": "escalation"
                        }, {
                            "mData": "noOfBusinessRules"
                        }, {
                            "mData": "notificationId"
                        }

                    ]
                });

            var oTable = $('#notificationTable').DataTable();
            $('.dataTables_filter').hide();

            $('#notificationTableSearch').keyup(
                function() {

                    var selectedValueInDropDown = $(
                        'select.tablesearchOption').find(
                        ':selected')[0].id;

                    if (selectedValueInDropDown === 'all') {
                        oTable.search(this.value).draw();
                    } else {
                        oTable.column(
                                Number(selectedValueInDropDown))
                            .search(this.value).draw();
                    }

                });

            $('[role="row"]').on('click', function() {

                $('[role="row"]').removeAttr('style');

            });

            function exportTableToCSV(tab, filename) {

                var tab_text = "<table border='2px'>";
                var textRange;
                var j = 0;
                tab = document.getElementById(tab);

                for (j = 0; j < tab.rows.length; j++) {

                    // only formatting of upper row
                    if (j == 0) {
                        tab_text = tab_text + "<tr bgcolor='#87AFC6'>";
                    } else {
                        tab_text = tab_text + "<tr>";
                    }

                    for (k = 0; k < tab.rows[j].cells.length-1; k++) {

                        tab_text = tab_text + "<td>" + tab.rows[j].cells[k].innerHTML + "</td>";

                    }
                    tab_text = tab_text + "</tr>";

                }

                tab_text = tab_text + "</table>";
                tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, ""); // remove
                tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove
                tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi,
                    ""); 
                // Data URI
                //csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text);

	            //Save file for IE 10+
	           	if (window.navigator.msSaveOrOpenBlob) {
	           	     csvData = decodeURIComponent(tab_text);
	
	           	     if(window.navigator.msSaveBlob){
	           	         var blob = new Blob([csvData],{ type: "application/csv;charset=utf-8;"});
	           	         navigator.msSaveBlob(blob, filename);
	           	     }
	           	 }
	           	//Other browsers
	           	 else
	           	 {
	           		 csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text);
	           	     $(this).attr({
	           	         "href": csvData,
	           	         "target": "_blank",
	           	         "download": filename
	           	     });
	           	 }
            }

            // This must be a hyperlink
            $(".export").on(
                'click',
                function(event) {
                    // CSV
                    exportTableToCSV.apply(this, [
                        'notificationTable',
                        'Notification.xls'
                    ]);

                });

        });

function callSetUp(value) {
    var id = $(value).parents('[role="row"]').find('.action-btns-grid')[0].id;
    editSetUp(id);
}

function callBussinessRule(value) {
    var id = $(value).parents('[role="row"]').find('.action-btns-grid')[0].id;
    editBussinessRule(id);
}

function editSetUp(id) {
    window.location.href = '../notification/setUpNotification?notificationId=' + id;
}

function editBussinessRule(id) {
    window.location.href = '../notification/editBussinessRules?notificationId=' + id;
}