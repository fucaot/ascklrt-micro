package com.ascklrt.infrastructure.framework.bigdata.hadoop.base.analysis.anchor;

import com.ascklrt.infrastructure.framework.bigdata.hadoop.base.analysis.LiveCleanWritable;
import com.ascklrt.infrastructure.framework.bigdata.hadoop.base.dc.LiveDataCleanJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Objects;

/**
 * 需求：对主播直播数据进行分析，总观看pv，总粉丝关注量，总视频开播时长
 */
public class LiveAnalysisJob {

    public static class LiveAnalysisMapper extends Mapper<LongWritable, Text, Text, LiveCleanWritable> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LiveCleanWritable>.Context context) throws IOException, InterruptedException {
            String[] vs = value.toString().split("\t");
            LiveCleanWritable liveCleanWritable = new LiveCleanWritable(Long.valueOf(vs[1]), Long.valueOf(vs[2]), Long.valueOf(vs[3]), Long.valueOf(vs[4]));
            context.write(new Text(vs[0]), liveCleanWritable);
        }
    }

    public static class LiveAnalysisReduce extends Reducer<Text, LiveCleanWritable, Text, LiveCleanWritable> {
        @Override
        protected void reduce(Text key, Iterable<LiveCleanWritable> values, Reducer<Text, LiveCleanWritable, Text, LiveCleanWritable>.Context context) throws IOException, InterruptedException {
            LiveCleanWritable liveCleanWritable = new LiveCleanWritable();
            for (LiveCleanWritable value : values) {
                liveCleanWritable.setGold(Objects.isNull(liveCleanWritable.getGold()) ? value.getGold() : liveCleanWritable.getGold() + value.getGold());
                liveCleanWritable.setWatchnumpv(Objects.isNull(liveCleanWritable.getWatchnumpv()) ? value.getWatchnumpv() : liveCleanWritable.getWatchnumpv() + value.getWatchnumpv());
                liveCleanWritable.setFollower(Objects.isNull(liveCleanWritable.getFollower()) ? value.getFollower() : liveCleanWritable.getFollower() + value.getFollower());
                liveCleanWritable.setLength(Objects.isNull(liveCleanWritable.getLength()) ? value.getLength() : liveCleanWritable.getLength() + value.getLength());
            }
            context.write(key, liveCleanWritable);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        // 创建Job
        Job job = Job.getInstance(config);
        // 因为是节点计算，所以需要指定
        job.setJarByClass(LiveDataCleanJob.class);

        // 指定输入路径
        FileInputFormat.setInputPaths(job, new Path("hdfs://localhost:8020/clean/part-m-00000"));
        // 指定输出路径
        FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:8020/analysis/anchor"));

        // 指定mapReduce计算资源
        job.setMapperClass(LiveAnalysisMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LiveCleanWritable.class);

        job.setReducerClass(LiveAnalysisReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LiveCleanWritable.class);

        // 提交job
        job.waitForCompletion(true);
    }
}
