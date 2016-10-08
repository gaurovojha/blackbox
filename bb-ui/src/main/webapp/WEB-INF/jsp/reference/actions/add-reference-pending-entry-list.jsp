<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String images = context+"/assets/images";
	String js = context+"/assets/js";
%>

<div class="col-sm-12">
            <h4 class="reference-head">Reference Entries</h4>
            
            <c:out value="${referenceDTO.correspondenceId.id}"></c:out>
            <c:forEach items="${referenceDTO.referencePusData}" var="reference">
               <div class="entry-block-ref">
                  <div class="head"><span>US</span> </div>
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
                  <div class="head"><span>Foreign</span></div>
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
               
                  <div class="head"><span>NPL</span> </div>
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