package github.dwstanley.atc.model;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum AcType {

    EMERGENCY("E", 1),
    VIP("V", 2),
    PASSENGER("P", 3),
    CARGO("C", 4);

    public static AcType ofCode(String code) {
        return CODE_MAP.get(code);
    }

    private static final Map<String, AcType> CODE_MAP = Stream.of(values())
            .collect(toMap(AcType::getCode, seatStatus -> seatStatus));

    private final String code;
    private final int priority;

    AcType(String code, int priority) {
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