/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : kiki_nacos

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 27/11/2019 18:34:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'kiki-auth.yaml', 'DEFAULT_GROUP', 'server:\r\n  port: 8101\r\n\r\nspring:\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    lettuce:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\n  datasource:\r\n    dynamic:\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n\r\n\r\nmybatis-plus:\r\n  type-aliases-package: com.sxdx.kiki.common.entity.system  #别名扫描路径\r\n  mapper-locations: classpath:mapper/*.xml\r\n  configuration:\r\n    jdbc-type-for-null: null  #指定为null，否则再插入空值时会报“无效的列类型”错误\r\n  global-config:\r\n    banner: false  #设置为false关闭MyBatis Plus Banner打印\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 30\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\n', '3e14a083bc2ce078903fb2abea666364', '2019-11-27 17:24:39', '2019-11-27 17:24:39', NULL, '127.0.0.1', '', '', 'kiki-auth配置中心', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (2, 'kiki-cloud-gateway.yaml', 'DEFAULT_GROUP', 'server:\r\n  port: 8301\r\nspring:\r\n  cloud:\r\n    gateway:\r\n      routes:\r\n        - id: kiki-auth\r\n          uri: lb://kiki-auth\r\n          predicates:\r\n            - Path=/auth/**\r\n          filters:\r\n            - name: Hystrix\r\n              args:\r\n                name: authfallback\r\n                fallbackUri: forward:/fallback/kiki-auth\r\n        - id: kiki-server-system\r\n          uri: lb://kiki-server-system\r\n          predicates:\r\n            - Path=/system/**\r\n          filters:\r\n            - name: Hystrix\r\n              args:\r\n                name: systemfallback\r\n                fallbackUri: forward:/fallback/kiki-server-system\r\n        - id: kiki-server-test\r\n          uri: lb://kiki-server-test\r\n          predicates:\r\n            - Path=/test/**\r\n          filters:\r\n            - name: Hystrix\r\n              args:\r\n                name: testfallback\r\n                fallbackUri: forward:/fallback/kiki-server-test\r\n      default-filters:\r\n        - StripPrefix=1 #请求转发前，将Path的内容截去前面一位\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n\r\nhystrix:\r\n  command:\r\n    default:\r\n      execution:\r\n        isolation:\r\n          thread:\r\n            timeoutInMilliseconds: 3000\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\n\r\nmanagement:\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: health,info,gateway', '2da52213aa009fbdee2c5ef3f0b3ccc0', '2019-11-27 17:34:06', '2019-11-27 17:34:06', NULL, '127.0.0.1', '', '', 'kiki-cloud-gateway', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (3, 'kiki-monitor-admin.yaml', 'DEFAULT_GROUP', 'server:\r\n  port: 8401\r\n\r\nspring:\r\n  boot:\r\n    admin:\r\n      ui:\r\n        title: ${spring.application.name}\r\n  security:\r\n    user:\r\n      name: kiki\r\n      password: 123456', 'b25df1c2b7e199562a5950448a29156a', '2019-11-27 17:38:45', '2019-11-27 17:38:45', NULL, '127.0.0.1', '', '', 'kiki-monitor-admin', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (4, 'kiki-server-system.yaml', 'DEFAULT_GROUP', 'server:\r\n  port: 8201\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      p6spy: true\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n  zipkin:\r\n    sender:\r\n      type: rabbit\r\n    sleuth:\r\n      sampler:\r\n        probability: 1\r\n    rabbitmq:\r\n      host: ${rabbitmq.url}\r\n      port: 5672\r\n      username: kiki\r\n      password: 123456\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    jedis:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nhystrix:\r\n  shareSecurityContext: true\r\n\r\n\r\nmybatis-plus:\r\n  type-aliases-package: com.sxdx.kiki.common.entity.system\r\n  mapper-locations: classpath:system/*/*.xml\r\n  configuration:\r\n    jdbc-type-for-null: null\r\n  global-config:\r\n    banner: false\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\n\r\nsecurity:\r\n  oauth2:\r\n    resource:\r\n      id: ${spring.application.name}\r\n      user-info-uri: http://${kiki-gateway}:8301/auth/user\r\n\r\nlogging:\r\n  level:\r\n    com.alibaba.nacos.client: error', '2bb7b3535b98243dc6912f416cd5717c', '2019-11-27 18:19:44', '2019-11-27 18:29:28', NULL, '127.0.0.1', '', '', 'kiki-server-system', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (5, 'kiki-server-test.yaml', 'DEFAULT_GROUP', 'server:\r\n  port: 8202\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      p6spy: true\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n  zipkin:\r\n    sender:\r\n      type: rabbit\r\n    sleuth:\r\n      sampler:\r\n        probability: 1\r\n    rabbitmq:\r\n      host: ${rabbitmq.url}\r\n      port: 5672\r\n      username: kiki\r\n      password: 123456\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    jedis:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\n\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n#  autoconfigure:\r\n#   exclude: com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nhystrix:\r\n  shareSecurityContext: true\r\n\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\nsecurity:\r\n  oauth2:\r\n    resource:\r\n      id: ${spring.application.name}\r\n      user-info-uri: http://${kiki-gateway}:8301/auth/user\r\n\r\nlogging:\r\n  level:\r\n    com.alibaba.nacos.client: error', '44d91f2f67ab8476d467a367a4c812f8', '2019-11-27 18:22:06', '2019-11-27 18:32:12', NULL, '127.0.0.1', '', '', 'kiki-server-test', 'null', 'null', 'yaml', 'null');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(64) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin,
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 1, 'kiki-auth.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8101\r\n\r\nspring:\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    lettuce:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\n  datasource:\r\n    dynamic:\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n\r\n\r\nmybatis-plus:\r\n  type-aliases-package: com.sxdx.kiki.common.entity.system  #别名扫描路径\r\n  mapper-locations: classpath:mapper/*.xml\r\n  configuration:\r\n    jdbc-type-for-null: null  #指定为null，否则再插入空值时会报“无效的列类型”错误\r\n  global-config:\r\n    banner: false  #设置为false关闭MyBatis Plus Banner打印\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 30\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\n', '3e14a083bc2ce078903fb2abea666364', '2010-05-05 00:00:00', '2019-11-27 17:24:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 2, 'kiki-cloud-gateway.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8301\r\nspring:\r\n  cloud:\r\n    gateway:\r\n      routes:\r\n        - id: kiki-auth\r\n          uri: lb://kiki-auth\r\n          predicates:\r\n            - Path=/auth/**\r\n          filters:\r\n            - name: Hystrix\r\n              args:\r\n                name: authfallback\r\n                fallbackUri: forward:/fallback/kiki-auth\r\n        - id: kiki-server-system\r\n          uri: lb://kiki-server-system\r\n          predicates:\r\n            - Path=/system/**\r\n          filters:\r\n            - name: Hystrix\r\n              args:\r\n                name: systemfallback\r\n                fallbackUri: forward:/fallback/kiki-server-system\r\n        - id: kiki-server-test\r\n          uri: lb://kiki-server-test\r\n          predicates:\r\n            - Path=/test/**\r\n          filters:\r\n            - name: Hystrix\r\n              args:\r\n                name: testfallback\r\n                fallbackUri: forward:/fallback/kiki-server-test\r\n      default-filters:\r\n        - StripPrefix=1 #请求转发前，将Path的内容截去前面一位\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n\r\nhystrix:\r\n  command:\r\n    default:\r\n      execution:\r\n        isolation:\r\n          thread:\r\n            timeoutInMilliseconds: 3000\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\n\r\nmanagement:\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: health,info,gateway', '2da52213aa009fbdee2c5ef3f0b3ccc0', '2010-05-05 00:00:00', '2019-11-27 17:34:06', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 3, 'kiki-monitor-admin.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8401\r\n\r\nspring:\r\n  boot:\r\n    admin:\r\n      ui:\r\n        title: ${spring.application.name}\r\n  security:\r\n    user:\r\n      name: kiki\r\n      password: 123456', 'b25df1c2b7e199562a5950448a29156a', '2010-05-05 00:00:00', '2019-11-27 17:38:45', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 4, 'kiki-server-system.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8201\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      p6spy: true\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n  zipkin:\r\n    sender:\r\n      type: rabbit\r\n    sleuth:\r\n      sampler:\r\n        probability: 1\r\n    rabbitmq:\r\n      host: ${rabbitmq.url}\r\n      port: 5672\r\n      username: kiki\r\n      password: 123456\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    jedis:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nhystrix:\r\n  shareSecurityContext: true\r\n\r\n\r\nmybatis-plus:\r\n  type-aliases-package: com.sxdx.kiki.common.entity.system\r\n  mapper-locations: classpath:system/*/*.xml\r\n  configuration:\r\n    jdbc-type-for-null: null\r\n  global-config:\r\n    banner: false\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\n\r\nsecurity:\r\n  oauth2:\r\n    resource:\r\n      id: ${spring.application.name}\r\n      user-info-uri: http://${kiki-gateway}:8301/auth/user', '278a53718d5d08ff18f8a8c7422c5473', '2010-05-05 00:00:00', '2019-11-27 18:19:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 5, 'kiki-server-test.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8202\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      p6spy: true\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n  zipkin:\r\n    sender:\r\n      type: rabbit\r\n    sleuth:\r\n      sampler:\r\n        probability: 1\r\n    rabbitmq:\r\n      host: ${rabbitmq.url}\r\n      port: 5672\r\n      username: kiki\r\n      password: 123456\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    jedis:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\n\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n#  autoconfigure:\r\n#   exclude: com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nhystrix:\r\n  shareSecurityContext: true\r\n\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\nsecurity:\r\n  oauth2:\r\n    resource:\r\n      id: ${spring.application.name}\r\n      user-info-uri: http://${kiki-gateway}:8301/auth/user\r\n\r\n', 'ed7f644e33685aff96ee8c960c0069b0', '2010-05-05 00:00:00', '2019-11-27 18:22:06', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (4, 6, 'kiki-server-system.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8201\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      p6spy: true\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n  zipkin:\r\n    sender:\r\n      type: rabbit\r\n    sleuth:\r\n      sampler:\r\n        probability: 1\r\n    rabbitmq:\r\n      host: ${rabbitmq.url}\r\n      port: 5672\r\n      username: kiki\r\n      password: 123456\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    jedis:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nhystrix:\r\n  shareSecurityContext: true\r\n\r\n\r\nmybatis-plus:\r\n  type-aliases-package: com.sxdx.kiki.common.entity.system\r\n  mapper-locations: classpath:system/*/*.xml\r\n  configuration:\r\n    jdbc-type-for-null: null\r\n  global-config:\r\n    banner: false\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\n\r\nsecurity:\r\n  oauth2:\r\n    resource:\r\n      id: ${spring.application.name}\r\n      user-info-uri: http://${kiki-gateway}:8301/auth/user', '278a53718d5d08ff18f8a8c7422c5473', '2010-05-05 00:00:00', '2019-11-27 18:29:28', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (5, 7, 'kiki-server-test.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8202\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      p6spy: true\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n  zipkin:\r\n    sender:\r\n      type: rabbit\r\n    sleuth:\r\n      sampler:\r\n        probability: 1\r\n    rabbitmq:\r\n      host: ${rabbitmq.url}\r\n      port: 5672\r\n      username: kiki\r\n      password: 123456\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    jedis:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\n\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n#  autoconfigure:\r\n#   exclude: com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nhystrix:\r\n  shareSecurityContext: true\r\n\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\nsecurity:\r\n  oauth2:\r\n    resource:\r\n      id: ${spring.application.name}\r\n      user-info-uri: http://${kiki-gateway}:8301/auth/user\r\n\r\n', 'ed7f644e33685aff96ee8c960c0069b0', '2010-05-05 00:00:00', '2019-11-27 18:29:48', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (5, 8, 'kiki-server-test.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8202\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      p6spy: true\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n  zipkin:\r\n    sender:\r\n      type: rabbit\r\n    sleuth:\r\n      sampler:\r\n        probability: 1\r\n    rabbitmq:\r\n      host: ${rabbitmq.url}\r\n      port: 5672\r\n      username: kiki\r\n      password: 123456\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    jedis:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\n\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n#  autoconfigure:\r\n#   exclude: com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nhystrix:\r\n  shareSecurityContext: true\r\n\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\nsecurity:\r\n  oauth2:\r\n    resource:\r\n      id: ${spring.application.name}\r\n      user-info-uri: http://${kiki-gateway}:8301/auth/user\r\n\r\nlogging:\r\n  level:\r\n    com.alibaba.nacos.client: error', '44d91f2f67ab8476d467a367a4c812f8', '2010-05-05 00:00:00', '2019-11-27 18:29:55', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (5, 9, 'kiki-server-test.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8202\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      p6spy: true\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n  zipkin:\r\n    sender:\r\n      type: rabbit\r\n    sleuth:\r\n      sampler:\r\n        probability: 1\r\n    rabbitmq:\r\n      host: ${rabbitmq.url}\r\n      port: 5672\r\n      username: kiki\r\n      password: 123456\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    jedis:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\n\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n#  autoconfigure:\r\n#   exclude: com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nhystrix:\r\n  shareSecurityContext: true\r\n\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\nsecurity:\r\n  oauth2:\r\n    resource:\r\n      id: ${spring.application.name}\r\n      user-info-uri: http://${kiki-gateway}:8301/auth/user\r\n\r\nlogging:\r\n  level:\r\n    com.alibaba.nacos.client: error', '44d91f2f67ab8476d467a367a4c812f8', '2010-05-05 00:00:00', '2019-11-27 18:30:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (5, 10, 'kiki-server-test.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 8203\r\n\r\nspring:\r\n  datasource:\r\n    dynamic:\r\n      p6spy: true\r\n      hikari:\r\n        connection-timeout: 30000\r\n        max-lifetime: 1800000\r\n        max-pool-size: 15\r\n        min-idle: 5\r\n        connection-test-query: select 1\r\n        pool-name: KikiHikariCP\r\n      primary: base\r\n      datasource:\r\n        base:\r\n          username: root\r\n          password: gaoyipeng\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://${mysql.url}:3306/kiki_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\r\n  boot:\r\n    admin:\r\n      client:\r\n        url: http://${kiki-monitor-admin}:8401\r\n        username: kiki\r\n        password: 123456\r\n  zipkin:\r\n    sender:\r\n      type: rabbit\r\n    sleuth:\r\n      sampler:\r\n        probability: 1\r\n    rabbitmq:\r\n      host: ${rabbitmq.url}\r\n      port: 5672\r\n      username: kiki\r\n      password: 123456\r\n  redis:\r\n    database: 0\r\n    host: ${redis.url}\r\n    port: 6379\r\n    jedis:\r\n      pool:\r\n        min-idle: 8\r\n        max-idle: 500\r\n        max-active: 2000\r\n        max-wait: 10000\r\n    timeout: 5000\r\n\r\ninfo:\r\n  app:\r\n    name: ${spring.application.name}\r\n    description: \"@project.description@\"\r\n    version: \"@project.version@\"\r\n\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\' #表示将SBA客户端的所有监控端点都暴露给SBA服务端\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS #表示总是展示详细的健康信息\r\n#  autoconfigure:\r\n#   exclude: com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nhystrix:\r\n  shareSecurityContext: true\r\n\r\n\r\n#eureka:\r\n#  instance:\r\n#    lease-renewal-interval-in-seconds: 20\r\n#  client:\r\n#    register-with-eureka: true\r\n#    fetch-registry: true\r\n#    instance-info-replication-interval-seconds: 30\r\n#    registry-fetch-interval-seconds: 3\r\n#    serviceUrl:\r\n#      defaultZone: http://kiki:123456@${kiki-register}:8001/register/eureka/\r\nsecurity:\r\n  oauth2:\r\n    resource:\r\n      id: ${spring.application.name}\r\n      user-info-uri: http://${kiki-gateway}:8301/auth/user\r\n\r\nlogging:\r\n  level:\r\n    com.alibaba.nacos.client: error', 'cfadebf65776f2f26d2c2d494fe6b317', '2010-05-05 00:00:00', '2019-11-27 18:32:12', NULL, '127.0.0.1', 'U', '');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
