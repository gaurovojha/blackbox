<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<html>

<%
   String context = request.getContextPath();
   String images = context+"/assets/images";
   String js = context+"/assets/js";
%>

<body>
<div class="main-content container">
<input type="hidden" id="correspondenceId" value="${referenceDTO.correspondenceId.id}">
		<div class="page-header">
            <h2 class="page-heading">Source Document</h2>
        </div>
		<table class="table custom-table">
			<thead>
				<tr>
					<th>Jurisdiction</th>
					<th>Application #</th>
					<th>Document Description</th>
					<th>Mailing Date</th>
					<th>Reference Entered by</th>
				</tr>
			</thead>
			<tbody>
		         <tr class="odd">
		            <td>${referenceDTO.applicationJurisdictionCode}</td>
		            <td>${referenceDTO.applicationNumber}</td>
		            <td>${referenceDTO.documentDescription}</td>
		            <td>${referenceDTO.mailingDateStr}</td>
		            <td>
		               <c:forEach var="userDetail" items="${referenceDTO.referenceEnteredByDetails}" begin="0" end="0">
		                  ${userDetail.key} - ${userDetail.value}
		               </c:forEach>
		               <c:forEach items="${referenceDTO.referenceEnteredByDetails}" var="userDetail" begin="1" varStatus="status">
		                  <c:if test="${status.index eq 1}">
		                     <a href='javascript:void(0);' class="moreUsers">...More</a>
		                  </c:if>
		                  <span class="spanMoreUsers" style="display: none;">
		                  <br/>
		                  ${userDetail.key} - ${userDetail.value}
		                  </span>	
		               </c:forEach>
		            </td>
		         </tr>
		      </tbody>
		</table>
		<div class="form-horizontal">
			<div class="row">
				<div class="col-sm-6">
		            <div class="pdf-preview-container">
		               <h2>OCR - Scanned</h2>
		               <div>
							<iframe style="height:800px; width:100%" src="<%=context%>/reference/dashboard/downloadFile"></iframe>
						</div>
		            </div>
		         </div>
				<div class="col-sm-6">
					<h4 class="reference-head">Added Reference Entries</h4>

				<c:forEach items="${referenceDTO.referencePusData}" var="reference">
					<div class="entry-block-ref">
						<div id="reviewDetails${reference.id}" style="display:none; position: absolute; min-height: 100px; z-index: 1; background: #f5f5f5">
							<c:forEach var="userReviewDetail" items="${reference.referenceReviewedDetails}">
			                  ${userReviewDetail.key} - ${userReviewDetail.value}
			                  <br/>
			               </c:forEach>
						</div>
						<div class="head"><span>US</span>
							<span class="action-btns">
								<a href="javascript:void(0)" id="${reference.id}" class="delete pull-right"><img id="${reference.id}" src="<%=images%>/svg/drop.svg" class="icon20x" onclick="popupMsgForReview(this);"></a>
								<%-- <a href="javascript:void(0)" class="reviewed pull-right"><img src="<%=images%>/svg/ok.svg" class="icon20x markReviewed"> Me, ${fn:length(reference.referenceReviewedDetails)} Other</a> --%>
								<c:choose>
									<c:when test="${bbx:containsKey(reference.referenceReviewedDetails, 'Me')}">
										<a class="pull-right"><img id="${reference.id}" src="<%=images%>/svg/ok.svg" class="icon20x">&nbsp;Me
											<c:if test=	"${fn:length(reference.referenceReviewedDetails)-1 > 0}">
												,&nbsp;${fn:length(reference.referenceReviewedDetails)-1}&nbsp;
												<span id="${reference.id}" class="reviewDetails">Other&nbsp;</span>
											</c:if>	
										</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0)" id="${reference.id}" class="flag pull-right"><img id="${reference.id}" src="<%=images%>/svg/flag.svg" class="icon20x markFlagged"> Review 
											<c:if test=	"${fn:length(reference.referenceReviewedDetails) > 0}">
												${fn:length(reference.referenceReviewedDetails)}&nbsp;
												<span id="${reference.id}" class="reviewDetails">Other&nbsp;</span>
											</c:if>
										</a>
									</c:otherwise>								
								</c:choose>
							</span>
						</div>
						<div class="content">
							<div class="form-group">
		                        <div class="col-sm-12">
		                           <div class="col-sm-4">
		                              <label class="control-label">Publication Number</label>
		                              <div class="form-control-static">${reference.convertedPublicationNumber}</div>
		                           </div>
		                           <div class="col-sm-4">
		                              <label class="control-label">Publication Date</label>
		                              <div class="form-control-static">${reference.publicationDate}</div>
		                           </div>
		                           <div class="col-sm-4 text-right">
			                           <label class="control-label current-status">
			                           	<c:choose>
			                           		<c:when test="${empty reference.idsId}">
												<a href="trifolder-may102015.html" target="_blank">${reference.flowStatus}</a>
											</c:when>
											<c:otherwise>
												${reference.idsStatus}<br/>${reference.filingDate}
											</c:otherwise>
										</c:choose>	
										</label>
									</div>
		                        </div>
		                     </div>
							<div class="clearfix">
								<div class="col-sm-12"><a href="#" class="showMoremore">More..</a></div>
							</div>
							<div class="hidden-fields">
								<div class="form-group">
									<div class="col-sm-12">
										<div class="col-sm-6">
											<label class="control-label">Comments
												<a href="javascript:void(0)" onclick="simpleMsg(${reference.id}')"><i><img src="<%=images%>/svg/edit.svg" class="icon16x"></i></a>
												<c:set var="display" value="none"/>
												<c:if test="${not empty reference.comment}">
													<c:set var="display" value="block"/>
												</c:if>
												<a href="javascript:void(0)" id="deleteReferenceLink${reference.id}" style="display:${display};"><i><img src="<%=images%>/svg/delete.svg" class="icon16x" title="Delete Reference" onclick="deleteComment(this,${reference.id});"></i></a>
											</label>
											<div id="comments${reference.id}" class="form-control-static">${reference.comment}</div>
										</div>
										<div class="col-sm-6">
											<label class="control-label">Kind Code</label>
											<div class="form-control-static">
												${reference.kindCode}
											</div>
										</div>
									</div>
								</div>
					 		</div>
						</div>
					</div>
				</c:forEach>
				
				<c:forEach items="${referenceDTO.referenceFpData}" var="reference">
					<div class="entry-block-ref">
						<div id="reviewDetails${reference.id}" style="display:none; position: absolute; min-height: 100px; z-index: 1; background: #f5f5f5">
							<c:forEach var="userReviewDetail" items="${referenceDTO.referenceReviewedDetails}">
			                  ${userReviewDetail.key} - ${userReviewDetail.value}
			                  <br/>
			               </c:forEach>
						</div>
						<div class="head"><span>Foreign</span>
							<span class="action-btns">
								<a href="javascript:void(0)" id="${reference.id}" class="delete pull-right"><img id="${reference.id}" src="<%=images%>/svg/drop.svg" class="icon20x" onclick="popupMsgForReview(this);"></a>
								<%-- <a href="javascript:void(0)" class="reviewed pull-right"><img src="<%=images%>/svg/ok.svg" class="icon20x"> Me, ${fn:length(reference.referenceReviewedDetails)} Other</a> --%>
								<c:choose>
									<c:when test="${bbx:containsKey(reference.referenceReviewedDetails, 'Me')}">
										<a class="pull-right reviewDetails"><img src="<%=images%>/svg/ok.svg" class="icon20x reviewDetails">&nbsp;Me
											<c:if test=	"${fn:length(reference.referenceReviewedDetails)-1 > 0}">
												,&nbsp;${fn:length(reference.referenceReviewedDetails)-1}&nbsp;
												<span id="${reference.id}" class="reviewDetails">Other&nbsp;</span>
											</c:if>	
										</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0)" id="${reference.id}" class="flag pull-right"><img id="${reference.id}" src="<%=images%>/svg/flag.svg" class="icon20x markFlagged"> Review 
											<c:if test=	"${fn:length(reference.referenceReviewedDetails) > 0}">
												${fn:length(reference.referenceReviewedDetails)}&nbsp;
												<span id="${reference.id}" class="reviewDetails">Other&nbsp;</span>
											</c:if>
										</a>
									</c:otherwise>								
								</c:choose>
							</span>
						</div>
						<div class="content">
							<div class="form-group">
								<div class="col-sm-12">
									<div class="col-sm-4">
		                              <label class="control-label">Jurisdiction</label>
		                              <div class="form-control-static">${reference.jurisdiction}</div>
                           			</div>
		                           	<div class="col-sm-4">
		                              <label class="control-label">Publication Number</label>
		                              <div class="form-control-static">${reference.convertedForeignDocumentNumber}</div>
		                           	</div>
									<div class="col-sm-3">
										<label class="control-label">Publication Date</label>
										<div class="form-control-static">${reference.publicationDate}</div>
									</div>
									<div class="col-sm-2 text-right">
										<label class="control-label examin-status">
											<c:choose>
				                           		<c:when test="${empty reference.idsId}">
													<a href="trifolder-may102015.html" target="_blank">${reference.flowStatus}</a>
												</c:when>
												<c:otherwise>
													${reference.idsStatus}<br/>${reference.filingDate}
												</c:otherwise>
											</c:choose>
										</label>
									</div>
								</div>
							</div>
							<div class="clearfix">
								<div class="col-sm-12"><a href="#" class="showMoremore">More..</a></div>
							</div>
							<div class="hidden-fields">
								<div class="form-group">
									<div class="col-sm-12">
										<div class="col-sm-6">
											<label class="control-label">Kind Code</label>
											<div class="form-control-static">
												${reference.kindCode}
											</div>
										</div>
										<div class="col-sm-6">
											<label class="control-label">Comments
												<a href="javascript:void(0)" onclick="simpleMsg(${reference.id})"><i><img src="<%=images%>/svg/edit.svg" class="icon16x"></i></a>
												<c:set var="display" value="none"/>
												<c:if test="${not empty reference.comment}">
													<c:set var="display" value="block"/>
												</c:if>
												<a href="javascript:void(0)" id="deleteReferenceLink${reference.id}" style="display:${display};"><i><img src="<%=images%>/svg/delete.svg" class="icon16x" title="Delete Reference" onclick="deleteComment(this,${reference.id});"></i></a>
											</label>
											<div id="comments${reference.id}" class="form-control-static">${reference.comment}</div>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-12">
										<div class="col-sm-6 t-flag-control">
											<label class="control-label">Translation (T) 
											<a href="javascript:void(0)" id="${reference.id}" class="edit-link"><i><img id="${reference.id}" src="<%=images%>/svg/edit.svg" class="icon16x"></i></a>
											<a href="javascript:void(0)" id="${reference.id}" class="save-link"><i><img id="${reference.id}" src="<%=images%>/svg/save.svg" class="icon16x"></i></a>
											</label>
											<div class="form-control-static">
												<div class="checkbox-without-label">
													<c:set var="check" value=""/>
													<c:if test= "${reference.englishTranslation eq true}">
														<c:set var="check" value="checked"/>
													</c:if>
                                                    <input id="translation${reference.id}" type="checkbox" ${check} disabled><label>default</label>
                                                </div>
											</div>
										</div>
										<div class="col-sm-6">
											<label class="control-label">PDF</label>
											<div class="add-remove-pdf">
												<c:set var="displayAddPdf" value="block"></c:set>
												<c:set var="displayPdf" value="none"></c:set>
												<c:if test = "${reference.attachment eq true}">
													<c:set var="displayAddPdf" value="none"></c:set>
													<c:set var="displayPdf" value="block"></c:set>
												</c:if>
												<form method="post" id="uploadFileForm${reference.id}" name="fileinfo" action="../reference/management/uploadAttachment" enctype="multipart/form-data">
											        <a href="javascript:document.getElementById('PdfFile-${reference.id}').click()" style="display:${displayAddPdf}" title="Add PDF">Add PDF</a>
											        <input type="file" name="attachment" id="PdfFile-${reference.id}" style = "display:none;" class="form-element pdfFileUpload" accept="application/pdf"/>
											        <input type="hidden" name="id" value="${reference.id}">
							         				<input type="hidden" id="referenceType${reference.id}" name="referenceType" value="FP">
											    </form>
	         									<div id="filename${reference.id}"></div>
								           		<form:form method="post" action="../reference/management/download/referencne" id="download${reference.id}" target="_blank">
							         				  <input type="hidden" name="id" value="${reference.id}">
							         				  <input type="hidden" id="referenceType${reference.id}" name="referenceType" value="FP">
							        				     <a href="javascript:void(0)" style="display:${displayPdf}" onclick="document.getElementById('download${reference.id}').submit();" target="_blank" title="PDF">PDF
															<img src="<%=images%>/svg/attachment.svg" class="icon16x attachmenticon">
														</a>
								   			  	</form:form>
	                                            <a href="javascript:void(0)" style="display:${displayPdf}" title="Remove PDF"><i><img id="${reference.id}" src="<%=images%>/svg/drop.svg" class="icon16x" title="Remove PDF"></i></a>
	                                        </div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
				
				<c:forEach items="${referenceDTO.referenceNplData}" var="reference">
					<div class="entry-block-ref">
						<div id="reviewDetails${reference.id}" style="display:none; position: absolute; min-height: 100px; z-index: 1; background: #f5f5f5">
							<c:forEach var="userReviewDetail" items="${referenceDTO.referenceReviewedDetails}">
			                  ${userReviewDetail.key} - ${userReviewDetail.value}
			                  <br/>
			               </c:forEach>
						</div>
						<div class="head"><span>NPL</span>
							<span class="action-btns">
								<a href="javascript:void(0)" id="${reference.id}" class="delete pull-right"><img id="${reference.id}" src="<%=images%>/svg/drop.svg" class="icon20x" onclick="popupMsgForReview(this);"></a>
								<%-- <a href="javascript:void(0)" class="reviewed pull-right"><img src="<%=images%>/svg/ok.svg" class="icon20x"> Me, ${fn:length(reference.referenceReviewedDetails)} Other</a> --%>
								<c:choose>
									<c:when test="${bbx:containsKey(reference.referenceReviewedDetails, 'Me')}">
										<a class="pull-right reviewDetails"><img id="${reference.id}" src="<%=images%>/svg/ok.svg" class="icon20x reviewDetails">&nbsp;Me
											<c:if test=	"${fn:length(reference.referenceReviewedDetails)-1 > 0}">
												,&nbsp;${fn:length(reference.referenceReviewedDetails)-1}&nbsp;
												<span id="${reference.id}" class="reviewDetails">Other&nbsp;</span>
											</c:if>	
										</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0)" id="${reference.id}" class="flag pull-right"><img id="${reference.id}" src="<%=images%>/svg/flag.svg" class="icon20x markFlagged reviewDetails"> Review 
											<c:if test=	"${fn:length(reference.referenceReviewedDetails) > 0}">
												${fn:length(reference.referenceReviewedDetails)}&nbsp;
												<span id="${reference.id}" class="reviewDetails">Other&nbsp;</span>
											</c:if>
										</a>
									</c:otherwise>								
								</c:choose>
							</span>
						</div>
						<div class="content">
							<div class="form-group">
								<div class="col-sm-12">
									<div class="col-sm-9">
										<p>${reference.stringData}</p>
									</div>
									<div class="col-sm-3 text-right">
										<label class="control-label current-status">
											<c:choose>
				                           		<c:when test="${empty reference.idsId}">
													<a href="trifolder-may102015.html" target="_blank">${reference.flowStatus}</a>
												</c:when>
												<c:otherwise>
													${reference.idsStatus}<br/>${reference.filingDate}
												</c:otherwise>
											</c:choose>
										</label>
									</div>
								</div>
							</div>
							<div class="clearfix">
								<div class="col-sm-12"><a href="#" class="showMoremore">More..</a></div>
							</div>
							<div class="hidden-fields">
								<div class="form-group">
									<div class="col-sm-12">
										
										<%-- <div class="col-sm-6">
											<label class="control-label">Comments
												<a href="javascript:void(0)" onclick="simpleMsg(${reference.id})"><i><img src="<%=images%>/svg/edit.svg" class="icon16x"></i></a>
												<a href="javascript:void(0)"><i><img src="<%=images%>/svg/delete.svg" class="icon16x" title="Delete Reference"></i></a>
											</label>
											
											no comments in npl 
											<div id="comments${reference.id}" class="form-control-static">${reference.comment}</div>
										</div>> --%>
										
										<div class="col-sm-6 t-flag-control">
											<label class="control-label">Translation Flag (T) 
											<a href="javascript:void(0)" id="${reference.id}" class="edit-link"><i><img id="${reference.id}" src="<%=images%>/svg/edit.svg" class="icon16x"></i></a>
											<a href="javascript:void(0)" id="${reference.id}" class="save-link"><i><img id="${reference.id}" src="<%=images%>/svg/save.svg" class="icon16x"></i></a>
											</label>
											<div class="form-control-static">
												<div class="checkbox-without-label">
													<c:set var="check" value=""/>
													<c:if test= "${reference.englishTranslation eq true}">
														<c:set var="check" value="checked"/>
													</c:if>
                                                    <input id="translation${reference.id}" type="checkbox" ${check} disabled><label>default</label>
                                                </div>
											</div>
										</div>
									
										<div class="col-sm-6">
											<label class="control-label">PDF</label>
											<div class="add-remove-pdf">
												<c:set var="displayAddPdf" value="block"></c:set>
												<c:set var="displayPdf" value="none"></c:set>
												<c:if test = "${reference.attachment eq true}">
													<c:set var="displayAddPdf" value="none"></c:set>
													<c:set var="displayPdf" value="block"></c:set>
												</c:if>
												<form method="post" id="uploadFileForm${reference.id}" name="fileinfo" action="../reference/management/uploadAttachment" enctype="multipart/form-data">
											        <a href="javascript:document.getElementById('PdfFile-${reference.id}').click()" style="display:${displayAddPdf}" title="Add PDF">Add PDF</a>
											        <input type="file" name="attachment" id="PdfFile-${reference.id}" style = "display:none;" class="form-element pdfFileUpload" accept="application/pdf"/>
											        <input type="hidden" name="id" value="${reference.id}">
							         				<input type="hidden" id="referenceType${reference.id}" name="referenceType" value="NPL">
											    </form>
												
	         									<div id="filename${reference.id}"></div>
								           		<form:form method="post" action="../reference/management/download/referencne" id="download${reference.id}" target="_blank">
							         				  <input type="hidden" name="id" value="${reference.id}">
							         				  <input type="hidden" id="referenceType${reference.id}" name="referenceType" value="NPL">
							        				     <a href="javascript:void(0)" style="display:${displayPdf}" onclick="document.getElementById('download${reference.id}').submit();" target="_blank" title="PDF">PDF
															<img src="<%=images%>/svg/attachment.svg" class="icon16x attachmenticon">
														</a>
								   			  	</form:form>
	                                            <a href="javascript:void(0)" style="display:${displayPdf}" title="Remove PDF"><i><img id="${reference.id}" src="<%=images%>/svg/drop.svg" class="icon16x" title="Remove PDF"></i></a>
	                                        </div>    
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
					<div class="text-left">
        				<button type="button" class="btn btn-cancel" onclick="window.location.href='ids-US.html'">Close</button>
        				<button type="button" class="btn btn-submit updateReviewStatusButton" disabled onclick="updateReviewAction();	">Update Review Status</button>
        			</div>
					<div class="divider"></div>
					<div class="text-left">
	       				<jsp:include page="../../reference/actions/add-reference.jsp"></jsp:include>
	       			</div>
				</div>
			</div>
		</div>
	</div>
	
	<!--simple popup msg-->
    <div class="popup-msg alert" id="simpleMsg">
        <div class="text-right"><a class="close" href="#">&times;</a></div>
        <div role="alert" class="content">
            <div class="form-group">
                <label class="control-label">Comments</label>
                <textarea id="commentText" class="form-control"></textarea>
            </div> 
            <div>
            	<input type="hidden" id="refId" name="id" value="">
                <button class="btn btn-cancel">Cancel</button>
                <button class="btn btn-submit" onclick="updateComments();">Save</button>
            </div>
        </div>
    </div>  
    
</body>
</html>	


<script type="text/javascript">
	        
	$(function () {
	    $(".hidden-fields").hide();
	    $(document).on("click",".showMoremore", function(){
	        $(this).parents(".clearfix").next(".hidden-fields").toggle();
	         $(this).text(function(i, text){
	              return text === "More.." ? "Less.." : "More..";
	         })
	    });
	    
	    $(".save-link").hide();
        $(".edit-link").on("click", function(){
        	$(this).hide();
        	$(this).next(".save-link").show();
        	$(this).parents(".t-flag-control").find("input[type='checkbox']").removeAttr("disabled");
        });

        $(".save-link").on("click", function(event){
        	var refId = ($(event.target).attr('id')==='undefined'?'':$(event.target).attr('id'));
        	updateTranslationFlag(refId);
        	$(this).hide();
        	$(this).prev(".edit-link").show();
        	$(this).parents(".t-flag-control").find("input[type='checkbox']").attr("disabled","disabled");
        });

        /* $(".add-remove-pdf a[title='Add PDF']").hide(); */
        $(".add-remove-pdf a[title='Remove PDF']").on("click", function(){
        	var refId = ($(event.target).attr('id')==='undefined'?'':$(event.target).attr('id'));
        	removeAttachment(this,refId);
        });
        
        $(".add-remove-pdf a[title='Add PDF']").on("click", function(){
        	var refId = ($(event.target).attr('id')==='undefined'?'':$(event.target).attr('id'));
        });
        
/* 		$(".reviewed").hide();
        $(".flag").on("click", function(){
        	$(this).hide();
        	$(this).closest("div .action-btns").children("a.reviewed").show();
        }); */
        
        $(".reviewDetails").on("mouseover", function(){
        	var refId = ($(event.target).attr('id')==='undefined'?'':$(event.target).attr('id'));
        	$('#reviewDetails'+refId).show();
        });
        
        $(".reviewDetails").on("mouseleave", function(){
        	var refId = ($(event.target).attr('id')==='undefined'?'':$(event.target).attr('id'));
        	$('#reviewDetails'+refId).hide();
        });
	});

    //simple alert popup js
       function simpleMsg(id){
    	   
    	// set refId and comments in popus msg
    	   $("#refId").val(id);
    	   $("#commentText").val($('#comments'+id).text());
           $("#simpleMsg").removeClass("hide");
           $("#simpleMsg").show();
           $("#simpleMsg").wrap("<div class='overlay'>");
       }
       $(document).on("click", ".popup-msg a.close, .popup-msg button", function(){
           $(this).parents(".popup-msg").addClass("hide");
           $(this).parents(".popup-msg").unwrap("<div class='overlay'>");
       });
            
     //delete comments
       function deleteComment(current,id){
    	   
    	// set refId and comments in popus msg
    	   $("#refId").val(id);
    	   $("#commentText").val("");
    	   updateComments();
    	   $('#deleteReferenceLink'+id).hide();
    	   $('#comments'+id).val("");
       }
     
       $('.pdfFileUpload').on('change', function (ev) {
    	   	var data = ($(ev.target).attr('id')==='undefined'?'':$(event.target).attr('id'));
    	   	var arr = data.split('-');
    	   	$('#filename'+arr[1]).html(ev.target.files[0].name);
    	    uploadAttachment($(ev.target),arr[1]);
    	});

    </script>