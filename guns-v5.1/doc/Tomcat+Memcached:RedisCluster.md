[TOC]



# 目标

* 掌握主流开发框架 Guns 开发和使用
* 掌握 SpringBoot 配置使用流程
* 掌握主流内存应用之 Memcached
* 掌握 Tomcat + Memcached 的分布式搭建和优化
* 掌握 Tomcat + Redis 的分布式搭建和优化

### 

## Guns 知识点概览

* guns-admin 分页、事务
* 前后端分离安全之 JWT 和签名机制

* Guns-rest 代码生成器
* 线上环境部署

## Tomcat 知识点概览

* 虚拟机优化
* 运行模式优化
* MSM

## Memcache 知识点概览

* Memcached
  * Slab Allocator
  * XMemcached
  * 分布式搭建
  * 内存级优化
  * 实战场景分析

## Redis 知识点概览

* 掌握 Redis 安装部署
* 掌握 Jedis 基本开发流程
* 了解 Redis 集群代理之 Codis

* 掌握 Codis 安装和部署
* 掌握 Codis 的经常应用

## Guns 进阶

#### Guns 分页

* 逻辑分页
  * 原理
    * 1、查询出所有数据
    * 2、数据传入前端框架【Bootstrap Table】
  * Guns 的默认实现
  * 实现方式
    * table.setPaginationType("client")

* 物理分页

  * 原理
    * 1、按照要求查询数据
    * 2、数据返回前端
      * rows
      * total
  * 实现方式
    * table.setPaginationType("server")
    * 加入数据库分页语句
      * Page 对象

  

## 事务：

1. 保证数据操作的一致性，完整性能...
2. ACID
3. 隔离级别
4. 属性





