var ReloadTimeID;function onLoad(){parent.loading="N",parent.ShowType="EO","Y"==parent.parent.leg_flag&&(parent.parent.leg_flag="N",parent.pg=0,reload_var()),"N"==parent.loading_var&&(parent.ShowGameList(),obj_layer=document.getElementById("LoadLayer"),obj_layer.style.display="none");var e="";for(e='<select id="g_date" name="g_date" onChange="chg_gdate()">',e+='<option value="ALL" selected>'+top.alldata+"</option>",i=0;i<DateAry.length;i++)e+='<option value="'+DateAry[i]+'" >'+DateAry[i]+"</option>";e+="</select>",document.getElementById("show_date_opt").innerHTML=e,count_down(),futureShowGtypeTable()}function count_down(){if(setTimeout("count_down()",1e3),"Y"==parent.retime_flag){if(parent.retime<=0)return void("N"==parent.loading_var&&reload_var());parent.retime--,obj_cd=document.getElementById("cd"),obj_cd.innerHTML=parent.retime}}function chg_pg(e){e!=parent.pg&&(parent.pg=e,parent.loading_var="Y",parent.body_var.location="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&g_date="+parent.g_date)}function reload_var(e){if(parent.loading_var,"up"==e)var t="./OP_future/body_var.php";else t="./body_var.php";parent.body_var.location=t+"?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&g_date="+parent.g_date+"&league_id="+parent.parent.OM_lid_type}function chg_gdate(){var e=document.getElementById("g_date"),t="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&g_date="+e.value+"&mtype="+parent.ltype+"&league_id="+parent.parent.OM_lid_type;parent.pg=0,parent.body_var.location=t}