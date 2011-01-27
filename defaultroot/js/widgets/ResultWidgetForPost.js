(function ($) {

AjaxSolr.ResultWidget = AjaxSolr.AbstractWidget.extend({
  beforeRequest: function () {
    $(this.target).html($('<img/>').attr('src', 'images/ajax-loader.gif'));
  },

  facetLinks: function (facet_field, facet_values) {
    var links = [];
    if (facet_values) {
      for (var i = 0, l = facet_values.length; i < l; i++) {
        links.push(AjaxSolr.theme('facet_link', facet_values[i], this.facetHandler(facet_field, facet_values[i])));
      }
    }
    return links;
  },

  facetHandler: function (facet_field, facet_value) {
    var self = this;
    return function () {
      self.manager.store.remove('fq');
      self.manager.store.addByValue('fq', facet_field + ':' + facet_value);
      self.manager.doRequest(0);
      return false;
    };
  },

  afterRequest: function () {
    $(this.target).empty();
    
    if (!this.manager.response) { return; }
    
    for (var i = 0, l = this.manager.response.length; i < l; i++) {
      var doc = this.manager.response[i];
      $(this.target).append(AjaxSolr.theme('result', doc, AjaxSolr.theme('snippet', doc)));

      var items = [];
      items = items.concat(this.facetLinks('topics', doc.topics));
      items = items.concat(this.facetLinks('organisations', doc.organisations));
      items = items.concat(this.facetLinks('exchanges', doc.exchanges));
      AjaxSolr.theme('list_items', '#links_' + doc.id, items);
    }
    
    //for load images to work
    LoadAllImages();
  },

  init: function () {
    $('a.more').livequery(function () {
      $(this).toggle(function () {
        $(this).parent().find('span').show();
        $(this).text('less');
        return false;
      }, function () {
        $(this).parent().find('span').hide();
        $(this).text('more');
        return false;
      });
    });
    
    /* copies the displayed text to the form */
    $('a.copy').livequery('click', function () {
    	var self = this;
    	
    	var title = $(this.parentNode.parentNode).find("#title").html();
    	var isbn = $(this.parentNode.parentNode).find("#isbn").html();
    	var isbn13 = $(this.parentNode.parentNode).find("#isbn13").html();
    	var authors = $(this.parentNode.parentNode).find("#authors").html();
    	var publisher = $(this.parentNode.parentNode).find("#publisher").html();
    	var notes = $(this.parentNode.parentNode).find("#notes").html();
    	var summary = $(this.parentNode.parentNode).find("#summary").html();
    	

    	//set it in the form
    	$('#postform').find("#title").val(title);
    	$('#postform').find("#isbn").val(isbn);
    	$('#postform').find("#author").val(authors);
    	$('#postform').find("#publisher").val(publisher);
    	$('#postform').find("#desc").val(summary);

    	
    });
    
  }
  
});

})(jQuery);