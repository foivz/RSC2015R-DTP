jQuery(document).ready(function($) {
	var Menu =
	{
		el: 
		{
			$icon: $('.menu-container__icon'),
			$iconTop: $('.menu-icon--top'),
			$iconMiddle: $('.menu-icon--middle'),
			$iconBottom: $('.menu-icon--bottom'),
			$innerContent: $('.flip-inner'),
			$menuLinks: $('nav ul li a')
		},   

		init: function()
		{
			this.BindActions();
		},

		BindActions: function()
		{
			this.el.$icon.on('click', function(){
				Menu.actions();
			});
			this.el.$menuLinks.on('click', function(){
				Menu.actions();
			})
		},

		actions: function()
		{
			this.el.$iconTop.toggleClass('menu-top-action');
			this.el.$iconMiddle.toggleClass('menu-middle-action');
			this.el.$iconBottom.toggleClass('menu-bottom-action');
			this.el.$icon.toggleClass('menu-rotate');			
			this.el.$innerContent.toggleClass('flip-inner-action');	
		}
	}
	Menu.init();
});