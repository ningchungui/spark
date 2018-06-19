package cn.spark.study.sql;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

/**
 * Create by NING on 2018/6/19.<br>
 */
public class HBaseDataSource {
    public static void main(String[] args) throws Exception {
        SparkConf sparkConf = new SparkConf().setAppName("HBaseDataSource").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        String tablename = "account";
        Configuration conf = HBaseConfiguration.create();
        //设置zooKeeper集群地址，也可以通过将hbase-site.xml导入classpath，但是建议在程序里这样设置
        conf.set("hbase.zookeeper.quorum","slave1,slave2,slave3");
        //设置zookeeper连接端口，默认2181
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set(TableInputFormat.INPUT_TABLE, tablename);

        // 如果表不存在则创建表
        HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
        if(!hBaseAdmin.isTableAvailable(tablename)){
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tablename));
            hBaseAdmin.createTable(hTableDescriptor);
        }

        //读取数据并转化成rdd
        JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = sc.newAPIHadoopRDD(conf, TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

        long count = hBaseRDD.count();
        System.out.println(count);
        hBaseRDD.foreach(new VoidFunction<Tuple2<ImmutableBytesWritable, Result>>() {
            @Override
            public void call(Tuple2<ImmutableBytesWritable, Result> result) throws Exception {
            }
        });

        sc.close();
        hBaseAdmin.close();


    }


}
