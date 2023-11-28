package com.ascklrt.infrastructure.framework.hadoop.base.analysis;

import lombok.Data;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 清洗后数据模型
 */
@Data
public class LiveCleanWritable implements Writable {

    private Long gold;
    private Long watchnumpv;
    private Long follower;
    private Long length;

    public LiveCleanWritable() {}

    public LiveCleanWritable(Long gold, Long watchnumpv, Long follower, Long length) {
        this.gold = gold;
        this.watchnumpv = watchnumpv;
        this.follower = follower;
        this.length = length;
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.gold = dataInput.readLong();
        this.watchnumpv = dataInput.readLong();
        this.follower = dataInput.readLong();
        this.length = dataInput.readLong();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(gold);
        dataOutput.writeLong(watchnumpv);
        dataOutput.writeLong(follower);
        dataOutput.writeLong(length);
    }


    @Override
    public String toString() {
        return new StringBuffer().append(gold)
                .append("\t").append(watchnumpv)
                .append("\t").append(follower)
                .append("\t").append(length)
                .toString();
    }
}
