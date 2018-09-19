package com.inca.lucene;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class AnalyzerTestor {

	private static String str = "我的Hello kitty,wo de x i z ,明天G ood morging";

	public static void testAnalyzer(Analyzer analyzer, String str) throws IOException {
		TokenStream stream = analyzer.tokenStream("content", new StringReader(str));
		// 读取源流后,重加载一次
		stream.reset();
		while (stream.incrementToken()) {
			System.out.println(stream);
		}
	}

	public static void main(String[] args) {
		try {
			testAnalyzer(new IKAnalyzer(), str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
