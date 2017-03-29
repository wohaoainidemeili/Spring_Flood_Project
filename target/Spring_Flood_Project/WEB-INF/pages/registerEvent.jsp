<%--
  Created by IntelliJ IDEA.
  User: Yuan
  Date: 2017/1/12
  Time: 13:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
  <script src="resources/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css"></script>
  <script src="resources/jquery-2.1.1/jquery.min.js"></script>
  <script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  <script src="resources/jquery-form-3.51/jquery.form.min.js"></script>
  <!--<script src=" http://api.tianditu.com/api?v=4.0" type="text/javascript"></script>
  <script>
    var sensors;
    var map;
    var zoom = 3;
    function onLoad() {
      map = new T.Map('mapDiv', {
        attributionControl: false,
        inertia: false
    });
      map.centerAndZoom(new T.LngLat(116.40969, 37.43997405227057), zoom);
      test();
    }
    function test(){
      $.ajax({
        type:"post",
        url:"getSensorInfo",
        success:function(data){
          sensors=data;
          var arrayObj=new Array();
          for(var i=0;i<data.length;i++){
            var marker=new T.Marker(new T.LngLat(data[i].lon,data[i].lat),{title:i});
            marker.id=data[i].sensorID;
            marker.addEventListener("click",function(e){
              clickSensor(e,marker);
            });
            arrayObj.push(marker);
          }
          var makers=new T.MarkerClusterer(map,{markers:arrayObj});
        }
      });
    }
    function clickSensor(e,marker){
      var infoWin = new T.InfoWindow();
      var lnglat = new T.LngLat(e.lnglat.getLng(),e.lnglat.getLat());
      infoWin.setLngLat(lnglat);
      //设置信息窗口要显示的内容
      infoWin.setContent(marker.id);
      //向地图上添加信息窗口
      map.addOverLay(infoWin);
    }
  </script>
-->
  <script type="text/javascript" src="http://api.tianditu.com/js/maps.js"></script>
  <script type="text/javascript" src="http://api.tianditu.com/js/maptools.js"></script>
  <script>
    //show sensor info on the map
    var sensors;
    var map;
    var zoom = 5;
    var markers=[];
    var infoWin;
    function onLoad() {
      //初始化地图对象
      map=new TMap("mapDiv");
      //设置显示地图的中心点和级别
      map.centerAndZoom(new TLngLat(116.40969,39.89945),zoom);
      //允许鼠标滚轮缩放地图
      map.enableHandleMouseScroll();
      drawSensors();
      addSensorClickEvent(markers,sensors);
    }
    function drawSensors(){
      $.ajax({
        type:"post",
        url:"getSensorInfo",
        async:false,
        success:function(data){
          sensors=data;
          for(var i=0;i<data.length;i++){
            var marker=new TMarker(new TLngLat(data[i].lon,data[i].lat));
            markers.push(marker);
          }
          var config = {
            markers:markers
          };
          var ma=new TMarkerClusterer(map,config);
        }
      });
    }
    function addSensorClickEvent(iconMarkers,sensors){
      var len=iconMarkers.length;
      for(var i=0;i<len;i++){
        (function(){
          var m=iconMarkers[i];
          var sensor=sensors[i];
          TEvent.addListener(m,"click",function(){
            var lnglat=new TLngLat(m.getLngLat().getLng(),m.getLngLat().getLat());
            //创建信息窗口对象
            infoWin=new TInfoWindow(lnglat,new TPixel([0,-100]));
            //设置信息窗口要显示的内容
            var tablehtml="<table class=\"table\"> <tbody> <tr class=\"warning\"> <td>传感器编号： </td> <td>"+sensor.sensorID+"</td> </tr> <tr class=\"success\"> <td>传感器名称： </td> <td>"+sensor.sensorName+" </td> </tr> </tbody> </table>";
            infoWin.setLabel(tablehtml);
            //向地图上添加信息窗口
            map.addOverLay(infoWin);
          });
        })();
      }
    }
    function zoomToSensor(obj){
      var tr=obj;
      var lonlat=tr.cells[2].innerHTML;
      var lonlatArray= lonlat.split(",");
      map.centerAndZoom(new TLngLat(lonlatArray[1],lonlatArray[0]),7);
    }
    function submitSelectSensor(){
      var table=document.getElementById("sensorTable")
      var rowNum=table.getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;
      var rows=table.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
      var sensorIDs=new Array();
      for(var i=0;i<rowNum;i++){
        if(rows[i].cells[3].getElementsByTagName("input")[0].checked==true){
          sensorIDs.push(rows[i].cells[0].innerHTML);
        }
      }
      // submit the select function
      $.ajax({
        type:"post",
        url:"getSelectSensors",
        data:{"sensorIDs":sensorIDs},
        traditional: true,
        async:false,
        success:function(data){
          if(data==true){
            window.location.href="/simpleSubscribeEvnt";
          }
          else aert("请重新选择注册传感器！")

        }
      });

    }
  </script>
  <title>洪涝事件管理</title>
</head>
<body onload="onLoad()">
<div class="container">
  <div class="row clearfix">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <nav class="navbar navbar-default" role="navigation">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="#">Flood</a>
          </div>
          <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
              <li>
                <a href="#">洪涝事件管理</a>
              </li>
              <li class="active">
                <a href="#">水文传感器查看</a>
              </li>
            </ul>
            <form class="navbar-form navbar-left" role="search">
              <div class="form-group">
                <input type="text" class="form-control" />
              </div> <button type="submit" class="btn btn-default">搜索</button>
            </form>
            <ul class="nav navbar-nav navbar-right">
              <li>
                <a>尊敬的${userName},您好！</a>
              </li>
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">个人中心<strong class="caret"></strong></a>
                <ul class="dropdown-menu">
                  <li>
                    <a href="#">我的事件</a>
                  </li>
                  <li>
                    <a href="#">注册事件</a>
                  </li>
                  <li>
                    <a href="#">个人信息修改</a>
                  </li>
                  <li class="divider">
                  </li>
                  <li>
                    <a href="#">帮助</a>
                  </li>
                </ul>
              </li>
            </ul>
          </div>
        </nav>
      </div>
    </div>
  </div>
  <div class="row clearfix">
    <div class="col-md-12 column">
      <div id="mapDiv" style="position:relative;width:100%; height:0px;padding-bottom: 50%"></div>
    </div>
  </div>
  <div class="row clearfix pre-scrollable" style="padding-bottom:8%">
    <div class="col-md-12 column ">
      <table id="sensorTable" class="table table-hover">
        <thead>
        <tr>
          <th>
            传感器ID
          </th>
          <th>
            传感器名称
          </th>
          <th>
            传感器位置
          </th>
          <th>
            状态
          </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${sensorList}" varStatus="status">
          <c:if test="${status.index%2==0}">
            <tr onclick="zoomToSensor(this)">
              <td>${sensorList.get(status.index).sensorID}</td>
              <td>${sensorList.get(status.index).sensorName}</td>
              <td>${sensorList.get(status.index).lat},${sensorList.get(status.index).lon}</td>
              <td><input type="checkbox" value=""></td>
            </tr>
          </c:if>
          <c:if test="${status.index%2==1}">
            <tr class="info" onclick="zoomToSensor(this)">
              <td>${sensorList.get(status.index).sensorID}</td>
              <td>${sensorList.get(status.index).sensorName}</td>
              <td>${sensorList.get(status.index).lat},${sensorList.get(status.index).lon}</td>
              <td><input type="checkbox" value=""></td>
            </tr>
          </c:if>
        </c:forEach>
        </tbody>
      </table>
      <a type="button" class="btn btn-primary btn-sm pull-right" onclick="submitSelectSensor()">完成传感器选择</a>
    </div>
  </div>

</div>
</body>
</html>
