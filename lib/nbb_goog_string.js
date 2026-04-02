import { $APP, shadow$provide } from "./nbb_core.js";
const shadow_esm_import = function(x) { return import(x) };
var RYa;$APP.NT=function(a,b){const c=Array.prototype.slice.call(arguments),d=c.shift();if(typeof d=="undefined")throw Error("[goog.string.format] Template required");return d.replace(/%([0\- \+]*)(\d+)?(\.(\d+))?([%sfdiu])/g,function(e,f,k,n,t,w,y,u){if(w=="%")return"%";const B=c.shift();if(typeof B=="undefined")throw Error("[goog.string.format] Not enough arguments");arguments[0]=B;return $APP.NT.Mf[w].apply(null,arguments)})};$APP.OT=new $APP.C(null,"token","token",-1211463215);
$APP.QYa=new $APP.C(null,"errors","errors",-908790718);RYa=new $APP.l(null,"format","format",333606761,null);/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
$APP.NT.Mf={};$APP.NT.Mf.s=function(a,b,c){return isNaN(c)||c==""||a.length>=Number(c)?a:a=b.indexOf("-",0)>-1?a+(0,$APP.ly)(" ",Number(c)-a.length):(0,$APP.ly)(" ",Number(c)-a.length)+a};
$APP.NT.Mf.f=function(a,b,c,d,e){d=a.toString();isNaN(e)||e==""||(d=parseFloat(a).toFixed(e));let f;f=Number(a)<0?"-":b.indexOf("+")>=0?"+":b.indexOf(" ")>=0?" ":"";Number(a)>=0&&(d=f+d);if(isNaN(c)||d.length>=Number(c))return d;d=isNaN(e)?Math.abs(Number(a)).toString():Math.abs(Number(a)).toFixed(e);a=Number(c)-d.length-f.length;b.indexOf("-",0)>=0?d=f+d+(0,$APP.ly)(" ",a):(b=b.indexOf("0",0)>=0?"0":" ",d=f+(0,$APP.ly)(b,a)+d);return d};
$APP.NT.Mf.d=function(a,b,c,d,e,f,k,n){return $APP.NT.Mf.f(parseInt(a,10),b,c,d,0,f,k,n)};$APP.NT.Mf.i=$APP.NT.Mf.d;$APP.NT.Mf.u=$APP.NT.Mf.d;$APP.FM.g($APP.jE,null);$APP.Tv(new $APP.F(null,2,[$APP.$u,new $APP.F(null,1,[$APP.Ioa,{format:$APP.NT}],null),$APP.Oq,new $APP.F(null,1,[$APP.Ioa,new $APP.F(null,1,[RYa,$APP.NT],null)],null)],null));