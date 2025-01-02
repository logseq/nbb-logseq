import { $APP, shadow$provide, $jscomp } from "./nbb_core.js";
const shadow_esm_import = function(x) { return import(x) };
var qLa;$APP.cV=function(a,b){const c=Array.prototype.slice.call(arguments),d=c.shift();if("undefined"==typeof d)throw Error("[goog.string.format] Template required");return d.replace(/%([0\- \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g,function(e,f,h,m,r,n,w,x){if("%"==n)return"%";const B=c.shift();if("undefined"==typeof B)throw Error("[goog.string.format] Not enough arguments");arguments[0]=B;return $APP.cV.Ie[n].apply(null,arguments)})};$APP.dV=new $APP.G(null,"errors","errors",-908790718);
qLa=new $APP.t(null,"format","format",333606761,null);/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
$APP.cV.Ie={};$APP.cV.Ie.s=function(a,b,c){return isNaN(c)||""==c||a.length>=Number(c)?a:a=-1<b.indexOf("-",0)?a+(0,$APP.vu)(" ",Number(c)-a.length):(0,$APP.vu)(" ",Number(c)-a.length)+a};
$APP.cV.Ie.f=function(a,b,c,d,e){d=a.toString();isNaN(e)||""==e||(d=parseFloat(a).toFixed(e));let f;f=0>Number(a)?"-":0<=b.indexOf("+")?"+":0<=b.indexOf(" ")?" ":"";0<=Number(a)&&(d=f+d);if(isNaN(c)||d.length>=Number(c))return d;d=isNaN(e)?Math.abs(Number(a)).toString():Math.abs(Number(a)).toFixed(e);a=Number(c)-d.length-f.length;0<=b.indexOf("-",0)?d=f+d+(0,$APP.vu)(" ",a):(b=0<=b.indexOf("0",0)?"0":" ",d=f+(0,$APP.vu)(b,a)+d);return d};
$APP.cV.Ie.d=function(a,b,c,d,e,f,h,m){return $APP.cV.Ie.f(parseInt(a,10),b,c,d,0,f,h,m)};$APP.cV.Ie.i=$APP.cV.Ie.d;$APP.cV.Ie.u=$APP.cV.Ie.d;$APP.GO.h($APP.fC,null);$APP.$t(new $APP.k(null,2,[$APP.et,new $APP.k(null,1,[$APP.tz,{format:$APP.cV}],null),$APP.Mp,new $APP.k(null,1,[$APP.tz,new $APP.k(null,1,[qLa,$APP.cV],null)],null)],null));