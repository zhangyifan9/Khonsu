<template>
  <el-container>
    <el-header height="30px">
      <h2 style="font-size: 25px; font-weight: bolder">
        <span>{{ this.titleName }}</span>
        <span style="color: red"> 隐式提交检索 </span>
      </h2>
    </el-header>
    <el-main>
      <el-switch
        style="float: right;"
        v-model="withTest"
        active-color="#13ce66"
        inactive-color="#ff4949"
        active-text="包含test"
        inactive-text="">
      </el-switch>
      <el-switch
        style="float: right; padding-right: 10px"
        v-model="withDelete"
        active-color="#13ce66"
        inactive-color="#ff4949"
        active-text="包含删除"
        inactive-text="">
      </el-switch>
      <el-switch
        style="float: right; padding-right: 10px"
        v-model="withAdd"
        active-color="#13ce66"
        inactive-color="#ff4949"
        active-text="包含新增"
        inactive-text="">
      </el-switch>
      <el-table :data="pageData"
                border
                stripe
                :default-sort = "{prop: 'score', order: 'ascending'}"
                v-loading="tableLoading"
                style="margin-top: 40px">
        <el-table-column
          prop="classPath"
          label="代码块所在文件"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <div class="font_custom">
              {{ scope.row.classPath.split("\\")[0] }}
            </div>
            <div class="font_custom">
              {{ handleClassPath(scope.row.classPath) }}
            </div>
            <div>
              <el-button class="font_custom" type="text" @click="handleTableClick(scope.row)"
                         style="font-weight: bold;">
                {{ scope.row.classPath.split("\\").reverse()[0] }}
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="tags"
          label="涉及版本">
          <template slot-scope="scope">
            <span v-if="isAdd(scope.row.tags)" class="font_custom" style="font-weight: bolder; color:#28a745">{{ scope.row.tags }}</span>
            <span v-else class="font_custom" style="font-weight: bolder; color: #cb2431">{{ scope.row.tags }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="score"
          label="与所有release的平均相似度"
          align="center"
          width="250px"
          sortable>
          <template slot-scope="scope">
            <span class="font_custom">{{ scope.row.score }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="100px">
          <template slot-scope="scope">
            <el-button id='file-button' type="primary" size="mini" round @click="skipCodeFile(scope.row)">
              参考
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="text-align: center; padding-top: 30px"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="tableData.length"
        @current-change="currentPage = $event"
        layout="total, prev, pager, next, jumper">
      </el-pagination>
      <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
        <div style="max-height: 600px; overflow-y: auto;">
          <pre><code ref="code"></code></pre>
        </div>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<style scoped>
.el-dialog__body {
  padding: 20px;
}

.el-dialog__footer {
  padding: 20px;
  text-align: center;
}

.font_custom {
  font-family: "Times New Roman";
  font-weight: bolder;
}
</style>

<script>
import {getAvg, getCodeFile} from "@/api/analyze/detail";
import hljs from "highlight.js";
import 'highlight.js/styles/atom-one-dark.css';

export default {
  name: 'Avg',
  mounted() {
    this.userName = this.$route.params.userName
    this.repoName = this.$route.params.repoName;
    this.tag = this.$route.params.tag;
    this.titleName = this.repoName + "/" + this.tag;
    this.bottomCodeBlock();
  },
  data() {
    return {
      userName: "",
      repoName: "spring-framework",
      dialogVisible: false,
      dialogTitle: "",
      tag: "v6.0.6",
      tableLoading: false,
      tableData: [],
      pageSize: 5,
      currentPage: 1,
      originData: [],
      withTest: true,
      withDelete: true,
      withAdd: true
    }
  },
  watch: {
    withTest(val) {
      this.filterData(val, this.withDelete, this.withAdd);
      this.currentPage = 1;
    },
    withDelete(val) {
      this.filterData(this.withTest, val, this.withAdd);
      this.currentPage = 1;
    },
    withAdd(val) {
      this.filterData(this.withTest, this.withDelete, val);
      this.currentPage = 1;
    }
  },
  computed: {
    pageData() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.tableData.slice(startIndex, endIndex);
    }
  },
  methods: {
    skipCodeFile(row) {
      let path = row.classPath;
      getCodeFile(this.userName, this.repoName, this.tag, path).then(response => {
        console.log(response.msg);
        window.open(response.msg.split("||")[1]);
      })
    },
    handleClassPath(classPath) {
      let arr = classPath.split("\\");
      arr.shift();
      arr.pop();
      return arr.reverse().join("/");
    },
    // 单行点击显示代码片段
    handleTableClick(row) {
      this.dialogVisible = true;
      let index = row.classPath.lastIndexOf('\\');
      this.dialogTitle = row.classPath.substring(index + 1);
      this.$nextTick(() => {
        this.$refs.code.textContent = row.content;
        hljs.highlightBlock(this.$refs.code, 'java');
      });
    },
    bottomCodeBlock() {
      this.tableLoading = true;
      // 一般修改文件不可能大于10000
      getAvg(this.repoName, this.tag, false, 1000000).then(response => {
        this.tableLoading = false;
        this.originData = response.data;
        this.tableData = this.originData;
      })
    },
    isAdd(tags) {
      return tags.includes(this.tag);
    },
    filterData(withTest, withDelete, withAdd) {
      if (withDelete) {
        if (withTest) {
          if (withAdd) {
            this.tableData = this.originData;
          } else {
            this.tableData = this.originData.filter(item => !item.tags.includes(this.tag));
          }
        } else {
          if (withAdd) {
            this.tableData = this.originData.filter(item => !item.classPath.includes('test'));
          } else {
            this.tableData = this.originData.filter(item => (!item.classPath.includes('test') && !item.tags.includes(this.tag)));
          }
        }
      } else {
        if (withTest) {
          if (withAdd) {
            this.tableData = this.originData.filter(item => item.tags.includes(this.tag));
          } else {
            this.tableData = this.originData.filter(item => (item.tags.includes(this.tag) && !item.tags.includes(this.tag)));
          }
        } else {
          if (withAdd) {
            this.tableData = this.originData.filter(item => (!item.classPath.includes('test') && item.tags.includes(this.tag)));
          } else {
            this.tableData = this.originData.filter(item => (!item.tags.includes(this.tag) && item.tags.includes(this.tag)) && !item.classPath.includes('test'));
          }
        }
      }
      this.currentPage = 1;
    }
  }
};
</script>
