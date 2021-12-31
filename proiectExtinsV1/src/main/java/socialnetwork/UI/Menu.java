package socialnetwork.UI;

import socialnetwork.config.DBconfigs;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.database.DatabaseRepository;
import socialnetwork.repository.database.MessageDatabaseRepository;
import socialnetwork.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private UserService service;

    public void menu() {
        //menuInitializare();
        menuService();
    }

    public void menuService() {
        while (true) {
            int serviceChoice = 999;
            try {
                serviceChoice = serviceChoice();
            } catch (Exception e) {
                System.out.println(e);
            }
            if (serviceChoice == 1) {
                try {
                    Long id = service.getMaxId() + 1L;
                    User user = readUser();
                    user.setId(id);
                    service.addUser(user);
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else if (serviceChoice == 2) {
                service.removeUser(readID());
            } else if (serviceChoice == 0)
                break;
            else if (serviceChoice == 3) {
                try {
                    Long id1 = readID();
                    Long id2 = readID();
                    service.addFriend(id1, id2, 0);
                } catch (Exception e) {
                    System.out.println(e);
                }

            } else if (serviceChoice == 4) {
                Long id1 = readID();
                Long id2 = readID();
                service.removeFriend(id1, id2);
            } else if (serviceChoice == 5) {
                Long id1 = readID();
                ArrayList<Long> to = readArrayList();
                String message = readMessage();
                Integer reply = readReply();
                service.addMessage(id1, to, message, reply);
            } else if (serviceChoice == 6) {
                ArrayList<Integer> comunity = service.ceaMaiMareComunitate();
                System.out.println(comunity);
                for (int i : comunity) {
                    System.out.println(i);
                }
            } else if (serviceChoice == 7) {
                Long id1 = readID();
                for (User user : service.showFriends(id1)) {
                    List<Long> friends = service.getFriendsAccepted(user);
                    for (Long friendId : friends) {
                        User friend = service.getUserById(friendId);
                        System.out.println(friend.getFirstName() + "|" + friend.getLastName() + "|" +
                                service.getDateOfFriendship(user, friend));
                    }
                }
            } else if (serviceChoice == 8) {
                Long id1 = readID();
                int luna = readLuna();
                for (User friend : service.showFriendsMadeInMonth(id1, luna))
                    System.out.println(friend.getFirstName() + "|" + friend.getLastName() + "|" +
                            service.getDateOfFriendship(service.getUserById(id1), friend));
            } else if (serviceChoice == 9) {
                Long id1 = readID();
                Long id2 = readID();
                for (Message mesaj : service.showMessagesBetweenTwo(id1, id2)) {
                    Long idFrom = mesaj.getFrom();
                    System.out.println(idFrom + ": " + mesaj.getMessage() + "   Date:" + mesaj.getDate());
                }

            } else if (serviceChoice == 10) {
                Long id1 = readID();
                Long id2 = readID();
                service.acceptFriendRequest(id1, id2);
            } else if (serviceChoice == 11) {
                Long id1 = readID();
                Long id2 = readID();
                service.rejectFriendRequest(id1, id2);
            }
        }
    }

    public int serviceChoice() {
        System.out.println("Ce operatie vrei sa faci? 1. Adauga Utilizator \n 2. Sterge Utilizator \n 3. Adauga" +
                " Prietenie \n" + " 4. Sterge Prietenie \n 5. Adaugare mesaj \n 6.Afisare cea mai sociabila" +
                " comunitate \n 7.Afisare toate relatiile de prietenie ale unui utilizator" + " \n 8.Afisati toate" +
                " relatiile de prietenie ale unui utilizator create intr-o anumita luna a anului \n" + "9.Afisati" +
                " cronologic conversatiile a doi utilizatori \n" + "10. Accepta cerere prietenie \n" + "11.Refuza cerere prietenie \n" +
                "0. Pentru iesire");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public User readUser() {
        System.out.println("Care este Prenumele?: ");
        Scanner scanner = new Scanner(System.in);
        String firstName = scanner.nextLine();
        System.out.println("Care este Numele?: ");
        String lastName = scanner.nextLine();
        return new User(firstName, lastName);
    }

    public String readMessage() {
        System.out.println("Care este mesajul?: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public Integer readReply() {
        System.out.println("La ce id raspunde?(0 in caz ca nu raspunde la nimeni): ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public Long readID() {
        System.out.println("Care este ID-ul?: ");
        Scanner scanner = new Scanner(System.in);
        return (long) scanner.nextInt();
    }

    public ArrayList<Long> readArrayList() {
        ArrayList<Long> lista = new ArrayList<>();
        System.out.println("Cate elemente contine lista?: ");
        Scanner scanner = new Scanner(System.in);
        int elemente = scanner.nextInt();
        while (elemente != 0) {
            System.out.println("Introduceti id-ul user-ului: ");
            Scanner scanner2 = new Scanner(System.in);
            lista.add(scanner2.nextLong());
            elemente--;
        }
        return lista;
    }

    public int readLuna() {
        System.out.println("Care este luna?: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
