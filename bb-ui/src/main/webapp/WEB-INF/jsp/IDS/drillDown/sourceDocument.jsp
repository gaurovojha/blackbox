<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<div class="col-sm-5 trifolder-source">
	<div id="sourceDoc">
	    <div class="clearfix document-reader">
			<span class="triangle-corner"></span>
		    <h3>Source Document</h3>
		    <div class="col-sm-12">
		    	<a href="#"><i><img src="${pathImg}/attachment.svg" class="icon16x"></i></a>
				<a href="ids-review.html"><i><img src="${pathImg}/review-doc.svg" class="icon16x"></i></a>
			</div>
		    <div class="form-horizontal">
		    	<div class="col-sm-12">
		        	<label class="control-label">Document Description:</label>
		            <div class="form-control-static" id = "docDescriptionSource"></div>
		        </div>
		    </div>

            <div class="form-horizontal clearfix">
                <div class="col-sm-6">
                    <label class="control-label">Mailing Date</label>
                    <div class="form-control-static" id = "mailDateSource"></div>
                </div>
                <div class="col-sm-6">
                    <label class="control-label">Family ID</label>
                    <div class="form-control-static" id ="familyIdSource"></div>
                </div>
                <div class="col-sm-6">
                    <label class="control-label">Jurisdiction</label>
                    <div class="form-control-static" id="jurisdictionSource"></div>
                </div>
                <div class="col-sm-6">
                    <label class="control-label">Application #</label>
                    <div class="form-control-static" id="applicationNoSource"></div>
                </div>
           </div><!--form-horizontal end-->
	   </div>
       <div class="clearfix document-reader-duplicate">
	        <div class="form-horizontal">
	           	<h4 class="heading">Other entered References</h4>
	          		<div class="content grey mCustomScrollbar _mCS_1">
	          			<div tabindex="0" style="max-height: none;" id="mCSB_1" class="mCustomScrollBox mCS-light mCSB_vertical mCSB_inside">
	          				<div id="mCSB_1_container" class="mCSB_container" style="position: relative; top: 0px; left: 0px;" dir="ltr">
	          				</div>	
	          				<div style="display: block;" id="mCSB_1_scrollbar_vertical" class="mCSB_scrollTools mCSB_1_scrollbar mCS-light mCSB_scrollTools_vertical">
		          				<div class="mCSB_draggerContainer">
			          				<div id="mCSB_1_dragger_vertical" class="mCSB_dragger" style="position: absolute; min-height: 30px; display: block; height: 120px; max-height: 198px; top: 0px;" oncontextmenu="return false;">
			          					<div style="line-height: 30px;" class="mCSB_dragger_bar"></div>
			          				</div>
			          				<div class="mCSB_draggerRail"></div>
		          				</div>
	          				</div>
	          			</div>
	          		</div>
           </div><!--form-horizontal end-->
  	   </div>
   </div>
 </div>
