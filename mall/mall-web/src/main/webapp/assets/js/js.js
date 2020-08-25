$(function() {
	function dropMenu(obj) {
		$(obj).each(function() {
			var theSpan = $(this);
			var theMenu = theSpan.find(".submenu");
			var panelId = theMenu.prop('id');
			if (panelId == 'regionPanel') {
				return;
			}
			var tarHeight = theMenu.height();
			theMenu.css({
				height : 0,
				opacity : 0
			});
			var t1;
			function expand() {
				clearTimeout(t1);
				theSpan.find('a').addClass("selected");
				theMenu.stop().show().animate({
					height : tarHeight,
					opacity : 1
				}, 200);
			}

			function collapse() {
				clearTimeout(t1);
				t1 = setTimeout(function() {
					theSpan.find('a').removeClass("selected");
					theMenu.stop().animate({
						height : 0,
						opacity : 0
					}, 200, function() {
						$(this).css({
							display : "none"
						});
					});
				}, 250);
			}
			theSpan.hover(expand, collapse);
			theMenu.hover(expand, collapse);
		});
	}
	dropMenu(".drop-menu-effect");

	var wid = $('#myTab0 li:first').outerWidth(true), step = 1, timer;
	$('.fa-caret-right').bind('click', function() {
		play(true)
	});
	$('.fa-caret-left').bind('click', function() {
		play(false)
	});
	$('.TabTitle').hover(function() {
		clearInterval(timer);
	})
	function play(flag) {
		if (!$('#myTab0').is(':animated')) {
			var dir = flag ? -1 : 1, objli;
			if (flag) {
				objli = $('#myTab0 li').slice(0, step);
				objli.clone().appendTo($('#myTab0'));
			} else {
				objli = $('#myTab0 li').slice(-step);
				objli.clone().prependTo($('#myTab0'));
				$('#myTab0').css({
					marginLeft : -wid
				});
			}
			$('#myTab0').animate({
				marginLeft : '+=' + wid * dir
			}, 500, function() {
				if (flag) {
					$('#myTab0').css({
						marginLeft : 0
					});
				}
				objli.remove();
			})
		}
	}
});

/* 店铺 */
$('#store').on("mouseover", function() {
	$(".shop_right_box").toggle();

	$(".shop_right_box").on("mouseleave", function() {
		$(".shop_right_box").hide();
	})
})

/* index头部切换 */
$(".big_class span").click(
		function() {
			$(this).addClass("focus_class font_ff").siblings().removeClass(
					"focus_class font_ff");
		})

/* 信息全选 */
$("#all_select").click(function() {
	var inputs = $(".check");
	for (var i = 0; i < inputs.length; i++) {
		inputs[i].checked = this.checked;
	}
})

/* 左边list切换 */
$('.left-list').click(function() {
	$(".left-list i").addClass('fa-sort-up');
	$(this).next('ul').toggle().parent().siblings().find('ul').hide();
})

$(".but1").click(function() {
	$(".password").show();
})

$(".but2").click(function() {
	$(".photo").show();
})

// 添加运费
$(".add_sell").click(function() {
	$(".select-city").show();
})
$(".city_ok").click(function() {
	$(".select-city").hide();
})

$(".moban_no").click(function() {
	$(".moban").hide();
})

$(".add_moban").click(function() {
	$(".moban").show();
})

/* 类目 */
$(".add_leimu")
		.on(
				"click",
				function() {
					var but_leimu = $(".input_con").val(), but_leimu2 = $(
							".input_con2").val(), but_leimu3 = $(".input_con3")
							.val();
					var str = "<p>" + but_leimu + but_leimu2 + but_leimu3
							+ "</p>";
					var add_leimu_xinxi = $(".add_leimu_xinxi");
					add_leimu_xinxi.after(str)
				})

/* 申请新经营品牌 */
$(".one_leimu").on("click", function() {
	$(".two_leimu").show();
})
$(".two_leimu").on("click", function() {
	$(".the_leimu").show();
})

$(".the_leimu").on("click", function() {
	$('.hide_img').show();
})

$(".show_san_img").on("click", function() {

	$(".show_one_img").toggle();
})

$(".show_si_img").on("click", function() {
	$(".show_two_img").toggle();
})

var from_num = 1;

function add_sb() {
	from_num++;
	var but = "<button  onclick='show_nr(this)' class='button_1 ok hei_24 mar_20 mar sb wid_80 shebei' index='"
			+ from_num
			+ "' id='show_but_"
			+ from_num
			+ "' style='width:80px; margin:0px 10px 0px 10px;'>"
			+ "设备"
			+ "</button>"
			+ '<i onclick="delete_nr(this)" id="hide_but_'
			+ from_num
			+ '" index="'
			+ from_num
			+ '" class="demo-icons fa-times-circle font_16"></i>';
	$(".add_sb").append(but);
	add_from_by_num();
}

function hide3() {
	$(".sb3").hide();
	$(".xx").hide();
	$(".neirong3").hide();
}
function hide2() {
	$(".sb2").hide();
	$(".xx").hide();
	$(".neirong2").hide();
}
function hide() {
	$(".sb").hide();
	$(".xx").hide();
	$(".neirong").hide();
}

function add_from_by_num() {
	var add_form = '<ul class="add_clone_box hide " id = "but_from_'
			+ from_num
			+ '"><li><span class="font_span_min">设备编号：</span><input name="" type="text" class="input_Style2 wid_180 hei_32" placeholder="输入法定代表人姓名..."/>                </li>                <li>                    <span class="font_span_min">品牌：</span>                    <!--品牌下拉选 -->                    <select name="" id="" class="input_Style2 wid_180 hei_35">                    <option value="请选择" selected="selected"> 请选择 </option>                    <option value="北京"> 北京 </option>                    <option value="上海"> 上海 </option>                    <option value="广州"> 广州 </option>                    <option value="深圳"> 深圳 </option>                   </select>                </li>                <li>                    <span class="font_span_min">分类选择：</span>                    <select name="" id="" class="input_Style2 wid_80 hei_35">                    <option value="请选择" selected="selected"> 请选择 </option>                    <option value="北京"> 北京 </option>                    <option value="上海"> 上海 </option>                    <option value="广州"> 广州 </option>                    <option value="深圳"> 深圳 </option>                   </select>                   <i class="demo-icons">-</i>                    <select name="" id="" class="input_Style2 wid_80 hei_35">                    <option value="请选择" selected="selected"> 请选择 </option>                    <option value="北京"> 北京 </option>                    <option value="上海"> 上海 </option>                    <option value="广州"> 广州 </option>                    <option value="深圳"> 深圳 </option>                   </select>                   <i class="demo-icons">-</i>                    <select name="" id="" class="input_Style2 wid_80 hei_35">                    <option value="请选择" selected="selected"> 请选择 </option>                    <option value="北京"> 北京 </option>                    <option value="上海"> 上海 </option>                    <option value="广州"> 广州 </option>                    <option value="深圳"> 深圳 </option></select></li><li><span class="font_span_min">规格：</span><select name="" id="" class="input_Style2 wid_180 hei_35">                    <option value="请选择" selected="selected"> 请选择 </option><option value="北京"> 北京 </option><option value="上海"> 上海 </option>                    <option value="广州"> 广州 </option>                    <option value="深圳"> 深圳 </option>                   </select>                </li>                <li style="height:200px;"><span class="font_span_min">描述：</span><textarea name="" cols="" rows="" class="input_Style2 wid_300 hei_150"></textarea></li></ul>';
	var index = from_num - 1;
	$("#but_from_" + index).after(add_form);

};

function hide_from_by_num(num) {
	for (var i = 1; i <= from_num; i++) {
		if (i != num) {
			var ids = "but_from_" + i;
			$("#" + ids).hide();
		}

	}
};

function show_nr(obj) {
	var index = $(obj).attr("index");
	$(".yingshuai_box").find('ul').hide();
	$("#but_from_" + index).show();
};

function delete_nr(obj) {
	var index = $(obj).attr("index");
	$("#but_from_" + index).hide();
	$("#show_but_" + index).hide();
	$("#hide_but_" + index).hide();

};

function show_man() {
	var shop_man = document.getElementById("shop_man"), down = document
			.getElementById("down");
	shop_man.style.display = "block";
	down.setAttribute("class", " fa-angle-up");
}
function show_man2() {
	var shop_man = document.getElementById("shop_man"), down = document
			.getElementById("down");
	shop_man.style.display = "none";
	down.setAttribute("class", "fa-angle-down");
}
