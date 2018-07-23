package github.dwstanley.atc.service;

import github.dwstanley.atc.model.Aircraft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class AircraftQueue<T> implements Comparator<T> {

    private final int TIMEOUT_IN_SECS = 5;

    // TODO - verify there is no limit to the size of this data structure
    private final PriorityBlockingQueue<T> queue = new PriorityBlockingQueue<>(100, this);

    private final Function<T, Long> getTimestamp;
    private final Function<T, Aircraft> getAircraft;

    public AircraftQueue(Function<T, Long> getTimestamp, Function<T, Aircraft> getAircraft) {
        this.getTimestamp = getTimestamp;
        this.getAircraft = getAircraft;
    }

    @Override
    public int compare(T o1, T o2) {
        int result = Integer.compare(typePriority(o1), typePriority(o2));

        if (0 == result) {
            result = Integer.compare(sizePriority(o1), sizePriority(o2));
        }

        if (0 == result) {
            result = Long.compare(getTimestamp.apply(o1), getTimestamp.apply(o2));
        }

        return result;
    }

    public void add(T data) {
        queue.add(data);
    }

    public List<T> pending() {
        return new ArrayList<>(queue);
    }

    public Optional<T> peek() {
        return Optional.ofNullable(queue.peek());
    }

    public Optional<T> poll() {
        T data = null;
        try {
            data = queue.poll(TIMEOUT_IN_SECS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(data);
    }

    public int size() {
        return queue.size();
    }

    private int typePriority(T data) {
        return getAircraft.apply(data).getType().getPriority();
    }

    private int sizePriority(T data) {
        return getAircraft.apply(data).getSize().getPriority();
    }

}
