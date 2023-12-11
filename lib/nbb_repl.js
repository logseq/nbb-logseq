import { $APP, shadow$provide, $jscomp } from "./nbb_core.js";
import "./nbb_api.js";
const shadow_esm_import = function(x) { return import(x) };
import*as esm_import$net from"net";import*as esm_import$readline from"readline";import*as esm_import$vm from"vm";
var BR=function(a){var b=function(){var e=$APP.Yp.h(a,"(");return $APP.m(e)?new $APP.H(null,2,5,$APP.I,[$APP.Ik.j(a,0,e+1),$APP.Ik.h(a,e+1)],null):new $APP.H(null,2,5,$APP.I,[null,a],null)}(),c=$APP.C.j(b,0,null),d=$APP.C.j(b,1,null);b=$APP.gf.h(function(){try{return $APP.yP(new $APP.g(null,2,[$APP.O,$APP.n.g($APP.q(AR)),$APP.bI,d],null))}catch(h){var e=h;console.warn($APP.n.g($APP.jya),$APP.vm(e));return null}}(),"completions");return $APP.Wk.h(function(e){return[$APP.n.g(c),$APP.n.g(e)].join("")},
$APP.ri(function(e){return $APP.tb(e,d)},$APP.Wk.h(function(e){return $APP.gf.h(e,"candidate")},b)))},CR=function(a){return[$APP.pp.g(BR(a)),a]},GR=function(a,b){$APP.Th(DR,!1);a.setPrompt([$APP.n.g($APP.q(AR)),"\x3d\x3e "].join(""));a.prompt();return $APP.zn($APP.q(ER))?null:FR.h?FR.h(b,a):FR.call(null,b,a)},HR=function(a){var b=$APP.No(a);a=$APP.Oo(a);var c=$APP.sn($APP.q(ER));b=$APP.Yh.h(b-1,c);c=$APP.u(b);b=$APP.x(c);c=$APP.z(c);a=$APP.m(b)?$APP.Ik.h(b,a):null;$APP.Th(ER,$APP.En.h("\n",$APP.Fg(a,
c)))},tia=function(a,b){b={f:b};esm_import$vm.createContext(b);try{return $APP.m($APP.m(JR)?$APP.qb(a):JR)&&process.stdin.setRawMode(!1),esm_import$vm.runInContext("f()",b,{displayErrors:!0,breakOnSigint:!0,microtaskMode:"afterEvaluate"}).then(function(c){var d={f:$APP.m(a)?function(){return c}:function(){var e=$APP.x(c);$APP.WA.l($APP.F([e]));return c}};esm_import$vm.createContext(d);return esm_import$vm.runInContext("f()",d,{displayErrors:!0,breakOnSigint:!0,microtaskMode:"afterEvaluate"})}).finally(function(){return $APP.m($APP.m(JR)?
$APP.qb(a):JR)?process.stdin.setRawMode(!0):null})}catch(c){return b=c,$APP.m($APP.m(JR)?$APP.qb(a):JR)&&process.stdin.setRawMode(!0),Promise.reject(b)}},FR=function(a,b){if($APP.m(function(){var e=$APP.q(DR);return $APP.m(e)?e:$APP.zn($APP.q(ER))}()))return null;$APP.Th(DR,!0);var c=$APP.as($APP.q(ER)),d=function(){try{var e=$APP.hj([$APP.ef,$APP.q(AR)]);$APP.jo(e);try{return $APP.Jy(c)}finally{$APP.lo()}}catch(h){e=h;if(-1!=$APP.vm(e).indexOf("EOF while reading"))return xFa;HR(c);$APP.WA.l($APP.F([$APP.n.g(e)]));
return yFa}}();if($APP.B.h(yFa,d))return GR(b,a);if($APP.B.h(xFa,d))return $APP.Th(DR,!1);HR(c);if($APP.B.h($APP.Cx,d))return $APP.Th(DR,!1);$APP.jo($APP.hj([$APP.ef,$APP.q(AR)]));return tia(a,function(){return $APP.NCa(d,new $APP.g(null,3,[$APP.O,$APP.q(AR),$APP.Km,$APP.q($APP.bf),$APP.uJ,$APP.Tt],null))}).then(function(e){var h=$APP.C.j(e,0,null);e=$APP.C.j(e,1,null);e=$APP.ph(e);e=$APP.gf.h(e,$APP.O);$APP.Th(AR,e);$APP.fN.h($APP.Yda,$APP.Rh($APP.q($APP.Xda)));$APP.fN.h($APP.Xda,$APP.Rh($APP.q($APP.Wda)));
$APP.fN.h($APP.Wda,$APP.Rh(h));$APP.m(a)&&a.write($APP.RA.l($APP.F([h])));return GR(b,a)}).catch(function(e){$APP.WA.l($APP.F([$APP.n.g(e)]));$APP.fN.h($APP.Zda,$APP.Rh(e));return GR(b,a)}).finally(function(){return $APP.lo()})},LR=function(a,b){a.on("line",function(c){$APP.Dl.I(ER,$APP.n,c,"\n");return FR(b,a)})},NR=function(a){return esm_import$readline.createInterface({input:a,output:a,completer:CR})},uia=function(a){var b=NR(a);LR(b,a);a.setNoDelay(!0);return a.on("close",function(){return $APP.VA.l($APP.F(["Client closed connection."]))})},
zFa=new $APP.r(null,"nbb.impl.repl","nbb.impl.repl",-339140484,null),xFa=new $APP.k("nbb.impl.repl","eof-while-reading","nbb.impl.repl/eof-while-reading",1743626215),AFa=new $APP.r("nbb.impl.repl","completer*","nbb.impl.repl/completer*",455172317,null),BFa=new $APP.r("nbb.impl.repl","repl","nbb.impl.repl/repl",-1645296655,null),CFa=new $APP.r(null,"completer*","completer*",-648150376,null),DFa=new $APP.r("nbb.impl.repl","socket-repl","nbb.impl.repl/socket-repl",1509490200,null),FFa=new $APP.k(null,
"init","init",-1875481434),GFa=new $APP.r(null,"get-completions","get-completions",1933789168,null),yFa=new $APP.k("nbb.impl.repl","continue","nbb.impl.repl/continue",-1326808644);var AR=$APP.Cl.g($APP.q($APP.ef)),ER=$APP.Cl.g(""),DR=$APP.Cl.g(!1),JR,HHa=process.stdout.isTTY;JR=$APP.m(HHa)?process.stdin.setRawMode:HHa;process.binding("contextify");var QR=$APP.py($APP.Qy),RR=function RR(a){switch(arguments.length){case 0:return RR.F();case 1:return RR.g(arguments[0]);default:throw Error(["Invalid arity: ",$APP.n.g(arguments.length)].join(""));}};RR.F=function(){return RR.g(null)};
RR.g=function(a){var b=function(){var d=$APP.zP.g(a);return $APP.m(d)?d:0}(),c=esm_import$net.createServer(uia);return c.listen(b,"127.0.0.1",function(){var d=c.address(),e=d.address;return $APP.VA.l($APP.F([["Socket REPL listening on port ",$APP.n.g(d.port)," on host ",$APP.n.g(e)].join("")]))})};RR.C=1;var SR=function SR(a){switch(arguments.length){case 0:return SR.F();case 1:return SR.g(arguments[0]);default:throw Error(["Invalid arity: ",$APP.n.g(arguments.length)].join(""));}};SR.F=function(){return SR.g(null)};
SR.g=function(a){function b(e){return $APP.rP(new $APP.G(null,$APP.Bm,new $APP.G(null,new $APP.G(null,$APP.Bm,new $APP.G(null,e,null,1,null),2,null),null,1,null),2,null))}$APP.m(JR)&&process.stdin.setRawMode(!0);var c=$APP.C.j($APP.iN,0,null),d=$APP.C.j($APP.iN,1,null);$APP.VA.l($APP.F([["Welcome to nbb-logseq v",$APP.jz(),"!"].join("")]));return b(c).then(function(){return b(d)}).then(FFa.h(a,$APP.zf)).then(function(){return new Promise(function(e){var h=$APP.m(null)?NR(null):esm_import$readline.createInterface({input:process.stdin,
output:process.stdout,completer:CR});LR(h,null);h.setPrompt([$APP.n.g($APP.q(AR)),"\x3d\x3e "].join(""));h.on("close",e);return h.prompt()})})};SR.C=1;
var yia=new $APP.g(null,3,[$APP.zDa,function(){var a=new $APP.Dd(function(){return SR},BFa,$APP.Oj([$APP.O,$APP.N,$APP.Km,$APP.vB,$APP.mJ,$APP.io,$APP.Lm,$APP.BP,$APP.U,$APP.V,$APP.mH],[zFa,$APP.zDa,"nbb/impl/repl.cljs",11,new $APP.g(null,6,[$APP.bC,!1,$APP.Jv,1,$APP.kK,1,$APP.VJ,new $APP.H(null,2,5,$APP.I,[$APP.qi,new $APP.H(null,1,5,$APP.I,[$APP.KH],null)],null),$APP.U,$APP.R($APP.qi,new $APP.H(null,1,5,$APP.I,[$APP.KH],null)),$APP.XG,$APP.R(null,null)],null),1,188,188,$APP.R($APP.qi,new $APP.H(null,
1,5,$APP.I,[$APP.KH],null)),null,$APP.m(SR)?SR.A:null])),b=$APP.q(a);a=$APP.Be(a);var c=$APP.m(null)?null:$APP.N.g(a),d=new $APP.g(null,4,[$APP.O,QR,$APP.N,c,$APP.U,$APP.U.g(a),$APP.V,$APP.V.g(a)],null);return $APP.m($APP.Ys.g(a))?$APP.jy(c,b,d):$APP.m($APP.is.g(a))?$APP.ky(c,b,d):$APP.iy(c,b,d)}(),GFa,function(){var a=new $APP.Dd(function(){return BR},AFa,$APP.Oj([$APP.O,$APP.N,$APP.Km,$APP.vB,$APP.io,$APP.Lm,$APP.BP,$APP.U,$APP.V,$APP.mH],[zFa,CFa,"nbb/impl/repl.cljs",17,1,15,15,$APP.R(new $APP.H(null,
1,5,$APP.I,[$APP.hka],null)),"Given a line, returns a flat vector of completions",$APP.m(BR)?BR.A:null])),b=$APP.q(a);a=$APP.Be(a);var c=$APP.m(null)?null:$APP.N.g(a),d=new $APP.g(null,4,[$APP.O,QR,$APP.N,c,$APP.U,$APP.U.g(a),$APP.V,$APP.V.g(a)],null);return $APP.m($APP.Ys.g(a))?$APP.jy(c,b,d):$APP.m($APP.is.g(a))?$APP.ky(c,b,d):$APP.iy(c,b,d)}(),$APP.yDa,function(){var a=new $APP.Dd(function(){return RR},DFa,$APP.Oj([$APP.O,$APP.N,$APP.Km,$APP.vB,$APP.mJ,$APP.io,$APP.Lm,$APP.BP,$APP.U,$APP.V,$APP.mH],
[zFa,$APP.yDa,"nbb/impl/repl.cljs",18,new $APP.g(null,6,[$APP.bC,!1,$APP.Jv,1,$APP.kK,1,$APP.VJ,new $APP.H(null,2,5,$APP.I,[$APP.qi,new $APP.H(null,1,5,$APP.I,[$APP.KH],null)],null),$APP.U,$APP.R($APP.qi,new $APP.H(null,1,5,$APP.I,[$APP.KH],null)),$APP.XG,$APP.R(null,null)],null),1,173,173,$APP.R($APP.qi,new $APP.H(null,1,5,$APP.I,[$APP.KH],null)),null,$APP.m(RR)?RR.A:null])),b=$APP.q(a);a=$APP.Be(a);var c=$APP.m(null)?null:$APP.N.g(a),d=new $APP.g(null,4,[$APP.O,QR,$APP.N,c,$APP.U,$APP.U.g(a),$APP.V,
$APP.V.g(a)],null);return $APP.m($APP.Ys.g(a))?$APP.jy(c,b,d):$APP.m($APP.is.g(a))?$APP.ky(c,b,d):$APP.iy(c,b,d)}()],null);$APP.Wy(new $APP.g(null,1,[$APP.Ts,new $APP.g(null,1,[$APP.Qy,yia],null)],null));