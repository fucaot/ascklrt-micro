package com.ascklrt.infrastructure.framework.bigdata.hadoop.base.dc;

import cn.hutool.json.JSONUtil;
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
 *
 *
 * file: 20231128
 * {"id":"158008900435","uid":"120010010445","nickname":"jack435","qold":445,"watchnumpv":4350,"watchnumuv":870,"hots":1305,"nofollower": 435,"looktime":8700,"smlook":2175,"follower":1740,"gifter":870,"length":2610,"area":"A_US","rating":"B","exp":1305,"type":"video_rating"}
 * {"id":"158008900773","uid":"120010010782","nickname":"jack772","gold": 782,"watchnumpv": 7720,"watchnumuv":1544,"hots":2316,"nofallower":772,"looktime":15440,"smlook":420,"follower":3088,"gifter":1544,"length":4632,"area":"A_US","rating":"B","exp":2316,"type":"video_rating"}
 * {"id":"158008900774","uid":"120010010783","nickname":"nike001","gold": 982,"watchnumpv": 9000,"watchnumuv": 7200,"hots":4569,"nofallower":32,"looktime":32000,"smlook":5890,"follower":6000,"gifter":3544,"length":980,"area":"A_US","rating":"A","exp":12316,"type":"video_rating"}
 * {"id":"158008900775","uid":"120010010783","nickname":"nike001","gold": 482,"watchnumpv": 5000,"watchnumuv": 4200,"hots":3988,"nofallower":12,"looktime":16000,"smlook":3160,"follower":1452,"gifter":1544,"length":980,"area":"A_US","rating":"A","exp":12316,"type":"video_rating"}
 * {"id":"158008900776","uid":"120010010784","nickname":"jack774","gold": 282,"watchnumpv": 4020,"watchnumuv": 2020,"hots":1299,"nofallower":32,"looktime":17828,"smlook":2059,"follower":120,"gifter":892,"length":980,"area":"A_US","rating":"B","exp":12316,"type":"video_rating"}
 * {"id":"158008900777","uid":"120010010785","nickname":"jack775","gold": 182,"watchnumpv": 3092,"watchnumuv": 1456,"hots":988,"nofallower":8,"looktime":12032,"smlook":1600,"follower":78,"gifter":546,"length":980,"area":"A_US","rating":"B","exp":12316,"type":"video_rating"}
 * {"id":"158008900778","uid":"120010010786","nickname":"jack776","gold": 101,"watchnumpv": 2092,"watchnumuv": 1001,"hots":322,"nofallower":52,"looktime":13452,"smlook":892,"follower":102,"gifter":156,"length":980,"area":"A_US","rating":"B","exp":12316,"type":"video_rating"}
 * {"id":"158008900779","uid":"120010010787","nickname":"jack777","gold": 199,"watchnumpv": 5600,"watchnumuv": 3400,"hots":1002,"nofallower":72,"looktime":85731,"smlook":3120,"follower":230,"gifter":354,"length":980,"area":"A_US","rating":"B","exp":12316,"type":"video_rating"}
 * {"id":"158008900780","uid":"120010010788","nickname":"jack778","gold": 233,"watchnumpv": 4389,"watchnumuv": 3200,"hots":2000,"nofallower":73,"looktime":13929,"smlook":3400,"follower":150,"gifter":144,"length":980,"area":"A_US","rating":"B","exp":12316,"type":"video_rating"}
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
