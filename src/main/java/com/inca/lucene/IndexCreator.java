package com.inca.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexCreator {

	public static void indexCreate() throws IOException {
		Directory directory = FSDirectory.open(Paths.get(Constants.INDEX_PATH));
		IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
		IndexWriter writer = new IndexWriter(directory, config);
		writer.addDocument(getDocument(new DemoBean("1", "歌唱祖国", "关关雎鸠,在河之洲窈窕淑女,滚一边去", "屈原")));
		writer.addDocument(getDocument(new DemoBean("2", "Just Beat", "Beat Beat Beat", "Tom Jack")));
		writer.commit();
		writer.close();
	}

	public static Document getDocument(DemoBean bean) {
		// Document可以理解为表中一行记录,但是动态列
		// 除了添加字段,还能删除,查询列值
		Document doc = new Document();
		doc.add(new TextField("id", bean.getId(), Store.YES));
		doc.add(new TextField("title", bean.getTitle(), Store.YES));
		doc.add(new TextField("content", bean.getContent(), Store.YES));
		doc.add(new TextField("author", bean.getAuthor(), Store.YES));
		return doc;
	}

}
