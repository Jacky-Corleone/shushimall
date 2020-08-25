jQuery.fn.extend({
    setValue: function (value) {
       jQuery(this).find("option[value='"+value+"']").attr('selected','selected');
    }
});
jQuery.validator.setDefaults({ ignore: '' });