$(function(){
	//�������˵�
	$("#leftBuyer").load($("#contextPath").val()+"/leftBuyer");
	//�����˿��
	/*
	$(".js_shenqing_refund").click(function(){
		var orderId = $(this).attr("name");
		console.log("orderId==="+orderId);
		$("#boxContent").load($("#contextPath").val()+"/order/refundBox", {orderId: orderId}, function(){
			$(".js_choice").show();
		});
	});
	*/
});
//������ѯ
function submitGoodsForm(){
	//条件 搜索时 初始化 分页page   start 李伟龙	2015年9月6日 13:52:57
	$("input[name='page']").val("1");
	//条件 搜索时 初始化 分页page   end 李伟龙
	$("#queryForm").submit();
};
function changeState(obj, state){
	$(".dingdan a").removeClass();
	$(obj).addClass("border-3");
	if(state==0){
		$("#state").val("");
	}else{
		$("#state").val(state);
	}
	$("#queryForm").submit();
}
//��ָ���ķ�ҳҳ��
function topage(page){
	$("input[name='page']").val(page);
	$("#queryForm").submit();
}