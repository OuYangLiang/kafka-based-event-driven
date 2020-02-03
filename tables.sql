create table if not exists `event_queue_0` (
  `id`          bigint      not null  auto_increment comment '主键',
  `event_id`    char(32)    not null comment '事件id',
  `event_type`  char(12)    not null comment '事件类型',
  `event_time`  datetime    not null comment '事件发生时间',
  `context`     mediumtext  not null comment '事件内容',
  primary key (`id`),
  unique key(`event_id`)
) engine=innodb default charset=utf8 comment='事件队列表';

create table if not exists `event_queue_1` (
  `id`          bigint      not null  auto_increment comment '主键',
  `event_id`    char(32)    not null comment '事件id',
  `event_type`  char(12)    not null comment '事件类型',
  `event_time`  datetime    not null comment '事件发生时间',
  `context`     mediumtext  not null comment '事件内容',
  primary key (`id`),
  unique key(`event_id`)
) engine=innodb default charset=utf8 comment='事件队列表';

create table if not exists `event_queue_2` (
  `id`          bigint      not null  auto_increment comment '主键',
  `event_id`    char(32)    not null comment '事件id',
  `event_type`  char(12)    not null comment '事件类型',
  `event_time`  datetime    not null comment '事件发生时间',
  `context`     mediumtext  not null comment '事件内容',
  primary key (`id`),
  unique key(`event_id`)
) engine=innodb default charset=utf8 comment='事件队列表';

create table if not exists `event_queue_3` (
  `id`          bigint      not null  auto_increment comment '主键',
  `event_id`    char(32)    not null comment '事件id',
  `event_type`  char(12)    not null comment '事件类型',
  `event_time`  datetime    not null comment '事件发生时间',
  `context`     mediumtext  not null comment '事件内容',
  primary key (`id`),
  unique key(`event_id`)
) engine=innodb default charset=utf8 comment='事件队列表';

create table if not exists `event_queue_4` (
  `id`          bigint      not null  auto_increment comment '主键',
  `event_id`    char(32)    not null comment '事件id',
  `event_type`  char(12)    not null comment '事件类型',
  `event_time`  datetime    not null comment '事件发生时间',
  `context`     mediumtext  not null comment '事件内容',
  primary key (`id`),
  unique key(`event_id`)
) engine=innodb default charset=utf8 comment='事件队列表';

create table if not exists `event_queue_5` (
  `id`          bigint      not null  auto_increment comment '主键',
  `event_id`    char(32)    not null comment '事件id',
  `event_type`  char(12)    not null comment '事件类型',
  `event_time`  datetime    not null comment '事件发生时间',
  `context`     mediumtext  not null comment '事件内容',
  primary key (`id`),
  unique key(`event_id`)
) engine=innodb default charset=utf8 comment='事件队列表';

create table if not exists `event_queue_6` (
  `id`          bigint      not null  auto_increment comment '主键',
  `event_id`    char(32)    not null comment '事件id',
  `event_type`  char(12)    not null comment '事件类型',
  `event_time`  datetime    not null comment '事件发生时间',
  `context`     mediumtext  not null comment '事件内容',
  primary key (`id`),
  unique key(`event_id`)
) engine=innodb default charset=utf8 comment='事件队列表';

create table if not exists `event_queue_7` (
  `id`          bigint      not null  auto_increment comment '主键',
  `event_id`    char(32)    not null comment '事件id',
  `event_type`  char(12)    not null comment '事件类型',
  `event_time`  datetime    not null comment '事件发生时间',
  `context`     mediumtext  not null comment '事件内容',
  primary key (`id`),
  unique key(`event_id`)
) engine=innodb default charset=utf8 comment='事件队列表';

create table if not exists `event_history` (
  `id`              bigint      not null auto_increment comment '主键',
  `event_id`        char(32)    not null comment '事件id',
  `subscriber_id`   varchar(12) not null comment '订阅者id',
  `event_type`      char(12)    not null comment '事件类型',
  `event_time`      datetime    not null comment '事件发生时间',
  `context`         mediumtext  not null comment '事件内容',
  primary key (`id`),
  unique key(`event_id`, `subscriber_id`)
) engine=innodb default charset=utf8 comment='事件历史表';