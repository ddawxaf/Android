

var count_win=false;
//if (self==top) 	self.location.href="http://"+document.domain;
var winRedirectTimer=45000;
var winRedirect=0;
window.onload = function (){
	//window.setTimeout("Win_Redirect()", 45000);
	document.getElementById("gold").blur();
	document.getElementById("gold").focus();
	if(resetCheck){
		var reloadTime=document.getElementById("checkOrder");
		reloadTime.checked=resetCheck;
	}
	var reloadautoOdd=document.getElementById("autoOdd");
	
	if(autoOddCheck){
			reloadautoOdd.checked=autoOddCheck;
	}else{
		autoOddCheck=false;
		reloadautoOdd.checked=autoOddCheck;
	}	
	onclickReloadTime();
	resetGold();
	parent.onloadSet(document.body.scrollWidth,document.body.scrollHeight,"bet_order_frame");
	check_ioradio();

}


//檢查賠率變色
function check_ioradio(){
	var tmp_ior=document.getElementById("ioradio_r_h").value;
	//alert("tmp_ior="+tmp_ior+",ioradio="+ioradio);
	if (ioradio==""){
		ioradio=tmp_ior;
	}
	if (ioradio!=tmp_ior){
		ioradio=tmp_ior;
		document.getElementById("ioradio_id").className="lightOn";
	}else{
		document.getElementById("ioradio_id").className="light";
	}
}
function onclickReloadTime(){
	var reloadTime=document.getElementById("checkOrder");
	resetCheck=reloadTime.checked;
	window.clearTimeout(winRedirect);
	if (!reloadTime.checked){
		//winRedirect=window.setTimeout("Win_Redirect()", winRedirectTimer);
	}else{
		winRedirect=window.setTimeout("winReload()", 1000);
	}
}
function onclickReloadAutoOdd(){
	
	var reloadautoOdd=document.getElementById("autoOdd");
	//alert(reloadautoOdd.checked);
	autoOddCheck=reloadautoOdd.checked;
}
function resetTimer(){
	//回復reload時間
//document.getElementById("checkOrder").checked	=resetCheck;


	onclickReloadTime();
	}
function clearAllTimer(){
	//keep住 reload
//	resetCheck=document.getElementById("checkOrder").checked;
//		reloadTime.checked=false;
		window.clearTimeout(winRedirect);
		winRedirect=window.setTimeout("Win_Redirect()", winRedirectTimer);
		//onclickReloadTime();
	}
function winReload(){
	var showTimer=document.getElementById("ODtimer");
	if(showTimer){
        showTimer = showTimer.innerHTML ;
        showTimer=showTimer*1-1;
        document.getElementById("ODtimer").innerHTML=showTimer;
        if (showTimer<=0){
            orderReload();
        }else{
            winRedirect=window.setTimeout("winReload()", 1000);
        }
	}

}
function orderReload(){
	window.location.href=window.location;
}
function loadedorderLive(){
	document.all.gold.focus();
	try{
		parent.live_order_height(document.body.scrollHeight);
	} catch (E) {}	
}
function Win_Redirect(){
	/*
	var i=document.all.uid.value;
	var live="";
	try{
		live= document.getElementById("live").value;
	} catch (E) {}
	self.location='../select.php?uid='+i+'&live='+live;
	*/
	parent.close_bet();
}
function resetGold(){
		if (""+keepGold!="undefined" && keepGold!="" ){
	
			document.getElementById("gold").value=keepGold;
			CountWinGold();
		}
	}

function CheckKey(evt){
    var key = window.event ? evt.keyCode : evt.which;
    //alert(key)
    //var keychar = String.fromCharCode(key);
    //alert(keychar)
    if(key == 32){
        return false;
    }
    if(key == 13) {
        CountWinGold();
        SubChk();

    }
    else if((key < 48 || key > 57) && (key > 95 || key < 106)){alert(message015); return false;}

}

//小數點位數
/*function printf(vals, points) {
 vals = "" + vals;
 var cmd = new Array();
 cmd = vals.split(".");
 if (cmd.length > 1){
  for (ii=0; ii<(points-cmd[1].length); ii++) vals = vals + "0";
 }else{
  vals = vals + ".";
  for (ii=0; ii<points; ii++) vals = vals + "0";
 }
 return vals;
}*/


$(function () {
    // 2018 新增
    fastBetAction() ;
    setBetFastAction() ;
}) ;