!function(){"use strict";var t,e,a={359:function(t,e,a){var n,o=this&&this.__values||function(t){var e="function"==typeof Symbol&&Symbol.iterator,a=e&&t[e],n=0;if(a)return a.call(t);if(t&&"number"==typeof t.length)return{next:function(){return t&&n>=t.length&&(t=void 0),{value:t&&t[n++],done:!t}}};throw new TypeError(e?"Object is not iterable.":"Symbol.iterator is not defined.")};Object.defineProperty(e,"__esModule",{value:!0}),e.ConfigMacrosConfiguration=void 0;var i=a(251),r=a(74),p=a(871),l=a(945),s=a(924),u=a(432),c=a(975),M="configmacros-map",f="configmacros-env-map";e.ConfigMacrosConfiguration=i.Configuration.create("configmacros",{init:function(t){new p.CommandMap(M,{},{}),new p.EnvironmentMap(f,l.default.environment,{},{}),t.append(i.Configuration.local({handler:{macro:[M],environment:[f]},priority:3}))},config:function(t,e){!function(t){var e,a,n=t.parseOptions.handlers.retrieve(M),i=t.parseOptions.options.macros;try{for(var r=o(Object.keys(i)),p=r.next();!p.done;p=r.next()){var l=p.value,c="string"==typeof i[l]?[i[l]]:i[l],f=Array.isArray(c[2])?new s.Macro(l,u.default.MacroWithTemplate,c.slice(0,2).concat(c[2])):new s.Macro(l,u.default.Macro,c);n.add(l,f)}}catch(t){e={error:t}}finally{try{p&&!p.done&&(a=r.return)&&a.call(r)}finally{if(e)throw e.error}}}(e),function(t){var e,a,n=t.parseOptions.handlers.retrieve(f),i=t.parseOptions.options.environments;try{for(var r=o(Object.keys(i)),p=r.next();!p.done;p=r.next()){var l=p.value;n.add(l,new s.Macro(l,u.default.BeginEnv,[!0].concat(i[l])))}}catch(t){e={error:t}}finally{try{p&&!p.done&&(a=r.return)&&a.call(r)}finally{if(e)throw e.error}}}(e)},items:(n={},n[c.BeginEnvItem.prototype.kind]=c.BeginEnvItem,n),options:{macros:r.expandable({}),environments:r.expandable({})}})},955:function(t,e){MathJax._.components.global.isObject,e.BO=MathJax._.components.global.combineConfig,MathJax._.components.global.combineDefaults,e.r8=MathJax._.components.global.combineWithMathJax,MathJax._.components.global.MathJax},74:function(t,e){Object.defineProperty(e,"__esModule",{value:!0}),e.APPEND=MathJax._.util.Options.APPEND,e.REMOVE=MathJax._.util.Options.REMOVE,e.Expandable=MathJax._.util.Options.Expandable,e.expandable=MathJax._.util.Options.expandable,e.makeArray=MathJax._.util.Options.makeArray,e.keys=MathJax._.util.Options.keys,e.copy=MathJax._.util.Options.copy,e.insert=MathJax._.util.Options.insert,e.defaultOptions=MathJax._.util.Options.defaultOptions,e.userOptions=MathJax._.util.Options.userOptions,e.selectOptions=MathJax._.util.Options.selectOptions,e.selectOptionsFromKeys=MathJax._.util.Options.selectOptionsFromKeys,e.separateOptions=MathJax._.util.Options.separateOptions},251:function(t,e){Object.defineProperty(e,"__esModule",{value:!0}),e.Configuration=MathJax._.input.tex.Configuration.Configuration,e.ConfigurationHandler=MathJax._.input.tex.Configuration.ConfigurationHandler,e.ParserConfiguration=MathJax._.input.tex.Configuration.ParserConfiguration},945:function(t,e){Object.defineProperty(e,"__esModule",{value:!0}),e.default=MathJax._.input.tex.ParseMethods.default},924:function(t,e){Object.defineProperty(e,"__esModule",{value:!0}),e.Symbol=MathJax._.input.tex.Symbol.Symbol,e.Macro=MathJax._.input.tex.Symbol.Macro},871:function(t,e){Object.defineProperty(e,"__esModule",{value:!0}),e.AbstractSymbolMap=MathJax._.input.tex.SymbolMap.AbstractSymbolMap,e.RegExpMap=MathJax._.input.tex.SymbolMap.RegExpMap,e.AbstractParseMap=MathJax._.input.tex.SymbolMap.AbstractParseMap,e.CharacterMap=MathJax._.input.tex.SymbolMap.CharacterMap,e.DelimiterMap=MathJax._.input.tex.SymbolMap.DelimiterMap,e.MacroMap=MathJax._.input.tex.SymbolMap.MacroMap,e.CommandMap=MathJax._.input.tex.SymbolMap.CommandMap,e.EnvironmentMap=MathJax._.input.tex.SymbolMap.EnvironmentMap},975:function(t,e){Object.defineProperty(e,"__esModule",{value:!0}),e.BeginEnvItem=MathJax._.input.tex.newcommand.NewcommandItems.BeginEnvItem},432:function(t,e){Object.defineProperty(e,"__esModule",{value:!0}),e.default=MathJax._.input.tex.newcommand.NewcommandMethods.default}},n={};function o(t){var e=n[t];if(void 0!==e)return e.exports;var i=n[t]={exports:{}};return a[t].call(i.exports,i,i.exports,o),i.exports}t=o(955),e=o(359),(0,t.r8)({_:{input:{tex:{configmacros:{ConfigMacrosConfiguration:e}}}}}),function(e,a,n){var o,i,r,p=MathJax.config.tex;if(p&&p.packages){var l=p.packages,s=l.indexOf(e);s>=0&&(l[s]=a),n&&p[e]&&((0,t.BO)(p,(o={},i=a,r=p[e],i in o?Object.defineProperty(o,i,{value:r,enumerable:!0,configurable:!0,writable:!0}):o[i]=r,o)),delete p[e])}}("configMacros","configmacros",!1)}();