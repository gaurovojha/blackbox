<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>



 <c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
 <c:set var="pathJs" value="${contextPath}/assets/js"
	scope="request" />

 <sec:authorize access="canAccessUrl('/ids/attorneyApproval/dashboard')">
 <div class="main-content container">
        <div class="clearfix content-links">
            <div class="pull-right">
			<div class="switch-control mdm">
				<label class="switch"> <input type="checkbox"
					id="switchRecordsView" class="switch-input" checked="checked">
					<span class="switch-label" data-on="Application View"
					data-off="Family View"></span> <span class="switch-handle"></span>
				</label>
			</div>
		</div>
        </div>
        <div class="clearfix">
            <ul class="tab-actions pull-right">
                <li>
                    <div class="daterange-picker tab">                                      
                        <input type="text" class="form-control date" name="datefilter" value="Showing Till Date" id="dateRangeCreateReq"> 
                        <span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
                    </div>
                </li>
                <li class="search-control">
                    <button type="submit" value="submit" class="search-btn">
                        <i class="icon-search" data-alt="search"><img src="${pathImg}/search.svg" class="icon20x"></i>
                    </button>
                    <div class="search-dropdown">
                        <h5>Search By</h5>
                        <ul class="nav nav-tabs custom-tabs" role="tablist">
                            <li role="presentation" class="active"><a href="#applicationNoTab" role="tab" data-toggle="tab">Application#</a></li>
                            <li role="presentation"><a href="#attorneyDocTab" role="tab" data-toggle="tab">Attorney Docket No.</a></li>
                            <li role="presentation"><a href="#mailingDateTab" role="tab" data-toggle="tab">Family Id</a></li>
                            <li role="presentation"><a href="#moreTab" role="tab" data-toggle="tab">More</a></li>
                        </ul>

                        <div class="tab-content">
                            <div role="tabpanel" class="tab-pane active" id="applicationNoTab">
                                <form class="form-horizontal">
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-6">
                                                <label class="control-label" >Application Number</label>
                                                <input type="text" class="form-control" id="txtApplicationNo">

                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Jurisdiction</label>
                                                <input type="text" class="form-control" id="txtJurisdiction">
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="attorneyDocTab">
                                <form class="form-horizontal">
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-6">
                                                <label class="control-label">Attorney Docket Number</label>
                                                <input type="text" class="form-control" id="txtAttorneyDocketNo">
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="mailingDateTab">
                                <form class="form-horizontal">
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-6">
                                                <label class="control-label">Family ID</label>
                                                <input type="text" class="form-control" id="txtFamilyId">
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="moreTab">
                                <div class="form-horizontal">
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-6">
                                                <label class="control-label">Last IDS Filing Date</label>
                                                <div class="input-group date datepicker single">
                                                    <input type="text" class="form-control" id="txtFilingDate">
                                                    <span class="input-group-addon">
                                                        <i class="glyphicon glyphicon-calendar"></i>
                                                    </span>
                                                </div>
                                            </div>
                                            <!-- <div class="col-sm-6">
                                                <label class="control-label">Jurisdiction</label>
                                                <input type="text" class="form-control" id="txtJurisdictionMore">
                                            </div> -->
                                        </div>
                                    </div>
                                    <div class="form-group">
                                       <!--  <div class="col-sm-12">
                                            <div class="col-sm-6">
                                                <label class="control-label">Application #</label>
                                                <input type="text" class="form-control" id="txtApplicationMore">
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Family ID</label>
                                                <input type="text" class="form-control" id="txtFamilyIdMore">
                                            </div>
                                        </div> -->
                                    </div>
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-6">
                                                <label class="control-label">Time Since Last Report File</label>
                                                <div class="input-group date datepicker single">
                                                    <input type="text" class="form-control"  id="txtDateLastReport">
                                                    <span class="input-group-addon">
                                                        <i class="glyphicon glyphicon-calendar"></i>
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Uncited References</label>
                                                <input type="text" class="form-control" id="txtUncited">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-6">
                                                <label class="control-label">Prosecution Status</label>
                                                <input type="text" class="form-control" id="prosecutionStatus">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="search-footer clearfix">
                            <div class="text-left">
                                <button class="btn btn-cancel" id="hideSearch">Cancel</button>
                                <button class="btn btn-submit" id="searchbtn">Search</button>
                            </div>
                        </div>
                    </div>
                </li>
                <li><a href="javascript:void(0)"><i><img src="${pathImg}/export.svg" class="icon20x"></i> Export</a></li>
            </ul>
        </div>
        
        <jsp:include page="attorney-approval-header.jsp"></jsp:include>
     </div>
  
      <jsp:include page="../scripts/ids-data-table.jsp" />
  
</sec:authorize>	
        
<script type="text/javascript" src="${pathJs}/ids/ids-common.js"></script>
<script type="text/javascript">

var tblAttorneyApprovalRecords = null;

$(document).ready(function() {
	
		clearTables();
		fetchAttorneyApprovalRecords();

});

function clearTables() {
	if (tblAttorneyApprovalRecords != null) {
		tblAttorneyApprovalRecords.destroy();
		tblAttorneyApprovalRecords = null;
	}
	
	$('#tbodyAttorneyApprovalRecords').html('');
}


function fetchAttorneyApprovalRecords() {
	
	$('#tblAttorneyApprovalRecords').show();
	 var filterData = recordFilters();
	 var colDef = {
			'col' : [],
			'defaultVal' : 'filingdate',
			'order' : [4, 'desc'],
		};
	tblAttorneyApprovalRecords = new $.blackbox.mdm.DataTable('#tblAttorneyApprovalRecords',
			'/ids/attorneyApproval/viewRecords', filterData, true,fnCallbackAttorneyApprovalRecords,colDef);
	
}

function recordFilters() {
	return {
		'iRecordFlag':$('#switchRecordsView').is(':checked'),
		'iDateRange' : $('#dateRangeCreateReq').val(),
		'iApplicationNumber' :$("#txtApplicationNo").val(),
		'iJurisdiction':$("#txtJurisdiction").val(),
		'iAttorneyDocketNumber':$("#txtAttorneyDocketNo").val(),
		'sFamilyId':$("#txtFamilyId").val(),
		'iFilterDate':$("#txtFilingDate").val(),
		'iFilterDateBy': $("#txtDateLastReport").val(),
		'iUncitatedReference': $("#txtUncited").val(),
	    'iprosecutionStatus' :$("#prosecutionStatus").val()
	}
}

function fnCallbackAttorneyApprovalRecords()
{
	showFamilyView(tblAttorneyApprovalRecords);
	bindFamilyClick(tblAttorneyApprovalRecords);
	showSpecificView();
}

$('#searchbtn').click(function() {
	
	clearTables();
	fetchAttorneyApprovalRecords();
	
});


$(document).on(
		'change',
		'#switchRecordsView',
		function() {
			if ($(this).is(':checked')) {
				disableExpandedView($('.mdmTable').find('.icon-minus'), tblAttorneyApprovalRecords);
				$('.duplicateFamily').closest('tr').show();
				showApplicationView(tblAttorneyApprovalRecords);
			} else {
				$('.duplicateFamily').closest('tr').hide();    
				showFamilyView(tblAttorneyApprovalRecords);
			}
		});

function showSpecificView() {
	if ($('#switchRecordsView').is(':checked')) {
		$('.duplicateFamily').closest('tr').show();
			showApplicationView(tblAttorneyApprovalRecords);
		
	} 
	else 
		{
		$('.duplicateFamily').closest('tr').hide();
		
				showFamilyView(tblAttorneyApprovalRecords);
		}
}

function showApplicationView(tableName) {
	var column1 = tableName.column(0);
	column1.visible(false);
}

function showFamilyView(tableName) {
	var column1 = tableName.column(0);
	column1.visible(true);
}

function disableExpandedView(element, tableName) {
	$(element).each(function(){
		if ($(this).hasClass("icon-minus")) {
			$(this).parents(".has-hidden-row").removeClass("active");
			$(this).removeClass("icon-minus").addClass("icon-plus");
			$(this).parents(".has-hidden-row").siblings(".hidden-row")
					.toggle();
		}
		var tr = $(this).closest('tr');
		var row = tableName.row(tr);

		if (row.child.isShown()) {
			// This row is already open - close it
			row.child.hide();
			tr.removeClass('shown');
		}
	})
}

			
 function bindFamilyClick(tableName) { 	
	
	// use delegated for the newly created rows
	 $('.expandFamily')
			.on(
					'click',
					function() {
					if ($(this).parents("tr").hasClass("even")) {
							$(this).parents("tr").addClass(
									"white");
			
						}
						else if($(this).parents("tr").hasClass("odd")){
							$(this).parents("tr").addClass(
							"blue");
						}
						if ($(this).hasClass("icon-plus")) {
							$(this).parents(".has-hidden-row").addClass(
									"active");
							$(this).removeClass("icon-plus").addClass(
									"icon-minus");
							$(this).parents(".has-hidden-row").siblings(
									".hidden-row").toggle();
						} else if ($(this).hasClass("icon-minus")) {
							$(this).parents(".has-hidden-row").removeClass(
									"active");
							$(this).removeClass("icon-minus").addClass(
									"icon-plus");
							$(this).parents(".has-hidden-row").siblings(
									".hidden-row").toggle();
						}

						var tr = $(this).closest('tr');
						var row = tableName.row(tr);

						if (row.child.isShown()) {
							// This row is already open - close it
							row.child.hide();
							tr.removeClass('shown');
						} else {
							// Open this row
								getFamilyMembers(this, tableName); 
						} 
					}); 
    }
       
function getFamilyMembers(element,tableName) {
	var famId = $(element).closest('tr').find('.familyId').text();
	var appId = $(element).closest('tr').find('.appId').attr('data');
	
	var notficationId = $(element).closest('tr').find('.notificationId').attr('data')
	var target =$('#contextPath').val() + "/ids/attorneyApproval/allFamilyDetails" ;
	var data = $.extend(recordFilters(),{"familyId" : famId},{"applicationId" : appId},{"notificationId": notficationId})
	$.ajax({
		type : "POST",
		url : target,
		data: data,
		success : function(response) {
			//var result = response.replace(/(\r\n|\n|\r)/gm, "");
			//result = result.trim("\t");
			//result= result.replace(/>\s+</g,'><'); 
			var result = response.replace(/>\s+</g,'><');
			tableName.row($(element).parent().parent()).child(
					$(result)).show();
		}
	});
}

</script>


 <div id="popupMsgReview" class="popup-msg">
	<div class="text-right"><a class="close" href="javascript:void(0)" onclick="hideReviewPopUp();">&times;</a></div>
	<div class="content">
		<p class="msg">Are you sure</p>
		<p class="msg">you don't want to file ?</p>
	</div>
	<div class="modal-footer">
		<input type="hidden" id="idsId">
		<button type="button" class="btn btn-submit" onclick="donotFileIDSAction();">Yes</button>
        <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hideReviewPopUp();">No</button>
    </div>
</div>   

<script>
function popupMsgForReview(id){
	
	$("#idsId").val(id);
	$("#popupMsgReview").removeClass("hide");
	$("#popupMsgReview").show();
	$("#popupMsgReview").wrap("<div class='overlay'>");
	$("#popupMsgReview").closest('a .close').bind('click', function() {
		hideReviewPopUp()
	});
}

function hideReviewPopUp(){
	$("#popupMsgReview").addClass("hide");
	$("#popupMsgReview").unwrap("<div class='overlay'>");
}

</script>

<script type="text/javascript">

function donotFileIDSAction(){
	hideReviewPopUp();
	var idsId=$("#idsId").val();
	var action;
	$.ajax({type: "POST",
        url: $('#contextPath').val() +"/ids/attorneyApproval/IDSActions",
        data: { idsId: idsId,
        		action:"donotFileIDS",
        		
         },
        success:function(result){
        	
        	// window.location.href= $('#contextPath').val() +'/ids/attorneyApproval/approveIDS ';
         }});	
	}
	
</script>

