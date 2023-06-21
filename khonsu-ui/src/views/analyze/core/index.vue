<template>
  <el-container>
    <el-header height="30px">
      <h2 style="font-size: 25px; font-weight: bolder">
        <span>{{ this.titleName }}</span>
        <span style="color: red"> 核心代码 </span>
      </h2>
    </el-header>
    <el-main>
      <el-switch
        style="float: right;"
        v-model="withTest"
        active-color="#13ce66"
        inactive-color="#ff4949"
        active-text="包含test"
        inactive-text="不包含test">
      </el-switch>
      <el-table :data="pageData"
                border
                stripe
                :default-sort = "{prop: 'tags', order: 'ascending'}"
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
          sortable
          label="涉及版本">
          <template slot-scope="scope">
              <span class="font_custom">
                {{ handleTags(scope.row.tags) }}
              </span>
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
import { getCore } from "@/api/analyze/detail";
import hljs from "highlight.js";
import 'highlight.js/styles/atom-one-dark.css';

export default {
  name: 'Core',
  mounted() {
    this.repoName = this.$route.params.repoName;
    this.tag = this.$route.params.tag;
    this.titleName = this.repoName + "/" + this.tag;
    this.coreCodeBlock();
  },
  data() {
    return {
      repoName: "spring-framework",
      dialogVisible: false,
      dialogTitle: "",
      tag: "v6.0.6",
      tableLoading: false,
      tableData: [],
      pageSize: 5,
      currentPage: 1,
      originData: [],
      withTest: true
    }
  },
  watch: {
    withTest(val) {
      if (val) {
        this.tableData = this.originData;
      } else {
        this.tableData = this.originData.filter(item => !item.classPath.includes('test'));
      }
      this.currentPage = 1;
    },
  },
  computed: {
    pageData() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.tableData.slice(startIndex, endIndex);
    }
  },
  methods: {
    handleTags(tags) {
      let arr = tags.split(" | ");
      arr.pop();
      return arr.join(" | ");
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
    coreCodeBlock() {
      this.tableLoading = true;
      getCore(this.repoName, this.tag).then(response => {
        this.tableLoading = false;
        this.originData = response.data;
        this.tableData = this.originData;
      })
    }
  }
};
</script>
