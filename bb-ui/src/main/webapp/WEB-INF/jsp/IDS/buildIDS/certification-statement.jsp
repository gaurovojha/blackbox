<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="sourceFileIDS"
	value="${idsCertificationForm.certificateSource eq 'fileIdsCertificate'}"
	scope="request" />
<c:set var="sourceInitiateIDS"
	value="${idsCertificationForm.certificateSource eq 'initiateIdsCertificate'}"
	scope="request" />

<!-- IDS : Certification Statement Contents -->
<div class="modal-dialog" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
		</div>

		<div class="modal-body" id="certification-popup">
			<form:form id="formIdsCertificate" commandName="idsCertificationForm"
				method="post" action="${contextPath}/ids/requestApproval"
				enctype="multipart/form-data">
				<form:hidden path="dbId" />
				<form:hidden path="ids" />
				<form:hidden path="application" />

				<c:set var="certificate" value="${idsCertificationForm}"
					scope="request" />
				<input type="hidden" id="filingFee" value="${certificate.filingFee}" />

				<div class="hanging-stip">
					$<span id="lblFilingFee">${certificate.filingFee}</span> filing
					fees
				</div>
				<div class="text-center">
					<h4>CERTIFICATION STATEMENT</h4>
					<p>Please see 37 CFR 1.97 and 1.98 to make the appropriate
						selection(s):</p>
				</div>

				<div class="ref-section-popup">
					<div id="divErrorCertificationCheck" class="error"><jsp:text /></div>
					<ul class="list-group">
						<li class="list-group-item">
							<div class="checkbox-without-label pull-left">
								<form:checkbox id="chkPriorCited"
									class="certificationCheck mutuxCheck" checkNo="1"
									path="priorCited" />
								<label>&nbsp;</label>
							</div>
							<div class="ref-info">
								<strong>That each item of information contained in the
									information disclosure statement was first cited in any
									communication from a foreign patent office in a counterpart
									foreign application not more than three months prior to the
									filing of the information disclosure statement. See 37 CFR
									1.97(e)(1).</strong>
							</div>
						</li>

						<li class="list-group-item"><strong>OR</strong></li>
						<li class="list-group-item">
							<div class="checkbox-without-label pull-left">
								<form:checkbox id="chkPriorUncited"
									class="certificationCheck mutuxCheck" checkNo="2"
									path="priorUncited" />
								<label>&nbsp;</label>
							</div>
							<div class="ref-info">That no item of information contained
								in the information disclosure statement was cited in a
								communication from a foreign patent office in a counterpart
								foreign application, and, to the knowledge of the person signing
								the certification after making reasonable inquiry, no item of
								information contained in the information disclosure statement
								was known to any individual designated in 37 CFR 1.56(c) more
								than three months prior to the filing of the information
								disclosure statement. See 37 CFR 1.97(e)(2).</div>
						</li>
						<li class="list-group-item">
							<div class="checkbox-without-label pull-left">
								<form:checkbox id="chkCertificationAttached"
									class="certificationCheck addPdf" checkNo="3"
									path="certificationAttached" />
								<label>&nbsp;</label>
							</div>
							<div class="ref-info">
								See attached certification statement. <a
									href="javascript:void()" id="addPdfBtn" style="display: none;"><i><img
										src="${imgPath}/svg/add-pdf.svg" class="icon20x"></i>Add PDF</a>
								<form:input type="file" path="certificationStatement"
									id="browserPdf" class="hide" accept="application/pdf" />
								<div class="error" id="errorAddPdf"><jsp:text /></div>
							</div>
						</li>
						<li class="list-group-item">
							<div class="checkbox-without-label pull-left">
								<form:checkbox id="chkFeeSubmitted" class="certificationCheck"
									path="feeSubmitted" checkNo="4" />
								<label>&nbsp;</label>
							</div>
							<div class="ref-info">The fee set forth in 37 CFR 1.17 (p)
								has been submitted herewith.</div>
						</li>

						<li class="list-group-item">
							<div class="checkbox-without-label pull-left">
								<form:checkbox id="chkCertificationStatementSubmitted"
									class="certificationCheck"
									path="certificationStatementSubmitted" checkNo="5" />
								<label>&nbsp;</label>
							</div>
							<div class="ref-info">A certification statement is not
								submitted herewith.</div>
						</li>
					</ul>
					<c:if test="${sourceInitiateIDS}">
						<div class="form-horizontal">
							<div class="form-group">
								<div class="col-sm-2">
									<label for="inputEmail3" class="control-label">Approver's
										Name: <span class="required">*</span>
									</label>
								</div>
								<div class="col-sm-3">
									<form:select id="ddAttorney" path="attorney"
										class="form-control">
										<form:option value="">Please Select</form:option>
										<c:forEach items="${attorneys}" var="user">
											<form:option value="${user.dbId}"
												regNo="${user.registrationNo}">${user.name}</form:option>
										</c:forEach>
									</form:select>
									<div class="error" id="errorAttorney"><jsp:text /></div>
								</div>
								<div class="col-sm-7">
									<form:textarea path="comments"
										placeholder="comments if any...." class="form-control" />
								</div>
							</div>
						</div>
					</c:if>
					<div class="well text-center">
						<p>SIGNATURE</p>
						<p>A signature of the applicant or representative is required
							in accordance with CFR 1.33, 10.18. Please see CFR 1.4(d) for the
							form of the signature.</p>
					</div>

					<div class="row">
						<div class="col-sm-4">
							<div class="certification-box">
								<div class="uploadfile"></div>
								<label>Signature</label>
								<div id="lblSignature" class="form-control-static"><jsp:text /></div>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="certification-box">
								<label>Date</label>
								<form:hidden path="idsApprovalDate" />
								<div class="form-control-static">${certificate.idsApprovalDate}</div>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="certification-box">
								<label>Registration #</label>
								<form:hidden path="registrationNo" />
								<div id="lblRegistrationNo" class="form-control-static"><jsp:text /></div>
							</div>
						</div>
					</div>
					<div class="bottom-note">This collection of information is
						required by 37 CFR 1.97 and 1.98. The information is required to
						obtain or retain a benefit by the public which is to file (and by
						the USPTO to process) an application. Confidentiality is governed
						by 35 U.S.C. 122 and 37 CFR 1.14. This collection is estimated to
						take 1 hour to complete, including gathering, preparing and
						submitting the completed application form to the USPTO. Time will
						vary depending upon the individual case. Any comments on the
						amount of time you require to complete this form and/or
						suggestions for reducing this burden, should be sent to the Chief
						Information Officer, U.S. Patent and Trademark Office, U.S.
						Department of Commerce, P.O. Box 1450, Alexandria, VA 22313-1450.
						DO NOT SEND FEES OR COMPLETED FORMS TO THIS ADDRESS. SEND TO:
						Commissioner for Patents, P.O. Box 1450, Alexandria, VA
						22313-1450.</div>
				</div>
			</form:form>
		</div>
		<c:if test="${sourceInitiateIDS}">
			<div class="modal-footer">
				<button type="button" class="btn btn-cancel red btnDiscardIDS">Discard
					IDS</button>
				<button type="button" class="btn btn-submit" id="btnRequestApproval">Send
					for Approval</button>
				<div class="pull-right">
					<button class="btn btn-cancel" type="button" id="btnCalculateFee">Re-Calculate
						Fee</button>
					<button class="btn btn-cancel" type="button" id="btnSaveIDS">Save
						for later</button>
				</div>
			</div>
		</c:if>

		<c:if test="${sourceFileIDS}">
			<div class="modal-footer form-inline">
				<button type="button" class="btn btn-cancel"
					onclick="showConfirmationBox(I want to file it my self)">I want to file it my self</button>
				<div class="inline-block selectpicker-submit">
					<select class="selectpicker form-control  btn-submit"
						data-live-search="true" Title="I WANT THE SYSTEM TO FILE">
						<c:forEach var="pki" items="${privatePairKey}">
	        					<option value="${pki.dbID}">${pki.filingPKIName}</option>
	        				</c:forEach>
					</select>
				</div>
			</div>
		</c:if>
	</div>
	<div id="fileIDSMySelf" class="modal custom fade modal-wide fileIDSMySelf" popup-msg" idsId="">
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
</div>


