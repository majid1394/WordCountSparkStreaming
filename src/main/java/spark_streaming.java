
import org.apache.spark.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import java.util.Arrays;


public class spark_streaming {
    public static void main(String[] args)  {
        // Create a local StreamingContext with two working thread and batch interval of 1 second
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));

        /*Spark Streaming provides a high-level abstraction called discretized stream or DStream*/
        // Create a DStream that will connect to hostname:port, like localhost:9999
            /*JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);*/
            JavaReceiverInputDStream<String> lines = jssc.socketTextStream("192.168.79.134", 9999);
//            JavaReceiverInputDStream<String> lines = jssc.socketTextStream("192.168.79.128", 9999);


// Split each line into words
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
        // Count each word in each batch
        JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);

// Print the first ten elements of each RDD generated in this DStream to the console
        wordCounts.print();

        jssc.start();              // Start the computation
        try {
            jssc.awaitTermination();   // Wait for the computation to terminate
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }


}
