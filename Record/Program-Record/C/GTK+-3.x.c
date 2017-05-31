http://developer.gnome.org/references  下有很多 Glib,GTK+ 3,GDK 3,libxml2,libxsl
http://developer.gnome.org/gtk3/stable/

Eclipse只有GTK的,Firefox基于GTK

gtkmm C++版的GTK SuSE Linux: 用 yast 安装 gtkmm3-devel.


zhaojin@linux-hlbo:~> pkg-config --cflags --libs gtk+-3.0
-pthread -I/usr/include/gtk-3.0 -I/usr/include/pango-1.0 -I/usr/include/gio-unix-2.0/ -I/usr/include/atk-1.0 -I/usr/include/cairo -I/usr/include/gdk-pixbuf-2.0 -I/usr/include/freetype2 -I/usr/include/glib-2.0 -I/usr/lib64/glib-2.0/include -I/usr/include/pixman-1 -I/usr/include/libpng12 -I/usr/include/libpng14  -lgtk-3 -lgdk-3 -latk-1.0 -lgio-2.0 -lpangocairo-1.0 -lgdk_pixbuf-2.0 -lcairo-gobject -lpango-1.0 -lcairo -lgobject-2.0 -lglib-2.0  
zhaojin@linux-hlbo:~> 
 

.cproject中的部分

 <option id="gnu.cpp.compiler.option.include.paths.1611167307" name="Include paths (-I)" superClass="gnu.cpp.compiler.option.include.paths" valueType="includePath">
        <listOptionValue builtIn="false" value="/usr/include/gtk-3.0"/>
        <listOptionValue builtIn="false" value="/usr/include/libpng14"/>
        <listOptionValue builtIn="false" value="/usr/include/pango-1.0"/>
        <listOptionValue builtIn="false" value="/usr/include/gio-unix-2.0"/>
        <listOptionValue builtIn="false" value="/usr/include/atk-1.0"/>
        <listOptionValue builtIn="false" value="/usr/include/cairo"/>
        <listOptionValue builtIn="false" value="/usr/include/gdk-pixbuf-2.0"/>
        <listOptionValue builtIn="false" value="/usr/include/freetype2"/>
        <listOptionValue builtIn="false" value="/usr/include/glib-2.0"/>
        <listOptionValue builtIn="false" value="/usr/lib64/glib-2.0/include"/>
        <listOptionValue builtIn="false" value="/usr/include/pixman-1"/>
        <listOptionValue builtIn="false" value="/usr/include/libpng12"/>
</option>	

...

<option id="gnu.cpp.link.option.libs.1129858520" superClass="gnu.cpp.link.option.libs" valueType="libs">
        <listOptionValue builtIn="false" value="gtk-3"/>
        <listOptionValue builtIn="false" value="gdk-3"/>
        <listOptionValue builtIn="false" value="atk-1.0"/>
        <listOptionValue builtIn="false" value="gio-2.0"/>
        <listOptionValue builtIn="false" value="pangocairo-1.0"/>
        <listOptionValue builtIn="false" value="gdk_pixbuf-2.0"/>
        <listOptionValue builtIn="false" value="cairo-gobject"/>
        <listOptionValue builtIn="false" value="pango-1.0"/>
        <listOptionValue builtIn="false" value="cairo"/>
        <listOptionValue builtIn="false" value="gobject-2.0"/>
        <listOptionValue builtIn="false" value="glib-2.0"/>
</option>


---win64 下 gtk+bundle_3.6.4-20131201_win64
gtk+bundle_3.6.4-20131201_win64\bin 放入PATH环境变量

pkg-config --cflags --libs gtk+-3.0 去除重复的

-mms-bitfields 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/gtk-3.0 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/cairo 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/pango-1.0 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/atk-1.0 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/pixman-1 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/freetype2 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/gdk-pixbuf-2.0 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/libpng15 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/glib-2.0 
-IC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/lib/glib-2.0/include 
-LC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/lib
-lgtk-3 -lgdk-3 -lgdi32 -limm32 -lshell32 -lole32 -Wl,-luuid -lpangocairo-1.0 -lpangoft2-1.0 -lfreetype -lfontconfig -lpangowin32-1.0   -lpango-1.0 -lm -latk-1.0 -lcairo-gobject -lcairo -lgdk_pixbuf-2.0 -lgio-2.0 -lgobject-2.0 -lglib-2.0 -lintl

加多加的 
C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/lib/glib-2.0/include

------eclipse 配置 

-mms-bitfields  
-Wl,-luuid   
-LC:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/lib

<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/gtk-3.0"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/libpng15"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/pango-1.0"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/atk-1.0"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/cairo"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/gdk-pixbuf-2.0"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/freetype2"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/glib-2.0"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/lib64/glib-2.0/include"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/include/pixman-1"/>
<listOptionValue builtIn="false" value="C:/My/soft_ware/c++/gtk+bundle_3.6.4-20131201_win64/lib/glib-2.0/include"/>


<listOptionValue builtIn="false" value="gtk-3"/>
<listOptionValue builtIn="false" value="gdk-3"/>
<listOptionValue builtIn="false" value="gdk_pixbuf-2.0"/>
<listOptionValue builtIn="false" value="atk-1.0"/>
<listOptionValue builtIn="false" value="glib-2.0"/>
<listOptionValue builtIn="false" value="gio-2.0"/>
<listOptionValue builtIn="false" value="gobject-2.0"/>
<listOptionValue builtIn="false" value="pango-1.0"/>
<listOptionValue builtIn="false" value="pangocairo-1.0"/>
<listOptionValue builtIn="false" value="pangoft2-1.0"/>
<listOptionValue builtIn="false" value="m"/>
<listOptionValue builtIn="false" value="intl"/>
<listOptionValue builtIn="false" value="freetype"/>
<listOptionValue builtIn="false" value="fontconfig"/>
<listOptionValue builtIn="false" value="cairo"/>
<listOptionValue builtIn="false" value="cairo-gobject"/>
<listOptionValue builtIn="false" value="gdi32"/>
<listOptionValue builtIn="false" value="shell32"/>
<listOptionValue builtIn="false" value="ole32"/>
<listOptionValue builtIn="false" value="imm32"/> 

---------------
gtk3-demo 命令  有示例代码, 双击列表项可以运行,代码也有很多编译时提示deprecate
gtk3-demo-application	一个小应用
gtk3-widget-factory		所有的组件界面

glade-3.x 有win32/Mac 版本的二进制文件,用于UI设计

 
#include<stdlib.h>
#define	EXIT_SUCCESS	0
#define	EXIT_FAILURE	1


 g_signal_handler_disconnect( gpointer object,
                                  gulong   id );//删回调

g_signal_handler_block( gpointer object,
                             gulong   id ); //暂停信号

g_signal_handlers_block_by_func( gpointer  object,
                                      GCallback func,
                                      gpointer  data );


g_signal_handler_unblock( gpointer object,
                               gulong   id );//继续暂停的信号

gtk_hbox_new(true，组件间的距离)
gtk_hbox_new()false时，gtk_box_pack_start(gtkbox    *box,
                         gtkwidget *child,
                         gboolean   expand, //只有当gtk_hbox_new()第一参false时,expand的值才有效,否则一直是可扩展的
                         gboolean   fill,
                         guint      padding )//组件边间的距离



g_signal_connect_swapped (g_object (button), "clicked",
			      g_callback (gtk_widget_destroy),
                              window);

先触发delete_event  信号,再destroy 信号
delete_event的回调返回true，表示已成功处理，不要继承传递


gtk_hbox_new()，对于纵向盒，用 gtk_vbox_new()。gtk_box_pack_start() 和 gtk_box_pack_end()

gtk_misc_set_alignment (GTK_MISC (label), 0, 0.5);



atoi() 把字符转为int (stdlib.h)
fprintf (stderr, "error"); //stdio.h,默认加载的
gtk_hseparator_new () //分隔线
gtk_label_new("标签")  


gtk_widget_set_size_request (separator, 400, 5); //设置分隔线的宽度为400象素点和5象素点高

gtkwidget * gtk_table_new( guint    rows, guint    columns,gboolean homogeneous );

true，所有表格框的大小都将调整为表中最大
false，每个表格框将会按照同行中最

void gtk_table_attach( gtktable         *table,
                       gtkwidget        *child,
                       guint            left_attach,
                       guint            right_attach,
                       guint            top_attach,
                       guint            bottom_attach,
                       gtkattachoptions xoptions,
                       gtkattachoptions yoptions,
                       guint            xpadding,
                       guint            ypadding );

xoptions及yoptions可取的值有  gtk_fill , gtk_shrink , gtk_expand
void gtk_table_attach_defaults(GTK_TABLE (table)...) ,x及y选项默认为gtk_fill | gtk_expand，x和y的padding则设为0

gtk_table_set_row_spacing //空白插入行的下边。如尾加s ,相同
gtk_table_set_col_spacing //空白插到列的右边.如尾加s ,相同

typedef void* gpointer;

gtk_widget_hide 使构件再次隐藏,最后显示窗口


gtk_button_new_with_mnemonic()   //(记忆的),来创建一个带标签的按钮,,,标签中的以 '_' 为前缀的助记语法符。
gtk_button_new_from_stock() //来从一个原料(stock)项创建一个包含图像和文本的按钮

gtk_image_new_from_file (xpm_filename);//创建一个图像,文件可是gif文件动画

gtk_button_new() 来加一个box ( 是有image和 lable)

按钮的鼠标信号:pressed,released,clicked,enter --- 当鼠标光标进入按钮构件时发出,leave --- 当鼠标光标离开按钮构件时发出


gtk_toggle_button_new
gtk_toggle_button_new_with_label
gtk_toggle_button_new_with_mnemonic

gtk_toggle_button_get_active (GTK_TOGGLE_BUTTON (xxxx))
gtk_toggle_button_set_active //发出 "clicked" 和 "toggled" 信号。

gtk_check_button_new..............

gtkwidget *gtk_radio_button_new( gslist *group );
gtk_radio_button_new_from_widget //组是null

gtk_radio_button_get_group (GTK_RADIO_BUTTON (button1))
gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(button)


   GTK_WIDGET_SET_FLAGS (button, GTK_CAN_DEFAULT);//buttion 是普通按钮,回车的默认按钮
   gtk_widget_grab_default (button);

gtkobject *gtk_adjustment_new( gdouble value,
                               gdouble lower,
                               gdouble upper,
                               gdouble step_increment,
                               gdouble page_increment,
                               gdouble page_size );



/* 视角构件会自动为自己创建一个调整对象 */
  viewport = gtk_viewport_new (null, null);
  /* 让垂直滚动条使用视角构件已经创建的调整对象 */
  vscrollbar = gtk_vscrollbar_new (gtk_viewport_get_vadjustment (viewport));

gtk_adjustment_get_value
gtk_adjustment_set_value


value_changed信号

整对象的upper或lower参数时,个changed信号

gtk_vscale_new(gtkadjustment
gtk_vscale_new_with_range(gdouble min.....


gtk_scale_set_draw_value( gtkscale *scale,gboolean draw_value );//默认行为是显示值,是否显示标签数值,


gtk_scale_set_digits( gtkscale *scale, gint     digits ); //显示的小数位
gtk_scale_set_value_pos( gtkscale        *scale, gtkpositiontype  pos );//值位置gtk_scale (hscale)
  GTK_POS_LEFT  大写
  GTK_POS_RIGHT
  GTK_POS_TOP
  GTK_POS_BOTTOM



gtk_range_set_update_policy( gtkrange      *range, //gtk_range(widget)
	gtkupdatetype  policy);
		gtk_update_continuous
		gtk_update_discontinuous
		gtk_update_delayed

gtk_range_get_adjustment(
gtk_range_set_adjustment(

g_signal_emit_by_name (g_object (adjustment), "changed");//发信号

中键(button-2)将使滑块跳到鼠标点击处。
可以用方向键,page up/down   home/end

 //自己写的 scale_set_default_values (gtk_scale (vscale));  gtk_scale (vscale)对gtkscale * 
  gtk_widget_set_size_request (GTK_WIDGET (hscale), 200, -1);//宽,高


 opt = gtk_option_menu_new ();
    menu = gtk_menu_new ();
   item=gtk_menu_item_new_with_label (name);//菜单项有activate信号
   
   item = make_menu_item ("top",  //make_menu_item自己写的
                           G_CALLBACK (cb_pos_menu_select),
                           gint_to_pointer (GTK_POS_TOP));

gtk_menu_shell_append (GTK_MENU_SHELL (menu), item);//菜单加菜单项
gtk_option_menu_set_menu (GTK_OPTION_MENU (opt), menu);//选项菜单加菜单
----------------lable & frame---------------------------
gtk_label_new( const char *str );
gtk_label_new_with_mnemonic( const char *str );
gtk_label_set_text( gtklabel   *label,  //gtk_label() 
                         const char *str );
gtk_label_get_text( gtklabel  *label )

gtk_label_set_justify( gtklabel         *label,
                            gtkjustification  jtype ); //对齐方式
	GTK_JUSTIFY_LEFT  左对齐
 	GTK_JUSTIFY_RIGHT  右对齐
  	GTK_JUSTIFY_CENTER  居中对齐(默认)
 	GTK_JUSTIFY_FILL  充满
gtk_label_set_line_wrap (gtklabel *label,
                              gboolean  wrap);//是否自动换行

gtk_label_set_pattern   (gtklabel          *label,
                                    const gchar       *pattern);//下划线,字符是下划线和空格
 gtk_label_set_text_with_mnemonic()
	"ss" \
	"bb"
	和"ssbb"相同

gtk_frame_new ("multi-line label");//
  gtk_widget_show_all (window);
----------------arrow---------------------------

gtk_arrow_set( gtkarrow      *arrow,
                    gtkarrowtype   arrow_type,
                    gtkshadowtype  shadow_type );
  GTK_ARROW_UP  向上
  GTK_ARROW_DOWN  向下
  GTK_ARROW_LEFT  向左
  GTK_ARROW_RIGHT  向右

以下几个值没有什么太大变化
  gtk_shadow_in
  gtk_shadow_out (缺省值)
  gtk_shadow_etched_in
  gtk_shadow_etched_out
----------------tooltips---------------------------
gtk_tooltips_new( void );
gtk_tooltips_set_tip( gtktooltips *tooltips,
                           gtkwidget   *widget,
                           const gchar *tip_text,
                           const gchar *tip_private )
gtk_tooltips_enable(
gtk_tooltips_disable(
-----------------progress---------------------------
gtk_progress_bar_new( void )
gtk_progress_bar_set_fraction ( gtkprogressbar *pbar,     //fraction小部分, 片断, 分数gtk_progress_bar(...)
                                     gdouble        fraction );//0-1 的完成%
			

gtk_progress_bar_get_orientation(GTK_PROGRESS_BAR(..))
gtk_progress_bar_set_orientation( gtkprogressbar *pbar,
                                       gtkprogressbarorientation orientation )
  GTK_PROGRESS_LEFT_TO_RIGHT  从左向右
  GTK_PROGRESS_RIGHT_TO_LEFT  从右向左
  GTK_PROGRESS_BOTTOM_TO_TOP  从下向上
  GTK_PROGRESS_TOP_TO_BOTTOM  从上向下



pulse 脉搏, 脉冲
 gtk_progress_bar_pulse ( gtkprogressbar *progress );//仅仅指示有活动在继续,gtk_progress_bar(..)


gtk_progress_bar_set_text( gtkprogressbar *progress,
                                const gchar    *text );//滑槽里显示文本串, null 作为第二个参数来关闭文本串的显示
const gchar *gtk_progress_bar_get_text( gtkprogressbar *pbar );

progressdata * pdata=(progressdata *)g_malloc (sizeof (progressdata));//分配内存返 gpointer 即void*,progressdata是一个struct
g_free(pdata)
gtk_window_set_resizable (GTK_WINDOW (pdata->window), TRUE)
int xx=gtk_timeout_add (100, progress_timeout, pdata);//每100毫秒调用一次progress_timeout函数,传pdata参数,返回true
gtk_timeout_remove(xx)

 g_signal_connect_swapped (G_OBJECT (button), "clicked",
                              G_CALLBACK (gtk_widget_destroy),
                              pdata->window);
-----------------dialog---------------------------

*gtk_dialog_new_with_buttons( const gchar    *title,
                                        gtkwindow      *parent,
                                        gtkdialogflags  flags, 
                                        const gchar    *first_button_text,
                                        ... );
gtk_dialog_modal  		使对话框使用独占模式。
gtk_dialog_destroy_with_parents	保证对话框在指定父窗口被关闭时也一起关闭。
gtk_dialog_no_separator		省略纵向盒与活动区之间的分隔线


*gtk_dialog_new( void )//空对话框
 gtk_box_pack_start (GTK_BOX (GTK_DIALOG (window)->action_area),
                        button, true, true, 0);
-----------------ruler---------------------------
gtk_hruler_new( void );    /* 水平标尺 */
gtk_vruler_new( void );    /* 垂直标尺 */
gtk_ruler_set_metric( gtkruler      *ruler,gtkmetrictype  metric );//GTK_PIXELS(默认,有显示值)，GTK_INCHES或GTK_CENTIMETERS。


gtk_ruler_set_range( gtkruler *ruler,  //gtk_ruler(vruler)
                          gdouble   lower,
                          gdouble   upper,
                          gdouble   position,
                          gdouble   max_size );

GTK_WIDGET_GET_CLASS(...)
gtk_drawing_area_new ()
 gtk_widget_set_size_request (gtk_widget (area), 10, 10);	
gtk_widget_set_events (area, GDK_POINTER_MOTION_MASK |GDK_POINTER_MOTION_HINT_MASK);//GDK_POINTER_MOTION_MASK就可以了


 g_signal_connect_swapped (g_object (area), "motion_notify_event",           //信号
                             // g_callback (event_method (vrule, motion_notify_event)),
				G_CALLBACK (GTK_WIDGET_GET_CLASS(vrule)->motion_notify_event),
                              vrule);
-----------------statusbar---------------------------
gtk_statusbar_new( void );
gint gtk_statusbar_get_context_id( gtkstatusbar *statusbar,
                                    const gchar  *context_description );
guint gtk_statusbar_push( gtkstatusbar *statusbar,//GTK_STATUSBAR(...)
                          guint         context_id,
                          const gchar  *text );

void gtk_statusbar_pop( gtkstatusbar *statusbar)
                        guint         context_id );// 删除在栈中给定上下文标识符的最上面的一条消息。

void gtk_statusbar_remove( gtkstatusbar *statusbar,
                           guint         context_id,
                           guint         message_id ); 

 gtk_statusbar_set_has_resize_grip( gtkstatusbar *statusbar,
					    gboolean      setting );//显示改改变大小的手柄默认是true

gpointer_to_int()
char buff[20];	
g_snprintf (buff, 20, "item %d", count++);

-----------------entry----------------------------
gtk_entry_new( void );
gtk_entry_set_text( gtkentry    *entry,
                         const gchar *text );
tk_entry_get_text( gtkentry *entry );// gtk_entry_get_text (GTK_ENTRY (entry))
gtk_editable_set_editable( gtkeditable *entry,
                                gboolean     editable );//是否可编辑
gtk_entry_set_visibility( gtkentry *entry,
                               gboolean  visible );//false 对口令,windows 上显示***
gtk_editable_select_region( gtkeditable *entry,
                                 gint         start,
                                 gint         end );//选中区域
activate或changed信号(输入构件内回车键时引发activate信号,文本发生变化时引发changed信号)

gtk_main_quit()
gtk_widget_destroy(window)
gtk_entry_set_max_length (GTK_ENTRY (entry), 50);

gint tmp_pos = gtk_entry (entry)->text_length;
gtk_editable_insert_text (GTK_EDITABLE (entry), " world", -1(对整个word),  &tmp_pos);
 GTK_TOGGLE_BUTTON (checkbutton)->active//得到是否激活,gtk_toggle_button_get_active (GTK_TOGGLE_BUTTON (xxxx))

gtk_button_new_from_stock (gtk_stock_close)//gtk的有图标的关闭按钮

	
-----------------spin----------------------------
gtk_spin_button_new( gtkadjustment *adjustment,
                                gdouble         climb_rate,//于0.0和1.0间,长时间按住按钮，数值会加速变化
                                guint          digits );//小数位数
gtk_spin_button_configure( gtkspinbutton *spin_button,
                                gtkadjustment *adjustment,
                                gdouble        climb_rate,
                                guint          digits );
gtk_spin_button_set_adjustment( 
gtkadjustment *gtk_spin_button_get_adjustment(
gtk_spin_button_set_digits( GtkSpinButton *spin_button,
                                 guint          digits) ;

gtk_spin_button_set_value( GtkSpinButton *spin_button,
                                gdouble        value );
gtk_spin_button_get_value(
gtk_spin_button_get_value_as_int(

gtk_spin_button_spin( GtkSpinButton *spin_button,
                           GtkSpinType    direction,
                           gdouble        increment );//当前值为基数改变微调按钮
direction  的取值
  GTK_SPIN_STEP_FORWARD
  GTK_SPIN_STEP_BACKWARD
  GTK_SPIN_PAGE_FORWARD
  GTK_SPIN_PAGE_BACKWARD
  GTK_SPIN_HOME
  GTK_SPIN_END
  GTK_SPIN_USER_DEFINED
gtk_spin_button_set_numeric( GtkSpinButton *spin_button,
                                  gboolean       numeric );//只能输入数值   GTK_TOGGLE_BUTTON (widget)->active);
gtk_spin_button_set_wrap( GtkSpinButton *spin_button,
                               gboolean       wrap );//upper和lower之间循环
gtk_spin_button_set_snap_to_ticks( GtkSpinButton  *spin_button,
                                        gboolean        snap_to_ticks );//最接近step_increment的值   GTK_TOGGLE_BUTTON (widget)->active);
gtk_spin_button_set_update_policy( GtkSpinButton  *spin_button,
                                    GtkSpinButtonUpdatePolicy policy );//GTK_UPDATE_ALWAYS或GTK_UPDATE_IF_VALID。

gtk_spin_button_update( GtkSpinButton  *spin_button );//强行更新


gtk_misc_set_alignment (GTK_MISC (label), 0, 0.5);

 gtk_container_add (GTK_CONTAINER (frame), vbox); //frame 是一个container  可add box
  g_object_set_data (G_OBJECT (button),  "user_data", val_label);  //object ,key ,value

GTK_SPIN_BUTTON(...)
GTK_LABEL (g_object_get_data (G_OBJECT (widget), "user_data"))

 sprintf (char * buf,"%d",1) 保存到buf中
 sprintf (char * buf,"%0.*f",2,3.1234)//只要两位小数

-----------------------combo box------------------
_GtkCombo 结构体
struct _GtkCombo { 
        GtkHBox hbox; 
        GtkWidget *entry; 
        GtkWidget *button;
        GtkWidget *popup; 
        GtkWidget *popwin; 
        GtkWidget *list;
	...  };


GtkWidget *gtk_combo_new( void );

gtk_entry_set_text (GTK_ENTRY (GTK_COMBO (combo)->entry), "My String.");//有什么用呢???

void gtk_combo_set_popdown_strings( GtkCombo *combo,
                                    GList    *strings );

GList *glist = NULL;
glist = g_list_append (glist, "String 1");
gtk_combo_set_popdown_strings (GTK_COMBO (combo), glist);

gtk_combo_set_use_arrows( GtkCombo *combo, gboolean  val );//上/下方向键改变文本输入,最后一项，按向下的方向键会改变焦点的,默认是TRUE

void gtk_combo_set_use_arrows_always( GtkCombo *combo, gboolean  val );//上/下方向键改变文本输入,它在列表项中循环//没有效果

gtk_combo_set_case_sensitive( GtkCombo *combo, gboolean  val );
//如果用户同时按下MOD-1和“Tab”键，组合框构件还可以简单地补全当前输入。MOD-1一般被 xmodmap 工具映射为“Alt”键

g_signal_connect (G_OBJECT (GTK_COMBO (combo)->entry), "activate",  //接到activate信号,按回车键时,文本输入
                      G_CALLBACK (my_callback_function), my_data);

//选择时什么信号呢? 如何才能不可以手工输入?
gtk_entry_get_text (GTK_ENTRY (GTK_COMBO (combo)->entry));


gtk_combo_disable_activate(   // 有什么用呢???

-----------------------calendar----------------------		

gtk_calendar_new( void );


//屏幕闪烁
void gtk_calendar_freeze( GtkCalendar *Calendar );
void gtk_calendar_thaw( GtkCalendar *Calendar );

gtk_calendar_display_options( GtkCalendar               *calendar, //GTK_CALENDAR(...)
                                   GtkCalendarDisplayOptions  flags )//(|)操作符组合
		GTK_CALENDAR_SHOW_HEADING
		GTK_CALENDAR_SHOW_DAY_NAMES
		GTK_CALENDAR_NO_MONTH_CHANGE   //在 SHOW_HEADING为选中时才有效
		GTK_CALENDAR_SHOW_WEEK_NUMBERS
		GTK_CALENDAR_WEEK_START_MONDAY//从星期一开始而不是从星期天开始。缺省设置是从星期天开始, 根据local


gint gtk_calendar_select_month( GtkCalendar *calendar, 
                                guint        month,
                                guint        year );

void gtk_calendar_select_day( GtkCalendar *calendar,
                              guint        day );


//。被“标记”的日期会在日历构件中高亮显示
gint gtk_calendar_mark_day( GtkCalendar *calendar, guint        day);
gint gtk_calendar_unmark_day( GtkCalendar *calendar,guint        day);
void gtk_calendar_clear_marks( GtkCalendar *calendar);


void gtk_calendar_get_date( GtkCalendar *calendar, 
                            guint       *year,//返回值就存放在这几个变量中, 是正常的年
                            guint       *month,
                            guint       *day );

//信号
month_changed
day_selected
day_selected_double_click
prev_month
next_month
prev_year
next_year

G_CALLBACK (gtk_false)

gtk_window_set_resizable(GTK_WINDOW (window), FALSE);




gtk_button_box_set_layout(GTK_BUTTON_BOX(hbbox), GTK_BUTTONBOX_SPREAD);
	gtk_box_set_spacing(GTK_BOX (hbbox), 5);

gtk_calendar_display_options(GTK_CALENDAR(..),..)


加(+) 等于 或(|)
 
  gtk_container_add( GTK_CONTAINER (frame), calendar);//frame 是一个CONTAINER  

time_t 是 sys/types.h 和 time.h中都定义为一个 long //typedef	long	time_t;
tm 结构体是在 time.h中定义 //tm.tm_year,是 从1900年开始计算的,要减去1900

g_print();

struct tm tm;
time_t time;
time = mktime (&tm); //不会改变tm 的数据
strftime (buffer, buff_len-1, "%x", gmtime (&time));  // gmtime (&time) 返回一个tm *, gmtime (&time)->mday 比tm.mday(准的) 少了一天 
//把tm类型的数据格式化后存入 char * buffer, 最长为

mktime (&tm); //不会改变tm 的数据
gmtime (&time)//不会改变time 的数据

font_win=gtk_font_selection_dialog_new("title")
g_return_if_fail (GTK_IS_FONT_SELECTION_DIALOG (font_win));
gtk_window_set_position (GTK_WINDOW (font_win), GTK_WIN_POS_MOUSE); //窗口的中心,在鼠标位置

   G_CALLBACK (gtk_widget_destroyed),
   G_OBJECT (GTK_FONT_SELECTION_DIALOG (font_win)->ok_button  , "clicked" //信号


gchar * font_name=gtk_font_selection_dialog_get_font_name (GTK_FONT_SELECTION_DIALOG (font_win));
PangoFontDescription * x=pango_font_description_from_string(font_name)
GtkStyle *style=gtk_style_copy (gtk_widget_get_style (font_win))
gtk_widget_set_style (font_win, style);



bbox = gtk_hbutton_box_new ();
gtk_button_box_set_layout (GTK_BUTTON_BOX (bbox), GTK_BUTTONBOX_END);

---------------------------color selection--------------
#include <gdk/gdk.h>
#include <gtk/gtk.h>

color_changed //信号
 gtk_color_selection_set_color(GtkColorSelection *colorsel,　gdouble  *color);

gtk_color_selection_new( void );
gtk_color_selection_dialog_new( const gchar *title );

GTK_COLOR_SELECTION_DIALOG (colorseldialog)->ok_button)

gtk_color_selection_set_has_opacity_control( GtkColorSelection *colorsel, gboolean  has_opacity );

gtk_color_selection_set_current_color(．．．．．， GdkColor  *color );
gtk_color_selection_set_current_alpha(     //  0(完全透明)和65636(完全不透明)

gtk_color_selection_get_current_color(．．．，GdkColor  *color );//结果存入color
guint16 z=gtk_color_selection_get_current_alpha(．．．)//教程上有错,看头文件



gtk_window_set_policy (GTK_WINDOW (window), TRUE, TRUE, TRUE);
										// allowshrink,allowgrow,autoshrink
g_signal_connect (GTK_OBJECT (drawingarea), "event", GTK_SIGNAL_FUNC (area_event), (gpointer)drawingarea);//event 事件/信号
													 GTK_SIGNAL_FUNC (myfunc) //会调用 myfunc( GtkWidget *widget,GdkEvent  *event,...mydata) 函数
																								event->type == GDK_BUTTON_PRESS

GdkColor color;
  color.red = 0;
  color.blue = 65535;  //最大值65535   ,65535/255=257   
  color.green = 0;
GtkWidget *drawingarea=gtk_drawing_area_new ();

gtk_widget_modify_bg  (drawingarea , GTK_STATE_NORMAL, &color);
gtk_widget_set_events (drawingarea, GDK_BUTTON_PRESS_MASK);

GtkWidget *colorseldlg = NULL;
colorseldlg = gtk_color_selection_dialog_new ("Select background color11111");

GtkColorSelection *colorsel;
colorsel = GTK_COLOR_SELECTION (GTK_COLOR_SELECTION_DIALOG (colorseldlg)->colorsel);// 有color_changed信号



gtk_color_selection_set_has_palette (colorsel, TRUE);//是否显示下方的颜色面板块

gtk_color_selection_set_previous_color (colorsel, &color);//最初的颜色

GTK_RESPONSE_OK＝＝gtk_dialog_run (GTK_DIALOG (colorseldlg))///显示对话框

--------------------------------------------file_selection--------------------------------------------

gtk_file_selection_new( const gchar *title );
gtk_file_selection_set_filename( GtkFileSelection *filesel,const gchar      *filename );//默认文件名,路径为运行的当前目录


内部指针
 dir_list
  file_list
  selection_entry
  selection_text
  main_vbox
  ok_button
  cancel_button
  help_button

G_OBJECT (GTK_FILE_SELECTION (filew)->ok_button  //cancel_button

gtk_file_selection_get_filename (GTK_FILE_SELECTION (fs))

gtk_file_selection_set_select_multiple(...,TRUE)//多选文件

//有新建文件夹,改文件名,删文件(不会去回收站的,会提示的)

--------------------------------------EventBox------------------------------------------------------

  GtkWidget *gtk_event_box_new( void );
 gtk_container_add (GTK_CONTAINER (event_box), child_widget);


    gtk_widget_set_events (event_box, GDK_BUTTON_PRESS_MASK);
    g_signal_connect (G_OBJECT (event_box), "button_press_event", G_CALLBACK (exit), NULL);

   gtk_widget_realize (event_box);
   gdk_window_set_cursor (event_box->window, gdk_cursor_new (GDK_HAND1));//手形鼠标,要先 gtk_widget_realize()

---------------------------------alignment----------------------------------

gtk_alignment_new( gfloat xalign,
                              gfloat yalign,
                              gfloat xscale,
                              gfloat yscale );
//四个参数都是介于0.0与1.0间

 gtk_alignment_set( GtkAlignment *alignment,
                        gfloat        xalign,
                        gfloat        yalign,
                        gfloat        xscale,
                        gfloat        yscale );

gtk_container_add (GTK_CONTAINER (alignment), child_widget);



//scrollbar 中有例子
--------------------------------Fixed Container--------------------------

GtkWidget* gtk_fixed_new( void );

void gtk_fixed_put( GtkFixed  *fixed,  //GTK_FIXED (fixed)
                    GtkWidget *widget,
                    gint       x,
                    gint       y );   //放入

void gtk_fixed_move( GtkFixed  *fixed,  //GTK_FIXED (fixed)
                     GtkWidget *widget,
                     gint       x,
                     gint       y );  //移动
//没有它们自己的 X 窗口
void gtk_fixed_set_has_window( GtkFixed  *fixed,
                               gboolean   has_window );   //必须在构件实例化(realizing)之前调

gboolean gtk_fixed_get_has_window( GtkFixed *fixed );


----------------------layout-------------------------------------------
gtk_layout_new( GtkAdjustment *hadjustment,GtkAdjustment *vadjustment );//要h,v两个

gtk_layout_put( GtkLayout *layout,
                     GtkWidget *widget,
                     gint       x,
                     gint       y );
gtk_layout_move( GtkLayout *layout,
                      GtkWidget *widget,
                      gint       x,
                      gint       y );

gtk_layout_set_size( GtkLayout *layout,    //设容器的大小
                          guint      width,
                          guint      height );


gtk_layout_get_hadjustment(...)
gtk_layout_get_vadjustment(..)
gtk_layout_set_hadjustment(..)
gtk_layout_set_vadjustment(..)

------------------- Frames--------------------------------------

gtk_frame_new( const gchar *label );//可传NULL

gtk_frame_set_label( GtkFrame    *frame,const gchar *label );//改变

gtk_frame_set_label_align( GtkFrame *frame,
                                gfloat    xalign,   //0.0和1.0之间   ,默认0,靠近左端
                                gfloat    yalign );//0在最下面,1在最上面


gtk_frame_set_shadow_type( GtkFrame      *frame,
                                GtkShadowType  type);

					  GTK_SHADOW_NONE
					  GTK_SHADOW_IN
					  GTK_SHADOW_OUT
					  GTK_SHADOW_ETCHED_IN (缺省值)
					  GTK_SHADOW_ETCHED_OUT

---------------Aspect--------------------------------------

//会使子构件的外观比例（也就是宽和长的比例）保持一定值,,构件中增加额外的可用空间
gtk_aspect_frame_new( const gchar *label,
                                 gfloat       xalign,
                                 gfloat       yalign,
                                 gfloat       ratio,
                                 gboolean     obey_child);//bey_child参数设置为 TRUE，子构件的长宽比例会和它所请求的理想长宽比例相匹配。否则，比例值由ratio参数指定。



gtk_aspect_frame_set( GtkAspectFrame *aspect_frame,
                           gfloat          xalign,   //框架的位置  0.5是中心,可比例变化,
                           gfloat          yalign,
                           gfloat          ratio,  //   x/y  等于几?   x 是y的几倍  
                           gboolean        obey_child);

------------------- paned -------------------------------------
GtkWidget *gtk_hpaned_new (void);

GtkWidget *gtk_vpaned_new (void);

gtk_paned_add1 (GtkPaned *paned, GtkWidget *child);//GTK_PANED (vpaned)
//gtk_paned_add1()将子构件添加到分栏窗口的左边或顶部。gtk_paned_add2()将子构件添加到分栏窗口的右边或下部。




-------
GtkListStore *model;
GtkTreeIter iter;
GtkCellRenderer *cell;
GtkTreeViewColumn *column;


gtk_scrolled_window_new (GtkAdjustment     *hadjustment, GtkAdjustment     *vadjustment);
gtk_scrolled_window_set_policy (GTK_SCROLLED_WINDOW (scrolled_window),
				    GTK_POLICY_AUTOMATIC,  //h
				    GTK_POLICY_AUTOMATIC); //v

model = gtk_list_store_new (1, G_TYPE_STRING);//1列
tree_view = gtk_tree_view_new ();
gtk_scrolled_window_add_with_viewport (GTK_SCROLLED_WINDOW (scrolled_window), tree_view);
gtk_tree_view_set_model (GTK_TREE_VIEW (tree_view), GTK_TREE_MODEL (model));


gchar *msg = g_strdup_printf ("Message #%d", i);
g_free (msg);


gtk_list_store_append (GTK_LIST_STORE (model), &iter);
gtk_list_store_set (GTK_LIST_STORE (model), &iter, 0, msg,-1);
cell = gtk_cell_renderer_text_new ();

column = gtk_tree_view_column_new_with_attributes ("Messages",
                                                       cell,
                                                       "text", 0,
                                                       NULL);
  
gtk_tree_view_append_column (GTK_TREE_VIEW (tree_view),
	  		         GTK_TREE_VIEW_COLUMN (column));
   GtkTextIter iter;
 
   gtk_text_buffer_get_iter_at_offset (buffer, &iter, 0);

   gtk_text_buffer_insert (buffer, &iter,   
    "From: pathfinder@nasa.gov\n"
------

------------------- viewport -------------------------------------
GtkWidget *gtk_viewport_new( GtkAdjustment *hadjustment,
                             GtkAdjustment *vadjustment );//数传递 NULL 参数，构件会自己创建调整对象。
gtk_viewport_get_hadjustment (
gtk_viewport_get_vadjustment (
gtk_viewport_set_hadjustment(
gtk_viewport_set_vadjustment(

gtk_viewport_set_shadow_type( GtkViewport   *viewport,
                                   GtkShadowType  type );
	GTK_SHADOW_NONE,
	GTK_SHADOW_IN,
	GTK_SHADOW_OUT,
	GTK_SHADOW_ETCHED_IN,
	GTK_SHADOW_ETCHED_OUT

--------------------------------scrolled_window----------------------------
gtk_scrolled_window_new( GtkAdjustment *hadjustment,
                                    GtkAdjustment *vadjustment );//它们一般都设置为NULL。

gtk_scrolled_window_set_policy( GtkScrolledWindow *scrolled_window,
                                     GtkPolicyType      hscrollbar_policy,
                                     GtkPolicyType      vscrollbar_policy );

		GTK_POLICY_AUTOMATIC或GTK_POLICY_ALWAYS

gtk_scrolled_window_add_with_viewport( GtkScrolledWindow *scrolled_window,//GTK_SCROLLED_WINDOW(..)
                                            GtkWidget         *child);

GTK_BOX (GTK_DIALOG(window)->vbox)  //显示文本区
GTK_BOX (GTK_DIALOG (window)->action_area)//显示按钮区

－－－－－－－－－－－－－－－－－－－－－hbutton_box－－－－－－－－－－－－－－－－－－－－－
GtkWidget *gtk_hbutton_box_new( void );

GtkWidget *gtk_vbutton_box_new( void );

gtk_button_new_from_stock (GTK_STOCK_OK);
gtk_button_new_from_stock (GTK_STOCK_CANCEL);
gtk_button_new_from_stock (GTK_STOCK_HELP);

 gtk_button_box_set_layout (GTK_BUTTON_BOX (bbox), (GtkButtonBoxStyle)layout);
									GTK_BUTTONBOX_DEFAULT_STYLE,
									GTK_BUTTONBOX_SPREAD,
									GTK_BUTTONBOX_EDGE,
									GTK_BUTTONBOX_START,
									GTK_BUTTONBOX_END,
									GTK_BUTTONBOX_CENTER

-------------------------------------toolbar--------------------------------------

gtk_toolbar_new( void );
前插或追加
GtkWidget *gtk_toolbar_append_item( GtkToolbar    *toolbar,
                                    const char    *text,
                                    const char    *tooltip_text,
                                    const char    *tooltip_private_text,
                                    GtkWidget     *icon,
                                    GtkSignalFunc  callback,
                                    gpointer       user_data );

GtkWidget *gtk_toolbar_prepend_item( GtkToolbar    *toolbar,
                                     const char    *text,
                                     const char    *tooltip_text,
                                     const char    *tooltip_private_text,
                                     GtkWidget     *icon,
                                     GtkSignalFunc  callback,
                                     gpointer       user_data );


gtk_toolbar_insert_item //多一个位置
GtkWidget *gtk_toolbar_insert_item( GtkToolbar    *toolbar,
                                    const char    *text,
                                    const char    *tooltip_text,
                                    const char    *tooltip_private_text,
                                    GtkWidget     *icon,
                                    GtkSignalFunc  callback,
                                    gpointer       user_data,
                                    gint           position );



void gtk_toolbar_append_space( GtkToolbar *toolbar ); //工具栏加空白,分隔线

void gtk_toolbar_prepend_space( GtkToolbar *toolbar );

void gtk_toolbar_insert_space( GtkToolbar *toolbar,
                               gint        position );



void gtk_toolbar_set_orientation( GtkToolbar     *toolbar, //GTK_TOOLBAR (toolbar)
                                  GtkOrientation  orientation )//GTK_ORIENTATION_HORIZONTAL或GTK_ORIENTATION_VERTICAL

void gtk_toolbar_set_style( GtkToolbar      *toolbar,
                            GtkToolbarStyle  style );//GTK_TOOLBAR_ICONS，GTK_TOOLBAR_TEXT或GTK_TOOLBAR_BOTH

void gtk_toolbar_set_tooltips( GtkToolbar *toolbar,
                               gint        enable );


 GTK_WINDOW (dialog)->allow_shrink = TRUE;//dialog=gtk_dialog_new ();


gtk_handle_box_new ();//可以把工具栏移开窗口外面



 /* 需要实例化窗口,因为我们要在它的内容中为工具栏设置图片 */
  gtk_widget_realize (dialog);
 gtk_toolbar_append_element (
                         GTK_TOOLBAR (toolbar),
                         GTK_TOOLBAR_CHILD_RADIOBUTTON, /* 元素类型 */  //所有的radiobutton 是一组GTK_TOOLBAR_CHILD_TOGGLEBUTTON
                         NULL,                          /* 指向构件的指针 */
                         "Icon",                        /* 标签 */
                         "Only icons in toolbar",       /* 工具提示 */
                         "Private",                     /* 工具提示的私有字符串 */
                         iconw,                         /* 图标 */
                         GTK_SIGNAL_FUNC (radio_event), /* 信号 */
                         toolbar);                      /* 信号传递的数据 */


－－－－－－－－－－－－－－－－－－notebook--------------------------------------
GtkWidget *gtk_notebook_new( void );
gtk_notebook_set_tab_pos( GtkNotebook     *notebook, //GTK_NOTEBOOK
                               GtkPositionType  pos );

  GTK_POS_LEFT
  GTK_POS_RIGHT
  GTK_POS_TOP
  GTK_POS_BOTTOM


void gtk_notebook_append_page( GtkNotebook *notebook,
                               GtkWidget   *child,
                               GtkWidget   *tab_label );

void gtk_notebook_prepend_page( GtkNotebook *notebook,
                                GtkWidget   *child,
                                GtkWidget   *tab_label );



gtk_notebook_insert_page( GtkNotebook *notebook,
                               GtkWidget   *child,
                               GtkWidget   *tab_label,
                               gint         position );//第一页位置为0。



gtk_notebook_remove_page( GtkNotebook *notebook,
                               gint         page_num );

gint gtk_notebook_get_current_page( GtkNotebook *notebook );

void gtk_notebook_next_page( GtkNoteBook *notebook );

void gtk_notebook_prev_page( GtkNoteBook *notebook );
gtk_notebook_set_current_page( GtkNotebook *notebook,
                                    gint         page_num );

void gtk_notebook_set_show_tabs( GtkNotebook *notebook,
                                 gboolean     show_tab );  //notebook->show_tabs == 0
gtk_notebook_set_show_border( GtkNotebook *notebook,
                                   gboolean     show_border );//notebook->show_border == 0


gtk_notebook_set_scrollable( GtkNotebook *notebook,
                                  gboolean     scrollable );//两个箭头按钮来滚动标签页

/*这会迫使构件重绘自身。可以有使用，会自动重绘*/
    gtk_widget_queue_draw (GTK_WIDGET (notebook));

---------------------------------------menu_bar------------------------------------
gtk_menu_bar_new( void );
gtk_menu_new( void );


GtkWidget *gtk_menu_item_new( void );

GtkWidget *gtk_menu_item_new_with_label( const char *label );

GtkWidget *gtk_menu_item_new_with_mnemnonic( const char *label );

 gtk_menu_append (GTK_MENU (file_menu), open_item);

  gtk_menu_item_set_submenu (GTK_MENU_ITEM (file_item), file_menu);


    gtk_menu_bar_append (GTK_MENU_BAR (menu_bar), file_item);

  gtk_menu_shell_append (GTK_MENU_SHELL (menu), menu_items);


  g_strdup (buf) //char buf[128],  复制字符

//activate菜单项的单击时发出的信号
// 不要gtk_show_widget (menu) 在它上点击时 它会弹出来


 g_signal_connect_swapped (G_OBJECT (button), "event",
	                      G_CALLBACK (button_press), 
                              menu);

 gint button_press( GtkWidget *widget,
                          GdkEvent *event )
{

    if (event->type == GDK_BUTTON_PRESS) {
        GdkEventButton *bevent = (GdkEventButton *) event; 
        gtk_menu_popup (GTK_MENU (widget), NULL, NULL, NULL, NULL,
                        bevent->button, bevent->time);
        /* 告诉调用代码我们已经处理了这个事件；事件传播(buck)在
         * 这里停止。 */
        return TRUE;
    }

    /* 告诉调用代码我们没有处理这个事件；继续传播它。 */
    return FALSE;
}




g_message ("M_Hello, World!\n");  //出现红色信息


GtkItemFactoryEntry menu_items[] =
{
{ "/_File", NULL, NULL, 0, "<Branch>" },   //_ 表示可用alt+F <Branch>"         -> 创建一个包含子项的项
{ "/File/_New", "<control>N", print_hello, 0, NULL },//快捷键ctrl+N ,print_hello()没有参数
{"/File/sep1", NULL, NULL, 0, "<Separator>" }, //创建一个分隔线
{"/_Help", NULL, NULL, 0, "<LastBranch>" },	//<LastBranch>"     -> 创建一个右对齐的分枝(branch)
}; 
 "<Item>" 正常
"<CheckItem>"   和   "<ToggleItem>"     很像
"<Title>" 灰色不可以使用
"<RadioItem>" 怎么分组呢?

GtkAccelGroup *accel_group = gtk_accel_group_new();
GtkItemFactory *item_factory=gtk_item_factory_new(GTK_TYPE_MENU_BAR, "<main>",	accel_group);
												//GTK_TYPE_MENU_BAR, GTK_TYPE_MENU, 或GTK_TYPE_OPTION_MENU
gtk_item_factory_get_widget(item_factory, "<main>");
---------------------------Timeouts---------------------
gint gtk_timeout_add( guint32     interval,   // 毫秒为单位
                      GtkFunction function,
                      gpointer    data );//传递给回调函数的数据
//每隔一段时间该函数



void gtk_timeout_remove( gint tag );

--------------监控IO
gint gdk_input_add( gint              source, //想监控的文件描述符
                    GdkInputCondition condition,//GDK_INPUT_READ,GDK_INPUT_WRITE
                    GdkInputFunction  function,//你想要调用的函数  ,函数参数类型看头文件
                    gpointer          data );//传递给该函数的参数


void gdk_input_remove( gint tag );//停止对文件描述符的监控
---------------程序空闲时调用一个函数
gtk_idle_add( GtkFunction function,
                   gpointer    data );



gtk_idle_remove( gint tag );

-----------------选中区
原子是一个唯一地标识一个字符串(在一个确定的显示区)的整数
GDK_PRIMARY_SELECTION常量对应于字符串"PRIMARY"

gdk_atom_intern()//根据字符串以获得对应的原子
gdk_atom_name()//获得原子的名称

GDK_CURRENT_TIME

"selection_received" 信号

GtkSelectionData结构
	type
		"STRING"，由拉丁-1(latin-1)字符组成的字符串，"ATOM"，一些原子，"INTEGER"，一个整数




	还不会用

-------drop and drag


 GDK_ACTION_COPY, GDK_ACTION_MOVE ,GDK_ACTION_LINK




gtk_drag_dest_unset()指定构件不再能接收拖动了


drag_data_received 信号





@黑体     //这种以@开头字体的 中文是返向的

GtkStyle 结构
	GdkColor　结构

GtkStateType
GtkShadowType
GdkWindow

void gtk_draw_box        (GtkStyle        *style,
			  GdkWindow       *window,
			  GtkStateType     state_type,
			  GtkShadowType    shadow_type,
			  gint             x,
			  gint             y,
			  gint             width,
			  gint             height);


void gtk_paint_flat_box   (GtkStyle        *style,
			   GdkWindow       *window,
			   GtkStateType     state_type,
			   GtkShadowType    shadow_type,
			   GdkRectangle    *area,
			   GtkWidget       *widget,
			   const gchar     *detail,
			   gint             x,
			   gint             y,
			   gint             width,
			   gint             height);


void gtk_paint_polygon 
gtk_draw_box();
gtk_draw_box_gap();
gtk_draw_string()
gtk_draw_hline()
gtk_draw_vline()
gtk_draw_ploygon()
gtk_draw_arrow()
gtk_draw_diamond()


GdkFont*            gtk_style_get_font                  (GtkStyle *style);


GtkWidget*          gtk_curve_new                       (void);
void                gtk_curve_set_curve_type            (GtkCurve *curve,
                                                         GtkCurveType type);
