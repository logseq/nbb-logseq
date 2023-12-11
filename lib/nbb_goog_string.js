import { $APP, shadow$provide, $jscomp } from "./nbb_core.js";
const shadow_esm_import = function(x) { return import(x) };
var pKa;$APP.R3=function(a,b){const c=Array.prototype.slice.call(arguments),d=c.shift();if("undefined"==typeof d)throw Error("[goog.string.format] Template required");return d.replace(/%([0\- \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g,function(e,h,l,p,t,A,y,D){if("%"==A)return"%";const v=c.shift();if("undefined"==typeof v)throw Error("[goog.string.format] Not enough arguments");arguments[0]=v;return $APP.R3.pe[A].apply(null,arguments)})};$APP.QLa=new $APP.k(null,"errors","errors",-908790718);
pKa=new $APP.r(null,"format","format",333606761,null);/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
$APP.R3.pe={};$APP.R3.pe.s=function(a,b,c){return isNaN(c)||""==c||a.length>=Number(c)?a:a=-1<b.indexOf("-",0)?a+(0,$APP.vz)(" ",Number(c)-a.length):(0,$APP.vz)(" ",Number(c)-a.length)+a};
$APP.R3.pe.f=function(a,b,c,d,e){d=a.toString();isNaN(e)||""==e||(d=parseFloat(a).toFixed(e));let h;h=0>Number(a)?"-":0<=b.indexOf("+")?"+":0<=b.indexOf(" ")?" ":"";0<=Number(a)&&(d=h+d);if(isNaN(c)||d.length>=Number(c))return d;d=isNaN(e)?Math.abs(Number(a)).toString():Math.abs(Number(a)).toFixed(e);a=Number(c)-d.length-h.length;0<=b.indexOf("-",0)?d=h+d+(0,$APP.vz)(" ",a):(b=0<=b.indexOf("0",0)?"0":" ",d=h+(0,$APP.vz)(b,a)+d);return d};
$APP.R3.pe.d=function(a,b,c,d,e,h,l,p){return $APP.R3.pe.f(parseInt(a,10),b,c,d,0,h,l,p)};$APP.R3.pe.i=$APP.R3.pe.d;$APP.R3.pe.u=$APP.R3.pe.d;$APP.Wy(new $APP.g(null,2,[$APP.Ax,new $APP.g(null,1,[$APP.bE,{format:$APP.R3}],null),$APP.Ts,new $APP.g(null,1,[$APP.bE,new $APP.g(null,1,[pKa,$APP.R3],null)],null)],null));