<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<div class="main-content container">
	<div class="clearfix">
			<div class="text-right form-group">
				<div class="switch-control mdm">
					<label class="switch">
                      <input type="checkbox" id="switchRecordsView" class="switch-input" checked="checked">
                      <span class="switch-label" data-on="Application View" data-off="Family View"></span>
                      <span class="switch-handle"></span>
                    </label>
				</div>
			</div>
		</div>

	<div class="tab-container">
		<ul class="tab-actions pull-right">
				<li>
					<div class="daterange-picker tab">										
          				<input type="text" class="form-control date"   name="datefilter" value="Showing Till Date">
						<span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
          			</div>
				</li>
			<!--search control header-->
			<li class="search-control"><jsp:include
					page="application-search.jsp"></jsp:include></li>
			<li><a href="javascript:void(0)" class="export"><i><img
						src="${pathImg}/export.svg" class="icon20x "></i> Export</a></li>
		</ul>
		<!-- Nav tabs -->
		<ul id = "tabGroupActionItems" class="nav nav-tabs custom-tabs" role="tablist">
			<li role="presentation" class="active"><a href="#InitiateIDStab"
				role="tab" data-toggle="tab">Initiate IDS</a></li>
			<li role="presentation"><a href="#pendingIDStab" role="tab"
				data-toggle="tab" id="idsPendingApproval">Pending Approval (<span id="idsPendingCount">${idsPendingCount}</span>)</a></li>
			<li role="presentation"><a href="#fileIDStab" role="tab"
				data-toggle="tab" id="idsFilingReady">Ready for Filing (<span id="idsReadyForFiling">${idsPendingCount}</span>)</a></li>
			<li role="presentation"><a href="#usptoIDStab" role="tab"
				data-toggle="tab">WIP USPTO Filing (<span id="idsWIPCount">${idsPendingCount}</span>)</a></li>
			<li role="presentation"><a  id = "tabIDSFiledAtUSPTO" href="#notification1449" role="tab"
				data-toggle="tab">IDS Filed @ USPTO (<span id="idsFiledCount">${idsPendingCount}</span>)</a></li>
		</ul>
		<div class="tab-content">
			<jsp:include page="initiateIDS.jsp" />

			<jsp:include page="pendingIDS.jsp" />

			<jsp:include page="fileIDS.jsp" />
			
			<jsp:include page="usptoIDS.jsp" />
			
			<jsp:include page="usptoFiledIDS.jsp" />
			
	
		</div>
	</div>
	<div id="changeIdsStatusConfirmationBox"
					class="changeIdsStatusConfirmationBox popup-msg" idsId="" selectedStatus="">
					<div class="text-right">
						<a class="close" href="#" onclick="hideConfirmationBox()">&times;</a>
					</div>
					<div class="content">
						<p class="msg"></p>
					</div>
					<div class="modal-footer">
						<button type="submit" class="btn btn-submit" id="submitIdsStatus">Yes</button>
						<button type="button" class="btn btn-cancel"
							onclick="hideConfirmationBox();">NO</button>
					</div>
	</div>
	
	<!-- Email response pop up -->
	<div class="modal custom fade" id="emailResponse" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Resend IDS for Approval</h4>
	      </div>
	      	<div class="modal-body">
		       <label class="control-label">Comments for Attorney</label> 
		       <textarea class="form-control" rows="3" id="parallegalComments"></textarea>
      		</div>
      		 <div class="modal-footer">
	        <button type="button" id="submitEmailResponse" class="btn btn-submit" data-dismiss="modal" notificationId="" idsId="">Send for Approval</button>
	      </div>
	    </div>
	  </div>
	</div>
	<jsp:include page="../scripts/ids-data-table.jsp" />
	<jsp:include page="../scripts/admin.jsp" />
</div>
<script type="text/javascript">
		$(function(){
			$(document).on("click", ".custom-tabs li a", function(){
				if($(this).attr("href")=="#InitiateIDStab"){
					$(".switch-control.mdm").show();
				}
				else if($(this).attr("href")=="#pendingIDStab" || $(this).attr("href")=="#fileIDStab" || $(this).attr("href")=="#usptoIDStab" || $(this).attr('href') == "#notification1449" ){
					$(".switch-control.mdm").hide();
				}
			});
		});
	</script>