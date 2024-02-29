//alert("SCRIPT FILE RUNNNING")


const toggleSidebar =()=>{
	
	if($('.sidebar').is(":visible"))
	{
		// close sidebar 
		$(".sidebar").css("display","none");
		//$(".fa-bars").css("display","block");
		$(".content").css("margin-left","0%");
	}
	else
	{
		// show sidebar
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
		//$(".fa-bars").css("display","none");
		
	}
	
};