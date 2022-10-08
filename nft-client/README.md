
## springboot 启动项目模板

### 快速上手
1. 项目里面内嵌一个商品模板，对应后台管理系统vue项目，若不要可删除相关业务。
2. 内置权限管理、分组及各种校验
3. 内置微信登录逻辑(可直接修改配置yml文件)
4. 内置用户端和后台管理系统拦截器(/common/interceptor/AuthorizeVerifyResolverImpl 和 PermissionInterceptor)
5. 内置图片上传功能
6.打包方法必须使用clean,kotlin:compile,package 
