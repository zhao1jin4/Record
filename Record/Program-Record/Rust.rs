语言 最初是 Mozilla 公司开发的 
第一个有版本号的 Rust 编译器于2012 年 1 月发布
Rust 1.0 是第一个稳定版本，于 2015年5月15日发布。

可替代C/C++  ,跨平台比c++好，比如Socket功能(GTK 4 支持用Rust开发)
多处理器支持好，并发好
没有拉圾回收
擅长 WebAssembly ，Firefox浏览器

microsoft 有支持 
https://docs.microsoft.com/zh-cn/windows/dev-environment/rust/overview
https://foundation.rust-lang.org/ 基金会里除了microsoft还有goole,aws,facebook
https://github.com/microsoft/windows-rs
https://doc.rust-lang.org/std/index.html
https://doc.rust-lang.org/std/all.html

https://github.com/Rust-for-Linux/linux  (google支持的) 不是要将所有 Linux 内核中的 C 代码替换成 Rust

网上发文称 AOSP (Android Open Source Project) 现已支持使用 Rust 开发 Android 操作系统。应该不是开发App
https://source.android.google.cn/

 
--rust linux 就可以不用安装gcc
下载提示  x86_64-unknown-linux-gnu  #可下载 rust-1.50.0-x86_64-unknown-linux-gnu.tar.gz
提示执行 source $HOME/.cargo/env    #有$HOME/.cargo/bin 目录 放PATH环境变量中
 
 linux下rust-up安装后还依赖于cc  , zypper install gcc-c++
rust-gdb --version  依赖于gdb  , zypper install  gdb


--rust windows 依赖VC++  ，也可使用rustup toolchain修改为gnu就不依赖VC++
stup-init.exe 提示Rust requires the Microsoft C++ build tools for Visual Studio 2013 or later 

https://visualstudio.microsoft.com/visual-cpp-build-tools/ 下载 vs_buildtools_xxx.exe 
	选 使用C++桌面开发-> 可选里 只选中MSVCv142 - VS 2019 C++  和windows 10 SDK(后面会要的)  大小为6.86GB 
	默认下载在 C:\ProgramData\Microsoft\VisualStudio\Packages (实际上是安装的解压目录,一个包下载在哪 ??? )
	默认安装在 C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools 安装后大小为2.36GB , 有VC#目录大小只为3M 
	下拉选择download all then install
	安装中提示 Dot Net版本为4.7.2,安装 263 个包 (可在windows 7中安装) 
	
	报找不到 link 命令 设置 PATH 有 C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Tools\MSVC\14.28.29910\bin\Hostx64\x64 
	报找不到 advapi32.lib 要下载windows 10 SDK,可以安装在 windows 7 SP1 上
		默认目录在	C:\Program Files (x86)\Microsoft SDKs\Windows Kits\10 	只有9MB
					C:\Program Files (x86)\Windows Kits\10\					大小1.46GB
						Lib\10.0.19041.0\ucrt
						Lib\10.0.19041.0\ucrt_enclave
						Lib\10.0.19041.0\um 
						
						ucrt=Universal C Runtime
						um=非托管
						enclave被包围的领土
						
	测试成功
	
	Microsoft C++ 生成工具 ,命令行界面（例如，持续集成工作流中）生成面向 Windows 的 C++ 库和应用程序， 无需使用 Visual Studio(有Commnity版本)
	或者安装Visual Studio 2013 及以上版本时选择C++ Tools
	 
	也可修改默认的工具链 stable-msvc 到  stable-gnu
		rustup toolchain install stable-gnu  下载 stable-x86_64-pc-windows-gnu
		rustup default stable-gnu
		
		rustup default 查看默认
		rustup toolchain list 查看
		rustup show
		rustup toolchain uninstall stable-x86_64-pc-windows-msvc

https://forge.rust-lang.org/infra/other-installation-methods.html 中下载 rust-1.51.0-x86_64-pc-windows-msvc.msi  ,里也有rust-up的下载地址(window gnu)
虽然有rustc cargo 命令，但运行时还是依赖VC++ 工具包
还有 x86_64-pc-windows-gnu 版本


使用rustup-init.exe安装 默认 %USERPROFILE%\.cargo 可用 CARGO_HOME 来修改 
有bin目录，下有多的rustup,rustc,cargo命令, 有registry目录，放下载的依赖

Rust编译问题Blocking waiting for file lock on package cache , 
删除后~\.cargo\registry\index\* ,报连接不上 https://github.com/rust-lang/crates.io-index , 是因没有配置 .cargo\config.toml
cargo build会显示配置的地址
 
%USERPROFILE%/.rustup/settings.toml 中有配置默认为 stable-x86_64-pc-windows-gnu
%USERPROFILE%/.rustup/toolchains 下东西很多,如果下载过两个版本,有2.5GB大小,是 rustup toolchain 命令生成的


---vscode 扩展 rust-analyzer 替代 rust
 
https://rust-analyzer.github.io/manual.html
要求有rust标准库的源码才行，安装方法 rustup component add rust-src  (如离线安装包安装并设置default toolchaint 会报 xxx  is a custom toolchain 错误，rust-init默认安装就正常)
源码安装在 %USERPROFILE%\.rustup\toolchains\stable-x86_64-pc-windows-msvc\lib\rustlib\src\rust\library\std\src

rustup component  list

#可选的  rust-analyzer ，eclipse rust插件Corrosion要的
%USERPROFILE%\.local\bin\rust-analyzer 可能是 vscode 扩展 rust-analyzer 生成的



 只是写代码有提示，点方法跟踪进入(也只是对有Cargo.toml文件 的项目),Find All References
 
 
 codeLLDB扩展的介绍有提到rust,如项目中Cargo.toml文件会自动生成很多配置，可以成功debug 

对于测试，方法上直接Debug的灰字，点击就可Debug 

当项目中有Cargo.toml时,Debug按钮提示建立文件,生成的launch.json
	{ 
		"configurations": [ 
		{
            "type": "lldb",
            "request": "launch",
            "name": "Debug executable 'myproject'",
            "cargo": {
                "args": [
                    "build",
                    "--bin=myproject",
                    "--package=myproject"
                ],
                "filter": {
                    "name": "myproject",
                    "kind": "bin"
                }
            },
            "args": [],
            "cwd": "${workspaceFolder}"
        }
	]
	} 
---rustc 方式 可运行，测试debug也成功  launch.json 为
{ 
	"version": "0.2.0",
	"configurations": [
		{
			"type": "lldb",
			"request": "launch",
			"name": "Debug",
			"program": "${workspaceFolder}/target/${fileBasenameNoExtension}.exe",
			"args": [],
			"cwd": "${workspaceFolder}",
			"preLaunchTask": "rustc_build"
		}
	]
}
---vc的cppvsdbg (launch.json )在win10下可以debug,win7下不能debug,但可以运行
{ 
    "version": "0.2.0",
    "configurations": [
        {
            "name": "rustc run  current file",
            "type": "cppvsdbg",
            "request": "launch",
            "program": "${workspaceFolder}/target/${fileBasenameNoExtension}.exe",
            "args": [],
            "stopAtEntry": false,
            "cwd": "${workspaceFolder}",
            "environment": [],
            "preLaunchTask": "rustc_build", 
            "externalConsole": false
            
        }
    ]
}
----tasks.json
{
    "tasks": [
        {
            "type": "shell",
            "label": "rustc_build",
            "command": "rustc",
            "args": [
                "${workspaceFolder}/src/${fileBasename}",
                "--out-dir",
                "target",
                "-o",
                "${workspaceFolder}/target/${fileBasenameNoExtension}.exe",
				"-g"
            ],
            "options": {
                "cwd": "${workspaceFolder}",
            }
        }
    ],
    "version": "2.0.0"
}


/* 变量备份 ${file} 是全路径文件名 
 "args": [
		"-g",
		"${file}",
		"-o",
		"${fileDirname}\\${fileBasenameNoExtension}.exe"
*/
---vscode 扩展 Better TOML  
vscode 默认没有Cargo.toml没有语法高亮
 (在扩展窗口的输入文本框中输入 ext:toml 显示所有打开toml插件)
	带语法高亮，也有写时的自动提示
TOML = Tom's Obvious, Minimal Language 


//vscode 不能从标准输入 ,只能用命令行才行
use std::io;
fn main() { 
    println!("Please input your guess."); 
    let mut guess = String::new(); 
    io::stdin().read_line(&mut guess)
        .expect("Failed to read line"); 
    println!("You guessed: {}", guess);

}
RUST panic 时 vscode debug 控制台中文乱码???  ,rustc命令行运行正常




---------eclipse rust 插件 Corrosion 腐蚀 (eclipse marketplace)
有单独版本 
https://download.eclipse.org/corrosion/releases/latest/products/  里面有CDT
https://eclipse.github.io/corrosion/
要先设置文件编码为UTF-8

依赖于rustup,rust-gbd命令
Settings->rust->Language Server:中有下载 rust analyzer 也可手工下载 https://github.com/rust-analyzer/rust-analyzer/releases
	gunzip rust-analyzer-x86_64-unknown-linux-gnu.gz 再重命名到 ~/.local/bin/rust-analyzer
	官方命令为
	$ curl -L https://github.com/rust-analyzer/rust-analyzer/releases/latest/download/rust-analyzer-x86_64-unknown-linux-gnu.gz | gunzip -c - > ~/.local/bin/rust-analyzer
	$ chmod +x ~/.local/bin/rust-analyzer

官方提供的截图也是 linux下使用，在windows下debug没试成功???
windows下不能运行rust-gdb --version (%CARGO_HOME%\bin下有rust-gdb) ,不兼容 stable-x86_64-pc-windows-msvc  
安装 rust-1.58.1-x86_64-pc-windows-gnu.msi 默认位置 C:\Program Files\Rust stable GNU 1.58 修改默认位置，如 D:\App\
rust gnu 1.62 ,安装包241M,安装后1.09G

rustup toolchain link rust-gnu D:\App\Rust-GNU-1.58 建立后 %USERPROFILE%/.rustup/toolchains 目录下有一个名为rust-gnu的快捷方式
rustup default rust-gnu
rustup toolchain list -v 显示指向的路径
rustup run rust-gnu rustc 做测试

GitBash 中用 ./rust-gdb --version 可以，但怎么让 eclipse的Corrosion使用GitBash??


linux　版本　eclipse rust Corrosion 报不能找swt相关.so(xfce4桌面，如gnome就不会报), eclipse是基于gtk ，要安装gtk,测试安装gtk3-devel就可以
	rustup只能使用rustup-init安装包，离线安装包 rust-1.50.0-x86_64-unknown-linux-gnu.tar.gz 中有install.sh安装在/usr/local/bin，有rustc,cargo等命令，但没有rustup命令
	里面可以建立C/C++项目 
	file->open  project  from File System
	

	debug configuration特别的慢！！！，还有问题？？(左则目录默认是在Rust级下，应该为cargo xx project级下，但没有？？？,只有cargo xx test)，
		解决方法用run Configuration,再点debug下拉按钮选择Run建立的配置,但不能传递启动参数
	
rustc --print sysroot


rustup update stable 在线升级

----------Intellij Idea 插件 Rust   开源的

Intellij  Community 可安装，也可用于Android Studio 
 
	
https://plugins.jetbrains.com/plugin/8182-rust/
0.3.144.3766-211 			 2021.1   		Mar 29, 2021 
	又依赖于toml
	https://plugins.jetbrains.com/plugin/8195-toml
 

可以建立rust项目，运行项目只认有 cargo.toml 的项目 ！！！  

Settings->Language& Frameworks -> Rust->
	Standard Library :默认检测到 %USERPROFILE%\.rustup\toolchains\stable-x86_64-pc-windows-msvc\lib\rustlib\src\rust
	ToolChain location :默认检测到 %CARGO_HOME%\bin
	  
打断点必须是收费工具Intellij Idea的 Ulimated版本, GoLand,CLion,PyCharm Pro才行


---main.rs
fn main(){
 println!("hello");
   let x = 2.0; // f64
   let y: f32 = 3.0; // f32
   let y1: f32 = 3.0; // f32
}
--- 
编译用 rustc main.rs 
rustc src/hello.rs --out-dir target  -o target/hello.exe -g
 
 
构建工具 cargo
建立项目  cargo new myproject 会生成 Cargo.toml文件(vscode扩展 Better TOML) 依赖管理 ,格式像npm
代码的包/库 叫 crate
构建用 cargo build  会生成 Cargo.lock 文件，像npm，生成在target/debug 目录中
构建和运行 cargo run (使用也像npm) 也会下载依赖
cargo install xxx 安装某个包

cargo check 比 cargo build快，检查代码编译是否正确 

cargo build --release 生成在target/release目录中
cargo --list  显示所有命令
/// 而不是两斜杆以支持 Markdown 注解来格式化文本,运行 cargo doc --open ,注释里的测试代码cargo test可以被执行
//! 而不是位于注释之后的项增加文档。这通常用于 crate 根文件（通常是 src/lib.rs）,应放在文件首部

--- Cargo.toml
[package]
name = "myproject"
version = "0.1.0"
authors = ["Your Name <you@example.com>"]
edition = "2018"

# See more keys and their definitions at https://doc.rust-lang.org/cargo/reference/manifest.html

[profile.dev]
opt-level = 0

[profile.release]
opt-level = 3

[dependencies]
rand = "0.8.3"
--- 

cargo build  会下载依赖,默认下载源码到 $HOME/.cargo/registry/ 目录中, 
	子级目录 src目录里是源码,cache中是x-1.0.0.crate文件 ,index目录是git仓库
	
----(像maven的子项目)
Cargo.toml 所在目录还可继续创建子项目 ,顶层的Cargo.toml 增加如下配置 (像maven的module)
[workspace] 
members = [
    "third_gtk4",
    "third_wasm",
]

如在third_gtk4/src/lib.rs 有如下
pub fn add_one(x: i32) -> i32 {
    x + 1
}

其中了一个子项目third_wasm引用另一个子项目third_gtk4
[dependencies]
third_gtk4 = { path = "../third_gtk4" }

在third_wasm中就可以使用 third_gtk4::add_one

顶级  cargo run -p third_wasm   # -p, --package

----


https://crates.io 是默认中央仓库


https://doc.rust-lang.org/cargo/reference/config.html
每个项目的config.toml配置优先级高于全局的配置
/projects/foo/.cargo/config.toml
/projects/.cargo/config.toml
/.cargo/config.toml
$CARGO_HOME/config.toml which defaults to:

    Windows: %USERPROFILE%\.cargo\config.toml
    Unix: $HOME/.cargo/config.toml


下载默认crates.io修改为国内镜像
https://doc.rust-lang.org/cargo/reference/source-replacement.html
增加配置文件 .cargo/config.toml (见上)
[source]
 
[source.my-vendor-source] 
#只可指定一种源，常用是registry
#directory = "vendor"  #目录是相对.cargo/config.toml的位置
registry = "git://mirrors.ustc.edu.cn/crates.io-index"
#local-registry = "path/to/registry"


# 如指定git那私 branch/tag/rev 是可选的
#git = "https://example.com/path/to/repo"
# branch = "master"
# tag = "v1.0.1"
# rev = "313f44e8"


[source.crates-io]
replace-with = "my-vendor-source"
#当请求默认的crates.io时是使用上面的my-vendor-source配置
 


#https://doc.rust-lang.org/cargo/reference/registries.html
上传使用的私有仓库,增加配置文件 .cargo/config.toml (见上)
[registries]
my-registry = { index = "https://my-intranet:8080/git/index" }
#也可以用 CARGO_REGISTRIES_MY_REGISTRY_INDEX=https://my-intranet:8080/git/index

在其它Cargo.toml文件中引用这个名字
[package]
name = "my-project"
version = "0.1.0"

publish = ["my-registry"] #表示只能发布这个私有仓库上

[dependencies]
other-crate = { version = "1.0", registry = "my-registry" }
gtk = { version = "0.4.6", package = "gtk4" } //https://crates.io/crates/gtk4 显示最新版本  ,win7 msys2 运行未成功？？

使用命令
cargo login --registry=my-registry  
	#可以保存密码在$HOME/.cargo/credentials.toml文件中，有一个token的键
	#[registries.my-registry]
	#token =

cargo publish --registry=my-registry

cargo tree 显示依赖树

cargo run 会下载依赖，默认到 %USERPROFILE%\.cargo\registry\cache\mirrors.ustc.edu.cn-xxxx 目录下

 

C++23没有反射
Rust只能对 'static 生命周期的变量（常量）进行反射！ 只能被用作类型推断 (Any)


教程
https://kaisery.github.io/trpl-zh-cn/ch02-00-guessing-game-tutorial.html

use std::io;
fn main() { 
    println!("Please input your guess."); 
    let mut guess = String::new(); 
    io::stdin().read_line(&mut guess)
        .expect("Failed to read line"); 
    println!("You guessed: {}", guess);

}
//vscode 不能从标准输入 ,只能用命令行才行
//Settings->Run Code configuration -> 复选 Run In Terminal 组下的唯一的一个Wheter to run code in Integerated Terminal -> ctrl+s保存 测试不行？？？



//---01
const THREE_HOURS_IN_SECONDS: u32 = 60 * 60 ; //常量只能被设置为常量表达式,值不可有变量
println!("{}",THREE_HOURS_IN_SECONDS);  

let x = 5; 
let x = x + 1;//两次let一个变量，是被覆盖的 
{
 let x = x * 2;//只是局部覆盖
 println!("The value of x in the inner scope is: {}", x);
}

println!("The value of x is: {}", x);//6

/*
i8	u8
i16	u16
i32	u32
i64	u64
i128	u128
isize	usize
*/
    let c0 = 'z';
    println!("{}",c0);
   let c:u8 =b'A'; //b 二进制节字符,只能u8,u=unsign
   let c1:u16 =0b1111_0000;  //二进制
   let c2:i32 = 98_222;//十进制
   let c3:i64 =  0o77;//八进制
   let c4:i128 = 0xff;//十六进制
   let c5:isize=64;//64 位架构上它们是 64 位的， 32 位架构上它们是 32 位的

   let x = 2.0; // 默认是 f64 
   let y: f32 = 3.0; // f32 
   let f: bool = false;  
   let tup: (i32, f64, u8) = (500, 6.4, 1);//元组,约束类型

   let tup = (500, 6.4, 1); 
   let (x, y, z) = tup;
   println!("{},{},{}",tup.0,tup.1,tup.2);  //0开始像数组

   let a = [1, 2, 3, 4, 5];
   let a: [i32; 5] = [1, 2, 3, 4, 5];//i32 是每个元素的类型。分号之后，数字 5 表明该数组包含五个元素。
   let a = [3; 5];//包含 5 个元素，这些元素的值最初都将被设置为 3，同let a = [3, 3, 3, 3, 3];
    let val=a[0];

    let y = {
        let x = 3;
        x + 1  //没有分号，最后一行是返回值
    };
    plus_one(5);


    let  number = 6; 
    if number % 4 == 0 {
        println!("number is divisible by 4");
    } else if number % 3 == 0 {
        println!("number is divisible by 3");
    } else if number % 2 == 0 {
        println!("number is divisible by 2");
    } else {
        println!("number is not divisible by 4, 3, or 2");
    }



    let condition = true;
    let mut number = if condition {// 在 let 语句中使用 if ，mut可变的
        5
    } else {
        6
    };

    //loop 无限循环，可用break;
    let mut count = 0; 
    'counting_up: loop { //标签
        println!("count = {}", count);
        let mut remaining = 10;

        loop {
            println!("remaining = {}", remaining);
            if remaining == 9 {
                break;
            }
            if count == 2 {
                break 'counting_up;
            }
            remaining -= 1;
        }

        count += 1;
    }

    let mut counter = 0; 
    let result = loop { 
        counter += 1;

        if counter == 10 {
            break counter * 2;//loop 带返回值 
        }
    };

    while number != 0 {
        println!("{}!", number); 
        number = number - 1;
    }

    let a = [10, 20, 30, 40, 50];
    let mut index = 0; 
    while index < 5 {
        println!("the value is: {}", a[index]);

        index = index + 1;
    }


    let a = [10, 20, 30, 40, 50]; 
    for element in a.iter() { //数组的iter(), for in 像JS
        println!("the value is: {}", element);
    }
    for number in (1..4).rev() { //..范围 1-3， 反向
        println!("{}!", number);
    }

}
 
fn plus_one(x: i32) -> i32 { //返回用 ->
    x + 1   //不加;号
  //return   x + 1 ;  
}

//--02 owner
fn main() {
    let mut s = String::from("hello"); 
    s.push_str(", world!"); // push_str() 在字符串后追加字面值 
    println!("{}", s);  

    let s1 = String::from("hello");
    let s2 = s1;
    //println!("{}, world!", s1);//不可再使用s1,一个堆区只能被最后的使用
     
    let s3 = s2.clone();//复制
    println!("{}, world!", s3);
 

    let s = String::from("hello");  
    takes_ownership(s); // s 的值移动到函数里 
    //println!("error {} ", s);  //所以到这里s不再有效

    let x = 5;   
    makes_copy(x);// 但 i32 是 Copy 的，所以在后面可继续使用 x 


    let s1 = String::from("hello"); 
    let (s2, len) = calculate_length(s1); 
    println!("The length of '{}' is {}.", s2, len);
}
fn takes_ownership(some_string: String) { 
    println!("{}", some_string);
} // 这里，some_string 移出作用域并调用 `drop` 方法。占用的内存被释放

fn makes_copy(some_integer: i32) {  
    println!("{}", some_integer);
}  
 
fn calculate_length(s: String) -> (String, usize) {
    let length = s.len(); // len() 返回字符串的长度 
    (s, length)
}

//---03 borrow
fn main() {
    let s1 = String::from("hello");

    let len = calculate_length(&s1);//引用，表示里面只可以读，可以读多次，(像数据库的读写锁，写锁只可有一个，读锁可多个)

    println!("The length of '{}' is {}.", s1, len);//这还继续使用s1 


    let mut s = String::from("hello"); 
    change(&mut s);//可变的引用，里面就可以变

    
    //let r1 = &mut s;//作用域内只能有一个可变引用
    //println!("{}, {}", r1, s);//s只可被可变借用一次，错误
   

    let mut s = String::from("hello"); 
    let r1 = &s; // 没问题 （读锁可多个）
    let r2 = &s; // 没问题 （读锁可多个）
    //let r3 = &mut s; // 大问题
    println!("{}, {}, and {}", r1, r2, r3);
    //不能在拥有不可变引用的同时拥有可变引用（有读锁了，也不能有写锁）
 
} 

fn calculate_length(s: &String) -> usize {//这里不能修改值
     println!("s= {}", s);
   // s.push_str(", world!");//error 
    s.len()
}


fn change(some_string: &mut String) {
    some_string.push_str(", world");
}
/*
fn dangle() -> &String {
    let s = String::from("hello");

    &s//s会被回收的，不能返回 ， dangle悬荡
}*/
fn no_dangle() -> String {
    let s = String::from("hello"); 
    s
}
//要么 只能有一个可变引用，要么 只能有多个不可变引用。

//---04 slice
fn main(){
    let mut s = String::from("hello world");

    let word = first_word(&s); // word 的值为 5 
    s.clear(); // 这清空了字符串，使其等于 "" 
    // word 在此处的值仍然是 5，

 
    let s = String::from("hello world"); 
    let hello = &s[0..5];//或者用&s[..5];
    let world = &s[6..11];//或者用&s[6..];
    let slice = &s[..];
    
    let a = [1, 2, 3, 4, 5]; 
    let slice = &a[1..3];
    //这个 slice 的类型是 &[i32]


    let s = "Hello, world!"; //这里 s 的类型是 &str
    //指向二进制程序特定位置的 slice。这也就是为什么字符串字面值是不可变的；&str 是一个不可变引用。


    let mut s = String::from("hello world"); 
    let word = first_word2(&s); 
    //s.clear(); // 错误!  原因为上面的first_word2(&s);的函数声明是不可变的(没有mut)，这里不能再为可变的
    println!("the first word is: {}", word);


 

    let my_string_literal = "hello world";

    // first_word 中传入字符串字面值的 slice
    let word = first_word2(&my_string_literal[..]);//切片类型为 str 不是String,函数参数类型为 &str
    println!("1the first word is: {}", word); 
    // 这样写也可以，即不使用 slice 语法！
    let word = first_word2(my_string_literal);
    println!("2the first word is: {}", word);

}


fn first_word(s: &String) -> usize {
    let bytes = s.as_bytes();

    for (i, &item) in bytes.iter().enumerate() {
        if item == b' ' {
            return i;
        }
    }

    s.len()
}
//fn first_word2(s: &String) -> &str {
fn first_word2(s: &str) -> &str {
    let bytes = s.as_bytes();

    for (i, &item) in bytes.iter().enumerate() {
        if item == b' ' {
            return &s[0..i];
        }
    }

    &s[..]
}

//----05  struct
fn main() {
     
    let user1 = User {
        email: String::from("someone@example.com"),
        username: String::from("someusername123"),
        active: true,
        sign_in_count: 1,
    };
  
    //元组结构体 
    struct Point(i32, i32, i32); 
    let origin = Point(10,20,30);
    println!(".0={}",origin.0);


    let rect1 = Rectangle { width: 30, height: 50 };

    println!("rect1 is {:?}", rect1); // {:?}是一行的，
    println!("rect1 is {:#?}", rect1); // {:#?}是多行的

    println!(
        "The area of the rectangle is {} square pixels.",
        rect1.area()
    );
    let sq = Rectangle::square(3);//不以self 作为参数
}

//增加注解来派生 Debug trait
#[derive(Debug)]  //可以被打印
struct Rectangle {
    width: u32,
    height: u32,
}
impl Rectangle { //结构体的方法impl
    fn area(&self) -> u32 {  //&self 也可为 &mut self,无参方法
        self.width * self.height
    }
    fn can_hold(&self, other: &Rectangle) -> bool { //有一个参数，要在&self后
        self.width > other.width && self.height > other.height
    }
}
impl Rectangle { //可以有多个impl
    //不以self 作为参数被称为 关联函数,如String::from 
    fn square(size: u32) -> Rectangle {
        Rectangle { width: size, height: size }
    } 
}

struct User {
    username: String,
    email: String,
    sign_in_count: u64,
    active: bool,
} 
fn build_user(email: String, username: String) -> User {
    User {
        email: email,
        username: username,
        active: true,
        sign_in_count: 1,
    }
}
//-----06  enum , match, if let
 
fn main() {
    let four = IpAddrKind::V4; //两个冒号
    
    let home = IpAddr::V4(127, 0, 0, 1); 
    let loopback = IpAddr::V6(String::from("::1"));

    let some_number = Some(5);//不需要 Option:: 
    let some_string = Some("a string");   

    let x=value_in_cents(Coin::Quarter(UsState::Alabama));
    println!("{}",x);

    let some_u8_value = 0u8;
    match some_u8_value { //像scala
        1 => println!("one"),
        3 => println!("three"),
        5 => println!("five"),
        7 => println!("seven"),
       // _ => (), //其它通配
       _ => println!("others"),
    }

    //---
    let some_u8_value = Some(0u8);

    match some_u8_value {
        Some(3) => println!("three"),
        _ => println!("others1"),
    }
    //效果同上 if let 只能用于enum,是match的简写
    if let Some(3) = some_u8_value {
        println!("three");
    }else {
        println!("others2");
    }


    let coin = &Coin::Quarter(UsState::Alabama); 
    let mut count = 0;
    match coin {
        Coin::Quarter(state) => println!("State quarter from {:?}!", state),
        _ => count += 1,
    }
    if let Coin::Quarter(state) = coin {//if let 效果同上，state可以接受值
        println!("State quarter from {:?}!", state);
    } else {
        count += 1;
    }

}
 
enum IpAddrKind {
    V4,
    V6,
}

 
enum IpAddr {
	//枚举替代结构体还有另一个优势：每个成员可以处理不同类型和数量的数据
    V4(u8, u8, u8, u8),
    V6(String),//关联了 String 值
}

enum Message {
    Quit,
    Move { x: i32, y: i32 }, //一个匿名结构体
    Write(String),
    ChangeColor(i32, i32, i32),
}



enum Option<T> { //同Java,C++
    Some(T),
    None,
}
 

#[derive(Debug)]
enum UsState {
    Alabama,
    Alaska 
}

enum Coin {
    Penny,
    Nickel,
    Dime,
    Quarter(UsState), //某一项，可以是另一个enum
}
fn value_in_cents(coin: Coin) -> u8 {
    match coin { //match
        Coin::Penny => {
            println!("Lucky penny!");
            1
        },
        Coin::Nickel => 5,
        Coin::Dime => 10,
       // Coin::Quarter => 25,
       Coin::Quarter(state) => { //state是接受到的enum值
            println!("State quarter from {:?}!", state);
            25
        },
    }
}

 ///----07  lib.rs  caret 
cargo new --lib restaurant


fn main(){
    use crate::front_of_house::hosting; //引入，默认是私有的，可使用 pub use
    use std::io::Result as IoResult; //别名
    use std::{cmp::Ordering, io as myio}; //多引用 
    use std::io::{self, Write}; //self表示引用std::io
    use std::collections::*; //所有 公有项引入作用域

    hosting::add_to_waitlist();
}
 
mod front_of_house {
    pub mod hosting {
    // Rust 中默认所有项（函数、方法、结构体、枚举、模块和常量）都是私有的。
        pub fn add_to_waitlist() {} //pub 关键字来创建公共项
    }
} 

pub fn eat_at_restaurant() {
    //绝对路径（absolute path）从 crate 根开始
    crate::front_of_house::hosting::add_to_waitlist();

    // Relative path
    front_of_house::hosting::add_to_waitlist();
}

fn serve_order() {}

mod back_of_house {
    fn fix_incorrect_order() {
        cook_order();
        super::serve_order(); //父模块中的
    }

    fn cook_order() {}
}

//---main.rs
use myproject::Config; //对应于Cargo.toml 中 [package] 下的 name = "myproject"
  
//--lib.rs

#[derive(Debug)]  //可以被打印
pub  struct Config { 
  pub query: String,
  pub filename: String,
  pub case_sensitive: bool,
}
 

//----08 collection.rs

use std::collections::HashMap;

fn main(){


    let v: Vec<i32> = Vec::new();
    let mut v = vec![1, 2, 3];//vec! 宏 
    v.push(5);//必须是 mut 
    v.push(6);

    let third: &i32 = &v[2]; //如超范围造成 panic
    println!("The third element is {}", third);
    
    match v.get(2) { //返回一个 Option<&T>,如超范围返回None
        Some(third) => println!("The third element is {}", third), //自带Some,None
        None => println!("There is no third element."),
    }
  
    //v.push(6); //前面有 &v[2]，不能修改
    //println!("The first element is: {}", third);

    for i in &v { //遍历
        println!("{}", i);
    }
    //遍历改变
    let mut v = vec![100, 32, 57];
    for i in &mut v {
        *i += 50; //解引用运算符（*）获取 i 中的值
    }
    //-------hashmap
    //所有的键必须是相同类型，值也必须都是相同类型。
    let mut scores = HashMap::new();

    scores.insert(String::from("Blue"), 10);
    scores.insert(String::from("Yellow"), 50);
    
 
    let teams  = vec![String::from("Blue"), String::from("Yellow")];
    let initial_scores = vec![10, 50];

    //<_, _> 类型注解是必要的，因为可能 collect 为很多不同的数据结构
    let scores: HashMap<_, _> = teams.iter().zip(initial_scores.iter()).collect();
    
    
    let field_name = String::from("Favorite color");
    let field_value = String::from("Blue");

    let mut map = HashMap::new();
    map.insert(field_name, field_value);
    // 这里 field_name 和 field_value 不再有效 
    // println!("{}={}", field_name，field_value);
    
    let team_name = String::from("Blue");
    let score = scores.get(&team_name);
    for (key, value) in &scores {
        println!("{}: {}", key, value);
    }


    let mut scores = HashMap::new();
    scores.insert(String::from("Blue"), 10);
    scores.insert(String::from("Blue"), 25); 
    scores.entry(String::from("Yellow")).or_insert(50);//没有键时才插入
    scores.entry(String::from("Blue")).or_insert(50);
    println!("{:?}", scores);

    
    let text = "hello world wonderful world"; 
    let mut map = HashMap::new(); 
    for word in text.split_whitespace() {
        let count = map.entry(word).or_insert(0);//or_insert 返回这个键的值的一个可变引用
        *count += 1; //map的值0 + 1
    } 
    println!("{:?}", map); 
}
//-----09 string
fn main(){
     
    let data = "initial contents"; 
    let s = data.to_string(); //从&str转换为String,只可是UTF-8 

    let mut s = String::from("lo"); 
    s.push_str("bar");
    s.push('l');


    let s1 = String::from("Hello, ");
    let s2 = String::from("world!");
    let s3 = s1 + &s2; // 注意 s1 被移动了，不能继续使用
    //println!("{}", s1);
    let s1 = String::from("tic");
    let s2 = String::from("tac");
    let s3 = String::from("toe"); 
    let s = format!("{}-{}-{}", s1, s2, s3); //format!宏 ，并且不会获取任何参数的所有权。
    println!("{}", s1);
 
    

    let hello = "abcdefg"; 
    let s = &hello[0..4];
    println!("{}", s);

    for c in "abcdefg".chars() {
        println!("{}", c);
    }
    for b in "abcdefg".bytes() {
        println!("{}", b);//是ascii
    }
}
//----10 error
use std::fs::File;
use std::io::ErrorKind;
fn main(){

    /*当出现 panic 时，程序默认会开始 展开（unwinding）,清理它遇到的每一个函数的数据,另一种选择是直接 终止（abort），这会不清理数据就退出程序
    Cargo.toml 的 [profile] 部分增加 panic = 'abort'，可以由展开切换为终止

    如果你想要在release模式中 panic 时直接终止：
    [profile.release]
    panic = 'abort'
    */

    // panic!("crash and burn");

    let v = vec![1, 2, 3]; 
    //v[99]; //引发panic


    //提示环境变量 RUST_BACKTRACE=1 ，backtrace 是一个执行到目前位置所有被调用的函数的列表
    //为了获取带有这些信息的 backtrace，必须启用 debug 标识。会默认启用(除非用 --release 参数运行 cargo build 或 cargo run 时 debug 标识)

    
 
    let f = File::open("hello.txt");
/*

    //panic时 vscode debug 控制台中文乱码???  ,rustc命令行运行正常
    let f = match f {
        Ok(file) => file,
        Err(error) => {
            panic!("Problem opening the file: {:?}", error)
        },
    };

*/

/*
    let f = match f {
        Ok(file) => file,
        Err(error) => match error.kind() {
            ErrorKind::NotFound => 
                match File::create("hello.txt") { 
                    Ok(fc) => fc,
                    Err(e) => panic!("Problem creating the file: {:?}", e),
                },
            other_error => panic!("Problem opening the file: {:?}", other_error),
        },
    };
*/
    //如果 Result 值是成员 Ok，unwrap 会返回 Ok 中的值。如果 Result 是成员 Err，unwrap 会为我们调用 panic!
    //let f = File::open("hello.txt").unwrap();

    use std::net::IpAddr; 
    let home: IpAddr = "127.0.0.1".parse().unwrap();
    
    //let f = File::open("hello.txt").expect("Failed --- to open hello.txt");//追加自己的错误消息
    
    
    read_username_from_file();
    read_username_from_file2();
    read_username_from_file3();
   let res:Result<String, io::Error> =  read_username_from_file4();
    println!("res={:?}",res);
 
}


use std::io;
use std::io::Read; 
fn read_username_from_file() -> Result<String, io::Error> {
    let f = File::open("hello.txt");

    let mut f = match f {
        Ok(file) => file,
        Err(e) => return Err(e),
    };

    let mut s = String::new(); 
    match f.read_to_string(&mut s) {
        Ok(_) => Ok(s),
        Err(e) => Err(e),
    }
}

// 传播错误  提供了? 问号运算符来使其更易于处理。如果出现了错误，? 运算符会提早返回整个函数并将一些 Err 值传播给调用者
fn read_username_from_file2() -> Result<String, io::Error> {
    let mut f = File::open("hello.txt")?;//?必须放在返回Result 或 Option 的函数中
    let mut s = String::new();
    f.read_to_string(&mut s)?;
    Ok(s)
}

fn read_username_from_file3() -> Result<String, io::Error> {
    let mut s = String::new(); 
    File::open("hello.txt")?.read_to_string(&mut s)?; //?也可链式调用 
    Ok(s)
}

use std::fs;  
fn read_username_from_file4() -> Result<String, io::Error> {
    fs::read_to_string("hello.txt")//找的位置是运行程序时的当前目录 
}


//---10-2 error
 use std::error::Error;//是一个 trait 
 //io::Error  进入为  std::io::Error  ，是一个struct ，只是文件读写的错误
 //Err
 use std::fs::File;
 
  //main 函数的一个有效的返回值是 ()
 fn main() -> Result<(), Box<dyn Error>> {
     let f = File::open("hello.txt")?;
 
     Ok(())
 }
 
//---11  generic
 
//&[i32] ,寻找 slice 中最大的 i32
fn largest(list: &[i32]) -> i32 {
    let mut largest = list[0];

    for &item in list {
        if item > largest {
            largest = item;
        }
    }

    largest
}
 


fn main() {
    let number_list = vec![34, 50, 25, 100, 65];

    let result = largest(&number_list);
    println!("The largest number is {}", result);

    let number_list = vec![102, 34, 6000, 89, 54, 2, 43, 8];

    let result = largest(&number_list);
    println!("The largest number is {}", result);



    let p = Point { x: 5, y: 10 }; 
    println!("p.x = {}", p.x());

}
struct Point<T> {
    x: T,
    y: T,
}

//impl<T>  ,Point 的尖括号中的类型是泛型而不是具体类型。
impl<T> Point<T> {  
    fn x(&self) -> &T {
        &self.x
    }
}

impl Point<f32> {
    fn distance_from_origin(&self) -> f32 {
        (self.x.powi(2) + self.y.powi(2)).sqrt()
    }
}

//--11-2 generic
 

fn main() { 
    let p1 = Point { x: 5, y: 10.4 };
    let p2 = Point { x: "Hello", y: 'c'};

    let p3 = p1.mixup(p2);

    println!("p3.x = {}, p3.y = {}", p3.x, p3.y);
	
	let num="123";
    match num.parse::<i32>().map(|i| i * 2) { //函数模板用::
        Ok(n) => println!("{n}"),//{}内是变量
        Err(..) => {}
    }
	
} 

struct Point<T, U> {
    x: T,
    y: U,
}

impl<T, U> Point<T, U> {
    //方法泛型
    fn mixup<V, W>(self, other: Point<V, W>) -> Point<T, W> {
        Point {
            x: self.x,
            y: other.y,
        }
    }
}

//使用泛型时没有运行时开销,只是编译生成特定类型的代码

//----12  trait
 
use std::fmt::Display;

//scala也有trait 特点，特征）,类似Java的 接口

pub trait Summary {
    // fn summarize(&self) -> String;
     fn summarize(&self) -> String { //可以有默认实现
         String::from("(Read more...)")
     }
 }
 
 pub struct NewsArticle {
     pub headline: String,
     pub location: String,
     pub author: String,
     pub content: String,
 }
 
 impl Summary for NewsArticle {
     fn summarize(&self) -> String {
         format!("{}, by {} ({})", self.headline, self.author, self.location)
     }
 }
  
 
 
 pub struct Tweet {
     pub username: String,
     pub content: String,
     pub reply: bool,
     pub retweet: bool,
 }
 
 impl Summary for Tweet {
     fn summarize(&self) -> String {
         format!("{}: {}", self.username, self.content)
     }
 }
 
fn main() { 

    let tweet = Tweet {
        username: String::from("horse_ebooks"),
        content: String::from("of course, as you probably already know, people"),
        reply: false,
        retweet: false,
    };
    
    println!("1 new tweet: {}", tweet.summarize());
    notify(tweet);


    let char_list = vec!['y', 'm', 'a', 'q']; 
    let result = largest(&char_list);
    println!("The largest char is {}", result);

    let number_list = vec![34, 50, 25, 100, 65]; 
    let result = largest(&number_list);
    println!("The largest number is {}", result);



    //----
    let tweet = Tweet {
        username: String::from("horse_ebooks"),
        content: String::from("of course, as you probably already know, people"),
        reply: false,
        retweet: false,
    };
    let tweet2 = Tweet {
        username: String::from("horse_ebooks"),
        content: String::from("of course, as you probably already know, people"),
        reply: false,
        retweet: false,
    };
    //let p:Pair<Tweet> =Pair::new(tweet,tweet2);//没有实现Display + PartialOrd不能调用cmp_display
    let p:Pair<String> =Pair::new(String::from("def"),String::from("abc"));
    p.cmp_display();

}
 

pub fn notify(item: impl Summary) {//参数也要加 impl
    println!("Breaking news! {}", item.summarize());
}
pub fn notify2<T: Summary>(item: T) {//泛型 类型 为 trait
    println!("Breaking news! {}", item.summarize());
}
pub fn notify3(item: impl Summary + Display) { //+ 多个 trait,Display导入的

}

/* 
fn some_function<T: Display + Clone, U: Clone + Debug>(t: T, u: U) -> i32 {
}
//简化写法，使用 where 从句 
fn some_function<T, U>(t: T, u: U) -> i32
    where T: Display + Clone,
            U: Clone + Debug
{
}
*/
fn returns_summarizable() -> impl Summary { //返回也要加impl
    Tweet {
        username: String::from("horse_ebooks"),
        content: String::from("of course, as you probably already know, people"),
        reply: false,
        retweet: false,
    }
}


//自带的 PartialOrd,Copy 两个trait ,std::cmp::PartialOrd 可以实现类型的比较功能
fn largest<T: PartialOrd + Copy>(list: &[T]) -> T {
//fn largest<T: PartialOrd >(list: &[T]) -> T {
    let mut largest = list[0];//i32 和 char 这样的类型是已知大小的并可以储存在栈上，所以他们实现了 Copy

    for &item in list.iter() {
        if item > largest { //就可以比较了
            largest = item;
        }
    }

    largest
}
//如函数返回值从 T 改为 &T 并改变函数体使其能够返回一个引用，我们将不需要  Copy
 

 

//结构体
struct Pair<T> {
    x: T,
    y: T,
}

impl<T> Pair<T> {
    fn new(x: T, y: T) -> Self {  //返回类型,大写S
        Self {
            x,
            y,
        }
    }
}
//两个实现的泛型T是不同，调用这个方法要x和y实现了两个trait
impl<T: Display + PartialOrd> Pair<T> {
    fn cmp_display(&self) {
        if self.x >= self.y { //PartialOrd
            println!("The largest member is x = {}", self.x);
        } else {
            println!("The largest member is y = {}", self.y);
        }
    }
}
 
//----13  lifecycle
fn main(){

    let string1 = String::from("abcd");
    let string2 = "xyz";

    let result = longest(string1.as_str(), string2);  //as_str() 切片
    println!("The longest string is {}", result);
    
    //--
    let string1 = String::from("long string is long"); 
    {
        let string2 = String::from("xyz");
        let result = longest(string1.as_str(), string2.as_str());
        println!("The longest string is {}", result);
    }
    //--
    /*
    let string1 = String::from("long string is long");
    let result; //生命周期比string2长,不行
    {
        let string2 = String::from("xyz");
        result = longest(string1.as_str(), string2.as_str());
    }
    println!("The longest string is {}", result);
    */


    let novel = String::from("Call me Ishmael. Some years ago...");
    let first_sentence = novel.split('.')
        .next()
        .expect("Could not find a '.'");
    let i = ImportantExcerpt { part: first_sentence };
 

/*
    生命周期省略规则
    1.每一个是引用的参数都有它自己的生命周期参数
    2.如果只有一个输入生命周期参数，那么它被赋予所有输出生命周期参数
    3.如果方法有多个输入生命周期参数并且其中一个参数是 &self 或 &mut self，说明是个对象的方法,那么所有输出生命周期参数被赋予 self 的生命周期
*/


    //所有的字符串字面值都拥有 'static 生命周期
    let s: &'static str = "I have a static lifetime.";//存在程序的二进制文件中

}

/*
fn longest(x: &str, y: &str) -> &str {
    if x.len() > y.len() {
        x
    } else {
        y
    }
}
*/

// &i32        // 引用
// &'a i32     // 带有显式生命周期的引用，   生命周期名字为a
// &'a mut i32 // 带有显式生命周期的可变引用 
fn longest<'a>(x: &'a str, y: &'a str) -> &'a str {
	//它的实际含义是 longest 函数返回的引用的生命周期与传入该函数的引用的生命周期的较小者一致。 
    if x.len() > y.len() {
        x
    } else {
        y
    }
}

/*
 当返回一个引用，返回值的生命周期参数需要与一个参数的生命周期参数相匹配。
 如果返回的引用 没有 指向任何一个参数，那么唯一的可能就是它指向一个函数内部创建的值，它将会是一个悬垂引用
fn longest2<'a>(x: &str, y: &str) -> &'a str {//编译失败原因，返回值的生命周期与参数完全没有关联
    let result = String::from("really long string");
    result.as_str()
}
*/


struct ImportantExcerpt<'a> {
    part: &'a str,
}
//ImportantExcerpt 的实例不能比其 part 字段中的引用存在的更久



//impl 之后和类型名称之后的生命周期参数是必要的
impl<'a> ImportantExcerpt<'a> { //impl后加生命周期，同泛型使用
    fn level(&self) -> i32 {//因为第一条生命周期规则我们并不必须标注 self 引用的生命周期。
        3
    }
}


impl<'a> ImportantExcerpt<'a> {
    fn announce_and_return_part(&self, announcement: &str) -> &str { //第三条生命周期省略规则的例子：
        println!("Attention please: {}", announcement);
        self.part
    }
}


use std::fmt::Display;
//泛型和生命周期一起使用
fn longest_with_an_announcement<'a, T>(x: &'a str, y: &'a str, ann: T) -> &'a str
    where T: Display
{
    println!("Announcement! {}", ann);
    if x.len() > y.len() {
        x
    } else {
        y
    }
}

  

//----14 test   src/lib.rs
 //文件名不叫lib.rs,单元测试不能跑？？
//编译提示 either src/lib.rs, src/main.rs, a [lib] section, or [[bin]] section must be present

fn  main(){
    println!("hello")
}

//cargo new mytest --lib 生成的项目有lib.rs代码 , cargo test 来运行
//不希望测试并行运行 cargo test -- --test-threads=1
 
#[cfg(test)]// cargo test 时才编译和运行测试代码,cargo build则忽略
//单元测试位于与源码相同的文件中，所以你需要使用 #[cfg(test)] 来指定他们不应该被包含进编译结果中。
mod tests {
    #[test] //cargo test 会执行有这个标志的
    fn it_works() {  //cargo test it_works 只测试这个用例 ，也可是名字中的一部分做匹配
        println!("2+2!=???"); //cargo test -- --nocapture 才能显示这个值 
        assert_eq!(2 + 2, 4);
    }

     #[ignore] //忽略, cargo test -- --ignored  只运行有忽略标志的
     #[test]
     fn another() {  //cargo test an ,名字中有an的
        panic!("Make this test fail"); //使用panic! 做失败
    }

    
    use super::*;  //导入父模块中的所有内容.就可以认Rectangle
    #[test]
    fn larger_can_hold_smaller() {
        let larger = Rectangle { width: 8, height: 7 };
        let smaller = Rectangle { width: 5, height: 1 };

        assert!(larger.can_hold(&smaller));//为true
    }

    
    #[test]
    fn it_adds_two() {
        assert_eq!(4, add_two(2));
        assert_ne!(5, add_two(2));
    }
 
    #[test]
    #[ignore] //忽略
    fn greeting_contains_name() {
        let result = greeting("Carol");
        assert!(
            result.contains("Carol"),
            "Greeting did not contain name, value was `{}`", result
        );//如失败显示自已定义的错误消息
    }

    #[should_panic]//测试有异常的情况
    #[test]
    fn greater_than_100() {
        Guess::new(200);
    }
    
    #[test]
    fn internal() {
        assert_eq!(4, internal_adder(2, 2));//可以测试私有函数，因已经 use
    }
}
 
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32,
}

impl Rectangle {
    fn can_hold(&self, other: &Rectangle) -> bool {
        self.width > other.width && self.height > other.height
    }
}
pub fn add_two(a: i32) -> i32 {
    a + 2
}
pub fn greeting(name: &str) -> String {
    String::from("Hello!")
}
//没pub 的函数
fn internal_adder(a: i32, b: i32) -> i32 {
    a + b
}

pub struct Guess {
    value: i32,
}

impl Guess {
    pub fn new(value: i32) -> Guess {
        if value < 1 || value > 100 {
            panic!("Guess value must be between 1 and 100, got {}.", value);
        }

        Guess {
            value
        }
    }
}

//----14 tests/common.rs 与src同级

pub fn setup() {
    // 编写特定库测试所需的代码
    println!("perpare test ----"); 
}

//----14 tests/integration_test.rs 与src同级

//集成测试，需要在项目根目录创建一个 tests 目录，与 src 同级,Cargo 会将每一个文件当作单独的 crate 来编译
//也是用cargo test 运行，有三部分：单元测试、集成测试和文档测试
//cargo test --test integration_test ，只集成测试，参数是文件名
use mytest;//项目名，tomal文件中的name的值

#[test]
fn it_adds_two() {
    assert_eq!(4, mytest::add_two(2));
}
 
mod common;  //模块声明
#[test]
fn it_adds_two2() {
    common::setup();
    assert_eq!(4, mytest::add_two(2));
}
//cargo test --test integration_test  -- --nocapture
  
  
  
//let case_sensitive = env::var("CASE_INSENSITIVE").is_err();//返回的是is_err()的值
let case_sensitive = env::var("CASE_INSENSITIVE").unwrap();//读环境变量 返回Result
  
  
//---16  闭包

fn  add_one_v1   (x: u32) -> u32 { x + 1 }//函数
let add_one_v2 = |x: u32| -> u32 { x + 1 };//闭包
let add_one_v3 = |x: u32|        { x +1 }; //新版本参数必须要有类型,因有+1操作，如直接返回就可以没有
let add_one_v4 = |x: u32|          x +1 ;

let example_closure = |x:u32| x;

let expensive_closure = |num| { //闭包,参数可没有类型
	println!("calculating slowly...");
	thread::sleep(Duration::from_secs(1));//线程暂停
	num
};
expensive_closure(10)

//---闭的包泛型
struct Cacher<T>
    where T: Fn(u32) -> u32 //所有的闭包都实现了 trait Fn、FnMut 或 FnOnce 中的一个trait 
{
    calculation: T,
    value: Option<u32>,
}

impl<T> Cacher<T>
    where T: Fn(u32) -> u32
{
    fn new(calculation: T) -> Cacher<T> {
        Cacher {
            calculation,
            value: None,
        }
    }

    fn value(&mut self, arg: u32) -> u32 {
        match self.value {
            Some(v) => v,
            None => {
                let v = (self.calculation)(arg);
                self.value = Some(v);
                v
            },
        }
    }
}
 
let mut expensive_result = Cacher::new(|num| {
	println!("calculating slowly...");
	thread::sleep(Duration::from_secs(2));
	num
});
expensive_result.value(10)




let mut c = Cacher::new(|a| a); 
let v1 = c.value(1);
let v2 = c.value(2); 
println!("{}",v2==2);//false ,第一次使用 1 调用 c.value，Cacher 实例将 Some(1) 保存进 self.value。在这之后，无论传递什么值调用 value，它总是会返回 1。

let x = 4; 
let equal_to_x = |z| z == x;  //闭包比函数强的是可以仿问到外部变量x
let y = 4;  
println!("{}",equal_to_x(y));//true
//FnOnce 闭包必须获取其所有权并在定义闭包时将其移动进闭包
//FnMut 获取可变的借用值所以可以改变其环境
//Fn 从其环境获取不可变的借用值
let x = vec![1, 2, 3]; 
let equal_to_x = move |z| z == x; 
// println!("can't use x here: {:?}", x);//移动的，这里不能再使用x 
let y = vec![1, 2, 3]; 
assert!(equal_to_x(y));
//Fn 开始，而编译器会根据闭包体中的情况告诉你是否需要 FnMut 或 FnOnce。

//-------17 iterator 
let v1 = vec![1, 2, 3];
let v1_iter = v1.iter();
assert_eq!(v1_iter.next(), Some(&1));//数字前也能加引用
 
let total: i32 = v1_iter.sum();//聚合计算
assert_eq!(total, 6);

let v1: Vec<i32> = vec![1, 2, 3]; 
let v2: Vec<_> = v1.iter().map(|x| x + 1).collect(); //像java ,scala 
assert_eq!(v2, vec![2, 3, 4]);

struct Shoe {
    size: u32,
    style: String,
} 
fn shoes_in_my_size(shoes: Vec<Shoe>, shoe_size: u32) -> Vec<Shoe> {
    shoes.into_iter()
        .filter(|s| s.size == shoe_size)//filter
        .collect()
}


struct Counter {
    count: u32,
} 
impl Counter {
    fn new() -> Counter {
        Counter { count: 0 }
    }
}
impl Iterator for Counter {
    type Item = u32;//有点像C中的typedef ,next 方法都会返回一个包含了此具体类型值的

    fn next(&mut self) -> Option<Self::Item> { //自定义迭代器
        self.count += 1;

        if self.count < 6 {
            Some(self.count)
        } else {
            None
        }
    }
}


let mut counter = Counter::new(); 
assert_eq!(counter.next(), Some(1));
assert_eq!(counter.next(), Some(2));
assert_eq!(counter.next(), Some(3));
assert_eq!(counter.next(), Some(4));
assert_eq!(counter.next(), Some(5));
assert_eq!(counter.next(), None);

//------------19 智能指针  Box
use crate::List::{Cons, Nil}; //自己定义的

fn main() {
    let b = Box::new(5); //Box<T> 在堆上储存数据
    println!("b = {}", b);
 
    let list = Cons(1,
        Box::new(Cons(2,
            Box::new(Cons(3,
                Box::new(Nil)))))); //像clojure，lisp这种括号,看着晕，可能是rust作者离开mozilla的原因


    let x = 5;
    let y = &x;
    assert_eq!(5, x);
    assert_eq!(5, *y);//解引用 

    let y = Box::new(x); //Box和&使用一样，用*
    assert_eq!(5, *y);
	
	 let x = 5;
    let y = MyBox::new(x); //自定义智能指针 实现 Deref 
    assert_eq!(5, x);
    assert_eq!(5, *y);//Rust 底层运行 *(y.deref()) 
	
	let c = CustomSmartPointer { data: String::from("my stuff") };
    let d = CustomSmartPointer { data: String::from("other stuff") };
    println!("CustomSmartPointers created.");
    //希望在作用域结束之前就强制释放变量的话，我们应该使用的是由标准库提供的 std::mem::drop。 
    std::mem::drop(c); //不能调用c.drop();
    println!("CustomSmartPointer dropped before the end of main.");
    
} 
enum List {
    Cons(i32, Box<List>),//如不用Box，就无限循环计算所占空间，报  recursive without indirection
    Nil,
}

struct MyBox<T>(T);   //元组结构体，self.0来引用这个

impl<T> MyBox<T> {
    fn new(x: T) -> MyBox<T> {
        MyBox(x)
    }
}
use std::ops::Deref; 
impl<T> Deref for MyBox<T> { //实现 Deref 
    type Target = T;

    fn deref(&self) -> &T { //通过 * 运算符访问的值的引用
        &self.0
    }
}
//Rust 提供了 DerefMut trait 用于重载可变引用的 * 运算符。
//Rust 也会将可变引用强转为不可变引用。但是反之是 不可能

struct CustomSmartPointer {
    data: String,
} 
//Drop  全路径为 std::ops::Drop 可省，标准库的前奏是包含每个模块中自动导入的所有东西的模块 ，即https://doc.rust-lang.org/std/all.html 下都自动导入，Box,Option等
impl Drop for CustomSmartPointer {//值离开作用域时应该执行的代码的方式是实现 Drop
    fn drop(&mut self) {
        println!("Dropping CustomSmartPointer with data `{}`!", self.data);
    }
}
//-------19 RC

enum List {
    Cons(i32, Rc<List>),//Rc<T> 的类型。其名称为 引用计数（reference counting）, 只能用于单线程场景
    Nil,
}

use crate::List::{Cons, Nil};
use std::rc::Rc;

fn main() {
    let a = Rc::new(Cons(5, Rc::new(Cons(10, Rc::new(Nil)))));
      println!("count after creating a = {}", Rc::strong_count(&a));//引用数
    let b = Cons(3, Rc::clone(&a));//克隆 Rc<T> 会增加引用计数
    println!("count after creating b = {}", Rc::strong_count(&a));
    {
        let c = Cons(4, Rc::clone(&a));
        println!("count after creating c = {}", Rc::strong_count(&a));
    }
    println!("count after c goes out of scope = {}", Rc::strong_count(&a));

  
}
//---------19 RefCell
//不同于 Rc<T>，RefCell<T> 代表其数据的唯一的所有权 ,RefCell<T> 只能用于单线程场景
use std::cell::RefCell;
struct MockMessenger {
	//sent_messages: Vec<String>,
	 sent_messages: RefCell<Vec<String>>,
}


//self.sent_messages.push(String::from(message)); //self在定义时为 不可变的
self.sent_messages.borrow_mut().push(String::from(message));;//borrow_mut()不可变，修改为 可变

// let mut one_borrow = self.sent_messages.borrow_mut();
// let mut two_borrow = self.sent_messages.borrow_mut();//不能创建两个可变借用，编译时没有错误，link时有
// one_borrow.push(String::from(message));
// two_borrow.push(String::from(message));


//assert_eq!(mock_messenger.sent_messages.len(), 1);
assert_eq!(mock_messenger.sent_messages.borrow().len(), 1);//取出用borrow()

//----19 
//结合 Rc<T> 和 RefCell<T> 来拥有多个可变数据所有者

#[derive(Debug)]
enum List {
    Cons(Rc<RefCell<i32>>, Rc<List>),
    Nil,
}

use crate::List::{Cons, Nil};
use std::rc::Rc;
use std::cell::RefCell;

fn main() {
    let value = Rc::new(RefCell::new(5));

    let a = Rc::new(Cons(Rc::clone(&value), Rc::new(Nil)));

    let b = Cons(Rc::new(RefCell::new(6)), Rc::clone(&a));
    let c = Cons(Rc::new(RefCell::new(10)), Rc::clone(&a));

    *value.borrow_mut() += 10;
  
    println!("a after = {:?}", a);
    println!("b after = {:?}", b);
    println!("c after = {:?}", c);
}
//---tree weak


//树，  leaf.parent 将会指向 branch 而 branch.children 会包含 leaf 的指针，这会形成引用循环，解决方法用Weak
use std::rc::{Rc, Weak};
use std::cell::RefCell;

#[derive(Debug)]
struct Node {
    value: i32,
    parent: RefCell<Weak<Node>>,
     //如果父节点被丢弃了，其子节点也应该被丢弃
     //然而子节点不应该拥有其父节点：如果丢弃子节点，其父节点应该依然存在
    children: RefCell<Vec<Rc<Node>>>,
}
 
fn main() { 

    let leaf = Rc::new(Node {
        value: 3,
        parent: RefCell::new(Weak::new()),
        children: RefCell::new(vec![]),
    });

    println!("leaf parent = {:?}", leaf.parent.borrow().upgrade());//调用 Weak<T> 实例的 upgrade 方法，这会返回 Option<Rc<T>>,新的返回 Option<Arc<T>>
    // ‘Arc’ stands for ‘Atomically Reference Counted’. (thread-safe)
    //upgrade 方法 如值还未被丢弃，则结果是 Some  如已被丢弃，则结果是 None

    let branch = Rc::new(Node {
        value: 5,
        parent: RefCell::new(Weak::new()),
        children: RefCell::new(vec![Rc::clone(&leaf)]),
    });

	
    *leaf.parent.borrow_mut() = Rc::downgrade(&branch);//Rc::downgrade 并传递 Rc<T> 实例的引用来创建其值的 弱引用

	println!(
        "branch strong = {}, weak = {}",
        Rc::strong_count(&branch),
        Rc::weak_count(&branch),//weak_count 无需计数为 0 就能使 Rc<T> 实例被清理。
    );

    println!(
        "leaf strong = {}, weak = {}",
        Rc::strong_count(&leaf),
        Rc::weak_count(&leaf),
    );
	
    println!("leaf parent = {:?}", leaf.parent.borrow().upgrade());
}
//----20 线程
use std::thread;
use std::time::Duration;

fn main() { 
    //新线程,调用 thread::spawn 函数并传递一个闭包,spawn返回一个 JoinHandle<T>   ，join方法返回  Result
    let handle = thread::spawn(|| { 
        for i in 1..10 {
            println!("hi number {} from the spawned thread!", i);
            thread::sleep(Duration::from_millis(1));
        }
    });

    
    for i in 1..5 {
        println!("hi number {} from the main thread!", i);
        thread::sleep(Duration::from_millis(1));
    }
    //主线程退出，子线程也会退出（未执行完）
    //join 阻塞当前线程直到 handle 所代表的线程结束。
    handle.join().unwrap();
 

    let v = vec![1, 2, 3];
    let handle = thread::spawn(move || {  //move 闭包获取其使用的值的所有权，如是只读引用子线程不知道v能活多久
        println!("Here's a vector: {:?}", v); 
    });
    handle.join().unwrap();
	
    //--通道 像go 语言
    //mpsc 是 多个生产者，单个消费者（multiple producer, single consumer）的缩写
    use std::sync::mpsc;
    let (tx, rx) = mpsc::channel();//返回 第一个元素是发送端，而第二个元素是接收端
    thread::spawn(move || {
        let val = String::from("hi");
        tx.send(val).unwrap();
        //println!("val is {}", val);//val被移动了，这不能使用
    });
   let received = rx.recv().unwrap();//recv 会阻塞主线程执行直到从通道中接收一个值,try_recv 不会阻塞
    println!("Got: {}", received);
 
 //-- 
    let (tx, rx) = mpsc::channel();  
    let tx1 = tx.clone(); //多个生产者
    thread::spawn(move || {
        let vals = vec![
            String::from("hi"),
            String::from("from"),
            String::from("the"),
            String::from("thread"),
        ];

        for val in vals {
            tx1.send(val).unwrap();
            thread::sleep(Duration::from_secs(1));
        }
    });

    thread::spawn(move || {
        let vals = vec![
            String::from("more"),
            String::from("messages"),
            String::from("for"),
            String::from("you"),
        ];

        for val in vals {
            tx.send(val).unwrap();
            thread::sleep(Duration::from_secs(1));
        }
    }); 
    for received in rx { //接收方式，也是阻塞的
        println!("Got: {}", received);
    }

}
//-----21 thread lock
 
use std::thread;  
use std::rc::Rc;
use std::sync::{Mutex, Arc};
fn main()
{
    let m = Mutex::new(5);
     //Mutex<T> 是一个智能指针。更准确的说，lock 调用 返回 一个叫做 MutexGuard 的智能指针。这个智能指针实现了 Deref 来指向其内部数据；其也提供了一个 Drop 实现当 MutexGuard 离开作用域时自动释放锁
     //也是会死锁的
    {
        let mut num = m.lock().unwrap();
        *num = 6;
    }

    println!("m = {:?}", m);

 
    //let counter = Mutex::new(0);
    //let counter = Rc::new(Mutex::new(0));//Rc不能用于多线程
    let counter = Arc::new(Mutex::new(0));
    //Arc<T> 类似 Rc<T> 安全的用于并发环境的类型。字母 “a” 代表 原子性（atomic）
    //可以使用 Mutex<T> 来改变 Arc<T> 中的内容。
    let mut handles = vec![];

    for _ in 0..10 {
        //let counter = Rc::clone(&counter);//如不克隆会被移动
        let counter = Arc::clone(&counter);
        let handle = thread::spawn(move || {
            let mut num = counter.lock().unwrap();

            *num += 1;
        });
        handles.push(handle);
    }

    for handle in handles {
        handle.join().unwrap();
    } 
    println!("Result: {}", *counter.lock().unwrap()); 
} 

//Send 标记 trait 表明类型的所有权可以在线程间传递。几乎所有的 Rust 类型都是Send 的，不过有一些例外，包括 Rc<T>
//Sync 标记 trait 表明一个实现了 Sync 的类型可以安全的在多个线程中拥有其值的引用,即如果 &T是 Send 的话， T 就是 Sync 的，
//基本类型是 Sync 的，Rc<T> 也不是 Sync 的,RefCell<T>和 Cell<T> 系列类型不是 Sync 的,Mutex<T> 是 Sync 的
//通常并不需要手动实现 Send 和 Sync trait,手动实现 Send 和 Sync 是不安全的


//-----22 1面向对象 
fn main(){
    let screen = Screen {
        components: vec![
            Box::new(SelectBox {
                width: 75,
                height: 10,
                options: vec![
                    String::from("Yes"),
                    String::from("Maybe"),
                    String::from("No")
                ],
            }),
            Box::new(Button {
                width: 50,
                height: 10,
                label: String::from("OK"),
            }),
        ],
    };

    screen.run();
}
pub struct AveragedCollection {
    list: Vec<i32>,
    average: f64,
}
impl AveragedCollection {
    pub fn add(&mut self, value: i32) {
        self.list.push(value);
        self.update_average();
    }

    pub fn remove(&mut self) -> Option<i32> {
        let result = self.list.pop();
        match result {
            Some(value) => {
                self.update_average();
                Some(value)
            },
            None => None,
        }
    }

    pub fn average(&self) -> f64 {
        self.average
    }

    fn update_average(&mut self) {
        let total: i32 = self.list.iter().sum();
        self.average = total as f64 / self.list.len() as f64; //as强转
    }
}
 

pub trait Draw {
    fn draw(&self);
}
pub struct Screen {
    pub components: Vec<Box<dyn Draw>>,  //dyn , 任何实现了Draw的 ,多态
//   trait 中所有的方法有如下属性时，则该 trait 是对象安全的：
// 1.返回值类型不为 Self
// 2.方法没有任何泛型类型参数
//Clone 不是安全的 不能放在dyn后面
}
impl Screen {
    pub fn run(&self) {
        for component in self.components.iter() {
            component.draw();
        }
    }
}
pub struct Button {
    pub width: u32,
    pub height: u32,
    pub label: String,
}

impl Draw for Button {
    fn draw(&self) {
        println!("button drawing ...");
    }
}


struct SelectBox {
    width: u32,
    height: u32,
    options: Vec<String>,
}

impl Draw for SelectBox {
    fn draw(&self) {
        println!("SelectBox drawing ...");
    }
}
//-----22 2 面向对象 
fn main() { 
    let text: Option<String> = Some("Hello, world!".to_string());
    // First, cast `Option<String>` to `Option<&String>` with `as_ref`,
    // then consume *that* with `map`, leaving `text` on the stack.
    let text_length: Option<usize> = text.as_ref().map(|s| s.len());//map方法是Option的
    println!("still can print text: {:?}", text);
    println!("still can print text: {:?}", text_length);

   //----
    let maybe_some_string = Some(String::from("Hello, World!"));
    // `Option::map` takes self *by value*, consuming `maybe_some_string`
    let maybe_some_len = maybe_some_string.map(|s| s.len()); //maybe_some_string 被移动了
    assert_eq!(maybe_some_len, Some(13));
    //println!("maybe_some_string: {:?}", maybe_some_string);//这不能使用maybe_some_string
   //----
   let text_length: Option<usize> =  Some("Hello, world!".to_string()).map(|s| s.len());//加as_ref() 报错，因没有变量,测试没有错误
   println!("still can print text: {:?}", text_length);
   let text_length: Option<usize> =  Some(String::from("Hello, World!")).map(|s| s.len());//error
   println!("still can print text: {:?}", text_length);
  
  //Option的take()方法
 //Option的as_ref()方法, 需要 Option 中值的引用而不是获取其所有权
}

 fn request_review(self: Box<Self>) -> Box<dyn State>; //self: Box<Self>  和 &self 一样？


//---23 match
fn main() {
    let favorite_color: Option<&str> = None;
    let is_tuesday = false;
    let age: Result<u8, _> = "34".parse();

    if let Some(color) = favorite_color {
        println!("Using your favorite color, {}, as the background", color);
    } else if is_tuesday {
        println!("Tuesday is green day!");
    } else if let Ok(age1) = age { //新的match(if let)
        if age1 > 30 {
            println!("Using purple as the background color");//print
        } else {
            println!("Using orange as the background color");
        }
    } else {
        println!("Using blue as the background color");
    }


    
    let mut stack = Vec::new();

    stack.push(1);
    stack.push(2);
    stack.push(3);
    //只要模式匹配就一直进行 while 循环
    while let Some(top) = stack.pop() {
        println!("{}", top);
    }



    let point = (3, 5);
    print_coordinates(&point);
 
     
   // 不可反驳的( irrefutable )   let x = 5;  x 可以匹配任何值所以不可能会失败
   if let x = 5 { //会有  irrefutable 的警告
        println!("{}", x);
    };

    let x = Some(5);
    let y = 10; 
    match x {
        Some(50) => println!("Got 50"),
        Some(y) => println!("Matched, y = {:?}", y),//此处y是内部的临时变量，不同于外部的y=10
        _ => println!("Default case, x = {:?}", x),//其它模式
    }

    println!("at the end: x = {:?}, y = {:?}", x, y);


    let x = 1;

    match x {
        1 | 2 => println!("one or two"), // | 或者的意思
        3 => println!("three"),
        _ => println!("anything"),
    }
    match x {
        1..=5 => println!("one through five"),  //..=是范围  1-5,还有 for number in (1..4).rev()  范围 1-3
        _ => println!("something else"),
    }
    let x = 'c'; 
    match x {
        'a'..='j' => println!("early ASCII letter"), 
        'k'..='z' => println!("late ASCII letter"),
        _ => println!("something else"),
    }


    let p = Point { x: 0, y: 7 };

    let Point { x: a, y: b } = p; //创建了变量 a 和 b 来匹配结构体 p 中的 x 和 y 字段
    assert_eq!(0, a);
    assert_eq!(7, b);
  
    let Point { x, y } = p; //同x:x,y:y
    assert_eq!(0, x);
    assert_eq!(7, y);

    let p = Point { x: 0, y: 7 }; 
    match p {
        Point { x, y: 0 } => println!("On the x axis at {}", x), //x任何值 
        Point { x: 0, y } => println!("On the y axis at {}", y),
        Point { x, y } => println!("On neither axis: ({}, {})", x, y),
    } 

    
    let ((feet, inches), Point {x, y}) = ((3, 10), Point { x: 3, y: -10 });//元组嵌套
   
    foo(3, 4);

    let mut setting_value = Some(5);
    let new_setting_value = Some(10); 
    match (setting_value, new_setting_value) {
        (Some(_), Some(_)) => { //_ match中项的忽略
            println!("Can't overwrite an existing customized value");
        }
        _ => { //其它
            setting_value = new_setting_value;
        }
    } 
    println!("setting is {:?}", setting_value);

    let numbers = (2, 4, 8, 16, 32);

    match numbers {
        (first, _, third, _, fifth) => { // _ match中项的忽略
            println!("Some numbers: {}, {}, {}", first, third, fifth)
        },
    }


    let _x = 5; //下划线开头的未使用变量,去掉未使用变量警告,仍然会绑定值，它可能会获取值的所有权
    
    let s = Some(String::from("Hello!"));
    if let Some(_) = s { //s 没有被移动进 _
        println!("found a string");
    }
    println!("{:?}", s);


    let numbers = (2, 4, 8, 16, 32); 
    match numbers {
        (first, .., last) => {  //.. 忽略剩余值,不能两边都用，歧义，如(.., second, ..)
            println!("Some numbers: {}, {}", first, last);
        },
    }



    let num = Some(4); 
    match num {
        Some(x) if x < 5 => println!("less than five: {}", x), //if条件也要同时成立
        Some(x) => println!("{}", x),
        None => (),
    }
    

    let x = 4;
    let y = false; 
    match x {
        4 | 5 | 6 if y => println!("yes"), //等同于 (4 | 5 | 6) if y => 
        _ => println!("no"),
    }


    enum Message {
        Hello { id: i32 },
    }
    
    let msg = Message::Hello { id: 5 };

    match msg {
        Message::Hello { id: id_variable @ 3..=7 } => { //@ 设置变量保存值,还要求值的范围
            println!("Found an id in range: {}", id_variable)
        },
        Message::Hello { id: 10..=12 } => {
            println!("Found an id in another range")
        },
        Message::Hello { id } => {
            println!("Found some other id: {}", id)
        },
    }

}

fn print_coordinates(&(x, y): &(i32, i32)) { //元组 做函数参数 指定类型
    println!("Current location: ({}, {})", x, y);
}
struct Point {
    x: i32,
    y: i32,
}
fn foo(_: i32, y: i32) { // _ 函数忽略
    println!("This code only uses the y parameter: {}", y);
}
 
//---unsafe
/*
不安全的超能力有： 
    解引用裸指针
    调用不安全的函数或方法
    访问或修改可变静态变量
    实现不安全 trait
    访问 union 的字段 

 */
fn main(){
    let mut num = 5;  
    let r1 = &num as *const i32; //不可变的 裸指针
    let r2 = &mut num as *mut i32; //可变的 裸指针  
    //裸指针不受控于rust的安全检查，使用同C的指针
    unsafe {//必须放在unsafe中仿问内存
        println!("r1 is: {}", *r1);
        println!("r2 is: {}", *r2);
    }
    unsafe {
        dangerous();
    }

    let mut v = vec![1, 2, 3, 4, 5, 6]; 
    let r = &mut v[..]; //..全部切片
    //let (a, b) = r.split_at_mut(3);  //Vec的split_at_mut 函数，在索引为3的位置（1开始），切成两个数组 
    let (a, b) =split_at_mut( r,3);

    assert_eq!(a, &mut [1, 2, 3]);
    assert_eq!(b, &mut [4, 5, 6]);

 
    use std::slice; 
    let address = 0x01234 ;//不安全的
    let r = address as *mut i32; 
    let slice: &[i32] = unsafe {  //无需将 split_at_mut 函数的结果标记为 unsafe
        slice::from_raw_parts_mut(r, 10000)//功能为在指定位置开始的长度，返回一个可变的切片
    };
  
    unsafe {
        println!("Absolute value of -3 according to C: {}", abs(-3));
    }

    
    add_to_count(3); 
    unsafe {
        println!("COUNTER: {}", COUNTER);
    }
}

unsafe fn dangerous() {}


// fn split_at_mut(slice: &mut [i32], mid: usize) -> (&mut [i32], &mut [i32]) {
//     let len = slice.len();

//     assert!(mid <= len);

//      (&mut slice[..mid],   //借用了同一个 slice 两次，Rust安全检查不通过
//       &mut slice[mid..])  
// }


fn split_at_mut(tmpSlice: &mut [i32], mid: usize) -> (&mut [i32], &mut [i32]) {
    let len = tmpSlice.len();
    let ptr = tmpSlice.as_mut_ptr(); //as_mut_ptr() 访问 slice 的裸指针

    assert!(mid <= len);

    use std::slice;
    unsafe {
        (slice::from_raw_parts_mut(ptr, mid),  //from_raw_parts_mut 要裸指针和一个长度来创建一个 slice
         slice::from_raw_parts_mut(ptr.add(mid), len - mid))
    }
}

extern "C" { //调用其它语言
    fn abs(input: i32) -> i32; //C标准库的abs函数
}


#[no_mangle] //不修改函数名，mangle(损坏)
pub extern "C" fn call_from_c() {//从其它语言调用 Rust 函数
    println!("Just called a Rust function from C!");
}
//extern 的使用无需 unsafe。


static mut COUNTER: u32 = 0; //静态变量只能储存拥有 'static 生命周期的引用,访问和修改可变静态变量都是 不安全 的。

fn add_to_count(inc: u32) {
    unsafe {
        COUNTER += inc;
    }
} 
unsafe trait Foo { 
}

unsafe impl Foo for i32 {  //实现也必须标记为 unsafe
} 

//union 和 struct 类似，但是在一个实例中同时只能使用一个声明的字段。
//联合体主要用于和 C 代码中的联合体交互。访问联合体的字段是不安全的，因为 Rust 无法保证当前存储在联合体实例中数据的类型

//-----25 1 rust高级

fn main(){ 
    assert_eq!(Point { x: 1, y: 0 } + Point { x: 2, y: 3 },
               Point { x: 3, y: 3 });  //运算符重载 + 

   let res = Millimeters(3) + Meters(1);
    println!("{}",res.0); 
}
//Iterator源码
pub trait Iterator1 {
    type Item; //type ,这个 trait 的实现者会指定 Item 的具体类型，然而不管实现者指定何种类型, next 方法都会返回一个包含了此具体类型值的 Option

    fn next(&mut self) -> Option<Self::Item>; //大写的Self
}//当 trait 有泛型参数时，可以多次实现这个 trait，每次需改变泛型参数的具体类型,next 方法时，必须提供类型注解来表明希望使用 Iterator 的哪一个实现。


use std::ops::Add; //运算符重载 + 

#[derive(Debug, PartialEq)]
struct Point {
    x: i32,
    y: i32,
}

impl Add for Point {
    type Output = Point;

    fn add(self, other: Point) -> Point {
        Point {
            x: self.x + other.x,
            y: self.y + other.y,
        }
    }
}
 
//Add源码
trait Add1<RHS=Self> {//泛型 默认类型
    type Output;

    fn add(self, rhs: RHS) -> Self::Output;
}
 
struct Millimeters(u32);
struct Meters(u32);

impl Add<Meters> for Millimeters { //泛型 替代默认类型
    type Output = Millimeters;

    fn add(self, other: Meters) -> Millimeters {
        Millimeters(self.0 + (other.0 * 1000))
    }
}
//-----25 2 rust高级 重名

trait Pilot {
    fn fly(&self);
}

trait Wizard {
    fn fly(&self);
}

struct Human;

impl Pilot for Human {
    fn fly(&self) {
        println!("This is your captain speaking.");
    }
}

impl Wizard for Human {
    fn fly(&self) {
        println!("Up!");
    }
}

impl Human {
    fn fly(&self) {
        println!("*waving arms furiously*");
    }
}
fn main() {
    let person = Human;
    person.fly();//用 Human 中的同名方法
    Pilot::fly(&person);
    Wizard::fly(&person); 

    println!("A baby dog is called a {}", Dog::baby_name());
    // println!("A baby dog is called a {}",Animal::baby_name());  //不知道使用哪个同名函数
    println!("A baby dog is called a {}", <Dog as Animal>::baby_name()); //类型转换,调用指定类的同名方法
 
   let p= Point{x:3,y:4};
   p.outline_print();

}
trait Animal {
    fn baby_name() -> String;
}

struct Dog;

impl Dog {
    fn baby_name() -> String {
        String::from("Spot")
    }
}

impl Animal for Dog {
    fn baby_name() -> String {
        String::from("puppy")
    }
}



use std::fmt;

trait OutlinePrint: fmt::Display { //: 类似继承(同c++),或者  类型定义
    fn outline_print(&self) {
        let output = self.to_string();//因有fmt::Display可以to_string(),会调用实现Display的fmt
        let len = output.len();
        println!("{}", "*".repeat(len + 4)); //repeat() 函数
        println!("*{}*", " ".repeat(len + 2));
        println!("* {} *", output);
        println!("*{}*", " ".repeat(len + 2));
        println!("{}", "*".repeat(len + 4));
    }
}

struct Point {
    x: i32,
    y: i32,
}

impl OutlinePrint for Point {} //只实现了一部分，还要实现 fmt::Display

impl fmt::Display for Point {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result { //函数格式 
        write!(f, "({}, {})", self.x, self.y)//write!
    }
}



//孤儿规则（orphan rule） 实现 trait时要求一定要在(crate )本地作用域中(当前包下)。 绕开这个限制的方法是使用 newtype 模式
//缺点是，因为 Wrapper 是一个新类型 ,即代理，Deref另一个方案
struct Wrapper(Vec<String>);

impl fmt::Display for Wrapper {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "[{}]", self.0.join(", "))//self.0来得到
    }
}
//---26 高级
fn main(){
    type Kilometers = i32; //类似C的typedef,同义词（synonym）,用于定义长的类型，减少重复

    let x: i32 = 5;
    let y: Kilometers = 5;

    println!("x + y = {}", x + y);

    //std::io 有这个类型别名声明：
    type MyResult<T> = std::result::Result<T, std::io::Error>;
    // 动态大小类型（dynamically sized types）DST,str 是一个 DST
    // let s1: str = "Hello there!";//不知道实际大小，报错，
    let s1:  &str = "Hello there!";//&str 则是 两个值,str的地址和其长度，在编译时可以知道的大小, Box<str> 或 Rc<str>, &dyn Trait 或 Box<dyn Trait>（Rc<dyn Trait> 也可以 
  

    let answer = do_twice(add_one, 5);
    println!("The answer is: {}", answer);

    //函数指针实现了所有三个闭包 trait（Fn、FnMut 和 FnOnce）
    let list_of_numbers = vec![1, 2, 3];
    let list_of_strings: Vec<String> = list_of_numbers
		.iter()
		.map(|i| i.to_string())//map参数为闭包  
		.collect();
     
    let list_of_numbers = vec![1, 2, 3];
    let list_of_strings: Vec<String> = list_of_numbers
        .iter()
        .map(ToString::to_string)//map参数为函数指针 , :: ,ToString::to_string
        .collect();

     enum Status {
        Value(u32),//元组
        Stop,
    }

    let list_of_statuses: Vec<Status> =
        (0u32..20)
        .map(Status::Value)//元组有map函数
        .collect();
}
fn generic<T>(t: T) { //泛型函数默认只能用于在编译时已知大小的类型
} 
//实际上被当作如下处理：
fn generic1<T: Sized>(t: T) {//是 Sized trait  在编译时就知道大小的类型实现。另外，Rust 隐式的为每一个泛型函数增加了 Sized bound
}


//?Sized ,T 可能是也可能不是 Sized 的”
fn generic2<T: ?Sized>(t: &T) {  
}

fn add_one(x: i32) -> i32 {
    x + 1
} 
fn do_twice(f: fn(i32) -> i32, arg: i32) -> i32 { //fn 被称为 函数指针（function pointer）, Fn 闭包 
    f(arg) + f(arg)
}

//返回闭包 放在 Box中 ，dyn约束类型, 大写Fn ,如没有dyn 可以用小写fn, dyn后只可trait,即大写Fn
fn returns_closure() -> Box<dyn Fn(i32) -> i32> {
    Box::new(|x| x + 1)
}

//-----------------27 自定义 derive 宏
hello_macro 项目下有一个子项目 hello_macro_derive
hello_macro 项目 的同级入口项目  demo


//---xxx\hello_macro\hello_macro_derive\src\lib.rs
//过程宏(proc-macro)
extern crate proc_macro; //Rust 自带 proc_macro crate，因此无需将其加到 Cargo.toml 文件的依赖中

use crate::proc_macro::TokenStream;
use quote::quote;
use syn;

#[proc_macro_derive(HelloMacro)]//当用户在一个类型上指定 #[derive(HelloMacro)] 时，hello_macro_derive 函数将会被调用
pub fn hello_macro_derive(input: TokenStream) -> TokenStream {//几乎在所有你能看到或创建的过程宏 crate 中都一样
    // 将 Rust 代码解析为语法树以便进行操作
    let ast = syn::parse(input).unwrap(); //unwrap,在生产代码中，则应该通过 panic! 或 expect 来

    // 构建 trait 实现
    impl_hello_macro(&ast)
}

fn impl_hello_macro(ast: &syn::DeriveInput) -> TokenStream { //依过程宏的目的而各有不同
    let name = &ast.ident; //ident（ identifier，表示名字）
    let gen = quote! { //
        impl HelloMacro for #name { //动态实现
            fn hello_macro() {
                println!("Hello, Macro! My name is {}", stringify!(#name));//stringify! 表达式转换为一个字符串常量
            }
        }
    };
    gen.into() //
}

/*
//Cargo.toml中有增加

[lib]
proc-macro = true

[dependencies]
syn = "1.0"
quote = "1.0"
 */


//---xxx\hello_macro\src\lib.rs
pub trait HelloMacro {
    fn hello_macro();
}

//---xxx\demo\src\main.rs
/*
use hello_macro::HelloMacro;
struct Pancakes;
impl HelloMacro for Pancakes {
    fn hello_macro() {
        println!("Hello, Macro! My name is Pancakes!");//Pancakes名字想要动态的，Rust 没有反射的能力
    }
}
下面的 #[derive(HelloMacro)]实现了上面的功能
*/

use hello_macro::HelloMacro;
use hello_macro_derive::HelloMacro;

#[derive(HelloMacro)]
struct Pancakes; 

fn main() {
    Pancakes::hello_macro(); //Hello, Macro! My name is Pancakes
}
  
//---xxx\demo\Cargo.toml
[dependencies]
hello_macro = { path = "../hello_macro" }
hello_macro_derive = { path = "../hello_macro/hello_macro_derive" }

//----------------- tcp server
use std::net::TcpListener;
use std::net::TcpStream;
use std::io::prelude::*;

fn main() {
    let listener = TcpListener::bind("127.0.0.1:7878").unwrap();

    for stream in listener.incoming() {
        let stream = stream.unwrap();

        println!("Connection established!");// 为了只打印一次这个，上面的for只循环一次
        for stream in listener.incoming() {//返回 Incoming 实现了 Iterator ,可以 for循环 ,内部类型为 type Item = Result<TcpStream, Error>
            let stream = stream.unwrap();

            handle_connection(stream);
        }
    }
}
fn handle_connection(mut stream: TcpStream) {
    let mut buffer = [0; 1024];

    stream.read(&mut buffer).unwrap();//返回Result<usize> 

    let response = "HTTP/1.1 200 OK\r\n\r\n";

    stream.write(response.as_bytes()).unwrap();
    stream.flush().unwrap();
}




