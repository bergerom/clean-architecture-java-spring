package details.repositories;

public enum FileDatabaseTable {

    USER_TABLE("users.csv"),
    SCORE_TABLE("score.csv"),
    SESSION_TABLE("session.csv");

    private final String value;

    FileDatabaseTable(String tableName) {
        value = tableName;
    }


    @Override
    public String toString() {
        return this.value;
    }
}
