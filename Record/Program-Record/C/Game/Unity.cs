Unity 2018.2  游戏开发,界面工具 Personal 版免费(像Maya),启动要 webchat 登录 和 绑定邮箱  完成license
https://unity3d.com/cn
类似的还有  Unreal Engine(虚幻引擎) Visual Studio 2017上有支持

可做2D,开发使用C#,Personal版本只有C#

安装时可选Visual Studio Community 2017 和 openGL build 去下载，都先不选,只选unity和documentation
视频教程  http://www.51zxw.net/list.aspx?cid=454 
doc 安装位置 D:\Program\Unity\Editor\Data\Documentation\en\Manual\index.html (开始 菜单有的)
			 D:\Program\Unity\Editor\Unity.exe


Inspector是属性面板
Hierachy面板 有一个create->有很多对象，如2d,3d对象，灯光，摄像机，右击->rename 可改名
gameobject 菜单也可以，建立点光，range属性大，就变亮
选中对象，在Scence面板中按f键，视图中显示选中的对象，有2d按钮切换 2d,3d 视图
中键平移视图(alt+左键拖动，右键拖动)，滚轮放大缩小视图(alt+右键拖动)
3d视图 中 旋转视图 （ alt+左键拖动 ）
运行按钮,切换Game视图查看效果，再点退出


选中物体->在Inspector属性面板 Add Component->Physics->RigidBody (rigid  刚硬的) 钢体有重力

Project属性面板 ->create按钮->Material 材质，拖动材质球到Hierachy面板中的物体 ，就赋材质给了物体
Project属性面板 ->create-> folder,把图片从外部拖进来，材质球的Inspector属性单选Normal map选择图片，颜色有影响可选择白色
修改图片拖动到Normal map的缩略图上

Project属性面板 ->create-> C# script (Personal版本只有这个),官方文档上说支持UnityScript基于JavaScript  
 Assets菜单 > Create > C# Script  (Personal版本只有这个)
 
Start()函数，只初始调用一次
Update()函数 每运一帧被调用一次

Edit->Project Setting->input 在Horizontal组有 左右键和a,d键  水平移动物体

脚本写好后拖动到物体上，看Inspector属性面板有脚本







