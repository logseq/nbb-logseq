import { $APP, shadow$provide, $jscomp } from "./nbb_core.js";
const shadow_esm_import = function(x) { return import(x) };
var U_a;$APP.VT=function(a,b){const c=Array.prototype.slice.call(arguments),d=c.shift();if("undefined"==typeof d)throw Error("[goog.string.format] Template required");return d.replace(/%([0\- \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g,function(e,f,m,q,u,t,B,C){if("%"==t)return"%";const E=c.shift();if("undefined"==typeof E)throw Error("[goog.string.format] Not enough arguments");arguments[0]=E;return $APP.VT.Jf[t].apply(null,arguments)})};$APP.WT=new $APP.F(null,"token","token",-1211463215);
$APP.T_a=new $APP.F(null,"errors","errors",-908790718);U_a=new $APP.p(null,"format","format",333606761,null);/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
$APP.VT.Jf={};$APP.VT.Jf.s=function(a,b,c){return isNaN(c)||""==c||a.length>=Number(c)?a:a=-1<b.indexOf("-",0)?a+(0,$APP.Cx)(" ",Number(c)-a.length):(0,$APP.Cx)(" ",Number(c)-a.length)+a};
$APP.VT.Jf.f=function(a,b,c,d,e){d=a.toString();isNaN(e)||""==e||(d=parseFloat(a).toFixed(e));let f;f=0>Number(a)?"-":0<=b.indexOf("+")?"+":0<=b.indexOf(" ")?" ":"";0<=Number(a)&&(d=f+d);if(isNaN(c)||d.length>=Number(c))return d;d=isNaN(e)?Math.abs(Number(a)).toString():Math.abs(Number(a)).toFixed(e);a=Number(c)-d.length-f.length;0<=b.indexOf("-",0)?d=f+d+(0,$APP.Cx)(" ",a):(b=0<=b.indexOf("0",0)?"0":" ",d=f+(0,$APP.Cx)(b,a)+d);return d};
$APP.VT.Jf.d=function(a,b,c,d,e,f,m,q){return $APP.VT.Jf.f(parseInt(a,10),b,c,d,0,f,m,q)};$APP.VT.Jf.i=$APP.VT.Jf.d;$APP.VT.Jf.u=$APP.VT.Jf.d;$APP.hN.h($APP.FE,null);$APP.$u(new $APP.h(null,2,[$APP.fu,new $APP.h(null,1,[$APP.MC,{format:$APP.VT}],null),$APP.Kq,new $APP.h(null,1,[$APP.MC,new $APP.h(null,1,[U_a,$APP.VT],null)],null)],null));