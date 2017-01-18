package springboot.module;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Transn on 2017-1-11.
 */
@Document(collection="pro")
public class Pro extends CONC{
    public int group; //连续时间段

    public String date;
    public String level;//这一秒中的高 低
    public Win win1;//
    public Win win3;//
    public Win win5;//
    public Win win15;//
    public Win win30;//
    public Win win60;//

    public Pro() {
    }

    public Pro(CONC conc) {
        this.second = conc.getSecond();
        this.name = conc.getName();
        this.price = conc.getPrice();
        this.time = conc.getTime();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.time = null;
        this.date = date;
    }

    public Win getWin1() {
        return win1;
    }

    public void setWin1(Win win1) {
        this.win1 = win1;
    }

    public Win getWin3() {
        return win3;
    }

    public void setWin3(Win win3) {
        this.win3 = win3;
    }

    public Win getWin5() {
        return win5;
    }

    public void setWin5(Win win5) {
        this.win5 = win5;
    }

    public Win getWin15() {
        return win15;
    }

    public void setWin15(Win win15) {
        this.win15 = win15;
    }

    public Win getWin30() {
        return win30;
    }

    public void setWin30(Win win30) {
        this.win30 = win30;
    }

    public Win getWin60() {
        return win60;
    }

    public void setWin60(Win win60) {
        this.win60 = win60;
    }
}
