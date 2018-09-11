package cn.wzgzs.springboot.task;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class Task {
	
	
	private final Logger log = LoggerFactory.getLogger(Task.class);

	@Scheduled(cron = "* 0/1 * * * *")  
	public void timer(){  
	    //获取当前时间  
	    LocalDateTime localDateTime =LocalDateTime.now();  
	    log.error("当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));  
	}  
	
	
}
