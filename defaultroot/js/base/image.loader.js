

var gLoadSpinnerUrl = '/tradeinbooks/images/img-ajax-loader.gif';
var gFailImage = '/tradeinbooks/images/no-image.jpeg';

function LoadImage(pSelector, pCallback){
    var loader = $(pSelector);
    loader.html('<img src="' + gLoadSpinnerUrl + '"/>');
 
    LoadThisImage($(img), loader, pCallback);
}
 
function LoadThisImage(loader, pCallback){
    image_src = loader.attr('src');
    checkImg = new Image();
    $(checkImg).attr('src', image_src);
    if ( $(checkImg).attr('height') <= 1 ) {
    	image_src = gFailImage;
    }
    
    img = new Image();
    $(img).attr('width', 90);
    $(img).attr('height', 127);
    $(img).hide();
 
    $(img).load(function() {
        cb_js = loader.get(0).getAttribute('onload');              
        onload_cb = function(){
            eval(cb_js);
        };       
 
        loader.html(this);
        loader.removeClass('loadable-image');
        loader.addClass('loaded-image');
        loader.removeAttr('src');
        loader.removeAttr('onload');
        $(this).show(); 
        if (onload_cb){                
            onload_cb($(this));
        }              
        if (pCallback){
            cb = pCallback;
            cb($(this));
        }
    })
    .error(function() { $(this).attr('src', gFailImage).show(); })
    .attr('src', image_src)
    .show();
 
}

function LoadAllImages(){
    $('.loadable-image').each(function(){       
        var loader = $(this);
        loader.html('<img src="' + gLoadSpinnerUrl + '"/>');
        LoadThisImage(loader);
    });
}


