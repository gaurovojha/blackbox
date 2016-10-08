<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Family</title>
</head>
<body>

<%
	String context = request.getContextPath();
	String js = context + "/assets/js";
	String images = context+"/assets/images";
%>

	    <div class="main-content container">
        <div class="notTopinfo">
            <div class="row">
                <div class="col-sm-2">
                    <label class="control-label">Family ID </label>
                    <div class="form-control-static"><a href="#" data-target="#family" data-toggle="modal">F292456</a></div>
                </div>
                <div class="col-sm-2">
                    <label class="control-label">Jurisdiction</label>
                    <div class="form-control-static">US</div>
                </div>
                <div class="col-sm-2">
                    <label class="control-label">Application #</label>
                    <div class="form-control-static">14/800,234</div>
                </div>  
                <div class="col-sm-2">
                    <label class="control-label">Attorney Docket #  </label>
                    <div class="form-control-static">63109-21010.01</div>
                </div>  
                
                <div class="col-sm-2">
                    <label class="control-label">&nbsp;</label>
                    <div class="form-control-static text-right">
                    <a href="javascript:void(0)" class="margin-rt-10">View Ref. Flow Rules</a>
                    </div>
                </div> 
                <div class="col-sm-2">
                    <label class="control-label">&nbsp;</label>
                    <div class="form-control-static">
                    <a href="javascript:void(0)" class="pull-right"><img src="<%=images%>/svg/download.svg">Download</a>
                    </div>
                </div>   
            </div>
        </div>
        
        <div class="row">
        	<div class="pull-right form-control-static">
                <a href="javascript:void(0)"><i><img src="<%=images%>/svg/search.svg" title="Search Reference" class="icon20x"></i></a>
            </div>
            <ul class="nav nav-tabs custom-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#citedIDS" role="tab" data-toggle="tab">Cited in IDS <span>(70)</span></a></li>
<!--                 <li role="presentation"><a href="#unCited" role="tab" data-toggle="tab">Uncited <span>(10)</span></a></li>
                <li role="presentation"><a href="#examinerCited" role="tab" data-toggle="tab">Examiner Cited <span>(3)</span></a></li>

                <li role="presentation"><a href="#citedParent" role="tab" data-toggle="tab">Cited in Parent <span>(0)</span></a></li>
                <li role="presentation"><a href="#doNotFile" role="tab" data-toggle="tab">Do Not File <span>(2)</span></a></li>
                <li role="presentation"><a href="#dropped" role="tab" data-toggle="tab">Deleted <span>(2)</span></a></li> -->
            </ul>
        	
        </div>
        
        <div class="tab-content reference-cited">
                <div role="tabpanel" class="tab-pane active" id="citedIDS">
                    <%-- <jsp:include page="reference-flow-cited.jsp"></jsp:include> --%>
                </div>
                <div role="tabpanel" class="tab-pane" id="unCited">
                    <div class="row">
                        <div class="col-sm-8 left-col">
                            <div class="form-horizontal">
                                <div class="row margin-btm10 text-right">
                                    <div class="col-sm-12 reference-cited-btns">
                                        <button class="btn disabled" id="uncitedDonot">Do Not File</button>
                                        <button class="btn btn-submit" id="uncitedIds">Initiate IDS</button>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-12">
                                        <div class="panel-group user-management" id="accordion" role="tablist" aria-multiselectable="true">
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
    			      	                                <span></span>
                                                        <a role="button"  class="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo" class="collapsed">Patents (8)<span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
    			                                        </a>
                                                    </h4>
                                                </div>
                                                <div id="collapseTwo" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="defineRole" aria-expanded="false">
                                                    <div class="panel-body">
                                                        <div class="row">
                                                            <div class="col-sm-9">
                                                                   <div class="inner-search mbtm0 trisearch">
                                                                        <div class="input-group">
                                                                            <span class="input-group-btn">
                                                                                <button class="search"><span class="icon icon-search-inner"></span>
                                                                                </button>
                                                                            </span>
                                                                            <input type="text" placeholder="Search">
                                                                        </div>
                                                                    </div> 
                                                                </div>
                                                                <div class="col-sm-3">
                                                                    <label class="control-label pull-right">*IDS in Progress</label>
                                                                </div>
                                                        </div>
                                                        <table class="table custom-table mTop10 clickable">
                                                            <thead>
                                                                <tr>
                                                                    <th>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox"><label>default</label>
                                                                        </div>
                                                                    </th>
                                                                    <th>Cite No.</th>
                                                                    <th>Jurisdiction</th>
                                                                    <th>Patent No.</th>
                                                                    <th>Kind Code</th>
                                                                    <th>Patentee/Applicant</th>
                                                                    <th>View</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr class="odd active" onclick="showSourceDoc()">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox" id="uncitedCheckbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>1</td>
                                                                    <td>US</td>
                                                                    <td><sup>*</sup>5,983,623</td>
                                                                    <td></td>
                                                                    <td>Reagan et al.</td>
                                                                    
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                <tr class="borderTB hiddenRow active">
                                                                    <td colspan="2"><strong>Ref. Entered by.</strong>
                                                                        <br/>System, Feb 26, 2015</td>
                                                                    <td colspan="4">
                                                                        <strong>Comments </strong>
                                                                         <a href="javascript:void(0)" onclick="simpleMsg()"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                                                         <a href="javascript:void(0)"><i><img src="images/svg/delete.svg" class="icon16x"></i></a>
                                                                        <br/> display Responsible Paralegal - Lisa Ross</td>
                                                                    <td>
                                                                        <a href="javascript:void(0)">PDF</a>
                                                                        <a href="javascript:void(0)"><i><img src="images/svg/delete.svg" class="icon16x"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <tr class="even">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox" id="uncitedCheckbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>2</td>
                                                                    <td>US</td>
                                                                    <td>6,134,824</td>
                                                                    <td></td>
                                                                    <td>Lighthouse</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                <tr class="odd">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox" id="uncitedCheckbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>3</td>
                                                                    <td>US</td>
                                                                    <td>20090239799</td>
                                                                    <td></td>
                                                                    <td>Bill McIntyre</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                <tr class="even">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox" id="uncitedCheckbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>4</td>
                                                                    <td>US</td>
                                                                    <td>20130900820</td>
                                                                    <td></td>
                                                                    <td>Agassi et al.</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                <tr class="odd">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox" id="uncitedCheckbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>5</td>
                                                                    <td>US</td>
                                                                    <td>20140783459</td>
                                                                    <td></td>
                                                                    <td>Chauhan et al.</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                 <tr class="even">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox" id="uncitedCheckbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>6</td>
                                                                    <td>CN</td>
                                                                    <td>102878834</td>
                                                                    <td>WA</td>
                                                                    <td>Huawei</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                 <tr class="odd">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox" id="uncitedCheckbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>7</td>
                                                                    <td>EP</td>
                                                                    <td><sup>*</sup>3402267</td>
                                                                    <td>B1</td>
                                                                    <td>Alcatel Lucent</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                 <tr class="even">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox" id="uncitedCheckbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>8</td>
                                                                    <td>KR</td>
                                                                    <td>1020080007358</td>
                                                                    <td>A</td>
                                                                    <td>Hynix</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
                                    			        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                                    			          NPL (2) <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                                    			        </a>
                                    			     </h4>
                                                </div>
                                                <div id="collapseFour" class="panel-collapse collapse" role="tabpanel" aria-expanded="false">
                                                    <div class="panel-body">
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 right-col">
                            <div class="clearfix document-reader" id="uncitedSourceDoc">
                                <span class="triangle-corner"></span>
                                <p class="title">Source Document</p>
                                <span class="doc-action-icons">
                                    <a href="javascript:void(0)" title="PDF"><i><img src="images/svg/pdf.svg" class="icon16x"></i></a>
                                    <a href="edit-reference.html" title="Edit References"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                </span>
                                <div class="form-horizontal">
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-12">
                                                <label class="control-label">Document Description:</label>
                                                <div class="form-control-static"> Non-final Rejection</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Mailing Date</label>
                                                <div class="form-control-static"> Feb 25, 2016</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Family ID</label>
                                                <div class="form-control-static">F292456</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Jurisdiction</label>
                                                <div class="form-control-static">US</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Application #</label>
                                                <div class="form-control-static">14/832,004</div>
                                            </div>
                                        </div>
                                        
                                    </div>
                                </div>

                            </div>


                            <div class="clearfix document-reader-duplicate" >
                                
                                <div class="form-horizontal">
                                    <div class="form-group">                                        
                                        <div class="col-sm-12">
                                            <div>
                                                <p class="heading">Duplicate Sources</p>
                                            </div>
                                            <span class="doc-action-icons">
                                                <a href="javascript:void(0)">PDF</a>
                                                <a href="javascript:void(0)"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                            </span>
                                            <div class="col-sm-12">
                                                <label class="control-label">Document Description:</label>
                                                <div class="form-control-static">Non-final Rejection</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Mailing Date</label>
                                                <div class="form-control-static">Mar 20, 2016</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Family ID</label>
                                                <div class="form-control-static"> F2340769</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Jurisdiction</label>
                                                <div class="form-control-static">US</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Application #</label>
                                                <div class="form-control-static">13/367,505</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <a href="#">1 More...</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="clearfix document-reader" id="uncitedSelfDoc">
                                <span class="triangle-corner"></span>
                                <p class="title">Self-Citation</p>
                                <span class="doc-action-icons">
                                    <a href="javascript:void(0)"><i><img src="images/svg/edit.svg" class="icon16x"></i> Review</a>
                                </span>
                                <div class="form-horizontal">
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-12">
                                                <label class="control-label">Reference Entered By</label>
                                                <div class="form-control-static">Lindsay Mazzola</div>
                                            </div>
                                        </div>
                                        <div class="col-sm-12">
                                            <div class="col-sm-12">
                                                <label class="control-label">Date</label>
                                                <div class="form-control-static">Oct 10, 2014</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane" id="examinerCited">
                    <div class="row">
                        <div class="col-sm-8 left-col">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="col-sm-12">
                                        <div class="panel-group user-management" id="accordion" role="tablist" aria-multiselectable="true">
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
    			                                     	<span></span>
                                                        <a role="button" class="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseOne" class="collapsed">Patents (2)<span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
    			                                        </a>
                                                    </h4>
                                                </div>
                                                <div id="collapseThree" class="panel-collapse in" role="tabpanel" aria-labelledby="defineRole" aria-expanded="false">
                                                    <div class="panel-body">
                                                        <div class="inner-search trisearch">
                                                            <div class="input-group">
                                                                <span class="input-group-btn">
                                                                    <button class="search"><span class="icon icon-search-inner"></span>
                                                                    </button>
                                                                </span>
                                                                <input type="text" placeholder="Search">
                                                            </div>
                                                        </div>
                                                        <table class="table custom-table mTop10 clickable">
                                                            <thead>
                                                                <tr>
                                                                    <th>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox"><label>default</label>
                                                                        </div>
                                                                    </th>
                                                                    <th>Cite No.</th>
                                                                    <th>Jurisdiction</th>
                                                                    <th>Patent No.</th>
                                                                    <th>Kind Code</th>
                                                                    <th>Patentee/Applicant</th>
                                                                    <th>View</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr class="odd active">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>1</td>
                                                                    <td>US</td>
                                                                    <td>6,531,226</td>
                                                                    <td></td>
                                                                    <td>Qualcomm</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                <!--<tr class="borderTB hiddenRow active">
                                                                    <td colspan="2"><strong>Ref. Entered by.</strong>
                                                                        <br/>John Moore, Oct 10, 2014</td>
                                                                    <td colspan="3">
                                                                        <strong>Comments </strong>
                                                                         <a href="javascript:void(0)"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                                                         <a href="javascript:void(0)"><i><img src="images/svg/delete.svg" class="icon16x"></i></a>
                                                                        <br/> IDS prepared by Joanne Lindsay</td>
                                                                    <td>
                                                                        <a href="javascript:void(0)">PDF</a>
                                                                        <a href="javascript:void(0)"><i><img src="images/svg/delete.svg" class="icon16x"></i></a>
                                                                    </td>
                                                                </tr>-->
                                                                <tr class="odd">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>2</td>
                                                                    <td>US</td>
                                                                    <td>5,098,456</td>
                                                                    <td></td>
                                                                    <td>Hill et al.</td>
                                                                    <td><a href="javascript:void(0)">More...</a></td>
                                                                </tr>
                                                            </tbody>
                                                        </table> 
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
                                    			        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                                    			          NPL (1)  <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                                    			        </a>
                                    			    </h4>
                                                </div>
                                                <div id="collapseFour" class="panel-collapse collapse" role="tabpanel" aria-expanded="false">
                                                    <div class="panel-body">

                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 right-col">
                            <div class="clearfix document-reader">
                                <span class="triangle-corner"></span>
                                <p class="title">Source Document</p>
                                <span class="doc-action-icons">
                                   <a href="javascript:void(0)" title="PDF"><i><img src="images/svg/pdf.svg" class="icon16x"></i></a>
                                    <a href="edit-reference.html" title="Edit References"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                </span>
                                <div class="form-horizontal">
                                        <div>
                                            <div class="col-sm-12">
                                                <label class="control-label">Document Description:</label>
                                                <div class="form-control-static">Non-final Rejection</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Mailing Date</label>
                                                <div class="form-control-static">Nov 20, 2015</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Family ID</label>
                                                <div class="form-control-static">F292456</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Jurisdiction</label>
                                                <div class="form-control-static">US</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Application #</label>
                                                <div class="form-control-static">14/800,234</div>
                                            </div>
                                            <div class="col-sm-12 devider"></div>
                                        </div>
                                        
                                </div>
                                

                            </div>
                            <div class="clearfix document-reader-duplicate">
                                
                                    <div class="form-horizontal">
                                        <div class="form-group">
                                            
                                            <div class="col-sm-12">
                                                <div>
                                                    <p class="heading">Reference Flow</p>
                                                </div>
                                                <div class="col-sm-12 mTop10">
                                                    <a href="#">Cited in IDS (Dec 10, 2015) in US 14/832,004 (F292456) <i><img src="images/svg/external-link.svg" class="icon16x"></i></a>
                                                </div>
                                                <div class="col-sm-12 mTop10">
                                                    <a href="#">Uncited in US 13/367,505 (F2340769) <i><img src="images/svg/external-link.svg" class="icon16x"></i></a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                        </div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane" id="citedParent">
                    <div class="row">
                        <div class="col-sm-8 left-col">
                                    
                            <div class="form-horizontal">
                                <div class="row">
                                    <div class="reference-cited-btns">
                                        <div class="tab-info-text col-sm-8">
                                            <span><i><img src="images/svg/info.svg" class="icon16x"></i></span> <p>Please review the request &amp; create an application record. You have received this request as the sender does not.</p> 
                                        </div>
                                        <div class="col-sm-4 text-right">
                                            <button class="btn disabled">Do Not File</button>
                                            <button class="btn btn-submit">Initiate IDS</button> 
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-12">
                                        <div class="panel-group user-management" id="accordion2" role="tablist" aria-multiselectable="true">
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
                                                        <span></span>
                                                        <a role="button"  class="collapse" data-toggle="collapse" data-parent="#accordion2" href="#collapseFive" aria-expanded="false" aria-controls="collapseTwo" class="collapsed">Patents (0)<span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                                                        </a>
                                                    </h4>
                                                </div>
                                                <div id="collapseFive" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="defineRole" aria-expanded="false">
                                                    <div class="panel-body">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
                                                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion2" href="#collapseSix" aria-expanded="false" aria-controls="collapseSix">
                                                          NPL (0) <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                                                        </a>
                                                     </h4>
                                                </div>
                                                <div id="collapseSix" class="panel-collapse collapse" role="tabpanel" aria-expanded="false">
                                                    <div class="panel-body">
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 right-col">
                            <div class="clearfix document-reader">
                                <span class="triangle-corner"></span>
                                <p class="title">Source Document</p>
                                <span class="doc-action-icons">
                                    <a href="javascript:void(0)" title="PDF"><i><img src="images/svg/pdf.svg" class="icon16x"></i></a>
                                    <a href="edit-reference.html" title="Edit References"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                </span>
                                <div class="form-horizontal">
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-12">
                                                <label class="control-label">Document Description:</label>
                                                <div class="form-control-static">Requirement for Restriction/Election</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Mailing Date</label>
                                                <div class="form-control-static">Oct 10, 2014</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Family ID</label>
                                                <div class="form-control-static">F123456</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Jurisdiction</label>
                                                <div class="form-control-static">CH</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Application #</label>
                                                <div class="form-control-static">123456789123356</div>
                                            </div>
                                        </div>
                                        
                                    </div>
                                </div>

                            </div>

                             <div class="clearfix document-reader-duplicate">
                                
                                <div class="form-horizontal">
                                    <div class="form-group">
                                        
                                        <div class="col-sm-12">
                                            <div>
                                                <p class="heading">Duplicate Sources</p>
                                            </div>
                                            <span class="doc-action-icons">
                                                <a href="javascript:void(0)">PDF</a>
                                                <a href="javascript:void(0)"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                            </span>
                                            <div class="col-sm-12">
                                                <label class="control-label">Document Description:</label>
                                                <div class="form-control-static">Requirement for Restriction/Election</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Mailing Date</label>
                                                <div class="form-control-static">Oct 10, 2014</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Family ID</label>
                                                <div class="form-control-static">F123456</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Jurisdiction</label>
                                                <div class="form-control-static">CH</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Application #</label>
                                                <div class="form-control-static">123456789123356</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <a href="#">20 More...</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane" id="doNotFile">
                    <div class="row">
                        <div class="col-sm-8 left-col">
                            <div class="form-horizontal">
                                <div class="row margin-btm10 text-right">
                                    <div class="col-sm-12 reference-cited-btns">
                                        <button class="btn btn-submit disabled">Include in IDS</button>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-12">
                                        <div class="panel-group user-management" id="accordion3" role="tablist" aria-multiselectable="true">
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
                                                        <span></span>
                                                        <a role="button"  class="collapse" data-toggle="collapse" data-parent="#accordion3" href="#collapseSeven" aria-expanded="false" aria-controls="collapseTwo" class="collapsed">Patents (2)<span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                                                        </a>
                                                    </h4>
                                                </div>
                                                <div id="collapseSeven" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="defineRole" aria-expanded="false">
                                                    <div class="panel-body">
                                                        <div class="inner-search mbtm0 trisearch">
                                                            <div class="input-group">
                                                                <span class="input-group-btn">
                                                                    <button class="search"><span class="icon icon-search-inner"></span>
                                                                    </button>
                                                                </span>
                                                                <input type="text" placeholder="Search">
                                                            </div>
                                                        </div>
                                                        <table class="table custom-table mTop10 clickable">
                                                            <thead>
                                                                <tr>
                                                                    <th>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox"><label>default</label>
                                                                        </div>
                                                                    </th>
                                                                    <th>Cite No.</th>
                                                                    <th>Jurisdiction</th>
                                                                    <th>Patent No.</th>
                                                                    <th>Kind Code</th>
                                                                    <th>Patentee/Applicant</th>
                                                                    <th>View</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr class="odd active">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>1</td>
                                                                    <td>US</td>
                                                                    <td>4,123,001</td>
                                                                    <td></td>
                                                                    <td>Laura Martino</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                <tr class="borderTB hiddenRow active">
                                                                    <td colspan="2"><strong>Ref. dropped by:</strong>
                                                                        <br/>Joanna Lindsay  Dec 1, 2015</td>
                                                                    <td colspan="4">
                                                                        <strong>Comments: </strong>
                                                                         <a href="javascript:void(0)"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                                                         <a href="javascript:void(0)"><i><img src="images/svg/delete.svg" class="icon16x"></i></a>
                                                                        <br/> </td>
                                                                    <td>
                                                                        <a href="javascript:void(0)">PDF</a>
                                                                        <a href="javascript:void(0)"><i><img src="images/svg/delete.svg" class="icon16x"></i></a>
                                                                    </td>
                                                                </tr>
                                                               
                                                            </tbody>
                                                        </table>
                                                        <!--pagination control-->
                                                        <nav class="pagination-control hide">
                                                            <ul class="pagination">
                                                                <li>
                                                                    <a href="#" aria-label="Previous">
                                                                        <span aria-hidden="true"><span class="glyphicon glyphicon-menu-left"></span></span>
                                                                    </a>
                                                                </li>
                                                                <li><a href="#">1</a></li>
                                                                <li><a href="#">2</a></li>
                                                                <li><a href="#">3</a></li>
                                                                <li><a href="#">4</a></li>
                                                                <li><a href="#">5</a></li>
                                                                <li>
                                                                    <a href="#" aria-label="Next">
                                                                        <span aria-hidden="true"><span class="glyphicon glyphicon-menu-right"></span></span>
                                                                    </a>
                                                                </li>
                                                            </ul>
                                                            <div class="form-inline">
                                                                <span>Showing</span>
                                                                <select class="form-control">
                                                                    <option>10</option>
                                                                    <option>20</option>
                                                                    <option>30</option>
                                                                    <option>40</option> 
                                                                </select>
                                                                <span>per page</span>
                                                            </div>
                                                        </nav>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
                                                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion3" href="#collapseEight" aria-expanded="false" aria-controls="collapseEight">
                                                          NPL (1) <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                                                        </a>
                                                     </h4>
                                                </div>
                                                <div id="collapseEight" class="panel-collapse collapse" role="tabpanel" aria-expanded="false">
                                                    <div class="panel-body">
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 right-col">
                            <div class="clearfix document-reader">
                                <span class="triangle-corner"></span>
                                <p class="title">Source Document</p>
                                <span class="doc-action-icons">
                                    <a href="javascript:void(0)" title="PDF"><i><img src="images/svg/pdf.svg" class="icon16x"></i></a>
                                    <a href="edit-reference.html" title="Edit References"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                </span>
                                <div class="form-horizontal">
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="col-sm-12">
                                                <label class="control-label">Document Description:</label>
                                                <div class="form-control-static">Search Report</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Mailing Date</label>
                                                <div class="form-control-static"> Jul 22, 2015</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Family ID</label>
                                                <div class="form-control-static"> F292456</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Jurisdiction</label>
                                                <div class="form-control-static">WO</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Application #</label>
                                                <div class="form-control-static">PCT/JP15/04534</div>
                                            </div>
                                        </div>                                       
                                    </div>
                                </div>

                            </div>
                             <div class="clearfix document-reader-duplicate">
                                
                                <div class="form-horizontal">
                                    <div class="form-group">
                                        
                                        <div class="col-sm-12">
                                            <div>
                                                <p class="heading">Duplicate Sources</p>
                                            </div>
                                            <span class="doc-action-icons">
                                                <a href="javascript:void(0)">PDF</a>
                                                <a href="javascript:void(0)"><i><img src="images/svg/edit.svg" class="icon16x"></i></a>
                                            </span>
                                            <div class="col-sm-12">
                                                <label class="control-label">Document Description:</label>
                                                <div class="form-control-static">Non-final Rejection</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Mailing Date</label>
                                                <div class="form-control-static"> Dec 2, 2015</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Family ID</label>
                                                <div class="form-control-static">F292456</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Jurisdiction</label>
                                                <div class="form-control-static">US</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label class="control-label">Application #</label>
                                                <div class="form-control-static">14/832,004</div>
                                            </div>
                                            <div class="col-sm-6">
                                                <a href="#">20 More...</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane" id="dropped">
                    <div class="row">
                        <div class="col-sm-8 left-col">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="col-sm-12">
                                        <div class="panel-group user-management" id="accordion4" role="tablist" aria-multiselectable="true">
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
                                                        <span></span>
                                                        <a role="button"  class="collapse" data-toggle="collapse" data-parent="#accordion4" href="#collapseNine" aria-expanded="false" aria-controls="collapseNine" class="collapsed">Patents (2)<span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                                                        </a>
                                                    </h4>
                                                </div>
                                                <div id="collapseNine" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="defineRole" aria-expanded="false">
                                                    <div class="panel-body">
                                                        <div class="inner-search mbtm0 trisearch">
                                                            <div class="input-group">
                                                                <span class="input-group-btn">
                                                                    <button class="search"><span class="icon icon-search-inner"></span>
                                                                    </button>
                                                                </span>
                                                                <input type="text" placeholder="Search">
                                                            </div>
                                                        </div> 
                                                        <table class="table custom-table mTop10 clickable">
                                                            <thead>
                                                                <tr>
                                                                    <th>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox"><label>default</label>
                                                                        </div>
                                                                    </th>
                                                                    <th>Cite No.</th>
                                                                    <th>Jurisdiction</th>
                                                                     <th>Publication #</th>
                                                                    <th>Kind Code</th>
                                                                    <th>Patentee/Applicant</th>
                                                                    <th>View</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr class="odd active">
                                                                    <td>
                                                                        <div class="checkbox-without-label">
                                                                            <input type="checkbox"><label>default</label>
                                                                        </div>
                                                                    </td>
                                                                    <td>1</td>
                                                                    <td>US</td>
                                                                    <td>5,223,012</td>
                                                                    <td></td>
                                                                    <td>Lighthouse</td>
                                                                    <td><a href="javascript:void(0)" class="showMore">More...</a></td>
                                                                </tr>
                                                                <tr class="borderTB hiddenRow active">
                                                                    <td colspan="2"><strong>Ref. Deleted by.</strong>
                                                                        <br/>Lisa Ross, Aug 10, 2015</td>
                                                                    <td colspan="3">
                                                                        <strong>Comments </strong>
                                                                        <br/></td>
                                                                    <td>
                                                                        <a href="javascript:void(0)">PDF</a>
                                                                    </td>
                                                                </tr>
                                                                
                                                            </tbody>
                                                        </table>
                                                        <!--pagination control-->
                                                        <nav class="pagination-control hide">
                                                            <ul class="pagination">
                                                                <li>
                                                                    <a href="#" aria-label="Previous">
                                                                        <span aria-hidden="true"><span class="glyphicon glyphicon-menu-left"></span></span>
                                                                    </a>
                                                                </li>
                                                                <li><a href="#">1</a></li>
                                                                <li><a href="#">2</a></li>
                                                                <li><a href="#">3</a></li>
                                                                <li><a href="#">4</a></li>
                                                                <li><a href="#">5</a></li>
                                                                <li>
                                                                    <a href="#" aria-label="Next">
                                                                        <span aria-hidden="true"><span class="glyphicon glyphicon-menu-right"></span></span>
                                                                    </a>
                                                                </li>
                                                            </ul>
                                                            <div class="form-inline">
                                                                <span>Showing</span>
                                                                <select class="form-control">
                                                                    <option>10</option>
                                                                    <option>20</option>
                                                                    <option>30</option>
                                                                    <option>40</option> 
                                                                </select>
                                                                <span>per page</span>
                                                            </div>
                                                        </nav>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="panel panel-default">
                                                <div class="panel-heading" role="tab">
                                                    <h4 class="panel-title">
                                                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion4" href="#collapseTen" aria-expanded="false" aria-controls="collapseTen">
                                                          NPL (1) <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                                                        </a>
                                                     </h4>
                                                </div>
                                                <div id="collapseTen" class="panel-collapse collapse" role="tabpanel" aria-expanded="false">
                                                    <div class="panel-body">
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
       </div>
	

</body>
</html>