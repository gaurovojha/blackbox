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
                        <a class="view-ref-btn" href="ids-reference-record-cited.html">View Reference Record</a>
                    </div>
                </div>
            </div>
            
              <div class="text-right margin-btm-10">
               <button type="button" class="btn disabled" id="donotBtn">Do Not File</button>
               <button type="button" class="btn disabled" id="dropBtn">Drop from this IDS</button>
               <button type="button" class="btn btn-submit" id="selfCiteBtn">Add Self-Citation</button>
             </div>
            
            
             <div class="pull-left">
              <a class="btn btn-submit btn-link" data-target="#patentsConfirm" data-toggle="modal">Approve</a>
              <button type="button" class="btn btn-submit">Request Changes</button>
            </div>
            <div class="pull-right">
              <button type="button" class="btn btn-cancel">Exit</button>
              <button type="button" class="btn btn-cancel">Do not file</button>
            </div>
            
            
            
            
  </div>