//package spark
//
//import org.apache.spark.{SparkConf, SparkContext}
//
//object SparkWordCount {
//  def main(args: Array[String]): Unit = {
//    // 创建spark上下文
//    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("local");
//    val sc = new SparkContext(conf);
//
//    val linesRDD = sc.textFile("/Users/wangjiawei/Dev/env/spark/hello.txt");
//    val wordsRDD = linesRDD.flatMap(_.split(" "));
//
//    // 转换为truple
//    val tupleRDD = wordsRDD.map((_, 1))
//
//    // 分组为tuple
//    val wordCountRDD = tupleRDD.reduceByKey(_ + _);
//
//    // 打印
//    wordCountRDD.foreach(wordcount => print(wordcount._1 + ": " + wordcount._2));
//
//
//    sc.stop();
//  }
//}
