<?php
if(!defined('PHPYOU_VER')) {
	exit('�Ƿ�����');
}

//�޸���Ϣ
if ($_GET['act']=="���") {
if (empty($_POST['kauser'])) {
       
  echo "<script>alert('�û�������Ϊ��!');window.history.go(-1);</script>"; 
  exit;
    }
if (empty($_POST['kapassword'])) {
       
  echo "<script>alert('���벻��Ϊ��!');window.history.go(-1);</script>"; 
  exit;
    }
if ($_POST['cs']>$_POST['kyx']) {
       
  echo "<script>alert('���ö�ȳ����������ö�!');window.history.go(-1);</script>"; 
  exit;
    }
if (($_POST['sj']+$_POST['sf'])>$_POST['sff']) {
       
  echo "<script>alert('�Բ���,����ȷ����ռ��!');window.history.go(-1);</script>"; 
  exit;
    }

if ($_POST['tv5']=="��") {$pz=0; }else{$pz=1;  }
if ($_POST['tv6']=="��") {$stat=0;}else{$stat=1;  }

$result = mysqli_query($dbLink,"select count(*) from ka_guan  where kauser='".$_POST['kauser']."'  order by id desc");   
$num = mysql_result($result,"0");

if($num!=0){
   echo "<script>alert('��һ�û������ѱ�ռ�ã���������룡!');window.history.go(-1);</script>"; 
  exit;
}
$result = mysqli_query($dbLink,"select count(*) from ka_mem  where kauser='".$_POST['kauser']."'  order by id desc");   
$num = mysql_result($result,"0");

if($num!=0){
   echo "<script>alert('��һ�û������ѱ�ռ�ã���������룡!');window.history.go(-1);</script>"; 
  exit;
}

$result = mysqli_query($dbLink,"select count(*) from ka_zi  where kauser='".$_POST['kauser']."'  order by id desc");   
$num = mysql_result($result,"0");

if($num!=0){
   echo "<script>alert('��һ�û������ѱ�ռ�ã���������룡!');window.history.go(-1);</script>"; 
  exit;
}

  
	
		
			 



 $pass = md5($_POST['kapassword']);
 $text=date("Y-m-d H:i:s");
 $ip=$_SERVER["REMOTE_ADDR"];


$result=mysqli_query($dbLink,"select * from ka_guan where id=".$_POST['guanid']."  order by id"); 
$row=mysqli_fetch_assoc($result);
$guan=$row['kauser'];

 $sj=$_POST['sj'];
	$sql="INSERT INTO  ka_guan set kapassword='".$pass."',kauser='".$_POST['kauser']."',xm='".$_POST['xm']."',rs='".$_POST['rs']."',tmb='".$_POST['tmb']."',cs='".$_POST['cs']."',ts='".$_POST['cs']."',sj='".$sj."',sf='".$_POST['sf']."',guan='".$guan."',zong='".$_POST['kauser']."',tm=500000,zm=50000,zt=50000,zm6=50000,lm=50000,gg=50000,xx=50000,sx=50000,bb=50000,ws=50000,guanid='".$_POST['guanid']."',zongid=0,lx=2,look=0,ztws=0,pz='".$pz."',stat='".$stat."',adddate='".$text."',slogin='".$text."',zlogin='".$text."',sip='".$ip."',zip='".$ip."'   ";
	
$exe=mysqli_query($dbLink,$sql) or  die("���ݿ��޸ĳ���");

$result=mysqli_query($dbLink,"select * from ka_guan where  kauser='".$_POST['kauser']."'  order by id desc"); 
$row=mysqli_fetch_assoc($result);
$SoftID=$row['id'];



//�ܴ�����






//$ygid=$_POST['ygid'];
$yg=$_POST['m'];
$ygb=$_POST['ygb'];
$ygc=$_POST['ygc'];
$ygd=$_POST['ygd'];
$xx=$_POST['mm'];
$xxx=$_POST['mmm'];
$ds=$_POST['ds'];
$style=$_POST['style'];

for ($I=0; $I<count($yg); $I=$I+1)
{

$exe=mysqli_query($dbLink,"INSERT INTO ka_quota Set yg='".$yg[$I]."',ygb='".$ygb[$I]."',ygc='".$ygc[$I]."',ygd='".$ygd[$I]."',xx='".$xx[$I]."',xxx='".$xxx[$I]."',username='".$_POST['kauser']."',userid='".$SoftID."',lx=0,flag=0,guanid='".$_POST['guanid']."',zongid=0,danid=0,memid=0,ds='".$ds[$I]."',style='".$style[$I]."' ");

} 

	


	echo "<script>alert('�ܴ���ӳɹ�!');window.location.href='index.php?action=zong_add';</script>"; 
exit;
	


}



if ($_GET['id']!="") {



$result=mysqli_query($dbLink,"select id,kauser,sf,cs,tmb,rs  from ka_guan where  id=".$_GET['id']." and lx=1"); 
$row=mysqli_fetch_assoc($result);
if ($row!=""){


$result1 = mysqli_query($dbLink,"Select SUM(cs) As sum_m  From ka_guan Where lx=2 and   guanid=".$row['id']." order by id desc");
	$rsw = mysqli_fetch_assoc($result1);
	if ($rsw[0]<>""){$mumu=$rsw[0];}else{$mumu=0;}
	
	 $result1 = mysqli_query($dbLink,"Select SUM(sum_m) As sum_m   From ka_tan Where kithe=".$Current_Kithe_Num." and   username='".$row['kauser']."' order by id desc");
	$rsw = mysqli_fetch_assoc($result1);
	if ($rsw[0]<>""){$mkmk=$rsw[0];}else{$mkmk=0;}
	
		$result1 = mysqli_query($dbLink,"Select SUM(rs) As memnum2 From ka_guan Where  lx=2 and guanid=".$row['id']." order by id desc");
	$rsw = mysqli_fetch_assoc($result1);
	if ($rsw[0]<>""){$rs1=$rsw[0];}else{$rs1=0;}
	

	
$rs1=$row['rs']-$rs1;
$guanid=$row['id'];
$maxnum=$row['cs']-$mumu-$mkmk;
$istar=0;
$iend=$row['sf'];
$tmb=$row['tmb'];


}else{
$maxnum=2000000000;
$rs1=1000;
$istar=0;
$iend=100;
$tmb=0;
}







}


?>








<link rel="stylesheet" href="images/xp.css?v=<?php echo AUTOVER; ?>" type="text/css">
<script language="javascript" type="text/javascript" src="js_admin.js?v=<?php echo AUTOVER; ?>"></script>
<script language="JavaScript" src="tip.js?v=<?php echo AUTOVER; ?>"></script>

<style type="text/css">
<!--
.style1 {
	color: #666666;
	font-weight: bold;
}
.style2 {color: #FF0000}
.STYLE3 {color: #FFFFFF;
	font-weight: bold;
}
-->
</style>
<div align="center">
<link rel="stylesheet" href="xp.css?v=<?php echo AUTOVER; ?>" type="text/css">
<script language="javascript" type="text/javascript" src="js_admin.js?v=<?php echo AUTOVER; ?>"></script>
<script src="inc/forms.js?v=<?php echo AUTOVER; ?>"></script>
<script language="JavaScript" type="text/JavaScript">
function SelectAllPub() {
	for (var i=0;i<document.form1.flag.length;i++) {
		var e=document.form1.flag[i];
		e.checked=!e.checked;
	}
}
function SelectAllAdm() {
	for (var i=0;i<document.form1.flag.length;i++) {
		var e=document.form1.flag[i];
		e.checked=!e.checked;
	}
}
</script>
<SCRIPT>
function LoadBody(){

}
function SubChk()
{
	
 	if(document.all.temppid.value=='')
 		{ document.all.temppid.focus(); alert("��ѡ���ϼ�!!"); return false; }
		
		if(document.all.kapassword.value=='')
 		{ document.all.kapassword.focus(); alert("�������������!!"); return false; }
		
		if(document.all.xm.value=='')
 		{ document.all.xm.focus(); alert("�������������!!"); return false; }
  	
 	
 	if(document.all.kauser.value=='')
 		{ document.all.alias.focus(); alert("�û������������!!"); return false; }
		
		
  	if(document.all.cs.value=='')
		{ document.all.maxcredit.focus(); alert("�����ö�����������!!"); return false; }
 	
	if(!confirm("�Ƿ�ȷ��д���ܴ�����?")){
  		return false;
 	}
}

function roundBy(num,num2) {
	return(Math.floor((num)*num2)/num2);
}
function show_count(w,s) {
	//alert(w+' - '+s);
	var org_str=document.all.ag_count.innerHTML
	if (s!=''){
		switch(w){
			//case 0:document.all.ag_count.innerHTML = s+org_str.substr(1,4);break;
			case 1:document.all.ag_count.innerHTML = org_str.substr(0,0)+s+org_str.substr(1,7);break;
			case 2:document.all.ag_count.innerHTML = org_str.substr(0,1)+s+org_str.substr(2,7);break;
			case 3:document.all.ag_count.innerHTML = org_str.substr(0,2)+s+org_str.substr(3,7);break;
			case 4:document.all.ag_count.innerHTML = org_str.substr(0,3)+s+org_str.substr(4,7);break; 
			case 5:document.all.ag_count.innerHTML = org_str.substr(0,4)+s+org_str.substr(5,7);break;
			case 6:document.all.ag_count.innerHTML = org_str.substr(0,5)+s+org_str.substr(6,7);break;
			case 7:document.all.ag_count.innerHTML = org_str.substr(0,6)+s+org_str.substr(7,7);break; }
	}
}
function changelocation(locationid,result)
{
var onecount;
subcat = new Array();
   
    document.testFrm.zc.length = 1; 
	    var locationid=locationid;
    var i;
		var k
	   for (j=10;j.toFixed(3)<=(result-locationid).toFixed(3);j=j+10)
   {
   		document.testFrm.zc.options[document.testFrm.zc.length] = new Option((j).toFixed(0)+"%");
	}
    
}
function changep(pid)
{
	var pp=pid.split(",");
	document.testFrm.guanid.value = pp[0];
	document.testFrm.kyx.value = pp[2];
	t=parseInt(pp[1]);
    document.testFrm.zc.length = 1; 
	for (j=10;j<=(t).toFixed(3);j=j+10)
   {
   		document.testFrm.zc.options[document.testFrm.zc.length] = new Option((j).toFixed(0)+"%");
	}
    document.testFrm.fei_max.length = 1; 
	for (j=10;j<=(t).toFixed(3);j=j+10)
   {
   		document.testFrm.fei_max.options[document.testFrm.fei_max.length] = new Option((j).toFixed(0)+"%");
	}
}

function changep1(pid)
{
var pp=pid.split(",");

	document.testFrm.winloss.value = pp[0];
	document.testFrm.bank.value = pp[1];
document.all.ag_count.innerHTML =pp[1];
}



function CountGold(gold,type,rtype){
goldvalue = gold.value;
str1="kyx";
zmzm=document.all[str1].value;
if (goldvalue=='') goldvalue=0;
if (rtype=='SP' && (eval(goldvalue) > eval(zmzm))) {gold.focus(); alert("�Բ���,�ܴ��������ö��������� : "+eval(zmzm)+"!!"); return false;}
}

function CountGold2(gold,type,rtype){
goldvalue = gold.value;
str1="rs1";
zmzm=document.all[str1].value;
if (goldvalue=='') goldvalue=0;
if (rtype=='SP' && (eval(goldvalue) > eval(zmzm))) {gold.focus(); alert("�Բ���,�ܴ�������������� : "+eval(zmzm)+"!!"); return false;}
}

function CountGold1(gold,type,rtype,bb){

goldvalue = gold.value;


if (goldvalue=='') goldvalue=0;

if (rtype=='SP' && (eval(goldvalue) > eval(bb))) {gold.focus(); alert("�Բ���,ֹ����߲��ܳ����ϼ��޶�: "+eval(bb)+"!!"); return false;}



}
</SCRIPT>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr class="tbtitle">
    <td width="29%"><span class="STYLE3">����ܴ����û�</span></td>
    <td width="34%">&nbsp;</td>
    <td width="37%">&nbsp;</td>
  </tr>
  <tr >
    <td height="5" colspan="3"></td>
  </tr>
</table>
<table width="99%"  border="1" cellpadding="2" cellspacing="2" bordercolor="#ECE9D8">
 <form name=testFrm onSubmit="return SubChk()" method="post" action="index.php?action=zong_add&act=���&id=<?php echo $_GET['id']?>"> <tr>
    <td height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">�ϼ��û���</td>
    <td height="30" colspan="3" bordercolor="#CCCCCC"><input name="guanid" type="hidden" value="<?php echo $guanid?>" />
        <select class="zaselect_ste" name="temppid" onchange="var jmpURL=this.options[this.selectedIndex].value ; if(jmpURL!='') {window.location=jmpURL;} else {this.selectedIndex=0 ;}">
          <option value="" ></option>
          <?php
		$result = mysqli_query($dbLink,"select id,kauser,sf,cs  from ka_guan  where lx=1");   
while($image = mysqli_fetch_assoc($result)){


$result1 = mysqli_query($dbLink,"Select SUM(cs) As sum_m  From ka_guan Where lx=2 and   guanid=".$image['id']." order by id desc");
	$rsw = mysqli_fetch_assoc($result1);
	if ($rsw[0]<>""){$mumul=$rsw[0];}else{$mumul=0;}
	
	 $result1 = mysqli_query($dbLink,"Select SUM(sum_m) As sum_m   From ka_tan Where kithe=".$Current_Kithe_Num." and   username=".$image['kauser']." order by id desc");
	$rsw = mysqli_fetch_assoc($result1);
	if ($rsw[0]<>""){$mkmkl=$rsw[0];}else{$mkmkl=0;}
	
	$cscs=$image['cs']-$mumul-$mkmkl;
	
			   
			     echo "<OPTION value=index.php?action=zong_add&id=".$image['id'];
				
				
				 if ($guanid!="") {
				 if ($guanid==$image['id']) {
				  echo " selected=selected ";
				  }				
				}
				
				 echo ">".$image['kauser']."--".$cscs."</OPTION>";
				 
				 
			  }
		?>
        </select>
        <span class="STYLE2">*(����ѡ����ϼ�)
          <?php echo $guanid?>
          /
          <?php echo $_GET['id']?>
        </span></td>
  </tr>
  <tr>
    <td width="11%" height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">�˺ţ�</td>
    <td width="27%" bordercolor="#CCCCCC"><input name="kauser" type="text" class="input1"  id="kauser" />
        <span class="STYLE2"> *</span></td>
    <td width="9%" height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">������</td>
    <td width="53%" bordercolor="#CCCCCC"><input name="xm" type="text" class="input1"  id="xm" />
        <span class="STYLE2">*</span> </td>
  </tr>
  <tr>
    <td height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">���룺</td>
    <td bordercolor="#CCCCCC"><input name="kapassword" type="password" class="input1"  id="kapassword" />
        <span class="STYLE2">*</span> </td>
    <td align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">�����ö</td>
    <td bordercolor="#CCCCCC"><input 
					  onblur="return CountGold(this,'blur','SP');"
					    
					    onkeyup="return CountGold(this,'keyup');" 
						
					  name="cs" type="text" class="input1"  id="cs" value="0" />
      �������ö�ȣ�
      <input type="text" name="kyx" class="input1"  readonly="readonly" value="<?php echo $maxnum?>" /></td>
  </tr>
  <tr>
    <td height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">�߷ɣ�</td>
    <td bordercolor="#CCCCCC"><input type="hidden" name="tv5" value="��">
        <img src="images/icon_21x21_selectboxon.gif" name="tv5_b" align="absmiddle" class="cursor" id="tv5_b" onclick="javascript:ra_select('tv5')" />(�����߷�/��ֹ�߷�)<span class="STYLE2">*</span></td>
    <td height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">״̬��</td>
    <td bordercolor="#CCCCCC"><input type="hidden" name="tv6" value="��" />
        <img src="images/icon_21x21_selectboxon.gif" name="tv6_b" align="absmiddle" class="cursor" id="tv6_b" onclick="javascript:ra_select('tv6')" />(����/��ֹ)<span class="STYLE2">*</span></td>
  </tr>
  <tr>
    <td height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">�ɶ�ռ�ɣ�</td>
    <td bordercolor="#CCCCCC"><span class="STYLE2">
      <select class="za_select_02" name="sj"  id="zc">
        <?php for ($bb=$istar; $bb<=$iend; $bb=$bb+10)
{
?>
        <option value="<?php   echo $bb; ?>">
        <?php   switch ($bb)
  {
    case 0:
      print "��ռ��";
      break;
    default:

      print $bb."%";
      break;
  } ?>
        </option>
        <?php 
} ?>
      </select>
      *
          
      </span></td>
    <td height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">�ܴ���ռ�ɣ�</td>
    <td bordercolor="#CCCCCC"><span class="STYLE2">
      <select class="za_select_02" name="sf"  id="zc">
        <?php for ($bb=$istar; $bb<=$iend; $bb=$bb+10)
{
?>
        <option value="<?php   echo $bb; ?>">
        <?php   switch ($bb)
  {
    case 0:
      print "��ռ��";
      break;
    default:

      print $bb."%";
      break;
  } ?>
        </option>
        <?php 
} ?>
      </select>
      *
  <input name="sff" type="hidden" id="sff" value="<?php echo $iend?>" />
    </span></td>
  </tr>
  <tr>
    <td height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">�Է�������B��</td>
    <td height="30" bordercolor="#CCCCCC"><select name="tmb" id="tmb">
        <?php if ($tmb!=1){?>
		<option value="0" selected="selected">����</option>
		<?php }?>
        
		<option value="1">������</option>
      </select>    </td>
    <td height="30" align="right" bordercolor="#CCCCCC" bgcolor="#FDF4CA">��Ա������</td>
    <td bordercolor="#CCCCCC"><input onblur="return CountGold2(this,'blur','SP');"
					    
					    onkeyup="return CountGold2(this,'keyup');" 
						name="rs" type="text" class="input1"  id="rs" value="0" size="10" />
      <span class="STYLE2">
      <input name="rs1" type="hidden" id="rs1" value="<?php echo $rs1?>" />
      ��ࣺ<?php echo $rs1?></span></td>
  </tr>
  <tr>
    <td height="30" colspan="4" bordercolor="#CCCCCC"><table width="100%" border="1" cellpadding="3" cellspacing="1" bordercolor="f1f1f1">
      <tr >
        <td width="90" height="25" align="center" bgcolor="#FDF4CA"><span class="STYLE2">����</span> </td>
        <td align="center" bgcolor="#FDF4CA">Ӷ��%A</td>
        <td align="center" bgcolor="#FDF4CA" >Ӷ��%B</td>
        <td align="center" bgcolor="#FDF4CA" >Ӷ��%C</td>
        <td align="center" bgcolor="#FDF4CA" >Ӷ��%D</td>
        <td align="center" bgcolor="#FDF4CA" >��ע�޶�</td>
        <td align="center" bgcolor="#FDF4CA" >����(��)�޶�</td>
      </tr>
      <?php 
					   
					   if ($guanid!=""){
					      $result = mysqli_query($dbLink,"select * from  ka_quota where userid=".$guanid." and  lx=0  and flag=0 order by id"); 
					   }else{
					   $result = mysqli_query($dbLink,"select * from  ka_guands where lx=0 order by id"); }
					   
					   
	$t=0;				     
while($image = mysqli_fetch_assoc($result)){
    //if ($image['ds'] == "����") continue;

?>
      <tr>
        <td height="20" align="center" bgcolor="#FDF4CA"><?php echo $image['ds']?>
              <input name="ds[]" type="hidden" id="ds[]" value="<?php echo $image['ds']?>" />
			  <input name="style[]" type="hidden" id="style[]" value="<?php echo $image['style']?>" /></td>
        <td align="center" bgcolor="#FEFBE9"><input name="m[]" class="input1" id="m[]" value='<?php echo $image['yg']?>' size="10" /></td>
        <td align="center" bgcolor="#FEFBE9"><input name="ygb[]" class="input1" id="mm[]" value='<?php echo $image['ygb']?>' size="10" /></td>
        <td align="center" bgcolor="#FEFBE9"><input name="ygc[]" class="input1" id="ygc[]" value='<?php echo $image['ygc']?>' size="10" /></td>
        <td align="center" bgcolor="#FEFBE9"><input name="ygd[]" class="input1" id="ygd[]" value='<?php echo $image['ygd']?>' size="10" /></td>
        <td align="center" bgcolor="#FEFBE9"><input name="mm[]" class="input1" id="mm[]" value='<?php echo $image['xx']?>' size="10" /></td>
        <td align="center" bgcolor="#FEFBE9"><input name="mmm[]" class="input1" id="mmm[]" value='<?php echo $image['xxx']?>' size="10" /></td>
      </tr>
      <?php 
	  $t++;
	  		 // if($t==35){echo "<tr><td>����</td></tr>";}
			 // if($t==54){echo "<tr><td>���</td></tr>";}
	  }?>
    </table></td>
  </tr>
  <tr>
    <td height="30" bordercolor="#CCCCCC">&nbsp;</td>
    <td colspan="3" bordercolor="#CCCCCC"><br />
        <table width="100" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="6"></td>
          </tr>
        </table>
      <input  class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" type="submit" name="Submit" value="�����ܴ���" />
        <br />
        <table width="100" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="10"></td>
          </tr>
      </table></td>
  </tr>
  </form>
</table>
</div>
