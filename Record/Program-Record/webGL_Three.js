

Mozilla Firefox的网页排版引擎Gecko在互联网中是最流行的排版引擎之一，
其流进程度仅次于Trident（用于Windows版的Internet Explorer4.0版本开始），
其后尚有WebKit（用于Safari以及Google Chrome）以及Presto（用于Opera）。
V8是一个由 Google开发的开源JavaScript引擎，用于Google Chrome中
V8在执行之前将JavaScript编译成了机器码，而非字节码或是直译它


===========webGL
https://developer.mozilla.org/en-US/docs/WebGL
https://developer.mozilla.org/en-US/docs/WebGL/Getting_started_with_WebGL 

chinese http://www.web3dmarket.com/html/tutorial/1.html <br/>
english http://learningwebgl.com/blog/?p=28   <br/>
code    https://github.com/gpjt/webgl-lessons <br/>

格式JS代码工具 ,也可用Eclipse  http://tool.chinaz.com/Tools/JsFormat.aspx  还可以普通压缩JS

对不支持webGL的机器,linux版的Firefox 地址栏输入 about:config 
设定“webgl.osmesalib”为您OSMesa分享库的地址（通常像这样/usr/lib/libOSMesa.so）
http://www.mesa3d.org/ 下载 ftp://ftp.freedesktop.org/pub/mesa/

var gl;
function init()
{
	try
	{
		var canvas = document.getElementById("lesson01-canvas");//在<body onload事件中可以得到
		gl = canvas.getContext("experimental-webgl");
		//gl = canvas.getContext("webgl");
		gl.viewportWidth = canvas.width;
		gl.viewportHeight = canvas.height;
	} catch (e)
	{
		alert("Could not initialise WebGL, sorry :-(");
	}
	gl.clearColor(0.0, 0.0, 255.0, 1.0);
	gl.clearDepth(1.0); // Clear everything
	gl.enable(gl.DEPTH_TEST); // Enable depth testing
	gl.depthFunc(gl.LEQUAL); // Near things obscure far things
}
<BODY onload="init()">
	<canvas id="lesson01-canvas" style="border: none;" width="500"
		height="500">
</BODY>


<script id="shader-fs" type="x-shader/x-fragment">
    void main(void) {        
    }
</script>

<script id="shader-vs" type="x-shader/x-vertex">
	void main(void) {
	}
</script>

//---建立Shader
var fragmentShaderSource =  document.getElementById("shader-fs").textContent;
var vertexShaderSource = document.getElementById("shader-vs").textContent;
var fragmentShader = gl.createShader(gl.FRAGMENT_SHADER);
var vertexShader = gl.createShader(gl.VERTEX_SHADER);
gl.shaderSource(vertexShader, vertexShaderSource);
gl.shaderSource(fragmentShader, vertexShaderSource);

gl.compileShader(vertexShader);
if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
	alert("An error occurred compiling the shaders: " + gl.getShaderInfoLog(shader));
}
gl.compileShader(fragmentShader);
if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
	alert("An error occurred compiling the shaders: " + gl.getShaderInfoLog(shader));
}
//--- 建立Program
var shaderProgram = gl.createProgram();
gl.attachShader(shaderProgram, vertexShader);
gl.attachShader(shaderProgram, fragmentShader);

gl.linkProgram(shaderProgram);
if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
	alert("Unable to initialize the shader program.");
}
gl.useProgram(shaderProgram);



===========Three.js
https://github.com/mrdoob/three.js
http://mrdoob.github.com/three.js/
	
Three.js 也是用"experimental-webgl" 

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">

<script type="text/javascript" src="js/Three.js"></script>
<script type="text/javascript" src="js/Stats.js"></script>

至少要一个 scene, 一个 camera, 一个 renderer
<body>
	<script type="text/javascript"> <!-- three.js的脚本放在<body>中 -->
	
		console.log("Three.js的版本是:"+THREE.REVISION);//Firefox,Chrome都支付,IE要运行提示的ActiveX
	
		var scene = new THREE.Scene();
		var camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 0.1, 1000 );
		//PerspectiveCamera(fov, aspect, near, far) fov=Field of View 视场 
		var renderer = new THREE.WebGLRenderer();
		renderer.setSize( window.innerWidth, window.innerHeight );//是分辨率,不是宽高
		document.body.appendChild( renderer.domElement );

		var geometry = new THREE.CubeGeometry(1,1,1);
		var material = new THREE.MeshBasicMaterial({color: 0x00ff00});
		var cube = new THREE.Mesh(geometry, material);//Mesh 把材质赋于物体
		scene.add(cube);//放原点

		camera.position.z = 5;
	
		var render = function () {
			requestAnimationFrame(render);	//动画记录开始,每秒50帧(次) ,当用户切换到其它网页时会暂停

			cube.rotation.x += 0.1;
			cube.rotation.y += 0.1;

			renderer.render(scene, camera);//动画记录结束,会不停的做记录的动画
		};

		render();
	</script>
</body>






	
	