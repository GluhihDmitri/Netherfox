package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.Phone;



@Controller
	public class MainController {
	    private static final Logger log = LoggerFactory.getLogger(MainController.class);

	    // Вводится (inject) из application.properties.
	    @Value("${welcome.message}")
	    private String message;
	    
	    
	    @Autowired
	    JdbcTemplate jdbcTemplate;

	    
	    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	    public String index(Model model) {
	    	List<Phone> lp = new ArrayList<>();
	    	jdbcTemplate.query(
	                "SELECT id, fio, age, home FROM phone",
	                (rs, rowNum) -> new Phone(rs.getLong("id"), rs.getString("fio"), rs.getInt("age"), rs.getString("home"))
	        ).forEach(phone -> lp.add(phone));
	    	
	        model.addAttribute("message", message);
	        model.addAttribute("phones", lp);
	 
	        return "index";
	    }

	    @RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	    public String createTable(){

	        log.info("Creating tables");

	        jdbcTemplate.execute("DROP TABLE phone IF EXISTS");
	        jdbcTemplate.execute("CREATE TABLE phone(" +
	                "id SERIAL, fio VARCHAR(255), num VARCHAR(255))");

	        
	        /*
	         * Arrays.asList - Создаем List из массива строк
	         * .stream() - Создаем из List поток (stream), можно сказать конвеер
	         * .map(inStr -> inStr.split(" ") - перобразуем каждую входную строку в массив строк, разделитель пробел
	         * .collect(Collectors.toList() - снова собираем все оъекты с конвеера в List и присваиваем переменной List<Object[]> splitUpNames
	         */
	        List<Object[]> splitUpNames = Arrays.asList("Романов 26 ул.Пушкина_дом_22_кв_30", "Иванов 22 ул.Советская_дом_45_кв_20", "Кожевников 35 4_мкр-н_дом_95_кв_9", "Попов 21 ул.Пушкина_дом_26_кв_31").stream()
	                .map(inStr -> inStr.split(" "))
	                .collect(Collectors.toList());

	        /*
	         * Распечатываем каждый объект из List splitUpNames
	         */
	        splitUpNames.forEach(inStr -> log.info(String.format("Добавлена запись: %s %s", inStr[0], inStr[1])));
	        
	        
	        /*
	         * Используем пакетное выполнение INSERT для каждого элемента LIst
	         */
	        jdbcTemplate.batchUpdate("INSERT INTO phone(fio, age, home) VALUES (?,?)", splitUpNames);
	        
	        /*
	         * Можно вставлять записи и так
	         */
	        jdbcTemplate.execute("INSERT INTO phone(fio, age, home) VALUES ('Романов1','26-1','ул.Пушкина_дом_22_кв_30-1')");
	        jdbcTemplate.execute("INSERT INTO phone(fio, age, home) VALUES ('Иванов1','22-1','ул.Советская_дом_45_кв_20-1')");

	        log.info("Запрос записи, where fio = 'Романов':");
			jdbcTemplate.query(
	                "SELECT id, fio, age, home FROM phone WHERE fio = ?", new Object[] { "Романов" },
	                (rs, rowNum) -> new Phone(rs.getLong("id"), rs.getString("fio"), rs.getInt("age"), rs.getString("home"))
	        ).forEach(phone -> log.info(phone.toString()));
	        
	        log.info("Запрос записи, where fio like 'Романов%':");
			jdbcTemplate.query(
	                "SELECT id, fio, age, home FROM phone WHERE fio like ?", new Object[] { "Романов%" },
	                (rs, rowNum) -> new Phone(rs.getLong("id"), rs.getString("fio"), rs.getInt("age"), rs.getString("home"))
	        ).forEach(phone -> log.info(phone.toString()));
			
	        return "create";
	    }


}
