<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
   String context = request.getContextPath();
   String images = context+"/assets/images";
   String js = context+"/assets/js";
%>
   
<input type="hidden" id="jurisdictionList"	value="${listJurisdictions}" />
<input type="hidden" id="correspondenceId" value="${referenceDTO.correspondenceId.id}" />
  
<div class="main-content container">
   <ul class="breadcrumb">
      <li><a href="../management">Reference Management</a></li>
      <li>Review References</li>
   </ul>
   <table class="table custom-table">
      <thead>
         <tr>
            <th>Jurisdiction</th>
            <th>Application #</th>
            <th>Mailing Date</th>
            <th>Document Description</th>
            <th>Reference Entered By</th>
            <th>Last Review By</th>
         </tr>
      </thead>
      <tbody>
         <tr class="odd">
            <td>${referenceDTO.applicationJurisdictionCode}</td>
            <td>${referenceDTO.applicationNumber}</td>
            <td>${referenceDTO.mailingDateStr}</td>
            <td>${referenceDTO.documentDescription}</td>
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
            <td>${referenceDTO.referenceReviewedBy}<br/>${referenceDTO.referenceReviewedDate}</td>
         </tr>
      </tbody>
   </table>
   <div class="form-horizontal">
      <div class="row">
         <div class="col-sm-6">
            <div class="pdf-preview-container">
               <h2>OCR - Scanned</h2>
               <div>
					<iframe style="height:800px; width:100%" src="<%=context%>/reference/management/download?correspondenceId=${referenceDTO.correspondenceId.id}"></iframe>
				</div>
            </div>
         </div>
        <div class="col-sm-6">
            <h4 class="reference-head">Reference Entries</h4>
            
            <c:forEach items="${referenceDTO.referencePusData}" var="reference">
               <div class="entry-block-ref">
                  <div class="head"><span>US</span> <a href="javascript:void(0)" class="pull-right"><img id="${reference.id}" src="<%=images%>/svg/delete.svg" class="icon20x" onclick="popupMsgForReview(this);"></a></div>
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
                        </div>
                     </div>
                     <div class="clearfix">
								<div class="col-sm-12"><a href="javascript:void(0)" class="showMore">More...</a></div>
							</div>
							<div class="hidden-fields">
								<div class="form-group">
									<div class="col-sm-12">
										<div class="col-sm-6">
											<label class="control-label">Comments
											 </label>
											<div class="form-control-static">
												${reference.comment}
											</div>
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
                  <div class="head"><span>Foreign</span><a href="javascript:void(0)" class="pull-right"><img id="${reference.id}" src="<%=images%>/svg/delete.svg" class="icon20x" onclick="popupMsgForReview(this);"></a></div>
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
                        </div>
                     </div>
                     		<div class="clearfix">
								<div class="col-sm-12"><a href="javascript:void(0)" class="showMore">More...</a></div>
							</div>
							<div class="hidden-fields">
								<div class="form-group">
									<div class="col-sm-12">
										<div class="col-sm-6">
											<label class="control-label">Comments
											 </label>
											<div class="form-control-static">
												${reference.comment}
											</div>
										</div>
										<div class="col-sm-6">
											<label class="control-label">Kind Code</label>
											<div class="form-control-static">
												${reference.kindCode}
											</div>
										</div>
										<div class="col-sm-6">
											<label class="control-label">Translation Flag</label>
											<div class="form-control-static">
											yes
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
               
                  <div class="head"><span>NPL</span> <a href="javascript:void(0)" class="pull-right"><img id="${reference.id}" src="<%=images%>/svg/delete.svg" class="icon20x" onclick="popupMsgForReview(this);" ></a></div>
                  <div class="content">
                     <div class="clearfix">
                        <div class="col-sm-12">
                           	<div class=""><label class="control-label"></label>
	                        	<div class="form-control-static" data-type="npl" style="word-wrap:break-word;">${reference.stringData}</div>
    	                	</div>
                  		 </div>
                        </div>
                     </div>
                  </div>
               
            </c:forEach>
            
            <div class="divider"></div>
             <c:choose>
	            <c:when test="${empty referenceDTO.referencePusData &&  empty referenceDTO.referenceFpData && empty referenceDTO.referenceNplData}">
					<div class="text-left"><p>No References Found</p></div>
	   			</c:when>
	   			<c:otherwise>
	   			</c:otherwise>
   			</c:choose>
   			<jsp:include page="add-reference.jsp"></jsp:include>
         </div>
        
   </div>
  
</div>
