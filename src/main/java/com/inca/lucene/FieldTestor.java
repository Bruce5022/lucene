package com.inca.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class FieldTestor {

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
		Document doc = new Document();
		doc.add(new TextField("content", bean.getContent(), Store.YES));
		FieldType type = new FieldType();
		type.setStored(true);// 在数据库是否存储
		// 是否创建索引
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		// 是否分词
		type.setTokenized(true);
		// 看图解IndexableField
		// 比较好的创建字段的方式:不需要直接创建Field并为他设置三个属性,因为每个子类已经设置好了对应的属性
		// 我们只需要了解常用子类,和三个属性的值
		// LongField
		// StringField:不支持分词
		// TextField:支持分词
		doc.add(new Field("title", bean.getTitle(), type));
		return doc;
	}

}
