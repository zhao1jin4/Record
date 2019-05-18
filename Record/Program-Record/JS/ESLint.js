  
------------------ ESLint
一个JavaScript代码规范检查工具,支持 ES2015 语法,JSX

npm install eslint -g
npm install eslint --save-dev

目前最新版本 5.16.0


./node_modules/.bin/eslint --init   建立配置文件 如使用了-g安装就可直接用 eslint --init 
有交互提示的
框架是Reac还是Vue.js
Browser 还是 Node
配置文件 默认是javascript 格式的 (可以是 YAML,JSON)  生成文件名为 .eslintrc.js 

./node_modules/.bin/eslint src/*.js      支持*这种能配符 */
npx eslint src/*.js     也可以使用npx*/


---插件
npm install  eslint-plugin-react -g
npm install eslint-plugin-react-hooks  -g
npm install eslint-plugin-react-hooks --save-dev   来强制执行这Hook规则


