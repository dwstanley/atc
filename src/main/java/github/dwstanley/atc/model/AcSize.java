package github.dwstanley.atc.model;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum AcSize {

    SMALL("S", 3),
    MEDIUM("M", 2),
    LARGE("L", 1);

    public static AcSize ofCode(String code) {
        return CODE_MAP.get(code);
    }

    private static final Map<String, AcSize> CODE_MAP = Stream.of(values())
            .collect(toMap(AcSize::getCode, seatStatus -> seatStatus));

    private final String code;

    private final int priority;

    AcSize(String code, int priority) {
        this.code = code;
        this.priority = priority;
    }

    public String getCode() {
        return code;
    }

    public int getPriority() {
        return priority;
    }

}