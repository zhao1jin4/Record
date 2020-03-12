https://developer.apple.com/library/ios/navigation/
Cocoapods 是iOS仓库管理 
Cocoapods 私服，很多公司使用 Git 仓库进行搭建,Cocoapods 的构建产出物通常较大，上传到 Git 仓库

//===============iOS ---Cocoa Touch
Simulator 是模拟器,不可以存数据,从AppStore下载的软件不能安装到Simulator上
Emulator是仿真器,可以存数据,

模拟器中可以用command+左,右 可旋转屏幕
模拟器Hardware->Toggle In Call Status Bar ,可以选中顶部的栏
模拟器 iOS Simulator->Reset Content and Setting
iPad 模拟器没有home键,要 硬件 菜单->首页
模拟器中按option+拖动  可以放大/缩小 图片/网页
模拟器中设所在位置 Debug->Location->custom location...
模拟器增加中文输入法 Settings->General->Keyboard->Add New Keyboard...->增加Chinese Simplied(Pinyin),Chinese Simplied(handwrite)





建立View Based Applicaton项目,resources目录下有 XXXViewController.xib格式文件,是基于XML格式,可以使用Interface Builder来编辑
XXXViewController.m文件中生成的 shouldAutoroateToInterfaceOrientation 方法,默认返回TRUE
-(BOOL)shouldAutoroateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	//自己修改的
	return (interfaceOrientation == UIInterfaceOrientationPortraint ||  //竖向的
			interfaceOrientation == UIInterfaceOrientationLandscapeLeft );//Home键在左边的横向
}

@interface XXX:UIVewController <UIAlertViewDelegate,UIActionSheetDelegate>//自己加<UIAlertViewDelegate,UIActionSheetDelegate>
{
	IBOutlet UITextField * txtName;//IBOutlet表示和界面中的组件可有绑定
	IBOutlet UIPageControl *pageControl;
	IBOutlet UIImageView *imageView1;
	IBOutlet UIImageView *imageView2;
	IBOutlet UIWebView *webView;
}
-(IBAction) btnClicked1:(id)senser;//自己新加的
@property (nonatomic,retain)UITextField * txtName;
@property (nonatomic,retain)UIPageControl * pageControl;
@property (nonatomic,retain)UIImageView * imageView1;
@property (nonatomic,retain)UIImageView * imageView2;
@property (nonatomic,retain)UIWebView * webView;
@end

右键拖动(或者ctrl+左键拖动) 按钮 -> View Controller Scence窗口中的View Controller->有evnts  btnClicked菜单,表示当按钮单击时调用的函数
右键拖动(或者ctrl+左键拖动) View Controller Scence窗口中的View Controller -> 文本框后有Outlets txtName菜单
在 按钮 或 窗口中的View Controller右键 ->菜单中有Touch up inside关联btnClicked方法
@implementation XXX
@synthesize txtName;
@synthesize pageControl;
@synthesize imageView1;
@synthesize imageView2;
@synthesize webView;

-(void)viewDidLoad //当程序启动时初始化时会
{

	//---动态增加组件
	NSString * s=@"这是一个很长的字串,要看到换行效果";
	UIFont * font=[UIFont fontWithName:@"Arial" size:50.f];
	CGSize size=CGSizeMake(320, 2000);
	UILabel *label2=[[UILabel alloc]initWithFrame:CGRectZero];
	[label2 setNumberOfLines:0];
	//根据字串的的长度,字体大小,计算出空间
	CGSize labelSize=[s sizeWithFont:font constrainedToSize:size lineBreakMode:NSLineBreakByWordWrapping];
	label2.frame=CGRectMake(0,0,labelSize.width,labelSize.height);
	label2.textColor=[UIColor blackColor];
	label2.font=font;
	label2.text=s;
	[self.view addSubview:label2];
	//[label2 release];
	//动态增加事件处理
	UIButton *button =[UIButton buttonWithType:UIButtonTypeRoundedRect];//UIButtonTypeContactAdd
	button.frame=CGRectMake(100,230,80,40);
	[button setTitle:@"动态按钮加事件" forState:UIControlStateNormal];
	button.tag=6;
   [button addTarget:self action:@selector(dynamicClick:) forControlEvents:UIControlEventTouchUpInside];//方法后要加:
   //[button removeTarget:self action:@selector(dynamicClick:) forControlEvents:UIControlEventTouchUpInside];

	[self.view addSubview:button];
 	//---DatePicker组件
	NSDate * date=[NSDate date];
	[datePicker setDate:date animated:YES];
	//---UIPickerView组件
	fontNames=[UIFont familyNames];
	fontColors=[NSArray arrayWithObjects:[UIColor redColor],[UIColor blueColor],[UIColor greenColor],[UIColor yellowColor],nil];
	fontSizes=[NSArray arrayWithObjects:@"10",@"20",@"30",@"40",nil];
	pickerView.dataSource=self;//实现协议UIPickerViewDelegate,UIPickerViewDataSource
	pickerView.delegate=self;
	//设置初始选中数据中间部分
	[pickerView selectRow:[fontNames count]/2 inComponent:0 animated:YES];
	[pickerView selectRow:[fontColors count]/2 inComponent:1 animated:YES];
	[pickerView selectRow:[fontSizes count]/2 inComponent:2 animated:YES];
	//---ProgressView
	progress.progress=0.7;//范围0-1 ,可使用NSTimer
	//---MapKit 要加库 和 #import <MapKit/MapKit.h>
	//MKMapView *map=[MKMapView alloc]initWithFrame:(CGRect);
	CLLocationCoordinate2D center;
	center.latitude = 31.11;//上海位于北纬31度11分,东经121度29分。
	center.longitude=121.29;
	MKCoordinateSpan span;
	span.latitudeDelta=0.2;//精度
	span.longitudeDelta=0.2;
	MKCoordinateRegion region={center,span};
	[self.map setRegion:region];
	//地图标记,要实现协议 MKAnnotation
	MyMapAnnotation *anno=[[MyMapAnnotation alloc]init];
	[self.map addAnnotation:anno];
	//---弹出对话框
	UIAlertView * alert=[[UIAlertView alloc]initWithTitle:@"这是AlertView" message:[txtName text] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:@"操作1",@"操作2", nil];
	[alert show];
    //[alert release];
    //---图片浏览
	[imageView1 setImage:[UIImage imageNamed:@"Beach.jpg"]];
	[imageView1 setHidden:NO];
	[imageView2 setHidden:YES];
	[pageControl addTarget:self action:@selector(pageTurning:)  forControlEvents:UIControlEventValueChanged];//方法后要加:
	
	 //---WebView
    NSString *path = [[NSBundle mainBundle] pathForResource:@"hello.html" ofType:nil];//本地URL
    NSURL *url = [NSURL fileURLWithPath:path];

    //--OC调用JS
    //NSURL *url=[NSURL URLWithString:@"http://www.google.com.hk"]; //远程URL http://threejs.org
    NSURLRequest *req = [NSURLRequest requestWithURL:url];
    [webView loadRequest:req];
    //--JS调用OC ,UIWebViewDelegate ,shouldStartLoadWithRequest
     webView.delegate=self;  
}
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType //UIWebViewDelegate
{
    //这是通过自定义仿问URL的方式,HTML文件中用document.location = "protocol://xxx",估计要用ajax请求,可是如何向JS返回结果???
    
    //获取时 url中含有中文
    NSString *urlStr = [[request.URL absoluteString]stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    //传输时 url中含有中文,js的方法 encodeURI
    //NSString *rateCardsName_CN=[[NSString stringWithFormat:@"%@",xxx]stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
  
    NSString* rurl=[[request URL] absoluteString];
    if ([rurl hasPrefix:@"protocol://"]) {
        UIAlertView* alert=[[UIAlertView alloc]initWithTitle:@"Called by JavaScript"
                                                     message:@"You've called iPhone provided control from javascript!!" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:nil];
        [alert show];
        return false;
    }
    return true;
}
- (void)webViewDidFinishLoad:(UIWebView *)webView //UIWebViewDelegate
{
    NSString *currentURL = [webView stringByEvaluatingJavaScriptFromString:@"document.location.href"];
    NSLog(@"%@",currentURL);
    NSString *title = [webView stringByEvaluatingJavaScriptFromString:@"document.title"];
    NSLog(@"%@",title);
    [webView stringByEvaluatingJavaScriptFromString:@"var script = document.createElement('script');"
     "script.type = 'text/javascript';"
     "script.text = \"function myFunction() { "
     "var field = document.getElementsByName('q')[0];"
     "field.value='中国';"
     "document.forms[0].submit();"
     "}\";"
     "document.getElementsByTagName('head')[0].appendChild(script);"];
    
    [webView stringByEvaluatingJavaScriptFromString:@"myFunction();"];
    
    //---
    NSString *filePath = [[NSBundle mainBundle] pathForResource:@"hello" ofType:@"js"];//为什么不能加载js文件???
//    NSError * err=[[NSError alloc]init];
//    NSString *jsString = [[NSString alloc] initWithContentsOfFile:filePath usedEncoding:NSUTF8StringEncoding error:&err];
//    NSLog(@"%@",jsString);
}

 -(void)pageTurning:(UIPageControl*)pageController //翻页,函数形式
{	
	NSInteger goPage=[pageController currentPage];
        switch (goPage) {
            case 0:
                [imageView1 setImage:[UIImage imageNamed:@"Beach.jpg"]];
                [imageView2 setImage:[UIImage imageNamed:@"Brushes.jpg"]];
                break;
            case 1:
                [imageView1 setImage:[UIImage imageNamed:@"Brushes.jpg"]];
                [imageView2 setImage:[UIImage imageNamed:@"Circles.jpg"]];
                break;
            case 2:
                [imageView1 setImage:[UIImage imageNamed:@"Circles.jpg"]];
                [imageView2 setImage:[UIImage imageNamed:@"Beach.jpg"]];
                break;
            default:
                break;
        }
        //left 动画
        [UIView beginAnimations:@"flipping view" context:nil];
        [UIView setAnimationDuration:0.5];
        [UIView setAnimationCurve:UIViewAnimationCurveEaseOut];
        [UIView setAnimationTransition:UIViewAnimationTransitionFlipFromLeft forView:imageView1 cache:YES];
        [UIView commitAnimations];
        //rigth 动画
        [UIView beginAnimations:@"flipping view" context:nil];
        [UIView setAnimationDuration:0.5];
        [UIView setAnimationCurve:UIViewAnimationCurveEaseOut];
        [UIView setAnimationTransition:UIViewAnimationTransitionFlipFromRight forView:imageView2 cache:YES];
        [UIView commitAnimations];
}
 -(IBAction) dynamicClick:(id)sender//为动态增加按钮的处理事件
    {
        NSLog(@"这个是动态处理方法");
    }

-(IBAction) btnClicked:(id)senser //Touch up inside 事件
{
	//下面不能放在初始化代码中,因self.view还未形成
	UIActionSheet * sheet=[[UIActionSheet alloc]initWithTitle:@"title" message:@"message" delegate:self 
										cancleButtonTitile:nil destructiveButtonTitle:@"delete message" otherButtonTitles:@"操作1",@"操作2",nil];
	//OK显示不出来
	[sheet showInView:self.view];//会调用actionSheet
	[sheet release];
	
}
//当DatePicker改变时valueChange事件调用这个,
-(IBAction)dateChanged:(id)sender
{
	NSDate *selDate=[datePicker date];
	NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
	//[formatter setDateStyle:NSDateFormatterMediumStyle];
	//[formatter setTimeStyle:NSDateFormatterMediumStyle];
	[formatter setDateFormat:@"YYYY-MM-dd HH:mm"];
	NSLog(@"NSDateFormatter: %@",[formatter stringFromDate:selDate]);
	//[formatter release];
	NSCalendar* calendar =[NSCalendar currentCalendar];
	NSDateComponents* component=[calendar components:NSYearCalendarUnit|NSMonthCalendarUnit fromDate:selDate];
	NSLog(@"NSCalendar: %d-%02d", [component year],[component month]);
	
}

-(void)alertView:(UIAlertView*)alertView clickedButtionAtIndex:(NSInteger)buttonIndex
//<UIAlertViewDelegate>中的函数, otherButtonTitles 的响应函数
{
	printf(buttonIndex);//操作1对应的buttonIndex是1 ,cancleButtonTitile对应的buttonIndex是0
}

-(void)actionSheet:(UIActionSheetView*)alertSheet clickedButtionAtIndex:(NSInteger)buttonIndex
//<UIActionSheetDelegate>中的函数, otherButtonTitles 的响应函数
{
	printf(buttonIndex); //delete message对应的buttonIndex是0 ,操作1对应的buttonIndex是1 ,在外面点是cancleButtonTitile的值
}
#pragma mark ---开始自定义PickerView
    -(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
    { //UIPickerViewDataSource 的方法
        return 3;
    }

    -(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
    {//UIPickerViewDataSource 的方法
        if(component == 0)
            return [fontNames count];
        else if(component == 1)
            return [fontColors count];
        else if(component == 2)
            return [fontSizes count];
        return -1;
    }

    -(CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component
    {//UIPickerViewDelegate 的方法
        if(component == 0)
            return 200.f;
        else if(component == 1)
            return 90.0f;
        else if(component == 2)
            return 50.0;
        return 0.0f;
    }

     -(CGFloat)pickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component
    { //UIPickerViewDelegate 的方法
        return 50.0f;
    }

    -(UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view
    {//UIPickerViewDelegate 的方法,自定义内部组件
        CGFloat  width=[self pickerView:pickerView widthForComponent:component];//调用自己实现的,得到宽
        CGFloat  height=[self pickerView:pickerView rowHeightForComponent:component];
        UIView *myView=[[UIView alloc]init];
        myView.frame=CGRectMake(0.0f,0.0f,width,height-10.0f);
        UILabel *label=[[UILabel alloc]init];
        label.tag=200;//为了后面的选择用
        label.frame=myView.frame;
        [myView addSubview:label];
        //[label release];
       
        if(component ==0)
            label.text=[fontNames objectAtIndex:row];
        else if(component==1)
            label.backgroundColor=[fontColors objectAtIndex:row];
        else if(component==2)
            label.text=[fontSizes objectAtIndex:row];
        
        return myView;
    }

    /*-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
     {//UIPickerViewDataSource 的方法,titleForRow和viewForRow只能有一个方法存在
        return [fontNames objectAtIndex:row];
    }*/

    //UIPickerViewDelegate  的方法,选中后的回调
    -(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
    {
       //UIFont 不能修改字体名,只能每初始化
        UIView *viewFont = [pickerView viewForRow:[pickerView selectedRowInComponent:0] forComponent:0];//只有自己实现了ViewForRow方法,才可调用
        UIView *viewColor = [pickerView viewForRow:[pickerView selectedRowInComponent:1] forComponent:1];
        UIView *viewSize = [pickerView viewForRow:[pickerView selectedRowInComponent:2] forComponent:2];

        UILabel *selectFont=(UILabel*)[viewFont viewWithTag:200];
        UILabel *selectColor=(UILabel*)[viewColor viewWithTag:200];
        UILabel *selectSize=(UILabel*)[viewSize viewWithTag:200];
    
        UIFont *font=[UIFont fontWithName:[selectFont text] size:[selectSize.text floatValue]];
        [labelFont setFont:font];
        [labelFont setTextColor:selectColor.backgroundColor];
    }
#pragma mark ---结束自定义PickerView
@end

  
imageView 组件的Tag属性,当ID使用
pageControl组件

Tabbed Bar组件大纲中选中Tab Bar Item，在Attriubte inspector中,badge和indentifier属性 
----Table View
关联TableView的datasource和delegate,tableView的style是Grouped

<UITableViewDataSource,UITableViewDelegate>
UISearchBar *search;

NSString *path =[[NSBundle mainBundle]pathForResource:@"table_data" ofType:@".plist"];
NSDictionary *dict=[[NSDictionary alloc]initWithContentsOfFile:path];

//UITableViewDataSource
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [self.depts count];
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSString *dept=[self.depts objectAtIndex:section ];
    NSArray * emps= [self.data  objectForKey:dept];
    return [emps count];
}
-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return  [self.depts objectAtIndex:section ];
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Cell"];//对数量较多的单元格,可重新使用不显示在屏幕中的
    if(cell==nil)
        cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"Cell"];
    
    NSArray* depts=[self.data objectForKey: [self.depts objectAtIndex:indexPath.section]];
    cell.textLabel.text=[depts objectAtIndex:indexPath.row];
    cell.imageView.image=[UIImage imageNamed:@"apple.jpg"];
    return cell;
}

-(NSString *)tableView:(UITableView *)tableView titleForFooterInSection:(NSInteger)section
{
    return @"-----";
}

//UITableViewDelegate
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"选中的组号=%d,行号=%d",indexPath.section,indexPath.row);
}
-(NSInteger)tableView:(UITableView *)tableView indentationLevelForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return  indexPath.row%2;
}
----上 Table View

-(void)viewDidAppear:(BOOL)animated
{
    keyboardShown=NO;
    //注册键盘的通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWasShown:)  name:UIKeyboardDidShowNotification object:nil];
}
-(void)viewDidDisappear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter]  removeObserver:self name:UIKeyboardDidShowNotification object:nil];
}
//--键盘
- (void)textFieldDidBeginEditing:(UITextField *)textField  //UITextFieldDelegate
{
    activeField = textField;
}
- (void)keyboardWasShown:(NSNotification*)aNotification
{
    if (keyboardShown)
        return;
    NSDictionary* info = [aNotification userInfo];
    NSValue* aValue = [info objectForKey:UIKeyboardFrameEndUserInfoKey];//得到键盘的大小
    CGSize keyboardSize = [aValue CGRectValue].size;//iPad高318
    CGRect viewFrame = [scrollView frame];
    viewFrame.size.height -= keyboardSize.height;//scrollView变小
    scrollView.frame = viewFrame;
    CGRect textFieldRect = [activeField frame];
    [scrollView scrollRectToVisible:textFieldRect animated:YES];//滚动srollView到指定组件
    keyboardShown = YES;
}


可以拖一个ViewController中的按钮到另一个ViewController -> modal 
也可以拖一个ViewController到另一个ViewController ->modal 在Attribute Inspector中修改Identifier 起个名字“goSecond”
if(self.swither.isOn)
	[self performSegueWithIdentifier:@"goSecond" sender:self];//storyboard 方式
else
	[self performSegueWithIdentifier:@"goThird" sender:self];
[self dismissViewControllerAnimated:YES completion:nil];//对组件以modal的方式的返回


可以拖一个ViewController中的按钮到另一个ViewController -> push,要求前面是一个NavigationController 以Relationship Segue拖入
push方式多一个工具栏自带返回按钮


===========OpenGL ES 2
CAEAGLLayer  , CA=CoreAnimation ,EA=ExtendAnimation ,

===========OpenAL

===========只可在真机测试的

方向(指南针),加速度,距离,GPRS定位,多于两点的触摸

 //C style 怎么写
-(void)log:(NSString * )text
{
    NSLog(text);
    NSString *out=[NSString stringWithFormat:@"%@\n %@",output.text,text];
    [output setText:@""];
    [output setText:out];//UITextView
}

//---系统版本
UIDevice *currentDev=[UIDevice currentDevice];
NSString *version=[NSString stringWithFormat:@"当前设备系统: %@,版本: %@d", currentDev.systemName,currentDev.systemVersion];
[self log:version];

//---设备方向
NSString *orien;
switch(currentDev.orientation)
{            
	case  UIDeviceOrientationPortrait:
		orien=@"肖像";
		break;
	case  UIDeviceOrientationPortraitUpsideDown:
		orien=@"反肖像";
		break;
	case  UIDeviceOrientationLandscapeLeft:
		orien=@"风景左";
		break;
	case  UIDeviceOrientationLandscapeRight:
		orien=@"风景右";
		break;
	case  UIDeviceOrientationFaceUp:
		orien=@"正面上";
		break;
	case  UIDeviceOrientationFaceDown:
		orien=@"正面下";
		break;
	case  UIDeviceOrientationUnknown:
		orien=@"未知";
		break;
}
NSString *orienLog=[NSString stringWithFormat:@"当前设备的方向是: %@",orien];
[self log:orienLog]

//---空间状态
NSFileManager *fm=[NSFileManager defaultManager];
NSError *myError=[[NSError alloc]init];
NSDictionary *attr=[fm attributesOfFileSystemForPath:NSHomeDirectory() error:&myError];
if(myError.code)
{
	NSString *diskError=[NSString stringWithFormat:@"查可用存储空间错误,%@",[myError userInfo]];
	[self log:diskError];
}else{
	long long val=[[attr objectForKey:NSFileSystemFreeSize]longLongValue];
	NSString *disk=[NSString stringWithFormat:@"存储可用空间:%lld MB",(val/1024)/1024];//long long使用%lld格式化输出
	[self log:disk];
}
    
//---电池状态
[UIDevice currentDevice].batteryMonitoringEnabled = YES;
NSString *state;
switch (currentDev.batteryState) {
	case UIDeviceBatteryStateUnplugged:
		state=@"未插电";
		break;
	case UIDeviceBatteryStateCharging:
		state=@"正在充电";
		break;
	case UIDeviceBatteryStateFull:
		state=@"已充满";
		break;
	case UIDeviceBatteryStateUnknown:
		state=@"未知";
		break;
	default:
		break;
}
NSString *battery=[NSString stringWithFormat:@"可用电量:%0.2f% ,电池状态:%@",currentDev.batteryLevel*100,state];
[self log:battery];
 

//---指南针
//指南针,不能自动旋转屏幕,要在.plist文件中的Supported Interface orientations和(iPad)两个中删除只留一个Portrait(bottom home button)
compassLocationManager = [[CLLocationManager alloc] init];
compassLocationManager.delegate = self;
if(CLLocationManager.headingAvailable)
{
	[compassLocationManager startUpdatingHeading];
	//[compassLocationManager stopUpdatingLocation];
}
else
{
	[self log:@"指南针无效"];
	arrow.alpha=0.0f;
};

//指南针
#define toRad(X) (X*M_PI/180.0)
-(void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading
{
    if (newHeading.headingAccuracy < 0)
        return;
    
    CLLocationDirection  theHeading = ((newHeading.trueHeading > 0) ?//0表示北,90表示东,180表示南,负数不能确定
                                       newHeading.trueHeading : newHeading.magneticHeading);
    [UIView     animateWithDuration:0.3
                              delay:0.0
                            options:UIViewAnimationOptionBeginFromCurrentState | UIViewAnimationOptionCurveEaseOut | UIViewAnimationOptionAllowUserInteraction
                         animations:^{
                             CGAffineTransform headingRotation;
                             headingRotation = CGAffineTransformRotate(CGAffineTransformIdentity, (CGFloat)-toRad(theHeading));
                             arrow.transform = headingRotation;
                         }
                         completion:^(BOOL finished) {
                             
                         }];
}
 
//---多点触摸 继承自UIView,在drawRect()中画,刷新调用[self setNeedsDisplay];

-(void)drawRect:(CGRect)rect //继承自UIView,setNeedsDisplay后调用
{
    CGContextRef context = UIGraphicsGetCurrentContext();
     [self drawRectangle:context];//两指触模
}//启用多点触摸
-(BOOL)isMultipleTouchEnabled
{
    return  YES;
}
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch * touch=[touches anyObject];
    if([touch tapCount]==2)
    {
        _draw=@"circle";
        self.centerPoint=[touch locationInView:self];
        [self setNeedsDisplay];//调用drawRect
        return ;
    }
    
    if([touches count]==1)
    {
        _draw=@"line";
        self.beginPoint = [touch locationInView:self];
    }
}
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    if([touches count]==1)
    {
        UITouch *touch = [touches anyObject];
        self.endPoint = [touch locationInView:self];
    }else if([touches count]==2)
    {
        NSArray *fingers=[touches allObjects];
        UITouch *first=[fingers objectAtIndex:0];
        UITouch *second=[fingers objectAtIndex:1];
        
        self.beginPoint=[first locationInView:self];
        self.endPoint=[second locationInView:self];
    }else if([touches count]==3)
    {
        
        NSArray *fingers=[touches allObjects];
        
        UITouch *first=[fingers objectAtIndex:0];
        UITouch *second=[fingers objectAtIndex:1];
        UITouch *third=[fingers objectAtIndex:2];
        
        CGPoint p_first=[first locationInView:self];
        CGPoint p_second=[second locationInView:self];
        CGPoint p_third=[third locationInView:self];
        [_array_points_1 addObject:[NSValue valueWithCGPoint:p_first]];
        [_array_points_2 addObject:[NSValue valueWithCGPoint:p_second]];
        [_array_points_3 addObject:[NSValue valueWithCGPoint:p_third]];
        
    }    
    [self setNeedsDisplay];//调用drawRect
}
- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    [_array_points_1 removeAllObjects];
    [_array_points_2 removeAllObjects];
    [_array_points_3 removeAllObjects];
}
//自定义方法
- (void)drawLine:(CGContextRef)_context  //OK
{
    CGContextMoveToPoint(_context, _beginPoint.x, _beginPoint.y);
    CGContextAddLineToPoint(_context, _endPoint.x, _endPoint.y);
    CGContextSetLineWidth(_context, 5);
    CGContextSetRGBFillColor(_context, 0, 0, 0, 1);
    CGContextStrokePath(_context);
}

- (void)drawRectangle:(CGContextRef)_context
{
    CGRect rect = CGRectMake(_beginPoint.x, _beginPoint.y, _endPoint.x - _beginPoint.x, _endPoint.y - _beginPoint.y);
    CGContextSetLineWidth(_context, 5);
    CGContextSetRGBFillColor(_context, 0.5, 0.2, 0, 1);
    CGContextFillRect(_context, rect);
    CGContextStrokePath(_context);
    
    CGContextAddRect(_context,rect);
    CGContextSetLineWidth(_context, 3);
    CGContextSetRGBStrokeColor(_context, 0, 0, 0, 1);
    CGContextStrokePath(_context);
}

- (void)drawCurve:(CGContextRef)_context
{
    CGContextSetLineWidth(_context, 2);
    //----
    //1
    CGPoint addPoint[[_array_points_1 count]];
    NSLog(@"%d",[_array_points_1 count]);

    for(int i = 0;i<[_array_points_1 count];i++)
    {
        addPoint[i] = [[_array_points_1 objectAtIndex:i] CGPointValue];
    }
    CGContextAddLines(_context, addPoint, sizeof(addPoint)/sizeof(addPoint[0]));//count
 
    //2
    CGPoint addPoint_2[[_array_points_2 count]];
    for(int i = 0;i<[_array_points_2 count];i++)
    {
        addPoint_2[i] = [[_array_points_2 objectAtIndex:i] CGPointValue];
    }
    CGContextAddLines(_context, addPoint_2, sizeof(addPoint_2)/sizeof(addPoint_2[0]));
        
    //3
    CGPoint addPoint_3[[_array_points_3 count]];
    for(int i = 0;i<[_array_points_3 count];i++)
    {
        addPoint_3[i] = [[_array_points_3 objectAtIndex:i] CGPointValue];
    }
    CGContextAddLines(_context, addPoint_3, sizeof(addPoint_3)/sizeof(addPoint_3[0]));
  
    //--
    CGContextStrokePath(_context);  
    
}
- (void)drawCircle:(CGContextRef)_context
{
    NSLog(@"in drawCircle");
    CGContextSetLineWidth(_context, 5);
    CGContextSetRGBStrokeColor(_context, 0, 1, 0, 1);
    CGContextAddArc(_context, self.centerPoint.x, self.centerPoint.y, 20.0, 0, 360, 0);
    CGContextStrokePath(_context);
}

//---多指 GestureRecognizer
[self.gestureView setUserInteractionEnabled:YES];  

UIPanGestureRecognizer* panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panDetected:)];
[panGesture setMaximumNumberOfTouches:3];//可设几点触模
[panGesture setMinimumNumberOfTouches:3];//
panGesture.delaysTouchesEnded = NO;
//panGesture.delegate=self;//UIGestureRecognizerDelegate
[self.gestureView addGestureRecognizer:panGesture];

- (void)panDetected:(UIPanGestureRecognizer*)gestureRecognizer
{//移动图标
    switch ([gestureRecognizer state])
    {
        case UIGestureRecognizerStateBegan:
        {
            CGPoint pt = [gestureRecognizer locationInView:self.view];
            NSLog(@"pt.x=%f,pt.y=%f",pt.x,pt.y);
            break;
        }
        case UIGestureRecognizerStateChanged://移动图标
        {
            CGPoint translation = [gestureRecognizer translationInView:[self.gestureView superview]];
            [self.gestureView setCenter:CGPointMake([self.gestureView center].x + translation.x, [self.gestureView center].y + translation.y)];
            [gestureRecognizer setTranslation:CGPointZero inView:[self.gestureView superview]];//清零
            break;
        }
        case UIGestureRecognizerStateEnded:
        {
            break;
        }
        default:
            break;
    }
}

//下面两个有效果,但有点小问题
UIPinchGestureRecognizer *pinchRecognizer = [[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(scale:)];
//[pinchRecognizer setDelegate:self];
[self.gestureView addGestureRecognizer:pinchRecognizer];

UIRotationGestureRecognizer *rotationRecognizer = [[UIRotationGestureRecognizer alloc] initWithTarget:self action:@selector(rotate:)];
//[rotationRecognizer setDelegate:self];
[self.gestureView addGestureRecognizer:rotationRecognizer];
//--

//----摄像头

UIImagePickerController *camera = [[UIImagePickerController alloc] init];
camera.delegate = self;//UIImagePickerControllerDelegate
camera.allowsEditing = YES;

//检查摄像头是否支持摄像机模式
if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
{
	camera.sourceType = UIImagePickerControllerSourceTypeCamera;
	camera.mediaTypes = [UIImagePickerController availableMediaTypesForSourceType:UIImagePickerControllerSourceTypeCamera];
}
else
{
	[self log:@"本机没有Camera "];
	return;
}
camera.videoQuality = UIImagePickerControllerQualityTypeHigh;//UIImagePickerControllerQualityTypeMedium
camera.videoMaximumDuration = 30.0f; //秒
//[self presentModalViewController:camera animated:YES];//过时API
[self presentViewController:camera animated:YES completion:^{  }];
    
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info//UIImagePickerControllerDelegate
{
	[picker dismissModalViewControllerAnimated:YES];
    
    NSLog(@"info = %@",info);
    
	NSString *mediaType = [info objectForKey:UIImagePickerControllerMediaType];
	if([mediaType isEqualToString:@"public.movie"])			//被选中的是视频
	{
		NSURL *url = [info objectForKey:UIImagePickerControllerMediaURL];
	}
	else if([mediaType isEqualToString:@"public.image"])	//被选中的是图片
	{
        //获取照片实例
		UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];
	}
}
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker//UIImagePickerControllerDelegate的方法
{
	NSLog(@"Cameral 取消了");
	[picker dismissModalViewControllerAnimated:YES];
}

//---加速度 加CoreMontion.framework,#import <CoreMotion/CoreMotion.h>
 motionManager = [[CMMotionManager alloc] init];
[motionManager startAccelerometerUpdatesToQueue:[[NSOperationQueue alloc] init] withHandler:^(CMAccelerometerData *accelerometerData, NSError *error) {
     //这里会不停的被调用1
        
        dispatch_sync(dispatch_get_main_queue(), ^(void) {
            //这里会不停的被调用2
            CGRect imgFrame = imgView.frame;//imgFrame是临时的
            NSLog(@"x increment valu=%f",accelerometerData.acceleration.x * 4);
            imgFrame.origin.x += accelerometerData.acceleration.x * 4;
            if(!CGRectContainsRect(self.view.bounds, imgFrame))
               imgFrame.origin.x = imgView.frame.origin.x;
            
            imgFrame.origin.y -= accelerometerData.acceleration.y * 4; //减法,方向是反的
            if(!CGRectContainsRect(self.view.bounds, imgFrame))
              imgFrame.origin.y = imgView.frame.origin.y;
           
           NSLog(@"before imgView.frame    ,x=%f,y=%f",imgView.frame.origin.x ,imgView.frame.origin.y);
           imgView.frame = imgFrame;//这里修改下次读的值还是老值,每次都一样,无效???

           NSLog(@"imgFrame.origin ,x=%f,y=%f",imgFrame.origin.x      ,imgFrame.origin.y);
           NSLog(@"imgView.frame    ,x=%f,y=%f",imgView.frame.origin.x ,imgView.frame.origin.y);
            
            accelerationX.text = [NSString stringWithFormat:@"accelerationX : %f", accelerometerData.acceleration.x];
            accelerationY.text = [NSString stringWithFormat:@"accelerationY : %f", accelerometerData.acceleration.y];
            accelerationZ.text = [NSString stringWithFormat:@"accelerationZ : %f", accelerometerData.acceleration.z ];//不动时保持在-0.98左右,向下加速变到-1.x
        });
    }];
if ([motionManager isAccelerometerActive])
        [motionManager stopAccelerometerUpdates];
//----map      
-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation//MKMapViewDelegate的方法
{
    //MKMap 组件选中Shows User Location,调用此方法,会弹出话框问是否允许仿问位置,如不能连网的错误处理???
     NSLog(@"in didUpdateUserLocation latitude=%f , longitude=%f", userLocation.location.coordinate.latitude, userLocation.location.coordinate.longitude);
    CLLocationCoordinate2D coordinate = userLocation.location.coordinate;
    MKCoordinateRegion reg =MKCoordinateRegionMakeWithDistance(coordinate, 600, 600);//加MapKit.framework和#import <MapKit/MapKit.h>
    mapView.region = reg;
}
-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations//CLLocationManagerDelegate的方法
{
    NSString *location=[NSString stringWithFormat:@"in locationManager latitude=%f , longitude=%f", manager.location.coordinate.latitude, manager.location.coordinate.longitude];
    [self log:location];
}
-(void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation//CLLocationManagerDelegate的方法
{
    NSString *err=[NSString stringWithFormat:@"新位置信息:%@,速度=%@", [newLocation description],newLocation.speed];//description是NSString的,,有altitude海拔,coordinate,
    //course如0表示北,90东,180南,270西(使用CLHeading)
    [self log:err];
}
-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error//CLLocationManagerDelegate的方法
{
    NSString *err=[NSString stringWithFormat:@"定位服务失败,原因:%@", [error description]];
    [self log:err];
}
//----蓝牙 聊天
#import <GameKit/GameKit.h>   //只可两台设备之间连接,不可第三台,可用模拟器

 NSUUID *uuid=[[UIDevice currentDevice] name];
loginId=  [uuid description];

- (IBAction)bluetoothConnect:(id)sender {
 	
    picker = [[GKPeerPickerController alloc] init];
    picker.delegate = self;//GKPeerPickerControllerDelegate
    picker.connectionTypesMask = GKPeerPickerConnectionTypeNearby;
    [picker show];
}

//GKPeerPickerControllerDelegate,弹出连接窗口之前调用，确定连接类型
- (GKSession *) peerPickerController:(GKPeerPickerController *)picker sessionForConnectionType:(GKPeerPickerConnectionType)type {
     NSLog(@"bluetooth 建立连接之前 1");
    currentSession = [[GKSession alloc] initWithSessionID:@"MyBluetoothAppID" displayName:nil sessionMode:GKSessionModePeer];
    return currentSession;
}

-(void)peerPickerController:(GKPeerPickerController *)picker didConnectPeer:(NSString*)peerID toSession:(GKSession *)session{
    NSLog(@"bluetooth 接受了连接 2");
	session.delegate=self;
	[session setDataReceiveHandler:self withContext:nil];
	currentSession=session;
	picker.delegate=nil;
	[picker dismiss];
	
}
//对应setDataReceiveHandler必须是
-(void)receiveData:(NSData*)data fromPeer:(NSString*)peer inSession:(GKSession*)session context:(void*)context
{
	NSString* aStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
   [self log:aStr];//接受数据
}
//GKSessionDelegate
-(void)session:(GKSession*)session peer:(NSString*)peerID didChangeState:(GKPeerConnectionState)state
{
	switch (state) {
		case GKPeerStateConnected:
			[currentSession setDataReceiveHandler :self withContext:nil];
            //setDataReceiveHandler必须是 - (void) receiveData:(NSData *)data fromPeer:(NSString *)peer inSession: (GKSession *)session context:(void *)context;
            [self log:@"bluetooth 连接了 3"];
			break;
		case GKPeerStateDisconnected:
			NSLog(@"bluetooth 连接断开");
			currentSession=nil;
			break;
	}
}

//GKPeerPickerControllerDelegate
- (void)peerPickerControllerDidCancel:(GKPeerPickerController *)picker {
    printf("bluetooth 连接尝试被取消 \n");
}
- (IBAction)bluetoothSend:(id)sender {
	NSString *strSend=[NSString stringWithFormat:@"%@ say: %@",loginId,txtLine.text];
    NSData* data=[strSend dataUsingEncoding:NSUTF8StringEncoding];
    txtLine.text=@"";
    if (currentSession) {
        NSError* err;
		[currentSession sendDataToAllPeers:data withDataMode:GKSendDataReliable error:&err];
         if(err)
            [self log:err.description];

	}
}
//---- 

#import <CoreBluetooth/CoreBluetooth.h>

uuidgen命令 生成一个UUID

RSSI-Received Signal Strength Indicator

CBPeripheralManager 周边,服务端
CBCentralManager  客户端


