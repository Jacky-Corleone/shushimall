// JavaScript Document
function SetActiveIndex(jElms, jElmNums, i) {
			if (jElms.length == 0)
				return;
			jElms.hide();
			$(jElms[i]).show();
			jElmNums.removeClass("fir_1");
			$(jElmNums[i]).addClass("fir_1");
			jElmNums.removeClass("fir_2");
			$(jElmNums[i]).addClass("fir_2");
		}


		function GetActiveIndex(jElms) {
			return jElms.index(jElms.filter(":visible"));
		}

		function SetNextActive(jElms, jElmNums) {
			if (jElms.length == 0)
				return;
			var i = GetActiveIndex(jElms) + 1;
			if (i >= jElms.length)
				i = 0;
			SetActiveIndex(jElms, jElmNums, i);
		}

		$(function() {
			window.setInterval(function() { SetNextActive($('.main_flash>.focusPic'), $(".main_flash>.num_bg1>ul>li")); }, 3000);
			window.setInterval(function() { SetNextActive($('.centerList>.focusPic1'), $(".centerList>.num_bg>ul>li")); }, 3000);
		});