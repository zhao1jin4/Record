UI框架有 https://reactjs.org/community/ui-components.html

MaterialUI 实现google android 的Material风格 ,面向移动的
https://material-ui.com/

还有另一个项目 Material Components for the web (MDC Web) https://github.com/material-components/material-components-web 

安装 react
npm install -g create-react-app
create-react-app my-app 
cd my-app 

安装 MaterialUI
npm install -g @material-ui/core
//D:\Program\node-v10.13.0-win-x64\node_modules\@material-ui
npm install @material-ui/core
npm install -g @material-ui/icons
npm install @material-ui/icons

import React from 'react';
import ReactDOM from 'react-dom';
import Button from '@material-ui/core/Button';

function App() {
  return (
    <Button variant="contained" color="primary">
      Hello World
    </Button>
  );
}
ReactDOM.render(<App />, document.querySelector('#root'));
<div id="app"></div>  

/*
npm install  react react-dom  --save 依赖保存到package.json  
 <script src="require.js"></script>  
 <script src="babel.min.js"></script>
 
 <script src="react-16.6.1/react.production.min.js"></script>
 <script src="react-16.6.1/react-dom.production.min.js"></script>
 <script src="material-ui-3.4/material-ui.development.js"></script>


 <!-- 
<script src="https://requirejs.org/docs/release/2.3.6/minified/require.js"></script>
<script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>

<script src="https://unpkg.com/@material-ui/core/umd/material-ui.production.min.js" crossorigin="anonymous"></script>
             https://unpkg.com/@material-ui/core/umd/material-ui.development.js
  -->			 
			 
<script  type="text/babel" >
	import Button from 'material-ui-3.4/material-ui.production.min.js';
	
	// 报  global is not defined ???  require is not defined ??? 加了require.js 无法提出都不报了，但什么也没有？？？
	//require应该是node.js的
	//https://github.com/mui-org/material-ui/tree/master/examples/cdn  示例也不行
	function App() {
	  return (
		<Button variant="contained" color="primary">
		  Hello World
		</Button>
	  );
	}
	ReactDOM.render(<App />, document.querySelector('#app'));
</script>
  
<div id="app"></div>  
 
*/

//---demo.js  SVG Icons
import React from 'react'; //<project_dir>\node_modules\@types\react\index.ts
import PropTypes from 'prop-types'; //<project_dir>\node_modules\@types\prop-types\index.ts
import { withStyles } from '@material-ui/core/styles';
import red from '@material-ui/core/colors/red';
import blue from '@material-ui/core/colors/blue';
import SvgIcon from '@material-ui/core/SvgIcon';

const styles = theme => ({
  root: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'flex-end',
  },
  icon: {
    margin: theme.spacing.unit * 2,
  },
  iconHover: {
    margin: theme.spacing.unit * 2,
    '&:hover': {
      color: red[800],
    },
  },
});

function HomeIcon(props) {
  return (
    <SvgIcon {...props}>
      <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z" />
    </SvgIcon>
  );
}

function SvgIcons(props) {
  const { classes } = props;
  return (
    <div className={classes.root}>
      <HomeIcon className={classes.icon} />
      <HomeIcon className={classes.icon} color="primary" />
      <HomeIcon className={classes.icon} color="secondary" />
      <HomeIcon className={classes.icon} color="action" />
      <HomeIcon className={classes.iconHover} color="error" style={{ fontSize: 30 }} />
      <HomeIcon color="disabled" className={classes.icon} fontSize="large" />
      <HomeIcon
        className={classes.icon}
        color="primary"
        fontSize="large"
        component={svgProps => (
          <svg {...svgProps}>
            <defs>
              <linearGradient id="gradient1">
                <stop offset="30%" stopColor={blue[400]} />
                <stop offset="70%" stopColor={red[400]} />
              </linearGradient>
            </defs>
            {React.cloneElement(svgProps.children[0], { fill: 'url(#gradient1)' })}
          </svg>
        )}
      />
    </div>
  );
}

SvgIcons.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SvgIcons);


//---index.js
import Demo from './demo';
ReactDOM.render(<Demo />, document.querySelector('#root'));

 









