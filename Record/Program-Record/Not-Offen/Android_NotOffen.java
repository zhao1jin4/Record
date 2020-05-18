 
//------Google Map
android-sdk-windows\add-ons\addon-google_apis-google-16

https://developers.google.com/maps/documentation/android/hello-mapview?hl=zh-CN
 
用户主目录/.android/debug.keystore 是用于开发使用自动生成的
keytool -list -v -keystore  debug.keystore  -storepass  android  查看信息MD5的值,复制到
https://developers.google.com/android/maps-api-signup?hl=zh-CN 生成API key

<uses-permission android:name="android.permission.INTERNET"/>
<application 
   <uses-library android:name="com.google.android.maps" />


<com.google.android.maps.MapView
 	    android:layout_width="fill_parent" 
 	    android:layout_height="fill_parent"
 	    android:enabled="true"
 		android:clickable="true"
 		android:apiKey="@string/map_api_key"
 	    />
extends MapActivity

Overlay 抽象类,是透明的图层
ItemizedOverlay 是Overlay的子类,可以放很多的 OverlayItem 标记
MapView 有getOverlayes 把自己的加进去

MapView mapView = (MapView) findViewById(R.id.mapView);
mapView.setBuiltInZoomControls(true);//使用缩放工具
projection =mapView.getProjection();

List <Overlay> allOverlay=mapView.getOverlays();
allOverlay.add(new PointOverlay(begin));//把自己的加进去

MapController controller=mapView.getController();
controller.animateTo(begin);
controller.setZoom(12);


PointOverlay extends  Overlay
{
    public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
		projection.toPixels(geoPoint, point);//把GeoPoint纬经度 转换为屏幕坐标	
		//画已有图
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		Paint paint=new Paint();
		canvas.drawBitmap(bitmap , point.x , point.y , paint);
		
		//画线
		Paint paint=new Paint();
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(2);
		Path path=new Path();
		path.moveTo(begin.x,begin.y);
		path.lineTo(end.x,end.y);
		canvas.drawPath(path, paint);
	}
	
}



//------OAuth 
signpost-commonshttp4-1.2.1.2.jar
signpost-core-1.2.1.2.jar
http://www.oauth.net  基于HTTP协议
2.0还是Draft阶段

安全认证协议,防止用户输入的用户名密码,被开发者盗用

1.Service Provider 
2.User 用户
3.Consumer 是程序
4.Protected Resources
流程 User ->使用应用程序 Consumer->请求Service Provider取私人信息->问User是否可以让Consumer仿问你的资源

像是网上购买DELL电脑,DELL不能得到我的银行密码,

Service Provider要提供下列三种URL
1.Request Token URL
2.User Authorization URL
3.Acces Token URL

Signpost　实现，官方说明，在Android中使用时，要使用CommonsHttpOAuth* 代替　DefaultOAuth*

OAuthConsumer consumer;
OAuthProvider provider;
consumer= new CommonsHttpOAuthConsumer("key","secret");
provider= new CommonsHttpOAuthProvider("requestURL","accesURL","authorizeURL");

String url=provider.retrieveRequestToken(consumer, "myproto://good");
//把RequestToken写入consumer,返回accessToken的URL
//回调对应Android的Activity的URL，是在用户输入用户名，密码后发启动哪个Activity
//Signpost即会调　intent.setData(Uri.parse("myproto://good"));

String reqToken= consumer.getToken();//返回RequestToken
System.out.println(reqToken);
Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));//启动流览器
intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| //如果Activity已经在History stack 的顶部,那么不启动Activity
				Intent.FLAG_ACTIVITY_NO_HISTORY);//新的Activity不保存在History stack中
this.startActivity(intent);
			
<activity
	android:name=".MyActivity"
	android:label="@string/title_activity_main" 
	android:launchMode="singleTask"> <!-- 表示是单例类 ,不会调用onCreate方法,还是调用OnNewIntent方法--> 　
	<intent-filter>
		<action android:name="android.intent.action.VIEW" /> <!-- 对应new Intent(Intent.ACTION_VIEW,...) -->
		 <category android:name="android.intent.category.DEFAULT" />
		 <category android:name="android.intent.category.BROWSABLE" />
		 <data  android:scheme="myproto" android:host="good"/>
	</intent-filter>
</activity>
	
protected void onNewIntent(Intent intent) //对应launchMode="singleTask" 
{
	super.onNewIntent(intent);
	Uri uri=intent.getData();
	String oauth_verifier=uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
	provider.retrieveAccessToken(consumer, oauth_verifier);//发送Access请求
	String accessToken=consumer.getToken();
	String accessTokenSecret=consumer.getTokenSecret();
}

 