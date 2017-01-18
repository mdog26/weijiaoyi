package springboot.module;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Transn on 2017-1-11.
 */
@Document(collection="cONC")
public class CONC {
    public CONC(){}

    public CONC(String name, double price, Date time, long second) {
        this.name = name;
        this.price = price;
        this.time = time;
        this.second = second;
    }

    @Id
    public String id;

    public String name;
    public double price;
    public Date time;
    @Indexed
    public long second;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CONC{" +
                "price=" + price +
                ", second=" + second +
                ", time=" + time +
                '}';
    }
}
