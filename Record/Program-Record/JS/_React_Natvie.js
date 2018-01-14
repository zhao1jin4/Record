
=============React 
http://www.runoob.com/react/react-tutorial.html
https://reactjs.org/
https://github.com/facebook/react/releases
16.2.0



npm install -g create-react-app
create-react-app my-app
cd my-app    //生成的node_modules目录非常大,100多M，2万多个文件 不适用
rm -f src/*  // */


src/下建立文件 index.css  
src/下建立文件 index.js  
	import React from 'react';
	import ReactDOM from 'react-dom';
	import './index.css';
项目目录下 npm start 打开 http://localhost:3000 










=============React Native 
写JS会生成 iOS 和 Android 的本地代码

npm install -g create-react-native-app
create-react-native-app AwesomeProject //生成的node_modules目录非常大,40多M，1万多个文件 不适用

cd AwesomeProject
npm start

