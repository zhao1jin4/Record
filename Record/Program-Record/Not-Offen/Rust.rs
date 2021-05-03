Rust 语言 最初是 Mozilla 公司开发的 

可替代C/C++
多处理器支持好，并发好
没有拉圾回收
擅长 WebAssembly ，Firefox浏览器
 
 
--rust linux 就可以不用安装gcc
下载提示  x86_64-unknown-linux-gnu  #可下载 rust-1.50.0-x86_64-unknown-linux-gnu.tar.gz
提示执行 source $HOME/.cargo/env    #有$HOME/.cargo/bin 目录 放PATH环境变量中
 
--rust windows 依赖VC++   (也是失败的)
stup-init.exe 提示Rust requires the Microsoft C++ build tools for Visual Studio 2013 or later 

https://visualstudio.microsoft.com/visual-cpp-build-tools/ 下载 vs_buildtools_xxx.exe  大项 选  C++ build tools  
	最新的是VS2019的,子项默认选中多个提示大小为6.86GB，只选中MSVCv142(VS 2019 C++)提示大小为4GB,下载中提示大小为1G
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
	 
https://forge.rust-lang.org/infra/other-installation-methods.html 中下载 rust-1.51.0-x86_64-pc-windows-msvc.msi
虽然有rustc cargo 命令，但运行时还是依赖VC++ 工具包


使用rustup-init.exe安装 默认 %USERPROFILE%\.cargo 在bin下有多的rustup命令, 可用 CARGO_HOME 来修改 

---visual studio code 的Rust扩展 preview
目前不能打断点，Settings->搜索break, Debug:Allow Breakpoints Everywhere 做选中
要 Run and Debug按钮 (或者  Run-> Start Debug ) -> windows系统 选择C++(Windows) 生成 launch.json
修改了 "program": "${workspaceFolder}/target/debug/${workspaceFolderBasename}",
cargo建立项目 调试测试成功，但如修改文件，要手工编译才生效
launch.json 手工增加 "preLaunchTask": "rust_cargo_build", 
preLaunchTask对应的值 rust_cargo_build 就是在tasks.json里的label字段,可复制 C/C++: g++.exe build active file 生成
--tasks.json
{
    "tasks": [
        {
            "type": "cppbuild",
            "label": "rust_cargo_build",
            "command": "cargo",
            "args": [
                "build"
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

--最终 launch.json (windows VC)
{ 
    "version": "0.2.0",
    "configurations": [
        {
            "name": "win rust cargo",
            "type": "cppvsdbg",
            "request": "launch",
            "program": "${workspaceFolder}/target/debug/${workspaceFolderBasename}",
            "args": [],
            "stopAtEntry": false,
            "cwd": "${workspaceFolder}",
            "environment": [], 
            "preLaunchTask": "rust_cargo_build", 
            "externalConsole": false
        }
    ]
}

cargo项目，修改文件,debug 测试成功
------rustc 方式 可运行，测试debug也成功
---launch.json
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
            "type": "cppbuild",
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

---Intellij Idea 插件 Rust 

https://plugins.jetbrains.com/plugin/8182-rust/
0.3.144.3766-211 			 2021.1   		Mar 29, 2021 
	又依赖于toml
	https://plugins.jetbrains.com/plugin/8195-toml

Settings->Rust-> Standard library 中设置rust主目录
可以运行(使用 cargo)，Community版本 不能打断点

https://plugins.jetbrains.com/plugin/8182-rust/docs/rust-debugging.html  
中说只可Intellij Idea的 Ulimated版本, GoLand,CLion,PyCharm Pro才行


---main.rs
fn main(){
 println!("hello");
}
--- 
编译用 rustc main.rs 
rustc src/hello.rs --out-dir target  -o target/hello.exe -g
 
 
构建工具 cargo
建立项目  cargo new myproject 会生成 Cargo.toml文件， 依赖管理 ,像maven
代码的包/库 叫 crate
构建用 cargo build  会生成 Cargo.lock 文件，像npm，生成在target/debug 目录中
构建和运行 cargo run
cargo check 比 cargo build快，检查代码编译是否正确 

cargo build --release 生成在target/release目录中



也有指针的，类似C++


C++23没有反射
Rust只能对 'static 生命周期的变量（常量）进行反射！ 只能被用作类型推断 (Any)







