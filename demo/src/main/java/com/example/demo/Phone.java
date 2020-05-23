package com.example.demo;

public class Phone {
	private long id;
	private String fio;
	private int age;
	private String home;

	public Phone(long id, String fio, int age, String home) {
		super();
		this.id = id;
		this.fio = fio;
		this.age = age;
		this.home = home;
	}

	@Override
	public String toString() {
		return "Phone(id=" + id + ") [ФИО=" + fio + ", Возраст=" + age + ", Адрес=" + home + "]</br>";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int num) {
		this.age = age;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

}
