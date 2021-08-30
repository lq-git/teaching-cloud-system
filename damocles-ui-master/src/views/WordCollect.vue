<template>
  <div class="infinite-list-wrapper" style="overflow:auto">
    <div class="infinite-grid" v-infinite-scroll="load" infinite-scroll-disabled="disabled">
      <el-card :body-style="{ padding: '0px' }" v-for="(word,index) in wordData.records" class="list-item" :key="index" shadow="hover">
        <div class="card-body">
          {{ word.english }}
        </div>
        <div style="padding: 14px;">
          <span>{{ word.chinese }}</span>
          <div class="bottom clearfix">
            <time class="time">{{ word.createTime }}</time>
            <el-button type="text" :class="'button '+ (word.collectStatus ? 'btn_active' : '')" @click="handleCollect(word)"><i class="iconfont icon-Collection"></i></el-button>
            <el-button type="text" :class="'button '+ (word.likeStatus ? 'btn_active' : '')" @click="handleLike(word)"><i class="iconfont icon-dianzan1"></i> {{ word.likeNum }} </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
  <div class="tip">
    <div v-if="loading">加载中...</div>
    <div v-if="noMore">没有更多了</div>
  </div>
</template>
<script lang='ts'>
import { computed, defineComponent, reactive, ref } from "vue";
import Word from "../types/word";
import { request, responseEntity } from "../utils/request";
export default defineComponent({
  name: "WordCollect",
  setup() {
    const currentDate = ref(new Date());
    const size = ref(0);
    const wordData = reactive({ records: [] as Array<Word>, total: 0 });
    const loading = ref(false);
    const noMore = computed(() => size.value >= wordData.total);
    const disabled = computed(() => noMore.value || loading.value);
    const query = (size = 16) => {
      request("/word/record/query", { size: size, collectStatus: true }).then(
        (response) => {
          const res = response as responseEntity;
          if (res.code === 0) {
            const data = res.data as any;
            wordData.records = data.records;
            wordData.total = data.total;
          }
        }
      );
    };
    const load = () => {
      loading.value = true;
      setTimeout(() => {
        query((size.value += 4));
        loading.value = false;
      }, 1000);
    };
    load();
    const wordLib: Array<string> = [];
    const fillWordLib = () => {
      for (let i = 0; i < 16; i++) {
        if (i > 9) wordLib.push(String.fromCharCode(87 + i));
        else wordLib.push(i.toString());
      }
    };
    const randomWord = () => {
      fillWordLib();
      return wordLib[Math.round(Math.random() * 15)];
    };
    return {
      currentDate,
      size,
      loading,
      noMore,
      disabled,
      wordData,
      query,
      load,
      randomWord,
    };
  },
  methods: {
    resetForm(formName: string) {
      const _this = this as any;
      _this.$refs[formName].resetFields();
    },
    handleLike(word: Word) {
      word.likeStatus = !word.likeStatus;
      let likeNum = word.likeNum as number;
      word.likeNum = word.likeStatus ? ++likeNum : --likeNum;
      request("/word/record/like", word, "post");
    },
    handleCollect(word: Word) {
      word.collectStatus = !word.collectStatus;
      request("/word/record/collect", word, "post");
    },
  },
});
</script>
<style scoped>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  color: #606266 !important;
  padding: 0;
  float: right;
}

.btn_active {
  color: #409eff !important;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.list-item {
  width: 300px;
  margin-bottom: 15px;
  margin: 20px;
}

.clearfix:after {
  clear: both;
}

.card-body {
  height: 200px;
  text-align: center;
  line-height: 200px;
  font-size: 22px;
  font-weight: bold;
  border: 1px solid #ebeef5;
  border-radius: 2px;
  box-shadow: -5px 5px 5px #888888;
  color: transparent;
}

.infinite-grid >>> .el-card__body {
  padding: 10px !important;
}

.infinite-list-wrapper {
  height: 670px;
}

.infinite-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  align-items: flex-start;
}

.tip {
  text-align: center;
}
</style>