<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <!-- Pair Audit Modal -->
    
    <div class="modal custom fade" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Pair Audit</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="pairAuditForm">
                        <div class="form-group">
                            <div class="col-sm-12">
                                <label class="control-label">Select Files to Upload</label>
                                <div class="input-group">
                                    <input id="uploadFileAudit" placeholder="File location..." disabled="disabled" class="form-control" />
                                    <div class="fileUpload btn btn-blue input-group-btn">
                                        <span>Browse</span>
                                        <input type="file" id="uploadBtnAudit" class="upload" accept="application/xml"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group"  id="auditDashBoard" class="pairAudiForm">
                            <div class="col-sm-12">
                            <div class="auditFileUploadError has-error"><p id="auditFileUploadErrorMsg" class="data error"></p> </div>
                                <table class="table custom-table single-head-table" id = "auditDateRangeTable">
                                    <thead>
                                        <tr>
                                            <th colspan="2" class="text-center">For Date Range</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr class="odd">
                                           <td class="text-center" id="initialDate" class= "data"></td>
										   <td class="text-center" id="finalDate" class= "data"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer" id="auditFooter" class="pairAuditForm">
                    <button type="button" class="btn btn-cancel auditCancel" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-submit" id="auditSubmit">Submit</button>
                </div>
            </div>
        </div>
</div>

	