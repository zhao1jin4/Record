--------------生成验证码 ,解析验证码

BufferedImage 实现  (java.awt.image 包下Interface )RenderedImage

BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
Graphics g = image.getGraphics();
...
g.dispose();
ImageIO.write(image, "JPEG", response.getOutputStream());


AffineTransform .setToIdentity()//正常状态
				.translate(x,y)
				rotate
				scale()
				shear()
		AffineTransform getRotateInstance(double theta) 
		getQuadrantRotateInstance(int numquadrants) //Quadrant  象限,
 
java.awt.font.TextLayout  (String string, Font font, FontRenderContext frc) 
	Shape getOutline(AffineTransform tx)  
	Rectangle2D getBounds()  



FontRenderContext  x=Graphics2D g2.getFontRenderContext()

Affine(几何学)的

Graphics2D g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//消剧齿

FontRenderContext frc = g2.getFontRenderContext();
Font f = new Font("Helvetica", 1, w/10);
String s = new String("The Starry Night");
TextLayout textTl = new TextLayout(s, f, frc);
AffineTransform transform = new AffineTransform();
Shape outline = textTl.getOutline(null);//要字的形
Rectangle r = outline.getBounds();
transform = g2.getTransform();
transform.translate(w/2-(r.width/2), h/2+(r.height/2));
g2.transform(transform);
g2.setColor(Color.blue);
g2.draw(outline);
  
g2.setClip(outline);//后面再画的内容只显示在这个区域内 clip(outline)
g2.drawImage(img, r.x, r.y, r.width, r.height, this); 



new RoundRectangle2D.Double(x, y,
                                   rectWidth,
                                   rectHeight,
                                   10, 10)//圆角矩形

BasicStroke   //虚线 描边
CAP_BUTT   是开口
CAP_ROUND 圆形闭口
CAP_SQUARE 方形闭口


JOIN_BEVEL 平角
JOIN_MITER 尖角
JOIN_ROUND 圆滑角

GeneralPath(int rule, int initialCapacity) //多边形 winding 绕, 缠 rule WIND_EVEN_ODD, WIND_NON_ZERO
		moveTo(x,y)
		lineTo(x,y);
		closePath()

BufferedImage bi.createGraphics();
Graphics2D  setPaint(new GradientPaint(0, 0, Color.lightGray, w-250, h, Color.blue, false))// 渐变

new GradientPaint(x,y,red,x+rectWidth, y,white)



BufferedImage bi = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
	        Graphics2D big = bi.createGraphics();
		big.setColor(Color.blue);
		big.fillRect(0, 0, 5, 5);
		big.setColor(Color.lightGray);
		big.fillOval(0, 0, 5, 5);
		Rectangle r = new Rectangle(0,0,5,5);//纹理大小
		g2.setPaint(new TexturePaint(bi, r)); //填充纹理



AlphaComposite ac =AlphaComposite.getInstance(AlphaComposite.SRC,alpha); //1不透时 

Graphics2D fill(xxx) //目标
Graphics2D setComposite(ac);
Graphics2D file(yyy) //源   alpha 有影响

Area(Shape s) 
Area intersect(Area rhs)//交集
	subtract(Area rhs)
	add(Area rhs)
	exclusiveOr(Area rhs) 

setFrame(float x,
		 float y,
		 float w,
		 float h)//设位置大小




MouseMotionListener
QuadCurve2D.Double  .setCurve(start, control, end);Point2D.Double  //一点控制线
 四方院子

Rectangle	contains(x,y)


CubicCurve2D.Double  	cubic.setCurve(start, one, two, end);;Point2D.Double//两点控制线
 立方的
 
----------------- 

java.uitl.Timer  purge() 净化, 清除, 泻药     //清除所用任务
				(使)净化, 清除, 肃清, (使)通便
		cancel();终止线程
		schema(TimerTask ,Date 或毫秒数)如在中间加一个数字会重复执行例如:
		schema(TimerTask,0,Date 或毫秒数)
				继承抽象类 TimerTask 定run()方法		
			
设计模式
	1.Prototype 原型
	
		 

-----------------------------swing
如何让JFrame,没有标题栏,

JFrame setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


button.setBorderPainted(false);
button.setFocusPainted(false);
button.setContentAreaFilled(false);
button.setPreferredSize(new Dimension(200,200));//初始化的大小
button.setBounds(x,y,weidth,height) //绝对定位大小
com.sun.awt.AWTUtilities.setWindowOpacity(frame, 0.6f);   //设置透明度
com.sun.awt.AWTUtilities.setWindowShape(frame, new Ellipse2D.Double(0, 0, w.getWidth(), w.getHeight()));//把窗体放在一个圆中 ,庶罩


JTree 的图标
这三个在文字前的不响应事件,文件夹,文件的图标;
UIManager.put("Tree.leafIcon", new ImageIcon("endnode.gif"));
UIManager.put("Tree.openIcon", new ImageIcon("expanded.gif"));
UIManager.put("Tree.closedIcon", new ImageIcon("collapsed.gif"));



UIManager.put("Tree.expandedIcon", new ImageIcon("drive.gif"));扩展-
UIManager.put("Tree.collapsedIcon", new ImageIcon("GArrow.gif"));收缩+


UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());// 看源码,windows 下是  
	com.sun.java.swing.plaf.windows.WindowsLookAndFeel  里面有
				"Tree.collapsedIcon",
				"Tree.openIcon",  
				"Tree.closedIcon",
				"Tree.expandedIcon"
				"Tree.collapsedIcon"
				"Tree.openIcon",   
				"Tree.closedIcon"

类继承JApplate
的paint(Graghic g)
{
	Graphic2D g2=(Graphic2D)g;
	g2.setStroke(new BasicStroke(5));//新笔
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        //消剧齿
	g2.draw( new RoundRectangle2D.Double(120,120,200,200,50,50));
	g2.draw( new Arc2D.Double(90, 90, 20, 30, 90, 135, Arc2D.OPEN));
	g2.setColor(Color.yellow);
	g2.fill(new RoundRectangle2D.Double(90,90,300,300, 10, 10));//Double精度高
	g2.setPaint(Color.BLACK);
	g2.draw3DRect(1,1,100,100,true);
	g2.draw(new Line2D.Double(20,20,50,20));
	g2.drawString("Line2D", 30, 30);

}
JButton 的fireActionPerformed(ActionEvent )

 

UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

 

table.setPreferredScrollableViewportSize(new Dimension(200, 200));
swing 设表的大
始大小

JEditorPane的子类是JTextPane
JLable 的setLableFor(   );
JFormattedTextField 格式化(过)的文本域 可对日期
的方法 setActionCommand();

 

				 BorderFactory.createTitledBorder("")
				BorderFactory.createCompoundBorder
	JCommpoent  的setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
				这里要一个Border                 上,左,下,右的间距

 TextArea 的.setWrapStyleWord(false);为true 时是设置换行时按单词,而不拆单词


setContenPane(xx)和getContenPane().add(XX) ;


 
JLayeredPane 对象.setLayer( Component 组件, int 层数,int position);
							如果position的值 为0表示最顶,  -1 表示最低
public Component add(Component comp, int index)

java.awt.Point 



setBounds(int x,
                      int y,
                      int width,
                      int height)


JMenu  menu.setMnemonic(KeyEvent.VK_D);  alt热键 如标题字符中有字母则有下划线
(JList)list.setSelectionMode(ListSelectionModel.SINGLE_ITERVAL_SELECTION);可按shift多选  连继的 XX(无用)
			ListSelectionModel.SINGLE_SELECTION不可多选项
					默认MULTIPLE_INTERVAL_SELECTION  打shift和ctrl


JList   HORIZONTAL_WRAP  VERTICAL_WRAP  像表


	tree.getSelectionModel().setSelectionModel
           (TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
splitPane.setOneTouchExpandable(true); 为true显示两个方向箭头
JSplitPane setDividerSize(int newSize) 分隔线大小
			
setDragEnabled(true)

java.net.URL imgURL = BorderDemo.class.getResource("path");//找资源文件




JTabbedPane 构造时,可指定标签的方向如TOP BUTTOM LEFT RIGHT


(abstract )AbstractTableModel
		 isCellEditable(int rowIndex, int columnIndex) 指定单元格是否可以修改内容
		 Class<?> getColumnClass(int columnIndex) 实现这个方法会把new Boolean 的数据变成 (JCheckBox)

 


(Toolkit.getDefaultToolkit())toolkit.beep(); 发出的声音
									getScreenSize() 


JTextArea  的setMargin(Insets 对象)文本内部与边框的距离

JProgressBar.setStringPainted(true);


JPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

setPreferredSize(Dimension)  //设置大小




Box.createRigidArea(new Dimension(0, 10) //创建一个空白区可放在布局管理器中
TitledBorder titled = BorderFactory.createTitledBorder("title");//有标题的边框
					  BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
					  BorderFactory.createMatteBorder(1, 5, 1, 1, Color.red);//上,左下右 ,逆时针
		JPanel	setBorder(titled)

JComboBox .addItemListener()


--------dnd clipboard
java.awt.datatransfer.Clipboard
javax.swing.undo 

JTextField textField.setDragEnabled(true);
  newContentPane.setOpaque(true); //content panes must be opaque
  frame.setContentPane(newContentPane);//如 setContentPane 一定要setOpaque(true)

label.setTransferHandler(new TransferHandler("text"))
TransferHandler handler=c[JComponent].getTransferHandler();
  handler.exportAsDrag(c, e[KeyEvent, MouseEvent ], TransferHandler.COPY);

JComponent 的void setTransferHandler(TransferHandler newHandler)  

JList list = new JList(listModel);
list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//单选,可以用ctrl ,shift多选

 StringSelection 实现了 Transferable 接口
Transferable 的 getTransferDataFlavors()//flavor 香料 返回数组DataFlavor[]有两个/x-java-jvm-local-objectref ,/x-java-serialized-object 元数据
			  getTransferData(DataFlavor.stringFlavor)

			boolean canImport(JComponent comp, DataFlavor[] transferFlavors)  
			boolean importData(JComponent comp, Transferable t)  
String mimeType =DataFlavor.javaJVMLocalObjectMimeType +";class=java.util.ArrayList";
new DataFlavor(mimeType);							  //application/x-java-jvm-local-objectref
new DataFlavor(ArrayList.class, "ArrayList");//mimeType=application/x-java-serialized-object  
getTransferData(DataFlavor xxx) 返回 Object //java.util.ArrayList
 


 table.getTableHeader().setReorderingAllowed(false);//表格不可以拖动列
						setResizingAllowed(boolean resizingAllowed)

DataFlavor.stringFlavor
DataFlavor.imageFlavor


JMenuItem(new DefaultEditorKit.CopyAction())

fc = new JFileChooser();
//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//设置只能选择目录
FileNameExtensionFilter filter = new FileNameExtensionFilter("zip压缩文件(*.zip)","zip");
fc.setFileFilter(filter);

fc.setMultiSelectionEnabled(true);

 JFileChooser .setControlButtonsAreShown(false);//打开,取消按钮没有
JComponent    getRootPane().setDefaultButton(clear);

int result=JOptionPane.showConfirmDialog(this, "output directory not empty ,continue?");
if(result == JOptionPane.NO_OPTION  || result == JOptionPane.CANCEL_OPTION) 
	return;
							
							
DataFlavor.javaFileListFlavor

JTextArea setMargin(new Insets(5,5,5,5));

JTextArea source;
int start = source.getSelectionStart();//文本区中选择文本的开始位置
int end = source.getSelectionEnd();
Document doc = source.getDocument();
Position p0 = doc.createPosition(start);
Position p1 = doc.createPosition(end);
 String data = source.getSelectedText();
return new StringSelection(data);//Transferable

 fileFlavor = DataFlavor.javaFileListFlavor;
  Transferable t.getTransferData(fileFlavor)//返回List<File>



JTextArea getCaretPosition() //^符号
			replaceSelection("")
			getDocument().remove(
Position  getOffset()

 setFocusable(true);  FocusListener ,Accessible(JComponent 实现了)

 requestFocusInWindow();//要得到焦点
 isFocusOwner()//是否得到焦点
 Graphics	.create();创建一个新的
			.dispose();//释放资源,可后不可以使用Graphics


InputMap imap = this.getInputMap();//JComponent,当得到焦点时,
          imap.put(KeyStroke.getKeyStroke("ctrl X"),TransferHandler.getCutAction().getValue(Action.NAME));//Action

ActionMap map = this.getActionMap();
		  map.put(TransferHandler.getCutAction().getValue(Action.NAME),     TransferHandler.getCutAction());



MouseEvent consume()//消费的这个事件,不会继续向下传递

InputEvent.CTRL_DOWN_MASK// ctrl 键

if(e.getButton()==e.BUTTON1 )//判断左键
e.getModifiersEx()==(InputEvent.SHIFT_DOWN_MASK + e.BUTTON1_DOWN_MASK)//// shift+左键

KeyEvent.getModifiersExText(e.getModifiersEx())得到修饰键的字符串（这里是"Ctrl"）

TransferHandler.COPY

KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager(); 
							addPropertyChangeListener("permanet[PropertyName]",PropertyChangeListener )

	permanent 永久的


MouseMotionListener  mouseDragged(..),mouseMoved(..)



--------undo
UndoableEdit inter;//接口
AbstractUndoableEdit aaaa;//实现了UndoableEdit

StateEditable in=null;//接口
StateEdit state=new StateEdit(in);//继承了AbstractUndoableEdit 

	
CompoundEdit

//如不是文本,而是画图形,要自己调用 addEdit(自己实现AbstractUndoableEdit中的undo,redo)

{//ShapePanel的鼠标事件
	Shape rec=new Rectangle(x,y,w,h);
	shapes.add(rec);
	((Graphics2D)g).draw(rec);
	shapeUndoManager.addEdit(new GraphEdit(rec)); //方式一 addEdit()
}				
public void paint(Graphics g) 
{
	super.paint(g);
	Graphics2D g2=((Graphics2D)g);
	for(Shape s :shapes)
	{
		g2.draw(s);
	}
}
 class GraphEdit extends AbstractUndoableEdit
 { 
	 Shape shape; 
	 public GraphEdit(Shape _shape)
	 { 
		 shape = _shape; 
	 } 
	 public void undo()
	 {
		 super.undo();
		 shapes.remove(shape); //是 ShapePanel的shapes
		 repaint(); //是 ShapePanel方法 重画调用paint方法
	 } 
	 public void redo(){ 
		 super.redo();
		 shapes.add(shape);//是 ShapePanel的shapes
		 repaint(); //是 ShapePanel方法 重画调用paint方法
	 } 
}


//-----------JDK demo/jfc/Notepad中有示例
UndoManager textUndoManager=new UndoManager() ;
JTextArea text=new JTextArea();
text.getDocument().addUndoableEditListener(new UndoableEditListener()//方式二  addUndoableEditListener
					{
					public void undoableEditHappened(UndoableEditEvent e) {
						TextPanel.this.textUndoManager.addEdit(e.getEdit());//UndoableEdit
					}});
							
if(textUndoManager.canUndo())//可用于是否按钮变可用
			textUndoManager.undo();//点了按钮后
if(textUndoManager.canRedo())
			textUndoManager.redo();		
			
textUndoManager.discardAllEdits();//在新建文档时,undo信息不要了

 
 
 
 
 new JButton("<html><b>T</b></html>")
-------------JFileChooser 可以预览图片,自定义陵形渐变,三维动画,toolip的文本 可以字体颜色,Jtable 的一个单元格是JList 有滚动条的,可显示图片,对话框中有组件,JEditorPane HTML,(javaDemo->SwingSet2)
				图片的n种 滤镜   
				代码提示 How to Write a Key Listener
						 How to Write a Undoable Edit Listener,MouseWheelListener
						 How to support Assistive Technologies 有标尺
						
C:\Program Files\Java\jdk1.6.0\demo\applets\DrawTest  Applet 例子手工画直线,点线

C:\Program Files\Java\jdk1.6.0\demo\applets\MoleculeViewer Applet 三维球
C:\Program Files\Java\jdk1.6.0\demo\applets\WireFrame

C:\Program Files\Java\jdk1.6.0\demo\applets\Clock  表
JButton(Icon icon) 
setIcon(Icon defaultIcon)
setPressedIcon(Icon pressedIcon)
setSelectedIcon(Icon selectedIcon)

JRadioButton(Icon icon) 
setRolloverIcon(Icon rolloverIcon)

---------------------sound
System.getProperty("user.dir")//用户当前目录,user.home
 AudioClip audioClip = Applet.newAudioClip(completeURL);
			audioClip.stop(); 
			audioClip.start(); 
			audioClip.loop(); 
-------------------,JInternalFrame  JDesktopPane ---JLayeredPane-----------

menu.setMnemonic(KeyEvent.VK_D); //菜单中第一个出现 D 字母下加_
 menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK)); // 显示alter +N ,CTRL_MASK


JDesktopPane 要放在一个JFrame中

JFrame setContentPane(desktop);// JDesktopPane ,如不用JDesktopPane,内激活窗口不会显示最前,最小化按钮无用
desktop.add(new JInternalFrame());


JLayeredPane layeredPane.add(label, new Integer(i));//JLabel
						add(label, new Integer(2),0)//最后参 -1 means insert at the end component 
						setSelectedIndex(2);
						moveToBack(dukeLabel);//position 1
						moveToFront(dukeLabel);//position 0
						
						setLayer(dukeLabel,
                                 layerList.getSelectedIndex(),
                                 position);//0 top  : -1 bottom;


JTable 中加入JCombo,  JTable 中加入JList? JFileChooser ? 

DefaultCellEditor(JCheckBox checkBox) 
         
DefaultCellEditor(JComboBox comboBox) 
         
DefaultCellEditor(JTextField textField)  


TableColumn sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

加数据时用	new Boolean(false) 就显示为 JCheckBox
isCellEditable(int row, int col)

TableColumnModel columnModel.getColumnIndexAtX(e.getX());// MouseEvent e ,来根据不同列生成不同的 tool tips.

显示颜色选择

        table.setDefaultRenderer(Color.class,  new ColorRenderer(true));//数据用new Color(153, 0, 153), 在表格中的显示方法( extends JLabel implements TableCellRenderer  setToolTipText(...))
        table.setDefaultEditor(Color.class,  new ColorEditor());//数据用new Color(153, 0, 153), 在修改方法(Interface TableCellEditor)
  fireEditingStopped();


如果用 JScrollPane  则要用 setPreferredScrollableViewportSize(

JScrollPane scroller = new JScrollPane(base64Text); //TextArea加滚动条
this.add(scroller);

-----------SpringLayout,BoxLayout
Container contentPane = frame.getContentPane();
SpringLayout layout = new SpringLayout();
contentPane.setLayout(layout);


JLabel label = new JLabel("Label: ");
JTextField textField = new JTextField("Text field", 15);
contentPane.add(label);
contentPane.add(textField);


layout.putConstraint(SpringLayout.WEST, label,
					 5,
					 SpringLayout.WEST, contentPane);
layout.putConstraint(SpringLayout.NORTH, label,
					 5,
					 SpringLayout.NORTH, contentPane);


layout.putConstraint(SpringLayout.WEST, textField,
					 5,
					 SpringLayout.EAST, label);
layout.putConstraint(SpringLayout.NORTH, textField,
					 5,
					 SpringLayout.NORTH, contentPane);


layout.putConstraint(SpringLayout.EAST, contentPane,
					 5,
					 SpringLayout.EAST, textField);
layout.putConstraint(SpringLayout.SOUTH, contentPane,
					 5,
					 SpringLayout.SOUTH, textField);



SpringLayout.Constraints labelCons =layout.getConstraints(label);
						labelCons.setX(Spring.constant(5));
						labelCons.setY(Spring.constant(5));
						labelCons.getConstraint(SpringLayout.EAST) //返回label 最东边的距离
									setConstraint( SpringLayout.SOUTH,xx);

		 Spring.sum(
		Spring.max(

JFrame getLayout();
JFrame	getComponents Component[]
JLabel.TRAILING //右对齐
		setLabelFor(如 JTextField)
 
 button.setAlignmentX(Component.CENTER_ALIGNMENT); //JButtion 自己放在容器约束在中心
JCheckBox addItemListener ItemEvent.getStateChange()== ItemEvent.SELECTED   , ItemEvent.DESELECTED




Box.createRigidArea(new Dimension(5,0)));
Box.createHorizontalGlue());  //Glue 胶, 胶水
new Box.Filler(minSize, prefSize, maxSize)
