package github.dwstanley.atc.service;

import github.dwstanley.atc.model.Aircraft;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AircraftQueue {

    private final int TIMEOUT_IN_SECS = 5;

    // TODO - verify there is no limit to the size of this data structure
    private final PriorityBlockingQueue<FifoAircraft> queue = new PriorityBlockingQueue<>(100, FifoAircraft::compareTo);

    public void add(Aircraft aircraft) {
        queue.add(new FifoAircraft(aircraft));
    }

    public List<Aircraft> pending() {
        return queue.stream()
                .map(FifoAircraft::getAircraft)
                .collect(Collectors.toList());
    }

    public Optional<Aircraft> peek() {
        return ofNullable(queue.peek());
    }

    public Optional<Aircraft> poll() {
        FifoAircraft fifoAircraft = null;
        try {
            fifoAircraft = queue.poll(TIMEOUT_IN_SECS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ofNullable(fifoAircraft);
    }

    public int size() {
        return queue.size();
    }

    private Optional<Aircraft> ofNullable(FifoAircraft fifoAircraft) {
        return (null == fifoAircraft) ? Optional.empty() : Optional.of(fifoAircraft.aircraft);
    }

    @Getter
    private static class FifoAircraft implements Comparable<FifoAircraft> {

        private final Aircraft aircraft;
        private final Long timestamp;

        FifoAircraft(Aircraft aircraft) {
            this.aircraft = Objects.requireNonNull(aircraft);
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public int compareTo(FifoAircraft other) {

            int result = Integer.compare(typePriority(this.aircraft), typePriority(other.aircraft));

            if (0 == result) {
                result = Integer.compare(sizePriority(this.aircraft), sizePriority(other.aircraft));
            }

            if (0 == result) {
                result = Long.compare(this.timestamp, other.timestamp);
            }

            return result;
        }

        private int typePriority(Aircraft aircraft) {
            return aircraft.getType().getPriority();
        }

        private int sizePriority(Aircraft aircraft) {
            return aircraft.getSize().getPriority();
        }
    }
}
