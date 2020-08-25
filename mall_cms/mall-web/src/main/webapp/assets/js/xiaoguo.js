$(document).ready(function(){


  var checks=$('.shop_show').find('input[type="checkbox"]');
  checks.click(function(){
    if(checks.attr("checked")){
    var ps=$(".shop_xq").find("p").eq(0).clone();
    $(".shop_xq").append(ps);
   }
  })

  var checks2=$('.shop_show_2').find('input[type="checkbox"]');
  checks2.click(function(){
    if(checks2.attr("checked")){
    var pp=$(".shop_jj").find("div").eq(0).clone();
    $(".shop_jj").append(pp);
   }
  })
  
$("#quanguo").click(function(){
  $(".all_chain").show();
})
 
$(".xx").click(function(){
  $(".all_chain").hide();
})


$(".guanqu").click(function(){
  $(".nTab_address").toggle();
  $("#Tabaddress_Content0 ul li").click(function(){
    $(".two").show();
  })

  $('.nTab_address ').mouseleave(function(){
    $(".nTab_address").hide();
  })

})

  $(".bianji").click(function(){
    $(".diloge_dijia").show();
  })

$(".but_qx").click(function(){
    $(".diloge_dijia").hide();
  })


  $("#city_toggle").mouseover(function(){
    $(".city_box_sh").show();
  })
  $(".city_box_sh").mouseleave(function(){
    $(".city_box_sh").hide();
  })
  $('#list li a').click( function(){
    $(this).next('ul').toggle().parent().siblings().find('ul').hide();;
        
  });

  $("#digloe_show").click(function(){
    $("#city_shiqu_all").show();
  })
  $('#city_all li').click(function(){
        //先把ｌｉ变量取出来
        var index=$(this).index(); 
        $('#city_shiqu_all').children('p').eq(index).show().siblings().hide();
  });

 $("#digloe_show2").click(function(){
    $("#city_shiqu_all2").show();
  })
  $('#city_all2 li').click(function(){
        //先把ｌｉ变量取出来
        var index=$(this).index(); 
        $('#city_shiqu_all2').children('p').eq(index).show().siblings().hide();
  });

  $("#digloe_show3").click(function(){
    $("#city_shiqu_all3").show();
  })
  $('#city_all3 li').click(function(){
        //先把ｌｉ变量取出来
        var index=$(this).index(); 
        $('#city_shiqu_all3').children('p').eq(index).show().siblings().hide();
  });

 $("#digloe_show4").click(function(){
    $("#city_shiqu_all4").show();
  })
  $('#city_all4 li').click(function(){
        //先把ｌｉ变量取出来
        var index=$(this).index(); 
        $('#city_shiqu_all4').children('p').eq(index).show().siblings().hide();
  });

	/*城市显示切换*/
  	$(".city_select").click(function(){
        $(".city").toggle();
        $(function(){setInterval(function(){
	        $(".city").mouseleave(function(){
	            $(".city").hide();
	        })
        },2000);}) 
    })

  	/*店铺收藏*/
//  	$("#top_collect").click(function(){
//  		$(".collect").toggle();
//        $(function(){setInterval(function(){
//	        $(".collect").mouseleave(function(){
//	            $(".collect").hide();
//	        })
//        },2000);}) 
//  	})
	$('#top_collect').on("mouseenter",function(){
		$(".collect").show();
	});
	$('#top_collect').on("mouseleave",function(){
		$(".collect").hide();
	});

//  	$("#shop_car").click(function(){
//  		$(".shopping").toggle();
//        $(function(){setInterval(function(){
//	        $(".shopping").mouseleave(function(){
//	            $(".shopping").hide();
//	        })
//        },2000);}) 
//  	})
	$('#shop_car').on("mouseenter",function(){
		$(".shopping").show();
    })
	$('#shop_car').on("mouseleave",function(){
    	$(".shopping").hide();
    })

  	/*弹出框*/

  	$(".no").click(function(){
  		$(".price").hide();
  	})

  	$(".cancel").click(function(){
  		$(".price").hide();
  	})

  	/*全部选项*/
  	$(".Nav_left").bind("mouseenter",function(){
  		$(".Nav_classify").show();
  	}) 
  	$(".Nav_left").bind("mouseleave",function(){
  		$(".Nav_classify").hide();
 	})
  	

  	// 发表咨询
 
  	$(".mid_f_right2").click(function(){
  		$('.price').show();
  	})

  	/*购物车加减*/
  	var qtyadd=$(".qtyadd");
	var qtydel=$(".qtydel");
	var qtyval=$(".qty");
	qtyadd.click(function(){
	var oddqty=Math.round($(".qty").val());
		oddqty=oddqty+1;
		qtyval.val(oddqty);
		});

	qtydel.click(function(){
		var oddqty=Math.round($(".qty").val());
		if(oddqty==1){
			return false;
		}else{
			oddqty=oddqty-1;
			qtyval.val(oddqty);
			}
		});
	

	/*选择品牌*/
	/*$(".mid_f_right span").click(function(){
		$(this).addClass("border-8").siblings().removeClass("border-8");
    $(this).find("b").addClass("po_ab pa_04").parent().siblings().find("b").removeClass("po_ab pa_04");


	})*/

/*全选*/
 $("#all_select2").click(function(){
  var all_sel2=$(".bg_05 td input");
  for(var i=0;i<all_sel2.length;i++){
      all_sel2[i].checked=this.checked;
  }
})

 /*微信显示*/
$(".wx").click(function(){
  $(".weixin").show();
  $(".logo").hide();
})


 /*微博显示*/
$(".wb").click(function(){
  $(".weibo").show();
  $(".logo").hide();

})

/*结算管理切换*/
$(".tit").click(function(){
  $(this).addClass("add_h_b");
  $(this).next(".zd_xinxi").toggle();
})




});
