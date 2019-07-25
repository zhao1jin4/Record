 
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
 