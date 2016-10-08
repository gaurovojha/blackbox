$(function(){
	//reference record dropdown js   
	$(document).on('click', '#reference-search-control', function (e) {
		e.stopPropagation();
	});
	$("#reference-search-control .btn-cancel").on("click", function(){
		$("body").click();
	});
	
	//submit search form
	$(document).on('click', ".ref-record-search", function(){
		if($("#referenceAppTab").hasClass("active")){
			$("#referenceAppTab form").submit();
		}else if($("#referenceAttorneyTab").hasClass("active")){
			$("#referenceAttorneyTab form").submit();
		}else if($("#referenceFamilyTab").hasClass("active")){
			$("#referenceFamilyTab form").submit();
		}
	});
	
	$(".hiddenRow").hide();
    $("#hiddenNotificationFields").hide();
    
    //more link in tr
    $(document).on('click', ".showMoreDetails", function(){
    	if(!$(this).parents("tr").next('tr').hasClass("hiddenRow")){
	    	var refEnteredBy, kindCode, comments, addClass;
	    	var ele = $(this).parents("td").siblings("td:first-child").find("input[type='hidden']");
	    	refEnteredBy = $(ele).eq(0).val() || null;
	    	kindCode = $(ele).eq(1).val() || null;
	    	comments = $(ele).eq(2).val() || null;
	    	addClass = $(this).parents("tr").attr("class");
	    	var appendTr = '<tr class="borderTB hiddenRow '+ addClass+'"> <td colspan="3"><strong>Ref. Entered by.</strong> <br /><div>'+ refEnteredBy +'</div> </td> <td colspan="1"> <strong>Kind Code :</strong> <div>'+kindCode+'</div> </td> <td	colspan="3"> <strong>Comments :</strong> <br />'+comments+'</td></tr>';
	    	$(this).parents("tr").after(appendTr);
	    	console.log("clicked", refEnteredBy);
    	}else{
    		$(this).parents("tr").next('tr').toggle();
    	}
    });
    
    $(function () {
        $(".hiddenRow").hide();
        $("#hiddenNotificationFields").hide();
        $(".showMoreDetails").on("click", function(){
            $(this).parents("tr").siblings("tr.hiddenRow").toggle();
             $(this).text(function(i, text){
                  return text === "More..." ? "Less..." : "More...";
              })
            
        });
    });
    
    
});
