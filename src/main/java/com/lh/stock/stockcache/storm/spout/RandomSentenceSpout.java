package com.lh.stock.stockcache.storm.spout;

import com.google.common.collect.Lists;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created with IntelliJ IDEA.
 * User: ASUS
 * Date: 2020/6/13
 * Time: 23:41
 * Description: No Description
 */
public class RandomSentenceSpout extends BaseRichSpout {

    private static final long serialVersionUID = 129623273233688951L;
    private SpoutOutputCollector spoutOutputCollector;

    private static final List<String> sentences = Lists.newArrayList("To develop topologies", "you'll need the Storm jars on your classpath.",
            "You should either include the unpacked jars in the classpath for your project or use Maven to include Storm as a development dependency",
            "Storm is hosted on Maven Central. To include Storm in your project as a development dependency");

    private ThreadLocalRandom localRandom;

    /**
     * 初始化一些成员变量，获得storm内部一些工具类，有点想aware接口
     * @param config
     * @param topologyContext
     * @param spoutOutputCollector
     */
    @Override
    public void open(Map config, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
        localRandom = ThreadLocalRandom.current();
    }

    /**
     * 模拟输入流
     * 这个spout类，之前说过，最终会运行在task中，某个worker进程的某个executor线程内部的某个task中
     * 那个task会负责去不断的无限循环调用nextTuple()方法
     * 只要的话呢，无限循环调用，可以不断发射最新的数据出去，形成一个数据流
     */
    @Override
    public void nextTuple() {
        String sentence = sentences.get(localRandom.nextInt(sentences.size()));
        spoutOutputCollector.emit(new Values(sentence));
    }

    /**
     * 很重要，这个方法是定义一个你发射出去的每个tuple中的每个field的名称是什么
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }
}
