import { $APP, shadow$provide, $jscomp } from "./nbb_core.js";
const shadow_esm_import = function(x) { return import(x) };
var iZa;$APP.rV=function(a,b){const c=Array.prototype.slice.call(arguments),d=c.shift();if("undefined"==typeof d)throw Error("[goog.string.format] Template required");return d.replace(/%([0\- \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g,function(e,f,l,p,u,t,A,B){if("%"==t)return"%";const E=c.shift();if("undefined"==typeof E)throw Error("[goog.string.format] Not enough arguments");arguments[0]=E;return $APP.rV.Bf[t].apply(null,arguments)})};$APP.sV=new $APP.F(null,"token","token",-1211463215);
$APP.hZa=new $APP.F(null,"errors","errors",-908790718);iZa=new $APP.q(null,"format","format",333606761,null);/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
$APP.rV.Bf={};$APP.rV.Bf.s=function(a,b,c){return isNaN(c)||""==c||a.length>=Number(c)?a:a=-1<b.indexOf("-",0)?a+(0,$APP.xx)(" ",Number(c)-a.length):(0,$APP.xx)(" ",Number(c)-a.length)+a};
$APP.rV.Bf.f=function(a,b,c,d,e){d=a.toString();isNaN(e)||""==e||(d=parseFloat(a).toFixed(e));let f;f=0>Number(a)?"-":0<=b.indexOf("+")?"+":0<=b.indexOf(" ")?" ":"";0<=Number(a)&&(d=f+d);if(isNaN(c)||d.length>=Number(c))return d;d=isNaN(e)?Math.abs(Number(a)).toString():Math.abs(Number(a)).toFixed(e);a=Number(c)-d.length-f.length;0<=b.indexOf("-",0)?d=f+d+(0,$APP.xx)(" ",a):(b=0<=b.indexOf("0",0)?"0":" ",d=f+(0,$APP.xx)(b,a)+d);return d};
$APP.rV.Bf.d=function(a,b,c,d,e,f,l,p){return $APP.rV.Bf.f(parseInt(a,10),b,c,d,0,f,l,p)};$APP.rV.Bf.i=$APP.rV.Bf.d;$APP.rV.Bf.u=$APP.rV.Bf.d;$APP.EO.h($APP.EF,null);$APP.Wu(new $APP.h(null,2,[$APP.$t,new $APP.h(null,1,[$APP.FC,{format:$APP.rV}],null),$APP.Iq,new $APP.h(null,1,[$APP.FC,new $APP.h(null,1,[iZa,$APP.rV],null)],null)],null));