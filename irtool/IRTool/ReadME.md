# 项目说明文档
该Demo实现了文本读取、预处理及VSM与LSI两种IR算法

提供的数据用于计算代码与需求之间的文本相似度

# 目录结构描述
.
├── data
│   ├── iTrust-master  // &ensp;&ensp; iTrust项目源码
│   ├── stopwords  // &ensp;&ensp; 停用词文档
│   └── uc_origin  // &ensp;&ensp; 需求文档
├── lib
│   ├── commons-math3-3.0-sources.jar
│   └── commons-math3-3.6.jar
├── out
├── result  // &ensp;&ensp; 数据处理结果
│   ├── class  // &ensp;&ensp; 提取项目中的所有代文件
│   ├── class_preprocessed  // &ensp;&ensp; 预处理后的代码文本
│   ├── class_txt  // &ensp;&ensp; 将代码文件转换成txt文本
│   └── uc_preprocessed  // &ensp;&ensp; 预处理后的需求文本
├── src
│   ├── Test.java  // &ensp;&ensp; 运行入口
│   ├── document  // &ensp;&ensp; 存储读取的文件
│   │   ├── Artifact.java
│   │   ├── ArtifactsCollection.java
│   │   ├── LinksList.java
│   │   ├── SimilarityMatrix.java
│   │   ├── SingleLink.java
│   │   ├── StringHashSet.java
│   │   ├── TermDocumentMatrix.java
│   │   └── TextDataset.java
│   ├── ir  // &ensp;&ensp; IR算法实现
│   │   ├── IR.java
│   │   ├── IRModel.java
│   │   ├── LSI.java
│   │   └── VSM.java
│   ├── preprocess  // &ensp;&ensp; 文本预处理
│   │   ├── BatchingPreprocess.java
│   │   ├── CamelCase.java
│   │   ├── CleanUp.java
│   │   ├── DataPreprecess.java
│   │   ├── Snowball.java
│   │   ├── Stopwords.java
│   │   ├── TextPreprocess.java
│   │   └── snowball //
│   │       ├── Among.java
│   │       ├── EnglishStemmer.java
│   │       ├── SnowballProgram.java
│   │       └── SnowballStemmer.java
│   └── util  // &ensp;&ensp; 工具类
│       ├── FileIOUtil.java  // &ensp;&ensp; 文件读取写入
│       ├── GetClassFileUtil.java  // &ensp;&ensp; 从文件夹中读取所有.java与.jsp文件
│       └── TransferToTxtUtil.java  // &ensp;&ensp; 代码文件转换成txt文本
└── ReadME.md


