package com.lh.stock.stockcache.storm;

import com.lh.stock.stockcache.storm.bolt.CountBolt;
import com.lh.stock.stockcache.storm.bolt.SplitBolt;
import com.lh.stock.stockcache.storm.spout.RandomSentenceSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: liuhai
 * Date: 2020/6/14
 * Time: 0:02
 * Description: No Description
 */
public class WordCountTopology {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {

        // 在main方法中，会去将spout和bolts组合起来，构建成一个拓扑
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        // 这里的第一个参数的意思，就是给这个spout设置一个名字
        // 第二个参数的意思，就是创建一个spout的对象
        // 第三个参数的意思，就是设置spout的executor有几个
        topologyBuilder.setSpout("RandomSentence", new RandomSentenceSpout(), 2);
        topologyBuilder.setBolt("SplitBout",new SplitBolt(), 5)
                        .setNumTasks(10).shuffleGrouping("RandomSentence");
        // 这个很重要，就是说，相同的单词，从SplitSentence发射出来时，一定会进入到下游的指定的同一个task中
        // 只有这样子，才能准确的统计出每个单词的数量
        // 比如你有个单词，hello，下游task1接收到3个hello，task2接收到2个hello
        // 5个hello，全都进入一个task
        topologyBuilder.setBolt("CountBout", new CountBolt(), 10)
                .setNumTasks(20).fieldsGrouping("SplitBout", new Fields("word"));

        Config config = new Config();

        // 说明是在命令行执行，打算提交到storm集群上去
        if(null != args && args.length > 0){
            config.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
        }else {
            // 说明是在eclipse里面本地运行
            config.setMaxTaskParallelism(20);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("WordCountTopology", config, topologyBuilder.createTopology());

            Utils.sleep(60000);
            cluster.shutdown();
        }
    }
}
