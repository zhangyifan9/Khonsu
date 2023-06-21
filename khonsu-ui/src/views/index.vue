<template>
  <el-row :gutter="20" style="margin-left: 30px; margin-right: 30px">
    <el-col>
      <div class="my-container">
        <span>
          <span style="margin-left: 20px;font-weight: bolder; font-size: 30px">我的项目</span>
          <el-button size="small" style="margin-left: 30px;" icon="el-icon-plus" type="primary" @click="clickNew">新建项目</el-button>
        </span>
        <span>
          <el-switch
            style="padding-right: 30px; padding-bottom: 5px"
            v-model="singleOrMulti"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="单版本"
            inactive-text="多版本">
          </el-switch>
          <el-input style="padding-top: 10px; width:300px;" placeholder="请输入关键字搜索"
                    v-model="projectName">
          <el-button slot="append" icon="el-icon-search" disabled></el-button>
        </el-input>
        </span>
      </div>
    </el-col>
    <el-col v-for="(item, index) in searchProject" :key="index" :xs="24" :sm="12" :md="8" :lg="6" v-loading="loading">
      <el-card style="margin-top: 10px; margin-bottom: 10px;"
               @click.native="handleCardClick(item.id, item.owner, item.name)">
        <div
          style="display: flex; align-items: center; justify-content: center; height: 200px; text-align: center; background-color: #bdeaea;">
          <span>{{ item.name }}</span>
        </div>
        <div style="padding: 1px; text-align: right;">
          <div class="bottom clearfix time">
            <span>上次修改时间：</span>
            <time>{{ item.date }}</time>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<style>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.el-card__body {
  pointer-events: none;
}

.my-container {
  margin-top: 40px;
  padding-bottom: 35px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-height: 80px;
}
</style>

<script>
import {getProject, updateProject} from "@/api/index";

export default {
  data() {
    return {
      list: [
        // {id: 1, name: 'spring-framework', date: '2023-03-15'},
        // {id: 112, name: 'maven', date: '2023-03-14'},
        // {id: 113, name: 'tomcat', date: '2023-03-13'},
        // {id: 114, name: 'lucene', date: '2023-03-12'},
        // {id: 115, name: 'spring-boot', date: '2023-03-11'},
        // {id: 116, name: 'elasticsearch', date: '2023-03-10'},
        // {id: 117, name: 'okhttp', date: '2023-03-09'},
        // {id: 118, name: 'dubbo', date: '2023-03-08'},
        // {id: 119, name: 'spark', date: '2023-03-07'},
        // {id: 120, name: 'fastjson', date: '2023-03-06'},
        // {id: 121, name: 'spring-cloud-alibaba', date: '2023-03-05'},
        // {id: 122, name: 'flink', date: '2023-03-04'},
      ],
      projectName: '',
      loading: false,
      singleOrMulti: true
    }
  },
  mounted() {
    this.loading = true;
    this.fetchData();
    this.loading = false;
  },
  methods: {
    fetchData() {
      getProject().then(response => {
        let projects = response.data;
        projects.forEach(item => {
          // 将后端传来的 Date 对象转为字符串
          let date = new Date(item.date);
          let year = date.getFullYear();
          let month = ('0' + (date.getMonth() + 1)).slice(-2);
          let day = ('0' + date.getDate()).slice(-2);
          // 创建一个新的对象，包含 id、name 和处理后的 date 字段
          const newItem = {
            id: item.id,
            owner: item.owner,
            name: item.name,
            date: year + '-' + month + '-' + day
          };
          // 将新的对象添加到已有列表中
          this.list.push(newItem);
        });
      })
    },
    handleCardClick(id, owner, name) {
      updateProject(id).then(response => {
        console.log(response.msg);
      });
      console.log(name);
      console.log(owner);
      if (this.singleOrMulti) {
        this.$router.push({
          name: 'Detail',
          params: {
            owner: owner,
            name: name
          }
        })
      } else {
        this.$router.push({
          name: 'Aggregation',
          params: {
            name: name
          }
        })
      }
    },
    clickNew() {
      this.$router.push({
        name: 'New'
      })
    }
  },
  computed: {
    searchProject() {
      const projectName = this.projectName.trim().toLowerCase();
      if (projectName) {
        return this.list.filter((item) =>
          item.name.toLowerCase().includes(projectName) || item.date.includes(projectName)
        );
      } else {
        return this.list;
      }
    }
  }
}
</script>


