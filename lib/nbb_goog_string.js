import { $APP, shadow$provide, $jscomp } from "./nbb_core.js";
const shadow_esm_import = function(x) { return import(x) };
var xYa;$APP.A_=function(a,b){const c=Array.prototype.slice.call(arguments),d=c.shift();if("undefined"==typeof d)throw Error("[goog.string.format] Template required");return d.replace(/%([0\- \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g,function(e,f,h,n,t,q,x,z){if("%"==q)return"%";const E=c.shift();if("undefined"==typeof E)throw Error("[goog.string.format] Not enough arguments");arguments[0]=E;return $APP.A_.af[q].apply(null,arguments)})};$APP.wYa=new $APP.G(null,"errors","errors",-908790718);
xYa=new $APP.r(null,"format","format",333606761,null);/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
$APP.A_.af={};$APP.A_.af.s=function(a,b,c){return isNaN(c)||""==c||a.length>=Number(c)?a:a=-1<b.indexOf("-",0)?a+(0,$APP.ax)(" ",Number(c)-a.length):(0,$APP.ax)(" ",Number(c)-a.length)+a};
$APP.A_.af.f=function(a,b,c,d,e){d=a.toString();isNaN(e)||""==e||(d=parseFloat(a).toFixed(e));let f;f=0>Number(a)?"-":0<=b.indexOf("+")?"+":0<=b.indexOf(" ")?" ":"";0<=Number(a)&&(d=f+d);if(isNaN(c)||d.length>=Number(c))return d;d=isNaN(e)?Math.abs(Number(a)).toString():Math.abs(Number(a)).toFixed(e);a=Number(c)-d.length-f.length;0<=b.indexOf("-",0)?d=f+d+(0,$APP.ax)(" ",a):(b=0<=b.indexOf("0",0)?"0":" ",d=f+(0,$APP.ax)(b,a)+d);return d};
$APP.A_.af.d=function(a,b,c,d,e,f,h,n){return $APP.A_.af.f(parseInt(a,10),b,c,d,0,f,h,n)};$APP.A_.af.i=$APP.A_.af.d;$APP.A_.af.u=$APP.A_.af.d;$APP.uS.h($APP.ZE,null);$APP.Qu(new $APP.k(null,2,[$APP.Wt,new $APP.k(null,1,[$APP.fC,{format:$APP.A_}],null),$APP.Cq,new $APP.k(null,1,[$APP.fC,new $APP.k(null,1,[xYa,$APP.A_],null)],null)],null));