<?php __('Layout', 'constructor'); // required for correct translation ?>
<script type="text/javascript">
/* <![CDATA[ */
(function($){
    $(document).ready(function (){

        var width    = $('#constructor-layout-width').val();
        var sidebar  = $('#constructor-layout-sidebar').val();
        var extrabar = $('#constructor-layout-extra').val();
        var header   = $('#constructor-layout-header').val();

        $('#layouts a').click(function(){
            var layout = $(this).attr('name');
            $('#layouts a').removeClass('selected');
            $(this).addClass('selected');

            $('#constructor-sidebar').val(layout);

            $('.layout-main-content').hide();
            $('#layout-'+layout).show(100, function(){
                createLayout(layout, width, sidebar, extrabar, header);
            });
            return false;
        });

        $("#tabs").bind("tabsselect", function(event, ui) {
            if (ui.tab.name == "layout") {
                $('#layouts a[name=<?php echo $constructor['sidebar']?>]').click();
            }
        });
    });

    /*
    content - real 640-1280; view 320-640
    sidebar - real 120-400; view 60-200
    extrabar - real 120-400; view 60-200
    header - real 40-320; view 20-160
    */
    function createLayout(name       /*layout name (none|left|right|two|two-left|two-right)*/
                        , container  /*content size in px*/
                        , sidebar    /*sidebar size in px*/
                        , extrabar   /*extrabar size in px*/
                        , header     /*header height in px*/
                        ) {
       var margin = (1280 /*max content*/ - container /*need content*/ + 82)/4;
       var east = 120 /*default*/;
       var west = 120 /*default*/;
       var north = header / 2;
       var mainContentFlag = true;

       switch (name) {
           case 'none':
                break;
           case 'left':
                west = sidebar/2;
                break;
           case 'right':
                east = sidebar/2;
                break;
           case 'two':
                west = extrabar/2;
                east = sidebar/2;
                break;
           case 'two-left':
                west = sidebar/2;
                east = (container - sidebar - extrabar) / 2;
                break;
           case 'two-right':
                west = (container - sidebar - extrabar) / 2;
                east = extrabar/2;
                break;
       }
       var mainContent =  jQuery("#layout-"+name).layout({
                           center__paneSelector: ".layout-content"
                           , east__paneSelector: ".margin-east"
                           , west__paneSelector: ".margin-west"
                           , closable:  false // pane can open & close
                           , resizable:  true // when open, pane can be resized
                           , slidable:  false
                           , resizeWhileDragging: true
                           , west__resizable: true // Set to TRUE to activate dynamic margin
                           , east__resizable: true // Set to TRUE to activate dynamic margin
                           , east__resizerClass: 'resizer-east'
                           , west__resizerClass: 'resizer-west'
                           , east__size:  margin
                           , west__size:  margin
                           , east__minSize:  20
                           , west__minSize:  20
                           , east__maxSize:  196
                           , west__maxSize:  196
                           , west__onresize: function (pane, $Pane, paneState){
                                if (mainContentFlag) {
                                    mainContentFlag = false;
                                    mainContent.sizePane('east', paneState.size);
                                } else {
                                    mainContentFlag = true;
                                }
                                updateView(name);Content.resizeAll();
                           }
                           , east__onresize: function (pane, $Pane, paneState){
                                if (mainContentFlag) {
                                    mainContentFlag = false;
                                    mainContent.sizePane('west', paneState.size);
                                } else {
                                    mainContentFlag = true;
                                }
                                updateView(name);Content.resizeAll();
                           }
                       });

       var Content = jQuery("#layout-"+name+" .layout-content").layout({
                               closable: false
                               , resizable: true
                               , slidable: false
                               , north__resizable:true
                               , south__resizable:false
                               , resizeWhileDragging:true
                               , north__resizerClass:'graver'
                               , east__resizerClass:'graver'
                               , west__resizerClass:'graver'
                               , north__minSize:20
                               , east__minSize:60
                               , west__minSize:60
                               , north__size:north
                               , east__size:east
                               , west__size:west
                               , north__onresize: function (pane, $Pane, paneState){
                                    updateView(name);
                               }
                               , west__onresize: function (pane, $Pane, paneState){
                                    updateView(name);
                               }
                               , east__onresize: function (pane, $Pane, paneState){
                                    updateView(name);
                               }
                            });
        updateView(name);
    }

    function updateView(name) {

        var $layout = $("#layout-"+name);
        var marginwidth = $layout.find(".margin-east").width();
        var content = $layout.width() - (marginwidth*2 + 12);

        $("#layout-"+name+" .layout-content").css({left:marginwidth+6});


        var west = $("#layout-"+name+" .west").width() || 0;
        var east = $("#layout-"+name+" .east").width() || 0;
        var north = $("#layout-"+name+" .north").height() || 0;
        var center = content - east - west;

        $("#layout-"+name+" .north .layout-size").html(north*2);
        $("#layout-"+name+" .center .layout-size").html(center*2);
        $("#layout-"+name+" .east .layout-size").html(east*2);
        $("#layout-"+name+" .west .layout-size").html(west*2);
        $("#layout-"+name+" .ui-layout-south .layout-size").html(content*2);


       switch (name) {
           case 'none':
           case 'right':
           case 'two':
                sidebar = east*2;
                extrabar = west*2;
                break;
           case 'left':
                sidebar = west*2;
                extrabar = east*2;
                break;
           case 'two-left':
                sidebar = west*2;
                extrabar = center*2;
                break;
           case 'two-right':
                sidebar = center*2;
                extrabar = east*2;
                break;
       }

        $('#constructor-layout-width').val((center+east+west)*2);
        $('#constructor-layout-sidebar').val(sidebar||120);
        $('#constructor-layout-extra').val(extrabar||120);
        $('#constructor-layout-header').val(north*2);
    }
})(jQuery);
/* ]]> */
</script>
<input type="hidden" id="constructor-sidebar" name="constructor[sidebar]" value="<?php echo $constructor['sidebar']?>"/>
<table class="form-table">
<tr>
	<tr>
        <td id="layouts" colspan="2" class="select">
            <a href="#none" class="el0" name="none" <?php if($constructor['sidebar'] == 'none') echo 'class="selected"'; ?>><?php echo __('None', 'constructor'); ?></a>
            <a href="#left" class="el1" name="left" <?php if($constructor['sidebar'] == 'left') echo 'class="selected"'; ?>><?php echo __('Left', 'constructor'); ?></a>
            <a href="#right" class="el2" name="right" <?php if($constructor['sidebar'] == 'right') echo 'class="selected"'; ?>><?php echo __('Right', 'constructor'); ?></a>
            <a href="#two" class="el3" name="two" <?php if($constructor['sidebar'] == 'two') echo 'class="selected"'; ?>><?php echo __('Two', 'constructor'); ?></a>
            <a href="#two-left" class="el4" name="two-left" <?php if($constructor['sidebar'] == 'two-left') echo 'class="selected"'; ?>><?php echo __('Two Left', 'constructor'); ?></a>
            <a href="#two-right" class="el5" name="two-right" <?php if($constructor['sidebar'] == 'two-right') echo 'class="selected"'; ?>><?php echo __('Two Right', 'constructor'); ?></a>
        </td>
	</tr>
    <tr>
        <td>
            <div class="layout-preview">
            <div id="layout-none" class="layout-main-content">
                <div class="layout-content">
                    <div class="ui-layout-center center content">
                        <div class="layout-info">Content <span>(<span class="layout-size">480</span>px)</span></div>
                    </div>
                    <div class="ui-layout-south south">
                        <div class="layout-info">Footer <span>(<span class="layout-size">960</span>px)</span></div>
                    </div>
                    <div class="ui-layout-north north">
                        <div class="layout-info">Header <span>(<span class="layout-size">40</span>px)</span></div>
                    </div>
                </div>
                <div class="margin-west">
                    <div class="layout-info">Margin</div>
                </div>
                <div class="margin-east">
                    <div class="layout-info">Margin</div>
                </div>
            </div>
            <div id="layout-two" class="layout-main-content">
                <div class="layout-content">
                    <div class="ui-layout-west west extrabar">
                        <div class="layout-info">Extrabar <span>(<span class="layout-size">240</span>px)</span></div>
                    </div>
                    <div class="ui-layout-center center content">
                        <div class="layout-info">Content <span>(<span class="layout-size">480</span>px)</span></div>
                    </div>
                    <div class="ui-layout-east east sidebar">
                        <div class="layout-info">Sidebar <span>(<span class="layout-size">240</span>px)</span></div>
                    </div>
                    <div class="ui-layout-south south">
                        <div class="layout-info">Footer <span>(<span class="layout-size">960</span>px)</span></div>
                    </div>
                    <div class="ui-layout-north north">
                        <div class="layout-info">Header <span>(<span class="layout-size">40</span>px)</span></div>
                    </div>
                </div>
                <div class="margin-west">
                    <div class="layout-info">Margin</div>
                </div>
                <div class="margin-east">
                    <div class="layout-info">Margin</div>
                </div>
            </div>
            <div id="layout-left" class="layout-main-content">
                <div class="layout-content">
                    <div class="ui-layout-west west sidebar">
                        <div class="layout-info">Sidebar <span>(<span class="layout-size">240</span>px)</span></div>
                    </div>
                    <div class="ui-layout-center center content">
                        <div class="layout-info">Content <span>(<span class="layout-size">480</span>px)</span></div>
                    </div>
                    <div class="ui-layout-south south">
                        <div class="layout-info">Footer <span>(<span class="layout-size">960</span>px)</span></div>
                    </div>
                    <div class="ui-layout-north north">
                        <div class="layout-info">Header <span>(<span class="layout-size">40</span>px)</span></div>
                    </div>
                </div>
                <div class="margin-west">
                    <div class="layout-info">Margin</div>
                </div>
                <div class="margin-east">
                    <div class="layout-info">Margin</div>
                </div>
            </div>
            <div id="layout-right" class="layout-main-content">
                <div class="layout-content">
                    <div class="ui-layout-east east sidebar">
                        <div class="layout-info">Sidebar <span>(<span class="layout-size">240</span>px)</span></div>
                    </div>
                    <div class="ui-layout-center center content">
                        <div class="layout-info">Content <span>(<span class="layout-size">480</span>px)</span></div>
                    </div>
                    <div class="ui-layout-south south">
                        <div class="layout-info">Footer <span>(<span class="layout-size">960</span>px)</span></div>
                    </div>
                    <div class="ui-layout-north north">
                        <div class="layout-info">Header <span>(<span class="layout-size">40</span>px)</span></div>
                    </div>
                </div>
                <div class="margin-west">
                    <div class="layout-info">Margin</div>
                </div>
                <div class="margin-east">
                    <div class="layout-info">Margin</div>
                </div>
            </div>

            <div id="layout-two-left" class="layout-main-content">
                <div class="layout-content">
                    <div class="ui-layout-west west sidebar">
                        <div class="layout-info">Sidebar <span>(<span class="layout-size">240</span>px)</span></div>
                    </div>
                    <div class="ui-layout-center center extrabar">
                        <div class="layout-info">Extrabar <span>(<span class="layout-size">240</span>px)</span></div>
                    </div>
                    <div class="ui-layout-east east content">
                        <div class="layout-info">Content <span>(<span class="layout-size">480</span>px)</span></div>
                    </div>
                    <div class="ui-layout-south south">
                        <div class="layout-info">Footer <span>(<span class="layout-size">960</span>px)</span></div>
                    </div>
                    <div class="ui-layout-north north">
                        <div class="layout-info">Header <span>(<span class="layout-size">40</span>px)</span></div>
                    </div>
                </div>
                <div class="margin-west">
                    <div class="layout-info">Margin</div>
                </div>
                <div class="margin-east">
                    <div class="layout-info">Margin</div>
                </div>
            </div>
            <div id="layout-two-right" class="layout-main-content">
                <div class="layout-content">
                    <div class="ui-layout-center center sidebar">
                        <div class="layout-info">Sidebar <span>(<span class="layout-size">240</span>px)</span></div>
                    </div>
                    <div class="ui-layout-west west content">
                        <div class="layout-info">Content <span>(<span class="layout-size">480</span>px)</span></div>
                    </div>
                    <div class="ui-layout-east east extrabar">
                        <div class="layout-info">Extrabar <span>(<span class="layout-size">240</span>px)</span></div>
                    </div>
                    <div class="ui-layout-south south">
                        <div class="layout-info">Footer <span>(<span class="layout-size">960</span>px)</span></div>
                    </div>
                    <div class="ui-layout-north north">
                        <div class="layout-info">Header <span>(<span class="layout-size">40</span>px)</span></div>
                    </div>
                </div>
                <div class="margin-west">
                    <div class="layout-info">Margin</div>
                </div>
                <div class="margin-east">
                    <div class="layout-info">Margin</div>
                </div>
            </div>
            </div>
        </td>
        <td id="layout-results" width="200px" valign="top">
            <fieldset>
                <legend>Size</legend>
                <?php _e('Width', 'constructor')?>: <input type="text" id="constructor-layout-width" class="tiny" name="constructor[layout][width]" value="<?php echo $constructor['layout']['width']?>" />px
                <br/>
                <?php _e('Sidebar Width', 'constructor'); ?>:<input type="text" id="constructor-layout-sidebar" class="tiny" name="constructor[layout][sidebar]" value="<?php echo $constructor['layout']['sidebar']?>" />px
                <br/>
                <?php _e('Extrabar Width', 'constructor'); ?>:<input type="text" id="constructor-layout-extra" class="tiny" name="constructor[layout][extra]" value="<?php echo $constructor['layout']['extra']?>" />px
                <br/>
                <?php _e('Header Height', 'constructor')?>: <input type="text" id="constructor-layout-header" class="tiny" name="constructor[layout][header]" value="<?php echo $constructor['layout']['header']?>" />px

            </fieldset>
        </td>
	</tr>
</table>