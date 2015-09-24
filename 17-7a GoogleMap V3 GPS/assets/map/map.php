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
	}    <!-- 圖片(文)置中及邊框效果(陰影&圓角)CSS語法-->

</style>

<style>	
   .ptext{
       font-size:28px;
       font-style:inherit;
       font-weight:bold;
       color:#1E4;
       text-align:center;	
	}    <!-- 字體設定CSS語法-->

</style>
  
<script language="javascript">
	$(function(){
	    //繪製地圖    
		var map_div=document.getElementById("map_div");	//宣告map_div並取得 map_div物件型態	       
		var lat=24.170566; //取得經緯度
		var lng=120.609932;
		
		var latlng = new google.maps.LatLng(lat,lng); //宣告經緯度物件變數
		var gmap= new google.maps.Map(map_div,{  
		      zoom:14, //設定地圖的縮放比例
			  center: latlng, //設定地圖中心點的經緯度，必須是LatLng物件型態
			  mapTypeId: google.maps.MapTypeId.ROADMAP //設定地圖的型(MapTypeId.ROADMAP:２Ｄ街景地圖)

		  });
		
		//自定地標
		var marker = new google.maps.Marker({
	    position: latlng,
	    icon:"image/Map-Icon-Hillel.png",
	    map: gmap,
	    title: " MY Company!! "
          });
		  
		//建立手機觸碰地標事件彈出  
        google.maps.event.addListener(marker, "click", function(event){	    
		var infowindow = new google.maps.InfoWindow({
		content:'<p class="mapcover"><img width="100" height="100" src="image/500px-anonymous_emblem-svg1.png"/><p><BR>'
		+'<a data-role="button" data-icon="plus">電話</a><BR>'
		+'<a data-role="button" data-icon="home">住址</a>'
		+'<p class="ptext">噗啦噗啦噗啦噗啦噗啦噗啦噗啦噗啦<p>'
	       });
		   
	    infowindow.open(gmap,marker);
           });
		  
		
	  }); //end fun
</script>

</head>

<body>
<div data-role="page" id="index">
  
  <div data-role="header" data-position="fixed">
    <h1>頁首</h1>
  </div>
  
  <div data-role="content">
  
   <div id="map_div">
    
   </div>
   
  </div>
  
  <div data-role="footer" data-position="fixed">
    <h4>頁尾</h4>
  </div>
  
</div>



</body>
</html>