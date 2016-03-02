package main.java.component;

import org.springframework.stereotype.Component;

/**
 * Created by dell on 19-01-2016.
 */
@Component
public interface IJobService<T> {

    public void execute(T jobDataList);
}
