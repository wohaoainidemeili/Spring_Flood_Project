<%--
  Created by IntelliJ IDEA.
  User: Yuan
  Date: 2017/2/7
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
  <title></title>

</head>
<body>
<script>
  function deleteTr(obj){
    var tr=obj.parentNode.parentNode.parentNode;
    var tbody=tr.parentNode;
    tbody.removeChild(tr);
  }
  $(function(){
    $("#EventForm").steps({
      bodyTag: "fieldset",
      onStepChanging: function (event, currentIndex, newIndex)
      {
        // Always allow going backward even if the current step contains invalid fields!
        if (currentIndex > newIndex)
        {
          return true;
        }

        // Forbid suppressing "Warning" step if the user is to young
        if (newIndex === 3 && Number($("#age").val()) < 18)
        {
          return false;
        }

        var form = $(this);

        // Clean up if user went backward before
        if (currentIndex < newIndex)
        {
          // To remove error styles
          $(".body:eq(" + newIndex + ") label.error", form).remove();
          $(".body:eq(" + newIndex + ") .error", form).removeClass("error");
        }

        // Disable validation on fields that are disabled or hidden.
        form.validate().settings.ignore = ":disabled,:hidden";

        // Start validation; Prevent going forward if false
        return form.valid();
      },
      onStepChanged: function (event, currentIndex, priorIndex)
      {
        // Suppress (skip) "Warning" step if the user is old enough and wants to the previous step.
        if (currentIndex === 2 && priorIndex === 3)
        {
          $(this).steps("previous");
          return;
        }

        // Suppress (skip) "Warning" step if the user is old enough.
        if (currentIndex === 2 && Number($("#age").val()) >= 18)
        {
          $(this).steps("next");
        }
      },
      onFinishing: function (event, currentIndex)
      {
        var form = $(this);

        // Disable validation on fields that are disabled.
        // At this point it's recommended to do an overall check (mean ignoring only disabled fields)
        form.validate().settings.ignore = ":disabled";

        // Start validation; Prevent form submission if false
        return form.valid();
      },
      onFinished: function (event, currentIndex)
      {
        var form = $(this);

        // Submit form input
        form.submit();
      }
    }).validate({
      errorPlacement: function (error, element)
      {
        element.before(error);
      }
    });
    var observationPropertyIDs =new Array();
    var units=new Array();
    //传感器与观测属性二级联动
    $("#sensorID").change( function() {
              var sensorEle = $(this);
              var sensorIndex = sensorEle[0].selectedIndex;
              var currentSensor = sensorEle[0].options[sensorIndex].text;
              var obsEle = document.getElementById("observationProperty");
              if (sensorEle[0].selectedIndex == 0) {
                obsEle.innerHTML = "<option value='-1'>请选择观测属性</option>";

                return;
              }
              if (observationPropertyIDs.length == 0) {
                $.ajax({
                  type: "post",
                  data: {"selectedSensor": currentSensor},
                  url: "getObseervationProperty",
                  async: false,
                  success: function (data) {
                    observationPropertyIDs = data['properties'];
                    units = data['units'];
                    for (var i = 0; i < observationPropertyIDs.length; i++) {
                      var op = new Option(observationPropertyIDs[i], i);
                      obsEle.options.add(op);
                    }
                  }
                })
              }
              else
              {
                for (var i = 0; i < observationPropertyIDs.length; i++) {
                  var op = new Option(observationPropertyIDs[i], i);
                  obsEle.options.add(op);
                }
              }
            }
    );
    $("#observationProperty").change(function(){
      var obsEle=$(this)[0];
      var obsIndex=obsEle.selectedIndex;
      var unitLabel=document.getElementById("unit");
      if(obsIndex==0){
        unitLabel.innerHTML="";
        return;
      }else{
        unitLabel.innerHTML=units[obsIndex-1];
      }
    });
    $("#sensorID_2").change(function(){
      var sensorEle=$(this)[0];
      var sensorIndex=sensorEle.selectedIndex;
      var obsEle=document.getElementById("observationProperty_2");
      if(sensorIndex==0){
        obsEle.innerHTML = "<option value='-1'>请选择观测属性</option>";

        return;
      }
      if (observationPropertyIDs.length == 0) {
        $.ajax({
          type: "post",
          data: {"selectedSensor": currentSensor},
          url: "getObseervationProperty",
          async: false,
          success: function (data) {
            observationPropertyIDs = data['properties'];
            units = data['units'];
            for (var i = 0; i < observationPropertyIDs.length; i++) {
              var op = new Option(observationPropertyIDs[i], i);
              obsEle.options.add(op);
            }
          }
        })
      }else{
        for (var i = 0; i < observationPropertyIDs.length; i++) {
          var op = new Option(observationPropertyIDs[i], i);
          obsEle.options.add(op);
        }
      }

    });
    $("#observationProperty_2").change(function(){
      var obsEle=$(this)[0];
      var obsIndex=obsEle.selectedIndex;
      var unitLabel=document.getElementById("unit_2");
      if(obsIndex==0){
        unitLabel.innerHTML="";
        return;
      }else{
        unitLabel.innerHTML=units[obsIndex-1];
      }
    });
    $("#addFilter").click(function(){
      var tbody=document.getElementById("filterTable").getElementsByTagName("tbody")[0];
      var sensorEle=document.getElementById("sensorID_2");
      var obsEle=document.getElementById("observationProperty_2");
      var operaterEle=document.getElementById("operator");
      var unitEle=document.getElementById("unit_2");
      var thresholdEle=document.getElementById("threshold_2");
      if(sensorEle.selectedIndex==0||obsEle.selectedIndex==0){
        alert("必须选择传感器,传感器观测属性与操作符！");
      }else{
        var tr=document.createElement("tr");
        var sensorIdTd=document.createElement("td");
        sensorIdTd.innerHTML=sensorEle.options[sensorEle.selectedIndex].text;
        var obsIdTd=document.createElement("td");
        obsIdTd.innerHTML=obsEle.options[obsEle.selectedIndex].text;
        var operateTd=document.createElement("td");
        operateTd.innerHTML=operaterEle.options[operaterEle.selectedIndex].text;
        var thresholdTd=document.createElement("td");
        thresholdTd.innerHTML=thresholdEle.value;
        var unitTd=document.createElement("td");
        unitTd.innerHTML=unitEle.innerHTML;
        var lastTd=document.createElement("td");
        lastTd.innerHTML= "<a href='#'><i class='glyphicon glyphicon-trash' onclick='deleteTr(this)'>&nbsp;</i></a>";
        tr.appendChild(sensorIdTd);
        tr.appendChild(obsIdTd);
        tr.appendChild(operateTd);
        tr.appendChild(thresholdTd);
        tr.appendChild(unitTd);
        tr.appendChild(lastTd);
        tbody.appendChild(tr);
        $('#myModal').modal('hide');
      }
    });
    $("#day").spinner();
    $("#hour").spinner();
    $("#minute").spinner();
    $("#second").spinner();
    $("#times").spinner();
  });

</script>
<form id="EventForm" action="/getEventParameters" method="post">
  <h1>诊断阶段</h1>
  <fieldset>

    <legend>诊断阶段探测规则设置</legend>
    <div class="form-group col-md-8">
      <label for="sensorID">传感器ID *</label>
       <select class="form-control"id="sensorID" name="sensorID" placeholder="选择传感器ID" type="select" onchange="">
         <option value="-1">请选择传感器</option>
         <c:forEach items="${sensorsOption}" var="status">
           <option value="${status}">${status}</option>
         </c:forEach>
       </select>
      <label for="observationProperty">观测属性 *</label>
      <select class="form-control" id="observationProperty" name="observationProperty" placeholder="选择观测属性" type="select">
        <option value="-1">请选择观测属性</option>
      </select>
      <label for="threshold">阈值 *</label>
      <input id="threshold" name="threshold" type="text" class="required">
      <label id="unit" for="threshold">单位</label>
    </div>
    <p>(*) Mandatory</p>
  </fieldset>

  <h1>准备阶段</h1>
  <fieldset>
    <legend>准备阶段探测规则设置</legend>


    <div class="form-area col-lg-12 col-md-12 col-sm-12 col-xs-12 pre-scrollable">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Attendee Information</h3>
        </div>
        <div class="panel-body">
          <!--<input id="time" type="text" placeholder="在线客服接待上限数" class="form-control " onKeyUp="value=value.replace(/[^\d]/g,'')" />-->
          <section>
            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <h3>时间范围</h3>
            <div class="row col-lg-12 col-md-12 col-sm-12 col-xs-12">
              <div class="form-inline center-block">
            <div class="input-group col-md-2">
              <input type="number" min="0" max="1000" value="0" id="day">
              <span class="input-group-addon">天</span>
            </div>
            <div class="input-group col-md-2">
              <input type="number" min="0" max="23" value="0" id="hour">
              <span class="input-group-addon">时</span>
            </div>
            <div class="input-group col-md-2">
              <input type="number" min="0" max="59" value="0" id="minute">
              <span class="input-group-addon">分</span>
            </div>
            <div class="input-group col-md-2">
              <input type="number" min="0" max="59" value="0"  id="second">
              <span class="input-group-addon">秒</span>
            </div>
            </div>
            </div>
            </div>
          </section>
          <section>
            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <h3>重复次数</h3>
            <div class="row col-md-6">
              <div class="center-block">
            <div class="input-group col-md-4">
              <input type="number" min="1" max="1000" value="2" id="times">
              <span class="input-group-addon">次数</span>
            </div>
              </div>
            </div>
            </div>
          </section>
          <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <h3>观测筛选条件（下列条件为并/and关系）</h3>
          <table class="table table-striped table-border table-hover" id="filterTable">
            <thead>
            <tr>
              <th>传感器ID</th>
              <th>观测属性ID</th>
              <th>比较符</th>
              <th>阈值</th>
              <th>单位</th>
              <th>&nbsp;</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
          <a type="button" class="btn btn-primary btn-sm pull-right" data-toggle="modal" data-target="#myModal">添加过滤条件</a>
          </div>
        </div>
      </div>
    </div>
  </fieldset>

  <h1>响应阶段</h1>
  <fieldset>
    <legend>响应阶段探测规则设置</legend>

    <p>Please go away ;-)</p>
  </fieldset>

  <h1>恢复阶段</h1>
  <fieldset>
    <legend>Terms and Conditions</legend>

    <input id="acceptTerms" name="acceptTerms" type="checkbox" class="required"> <label for="acceptTerms">I agree with the Terms and Conditions.</label>
  </fieldset>
</form>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">观测属性规则</h4>
      </div>
      <div class="modal-body">
       <div class="form-group">
         <label for="sensorID_2">传感器ID *</label>
         <select class="form-control" id="sensorID_2" type="select">
           <option value="-1">请选择传感器</option>
           <c:forEach items="${sensorsOption}" var="status">
             <option value="${status}">${status}</option>
           </c:forEach>
         </select>
         <label for="observationProperty_2">观测属性ID *</label>
         <select class="form-control" id="observationProperty_2" type="select">
           <option value="-1">请选择观测属性</option>
         </select>
         <label for="operator">比较符号*</label>
         <select class="form-control" id="operator">
           <option>请选择比较符</option>
           <option>&lt;</option>
           <option>&gt;</option>
           <option>&le;</option>
           <option>&ge;</option>
         </select>
         <label for="threshold_2">阈值</label>
         <input class="form-control required  " id="threshold_2" type="text">
         <label for="threshold_2" id="unit_2">单位</label>
       </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" id="addFilter">添加</button>
      </div>
    </div>
  </div>
</div>

</body>
</html>
