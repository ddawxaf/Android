<?php
if(!defined('PHPYOU')) {
	exit('�Ƿ�����');
}
if ($_GET['ids']!="") {$ids=$_GET['ids'];}else{$ids="��Ф";}
$class2=$ids;

if ($_GET['act']=="�޸�") {
for ($tt=1; $tt<=12; $tt++) {
if (empty($_POST['lm'.$tt])) {       
  echo "<script>alert('���ʲ���Ϊ��:".$_POST['Num_'.$tt]."/".$tt."!');window.history.go(-1);</script>"; 
  exit;
}

 }
 
 
 for ($tt=1; $tt<=12; $tt++) {
 
 $num=$_POST['lm'.$tt];
 

  
 $num1=$num+ka_config(3);
 $num2=$num-ka_config(3);
 
  $num3=$num+ka_config(4);
 $num4=$num-ka_config(4);
  $num5=$num+ka_config(5);
 $num6=$num-ka_config(5);
 
 $class3=$_POST['class3_'.$tt];
  $class2=$_POST['class2_'.$tt];
 
  $text=date("Y-m-d H:i:s");
$exe=mysqli_query($dbMasterLink,"update ka_bl  set adddate='". $text."',rate=".$num." where class1='��Ф' and class2='".$class2."' and  class3='".$class3."'");

 
 

 }//for
 
 
echo "<script>alert('�޸ĳɹ�!');window.location.href='index.php?action=rake_sxx&ids=".$ids."';</script>"; 
exit;
	
}//����


$result=mysqli_query($dbLink,"Select rate,class3,class2,locked from ka_bl where class1='��Ф' and class2='".$class2."' Order By ID");
$ShowTable = array();
$y=0;
while($Image = mysqli_fetch_assoc($result)){
$y++;
array_push($ShowTable,$Image);

}

$drop_count=$y-1;

?>

<link rel="stylesheet" href="images/xp.css?v=<?php echo AUTOVER; ?>" type="text/css">


<script>

function makeRequest(url) {

    http_request = false;
   
    if (window.XMLHttpRequest) {
   
        http_request = new XMLHttpRequest();
   
        if (http_request.overrideMimeType){
   
            http_request.overrideMimeType('text/xml');
   
        }
   
    } else if (window.ActiveXObject) {
   
        try{
       
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
       
        } catch (e) {
       
            try {
           
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
           
            } catch (e) {
       
            }
   
        }

     }
     if (!http_request) {
     
        alert("Your browser nonsupport operates at present, please use IE 5.0 above editions!");
       
        return false;
       
     }
 

//method init,no init();
 http_request.onreadystatechange = init;
 
 http_request.open('GET', url, true);

//Forbid IE to buffer memory
 http_request.setRequestHeader("If-Modified-Since","0");

//send count
 http_request.send(null);

//Updated every two seconds a page
 setTimeout("makeRequest('"+url+"')", <?php echo $ftime?>);

}


function init() {
 
    if (http_request.readyState == 4) {
   
        if (http_request.status == 0 || http_request.status == 200) {
       
            var result = http_request.responseText;
			
           
            if(result==""){
           
                result = "Access failure ";
           
            }
           
		   var arrResult = result.split("###");	
		   for(var i=0;i<12;i++)
{	   
		   arrTmp = arrResult[i].split("@@@");
		   


num1 = arrTmp[0]; //�ֶ�num1��ֵ
num2 = arrTmp[1]; //�ֶ�num2��ֵ
num3 = arrTmp[2]; //�ֶ�num1��ֵ
num4 = arrTmp[3]; //�ֶ�num2��ֵ
var bl;
bl="bl"+i;
document.all[bl].innerHTML= num2;

var gold;
gold="gold"+i;

document.all[gold].innerHTML= "<font color=ff6600>"+num4+"</font>";



}
			
			
           
        } else {//http_request.status != 200
           
                alert("Request failed! ");
       
        }
   
    }
 
}


function UpdateRate(commandName,inputID,cellID,strPara)
{
	//���ܣ���strPara���������͸�rake_updateҳ�棬�������ؽ���ش�
	//���������	inputID,cellID:Ҫ��ʾ�������ݵ�ҳ��ؼ���
	//		strPara���������͸�rake_updateҳ��Ĳ���
	//class1:���1
	//ids:(��class2)�������Ϊ��A����B��qtqt:�������ȣ�lxlx��������1Ϊ�ӣ�����Ϊ��
	//class3:���������
	switch(commandName)
	{
		case "MODIFYRATE":	//��������
			{
				var strResult = sendCommand(commandName,"rake_update.php",strPara);
				
				if (strResult!="")
				{
					makeRequest('index.php?action=server&class1=��Ф&class2=<?php echo $ids?>')
					document.all[inputID].value=strResult;
					
				}
				break;
			}
		case "LOCK":		//�ر���Ŀ
			{


				var strResult=sendCommand(commandName,"rake_update.php",strPara);
				

				if (strResult!="")
				
				{
					if(strResult=='1')					
						document.all[inputID].checked=true;
					else
						document.all[inputID].checked=false;
				}else{
				
				
					document.all[inputID].checked=!document.all[inputID].checked;
				}
				break;
			}
		default:	//�������
	}
}
function sendCommand(commandName,pageURL,strPara)
{
	//���ܣ���pageURLҳ�淢�����ݣ�����ΪstrPara
	//���ش����������ص�����
	var oBao = new ActiveXObject("Microsoft.XMLHTTP");
	//�����ַ���+,%,&,=,?�ȵĴ������취.�ַ�������escape�����.
	oBao.open("GET",pageURL+"?commandName="+commandName+"&"+strPara,false);
	oBao.send();
	//�������˴����ص��Ǿ���escape������ַ���.
	var strResult = unescape(oBao.responseText);
	return strResult;
}


</script>
<SCRIPT language=javascript>


function adv_format(value,num) //��������
{
var a_str = formatnumber(value,num);
var a_int = parseFloat(a_str);
if (value.toString().length>a_str.length)
{
var b_str = value.toString().substring(a_str.length,a_str.length+1)
var b_int = parseFloat(b_str);
if (b_int<5)
{
return a_str
}
else
{
var bonus_str,bonus_int;
if (num==0)
{
bonus_int = 1;
}
else
{
bonus_str = "0."
for (var i=1; i<num; i++)
bonus_str+="0";
bonus_str+="1";
bonus_int = parseFloat(bonus_str);
}
a_str = formatnumber(a_int + bonus_int, num)
}
}
return a_str
}

function formatnumber(value,num) //ֱ��ȥβ
{
var a,b,c,i

a = value.toString();
b = a.indexOf('.');
c = a.length;
if (num==0)
{
if (b!=-1)
a = a.substring(0,b);
}
else
{
if (b==-1)
{
a = a + ".";
for (i=1;i<=num-1;i++)
a = a + "0";
}
else
{
a = a.substring(0,b+num+1);
for (i=c;i<=b+num-1;i++)
a = a + "0";
}
}
return a
}

var ball_color = Array(0,0,1,1,2,2,0,0,1,1,2,0,0,1,1,2,2,0,0,1,2,2,0,0,1,1,2,2,0,0,1,2,2,0,0,1,1,2,2,0,1,1,2,2,0,0,1,1,2);
var bcolor = Array('red','blue','green');

function sel_col_ball(color)
{
	var c;
	var str1
	var zmzm
	var zmn=0.5
	var zmnn=0.1
	switch(color) {
		case 'blue':
			c = 1;
			break;
		case 'red':
			c = 0;
			break;
		case 'green':
			c = 2;
			break;
		case 'alal':
			c = 4;
			break;
		case 'all':
			c = 5;
			break;
		default:
			return;
			break;
	}
	
	if (c==4 ){
		
		var m=0
	for(i=0; i<99 ;i++)
	{
				
		m++
			str1="lm"+m;
			var t_big = new Number(document.all[str1].value);
t_big*=10000;
t_big+=10000*zmnn;
t_big/=10000;
document.all[str1].value=adv_format(t_big,2);

}
	
	}else{
	
	var m=0
	for(i=0; i<32 ;i++)
	{
				
		m++
		
			str1="lm"+m;
			var t_big = new Number(document.all[str1].value);
t_big*=10000;
t_big+=10000*zmnn;
t_big/=10000;
document.all[str1].value=adv_format(t_big,2);

}
	}
}


function sel_col_ball1(color,sj)
{
	var c;
	var str1
	var zmzm
	var zmn=0.5
	var zmnn=0.1
	switch(color) {
		case 'blue':
			c = 1;
			break;
		case 'red':
			c = 0;
			break;
		case 'green':
			c = 2;
			break;
		case 'alal':
			c = 4;
			break;
		case 'all':
			c = 5;
			break;
		default:
			return;
			break;
	}
	
	if (c==4 ){var m=0
	for(i=0; i<99 ;i++)
	{
				
		m++
		
			str1="lm"+m;
			var t_big = new Number(document.all[str1].value);
t_big*=10000;
t_big-=10000*zmnn;
t_big/=10000;
document.all[str1].value=adv_format(t_big,2);

		
			
			
		
}
		
		//// document.all.t_double.value=eval(document.all.t_double.value+"-"+zmnn);
		 ///document.all.t_big.value=eval(document.all.t_big.value+"-"+zmnn);
		 ///document.all.t_small.value=eval(document.all.t_small.value+"-"+zmnn);
		/// document.all.h_signle.value=eval(document.all.h_signle.value+"-"+zmnn);
		//document.all.h_double.value=eval(document.all.h_double.value+"-"+zmnn);
	
	}else{
	var m=0
	for(i=0; i<32 ;i++)
	{
		m++
	
			if (ball_color[i] == c)
		{
			str1="lm"+m;
		zmzm=document.all[str1].value;
			zmzm=eval(zmzm+"-"+sj);
			 document.all[str1].value =zmzm ;
		}
}
	}
}

function j_soj(a,b,c)
{

if (c==1 ){

var t_big = new Number(document.all[a].value);
t_big*=100;
t_big+=100*b;
t_big/=100;
document.all[a].value=adv_format(t_big,2);

	
	
	}else{
	
var t_big = new Number(document.all[a].value);
//t_big*=100;
t_big-=b;
//t_big/=100;
document.all[a].value=adv_format(t_big,2);
	}
}

function j_dx(b,c,sj)
{

var zmn=0.5;

switch(b) {
		case '1':
			
			s=25;
			e=50;
			break;
		case '2':
		
		s=1;
		e=25;
			break;
		
			
			
		case '20':
			d = 20;
			break;
		default:
			return;
			break;
	}
	

if (c==1 ){


	for(i=s; i<e ;i++)
	{			
		
		
			
			str1="Num_"+i;
		zmzm=document.all[str1].value;
			zmzm=eval(zmzm+"+"+sj);
			
			 document.all[str1].value =zmzm ;
			
			
		
}



	
	
	}else{
	
for(i=s; i<e ;i++)
	{			
		
		
			
			str1="Num_"+i;
		zmzm=document.all[str1].value;
			zmzm=eval(zmzm+"-"+sj);
			
			 document.all[str1].value =zmzm ;		
		
		
}

	
	}


}


function j_ds(b,c,sj)
{

var zmn=0.5;


switch(b) {
		case '1':
			
			
			var e=1;
			break;
		case '2':
		
			
		e=0;
		
			break;
		
			
			
		case '20':
			d = 20;
			break;
		default:
			return;
			break;
	}
	m=0

if (c==1 ){


for(i=0; i<49 ;i++)
	{
	m++
	if ((i+1) % 2 == e)
	{			
		
		
			
			str1="Num_"+m;
		zmzm=document.all[str1].value;
			zmzm=eval(zmzm+"+"+sj);
			
			 document.all[str1].value =zmzm ;
			
			
		
}

}

	
	
	}else{
	m=0

for(i=0; i<49 ;i++)
	{m++
	if ((i+1) % 2 == e)
	{			
		
		
			
			str1="Num_"+m;
		zmzm=document.all[str1].value;
			zmzm=eval(zmzm+"-"+sj);
			
			 document.all[str1].value =zmzm ;
			
			
		
}

}

	
	}


}


function j_dsx(b,c,sj)
{

var zmn=0.5;


switch(b) {
		case '1':
			s=25;
			f=50;
			e=1;
			break;
		case '2':
		
		s=25;
		f=50;
		
		e=0;
			break;
			
	case '3':
			s=1;
			f=25;
			e=1;
			break;
		case '4':
		
		s=1;
		f=25;
		
		e=0;
			break;
		
		
			
			
		case '20':
			d = 20;
			break;
		default:
			return;
			break;
	}
	

if (c==1 ){


for(i=s; i<f ;i++)
	{
	
	if ((i+1) % 2 == e)
	{			
		
		
			
			str1="Num_"+i;
		zmzm=document.all[str1].value;
			zmzm=eval(zmzm+"+"+sj);
			
			 document.all[str1].value =zmzm ;
			
			
		
}

}

	
	
	}else{
	
m=0
for(i=s; i<f ;i++)
	{
	
	if ((i+1) % 2 == e)
	{			
		
		
			
			str1="Num_"+i;
		zmzm=document.all[str1].value;
			zmzm=eval(zmzm+"-"+sj);
			
			 document.all[str1].value =zmzm ;
			
			
		
}

}

	
	}


}



</SCRIPT>
<style type="text/css">
<!--
.STYLE3 {color: #FF0000}
.style2 {color: #0000FF}
-->
</style>
<body oncontextmenu="return false"   onselect="document.selection.empty()" oncopy="document.selection.empty()" 
>
<noscript>
<iframe scr=��*.htm��></iframe>
</noscript>

<div align="center">
<link rel="stylesheet" href="xp.css?v=<?php echo AUTOVER; ?>" type="text/css">


       <table width="100%" border="0" cellspacing="0" cellpadding="5">
         <tr class="tbtitle">
           <td width="81%"><?php require_once 'retop.php';?></td>
           <td width="19%">&nbsp;</td>
         </tr>
      <tr>
                  <td><font color="#FF0000"> <strong>
                    <?php echo $ids?>
                    </strong></font></td>
                  <td><div align="right">
                    <button onClick="javascript:location.href='index.php?action=rake_sxx&ids=��Ф';"  class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" style="height:22" ;><img src="images/add.gif" width="16" height="16" align="absmiddle"><SPAN id=rtm1 STYLE='color:<?php echo $z1color?>;'>��Ф</span></button>&nbsp;
                    <button onClick="javascript:location.href='index.php?action=rake_sxx&ids=��Ф';"  class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" style="height:22" ;><img src="images/add.gif" width="16" height="16" align="absmiddle"><SPAN id=rtm2 STYLE='color:<?php echo $z2color?>;'>��Ф</span></button>
                    <button onClick="javascript:location.href='index.php?action=rake_sxx&ids=��Ф';"  class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" style="height:22" ;><img src="images/add.gif" width="16" height="16" align="absmiddle"><SPAN id=rtm2 STYLE='color:<?php echo $z2color?>;'>��Ф</span></button>
                    <button onClick="javascript:location.href='index.php?action=rake_sxx&ids=��Ф';"  class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" style="height:22" ;><img src="images/add.gif" width="16" height="16" align="absmiddle"><SPAN id=rtm2 STYLE='color:<?php echo $z2color?>;'>��Ф</span></button>
                    <button onClick="javascript:location.href='index.php?action=rake_sxx&ids=��Ф';"  class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" style="height:22" ;><img src="images/add.gif" width="16" height="16" align="absmiddle"><SPAN id=rtm2 STYLE='color:<?php echo $z2color?>;'>��Ф</span></button>
                  </div></td>
                </tr>
       </table>
    
	
        
          <table border="1" align="center" cellspacing="1" cellpadding="2" bordercolor="f1f1f1" bordercolordark="#FFFFFF" width="99%">
            <form name="form1" method="post" action="index.php?action=rake_sxx&act=�޸�&ids=<?php echo $ids?>"><TBODY>
              <TR align=center>
                <TD height=26 colSpan=2 bordercolor="cccccc" bgcolor="#FDF4CA" >����</TD>
                <TD width="22%" bordercolor="cccccc" bgcolor="#FDF4CA" >����/���</TD>
                <TD width="22%" bordercolor="cccccc" bgcolor="#FDF4CA" >��ǰ����</TD>
                <TD width="26%" bordercolor="cccccc" bgcolor="#FDF4CA" >��ע���</TD>
              </TR>
             <?php
			 for ($I=0; $I<=11; $I=$I+1)
{?> 
			  <TR>
                <TD width="15%" align="center" bordercolor="cccccc"><?php echo $ShowTable[$I][2]?></TD>
                <TD width="15%" align="center" bordercolor="cccccc"><?php echo $ShowTable[$I][1]?></TD>
                <TD align="center" bordercolor="cccccc"><table border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><INPUT  style="HEIGHT: 18px"  class="input1" size=10 value='<?php echo $ShowTable[$I][0]?>' name=lm<?php echo $I+1?>>
                        <input  name="class3_<?php echo $I+1?>" value="<?php echo $ShowTable[$I][1]?>" type="hidden" >
                        <input name="class2_<?php echo $I+1?>" value="<?php echo $ShowTable[$I][2]?>" type="hidden" ></td>
                    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td><a style="cursor:hand" onClick="UpdateRate('MODIFYRATE','lm<?php echo $I+1?>','bl<?php echo $I?>','class1=��Ф&ids=<?php echo $ShowTable[$I][2]?>&sqq=sqq&lxlx=1&qtqt=0.01&class3=<?php echo $ShowTable[$I][1]?>');"><img src="images/bvbv_01.gif"   width="19" height="17" border="0"></a></td>
                        </tr>
                        <tr>
                          <td><a style="cursor:hand" onClick="UpdateRate('MODIFYRATE','lm<?php echo $I+1?>','bl<?php echo $I?>','class1=��Ф&ids=<?php echo $ShowTable[$I][2]?>&sqq=sqq&lxlx=0&qtqt=0.01&class3=<?php echo $ShowTable[$I][1]?>');"><img src="images/bvbv_02.gif" width="19" height="17" border="0"  ></a></td>
                        </tr>
                    </table></td>
                    <td><input name="checkbox" type=checkbox id=lock<?php echo $I?> style="zoom:95%" title="�رո���" onClick="UpdateRate('LOCK','lock<?php echo $I?>','','class1=��Ф&ids=<?php echo $ShowTable[$I][2]?>&sqq=sqq&class3=<?php echo $ShowTable[$I][1]?>&lock='+this.checked);" value="TRUE"  <?php if ($ShowTable[$I][3]==1){echo "checked";}?>></td>
                  </tr>
                </table></TD>
                <TD align="center" bordercolor="cccccc"><span id=bl<?php echo $I?>><?php echo $ShowTable[$I][0]?></span></TD>
                <TD align="center" bordercolor="cccccc" ><span id=gold<?php echo $I?>>0</span></TD>
              </TR>
			  
			  <?php }?>
			  <TR>
			    <TD colspan="5" align="center" bordercolor="cccccc"><table width="98" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="88" align="center"><input name="button2"  class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" type=button onClick="javascript:sel_col_ball('alal')" value=��������></td>
                    <td width="88" align="center"><INPUT name="button3"  class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" type=button onClick="javascript:sel_col_ball1('alal')" value=���ʼ���></td>
                    <td width="88" align="center"><input type="submit"   class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" name="Submit2" value="�ύ" /></td>
                    <td width="88" align="center"><input type="reset"    class="but_c1" onMouseOut="this.className='but_c1'" onMouseOver="this.className='but_c1M'" name="Submit3" value="����" /></td>
                    <td>&nbsp;</td>
                  </tr>
                </table></TD>
		      </TR>
            </TBODY> </form>
          </TABLE>
     
      <SCRIPT language=javascript>
 makeRequest('index.php?action=server&class1=��Ф&class2=<?php echo $ids?>')</script>
