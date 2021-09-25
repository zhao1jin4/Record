可使用C/C++ 来开发
https://developer.mozilla.org/en-US/docs/WebAssembly/C_to_wasm


============== C/C++  (Emscripten)开发Wasm  

--hello.c
#include <stdio.h>

int main() {
    printf("Hello World\n");
}


--linux下安装C++ 编译为wasm的工具 Emsdk = Emscripten   
git clone https://github.com/emscripten-core/emsdk.git
cd emsdk 就有emsdk命令 
./emsdk install latest 下载
./emsdk activate latest 激活版本
source ./emsdk_env.sh 环境变量 就有 emcc 命令 和 em++ 命令
#zypper install python3

---windows 下安装 Emsdk = Emscripten   
安装 Python 3.6 更高,win7上最高只可3.8版本(安装时就要)
win7上最高只可 node 12 版本

也是下载github项目,执行命令
emsdk.bat install latest (windows 7上用的是python3.9不行,windows 7上用的是node-14不行,但可后面替换)
#emsdk.bat install tot 
#emsdk.bat install sdk-1.38.20-64bit 也不行  

日志显示下载(path中有也下) Node,python,jre,
https://storage.googleapis.com/webassembly/emscripten-releases-builds/win/c2ac7520fad29a7937ed60ab6a95b08eb374c7ba/wasm-binaries.zip
emcc --version 2.0.18
emcc -v 显示使用clang 13
 
 
emsdk activate latest 报错，是python版本太高, 只能把下载解压目录\emsdk\python\3.9.2-1_64bit中python/vc 开头的几个文件用3.8版本替换

emsdk.bat activate latest

emsdk_env.bat

#emsdk_env.bat 就是把 当前目录emsdk 和 upstream\emscripten 目录(wasm-binaries.zip解压)放PATH环境变量中
#emcc/em++ 命令在 emsdk\upstream\emscripten\ 目录下

emcc hello.c 时提示 node 不支持windows 7  只能 emsdk\node\14.15.5_64bit\bin\node.exe 目录的文件用 12 版本替换


#--
如直接使用下载的wasm-binaries.zip解压放PATH环境变量, em++ hell.cpp提示
生成 D:\Application\wasm-binaries\install\emscripten\.emscripten 文件
 下面是猜出来的 
 LLVM_ROOT       = /usr/bin
 NODE_JS         = D:\Application\node-v12.16.1-win-x64\node.exe
 EMSCRIPTEN_ROOT = D:\Application\wasm-binaries\install\emscripten
修改
	LLVM_ROOT = 'D:\\Application\\wasm-binaries\\install\\bin'
	BINARYEN_ROOT = 'D:\\Application\\wasm-binaries\\install'
em++ hell.cpp 不行 ??? 长时间占用CPU也没有输出，不行的 
--

emcc hello.cpp  输出  a.out.js 和 a.out.wasm （二进制）
emcc hello.cpp -o hello.html 输出  hello.js 和 hello.wasm , hello.html 可以放web服务器上运行,有emscripten界面

emcc hello.c -s WASM=1 -o hello.html (-s WASM=1 表示输出wasm)


	
node a.out.js 可以执行，5000多行的代码
 
firefox打开 about:config 中 javascript.options.wasm  默认为true
chrome 中 chrome://flags/ 中 Experimental WebAssembly 默认是disabled
 
自定义模板  msdk\upstream\emscripten\src\shell_minimal.html 复制到自己的项目中
emcc -o hello.html hello.c -O3 -s WASM=1 --shell-file shell_minimal.html
 
 
优化选项
	-O1
	-O2
	-O3


输出js 
emcc -o hello.js hello.c -O3 -s WASM=1  #有hello.js(压缩的, "glue" 胶合,紧附于;胶水 ), hello.wasm
 
---hello3.c  JS调用C函数
#include <stdio.h>
#include <emscripten/emscripten.h>

int main() {
    printf("Hello World\n");
}

#ifdef __cplusplus
extern "C" {
#endif

EMSCRIPTEN_KEEPALIVE void myFunction(int argc, char ** argv) {
    printf("MyFunction Called\n");
}

#ifdef __cplusplus
}
#endif
--

emcc -o hello3.html hello3.c -O3 -s WASM=1 --shell-file html_template/shell_minimal.html -s NO_EXIT_RUNTIME=1  -s "EXTRA_EXPORTED_RUNTIME_METHODS=['ccall']"
提示 EXTRA_EXPORTED_RUNTIME_METHODS 过时, 用EXPORTED_RUNTIME_METHODS

emcc -o hello3.html hello3.c -O3 -s WASM=1 --shell-file html_template/shell_minimal.html -s NO_EXIT_RUNTIME=1  -s "EXPORTED_RUNTIME_METHODS=['ccall']"


-s所有选项
https://github.com/emscripten-core/emscripten/blob/main/src/settings.js
	MODULARIZE
	EXPORT_ES6
	
//文档上查不到 NO_EXIT_RUNTIME  选项  ,-O3 则在html文件内容是一行
emcc -o hello3.html hello3.c   -s WASM=1 --shell-file  shell_minimal.html   -s "EXPORTED_RUNTIME_METHODS=['ccall']"

输出的 hello3.html 增加 (其实可以自己写HTML, Module 定义在生成的js代码中,引入即可)
<button class="mybutton">Run myFunction</button>

document.querySelector('.mybutton')
    .addEventListener('click', function() {
        alert('check console');
        var result = Module.ccall(
            'myFunction',	// name of C function
            null,	// return type
            null,	// argument types
            null	// arguments
        );
    });
 
--------
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

操作文件
//---hello_world_file.cpp

#include <stdio.h>
int main()
{
    FILE *file = fopen("./hello_world_file.txt", "rb");
    if (!file)
    {
        printf("cannot open file\n");
        return 1;
    }
    while (!feof(file))
    {
        char c = fgetc(file);
        if (c != EOF)
        {
            putchar(c);
        }
    }
    fclose(file);
    return 0;
}

当前目录下有  hello_world_file.txt
//emcc hello_world_file.cpp -o hello_world_file.html --preload-file ./hello_world_file.txt  提示加 -s EXIT_RUNTIME=1
//emcc hello_world_file.cpp -o hello_world_file.html --preload-file ./hello_world_file.txt  -s EXIT_RUNTIME=1  执行后生成 hello_world_file.data 就是 hello_world_file.txt
 
 
 
emrun hello_world_file.html 会启动自已的服务，启动浏览器 ，方式 打开 http://localhost:6931/hello_world_file.html
emrun --browser chrome  --host 127.0.0.1 --port 9000  hello_world_file.html 





 


