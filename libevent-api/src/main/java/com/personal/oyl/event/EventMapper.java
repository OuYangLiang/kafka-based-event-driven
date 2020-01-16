package com.personal.oyl.event;

import java.util.List;

public interface EventMapper {
    void insert(int tbNum, Event event);
    
    List<Event> queryTopN(int tbNum, int limit);
    
    void batchClean(int tbNum, List<String> eventIds);
    
    void archive(String subscriberId, Event event);
}
