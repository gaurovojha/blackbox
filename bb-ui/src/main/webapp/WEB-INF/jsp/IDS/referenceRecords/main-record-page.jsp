<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<jsp:include page="../../reference/scripts/reference-data-table.jsp"></jsp:include> 

<div class="main-content container">

	<div class="notTopinfo">
		<div class="row">
			<div class="col-sm-2">
				<label class="control-label">Family ID </label>
				<div class="form-control-static">
					<a href="#" data-target="#family" data-toggle="modal" id="familyMemId">${familyId}</a>
					<input type="hidden" id="familyId" name="familyId" value="${familyId}"/>
				</div>
			</div>
			<div class="col-sm-2">
				<label class="control-label">Jurisdiction</label>
				<div class="form-control-static">${jurisCode}</div>
				<input type="hidden" name="jurisCode" value="${jurisCode}"/>
			</div>
			<div class="col-sm-2">
				<label class="control-label">Application #</label>
				<div class="form-control-static">${appNo}</div>
				<input type="hidden" name="appNo" value="${appNo}"/>
			</div>
			<div class="col-sm-2">
				<label class="control-label">Attorney Docket # </label>
				<div class="form-control-static">${attorneyDocket}</div>
			</div>

			<div class="col-sm-2">
				<label class="control-label">&nbsp;</label>
				<div class="form-control-static text-right">
					<a href="javascript:void(0)" class="margin-rt-10">Review Ref.Flow Rules</a>
				</div>
			</div>
			<div class="col-sm-2">
				<label class="control-label">&nbsp;</label>
				<div class="form-control-static"><a href="javascript:void(0)" class="pull-right" id="download"><img src="<%=images %>/svg/download.svg">Download</a>
				</div>
			</div>
		</div>
	</div>
	
	<div>
		<!-- Nav tabs -->
		<div class="pull-right form-control-static">
        	<a href="javascript:void(0)" data-target="#searchReference" data-toggle="modal"><i><img src="<%=images%>/svg/search.svg" title="Search Reference" class="icon20x"></i></a>
        </div>
	    <ul class="nav nav-tabs custom-tabs" id="idsTabs" role="tablist">
	        <li role="presentation" class="active"><a href="#citedIDS" role="tab" data-toggle="tab">Cited in IDS <span>(${statusCount.citeCount})</span></a></li>
	        <li role="presentation"><a href="#unCited" role="tab" data-toggle="tab">Uncited <span>(${statusCount.uncitedCount})</span></a></li>
	        <li role="presentation"><a href="#examinerCited" role="tab" data-toggle="tab">Examiner Cited <span>(${statusCount.examinerCitedCount})</span></a></li>
	
	        <li role="presentation"><a href="#citedParent" role="tab" data-toggle="tab">Cited in Parent <span>(${statusCount.citedInParentCount})</span></a></li>
	        <li role="presentation"><a href="#doNotFile" role="tab" data-toggle="tab">Do Not File <span>(${statusCount.doNotFileCount})</span></a></li>
	        <li role="presentation"><a href="#dropped" role="tab" data-toggle="tab">Deleted <span>(${statusCount.deletedCount})</span></a></li>
    	</ul>
    	
    	<!-- Tab panes -->
        <div class="tab-content reference-cited">
			<div role="tabpanel" class="tab-pane active" id="citedIDS">
        		<jsp:include page="cited/cited-header.jsp"></jsp:include>
        	</div>
        	<div role="tabpanel" class="tab-pane" id="unCited">
        		<jsp:include page="uncited/uncited-header.jsp"></jsp:include>
        	</div>
        	<div role="tabpanel" class="tab-pane" id="examinerCited">
        		<jsp:include page="examinercited/examiner-cited-header.jsp"></jsp:include>
        	</div>
        	<div role="tabpanel" class="tab-pane" id="citedParent">
        		<jsp:include page="citedparent/cited-parent-header.jsp"></jsp:include>
        	</div>
        	<div role="tabpanel" class="tab-pane" id="doNotFile">
        		<jsp:include page="donotfile/uncited-header.jsp"></jsp:include>
        	</div>
        	<div role="tabpanel" class="tab-pane" id="dropped">
        		<jsp:include page="dropped/uncited-header.jsp"></jsp:include>
        	</div>
        </div>
    	
	</div>
	
	  <!-- List of users modal -->
    <div class="modal custom fade modal-wide" id="family" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">${familyId}</h4>
          </div>
          <div class="modal-body">
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-sm-12">
                        <table class="table custom-table" id = "familyMembersTable">
                            <thead>
                                <tr>
                                    <th>Jurisdiction</th>
                                    <th>Application #</th>
                                    <th>Attorney Docket #</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody id= "familyMembersDetails">

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- duplicate source modal -->
    <div class="modal custom fade" id="searchReference" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Search Reference</h4>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <div class="col-sm-5">
                                <select class="form-control">
                                    <option>Please select reference type</option>
                                    <option>US</option>
                                    <option>Foreign</option>
                                    <option>NPL</option>
                                </select>
                            </div>
                        </div>
                        <div class="inner-search trisearch">
                            <div class="input-group">
                                <span class="input-group-btn">
                                    <button class="search"><span class="icon icon-search-inner"></span>
                                    </button>
                                </span>
                                <input type="text" placeholder="Search" value="5762890">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                                  <a href="ids-reference-record-search.html" target="_blank">5762890</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script>
$(function(){
	var tablePatentData = null, tableNplData = null;
	var getActiveTab = function(){
		var tab = $("#idsTabs li.active").find("a").text();
		if(tab.indexOf('Cited in IDS') == 0){
			return "cited";
		}else if(tab.indexOf('Uncited') == 0){
			return "uncited";
		}else if(tab.indexOf('Examiner Cited') == 0){
			return "examinerCited";
		}else if(tab.indexOf('Cited in Parent') == 0){
			return "citedInParent";
		}else if(tab.indexOf('Do Not File') == 0){
			return "doNotFile";
		}else if(tab.indexOf('Deleted') == 0){
			return "deleted";	
		}
	}
	
	var getPatentParams = function(){
		return{
/* 			familyid: $('input[name="familyId"]').val(),
			applicationNumber: $('input[name="appNo"]').val(),
			iJurisdiction:  $('input[name="jurisCode"]').val(),
			iFilterDate : $("#iFilterDate").val(),
			referenceType:"patents",
			activeTab: getActiveTab(),
			status:"" */
			familyid: $('input[name="familyId"]').val(),
			applicationNumber: "",
			iJurisdiction:  "",
			iFilterDate : "",
			referenceType:"NPL",
			activeTab: getActiveTab(),
			status:""
		}
	};
	
	var getNplParams = function(){
		return{
/* 			familyid: $('input[name="familyId"]').val(),
			applicationNumber: $('input[name="appNo"]').val(),
			iJurisdiction:  $('input[name="jurisCode"]').val(),
			iFilterDate : $("#iFilterDate").val(),
			referenceType:"NPL",
			activeTab: getActiveTab(),
			status:"" */
			
				familyid: $('input[name="familyId"]').val(),
				applicationNumber: "",
				iJurisdiction:  "",
				iFilterDate : "",
				referenceType:"NPL",
				activeTab: getActiveTab(),
				status:""
		}
	};
	
	function clearTables() {
		if (tablePatentData != null) {
			tablePatentData.destroy();
			tablePatentData = null;
		}
		
		if (tableNplData != null) {
			tableNplData.destroy();
			tableNplData = null;
		}
		
		var tabname = getActiveTab();
				
		$('#'+tabname+'PatentTable tbody').html("");
		$('#'+tabname+'NplTable tbody').html("");
	}
	
	//Pagination for familyMembers data
	var familyMembersTable = $('#familyMembersTable').dataTable({
		"responsive" : 	true,
		"ordering": 	false,
        "info":     	false,
        "bFilter": 		false,
        "paging":		true,
		"aLengthMenu": [[2, 20, 30, 40, -1], [2, 20, 30, 40, "All"]],
       "iDisplayLength":2
	});
	
	function citedHeader() {
		url = $('#contextPath').val() + '/idsReference/viewReferenceRecords/citedHeader';
		$.ajax({
			type: "POST",
			url: url,
			success: function(result) {
				 $('#citedHeader').html(result);
			}	
		});
	}
	
	function getSourceDocument(refFlowId) {
		var url = $('#contextPath').val() + '/idsReference/viewReferenceRecords/citedSourceDoc';
		var data = {
				"refFlowId": refFlowId
		};
		
		$.ajax({
			type : "POST",
			url : url,
			data : data,
			success: function(result) {
				console.log(result);
				//$('#familyMembersDetails').html(result);
			}
		});
	}
	
	$('#familyMemId').on('click',function(){
		var url = $('#contextPath').val() + '/idsReference/viewReferenceRecords/viewFamilyMembers';
		var data = {
				"familyid": $('#familyId').val()
		};
		
			$.ajax({
				type : "POST",
				url : url,
				data : data,
				success: function(result) {
					$('#familyMembersDetails').html(result);
				}
			});
	});

	$(document).on("click", "#idsTabs li", function(){
		clearTables();
		var tabname = getActiveTab();
		if (tabname == "cited"){
			citedHeader();
		}
		tablePatentData = new $.blackbox.reference.DataTable('#'+tabname+'PatentTable', '/idsReference/viewReferenceRecords/'+tabname+'PatentData', getPatentParams(), true);
		tableNplData = new $.blackbox.reference.DataTable('#'+tabname+'NplTable', '/idsReference/viewReferenceRecords/'+tabname+'NplData', getNplParams(), true);
/* 		$('.patentsCount').text($('#patentCount').val());
		$('.nplCount').text($('#nplCount').val()); */
	});
	
	$(document).on('click','tr',function(){
		//var td=$(event.target+' >td');
		//$(td[0]).attr('');
		var tr=$(event.target).closest('tr');
		console.log($(tr +' td a .refFlowId'));
		
	});
	
	$("#idsTabs li.active").trigger('click');
	
	/* $("#download").on('click', function () {
		$.blackbox.util.exportTableToCSV.apply(getActiveTab(), ['citedPatentTable', 'AllRecords.xls']);
	}); */
	
	$(document).on('click','#download', function(){
		//$.blackbox.util.exportTableToCSV.apply(this, ['tblActiveRecords', 'Active_Records_Application_View.xls']);
		$.blackbox.util.exportTableToCSV.apply(this, [getActiveTab()+'PatentTable', getActiveTab()+'PatentRecords.xls']);
		
		//$.blackbox.util.exportTableToCSV.apply(this, [getActiveTab()+'NplTable', getActiveTab()+'NplRecords.xls']);
		
	});
})

</script> 
