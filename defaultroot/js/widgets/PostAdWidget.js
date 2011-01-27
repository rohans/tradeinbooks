(function ($) {

AjaxSolr.PostAdWidget = AjaxSolr.AbstractWidget.extend({
	  
  init: function () {
    var self = this;

    $(this.target).find('#postthisbook').bind('click', function(e) { 
    	/*check to see if all the form fields are valid using tasty, if false skip*/
    	if($.ketchup.allFieldsValid($(self.target), true)==false) { return; }
    	
  		var price = $(self.target).find('#price').val();
  	    var title = $(self.target).find('#title').val();
  	    var isbn = $(self.target).find('#isbn').val();
  	    var author = $(self.target).find('#author').val();
  	    var publisher = $(self.target).find('#publisher').val();

  	    if ( self.manager.store.addByValue('p', price) && self.manager.store.addByValue('t', title) &&
  	    	 self.manager.store.addByValue('i', isbn) && self.manager.store.addByValue('a', author) &&
  	    	 self.manager.store.addByValue('pb', publisher) ) {
  	    	self.manager.doRequest(0);
  	    	$(':input', self.target).not(':button, :submit, :reset, :hidden')
  	       	.val('').removeAttr('checked').removeAttr('selected');
  	    	$('.ketchup-error').remove();/* hides all the errors displayed by ketchup validation*/
  	    	$('#postresult').html('Book posted successfully.');
        }
        
    });
    
    /*clears the form elements when reset button is clicked*/
    $(this.target).find('#resetposting').bind('click', function() {
    	$(':input', self.target).not(':button, :submit, :reset, :hidden')
       	.val('').removeAttr('checked').removeAttr('selected');
	    	
    });

  },
  
  afterRequest: function () {
    var self = this;
    $(self.target).find('input').val('');
    $('#postresult').html('Book posted successfully.');
  },

  removeFacet: function (facet) {
    var self = this;
    return function () {
      if (self.manager.store.removeByValue('fq', facet)) {
        self.manager.doRequest(0);
      }
      return false;
    };
  }
});

})(jQuery);
