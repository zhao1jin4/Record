
==============windows 打印功能 

会提示下载 CLodopPrint_Setup_for_Win32NT


<object  id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
</object>

<div id="mycode1">
	这是打印内容 
</div>
<div id='myvehicleno'>车牌号</div>

 <button onclick="prn_Preview()">打印 </button>
				
function prn_Preview(){
	LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	if(LODOP.webskt && LODOP.webskt.readyState==1 && LODOP.VERSION){
		var top = 50;
		$($("div[id*='mycode']")).each(function(i){
			i = i + 1;
			LODOP.ADD_PRINT_TEXT(top-30,320,520,160,"远成集团车标码");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
			LODOP.ADD_PRINT_BARCODE(top,110,520,160,"128A",$.trim($(this).text()));
			LODOP.ADD_PRINT_TEXT(top+170,310,520,160,$.trim($("#myvehicleno").text()));
			LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
			top = top + 110 + 150;
			if(i % 4 == 0){
				LODOP.NewPageA();
				top = 50;
			}
		});
		LODOP.PREVIEW();
	} else {
		alert("打印插件没准备好，请刷新页面后重试！");
	}
}




//====判断是否需要安装CLodop云打印服务器:====
function needCLodop() {
    try {
        var ua = navigator.userAgent;
        if (ua.match(/Windows\sPhone/i) != null) return true;
        if (ua.match(/iPhone|iPod/i) != null) return true;
        if (ua.match(/Android/i) != null) return true;
        if (ua.match(/Edge\D?\d+/i) != null) return true;

        var verTrident = ua.match(/Trident\D?\d+/i);
        var verIE = ua.match(/MSIE\D?\d+/i);
        var verOPR = ua.match(/OPR\D?\d+/i);
        var verFF = ua.match(/Firefox\D?\d+/i);
        var x64 = ua.match(/x64/i);
        if ((verTrident == null) && (verIE == null) && (x64 !== null)) return true;
        else if (verFF !== null) {
            verFF = verFF[0].match(/\d+/);
            if ((verFF[0] >= 42) || (x64 !== null)) return true;
        } else if (verOPR !== null) {
            verOPR = verOPR[0].match(/\d+/);
            if (verOPR[0] >= 32) return true;
        } else if ((verTrident == null) && (verIE == null)) {
            var verChrome = ua.match(/Chrome\D?\d+/i);
            if (verChrome !== null) {
                verChrome = verChrome[0].match(/\d+/);
                if (verChrome[0] >= 42) return true;
            };
        };
        return false;
    } catch(err) {
        return true;
    };
};

//====获取LODOP对象的主过程：====
function getLodop(oOBJECT, oEMBED) {
    var strHtmInstall = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='install_lodop32.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
    var strHtmUpdate = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='install_lodop32.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
    var strHtm64_Install = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='install_lodop64.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
    var strHtm64_Update = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='install_lodop64.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
    var strHtmFireFox = "<br><br><font color='#FF00FF'>（注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它）</font>";
    var strHtmChrome = "<br><br><font color='#FF00FF'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</font>";
    var strCLodopInstall = "<br><font color='#FF00FF'>CLodop云打印服务(localhost本地)未安装启动!点击这里<a href='CLodop_Setup_for_Win32NT.exe' target='_self'>执行安装</a>,安装后请刷新页面。</font>";
    var strCLodopUpdate = "<br><font color='#FF00FF'>CLodop云打印服务需升级!点击这里<a href='CLodop_Setup_for_Win32NT.exe' target='_self'>执行升级</a>,升级后请刷新页面。</font>";
    var LODOP;
    try {
        var isIE = (navigator.userAgent.indexOf('MSIE') >= 0) || (navigator.userAgent.indexOf('Trident') >= 0);
        if (needCLodop()) {
            try {
                LODOP = getCLodop();
            } catch(err) {};
            if (!LODOP && document.readyState !== "complete") {
                alert("C-Lodop没准备好，请稍后再试！");
                return;
            };
            if (!LODOP) {
                if (isIE) {
                	if(confirm("CLodop云打印服务(localhost本地)未安装启动!点击确定进行安装,安装后请刷新页面。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                else {
                	if(confirm("CLodop云打印服务(localhost本地)未安装启动!点击确定进行安装,安装后请刷新页面。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                return;
            } else {

                if (CLODOP.CVERSION < "2.1.0.2") {
                    if (isIE) {
                    	if(confirm("CLodop云打印服务需升级!点击确定下载控件进行升级,升级后请刷新页面。")){
                    		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                    	} else {
                    		alert("下载失败!");
                    	}
                    }
                    else { 
	                	if(confirm("CLodop云打印服务需升级!点击确定下载控件进行升级,升级后请刷新页面。")){
	                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
	                	} else {
	                		alert("下载失败!");
	                	}
                    }
                };
                if (oEMBED && oEMBED.parentNode) oEMBED.parentNode.removeChild(oEMBED);
                if (oOBJECT && oOBJECT.parentNode) oOBJECT.parentNode.removeChild(oOBJECT);
            };
        } else {
            var is64IE = isIE && (navigator.userAgent.indexOf('x64') >= 0);
            //=====如果页面有Lodop就直接使用，没有则新建:==========
            if (oOBJECT != undefined || oEMBED != undefined) {
                if (isIE) LODOP = oOBJECT;
                else LODOP = oEMBED;
            } else if (CreatedOKLodop7766 == null) {
                LODOP = document.createElement("object");
                LODOP.setAttribute("width", 0);
                LODOP.setAttribute("height", 0);
                LODOP.setAttribute("style", "position:absolute;left:0px;top:-100px;width:0px;height:0px;");
                if (isIE) LODOP.setAttribute("classid", "clsid:2105C259-1E0C-4534-8141-A753534CB4CA");
                else LODOP.setAttribute("type", "application/x-print-lodop");
                document.documentElement.appendChild(LODOP);
                CreatedOKLodop7766 = LODOP;
            } else LODOP = CreatedOKLodop7766;
            //=====Lodop插件未安装时提示下载地址:==========
            if ((LODOP == null) || (typeof(LODOP.VERSION) == "undefined")) {
                if (navigator.userAgent.indexOf('Chrome') >= 0) {
                	if(confirm("如果此前正常，仅因浏览器升级或重安装而出问题，需卸载后重新进行安装。注卸载需重新启动")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                if (navigator.userAgent.indexOf('Firefox') >= 0) {
                	if(confirm("注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸载。在进行重新安装")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                if (is64IE) {
                	if(confirm("打印控件未安装!点击确定进行下载,安装后请刷新页面或重新进入。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win64NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                else if (isIE) {
                	if(confirm("打印控件未安装!点击确定下载控件,安装后请刷新页面或重新进入。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                else {
                	if(confirm("打印控件未安装!点击确定下载控件,安装后请刷新页面或重新进入。")){
                		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
                	} else {
                		alert("下载失败!");
                	}
                }
                return LODOP;
            };
        };
        if (LODOP.VERSION < "6.2.1.7") {
            if (needCLodop()) document.documentElement.innerHTML = strCLodopUpdate + document.documentElement.innerHTML;
            else if (is64IE) {
            	if(confirm("打印控件需要升级!点击确定下载控件进行升级,升级后请重新进入。")){
            		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win64NT.zip";
            	} else {
            		alert("下载失败!");
            	}
            }
            else if (isIE) {
            	if(confirm("打印控件需要升级!点击确定下载控件进行升级,升级后请重新进入。。")){
            		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
            	} else {
            		alert("下载失败!");
            	}
            }
            else {
            	if(confirm("打印控件需要升级!点击确定下载控件进行升级,升级后请重新进入。。")){
            		window.location.href = "http://www.lodop.net/demolist/CLodopPrint_Setup_for_Win32NT.zip";
            	} else {
            		alert("下载失败!");
            	}
            }
            return LODOP;
        };
        //===如下空白位置适合调用统一功能(如注册语句、语言选择等):===
        //===========================================================
        return LODOP;
    } catch(err) {
        alert("getLodop出错:" + err);
    };
};

