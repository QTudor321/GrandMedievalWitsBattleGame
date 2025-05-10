package Network;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.Scanner;

public class Client {
    public static boolean isInRound = false;
    public static void main(String[] args) {
        System.out.println("Introduceti adresa ip: ");
        Scanner sc = new Scanner(System.in);
        String ipaddr = sc.nextLine();
        try {
            InetAddress ip = InetAddress.getByName(ipaddr);
            DatagramPacket dp = new DatagramPacket(new byte[120], 120, ip, 55000);
            DatagramSocket ds = new DatagramSocket();
            System.out.println("Conectarea cu adresa ip: " + ip.getHostName());
            System.out.println("Introduceti numele utilizatorului: ");
            String nume = sc.nextLine();
            dp.setData(nume.getBytes());
            ds.send(dp);
            DatagramPacket responsepacket1 = new DatagramPacket(new byte[1024], 1024, ip, 55000);
            ds.receive(responsepacket1);
            String responseserver = new String(responsepacket1.getData()).trim();
            System.out.println(responseserver);
            while (true) {
                DatagramPacket menu = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                ds.receive(menu);
                String menustring = new String(menu.getData()).trim();
                System.out.println(menustring);
                String choice = sc.nextLine();
                byte[] choiceBytes = choice.getBytes();
                DatagramPacket choicep = new DatagramPacket(choiceBytes, choiceBytes.length, ip, 55000);
                ds.send(choicep);
                if (choice.equals("1")) {
                    byte[] categorychoiceb = new byte[120];
                    DatagramPacket categorychoice = new DatagramPacket(categorychoiceb, categorychoiceb.length);
                    ds.receive(categorychoice);
                    System.out.println("Jocul incepe!");
                    isInRound = true;
                    String categorystringchoise = new String(categorychoice.getData()).trim();
                    System.out.println(categorystringchoise);
                    while (isInRound) {
                        DatagramPacket question1packet = new DatagramPacket(new byte[300], 300, ip, 55000);
                        ds.receive(question1packet);
                        String question1string = new String(question1packet.getData()).trim();
                        if (question1string.contains("END_OF_ROUND")) {
                            isInRound = false;
                            System.out.println(question1string);

                            DatagramPacket possibleFinalMessagePacket = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                            ds.receive(possibleFinalMessagePacket);
                            String possibleFinalMessage = new String(possibleFinalMessagePacket.getData()).trim();

                            if (possibleFinalMessage.contains("---GAME OVER---")) {
                                System.out.println(possibleFinalMessage);
                                break;
                            }else if(possibleFinalMessage.contains("NEXT_ROUND")) {
                                System.out.println("Inceperea unei noi runde...");
                                continue;
                            } else{
                                System.out.println("Mesaj neasteptat de la server "+possibleFinalMessage);
                                break;
                            }
                        }
                        System.out.println("Intrebarea: " + question1string);
                        System.out.println("Do you wish to answer the question or use a power?");
                        String answerorpower = sc.nextLine();
                        DatagramPacket packetanswerpower = new DatagramPacket(new byte[120], 120, ip, 55000);
                        packetanswerpower.setData(answerorpower.getBytes());
                        ds.send(packetanswerpower);
                        if (answerorpower.contains("Answer")) {
                            String answer1 = sc.nextLine();
                            DatagramPacket answer1packet = new DatagramPacket(new byte[300], 300, ip, 55000);
                            answer1packet.setData(answer1.getBytes());
                            ds.send(answer1packet);
                            DatagramPacket feedback1packet = new DatagramPacket(new byte[300], 300, ip, 55000);
                            ds.receive(feedback1packet);
                            String feedback1string = new String(feedback1packet.getData()).trim();
                            System.out.println(feedback1string);
                        } else if (answerorpower.contains("Power")) {
                            DatagramPacket powermenu = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                            ds.receive(powermenu);
                            String powermenustring = new String(powermenu.getData()).trim();
                            System.out.println(powermenustring);
                            DatagramPacket choosepower = new DatagramPacket(new byte[120], 120, ip, 55000);
                            String choosepowertouse = sc.nextLine();
                            choosepower.setData(choosepowertouse.getBytes());
                            ds.send(choosepower);
                            if (choosepowertouse.contains("Healing")) {
                                DatagramPacket healbefore = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                                ds.receive(healbefore);
                                String healbeforestring = new String(healbefore.getData()).trim();
                                System.out.println(healbeforestring);
                                DatagramPacket healafter = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                                ds.receive(healafter);
                                String healafterstring = new String(healafter.getData()).trim();
                                System.out.println(healafterstring);
                            } else if (choosepowertouse.contains("Change")) {
                                DatagramPacket promptChangepacket = new DatagramPacket(new byte[1024],1024,ip,55000);
                                ds.receive(promptChangepacket);
                                String promptchangestring = new String(promptChangepacket.getData()).trim();
                                System.out.println(promptchangestring);
                                DatagramPacket choosechangeCat = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                                String chooseCat = sc.nextLine();
                                choosechangeCat.setData(chooseCat.getBytes());
                                ds.send(choosechangeCat);
                                DatagramPacket serverresponseChange = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                                ds.receive(serverresponseChange);
                                String serverresponsechangestring = new String(serverresponseChange.getData()).trim();
                                System.out.println(serverresponsechangestring);
                            } else if (choosepowertouse.contains("Skip")) {
                                DatagramPacket skipped = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                                ds.receive(skipped);
                                String skippedstring = new String(skipped.getData()).trim();
                                System.out.println(skippedstring);
                            } else if(choosepowertouse.contains("Lottery")){
                                DatagramPacket receiveCurrentGold = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                                ds.receive(receiveCurrentGold);
                                String receiveCurrentstring = new String(receiveCurrentGold.getData()).trim();
                                System.out.println(receiveCurrentstring);
                                DatagramPacket lotteryStatus = new DatagramPacket(new byte[1024],1024,ip,55000);
                                ds.receive(lotteryStatus);
                                String lotterystatusstring = new String(lotteryStatus.getData()).trim();
                                System.out.println(lotterystatusstring);
                            } else if(choosepowertouse.contains("Hint")){
                                DatagramPacket receiveHint = new DatagramPacket(new byte[1024],1024,ip,55000);
                                ds.receive(receiveHint);
                                String receivehintstring = new String(receiveHint.getData()).trim();
                                System.out.println(receivehintstring);
                            }
                            DatagramPacket backQuestion = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                            ds.receive(backQuestion);
                            String backquestionstring = new String(backQuestion.getData()).trim();
                            System.out.println(backquestionstring);
                        }
                    }
                } else if(choice.contains("2")){
                    DatagramPacket receiveShopPacket = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                    ds.receive(receiveShopPacket);
                    String receiveshopstring = new String(receiveShopPacket.getData()).trim();
                    System.out.println("--Item Shop--"+receiveshopstring);
                    System.out.println("Introduceti numele puterii: ");
                    DatagramPacket choosePower = new DatagramPacket(new byte[120], 120, ip, 55000);
                    String choosePowertouseString = sc.nextLine();
                    choosePower.setData(choosePowertouseString.getBytes());
                    ds.send(choosePower);
                    DatagramPacket currentPowersUser = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                    ds.receive(currentPowersUser);
                    String currentPowersUserstring = new String(currentPowersUser.getData()).trim();
                    System.out.println(currentPowersUserstring);
                } else if (choice.contains("3")) {
                    DatagramPacket infouser = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                    ds.receive(infouser);
                    String infouserstring = new String(infouser.getData()).trim();
                    System.out.println(infouserstring);
                } else if (choice.contains("4")) {
                    DatagramPacket exitp = new DatagramPacket(new byte[1024], 1024, ip, 55000);
                    ds.receive(exitp);
                    String exitpstring = new String(exitp.getData()).trim();
                    System.out.println(exitpstring);
                    break;
                }
            }
        } catch (SocketException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } catch (UnknownHostException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
}