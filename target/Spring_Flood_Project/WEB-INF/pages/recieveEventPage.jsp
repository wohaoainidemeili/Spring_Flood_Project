<%--
  Created by IntelliJ IDEA.
  User: Yuan
  Date: 2017/2/26
  Time: 14:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <link rel="stylesheet" href="resources/jquery.steps-1.1.0/css/main.css">
  <link rel="stylesheet" href="resources/jquery.steps-1.1.0/css/normalize.css">
  <link rel="stylesheet" href="resources/jquery-ui-1.12.1.custom/jquery-ui.css">
  <link rel="stylesheet" href="resources/jquery.steps-1.1.0/css/jquery.steps.css">
  <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
  <script src="resources/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css"></script>
  <script src="resources/jquery-2.1.1/jquery.min.js"></script>
  <script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  <script src="resources/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
  <script src="resources/jquery-form-3.51/jquery.form.min.js"></script>
  <script src="resources/jquery.steps-1.1.0/modernizr-2.6.2.min.js"></script>
  <script src="resources/jquery.steps-1.1.0/jquery.cookie-1.3.1.js"></script>
  <script src="resources/jquery.steps-1.1.0/jquery.steps.js"></script>
  <script src="resources/jquery-2.1.1/jquery.validate.min.js"></script>
  <style>
    /*web background*/
    .container{
      display:table;
      height:100%;
    }

    .row{
      display: table-cell;
      vertical-align: middle;
    }
    /* centered columns styles */
    .row-centered {
      text-align:center;
    }
    .col-centered {
      display:inline-block;
      float:none;
      text-align:left;
      margin-right:-4px;
    }
  </style>
  <script>
    var isHaveStartLister=false;
    function startListern(){
      if(!isHaveStartLister){
        $.ajax({
          type:"post",
          url:"startWNSLisener"
        });
        alert("事件监听模块已开启！")
      }
    }
  </script>
    <title>洪涝事件接收模块</title>
</head>
<body>
<div class="container">
  <div class="row row-centered">
    <div class="well col-md-2 col-centered">
      <button id="eventListerButton" class="btn btn-primary row-centered" onclick="startListern()">开启监听</button>
    </div>
  </div>
</div>

</body>
</html>
