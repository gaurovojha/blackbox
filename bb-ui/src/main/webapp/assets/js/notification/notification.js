(function () {

    "use strict";
   
    var selectedContainer ,newComponentToBeAddded, bussinessRuleJson, deleteRuleId, numberOfLevel, canceRuleId;
    
    var notificationId = $('#notificationId').attr('name');

    window.editBussinessRule=function (id){
        window.location.href='../notification/editBussinessRules?notificationId='+id;
    }

    function ajaxCall(ajaxUrl, ajaxData,contenType) {
        return $.ajax({
            url: ajaxUrl,
            type: "POST",
            data: ajaxData,
            dataType: "JSON",
            async: false,
            contentType: (contenType !== undefined)?contenType:"application/json"

        });

    }

    $('select#roles').on('change', function(i) {
    	
        $(this).find('option').each(function(i) {
            var $rowid = '#tableRow' + this.id;
            if ($(this).is(':selected')) {
                $($rowid).show();
            } else {
                $($rowid).hide();
            }
        })
    });

    function fadeInElements(){

        $('.ruleRow').find('.ediRuleIcon').addClass('columnFadeIn').css('pointer-events','none');
        $('.ruleRow').find('.deleteRuleIcon').addClass('columnFadeIn').css('pointer-events','none');
        $('#addNewBussinessRuleId').addClass('columnFadeIn').find('a').css('pointer-events','none');
        $('.ruleRow').find('.ediRuleIcon').addClass('columnFadeIn').css('pointer-events','none');
        $('.ruleRow').find('.editRuleForSytem').addClass('columnFadeIn').css('pointer-events','none');
    }

    function fadeOutElements(){
        $('.activeRowBlock').removeClass('activeRowBlock');
        $('.ruleRow').find('.ediRuleIcon').removeClass('columnFadeIn').css('pointer-events','auto');
        $('.ruleRow').find('.deleteRuleIcon').removeClass('columnFadeIn').css('pointer-events','auto');
        $('#addNewBussinessRuleId').removeClass('columnFadeIn').find('a').css('pointer-events','auto');
        $('.ruleRow').find('.editRuleForSytem').removeClass('columnFadeIn').css('pointer-events','auto');
    }



    function fadeInDropDownAndsavaCancelButton(){
        $('#notificationLevelNo').addClass('columnFadeIn').css('pointer-events','none');
        $('.cancelAndsaveContainer').addClass('columnFadeIn').css('pointer-events','none');
    }

    function fadeOutDropDownAndsavaCancelButton(){
        $('#notificationLevelNo').removeClass('columnFadeIn').css('pointer-events','auto');
        $('.cancelAndsaveContainer').removeClass('columnFadeIn').css('pointer-events','auto');
    }

    function editRuleIcon(){
        $('.ediRuleIcon').on('click',function(event){
			 var elem = event.target;
            fadeInElements();
            fadeInDropDownAndsavaCancelButton();
            var $ruleRows = $(elem).parents('.ruleRow');
            $(elem).parents('.ruleRow').addClass('activeRowBlock');
            var ruleCols= $ruleRows.find('.ruleCol');
            var editedIndex = $ruleRows.index();
            $ruleRows.removeClass('columnFadeIn');
            $.each($('.ruleRow'),function(rowIndex){
                if(rowIndex !== editedIndex){
                    $($('.ruleRow')[rowIndex]).removeClass('columnFadeIn');
                }
            })
            $.each(ruleCols,function(i){
                var $colContainer =  $(ruleCols[i]);
                $colContainer.find('.role-input-block').show();
                $colContainer.find('.rule-text').hide();
            });
            $(elem).parents('.ruleRow').find('.buttonContainer').show();
        });
    }

    function editSystemRule(){
        $('.editRuleForSytem').on('click',function(event){
		    var elem = event.target;
            var ruleContent = '';
            var content = '';
            fadeInElements();
            fadeInDropDownAndsavaCancelButton();
            var $ruleRows = $(elem).parents('.ruleRow');
            $(elem).parents('.ruleRow').addClass('activeRowBlock');
            var transactionAttributes = $ruleRows.find('.trasactionAttriButesForSystem')[0];
            var ruleColumn = $ruleRows.find('.rolesForSystem')[0];
            var transactionBlock = $(transactionAttributes).find('.block');
            $.each(transactionBlock,function(i,transaction){
                var value = $(transaction).find('.value');
                var name = $($(transaction).find('.field')[0]).text();
                $.each(value,function(i,val){
                    var id = val.id+'#'+$(val).text();
                    content +=
                        '<div id='+id+' class="form-group '+name.trim().replace(" ", "").replace(".", "")+' ">'
                        +'<div class="system-sender-block input-group">'
                        +'<div class="field clearfix">'
                        +'<span>'+name+'</span>'
                        +'<span>'+$(val).text()+'</span>'
                        +'</div>'
                        +'<span class="input-group-btn"><i class="icon icon-delete closeAttributes"></i></span>'
                        +'</div>'
                        +'</div>';
                });
            });
            $.each($(ruleColumn),function(i,elem){
                var $colContainer =  $(elem);
                $colContainer.find('.role-input-block').show();
                $colContainer.find('.rule-text').hide();
            });
            $(transactionBlock).hide();
            $ruleRows.find('.buttonContainer').show();
            content +='<div class="form-group role-input-block addNewReciepientRoleAnchor" style="display:block">'
                +'<a href="javascript:void(0)" onclick="addRolesModalPanelForSystemTransactionAttribute(this)">Add New Transcation Attribute</a></div>';
            $(transactionAttributes).append(content);
            removeAttributes();
        });
    }

    editRuleIcon();

    editSystemRule();

    window.deleteRuleIcon=function (){
        $('.deleteRuleIcon').on("click",function(event){
		    var elem = event.target;
            var ruleRowDiv = $(elem).parents('.ruleRow');
            deleteRuleId = ruleRowDiv.attr('id');
            $('#deleteRulePopup').find('.msg ').text('Do you want to remove Bussiness Rule ?');
            showMessage();
        });
    };

    deleteRuleIcon();

    window.deleteRule=function (){
    	
        var ajaxResponse =  ajaxCall('../notification/deleteRule',{'ruleId':deleteRuleId,'notificationId':notificationId},"application/x-www-form-urlencoded");
        
        ajaxResponse.done(function(response) {
            console.log('response is succusseful');
            editBussinessRule(response);
        });
        ajaxResponse.fail(function(jqXHR, textStatus, errorThrown) {
            console.log('ajax call is failde due to reason : ' + "jqXHR : " + jqXHR + "textStatus : " + textStatus + "errorThrown : " + errorThrown);
        });
    }
    function onClickInputContainer(){
        $('.inputRole').on("click",function(event){
			var elem = event.target;
            var parentDivContainer = $(elem).parents('.role-input-block');
            var roleNameDiv = parentDivContainer.siblings('.rule-text');
            var roleNameInput = parentDivContainer.find('input:text').attr('name');
            $.each(roleNameDiv,function(i){
                var roleName = $(roleNameDiv[i]).text().trim();
                if(roleNameInput.trim() === roleName.trim()){
                    $(parentDivContainer).removeAttr("style").addClass('deletedElement');
                }
            })
        });
    }

    window.saveRoles=function (saveInstance){
        var businessLevelDto = [];
        var levelDtos = [];
        var blankCol = false;
        var parentDivInstance = $(saveInstance).parents('.ruleRow');
        numberOfLevel = Number($('#notificationLevelNo').find('option:selected').val());
        $(parentDivInstance).find('.deletedElement').remove();
        var addedElement = $(parentDivInstance).find('.addedNewElement');
        if(addedElement.size()>0){
            $(parentDivInstance).find('.addedNewElement').removeClass('addedNewElement');
        }
        var colDivs = parentDivInstance.find('.ruleCol');
        $(colDivs).each(function(i){
            if($(this).find('input').size() === 0){
                var html = '<p class ="error" style="color:red">'+ 'You must have atleast one Sender or one Receiver on each level </p>';
                $('#message').html(html);
                setTimeout(function() { $("#message p").hide(); }, 2000);
                blankCol = true;
                return false;
            }
        });
        if(blankCol) return;
        for(var i =0; i<numberOfLevel; i++){
            levelDtos[i]=[];
        }
        bussinessRuleJson = {
            "notificationBusinessRuleId": Number(parentDivInstance.attr('id')),
            "notificationId": Number($('#notificationId').attr('name'))
        };
        $.each(colDivs,function(colDivIndex){
            var inputTextBoxes = $(colDivs[colDivIndex]).find('input:text');
            if(colDivIndex === 0){
                $.each(inputTextBoxes,function(inputBoxIndex){
                    var obj = {}
                    var inputValue = $(inputTextBoxes[inputBoxIndex]).attr('id');
                    var inputs = inputValue.split('#');
                    obj.type = 'Sender';
                    obj.roleId = inputs[0];
                    obj.roleName = inputs[1];
                    levelDtos[0].push(obj);
                })
            }else{
                $.each(inputTextBoxes,function(inputBoxIndex){
                    var obj = {};
                    var inputValue = $(inputTextBoxes[inputBoxIndex]).attr('id');
                    var inputs = inputValue.split('#');
                    obj.type = 'Receiver';
                    obj.roleId = inputs[0];
                    obj.roleName = inputs[1];
                    levelDtos[colDivIndex-1].push(obj);
                    if(colDivIndex<numberOfLevel){
                        var cloneObject = jQuery.extend(true, {}, obj);
                        var senderObject = setSender(cloneObject);
                        levelDtos[colDivIndex].push(senderObject);
                    }
                })
            }
        })
        
        $.each(levelDtos,function(index){
        	
            var businessLevel = {};
            businessLevel.currentLevelNo = index+1;
            businessLevel.levelRoleDtos  = levelDtos[index];
            businessLevelDto.push(businessLevel);
        });
        bussinessRuleJson.businessLevelDto = businessLevelDto;
        console.log(JSON.stringify(bussinessRuleJson))
        var ajaxResponse =       ajaxCall('../notification/editRoles',JSON.stringify(bussinessRuleJson));
        ajaxResponse.done(function(response) {
            editBussinessRule(response);
            if(bussinessRuleJson.notificationBusinessRuleId){
                var html = '<p <p class="savedMessage" >A Business Rule  with id :'+response +' has been updated successfully.</p>';
                $('#message').html(html);
                $(parentDivInstance).find('.role-input-block').hide();
                $(parentDivInstance).find('.rule-text').show();
                $(parentDivInstance).find('.buttonContainer').hide();
            }else{
                var html = '<p class="savedMessage" >A Business Rule with id :'+response +' has been added successfully.</p>';
                $('#message').html(html);
                $(parentDivInstance).attr('id',response);
            }
        });
        ajaxResponse.fail(function(jqXHR, textStatus, errorThrown) {
            var html = '<p class ="error" style="color:red">'+ 'A Business Rule has occured due to reason : +' + " jqXHR : " + jqXHR + "textStatus : " + textStatus + "errorThrown : " + errorThrown+'</p>';
            $('#message').html(html);
            console.log('ajax call is faild due to reason : ' + "jqXHR : " + jqXHR + "textStatus : " + textStatus + "errorThrown : " + errorThrown);
        });
        setTimeout(function() { $("#message p").hide(); }, 2000);
        fadeOutElements();
        fadeOutDropDownAndsavaCancelButton();
    }

    function setSender(inputObject){
        inputObject.type = 'Sender';
        return inputObject;
    }

    window.addRolesModalPanel=function (containerDiv){
    	
        var options = $('select#roles option').show();
        selectedContainer = $(containerDiv).parents('.ruleCol');
        var inputBoxContainers =$(selectedContainer).find('.role-input-block:visible').find('input:text');
        $(inputBoxContainers).each(function(inputBoxIndex){
            var inputRoleId =  $($(inputBoxContainers)[inputBoxIndex]).attr('id').split('#')[0].trim();
            options.each(function(optionIndex){
                var option = $(options[optionIndex]);
                var optionRoleId = option.attr('id').trim();
                if(optionRoleId === inputRoleId){
                    option.hide();
                }
            });
        });
        $('#addRolesModal').modal('show');
    }

    window.subMitRolesAnadCloseModalPanel=function (){
        
    	var anchorChild = $(selectedContainer).children('.addNewReciepientRoleAnchor')[0];
        $(anchorChild).before(newComponentToBeAddded);
        onClickInputContainer();
        $('#addRolesModal').modal('hide');
        removeErrorMessage();
    }

    window.onselectOptions=function (selectDropDownDiv) {
        
    	newComponentToBeAddded = '';
        var selectedDropDown = $(selectDropDownDiv).find('option:selected');
        $.each(selectedDropDown,function(i) {
            var selectedOption = $(selectedDropDown[i]);
            if ($(selectedOption).attr('id')) {
                var id = $(selectedOption).attr('id')+'#'+$(selectedOption).attr('value');
                var value = $(selectedOption).attr('value');
                newComponentToBeAddded += '<div class="form-group role-input-block addedNewElement" >'
                    +'<div class="input-group">'
                    +'<input type="text" id='+id+' name='+value+' value='+value+' class="form-control">'
                    +'<span class="input-group-btn"><i class="icon icon-delete inputRole"></i></span>'
                    +'</div>'
                    +'</div>'
                    +'<div class="rule-text addedNewElement" style="display:none" >'+value+'</div>';
            }
        });
    }

    onClickInputContainer();

    window.addNewRuleRow = function (){
        
        var colSize = Number($('#notificationLevelNo').find('option:selected').val())+1;
        var addNewRoles = '<div  class="clearfix odd-row ruleRow activeRowBlock" style="display:block;">';
        var newCol = '';
        for(var i =0;i<colSize; i++){
            newCol += '<div class="col-sm-4 ruleCol">'
                +'<div class="form-group role-input-block addNewReciepientRoleAnchor" style="display:block">';
            if(i === 0){
                newCol += '<a href="javascript:void(0)" onclick="addRolesModalPanel(this)">Add New Sender Role</a>';
            }
            else{
                newCol += '<a href="javascript:void(0)" onclick="addRolesModalPanel(this)">Add New Recipient Role</a>';
            }
            newCol += '</div>'
                +'</div>';
        }
        addNewRoles += newCol;
        addNewRoles += '<div class="col-sm-offset-12">'
            +'<div class="action-btns-grid">'
            +'<a href="javascript:void(0)" class="ediRuleIcon"><span class="icon icon-edit "></span> Edit</a>'
            +'<a href="javascript:void(0)" class="deleteRuleIcon"><span class="icon icon-delete "></span> Delete</a>'
            +'</div>'
            +'</div>'
            +'<div class="buttonContainer">'
            +'<button type="button" class="btn canRule submitRuleEditOrAdd" style="display: inline-block;border: 2px #2e3192 solid;background: #fff; color:#2e3192" onclick="closeTheRuleDivContainer(this)">Cancel</button>'
            +' <button type="button" class="btn submitRuleEditOrAdd" style="display: inline-block;border: 2px #2e3192 solid;background: #2e3192; color:#fff" onclick="saveRoles(this)">Save</button>'
            +'</div>'
            +'</div>';
        $('#addNewBussinessRuleId').before(addNewRoles);
        deleteRuleIcon();
        editRuleIcon();
        adjustColumToWidth();
        fadeInElements();
        fadeInDropDownAndsavaCancelButton();
    }

    window.addNewRuleRowForSystem = function (){
    	
        var colSize = 2;
        var addNewRoles = '<div  class="clearfix odd-row ruleRow activeRowBlock" style="display:hidden";">';
        var newCol = '';
        for(var i =1;i<=colSize; i++){
            if(i === 1){
                newCol += '<div class="col-sm-9 ruleCol trasactionAttriButesForSystem">'
                    +'<div class="form-group role-input-block addNewReciepientRoleAnchor" style="display:block">'
                    +'<a href="javascript:void(0)" onclick="addRolesModalPanelForSystemTransactionAttribute(this)">Add New Transcation Attribute</a>'
                    +'</div>'
                    +'</div>';
            }else{
                newCol += '<div class="col-sm-3 ruleCol rolesForSystem">'
                    +'<div class="form-group role-input-block addNewReciepientRoleAnchor" style="display:block">'
                    +'<a href="javascript:void(0)" onclick="addRolesModalPanel(this);showRolesOnlyByAttributes(this)">Add New Recipient Role</a>'
                    +'</div>'
                    +'</div>';
            }
        }
        addNewRoles += newCol;
  	     addNewRoles += '<div class="col-sm-offset-12">'
 	        +'<div class="action-btns-grid">'
 	           +'<a href="javascript:void(0)" class="editRuleForSytem"><span class="icon icon-edit "></span> Edit</a>'
 	           +'<a href="javascript:void(0)" class="deleteRuleIcon"><span class="icon icon-delete "></span> Delete</a>'
 	        +'</div>'
 	     +'</div>'
          +'<div class="buttonContainer">'
 	     +'<button type="button" class="btn canRule submitRuleEditOrAdd" style="display: inline-block;border: 2px #2e3192 solid;background: #fff; color:#2e3192" onclick="closeTheRuleDivContainer(this)">Cancel</button>'
 	    +' <button type="button" class="btn submitRuleEditOrAdd" style="display: inline-block;border: 2px #2e3192 solid;background: #2e3192; color:#fff" onclick="saveRolesForSystem(this)">Save</button>'
 	  +'</div>'
 	  +'</div>';

        $('#addNewBussinessRuleId').before(addNewRoles);
        fadeInElements();
        fadeInDropDownAndsavaCancelButton();
    }

    window.closePopUpForRule=function () {
        
    	var parentrowDiv = canceRuleId;
        parentrowDiv.find('.addedNewElement').remove();
        parentrowDiv.find('.deletedElement').removeClass('deletedElement');
        var ruleCols = parentrowDiv.find('.ruleCol');
        $.each(ruleCols, function (i){
            var $colContainer =  $(ruleCols[i]);
            $colContainer.find('.role-input-block').hide();
            $colContainer.find('.rule-text').show();
        });
        parentrowDiv.find('.buttonContainer').hide();
        fadeOutElements();
        fadeOutDropDownAndsavaCancelButton();
    }

    window.closeTheRuleDivContainer=function (closedDiv){
        
    	canceRuleId = $(closedDiv).parents('.ruleRow');
        if($(canceRuleId).attr('id') === undefined){
            $(closedDiv).parents('.ruleRow').remove();
        }
        $('#cancelRulePopup').find('.content').html('<p>Your changes will not be saved .Do You want to proceed.<p>')
        showCancelRuleMessage();
    }

    function adjustColumToWidth() {
        
    	var numberOfLevel =  Number($('#notificationLevelNo').find('option:selected').val());
        if (numberOfLevel === 4 || numberOfLevel === 5){
            $('.senderReceiver').removeClass('col-sm-4').removeClass('col-sm-2').removeClass('col-sm-3').addClass('col-sm-2');
            $('.ruleCol').removeClass('col-sm-4').removeClass('col-sm-2').removeClass('col-sm-3').addClass('col-sm-2');
        }
        else if (numberOfLevel === 3){
            $('.senderReceiver').removeClass('col-sm-4').removeClass('col-sm-2').removeClass('col-sm-3').addClass('col-sm-3');
            $('.ruleCol').removeClass('col-sm-4').removeClass('col-sm-2').removeClass('col-sm-3').addClass('col-sm-3');
        }
        $('.ruleRow').attr('length', numberOfLevel);
    }

    $('#notificationLevelNo').on('change',function(event){
        
    	var newCol = '';
        var $ruleRow = $('.ruleRow');
        $('.ruleCol').removeClass('columnFadeIn');
        var $firstRuleRowWithCoulms = $('.ruleRow :nth-child(1)');
        var senderReceipientBlock = '<div class="col-sm-4 senderReceiver"><span>Sender</span></div>';
        var numberOfLevel =  Number($('#notificationLevelNo').find('option:selected').val());
        var totalNoOfColums= numberOfLevel+1;
        var nooOfColsInARule = $('.ruleRow:nth-child(1)').find('.ruleCol').size();
        var colsToBeAdded = totalNoOfColums  - nooOfColsInARule;
        function addingNewColumns(){
            for(var j = 1; j<=numberOfLevel; j++){
                senderReceipientBlock += '<div class="col-sm-4 senderReceiver">'
                    +'<span class="recipent">'+"Recipient "+ j+'</span>'
                    +'</div>';
            }
            $('.sender-recipent-block').empty().html(senderReceipientBlock);
            for(var i=0; i<colsToBeAdded; i++){
                newCol += '<div class="col-sm-4 ruleCol">'
                    +'<div class="form-group role-input-block addNewReciepientRoleAnchor" style="display:block">'
                    +'<a href="javascript:void(0)" onclick="addRolesModalPanel(this)">Add New Recipient Role</a>'
                    +'</div>'
                    +'</div>'
            }
            var cols = $ruleRow.find('.col-sm-offset-12').before(newCol);
            adjustColumToWidth();
        }
        if(colsToBeAdded>0){
            addingNewColumns();
        }else {
            for (var columnIndex = 1; columnIndex < nooOfColsInARule; columnIndex++) {
                var columnNo = numberOfLevel + columnIndex+1;
                $('.ruleCol:nth-child(' +columnNo+ ')').addClass('columnFadeIn');
            }
        }
        if(totalNoOfColums !== nooOfColsInARule){
            fadeInElements();
        }
        else{
            fadeOutElements();
            fadeOutDropDownAndsavaCancelButton();
        }
        levelChangeMessage();
    });

    adjustColumToWidth();

    $('.addAttributeFromModal').on('click',function(event){
        if(!validateAttributeOnModalPanel(event.target)){
            return;
        }
        var selectedTransactionAttributeContainer =  $('.selectedAttributes').find('select');
        var newContainerToBeAdded = $('.trasactionAttriButesForSystem').find('.addNewReciepientRoleAnchor');
        $.each(selectedTransactionAttributeContainer,function(i){
            var attributeId = $(selectedTransactionAttributeContainer[i]).attr('id');
            if(attributeId ==='jurisdiction'){
                var content ='';
                var selectedJurisdictions = $('#jurisdiction').find('option');
                $.each(selectedJurisdictions,function(i){
                    var values = [];
                    var id = selectedJurisdictions[i].id;
                    values  = id.split('#');
                    content += '<div id ='+id+' class="form-group Jurisdiction">'
                        +'<div class="system-sender-block input-group">'
                        +'<div class="field clearfix">'
                        +'<span>'+"Jurisdiction"+'</span>'
                        +'<span>'+values[1]+'</span>'
                        +'</div>'
                        +'<span class="input-group-btn"><i class="icon icon-delete closeAttributes"></i></span>'
                        +'</div>'
                        +'</div>'
                });
                newContainerToBeAdded.before(content);
            }
            if(attributeId ==='assignee'){
                var content ='';
                var selectedassignees = $('#assignee').find('option');
                $.each(selectedassignees,function(i){
                    var values = [];
                    var id = selectedassignees[i].id;
                    values  = id.split('#');
                    content += '<div id ='+id+' class="form-group Assignee">'
                        +'<div class="system-sender-block input-group">'
                        +'<div class="field clearfix">'
                        +'<span>'+"Assignee"+'</span>'
                        +'<span>'+values[1]+'</span>'
                        +'</div>'
                        +'<span class="input-group-btn"><i class="icon icon-delete closeAttributes"></i></span>'
                        +'</div>'
                        +'</div>'
                });
                newContainerToBeAdded.before(content);
            }
            if(attributeId ==='customerNo'){
                var content ='';
                var selectedcustomerNos = $('#customerNo').find('option');
                $.each(selectedcustomerNos,function(i){
                    var values = [];
                    var id = selectedcustomerNos[i].id;
                    values  = id.split('#');
                    content += '<div id ='+id+' class="form-group CustomerNo">'
                        +'<div class="system-sender-block input-group">'
                        +'<div class="field clearfix">'
                        +'<span>'+"Customer No."+'</span>'
                        +'<span>'+values[1]+'</span>'
                        +'</div>'
                        +'<span class="input-group-btn"><i class="icon icon-delete closeAttributes"></i></span>'
                        +'</div>'
                        +'</div>'
                });
                newContainerToBeAdded.before(content);
            }
            if(attributeId ==='technologyGroup'){
                var content ='';
                var selectedtechnologyGroups = $('#technologyGroup').find('option');
                $.each(selectedtechnologyGroups,function(i){
                    var values = [];
                    var id = selectedtechnologyGroups[i].id;
                    values  = id.split('#');
                    content += '<div id ='+id+' class="form-group TechnologyGroup">'
                        +'<div class="system-sender-block input-group">'
                        +'<div class="field clearfix">'
                        +'<span>'+"Technology Group"+'</span>'
                        +'<span>'+values[1]+'</span>'
                        +'</div>'
                        +'<span class="input-group-btn"><i class="icon icon-delete closeAttributes"></i></span>'
                        +'</div>'
                        +'</div>'
                });
                newContainerToBeAdded.before(content);
            }
            if(attributeId ==='organization'){
                var content ='';
                var selectedorganizations = $('#organization').find('option');
                $.each(selectedorganizations,function(i){
                    var values = [];
                    var id = selectedorganizations[i].id;
                    values  = id.split('#');
                    content += '<div id ='+id+' class="form-group Organization">'
                        +'<div class="system-sender-block input-group">'
                        +'<div class="field clearfix">'
                        +'<span>'+"Organisation"+'</span>'
                        +'<span>'+values[1]+'</span>'
                        +'</div>'
                        +'<span class="input-group-btn"><i class="icon icon-delete closeAttributes"></i></span>'
                        +'</div>'
                        +'</div>'
                });
                newContainerToBeAdded.before(content);
            }
        })

        $('#systemModalAttributes').modal('hide');
        removeByAttributesSelection(newContainerToBeAdded);

        removeAttributes();

        $('.selectedAttributes').find('[name="to[]"] option').remove();
        removeErrorMessage();

    })

    window.saveRolesForSystem = function (saveInstance){
        
    	var businessLevelDto = [];
        var levelDtos = [];
        var blankCol = false;
        levelDtos[0]=[];
        var parentDivInstance = $(saveInstance).parents('.ruleRow');
        var colDivs = parentDivInstance.find('.ruleCol');
        $(parentDivInstance).find('.deletedElement').remove();
        if(!validateAbsanctAttributesAndRoles(saveInstance)){
            return;
        }
        bussinessRuleJson = {
            "notificationBusinessRuleId": Number(parentDivInstance.attr('id')),
            "notificationId": Number($('#notificationId').attr('name'))
        };
        $.each(colDivs,function(colDivIndex){
            if(colDivIndex === 0){
                var obj = {}
                obj.type = 'Sender';
                obj.assignees = getAssignees(colDivs[0]);
                obj.jurisdictions = getJurisdictions(colDivs[0]);
                obj.customerNos = getCustomerNos(colDivs[0]);
                obj.techGroups = getTechGroups(colDivs[0]);
                obj.organisations = getOrganisations(colDivs[0]);
                levelDtos[0].push(obj);
            }else{
                var inputTextBoxes = $(colDivs[colDivIndex]).find('input:text');
                $.each(inputTextBoxes,function(inputBoxIndex){
                    var obj = {};
                    var inputValue = $(inputTextBoxes[inputBoxIndex]).attr('id');
                    var inputs = inputValue.split('#');
                    obj.type = 'Receiver';
                    obj.roleId = inputs[0];
                    obj.roleName = inputs[1];
                    levelDtos[0].push(obj);
                })
            }
        })

        $.each(levelDtos,function(index){
        
        	var businessLevel = {};
            businessLevel.currentLevelNo = index+1;
            businessLevel.levelRoleDtos  = levelDtos[index];
            businessLevelDto.push(businessLevel);
        });
        bussinessRuleJson.businessLevelDto = businessLevelDto;
        console.log(JSON.stringify(bussinessRuleJson))
        var ajaxResponse =       ajaxCall('../notification/editRuleForSystem',JSON.stringify(bussinessRuleJson));
        ajaxResponse.done(function(response) {
            editBussinessRule(response);
        });
        ajaxResponse.fail(function(jqXHR, textStatus, errorThrown) {
            var html = '<p class ="error" style="color:red">'+ 'A Business Rule has occured due to reason : +' + " jqXHR : " + jqXHR + "textStatus : " + textStatus + "errorThrown : " + errorThrown+'</p>';
            $('#message').html(html);
            console.log('ajax call is faild due to reason : ' + "jqXHR : " + jqXHR + "textStatus : " + textStatus + "errorThrown : " + errorThrown);
        });

        function getAssignees(colDiv){
            var assignees = $(colDiv).find('.Assignee');
            var assigneeList = [];
            $.each(assignees,function(index){
                var obj={};
                var id = assignees[index].id;
                var value  = id.split('#');
                obj.id=value[0];
                obj.name=value[1];
                assigneeList.push(obj);
            });
            return assigneeList;
        }

        function getJurisdictions(colDiv){
            var assignees = $(colDiv).find('.Jurisdiction');

            var assigneeList = [];

            $.each(assignees,function(index){

                var obj={};

                var id = assignees[index].id;

                var value  = id.split('#');

                obj.id=value[0];

                obj.name=value[1];

                assigneeList.push(obj);

            });

            return assigneeList;
        }

        function getCustomerNos(colDiv){
            var assignees = $(colDiv).find('.CustomerNo');

            var assigneeList = [];

            $.each(assignees,function(index){

                var obj={};

                var id = assignees[index].id;

                var value  = id.split('#');

                obj.id=value[0];

                obj.number=value[1];

                assigneeList.push(obj);

            });

            return assigneeList;
        }

        function getTechGroups(colDiv){
            var assignees = $(colDiv).find('.TechnologyGroup');

            var assigneeList = [];

            $.each(assignees,function(index){

                var obj={};

                var id = assignees[index].id;

                var value  = id.split('#');

                obj.id=value[0];

                obj.name=value[1];

                assigneeList.push(obj);

            });

            return assigneeList;
        }

        function getOrganisations(colDiv){
            var assignees = $(colDiv).find('.Organization');

            var assigneeList = [];

            $.each(assignees,function(index){

                var obj={};

                var id = assignees[index].id;

                var value  = id.split('#');

                obj.id=value[0];

                obj.name=value[1];

                assigneeList.push(obj);

            });

            return assigneeList;
        }

    }

    window.saveNotification=function (){
        
    	var notification ={

            "notificationLevelNo":Number($('#notificationLevelNo').find('option:selected').val()),

            "notificationId": Number($('#notificationId').attr('name')),

            "businessRuleDtos":[]

        }

        var ruleRows = $('.ruleRow');
        
    	$.each(ruleRows,function(i){
            var businessLevelDto = generateBussinessLvelDTO(ruleRows[i]);

            if(businessLevelDto === false){

                return;

            }

            else{

                notification['businessRuleDtos'].push(businessLevelDto);

            }

        })
        console.log(JSON.stringify(notification));
        var ajaxResponse =       ajaxCall('../notification/editNotification',JSON.stringify(notification));
        ajaxResponse.done(function(response){

            console.log(response);
            
            editBussinessRule(response);
            
        });

        ajaxResponse.fail(function(jqXHR, textStatus, errorThrown) {

            console.log('ajax call is faild due to reason : ' + "jqXHR : " + jqXHR + "textStatus : " + textStatus + "errorThrown : " + errorThrown);

        });

    }

    function generateBussinessLvelDTO(saveInstance){

        var businessLevelDto = [];

        var levelDtos = [];

        var blankColumn = false;

        var parentDivInstance = $(saveInstance);

        numberOfLevel = Number($('#notificationLevelNo').find('option:selected').val());

        $(parentDivInstance).find('.deletedElement').remove();

        var addedElement = $(parentDivInstance).find('.addedNewElement');
        if(addedElement.size()>0){

            $(parentDivInstance).find('.addedNewElement').removeClass('addedNewElement');

        }

        var colDivs = parentDivInstance.find('.ruleCol');

        $(colDivs).each(function(i,elem){
            if($(this).hasClass('columnFadeIn')){

                $(this).remove();

            }
        });
        var colDivs = parentDivInstance.find('.ruleCol');
        $(colDivs).each(function(i,elem){

            if($(elem).find('input').size() === 0){

                var html = '<p class ="error" style="color:red">'+ 'You must have atleast one Sender or one Receiver on each level </p>';

                $('#message').html(html);

                setTimeout(function() { $("#message p").hide(); }, 2000);

                blankColumn = true;

                return false;

            }

        });

        if(blankColumn) return;

        for(var i =0; i<numberOfLevel; i++){

            levelDtos[i]=[];

        }

        bussinessRuleJson = {

            "notificationBusinessRuleId": Number(parentDivInstance.attr('id')),

            "notificationId": Number($('#notificationId').attr('name'))

        };

        $.each(colDivs,function(colDivIndex){

            if($(colDivs[colDivIndex]).hasClass('columnFadeIn')){

                return;

            }

            var inputTextBoxes = $(colDivs[colDivIndex]).find('input:text');

            if(colDivIndex === 0){

                $.each(inputTextBoxes,function(inputBoxIndex){

                    var obj = {}

                    var inputValue = $(inputTextBoxes[inputBoxIndex]).attr('id');

                    var inputs = inputValue.split('#');
                    
                    obj.type = 'Sender';

                    obj.roleId = inputs[0];

                    obj.roleName = inputs[1];
                    
                    levelDtos[0].push(obj);

                })

            }else{

                $.each(inputTextBoxes,function(inputBoxIndex){

                    var obj = {};

                    var inputValue = $(inputTextBoxes[inputBoxIndex]).attr('id');

                    var inputs = inputValue.split('#');

                    obj.type = 'Receiver';

                    obj.roleId = inputs[0];

                    obj.roleName = inputs[1];

                    levelDtos[colDivIndex-1].push(obj);

                    if(colDivIndex<numberOfLevel){

                        var cloneObject = jQuery.extend(true, {}, obj);

                        var senderObject = setSender(cloneObject);

                        levelDtos[colDivIndex].push(senderObject);

                    }

                })

            }

        })

        $.each(levelDtos,function(index){

            var businessLevel = {};

            businessLevel.currentLevelNo = index+1;

            businessLevel.levelRoleDtos  = levelDtos[index];
            
            businessLevelDto.push(businessLevel);

        });

        bussinessRuleJson.businessLevelDto = businessLevelDto;

        return bussinessRuleJson;

    }

    window.closePage=function (){
        window.location.href='../notification/dashBoard';
    }

    window.showMessageForSystemDeleteRule = function (){
    	$("#deleteRulePopup").show();
    	$("#deleteRulePopup").wrap("<div class='overlay'>");
    }
    window.hideMessageForSystemDeleteRule = function (){
    	$("#deleteRulePopup").hide();
    	$("#deleteRulePopup").unwrap("<div class='overlay'>");
    }

    window.showMessage=function (){

        $("#deleteRulePopup").show();

        $("#deleteRulePopup").wrap("<div class='overlay'>");

    }

    window.hideMessage=function (){

        $("#deleteRulePopup").hide();

        $("#deleteRulePopup").unwrap("<div class='overlay'>");

    }

    function showCancelMessage(){

        $("#cancelPopup").show();

        $("#cancelPopup").wrap("<div class='overlay'>");

    }

    window.hidecancelMessage=function (){

        $("#cancelPopup").hide();

        $("#cancelPopup").unwrap("<div class='overlay'>");

    }

    function showCancelRuleMessage(){

        $("#cancelRulePopup").show();

        $("#cancelRulePopup").wrap("<div class='overlay'>");

    }

    window.hidecancelRuleMessage=function (){

        $("#cancelRulePopup").hide();

        $("#cancelRulePopup").unwrap("<div class='overlay'>");

    }

    window.showMesageForCancel=function (){

        showCancelMessage();

    }

    window.editBussinessRule=function (id){

        window.location.href='../notification/editBussinessRules?notificationId='+id;

    }

    function validateAbsanctAttributesAndRoles(cellIndex){

        var row = $(cellIndex).parents('.ruleRow')[0];

        var attribues = $($(row).find('.trasactionAttriButesForSystem')[0]).find('.form-group');

        var roles = $($(row).find('.col-sm-3')[0]).find('input:text');

        if(!$(attribues).hasClass('Jurisdiction')){

            createMessage('Jurisdiction');

            return false;

        }

        else if(!$(attribues).hasClass('Assignee')){

            createMessage('Assignee');

            return false;

        }

        else if(!$(attribues).hasClass('CustomerNo')){

            createMessage('Customer No.');

            return false;

        }

        else if(!$(attribues).hasClass('TechnologyGroup')){

            createMessage('Technology Group');

            return false;

        }

        else  if(!$(attribues).hasClass('Organization')){

            createMessage('Organization');

            return false;

        }

        else if(roles.length==0){

            createMessage('Recipient Role');

            return false;

        }

        return true;

    }

    function createMessage(value){

        $('#message').html('');

        var message = '<p id="error" style="color:red;">'+value +' is not present.'+'</p>';

        $('#message').html(message);

     //   setTimeout(function() { $("#message p").hide(); }, 2000);

    }
    function removeErrorMessage(){
    	$("#message p").hide();
    }

    function removeByAttributesSelection($eventTarget) {

        if (!$eventTarget.hasClass('addNewReciepientRoleAnchor')) {

            var parentElement = $($eventTarget).parents('.form-group')[0];

            var attributeClassname = getAttriButeByModalName(parentElement);

            var attributeId = $($eventTarget).parents('.form-group')[0].id;

            showIfAvailableOrAddTheElement(attributeId,attributeClassname);

        }

        function showIfAvailableOrAddTheElement(attributeId,attributeClassname){

            var isElementAvailble = false;

            $.each($('[name="from[]"] option'), function(i, elem) {

                if (elem.id === attributeId) {

                    $(elem).show();

                    isElementAvailble = true;

                }

            });

            if(!isElementAvailble){

                var elementAelectContainerInModal = $('.user-management-tabs').find('.'+attributeClassname)[0];

                var selectAttr = $(elementAelectContainerInModal).find('[name="from[]"]')[0];

                var attr = attributeId.split('#');

                var option  = '<option id='+attributeId+'>'+attr[1]+'</option>';

                $(selectAttr).append(option);

            }

        }

        function getAttriButeByModalName(element) {

            var $element = $(element);

            if ($element.hasClass('Jurisdiction')) {

                return 'Jurisdiction';

            } else if ($element.hasClass('Assignee')) {

                return 'Assignee';

            } else if ($element.hasClass('CustomerNo')) {

                return 'Customer';

            } else if ($element.hasClass('TechnologyGroup')) {

                return 'technologyGroup';

            } else if ($element.hasClass('Organization')) {

                return 'organization';

            }

        }

        var rulerow = $eventTarget.parents('.ruleRow')[0];

        var ruleCell = $(rulerow).find('.rolesForSystem')[0];

        $eventTarget.parents('.form-group').remove();

        var roles = getAllSystemAttributes();

        var texts = $(ruleCell).find('.rule-text');

        var inputs = $(ruleCell).find('input:text');

        $.each(texts, function (i, textElem){

            var elemPresence = false;

            $.each(roles,function(index,elem){

                if($(textElem).text() === elem.name){

                    elemPresence = true;

                    return;

                }

            });

            if(!elemPresence){

                $(textElem).remove();

            }

        });

        $.each(inputs, function (i, inputElem){

            var elemPresence = false;

            $.each(roles,function(index,elem){

                if($(inputElem).attr('name') === elem.name){

                    elemPresence = true;

                    return;

                }

            });

            if(!elemPresence){

                $(inputElem).parents('.role-input-block').remove();

            }

        });

    }

    function removeAttributes(){

        $('.closeAttributes').on('click',function(event){

            var $eventTarget = $(event.target);

            removeByAttributesSelection($eventTarget);

        });

    }

    removeAttributes();

    function getAllSystemAttributes(){

        var systemAttributesIds = {

            "juricIds":[],

            "assigneeIds":[],

            "techGrpIds":[],

            "custmNoIds":[],

            "organIds":[]

        }

        var roles = undefined;

        var element =  $('.trasactionAttriButesForSystem');

        function setAttributesIds(attrArray,attrs){

            $.each($(attrs),function(i,element){

            	var value  = $(element).attr('id');

                var idAndValue = value.split('#');

                attrArray.push(idAndValue[0]);

            });

        }

        var jurisdictions = $(element).find('.Jurisdiction');

        setAttributesIds(systemAttributesIds.juricIds,jurisdictions);

        var assignees = $(element).find('.Assignee');

        setAttributesIds(systemAttributesIds.assigneeIds,assignees);

        var techGrpIds = $(element).find('.TechnologyGroup');

        setAttributesIds(systemAttributesIds.techGrpIds,techGrpIds);

        var orgs  = $(element).find('.Organization');

        setAttributesIds(systemAttributesIds.organIds,orgs);

        var customerIds = $(element).find('.CustomerNo');

        setAttributesIds(systemAttributesIds.custmNoIds,customerIds);

        var ajaxResponse =  ajaxCall('../notification/getRolesByAttributes',JSON.stringify(systemAttributesIds));

        ajaxResponse.done(function(response) {

           roles = response;

        });

        ajaxResponse.fail(function(jqXHR, textStatus, errorThrown) {

            console.log('ajax call is failde due to reason : ' + "jqXHR : " + jqXHR + "textStatus : " + textStatus + "errorThrown : " + errorThrown);

        });

        return roles;

    }

    window.showRolesOnlyByAttributes=function (containerDiv){

        var rolesByattributes = getAllSystemAttributes();

        var $options = $('select#roles option');

        $options.hide();

        var selectedContainer = $(containerDiv).parents('.ruleCol');

        var inputBoxContainers =$(selectedContainer).find('.role-input-block:visible').find('input:text');

        if(rolesByattributes !== undefined){

            $.each(inputBoxContainers,function(index,elem){

                $.each(rolesByattributes,function(roleIndex,roleElem){

                    if(roleElem!==undefined && roleElem.name === $(elem).attr('name')){
                    	
                        rolesByattributes.splice(roleIndex, 1);

                    }

                })

            })

            if(rolesByattributes.length>0){

                $.each(rolesByattributes,function(roleIndex,roleElem){
                	
                    $options.each(function(optionIndex,option){

                        var optionValue = $(option).attr('value');

                        if(optionValue === roleElem.name){

                            $(option).show();

                        }
                   })

                });

            }

        }

    }

    window.addRolesModalPanelForSystemTransactionAttribute = function (val){

        $('select option').show();

        var transactionAttributes = $(val).parents('.trasactionAttriButesForSystem')[0];

        var attributesBlocks = $(transactionAttributes).find('.form-group');

        $.each(attributesBlocks,function(i,elem){

            if(elem.id !== ""){

                $('option[id='+elem.id+']').hide();

            }

        })

        $('#systemModalAttributes').modal('show');

    }

    function validateAttributeOnModalPanel(target) {

        var editInputBox = $('.closeAttributes').length;

        if(editInputBox>0){

            return true;

        }

        $('.modalMessage').html('');

        var attributesOptions = $('.selectedAttributes').find('[name="to[]"] option');

        var array = ['Jurisdiction', 'Assignee', 'Customer', 'technologyGroup', 'organization'];

        $.each(attributesOptions, function(i, elem) {

            var tab = $(elem).parents('.tab-pane')[0];

            $.each(array, function(j, arr) {

                if ($(tab).hasClass(arr)) {

                    array.splice(j, 1);

                }

            })

        });

        var text = 'Plaese Select Attributes : ';

        $.each(array,function(i, elem) {

            if (elem === 'technologyGroup') {

                text += 'Tectnology Group ,';

            } else if (elem === 'organization') {

                text += 'Organisation ,';

            } else if (elem === 'Customer') {

                text += 'Customer No. ,';

             } else {

                text += elem +', ';

            }

        })

        if (array.length === 0) {

            return true;

        } else {

            var messageHtml = '<p style="color:red">'+text+'</p>';

            $('.modalMessage').html(messageHtml);

            return false;

        }

    }

    $('[role="presentation"]').on('click',function(event){

        $('.modalMessage').html('');

    });

    function levelChangeMessage() {

        var html = '<p  style="color:#0088CC">' + 'This change will start with new notifications, all existing notifications will remain unchanged.</p>';

        $('#message').html(html);

        setTimeout(function() {

            $("#message p").hide();

        }, 5000);

    }

    window.populatedefalutRolesRoles = function (){
    	

    	var inputs = $('.role-details').find('input');
    	$.each(inputs, function(i) {
    		var id = inputs[i].id;

    		var selctedOptions = $('select#defaultRoles option');

    		selctedOptions.each(function(optionIndex) {

    			var option = $(selctedOptions[optionIndex]);

    			var optionRoleId = option.attr('id').trim();

    			if (inputs[i].id === optionRoleId) {

    				option.hide();
    			}

    		});

    	})

        $('#addRolesForDefaultReciepientRoles').modal('show');
    	  defaultRoleSelect();

    }
    
    window.defaultRoleSelect = function(){
    	$('select#defaultRoles').on('change', function(i) {
    		$(this).find('option').each(function(i) {
    			var $rowid = '#defaultRolesRow' + this.id;
    			if ($(this).is(':selected')) {
    				$($rowid).show();
    			} else {
    				$($rowid).hide();
    			}
    		})
    	});
    	}
    
    defaultRoleSelect();

    window.addingRolesDefaultRoles = function (){

        var selctedOptions = $('select#defaultRoles').find('option:selected');

        $.each(selctedOptions,function(i,elem) {

           var id = selctedOptions[i].id;

            var value = selctedOptions[i].value;

            var content = '<div class="input-group role-input-block" style="display:block">'

                + '<input type="text" id='

                + id

                + '  value='

                + value

                + ' style="width:150px;height:25px;" class="form-control inputDefaultRoles"></input>'

                + '<span class="input-group-btn"><i class="icon icon-delete defaultRoleDelete"></i></span>'

                + '</div>';

            $('.role-details').append(content);
        })
       removeDefaultRoles();
        $('#addRolesForDefaultReciepientRoles').modal('hide');
    }
    
    window.savedefaultsRoles = function (){
    	
    	var notification ={

                "notificationId": Number($('#notificationId').attr('name')),

                "defaultRoles":[]

            }
    	
        $('.role-details').find('input:text').each(function(i,elem){
        
	        var defaultRole = 	{"roleId":elem.id,
	                             "name":elem.value
	                             }
	        notification.defaultRoles.push(defaultRole);
	        	
        });
        
        var ajaxResponse = ajaxCall('../notification/saveDefaultsRole',JSON.stringify(notification));

        ajaxResponse.done(function(response) {
      
        	editBussinessRule(response);
         
        });

        ajaxResponse.fail(function(jqXHR, textStatus, errorThrown) {

            console.log('ajax call is failde due to reason : ' + "jqXHR : " + jqXHR + "textStatus : " + textStatus + "errorThrown : " + errorThrown);

        });

    }
    
    window.removeDefaultRoles = function(){
    	
    $('.defaultRoleDelete').on('click',function(event){
    	
    	$(event.target).parents('.role-input-block').remove();
    	savedefaultsRoles();
    });
    }

    removeDefaultRoles();
}());

$(function () {
	
    $("#hiddenNotificationFields").hide();
    $("#toggleNotificationFields").on("click", function(){

    	$("#hiddenNotificationFields").slideToggle();
    	 $(this).text(function(i, text){
    		 
  return text === "Hide Details" ? "Show All Details" : "Hide Details";
})
    	
    });
});

jQuery(document).ready(function($) {
 	$('.multiselect').multiselect();
 	$(".buttonContainer").hide();
 });