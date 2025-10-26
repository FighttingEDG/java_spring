package fun.jevon.springboot;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory factory;
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
