package cn.spark.study.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.DataTypes;

/**
 * Create by NING on 2018/6/19.<br>
 */
public class UDF {
    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setAppName("UDF").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        SQLContext sqlContext = new SQLContext(sc);

        sqlContext.udf().register("udf1", new UDF1<String, Integer>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Integer call(String s) throws Exception {
                return s.length();
            }
        }, DataTypes.IntegerType);
        sqlContext.sql("select name,StrLen(name) as length from user").show();



        sqlContext.udf().register("udf2", new UDF2<String, Integer, Integer>() {
            @Override
            public Integer call(String t1, Integer t2) throws Exception {
                return t1.length() + t2;
            }
        }, DataTypes.IntegerType);
        sqlContext.sql("select name,StrLen(name) as length from user").show();
    }

}
