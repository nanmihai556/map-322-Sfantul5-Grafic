package socialnetwork.service;

import socialnetwork.domain.Account;
import socialnetwork.domain.Graph;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.repository.database.DatabaseRepository;
import socialnetwork.repository.database.LogInDatabaseRepository;
import socialnetwork.repository.database.MessageDatabaseRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserService {
    private DatabaseRepository repo;
    private MessageDatabaseRepository repoMessage;
    private LogInDatabaseRepository repoAccount;

    public UserService(DatabaseRepository repo, MessageDatabaseRepository messageRepo, LogInDatabaseRepository repoAccount) {
        this.repo = repo;
        this.repoMessage = messageRepo;
        this.repoAccount = repoAccount;
    }

    public User addUser(User user) {
        User task = repo.save(user);
        return task;
    }

    public User getUserById(Long id) {
        return repo.findOne(id);
    }

    public Message getMessageById(Long id) {
        return repoMessage.findOne(id);
    }

    public Iterable<User> getAll() {
        return repo.findAll();
    }

    public User removeUser(Long id) {
        User task = repo.delete(id);
        return task;
    }

    public boolean removeFriend(Long ID1, Long ID2) {
        User User1 = repo.findOne(ID1);
        User User2 = repo.findOne(ID2);
        List<Long> f1;
        List<Long> f2;
        if (User1.getFriends() == null)
            return false;
        else
            f1 = User1.getFriends();
        if (User2.getFriends() == null)
            return false;
        else
            f2 = User2.getFriends();

        f1.remove(ID2);
        f2.remove(ID1);
        User1.setFriends(f1);
        User2.setFriends(f2);
        this.update(User1, 2);
        this.update(User2, 2);
        return true;
    }

    public Long getMaxId() {
        Long max = 0L;
        for (User user : repo.findAll()) {
            if (user.getId() > max) {
                max = user.getId();
            }
        }
        return max;
    }

    public Long getDbMaxId() {
        Long max = 0L;
        for (Message message : repoMessage.findAll()) {
            if (message.getId() > max) {
                max = message.getId();
            }
        }
        return max;
    }

    public User update(User user, Integer status) {
        return repo.update(user, status);
    }

    public void addFriend(Long ID1, Long ID2, Integer status) {
        User User1 = repo.findOne(ID1);
        User User2 = repo.findOne(ID2);
        List<Long> f1;
        List<Long> f2;
        if (User1.getFriends() == null)
            f1 = new ArrayList<Long>();
        else
            f1 = User1.getFriends();
        if (User2.getFriends() == null)
            f2 = new ArrayList<Long>();
        else
            f2 = User2.getFriends();
        f1.add(ID2);
        f2.add(ID1);
        User1.setFriends(f1);
        User2.setFriends(f2);
        this.update(User1, status);
        this.update(User2, status);
    }

    public Graph createGraph() {
        Graph graph = new Graph(sizeOfGetAll());
        for (User user : repo.findAll()) {
            if (user.getFriends() != null)
                for (Long id : user.getFriends())
                    graph.addEdge(user.getId().intValue(), id.intValue());
        }
        return graph;
    }

    public boolean isFullyVisited(boolean[] visits) {
        for (boolean i : visits) {
            if (!i)
                return false;
        }
        return true;
    }

    public int sizeOfGetAll() {
        int size = 0;
        for (User value : getAll()) {
            size++;
        }
        return size;
    }

    public int firstZero(boolean[] visits) {
        int index = -1;
        for (boolean i : visits) {
            index += 1;
            if (!i)
                return index;
        }
        return index;
    }

    public int nrComunitati() {
        Graph graph = createGraph();
        boolean[] visited = new boolean[sizeOfGetAll() + 1];
        int comunitati = 0;
        while (!isFullyVisited(visited)) {
            comunitati += 1;
            int firstZero = firstZero(visited);
            if (graph.isNodenULL(firstZero)) {
                visited[firstZero] = true;
            } else {
                visited = graph.DFS(firstZero(visited), visited);
            }
        }
        return comunitati - 1;
    }

    public ArrayList<Integer> getComunity(boolean[] prevVisited, boolean[] visted) {
        ArrayList<Integer> comunity = new ArrayList();
        for (int i = 0; i < sizeOfGetAll(); i++) {
            if (prevVisited[i] != visted[i])
                comunity.add(i);
        }
        return comunity;
    }

    public ArrayList<Integer> ceaMaiMareComunitate() {
        Graph graph = createGraph();
        boolean[] visited = new boolean[sizeOfGetAll() + 1];
        boolean[] prevVisited;
        int maxSize = -10;
        int size;
        ArrayList comunity;
        ArrayList maxComunity = new ArrayList();
        while (!isFullyVisited(visited)) {
            prevVisited = visited.clone();
            int firstZero = firstZero(visited);
            if (graph.isNodenULL(firstZero))
                visited[firstZero] = true;
            else
                visited = graph.DFS(firstZero(visited), visited);
            comunity = getComunity(prevVisited, visited);
            size = comunity.size();
            if (size > maxSize) {
                maxSize = size;
                maxComunity = (ArrayList<Integer>) comunity.clone();
            }
        }
        return maxComunity;
    }

    public List<User> showFriends(Long userID) {
        List<User> users = new ArrayList<>();
        repo.findAll().forEach(users::add);
        Predicate<User> userPredicate = user -> Objects.equals(user.getId(), userID);
        return users.stream().filter(userPredicate).collect(Collectors.toList());
    }

    public List<User> showFriendsMadeInMonth(Long userID, int luna) {
        User user = repo.findOne(userID);
        List<Long> lista_prieteni_long = getFriendsAccepted(user);
        List<User> lista_prieteni_user = lista_prieteni_long.stream().map(id_user -> getUserById(id_user))
                .collect(Collectors.toList());
        Predicate<User> userFriendPredicate = friend -> repo.dateOfFriendship(friend, user).getMonthValue() == luna;
        return lista_prieteni_user.stream().filter(userFriendPredicate).collect(Collectors.toList());
    }

    public List<Message> showMessagesBetweenTwo(Long user1ID, Long user2ID) {
        List<Message> messages = new ArrayList<>();
        repoMessage.findAll().forEach(messages::add);
        Predicate<Message> messagePredicate = message -> Objects.equals(message.getFrom(), user1ID) && message.getTo().contains(user2ID) ||
                Objects.equals(message.getFrom(), user2ID) && message.getTo().contains(user1ID);
        List<Message> necessaryMessages = messages.stream().filter(messagePredicate).collect(Collectors.toList());
        necessaryMessages.sort(Comparator.comparing(Message::getDate));
        return necessaryMessages;
    }

    public LocalDateTime getDateOfFriendship(User user1, User user2) {
        return repo.dateOfFriendship(user1, user2);
    }

    public void acceptFriendRequest(Long userID1, Long userID2) {
        repo.setStatusFriendRequests(repo.findOne(userID1), repo.findOne(userID2), 1);
    }

    public void rejectFriendRequest(Long userID1, Long userID2) {
        repo.setStatusFriendRequests(repo.findOne(userID1), repo.findOne(userID2), 2);
    }

    public ArrayList<Long> getFriendsAccepted(User entity) {
        return repo.getFriendsAccepted(entity);
    }

    public ArrayList<Long> getFriendsRejected(User entity){
        return repo.getFriendsRejected(entity);
    }

    public ArrayList<Long> getFriendsPending(User entity){
        return repo.getFriendsPending(entity);
    }

    public Boolean didF1RequestF2(Long F1,Long F2){
        return repo.didF1RequestF2(F1, F2);
    }

    public Boolean areFriends(Long userID1,Long userID2){
        return repo.areFriends(userID1, userID2);
    }

    public void addMessage(Long userid1, ArrayList<Long> to, String message, Integer reply) {
        LocalDateTime localDt = LocalDateTime.now();
        Message MessageObj = new Message(userid1, to, message, localDt, reply);
        MessageObj.setId(this.getDbMaxId() + 1L);
        repoMessage.save(MessageObj);
    }

    public void addAccount(String AccountUsername, String AccountPassword, Long user_id){
        Account account = new Account(AccountUsername,AccountPassword, user_id);
        account.setId(user_id);
        repoAccount.save(account);
    }

    public Boolean usernameTaken(String AccountUsername){
        return repoAccount.usernameTaken(AccountUsername);
    }

    public Long logIn(String AccountUsername, String AccountPassword){
        return repoAccount.LogIn(AccountUsername, AccountPassword);
    }
    public String getUsername(Long id){
        return repoAccount.getUsername(id);
    }
}
