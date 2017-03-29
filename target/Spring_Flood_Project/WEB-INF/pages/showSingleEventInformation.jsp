<%--
  Created by IntelliJ IDEA.
  User: Yuan
  Date: 2017/2/26
  Time: 20:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/jquery-2.1.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/highchart-5.0.7/highcharts.js"></script>

    <script>

        //get sensor data information and event information

        //form the dataset of zone information for creating chart

        //refresh or not is a question(add information dynamic?)

    </script>
    <title></title>
</head>
<body>
<script>
    //model for test
//    Highcharts.chart('container', {
//        xAxis: {
//            type: 'datetime',
//            tickInterval: 10 * 24 * 36e5,
//            labels: {
//                format: '{value: %y-%m-%d %H:%M}',
//                align: 'right',
//                rotation: -30
//            }
//        },
//
//        series: [{
//            name:'eventTest',
//            type: 'areaspline',
//            data: [{x:1420070400000,y:29.9,className:'diagnosis'}, {x:1433116800000,y:71.5,className:'prepare'}, {x:1435708800000,y:110,className:'response'}, {x:1441065600000,y:129.2,className:'recovery'}],
//            zoneAxis: 'x',
//            zones: [{
//                value: 1433116800000,
//                color: '#90ed7d'
//            }, {
//                value: 1435708800000,
//                color: '#f15c80'
//            },{
//                value: 1441065600000,
//                color: '#90ed7d'
//            }],
//            tooltip: {
//                shared: true,
//                useHTML: true,
//                headerFormat: '<small>{point.key}</small><table>',
//                pointFormat: '<tr><td style="color: {series.color}">{series.name}: </td>' +
//                '<td style="text-align: right"><b>{point.y}</b></td><td style="text-align: right"><b>{point.className}</b></td></tr>',
//                footerFormat: '</table>',
//                valueDecimals: 2
//            }
//        }]
//
//    });
    $(function () {
        //get all series and cast json to object
        var series;
        $.ajax({
           type:"post",
            url:"/getEventData",
            async:false,
            success:function(data){
                series=eval("("+data+")");
            }
        });
        var chart={zoomType: 'x'};
        var title={text:'洪涝事件异常探测结果图'};
        var subtitle={text:'异常事件'};
        var xAxis={type: 'datetime',
            labels: {
                format: '{value: %y-%m-%d %H:%M}',
                align: 'right',
                rotation: -30
            }
        };

        var json={};
        json.chart=chart;
        json.title=title;
        json.subtitle=subtitle;
        json.xAxis=xAxis;
        json.series=series;
        $("#container").highcharts(json);
//        Highcharts.chart('container', {
//            title: {
//                text: 'Zones on X axis'
//            },
//            subtitle: {
//                text: 'Colors signify periods of increase and decrease'
//            },
//            xAxis: {
//                type: 'datetime'
//            },
//            series: [{
//                type: 'areaspline',
//                data:[1,2,34,5,3,54,5476,23,32],
//                    //data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4],
//                pointStart: Date.UTC(2015, 0),
//                pointIntervalUnit: 'month',
//                zoneAxis: 'x',
//                zones: [{
//                    value: Date.UTC(2015, 5),
//                    color: colors[2]
//                }, {
//                    value: Date.UTC(2015, 6),
//                    color: colors[5]
//                }, {
//                    value: Date.UTC(2015, 8),
//                    color: colors[2]
//                }, {
//                    color: colors[5]
//                }]
//            }]
//        });
    });
</script>
<div id="container" style="min-width:800px;height:400px">
</div>
<div class="alert">
    <img src="${pageContext.request.contextPath}/resources/image/7cb5ec.png" alt="无事件（NoEvent）"/><label
        class="label label-default">无事件（NoEvent）</label>&nbsp;&nbsp;
    <img src="${pageContext.request.contextPath}/resources/image/91e8e1.png" alt="诊断阶段（diagnosis）"/><label
        class="label label-default">诊断阶段（diagnosis）</label>&nbsp;&nbsp;
    <img src="${pageContext.request.contextPath}/resources/image/f7a35c.png" alt="准备阶段(prepare)"/><label
        class="label label-default">准备阶段(prepare)</label>&nbsp;&nbsp;
    <img src="${pageContext.request.contextPath}/resources/image/f15c80.png" alt="响应阶段(response)"/><label
        class="label label-default">响应阶段(response)</label>&nbsp;&nbsp;
    <img src="${pageContext.request.contextPath}/resources/image/90ed7d.png" alt="恢复阶段(response)"/><label
        class="label label-default">恢复阶段(response)</label>&nbsp;&nbsp;
    <!--<img src="img/device_abnormal_red_48.png" alt="设备异常，水压<3kg"/><label
        class="label label-default">设备异常，水压<3kg</label>&nbsp;
    <img src="img/device_abnormal_green_48.png" alt="设备异常，水压3-5kg"/><label
        class="label label-default">设备异常，水压3-5kg</label>&nbsp;
    <img src="img/device_abnormal_blue_48.png" alt="设备异常，水压>5kg"/><label
        class="label label-default">设备异常，水压>5kg</label>-->
   <!-- <img src="img/device_unpoll_black_64.png" alt="未巡检"/><label
        class="label label-default">未巡检</label>-->
</div>
      <p>诊断阶段</p>
      <section>当传感器<i>${eventParams.diagnosisSensor}</i>水位观测属性${eventParams.diagnosisObservation}的观测值在<fmt:formatNumber value=" ${eventParams.diagnosisHour+eventParams.diagnosisMinute/60.0+eventParams.diagnosisSecond/3600.0}" pattern="#.00" type="number" />小时内，
      ${eventParams.diagnosisRepeatTimes}次大于${eventParams.diagnosisThreshold}时，洪涝事件进入诊断阶段。</section>
      <p>准备阶段</p>
      <section>在诊断阶段发生的基础上，当传感器${eventParams.prepareSensor}水位观测属性${eventParams.prepareObservation}的观测值在<fmt:formatNumber value=" ${eventParams.prepareHour+eventParams.prepareMinute/60.0+eventParams.prepareSecond/3600.0}" pattern="#.00" type="number" />小时内，
        ${eventParams.prepareRepeatTimes}次大于${eventParams.prepareThreshold}，洪涝事件进入准备阶段。</section>
      <p>响应阶段</p>
      <section>在准备阶段发生的基础上，当传感器${eventParams.responseSensor}水位观测属性${eventParams.responseObservation}的观测值在<fmt:formatNumber value=" ${eventParams.responseHour+eventParams.recoveryMinute/60.0+eventParams.responseSecond/3600.0}" pattern="#.00" type="number" />小时内，
        ${eventParams.responseRepeatTimes}次大于${eventParams.responseThreshold}，洪涝事件进入响应阶段。</section>
      <p>恢复阶段</p>
      <section>在响应阶段发生的基础上，当传感器${eventParams.recoverySensor}水位观测属性${eventParams.recoveryObservation}的观测值在<fmt:formatNumber value=" ${eventParams.recoveryHour+eventParams.recoveryMinute/60.0+eventParams.recoverySecond/3600.0}" pattern="#.00" type="number" />小时内，
        ${eventParams.responseRepeatTimes}次大于${eventParams.responseThreshold}，洪涝事件进入恢复阶段。</section>

</body>
</html>
