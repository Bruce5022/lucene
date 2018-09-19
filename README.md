# Lucene 入门学习
# 为什么要用lucene,它在项目中存在的意义是什么?
例如:你要在数据库表中查询标题是:数据结构 清华大学 的记录</br>
用sql查询的话,是这样:title like '%数据结构%清华大学%'</br>
sql的问题:</br>
首先:like不走索引,效率低;</br>
其次:查询不准确,不会做匹配度排名;</br>
再次:也不会在结果中高亮显示匹配到的内容;</br>
最后:如果查询附件内容包含什么关键词的话,附件存哪里了呢,sql处理不到吧?</br>
这就是lucene的价值.</br>

# 全文索引工具组成部分
1.索引部分</br>
2.分词部分</br>
3.搜索部分</br>