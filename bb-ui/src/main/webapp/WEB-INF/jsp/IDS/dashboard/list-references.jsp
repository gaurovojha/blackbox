<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
         
         <div class="panel-group user-management" id="accordion" role="tablist" aria-multiselectable="true">
            <div class="panel panel-default">
                <div class="panel-heading attroney " role="tab" id="UsPatents">
                    <h4 class="panel-title">
                    <span></span>
                    <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                      US Patent (${refCounts['PUS']})<span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                    </a>
                  </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="UsPatents">
                    <div class="panel-body">
         					<div class="previewIDS"></div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="USPatendsPublication">
                    <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseThree">
                      US Publication (${refCounts['US_PUBLICATION']}) <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                    </a>
                  </h4>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="USPatendsPublication">
                    <div class="panel-body">
             			<div class="previewIDS"></div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="foreignPatents">
                    <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                      Foreign Patents (${refCounts['FP']}) <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                    </a>
                  </h4>
                </div>
                <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="foreignPatents">
                    <div class="panel-body">
                        	<div class="previewIDS"></div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="npl">
                    <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                      NPL (${refCounts['NPL']}) <span class="selected"></span><span class="icon icon-arrow-down"></span><span class="icon icon-arrow-up"></span>
                    </a>
                  </h4>
                </div>
                <div id="collapseFour" class="panel-collapse collapse" role="tabpanel" aria-labelledby="npl">
                    <div class="panel-body">
	                 <div class="previewIDS"></div>
                    </div>
                </div>
            </div>

        </div>
