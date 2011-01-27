(function ($) {

AjaxSolr.theme.prototype.result = function (doc, snippet) {
  var output = '<div style="display:block;min-height:120px;height:auto !important"><h2><a id="title" href="#" class="copy" title="Auto populates form">' + doc.title + '</a></h2>';
  output += '<p id="links_' + doc.isbn + '" class="links"></p>';
  output += '<div id="image-thumb" class="loadable-image" src="http://covers.openlibrary.org/b/isbn/'+doc.isbn+'-M.jpg" onload="img = $(arguments[0]); img.bind(\'click\', function(){ alert(\'Image was clicked\'); })"></div>';
  output += '<p>' + snippet + '</p></div><div class="clear"></div>';
  return output;
};

AjaxSolr.theme.prototype.snippet = function (doc) {
  var output = '';

 
  if (doc.summary.length > 200) {
    output += '<u>ISBN:</u> <span id="isbn">' + doc.isbn + '</span>, <u>ISBN13:</u> <span id="isbn13">' + doc.isbn13 + '</span><br/>';
    output += '<u>Authors name:</u> <span id="authors">' + doc.authorsText + '</span><br/>';
    output += '<u>Publisher name:</u> <span id="publisher">' + doc.publisherText.content + '</span>, ' +  doc.publisherText.publisherId + '<br/>';
    output += '<u>Notes:</u> <span id="notes">' + doc.notes + '</span><br/>';
    output += '<u>Summary:</u> <span id="summary">' + doc.summary.substring(0,200) + '</span><br/>';
    output += '<span style="display:none;">' + doc.summary.substring(200);
    output += '</span> <a href="#" class="more">more</a>';
    
  }
  else {
	  output += '<u>ISBN:</u> <span id="isbn">' + doc.isbn + '</span>, <u>ISBN13:</u> <span id="isbn13">' + doc.isbn13 + '</span><br/>';
	  output += '<u>Authors name:</u> <span id="authors">' + doc.authorsText + '</span><br/>';
	  output += '<u>Publisher name:</u> <span id="publisher">' + doc.publisherText.content + '</span>, ' +  doc.publisherText.publisherId + '<br/>';
	  output += '<u>Notes:</u> <span id="notes">' + doc.notes + '</span><br/>';
	  output += '<u>Summary:</u> <span id="summary">' + doc.summary + '</span><br/>';
  }
  
  return output;
};

AjaxSolr.theme.prototype.tag = function (value, weight, handler) {
  return $('<a href="#" class="tagcloud_item"/>').text(value).addClass('tagcloud_size_' + weight).click(handler);
};

AjaxSolr.theme.prototype.facet_link = function (value, handler) {
  return $('<a href="#"/>').text(value).click(handler);
};

AjaxSolr.theme.prototype.no_items_found = function () {
  return 'no items found in current selection';
};

})(jQuery);
