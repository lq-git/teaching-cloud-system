<template>
  <div id="nav">
    <router-view></router-view>
  </div>
</template>

<style>
body {
  margin: 0;
}

.el-select {
  width: 202px;
}

/* 日报弹出层 */
.daily_container {
  width: 100%;
  height: 800px;
  border: 1px solid black;
}

.daily_container input.el-input__inner {
  width: 100px;
  border-radius: 0;
  border: 0;
  border-bottom: 1px solid #ccc;
}

.daily_container .header.two {
  float: right;
}

.daily_body {
  width: 100%;
  height: 630px;
  display: grid;
  grid-template-rows: repeat(6, 1fr);
  grid-template-columns: repeat(4, 1fr);
  grid-template-areas:
    "question1 content1 content1 content1"
    "question2 content2 content2 content2"
    "question3 content3 content3 content3"
    "question4 content4 content4 content4"
    "question5 content5 content5 content5"
    "question6 content6 content6 content6";
}

.daily_body .el-textarea__inner {
  width: 97%;
  border: 0;
  resize: none;
}

.daily_body > .item {
  border: 1px solid black;
}

.daily_body > .item:nth-child(1) {
  grid-area: question1;
  padding: 5px 0 0 5px;
}

.daily_body > .item:nth-child(2) {
  grid-area: question2;
  padding: 5px 0 0 5px;
}

.daily_body > .item:nth-child(3) {
  grid-area: question3;
  padding: 5px 0 0 5px;
}
.daily_body > .item:nth-child(4) {
  grid-area: question4;
  padding: 5px 0 0 5px;
}
.daily_body > .item:nth-child(5) {
  grid-area: question5;
  padding: 5px 0 0 5px;
}
.daily_body > .item:nth-child(6) {
  grid-area: question6;
  padding: 5px 0 0 5px;
}
.daily_body > .item:nth-child(7) {
  grid-area: content1;
}
.daily_body > .item:nth-child(8) {
  grid-area: content2;
}
.daily_body > .item:nth-child(9) {
  grid-area: content3;
}

.daily_body > .item:nth-child(10) {
  grid-area: content4;
}

.daily_body > .item:nth-child(11) {
  grid-area: content5;
}

.daily_body > .item:nth-child(12) {
  grid-area: content6;
}

ul {
  list-style-type: none;
}
</style>