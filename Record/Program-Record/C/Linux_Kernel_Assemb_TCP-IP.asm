--------------------------汇编语言

CPU和内存/南桥蕊片/PCI插槽，之前通讯的线 总线

CPU去找程序在内存中的位置通过　地址总线 
内存把数据通过　数据总线　传给CPU
控制总线　CPU用来控制外围设备如硬盘，光驱，声卡

每一根总可以传送，高电频，低电频，即０和１

8086的CPU　有20根CPU连接到内存的地址总线，就是说CPU对内存的最大寻址范围是2的20次方=1M  (地址总线)

CPU把要找内存中的地址传给内存，内存中有一个内存控制器能识别CPU发来的地址，找到对应的数据后通过数据总线传给CPU

控制总线，CPU对内存是读还是写


总线的宽度， CPU有多少根连接到内存  ，32位CP，cpu的内存寻找范围是2的32次方＝4G


CPU从内存中拿到数据后，先把它放入寄存器，以后再执行
寄存器存放数据或者指令，分为通用寄存器，段寄存器


8086寄存器AX 16位 通用寄存器，专用来存放数据 前8位AH(高) ，后8位AL(低位)
字节 8个二进制(位)
字 用两个字节
双字 两个字

CPU的组成
1.寄存器
2.运算器
3.控制器 发送命令
4.内部总线 连接各种器件


地址加法器 用来解决 16位CPU最多一次只能专16个二进制数,而有20的数据总线,把数据总线占满
	(20位用来存两个16位, 可能是 第一个*10变成20位,+第二个数)
		第一个叫基地址 第二个叫偏移地址 
		基地址:偏移地址=实际内存地址
		基地址*16+偏移地址=实际内存地址    16进制




windows 下debug命令
-? 帮助
-d (dump) 查看物理内存的信息
第一列就是物理内存的表示方法  基地址:偏移地址

通用寄存器用来存放数据 
段寄存器专门存放基地址  (段地址)


CS(code segment)寄存器存放要执行程序代码段(CPU要执行代码的基地址)
IP寄存器保存了要执行程序代码的偏移地址  ,也叫指令指针(Instruction point)寄存器

CS*16+IP 


debug命令(实模式)
-r  (register)命令 查看和改变各个寄存器的内容
显示有CS和IP的值 ,就是CPU要执行的第一条指令的地址

-d命令查看内存地址的内容,不加参数默认是CS:IP的地址内容
第一列是地址  第二列是16位进显示值,第三列是ASCII的方式显示值
例如 -d ss:0000 查看ss寄存器,ss的基地址,0000偏移地址

-u (unassemble) 将内存的二进制,显示成汇编语言
不加参数默认对CS:IP的地址操作
例如 -u cs:0100  CPU要执行的第一个指令
第一列是地址  第二列是16位进显示值 第三,四列是汇编


-a (assemble) 以汇编向内存写入 [address] ,默认是CS:IP的值，可写自己的代码
//修改内存,最好在虚拟机中做
例如 -a cs:0100 回车,再输入命令 mov ax,1234	//这是16进制数,不用加h，
对ax寄存器赋值,再回车,就退出修改 , -u cs:0100再看修改的值


-r 
-r ip 修改IP的值

-t  (trace)能够跟踪汇编语言的执行 ,
	每次执行后IP的值加2或3(根据要执行代码所占空间),用u来看,显示指令是CS:IP的最顶值
	
-q (quit)


ds数据段,存放代码的全局变量 
cs代码段,
ss堆栈段,存放代码的局部变量 

DS(Data Segment) 寄存器, 仿问物理内存的地址

DS:[0]  //0是偏移地址  ,
DS:[BX] //BX是通用寄存器,把BX的值当偏移地址,
都表示内存的地址
 
mov ds:[13ABH],1234H  //把16进制的1234 赋给物理内存地址是ds *16+13AB指向的数据区
mov [13ABH],1234H     //可以省略ds: ,默认是ds

ss,sp 寄存器 ss *16+sp指向的就是栈的地址,(先进后出)
ss:存放栈的基地址,sp存放栈顶的偏移地址
ss:sp始终指向栈顶
push入栈指令 栈底地址是最大的 sp=sp-2,栈顶是数据的高字节
pop ax出栈指令 ,赋给ax寄存器

可以使用 debug -a 写代码，-t 单步执行，观察SP,栈顶的值始终是空的(开始时栈顶／栈底都是空的)，push时先放值，再减SP

ax 累加器,bx 基地寄存器,cx计数器,dx数据或地址寄存器	 通用寄存器(数据寄存器) 
(高8位用ah,bh,ch,dh.低位al,bl,cl,dl)

bp 基地址寄存器


8086不知道栈是否到边界,要自己维护,没有一个寄存器专门存放栈顶,只有一个寄存器专门存放栈底BP

assume (假定)  关键字,来定义段 和 segment ....ends (相当于C的{})关联


db (define byte) 指令定义字节内容
msg db "hello world"  汇编中"" 和 ' ' 是一样的 ,变量名msg的值是"hello world"

CPU的特定的空间存主存储器地址空间(RAM),显存地址空间 ,只要把要显示的东西复制显存地址空间中,显卡会自动去显示的
8086的pc机 00000-9FFFF 是主存储器的地址空间
	    A0000-BFFFF 是显存地址空间
	    C0000-FFFFF 是各类ROM地址空间
SVGA显示模式
VGA显示模式的段地址是 B8000H-BFFFFH (2的15次方 -1)共32K,的空间为80*25的彩色字符 

字体/颜色  颜色跟在字符后8位

   7		6 5 4	  3	      2	1 0
BL(闪烁) 背景色R G B	I高亮	前景色R G B
红底绿字用 01000010B来表示

共32K,显示缓存共8页，每页4kb(4000D)   80*25　(25行,80列) 每字占两位（字＿颜色）
即25*80*2=4000
B8000H-B8F9FH 地址空间第一页

#B8后是两个０？
B800H 是段，不能放在cs,ds,ss中要用
es 扩展段寄存器,用存放仿问内存的段地址,如果一个十六进制数的第一是字母,必须在前加0
  不能把一个常量直接赋给段寄存器
  0b800h不能直接赋给es,要先给通用寄存器ax,bx,cx,dx再给es
　
loop循环,循环的次数由cx寄存器的值来决定，计数，每做一次loop对CX减１
标号:
   指令 ...
loop 标号

没有加h 表示十进制,,b表示二进制

数据段地址在ds寄存器中 ,ds:[0] 表示得到ds中第一个字节内容

si寄存器 也是 通用寄存器,循环增..

mov es:[bx] ,ds[si]//不支持,只知道起始地址,不知道结束地址

mov ax,ds:[si] //ax是16位 所以会把ds:[si]值的前16位都复制过去


ah,al 分别是ax的高8位和低8位
bh,bl 分别是bx的高8位和低8位
ch,cl 分别是cx的高8位和低8位
dh,dl 分别是dax的高8位和低8位

二进制数后加b

inc si 表示si每次加+1
add bx,2 加指令相当于C的 bx=bx+2

把标记ds的寄存器的data 给ds,告诉CPU,DS 的地址

CPU会自动获得代码段的基地址,偏移地址要使用start : CPU给IP寄存器赋于第一条指令的地址

;可以在MASM中成功运行
assume cs:code,ds:data
data segment 
	msg db "hello world"
data ends

code segment 
	start:	   ;start是关键字,也可以是begin,也可是b,是个别名
	mov ax,data
	mov ds,ax  ;把标记ds的寄存器的data 给ds  
	
	mov bx,0b800h ;不能把一个常数直接给段寄存器es
	mov es ,bx
	
	mov cx,0Bh	;循环数
	mov si,0	;字串的下标			
	mov bx,0	;是es下标		
	mov ah,01000010b ;红底绿字
	s:mov al,[si]	;省了ds:,完整的是si:[si]
	mov es:[bx],al
	mov es:[bx+1],ah
	inc si
	add bx,2
	loop s

	mov ax,4c00h;中断,暂停
	int 21h
code ends
	end start;偏移地址,IP开始执行位置



２.
开机按del产生中断信号,跳到设置好中断程序执行,执行后又回到原来产生中断处继续执行

中断向量表, 第一列存中断编号,第二列存中断程序的地址,
中断向量表一般保存在内存的 0000:0000 - 0000:03FE 空间内 ,256个中断记录,
每个中断表项占用2个字(4个字节),高位存段地址(后2个),低存偏移地址(前2个)  


产生中断时CPU先把 cs,ip存放在栈中,中断程序结束后,通过出栈指令重新得到cs,ip,CPU自动完成

0号中断表示除法溢出错误 

以下产生0号中断,是第一个中断
mov ax,1000h
mov bh ,1
div bh     #除法指令 AX/bh 商给 AL,余数给 AH ,因结果是16位,存8位的AL,会溢出错误,0中断

格式 :div 除数 ;可是寄存器,也可段地址和偏移地址

如除数为8位,被除数为16位,被除数默认放在ax
如除数为16位,被除数为32位,被除数默认放在ax(存低16位),dx(存高16位)中


如除数为8位,AL存除法的商,AH存除法的余数
如除数为16位,AX存除法的商,DX存除法的余数

0000:0000开始的4个字节中 是 0号中断的处理程序的地址
mov ds:[0],中断程序的偏移地址
mov ds:[2],中断程序的段地址

中断程序存放位置必须任何时候不能被其它程序覆盖,中断随时可能产生,
地址段 0000:0200 - 0000:0300 正常情况下没有其它程序使用,在中断向量表内部

debug 
-d 0000:0200后发现都是0
代码段中定义 db "xx"数据内容,没有放在数据段中, db 不能被执行,要跳过

;MASM可成功运行,一定要注意中文的空格
assume cs:code
code segment
	start:
	mov ax,0		;基地址,ax是16位的不用指定word ptr
	mov ds,ax
	mov word ptr ds:[0],0200h  ;设置中向量表中的0号中断程序位置,常数没有指定复制几个字节,word ptr指定复制2个字节
	mov word ptr ds:[2],0
	;第三部
	mov ax,cs	;cs给ds ,0号中断不区分代码段和数据段,它们指向同一地址
	mov ds ,ax
	mov si ,offset int0   ;把int0标记的偏移地址给si ,把代码复制到正确位置,复制的源
	mov ax,0
	mov es,ax
	mov di,200h	;复制　中断处理程序　到 200h
	mov cx,offset int0end-offset int0 ;计数,REP使用CX
	cld	;clear DF 
	rep movsb 
	;产生0号中断
	mov ax,1000h
	mov bh ,1
	div bh
	
	mov ax,4c00h  ;中断，暂停
	int 21h

	;自己的中断处理程序
	int0:jmp short int0start  ;  :后代码占两个字节,int0start的地址是0200h,开始地址
		db "i am teacher" ;i是从 0202h开始
	int0start:mov ax,0b800h
		  mov es,ax	;显存首地址放es中
	mov ax,cs	;cs 给ds ,0号中断不区分cs还是ds,它们在一起
	mov ds,ax	
	;在屏幕中心显示字
	mov si ,202h     ;"i"的地址是0202h开始
	mov di ,12*160+36*2	;di 是通用寄存器,(25行,80列)的中央是 12行,每行80字符,每字符两字表示(80*2=160)
				;前12行要的字节是12*160,列中间40偏左一点,到36 ,每个2字节,显存起始地址(屏幕中心)
	mov cx ,12	;12个字符,循环12次
	s:mov al,ds:[si]	;可缩写[si]
	mov es:[di],al  ;es:[di]显存地址
	inc si
	add di,2  ;使用默认字体
	loop s

	mov ax,4c00h   ;中断，暂停
	int 21h

	int0end:nop  ;No Operation
code ends
	end start







jmp 指令用来跳转 ,中断代码段中的db 执行时 要跳过

jmp far　 标号	段间跳转，会修改cs,ip寄存器值变为标号所在的地址
jmp near　标号	段内跳转，只修改ip寄存器　#8086的CPU,段最大值2的16次方
jmp short 标号	段内短跳转，不修改cs和ip,自动根据标号计算跳转距离，距离不能大于256字节


movsb (string byte/word)把一段汇编语句(字符串) 复制到指定的
	DS:SI 指向的是 要复制的字串,
	ES:DI 指向目的地址
	方向是 DF 的值 ,0表示 SI 和 DI+ 1/2. 1表示减
	CLD 把 DF 清0 +
	STD 把 DF 设1 -
	
	CLD 指令 复制方向从低到高,SI,DI加1
	STD 指令 复制方向从高到低,SI,DI减1

REP movsb ;rep 次数,使用CX的值

offset 标记　　　;得到标记的偏移地址

?????????????????????????????不知什么意思
mov ax,4c00h   ;中断，暂停
int 21h
nop　
?????????????????????????????　

３.引导程序

Visual PC2004　微软的虚拟机
开机时发电信号给BIOS ，BIOS启动 自检程序来检查周边设备是否通电,来到BIOS,引导驱动器中的程序

BIOS对内存分配1M内存

0x00000-0x003FF		中断向量表
0x00400-0x004FF		BIOS数据区
0x00500-0x07BFF		自由内存区
0x07C00-0x07DFF		引导程序加载区  512B
0x07E00-0x9FFFF		自由内存区
0xA0000-0xBFFFF		显示内存区
0xC0000-0xFFFFF		中断处理程序

---------------NASM的汇编程序
BIOS启动磁盘(0磁头,0磁道,1扇区)第一个扇区512字节到内存,放内存的0x0000:0x07c00
如果第一扇区最后两个字节是55AA,那么它是引导程序

masm 微软的汇编器
nasm 是linux上的

Nasm 对内存 存取必须在 地址加上方括号
nasm中 mov ax,bar  把bar的地址给ax,相当于masm中mov,offset bar
nasm中 mov ax,[bar] 是 bar的值给

masm中用 mov ax,es:di 
nasm中用 mov ax,[es:di]

vi hello.asm

nasm boot.asm  -o boot.bin   在安装光盘中有nasm 的rpm包
ndisasm boot.bin 反汇编器 

org 07c00 ;汇编不知道引导程序放在0x0000:0x07c00的内存 ,org指令来告诉汇编,这个程序的开始位置
	;段内偏移地址,下面的程序在07c00的地址上

int 指令后加BOIS中断指令
int 10h ;专门用来显示字符串的功能
	会读取ah 的值 ,决定调用哪个子程序

Masm 开发工具双击左则 BIOS功能调用表,查看AH为13显示字符串功能
看帮助 ES:BP=串首地址,CX=串长度
	DH:DL 起始行号:列号
	BH=0 显示页号
	AL=1串结构,BL=属性

nasm中
$ 表示当前指令偏移地址
$$ 表示指令所在的开始地址

$-$$ 就是汇编代码的长度 ,总长512-最后2个

times 次数 指令 (nasm中)

;;;;;;;;;;;;;;;;;;;;;;;;;;
org 07c00h ;汇编不知道引导程序放在0x0000:0x07c00的内存 ,org指令来告诉汇编
;屏幕中央显示字符串
mov ax,cs
mov es,ax  ;ES:BP=串首地址的ES

mov bp,msgstr;把msgstr的地址给,BP
;ES:BP 是要显示的字串地址,看帮助

mov cx,12 ;cx字串长
mov dh,12;DH;DL 起始行号:列号
mov dl,36
mov bh,0 ;显示页号
mov al,1 ;AL=1,BL=属性
mov bl,0ch;AL=1,BL=0ch,二进制1100,红字,黑底?????
mov ah,13h;13h子程序
int 10h ;用来显示字符串的功能
msgstr: db "hello my os!";有 :号12个

;写512最后两字节为55AA
times  510-($-$$) db 0 ;是要510-$-$$填充为0,times是做多少次,()不能省
dw 55aah ;写数据,或者用0x55aa

jmp $;死循环,不断跳到当前指令
;;;;;;;;;;;;;;;;;;;;;;;;;;
nasm boot.asm  -o boot.bin编译

虚拟机的linux系统可以仿问本机的硬盘,SAMB???


WnImage制作软盘镜像 新建一个空白的,保存为virtual_floppy.vfd(virtual floppy Image)

写C程序
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <stdio.h>
int main(int argc,char * argv[])
{
	int fd_source;
	int fd_dest;
	int read_count=0;
	char buffer[512];

	if(argc <3)
	{
		printf("usage write_image from_asm_bin to_virtual_floppy.vfd\n");
		return 0;
	}
	fd_source=open(argv[1],O_RDONLY);
	if(fd_source<0)
	{
		perror("open argv[1] error:");
		return 0;
	}
	fd_dest=open(argv[2],O_WRONLY);
	if(fd_dest<0)
	{
		perror("open argv[2] error:");
		return 0;
	}
	while((read_count=read(fd_source,buffer,512))>0)
	{
		write(fd_dest,buffer,read_count);
		memset(buffer,0,512);
	}
	printf("write image ok");
	return 0;

}

编译C,执行, 用UltraEdit 来验证生成的文件,有AA55的标记
在Virtual PC中双击一个系统->floppy -> Capture floppy disk image->选择virtual_floppy.vfd文件->
action->reset重启


4.保护模式 (32位兼容16位)

CPU默认加电后属于real-model(实模式),只能仿问1M以下的内存,称为常规内存,1M以上称扩展内存
CPU对内存的最大寻址范围是2的20次方=1M (之前的都是实模式)

Intel 32位CPU的80386 32根地址线,2的32次方是4GB

16位使用 16位的段地址和16位的偏移地址,表示20位地址

32位CPU为了和16位兼容,有保护模式,
这时原来的16位段寄存器,并不用来表示原来的段地址,而是表示 一表的索引号 ,这张表中记录 索引号 和 32位地址的大小

保存表中索引的段寄存器   称为 段选择子
表中每个表示32位内存信息 称为 段描述符
整张表			称为 段描述表


段选择子的高13位(3-15)表示表的索引,低3位(0-3)表示段描述符的属性 ,段描述符最多2的13次方=8192 (8K)
	 第3位是TI(Table Indicator) 表示从全局描述符表中读,还是从局部描述符表中读
		局部描述符表 表示进程自己的数据
		全局描述符表 表示操作系统提供的共享的
	 第0-2位RPL (Request Priviledge 特权 Level)  用于特权检查
 
段描述符保存32地址的开始地址和段长度(段界限)
在CPU 没有分页时 　线性地址空间(是一维排列)就是物理空间,

段描述符共有8个字节

	段界限(segment limit) 共20位,前16位在第1,2字节中,后4位在第7字节的低4位 
	段的基地址 共32位,前23个位存放在3到5字节中,其它高位放在第8字节中 
	段属性 在段描述符中第6,7字节中 ,第7字节的低4位存段界限,是数据段还是代码段,可读,
		
		从右到左
		段属性的低0-3位 是TYPE,表示是数据段,代码段还是堆栈段，(如是数据段可读可写是0010,如是代码段可读可写是1010)
		段属性的低4位 DT 是系统段,存储段,一般是存储段,(1表示存储段)
		段属性的低5-6位DPL 特权级别,系统内存的仿问权限,最高0,最低3
		段属性的低7 P  表示对地址的转换是否有效,1有效,0无效
		段属性的高(第二个字节)0-3位  Limit 存段界限第二部分高位
		段属性的高(第二个字节4位 AVL Intel保留位,0值,只要后面CPU向前兼容这个值一定是0
		段属性的高(第二个字节5位 没有使用 0值
		段属性的高(第二个字节6位 D 如是代码段 0表示16位,1表示32位.如是数据段,0表示16位地址,1表示32位地址
		段属性的高(第二个字节7位 G 段界限粒度,0表示段边界64k,1表示段边界是4GB,保护模式使用1,实模式使用0


我们示例使用段界限8M,1024*1024*8=二进制23位表示,8M=800000H

段界限只有20位不能写入段描述符  要用段界限公式
段界限=limit*4k+0FFFH
 因此有如下计算 4k=4*1024=2的12次方=1000H
800000H=limit*4k+0FFFh 计算limit=7FFH 

0-3位的TYPE如是数据段可读可写是0010   ,如是代码段可读可写是1010
	
80386 32位机,48位寄存器,称之为GDT 全局描述符,前16位保存段描述符表的大小,后32位保存段描述符表的开始地址

lgdt 段描述符表的地址   指令

IO端口 0x92 开

CPU 和设备(硬盘,显示,内存)交互数据必须有一寄存器 来中转,每个设备的寄存器编号,称 端口

in accum port  ;in指令读取设备寄存中的内容,把十六进制port端口内容存到accum中，accum必须是AL,AX

out  port accum　;写数据accum可以是其它的
--
in al,92h	;92端口入写
or al,00000010b ;开启A20地址,32位的只要后20位,和前兼容,or 是相与??? ,C的&,
out 92h,al
--

BIOS创建的中断向量表,实模式转保护模式时,必须废除原来的中断向量表(16位)
使用 cli 指令,必须重新建立32位中断向量表和中断处理程序


80386新加4个32位寄存器  CR0-CR3
CR0 的某位  告诉CPU进入保护模式
CR1 保留,没有使用
CR2 ,CR3 用于分页机制

CR0 的第0位 PE 为0 是实模式,1是保护模式
CR0 的最后一位第31位 PG 0表示禁用分页机制,1表示启用分页

不可以PG=1,PE=0,非法的,实模式不能分页

;开启保护模式
mov eax,cr0 ;//eax 是32位扩展寄存器
or eax,1	;或操作
mov cr0,eax

保护模式的段地址是段描述符中的索引
jmp gdt_code_addr:0	;gdt_code_addr 叫 [段选择子]


[BITS 32] ;后面的32位,默认是16位,保护模式的内存是32位的

显示helloworld必须在表中加显存地址


dd 占4个字节 define double word
dw 两相字节 define word
db 1个字节 define byte

下面是完成代码
;;;;;;;;;;;;;;;;;;;;;;;;;;
[BITS 16]
org 07c00h ;汇编不知道引导程序放在0x0000:0x07c00的内存 ,org指令来告诉汇编

jmp main   ;//描述符不能被执行
gdt_table_start:    ;后面是定段描述符,开始地址描述
	gdt_null:		
		dd 0h	;数据段,intel规定,描述符表的第一个描述符必须填充为0是
		dd 0h
	gdt_data_addr equ $-gdt_table_start ;数据段开始地址,equ相当于C的= 赋值
	gdt_data:
		dw	07FFh	;8M段界限  ,dw占两字节,
		dw	0h	;段基地址0-18位 
		db	0h	;段基地址19-23位
		db	10010010b;段述符的第6个字节属性(数据段可读可写)
		db	11000000b;段述符的第7个字节属性
		db	0	;段述符的最后一个字节,即基地址第二部分
	gdt_video_addr equ $-gdt_table_start	
	gdt_video:			
		dw	0ffH		;1M段界限
		dw	8000h		;显存地址B8000H
		db	0Bh
		db	10010010b	; 和数据段一样
		db	11000000b
		db	0
	gdt_code_addr equ $-gdt_table_start
	gdt_code:
		dw	07FFh
		dw	1h	;段基地址0-18位 	
		db	80h	;段基地址19-23位
		db	10011010b;段属性,代码段
		db	11000000b
		db	0
gdt_table_end:

gdtr_addr:
	dw gdt_table_end - gdt_table_start - 1	 ;0开始 ,段描述符的长度 (,两个字节,一个字
	dd gdt_table_start	;段描述符的基地址,(两个字,4个字节
	lgdt [gdtr_addr]

main:
	
	xor eax,eax  ;清空
	add eax,data_32 ;data_32是标记,eax的低位是ax,ax又有ah,al
	mov word [gdt_data+2],ax ;word 16位,+2是因第3个字节存基地址
	shr eax,16 ;shr 是右移指令
	mov byte [gdt_data+4],al ;第5位基地址
	mov byte [gdt_data+7],ah ;第8位

	;代码段
	xor eax,eax  
	add eax,code_32 
	mov word [gdt_code+2],ax
	shr eax,16 
	mov byte [gdt_code+4],al 
	mov byte [gdt_code+7],ah 

	lgdt [gdtr_addr];读取它的值
enable_a20:
	in al,92h
	or al,00000010b ;开启A20地址,32位的只要后20位,和前兼容
	out 92h,al

	cli ;废除原来的中断向量表
	

;开启保护模式
mov eax,cr0	;eax 是32位扩展寄存器
or eax,1	;或操作
mov cr0,eax
;跳到保护模式
jmp gdt_code_addr:0	;gdt_code_addr 叫 [段选择子]

[BITS 32] ;后面的32位,默认是16位,保护模式的内存是32位的

data_32:
	db "hello world"
code_32:
	;屏幕中央显示字符串
	mov ax,gdt_data_addr
	mov ds,ax

	mov ax,gdt_video_addr
	mov gs,ax
	mov cx,11 ;cx字串长
	mov edi,(80*10+12)*2  ;edi 是32位的di, 80*25,中央显示
	mov bx,0
	mov ah,0ch;黑底红字
	s:mov al,[ds:bx]
	mov [gs:edi],al
	mov [gs:edi+1],ah
	inc bx
	add edi,2
	loop s
jmp $;死循环,不断跳到当前指令
;写512最后两字节为55AA
times  510-($-$$) db 0 ;是要510-$-$$填充为0,times是做多少次,()不能省
dw 55aah  ;或者用0x55aa


;;;;;;;;;;;;;;;;;;;;;;;;;;以下是OK的 用UltraISO修建空img文件
[bits 16]
org 07c00h
jmp main
gdt_table_start:
    gdt_null:
        dd 0h
        dd 0h
    gdt_data_addr  equ $-gdt_table_start
    gdt_data:
        dw 07ffh
        dw 0h
        db 0h
        db 10010010b
        db 11000000b
        db 0
    gdt_video_addr  equ $-gdt_table_start
    gdt_video:
        dw 0ffh
        dw 8000h    
        db 0BH
        db 10010010b
        db 11000000b
        db 0
    gdt_code_addr  equ $-gdt_table_start
    gdt_code:
        dw 07ffh
        dw 1h
        db 80h
        db 10011010b
        db 11000000b
        db 0
gdt_table_end:
    gdtr_addr:
        dw  gdt_table_end-gdt_table_start-1
        dd  gdt_table_start
    lgdt [gdtr_addr]
    
main:   
        mov ax,cs
        mov ds,ax

        xor eax,eax
	mov eax,code_32
	mov word [gdt_code+2],ax
	shr eax,16
	mov byte [gdt_code+4],al
	mov byte [gdt_code+7],ah
	
	xor eax,eax
	mov eax,data_32
	mov word [gdt_data+2],ax
	shr eax,16
	mov byte [gdt_data+4],al
	mov byte [gdt_data+7],ah
	
	lgdt   [gdtr_addr]
	
enable_a20:
    in  al,92h
    or  al,10b
    out 92h,al
    
cli

mov eax, cr0
or  eax, 1
mov cr0, eax

jmp dword gdt_code_addr:0
[bits 32]
data_32:
    db "Hello world"
code_32:
    mov ax, gdt_data_addr
    mov ds,ax
    mov ax, gdt_video_addr
    mov gs,ax
    mov cx,11
    mov edi,(80*10+12)*2
    mov bx,0
    mov ah,0ch
s:  mov al,[ds:bx]
    mov [gs:edi],al
    mov [gs:edi+1],ah
    inc bx
    add edi,2
    loop s
    jmp $
    
    times 510-($-$$) db 0
    dw 0aa55h
;;;;;;;;;;;;;;;;;;;;;;;;;;





bochs的bochsdbg.exe是调试用的
写run.bat文件放在dlxlinux(单独下)中
cd "C:\Program Files\Bochs-2.4.1\dlxlinux"
..\bochs -q -f bochsrc.txt
双击run.bat就可以启动了

复制dlxlinux目录　改cd目录
改　floppya: 1_44=floppya.img, status=inserted 的　floppya.img　为自己做的img
		(用bximage　-fd命令,创建的也不认识啊????????)
改　boot: c　为boot: a


调试用改用　bochsdbg
调试命令 c  (continue),s(step),vb(vbreak)虚拟加断点 vb 段地址:偏移地址
	b (pbreak) 物理地址加断点,(lb)lbreak线性址 ,disassemble反汇编


b 0x07c00 ;BIOS读数据放到内存的地址
info b ;显示断点信息,Enb表示开启
disassemble 0x7c29 0x7c3c ;0x7c29是开始地址,看显示的,到xx结束


eax寄存器是32位的,扩展ax
xor eax ,eax ;清空eax


loopw指令 使用CX 
loopd指令 使用ECX

JA/JNBE , JG/JNLE (多一个SF符号位)

TEST 指令

call far 要把cs:ip 放入栈





清BIOS密码
Debug命令-o（输出）将字节值发送到输出端口。 o port byte-value 参数 port 通过地址指定输出端口。端口地址可以是 16 位值
在Windows下调用debug -o 70 11  -o 71 11 -q 重启机器 清BIOS密码
方法一
-o 70 16
-o 71 16
-q
方法二
-o 70 11
-o 71 ff
-q




-----------------linux内核__嵌入式arm

汇编中head.s bootsec.s  用的s86和intel类似
内核中最多的汇编 AT & T 系统 与Intel有区别


AT & T 语法

mov %eax, %ebx   ####Intel不用加% 
		表示把eax放入到ebx中,第一个是源, Intel则有第二是原,把ebx放入到eax中,

mov $4 ,%ebx  ####立即数前加$,否则是内存地址  ,Intel数前不用加$,
mov value ,%ebx ##
mov $value ,%ebx ##把value的地址放入ebx中	,Intel地址不用加$,

b(byte) 8位
w(word) 16位
l(long) 32位


movw %ax,%bx  ##16位     ,Intel 没有w


---------------------32位汇编　　 

pushad 把寄存器从EAX 到 EIP　，存入栈中

FLD指令 (float)
ST０ 浮点寄存器，有Empty和Valid状态
FSTP 指令　取出浮点寄存器，
FMUL 浮点相乘
FCOMPP比较
NOP 指令

--------其它工具
1.PEiD_看是Delphi_还是VC开发
	可选项->复选 右键菜单扩展
	右下角的 -> 按钮->插件->Krypto Analyzer 可以看使用了哪些算法,如MD5
2.DeDe 3.5__Borland C++ builder_Delphi_反编

3.PE_Explorer_反汇编＿看EXE文件界面组件代码
	打开exe文件后，点工具栏上的资源查看/编辑器 ->RC 数据
--------OD 应用 

双击修改代码,双击寄存器修改值,只是测试,破解只能修改代码


右击内存区->go to ->express (ctrl+g)输入内存地址
右击内存区->Long->Address 更改显示

双击注释区，可为该行代码加注释
下断点　，双击第二列，左边的地址列变红，右击->breakpoint->toggle(F2)
Run Program 按钮（F9）运行，单步走地址列变深蓝色
step into 按钮(F7) 进入call的代码

VC查找按钮事件
右击->search for->All referenced text Strings,所代码中的所有字符都找出,右击->search for text(输入界面上有的文字)
search Next(ctrl+L) ,找到后双击返回到原始代码,


选中 右击->binary ->fill with NOPS,选中的代码修改为 一行 nop指令
选中代码 右击->undo selection
	 右击->anylysis ->anylyse code,

view ->breakpoint ->显示一个视图有所有的断点,可删,用后关闭窗口


当注册失败时不点确定,debug->pause (F12),
点K按钮,也可view->call stack (ctrl+k),打开新窗口,找到MessageBox (Called From 列的值是MFC开头) 右击->show Call

标题栏上显示最后是 module XXX,如XXX是MFC开头,表示在系统区中,


view -> memory->中显示Owner列是 自己的程序名的 表示是自己程序使用的内存区,其它是系统区

---------------------TCP/IP


IP首部 前加20字节是必须,后有选项


===================
http://archlinuxarm.org/platforms/armv8/generic

ARM 处理器    (Acorn RISC Machine   ,Acorn计算机有限公司)
RISC的英文全称是 Reduced Instruction Set Computer 精简指令集计算机
ABI是Application Binary Interface

Linux ARM v8 Hard Float ABI





