<?php __('Fonts', 'constructor'); // required for correct translation ?>
<script type="text/javascript">
/* <![CDATA[ */
(function($){
$(document).ready(function(){
    $(".constructor-font-family").change(function(){
		var font = $(this).find("option:selected").html();
        var name = $(this).parent('td').attr('title');

        $('#font-'+name).css({'font-family':font});

        if ($(this).find("option:selected").is('.webfonts')) {
            loadFont(font);
        }
	});
    $(".constructor-font-size").change(function(){
		var size = $(this).find("option:selected").html();
        var name = $(this).parent('td').attr('title');

        $('#font-'+name).css({'font-size':size});
	});
    $(".constructor-font-weight").change(function(){
		var weight = $(this).find("option:selected").html();
        var name = $(this).parent('td').attr('title');

        $('#font-'+name).css({'font-weight':weight});
	});
    $(".constructor-font-transform").change(function(){
		var transform = $(this).find("option:selected").html();
        var name = $(this).parent('td').attr('title');

        $('#font-'+name).css({'text-transform':transform});
	});


});

})(jQuery);
var loaded = new Array();
function loadFont(font) {
    if (loaded.has(font)) return true;
    loaded.push(font);
    font = font.replace(/\"/gi, '');
    font = font.replace(/ /gi, '+');

    jQuery('head').append("<link href='http://fonts.googleapis.com/css?family="+font+"' rel='stylesheet' type='text/css'>");
}
/* ]]> */
</script>
<table class="form-table">
    <tr>
        <td colspan="2" class="font-example">
            <h1 id="font-title" style='
                    color:<?php echo $constructor['fonts']['title']['color']?>;
                    font-family:<?php echo $constructor['fonts']['title']['family']?>;
                    font-weight:<?php echo $constructor['fonts']['title']['weight']?>;
                    font-size:<?php echo $constructor['fonts']['title']['size']?>px;
                    text-transform:<?php echo $constructor['fonts']['title']['transform']?>;
                    '>
                <?php echo bloginfo('name');?>
            </h1>
            <h2 id="font-description" style='
                    color:<?php echo $constructor['fonts']['description']['color']?>;
                    font-family:<?php echo $constructor['fonts']['description']['family']?>;
                    font-weight:<?php echo $constructor['fonts']['description']['weight']?>;
                    font-size:<?php echo $constructor['fonts']['description']['size']?>px;
                    text-transform:<?php echo $constructor['fonts']['description']['transform']?>;
                    '>
                <?php echo bloginfo('description'); ?>
            </h2>
            <h3 id="font-header" style='font-family:<?php echo $constructor['fonts']['header']['family']?>;'>
                <?php _e('The quick brown fox jumps over the lazy dog', 'constructor');?>
            </h3>
            <p id="font-content" style='font-family:<?php echo $constructor['fonts']['content']['family']?>;'>
                <?php _e('Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. 0123456789', 'constructor');?>
            </p>
        </td>
        <td rowspan="5" valign="top" class="updated quick-links" width="320px">
            <h3><?php _e('Font Weight', 'constructor') ?></h3>
            <p><?php _e('Defines from thin to thick characters. 400 is the same as "normal", and 700 is the same as "bold"', 'constructor') ?>
            </p>

            <h3><?php _e('Text Decoration', 'constructor') ?></h3>
            <ul>
              <li><strong>none</strong> - <?php _e('No capitalization. The text renders as it is. This is default', 'constructor') ?></li>
              <li><strong>capitalize</strong> - <?php _e('Transforms the first character of each word to uppercase', 'constructor') ?></li>
              <li><strong>uppercase</strong> - <?php _e('Transforms all characters to uppercase', 'constructor') ?></li>
              <li><strong>lowercase</strong> - <?php _e('Transforms all characters to lowercase', 'constructor') ?></li>
            </ul>
		</td>
    </tr>
    <tr>
        <th scope="row" valign="top" class="th-full"><?php _e('Title', 'constructor'); ?></th>
		<td valign="top" class="color-selector" title="title">
		    <?php $this->getFontColor('title') ?>
		    <?php $this->getFontFamily('title') ?>
		    <?php $this->getFontSize('title') ?><br/>
		    <?php _e('Font Weight', 'constructor') ?>: <?php $this->getFontWeight('title') ?>
		    <?php _e('Text Decoration', 'constructor') ?>: <?php $this->getFontTransform('title') ?>
		</td>

	</tr>
    <tr>
        <th scope="row" valign="top" class="th-full"><?php _e('Description', 'constructor'); ?></th>
		<td valign="top" class="color-selector" title="description">
		    <?php $this->getFontColor('description') ?>
		    <?php $this->getFontFamily('description') ?>
		    <?php $this->getFontSize('description') ?><br/>
		    <?php _e('Font Weight', 'constructor') ?>: <?php $this->getFontWeight('description') ?>
		    <?php _e('Text Decoration', 'constructor') ?>: <?php $this->getFontTransform('description') ?>
		</td>
	</tr>
    <tr>
        <th scope="row" valign="top" class="th-full"><?php _e('Headers', 'constructor'); ?></th>
        <td valign="top" title="header">
            <?php $this->getFontFamily('header') ?>
        </td>
    </tr>
    <tr>
        <th scope="row" valign="top" class="th-full"><?php _e('Content', 'constructor'); ?></th>
        <td valign="top" title="content">
            <?php $this->getFontFamily('content') ?>
        </td>
    </tr>
</table>