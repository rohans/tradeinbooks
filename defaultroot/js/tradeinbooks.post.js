
var NonAjaxManager;
var NonAjaxManager2;

(function ($) {

  $(function () {
	NonAjaxManager = new AjaxSolr.Manager({
		ajaxUrl: 'http://localhost:8080/tradeinbooks/ajax/postbook.jsp'
	});
    
	NonAjaxManager2 = new AjaxSolr.Manager({
		ajaxUrl: 'http://localhost:8080/tradeinbooks/ajax/getbookinfo.jsp'
    });
    
    

    	NonAjaxManager.addWidget(new AjaxSolr.PostAdWidget({
    		id: 'post',
    		target: '#postform'
    	}));/*this widget is added only if element with id post present*/
    	
    	NonAjaxManager2.addWidget(new AjaxSolr.ResultWidget({
            id: 'result',
            target: '#docs'
          }));
    	
    	NonAjaxManager2.addWidget(new AjaxSolr.AutocompleteWidget({
    	      id: 'text',
    	      target: '#postform'
    	    }));


        NonAjaxManager.init();
        NonAjaxManager2.init();
        NonAjaxManager.doRequest();
        NonAjaxManager2.doRequest();
    
        
  });

  $.fn.showIf = function (condition) {
    if (condition) {
      return this.show();
    }
    else {
      return this.hide();
    }
  };

})(jQuery);
