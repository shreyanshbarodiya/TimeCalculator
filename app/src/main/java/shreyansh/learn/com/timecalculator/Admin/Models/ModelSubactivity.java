package shreyansh.learn.com.timecalculator.Admin.Models;

public class ModelSubactivity {
    private String name;
    private int value; /* 0 --> checkbox disable, 1 --> checkbox enable */
    private float labour, time, length;
    private float c1, c2;
    private String modelActivity;
    private float userLabour, userLength;

    public float getUserLabour() {
        return userLabour;
    }

    public void setUserLabour(float userLabour) {
        this.userLabour = userLabour;
    }

    public float getUserLength() {
        return userLength;
    }

    public void setUserLength(float userLength) {
        this.userLength = userLength;
    }

    public float getLabour() {
        return labour;
    }

    public void setLabour(float labour) {
        this.labour = labour;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getC1() {
        return c1;
    }

    public void setC1(float c1) {
        this.c1 = c1;
    }

    public float getC2() {
        return c2;
    }

    public void setC2(float c2) {
        this.c2 = c2;
    }

    public String getModelActivity() {
        return modelActivity;
    }

    public void setModelActivity(String modelActivity) {
        this.modelActivity = modelActivity;
    }

    public ModelSubactivity(String name, int value, float labour, float time, float length, float c1, float c2, String modelActivity, float userLabour, float userLength) {
        this.name = name;
        this.labour = labour;
        this.time = time;
        this.length = length;
        this.c1 = c1;
        this.c2 = c2;
        this.modelActivity = modelActivity;
        this.userLabour = userLabour;
        this.userLength = userLength;
        this.value=value;
    }

    public ModelSubactivity(String name, int value, float labour, float length, float time, float c1, float c2, String modelActivity){
        this.name = name;
        this.value = value;
        this.labour = labour;

        this.length = length;
        this.time = time;
        this.c1 = c1;
        this.c2 = c2;
        this.modelActivity = modelActivity;
        this.userLabour =0.0f;
        this.userLength =0.0f;

    }

    public String getName(){
        return this.name;
    }
    public int getValue(){
        return this.value;
    }
}
