package com.lh.stock.stockcache.listener;

import com.lh.stock.stockcache.storm.bolt.hotprod.SplitHotProdBolt;
import com.lh.stock.stockcache.storm.spout.HotProductSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: ASUS
 * Date: 2020/6/30
 * Time: 22:07
 * Description: No Description
 */
@Component
public class CheckHotCacheListener{

    private Logger logger = LoggerFactory.getLogger(CheckHotCacheListener.class);

    @Autowired
    private HotProductSpout hotProductSpout;
    @Autowired
    private SplitHotProdBolt splitHotProdBolt;

    private LocalCluster cluster = new LocalCluster();


    @EventListener(classes={ApplicationStartedEvent.class})
    public void runHotCacheTopology() {
        logger.warn("begin to start topology to deal with hot product data...");
        TopologyBuilder hotProdCacheTopology = new TopologyBuilder();
        //set hot product cache data source
        hotProdCacheTopology.setSpout("hotProdCacheSpout", hotProductSpout, 1);
        //add bolt to record cache data
        hotProdCacheTopology.setBolt("hotProdCacheBolt", splitHotProdBolt, 1)
                .setNumTasks(2).fieldsGrouping("hotProdCacheSpout", new Fields("hotProdInfo"));

        Config config = new Config();

        // 说明是在eclipse里面本地运行
        config.setMaxTaskParallelism(20);

        cluster.submitTopology("WordCountTopology", config, hotProdCacheTopology.createTopology());
        logger.warn("hot product cache topology started...");
    }

    @EventListener(classes = {ContextClosedEvent.class})
    public void shutdownHotCacheTopology(){
        cluster.shutdown();
        logger.warn("hot product cache topology shutdown...");
    }

}
