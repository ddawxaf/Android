
var ReloadTimeID='';

function onLoad(){
	parent.loading = 'N';
	parent.ShowType = 'OU';
	if (parent.parent.leg_flag=="Y"){
		parent.parent.leg_flag="N";
		parent.pg=0;
		reload_var();
	}		
	if(parent.loading_var == 'N'){
		parent.ShowGameList();
		obj_layer = document.getElementById('LoadLayer');
		obj_layer.style.display = 'none';
	}
	if (parent.retime_flag == 'Y'){
		ReloadTimeID = setInterval("reload_var()",parent.retime*1000);
	}		

	futureShowGtypeTable();
}

//倒數自動更新時間
function count_down(){
	setTimeout('count_down()',1000);
	if (parent.retime_flag == 'Y'){
		if(parent.retime <= 0){
			if(parent.loading_var == 'N')
			reload_var();
			return;
		}
		parent.retime--;
		obj_cd = document.getElementById('cd');
		obj_cd.innerHTML = parent.retime;
	}
}

function reload_var(Level){
	parent.loading_var = 'Y';
	if(Level=="up"){
		var tmp = "./OP_future/body_var.php";
	}else{
		var tmp = "./body_var.php";
	}
	//parent.body_var.location.reload();
	parent.body_var.location = tmp+"?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&league_id="+parent.parent.OM_lid_type+"&g_date="+parent.g_date;	
	//onload();

}

function chg_gdate(){
	var obj_gdate = document.getElementById("g_date");
	var homepage = "./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&g_date="+obj_gdate.value+"&mtype="+parent.ltype+"&league_id="+parent.parent.OM_lid_type;
	//alert(homepage);
	parent.pg=0;
	parent.body_var.location = homepage;
}

function chg_pg(pg){
	if (pg==parent.pg) {return;}
	parent.pg=pg;
	parent.loading_var = 'Y';
	parent.body_var.location = "./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&page="+parent.g_date;
	//onload();
}
function unload(){
	clearInterval(ReloadTimeID);
}
window.onunload=unload;
