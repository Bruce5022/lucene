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
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
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

	public static void testSearch() throws IOException, ParseException {
		Directory directory = FSDirectory.open(Paths.get(Constants.INDEX_PATH));
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		String keyword = "title:Beat";
		String defaultField = "content";
		Analyzer analyzer = new IKAnalyzer();
		QueryParser parser = new QueryParser(defaultField, analyzer);
		Query query = parser.parse(keyword);
		TopDocs hits = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
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
		long result = writer.deleteDocuments(new Term("content","beat"));
		System.out.println("结果:"+result);
		writer.commit();
		writer.close();
	}

	public static void main(String[] args) throws IOException, ParseException {
		testSearch();
//		testDelete();
		System.out.println("执行完毕!!!");
	}
}
