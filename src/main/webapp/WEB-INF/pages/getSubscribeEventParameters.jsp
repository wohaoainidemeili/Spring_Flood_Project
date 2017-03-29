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
  <title>洪涝事件注册模块</title>

</head>
<body>
<script>
  var observationPropertyIDs =new Array();
  var units=new Array();
  function changeSensorSelection(obj,observationName){
    var sensorEle = obj;
    var sensorIndex = sensorEle.selectedIndex;
    var currentSensor = sensorEle.options[sensorIndex].text;
    var obsEle = document.getElementById(observationName);
    if (sensorEle.selectedIndex == 0) {
      obsEle.innerHTML = "<option value='-1'>请选择观测属性</option>";

      return;
    }
    if (observationPropertyIDs.length == 0) {
      $.ajax({
        type: "post",
        data: {"selectedSensor": currentSensor},
        url: "getSimpleObseervationProperty",
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
  function changeObservationSelection(obj,unitName){
    var obsEle=obj;
    var obsIndex=obsEle.selectedIndex;
    var unitLabel=document.getElementById(unitName);
    if(obsIndex==0){
      unitLabel.innerHTML="";
      return;
    }else{
      unitLabel.innerHTML=units[obsIndex-1];
    }
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
        var sensor_0=document.getElementById("sensorID");
        var observation_0=document.getElementById("observationProperty");
        var day_0=document.getElementById("day");
        var hour_0=document.getElementById("hour");
        var minute_0=document.getElementById("minute");
        var second_0=document.getElementById("second");
        var times_0=document.getElementById("times");
        var threshlod_0=document.getElementById("threshold");
        var unit_0=document.getElementById("unit");

        var sensorID_0= sensor_0.options[sensor_0.selectedIndex].text;
        var obsID_0=observation_0.options[observation_0.selectedIndex].text;
        var thresholdValue_0=threshlod_0.value;
        var unitValue_0=unit_0.value;
        var dayValue_0=day_0.value;
        var hourValue_0=hour_0.value;
        var minuteValue_0=minute_0.value;
        var secondValue_0=second_0.value;
        var timesValue_0=times_0.value;

        //prepare
        var sensor_1=document.getElementById("sensorID_1");
        var observation_1=document.getElementById("observationProperty_1");
        var threshlod_1=document.getElementById("threshold_1");
        var unit_1=document.getElementById("unit_1");
        var day_1=document.getElementById("day_1");
        var hour_1=document.getElementById("hour_1");
        var minute_1=document.getElementById("minute_1");
        var second_1=document.getElementById("second_1");
        var times_1=document.getElementById("times_1");

        var sensorID_1= sensor_1.options[sensor_1.selectedIndex].text;
        var obsID_1=observation_1.options[observation_1.selectedIndex].text;
        var thresholdValue_1=threshlod_1.value;
        var unitValue_1=unit_1.value;
        var dayValue_1=day_1.value;
        var hourValue_1=hour_1.value;
        var minuteValue_1=minute_1.value;
        var secondValue_1=second_1.value;
        var timesValue_1=times_1.value;
        //response
        var sensor_2=document.getElementById("sensorID_2");
        var observation_2=document.getElementById("observationProperty_2");
        var threshlod_2=document.getElementById("threshold_2");
        var unit_2=document.getElementById("unit_2");
        var day_2=document.getElementById("day_2");
        var hour_2=document.getElementById("hour_2");
        var minute_2=document.getElementById("minute_2");
        var second_2=document.getElementById("second_2");
        var times_2=document.getElementById("times_2");

        var sensorID_2= sensor_2.options[sensor_2.selectedIndex].text;
        var obsID_2=observation_2.options[observation_2.selectedIndex].text;
        var thresholdValue_2=threshlod_2.value;
        var unitValue_2=unit_2.value;
        var dayValue_2=day_2.value;
        var hourValue_2=hour_2.value;
        var minuteValue_2=minute_2.value;
        var secondValue_2=second_2.value;
        var timesValue_2=times_2.value;
        //recovery
        var sensor_3=document.getElementById("sensorID_3");
        var observation_3=document.getElementById("observationProperty_3");
        var threshlod_3=document.getElementById("threshold_3");
        var unit_3=document.getElementById("unit_3");
        var day_3=document.getElementById("day_3");
        var hour_3=document.getElementById("hour_3");
        var minute_3=document.getElementById("minute_3");
        var second_3=document.getElementById("second_3");
        var times_3=document.getElementById("times_3");

        var sensorID_3= sensor_3.options[sensor_3.selectedIndex].text;
        var obsID_3=observation_3.options[observation_3.selectedIndex].text;
        var thresholdValue_3=threshlod_3.value;
        var unitValue_3=unit_3.value;
        var dayValue_3=day_3.value;
        var hourValue_3=hour_3.value;
        var minuteValue_3=minute_3.value;
        var secondValue_3=second_3.value;
        var timesValue_3=times_3.value;
        //提交参数
        $.ajax({
          type:"post",
          url:"subscribe",
          data:{"diagnosisSensor":sensorID_0,"diagnosisObservation":obsID_0,"diagnosisThreshold":thresholdValue_0,"diagnosisDay":dayValue_0,"diagnosisHour":hourValue_0,"diagnosisMinute":minuteValue_0,"diagnosisSecond":secondValue_0,"diagnosisRepeatTimes":timesValue_0,"diagnosisUnit":unitValue_0,
            "prepareSensor":sensorID_1,"prepareObservation":obsID_1,"prepareThreshold":thresholdValue_1,"prepareDay":dayValue_1,"prepareHour":hourValue_1,"prepareMinute":minuteValue_1,"prepareSecond":secondValue_1,"prepareRepeatTimes":timesValue_1,"prepareUnit":unitValue_1,
            "responseSensor":sensorID_2,"responseObservation":obsID_2,"responseThreshold":thresholdValue_2,"responseDay":dayValue_2,"responseHour":hourValue_2,"responseMinute":minuteValue_2,"responseSecond":secondValue_2,"responseRepeatTimes":timesValue_2,"responseUnit":unitValue_2,
            "recoverySensor":sensorID_3,"recoveryObservation":obsID_3,"recoveryThreshold":thresholdValue_3,"recoveryDay":dayValue_3,"recoveryHour":hourValue_3,"recoveryMinute":minuteValue_3,"recoverySecond":secondValue_3,"recoveryRepeatTimes":timesValue_3,"recoveryUnit":unitValue_3
          },
          success:function(data){
            if(data==true){
              alter("成功注册！")
            }
          }
        })
      }
    }).validate({
      errorPlacement: function (error, element)
      {
        element.before(error);
      }
    });
    $("#day").spinner();
    $("#hour").spinner();
    $("#minute").spinner();
    $("#second").spinner();
    $("#times").spinner();

    $("#day_1").spinner();
    $("#hour_1").spinner();
    $("#minute_1").spinner();
    $("#second_1").spinner();
    $("#times_1").spinner();

    $("#day_2").spinner();
    $("#hour_2").spinner();
    $("#minute_2").spinner();
    $("#second_2").spinner();
    $("#times_2").spinner();

    $("#day_3").spinner();
    $("#hour_3").spinner();
    $("#minute_3").spinner();
    $("#second_3").spinner();
    $("#times_3").spinner();

    $("#day_4").spinner();
    $("#hour_4").spinner();
    $("#minute_4").spinner();
    $("#second_4").spinner();
    $("#times_4").spinner();
  });

</script>
<form id="EventForm" action="#">
  <h1>诊断阶段</h1>
  <fieldset>

    <legend>诊断阶段探测规则设置：当水位观测值在某一时间范围内小于(<)阈值次数大于规定阈值时，触发该事件阶段</legend>
    <div class="form-group col-md-12">
      <label for="sensorID">传感器ID *</label>
      <select class="form-control"id="sensorID" name="sensorID" placeholder="选择传感器ID" type="select" onchange=changeSensorSelection(this,"observationProperty")>
        <option value="-1">请选择传感器</option>
        <c:forEach items="${sensorsOption}" var="status">
          <option value="${status}">${status}</option>
        </c:forEach>
      </select>
      <label for="observationProperty">观测属性 *</label>
      <select class="form-control" id="observationProperty" name="observationProperty" placeholder="选择观测属性" type="select" onchange=changeObservationSelection(this,"unit")>
        <option value="-1">请选择观测属性</option>
      </select>
      <label for="threshold">阈值 *</label>
      <input id="threshold" name="threshold" type="text" class="form-control required">
      <label id="unit" for="threshold">单位</label>
      <div class="row col-lg-12 col-md-12 col-sm-12 col-xs-12">
        <label>时间窗口</label>
        <div class="form-inline center-block">
          <div class="input-group">
            <input type="text" min="0" max="1000" value="0" id="day">
            <span class="input-group-addon">天</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="23" value="0" id="hour">
            <span class="input-group-addon">时</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="59" value="0" id="minute">
            <span class="input-group-addon">分</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="59" value="0"  id="second">
            <span class="input-group-addon">秒</span>
          </div>
        </div>
      </div>
      <label>重复次数</label>
      <div class="input-group col-md-2">
        <input type="text" min="1" max="1000" value="2" id="times">
        <span class="input-group-addon">次数</span>
      </div>
    </div>
    </div>

    <p>(*) Mandatory</p>
  </fieldset>

  <h1>准备阶段</h1>
  <fieldset>
    <legend>准备阶段探测规则设置：基于诊断阶段，在一定时间范围内，传感器水位观测值大于某一阈值的现象出现次数超过规定值</legend>
    <div class="form-group col-md-12">
      <label for="sensorID_1">传感器ID *</label>
      <select class="form-control"id="sensorID_1" name="sensorID" placeholder="选择传感器ID" type="select" onchange=changeSensorSelection(this,"observationProperty_1")>
        <option value="-1">请选择传感器</option>
        <c:forEach items="${sensorsOption}" var="status">
          <option value="${status}">${status}</option>
        </c:forEach>
      </select>
      <label for="observationProperty_1">观测属性 *</label>
      <select class="form-control" id="observationProperty_1" name="observationProperty" placeholder="选择观测属性" type="select" onchange=changeObservationSelection(this,"unit_1")>
        <option value="-1">请选择观测属性</option>
      </select>
      <label for="threshold_1">阈值 *</label>
      <input id="threshold_1" name="threshold" type="text" class="form-control required">
      <label id="unit_1" for="threshold_1">单位</label>

      <div class="row col-lg-12 col-md-12 col-sm-12 col-xs-12">
        <label>时间窗口</label>
        <div class="form-inline center-block">
          <div class="input-group">
            <input type="text" min="0" max="1000" value="0" id="day_1">
            <span class="input-group-addon">天</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="23" value="0" id="hour_1">
            <span class="input-group-addon">时</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="59" value="0" id="minute_1">
            <span class="input-group-addon">分</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="59" value="0"  id="second_1">
            <span class="input-group-addon">秒</span>
          </div>
        </div>
      </div>
        <label>重复次数</label>
          <div class="input-group col-md-2">
            <input type="text" min="1" max="1000" value="2" id="times_1">
            <span class="input-group-addon">次数</span>
          </div>
    </div>
    <p>(*) Mandatory</p>
  </fieldset>

  <h1>响应阶段</h1>
  <fieldset>
    <legend>响应阶段探测规则设置：前两个阶段触发后，再满足在时间窗口范围内，水位观测大于(>)阈值出现次数超过规定的重复次数</legend>
    <div class="form-group col-md-12">
      <label for="sensorID_2">传感器ID *</label>
      <select class="form-control"id="sensorID_2" name="sensorID" placeholder="选择传感器ID" type="select" onchange=changeSensorSelection(this,"observationProperty_2")>
        <option value="-1">请选择传感器</option>
        <c:forEach items="${sensorsOption}" var="status">
          <option value="${status}">${status}</option>
        </c:forEach>
      </select>
      <label for="observationProperty_2">观测属性 *</label>
      <select class="form-control" id="observationProperty_2" name="observationProperty" placeholder="选择观测属性" type="select" onchange=changeObservationSelection(this,"unit_2")>
        <option value="-1">请选择观测属性</option>
      </select>
      <label for="threshold_2">阈值 *</label>
      <input id="threshold_2" name="threshold" type="text" class="form-control required">
      <label id="unit_2" for="threshold_1">单位</label>

      <div class="row col-lg-12 col-md-12 col-sm-12 col-xs-12">
        <label>时间窗口</label>
        <div class="form-inline center-block">
          <div class="input-group">
            <input type="text" min="0" max="1000" value="0" id="day_2">
            <span class="input-group-addon">天</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="23" value="0" id="hour_2">
            <span class="input-group-addon">时</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="59" value="0" id="minute_2">
            <span class="input-group-addon">分</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="59" value="0"  id="second_2">
            <span class="input-group-addon">秒</span>
          </div>
        </div>
      </div>
      <label>重复次数</label>
      <div class="input-group col-md-2">
        <input type="text" min="1" max="1000" value="2" id="times_2">
        <span class="input-group-addon">次数</span>
      </div>
    </div>
    <p>(*) Mandatory</p>
  </fieldset>

  <h1>恢复阶段</h1>
  <fieldset>
    <legend>恢复阶段探测规则设置：前三个阶段触发后，再满足在时间窗口范围内，水位观测大于(<)阈值出现次数超过规定的重复次数</legend>
    <div class="form-group col-md-12">
      <label for="sensorID_3">传感器ID *</label>
      <select class="form-control"id="sensorID_3" name="sensorID" placeholder="选择传感器ID" type="select" onchange=changeSensorSelection(this,"observationProperty_3")>
        <option value="-1">请选择传感器</option>
        <c:forEach items="${sensorsOption}" var="status">
          <option value="${status}">${status}</option>
        </c:forEach>
      </select>
      <label for="observationProperty_3">观测属性 *</label>
      <select class="form-control" id="observationProperty_3" name="observationProperty" placeholder="选择观测属性" type="select" onchange=changeObservationSelection(this,"unit_3")>
        <option value="-1">请选择观测属性</option>
      </select>
      <label for="threshold_3">阈值 *</label>
      <input id="threshold_3" name="threshold" type="text" class="form-control required">
      <label id="unit_3" for="threshold_1">单位</label>

      <div class="row col-lg-12 col-md-12 col-sm-12 col-xs-12">
        <label>时间窗口</label>
        <div class="form-inline center-block">
          <div class="input-group">
            <input type="text" min="0" max="1000" value="0" id="day_3">
            <span class="input-group-addon">天</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="23" value="0" id="hour_3">
            <span class="input-group-addon">时</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="59" value="0" id="minute_3">
            <span class="input-group-addon">分</span>
          </div>
          <div class="input-group">
            <input type="text" min="0" max="59" value="0"  id="second_3">
            <span class="input-group-addon">秒</span>
          </div>
        </div>
      </div>
      <label>重复次数</label>
      <div class="input-group col-md-2">
        <input type="text" min="1" max="1000" value="2" id="times_3">
        <span class="input-group-addon">次数</span>
      </div>
    </div>
    <p>(*) Mandatory</p>
  </fieldset>
</form>
</body>
</html>
