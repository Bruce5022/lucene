package com.inca.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 所有的全文索引都应该跟随数据库的变化变化 必然需要增删改查操作
 * 
 * @author Bruce
 *
 */
public class CrudTestor {

	public static void testSearch(String keyword) throws IOException, ParseException {
		Directory directory = FSDirectory.open(Paths.get(Constants.INDEX_PATH));
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		//查询的两种方式：
		//1.通过QueryParser来解析字符串封装为Query对象，执行查询：这里的query其实是TermQuery
		//2.直接创建Query子类来查询
		Analyzer analyzer = new IKAnalyzer();
		QueryParser parser = new QueryParser(keyword, analyzer);
		Query query = parser.parse(keyword);
		System.out.println("真正的类型："+query.getClass());
		TopDocs hits = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			int docId = scoreDoc.doc;
			System.out.println(docId);
			Document doc = searcher.doc(docId);
			System.out.println(doc.get("content"));
		}
	}
	
	public static void testSearch2(Query query) throws IOException, ParseException {
		Directory directory = FSDirectory.open(Paths.get(Constants.INDEX_PATH));
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		System.out.println("query带的字符串："+query.toString());
		//2.直接创建Query子类来查询
		System.out.println("真正的类型："+query.getClass());
		TopDocs hits = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			int docId = scoreDoc.doc;
			System.out.println(docId);
			Document doc = searcher.doc(docId);
			System.out.println(doc.get("content"));
		}
	}
	
	public static void testDelete() throws IOException, ParseException {
		Directory directory = FSDirectory.open(Paths.get(Constants.INDEX_PATH));
		IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
		IndexWriter writer = new IndexWriter(directory, config);
		
//		writer.deleteAll();
		// 根据某个字段是否包含关键字删除
		// 分词的结果:原英文全变成小写,比较坑的地方;删除的时候,不管原单词英文字母什么样,都用小写
//		long result = writer.deleteDocuments(new Term("content","beat"));
//		System.out.println("结果:"+result);
		
		
		String keyword = "title:Beat";
		String defaultField = "content";
		Analyzer analyzer = new IKAnalyzer();
		QueryParser parser = new QueryParser(defaultField, analyzer);
		Query query = parser.parse(keyword);
		writer.deleteDocuments(query);
		
		writer.commit();
		writer.close();
	}

	public static void main(String[] args) throws IOException, ParseException {
		//下满方法效果一样：term查询
//		testSearch("title:beat");
//		TermQuery termQuery = new TermQuery(new Term("title", "beat"));
//		testSearch2(termQuery);
//		testDelete();
		// 段落查询:下面两种方式一样的效果
//		testSearch("content:\"just beat\"");其实是PhraseQuery
//		PhraseQuery.Builder builder = new PhraseQuery.Builder();
//		//有先后顺序，这两个构造出来的就是根据先后顺序：query带的字符串：title:"just beat"
//		builder.add(new Term("title", "just"));
//		builder.add(new Term("title", "beat"));
//		testSearch2(builder.build());
		//通配符查询：*其实类型是WildcardQuery，下面两种方式一样
//		testSearch("title:be*at");
//		testSearch2(new WildcardQuery(new Term("title","be*at")));
		// 模糊查询：~真正的类型是FuzzyQuery
		// 段落基础上用“~”
		testSearch("title:be~at");
		//与或非查询
		System.out.println("执行完毕!!!");
	}
}
