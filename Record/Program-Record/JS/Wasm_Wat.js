WebAssembly  Wasm 在浏览中运行二进制指令的形式（chrome,firefox调试时会被返编译成汇编指令），比JavaScript 快,应用于视频解码，3D游戏 ，可用来替代解释执行的WebGL (Unity 使用C#开发，可编译为web assembly)

WebAssembly 1.0 已经在4大浏览器中有了Chrome, Edge, Firefox, and WebKit (Opera)
https://webassembly.org/roadmap/ 


https://developer.mozilla.org/en-US/docs/WebAssembly 

浏览器console中 WebAssembly.很多方法 
https://developer.mozilla.org/en-US/docs/WebAssembly/Using_the_JavaScript_API


可使用 Rust 来开发
https://developer.mozilla.org/en-US/docs/WebAssembly/Rust_to_wasm

可使用C/C++ 来开发
https://developer.mozilla.org/en-US/docs/WebAssembly/C_to_wasm

 
 
使用Web Assembly 扩展Istio中的Envoy     ####配置有一个 EnvoyFilter 
Web Assembly  的开发语言只能使用单线程，是一个沙盒中执行的 （Envoy带有wasm的运行时，是一个Filter）


istio 1.9    istio agent 拦截  xDS 下载wasm （envoy xDS 协议 ）

----vscode 扩展 WebAssembly 
作者为 WebAssembly Foundation  
会高亮wat文件
右击wasm文件->show  WebAssembly

==============WebAssembly text format 
一般扩展名为 .wat 

https://developer.mozilla.org/en-US/docs/WebAssembly/Text_format_to_wasm#a_first_look_at_the_text_format

https://github.com/webassembly/wabt  在Release中有 wabt-1.0.23-windows.tar.gz  wabt-1.0.23-ubuntu.tar.gz
	linux下用
		$ mkdir build
		$ cd build
		$ cmake ..  	  # 报 -lpthreads 找不到（多了一个s吗）
		$ cmake --build .
	
	windows下安装MinGW或 Visual Studio  2015+ ,要cmake , https://cmake.org/download/ 下载windows zip包,bin目录放PATH环境变量中 
		> mkdir build
		> cd build
		> cmake .. -DCMAKE_BUILD_TYPE=DEBUG -DCMAKE_INSTALL_PREFIX=..\ -G "Visual Studio 16 2019" 
		#cmake --help 看当前用加*的Visual Studio字串或MinGW字串, 安装了Microsoft C++ build tools for Visual Studio 2019, 还是报找不到 'pthread.h' 错误,打开Developer Command Prompt for VS209运行，还是一样的
		#cmake .. -DCMAKE_BUILD_TYPE=DEBUG -DCMAKE_INSTALL_PREFIX=..\ -G "MinGW Makefiles" 看日志像是生成Check代码有bug
		> cmake --build . --config DEBUG --target install


	
	
wat2wasm: 转换 wat(文本) 到 wasm(二进制) 的工具

wat2wasm simple.wat -o simple.wasm

wasm2wat simple.wasm -o text.wat  反向转换，生成很多默认的东西

wat2wasm simple.wat -v  看二进制和 注释指令


Firefox Developer tools可以看wasm的反编译源码


https://developer.mozilla.org/en-US/docs/WebAssembly/Understanding_the_text_format

基本单元是 module 即一个S表达式，树。
节点 一对(),每个()中可有属性，概念像xml

--first.wat 最简单的
(module)

wat2wasm first.wat -v 显示为
0000000: 0061 736d              ; WASM_BINARY_MAGIC    对应"\0asm"字符串 String.fromCharCode(parseInt("0x61",16))
0000004: 0100 0000              ; WASM_BINARY_VERSION
格式参考 https://webassembly.org/docs/binary-encoding/#high-level-structure


//只这4种数据类型
i32: 32-bit integer
i64: 64-bit integer
f32: 32-bit float
f64: 64-bit float

格式 ( func <signature> <locals> <body> )
示例 (func (param i32) (param i32) (result f64) ... )  //param 表示参数,result表示返回,如没有result表示没有返回


(func (param i32) (param f32) (local f64)
  local.get 0		//表示第一个参数 ,即i32类型
  local.get 1
  local.get 2)	//取出 f64 类型

(func (param $p1 i32) (param $p2 f32) (local $loc f64) …)  //就可以用 local.get $p1 代替local.get 0 ，二进制中不保存$p1名字



Stack machines 的示例
(func (param $p i32)
  (result i32)
  local.get $p  //入栈
  local.get $p //入栈
  i32.add) //加法取(pop 删式的取)栈里的两个数，结果相加再入栈，做为返回结果 ，类型要和result一致

可运行的示例 .wat文件中 ;; 是注释
---add.wat
(module
  (func $add (param $lhs i32) (param $rhs i32) (result i32)  ;;为函数取名为$add
    local.get $lhs
    local.get $rhs
    i32.add)
  (export "add" (func $add)) ;; 要导出才可以用, add  是JS中使用的
)

//wat2wasm add.wat -o add.wasm

 
WebAssembly.instantiateStreaming(fetch('add.wasm'))
  .then(obj => {
    console.log(obj.instance.exports.add(1, 2));  // "3"
  });

 
/*
 或者web.xml增加配置
<mime-mapping>
	<extension>wasm</extension>
	<mime-type>application/wasm</mime-type>
</mime-mapping>
*/
//JS中使用fetch返回的.wasm文件。服务器返回头要加 Content-Type 值为 application/wasm , Content-Length和文件内容
@WebFilter(filterName = "wasmFilter", urlPatterns = { "*.wasm"})
public class WASMFilter implements Filter{
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		resp.setContentType("application/wasm");
		HttpServletRequest request=(HttpServletRequest)req;
		String filePath=request.getServletContext().getRealPath("/")+request.getServletPath();
		File file=new File(filePath);
		long len=file.length();
		resp.setContentLengthLong(len);
		FileInputStream input=new FileInputStream(file);
		
		byte[] buf=new byte[1024];
		int readed=0;
		while((readed=input.read(buf))!=-1) {
			resp.getOutputStream().write(buf,0,readed);
		}
		System.out.println(request.getRequestURI()+",len="+len);
		input.close();//FileInputStream实现AutoCloseable接口的自动关闭
	}
} 
	

--call.wat 示例
(module
  (func $getAnswer (result i32)
    i32.const 42) ;;返回固定42, i32.const 定义一个32位整数，入栈
  (func (export "getAnswerPlus1") (result i32) ;;导出的简略写法
    call $getAnswer ;;调用另一个函数，结果入入栈
    i32.const 1
    i32.add))

 WebAssembly.instantiateStreaming(fetch('call.wasm'))
	 .then(obj => {
	    console.log(obj.instance.exports.getAnswerPlus1());  // "43"
	  });
	  
--导入 ,可是从JS导入，也可是wasm中
--logger.wat
(module
  (import "console" "log" (func $log (param i32)))  ;; 从 console 模块中导入 log ，做为一个函数名为$log,参数为32位整数
  (func (export "logIt")
    i32.const 13
    call $log))


//JS对象 对应wasm console模块log函数
var importObject = {
  console: {
    log: function(arg) {
      console.log(arg);
    }
  }
};

WebAssembly.instantiateStreaming(fetch('logger.wasm'), importObject)
  .then(obj => {
    obj.instance.exports.logIt();
  });
 
 
--全局变量 可被JS和WASM 仿问
----global.wat
(module
   (global $g (import "js" "global") (mut i32))  ;; 第一个global 全局，后面是名字$g，mut可变的  
   (func (export "getGlobal") (result i32)
        (global.get $g))
   (func (export "incGlobal")
        (global.set $g
            (i32.add (global.get $g) (i32.const 1))))
)

//JS
const global = new WebAssembly.Global({value: "i32", mutable: true}, 0);//类型和mut应该要匹配,最后的0是变量的值
WebAssembly.instantiateStreaming(fetch('global.wasm'), { js: { global } })
  .then(({instance}) => {
       console.log(instance.exports.getGlobal() == 0);
        global.value = 42;
        console.log( instance.exports.getGlobal() == 42);
        instance.exports.incGlobal();
        console.log(global.value == 43);
    });

----
字符串传递要用内存 或者 后面的 引用类型 
Wasm 内存是线性的，i32.load,i32.store 对应JS就是ArrayBuffer

WebAssembly.Memory() 有一个属性 buffer  来仿问整个线性内存
 Memory.grow()  可扩容
(import "js" "mem" (memory 1)) //1表示最少1页内存，1页=64KB

---- logger2.wat
(module
  (import "console" "log" (func $log (param i32 i32))) ;; 两个参数
  (import "js" "mem" (memory 1))  ;; 导入做为内存,1表示最少1页内存，1页=64KB
  (data (i32.const 0) "Hi")  ;; data 写入内存 ，从0位置开始
  (func (export "writeHi")
    i32.const 0  ;; pass offset 0 to log
    i32.const 2  ;; pass length 2 to log
    call $log))


//JS 中创建内存，也可在wasm中创建
    var memory = new WebAssembly.Memory({ initial : 1 });//初始页大小，可以指定 maximum 

      function consoleLogString(offset, length) {
        var bytes = new Uint8Array(memory.buffer, offset, length);//属性 buffer  来仿问整个线性内存
        var string = new TextDecoder('utf8').decode(bytes);//JS API TextDecoder
        console.log(string);
      }

      var importObject = {
        console: {
          log: consoleLogString
        },
        js: {
          mem: memory
        }
      };

      WebAssembly.instantiateStreaming(fetch('logger2.wasm'), importObject)
      .then(obj => {
        obj.instance.exports.writeHi();
      });

---tables 解决 动态调用函数


(module
  (table 2 funcref)  ;; 表格有2个元素，类型为函数指针，funcref 可是任何类型(安全原因除了Memory)
  (elem (i32.const 0) $f1 $f2) ;; elem为表格内容,可重复，0是表格的偏移量，表格的下标
  (func $f1 (result i32) ;; 函数指针可放在elem里面，也可是外面
    i32.const 42)
  (func $f2 (result i32)
    i32.const 13)
 
)

//JS中创建Table
function() {
  // table section
  var tbl = new WebAssembly.Table({initial:2, element:"funcref"});//表格有2个元素，类型为函数指针

  // function sections:
  var f1 = ... /* some imported WebAssembly function */
  var f2 = ... /* some imported WebAssembly function */

  // elem section
  tbl.set(0, f1);//0下标
  tbl.set(1, f2);
};


(type $return_i32 (func (result i32))) ;; if this was f32, type checking would fail 像类型定义
(func (export "callByIndex") (param $i i32) (result i32)
  local.get $i ;; 参数入栈
  call_indirect (type $return_i32)) ;; call_indirect 隐式的出栈，表格中的第$i(下标必须是i32类型)个函数，定义的type返回类型要匹配
 
  也可显示指定call_indirect参数 (call_indirect (type $return_i32) (local.get $i))
  如一个模块中只可有一个table ,call_indirect 隐式使用这个table,如有多个表，就要指定，如call_indirect $my_spicy_table (type $i32_to_void)

---wasm-table.wat
(module
  (table 2 funcref)  ;; 表格有2个元素，类型为函数指针，anyfunc  可是任何类型(安全原因除了linear  Memory)
  (func $f1 (result i32)
    i32.const 42)
  (func $f2 (result i32)
    i32.const 13)
  (elem (i32.const 0) $f1 $f2) ;; elem为表格内容,可重复，0是表格的偏移量，表格的下标
  (type $return_i32 (func (result i32))) ;;像类型定义
  (func (export "callByIndex") (param $i i32) (result i32)
    local.get $i ;; 参数入栈
    call_indirect (type $return_i32)) ;; call_indirect 隐式的出栈，表格中的第$i(下标必须是i32类型)个函数调用，定义的type返回类型要匹配
 ;;  也可显示指定call_indirect参数 (call_indirect (type $return_i32) (local.get $i))
 ;;  如一个模块中只可有一个table ,call_indirect 隐式使用这个table,如有多个表，就要指定，如call_indirect $my_spicy_table (type $i32_to_void)
)

-----表格的修改和动态链接
JS   中修改 grow(), get() ,	 set()
WASM 中修改 table.get , table.set

像dll

wat跨文件
---shared0.wat  
(module
  (import "js" "memory" (memory 1))
  (import "js" "table" (table 1 funcref))
  (elem (i32.const 0) $shared0func)
  (func $shared0func (result i32)
   i32.const 0
   i32.load)   ;; load隐式出栈，返回值为 内存(memory) 中取第0(出栈)个下标
)


----shared1.wat 
(module
  (import "js" "memory" (memory 1))
  (import "js" "table" (table 1 funcref))
  (type $void_to_i32 (func (result i32)))
  (func (export "doIt") (result i32)
   i32.const 0
   i32.const 42
   i32.store  ;; 导入的内存(memory)的0下标保存为42,隐式出栈两次
   ;;也可显示 (i32.store (i32.const 0) (i32.const 42))
   i32.const 0
   call_indirect (type $void_to_i32)) ;; 表格 中的第0(隐式出栈)下标，即上一个文件shared0.wat中的 shared0func,再调用
   ;;也可显示 (call_indirect (type $void_to_i32) (i32.const 0))
   ;;结果为42
)

---shared-address-space.html
var importObj = {
  js: {
    memory : new WebAssembly.Memory({ initial: 1 }),
    table : new WebAssembly.Table({ initial: 1, element: "funcref" })//JS中创建Table
  }
};
 
Promise.all([
  WebAssembly.instantiateStreaming(fetch('shared0.wasm'), importObj),//多个wasm文件使用相同的对象(memory,table)
  WebAssembly.instantiateStreaming(fetch('shared1.wasm'), importObj)
]).then(function(results) {
  console.log(results[1].instance.exports.doIt());  // prints 42
  //results数组下标,对应前面的文件数组下标
});

---内存批量操作
https://github.com/WebAssembly/bulk-memory-operations/blob/master/proposals/bulk-memory-operations/Overview.md
WebAssembly  的本地方法   memcpy 和  memmove

data.drop: Discard the data in an data segment.
elem.drop: Discard the data in an element segment.
memory.copy: Copy from one region of linear memory to another.
memory.fill: Fill a region of linear memory with a given byte value.
memory.init: Copy a region from a data segment.
table.copy: Copy from one region of a table to another.
table.init: Copy a region from an element segment.


---引用类型
https://github.com/WebAssembly/reference-types/blob/master/proposals/reference-types/Overview.md

externref 类型 ，wasm 模块 只能接受 和传递 ， 不能操纵，可用于表格的元素

https://rustwasm.github.io/docs/wasm-bindgen/  有关于rustr操作 externref 的文档


---多值
pave the way for  为…铺平道路
(module
  (func $get_two_numbers (result i32 i32)
    i32.const 1
    i32.const 2
  )
  (func (export "add_to_numbers") (result i32)
    call $get_two_numbers  //返回结果也入栈
    i32.add
  )
)

 
https://hacks.mozilla.org/2019/11/multi-value-all-the-wasm/  使用Rust操作多值

----线程
--1.共享内存
Memory对象 允许共享 在多个 WebAssembly  运行在不同的worker ，像JS中的 SharedArrayBuffer

和JS中的 SharedArrayBuffer 一样 使用 postMessage()方法 ，在worker和window间传输
let memory = new WebAssembly.Memory({initial:10, maximum:100, shared:true}); //shared表示共享,默认为false
memory.buffer // 返回一个 SharedArrayBuffer

在 wasm 中使用 (memory 1 2 shared) ,必须指定最大

https://github.com/WebAssembly/threads/blob/master/proposals/threads/Overview.md

--2. 内存访问的原子性
https://github.com/WebAssembly/threads/blob/master/proposals/threads/Overview.md#atomic-memory-accesses

mutexes, condition variables,
https://emscripten.org/docs/porting/pthreads.html


------WebAssembly.compile
https://developer.mozilla.org/en-US/docs/WebAssembly/Using_the_JavaScript_API

;; 从一个叫 imports的module中导入一个函数叫 imported_func , 导出一个函数叫 exported_func
 ;; 导出表示为web中WebAssembly使用,运行时传递42这个参数
(module
  (func $i (import "imports" "imported_func") (param i32))
  (func (export "exported_func")
    i32.const 42
    call $i))
 
;; wat2wasm simple.wat  生成 simple.wasm 

//JS compile 方式
 /*
    //Ajax
   var importObject = { imports: { imported_func: arg => console.log(arg) } };
   var request = new XMLHttpRequest();
    request.open('GET', 'simple.wasm');
    request.responseType = 'arraybuffer';
    request.send();

    request.onload = function() {
      var bytes = request.response;
      WebAssembly.instantiate(bytes, importObject).then(results => {
        results.instance.exports.exported_func();
      });
    };
     //-- 三选一
      var importObject = { imports: { imported_func: arg => console.log(arg) } };
      fetch('simple.wasm')
      .then(response => response.arrayBuffer())
      .then(bytes => WebAssembly.compile(bytes))
      .then(module => WebAssembly.instantiate(module, importObject))
      .then(instance => instance.exports.exported_func());
      */
      //-- 三选一， 这种简单不需要arrayBuffer
     WebAssembly.compileStreaming(fetch('simple.wasm'))
     .then(module => WebAssembly.instantiate(module, importObject))
     .then(instance => instance.exports.exported_func());
    
 
 
//JS instantiate 方式
  var importObject = { imports: { imported_func: arg => 
      	console.log(arg) 
      } };
      WebAssembly.instantiateStreaming( fetch('simple.wasm') , importObject)
      		.then(  obj => obj.instance.exports.exported_func() );
      



--- table.wat
(module
  (func $thirteen (result i32) (i32.const 13))
  (func $fourtytwo (result i32) (i32.const 42))
  (table (export "tbl") anyfunc (elem $thirteen $fourtytwo)) ;; 可被导出，类型为anyfuc
)


  var table = new WebAssembly.Table({ element: "anyfunc", initial: 1, maximum: 10 });//anyfunc
      table.grow(1);//扩大一个
      console.log(table.length);//2
      WebAssembly.instantiateStreaming(fetch('table.wasm'))
      .then(function(obj) {
        var tbl = obj.instance.exports.tbl;
        console.log(tbl.get(0)());  // 13
        //.get(0) JS来得到
        console.log(tbl.get(1)());  // 42
      });


