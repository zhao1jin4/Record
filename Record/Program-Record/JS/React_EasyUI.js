-----react  create-react-app
npm init  会提示回车生成package.json文件
npm install  react react-dom --save 依赖保存到package.json(可选 npm install  react-scripts --save 要200M)

npm install -g create-react-app
create-react-app my-app  //也会下载react, react-dom(这两个8M多), and react-scripts(这个很大,3个共200M多)...很多东西 

npm start 开发级,看报错要ping通`hostname`(start定义在package.json的 "scripts": 下   "start": "react-scripts start" ) 
#打开 http://localhost:3000 

package.json 中有"main": "index.js"

package.json 下增加如下设置代理，npm start就可debug,但必须是使用index.html中用ajax请求才行
如在浏览器无论输入什么地址还是进入index.html页??? (GET请求) ,ajax 用POST请求是可以代理的
"proxy":"http://localhost:8080/S_ReactEasyUI",

生成的index.js代码就有 
	import './index.css';
	import App from './App';

npm run build ( package.json有定义 "build": "react-scripts build", ) 生成 build 目录，生产级
提示
  npm install -g serve
  serve -s build
			 http://localhost:5000
			 
和jQuery一起使用			 
npm install -g jquery
npm install --save jquery
import $ from  'jquery'


-----EasyUI for React

2018-12-28   版本  1.0.1  
2019-03-14   版本  1.0.18
2020-03-16   版本  1.0.53

npm install -g rc-easyui
npm install rc-easyui --save 

index.css  中增加
    @import '~rc-easyui/dist/themes/default/easyui.css';
    @import '~rc-easyui/dist/themes/icon.css';
    @import '~rc-easyui/dist/themes/react.css';


App.js   中增加
    import React from 'react';
    import { DataGrid, GridColumn } from 'rc-easyui';
	
  

----------DataGrid 简单示例
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      data: this.getData()
    }
  }
  getData() {
    return [
      {"code":"FI-SW-01","name":"Koi","unitcost":10.00,"status":"P","listprice":36.50,"attr":"Large","itemid":"EST-1"},
      {"code":"K9-DL-01","name":"Dalmation","unitcost":12.00,"status":"P","listprice":18.50,"attr":"Spotted Adult Female","itemid":"EST-10"},
      {"code":"RP-SN-01","name":"Rattlesnake","unitcost":12.00,"status":"P","listprice":38.50,"attr":"Venomless","itemid":"EST-11"},
      {"code":"RP-SN-01","name":"Rattlesnake","unitcost":12.00,"status":"P","listprice":26.50,"attr":"Rattleless","itemid":"EST-12"},
      {"code":"RP-LI-02","name":"Iguana","unitcost":12.00,"status":"P","listprice":35.50,"attr":"Green Adult","itemid":"EST-13"},
      {"code":"FL-DSH-01","name":"Manx","unitcost":12.00,"status":"P","listprice":158.50,"attr":"Tailless","itemid":"EST-14"},
      {"code":"FL-DSH-01","name":"Manx","unitcost":12.00,"status":"P","listprice":83.50,"attr":"With tail","itemid":"EST-15"},
      {"code":"FL-DLH-02","name":"Persian","unitcost":12.00,"status":"P","listprice":23.50,"attr":"Adult Female","itemid":"EST-16"},
      {"code":"FL-DLH-02","name":"Persian","unitcost":12.00,"status":"P","listprice":89.50,"attr":"Adult Male","itemid":"EST-17"},
      {"code":"AV-CB-01","name":"Amazon Parrot","unitcost":92.00,"status":"P","listprice":63.50,"attr":"Adult Male","itemid":"EST-18"}
    ]
  }
  render() {
    return (
      <div>
        <DataGrid data={this.state.data} style={{height:250}}>
          <GridColumn field="itemid" title="Item ID"></GridColumn>
          <GridColumn field="name" title="Name"></GridColumn>
          <GridColumn field="listprice" title="List Price" align="right"></GridColumn>
          <GridColumn field="unitcost" title="Unit Cost" align="right"></GridColumn>
          <GridColumn field="attr" title="Attribute" width="30%"></GridColumn>
          <GridColumn field="status" title="Status" align="center"></GridColumn>
        </DataGrid>
      </div>
    );
  }
}
 
export default App;



---------DataGrid 从服务取数据fetch , jQuery ,行内编辑
import React from 'react'; 
import './App.css';

import { DataGrid, GridColumn, ComboBox, Label } from 'rc-easyui';
import { NumberBox,SwitchButton,CheckBox,TextBox } from 'rc-easyui';
import $ from  'jquery'
class App extends React.Component 
{
	constructor(props) 
	{
		super(props);
		this.state = 
	    {
			total: 0,
			pageNumber: 1,
			pageSize: 20,
			data: [],
			loading: false,
			//以下自已使用的
			pagePosition: "bottom",
			options: [
				{ value: "bottom", text: "Bottom" },
				{ value: "top", text: "Top" },
				{ value: "both", text: "Both" }
			]
	    }
	} 
	componentDidMount() 
	{
		this.loadPage(this.state.pageNumber, this.state.pageSize)
	}
	loadPage(pageNumber, pageSize)
	{
		this.setState({ loading: true })
		var that=this;
		//这时 Content-Type默认为 text/plain;charset=UTF-8 要修改为  application/x-www-form-urlencoded 
		var data = "page="+pageNumber+"&rows=20&my_custome_param=123"; 
		//方式二 		 
		// var data = new URLSearchParams();  // Content-Type默认为 application/x-www-form-urlencoded
		// data.set("my_custome_param",123);
		// data.set("page",pageNumber);
		// data.set("rows",pageSize); 
		 
		fetch('/easyUI/queryJsonData', 
			{ 
				method: "POST",

				headers: {
					//"Content-Type": "application/json;charset=UTF-8",  
					"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" 
				},
				body:data
			}
		).then(response => 
			response.json()//如debug报body stream is locked
		).then(function(result) 
		{ 
			console.log(result);
			console.log(result.rows); 
			//这里不能调用 this.setState 
			that.setState(Object.assign({}, result,
				{
					data: result.rows,
					loading: false
				})
			);
		
		});
		/* 
		setTimeout(() => {
		let result = this.getData(pageNumber, pageSize);
		this.setState(Object.assign({}, result, {
			data: result.rows,
			loading: false
		}))
		}, 1000);
		*/
	}
	getData(pageNumber, pageSize) 
	{  
		var res=null;
		var data={
			my_custome_param:"123",
			page: pageNumber,
			rows:pageSize
		};

		$.ajax
		({
			url:'/easyUI/queryJsonData',
			type:"POST",
			dataType:"json",
			async:false,
			data:data, 
			success:function(response)
			{
				res=response;				
			},error:function( jqXHR,   textStatus,   errorThrown ) 
			{ 
				alert('error,'+errorThrown);
			}
		});
		return res; 
	}
  
	handlePageChange(event) {//onPageChange 参应的事件 event做函数参数，有属性pageNumber,pageSize
		this.loadPage(event.pageNumber, event.pageSize)
	}
	render() {
		return (
		<div>
		<h2>Pagination - Lazy Load</h2>
		<div style={{ marginBottom: 10 }}>
			<Label htmlFor="c1">Pager on: </Label>  {/* htmlFor ,inputId */}
			<ComboBox inputId="c1" style={{ width: 120 }}
			data={this.state.options}
			editable={false}
			panelStyle={{ height: 'auto' }}
			value={this.state.pagePosition}
			onChange={(value) => this.setState({ pagePosition: value })}
			/>
		</div>
		<DataGrid
			style={{ height: 250 }}
			pagination //出现属性即可,prop的键可无值,默认为true?
			lazy
			{...this.state}
			onPageChange={this.handlePageChange.bind(this)}

			//行内编辑
			//clickToEdit //单击
			dblclickToEdit

			//selectionMode="cell"
			//editMode="cell"
			selectionMode="row"//默认是row
			editMode="row"
		>
				<GridColumn field="id" title="id"></GridColumn>
				<GridColumn field="username" title="Name"
					editable
					//下面可不加，有时报警告
					editor={({ row }) => (
							<TextBox value={row.username} ></TextBox>
						)}
				></GridColumn>
				<GridColumn field="hobby" title="hobby" align="right"
					editable
					editor={({ row }) => (
						<ComboBox value={row.hobby} 
							data={[{ value: "F", text: "Football" },
								{ value: "B", text: "Basketball" }]} >
						</ComboBox>
					)}
				></GridColumn>
				<GridColumn field="salary" title="salary" align="right"
					editable
					editor={({ row }) => (
					<NumberBox value={row.salary} precision={1}></NumberBox>
					)}
				 ></GridColumn>
				<GridColumn field="isMan" title="isMan" align="center"
					header={() => <span>[isMan是强转String]</span>}
					render={({ row }) => (
					<div style={{ padding: '4px 0' }}>
						<div style={{ fontSize: 20,color:'red' }}>isMan={String(row.isMan)}</div> 
					</div>
					)}
					editable
					editor={({ row }) => (
					//<SwitchButton value={row.man}></SwitchButton>
					<CheckBox checked={row.man}></CheckBox>
					)} 
				 ></GridColumn>
				<GridColumn field="birthday" title="birthday"></GridColumn>
		</DataGrid>
		</div>
    	);
	}
}
 
export default App;

---------TreeGrid 
import './App.css';

import React from 'react';
import { TreeGrid, GridColumn } from 'rc-easyui';
 
class App extends React.Component {
  constructor() {
    super();
    this.state = {
      data: this.getData()
    }
  }
  getData() {
    return [
      {
        id: 1,
        name: "C",
        size: "",
        date: "02/19/2010",
        children: [
          {
            id: 2,
            name: "Program Files",
            size: "120 MB",
            date: "03/20/2010",
            children: [
              {
                id: 21,
                name: "Java",
                size: "",
                date: "01/13/2010",
                state: "closed",
                children: [
                  {
                    id: 211,
                    name: "java.exe",
                    size: "142 KB",
                    date: "01/13/2010"
                  },
                  {
                    id: 212,
                    name: "jawt.dll",
                    size: "5 KB",
                    date: "01/13/2010"
                  }
                ]
              },
              {
                id: 22,
                name: "MySQL",
                size: "",
                date: "01/13/2010",
                state: "closed",
                children: [
                  {
                    id: 221,
                    name: "my.ini",
                    size: "10 KB",
                    date: "02/26/2009"
                  },
                  {
                    id: 222,
                    name: "my-huge.ini",
                    size: "5 KB",
                    date: "02/26/2009"
                  },
                  {
                    id: 223,
                    name: "my-large.ini",
                    size: "5 KB",
                    date: "02/26/2009"
                  }
                ]
              }
            ]
          },
          {
            id: 3,
            name: "eclipse",
            size: "",
            date: "01/20/2010",
            children: [
              {
                id: 31,
                name: "eclipse.exe",
                size: "56 KB",
                date: "05/19/2009"
              },
              {
                id: 32,
                name: "eclipse.ini",
                size: "1 KB",
                date: "04/20/2010"
              },
              {
                id: 33,
                name: "notice.html",
                size: "7 KB",
                date: "03/17/2005"
              }
            ]
          }
        ]
      }
    ];
  }
  render() {
    return (
      <div>
        <h2>Basic TreeGrid</h2>
        <TreeGrid 
          style={{ height: 250 }}
          data={this.state.data}
          idField="id"
          treeField="name"
        >
          <GridColumn field="name" title="Name"></GridColumn>
          <GridColumn field="size" title="Size"></GridColumn>
          <GridColumn field="date" title="Date"></GridColumn>
        </TreeGrid>
      </div>
    );
  }
}
 
export default App







	