package io.sponges.bot.server.event.framework;

import io.sponges.bot.api.event.framework.Event;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.util.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventManagerImpl implements EventManager {

    // TODO add event priority

    private final Map<Module, List<Consumer>> consumers = new ConcurrentHashMap<>();

    private final EventBus eventBus;

    public EventManagerImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public <T extends Event> boolean register(Module module, Class<T> aClass, Consumer<T> consumer) {
        if (module != null) {
            List<Consumer> consumers = new ArrayList<>();
            if (this.consumers.containsKey(module)) {
                consumers = this.consumers.get(module);
            }
            consumers.add(consumer);
            this.consumers.put(module, consumers);
        }
        return eventBus.register(aClass, consumer);
    }

    @Override
    public <T extends Event> boolean unregister(Consumer<T> consumer) {
        return eventBus.unregister(consumer);
    }

    @Override
    public void unregister(Module module) {
        if (!consumers.containsKey(module)) return;
        List<Consumer> consumers = this.consumers.get(module);
        consumers.forEach(this::unregister);
        this.consumers.remove(module);
    }

    @Override
    public <T extends Event> T post(T t) {
        return eventBus.post(t);
    }

    @Override
    public <T extends Event> void postAsync(T t) {
        Scheduler.runAsyncTask(() -> {
            if (t.isCancellable()) {
                long slot = t.getTimeSlot();
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - slot < start) {
                    if (t.isCancelled()) {
                        return;
                    } else {
                        try {
                            Thread.sleep(t.getCheckInterval());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }
            eventBus.post(t);
        });
    }
}
