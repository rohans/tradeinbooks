(function ($) {
	
AjaxSolr.theme.prototype.result = function (doc, snippet) {
  var output = '<div style="display:block;"><h2><a href="/tradeinbooks/detail.html">' + doc.title + '</a></h2>';
  output += '<p id="links_' + doc.id + '" class="links"></p>';
  //output += '<img src="http://covers.openlibrary.org/b/isbn/9780385533225-M.jpg" height="81" width="81" align="left" /><p>' + snippet + '</p></div>';
  //output += '<div id="image-thumb" class="loadable-image" src="/tradeinbooks/imagescale?'+pick_image()+'" onload="img = $(arguments[0]); img.bind(\'click\', function(){ alert(\'Image was clicked\'); })"></div>';
  output += '<div id="image-thumb" class="loadable-image" src="http://covers.openlibrary.org/b/isbn/'+doc.isbn+'-M.jpg" onload="img = $(arguments[0]); img.bind(\'click\', function(){ alert(\'Image was clicked\'); })"></div>';
  output += '<p>' + snippet + '</p></div><div class="clear"></div>';
  return output;
};

AjaxSolr.theme.prototype.snippet = function (doc) {
  var output = '';
  if (doc.text.length > 300) {
    output += doc.dateline + ' ' + doc.text.substring(0, 300);
    output += '<span style="display:none;">' + doc.text.substring(300);
    output += '</span> <a href="#" class="more">more</a>';
  }
  else {
    output += doc.dateline + ' ' + doc.text;
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
