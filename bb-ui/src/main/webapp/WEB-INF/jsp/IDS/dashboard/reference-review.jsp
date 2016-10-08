<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<div class="main-content container">
	<table class="table custom-table" id="applicationGrid">
		<thead>
			<tr>
				<th>Jurisdiction</th>
				<th>Application #</th>
				<th>Mailing Date</th>
				<th>Document Description</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${listItems}" var="item">
			<tr class="odd">
				<td>${item.jurisdiction}</td>
				<td>${item.applicationNo}</td>
				<td><bbx:date dateFormat="MMM dd, yyyy" date="${item.mailingDate}" /></td>
				<td>${item.documentDesc}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="row">
		<div class="col-sm-5">
			<div class="pdf-preview-container single">
				<h2>1449</h2>
				<img src="images/pdf-reader.png" class="img-responsive">
			</div>
		</div>
		<div class="col-sm-7">
			<div class="form-horizontal">
				<div class="form-group">
					<div class="col-sm-5 form-inline">
						<label class="control-label">Showing</label> <select
							class="form-control" id = "selIDSPending1449">
							<c:forEach items="${listItems}" var="item">
							<c:forEach  items="${item.idsPending1449 }" var="entry" varStatus = "loop">
							<c:choose>
								<c:when test="${loop.first}">
									<option value = "${entry.key}" selected><bbx:date dateFormat="MMM dd, yyyy" date="${entry.value}" /></option>
								</c:when>
								<c:otherwise>
									<option value = "${entry.key}"><bbx:date dateFormat="MMM dd, yyyy" date="${entry.value}" /></option>
								</c:otherwise>
							</c:choose>
							</c:forEach>
							</c:forEach>
						</select>
					</div>
					<div class="col-sm-7 text-right ">
						<button type="button" id="btnAccepted" class="btn btn-submit small disabled actionBtn">Accepted</button>
						<button type="button" id= "btnRejected" class="btn btn-cancel small disabled actionBtn">Rejected</button>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<div class="panel-group user-management" id="accordion"
							role="tablist" aria-multiselectable="true">
							<div class="panel panel-default">
								<div class="panel-heading" role="tab">
									<h4 class="panel-title">
										<span></span> <a id = "accordianUSPatent" role="button" data-toggle="collapse"
											data-parent="#accordion" href="#collapseOne"
											aria-expanded="false" aria-controls="collapseOne"
											>US Patents
											<%-- <c:choose>
												<c:when test = " ${countMap['PUS'] eq 0}">
													(0)
												</c:when>
												<c:otherwise>
												(${countMap['PUS']})
												</c:otherwise>
											</c:choose> --%>
											(<c:set var = "found" value = "0"/>
											<c:forEach items = "${countMap}" var ="entry" varStatus ="loop">
												<c:if test = "${entry.key eq 'PUS' }">
													<span id="pusCount" class="RecordCount">${countMap['PUS']}</span>
													<c:set var = "found" value = "1"></c:set>
												</c:if>											
											</c:forEach>
											<c:if test ="${found eq 0 }"><span id="pusCount" class="RecordCount">0 </span></c:if>)
											<span class="selected"></span><span
											class="icon icon-arrow-down"></span><span
											class="icon icon-arrow-up"></span>
										</a>
									</h4>
								</div>
								<div id="collapseOne" class="panel-collapse collapse in"
									role="tabpanel" aria-labelledby="defineRole"
									aria-expanded="false">
									<div class="panel-body">
										<table class="table custom-table">
											<thead>
												<tr>
													<th>
														<div class="checkbox-without-label">
															<input class ="selectAll" type="checkbox"><label>default</label>
														</div>
													</th>
													<th>Cite No.</th>
													<th>Patent #</th>
													<th>Kind Code</th>
													<th>Issue Date</th>
													<th>Name of Parentee or <br />Applicant of cited
														Document
													</th>
													<th>USPTO Status</th>
												</tr>
											</thead>
											<tbody id = "tbodyAccUSPatent">
											<c:forEach items = "${referenceList}" var = "item">
												<tr>
												<input type = "hidden" class="flowId" value = "${item.referenceFlowId}"/>
													<td>
														<div class="checkbox-without-label">
															<input class ="selectOne" type="checkbox"><label>default</label>
														</div>
													</td>
													<td>${item.citeId }</td>
													<td>${item.publicationNo }</td>
													<td>${item.kindCode }</td>
													<td><bbx:date dateFormat="MMM dd, yyyy" date="${item.publicationDate}" /></td>
													<td>${item.applicantName }</td>
													<td class="usptoStatus">${item.refFlowStatus}/${item.refFlowSubStatus}</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<div class="panel panel-default">
								<div class="panel-heading" role="tab">
									<h4 class="panel-title">
										<a  id = "accordianUSPub" class="collapsed" role="button" data-toggle="collapse"
											data-parent="#accordion" href="#collapseThree"
											aria-expanded="false" aria-controls="collapseThree"> US
											Publication 
											<%-- <c:choose>
												<c:when test = "${countMap['US_PUBLICATION'] eq 0}">
													(0)
												</c:when>
												<c:otherwise>
												(${countMap['US_PUBLICATION']})
												</c:otherwise>
											</c:choose> --%>
											(<c:set var = "found" value = "0"/>
											<c:forEach items = "${countMap}" var ="entry" varStatus ="loop">
												<c:if test = "${entry.key eq 'US_PUBLICATION' }">
													<span id="usPubCount" class="RecordCount">${countMap['US_PUBLICATION']}</span>
													<c:set var = "found" value = "1"></c:set>
												</c:if>											
											</c:forEach>
											<c:if test ="${found eq 0 }"><span id="usPubCount" class="RecordCount">0 </span></c:if>)
											<span class="selected"></span><span
											class="icon icon-arrow-down"></span><span
											class="icon icon-arrow-up"></span>
										</a>
									</h4>
								</div>
								<div id="collapseThree" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="accessProfile"
									aria-expanded="false">
									<div class="panel-body">
										<table class="table custom-table">
											<thead>
												<tr>
													<th>
														<div class="checkbox-without-label">
															<input class="selectAll" type="checkbox"><label>default</label>
														</div>
													</th>
													<th>Cite No.</th>
													<th>Publication No.</th>
													<th>Kind Code</th>
													<th>Publication Date</th>
													<th>Name of Patentee or  <br />Applicant of cited
														Document
													</th>
													<th>USPTO Status</th>
												</tr>
											</thead>
											<tbody id = "tbodyAccUSPub">
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<div class="panel panel-default">
								<div class="panel-heading" role="tab">
									<h4 class="panel-title">
										<a id = "accordianForeign" class="collapsed" role="button" data-toggle="collapse"
											data-parent="#accordion" href="#collapseFour"
											aria-expanded="false" aria-controls="collapseFour">
											Foreign
										<%-- 	<c:choose>
												<c:when test = " ${countMap['FP'] eq 0}">
													(0)
												</c:when>
												<c:otherwise>
												(${countMap['FP']})
												</c:otherwise>
											</c:choose> --%>
											(<c:set var = "found" value = "0"/>
											<c:forEach items = "${countMap}" var ="entry" varStatus ="loop">
												<c:if test = "${entry.key eq 'FP' }">
													<span id="fpCount" class="RecordCount">${countMap['FP']}</span>
													<c:set var = "found" value = "1"></c:set>
												</c:if>											
											</c:forEach>
											<c:if test ="${found eq 0}"><span id="fpCount" class="RecordCount">0 </span></c:if>)
											<span class="selected"></span><span
											class="icon icon-arrow-down"></span><span
											class="icon icon-arrow-up"></span>
										</a>
									</h4>
								</div>
								<div id="collapseFour" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="dataAccess"
									aria-expanded="false">
									<div class="panel-body">
										<table class="table custom-table">
											<thead>
												<tr>
													<th>
														<div class="checkbox-without-label">
															<input class="selectAll" type="checkbox"><label>default</label>
														</div>
													</th>
													<th>Cite No.</th>
													<th>Foreign Document No.</th>
													<th>Country Code</th>
													<th>Kind Code</th>
													<th>Publication Date</th>
													<th>Name of Patentee or  <br />Applicant of cited
														Document
													</th>
													<th>USPTO Status</th>
												</tr>
											</thead>
											<tbody id = "tbodyAccForeign">
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<div class="panel panel-default">
								<div class="panel-heading" role="tab">
									<h4 class="panel-title">
										<a  id = "accordianNPL" class="collapsed" role="button" data-toggle="collapse"
											data-parent="#accordion" href="#collapseFive"
											aria-expanded="false" aria-controls="collapseFour"> NPL 
											<%-- <c:choose>
												<c:when test = " ${countMap['NPL'] eq 0}">
													(0)
												</c:when>
												<c:otherwise>
												(${countMap['NPL']})
												</c:otherwise>
											</c:choose> --%>
											(<c:set var = "found" value = "0"/>
											<c:forEach items = "${countMap}" var ="entry" varStatus ="loop">
												<c:if test = "${entry.key eq 'NPL' }">
													<span id="nplCount" class="RecordCount">${countMap['NPL']}</span>
													<c:set var = "found" value = "1"></c:set>
												</c:if>											
											</c:forEach>
											<c:if test ="${found eq 0 }"><span id="nplCount" class="RecordCount">0 </span></c:if>)
											<span class="selected"></span><span
											class="icon icon-arrow-down"></span><span
											class="icon icon-arrow-up"></span>
										</a>
									</h4>
								</div>
								<div id="collapseFive" class="panel-collapse collapse"
									role="tabpanel" aria-expanded="false">
									<div class="panel-body">

										<table class="table custom-table">
											<thead>
												<tr>
													<th>
														<div class="checkbox-without-label">
															<input class="selectAll" type="checkbox"><label>default</label>
														</div>
													</th>
													<th>Cite No.</th>
													<th>Include name of the author (in CAPITAL LETTERS),
														title of the article (when appropriate),title of the item
														(book, magazine, journal, serial, symposium, catalog,
														etc), date, pages(s),volume-issue number(s), publisher,
														city and/or country where published</th>
													<th>USPTO Status</th>
												</tr>
											</thead>
											<tbody id = "tbodyAccNPL">
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="divider"></div>
					<div class="col-sm-12">
						<div class="form-group  text-left">
						<!-- <div id ="tblTemp"></div> -->
							<button type="button" id="btnCloseReference" class="btn btn btn-cancel">Close</button>
							<button type="button" id ="btnUpdateReference" class="btn btn-submit">Update</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="../scripts/reference-update.jsp"></jsp:include>