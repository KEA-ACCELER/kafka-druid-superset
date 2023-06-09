-- # 테스트용 DB from 예진
create database testDB;
use testDB;

CREATE TABLE BusRoute (
  id INT PRIMARY KEY AUTO_INCREMENT,
  bus_route_no VARCHAR(20),
  bus_route_nm VARCHAR(60)
);

CREATE TABLE BusType (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    mntn_typ_nm VARCHAR(20),
    mntn_typ_cd VARCHAR(20)
);

CREATE TABLE BusStation (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  bsst_ars_no VARCHAR(60),
  bus_sta_nm VARCHAR(60),
  gu VARCHAR(20),
  dong VARCHAR(20)
);

CREATE TABLE BusRide (
  id INT PRIMARY KEY AUTO_INCREMENT,
  eighteen_alight_num INT,
  nineteen_alight_num INT,
  five_alight_num INT,
  fifteen_ride_num INT,
  twenty_three_alight_num INT,
  eight_alight_num INT,
  sixteen_ride_num INT,
  seventeen_alight_num INT,
  twenty_two_alight_num INT,
  twenty_two_ride_num INT,
  eleven_alight_num INT,
  six_ride_num INT,
  four_alight_num INT,
  thirteen_ride_num INT,
  seventeen_ride_num INT,
  fourteen_ride_num INT,
  seven_alight_num INT,
  nine_alight_num INT,
  eighteen_ride_num INT,
  stnd_bsst_id INT,
  three_alight_num INT,
  twelve_alight_num INT,
  use_mon INT,
  two_alight_num INT,
  twenty_alight_num INT,
  ten_alight_num INT,
  one_ride_num INT,
  thirteen_alight_num INT,
  four_ride_num INT,
  sixteen_alight_num INT,
  five_ride_num INT,
  fourteen_alight_num INT,
  six_alight_num INT,
  nineteen_ride_num INT,
  two_ride_num INT,
  ten_ride_num INT,
  twenty_three_ride_num INT,
  one_alight_num INT,
  midnight_alight_num INT,
  fifteen_alight_num INT,
  seven_ride_num INT,
  nine_ride_num INT,
  eight_ride_num INT,
  twenty_one_ride_num INT,
  twenty_ride_num INT,
  twenty_one_alight_num INT,
  twelve_ride_num INT,
  three_ride_num INT,
  work_dt INT,
  midnight_ride_num INT,
  eleven_ride_num INT,
  bsst_ars_no_id INT,
  bus_route_no_id INT,
  mntn_typ_cd_id INT
);

ALTER TABLE BusStation
ADD INDEX idx_busstation_id (id);

ALTER TABLE BusRide
ADD CONSTRAINT fk_busstation_bsst_ars_no_id
FOREIGN KEY (bsst_ars_no_id) REFERENCES BusStation(id);

ALTER TABLE BusRoute
ADD INDEX idx_busroute_id (id);

ALTER TABLE BusRide
ADD CONSTRAINT fk_busroute_bus_route_no_id
FOREIGN KEY (bus_route_no_id) REFERENCES BusRoute(id);

ALTER TABLE BusType
ADD INDEX idx_bustype_id (id);

ALTER TABLE BusRide
ADD CONSTRAINT fk_bustype_mntn_typ_cd_id
FOREIGN KEY (mntn_typ_cd_id) REFERENCES BusType(id);

-- # 인덱스 추가
ALTER TABLE BusRoute
ADD INDEX idx_bus_route_no (bus_route_no);

-- # csv 파일로부터 데이터 추가
LOAD DATA INFILE '/docker-entrypoint-initdb.d/bus_route.csv' 
INTO TABLE BusRoute
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(@bus_route_no,@bus_route_nm)
SET bus_route_no = @bus_route_no,
    bus_route_nm = @bus_route_nm;

LOAD DATA INFILE '/docker-entrypoint-initdb.d/bus_type.csv' 
INTO TABLE BusType
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(@mntn_typ_nm,@mntn_typ_cd)
SET mntn_typ_nm = @mntn_typ_nm,
    mntn_typ_cd = @mntn_typ_cd;

LOAD DATA INFILE '/docker-entrypoint-initdb.d/bus_station.csv' 
INTO TABLE BusStation
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(@bsst_ars_no,@bus_sta_nm,@gu,@dong)
SET bsst_ars_no = @bsst_ars_no,
    bus_sta_nm = @bus_sta_nm,
    gu = @gu,
    dong = @dong;

LOAD DATA INFILE '/docker-entrypoint-initdb.d/bus_ride.csv' 
INTO TABLE BusRide
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(@eighteen_alight_num,@nineteen_alight_num,@five_alight_num,@fifteen_ride_num,@twenty_three_alight_num,@eight_alight_num,@sixteen_ride_num,@seventeen_alight_num,@twenty_two_alight_num,@twenty_two_ride_num,@eleven_alight_num,@six_ride_num,@four_alight_num,@thirteen_ride_num,@seventeen_ride_num,@fourteen_ride_num,@seven_alight_num,@nine_alight_num,@eighteen_ride_num,@stnd_bsst_id,@three_alight_num,@twelve_alight_num,@use_mon,@two_alight_num,@twenty_alight_num,@ten_alight_num,@one_ride_num,@thirteen_alight_num,@four_ride_num,@sixteen_alight_num,@five_ride_num,@fourteen_alight_num,@six_alight_num,@nineteen_ride_num,@two_ride_num,@ten_ride_num,@twenty_three_ride_num,@one_alight_num,@midnight_alight_num,@fifteen_alight_num,@seven_ride_num,@nine_ride_num,@eight_ride_num,@twenty_one_ride_num,@twenty_ride_num,@twenty_one_alight_num,@twelve_ride_num,@three_ride_num,@work_dt,@midnight_ride_num,@eleven_ride_num,@bsst_ars_no_id,@bus_route_no_id,@mntn_typ_cd_id)
SET eighteen_alight_num = @eighteen_alight_num,
    nineteen_alight_num = @nineteen_alight_num,
    five_alight_num = @five_alight_num,
    fifteen_ride_num = @fifteen_ride_num,
    twenty_three_alight_num = @twenty_three_alight_num,
    eight_alight_num = @eight_alight_num,
    sixteen_ride_num = @sixteen_ride_num,
    seventeen_alight_num = @seventeen_alight_num,
    twenty_two_alight_num = @twenty_two_alight_num,
    twenty_two_ride_num = @twenty_two_ride_num,
    eleven_alight_num = @eleven_alight_num,
    six_ride_num = @six_ride_num,
    four_alight_num = @four_alight_num,
    thirteen_ride_num = @thirteen_ride_num,
    seventeen_ride_num = @seventeen_ride_num,
    fourteen_ride_num = @fourteen_ride_num,
    seven_alight_num = @seven_alight_num,
    nine_alight_num = @nine_alight_num,
    eighteen_ride_num = @eighteen_ride_num,
    stnd_bsst_id = @stnd_bsst_id,
    three_alight_num = @three_alight_num,
    twelve_alight_num = @twelve_alight_num,
    use_mon = @use_mon,
    two_alight_num = @two_alight_num,
    twenty_alight_num = @twenty_alight_num,
    ten_alight_num = @ten_alight_num,
    one_ride_num = @one_ride_num,
    thirteen_alight_num = @thirteen_alight_num,
    four_ride_num = @four_ride_num,
    sixteen_alight_num = @sixteen_alight_num,
    five_ride_num = @five_ride_num,
    fourteen_alight_num = @fourteen_alight_num,
    six_alight_num = @six_alight_num,
    nineteen_ride_num = @nineteen_ride_num,
    two_ride_num = @two_ride_num,
    ten_ride_num = @ten_ride_num,
    twenty_three_ride_num = @twenty_three_ride_num,
    one_alight_num = @one_alight_num,
    midnight_alight_num = @midnight_alight_num,
    fifteen_alight_num = @fifteen_alight_num,
    seven_ride_num = @seven_ride_num,
    nine_ride_num = @nine_ride_num,
    eight_ride_num = @eight_ride_num,
    twenty_one_ride_num = @twenty_one_ride_num,
    twenty_ride_num = @twenty_ride_num,
    twenty_one_alight_num = @twenty_one_alight_num,
    twelve_ride_num = @twelve_ride_num,
    three_ride_num = @three_ride_num,
    work_dt = @work_dt,
    midnight_ride_num = @midnight_ride_num,
    eleven_ride_num = @eleven_ride_num,
    bsst_ars_no_id = @bsst_ars_no_id,
    bus_route_no_id = @bus_route_no_id,
    mntn_typ_cd_id = @mntn_typ_cd_id;