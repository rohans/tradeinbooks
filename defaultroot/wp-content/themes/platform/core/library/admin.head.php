<link rel="stylesheet" media="screen" type="text/css" href="<?php echo CORE_JS;?>/colorpicker/css/colorpicker.css" />
<script type="text/javascript" src="<?php echo CORE_JS;?>/colorpicker/js/colorpicker.js"></script>

<script type="text/javascript">/*<![CDATA[*/
jQuery(document).ready(function(){

	
	//Color Picker
	<?php
	
	foreach (get_option_array() as $menuitem){
		
		foreach($menuitem as $optionid => $option_info){ 
			if($option_info['type'] == 'colorpicker'){
		
				$color = pagelines_option($optionid);
		
?>
				 jQuery('#<?php echo $optionid; ?>_picker').children('div').css('backgroundColor', '<?php echo $color; ?>');    
				 jQuery('#<?php echo $optionid; ?>_picker').ColorPicker({
					color: '<?php echo $color; ?>',
					onShow: function (colpkr) {
						jQuery(colpkr).fadeIn(500);
						return false;
					},
					onHide: function (colpkr) {
						jQuery(colpkr).fadeOut(500);
						return false;
					},
					onChange: function (hsb, hex, rgb) {
				
						jQuery('#<?php echo $optionid; ?>_picker').children('div').css('backgroundColor', '#' + hex);
						jQuery('#<?php echo $optionid; ?>_picker').next('input').attr('value','#' + hex);
				
					}
				  });
<?php 	} 
		} 
	}
?>


/*
	Section Drag-Drop & Saving
*/
<?php global $pagelines_template; ?>

	var stemplate = jQuery('#tselect').val();

	jQuery('.'+stemplate).addClass('selected_template');

	setSortable(stemplate);

	jQuery('#tselect').change(function() {
	
		stemplate = jQuery(this).val();
		jQuery('.selected_template').removeClass('selected_template');
		jQuery('.'+stemplate).addClass('selected_template');
		setSortable(stemplate);
	
	});

/*
	Layout Builder Control	
*/
	// Default Layout Select
	jQuery(' .layout-select-default .layout-image-border').click(function(){
		LayoutSelectControl(this);
	});
	
	<?php 
		if( pagelines_option('layout') ) $tmap = pagelines_option('layout');
		if( isset($tmap['last_edit']) ) $last_edit = $tmap['last_edit'];
		else $last_edit = null;
	
		$load_layout = new PageLinesLayout($last_edit);
		$load_margin = $load_layout->margin->bwidth;
		$load_west = $load_layout->west->bwidth;
		$load_east = $load_layout->east->bwidth;
		$load_gutter = $load_layout->gutter->bwidth;

	?>
	setLayoutBuilder('<?php echo $load_layout->layout_map['last_edit']; ?>', <?php echo $load_margin;?>, <?php echo $load_east;?>, <?php echo $load_west;?>, 10);

	jQuery('.selected_template .layout-builder-select .layout-image-border').click(function(){
		var LayoutMode;
		var marginwidth;
		var innerwestwidth;
		var innereastwidth;
		var gtrwidth;


		// Get previous selected layout margin
		var mwidth = jQuery('.selectededitor .margin-west').width();
	
		var OldLayoutMode = jQuery('.layout-image-border.selectedlayout').next().val();
		
		// Control selector class & visualization
		LayoutSelectControl(this);
	
	
		// For Layout Builder mode e.g. 'one-sidebar-right'
		LayoutMode = jQuery(this).parent().find('.layoutinput').val();
	
		// Deactivate old builder
		jQuery('.layout_controls').find('.layouteditor').removeClass('selectededitor');
		if ( window['OuterLayout'] ) window['OuterLayout'].destroy();
		if ( window['InnerLayout'] ) window['InnerLayout'].destroy();
	
		// Display selected builder
		jQuery('.'+LayoutMode).addClass('selectededitor');

		<?php foreach(get_the_layouts() as $layout):
			$mylayout = new PageLinesLayout($layout);
			$default_margin = $mylayout->margin->bwidth;
			?>
			if (LayoutMode == '<?php echo $layout;?>') { 
				marginwidth = mwidth;
				innereastwidth = <?php echo $mylayout->east->bwidth;?>;
				innerwestwidth = <?php echo $mylayout->west->bwidth;?>; 
				gtrwidth = 10
			}
		<?php endforeach;?>
	
		setLayoutBuilder(LayoutMode, marginwidth, innereastwidth, innerwestwidth, gtrwidth);
	
	});	
	

});



/*]]>*/</script>