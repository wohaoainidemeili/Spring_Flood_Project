# Spring_Flood_Project
   using SOS1.0.0 、SES1.2.2 and SOS-SES-Feeder which developed by 52 north to detect flood .
   
   This project based on the three aforementioned services is delicated to provide users with a user-friendly event detecting system,
  which contains logining in, showing sensors, subscribing events, detecting events and showing detected events.
  
   The event detecting system divided a event into four part: diagnosis period, prepare period, response period and recovery period.
  Each period uses repeat times and observation thresholds to seperate itself from others. Perpare event happens when diagnosis 
  event happen before. Also the response is based on prepare, the recovery is based on response.
  
   If you want to use the system, you should have a good understanding of OGC SWE(SOS/SES).
   In addition, the Standards of SensorML, Observation and Measurement, EML are required.
   
   About the framework of the system, it is developed with spring MVC and hibernate to realize the backend of it. The user interface
  is realized with bootstrap, jquery and highcharts.
   Hope it will be useful for you!
