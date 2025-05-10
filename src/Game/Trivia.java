package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
public class Trivia {
    private List<Question> questions;
    private User user;
    private int score;
    public Trivia(List<Question> questions, User user, int score) {
        this.questions = questions;
        this.user = user;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public String toString() {
        return "Trivia{" +
                "questions=" + questions +
                ", score=" + score +
                '}';
    }

    public void damageUser() {
        int newHealth = user.getHealth() - 25;
        user.setHealth(Math.max(0, newHealth));
    }

    public void changeCategoryQuestions(Trivia trivia, List<Question> allQuestions, String desiredCategory) {
        List<String> powers = new ArrayList<>(Arrays.asList(trivia.getUser().getPowers()));
        if (powers.contains("Change")) {
            powers.remove("Change");
            trivia.getUser().setPowers(powers.toArray(new String[0]));

            List<Question> filtered = new ArrayList<>();
            for (Question q : allQuestions) {
                if (q.getCategory().equalsIgnoreCase(desiredCategory)) {
                    filtered.add(q);
                }
            }

            if (filtered.isEmpty()) {
                System.out.println("No questions found for category: " + desiredCategory);
            } else {
                trivia.setQuestions(filtered);
                System.out.println("Category changed to: " + desiredCategory);
            }
        } else {
            System.out.println("User " + trivia.getUser().getName() + " has no Change power.");
        }
    }

    public void healingHealth(Trivia trivia) {
        List<String> powers = new ArrayList<>(Arrays.asList(trivia.getUser().getPowers()));
        if (powers.contains("Heal")) {
            powers.remove("Heal");
            trivia.getUser().setPowers(powers.toArray(new String[0]));

            int newHealth = Math.min(100, trivia.getUser().getHealth() + 30);
            trivia.getUser().setHealth(newHealth);
            System.out.println("User healed. Current health: " + newHealth);
        } else {
            System.out.println("No healing power available.");
        }
    }

    public void skipQuestion(Trivia trivia) {
        List<String> powers = new ArrayList<>(Arrays.asList(trivia.getUser().getPowers()));
        if (powers.contains("Skip")) {
            powers.remove("Skip");
            trivia.getUser().setPowers(powers.toArray(new String[0]));
            System.out.println("Question skipped.");
        } else {
            System.out.println("No skip power available.");
        }
    }
    public int useLotteryTicket() {
        Random rand = new Random();
        int result = rand.nextBoolean() ? 100 : 0;
        user.setMoney(user.getMoney() + result);
        System.out.println("The fates whisper... you have gained " + result + " gold coins!");
        return result;
    }

    public void buyAbilities(String abilityName) {
        int price = 30;
        List<String> available = Arrays.asList("Heal", "Change", "Skip", "Lottery", "Hint");

        if (abilityName == null || !available.contains(abilityName)) {
            System.out.println("Ability not recognized: " + abilityName);
            return;
        }

        if (this.user.getMoney() < price) {
            System.out.println("Not enough money to buy: " + abilityName);
            return;
        }

        String[] currentPowersArray = this.user.getPowers();
        if (currentPowersArray == null) {
            currentPowersArray = new String[0];
        }

        List<String> powers = new ArrayList<>(Arrays.asList(currentPowersArray));

        powers.add(abilityName);

        this.user.setPowers(powers.toArray(new String[0]));

        this.user.setMoney(this.user.getMoney() - price);

        System.out.println("Bought ability: " + abilityName + ". Remaining gold: " + this.user.getMoney());
    }


    public void answerRight() {
        this.score += 1;
        System.out.println("Correct! Score is now: " + this.score);
    }
    public void rewardMoney() {
        int reward = 30;
        user.setMoney(user.getMoney() + reward);
        System.out.println("You've been rewarded with gold " + reward + ". Current gold: " + user.getMoney());
    }

}
