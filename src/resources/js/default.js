$(function(){
	
	$("body").prepend("<div class='overlay'></div>");
	
	$(".navbar-toggle").on("click",function(){
		if (!$(".navbar-collapse").hasClass("in")) {
			$(".overlay").show();
		} else {
			$(".overlay").hide();
		}
	});
	
	$(".overlay").on("click",function(){
		$(".navbar-toggle").trigger("click");
	});
	
	
	
	$(document).on('click', 'a.anchor', function(event){
		event.preventDefault();

		$('html, body').animate({
			scrollTop: $( $.attr(this, 'href') ).offset().top
		}, 500);
	});
	
});