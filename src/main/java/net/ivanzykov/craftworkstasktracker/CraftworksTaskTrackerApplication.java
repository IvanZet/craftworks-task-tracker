package net.ivanzykov.craftworkstasktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {TaskSchedulingAutoConfiguration.class})
@EnableScheduling
public class CraftworksTaskTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CraftworksTaskTrackerApplication.class, args);
	}

}
