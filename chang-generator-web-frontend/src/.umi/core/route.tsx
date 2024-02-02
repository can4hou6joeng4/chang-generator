// @ts-nocheck
// This file is generated by Umi automatically
// DO NOT CHANGE IT MANUALLY!
import React from 'react';

export async function getRoutes() {
  const routes = {"1":{"path":"/user","layout":false,"id":"1"},"2":{"path":"/user/login","parentId":"1","id":"2"},"3":{"path":"/user/register","parentId":"1","id":"3"},"4":{"path":"/","icon":"home","name":"主页","parentId":"ant-design-pro-layout","id":"4"},"5":{"path":"/generator/add","icon":"plus","name":"创建生成器","parentId":"ant-design-pro-layout","id":"5"},"6":{"path":"/generator/update","icon":"plus","name":"修改生成器","hideInMenu":true,"parentId":"ant-design-pro-layout","id":"6"},"7":{"path":"/generator/detail/:id","icon":"home","name":"生成器详情","hideInMenu":true,"parentId":"ant-design-pro-layout","id":"7"},"8":{"path":"/admin","icon":"crown","name":"管理页","access":"canAdmin","parentId":"ant-design-pro-layout","id":"8"},"9":{"path":"/admin","redirect":"/admin/user","parentId":"8","id":"9"},"10":{"icon":"table","path":"/admin/user","name":"用户管理","parentId":"8","id":"10"},"11":{"path":"*","layout":false,"id":"11"},"ant-design-pro-layout":{"id":"ant-design-pro-layout","path":"/","isLayout":true},"umi/plugin/openapi":{"path":"/umi/plugin/openapi","id":"umi/plugin/openapi"}} as const;
  return {
    routes,
    routeComponents: {
'1': React.lazy(() => import( './EmptyRoute')),
'2': React.lazy(() => import(/* webpackChunkName: "p__User__Login__index" */'@/pages/User/Login/index.tsx')),
'3': React.lazy(() => import(/* webpackChunkName: "p__User__Register__index" */'@/pages/User/Register/index.tsx')),
'4': React.lazy(() => import(/* webpackChunkName: "p__Index__index" */'@/pages/Index/index.tsx')),
'5': React.lazy(() => import(/* webpackChunkName: "p__Generator__Add__index" */'@/pages/Generator/Add/index.tsx')),
'6': React.lazy(() => import(/* webpackChunkName: "p__Generator__Add__index" */'@/pages/Generator/Add/index.tsx')),
'7': React.lazy(() => import(/* webpackChunkName: "p__Generator__Detail__index" */'@/pages/Generator/Detail/index.tsx')),
'8': React.lazy(() => import( './EmptyRoute')),
'9': React.lazy(() => import( './EmptyRoute')),
'10': React.lazy(() => import(/* webpackChunkName: "p__Admin__User__index" */'@/pages/Admin/User/index.tsx')),
'11': React.lazy(() => import(/* webpackChunkName: "p__404" */'@/pages/404.tsx')),
'ant-design-pro-layout': React.lazy(() => import(/* webpackChunkName: "umi__plugin-layout__Layout" */'/Users/bobochang/Documents/PROJECTS/chang-generator/chang-generator-web-frontend/src/.umi/plugin-layout/Layout.tsx')),
'umi/plugin/openapi': React.lazy(() => import(/* webpackChunkName: "umi__plugin-openapi__openapi" */'/Users/bobochang/Documents/PROJECTS/chang-generator/chang-generator-web-frontend/src/.umi/plugin-openapi/openapi.tsx')),
},
  };
}
