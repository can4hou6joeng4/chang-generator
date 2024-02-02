export default [
  {
    path: "/user",
    layout: false,
    routes: [
      { path: "/user/login", component: "./User/Login" },
      { path: "/user/register", component: "./User/Register" },
    ],
  },
  { path: "/", icon: "home", component: "./Index", name: "主页" },
  {
    path: "/generator/add",
    icon: "plus",
    component: "./Generator/Add",
    name: "创建生成器",
  },
  {
    path: "/generator/update",
    icon: "plus",
    component: "./Generator/Add",
    name: "修改生成器",
    hideInMenu: true,
  },
  {
    path: "/generator/detail/:id",
    icon: "home",
    component: "./Generator/Detail",
    name: "生成器详情",
    hideInMenu: true,
  },
  {
    path: "/admin",
    icon: "crown",
    name: "管理页",
    access: "canAdmin",
    routes: [
      { path: "/admin", redirect: "/admin/user" },
      {
        icon: "table",
        path: "/admin/user",
        component: "./Admin/User",
        name: "用户管理",
      },
    ],
  },
  { path: "*", layout: false, component: "./404" },
];
