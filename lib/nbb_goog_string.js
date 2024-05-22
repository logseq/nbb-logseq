import { $APP, shadow$provide, $jscomp } from "./nbb_core.js";
const shadow_esm_import = function(x) { return import(x) };
var yLa;$APP.VU=function(a,b){const c=Array.prototype.slice.call(arguments),d=c.shift();if("undefined"==typeof d)throw Error("[goog.string.format] Template required");return d.replace(/%([0\- \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g,function(e,f,h,m,r,n,w,x){if("%"==n)return"%";const D=c.shift();if("undefined"==typeof D)throw Error("[goog.string.format] Not enough arguments");arguments[0]=D;return $APP.VU.Ie[n].apply(null,arguments)})};$APP.WU=new $APP.F(null,"errors","errors",-908790718);
yLa=new $APP.t(null,"format","format",333606761,null);/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
$APP.VU.Ie={};$APP.VU.Ie.s=function(a,b,c){return isNaN(c)||""==c||a.length>=Number(c)?a:a=-1<b.indexOf("-",0)?a+(0,$APP.vu)(" ",Number(c)-a.length):(0,$APP.vu)(" ",Number(c)-a.length)+a};
$APP.VU.Ie.f=function(a,b,c,d,e){d=a.toString();isNaN(e)||""==e||(d=parseFloat(a).toFixed(e));let f;f=0>Number(a)?"-":0<=b.indexOf("+")?"+":0<=b.indexOf(" ")?" ":"";0<=Number(a)&&(d=f+d);if(isNaN(c)||d.length>=Number(c))return d;d=isNaN(e)?Math.abs(Number(a)).toString():Math.abs(Number(a)).toFixed(e);a=Number(c)-d.length-f.length;0<=b.indexOf("-",0)?d=f+d+(0,$APP.vu)(" ",a):(b=0<=b.indexOf("0",0)?"0":" ",d=f+(0,$APP.vu)(b,a)+d);return d};
$APP.VU.Ie.d=function(a,b,c,d,e,f,h,m){return $APP.VU.Ie.f(parseInt(a,10),b,c,d,0,f,h,m)};$APP.VU.Ie.i=$APP.VU.Ie.d;$APP.VU.Ie.u=$APP.VU.Ie.d;$APP.DO.h($APP.XB,null);$APP.Zt(new $APP.k(null,2,[$APP.dt,new $APP.k(null,1,[$APP.lz,{format:$APP.VU}],null),$APP.Kp,new $APP.k(null,1,[$APP.lz,new $APP.k(null,1,[yLa,$APP.VU],null)],null)],null));