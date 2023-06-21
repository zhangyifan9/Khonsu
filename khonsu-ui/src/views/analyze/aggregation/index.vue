<template xmlns="http://www.w3.org/1999/html">
  <el-container style="border: 1px solid #eeeeee;">
    <el-aside width="600px" style="overflow: hidden; height: 785px; background-color: rgb(255,255,255)">
      <el-header style="width: 100%; height: 50px; background-color: rgb(255,255,255)">
        <el-form style="padding-top: 10px">
          <el-form-item label="当前项目" style="font-weight: bolder; font-size: 25px; font-family: 'Times New Roman'">
            {{ this.repoName }}
          </el-form-item>
        </el-form>
      </el-header>
      <el-container style="background: #ffffff">
        <el-header style="background: #ffffff">
          <el-input style="margin-top: 10px; width: 400px" placeholder="输入关键字进行过滤" v-model="filterText">
          </el-input>
        </el-header>
        <el-main style="max-height: 645px">
          <div>
            <el-tree
              v-loading="treeLoading"
              :data="treeData"
              :props="treeProps"
              :highlight-current="true"
              :filter-node-method="filterNode"
              @node-click="handleTreeClick"
              :render-content="renderContent"
              ref="tree">
            </el-tree>
          </div>
        </el-main>
      </el-container>
    </el-aside>
    <el-container>
      <el-main style="background: #ffffff">
        <span class="font_custom" style="font-weight: bolder; font-size: 15px; color: #3A71A8">
          {{ this.classPath.split("/")[0] }}
        </span>
        <div class="font_custom my-container" style="font-weight: bolder; font-size: 15px">
          <span>{{ handleClassPath(this.classPath) }}</span>
          <el-switch
            style="padding-right: 10px"
            v-model="withBase"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="包含base"
            inactive-text="">
          </el-switch>
        </div>
        <span class="font_custom" style="font-weight: bolder; font-size: 15px; color: #C03639">
          {{ this.classPath.split("/").reverse()[0] }}
        </span>
        <el-table :data="pageData"
                  border
                  stripe
                  v-loading="tableLoading"
                  style="margin-top: 10px;"
                  :default-sort = "{prop: 'tags', order: 'descending'}">
          <el-table-column
            prop="tags"
            label="涉及版本"
            sortable
            align="center">
            <template slot-scope="scope">
              <el-button class="font_custom" type="text" @click="handleTableClick(scope.row)"
                         style="font-weight: bold;">
                {{ scope.row.tags.replaceAll("&", " | ") }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="note"
            label="生命周期"
            width="200px"
            align="center">
            <template slot-scope="scope">
              <span v-if="isAdd(scope.row.n1)" class="font_custom" style="font-weight: bolder; color:#28a745">{{ scope.row.n1 }}</span>
              <span v-else class="font_custom" style="font-weight: bolder;">{{ scope.row.n1}}</span>
              <br/>
              <span v-if="isDelete(scope.row.n2)" class="font_custom" style="font-weight: bolder; color: #cb2431">{{ scope.row.n2 }}</span>
              <span v-else class="font_custom" style="font-weight: bolder;">{{ scope.row.n2 }}</span>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          style="text-align: center; padding-top: 20px"
          @current-change="currentPage = $event"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="tableData.length">
        </el-pagination>
      </el-main>
    </el-container>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
      <div style="max-height: 600px; overflow-y: auto;">
        <pre><code ref="code"></code></pre>
      </div>
    </el-dialog>
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

.my-container {
  height: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.font_custom {
  font-family: "Times New Roman";
}
</style>

<script>
import {getClassPath} from "@/api/analyze/detail";
import hljs from "highlight.js";
import 'highlight.js/styles/atom-one-dark.css';

export default {
  name: 'Aggregation',
  mounted() {
    this.repoName = this.$route.params.name;
    this.getClassPathTree(this.$route.params.name);
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
    withBase(val) {
      if (val) {
        this.tableData = this.originData;
      } else {
        this.tableData = this.originData.filter(item => !(item.tags.split("&").length == 1 && item.tags === "base"));
      }
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
  data() {
    return {
      repoName: "spring-framework",
      filterText: "",
      tableData: [],
      treeData: [],
      treeLoading: false,
      treeProps: {
        children: 'children',
        label: 'label',
        path: 'path',
        code: 'code'
      },
      dialogVisible: false,
      dialogTitle: "",
      tableLoading: false,
      classPath: "",
      currentPage: 1,
      pageSize: 8,
      total: 0,
      originData: [],
      withBase: true
    }
  },
  methods: {
    handleClassPath(classPath) {
      let arr = classPath.split("/");
      arr.shift();
      arr.pop();
      return arr.reverse().join("/");
    },
    handleTableClick(row) {
      this.dialogVisible = true;
      let index = this.classPath.lastIndexOf('/');
      this.dialogTitle = row.tags + " / " + this.classPath.substring(index + 1);
      this.$nextTick(() => {
        this.$refs.code.textContent = row.content;
        hljs.highlightBlock(this.$refs.code, 'java');
      });
    },
    getClassPathTree(repoName) {
      this.treeLoading = true;
      getClassPath(repoName).then(response => {
        console.log(response.data);
        this.treeData = [];
        response.data.forEach(path => {
          const pathSegments = path.classPath.split("/");
          let currentNode = {children: this.treeData, path: ""};
          pathSegments.forEach((segment, index) => {
            currentNode = currentNode.children.find(node => node.label === segment) ||
              currentNode.children[currentNode.children.push({
                label: segment,
                children: [],
                path: currentNode.path + "/" + segment,
                code: index === pathSegments.length - 1 ? path.codeBlocks : null
              }) - 1];
          });
        });
        console.log(this.treeData);
        this.treeLoading = false;
      });
    },
    // 渲染树形结构
    renderContent(h, {node}) {
      return h('el-tooltip', {
        props: {
          content: `${node.data.label}`,
          openDelay: 500
        }
      }, [
        h('div', {
          class: 'tree-node-content',
          style: {
            fontWeight: 'bold',
            fontFamily: 'Times New Roman'
          }
        }, [`${node.data.label}`])
      ])
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    handleTreeClick(data, node) {
      console.log(node);
      console.log(data);
      if (data.children.length != 0) {
        if (node.expanded) {
          node.expanded = false;
        } else {
          this.expandNode(data, node);
        }
      } else {
        this.classPath = data.path.substring(1);
        console.log(data.code);
        this.originData = data.code;
        for (let i = 0; i < this.originData.length; i++) {
          let notes = this.originData[i].note.split("，");
          this.originData[i].n1 = notes[0];
          this.originData[i].n2 = notes[1];
        }
        this.tableData = this.originData;
        console.log(this.classPath);
      }
    },
    // 递归展开节点及其所有子节点
    expandNode(data, node) {
      node.expanded = true;
      const children = data.children;
      const childrenNode = node.childNodes;
      if (children.length === 0) {
        return;
      }
      for (let i = 0; i < children.length; i++) {
        const child = children[i];
        const childNode = childrenNode[i];
        this.expandNode(child, childNode);
      }
    },
    isAdd(note) {
      return note.includes("新增");
    },
    isDelete(note) {
      return note.includes("删除");
    }
  }
};
</script>
