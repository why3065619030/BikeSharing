# BikeSharing
2022年物联网竞赛模拟题-安卓代码

1.bean/WeatherBean.java：用于做折线图   
2.notuse/index:对主界面的三个按键进行处理  
3.borrowBike.java：对借车的逻辑进行处理，包括间隔两秒向服务器请求一次bike数据(json)，向服务器发送控制信息用于控制车辆状态等； 
4.constant.java:服务器IP  
5.history.java:用于展示从借车开始后的速度变化（折线图）  
6.Welcome.java：登录页面  
7.xunjian.java：和板子进行直连（不经过服务器），通过IP+端口进行套接字连接  
