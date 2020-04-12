public class person {

    private int current_floor;
    private int destination_floor;
    private String status;
    private int weight;

    public person(int current_floor, int destination_floor, String status, int weight){
        this.current_floor = current_floor;
        this.destination_floor = destination_floor;
        this.status = status;
        this.weight = weight;
    }

    public void setCurrent_floor(int current_floor) {
        this.current_floor = current_floor;
    }
    public void setDestination_floor(int destination_floor) {
        this.destination_floor = destination_floor;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getCurrent_floor() {
        return current_floor;
    }
    public int getDestination_floor() {
        return destination_floor;
    }
    public String getStatus() {
        return status;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
}
