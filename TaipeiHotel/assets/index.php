<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes"/> <!-- 配合載具自動調整大小 -->
<meta name="apple-mobile-web-app-capable" content="yes" /> <!--應用於iOS的viewport函式-->

<title>來去台北住一晚</title>
<link href="jquery-mobile/jquery.mobile-1.4.5.min.css" rel="stylesheet" type="text/css">
<script src="jquery-mobile/jquery-2.1.3.min.js" type="text/javascript"></script>
<script src="jquery-mobile/jquery.mobile-1.4.5.min.js" type="text/javascript"></script>

<style>
	.title{
		color: #736148;
		font-size: 11pt;
		font-weight: bold
	}
	
	.ui-page{
		background: #CFBF9E;
	}
</style>

<script language="Javascript">
	var regionTitle= new Array();  // 記錄分區的名稱
	var counter = new Array();     // 記錄分區的編號
	var regionData = new Array();  // 記錄分區中所有旅館的資料
    // 從指定的 url 網址取得可跨網域的 json 資料，完成後以 ShowData()函式處理
	$(document).ready(function() {
		$.ajax({
			type:"GET",
			url: "http://data.taipei.gov.tw/opendata/apply/json/QTdBNEQ5NkQtQkM3MS00QUI2LUJENTctODI0QTM5MkIwMUZE",
			dataType: "json" ,
			success: ShowData,
			error: function(data){
				alert("系統錯誤！");
			},
		});	
	});
	
	function ShowData(data){
		for( var i=0 ,cnt=data.length ; i<cnt ; ++i ){
			// 從 json 資料取得區名
			var getRegion = data[i]["display_addr"].substring(0,data[i]["display_addr"].indexOf("區",0)+1);
			if( counter[getRegion] == undefined ){
				counter[getRegion] = regionData.length;  // 記錄分區的編號
				regionData.push(new Array());  // 新增陣列記錄一個分區中所有旅館的資料
				regionTitle[counter[getRegion]] = getRegion; // 記錄分區的名稱
			}
			// 將 data[i] 的資料放入 regionData[n] 動態陣列中 
			regionData[counter[getRegion]].push(data[i]);
		}
		
		$("#output").empty(); //清空
		// cntRegion 代表台北市有旅館共有幾區
		for( var i=0 ,cntRegion=regionData.length ; i<cntRegion ; ++i ){	
			var pageTitle = ""; // 儲存 [名稱]  
			var pageAddr="";    // 儲存 [地址] 
			var pageTel="";     // 儲存 [電話]
			
			// subCnt 代表每一區的旅館數目 
			for( var j=0,subCnt=regionData[i].length ; j<subCnt ; ++j ){
				pageTitle+=regionData[i][j]["name"]+"|"; // 組合 [名稱]
				pageAddr+=regionData[i][j]["display_addr"]+"|"; // 組合 [地址]
				pageTel+="聯絡電話："+regionData[i][j]["tel"]+"|"; // 組合 [電話]			
			}
			
			// 在 ListView 中加上以旅館名稱當作超連結
			var strHtml= '<li class="title"><a href="#show" data-title="' + regionTitle[i] + '" ';
			    strHtml+= 'page-title="' + pageTitle + '" page-addr="' + pageAddr + '" page-tel="' + pageTel +'">';
			    strHtml+= '<div >'+regionTitle[i]+'旅館資料</div> <span class="ui-li-count">'+ regionData[i].length+'</span></a></li>';
			$("#output").append(strHtml);	 
			
			// data-title 、page-title、page-addr、pag-tel 傳遞 分區名稱、旅館名稱、地址、電話
			$("a", $("#output")).bind("click", function(e) {
				getItem($(this).attr("data-title"),$(this).attr("page-title"),$(this).attr("page-addr"),$(this).attr("page-tel"));
			});		
		}
		$("#output").listview("refresh");
	}

	// 在 show 頁面顯示該區中所有旅館的 [旅館名稱、地址、連絡電話] 並建立超連結
	    function getItem(regiontitle,pagetitle,pageaddr,pagetel) {	
		$("#show h1").html(regiontitle+"旅館");  // 顯示分區 header 的標題
		$("#output2").empty();
		var Arytitle=pagetitle.split("|"); // 取得旅館的名稱
		var Aryaddr=pageaddr.split("|");   // 取得旅館的地址
		var Arytel=pagetel.split("|");     // 取得旅館的電話
		for( var i=0 ;i<Arytitle.length-1;i++){
			// 建立分區各旅館的資料和超連結
			var strHtml='<li><a href="#" data-addr="' + Aryaddr[i] + '"</a>';		
			strHtml+='<div class="title"><h3>' + Arytitle[i]+ '</h3></div>';
			strHtml+='<div>' + Aryaddr[i]+ '</div>';
			strHtml+='<div>' + Arytel[i] +'</div></li>';
			$("#output2").append(strHtml) ;  	
		}
		
		// 按下超連結時以 data-addr 傳遞該旅館的地址用以顯示該旅館的地圖 
		$("a",$("#output2")).bind("click", function(e) {
			SearchFor($(this).attr("data-addr"));  // 以地址搜尋旅館位置
		});	
		$("#output2").listview("refresh");
	}	
	
	function SearchFor(addr) { 
		window.open("http://maps.google.com/maps?hl=zh-TW&q=" + addr );
	}	
</script>

</head>

<body>
<div data-role="page" id="index">
  <div data-role="header" data-position="fixed"">
    <h1>來去台北住一晚</h1>
  </div>
  
  <div data-role="content">
  
   <div style="text-align:center"> 
     <img src="images/taipeiHotelogo.png" width="320" height="320" >
	  
     <!-- LIST VIEW -->
	 <ul data-role="listview" data-inset="true" id="output">
	  	<li></li>
	 </ul>
   </div>
  
  </div>
  
  <div data-role="footer" data-position="fixed"">
    <!--<h4>頁尾</h4>-->
  </div>
  
</div>


<div data-role="page" id="show">
  <div data-role="header" data-position="fixed"">
    <a data-role="button" data-rel="back" data-icon="arrow-l">back</a>
    <h1>來去台北住一晚</h1>
  </div>
  
  <div data-role="content">
  
   <div style="text-align:center"> 
     <img src="images/taipeiHotelogo.png" width="320" height="320" >
	  
     <!-- LIST VIEW -->
	 <ul data-role="listview" data-inset="true" id="output2">
	  	<li></li>
	 </ul>
   </div>
  
  <div data-role="footer" data-position="fixed"">
    <!--<h4>頁尾</h4>-->
  </div>
  
</div>

</body>
</html>