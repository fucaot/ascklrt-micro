package com.ascklrt.infrastructure.framework.hadoop.dc;

import cn.hutool.json.JSONUtil;
import com.ascklrt.infrastructure.framework.hadoop.WordCountJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Objects;

/**
 * 需求：
 * 1. 从原始数据（JSON）中过滤出需要的字段
 * - 主播id uid
 * - 金币数量 gold
 * - 总观看pv watchnumpv
 * - 粉丝关注数量 follower
 * - 视频总开播时常 length
 * 2. 针对核心字段进行校验
 */
public class LiveDataCleanJob {

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        // 创建Job
        Job job = Job.getInstance(config);
        // 因为是节点计算，所以需要指定
        job.setJarByClass(LiveDataCleanJob.class);

        // 指定输入路径
        FileInputFormat.setInputPaths(job, new Path("hdfs://localhost:8020/data/videoinfo/20231128"));
        // 指定输出路径
        FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:8020/clean"));

        // 指定mapReduce计算资源
        job.setMapperClass(LiveDataCleanJob.CleanMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // 禁用reduce
        job.setNumReduceTasks(0);

        // 提交job
        job.waitForCompletion(true);
    }

    public static class CleanMapper extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            LiveData liveData = JSONUtil.toBean(value.toString(), LiveData.class);
            if (Objects.isNull(liveData.getUid()) || Objects.isNull(liveData.getGold()) || Objects.isNull(liveData.getWatchnumpv()) || Objects.isNull(liveData.getFollower()) || Objects.isNull(liveData.getLength())) {
                return;
            }

            if (liveData.getGold() < 0 || liveData.getWatchnumpv() < 0 || liveData.getFollower() < 0 || liveData.getLength() < 0) {
                return;
            }

            context.write(
                    new Text(liveData.getUid()),
                    new Text(
                            new StringBuffer().append(liveData.getGold())
                                    .append("\t").append(liveData.getWatchnumpv())
                                    .append("\t").append(liveData.getFollower())
                                    .append("\t").append(liveData.getLength())
                                    .toString()
                    )
            );
        }
    }
}
