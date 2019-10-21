## 多线程爬虫和ES数据分析

####  1.什么是MyBatis？

MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。MyBatis 可以使用简单的 XML 或注解来配置和映射原生类型、接口和 Java 的 POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

- MyBatis的使用

  在resource/db/mybatis文件夹下面，创建`config.xml`、`MyMapper.xml`文件；

  具体配置可以参考[官网](https://mybatis.org/mybatis-3/zh/configuration.html)

- MyBatis的映射

  MyBatis框架的优点是将SQL语句映射成为对象，只需要配置`MyMapper.xml`文件就可以轻松实现SQL语句的复用，方便管理SQL语句

- MyBatis的动态SQL

  动态SQL非常有趣，不拘泥于传统的死板的SQL语句；可以在配置文件中实现条件选择具体执行那条语句;如下，一个条件插入语句，当tableName为LINK_TO_BE_PROCESSED时插入这个表；否则插入另一张表

  ```pom.xml
  <insert id="insertLink" parameterType="HashMap">
          insert into
          <choose>
              <when test="tableName == 'LINK_TO_BE_PROCESSED'">
                  LINK_TO_BE_PROCESSED
              </when>
              <otherwise>
                  LINK_ALREADY_PROCESSED
              </otherwise>
          </choose>
          (link)
          values ( #{link} )
      </insert>
  ```

  

#### 2.Flyway

- Flyway的使用

  这是一个自动化管理数据库的软件，只需要将创建表以及插入数据的SQL语句写入resource/db/migration文件夹中；并按照`V1(自定义版本号)_自定义表名.sql`格式存放，在迁移至其他操作系统或电脑时，能够轻松复原原来的数据库

  使用`mvn flyway:migrate`命令，就能实现数据库管理的自动化创建。

#### 3.Docker

- Docker容器

  Docker 是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中,然后发布到任何流行的Linux机器上,也可以实现虚拟化,容器是完全使用沙箱机制,相互之间不会有任何接口。

  简单来讲，就是Docker容器是独立于你当前系统环境空间的一块计算机区域；它与你本机的任何接口都不会有接触，除非你实现端口映射。

  可以通过[官网](https://www.docker.com/)下载Docker

- Docker安装MySQL

  使用Docker安装一些程序相当的**方便**，方便到什么样子呢？

  根本不需要操心数据库的安装产生的残留，导致无法升级数据库；以及各种层出不穷的问题，因为它与你本机环境**完全隔离**。

  只需简单的命令就能轻松安装MySQL数据库；`docker run --name Mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=mydata -p 3306:3306 -d mysql:tag(你想要使用的版本)`

- Docker安装Elasticsearch

  也是同样的道理，只需简单的命令行就能自动帮你搞定；

  `docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:tag(版本号)`

#### 4.Elasticsearch

- 简介

  ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java语言开发的，并作为Apache许可条款下的开放源码发布，是一种流行的企业级搜索引擎。ElasticSearch用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。

- **基本概念**

- 4.1 Node 与 Cluster

  Elastic 本质上是一个分布式数据库，允许多台服务器协同工作，每台服务器可以运行多个 Elastic 实例。

  单个 Elastic 实例称为一个节点（node）。一组节点构成一个集群（cluster）。

  4.2 Index

  Elastic 会索引所有字段，经过处理后写入一个反向索引（Inverted Index）。查找数据的时候，直接查找该索引。

  所以，Elastic 数据管理的顶层单位就叫做 Index（索引）。它是单个数据库的同义词。每个 Index （即数据库）的名字必须是小写。

  下面的命令可以查看当前节点的所有 Index。

  ```
  $ curl -X GET 'http://localhost:9200/_cat/indices?v'
  ```

  4.3 Document

  Index 里面单条的记录称为 Document（文档）。许多条 Document 构成了一个 Index。

  Document 使用 JSON 格式表示，下面是一个例子。Document即对应数据库中的每一条记录。

  > ```javascript
  > {
  >   "id": "1",
  >   "title": "新浪微博",
  >   "content": "新浪微博十周年大庆典"
  > }
  > ```

  同一个 Index 里面的 Document，不要求有相同的结构（scheme），但是最好保持相同，这样有利于提高搜索效率。

  

