package com.example.beanstalk.user.domain.enums;

public enum AgeGroup {
    ONE(10),
    TWO(20),
    THREE(30),
    FOUR(40),
    FIVE(50),
    SIX(60),
    SEVEN(70),
    DEFAULT(0);

    private int ageGroup;

    AgeGroup(int age)
    {
        this.ageGroup =age;
    }

    public int getAgeGroup()
    {
        return ageGroup;
    }
}
