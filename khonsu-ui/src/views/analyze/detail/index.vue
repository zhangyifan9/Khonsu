<template xmlns="http://www.w3.org/1999/html">
  <el-container style="border: 1px solid #eeeeee;">
    <el-aside width="400px" style="overflow: hidden; height: 785px; background-color: rgb(255,255,255)">
      <el-header style="width: 100%; height: 165px; background-color: rgb(255,255,255)">
        <el-form style="padding-top: 10px">
          <el-form-item label="当前项目" style="font-weight: bolder; font-size: 25px; font-family: 'Times New Roman'">
            {{ repoName }}
          </el-form-item>
          <el-form-item label="选择版本">
            <el-select
              class="font_custom"
              v-model="tag"
              v-loading="selectLoading"
              placeholder="请选择"
              @change="getReleasesByTag">
              <el-option
                v-for="(tag, index) in tags"
                :key="index"
                :label="tag"
                :value="tag">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <div class="detail-container" style="margin-top: 10px;">
              <el-button type="success" icon="el-icon-info" plain @click="diffSkip">对比变化</el-button>
              <el-button type="danger" icon="el-icon-warning-outline" plain @click="bottomCodeBlock">隐式提交
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </el-header>
      <el-container style="background: #ffffff">
        <el-header style="background: #ffffff">
          <el-input style="margin-top: 30px" placeholder="输入关键字进行过滤" v-model="filterText">
          </el-input>
        </el-header>
        <el-main style="max-height: 645px">
          <div>
            <el-tree
              :data="treeData"
              :props="treeProps"
              accordion
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
        <div class="detail-container" style="margin-top: 20px; margin-bottom: 20px">
          <span>
            <span class="font_custom" style="font-size: 20px; font-weight: bold;">{{ release1 }}</span>
          </span>
          <span>
            <el-switch
              style="padding-right: 10px"
              v-model="withZero"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="包含0"
              inactive-text="">
            </el-switch>
            <el-switch
              style="padding-right: 10px"
              v-model="withTest"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="包含test"
              inactive-text="">
            </el-switch>
            <el-button type="primary" plain @click="updateState2">全部已读</el-button>
          </span>
        </div>
        <div class="detail-container" style="margin-top: 20px; margin-bottom: 20px">
          <span>
            <span class="font_custom" style="font-size: 15px; font-weight: bold">{{ release2 }}</span>
            <span style="padding-left: 10px">
              <el-button v-for="(text, index) in releaseNum" :key="index" type="text" @click="issueClick(text)"
                         style="font-weight: bolder">
                {{ `链接${index + 1}` }}
              </el-button>
            </span>
          </span>
          <el-input v-model="filterCodeBlock" style="width: 300px" placeholder="请输入关键词"></el-input>
        </div>
        <el-table :data="pageData"
                  border
                  stripe
                  v-loading="tableLoading"
                  style="margin-top: 20px">
          <el-table-column
            prop="classPath"
            label="代码块所在文件"
            width="600"
            show-overflow-tooltip>
            <template slot-scope="scope">
              <div class="font_custom" style="font-weight: bolder">
                {{ scope.row.classPath.split("\\")[0] }}
              </div>
              <div class="font_custom" style="font-weight: bolder">
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
            label="涉及版本"
            width="400">
            <template slot-scope="scope">
              <span v-if="isAdd(scope.row.tags)" class="font_custom" style="font-weight: bolder; color:#28a745">{{ scope.row.tags }}</span>
              <span v-else class="font_custom" style="font-weight: bolder; color: #cb2431">{{ scope.row.tags }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="score"
            label="相似度"
            width="145px"
            align="center">
            <template slot-scope="scope">
              <span :style="handleIsChange(scope.row)">{{ scope.row.score }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center">
            <template slot-scope="scope">
              <el-button id='file-button' type="primary" size="mini" round @click.stop="skipCodeFile(scope.row)">
                参考
              </el-button>
              <el-button type="success" size="mini" round @click.stop="setScore1(scope.row)">
                接受
              </el-button>
              <el-button type="info" size="mini" round @click.stop="setScore0(scope.row)">
                拒绝
              </el-button>
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

.detail-container {
  height: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.font_custom {
  font-family: "Times New Roman";
}
</style>

<script>
import {getReleaseByRepoName, getCodeByReleaseId, updateState, updateScore, getBase, getCodeFile} from "@/api/analyze/detail";
import hljs from "highlight.js";
import 'highlight.js/styles/atom-one-dark.css';

export default {
  name: 'Detail',
  mounted() {
    this.repoName = this.$route.params.name;
    this.userName = this.$route.params.owner;
    this.getReleases();
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
    filterCodeBlock(val) {
      if (val != "") {
        this.tableData = this.tableData.filter(item => item.classPath.toLowerCase().indexOf(val.toLowerCase()) != -1)
      } else {
        this.tableData = this.tableDataBeforeFilter;
      }
    },
    withTest(val) {
      if (this.withZero) {
        if (val) {
          this.tableData = this.originData;
          this.tableDataBeforeFilter = this.tableData;
        } else {
          this.tableData = this.originData.filter(item => !item.classPath.includes('test'));
          this.tableDataBeforeFilter = this.tableData;
        }
      } else {
        if (val) {
          this.tableData = this.originData.filter(item => item.score != "0.00000");
          this.tableDataBeforeFilter = this.tableData;
        } else {
          this.tableData = this.originData.filter(item => (!item.classPath.includes('test') && item.score != "0.00000"));
          this.tableDataBeforeFilter = this.tableData;
        }
      }
      this.currentPage = 1;
    },
    withZero(val) {
      if (this.withTest) {
        if (val) {
          this.tableData = this.originData;
          this.tableDataBeforeFilter = this.tableData;
        } else {
          this.tableData = this.originData.filter(item => item.score != "0.00000");
          this.tableDataBeforeFilter = this.tableData;
        }
      } else {
        if (val) {
          this.tableData = this.originData.filter(item => !item.classPath.includes('test'));
          this.tableDataBeforeFilter = this.tableData;
        } else {
          this.tableData = this.originData.filter(item => (!item.classPath.includes('test') && item.score != "0.00000"));
          this.tableDataBeforeFilter = this.tableData;
        }
      }
      this.currentPage = 1;
    }
  },
  computed: {
    handleIsChange() {
      return function (row) {
        return {
          color: row.isChange ? 'red' : 'black'
        };
      };
    },
    pageData() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.tableData.slice(startIndex, endIndex);
    }
  },
  data() {
    const item = {
      classPath: '',
      tags: '',
      score: ''
    };
    return {
      repoName: "spring-framework",
      userName: "",
      filterText: "",
      tableData: Array(0).fill(item),
      projects: [],
      treeData: [],
      treeProps: {
        id: 'id',
        children: 'children',
        label: 'label',
        order: 'order',
        state: 'state',
        zero: 'zero',
        one: 'one',
        two: 'two'
      },
      releaseId: 0,
      currentPage: 1,
      pageSize: 5,
      total: 0,
      dialogVisible: false,
      dialogTitle: "",
      allNodes: [],
      tags: [],
      tag: "",
      release: "",
      tableLoading: false,
      node: null,
      hasOpen: false,
      base: "",
      selectLoading: false,
      parentNode: null,
      withTest: true,
      withZero: true,
      originData: [],
      filterCodeBlock: "",
      tableDataBeforeFilter: [],
      release1: "",
      release2: "",
      releaseNum: []
    }
  },
  methods: {
    // 渲染树形结构
    renderContent(h, {node}) {
      let hasChildren = (node.data.children != null) && (node.data.children.length > 0);
      if (hasChildren) {
        return h('div', {
          class: 'tree-node-content',
          style: {
            fontWeight: 'bold',
            color: 'rgb(139, 0, 0)',
            fontSize: '20px',
            fontFamily: 'Times New Roman'
          }
        }, [
          `${node.data.label} (`,
          h('span', {style: {color: 'rgb(173, 216, 230)'}}, `${node.data.two}`),
          h('span', {style: {color: 'rgb(255, 165, 0)'}}, `/${node.data.one}`),
          h('span', {style: {color: 'black'}}, `/${node.data.zero}`),
          ')'
        ])
      }
      if (node.data.state === 1) {
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
              fontFamily: 'Times New Roman',
              color: 'rgb(255, 165, 0)'
            }
          }, [`${node.data.order}` + `.` + `${node.data.label}`])
        ])
      } else if (node.data.state === 2) {
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
              fontFamily: 'Times New Roman',
              color: 'rgb(173, 216, 230)'
            }
          }, [`${node.data.order}` + `.` + `${node.data.label}`])
        ])
      }
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
        }, [`${node.data.order}` + `.` + `${node.data.label}`])
      ])
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    updateState(state) {
      console.log("state:" + state);
      // 默认为0没看过，1为看过，2为全部看过
      console.log(this.releaseId);
      updateState(this.releaseId, state).then(response => {
        console.log(response.msg);
      });
    },
    updateState2() {
      this.updateState(2);
      this.node.state = 2;
      this.parent.one--;
      this.parent.two++;
      this.$message('状态设置成功！');
    },
    getReleasesByTag() {
      this.tableData = [];
      this.getReleases();
      this.hasOpen = false;
      let node = this.allNodes.filter(item => item.label === this.tag);
      let categories = [];
      node.forEach(item => {
        item.children.forEach(category => {
          category.children.map((obj, index) => {
            obj.order = index + 1;
            if (obj.state === 0) category.zero++;
            if (obj.state === 1) category.one++;
            if (obj.state === 2) category.two++;
          });
          categories.push(category);
          console.log(category);
        });
      });
      this.treeData = categories;
    },
    getReleases() {
      // console.log("RepoName:" + this.repoName);
      let repoName = this.repoName;
      getReleaseByRepoName(repoName).then(response => {
        console.log(response);
        const newTreeData = response.data.reduce((acc, curr) => {
          const tagIndex = acc.findIndex(item => item.label === curr.tag);
          if (tagIndex === -1) {
            acc.push({
              label: curr.tag,
              children: [{
                label: curr.category,
                children: [{
                  label: curr.content,
                  id: curr.id,
                  state: curr.readState
                }],
                zero: 0,
                one: 0,
                two: 0
              }]
            });
          } else {
            const categoryIndex = acc[tagIndex].children.findIndex(item => item.label === curr.category);
            if (categoryIndex === -1) {
              acc[tagIndex].children.push({
                label: curr.category,
                children: [{
                  label: curr.content,
                  id: curr.id,
                  state: curr.readState,
                }],
                zero: 0,
                one: 0,
                two: 0
              });
            } else {
              acc[tagIndex].children[categoryIndex].children.push({
                label: curr.content,
                id: curr.id,
                state: curr.readState
              });
            }
          }
          return acc;
        }, []);

        if (this.tags.length === 0) {
          this.selectLoading = true;
          let tmpTags = [];
          newTreeData.forEach(node => {
            tmpTags.push(node.label);
          })
          tmpTags = this.sortVersionsDescending(tmpTags);
          console.log(tmpTags);
          this.tags = tmpTags;
          getBase(this.userName, this.repoName, this.tags[this.tags.length - 1]).then(response => {
            this.base = response.msg;
          })
          this.selectLoading = false;
        }
        this.allNodes = newTreeData;
      })
    },
    sortVersionsDescending(versions) {
      console.log("!!!!!!");
      return versions.sort((a, b) => {
        // 提取版本号中的数字和小数点
        const regex = /\d+(\.\d+)+/g;
        const aMatch = a.match(regex);
        const bMatch = b.match(regex);
        if (!aMatch || !bMatch) return 0;
        const aParts = aMatch[0].split('.');
        const bParts = bMatch[0].split('.');

        // 确定每个版本号部分的长度
        const maxParts = Math.max(aParts.length, bParts.length);
        const lengths = new Array(maxParts).fill(0);
        for (let i = 0; i < maxParts; i++) {
          lengths[i] = Math.max((aParts[i] || '').length, (bParts[i] || '').length);
        }

        // 在每个版本号部分前面填充 0，使它们的长度相同
        for (let i = 0; i < maxParts; i++) {
          aParts[i] = (aParts[i] || '').padStart(lengths[i], '0');
          bParts[i] = (bParts[i] || '').padStart(lengths[i], '0');
        }

        // 将填充后的版本号部分拼接成一个新的字符串，再进行比较
        const aStr = aParts.join('');
        const bStr = bParts.join('');
        return bStr.localeCompare(aStr);
      });
    },
    handleTreeClick(data) {
      if (data.id) {
        this.parent = this.findParentNode(this.treeData, data);
        // this.release = this.parent.label + '#=#' + data.order + '.' + data.label.substring(0, data.label.lastIndexOf("#")) + "#-#" + data.label.substring(data.label.lastIndexOf("#"));
        this.release1 = this.parent.label;
        this.release2 = data.order + '.' + data.label;
        const regex = /#(\d+)/g;
        console.log(data.label.match(regex));
        this.releaseNum = data.label.match(regex);
        // console.log(data.id);
        this.currentPage = 1;
        this.releaseId = data.id;
        this.getCodeBlocks(this.releaseId);
        this.node = data;
        this.withZero = true;
        this.withTest = true;
        if (data.state === 0) {
          this.updateState(1);
          this.node.state = 1;
          this.parent.zero--;
          this.parent.one++;
        }
      }
    },
    findParentNode(nodes, node) {
      for (let i = 0; i < nodes.length; i++) {
        if (nodes[i].children) {
          if (nodes[i].children.includes(node)) {
            return nodes[i];
          } else {
            const result = this.findParentNode(nodes[i].children, node);
            if (result) {
              return result;
            }
          }
        }
      }
      return null;
    },
    getCodeBlocks(id) {
      this.tableLoading = true;
      // getCodeByReleaseId(id, this.currentPage, this.pageSize).then(response => {
      //   this.tableLoading = false;
      //   console.log(response.data);
      //   this.tableData = response.data.codeBlockVOList;
      //   console.log(this.tableData);
      //   this.total = response.data.total;
      // })
      getCodeByReleaseId(id).then(response => {
        this.tableLoading = false;
        console.log(response.data);
        this.originData = response.data;
        this.tableData = this.originData;
        this.tableDataBeforeFilter = this.tableData;
        console.log(this.tableData);
      })
    },
    // // 处理分页
    // handleCurrentChange(val) {
    //   this.currentPage = val;
    //   this.getCodeBlocks(this.releaseId);
    // },
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
    issueClick(release) {
      let url = "https://github.com/" + this.userName + "/" + this.repoName + "/pull/" + release.substring(1) + "/files";
      // 打开指定的 URL
      console.log(url);
      window.open(url);
    },
    handleClassPath(classPath) {
      let arr = classPath.split("\\");
      arr.shift();
      arr.pop();
      return arr.reverse().join("/");
    },
    skipCodeFile(row) {
      let path = row.classPath;
      getCodeFile(this.userName, this.repoName, this.tag, path).then(response => {
        console.log(response.msg);
        window.open(response.msg.split("||")[0]);
      })
    },
    diffSkip() {
      // console.log(row.classPath);
      // console.log(index);
      // navigator.clipboard.writeText(row.classPath.substring(row.classPath.lastIndexOf('\\java\\') + 6).replaceAll('\\', '/'))
      //   .then(() => {
      //     console.log('Text copied to clipboard');
      //   })
      //   .catch((err) => {
      //     console.error('Failed to copy text: ', err);
      //   });
      // this.$message('复制类路径到剪切板');
      let index = this.tags.indexOf(this.tag);
      if (!this.hasOpen) {
        let url = "https://github.com/" + this.userName + "/" + this.repoName + "/compare/";
        if (index != this.tags.length - 1) {
          // 将 "url" 替换为要打开的 URL
          url = url + this.tags[index + 1] + "..." + this.tags[index] + "#files_bucket";
        } else {
          url = url + this.base + "..." + this.tags[index] + "#files_bucket";
        }
        // 打开指定的 URL
        console.log(url);
        let newWindow = window.open(url);
        // this.hasOpen = true;
      }
    },
    setScore1(row) {
      row.score = '1.00000';
      row.isChange = true;
      updateScore(this.releaseId, row.codeBlockId, '1.00000').then(response => {
        this.$message.success(response.msg);
      })
    },
    setScore0(row) {
      row.score = '0.00000';
      row.isChange = true;
      updateScore(this.releaseId, row.codeBlockId, '0.00000').then(response => {
        this.$message.success(response.msg);
      })
    },
    coreCodeBlock() {
      this.$router.push({
        name: 'Core',
        params: {
          repoName: this.repoName,
          tag: this.tag
        }
      })
    },
    bottomCodeBlock() {
      this.$router.push({
        name: 'Avg',
        params: {
          userName: this.userName,
          repoName: this.repoName,
          tag: this.tag
        }
      })
    },
    isAdd(tags) {
      return tags.includes(this.tag);
    }
  },
};
</script>
