/*数据库初始化脚本*/

create database if not exists redpacket;

use redpacket;
drop table  if exists red_packet ;

create table red_packet(
red_packet_id int(12) not null auto_increment comment '红包ID',
user_id  int(12) not null comment '用户id',
amount decimal(16,2) not null comment '红包金额',
send_date timestamp not null comment '发送红包时间',
total int(12) not null comment '红包个数',
unit_amount decimal(16,2) not null comment '单个红包金额',
stock int(12) not null comment '剩余红包个数',
version int(2) default 0 not null,
note varchar(120) null,
primary key (red_packet_id),
key idx_stock(stock)
)engine=InnoDB  default charset=utf8 comment='红包库存表';

drop table  if exists user_red_packet ;

create table user_red_packet(
id int(12) not null auto_increment,
user_id int(12) not null ,
red_packet_id int(12) not null ,
amount decimal(16,2),
grab_time timestamp not null ,
primary key (id),
key idx_grab_time(grab_time)
)comment ='用户抢红包表';

insert into red_packet(user_id, amount, send_data, total, unit_amount, stock,note) values(1,200000.00,now(),20000,10,20000,'20万元金额，2万个小红包，每个10元');