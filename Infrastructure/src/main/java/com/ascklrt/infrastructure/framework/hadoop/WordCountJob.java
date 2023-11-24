package com.ascklrt.infrastructure.framework.hadoop;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 读取hdfs上的helo.txt，计算文件中每个单词出现的次数
 *
 * hello.txt 文件内容如下：
 * hello you
 * hello me
 *
 * 最终需要的结果形式如下：
 * hello 2
 * you 1
 * me 1
 */
public class WordCountJob {

    /**
     * 创建自定义map函数
     * 接收k1v1，产生k2v2
     */
    public static class WordMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context) throws IOException, InterruptedException {
            // k1 代表每一行的行首偏移量，v1代表每一行内容
            // 对获取到的每一行数据进行切割
            String[] words = value.toString().split(" ");

            // 迭代切割出来的单词数据
            for (String word: words) {
                Text k2 = new Text(word);
                LongWritable v2 = new LongWritable(1L);
                context.write(k2, v2);
            }
        }
    }


    /**
     * 创建自定义Reduce函数
     */
    public static class WordReduce extends Reducer<Text, LongWritable, Text, LongWritable> {
        /**
         * 针对map的输出进行累加求和
         * @param key
         * @param values                次数数据已经做过分组，因此是迭代器
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
            long sum = 0L;
            for (LongWritable value : values) {
                sum = sum + value.get();
            }

            Text k3 = key;
            LongWritable v3 = new LongWritable(sum);
            context.write(k3, v3);
        }
    }
}