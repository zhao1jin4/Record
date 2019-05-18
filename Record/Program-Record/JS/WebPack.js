  
------------------ Webpack
Intellij Idea->File->Setings->Language & -Framework >JavaScript->webpack 可配置.js文件

从 4.0 版本开始可以不使用配置文件 ，如复杂的环境也可以使用配置文件
http://www.runoob.com/w3cnote/webpack-tutorial.html  还是要配置文件的

npm install webpack webpack-cli -g


mkdir webpack-demo && cd webpack-demo
npm init -y
npm install webpack webpack-cli --save-dev

--src/index.js
function component() {
  let element = document.createElement('div');

  // Lodash, currently included via a script, is required for this line to work
  element.innerHTML = _.join(['Hello', 'webpack'], ' ');

  return element;
}

document.body.appendChild(component());

---index.html 
<!doctype html>
<html>
  <head>
    <title>Getting Started</title>
    <script src="https://unpkg.com/lodash@4.16.6"></script>
  </head>
  <body>
    <script src="./src/index.js"></script>
  </body>
</html>

就可以直接用了,但lodash是手工引用的
index.html 放在dist目录下

--package.json
+   "private": true,
-   "main": "index.js",

npm install --save lodash  安装 lodash  如是开发模式用 --save-dev

--src/index.js 增加 
+ import _ from 'lodash';
其实是 node_modules/lodash/xxx.js

npx webpack  生成 dist/main.js 是压缩式的，变量一个字母
index.html  移动到dist目录, 去除那两个js引用 增加 <script src="main.js"></script>
就可以直接用了

如复杂的环境也可以使用配置文件
--- webpack.config.js
const path = require('path');

module.exports = {
  mode: 'development', //production,development 用development模式生成的js不是压缩的,但还是不能debug
  entry: './src/index.js',
  output: {
    filename: 'main.js',
    path: path.resolve(__dirname, 'dist')
  }
};

npx webpack --config webpack.config.js   如不指定默认文件名 webpack.config.js
也可以直接用
---package.json  "scripts" 区中增加子项 
+     "build": "webpack" 
运行 npm run build  相当于  npx webpack --config webpack.config.js 


========= 加载CSS
npm install -g style-loader css-loader
npm install --save-dev style-loader css-loader

webpack.config.js 在  module.exports = 下增加一个属性
+  , module: {
+     rules: [
+       {
+         test: /\.css$/,
+         use: [
+           'style-loader',
+           'css-loader'
+         ]
+       }
+     ]
+   }
--建立文件 src/style.css
.hello {
  color: red;
}
--src/index.js
+ import './style.css';
+ element.classList.add('hello');

npm run build 即  npx webpack --config webpack.config.js 
也可以直接用，样式被存到了.js文件中了

========= 加载图片
npm install --g file-loader
npm install --save-dev file-loader
webpack.config.js 在  module.exports = 下刚刚加的 rules: 属性下增加

+      , {
+         test: /\.(png|svg|jpg|gif)$/,
+         use: [
+           'file-loader'
+         ]
+       }
 
--src/icon.png
--src/index.js
+ import Icon from './icon.png';

+   var myIcon = new Image();
+   myIcon.src = Icon; 
+   element.appendChild(myIcon);
--src/style.css  .hello中加 这样会重复，可不用加
+   background: url('./icon.png');

npm run build 即  npx webpack --config webpack.config.js 
就可以直接用了

=== 最后版本是
const path = require('path');

module.exports = {
  mode: 'development', //production,development 用development模式生成的js不是压缩的,但还是不能debug
  entry: './src/index.js',
  output: {
    filename: 'main.js',
    path: path.resolve(__dirname, 'dist')
  }
  , module: {
     rules: [
       {
         test: /\.css$/,
         use: [
           'style-loader',
           'css-loader'
         ]
       }
	   , {
	      test: /\.(png|svg|jpg|gif)$/,
	      use: [
	        'file-loader'
	      ]
	    } 
		
     ]
   }  
    
};
=== 


 --watch, -w   如修改自动重新构建



