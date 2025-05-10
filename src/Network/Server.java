package Network;
import java.io.FileNotFoundException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

import Game.*;

public class Server{
    private static int index = 0;
    private static boolean isInRound = false;
    public static List<Question> readQuestionsFile(String file_name){
        List<Question> questionlist = new ArrayList<>();
        try{
            FileReader f = new FileReader(file_name);
            BufferedReader r = new BufferedReader(f);
            String line;
            while((line = r.readLine())!=null){
                String[] file_parts = line.split(",");
                String category = file_parts[0].trim();
                String question = file_parts[1].trim();
                String answer = file_parts[2].trim();
                String hint = file_parts[3].trim();
                questionlist.add(new Question(category,question,answer,hint));
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return questionlist;
    }
    public static void chooseQuestions(Trivia trivia, List<Question> question_list, List<String> used_categories){
        List<String> allCategories =new ArrayList<>();
        for(Question q : question_list)
            if(!used_categories.contains(q.getCategory()))
                allCategories.add(q.getCategory());
        allCategories.removeAll(used_categories);
        Random rand = new Random();
        String chosenCategory = allCategories.get(rand.nextInt(allCategories.size()));
        used_categories.add(chosenCategory);
        List<Question> filtered_questions = new ArrayList<>();
        for(Question q : question_list)
            if(q.getCategory().equals(chosenCategory))
                filtered_questions.add(q);
        trivia.setQuestions(filtered_questions);
        System.out.println("Questions chosen: " + chosenCategory);
    }
    public static void main(String[] args){
        System.out.println("Pornire server...");
        User u = new User(null,100, null,15);
        List<Question> qlist;
        qlist = readQuestionsFile("questions.txt");
        List<String> used_categories = new ArrayList<>();
        Trivia t = new Trivia(qlist,u,0);
        try{
            DatagramPacket username = new DatagramPacket(new byte[120],120);
            DatagramSocket ds = new DatagramSocket(55000);
            ds.receive(username);
            System.out.println("Conectarea la server cu adresa IP: "+username.getAddress());
            String request = new String(username.getData()).trim();
            System.out.println("Raspuns primit de la client: "+request+" cu ip: "+username.getAddress());
            u.setName(request);
            t.setUser(u);
            String welcomeresponse = "Welcome "+u.getName()+"!";
            DatagramPacket welcome = new DatagramPacket(new byte[1024],1024,username.getAddress(),username.getPort());
            welcome.setData(welcomeresponse.getBytes());
            ds.send(welcome);
            while(true){
                DatagramPacket sendmenu = new DatagramPacket(new byte[120],120,username.getAddress(),username.getPort());
                String menu = "Grand Medieval Wits Battle Game\nMenu:\n1.Play\n2.ItemShop\n3.Information\n4.Exit";
                sendmenu.setData(menu.getBytes());
                ds.send(sendmenu);
                DatagramPacket choicep = new DatagramPacket(new byte[120],120,username.getAddress(),username.getPort());
                ds.receive(choicep);
                String choice = new String(choicep.getData());
                System.out.println("Raspuns de la client: "+choice+" cu ip: "+choicep.getAddress());
                if(choice.contains("1")){
                    isInRound=true;
                    chooseQuestions(t,qlist,used_categories);
                    if (!used_categories.isEmpty()) {
                        DatagramPacket categoryresponse = new DatagramPacket(new byte[120], 120, username.getAddress(), username.getPort());
                        String categorystring = used_categories.get(index);
                        categoryresponse.setData(categorystring.getBytes());
                        ds.send(categoryresponse);
                        index++;
                    }
                    for(Question q : t.getQuestions())
                    {
                        if(isInRound) {
                            DatagramPacket questionpacket = new DatagramPacket(new byte[300], 300, username.getAddress(), username.getPort());
                            questionpacket.setData(q.getQuestion().getBytes());
                            ds.send(questionpacket);
                            String question1 = new String(questionpacket.getData()).trim();
                            System.out.println(question1);
                            String responseorpower;
                            DatagramPacket responseorpowerpacket = new DatagramPacket(new byte[120], 120, username.getAddress(), username.getPort());
                            ds.receive(responseorpowerpacket);
                            responseorpower = new String(responseorpowerpacket.getData()).trim();
                            System.out.println("User " + u.getName() + " chose " + responseorpower);
                            if (responseorpower.contains("Answer")) {
                                String response1;
                                String feedback1;
                                DatagramPacket answer1packet = new DatagramPacket(new byte[300], 300, username.getAddress(), username.getPort());
                                ds.receive(answer1packet);
                                response1 = new String(answer1packet.getData()).trim();
                                System.out.println("Raspuns de la client: " + response1);
                                if (response1.equalsIgnoreCase(q.getAnswer())) {
                                    t.answerRight();
                                    feedback1 = "CORRECT!";
                                } else {
                                    t.damageUser();
                                    feedback1 = "WRONG!";
                                }
                                DatagramPacket feedback1packet = new DatagramPacket(new byte[300], 300, username.getAddress(), username.getPort());
                                feedback1packet.setData(feedback1.getBytes());
                                String feedback1string = new String(feedback1packet.getData()).trim();
                                System.out.println("Feedback: " + feedback1string);
                                ds.send(feedback1packet);
                            } else if (responseorpower.contains("Power")) {
                                DatagramPacket powermenu1 = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                String powermeenustring = "Puterile tale valabile: " + Arrays.toString(u.getPowers());
                                powermenu1.setData(powermeenustring.getBytes());
                                ds.send(powermenu1);
                                DatagramPacket receivePowerChoise = new DatagramPacket(new byte[120], 120, username.getAddress(), username.getPort());
                                ds.receive(receivePowerChoise);
                                String receiveStringPower = new String(receivePowerChoise.getData()).trim();
                                System.out.println("Puterea de la client: " + receiveStringPower);
                                if (receiveStringPower.contains("Healing")) {
                                    DatagramPacket healbefore = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                    String healbefores = u.getName() + " avea inainte " + u.getHealth() + " viata.";
                                    healbefore.setData(healbefores.getBytes());
                                    ds.send(healbefore);
                                    t.healingHealth(t);
                                    DatagramPacket healafter = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                    String healnow = u.getName() + " are acum " + u.getHealth() + " viata.";
                                    healafter.setData(healnow.getBytes());
                                    ds.send(healafter);
                                } else {
                                    if (receiveStringPower.contains("Change")) {
                                        DatagramPacket promptChange = new DatagramPacket(new byte[1024],1024, username.getAddress(), username.getPort());
                                        String promptChangeString = "Introduceti categoria dorita: ";
                                        promptChange.setData(promptChangeString.getBytes());
                                        ds.send(promptChange);
                                        DatagramPacket chooseChangeCategory = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                        ds.receive(chooseChangeCategory);
                                        String chooseChangeCategorystring = new String(chooseChangeCategory.getData()).trim();
                                        System.out.println("Categoria aleasa pentru schimbare de la client: " + chooseChangeCategorystring);
                                        t.changeCategoryQuestions(t, qlist, chooseChangeCategorystring);
                                        List<Question> filteredQuestionsChange = t.getQuestions();
                                        if(!filteredQuestionsChange.isEmpty()) {
                                            DatagramPacket sendNewQuestion = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                            String sendNewQuestionstring = "Your new category is: " + q.getCategory();
                                            sendNewQuestion.setData(sendNewQuestionstring.getBytes());
                                            ds.send(sendNewQuestion);
                                        } else {
                                            DatagramPacket failurePacket = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                            String failureMessage = "Failed to change category. No questions found for: " + chooseChangeCategorystring;
                                            failurePacket.setData(failureMessage.getBytes());
                                            ds.send(failurePacket);
                                        }
                                    } else if (receiveStringPower.contains("Skip")) {
                                        t.skipQuestion(t);
                                        DatagramPacket skipped = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                        String skippedstring = "Question is skipped!";
                                        skipped.setData(skippedstring.getBytes());
                                        ds.send(skipped);
                                    } else if(receiveStringPower.contains("Lottery")){
                                        DatagramPacket currentGold = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                        String currentGoldstring = "You currently have "+u.getMoney()+" gold coins!";
                                        currentGold.setData(currentGoldstring.getBytes());
                                        ds.send(currentGold);
                                        int gold = t.useLotteryTicket();
                                        if(gold > 0)
                                        {
                                            DatagramPacket lotterypacket = new DatagramPacket(new byte[1024],1024,username.getAddress(), username.getPort());
                                            String winner = "You won 100 gold coins and have " + u.getMoney() + " gold coins in total!";
                                            lotterypacket.setData(winner.getBytes());
                                            ds.send(lotterypacket);
                                            System.out.println("User with ip "+lotterypacket.getAddress()+" won 100 from the lottery and has "+u.getMoney()+" gold coins!");
                                        }
                                        else {
                                            DatagramPacket lotteryfailure = new DatagramPacket(new byte[1024],1024,username.getAddress(), username.getPort());
                                            String loser = "You used a lottery ticket and lost, you have "+u.getMoney()+" gold coins in total.";
                                            lotteryfailure.setData(loser.getBytes());
                                            ds.send(lotteryfailure);
                                            System.out.println("User with ip "+lotteryfailure.getAddress()+" lost from lottery and has "+u.getMoney()+" gold coins!");
                                        }
                                    } else if(receiveStringPower.contains("Hint")){
                                        DatagramPacket usesHint = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                        if(Arrays.toString(u.getPowers()).contains("Hint"))
                                        {
                                            String usesHintString = "Hint: "+q.getHint()+".";
                                            usesHint.setData(usesHintString.getBytes());
                                            ds.send(usesHint);
                                            System.out.println(u.getName()+" used hint "+usesHintString);
                                        }
                                        else{
                                            String noHintString = "You don't have a hint to use!";
                                            usesHint.setData(noHintString.getBytes());
                                            ds.send(usesHint);
                                            System.out.println(u.getName()+" doesn't have hints!");
                                        }
                                    }
                                }
                                DatagramPacket backtoQuestions = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                                String backQuestion = "Back to the questions...";
                                backtoQuestions.setData(backQuestion.getBytes());
                                ds.send(backtoQuestions);
                            }
                        }
                    }
                    DatagramPacket roundEnd = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                    String roundEndMsg = "END_OF_ROUND"+"\nUsers score is: "+t.getScore();
                    System.out.println(roundEndMsg);
                    if (t.getScore() > 7) {
                        t.rewardMoney();
                        roundEndMsg += "\nCongratulations! You have been rewarded with 30 coins for your performance!";
                        System.out.println("Rewarded: " + u.getName() + " score: " + t.getScore()+" money: "+u.getMoney());
                    }
                    else
                        roundEndMsg += "\nIncrease the score for a reward!";
                    roundEnd.setData(roundEndMsg.getBytes());
                    ds.send(roundEnd);
                    isInRound = false;
                    List<String> allCategories = new ArrayList<>();
                    for (Question q : qlist) {
                        if (!allCategories.contains(q.getCategory())) {
                            allCategories.add(q.getCategory());
                        }
                    }
                    if (used_categories.containsAll(allCategories)) {
                        DatagramPacket finalMessagePacket = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                        String finalMessage = "---GAME OVER---\nCongratulations " + u.getName() + "!\nYou have completed all categories.\n" +
                                "Final Score: " + t.getScore() + "\nCoins: " + u.getMoney() + "\nThank you for playing!";
                        finalMessagePacket.setData(finalMessage.getBytes());
                        ds.send(finalMessagePacket);
                        System.out.println("Final message sent to client: " + username.getAddress());
                        break;
                    }
                    else
                    {
                        DatagramPacket MessagePacket = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                        String anotherMessage = "NEXT_ROUND";
                        MessagePacket.setData(anotherMessage.getBytes());
                        ds.send(MessagePacket);
                        System.out.println("Round message sent to client: " + username.getAddress());
                    }
                }
                else if(choice.contains("2")){
                    DatagramPacket shoppacket = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                    String shopmenu = "\nAvailable powers: \n1.Heal\n2.Change\n3.Skip\n4.Lottery\n5.Hint";
                    shoppacket.setData(shopmenu.getBytes());
                    ds.send(shoppacket);
                    System.out.println("Item shop sent to client: "+shoppacket.getAddress());
                    DatagramPacket choiceItem = new DatagramPacket(new byte[120],120,username.getAddress(),username.getPort());
                    ds.receive(choiceItem);
                    String choiceitemstring = new String(choiceItem.getData()).trim();
                    System.out.println("User choice item is: "+choiceitemstring);
                    t.buyAbilities(choiceitemstring);
                    System.out.println("User bought: "+choiceitemstring);
                    DatagramPacket powersPacketUser = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                    String powersPacketUserstring = "User "+u.getName()+" has the following powers: "+Arrays.toString(u.getPowers());
                    powersPacketUser.setData(powersPacketUserstring.getBytes());
                    ds.send(powersPacketUser);
                }
                else if(choice.contains("3")){
                    DatagramPacket infouser = new DatagramPacket(new byte[1024], 1024, username.getAddress(), username.getPort());
                    String infos = "User: " + u.getName() + "\nHealth: " + u.getHealth() + "\nPowers: " + Arrays.toString(u.getPowers()) + "\nCoins: " + u.getMoney()+"\nScore:  "+t.getScore();
                    infouser.setData(infos.getBytes());
                    ds.send(infouser);
                }
                else if(choice.contains("4")){
                    DatagramPacket exitpacket = new DatagramPacket(new byte[1024],1024,username.getAddress(),username.getPort());
                    String exitmsg = "User "+u.getName()+" has exited.";
                    exitpacket.setData(exitmsg.getBytes());
                    ds.send(exitpacket);
                    System.out.println("Exit packet: "+exitmsg);
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}