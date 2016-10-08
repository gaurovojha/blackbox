<script>
$(document).ready(function() {
	bindCheckBoxClick();
	idsRefMap = [[]];
	countRefMap =[[]];
	selectsave = $('#selIDSPending1449 option:selected').val();
	idsRefMap[selectsave]=[$('#tbodyAccUSPatent').html().trim(),$('#tbodyAccUSPub').html().trim(),$('#tbodyAccForeign').html().trim(),$('#tbodyAccNPL').html().trim()];
	countRefMap[selectsave] = [$('#pusCount').text(),$('#usPubCount').text(),$('#fpCount').text(), $('#nplCount').text()];
	$('#selIDSPending1449 option').each(function(){
		if(!this.selected)
		{
			var val = $(this).val();
			getAllCounts(val);
		}
	});
	
});

$('#selIDSPending1449').on('click',function(){
	var select = $('#selIDSPending1449 option:selected').val();
	/* if(selectsave == select)
		{
		idsRefMap[select]=[$('#tbodyAccUSPatent').html().trim(),$('#tbodyAccUSPub').html().trim(),$('#tbodyAccForeign').html().trim(),$('#tbodyAccNPL').html().trim()];
		}
	else
		{
		selectsave = select;
		} */
		if(!idsRefMap[select])
			{
			idsRefMap[select]=["","","",""];
			}
});

$('#selIDSPending1449').on('change',function(){
	getReferencesByIdsId();
});

function getReferencesByIdsId()
{	
		var select = $('#selIDSPending1449 option:selected').val();
		
		if(!(idsRefMap[select])  || idsRefMap[select][0] =="")
		{
			var target = $("#contextPath").val() + "/ids/fetchReviewReferenceRecords";
			var data = {'refType':'PUS','idsId':select};
			$.ajax({
				type:"POST",
				url : target,
				data : data,
				success : function(response)
				{
				if(response.trim() =="")
					{
						response ="<tr><td colspan='8'><div class ='noRecords' style='text-align:center;'>No Records Found.</div></td></tr>";
					}
					printAllCounts(select);
					$('#tbodyAccUSPatent').html(response);
					idsRefMap[select][0] = response;
					changeButtonState('#tbodyAccUSPatent');
					
				}
			});
		}
		else
			{
				printAllCounts(select);
				$('#tbodyAccUSPatent').html(idsRefMap[select][0]);
				changeButtonState('#tbodyAccUSPatent');
				
			}
		
	$('#accordion').find('.panel').each(function(i,panel){
		var panel = $(panel);
		if(panel.find('.panel-collapse').hasClass('in'))
			{
				panel.find('.panel-collapse').removeClass('in');
				panel.find('.panel-title a').addClass('collapsed');
			}
	})	;
	
	
	if($('#accordianUSPatent').hasClass('collapsed'))
		{
			//$('#collapseOne').addClass('in');
			$('#accordianUSPatent').removeClass('collapsed');
			
		}
	if(!$('#collapseOne').hasClass('in'))
	{
	$('#collapseOne').addClass('in');
	//$('#collapseOne').attr('aria-expanded',true);
	$('#collapseOne').removeAttr('style')
	}
	
	$('.selectAll').each(function(){
		this.checked=false;
	})
	$('.selectOne').each(function(){
		this.checked=false;
	});
	deActivateActionButtons();
}
	
	function setDropdownvalue(){
		var url = window.location.pathname;
		var url_array = url.split('/');
		var id  = url_array[url_array.length-1];
		var selectEle =  $('#selIDSPending1449')[0];
		for ( var i = 0; i < selectEle.options.length; i++ ) {

	        if ( selectEle.options[i].value == id ) {

	        	selectEle.options[i].selected = true;

	            break;

	        }

	    }
	}
	
	function getAllCounts(select)
	{
		var countTarget = $("#contextPath").val() + "/ids/getCountMap";
		$.ajax({
			type:"POST",
			url : countTarget,
			data :{'idsId':select},
			success:function(response)
			{
				//console.log(response);
				countRefMap[select] =  ["","","",""];
			if(response.PUS)
				{
					countRefMap[select][0] = response.PUS;
				}
			else
				{
					 countRefMap[select][0] = 0;
				}
			if(response.US_PUBLICATION)
				{
				 countRefMap[select][1] = response.US_PUBLICATION;
				}
			else
				{
				 countRefMap[select][1] = 0;
				}
			if(response.FP)
				{
				 countRefMap[select][2] = response.FP;
				}
			else
				{
				 countRefMap[select][2] = 0;
				}
			if(response.NPL)
				{
				 countRefMap[select][3] = response.NPL;
				}
			else
				{
				 countRefMap[select][3] = 0;
				}
				
			}
		});
	}
	
	function printAllCounts(select)
	{
		 $('#pusCount').text(countRefMap[select][0]+" ");
		 $('#usPubCount').text(countRefMap[select][1]+" ");
		 $('#fpCount').text(countRefMap[select][2]+" ");
		 $('#nplCount').text(countRefMap[select][3]+" ");
	}
	
	$('#accordianUSPatent').on('click',function(){
		$('.selectAll').each(function(){
			this.checked=false;
		})
		$('.selectOne').each(function(){
			this.checked=false;
		});
		deActivateActionButtons();
	});
	
	$('#accordianUSPub').on('click',function(){
		fetchReferenceReviewUSRecords('#tbodyAccUSPub','US_PUBLICATION');
	});
	
	$('#accordianForeign').on('click',function(){
		fetchReferenceReviewFPRecords('#tbodyAccForeign','FP');
	});
	
	$('#accordianNPL').on('click',function(){
		fetchReferenceReviewNPLRecords('#tbodyAccNPL','NPL');
	});
	
	/* function fetchReferenceReviewRecords(tbody,refType)
	{
		//if($(tbody).html().trim()=='')
		{
			var select = $('#selIDSPending1449 option:selected').val();
			var target = $("#contextPath").val() + "/ids/fetchReviewReferenceRecords";
			var data = {'refType':refType,'idsId':select};
			$.ajax({
				type:"POST",
				url : target,
				data : data,
				success : function(response)
				{
					//console.log(response);
					$(tbody).html(response);
					changeButtonState(tbody);
				}
			});
		}
		$('.selectAll').each(function(){
			this.checked=false;
		})
		$('.selectOne').each(function(){
			this.checked=false;
		});
		deActivateActionButtons();
		
	} */
	
	
	function fetchReferenceReviewUSRecords(tbody,refType)
	{
		var select = $('#selIDSPending1449 option:selected').val();
		if(!(idsRefMap[select])||idsRefMap[select][1]=="")
		{
			
			var target = $("#contextPath").val() + "/ids/fetchReviewReferenceRecords";
			var data = {'refType':refType,'idsId':select};
			$.ajax({
				type:"POST",
				url : target,
				data : data,
				success : function(response)
				{
					if(response.trim() =="")
					{
						response ="<tr><td colspan='8'><div class ='noRecords' style='text-align:center;'>No Records Found.</div></td></tr>";
						//$(tbody).parent().find('.selectAll').attr('disabled',true);
					}
					$(tbody).html(response);
					idsRefMap[select][1]=response;
					changeButtonState(tbody);
				}
			});
		}
		else
			{
			
			$(tbody).html(idsRefMap[select][1]);
			changeButtonState(tbody);
			}
		$('.selectAll').each(function(){
			this.checked=false;
		})
		$('.selectOne').each(function(){
			this.checked=false;
		});
		deActivateActionButtons();
		
	} 
	
	function fetchReferenceReviewFPRecords(tbody,refType)
	{
		var select = $('#selIDSPending1449 option:selected').val();
		if(!(idsRefMap[select])||idsRefMap[select][2]=="")
		{
			
			var target = $("#contextPath").val() + "/ids/fetchReviewReferenceRecords";
			var data = {'refType':refType,'idsId':select};
			$.ajax({
				type:"POST",
				url : target,
				data : data,
				success : function(response)
				{
					//console.log(response);
					if(response.trim() =="")
					{
						response ="<tr><td colspan='8'><div class ='noRecords' style='text-align:center;'>No Records Found.</div></td></tr>";
					}
					$(tbody).html(response);
					idsRefMap[select][2]=response;
					changeButtonState(tbody);
				}
			});
		}
		else
		{
			$(tbody).html(idsRefMap[select][2]);
			changeButtonState(tbody);
		}
		$('.selectAll').each(function(){
			this.checked=false;
		})
		$('.selectOne').each(function(){
			this.checked=false;
		});
		deActivateActionButtons();
		
	} 
	
	function fetchReferenceReviewNPLRecords(tbody,refType)
	{
		var select = $('#selIDSPending1449 option:selected').val();
		if(!(idsRefMap[select])||idsRefMap[select][3]=="")
		{
			
			var target = $("#contextPath").val() + "/ids/fetchReviewReferenceRecords";
			var data = {'refType':refType,'idsId':select};
			$.ajax({
				type:"POST",
				url : target,
				data : data,
				success : function(response)
				{
					//console.log(response);
					if(response.trim() =="")
					{
						response ="<tr><td colspan='8'><div class ='noRecords' style='text-align:center;'>No Records Found.</div></td></tr>";
					}
					$(tbody).html(response);
					idsRefMap[select][3]=response;
					changeButtonState(tbody);
				}
			});
		}
		else
			{
				$(tbody).html(idsRefMap[select][3]);
				changeButtonState(tbody);
			}
		$('.selectAll').each(function(){
			this.checked=false;
		})
		$('.selectOne').each(function(){
			this.checked=false;
		});
		deActivateActionButtons();
		
	} 
	
		$('.selectAll').on('click',function(){
			if(this.checked)
				{
				$('.selectAll').parents('table').children('tbody').find('.selectOne').each(function(){
						this.checked=true;
					});
					activateActionButtons();
				}
			else
				{
				$('.selectAll').parents('table').children('tbody').find('.selectOne').each(function(){
						this.checked=false;
					});
					deActivateActionButtons();
				}
		});
function changeButtonState(tbody)
{
	
		$(tbody + ' .selectOne').on('click',function(){
			var checkedCount = $(".selectOne:checked").length;
			if (checkedCount == 0)
				deActivateActionButtons();
			else
				activateActionButtons();
			if(this.checked == false)
				{
				$(this).parents('table').children('thead').find('.selectAll')[0].checked = false;
				}
		});
}

 function deActivateActionButtons()
 {
	 if(!$('.actionBtn').hasClass('disabled'))
		{
		$('.actionBtn').addClass('disabled');
		}
 }
 
  function activateActionButtons()
  {
	  if($('.actionBtn').hasClass('disabled'))
		{
		$('.actionBtn').removeClass('disabled');
		}
  }
  
  
 $('#btnAccepted').on('click',function(){
	 var select = $('#selIDSPending1449 option:selected').val();
	 var subIndex = 0;
	 switch($('#accordion').find('.panel .panel-collapse.collapse.in .table tbody').attr('id'))
		 {
	 		
	 		case "tbodyAccUSPatent":
	 			subIndex = 0;
	 			break;
	 		case "tbodyAccUSPub":
	 			subIndex = 1;
	 			break;
	 		case "tbodyAccForeign":
	 			subIndex =2;
	 			break;
	 		case "tbodyAccNPL":
	 			subIndex = 3;
	 			break;
		 }
	 
	 $('#accordion').find('.panel .panel-collapse.collapse.in .table tbody tr').each(function(i,row){
		var row = $(row);
		if(row.find('input:checked').val())
			{
				row.find('.usptoStatus').text('ACCEPTED');
			}
	 });
	 idsRefMap[select][subIndex]=$('#accordion').find('.panel .panel-collapse.collapse.in .table tbody').html().trim();
 });
 
 
 $('#btnRejected').on('click',function(){
	 var select = $('#selIDSPending1449 option:selected').val();
	 var subIndex = 0;
	 switch($('#accordion').find('.panel .panel-collapse.collapse.in .table tbody').attr('id'))
		 {
	 		
	 		case "tbodyAccUSPatent":
	 			subIndex = 0;
	 			break;
	 		case "tbodyAccUSPub":
	 			subIndex = 1;
	 			break;
	 		case "tbodyAccForeign":
	 			subIndex =2;
	 			break;
	 		case "tbodyAccNPL":
	 			subIndex = 3;
	 			break;
		 }
	 $('#accordion').find('.panel .panel-collapse.collapse.in .table tbody tr').each(function(i,row){
		var row = $(row);
		if(row.find('input:checked').val())
			{
				row.find('.usptoStatus').text('REJECTED');
			}
		
	 });
	 idsRefMap[select][subIndex]=$('#accordion').find('.panel .panel-collapse.collapse.in .table tbody').html().trim();
	 alert("the references needs to be added as a new entry with necessary corrections");
 });
   
 $('#btnUpdateReference').on('click',function(){
	 var isUpdate = 1;
	 refStatusMap = {};
	 $.each(countRefMap,function(select,countArr){
		if(countArr && countArr.length!=0)
			{
				$.each(countArr,function(j,val)
					{
						var referenceCount = countUpdatedReferences(select,j)	;
						if(val != referenceCount)
							{
								isUpdate = 0;
								return false;
							}
					});
				if(isUpdate ==0)
					{
						return false;
					}
			}
	 });
	 if(isUpdate == 0)
		 alert("please update status of all references");
	 else
	 {
		 
		 var target = $("#contextPath").val() + "/ids/updateReferenceStatus";
		 	$.ajax({
				type :"POST",
				url : target,
		 		contentType : "application/json",
		 		data : JSON.stringify(refStatusMap),
		 		success : function(response)
		 		{
		 			if(response>=1)
		 				{
		 					window.location.href = $("#contextPath").val() + "/ids/dashboard";
		 				}
		 		}
		 	});
	 }
 });
 
 
 
 	function countUpdatedReferences(select,j)
 	{
 		var tempCount = 0;
 		if(idsRefMap[select] && idsRefMap[select][j]!="")
 			{
 				var div = $("<div>",{id :"tblTemp"});
 				div.html('<table>'+idsRefMap[select][j]+'</table>');
 				div.find('tr').each(function(ind,row){
 					var row = $(row);
 					var status = row.find('.usptoStatus').text().trim();
 					if(!status)
 					{
 						return true;
 					}
 					else if(status=='ACCEPTED' || status=='REJECTED')
	 				{
 						refStatusMap[row.find('.flowId').val()] = row.find('.usptoStatus').text().trim();
	 					tempCount++;
	 				}
 				});
 			}
 		return tempCount;
 	}
 
 $('#btnCloseReference').on('click',function(){
	 window.location.href = $("#contextPath").val() + "/ids/dashboard";
 });
	
	
	
 /****************************** update reference status **********************************************/
	
	$('#lnkUsPub').on('click', function(){
		 
		 if($('#refUsPubBody').html().trim()=="") {
			 fetchPUSReferences();
		 }
  });
  
  function fetchPUSReferences() {
 	 
 	 var idsId = $('#idsId').val();
   	var target = $("#contextPath").val() + "/ids/userAction/updateRefStatus/getRefDetails";;
   	 $.ajax({
			type : "POST",
			url : target,
			data : {"idsId" : idsId , "refType" : "US_PUBLICATION"},
			success : function(response) {
				$('#refUsPubBody').append(response);
				$(".idsFillingDate").hide();
				bindCheckBoxClick();
			}
   	});	 
  }
  
	 
	$('#lnkFp').on('click', function(){
		 
		 if($('#refFpBody').html().trim()=="") {
			 fetchFPReferences();
		 }
  });
	
	
	
	function fetchFPReferences() {
		var idsId = $('#idsId').val();
  	var target = $("#contextPath").val() + "/ids/userAction/updateRefStatus/getRefDetails";;
  	 $.ajax({
			type : "POST",
			url : target,
			data : {"idsId" : idsId , "refType" : "FP"},
			success : function(response) {
				$('#refFpBody').append(response);
				$(".idsFillingDate").hide();
				bindCheckBoxClick();
			}
  	});	 
	}
	
	$('#lnkNpl').on('click', function(){
		 
		 if($('#refNplBody').html().trim()=="") {
			 $(".idsFillingDate").hide();
			 fetchNPLReferences(); 
		 }
 });
	
	function fetchNPLReferences() {
		var idsId = $('#idsId').val();
  	var target = $("#contextPath").val() + "/ids/userAction/updateRefStatus/getRefDetails";;
  	 $.ajax({
			type : "POST",
			url : target,
			data : {"idsId" : idsId , "refType" : "NPL"},
			success : function(response) {
				$('#refNplBody').append(response);
				$(".idsFillingDate").hide();
				bindCheckBoxClick();
			}
  	});	
	}
	
	
	$(function () {
	 
     $(".hidden-row").hide();
     $(".has-hidden-row span").on("click", function () {

         if ($(this).hasClass("icon-plus")) {
             $(this).parents(".has-hidden-row").addClass("active");
             $(this).removeClass("icon-plus").addClass("icon-minus");
             $(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
         } else if ($(this).hasClass("icon-minus")) {
             $(this).parents(".has-hidden-row").removeClass("active");
             $(this).removeClass("icon-minus").addClass("icon-plus");
             $(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
         }


     });

 });
	
	
	

	function bindCheckBoxClick() {
     $('#datetimepicker3, #datetimepicker4').hide();
     $('#datetimepicker, #datetimepicker2, #datetimepicker3, #datetimepicker4').datepicker({
          format: "M dd, yyyy"
     });
     
     $(".idsFillingDate").hide();
		$('.citedIDSRadio').click(function () {
	    	if($(this).is(":checked")){
	            $(this).parents("td").find(".idsFillingDate").show();
	          }
	          else{
	             $(this).parents("td").find(".idsFillingDate").hide();
	          }
	    });

     $('.citedIDSRadio2').click(function () {
     	if($(this).is(":checked")){
             $(this).parents("td").find(".idsFillingDate").show();
           }
           else{
              $(this).parents("td").find(".idsFillingDate").hide();
           }
     });
      $('.citedIDSRadio3').click(function () {
     	 if($(this).is(":checked")){
              $(this).parents("td").find(".idsFillingDate").show();
            }
            else{
               $(this).parents("td").find(".idsFillingDate").hide();
            }
     });
       $('#citedIDSRadio4').click(function () {
     	  if($(this).is(":checked")){
               $(this).parents("td").find(".idsFillingDate").show();
             }
             else{
                $(this).parents("td").find(".idsFillingDate").hide();
             }
     });
 }

 $(function () {
     $('.icon-calendar').click(function () {
         $(document).ready(function () {
             $("#config-demo").daterangepicker({
                 opens: 'left'
             }).focus();
         });
     });
     /////////////////////
     $(".header .search-input .form-control").on("focus", function () {
         $(this).parents(".search-input").addClass("active");
     });
     $(".header .search-input .form-control").on("focusout", function () {
         $(this).parents(".search-input").removeClass("active");
     });

     /////////////////////////
     $("#gotoSearch").on("click", function () {
         window.location.assign("search.html");
     });
     /////////////////////////

     var searchHeight = $('.search-dropdown').height();

     $(".search-control .icon-search").on("click", function () {
         $('.search-dropdown').slideToggle("show");
     });

     $("#hideSearch").on("click", function () {
         $(this).parents(".search-input").removeClass("active");
         $(".search-dropdown").slideUp("hide");
     });



     //tooltip
     $(function () {
         $('[data-toggle="tooltip"]').tooltip();
         $('.has-error .form-control').tooltip({
             trigger: 'manual'
         }).tooltip('show');
         $('.has-error .form-control').on('focus', function () {
             $(this).tooltip('destroy');
         });
     })

 });

 jQuery(document).ready(function ($) {
     $('.multiselect').multiselect();
 });


 $("#createNewAccess").on("click", function () {
     window.location.assign("create-new-access-profile.html");
 });

 $("#createNewUser").on("click", function () {
     window.location.assign("create-new-user.html");
 });

 $("#duplicateAcessProfile").on("click", function () {
     window.location.assign("user-management.html");
 });

 jQuery(document).ready(function ($) {
     $('#newRole').on("click", function () {
         if ($(this).is(":checked")) {
             window.location.assign("create-new-role.html");
         }
     });
 });

 $(function () {
     $("#familyGrid, #familyGrid_AllRecord, #familyGrid_inactiveRecord").hide();
     $(".switch-control input[type='checkbox']").on("click", function () {
         if ($(".switch-control.mdm input[type='checkbox']").is(":checked")) {
             $("#applicationGrid, #applicationGrid_AllRecord, #applicationGrid_inactiveRecord").show();
             $("#familyGrid, #familyGrid_AllRecord, #familyGrid_inactiveRecord").hide();
         } else {
             $("#applicationGrid, #applicationGrid_AllRecord, #applicationGrid_inactiveRecord").hide();
             $("#familyGrid, #familyGrid_AllRecord, #familyGrid_inactiveRecord").show();
         }
     });

 });

 //date range picker js
 $(document).ready(function () {
     $(".daterange-picker .date").daterangepicker({
         opens: 'left',
         autoUpdateInput: false,
         locale: {
             format: 'MMM DD, YYYY',
             cancelLabel: 'Clear'
         }
     });
     $('.daterange-picker i').click(function () {
         $(this).parents(".daterange-picker").find('input').click();
     });

     $('.daterange-picker .date').on('cancel.daterangepicker', function (ev, picker) {
         $(this).parents(".daterange-picker").find('input').val('');
     });

     $('input[name="datefilter"]').on('apply.daterangepicker', function (ev, picker) {
         $(this).val(picker.startDate.format('MMM DD, YYYY') + ' - ' + picker.endDate.format('MMM DD, YYYY'));
     });

     $('input[name="datefilter"]').on('cancel.daterangepicker', function (ev, picker) {
         $(this).val('');
     });
 });
 
</script>