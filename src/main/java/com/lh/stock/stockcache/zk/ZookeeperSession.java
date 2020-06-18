package com.lh.stock.stockcache.zk;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: liuhai
 * @Date: 2020/6/18 10:09
 */
@Component
public class ZookeeperSession implements ApplicationContextAware, InitializingBean,Watcher {

    private ApplicationContext applicationContext;

    private String DEFAULT_LOCK_VALUE = "1";

    private static Logger logger = LoggerFactory.getLogger(ZookeeperSession.class);

    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

    public void init(){
        try {
            zooKeeper = new ZooKeeper(applicationContext.getEnvironment().getProperty("zookeeper.hosts"),
                    5000, this);

            if(null != zooKeeper.exists("/stock_cache", true)){
                zooKeeper.delete("/stock_cache", -1);
            }
            zooKeeper.create("/stock_cache", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            COUNT_DOWN_LATCH.await();
            logger.warn("ZooKeeper session established......");
        } catch (IOException | InterruptedException | KeeperException e) {
            logger.error("create zookeeper client error: ", e);
        }
    }

    public void acquireDistributeLock(String resource) throws KeeperException, InterruptedException {
        if(StringUtils.isBlank(resource)){
            throw new IllegalArgumentException("lock resource can not be null or blank");
        }
        try {
            zooKeeper.create(resource, DEFAULT_LOCK_VALUE.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.warn("success to acquire lock for resource=[" + resource + "]");
        } catch (KeeperException | InterruptedException e ) {
            logger.error("create lock node error when acquire distribute lock: ");
            // 如果那个商品对应的锁的node，已经存在了，就是已经被别人加锁了，那么就这里就会报错
            // NodeExistsException
            int count = 0;
            while(true) {
                try {
                    Thread.sleep(500);
                    zooKeeper.create(resource, DEFAULT_LOCK_VALUE.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (Exception e2) {
                    logger.error("can not get distribute lock for resource[" + resource + "]");
                    if(count >=10){
                        throw e;
                    }
                    count++;
                    continue;
                }
                logger.warn("success to acquire lock for resource=" + resource + " after " + count + " times try......");
                break;
            }
        }
    }

    public void releaseDistributeLock(String resource){
        try {
            zooKeeper.delete(resource, -1);
            logger.error("release lock for resource=[" + resource + "]");
        } catch (InterruptedException | KeeperException e) {
            logger.error("release lock for resource=" + resource + " error:", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    @Override
    public void process(WatchedEvent event) {
        try {

            if(Event.KeeperState.SyncConnected == event.getState()) {

                if(Event.EventType.None == event.getType() && null == event.getPath()) {
                    COUNT_DOWN_LATCH.countDown();
                } else if (Event.EventType.NodeCreated == event.getType()) {
                    logger.warn("Node(" + event.getPath() + ")Created");
                    zooKeeper.exists(event.getPath(), true);
                } else if (Event.EventType.NodeDeleted == event.getType()) {
                    logger.warn("Node(" + event.getPath() + ")Deleted");
                    zooKeeper.exists(event.getPath(), true);
                } else if (Event.EventType.NodeDataChanged == event.getType()) {
                    logger.warn("Node(" + event.getPath() + ")DataChanged");
                    zooKeeper.exists(event.getPath(), true);
                }
            }

        }  catch(Exception e) {}
    }



}
