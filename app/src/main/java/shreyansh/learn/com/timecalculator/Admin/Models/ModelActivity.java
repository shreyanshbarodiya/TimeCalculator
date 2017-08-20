package shreyansh.learn.com.timecalculator.Admin.Models;

public class ModelActivity {
    String name;
    float totalTime;

    public ModelActivity(String name, float totalTime){
        this.name = name;
        this.totalTime = totalTime;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }
}
