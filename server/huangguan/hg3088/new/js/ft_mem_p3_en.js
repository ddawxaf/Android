function ChkSelect(sdate){parent.parent.paramData=new Array;var count=0,teamcount=eval("document.form"+sdate+".teamcount.value");for(i=1;i<=teamcount;i++)for(var j=0;j<eval("document.form"+sdate+".game"+i).length;j++)1==eval("document.form"+sdate+".game"+i+"["+j+"].checked")&&count++;return count<1?(alert("please select teams!!"),!1):1==count?(alert("Single wager please goto Single Wage Page for betting!!"),!1):count<minlimit||count>maxlimit?(alert("please make sure you select at "+minlimit+"~"+maxlimit+" different matches!!"),!1):void reload_var()}var ReloadTimeID;function onLoad(){parent.loading="N",parent.ShowType="PR","N"==parent.loading_var&&(parent.ShowGameList(),obj_layer=document.getElementById("LoadLayer"),obj_layer.style.display="none"),count_down(),futureShowGtypeTable()}function count_down(){if(setTimeout("count_down()",1e3),"Y"==parent.retime_flag){if(parent.retime<=0)return void("N"==parent.loading_var&&reload_var());parent.retime--,obj_cd=document.getElementById("cd"),obj_cd.innerHTML=parent.retime}}function reload_var(){minlimit=parent.minlimit_VAR,maxlimit=parent.maxlimit_VAR,parent.loading_var,parent.body_var.location.reload()}