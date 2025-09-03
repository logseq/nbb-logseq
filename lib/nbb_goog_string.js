import { $APP, shadow$provide, $jscomp } from "./nbb_core.js";
const shadow_esm_import = function(x) { return import(x) };
var Q_a;$APP.XT=function(a,b){const c=Array.prototype.slice.call(arguments),d=c.shift();if("undefined"==typeof d)throw Error("[goog.string.format] Template required");return d.replace(/%([0\- \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g,function(e,f,m,p,u,t,B,C){if("%"==t)return"%";const E=c.shift();if("undefined"==typeof E)throw Error("[goog.string.format] Not enough arguments");arguments[0]=E;return $APP.XT.Jf[t].apply(null,arguments)})};$APP.YT=new $APP.F(null,"token","token",-1211463215);
$APP.P_a=new $APP.F(null,"errors","errors",-908790718);Q_a=new $APP.q(null,"format","format",333606761,null);/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
$APP.XT.Jf={};$APP.XT.Jf.s=function(a,b,c){return isNaN(c)||""==c||a.length>=Number(c)?a:a=-1<b.indexOf("-",0)?a+(0,$APP.Cx)(" ",Number(c)-a.length):(0,$APP.Cx)(" ",Number(c)-a.length)+a};
$APP.XT.Jf.f=function(a,b,c,d,e){d=a.toString();isNaN(e)||""==e||(d=parseFloat(a).toFixed(e));let f;f=0>Number(a)?"-":0<=b.indexOf("+")?"+":0<=b.indexOf(" ")?" ":"";0<=Number(a)&&(d=f+d);if(isNaN(c)||d.length>=Number(c))return d;d=isNaN(e)?Math.abs(Number(a)).toString():Math.abs(Number(a)).toFixed(e);a=Number(c)-d.length-f.length;0<=b.indexOf("-",0)?d=f+d+(0,$APP.Cx)(" ",a):(b=0<=b.indexOf("0",0)?"0":" ",d=f+(0,$APP.Cx)(b,a)+d);return d};
$APP.XT.Jf.d=function(a,b,c,d,e,f,m,p){return $APP.XT.Jf.f(parseInt(a,10),b,c,d,0,f,m,p)};$APP.XT.Jf.i=$APP.XT.Jf.d;$APP.XT.Jf.u=$APP.XT.Jf.d;$APP.jN.h($APP.OE,null);$APP.$u(new $APP.h(null,2,[$APP.fu,new $APP.h(null,1,[$APP.OC,{format:$APP.XT}],null),$APP.Kq,new $APP.h(null,1,[$APP.OC,new $APP.h(null,1,[Q_a,$APP.XT],null)],null)],null));