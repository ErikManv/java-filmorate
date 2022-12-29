package ru.yandex.practicum.filmorate.storage.user;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Integer userId = 0;
    private void countId() {
        userId++;
    }

    private Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user){
        if(user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        countId();
        user.setId(userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean containsUser(Integer userId) {
        return users.containsKey(userId);
    }

    @Override
    public User updateUser(User user){
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(Integer userId){
        return users.get(userId);
    }

    @Override
    public List<User> getFriendsList(Integer userId){
        List<Integer> frinedsId = new ArrayList<>(this.getUser(userId).getFriendsList());
        List<User> friends = new ArrayList<>();
        for(Integer friendId: frinedsId){
            friends.add(users.get(friendId));
        }
        return friends;
    }
    @Override
    public void putFriend(Integer userId, Integer newFriendId) {
        users.get(userId).getFriendsList().add(newFriendId);
        users.get(newFriendId).getFriendsList().add(userId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer targetFriendId) {
        users.get(userId).getFriendsList().remove(targetFriendId);
    }

    @Override
    public List<User> commonFriends(Integer fUserId, Integer sUserId) {
        Set<Integer> firstUserFriends = users.get(fUserId).getFriendsList();
        Set<Integer> secondUserFriends = users.get(sUserId).getFriendsList();
        List<User> commonFriends = new ArrayList<>();
        for(Integer friendId: firstUserFriends){
            if(secondUserFriends.contains(friendId)) {
                commonFriends.add(users.get(friendId));
            }
        }
        return commonFriends;
    }
}
