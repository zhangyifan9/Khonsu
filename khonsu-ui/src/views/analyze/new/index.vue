<template>
  <el-container style="width: 100%; height: 100%" v-loading="analyzeLoading" element-loading-text="分析项目中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.2)">
    <el-header style="background: #ffffff">
      <h2 style="font-weight: bolder">新建项目</h2>
    </el-header>
    <el-main style="height: 600px;background: #ffffff">
      <el-form ref="form" label-width="120px" class="form">
        <!-- 第一步 -->
        <div v-show="currentStep === 0">
          <el-form-item label="项目地址" style="font-size: 28px">
            <span> https://github.com/ </span>
            <el-input placeholder="请输入GitHub项目作者" v-model="userName" style="width: 200px"></el-input>
            <span> / </span>
            <el-input placeholder="请输入GitHub项目仓库名" v-model="repoName" style="width: 200px"></el-input>
          </el-form-item>
          <el-form-item label="筛选tag范围">
            <el-select
              v-model="tag1"
              placeholder="请选择"
              :loading="selectLoading"
              :loading-text="selectLoadingText"
              @visible-change="displayTags">
              <el-option
                v-for="item in allTags"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <span>~~~</span>
            <el-select
              v-model="tag2"
              placeholder="请选择">
              <el-option
                v-for="item in allTags"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <el-button style="margin-left: 20px" type="info" @click="getTags">获取</el-button>
          </el-form-item>
          <el-form-item label="tag详情">
            <el-table :data="tableData"
                      border
                      stripe
                      style="max-height: 400px; overflow-y: auto"
                      v-loading="tableLoading"
                      element-loading-spinner="el-icon-loading"
                      element-loading-text="拼命加载中"
                      @selection-change="handleSelectionChange">
              <el-table-column
                align="center"
                prop="tag"
                label="tag"
                width="150px">
              </el-table-column>
              <el-table-column
                align="center"
                prop="message"
                label="提交信息"
                width="240px"
                show-overflow-tooltip>
              </el-table-column>
              <el-table-column
                prop="release"
                label="是否为release"
                width="155px"
                align="center">
                <template slot-scope="scope">
                  <span v-if="scope.row.release">
                    <a href="javascript:void(0);" @click="openLink(scope.row)"
                       :style="{ 'text-decoration': 'underline', 'color': 'blue' }" :attr="{ target: '_blank' }">{{
                        "是"
                      }}</a>
                  </span>
                  <span v-else>{{ "否" }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="taggerName"
                label="标记者"
                align="center"
                width="150px">
              </el-table-column>
              <el-table-column
                prop="taggerEmail"
                label="标记者邮箱"
                align="center"
                width="300px">
              </el-table-column>
              <el-table-column
                prop="taggerDate"
                label="标记日期"
                align="center"
                width="210px">
              </el-table-column>
              <el-table-column
                type="selection"
                width="70px"
                align="center">
              </el-table-column>
            </el-table>
          </el-form-item>
        </div>
        <!-- 第二步 -->
        <div v-show="currentStep >= 1">
          <el-form-item label="已选版本">
            {{ selection.map(item => item.tag).join(", ") }}
          </el-form-item>
          <el-form-item label="release">
            <el-tooltip :content="releaseTip" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            <el-input class="my-textarea" v-model="rns" :rows="22" type="textarea" placeholder="请输入内容"
                      style="width: 1300px; height: 500px; overflow-y: auto; resize: none;" v-loading="rnsLoading" element-loading-text="正在自动生成release中">
            </el-input>
          </el-form-item>
        </div>
      </el-form>
    </el-main>
    <el-footer style="background: #ffffff">
      <div>
        <el-steps :active="currentStep" finish-status="success" align-center>
          <el-step title="选择版本"></el-step>
          <el-step title="release整合"></el-step>
        </el-steps>
        <div class="button-group">
          <el-button :disabled="currentStep === 0" type="danger" @click="prev">返回</el-button>
          <el-button v-show="currentStep < 1" type="primary" @click="next">前进</el-button>
          <el-button v-show="currentStep >= 1" type="success" @click="analyzeProject">提交</el-button>
        </div>
      </div>
    </el-footer>
  </el-container>
</template>

<style scoped>
.form {
  margin-left: 150px;
  margin-right: 280px;
}

.button-group {
  margin-top: 20px;
  text-align: center;
}

.button-group > button {
  margin: 0 10px;
}

.my-textarea {
  height: 500px;
  width: 1300px;
}
</style>

<script>
import {getTags, displayTags, getRNs, analyze} from "@/api/analyze/new";

export default {
  name: 'New',
  data() {
    return {
      currentStep: 0,
      userName: '',
      repoName: '',
      tableData: [],
      tag1: '',
      tag2: '',
      allTags: [],
      rns: '',
      selection: [],
      query: {
        userName: '',
        repoName: '',
        tags: '',
        rns: ''
      },
      selectLoading: false,
      selectLoadingText: '加载中，请稍等',
      tableLoading: false,
      rnsLoading: false,
      analyzeLoading: false,
      releaseTip: "格式为    版本间用'============================'分割，版本内部用'========'分割，一个版本release包括版本号/分类s/分类下条目s"
    };
  },
  methods: {
    // 上一步按钮点击事件
    prev() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    // 下一步按钮点击事件
    next() {
      if (this.currentStep < 1) {
        this.currentStep++
      }
      let tags = this.selection.map(item => item.tag);
      let tagsStr = tags.join("&");
      console.log(tagsStr)
      this.rnsLoading = true;
      getRNs(this.userName, this.repoName, tagsStr).then(response => {
        console.log(response.data);
        this.rnsLoading = false;
        this.rns = response.data.join("\n============================\n");
      })
    },
    displayTags(visible) {
      if (visible && !this.allTags.length) {
        this.selectLoading = true;
        displayTags(this.userName, this.repoName).then(response => {
          this.selectLoading = false;
          // this.allTags = response.data;
          console.log(response.data);
          this.allTags = response.data.map(option => ({
            value: option.tag,
            label: option.tag + '[' + option.date + ']'
          }))
          let allTags = this.allTags;
          console.log(allTags);
        })
      }
    },
    getTags() {
      this.tableLoading = true;
      getTags(this.userName, this.repoName, this.tag1, this.tag2).then(response => {
        console.log(response.data);
        this.tableLoading = false;
        this.tableData = response.data;
      })
    },
    // 分析项目
    analyzeProject() {
      this.currentStep++;
      this.query.userName = this.userName;
      this.query.repoName = this.repoName;
      this.query.tags = this.selection.map(item => item.tag).join("&");
      this.query.rns = this.rns;
      this.analyzeLoading = true;
      analyze(this.query).then(response => {
        console.log("分析完成");
        this.analyzeLoading = false;
        this.$router.push({
          name: 'Detail',
          params: {
            owner: this.userName,
            name: this.repoName
          }
        })
      })
    },
    openLink(row) {
      console.log(row);
      if (row.release) {
        let url = "https://github.com/" + this.userName + "/" + this.repoName + "/releases/tag/" + row.tag;
        console.log(url);
        window.open(url);
      }
    },
    handleSelectionChange(value) {
      this.selection = value;
    }
  }
};
</script>
