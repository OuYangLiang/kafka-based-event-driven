package com.personal.oyl.event;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EventPusher {
    List<String> push(int tbNum, List<Event> events) throws ExecutionException, InterruptedException;
}