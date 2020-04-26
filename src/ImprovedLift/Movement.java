package ImprovedLift;

public enum Movement {

    UP(1),
    DOWN(-1),
    IDLE(0);

    private final int listOperation;

    Movement(int listOperation) {
        this.listOperation = listOperation;
    }

    public int getListOperation() {
        return listOperation;
    }
}