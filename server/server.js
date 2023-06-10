const express = require('express')
const axios = require("axios");
const mysql = require("mysql2/promise");
const app = express()
const port = 3000

let STEP = 10;

app.use(express.json());
app.use(express.static('/app/front/build'));

/**
 * Trigger bus ride data api
 * 
 * Request Body
 * {
 *   "start_year": 2021,
 *   "start_month": 1,
 *   "end_year": 2021,
 *   "end_month": 12
 * }
 */
app.post('/api/bus-ride', (req, res) => {
  busRide(req.body.start_year, req.body.start_month, req.body.end_year, req.body.end_month, async (log) => {
    res.send(log);
  });
})

/**
 * Trigger bus stop data api
 */
app.post('/api/bus-stop', (req, res) => {
  busStop(async (log) => {
    res.send(log);
  });
})

/**
 * Post raw-db mysql query
 * 
 * Request Body
 * {
 *  "query": "SELECT * FROM seoulRide"
 * }
 */
app.post('/api/raw-db', async (req, res) => {
  let connection = await mysql.createConnection({
    host: process.env.RAW_MYSQL_HOST,
    user: process.env.RAW_MYSQL_USER,
    password: process.env.RAW_MYSQL_ROOT_PASSWORD,
    database: process.env.RAW_MYSQL_DATABASE,
    insecureAuth: true,
  });
  let results = await connection.query(
    req.body.query
  );
  res.send(results);
})

/**
 * Post tartget-db mysql query
 * 
 * Request Body
 * {
 * "query": "SELECT * FROM seoulRide"
 * }
 */
app.post('/api/target-db', async (req, res) => {
  let connection = await mysql.createConnection({
    host: process.env.TARGET_MYSQL_HOST,
    user: process.env.TARGET_MYSQL_USER,
    password: process.env.TARGET_MYSQL_ROOT_PASSWORD,
    database: process.env.TARGET_MYSQL_DATABASE,
    insecureAuth: true,
  });
  let results = await connection.query(
    req.body.query
  );
  res.send(results);
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})

/**
 * Get bus rider data from Seoul Open API and store it in MySQL raw-db
 * 
 * META 
 * 공개 일자 | 2016.01.04.
 *
 */
async function busRide(start_year, start_month, end_year, end_month, callback) {
  let connection = await mysql.createConnection({
    host: process.env.RAW_MYSQL_HOST,
    user: process.env.RAW_MYSQL_USER,
    password: process.env.RAW_MYSQL_ROOT_PASSWORD,
    database: process.env.RAW_MYSQL_DATABASE,
    insecureAuth: true,
  });

  let logs = [];
  // start from start_year, start_month and finish at end_year, end_month
  for (var year = start_year; year <= end_year; year++) {
    var month_start = (year === start_year) ? start_month : 1;
    var month_end = (year === end_year) ? end_month : 12;

    for (var month = month_start; month <= month_end; month++) {
      console.log(year, month);
      let page = 1;
      while (true) {
        let url = "http://openapi.seoul.go.kr:8088/" + process.env.SEOUL_API_KEY + "/json/CardBusTimeNew/" + page + "/" + (page + STEP - 1) + "/" + year + (month >= 10 ? '' : '0') + month;
        let response = await axios.get(url);
        let body = response.data;
        console.log("body: ", body);
        if (body.CardBusTimeNew?.RESULT?.MESSAGE !== undefined && body.CardBusTimeNew.RESULT.MESSAGE === "정상 처리되었습니다") {
          for (let element of body.CardBusTimeNew.row) {
            let results = await connection.query(
              "INSERT INTO seoulRide SET ?",
              element
            );
            console.log(results);
            if (page < STEP && year === start_year && month === start_month) {
              logs.push(results);
            }
          }
        }
        else {
          console.log("Response received", body);
          break;
        }
        page += STEP;
        if (page === STEP + 1 && year === start_year && month === start_month) {
          callback(logs);
        }
      };
    }
  }
}

/*
 *   Get Bus Stop Location Info
 *
 */
async function busStop(callback) {
  let connection = await mysql.createConnection({
    host: process.env.RAW_MYSQL_HOST,
    user: process.env.RAW_MYSQL_USER,
    password: process.env.RAW_MYSQL_ROOT_PASSWORD,
    database: process.env.RAW_MYSQL_DATABASE,
    insecureAuth: true,
  });
  let page = 1;

  let logs = [];
  while (true) {
    let url = "http://openapi.seoul.go.kr:8088/" + process.env.SEOUL_API_KEY + "/json/busStopLocationXyInfo/" + page + "/" + (page + STEP - 1) + "/";

    let response = await axios.get(url);
    let body = response.data;
    //console.log("body: ", body);
    if (body.busStopLocationXyInfo?.RESULT?.MESSAGE !== undefined && body.busStopLocationXyInfo.RESULT.MESSAGE === "정상 처리되었습니다") {
      //console.log("Response received", body);
      for (let element of body.busStopLocationXyInfo.row) {
        let results = await connection.query(
          "INSERT INTO busStation SET ?",
          element
        );
        console.log(results);
        if (page < STEP) {
          logs.push(results);
        }
      }
    }
    else {
      console.log("Response received", body);
      break;
    }
    page += STEP;
    if (page === STEP + 1) {
      callback(logs);
    }
  }
}
