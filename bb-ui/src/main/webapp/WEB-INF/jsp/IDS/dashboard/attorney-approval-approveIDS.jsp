<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
 
<input type="hidden" id = "idsAppId" value='${appForm.dbId }'>
<input type="hidden" id = "var" value='attorney'>
<c:set var="pathjs" value="${contextPath}/assets/js/ids"
	scope="request" />
<c:set var="appId" value="${appForm.dbId }" />
<c:set var="idsId" value="${IdsId }"></c:set>


 <div class="main-content container">
            <div class="infopanel table-bordered">
                <div class="row">
                    <div class="col-xs-12 col-sm-6 col-md-4">
                        <div class="text-center ">
                            <h3>INFORMATION DISCLOSURE</h3>
                            <p>STATEMENT BY APPLICANT <br>( Not for submission under 37 CFR 1.99)</p>
                            
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-6 col-md-8">
                        <div class="row info-patent">
                            <div class="col-sm-3">
                                <label class="control-label">Application #</label>
                                <div class="form-control-static">${appForm.applicationNo }</div>
                            </div>
                            <div class="col-sm-3">
                                <label class="control-label">Filing Date</label>
                                <div class="form-control-static">${appForm.filingDate }</div>
                            </div>
                            <div class="col-sm-3">
                                <label class="control-label">First Name Inventor</label>
                                <div class="form-control-static">${appForm.inventor }</div>
                            </div>
                            <div class="col-sm-3">
                                <label class="control-label">Attorney Docket #</label>
                                <div class="form-control-static">${appForm.docketNo }</div>
                            </div>
                            <div class="col-sm-3">
                                <label class="control-label">Art Unit</label>
                                <div class="form-control-static">${appForm.artUnit }</div>
                            </div>
                            <div class="col-sm-3">
                                <label class="control-label">Examiner Name</label>
                                <div class="form-control-static">${appForm.examiner }</div>
                            </div>
                        </div>
                        <div class="action-btns-grid">
					 			<form:form method="post" action="${contextPath}/idsReference/viewReferenceRecords" id="viewReferenceRecordsForm${appForm.applicationNo }"><!-- family ID we have to give--> 
									<%-- <input type="hidden" name="familyid" value="${application.familyId}"/>
									<input type="hidden" name="jurisdiction" value="${application.jurisdiction}"/>
									<input type="hidden" name="applicationNumber" value="${appForm.applicationNo }"/>
									<input type="hidden" name="attorneyDocket" value="${appForm.docketNo }"/> --%>
		      						<div class="action-btns-grid"><a href="javascript:void(0)" onclick="document.getElementById('viewReferenceRecordsForm${appForm.applicationNo }').submit();"> View Reference Record</a> 
		      						
		      						</div> 
								</form:form>
							</div>
                    </div>
                </div>
            </div>
            
            <c:if test="${newRefCount>0 }">
             <div class="form-group clearfix ids-update">
             
                <div class="col-sm-12">
                    <h4 class="pull-left"><strong>${newRefCount } New References since IDS was prepared</strong> </h4>
                    <div class="pull-right row">
                     	<form method="post" action="../attorneyApproval/viewIncludeAllReferences" id="addRefLinkFrom${appForm.dbId}" >
                     	<input type="hidden" name="appForm" value="${appForm.dbId}"/>
                     	<input type="hidden" name="idsID" value="${IdsId }"/>
                        <button type="button" class="btn btn-success"  onclick="document.getElementById('addRefLinkFrom${appForm.dbId}').submit();"><strong>Include All</strong></button>
                       
                         <button type="button" class="btn btn-cancel " id="donotInlcude"onclick="DonotIncludeAction();">Do Not Include</button>
                 		 </form>
                    </div>
                </div>
            </div>
            </c:if>
            
          <div class="text-right margin-btm-10">
            <button type="button" class="btn disabled" id="donotBtn">Do Not File</button>
            <button type="button" class="btn disabled" id="dropBtn">Drop from this IDS</button>
           <a href="${contextPath}/reference/dashboard/newSelfCitedReference" target="_blank"><button type="button" class="btn btn-submit" id="selfCiteBtn" >Add Self-Citation</button></a> 
          </div>
            
            <div id="refList">
           
            </div>
           <div class="pull-left">
            <a class="btn btn-submit btn-link" data-target="#patentsConfirm" data-toggle="modal"  onclick="simpleMsg()" id="approvalBtn">Approve</a>
           <a  class="btn btn-submit btn-link" data-target="#patentsConfirm" data-toggle="modal"  onclick="simpleMsg()" id="requestChangeBtn"> Request Changes</a>
           </div>
          <div class="pull-right">
           <a href="${contextPath}/ids/attorneyApproval/dashboard"><button type="button" class="btn btn-cancel">Exit</button></a>  
            <a  href="javascript:void(0)" ><button type="button" class="btn btn-cancel" onclick="popupMsgForReview()"id="donotFileIDS">Do not file</button> </a>
          </div>

  </div>    
  
 <div class="popup-msg alert" id="simpleMsg">
        <div class="text-right"><a class="close" href="#">×</a></div>
        <div role="alert" class="content">
            <div class="form-group">
                <label class="control-label">Comments</label>
                <textarea class="form-control" id="attorneyComments"></textarea>
            </div> 
            <div>
                <button class="btn btn-cancel">Cancel</button>
                <button class="btn btn-submit" id="saveBtn">Save</button>
            </div>
        </div>
    </div>      
    
 <div id="popupMsgReview" class="popup-msg">
	<div class="text-right"><a class="close" href="javascript:void(0)" onclick="hideReviewPopUp();">&times;</a></div>
	<div class="content">
		<p class="msg">Are you sure</p>
		<p class="msg">you don't want to file ?</p>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-submit" onclick="donotFileIDSAction();">Yes</button>
        <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hideReviewPopUp();">No</button>
    </div>
</div>   
 <script type="text/javascript" src="${pathjs}/ids-common.js"></script>     
 <jsp:include page="../scripts/build-ids.jsp"></jsp:include> 
 
 <script type="text/javascript" >
 var refIds = [];
 $( document ).ready(function() {
	 
	 alert('${appId}');
	 viewReferences();
		
 });
 
 function viewReferences() {
		$.ajax({
	        type: 'POST',       
	        url: $('#contextPath').val()+'/ids/previewPage/${appId}',
	        data:{appId : '${appId}', sourceURL:'attorney', idsID: '${idsId}'
	        },
	        success:function(data){
	        	$('#refList').append(data);
	        	preview1();
	        }
	    });
	}
 
 
 function simpleMsg(){
     $("#simpleMsg").removeClass("hide");
     $("#simpleMsg").show();
     $("#simpleMsg").wrap("<div class='overlay'>");
 }
 $(document).on("click", ".popup-msg a.close, .popup-msg button", function(){
     $(this).parents(".popup-msg").addClass("hide");
     $(this).parents(".popup-msg").unwrap("<div class='overlay'>");
 });
 
 function hideReviewPopUp(){
		$("#popupMsgReview").addClass("hide");
		$("#popupMsgReview").unwrap("<div class='overlay'>");
}
 
	function preview1() {
		 $('.panel-heading').on('click',	function() {
					//aria-controls="collapseOne"
					//console.log($(event.target).children().length);
					var targetattr = $(event.target).attr("aria-controls");
					console.log(targetattr);
					var $targetattr=$(event.target);
					//console.log(indexofExpandTab($targetattr));
					console.log($targetattr.children().length);
					
					if ($targetattr.children().length > 0) {
					//if ($targetattr.hasClass('panel-title')) {
						var $aElement=$(event.target);
						
					} else {
						console.log('in else')
						console.log($targetattr.parent('a'));
						var $aElement=$($targetattr.parent('a'));
						
					}
					for (var i = 0; i < $('.panel-title a').length; i++) {
						if ($($('.panel-title  a')[i]).attr('aria-controls') === $($aElement).attr('aria-controls')) {
							break;
						} else {
							continue;
						}
					}
					console.log('final i '+i);
					if(i===$('.panel-title a').length){
						alert('all condition fail'+i);
					}
					else if($($aElement).hasClass('collapsed')){
						var targetId=$($aElement).attr('aria-controls');
						console.log($('#'+targetId+' .step-element').length);
						if($('#'+targetId+' .step-element').length===0){
							previewAjaxCall(i);
						}
					}
					else{
						
					}
				});
 }

	function previewAjaxCall(index) {
		$.ajax({
			url : $('#contextPath').val() + '/ids/' + urlArray[index] + '/'+$('#idsAppId').val(),
			type : 'POST',
			data:{
				sourceURL: "attorney"
			},
			success : function(data){
				//console.log(data);
				$('#back').show();
				var ullist = $('.panel-title a');
				var targetId = $(ullist[index]).attr('aria-controls');
				$('#' + targetId+' .previewIDS').html(data);
				previewShowMoreClick();
				showMorePreviewBtn(targetId);
				//$('.step-element').hide();
				
			}
		});
	}
	
	function previewShowMoreClick() {
		$('.user-management .show-more').unbind('click');
		$('.user-management .show-more').on('click',function() {
							var index = indexofExpand();
							var ullist=$('.panel-title a');
						
							var elemnetId=$(ullist[index]).attr('aria-controls');
												 
							$.ajax({
								url : $('#contextPath').val() + '/ids/'+urlArray[index]+'/'+$('#idsAppId').val(),
										type : 'POST',
										data : {
											offset : $('#' + elemnetId+ ' .step-element tbody tr').length,
											sourceURL: "previewIDS"
										},
										success : function(data) {
											$('#back').show();
						
											$('#' + elemnetId+ ' .step-element tbody').append(data);
											showMorePreviewBtn(elemnetId);
						
										}
									});
	  					});
	}
	
	function showMorePreviewBtn(parentElement){
		console.log('show more hide');
		console.log($('#'+parentElement+' .show-more'));
		console.log($('#'+parentElement+' .totalRecords').val());
		console.log(Number($('#'+parentElement+' .totalRecords').val()));
		console.log($('#' + parentElement+ ' .step-element tbody tr').length);
		if(Number($('#'+parentElement+' .totalRecords').val())==$('#' + parentElement+ ' .step-element tbody tr').length){
			$('#'+parentElement+' .show-more').hide();
		}
		else{
			$('#'+parentElement+' .show-more').show();
		}
	}
	
 </script>
  <script type="text/javascript">
        $(function(){
            $(document).on("click",  '.activeInactiveBtns',  function(){
                if($(this).is(":checked")){
         
                	$("#selfCiteBtn").addClass("disabled").removeClass("btn-submit");
                    $("#donotBtn, #dropBtn").addClass("btn-submit").removeClass("disabled");
                }
                else{
            			if(!($("input.activeInactiveBtns:checked").length)){
            				$("#selfCiteBtn").addClass("btn-submit").removeClass("disabled");
                            $("#donotBtn, #dropBtn").addClass("disabled").removeClass("btn-submit");
            			}
                }   
            });
            
        });
        
    </script>
  
<script type="text/javascript">

$( "#donotBtn" ).click(function() {
	var selected = [];
	$("input.activeInactiveBtns:checked").each(function(item, ele){
		selected.push($(ele).attr('data-id'));
	});
	console.log("selected", selected);
	
	$.ajax({type: "POST",
        url: $('#contextPath').val() +"/ids/attorneyApproval/refActions",
        data: { refIds: selected,
        		action:"DonotFile"	
        },
        success:function(result){
}});
});

$( "#dropBtn" ).click(function() {
	var selectedDrop = [];
	$("input.activeInactiveBtns:checked").each(function(item, ele){
		selectedDrop.push($(ele).attr('data-id'));
	});
	
	$.ajax({type: "POST",
        url: $('#contextPath').val() +"/ids/attorneyApproval/refActions",
        data: { refIdsDrop: selectedDrop,
        		action:"DropFromIDS"	
        },
        success:function(result){
        	//	 window.location= $('#contextPath').val() +'/ids/attorneyApproval/dashboard';
}});
});

var btn;
$( "#approvalBtn" ).click(function() {
	btn = "ApproveBtn";
});

$( "#requestChangeBtn" ).click(function() {
	 btn = "requestChangeBtn";
});


$( "#saveBtn" ).click(function() {
	
	if(btn==="ApproveBtn"){
		
	var idsId= ${idsId};
	var attorneyComments = document.getElementById("attorneyComments").value
	$.ajax({type: "POST",
        url: $('#contextPath').val() +"/ids/attorneyApproval/IDSActions",
        data: { idsId: idsId,
        		action:"approveIDS"	,
        		attorneyComments: attorneyComments
         },
        success:function(result){
        	
        	// window.location.href= $('#contextPath').val() +'/ids/attorneyApproval/approveIDS ';
         }});
	}
	
	if(btn==="requestChangeBtn"){
	
		var idsId= ${idsId};
		var attorneyComments = document.getElementById("attorneyComments").value
		$.ajax({type: "POST",
	        url: $('#contextPath').val() +"/ids/attorneyApproval/IDSActions",
	        data: { idsId: idsId,
	        		action:"requestChanges",
	        		attorneyComments: attorneyComments
	         },
	        success:function(result){
	        	
	        	// window.location.href= $('#contextPath').val() +'/ids/attorneyApproval/approveIDS ';
	         }});	
	
	}
	
});

function donotFileIDSAction(){
	hideReviewPopUp();
	var idsId= ${idsId};
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

function DonotIncludeAction(){
	$('#donotInlcude').parent().parent().parent().remove();
	var appId= ${appId};
	var action;
	$.ajax({type: "POST",
        url: $('#contextPath').val() +"/ids/attorneyApproval/IDSActions",
        data: { appId: appId,
        		action:"donotIncludeReference",
        		
         },
        success:function(result){
        	
        	// window.location.href= $('#contextPath').val() +'/ids/attorneyApproval/approveIDS ';
         }});	
	
}

</script>
