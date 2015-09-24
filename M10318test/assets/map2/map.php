<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes"/>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script> <!-- 外掛google map的script函數集 -->

<title>地圖測試</title>
<link href="jquery-mobile/jquery.mobile-1.4.5.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="jquery-mobile/listview-grid.css"> <!-- 外掛listview-grid的CSS函數集 -->
<script src="jquery-mobile/jquery-2.1.3.min.js" type="text/javascript"></script>
<script src="jquery-mobile/jquery.mobile-1.4.5.min.js" type="text/javascript"></script>

<style>

	#map_div{
		margin: 0 auto;
		width: 280px;
		height: 440px;
		border: 5px solid #FFF;
		border-radius: 5px;   
		box-shadow: 2px 2px 2px 6px #666;
	}     <!-- 定義地圖顯示區域的 CSS 樣式 - 例如：大小為280*440 pixels、上下邊界為 0、左右置中 -->
	      <!-- 因為要給script呼叫 一定要用id 故利用#宣告-->

</style>

<style>
   .mapcover{
	   width:100px;
       margin-top:0px;
       margin-botton:0px;
       margin-right:auto;
       margin-left:auto;	
       box-shadow:2px 2px 2px 2px #999;
       border-radius:10px;
	}    

   .ptext{
       font-size:28px;
       font-style:inherit;
       font-weight:bold;
       color:#1E4;
       text-align:center;	
	}   
	<!-- 圖片(文)置中及邊框效果(陰影&圓角)CSS語ptext法-->
	<!-- 字體設定CSS語法-->

</style>
  
<script language="javascript">
	$(function(){  //載入地圖和地標主程式
	    $('#map').bind('pageshow',getmap);  //pageshow - 換頁時自動從新載入副函式getmap避免因暫存資料而未更新頁面			 
		 });   
			  
	var curGeoPoint={lat:24.170566, lng:120.609932, array:"TCNR", pic:"images/500px-anonymous_emblem-svg1.png"}; //設定初始頁面的經緯度(及外加圖文)
	
	function GetGeo(lat,lng,array,pic) {	//設定經緯度函式
        curGeoPoint.lat=lat;
	    curGeoPoint.lng=lng;
		
		curGeoPoint.array=array;
		curGeoPoint.pic=pic;
		// mappage 的 pageshow 會呼叫 GetMap() 顯示地圖
		$.mobile.changePage("#map", "slide", false, true); //changePage 轉頁時就會重新載入map
		//GetMap();  // 也可以在此直接呼叫 GetMap() 顯示地圖
	    e.preventDefault();  // 避免重複觸發
	}
  
		
	  function getmap(){	  //繪製地圖副函式
		var map_div=document.getElementById("map_div");	//宣告map_div並取得map_div物件型態	       
		
		var latlng = new google.maps.LatLng(curGeoPoint.lat,curGeoPoint.lng); //宣告經緯度物件變數
		var gmap= new google.maps.Map(map_div,{  
		    zoom:14, //設定地圖的縮放比例
			center: latlng, //設定地圖中心點的經緯度，必須是LatLng物件型態
			mapTypeId: google.maps.MapTypeId.ROADMAP //設定地圖的型(MapTypeId.ROADMAP:２Ｄ街景地圖)

		  });
		
		//自定地標
		var marker = new google.maps.Marker({
	    position: latlng,
	    icon:"images/Map-Icon-Hillel.png",
	    map: gmap,
	    title: curGeoPoint.array //直接顯示變數
          });
		  
		//建立手機觸碰地標事件彈出  
        google.maps.event.addListener(marker, "click", function(event){	    
		var infowindow = new google.maps.InfoWindow({
		content:'<p class="mapcover"><img width="100" height="100" src="'+curGeoPoint.pic+'"/><p>'
		        +'<p class="ptext">'+curGeoPoint.array+'<p>'
	       });
		   
	    infowindow.open(gmap,marker);
           });		
	  }
</script>

</head>

<body>
<div data-role="page" id="index">
  
 
  <div data-role="header" data-position="fixed">
    <!--<h1>頁首</h1>-->
  </div>

  <div data-role="content">
    <ul data-role="listview" data-inset="true" data-theme="b">
      <li data-role="list-divider" data-theme="a"><a class="iconcover">Attractions</a></li>
      <!--利用超連結方式傳遞經緯度至javascript-->
      <!--javascript的聯繫語法是:(php是? HTML是#)-->
      <li><a href="javascript:GetGeo(24.170566,120.609932,'中區職訓局','images/01.gif')">TCNR</a></li>             
      <li><a href="javascript:GetGeo(25.033773,121.564787,'台北世貿101','images/02.gif')">Taipei 101</a></li>       
      <li><a href="javascript:GetGeo(25.102398,121.548613,'國立故宮博物院','images/03.gif')">National Palace Museum</a></li>
      <li><a href="javascript:GetGeo(23.84932,120.929622,'日月潭風景區','images/04.gif')">Sun moon lake</a></li>
      <li><a href="javascript:GetGeo(21.949812,120.779816,'墾丁國家公園','images/05.gif')">Kenting National Park</a></li>
    </ul>
  </div>
  
  <div data-role="footer" data-position="fixed">
    <div data-role="navbar" data-iconpos="top">
     <ul>
      <li><a href="#index" data-icon="home" class="ui-btn-active">HOME</a></li>
      <li><a href="#map" data-icon="grid">MAP</a></li>
     </ul>
    </div>
  </div>
  
</div>

<div data-role="page" id="map">
  
 
  <div data-role="header" data-position="fixed">
    <!--<h1>頁首</h1>-->
  </div>

  <div data-role="content">
     <div id="map_div"></div>
  </div>
  
  <div data-role="footer" data-position="fixed">
    <div data-role="navbar" data-iconpos="top">
     <ul>
      <li><a href="#index" data-icon="home">HOME</a></li>
      <li><a href="#map" data-icon="grid" class="ui-btn-active">MAP</a></li>
     </ul>
    </div>
  </div>
  
</div>

</body>
</html>