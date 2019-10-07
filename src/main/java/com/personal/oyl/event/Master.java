package com.personal.oyl.event;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.oyl.event.util.Configuration;
import com.personal.oyl.event.util.SimpleLock;
import com.personal.oyl.event.util.ZkUtil;

public class Master {
    
    private static final Logger log = LoggerFactory.getLogger(Master.class);
    
    private ZooKeeper zk;

    private Watcher masterWatcher = (event) -> {
        if (event.getType().equals(EventType.NodeChildrenChanged)) {
            try {
                Master.this.onWorkerChange();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    };
    
    public void start() throws IOException, InterruptedException, KeeperException {
        String serverId = Configuration.instance().uuid();
        
        CountDownLatch latch = new CountDownLatch(1);
        
        zk = new ZooKeeper(Configuration.instance().getZkAddrs(), Configuration.instance().getSessionTimeout(),
                (event) -> {
                    if (event.getState().equals(KeeperState.Expired)) {
                        try {
                            Master.this.close();
                            Master.this.start();
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }

                    }

                    if (event.getState().equals(KeeperState.SyncConnected)) {
                        latch.countDown();
                    }
                });
        
        latch.await();
        
        ZkUtil.getInstance().createRoot(zk, Configuration.instance().getNameSpace());
        
        SimpleLock lock = new SimpleLock(zk);
        lock.lock(serverId, Configuration.instance().getMasterNode());
        log.info("Now it is the master server...");
        // do what it should do as a master...
        
        ZkUtil.getInstance().getChildren(zk, Configuration.instance().getWorkerNode(), masterWatcher);
        log.info("ready for listening to workers...");
        
        log.info("perform the first check of the assignment, invoke method onWorkerChange()...");
        this.onWorkerChange();
    }
    
    public void close() {
        if (null != zk) {
            try {
                zk.close();
                zk = null;
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
    
    private synchronized void onWorkerChange() throws KeeperException, InterruptedException {
        List<String> workerList = ZkUtil.getInstance().getChildren(zk, Configuration.instance().getWorkerNode(), masterWatcher);
        List<Holder> holders = new LinkedList<>();
        Set<Integer> assigned = new HashSet<>();
        for (String worker : workerList) {
            Holder holder = new Holder();
            String content = ZkUtil.getInstance().getContent(zk, Configuration.instance().getWorkerNode() + Configuration.SEPARATOR + worker, null);
            holder.node = worker;
            holder.setAssigned(content);
            holders.add(holder);
            assigned.addAll(holder.assigned);
        }
        
        Configuration.instance().getTables().forEach((t) -> {
            if (!assigned.contains(t)) {
                holders.sort(Comparator.comparing(Holder::payload));
                holders.get(0).addAssigned(t);
            }
        });
        
        int lastIdx = holders.size() - 1;
        while (true) {
            holders.sort(Comparator.comparing(Holder::payload));
            
            if (holders.get(lastIdx).assigned.size() - holders.get(0).assigned.size() >= 2) {
                Integer tmp = holders.get(lastIdx).removeFirstAssigned();
                holders.get(0).addAssigned(tmp);
                
                continue;
            }
            
            break;
        }
        
        for (Holder holder : holders) {
            if (holder.affected) {
                try{
                    ZkUtil.getInstance().setContent(zk,
                            Configuration.instance().getWorkerNode() + Configuration.SEPARATOR + holder.node,
                            holder.assignedString());
                } catch(KeeperException e){
                    if (e instanceof KeeperException.NoNodeException) {
                        // 可能发生NONODE异常，这不是问题。
                        // NONODE意味着某个Worker下线了，Master会收到通知，并重新进行分配。
                        return;
                    }
                    throw e;
                }
            }
        }
    }
    
    private static class Holder {
        private String node;
        private List<Integer> assigned = new LinkedList<>();
        private boolean affected = false;

        public void addAssigned(Integer i) {
            this.assigned.add(i);
            affected = true;
        }

        public int payload() {
            return this.assigned.size();
        }

        public Integer removeFirstAssigned() {
            affected = true;
            return this.assigned.remove(0);
        }

        public void setAssigned(String nodeContent) {
            if (null == nodeContent || nodeContent.trim().isEmpty()) {
                return;
            }
            
            String[] parts = nodeContent.trim().split(Configuration.GROUP_SEPARATOR);
            for (String part : parts) {
                assigned.add(Integer.valueOf(part.trim()));
            }
        }
        
        public String assignedString() {
            StringBuilder sb = new StringBuilder();
            int size = assigned.size();
            
            for (int i = 0; i < size; i++) {
                sb.append(this.assigned.get(i));
                if (i < (size - 1)) {
                    sb.append(Configuration.GROUP_SEPARATOR);
                }
            }
            return sb.toString();
        }
    }
}
