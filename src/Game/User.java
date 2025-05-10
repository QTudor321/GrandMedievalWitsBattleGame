package Game;

import java.util.Arrays;

public class User {
    private String name;
    private int health;
    private String[] powers = new String[4];
    private int money;
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public User(String name, int health, String[] powers, int money) {
        this.name = name;
        this.health = health;
        this.powers = powers;
        this.money = money;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPowers() {
        return powers;
    }

    public void setPowers(String[] powers) {
        this.powers = powers;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", powers=" + Arrays.toString(powers) +
                ", money=" + money +
                '}';
    }
}
