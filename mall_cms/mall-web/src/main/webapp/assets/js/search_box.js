/*---------------------------------------------------------------------Trade平台常用基本脚本函数（可扩展）-------------------------------------------------------------------------*/


//搜索条件
function search_More() {
    var types = $(".search_condition_box");
    types.each(function () {
        var s_ul = $(this).find("ul");
        var s_li = $(this).find("li");
        var width = s_ul.width();
        var e = 0;
        s_li.each(function () {
            e += $(this).outerWidth(!0);
        });
        s_ul.hasClass("brand")
        {
            if (width >= e)
            {
                $(this).removeClass("TabContent_first");
                $(this).addClass("TabContent_second");
            }
            width = width *2;
        }
        var s_more = $(this).find(".s_more");
        if (width < e)
            s_more.css("display", "");
        else s_more.css("display", "none");
    });
}

$(".s_more").click(function () {
    var pul = $(this).parent().find("ul");
    var clazz = "TabContent_first";
    if (pul.hasClass("attr")) {
        clazz = "TabContent_second";
    }
    if (pul.hasClass(clazz)) {
        pul.removeClass(clazz);
        $(this).html("收起<i></i>");
    }
    else {
        pul.addClass(clazz);
        $(this).html("更多<i></i>");
    }
});

$("#J_selectorMore").click(function () {
    var more_text = $(this);
    if (more_text.html() == "更多选项<i></i>")
        more_text.html("收起<i></i>");
    else  more_text.html("更多选项<i></i>");
    selectorMore();
});

function selectorMore() {
    var search = $(".search_condition_box");
    if(search.length <=4)
    {
        $("#J_selectorMore").css('display', 'none');
    }
　$.each(search, function (n, value) {
        if (n > 4) {
            if ($(this).css('display') == 'none')
                $(this).css('display', '');
            else $(this).css('display', 'none');
        }
    });
}


