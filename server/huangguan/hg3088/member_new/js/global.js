function $(){for(var e=new Array,t=0;t<arguments.length;t++){var n=arguments[t];if("string"==typeof n&&(n=document.getElementById(n)),1==arguments.length)return n;e.push(n)}return e}function $ce(e){return document.createElement(e)}Object.extend=function(e,t){for(property in t)e[property]=t[property];return e},Object.prototype.extend=function(e){return Object.extend.apply(this,[this,e])};var Class={create:function(){return function(){this.initialize.apply(this,arguments)}}};if(Function.prototype.bind=function(e){var t=this;return function(){t.apply(e,arguments)}},!window.Event)var Event=new Object;Object.extend(Event,{element:function(e){return e.target||e.srcElement},pointerX:function(e){return e.pageX||e.clientX+(document.documentElement.scrollLeft||document.body.scrollLeft)},pointerY:function(e){return e.pageY||e.clientY+(document.documentElement.scrollTop||document.body.scrollTop)},stop:function(e){e.preventDefault?(e.preventDefault(),e.stopPropagation()):e.returnValue=!1},position:function(e){for(var t=e.offsetTop,n=e.offsetLeft;e=e.offsetParent;)t+=e.offsetTop,n+=e.offsetLeft;return{top:t,left:n}},observers:!1,_observeAndCache:function(e,t,n,r){this.observers||(this.observers=[]),e.addEventListener?(this.observers.push([e,t,n,r]),e.addEventListener(t,n,r)):e.attachEvent&&(this.observers.push([e,t,n,r]),e.attachEvent("on"+t,n))},observe:function(e,t,n,r){e=$(e);r=r||!1,"keypress"==t&&(0<navigator.appVersion.indexOf("AppleWebKit")||e.attachEvent)&&(t="keydown"),this._observeAndCache(e,t,n,r)},stopObserving:function(e,t,n,r){e=$(e);r=r||!1,"keypress"==t&&(0<navigator.appVersion.indexOf("AppleWebKit")||e.detachEvent)&&(t="keydown"),e.removeEventListener?e.removeEventListener(t,n,r):e.detachEvent&&e.detachEvent("on"+t,n)}});