-----react  create-react-app
npm init  会提示回车生成package.json文件
npm install  react react-dom --save 依赖保存到package.json(可选 npm install  react-scripts --save 要200M)

npm install -g create-react-app
create-react-app my-app  //也会下载react, react-dom(这两个8M多), and react-scripts(这个很大,3个共200M多)...很多东西 

npm start 开发级,看报错要ping通`hostname`(start定义在package.json的 "scripts": 下   "start": "react-scripts start" ) 
#打开 http://localhost:3000 

package.json 中有"main": "index.js"

package.json 下增加如下设置代理，npm start就可debug
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

npm install rc-easyui --save 

index.css  中增加
    @import '~rc-easyui/dist/themes/default/easyui.css';
    @import '~rc-easyui/dist/themes/icon.css';
    @import '~rc-easyui/dist/themes/react.css';


App.js   中增加
    import React from 'react';
    import { DataGrid, GridColumn } from 'rc-easyui';
	
	 
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














	