(function ($) {

AjaxSolr.AutocompleteWidget = AjaxSolr.AbstractFacetWidget.extend({
  init: function () {
    var self = this;

    $(this.target).find('input.searchable').bind('keydown', function(e) {
      if (true) {///*self.requestSent === false &&*/ e.which == 13) {
    	var key = $(this).attr('id');
    	
    	if (key=='price') {/*do not validate, in future implementation to pull amazon sellers record. so that sellers can price items intelligently*/
    		return;
    	}
    	
    	if (key==='author') { 
    		key = 'full';	//since author not supported by isbndb.com api
    	}
    	
        var value = "index1=" + key + "&value1=" + $(this).val();
        if (value && self.manager.store.addByValue('q', value)) {
          self.manager.doRequest(0);
        }
      }
    });
  },

  afterRequest: function () {
    //$(this.target).find('input').val('');

    var self = this;

    var callback = function (response) {
      var list = [];
      for (var i = 0; i < self.fields.length; i++) {
        var field = self.fields[i];
        for (var facet in response.facet_counts.facet_fields[field]) {
          list.push({
            field: field,
            value: facet,
            text: facet + ' (' + response.facet_counts.facet_fields[field][facet] + ') - ' + field
          });
        }
      }

      self.requestSent = false;
      $(self.target).find('input').autocomplete(list, {
        formatItem: function(facet) {
          return facet.text;
        }
      }).result(function(e, facet) {
        self.requestSent = true;
        if (self.manager.store.addByValue('fq', facet.field + ':' + facet.value)) {
          self.manager.doRequest(0);
        }
      });
    } // end callback

    var params = [ 'q=*:*' ];
    
    jQuery.getJSON(this.manager.ajaxUrl + '?' + params.join('&') + '&wt=json&json.wrf=?', {}, callback);
  }
});

})(jQuery);
