import { $APP, shadow$provide, $jscomp } from "./nbb_core.js";
const shadow_esm_import = function(x) { return import(x) };
var P_a;$APP.YT=function(a,b){const c=Array.prototype.slice.call(arguments),d=c.shift();if("undefined"==typeof d)throw Error("[goog.string.format] Template required");return d.replace(/%([0\- \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g,function(e,f,m,p,u,t,B,C){if("%"==t)return"%";const E=c.shift();if("undefined"==typeof E)throw Error("[goog.string.format] Not enough arguments");arguments[0]=E;return $APP.YT.Jf[t].apply(null,arguments)})};$APP.ZT=new $APP.F(null,"token","token",-1211463215);
$APP.O_a=new $APP.F(null,"errors","errors",-908790718);P_a=new $APP.q(null,"format","format",333606761,null);/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
$APP.YT.Jf={};$APP.YT.Jf.s=function(a,b,c){return isNaN(c)||""==c||a.length>=Number(c)?a:a=-1<b.indexOf("-",0)?a+(0,$APP.Cx)(" ",Number(c)-a.length):(0,$APP.Cx)(" ",Number(c)-a.length)+a};
$APP.YT.Jf.f=function(a,b,c,d,e){d=a.toString();isNaN(e)||""==e||(d=parseFloat(a).toFixed(e));let f;f=0>Number(a)?"-":0<=b.indexOf("+")?"+":0<=b.indexOf(" ")?" ":"";0<=Number(a)&&(d=f+d);if(isNaN(c)||d.length>=Number(c))return d;d=isNaN(e)?Math.abs(Number(a)).toString():Math.abs(Number(a)).toFixed(e);a=Number(c)-d.length-f.length;0<=b.indexOf("-",0)?d=f+d+(0,$APP.Cx)(" ",a):(b=0<=b.indexOf("0",0)?"0":" ",d=f+(0,$APP.Cx)(b,a)+d);return d};
$APP.YT.Jf.d=function(a,b,c,d,e,f,m,p){return $APP.YT.Jf.f(parseInt(a,10),b,c,d,0,f,m,p)};$APP.YT.Jf.i=$APP.YT.Jf.d;$APP.YT.Jf.u=$APP.YT.Jf.d;$APP.kN.h($APP.PE,null);$APP.$u(new $APP.h(null,2,[$APP.fu,new $APP.h(null,1,[$APP.MC,{format:$APP.YT}],null),$APP.Kq,new $APP.h(null,1,[$APP.MC,new $APP.h(null,1,[P_a,$APP.YT],null)],null)],null));