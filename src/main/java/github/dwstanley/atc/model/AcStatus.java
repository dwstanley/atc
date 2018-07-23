package github.dwstanley.atc.model;

public enum AcStatus {

    UNKNOWN("Unknown"),
    LANDED("Landed"),
    DEPARTING("Departing"),
    ARRIVING("Arriving");

    private final String description;

    AcStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}