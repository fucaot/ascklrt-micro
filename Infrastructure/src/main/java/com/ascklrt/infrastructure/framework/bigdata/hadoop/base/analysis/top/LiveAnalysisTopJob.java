package com.ascklrt.infrastructure.framework.bigdata.hadoop.base.analysis.top;

import com.ascklrt.infrastructure.framework.bigdata.hadoop.base.analysis.LiveCleanWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;

/**
 * 统计直播时长前十的主播
 */
public class LiveAnalysisTopJob {

    public static class LiveTopMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context) throws IOException, InterruptedException {
            String[] vs = value.toString().split("\t");
            context.write(new Text(vs[0]), new LongWritable(Long.parseLong(vs[4])));
        }
    }

    public static class LiveTopReduce extends Reducer<Text, LongWritable, Text, LiveCleanWritable> {
        HashMap<String, Long> map = new HashMap<>();

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LiveCleanWritable>.Context context) throws IOException, InterruptedException {
            // todo 排序逻辑
        }

        /**
         * 初始化时执行
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void setup(Reducer<Text, LongWritable, Text, LiveCleanWritable>.Context context) throws IOException, InterruptedException {
            super.setup(context);
        }

        /**
         * 清理初始化资源
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void cleanup(Reducer<Text, LongWritable, Text, LiveCleanWritable>.Context context) throws IOException, InterruptedException {
            super.cleanup(context);
        }
    }
}

